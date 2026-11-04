# tlias 员工管理系统 —— 前后端接口契约（v1）

后端基地址：`http://localhost:8080`　前端开发服务器：`http://localhost:5173`（用 Vite 代理把 `/api` 转到后端）

## 0. 约定

**统一响应体**（所有接口）

```json
{ "code": 1, "msg": "success", "data": {} }
```

- `code`: `1` 成功，`0` 失败（失败时 `msg` 为中文错误信息，前端用 `ElMessage.error` 弹出）
- 分页响应 `data`：`{ "total": 100, "rows": [ ... ] }`

**登录认证**

- 登录成功后返回 `token`（JWT），前端存 `localStorage`
- 之后**每个请求**都要在请求头里带：`token: <jwt>`
- 未登录或 token 失效：HTTP 401，前端拦截后跳回登录页
- 白名单（不需要 token）：`/login`

**测试账号**：`zhangsong` 之类不存在，用 `zhangsan` / `123456`（另有 19 个账号密码都是 `123456`，用户名见种子数据）

---

## 1. 登录

| 方法 | 路径 | 请求 | 响应 data |
|---|---|---|---|
| POST | `/login` | `{ "username": "zhangsan", "password": "123456" }` | `{ "id":1, "username":"zhangsan", "name":"张无忌", "token":"eyJ..." }` |

失败响应：`{ "code":0, "msg":"用户名或密码错误", "data":null }`

---

## 2. 部门管理 `/depts`

| 方法 | 路径 | 请求 | 响应 data |
|---|---|---|---|
| GET | `/depts` | — | `[ {id, name, createTime, updateTime}, ... ]` |
| GET | `/depts/{id}` | — | `{id, name, createTime, updateTime}` |
| POST | `/depts` | `{ "name": "教研部" }` | `null` |
| PUT | `/depts` | `{ "id": 2, "name": "教研部" }` | `null` |
| DELETE | `/depts/{id}` | — | `null`（部门下有员工时应返回失败） |

---

## 3. 员工管理 `/emps`

| 方法 | 路径 | 请求 | 响应 data |
|---|---|---|---|
| GET | `/emps/page` | query: `name`、`gender`、`begin`、`end`、`page`(默认1)、`pageSize`(默认10) | `{ total, rows:[Emp] }` |
| GET | `/emps/list` | — | `[Emp]`（全部员工，给班级的"班主任"下拉用） |
| GET | `/emps/{id}` | — | `Emp`（含 `exprList` 工作经历数组） |
| POST | `/emps` | `Emp`（含 `exprList`） | `null` |
| PUT | `/emps` | `Emp`（含 `exprList`） | `null` |
| DELETE | `/emps/{ids}` | 逗号分隔，如 `/emps/1,2,3` | `null` |

**Emp 对象**

```json
{
  "id": 1, "username": "zhangsan", "password": "123456",
  "name": "张无忌", "gender": 1, "phone": "13309091111",
  "job": 1, "salary": 12000, "image": "",
  "entryDate": "2020-03-15", "deptId": 1,
  "createTime": "...", "updateTime": "...",
  "deptName": "学工部",
  "exprList": [
    { "begin": "2016-03-01", "end": "2020-02-28", "company": "北京学知教育", "job": "班主任" }
  ]
}
```

- `gender`: 1 男 / 2 女
- `job`: 1 班主任 / 2 讲师 / 3 学生主管 / 4 教研主管 / 5 咨询师
- `entryDate` 格式 `yyyy-MM-dd`
- 新增时不传 `id`；修改时必传 `id`
- `exprList` 里的 `begin`/`end` 也是 `yyyy-MM-dd`

---

## 4. 班级管理 `/clazzs`

| 方法 | 路径 | 请求 | 响应 data |
|---|---|---|---|
| GET | `/clazzs/page` | query: `name`、`begin`、`end`、`page`、`pageSize` | `{ total, rows:[Clazz] }` |
| GET | `/clazzs/list` | — | `[Clazz]`（全部班级，给学员的"班级"下拉用） |
| GET | `/clazzs/{id}` | — | `Clazz` |
| POST | `/clazzs` | `Clazz` | `null` |
| PUT | `/clazzs` | `Clazz` | `null` |
| DELETE | `/clazzs/{id}` | — | `null`（班级下有学员时应返回失败） |

**Clazz 对象**

```json
{
  "id": 1, "name": "JavaEE就业163期", "room": "A101",
  "beginDate": "2026-03-02", "endDate": "2026-09-30",
  "masterId": 1, "masterName": "张无忌", "subject": 1,
  "status": "在读中", "createTime": "...", "updateTime": "..."
}
```

- `subject`: 1 Java / 2 前端 / 3 大数据 / 4 Python / 5 测试 / 6 运维
- `status` 由后端算好：开课时间晚于今天 → `未开班`；结课时间早于今天 → `已结课`；否则 → `在读中`
- 新增/修改时前端只传 `name, room, beginDate, endDate, masterId, subject`

---

## 5. 学员管理 `/students`

| 方法 | 路径 | 请求 | 响应 data |
|---|---|---|---|
| GET | `/students/page` | query: `name`、`degree`、`clazzId`、`page`、`pageSize` | `{ total, rows:[Student] }` |
| GET | `/students/{id}` | — | `Student` |
| POST | `/students` | `Student` | `null` |
| PUT | `/students` | `Student` | `null` |
| DELETE | `/students/{ids}` | 逗号分隔 | `null` |
| PUT | `/students/violation/{id}/{score}` | — | `null`（违纪处理，扣分累加、次数+1） |

**Student 对象**

```json
{
  "id": 1, "name": "林晚", "no": "2026010001", "gender": 2,
  "phone": "13800001001", "idCard": "110101200301011234",
  "isCollege": 1, "address": "北京市海淀区中关村大街1号",
  "degree": 4, "graduationDate": "2026-06-30",
  "clazzId": 1, "clazzName": "JavaEE就业163期",
  "violationCount": 0, "violationScore": 0,
  "createTime": "...", "updateTime": "..."
}
```

- `degree`: 1 初中 / 2 高中 / 3 大专 / 4 本科 / 5 硕士 / 6 博士
- `isCollege`: 1 是 / 0 否

---

## 6. 数据报表 `/report`

| 方法 | 路径 | 响应 data | 图形 |
|---|---|---|---|
| GET | `/report/empJobData` | `{ "jobList":["班主任","讲师",...], "dataList":[3,8,...] }` | 柱状图 |
| GET | `/report/empGenderData` | `[ {"name":"男性员工","value":12}, {"name":"女性员工","value":8} ]` | 饼图 |
| GET | `/report/studentDegreeData` | `[ {"name":"本科","value":8}, ... ]` | 饼图 |
| GET | `/report/studentCountData` | `{ "clazzList":["JavaEE就业163期",...], "dataList":[5,3,...] }` | 柱状图 |

---

## 7. 文件上传

| 方法 | 路径 | 请求 | 响应 data |
|---|---|---|---|
| POST | `/upload` | `multipart/form-data`，字段名 `file`（也兼容 `image`） | `"http://localhost:8080/uploads/xxx.jpg"`（字符串 URL） |

限制：单文件 ≤ 10MB，只允许 `jpg/jpeg/png/gif`。

---

## 8. 前端页面清单（Vue3 + Element Plus + ECharts）

| 路由 | 页面 | 关键内容 |
|---|---|---|
| `/login` | 登录页 | 用户名/密码/登录按钮，渐变或背景图，回车提交 |
| `/` | 布局框架 | 左侧菜单（首页、部门管理、员工管理、班级管理、学员管理、数据报表）+ 顶部栏（系统名/当前用户/退出） |
| `/dashboard` | 首页看板 | 4 个统计卡片（员工总数、部门数、班级数、学员数）+ 2 个 ECharts 图（员工职位分布柱状图、员工性别饼图） |
| `/dept` | 部门管理 | 表格 + 新增/编辑弹窗 + 删除确认 |
| `/emp` | 员工管理 | 搜索栏（姓名/性别/入职日期区间）+ 表格（含头像、部门、职位标签）+ 分页 + 新增/编辑弹窗（含工作经历动态行）+ 批量删除 |
| `/clazz` | 班级管理 | 搜索栏 + 表格（班级名/教室/班主任/学科/开班结课时间/状态标签）+ 分页 + 新增/编辑弹窗 |
| `/student` | 学员管理 | 搜索栏（姓名/学历/班级）+ 表格 + 分页 + 新增/编辑弹窗 + 违纪处理弹窗（输入扣分） |
| `/report` | 数据报表 | 4 个 ECharts 图（职位分布、性别分布、学员学历分布、班级人数分布） |

**技术要求**

- Vue 3（`<script setup>` 组合式 API）+ Vite + Vue Router 4 + Pinia + Element Plus + Axios + ECharts 5
- 统一 axios 实例：`baseURL: '/api'`（Vite 代理到 8080），请求拦截器加 `token` 头，响应拦截器统一处理 `code` 与 401 跳登录
- 中文界面，`zh-cn` 语言包
- 页面风格统一：卡片式布局、圆角、浅色背景（`#f5f7fa`）、主色 `#409eff`
- 表格分页使用 `el-pagination`，搜索栏与表格同卡片
- 所有日期用 `yyyy-MM-dd` 字符串，不要传时间戳

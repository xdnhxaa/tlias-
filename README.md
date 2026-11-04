# tlias 员工管理系统

> 工程训练项目 —— 基于 SpringBoot 3 + Vue 3 的前后端分离管理系统
> 代码、文档、截图、启动脚本全部在本目录内。

## 一、快速开始（三步）

1. 双击 **`启动系统.bat`**（会自动启动 MySQL、导入数据库、启动后端、打开浏览器）
2. 浏览器打开 <http://localhost:8080>
3. 用 **`zhangsan` / `123456`** 登录

停止系统：双击 **`停止系统.bat`**

## 二、目录说明

| 目录 / 文件 | 说明 |
|---|---|
| `tlias-backend\` | 后端源码（SpringBoot 3.4 + MyBatis + JWT） |
| `tlias-frontend\` | 前端源码（Vue 3 + Vite + Element Plus + ECharts） |
| `sql\tlias.sql` | 建库建表脚本 + 测试数据（库名 `tlias`） |
| `docs\API.md` | 前后端接口契约文档 |
| `screenshots\` | 10 张系统运行截图 |
| `dist\tlias-backend.jar` | 已打包好的可执行 jar（前端已内嵌，30 MB） |
| `工程训练提交文档.docx` | 按学校模板填写的提交文档（含 10 张截图） |
| `启动系统.bat` / `停止系统.bat` | 一键启停 |
| `前端开发模式.bat` | 前端热更新开发（改代码即时生效） |

## 三、技术栈

**后端**：SpringBoot 3.4.5 · MyBatis 3.0.3 · MySQL 8.0.44 · JWT (jjwt 0.9.1) · PageHelper 2.1.0 · Spring AOP · Lombok
**前端**：Vue 3.5 · Vite 6 · Vue Router 4 · Pinia · Element Plus 2.9 · Axios · ECharts 5
**架构**：前后端分离，RESTful 接口，JWT 无状态认证，前端构建产物内嵌进 jar 单包部署

## 四、功能模块

1. **登录认证**：账号密码登录 → 签发 JWT（12 小时）→ 请求头携带 → 拦截器校验 → 未登录返回 401
2. **首页看板**：员工/部门/班级/学员四张统计卡片 + 职位分布柱状图 + 性别分布环形图
3. **部门管理**：查询、新增、编辑、删除（部门下有员工时禁止删除）
4. **员工管理**：条件分页查询、新增、编辑、单个/批量删除、多段工作经历动态录入、头像上传
5. **班级管理**：条件分页查询、增删改、班主任与学科指定、在读状态自动计算
6. **学员管理**：条件分页查询、增删改、批量删除、违纪处理（次数+扣分累加）
7. **数据报表**：职位分布、性别分布、学历分布、班级人数四张 ECharts 图表
8. **系统服务**：AOP 操作日志、文件上传、全局异常处理、跨域配置

## 五、环境依赖

| 依赖 | 版本 | 位置 | 说明 |
|---|---|---|---|
| MySQL | 8.0.44 | `D:\shujuku\` | 便携版，账号 `root` / `123456`，库 `tlias` |
| JDK | Temurin 21 | `D:\1\tools\jdk-21` | 运行后端必需 |
| Maven | 3.9.11 | `D:\1\tools\maven` | 仅重新打包时需要 |
| Node.js | 18+ | 系统已装（`D:\node`） | 仅前端开发模式需要 |

> 后端 jar 已打包好，**日常使用不需要 JDK 之外的工具**；要改后端代码才需要 Maven。

## 六、手动运行（不用 bat）

```bash
# 1. 启动 MySQL（若未启动）
D:\shujuku\start-mysql.bat

# 2. 导入数据库（首次）
D:\shujuku\mysql-8.0.44-winx64\bin\mysql.exe -u root -p123456 < sql\tlias.sql

# 3. 启动后端
D:\1\tools\jdk-21\bin\java.exe -jar dist\tlias-backend.jar

# 4. 浏览器访问
http://localhost:8080
```

### 开发模式（改前端代码）

```bash
cd tlias-frontend
npm install            # 首次
npm run dev            # 访问 http://localhost:5173
```

### 重新打包

```bash
# 前端
cd tlias-frontend
npm run build
# 把 dist/* 复制到 tlias-backend/src/main/resources/static/

# 后端（需要 Maven）
set JAVA_HOME=D:\1\tools\jdk-21
D:\1\tools\maven\bin\mvn.cmd -f tlias-backend\pom.xml clean package -DskipTests
```

## 七、数据库表结构

| 表 | 说明 | 初始数据 |
|---|---|---|
| `dept` | 部门表 | 5 条 |
| `emp` | 员工表（用户名唯一，密码默认 123456） | 20 条 |
| `emp_expr` | 员工工作经历表 | 9 条 |
| `clazz` | 班级表（班主任关联 emp） | 6 条 |
| `student` | 学员表（学号唯一，含违纪次数与扣分） | 20 条 |
| `emp_log` | 操作日志表（AOP 自动写入） | 3 条 |

## 八、测试账号

系统内 20 个员工账号密码**均为 `123456`**，例如：

| 用户名 | 姓名 | 职位 |
|---|---|---|
| `zhangsan` | 张无忌 | 班主任 |
| `lisi` | 李慕白 | 讲师 |
| `wangwu` | 王语嫣 | 讲师 |
| `sunqi` | 孙悟空 | 教研主管 |

## 九、常见问题

**Q：双击启动脚本后浏览器打不开？**
看任务栏是否弹出了 `tlias-backend` 的命令行窗口。若窗口一闪而过，多半是 MySQL 没起来——
先双击 `D:\shujuku\start-mysql.bat`，看到 `mysqld is alive` 再重试。

**Q：8080 端口被占用？**
改 `tlias-backend\src\main\resources\application.yml` 里的 `server.port`（需重新打包），
或先关掉占用 8080 的程序（如 Tomcat、其他 SpringBoot 项目）。

**Q：数据库连不上？**
确认 `application.yml` 里的数据库账号密码与你的 MySQL 一致（默认 `root` / `123456`）。

**Q：想清空数据重新开始？**
重新执行 `sql\tlias.sql` 即可（脚本会先 DROP TABLE 再重建）。

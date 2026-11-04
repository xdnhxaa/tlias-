<template>
  <div class="page-container">
    <!-- 搜索栏 -->
    <el-card class="search-card">
      <el-form :model="query" inline>
        <el-form-item label="姓名">
          <el-input v-model="query.name" placeholder="请输入姓名" clearable style="width: 160px" />
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="query.gender" placeholder="请选择" clearable style="width: 120px">
            <el-option label="男" :value="1" />
            <el-option label="女" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="入职日期">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
            style="width: 260px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">查询</el-button>
          <el-button :icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 表格 -->
    <el-card class="table-card">
      <div class="card-header">
        <div class="card-title">员工管理</div>
        <div>
          <el-button type="danger" :icon="Delete" :disabled="selectedIds.length === 0" @click="handleBatchDelete">
            批量删除
          </el-button>
          <el-button type="primary" :icon="Plus" @click="openDialog()">新增员工</el-button>
        </div>
      </div>

      <el-table :data="tableData" border stripe v-loading="loading" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column label="头像" width="80" align="center">
          <template #default="{ row }">
            <el-avatar :size="36" :src="row.image" class="emp-avatar">
              {{ row.name ? row.name.charAt(0) : '?' }}
            </el-avatar>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column label="性别" width="70" align="center">
          <template #default="{ row }">{{ row.gender === 1 ? '男' : '女' }}</template>
        </el-table-column>
        <el-table-column prop="deptName" label="所属部门" width="120" />
        <el-table-column label="职位" width="110" align="center">
          <template #default="{ row }">
            <el-tag :type="jobTagType(row.job)" effect="light">{{ jobLabel(row.job) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="salary" label="薪资" width="100" align="right">
          <template #default="{ row }">{{ row.salary }} 元</template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号" width="130" align="center" />
        <el-table-column prop="entryDate" label="入职日期" width="120" align="center" />
        <el-table-column prop="updateTime" label="最后修改时间" min-width="170" align="center" />
        <el-table-column label="操作" width="150" align="center" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link :icon="Edit" @click="openDialog(row)">编辑</el-button>
            <el-button type="danger" link :icon="Delete" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrap">
        <el-pagination
          v-model:current-page="query.page"
          v-model:page-size="query.pageSize"
          :page-sizes="[10, 20, 50]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="loadData"
          @current-change="loadData"
        />
      </div>
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑员工' : '新增员工'" width="820px" top="6vh">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="form.username" placeholder="登录用户名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="密码" prop="password">
              <el-input v-model="form.password" placeholder="不填默认为 123456" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="form.name" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-radio-group v-model="form.gender">
                <el-radio :value="1">男</el-radio>
                <el-radio :value="2">女</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入 11 位手机号" maxlength="11" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="职位" prop="job">
              <el-select v-model="form.job" placeholder="请选择职位" style="width: 100%">
                <el-option v-for="j in jobOptions" :key="j.value" :label="j.label" :value="j.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="薪资" prop="salary">
              <el-input-number v-model="form.salary" :min="0" :step="500" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属部门" prop="deptId">
              <el-select v-model="form.deptId" placeholder="请选择部门" style="width: 100%">
                <el-option v-for="d in deptOptions" :key="d.id" :label="d.name" :value="d.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="入职日期" prop="entryDate">
              <el-date-picker v-model="form.entryDate" type="date" value-format="YYYY-MM-DD"
                              placeholder="选择日期" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="头像">
              <el-upload
                class="avatar-uploader"
                :show-file-list="false"
                :http-request="handleUpload"
                accept="image/*"
              >
                <el-avatar v-if="form.image" :size="60" :src="form.image" />
                <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
              </el-upload>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 工作经历 -->
        <el-divider content-position="left">工作经历</el-divider>
        <div v-for="(expr, index) in form.exprList" :key="index" class="expr-row">
          <el-date-picker v-model="expr.begin" type="date" value-format="YYYY-MM-DD"
                          placeholder="开始时间" style="width: 150px" />
          <span class="expr-sep">-</span>
          <el-date-picker v-model="expr.end" type="date" value-format="YYYY-MM-DD"
                          placeholder="结束时间" style="width: 150px" />
          <el-input v-model="expr.company" placeholder="公司名称" style="width: 200px" />
          <el-input v-model="expr.job" placeholder="职位" style="width: 150px" />
          <el-button type="danger" link :icon="Delete" @click="removeExpr(index)">删除</el-button>
        </div>
        <el-button type="primary" plain :icon="Plus" @click="addExpr">添加工作经历</el-button>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete, Search, Refresh } from '@element-plus/icons-vue'
import {
  getEmpPage, getEmpById, addEmp, updateEmp, deleteEmps,
  getDeptList, uploadFile
} from '@/api'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const dateRange = ref([])
const selectedIds = ref([])
const deptOptions = ref([])
const dialogVisible = ref(false)
const formRef = ref()

const jobOptions = [
  { value: 1, label: '班主任' },
  { value: 2, label: '讲师' },
  { value: 3, label: '学生主管' },
  { value: 4, label: '教研主管' },
  { value: 5, label: '咨询师' }
]

const query = reactive({ name: '', gender: null, page: 1, pageSize: 10 })

const emptyForm = () => ({
  id: null, username: '', password: '', name: '', gender: 1, phone: '',
  job: null, salary: 8000, image: '', entryDate: '', deptId: null, exprList: []
})
const form = reactive(emptyForm())

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1\d{10}$/, message: '手机号格式不正确', trigger: 'blur' }
  ],
  job: [{ required: true, message: '请选择职位', trigger: 'change' }],
  salary: [{ required: true, message: '请输入薪资', trigger: 'blur' }],
  deptId: [{ required: true, message: '请选择部门', trigger: 'change' }],
  entryDate: [{ required: true, message: '请选择入职日期', trigger: 'change' }]
}

const jobLabel = (job) => (jobOptions.find((j) => j.value === job) || {}).label || '未知'
const jobTagType = (job) => ({ 1: 'success', 2: 'primary', 3: 'warning', 4: 'danger', 5: 'info' }[job] || 'info')

const loadData = async () => {
  loading.value = true
  try {
    const params = { ...query }
    if (dateRange.value && dateRange.value.length === 2) {
      params.begin = dateRange.value[0]
      params.end = dateRange.value[1]
    }
    const res = await getEmpPage(params)
    tableData.value = res.data.rows
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

const loadDepts = async () => {
  const res = await getDeptList()
  deptOptions.value = res.data
}

const handleSearch = () => {
  query.page = 1
  loadData()
}

const handleReset = () => {
  Object.assign(query, { name: '', gender: null, page: 1, pageSize: 10 })
  dateRange.value = []
  loadData()
}

const handleSelectionChange = (rows) => {
  selectedIds.value = rows.map((r) => r.id)
}

const openDialog = async (row) => {
  Object.assign(form, emptyForm())
  if (row) {
    const res = await getEmpById(row.id)
    const data = res.data
    Object.assign(form, {
      id: data.id, username: data.username, password: '', name: data.name,
      gender: data.gender, phone: data.phone, job: data.job, salary: data.salary,
      image: data.image || '', entryDate: data.entryDate, deptId: data.deptId,
      exprList: (data.exprList || []).map((e) => ({
        begin: e.begin, end: e.end, company: e.company, job: e.job
      }))
    })
  }
  dialogVisible.value = true
}

const addExpr = () => form.exprList.push({ begin: '', end: '', company: '', job: '' })
const removeExpr = (index) => form.exprList.splice(index, 1)

const handleUpload = async ({ file }) => {
  const res = await uploadFile(file)
  form.image = res.data
  ElMessage.success('头像上传成功')
}

const handleSubmit = () => {
  formRef.value.validate(async (valid) => {
    if (!valid) return
    const payload = { ...form }
    if (!payload.password) delete payload.password
    if (form.id) {
      await updateEmp(payload)
      ElMessage.success('修改成功')
    } else {
      await addEmp(payload)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    loadData()
  })
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除员工「${row.name}」吗？`, '提示', {
    type: 'warning', confirmButtonText: '确定', cancelButtonText: '取消'
  }).then(async () => {
    await deleteEmps(row.id)
    ElMessage.success('删除成功')
    loadData()
  }).catch(() => {})
}

const handleBatchDelete = () => {
  ElMessageBox.confirm(`确定要删除选中的 ${selectedIds.value.length} 名员工吗？`, '提示', {
    type: 'warning', confirmButtonText: '确定', cancelButtonText: '取消'
  }).then(async () => {
    await deleteEmps(selectedIds.value.join(','))
    ElMessage.success('删除成功')
    loadData()
  }).catch(() => {})
}

onMounted(() => {
  loadDepts()
  loadData()
})
</script>

<style scoped>
.emp-avatar {
  background-color: #409eff;
  color: #fff;
}

.expr-row {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 10px;
}

.expr-sep {
  color: #909399;
}

.avatar-uploader :deep(.el-upload) {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  width: 60px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar-uploader :deep(.el-upload:hover) {
  border-color: #409eff;
}

.avatar-uploader-icon {
  font-size: 22px;
  color: #8c939d;
}
</style>

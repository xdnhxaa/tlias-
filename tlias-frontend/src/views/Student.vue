<template>
  <div class="page-container">
    <el-card class="search-card">
      <el-form :model="query" inline>
        <el-form-item label="姓名">
          <el-input v-model="query.name" placeholder="请输入姓名" clearable style="width: 150px" />
        </el-form-item>
        <el-form-item label="学历">
          <el-select v-model="query.degree" placeholder="请选择" clearable style="width: 130px">
            <el-option v-for="d in degreeOptions" :key="d.value" :label="d.label" :value="d.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="班级">
          <el-select v-model="query.clazzId" placeholder="请选择" clearable style="width: 170px">
            <el-option v-for="c in clazzOptions" :key="c.id" :label="c.name" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">查询</el-button>
          <el-button :icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <div class="card-header">
        <div class="card-title">学员管理</div>
        <div>
          <el-button type="danger" :icon="Delete" :disabled="selectedIds.length === 0" @click="handleBatchDelete">
            批量删除
          </el-button>
          <el-button type="primary" :icon="Plus" @click="openDialog()">新增学员</el-button>
        </div>
      </div>

      <el-table :data="tableData" border stripe v-loading="loading" @selection-change="onSelectionChange">
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="no" label="学号" width="120" align="center" />
        <el-table-column label="性别" width="70" align="center">
          <template #default="{ row }">{{ row.gender === 1 ? '男' : '女' }}</template>
        </el-table-column>
        <el-table-column prop="clazzName" label="班级" min-width="150" />
        <el-table-column prop="phone" label="手机号" width="130" align="center" />
        <el-table-column label="学历" width="90" align="center">
          <template #default="{ row }">
            <el-tag effect="plain">{{ degreeLabel(row.degree) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="graduationDate" label="毕业时间" width="120" align="center" />
        <el-table-column prop="violationCount" label="违纪次数" width="100" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.violationCount > 0" type="danger" effect="light">{{ row.violationCount }}</el-tag>
            <span v-else>0</span>
          </template>
        </el-table-column>
        <el-table-column prop="violationScore" label="违纪扣分" width="100" align="center">
          <template #default="{ row }">
            <span :class="{ 'violation-score': row.violationScore > 0 }">{{ row.violationScore }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="210" align="center" fixed="right">
          <template #default="{ row }">
            <el-button type="warning" link :icon="Warning" @click="openViolation(row)">违纪</el-button>
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

    <!-- 新增/编辑 -->
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑学员' : '新增学员'" width="720px" top="6vh">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="姓名" prop="name"><el-input v-model="form.name" /></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学号" prop="no"><el-input v-model="form.no" /></el-form-item>
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
              <el-input v-model="form.phone" maxlength="11" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="身份证号" prop="idCard">
              <el-input v-model="form.idCard" maxlength="18" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否院校" prop="isCollege">
              <el-radio-group v-model="form.isCollege">
                <el-radio :value="1">是</el-radio>
                <el-radio :value="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学历" prop="degree">
              <el-select v-model="form.degree" style="width: 100%">
                <el-option v-for="d in degreeOptions" :key="d.value" :label="d.label" :value="d.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="班级" prop="clazzId">
              <el-select v-model="form.clazzId" style="width: 100%">
                <el-option v-for="c in clazzOptions" :key="c.id" :label="c.name" :value="c.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="毕业时间" prop="graduationDate">
              <el-date-picker v-model="form.graduationDate" type="date" value-format="YYYY-MM-DD"
                              style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="联系地址" prop="address">
              <el-input v-model="form.address" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 违纪处理 -->
    <el-dialog v-model="violationVisible" title="违纪处理" width="420px">
      <el-form label-width="90px">
        <el-form-item label="学员姓名">
          <span>{{ violationForm.name }}</span>
        </el-form-item>
        <el-form-item label="当前扣分">
          <span>{{ violationForm.violationScore }} 分 / {{ violationForm.violationCount }} 次</span>
        </el-form-item>
        <el-form-item label="本次扣分">
          <el-input-number v-model="violationForm.score" :min="1" :max="100" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="violationVisible = false">取消</el-button>
        <el-button type="primary" @click="submitViolation">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete, Search, Refresh, Warning } from '@element-plus/icons-vue'
import {
  getStudentPage, getStudentById, addStudent, updateStudent, deleteStudents,
  handleViolation, getClazzList
} from '@/api'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const selectedIds = ref([])
const clazzOptions = ref([])
const dialogVisible = ref(false)
const violationVisible = ref(false)
const formRef = ref()

const degreeOptions = [
  { value: 1, label: '初中' }, { value: 2, label: '高中' }, { value: 3, label: '大专' },
  { value: 4, label: '本科' }, { value: 5, label: '硕士' }, { value: 6, label: '博士' }
]

const query = reactive({ name: '', degree: null, clazzId: null, page: 1, pageSize: 10 })
const emptyForm = () => ({
  id: null, name: '', no: '', gender: 1, phone: '', idCard: '', isCollege: 1,
  address: '', degree: null, graduationDate: '', clazzId: null
})
const form = reactive(emptyForm())
const violationForm = reactive({ id: null, name: '', score: 5, violationCount: 0, violationScore: 0 })

const rules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  no: [{ required: true, message: '请输入学号', trigger: 'blur' }],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1\d{10}$/, message: '手机号格式不正确', trigger: 'blur' }
  ],
  idCard: [{ required: true, message: '请输入身份证号', trigger: 'blur' }],
  degree: [{ required: true, message: '请选择学历', trigger: 'change' }],
  clazzId: [{ required: true, message: '请选择班级', trigger: 'change' }],
  graduationDate: [{ required: true, message: '请选择毕业时间', trigger: 'change' }]
}

const degreeLabel = (d) => (degreeOptions.find((i) => i.value === d) || {}).label || '其他'

const loadData = async () => {
  loading.value = true
  try {
    const res = await getStudentPage({ ...query })
    tableData.value = res.data.rows
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

const handleSearch = () => { query.page = 1; loadData() }
const handleReset = () => {
  Object.assign(query, { name: '', degree: null, clazzId: null, page: 1, pageSize: 10 })
  loadData()
}
const onSelectionChange = (rows) => { selectedIds.value = rows.map((r) => r.id) }

const openDialog = async (row) => {
  Object.assign(form, emptyForm())
  if (row) {
    const res = await getStudentById(row.id)
    Object.assign(form, res.data)
  }
  dialogVisible.value = true
}

const handleSubmit = () => {
  formRef.value.validate(async (valid) => {
    if (!valid) return
    if (form.id) {
      await updateStudent(form)
      ElMessage.success('修改成功')
    } else {
      await addStudent(form)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    loadData()
  })
}

const openViolation = (row) => {
  Object.assign(violationForm, {
    id: row.id, name: row.name, score: 5,
    violationCount: row.violationCount, violationScore: row.violationScore
  })
  violationVisible.value = true
}

const submitViolation = async () => {
  await handleViolation(violationForm.id, violationForm.score)
  ElMessage.success('违纪处理完成')
  violationVisible.value = false
  loadData()
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除学员「${row.name}」吗？`, '提示', {
    type: 'warning', confirmButtonText: '确定', cancelButtonText: '取消'
  }).then(async () => {
    await deleteStudents(row.id)
    ElMessage.success('删除成功')
    loadData()
  }).catch(() => {})
}

const handleBatchDelete = () => {
  ElMessageBox.confirm(`确定要删除选中的 ${selectedIds.value.length} 名学员吗？`, '提示', {
    type: 'warning', confirmButtonText: '确定', cancelButtonText: '取消'
  }).then(async () => {
    await deleteStudents(selectedIds.value.join(','))
    ElMessage.success('删除成功')
    loadData()
  }).catch(() => {})
}

onMounted(async () => {
  const res = await getClazzList()
  clazzOptions.value = res.data
  loadData()
})
</script>

<style scoped>
.violation-score {
  color: #f56c6c;
  font-weight: 700;
}
</style>

<template>
  <div class="page-container">
    <el-card class="search-card">
      <el-form :model="query" inline>
        <el-form-item label="班级名称">
          <el-input v-model="query.name" placeholder="请输入班级名称" clearable style="width: 180px" />
        </el-form-item>
        <el-form-item label="结课时间">
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

    <el-card class="table-card">
      <div class="card-header">
        <div class="card-title">班级管理</div>
        <el-button type="primary" :icon="Plus" @click="openDialog()">新增班级</el-button>
      </div>

      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column type="index" label="序号" width="70" align="center" />
        <el-table-column prop="name" label="班级名称" min-width="170" />
        <el-table-column prop="room" label="班级教室" width="110" align="center" />
        <el-table-column prop="masterName" label="班主任" width="110" align="center" />
        <el-table-column label="学科" width="110" align="center">
          <template #default="{ row }">
            <el-tag effect="plain">{{ subjectLabel(row.subject) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="beginDate" label="开课时间" width="120" align="center" />
        <el-table-column prop="endDate" label="结课时间" width="120" align="center" />
        <el-table-column label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="statusType(row.status)">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
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

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑班级' : '新增班级'" width="620px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="班级名称" prop="name">
          <el-input v-model="form.name" placeholder="如：JavaEE就业163期" />
        </el-form-item>
        <el-form-item label="班级教室" prop="room">
          <el-input v-model="form.room" placeholder="如：A101" />
        </el-form-item>
        <el-form-item label="班主任" prop="masterId">
          <el-select v-model="form.masterId" placeholder="请选择班主任" style="width: 100%">
            <el-option v-for="e in empOptions" :key="e.id" :label="e.name" :value="e.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="学科" prop="subject">
          <el-select v-model="form.subject" placeholder="请选择学科" style="width: 100%">
            <el-option v-for="s in subjectOptions" :key="s.value" :label="s.label" :value="s.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="开课时间" prop="beginDate">
          <el-date-picker v-model="form.beginDate" type="date" value-format="YYYY-MM-DD"
                          placeholder="选择开课时间" style="width: 100%" />
        </el-form-item>
        <el-form-item label="结课时间" prop="endDate">
          <el-date-picker v-model="form.endDate" type="date" value-format="YYYY-MM-DD"
                          placeholder="选择结课时间" style="width: 100%" />
        </el-form-item>
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
import { getClazzPage, getClazzById, addClazz, updateClazz, deleteClazz, getEmpList } from '@/api'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const dateRange = ref([])
const empOptions = ref([])
const dialogVisible = ref(false)
const formRef = ref()

const subjectOptions = [
  { value: 1, label: 'Java' }, { value: 2, label: '前端' }, { value: 3, label: '大数据' },
  { value: 4, label: 'Python' }, { value: 5, label: '测试' }, { value: 6, label: '运维' }
]

const query = reactive({ name: '', page: 1, pageSize: 10 })
const emptyForm = () => ({
  id: null, name: '', room: '', masterId: null, subject: null, beginDate: '', endDate: ''
})
const form = reactive(emptyForm())

const rules = {
  name: [{ required: true, message: '请输入班级名称', trigger: 'blur' }],
  room: [{ required: true, message: '请输入班级教室', trigger: 'blur' }],
  masterId: [{ required: true, message: '请选择班主任', trigger: 'change' }],
  subject: [{ required: true, message: '请选择学科', trigger: 'change' }],
  beginDate: [{ required: true, message: '请选择开课时间', trigger: 'change' }],
  endDate: [{ required: true, message: '请选择结课时间', trigger: 'change' }]
}

const subjectLabel = (s) => (subjectOptions.find((i) => i.value === s) || {}).label || '未知'
const statusType = (status) => ({ 未开班: 'warning', 在读中: 'success', 已结课: 'info' }[status] || 'info')

const loadData = async () => {
  loading.value = true
  try {
    const params = { ...query }
    if (dateRange.value && dateRange.value.length === 2) {
      params.begin = dateRange.value[0]
      params.end = dateRange.value[1]
    }
    const res = await getClazzPage(params)
    tableData.value = res.data.rows
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

const handleSearch = () => { query.page = 1; loadData() }
const handleReset = () => {
  Object.assign(query, { name: '', page: 1, pageSize: 10 })
  dateRange.value = []
  loadData()
}

const openDialog = async (row) => {
  Object.assign(form, emptyForm())
  if (row) {
    const res = await getClazzById(row.id)
    Object.assign(form, res.data)
  }
  dialogVisible.value = true
}

const handleSubmit = () => {
  formRef.value.validate(async (valid) => {
    if (!valid) return
    if (form.id) {
      await updateClazz(form)
      ElMessage.success('修改成功')
    } else {
      await addClazz(form)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    loadData()
  })
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除班级「${row.name}」吗？`, '提示', {
    type: 'warning', confirmButtonText: '确定', cancelButtonText: '取消'
  }).then(async () => {
    await deleteClazz(row.id)
    ElMessage.success('删除成功')
    loadData()
  }).catch(() => {})
}

onMounted(async () => {
  const res = await getEmpList()
  empOptions.value = res.data
  loadData()
})
</script>

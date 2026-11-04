<template>
  <div class="page-container">
    <el-card class="table-card">
      <div class="card-header">
        <div class="card-title">部门管理</div>
        <el-button type="primary" :icon="Plus" @click="openDialog()">新增部门</el-button>
      </div>

      <el-table :data="tableData" border stripe v-loading="loading" style="width: 100%">
        <el-table-column type="index" label="序号" width="70" align="center" />
        <el-table-column prop="name" label="部门名称" min-width="200" />
        <el-table-column prop="createTime" label="创建时间" min-width="180" align="center" />
        <el-table-column prop="updateTime" label="最后修改时间" min-width="180" align="center" />
        <el-table-column label="操作" width="180" align="center">
          <template #default="{ row }">
            <el-button type="primary" link :icon="Edit" @click="openDialog(row)">编辑</el-button>
            <el-button type="danger" link :icon="Delete" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑部门' : '新增部门'" width="440px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="部门名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入部门名称" clearable />
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
import { Plus, Edit, Delete } from '@element-plus/icons-vue'
import { getDeptList, addDept, updateDept, deleteDept } from '@/api'

const loading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const formRef = ref()
const form = reactive({ id: null, name: '' })

const rules = {
  name: [
    { required: true, message: '请输入部门名称', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
  ]
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getDeptList()
    tableData.value = res.data
  } finally {
    loading.value = false
  }
}

const openDialog = (row) => {
  if (row) {
    Object.assign(form, { id: row.id, name: row.name })
  } else {
    Object.assign(form, { id: null, name: '' })
  }
  dialogVisible.value = true
}

const handleSubmit = () => {
  formRef.value.validate(async (valid) => {
    if (!valid) return
    if (form.id) {
      await updateDept({ id: form.id, name: form.name })
      ElMessage.success('修改成功')
    } else {
      await addDept({ name: form.name })
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    loadData()
  })
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除部门「${row.name}」吗？`, '提示', {
    type: 'warning', confirmButtonText: '确定', cancelButtonText: '取消'
  }).then(async () => {
    await deleteDept(row.id)
    ElMessage.success('删除成功')
    loadData()
  }).catch(() => {})
}

onMounted(loadData)
</script>

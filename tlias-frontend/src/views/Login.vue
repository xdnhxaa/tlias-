<template>
  <div class="login-page">
    <div class="login-box">
      <div class="login-left">
        <div class="brand">
          <el-icon :size="46"><School /></el-icon>
          <h1>tlias 员工管理系统</h1>
          <p>Employee Management System</p>
        </div>
        <ul class="feature-list">
          <li><el-icon><Select /></el-icon> 部门 / 员工 / 班级 / 学员 一体化管理</li>
          <li><el-icon><Select /></el-icon> JWT 登录认证与拦截器校验</li>
          <li><el-icon><Select /></el-icon> ECharts 数据可视化看板</li>
        </ul>
      </div>

      <div class="login-right">
        <h2>欢迎登录</h2>
        <p class="sub-title">请输入账号密码</p>
        <el-form ref="formRef" :model="form" :rules="rules" size="large" @keyup.enter="handleLogin">
          <el-form-item prop="username">
            <el-input v-model="form.username" placeholder="用户名" clearable>
              <template #prefix><el-icon><User /></el-icon></template>
            </el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="form.password" type="password" placeholder="密码" show-password clearable>
              <template #prefix><el-icon><Lock /></el-icon></template>
            </el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" class="login-btn" :loading="loading" @click="handleLogin">
              {{ loading ? '登录中...' : '登 录' }}
            </el-button>
          </el-form-item>
        </el-form>
        <div class="tips">
          <el-icon><InfoFilled /></el-icon>
          <span>演示账号：zhangsan / 123456</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { loginApi } from '@/api'
import { useUserStore } from '@/store/user'

const router = useRouter()
const userStore = useUserStore()

const formRef = ref()
const loading = ref(false)
const form = reactive({ username: 'zhangsan', password: '123456' })

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = () => {
  formRef.value.validate(async (valid) => {
    if (!valid) return
    loading.value = true
    try {
      const res = await loginApi(form)
      userStore.setLoginInfo(res.data)
      ElMessage.success('登录成功，欢迎 ' + res.data.name)
      router.push('/dashboard')
    } catch (e) {
      // 错误提示已由响应拦截器统一处理
    } finally {
      loading.value = false
    }
  })
}
</script>

<style scoped>
.login-page {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #1f4e79 0%, #409eff 55%, #6fb1fc 100%);
}

.login-box {
  width: 860px;
  height: 470px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.22);
  display: flex;
  overflow: hidden;
}

.login-left {
  width: 46%;
  background: linear-gradient(160deg, #1f2d3d 0%, #2c4a6b 100%);
  color: #fff;
  padding: 46px 34px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.brand h1 {
  font-size: 22px;
  margin: 16px 0 4px;
}

.brand p {
  font-size: 12px;
  color: #8fa6bd;
  letter-spacing: 1px;
  margin: 0;
}

.feature-list {
  list-style: none;
  padding: 0;
  margin: 0;
  font-size: 13px;
  color: #c8d6e5;
  line-height: 2.2;
}

.feature-list .el-icon {
  color: #67c23a;
  vertical-align: -2px;
  margin-right: 6px;
}

.login-right {
  flex: 1;
  padding: 56px 46px;
}

.login-right h2 {
  margin: 0 0 6px;
  font-size: 22px;
  color: #303133;
}

.sub-title {
  color: #909399;
  font-size: 13px;
  margin: 0 0 26px;
}

.login-btn {
  width: 100%;
  letter-spacing: 4px;
}

.tips {
  margin-top: 10px;
  font-size: 12px;
  color: #909399;
  display: flex;
  align-items: center;
  gap: 5px;
}
</style>

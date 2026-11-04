<template>
  <el-container class="layout">
    <!-- 左侧菜单 -->
    <el-aside width="210px" class="aside">
      <div class="logo">
        <el-icon :size="22"><School /></el-icon>
        <span>tlias 管理系统</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        router
        background-color="#1f2d3d"
        text-color="#c0c4cc"
        active-text-color="#ffffff"
      >
        <el-menu-item
          v-for="item in menus"
          :key="item.path"
          :index="item.path"
        >
          <el-icon><component :is="item.icon" /></el-icon>
          <span>{{ item.title }}</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <!-- 顶部栏 -->
      <el-header class="header">
        <div class="breadcrumb">
          <el-icon><Location /></el-icon>
          <span>{{ currentTitle }}</span>
        </div>
        <div class="user-area">
          <el-avatar :size="30" class="avatar">{{ avatarText }}</el-avatar>
          <span class="username">{{ userStore.userInfo.name || userStore.userInfo.username }}</span>
          <el-dropdown @command="handleCommand">
            <el-icon class="more"><MoreFilled /></el-icon>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <!-- 主内容区 -->
      <el-main class="main">
        <router-view v-slot="{ Component }">
          <component :is="Component" />
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'
import { useUserStore } from '@/store/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const menus = [
  { path: '/dashboard', title: '首页看板', icon: 'HomeFilled' },
  { path: '/dept', title: '部门管理', icon: 'OfficeBuilding' },
  { path: '/emp', title: '员工管理', icon: 'User' },
  { path: '/clazz', title: '班级管理', icon: 'School' },
  { path: '/student', title: '学员管理', icon: 'Avatar' },
  { path: '/report', title: '数据报表', icon: 'TrendCharts' }
]

const activeMenu = computed(() => route.path)
const currentTitle = computed(() => route.meta.title || '首页看板')
const avatarText = computed(() => {
  const name = userStore.userInfo.name || userStore.userInfo.username || 'U'
  return name.charAt(0)
})

const handleCommand = (command) => {
  if (command === 'logout') {
    ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      userStore.logout()
      ElMessage.success('已退出登录')
      router.push('/login')
    }).catch(() => {})
  }
}
</script>

<style scoped>
.layout {
  height: 100%;
}

.aside {
  background-color: #1f2d3d;
  overflow-x: hidden;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  color: #fff;
  font-size: 16px;
  font-weight: 600;
  letter-spacing: 1px;
  border-bottom: 1px solid #2c3e50;
}

.aside :deep(.el-menu) {
  border-right: none;
}

.aside :deep(.el-menu-item.is-active) {
  background-color: #409eff !important;
}

.header {
  height: 60px;
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  z-index: 10;
}

.breadcrumb {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 15px;
  color: #606266;
}

.user-area {
  display: flex;
  align-items: center;
  gap: 8px;
}

.avatar {
  background-color: #409eff;
  color: #fff;
}

.username {
  font-size: 14px;
  color: #303133;
}

.more {
  cursor: pointer;
  color: #909399;
}

.main {
  background-color: #f5f7fa;
  padding: 0;
  overflow-y: auto;
}
</style>

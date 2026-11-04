import { createRouter, createWebHashHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: () => import('@/layout/Layout.vue'),
    redirect: '/dashboard',
    children: [
      { path: 'dashboard', name: 'Dashboard', component: () => import('@/views/Dashboard.vue'), meta: { title: '首页看板', icon: 'HomeFilled' } },
      { path: 'dept', name: 'Dept', component: () => import('@/views/Dept.vue'), meta: { title: '部门管理', icon: 'OfficeBuilding' } },
      { path: 'emp', name: 'Emp', component: () => import('@/views/Emp.vue'), meta: { title: '员工管理', icon: 'User' } },
      { path: 'clazz', name: 'Clazz', component: () => import('@/views/Clazz.vue'), meta: { title: '班级管理', icon: 'School' } },
      { path: 'student', name: 'Student', component: () => import('@/views/Student.vue'), meta: { title: '学员管理', icon: 'Avatar' } },
      { path: 'report', name: 'Report', component: () => import('@/views/Report.vue'), meta: { title: '数据报表', icon: 'TrendCharts' } }
    ]
  },
  { path: '/:pathMatch(.*)*', redirect: '/dashboard' }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

// 全局前置守卫：没有 token 一律回登录页
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path === '/login') {
    return next()
  }
  if (!token) {
    return next('/login')
  }
  document.title = (to.meta.title ? to.meta.title + ' - ' : '') + 'tlias 员工管理系统'
  next()
})

export default router

import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'

// 统一的 axios 实例
//  - 开发环境（npm run dev）：走 Vite 代理，/api 前缀会被转发到 8080
//  - 生产构建（打进 jar 由 SpringBoot 直接托管）：前后端同源，用根路径
const baseURL = import.meta.env.DEV ? '/api' : ''

const request = axios.create({
  baseURL,
  timeout: 20000
})

// 请求拦截器：自动携带 token
request.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.token = token
    }
    return config
  },
  (error) => Promise.reject(error)
)

// 响应拦截器：统一处理业务状态码与 401
request.interceptors.response.use(
  (response) => {
    const res = response.data
    if (res.code === 1) {
      return res
    }
    ElMessage.error(res.msg || '操作失败')
    return Promise.reject(new Error(res.msg || '操作失败'))
  },
  (error) => {
    const status = error.response && error.response.status
    if (status === 401) {
      ElMessage.error('登录已过期，请重新登录')
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      router.push('/login')
    } else {
      ElMessage.error(error.message || '网络异常，请稍后重试')
    }
    return Promise.reject(error)
  }
)

export default request

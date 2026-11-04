import request from '@/utils/request'

/* ---------------- 登录 ---------------- */
export const loginApi = (data) => request.post('/login', data)

/* ---------------- 部门 ---------------- */
export const getDeptList = () => request.get('/depts')
export const getDeptById = (id) => request.get(`/depts/${id}`)
export const addDept = (data) => request.post('/depts', data)
export const updateDept = (data) => request.put('/depts', data)
export const deleteDept = (id) => request.delete(`/depts/${id}`)

/* ---------------- 员工 ---------------- */
export const getEmpPage = (params) => request.get('/emps/page', { params })
export const getEmpList = () => request.get('/emps/list')
export const getEmpById = (id) => request.get(`/emps/${id}`)
export const addEmp = (data) => request.post('/emps', data)
export const updateEmp = (data) => request.put('/emps', data)
export const deleteEmps = (ids) => request.delete(`/emps/${ids}`)

/* ---------------- 班级 ---------------- */
export const getClazzPage = (params) => request.get('/clazzs/page', { params })
export const getClazzList = () => request.get('/clazzs/list')
export const getClazzById = (id) => request.get(`/clazzs/${id}`)
export const addClazz = (data) => request.post('/clazzs', data)
export const updateClazz = (data) => request.put('/clazzs', data)
export const deleteClazz = (id) => request.delete(`/clazzs/${id}`)

/* ---------------- 学员 ---------------- */
export const getStudentPage = (params) => request.get('/students/page', { params })
export const getStudentById = (id) => request.get(`/students/${id}`)
export const addStudent = (data) => request.post('/students', data)
export const updateStudent = (data) => request.put('/students', data)
export const deleteStudents = (ids) => request.delete(`/students/${ids}`)
export const handleViolation = (id, score) => request.put(`/students/violation/${id}/${score}`)

/* ---------------- 报表 ---------------- */
export const getOverview = () => request.get('/report/overview')
export const getEmpJobData = () => request.get('/report/empJobData')
export const getEmpGenderData = () => request.get('/report/empGenderData')
export const getStudentDegreeData = () => request.get('/report/studentDegreeData')
export const getStudentCountData = () => request.get('/report/studentCountData')

/* ---------------- 上传 ---------------- */
export const uploadFile = (file) => {
  const formData = new FormData()
  formData.append('file', file)
  return request.post('/upload', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

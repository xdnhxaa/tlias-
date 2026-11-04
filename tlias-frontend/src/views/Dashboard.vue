<template>
  <div class="page-container">
    <!-- 统计卡片 -->
    <el-row :gutter="16">
      <el-col :span="6" v-for="item in cards" :key="item.label">
        <el-card class="stat-card">
          <div class="stat-body">
            <div class="stat-icon" :style="{ background: item.color }">
              <el-icon :size="26"><component :is="item.icon" /></el-icon>
            </div>
            <div class="stat-text">
              <div class="stat-value">{{ item.value }}</div>
              <div class="stat-label">{{ item.label }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表 -->
    <el-row :gutter="16" style="margin-top: 16px">
      <el-col :span="14">
        <el-card>
          <template #header>
            <div class="card-title">各职位员工人数</div>
          </template>
          <div ref="jobChartRef" class="chart"></div>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card>
          <template #header>
            <div class="card-title">员工性别分布</div>
          </template>
          <div ref="genderChartRef" class="chart"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" style="margin-top: 16px">
      <el-col :span="24">
        <el-card>
          <template #header>
            <div class="card-title">系统说明</div>
          </template>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="技术栈">SpringBoot 3 + MyBatis + MySQL 8 + Vue 3 + Element Plus + ECharts</el-descriptions-item>
            <el-descriptions-item label="登录认证">JWT 令牌 + 拦截器校验，令牌有效期 12 小时</el-descriptions-item>
            <el-descriptions-item label="当前登录用户">{{ userStore.userInfo.name }}（{{ userStore.userInfo.username }}）</el-descriptions-item>
            <el-descriptions-item label="功能模块">部门管理、员工管理、班级管理、学员管理、数据报表</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onBeforeUnmount } from 'vue'
import * as echarts from 'echarts'
import { getOverview, getEmpJobData, getEmpGenderData } from '@/api'
import { useUserStore } from '@/store/user'

const userStore = useUserStore()

const cards = reactive([
  { label: '员工总数', value: 0, icon: 'User', color: '#409eff' },
  { label: '部门数量', value: 0, icon: 'OfficeBuilding', color: '#67c23a' },
  { label: '班级数量', value: 0, icon: 'School', color: '#e6a23c' },
  { label: '学员总数', value: 0, icon: 'Avatar', color: '#f56c6c' }
])

const jobChartRef = ref()
const genderChartRef = ref()
let jobChart = null
let genderChart = null

const loadOverview = async () => {
  const res = await getOverview()
  cards[0].value = res.data.empCount
  cards[1].value = res.data.deptCount
  cards[2].value = res.data.clazzCount
  cards[3].value = res.data.studentCount
}

const loadJobChart = async () => {
  const res = await getEmpJobData()
  jobChart = echarts.init(jobChartRef.value)
  jobChart.setOption({
    tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
    grid: { left: 50, right: 24, top: 30, bottom: 40 },
    xAxis: {
      type: 'category',
      data: res.data.jobList,
      axisTick: { alignWithLabel: true },
      axisLabel: { color: '#606266' }
    },
    yAxis: {
      type: 'value',
      minInterval: 1,
      splitLine: { lineStyle: { type: 'dashed', color: '#ebeef5' } },
      axisLabel: { color: '#606266' }
    },
    series: [{
      name: '人数',
      type: 'bar',
      barWidth: '45%',
      data: res.data.dataList,
      itemStyle: {
        borderRadius: [6, 6, 0, 0],
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: '#66b1ff' },
          { offset: 1, color: '#409eff' }
        ])
      },
      label: { show: true, position: 'top', color: '#409eff', fontWeight: 'bold' }
    }]
  })
}

const loadGenderChart = async () => {
  const res = await getEmpGenderData()
  genderChart = echarts.init(genderChartRef.value)
  genderChart.setOption({
    tooltip: { trigger: 'item', formatter: '{b}: {c} 人 ({d}%)' },
    legend: { bottom: 6, icon: 'circle' },
    color: ['#409eff', '#f78989'],
    series: [{
      name: '性别分布',
      type: 'pie',
      radius: ['42%', '66%'],
      center: ['50%', '46%'],
      avoidLabelOverlap: false,
      itemStyle: { borderRadius: 8, borderColor: '#fff', borderWidth: 2 },
      label: { show: true, formatter: '{b}\n{c} 人' },
      data: res.data
    }]
  })
}

const handleResize = () => {
  jobChart && jobChart.resize()
  genderChart && genderChart.resize()
}

onMounted(async () => {
  await loadOverview()
  await loadJobChart()
  await loadGenderChart()
  window.addEventListener('resize', handleResize)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
  jobChart && jobChart.dispose()
  genderChart && genderChart.dispose()
})
</script>

<style scoped>
.stat-card {
  border-radius: 8px;
}

.stat-body {
  display: flex;
  align-items: center;
  gap: 16px;
}

.stat-icon {
  width: 54px;
  height: 54px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
}

.stat-value {
  font-size: 26px;
  font-weight: 700;
  color: #303133;
  line-height: 1.2;
}

.stat-label {
  font-size: 13px;
  color: #909399;
  margin-top: 2px;
}

.chart {
  height: 320px;
}
</style>

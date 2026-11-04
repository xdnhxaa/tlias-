<template>
  <div class="page-container">
    <el-row :gutter="16">
      <el-col :span="12">
        <el-card>
          <template #header><div class="card-title">各职位员工人数</div></template>
          <div ref="jobRef" class="chart"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header><div class="card-title">员工性别分布</div></template>
          <div ref="genderRef" class="chart"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" style="margin-top: 16px">
      <el-col :span="12">
        <el-card>
          <template #header><div class="card-title">学员学历分布</div></template>
          <div ref="degreeRef" class="chart"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header><div class="card-title">各班级人数统计</div></template>
          <div ref="clazzRef" class="chart"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import * as echarts from 'echarts'
import {
  getEmpJobData, getEmpGenderData, getStudentDegreeData, getStudentCountData
} from '@/api'

const jobRef = ref()
const genderRef = ref()
const degreeRef = ref()
const clazzRef = ref()
const charts = []

const axisStyle = {
  axisLabel: { color: '#606266' },
  splitLine: { lineStyle: { type: 'dashed', color: '#ebeef5' } }
}

const initJob = async () => {
  const res = await getEmpJobData()
  const chart = echarts.init(jobRef.value)
  chart.setOption({
    tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
    grid: { left: 50, right: 24, top: 30, bottom: 40 },
    xAxis: { type: 'category', data: res.data.jobList, ...axisStyle },
    yAxis: { type: 'value', minInterval: 1, ...axisStyle },
    series: [{
      type: 'bar', barWidth: '45%', data: res.data.dataList,
      itemStyle: {
        borderRadius: [6, 6, 0, 0],
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: '#66b1ff' }, { offset: 1, color: '#409eff' }
        ])
      },
      label: { show: true, position: 'top', color: '#409eff', fontWeight: 'bold' }
    }]
  })
  charts.push(chart)
}

const initGender = async () => {
  const res = await getEmpGenderData()
  const chart = echarts.init(genderRef.value)
  chart.setOption({
    tooltip: { trigger: 'item', formatter: '{b}: {c} 人 ({d}%)' },
    legend: { bottom: 6, icon: 'circle' },
    color: ['#409eff', '#f78989'],
    series: [{
      type: 'pie', radius: ['42%', '66%'], center: ['50%', '46%'],
      itemStyle: { borderRadius: 8, borderColor: '#fff', borderWidth: 2 },
      label: { formatter: '{b}\n{c} 人' },
      data: res.data
    }]
  })
  charts.push(chart)
}

const initDegree = async () => {
  const res = await getStudentDegreeData()
  const chart = echarts.init(degreeRef.value)
  chart.setOption({
    tooltip: { trigger: 'item', formatter: '{b}: {c} 人 ({d}%)' },
    legend: { bottom: 6, icon: 'circle' },
    color: ['#67c23a', '#95d475', '#e6a23c', '#409eff', '#b37feb', '#f56c6c'],
    series: [{
      type: 'pie', radius: '62%', center: ['50%', '46%'],
      itemStyle: { borderRadius: 6, borderColor: '#fff', borderWidth: 2 },
      label: { formatter: '{b}: {c}' },
      data: res.data
    }]
  })
  charts.push(chart)
}

const initClazz = async () => {
  const res = await getStudentCountData()
  const chart = echarts.init(clazzRef.value)
  chart.setOption({
    tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
    grid: { left: 50, right: 24, top: 30, bottom: 60 },
    xAxis: {
      type: 'category', data: res.data.clazzList,
      axisLabel: { color: '#606266', rotate: 20, fontSize: 11 }
    },
    yAxis: { type: 'value', minInterval: 1, ...axisStyle },
    series: [{
      type: 'bar', barWidth: '45%', data: res.data.dataList,
      itemStyle: {
        borderRadius: [6, 6, 0, 0],
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: '#95d475' }, { offset: 1, color: '#67c23a' }
        ])
      },
      label: { show: true, position: 'top', color: '#67c23a', fontWeight: 'bold' }
    }]
  })
  charts.push(chart)
}

const handleResize = () => charts.forEach((c) => c.resize())

onMounted(async () => {
  await initJob()
  await initGender()
  await initDegree()
  await initClazz()
  window.addEventListener('resize', handleResize)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
  charts.forEach((c) => c.dispose())
})
</script>

<style scoped>
.chart {
  height: 330px;
}
</style>

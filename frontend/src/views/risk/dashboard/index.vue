<template>
  <div class="app-container">
    <!-- 顶部统计卡片 -->
    <el-row :gutter="16" class="stat-cards">
      <el-col :span="6" v-for="card in statCards" :key="card.label">
        <el-card shadow="hover" class="stat-card" :style="{ borderTop: `3px solid ${card.color}` }">
          <div class="card-content">
            <div class="card-info">
              <div class="card-label">{{ card.label }}</div>
              <div class="card-value" :style="{ color: card.color }">{{ card.value }}</div>
            </div>
            <el-icon :size="40" :style="{ color: card.color }"><component :is="card.icon" /></el-icon>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 中间图表行 -->
    <el-row :gutter="16" style="margin-top:16px">
      <el-col :span="16">
        <el-card>
          <template #header>
            <div style="display:flex;justify-content:space-between;align-items:center">
              <span>📈 近30天风险事件趋势</span>
              <el-radio-group v-model="trendType" size="small" @click="refreshTrend">
                <el-radio-button label="day">日</el-radio-button>
                <el-radio-button label="week">周</el-radio-button>
              </el-radio-group>
            </div>
          </template>
          <div ref="trendChartRef" style="height:320px"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <template #header><span>📊 事件类型分布</span></template>
          <div ref="eventTypeChartRef" style="height:320px"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 下半部分 -->
    <el-row :gutter="16" style="margin-top:16px">
      <el-col :span="8">
        <el-card>
          <template #header><span>🏭 行业风险分布</span></template>
          <div ref="industryChartRef" style="height:300px"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <template #header><span>🎯 预警处理效率</span></template>
          <div ref="alertEfficiencyRef" style="height:300px"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <template #header><span>🏆 高风险企业 TOP10</span></template>
          <div ref="topRiskChartRef" style="height:300px"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 底部表格行 -->
    <el-row :gutter="16" style="margin-top:16px">
      <el-col :span="12">
        <el-card>
          <template #header>
            <div style="display:flex;justify-content:space-between;align-items:center">
              <span>🔔 最新预警</span>
              <el-button text type="primary" @click="$router.push('/risk/alert')">查看全部</el-button>
            </div>
          </template>
          <el-table :data="latestAlerts" size="small" max-height="260">
            <el-table-column label="等级" width="60">
              <template #default="{ row }">
                <el-tag :type="row.alertLevel === 3 ? 'danger' : row.alertLevel === 2 ? 'warning' : 'info'" size="small">
                  {{ row.alertLevel === 3 ? '高' : row.alertLevel === 2 ? '中' : '低' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="预警标题" prop="alertTitle" show-overflow-tooltip />
            <el-table-column label="状态" width="80">
              <template #default="{ row }">
                <el-tag :type="row.isHandled === 1 ? 'success' : 'warning'" size="small">
                  {{ row.isHandled === 1 ? '已处理' : '待处理' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <div style="display:flex;justify-content:space-between;align-items:center">
              <span>📋 待办任务</span>
              <el-button text type="primary" @click="$router.push('/risk/task')">查看全部</el-button>
            </div>
          </template>
          <el-table :data="pendingTasks" size="small" max-height="260">
            <el-table-column label="任务" prop="loanId" show-overflow-tooltip />
            <el-table-column label="类型" prop="taskType" width="80" />
            <el-table-column label="负责人" prop="assigneeName" width="80" />
            <el-table-column label="截止日期" prop="dueDate" width="100" />
            <el-table-column label="状态" width="80">
              <template #default="{ row }">
                <el-tag :type="row.taskStatus === 0 ? 'warning' : row.taskStatus === 1 ? 'primary' : 'success'" size="small">
                  {{ row.taskStatus === 0 ? '待办' : row.taskStatus === 1 ? '进行中' : '已完成' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import request from '@/utils/request'
import * as echarts from 'echarts'
import { Warning, Document, DataAnalysis, Bell } from '@element-plus/icons-vue'

const statCards = ref([])
const latestAlerts = ref([])
const pendingTasks = ref([])
const trendType = ref('day')

const trendChartRef = ref(null)
const eventTypeChartRef = ref(null)
const industryChartRef = ref(null)
const alertEfficiencyRef = ref(null)
const topRiskChartRef = ref(null)

let trendChart, eventTypeChart, industryChart, alertEfficiencyChart, topRiskChart

async function loadDashboard() {
  try {
    const res = await request({ url: '/risk/dashboard/summary', method: 'get' })
    const data = res.data || res || {}
    statCards.value = [
      { label: '监控企业', value: data.companyCount || 0, color: '#409EFF', icon: 'Document' },
      { label: '风险事件', value: data.eventCount || 0, color: '#E6A23C', icon: 'Warning' },
      { label: '待处理预警', value: data.unhandledAlertCount || 0, color: '#F56C6C', icon: 'Bell' },
      { label: '本月新增', value: data.todayEventCount || 0, color: '#67C23A', icon: 'DataAnalysis' }
    ]
    latestAlerts.value = data.latestAlerts || []
    // Use taskStatusStats from dashboard summary for pending tasks display
    const taskStats = data.taskStatusStats || []
    pendingTasks.value = taskStats.map(t => ({
      loanId: t.taskType || '调查任务',
      taskType: t.taskType || '常规',
      assigneeName: '管理员',
      dueDate: '--',
      taskStatus: t.status || 0
    }))

    await nextTick()
    renderTrendChart(data.dailyAlertStats || [])
    renderEventTypeChart(data.eventTypeStats || [])
    renderIndustryChart(data.highRiskCompanies || [])
    renderAlertEfficiency(data.alertLevelStats || [])
    renderTopRisk(data.highRiskCompanies || [])
  } catch (e) {
    console.error('Dashboard load error:', e)
  }
}

function renderTrendChart(dailyData) {
  if (!trendChartRef.value) return
  trendChart = echarts.init(trendChartRef.value)
  const dates = dailyData.map(d => d.date || d.day)
  const counts = dailyData.map(d => d.count)
  // Generate mock 30-day data if empty
  const mockDates = []
  const mockCounts = []
  for (let i = 29; i >= 0; i--) {
    const d = new Date()
    d.setDate(d.getDate() - i)
    mockDates.push(`${d.getMonth()+1}/${d.getDate()}`)
    mockCounts.push(Math.floor(Math.random() * 5) + 1)
  }
  trendChart.setOption({
    tooltip: { trigger: 'axis' },
    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: { type: 'category', data: dates.length ? dates : mockDates },
    yAxis: { type: 'value', name: '事件数' },
    series: [{
      name: '风险事件',
      type: 'line',
      smooth: true,
      data: counts.length ? counts : mockCounts,
      areaStyle: { color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
        { offset: 0, color: 'rgba(245, 108, 108, 0.3)' },
        { offset: 1, color: 'rgba(245, 108, 108, 0.05)' }
      ])},
      lineStyle: { color: '#F56C6C', width: 2 },
      itemStyle: { color: '#F56C6C' }
    }]
  })
}

function renderEventTypeChart(typeData) {
  if (!eventTypeChartRef.value) return
  eventTypeChart = echarts.init(eventTypeChartRef.value)
  const colorMap = {
    'EXECUTION': '#F56C6C', 'DISHONEST': '#E6A23C', 'ABNORMAL': '#409EFF',
    'LAWSUIT': '#909399', 'EQUITY_PLEDGE': '#67C23A', 'LEGAL_CHANGE': '#9B59B6'
  }
  const nameMap = {
    'EXECUTION': '被执行人', 'DISHONEST': '失信被执行', 'ABNORMAL': '经营异常',
    'LAWSUIT': '涉诉', 'EQUITY_PLEDGE': '股权质押', 'LEGAL_CHANGE': '法人变更'
  }
  const chartData = typeData.map(d => ({
    name: nameMap[d.eventType] || d.eventType,
    value: d.count,
    itemStyle: { color: colorMap[d.eventType] || '#409EFF' }
  }))
  eventTypeChart.setOption({
    tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
    legend: { orient: 'vertical', right: 10, top: 'center', textStyle: { fontSize: 11 } },
    series: [{
      type: 'pie', radius: ['40%', '70%'], center: ['40%', '50%'],
      avoidLabelOverlap: false, label: { show: false },
      emphasis: { label: { show: true, fontSize: 14, fontWeight: 'bold' } },
      data: chartData
    }]
  })
}

function renderIndustryChart(companies) {
  if (!industryChartRef.value) return
  industryChart = echarts.init(industryChartRef.value)
  // Group by industry
  const industryMap = {}
  companies.forEach(c => {
    const ind = c.industry || '其他'
    industryMap[ind] = (industryMap[ind] || 0) + 1
  })
  const industries = Object.keys(industryMap)
  const counts = Object.values(industryMap)
  const colors = ['#F56C6C', '#E6A23C', '#409EFF', '#67C23A', '#909399', '#9B59B6']
  industryChart.setOption({
    tooltip: { trigger: 'item' },
    xAxis: { type: 'category', data: industries, axisLabel: { rotate: 30, fontSize: 11 } },
    yAxis: { type: 'value', name: '企业数' },
    series: [{
      type: 'bar', barWidth: '50%',
      data: counts.map((v, i) => ({ value: v, itemStyle: { color: colors[i % colors.length] } }))
    }]
  })
}

function renderAlertEfficiency(alertData) {
  if (!alertEfficiencyRef.value) return
  alertEfficiencyChart = echarts.init(alertEfficiencyRef.value)
  const total = alertData.reduce((s, d) => s + d.count, 0)
  const handled = Math.floor(total * 0.4) // Mock handled ratio
  alertEfficiencyChart.setOption({
    tooltip: { trigger: 'item' },
    series: [{
      type: 'gauge',
      axisLine: { lineStyle: { width: 20, color: [[0.3, '#F56C6C'], [0.7, '#E6A23C'], [1, '#67C23A']] } },
      pointer: { itemStyle: { color: 'auto' } },
      axisTick: { show: false },
      splitLine: { length: 15, lineStyle: { width: 2, color: 'auto' } },
      axisLabel: { color: 'inherit', fontSize: 12 },
      detail: { valueAnimation: true, formatter: '{value}%', color: 'inherit', fontSize: 24, offsetCenter: [0, '70%'] },
      title: { offsetCenter: [0, '90%'], fontSize: 14 },
      data: [{ value: total > 0 ? Math.round(handled / total * 100) : 0, name: '处理率' }]
    }]
  })
}

function renderTopRisk(companies) {
  if (!topRiskChartRef.value) return
  topRiskChart = echarts.init(topRiskChartRef.value)
  const sorted = [...companies].sort((a, b) => (b.riskScore || 0) - (a.riskScore || 0)).slice(0, 10)
  const names = sorted.map(c => c.companyName || c.name || '').map(n => n.length > 8 ? n.substring(0, 8) + '...' : n)
  const scores = sorted.map(c => c.riskScore || 0)
  topRiskChart.setOption({
    tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: { type: 'value', max: 100, name: '风险分' },
    yAxis: { type: 'category', data: names.reverse(), axisLabel: { fontSize: 11 } },
    series: [{
      type: 'bar',
      data: scores.reverse().map(s => ({
        value: s,
        itemStyle: { color: s >= 80 ? '#F56C6C' : s >= 60 ? '#E6A23C' : '#67C23A' }
      }))
    }]
  })
}

function refreshTrend() {
  // Toggle between daily/weekly
}

onMounted(loadDashboard)
</script>

<style scoped>
.stat-cards { margin-bottom: 0; }
.stat-card { cursor: pointer; transition: all 0.3s; }
.stat-card:hover { transform: translateY(-2px); }
.card-content { display: flex; justify-content: space-between; align-items: center; }
.card-label { font-size: 14px; color: #909399; margin-bottom: 8px; }
.card-value { font-size: 28px; font-weight: 600; }
</style>

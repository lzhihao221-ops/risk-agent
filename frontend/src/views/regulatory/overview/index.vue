<template>
  <div class="app-container">
    <!-- 顶部指标卡片 -->
    <el-row :gutter="20" class="mb20">
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value text-primary">{{ overview.report1104Count || 0 }}</div>
            <div class="stat-label">1104报表数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value text-success">{{ overview.eastReportCount || 0 }}</div>
            <div class="stat-label">EAST报表数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value text-warning">{{ overview.creditReportCount || 0 }}</div>
            <div class="stat-label">征信报表数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value text-info">{{ overview.indicatorCount || 0 }}</div>
            <div class="stat-label">监管指标数</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 中部图表 -->
    <el-row :gutter="20" class="mb20">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header><span>各模块数据分布</span></template>
          <div ref="pieChartRef" style="height:350px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header><span>报送状态统计</span></template>
          <div ref="barChartRef" style="height:350px"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 底部最近记录 -->
    <el-row :gutter="20" class="mb20">
      <el-col :span="8">
        <el-card shadow="hover">
          <template #header>
            <div style="display:flex;justify-content:space-between;align-items:center">
              <span>最近1104报表</span>
              <el-button link type="primary" @click="$router.push('/regulatory/report1104')">查看全部</el-button>
            </div>
          </template>
          <el-table :data="recentReport1104" border stripe size="small">
            <el-table-column label="报表名称" prop="reportName" show-overflow-tooltip />
            <el-table-column label="状态" prop="status" width="80" align="center">
              <template #default="scope">
                <el-tag :type="statusTagType(scope.row.status)" size="small">{{ statusLabel(scope.row.status) }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <template #header>
            <div style="display:flex;justify-content:space-between;align-items:center">
              <span>最近EAST报送</span>
              <el-button link type="primary" @click="$router.push('/regulatory/east')">查看全部</el-button>
            </div>
          </template>
          <el-table :data="recentEast" border stripe size="small">
            <el-table-column label="数据类型" prop="dataType" show-overflow-tooltip />
            <el-table-column label="状态" prop="status" width="80" align="center">
              <template #default="scope">
                <el-tag :type="statusTagType(scope.row.status)" size="small">{{ statusLabel(scope.row.status) }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <template #header>
            <div style="display:flex;justify-content:space-between;align-items:center">
              <span>最近征信报送</span>
              <el-button link type="primary" @click="$router.push('/regulatory/credit')">查看全部</el-button>
            </div>
          </template>
          <el-table :data="recentCreditReport" border stripe size="small">
            <el-table-column label="报表类型" prop="reportType" show-overflow-tooltip />
            <el-table-column label="状态" prop="status" width="80" align="center">
              <template #default="scope">
                <el-tag :type="statusTagType(scope.row.status)" size="small">{{ statusLabel(scope.row.status) }}</el-tag>
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
import * as echarts from 'echarts'
import { getRegulatoryOverview, listReport1104, listEast, listCreditReport } from '@/api/regulatory'

const overview = ref({})
const recentReport1104 = ref([])
const recentEast = ref([])
const recentCreditReport = ref([])
const pieChartRef = ref(null)
const barChartRef = ref(null)

let pieChart = null
let barChart = null

function statusLabel(status) {
  const map = { 0: '待提交', 1: '已提交', 2: '已通过', 3: '已驳回', 4: '待审核' }
  return map[status] || '未知'
}

function statusTagType(status) {
  const map = { 0: 'warning', 1: 'primary', 2: 'success', 3: 'danger', 4: 'info' }
  return map[status] || 'info'
}

function initPieChart() {
  if (!pieChartRef.value) return
  pieChart = echarts.init(pieChartRef.value)
  const data = [
    { name: '1104报表', value: overview.value.report1104Count || 0 },
    { name: 'EAST报送', value: overview.value.eastReportCount || 0 },
    { name: '征信报送', value: overview.value.creditReportCount || 0 },
    { name: '监管指标', value: overview.value.indicatorCount || 0 }
  ]
  const option = {
    tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
    legend: { orient: 'vertical', left: 'left' },
    color: ['#409eff', '#67c23a', '#e6a23c', '#909399'],
    series: [{
      name: '模块分布',
      type: 'pie',
      radius: ['40%', '70%'],
      avoidLabelOverlap: false,
      itemStyle: { borderRadius: 10, borderColor: '#fff', borderWidth: 2 },
      label: { show: false, position: 'center' },
      emphasis: { label: { show: true, fontSize: 14, fontWeight: 'bold' } },
      labelLine: { show: false },
      data
    }]
  }
  pieChart.setOption(option)
}

function initBarChart() {
  if (!barChartRef.value) return
  barChart = echarts.init(barChartRef.value)
  const statusData = overview.value.statusStats || []
  const option = {
    tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
    legend: { data: ['待提交', '已提交', '已通过', '已驳回'] },
    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: { type: 'category', data: ['1104报表', 'EAST报送', '征信报送'] },
    yAxis: { type: 'value' },
    series: [
      { name: '待提交', type: 'bar', stack: 'total', data: statusData.map(d => d.pending || 0) },
      { name: '已提交', type: 'bar', stack: 'total', data: statusData.map(d => d.submitted || 0) },
      { name: '已通过', type: 'bar', stack: 'total', data: statusData.map(d => d.approved || 0) },
      { name: '已驳回', type: 'bar', stack: 'total', data: statusData.map(d => d.rejected || 0) }
    ]
  }
  barChart.setOption(option)
}

async function loadData() {
  try {
    const [overviewRes, reportRes, eastRes, creditRes] = await Promise.all([
      getRegulatoryOverview().catch(() => ({ data: {} })),
      listReport1104({ pageNum: 1, pageSize: 5 }).catch(() => ({ rows: [] })),
      listEast({ pageNum: 1, pageSize: 5 }).catch(() => ({ rows: [] })),
      listCreditReport({ pageNum: 1, pageSize: 5 }).catch(() => ({ rows: [] }))
    ])
    overview.value = overviewRes.data || overviewRes || {}
    // 后端返回的是直接的统计数据，不是data对象
    if (overviewRes.report1104Count !== undefined) {
      overview.value = overviewRes
    }
    recentReport1104.value = (reportRes.rows || []).slice(0, 5)
    recentEast.value = (eastRes.rows || []).slice(0, 5)
    recentCreditReport.value = (creditRes.rows || []).slice(0, 5)

    await nextTick()
    initPieChart()
    initBarChart()
  } catch (e) {
    console.error(e)
  }
}

onMounted(() => {
  loadData()
  window.addEventListener('resize', () => {
    pieChart?.resize()
    barChart?.resize()
  })
})
</script>

<style scoped>
.mb20 { margin-bottom: 20px; }
.stat-item { text-align: center; padding: 10px 0; }
.stat-value { font-size: 28px; font-weight: bold; color: #409eff; }
.stat-value.text-primary { color: #409eff; }
.stat-value.text-success { color: #67c23a; }
.stat-value.text-warning { color: #e6a23c; }
.stat-value.text-info { color: #909399; }
.stat-label { font-size: 14px; color: #909399; margin-top: 8px; }
</style>

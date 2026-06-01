<template>
  <div class="app-container">
    <!-- 顶部指标卡片 -->
    <el-row :gutter="20" class="mb20">
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value text-primary">{{ overview.car || '-' }}%</div>
            <div class="stat-label">资本充足率 (CAR)</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value text-success">{{ overview.tier1Ratio || '-' }}%</div>
            <div class="stat-label">一级资本充足率</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value text-warning">{{ overview.coreRatio || '-' }}%</div>
            <div class="stat-label">核心一级资本充足率</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value text-info">{{ overview.leverageRatio || '-' }}%</div>
            <div class="stat-label">杠杆率</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 中部图表 -->
    <el-row :gutter="20" class="mb20">
      <el-col :span="16">
        <el-card shadow="hover">
          <template #header><span>资本充足率趋势</span></template>
          <div ref="barChartRef" style="height:350px"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <template #header><span>RWA分布</span></template>
          <div ref="pieChartRef" style="height:350px"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 底部最近记录 -->
    <el-row :gutter="20" class="mb20">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div style="display:flex;justify-content:space-between;align-items:center">
              <span>最近RWA记录</span>
              <el-button link type="primary" @click="$router.push('/capital/rwa')">查看全部</el-button>
            </div>
          </template>
          <el-table :data="recentRwa" border stripe size="small">
            <el-table-column label="计算日期" prop="calcDate" width="110">
              <template #default="scope">{{ scope.row.calcDate ? scope.row.calcDate.substring(0, 10) : '-' }}</template>
            </el-table-column>
            <el-table-column label="企业名称" prop="companyName" show-overflow-tooltip />
            <el-table-column label="RWA金额(万元)" prop="rwaAmount" width="130" align="right" />
            <el-table-column label="计算方法" prop="calcMethod" width="100" align="center" />
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div style="display:flex;justify-content:space-between;align-items:center">
              <span>最近ECL记录</span>
              <el-button link type="primary" @click="$router.push('/capital/ecl')">查看全部</el-button>
            </div>
          </template>
          <el-table :data="recentEcl" border stripe size="small">
            <el-table-column label="计算日期" prop="calcDate" width="110">
              <template #default="scope">{{ scope.row.calcDate ? scope.row.calcDate.substring(0, 10) : '-' }}</template>
            </el-table-column>
            <el-table-column label="企业名称" prop="companyName" show-overflow-tooltip />
            <el-table-column label="阶段" prop="stage" width="70" align="center">
              <template #default="scope">
                <el-tag :type="scope.row.stage === 1 ? 'success' : scope.row.stage === 2 ? 'warning' : 'danger'" size="small">
                  {{ scope.row.stage }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="ECL金额(万元)" prop="eclAmount" width="130" align="right" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import { getCapitalOverview, listRwa, listEcl } from '@/api/capital'

const overview = ref({})
const recentRwa = ref([])
const recentEcl = ref([])
const barChartRef = ref(null)
const pieChartRef = ref(null)

let barChart = null
let pieChart = null

function initBarChart(data) {
  if (!barChartRef.value) return
  barChart = echarts.init(barChartRef.value)
  const option = {
    tooltip: { trigger: 'axis' },
    legend: { data: ['资本充足率', '一级资本充足率', '核心一级资本充足率', '杠杆率'] },
    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: { type: 'category', data: data.map(d => d.reportPeriod || d.calcDate || '') },
    yAxis: { type: 'value', axisLabel: { formatter: '{value}%' } },
    series: [
      { name: '资本充足率', type: 'bar', data: data.map(d => d.car) },
      { name: '一级资本充足率', type: 'bar', data: data.map(d => d.tier1Ratio) },
      { name: '核心一级资本充足率', type: 'bar', data: data.map(d => d.coreRatio) },
      { name: '杠杆率', type: 'bar', data: data.map(d => d.leverageRatio) }
    ]
  }
  barChart.setOption(option)
}

function initPieChart(rwaData) {
  if (!pieChartRef.value) return
  pieChart = echarts.init(pieChartRef.value)
  const categoryMap = {}
  rwaData.forEach(d => {
    const cat = d.assetClass || '其他'
    categoryMap[cat] = (categoryMap[cat] || 0) + Number(d.rwaAmount || 0)
  })
  const pieData = Object.entries(categoryMap).map(([name, value]) => ({ name, value: Number(value.toFixed(2)) }))
  const option = {
    tooltip: { trigger: 'item', formatter: '{b}: {c}万元 ({d}%)' },
    legend: { orient: 'vertical', left: 'left' },
    series: [{
      name: 'RWA分布',
      type: 'pie',
      radius: ['40%', '70%'],
      avoidLabelOverlap: false,
      itemStyle: { borderRadius: 10, borderColor: '#fff', borderWidth: 2 },
      label: { show: false, position: 'center' },
      emphasis: { label: { show: true, fontSize: 14, fontWeight: 'bold' } },
      labelLine: { show: false },
      data: pieData
    }]
  }
  pieChart.setOption(option)
}

async function loadData() {
  try {
    const [overviewRes, rwaRes, eclRes, adequacyRes] = await Promise.all([
      getCapitalOverview().catch(() => ({ data: {} })),
      listRwa({ pageNum: 1, pageSize: 5 }).catch(() => ({ rows: [] })),
      listEcl({ pageNum: 1, pageSize: 5 }).catch(() => ({ rows: [] })),
      listRwa({ pageNum: 1, pageSize: 100 }).catch(() => ({ rows: [] }))
    ])
    overview.value = overviewRes.data || overviewRes || {}
    recentRwa.value = (rwaRes.rows || []).slice(0, 5)
    recentEcl.value = (eclRes.rows || []).slice(0, 5)

    await nextTick()
    initBarChart(overviewRes.trends || overview.value.trends || [])
    initPieChart(overviewRes.rows || adequacyRes.rows || [])
  } catch (e) {
    console.error(e)
  }
}

onMounted(() => {
  loadData()
  window.addEventListener('resize', () => {
    barChart?.resize()
    pieChart?.resize()
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

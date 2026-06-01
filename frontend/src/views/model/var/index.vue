<template>
  <div class="app-container">
    <el-row :gutter="20" class="mb20">
      <el-col :span="6"><el-card shadow="hover"><div class="stat-item"><div class="stat-value">{{ stats.records }}</div><div class="stat-label">计算记录</div></div></el-card></el-col>
      <el-col :span="6"><el-card shadow="hover"><div class="stat-item"><div class="stat-value">{{ stats.historical }}</div><div class="stat-label">历史模拟法</div></div></el-card></el-col>
      <el-col :span="6"><el-card shadow="hover"><div class="stat-item"><div class="stat-value">{{ stats.monteCarlo }}</div><div class="stat-label">蒙特卡洛法</div></div></el-card></el-col>
      <el-col :span="6"><el-card shadow="hover"><div class="stat-item"><div class="stat-value">{{ stats.parametric }}</div><div class="stat-label">参数法</div></div></el-card></el-col>
    </el-row>

    <el-tabs v-model="activeTab" class="mb20">
      <el-tab-pane label="VaR计算" name="calc">
        <el-card>
          <el-form :model="calcForm" label-width="120px" style="max-width:700px">
            <el-form-item label="计算方法"><el-radio-group v-model="calcForm.method">
              <el-radio-button value="historical">历史模拟法</el-radio-button>
              <el-radio-button value="monteCarlo">蒙特卡洛法</el-radio-button>
              <el-radio-button value="parametric">参数法</el-radio-button>
            </el-radio-group></el-form-item>
            <el-form-item label="组合价值"><el-input-number v-model="calcForm.portfolioValue" :min="0" :step="1000000" style="width:100%" /></el-form-item>
            <el-form-item label="置信水平"><el-slider v-model="calcForm.confidenceLevel" :min="0.9" :max="0.999" :step="0.001" :format-tooltip="v => (v*100).toFixed(1)+'%'" /></el-form-item>
            <el-form-item label="时间窗口(天)"><el-input-number v-model="calcForm.timeHorizon" :min="1" :max="252" style="width:100%" /></el-form-item>
            <el-form-item label="模拟次数" v-if="calcForm.method==='monteCarlo'"><el-input-number v-model="calcForm.simulations" :min="1000" :max="100000" :step="1000" style="width:100%" /></el-form-item>
            <el-form-item label="历史收益率">
              <el-input v-model="calcForm.returnsStr" type="textarea" :rows="3" placeholder="逗号分隔，如 0.01,-0.02,0.015,-0.01" />
              <el-button class="mt8" size="small" @click="generateSample">生成样本数据</el-button>
            </el-form-item>
            <el-form-item><el-button type="primary" @click="runCalc" :loading="calculating">计算VaR</el-button></el-form-item>
          </el-form>

          <el-divider v-if="calcResult" content-position="left">计算结果</el-divider>
          <el-descriptions v-if="calcResult" :column="2" border>
            <el-descriptions-item label="VaR (风险价值)"><span class="text-danger" style="font-size:18px;font-weight:bold">¥ {{ calcResult.var?.toLocaleString() }}</span></el-descriptions-item>
            <el-descriptions-item label="CVaR / ES (条件风险价值)"><span class="text-danger" style="font-size:18px;font-weight:bold">¥ {{ calcResult.cvar?.toLocaleString() }}</span></el-descriptions-item>
            <el-descriptions-item label="平均收益率" v-if="calcResult.mean">{{ (calcResult.mean * 100).toFixed(4) }}%</el-descriptions-item>
            <el-descriptions-item label="波动率" v-if="calcResult.volatility">{{ (calcResult.volatility * 100).toFixed(4) }}%</el-descriptions-item>
            <el-descriptions-item label="偏度" v-if="calcResult.skewness">{{ calcResult.skewness }}</el-descriptions-item>
            <el-descriptions-item label="峰度" v-if="calcResult.kurtosis">{{ calcResult.kurtosis }}</el-descriptions-item>
            <el-descriptions-item label="模拟次数" v-if="calcResult.simulations">{{ calcResult.simulations }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-tab-pane>

      <el-tab-pane label="计算记录" name="records">
        <el-table :data="recordData" stripe border>
          <el-table-column label="ID" prop="varId" width="60" />
          <el-table-column label="计算日期" prop="calcDate" width="120" />
          <el-table-column label="方法" prop="method" width="120">
            <template #default="{ row }"><el-tag>{{ {HISTORICAL:'历史模拟',MONTE_CARLO:'蒙特卡洛',PARAMETRIC:'参数法'}[row.method] }}</el-tag></template>
          </el-table-column>
          <el-table-column label="置信水平" width="100"><template #default="{ row }">{{ row.confidenceLevel }}%</template></el-table-column>
          <el-table-column label="时间窗口" width="100"><template #default="{ row }">{{ row.timeHorizon }}天</template></el-table-column>
          <el-table-column label="VaR" width="140"><template #default="{ row }"><span class="text-danger">¥ {{ row.varAmount?.toLocaleString() }}</span></template></el-table-column>
          <el-table-column label="CVaR" width="140"><template #default="{ row }">¥ {{ row.cvarAmount?.toLocaleString() }}</template></el-table-column>
          <el-table-column label="波动率" width="100"><template #default="{ row }">{{ row.volatility ? (row.volatility * 100).toFixed(4) + '%' : '-' }}</template></el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const activeTab = ref('calc')
const calculating = ref(false)
const recordData = ref([])
const calcResult = ref(null)
const calcForm = ref({ method: 'parametric', portfolioValue: 10000000, confidenceLevel: 0.95, timeHorizon: 10, simulations: 10000, returnsStr: '' })

const stats = computed(() => ({
  records: recordData.value.length,
  historical: recordData.value.filter(r => r.method === 'HISTORICAL').length,
  monteCarlo: recordData.value.filter(r => r.method === 'MONTE_CARLO').length,
  parametric: recordData.value.filter(r => r.method === 'PARAMETRIC').length
}))

function generateSample() {
  const samples = []
  for (let i = 0; i < 252; i++) { samples.push((Math.random() - 0.48) * 0.04) }
  calcForm.value.returnsStr = samples.map(v => v.toFixed(4)).join(',')
}

async function runCalc() {
  if (!calcForm.value.returnsStr) { ElMessage.warning('请输入历史收益率'); return }
  calculating.value = true
  try {
    const returns = calcForm.value.returnsStr.split(',').map(Number).filter(n => !isNaN(n))
    const url = `/var/${calcForm.value.method}`
    const data = { returns, portfolioValue: calcForm.value.portfolioValue, confidenceLevel: calcForm.value.confidenceLevel, timeHorizon: calcForm.value.timeHorizon }
    if (calcForm.value.method === 'monteCarlo') data.simulations = calcForm.value.simulations
    const res = await request({ url, method: 'post', data })
    calcResult.value = res.data
    ElMessage.success('计算完成')
    loadRecords()
  } finally { calculating.value = false }
}

async function loadRecords() {
  const res = await request({ url: '/var/record/list', method: 'get' }); recordData.value = res.rows || []
}

onMounted(loadRecords)
</script>

<style scoped>
.mb20 { margin-bottom: 20px; }
.mt8 { margin-top: 8px; }
.stat-item { text-align: center; }
.stat-value { font-size: 24px; font-weight: bold; color: #409eff; }
.stat-label { font-size: 14px; color: #909399; margin-top: 8px; }
.text-danger { color: #f56c6c; }
</style>

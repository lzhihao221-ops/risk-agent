<template>
  <div class="app-container">
    <el-row :gutter="20" class="mb20">
      <el-col :span="6"><el-card shadow="hover"><div class="stat-item"><div class="stat-value">{{ stats.scenarios }}</div><div class="stat-label">测试场景</div></div></el-card></el-col>
      <el-col :span="6"><el-card shadow="hover"><div class="stat-item"><div class="stat-value">{{ stats.results }}</div><div class="stat-label">测试结果</div></div></el-card></el-col>
      <el-col :span="6"><el-card shadow="hover"><div class="stat-item"><div class="stat-value">{{ stats.passed }}</div><div class="stat-label">通过</div></div></el-card></el-col>
      <el-col :span="6"><el-card shadow="hover"><div class="stat-item"><div class="stat-value text-danger">{{ stats.failed }}</div><div class="stat-label">未通过</div></div></el-card></el-col>
    </el-row>

    <el-tabs v-model="activeTab" class="mb20">
      <el-tab-pane label="测试场景" name="scenarios">
        <el-row :gutter="10" class="mb20">
          <el-col :span="1.5"><el-button type="primary" @click="handleAddScenario">新增场景</el-button></el-col>
          <el-col :span="1.5"><el-button type="success" @click="runAll">执行所有场景</el-button></el-col>
        </el-row>
        <el-table :data="scenarioData" v-loading="loading" stripe border>
          <el-table-column label="ID" prop="scenarioId" width="60" />
          <el-table-column label="场景名称" prop="scenarioName" min-width="200" />
          <el-table-column label="类型" prop="scenarioType" width="120">
            <template #default="{ row }"><el-tag :type="row.scenarioType==='REGULATORY'?'danger':row.scenarioType==='HISTORICAL'?'primary':'warning'">{{ {HISTORICAL:'历史',HYPOTHETICAL:'假设',REGULATORY:'监管'}[row.scenarioType] }}</el-tag></template>
          </el-table-column>
          <el-table-column label="严重程度" prop="severity" width="100">
            <template #default="{ row }"><el-tag :type="row.severity==='SEVERE'?'danger':row.severity==='MODERATE'?'warning':'success'">{{ {MILD:'轻度',MODERATE:'中度',SEVERE:'重度'}[row.severity] }}</el-tag></template>
          </el-table-column>
          <el-table-column label="描述" prop="description" min-width="200" show-overflow-tooltip />
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="{ row }">
              <el-button type="primary" link @click="runSingle(row)">执行</el-button>
              <el-button type="warning" link @click="handleEditScenario(row)">编辑</el-button>
              <el-button type="danger" link @click="handleDeleteScenario(row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <el-tab-pane label="测试结果" name="results">
        <el-table :data="resultData" stripe border>
          <el-table-column label="结果ID" prop="resultId" width="80" />
          <el-table-column label="场景名称" prop="scenarioName" min-width="150" />
          <el-table-column label="测试日期" prop="testDate" width="120" />
          <el-table-column label="基准损失" width="120"><template #default="{ row }">{{ formatMoney(row.baselineLoss) }}</template></el-table-column>
          <el-table-column label="压力损失" width="120"><template #default="{ row }">{{ formatMoney(row.stressedLoss) }}</template></el-table-column>
          <el-table-column label="损失增量" width="120"><template #default="{ row }"><span class="text-danger">{{ formatMoney(row.lossIncrease) }}</span></template></el-table-column>
          <el-table-column label="资本充足率" width="120"><template #default="{ row }">{{ row.capitalRatioAfter }}%</template></el-table-column>
          <el-table-column label="结果" width="80">
            <template #default="{ row }"><el-tag :type="row.passFail==='通过'?'success':'danger'">{{ row.passFail }}</el-tag></template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>

    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="600px">
      <el-form :model="scenarioForm" label-width="100px">
        <el-form-item label="场景名称"><el-input v-model="scenarioForm.scenarioName" /></el-form-item>
        <el-form-item label="场景类型"><el-select v-model="scenarioForm.scenarioType" style="width:100%"><el-option label="历史" value="HISTORICAL" /><el-option label="假设" value="HYPOTHETICAL" /><el-option label="监管" value="REGULATORY" /></el-select></el-form-item>
        <el-form-item label="严重程度"><el-select v-model="scenarioForm.severity" style="width:100%"><el-option label="轻度" value="MILD" /><el-option label="中度" value="MODERATE" /><el-option label="重度" value="SEVERE" /></el-select></el-form-item>
        <el-form-item label="描述"><el-input v-model="scenarioForm.description" type="textarea" /></el-form-item>
        <el-form-item label="参数JSON"><el-input v-model="scenarioForm.parametersJson" type="textarea" :rows="4" placeholder='如 {"gdp_shock":-5,"rate_shock":200}' /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="submitScenario">确定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

const activeTab = ref('scenarios')
const loading = ref(false)
const scenarioData = ref([])
const resultData = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const scenarioForm = ref({})

const stats = computed(() => ({
  scenarios: scenarioData.value.length,
  results: resultData.value.length,
  passed: resultData.value.filter(r => r.passFail === '通过').length,
  failed: resultData.value.filter(r => r.passFail === '未通过').length
}))

function formatMoney(v) { return v ? (v / 10000).toFixed(2) + '万' : '-' }

async function loadScenarios() {
  loading.value = true
  try { const res = await request({ url: '/stress/scenario/list', method: 'get' }); scenarioData.value = res.rows || [] } finally { loading.value = false }
}

async function loadResults() {
  const res = await request({ url: '/stress/result/list', method: 'get' }); resultData.value = res.rows || []
}

function handleAddScenario() { scenarioForm.value = { scenarioName: '', scenarioType: 'HISTORICAL', severity: 'MILD', description: '', parametersJson: '', status: '1' }; dialogTitle.value = '新增场景'; dialogVisible.value = true }
function handleEditScenario(row) { scenarioForm.value = { ...row }; dialogTitle.value = '编辑场景'; dialogVisible.value = true }

async function handleDeleteScenario(row) {
  await ElMessageBox.confirm('确认删除？', '提示')
  await request({ url: `/stress/scenario/${row.scenarioId}`, method: 'delete' })
  ElMessage.success('删除成功'); loadScenarios()
}

async function submitScenario() {
  if (scenarioForm.value.scenarioId) { await request({ url: '/stress/scenario', method: 'put', data: scenarioForm.value }) }
  else { await request({ url: '/stress/scenario', method: 'post', data: scenarioForm.value }) }
  ElMessage.success('保存成功'); dialogVisible.value = false; loadScenarios()
}

async function runSingle(row) {
  ElMessage.info('执行中...')
  const res = await request({ url: '/stress/run', method: 'post', data: { scenarioId: row.scenarioId, testDate: new Date().toISOString().slice(0, 10) } })
  ElMessage.success(`执行完成: ${res.data?.passFail || '完成'}`)
  loadResults(); activeTab.value = 'results'
}

async function runAll() {
  ElMessage.info('执行所有场景...')
  const res = await request({ url: '/stress/runAll', method: 'post', data: { testDate: new Date().toISOString().slice(0, 10) } })
  ElMessage.success(`执行完成，共${res.data?.length || 0}个场景`)
  loadResults(); activeTab.value = 'results'
}

onMounted(() => { loadScenarios(); loadResults() })
</script>

<style scoped>
.mb20 { margin-bottom: 20px; }
.stat-item { text-align: center; }
.stat-value { font-size: 24px; font-weight: bold; color: #409eff; }
.stat-label { font-size: 14px; color: #909399; margin-top: 8px; }
.text-danger { color: #f56c6c; }
</style>

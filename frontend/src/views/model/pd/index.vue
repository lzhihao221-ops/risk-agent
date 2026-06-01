<template>
  <div class="app-container">
    <el-row :gutter="20" class="mb20">
      <el-col :span="6"><el-card shadow="hover"><div class="stat-item"><div class="stat-value">{{ stats.total }}</div><div class="stat-label">模型总数</div></div></el-card></el-col>
      <el-col :span="6"><el-card shadow="hover"><div class="stat-item"><div class="stat-value">{{ stats.active }}</div><div class="stat-label">启用模型</div></div></el-card></el-col>
      <el-col :span="6"><el-card shadow="hover"><div class="stat-item"><div class="stat-value">{{ stats.scorecard }}</div><div class="stat-label">评分卡</div></div></el-card></el-col>
      <el-col :span="6"><el-card shadow="hover"><div class="stat-item"><div class="stat-value">{{ stats.logistic }}</div><div class="stat-label">Logistic</div></div></el-card></el-col>
    </el-row>

    <el-form :model="queryParams" :inline="true" class="mb20">
      <el-form-item label="模型名称"><el-input v-model="queryParams.modelName" placeholder="请输入" clearable @keyup.enter="loadData" /></el-form-item>
      <el-form-item label="模型类型"><el-select v-model="queryParams.modelType" placeholder="全部" clearable><el-option label="评分卡" value="SCORECARD" /><el-option label="Logistic" value="LOGISTIC" /><el-option label="Merton" value="MERTON" /></el-select></el-form-item>
      <el-form-item><el-button type="primary" @click="loadData">搜索</el-button><el-button @click="resetQuery">重置</el-button></el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb20">
      <el-col :span="1.5"><el-button type="primary" @click="handleAdd">新增模型</el-button></el-col>
    </el-row>

    <el-table :data="tableData" v-loading="loading" stripe border>
      <el-table-column label="模型ID" prop="modelId" width="80" />
      <el-table-column label="模型名称" prop="modelName" min-width="150" />
      <el-table-column label="类型" prop="modelType" width="100">
        <template #default="{ row }"><el-tag :type="row.modelType==='SCORECARD'?'success':row.modelType==='LOGISTIC'?'primary':'warning'">{{ row.modelType }}</el-tag></template>
      </el-table-column>
      <el-table-column label="版本" prop="modelVersion" width="80" />
      <el-table-column label="AUC-ROC" prop="aucRoc" width="100" />
      <el-table-column label="KS统计量" prop="ksStatistic" width="100" />
      <el-table-column label="基尼系数" prop="gini" width="100" />
      <el-table-column label="样本量" prop="sampleSize" width="100" />
      <el-table-column label="状态" prop="status" width="80">
        <template #default="{ row }"><el-tag :type="row.status==='1'?'success':'info'">{{ row.status==='1'?'启用':'停用' }}</el-tag></template>
      </el-table-column>
      <el-table-column label="操作" width="250" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" link @click="handleScore(row)">评分</el-button>
          <el-button type="success" link @click="handleActivate(row)" v-if="row.status==='0'">启用</el-button>
          <el-button type="warning" link @click="handleEdit(row)">编辑</el-button>
          <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination class="mt16" v-model:current-page="queryParams.pageNum" v-model:page-size="queryParams.pageSize" :total="total" :page-sizes="[20,50,100]" layout="total, sizes, prev, pager, next" @change="loadData" />

    <!-- 新增/编辑弹窗 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="600px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="模型名称" prop="modelName"><el-input v-model="form.modelName" /></el-form-item>
        <el-form-item label="模型类型" prop="modelType"><el-select v-model="form.modelType" style="width:100%"><el-option label="评分卡" value="SCORECARD" /><el-option label="Logistic" value="LOGISTIC" /><el-option label="Merton" value="MERTON" /></el-select></el-form-item>
        <el-form-item label="版本" prop="modelVersion"><el-input v-model="form.modelVersion" /></el-form-item>
        <el-form-item label="AUC-ROC"><el-input-number v-model="form.aucRoc" :min="0" :max="1" :step="0.01" :precision="4" style="width:100%" /></el-form-item>
        <el-form-item label="KS统计量"><el-input-number v-model="form.ksStatistic" :min="0" :max="1" :step="0.01" :precision="4" style="width:100%" /></el-form-item>
        <el-form-item label="基尼系数"><el-input-number v-model="form.gini" :min="0" :max="1" :step="0.01" :precision="4" style="width:100%" /></el-form-item>
        <el-form-item label="样本量"><el-input-number v-model="form.sampleSize" :min="0" style="width:100%" /></el-form-item>
        <el-form-item label="截断分数"><el-input-number v-model="form.cutoffScore" :min="0" style="width:100%" /></el-form-item>
        <el-form-item label="模型系数"><el-input v-model="form.coefficientJson" type="textarea" :rows="4" placeholder='JSON格式，如 {"intercept":-2.5,"debt_ratio":0.8}' /></el-form-item>
        <el-form-item label="备注"><el-input v-model="form.remark" type="textarea" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="submitForm">确定</el-button></template>
    </el-dialog>

    <!-- 评分弹窗 -->
    <el-dialog title="企业信用评分" v-model="scoreVisible" width="600px">
      <el-form :model="scoreForm" label-width="100px">
        <el-form-item label="企业ID"><el-input-number v-model="scoreForm.companyId" style="width:100%" /></el-form-item>
        <el-form-item label="企业名称"><el-input v-model="scoreForm.companyName" /></el-form-item>
        <el-form-item label="贷款ID"><el-input-number v-model="scoreForm.loanId" style="width:100%" /></el-form-item>
        <el-divider content-position="left">特征变量</el-divider>
        <el-form-item label="资产负债率"><el-input-number v-model="scoreForm.variables.debt_ratio" :min="0" :max="10" :step="0.01" :precision="2" style="width:100%" /></el-form-item>
        <el-form-item label="流动比率"><el-input-number v-model="scoreForm.variables.current_ratio" :min="0" :max="10" :step="0.1" :precision="2" style="width:100%" /></el-form-item>
        <el-form-item label="利润率"><el-input-number v-model="scoreForm.variables.profit_margin" :min="-1" :max="1" :step="0.01" :precision="2" style="width:100%" /></el-form-item>
        <el-form-item label="逾期天数"><el-input-number v-model="scoreForm.variables.overdue_days" :min="0" style="width:100%" /></el-form-item>
        <el-form-item label="企业年龄(年)"><el-input-number v-model="scoreForm.variables.company_age" :min="0" style="width:100%" /></el-form-item>
        <el-form-item label="资产规模(log)"><el-input-number v-model="scoreForm.variables.asset_size" :min="0" :step="0.1" :precision="2" style="width:100%" /></el-form-item>
      </el-form>
      <el-result v-if="scoreResult" :icon="scoreResult.probability<0.05?'success':scoreResult.probability<0.2?'warning':'error'" :title="'评级: ' + scoreResult.ratingGrade" :sub-title="'违约概率PD: ' + (scoreResult.probability*100).toFixed(2) + '% | 风险等级: ' + scoreResult.scoreLevel">
        <template #extra><el-descriptions :column="2" border size="small">
          <el-descriptions-item label="原始分数">{{ scoreResult.rawScore }}</el-descriptions-item>
          <el-descriptions-item label="评分模型">{{ scoreResult.modelId }}</el-descriptions-item>
        </el-descriptions></template>
      </el-result>
      <template #footer><el-button @click="scoreVisible=false">关闭</el-button><el-button type="primary" @click="submitScore">计算评分</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const queryParams = ref({ pageNum: 1, pageSize: 20, modelName: '', modelType: '' })
const dialogVisible = ref(false)
const dialogTitle = ref('')
const form = ref({})
const formRef = ref(null)
const scoreVisible = ref(false)
const scoreResult = ref(null)
const scoreForm = ref({ companyId: 1, companyName: '', loanId: 1, variables: { debt_ratio: 0.65, current_ratio: 1.2, profit_margin: 0.08, overdue_days: 0, company_age: 5, asset_size: 8.5 } })

const rules = { modelName: [{ required: true, message: '请输入模型名称', trigger: 'blur' }], modelType: [{ required: true, message: '请选择模型类型', trigger: 'change' }] }

const stats = computed(() => {
  const list = tableData.value
  return {
    total: list.length,
    active: list.filter(i => i.status === '1').length,
    scorecard: list.filter(i => i.modelType === 'SCORECARD').length,
    logistic: list.filter(i => i.modelType === 'LOGISTIC').length
  }
})

async function loadData() {
  loading.value = true
  try {
    const res = await request({ url: '/pd/model/list', method: 'get', params: queryParams.value })
    tableData.value = res.rows || []
    total.value = res.total || 0
  } finally { loading.value = false }
}

function resetQuery() { queryParams.value = { pageNum: 1, pageSize: 20, modelName: '', modelType: '' }; loadData() }
function handleAdd() { form.value = { modelName: '', modelType: 'SCORECARD', modelVersion: '1.0', aucRoc: null, ksStatistic: null, gini: null, sampleSize: null, cutoffScore: null, coefficientJson: '', remark: '' }; dialogTitle.value = '新增PD模型'; dialogVisible.value = true }
function handleEdit(row) { form.value = { ...row }; dialogTitle.value = '编辑PD模型'; dialogVisible.value = true }
function handleScore(row) { scoreResult.value = null; scoreForm.value.modelId = row.modelId; scoreVisible.value = true }

async function handleActivate(row) {
  await request({ url: `/pd/model/${row.modelId}/activate`, method: 'post' })
  ElMessage.success('已启用')
  loadData()
}

async function handleDelete(row) {
  await ElMessageBox.confirm('确认删除该模型？', '提示')
  await request({ url: `/pd/model/${row.modelId}`, method: 'delete' })
  ElMessage.success('删除成功')
  loadData()
}

async function submitForm() {
  if (form.value.modelId) {
    await request({ url: '/pd/model', method: 'put', data: form.value })
  } else {
    await request({ url: '/pd/model', method: 'post', data: form.value })
  }
  ElMessage.success('保存成功')
  dialogVisible.value = false
  loadData()
}

async function submitScore() {
  const res = await request({ url: '/pd/model/score', method: 'post', data: scoreForm.value })
  scoreResult.value = res.data
}

onMounted(loadData)
</script>

<style scoped>
.mb20 { margin-bottom: 20px; }
.mt16 { margin-top: 16px; }
.stat-item { text-align: center; }
.stat-value { font-size: 24px; font-weight: bold; color: #409eff; }
.stat-label { font-size: 14px; color: #909399; margin-top: 8px; }
</style>

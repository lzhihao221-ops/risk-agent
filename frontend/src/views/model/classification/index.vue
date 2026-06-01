<template>
  <div class="app-container">
    <el-row :gutter="20" class="mb20">
      <el-col :span="6"><el-card shadow="hover"><div class="stat-item"><div class="stat-value">{{ stats.total }}</div><div class="stat-label">规则总数</div></div></el-card></el-col>
      <el-col :span="6"><el-card shadow="hover"><div class="stat-item"><div class="stat-value">{{ stats.overdue }}</div><div class="stat-label">逾期规则</div></div></el-card></el-col>
      <el-col :span="6"><el-card shadow="hover"><div class="stat-item"><div class="stat-value">{{ stats.payment }}</div><div class="stat-label">还款规则</div></div></el-card></el-col>
      <el-col :span="6"><el-card shadow="hover"><div class="stat-item"><div class="stat-value">{{ stats.debt }}</div><div class="stat-label">财务规则</div></div></el-card></el-col>
    </el-row>

    <el-tabs v-model="activeTab" class="mb20">
      <el-tab-pane label="分类规则" name="rules">
        <el-form :model="queryParams" :inline="true" class="mb20">
          <el-form-item label="规则名称"><el-input v-model="queryParams.ruleName" placeholder="请输入" clearable /></el-form-item>
          <el-form-item label="规则类型"><el-select v-model="queryParams.ruleType" placeholder="全部" clearable><el-option label="逾期天数" value="OVERDUE_DAYS" /><el-option label="还款状态" value="PAYMENT_STATUS" /><el-option label="财务指标" value="DEBT_RATIO" /></el-select></el-form-item>
          <el-form-item><el-button type="primary" @click="loadRules">搜索</el-button><el-button @click="resetQuery">重置</el-button></el-form-item>
        </el-form>
        <el-row :gutter="10" class="mb20"><el-col :span="1.5"><el-button type="primary" @click="handleAddRule">新增规则</el-button></el-col></el-row>
        <el-table :data="ruleData" v-loading="loading" stripe border>
          <el-table-column label="规则ID" prop="ruleId" width="80" />
          <el-table-column label="规则名称" prop="ruleName" min-width="200" />
          <el-table-column label="类型" prop="ruleType" width="120">
            <template #default="{ row }"><el-tag :type="row.ruleType==='OVERDUE_DAYS'?'primary':row.ruleType==='PAYMENT_STATUS'?'warning':'success'">{{ {OVERDUE_DAYS:'逾期天数',PAYMENT_STATUS:'还款状态',DEBT_RATIO:'财务指标'}[row.ruleType] }}</el-tag></template>
          </el-table-column>
          <el-table-column label="五级分类" prop="fiveLevel" width="100">
            <template #default="{ row }"><el-tag :type="{'正常':'success','关注':'','次级':'warning','可疑':'danger','损失':'danger'}[row.fiveLevel]">{{ row.fiveLevel }}</el-tag></template>
          </el-table-column>
          <el-table-column label="优先级" prop="priority" width="80" />
          <el-table-column label="条件" prop="conditionJson" min-width="200" show-overflow-tooltip />
          <el-table-column label="状态" prop="status" width="80">
            <template #default="{ row }"><el-tag :type="row.status==='1'?'success':'info'">{{ row.status==='1'?'启用':'停用' }}</el-tag></template>
          </el-table-column>
          <el-table-column label="操作" width="160" fixed="right">
            <template #default="{ row }">
              <el-button type="warning" link @click="handleEditRule(row)">编辑</el-button>
              <el-button type="danger" link @click="handleDeleteRule(row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <el-tab-pane label="五级分类操作" name="classify">
        <el-card>
          <el-form :model="classifyForm" label-width="100px" style="max-width:600px">
            <el-form-item label="贷款ID"><el-input-number v-model="classifyForm.loanId" style="width:100%" /></el-form-item>
            <el-form-item label="企业ID"><el-input-number v-model="classifyForm.companyId" style="width:100%" /></el-form-item>
            <el-form-item label="企业名称"><el-input v-model="classifyForm.companyName" /></el-form-item>
            <el-form-item label="逾期天数"><el-input-number v-model="classifyForm.overdueDays" :min="0" style="width:100%" /></el-form-item>
            <el-form-item label="未偿余额"><el-input-number v-model="classifyForm.outstandingBalance" :min="0" :step="10000" style="width:100%" /></el-form-item>
            <el-form-item label="资产负债率"><el-input-number v-model="classifyForm.debtRatio" :min="0" :max="200" :step="1" style="width:100%" /></el-form-item>
            <el-form-item><el-button type="primary" @click="runClassify">执行分类</el-button></el-form-item>
          </el-form>
          <el-result v-if="classifyResult" :icon="classifyResult==='正常'?'success':classifyResult==='关注'?'warning':'error'" :title="'五级分类结果: ' + classifyResult" />
        </el-card>
      </el-tab-pane>

      <el-tab-pane label="迁移记录" name="migration">
        <el-table :data="migrationData" stripe border>
          <el-table-column label="迁移ID" prop="migrationId" width="80" />
          <el-table-column label="贷款ID" prop="loanId" width="100" />
          <el-table-column label="企业名称" prop="companyName" min-width="150" />
          <el-table-column label="原分类" prop="fromLevel" width="100">
            <template #default="{ row }"><el-tag>{{ row.fromLevel }}</el-tag></template>
          </el-table-column>
          <el-table-column label="新分类" prop="toLevel" width="100">
            <template #default="{ row }"><el-tag :type="row.toLevel==='正常'?'success':row.toLevel==='关注'?'':'warning'">{{ row.toLevel }}</el-tag></template>
          </el-table-column>
          <el-table-column label="迁移日期" prop="migrateDate" width="120" />
          <el-table-column label="原因" prop="reason" min-width="200" />
        </el-table>
      </el-tab-pane>
    </el-tabs>

    <el-pagination class="mt16" v-model:current-page="queryParams.pageNum" v-model:page-size="queryParams.pageSize" :total="total" :page-sizes="[20,50,100]" layout="total, sizes, prev, pager, next" @change="loadRules" />

    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="600px">
      <el-form :model="ruleForm" :rules="ruleRules" ref="ruleFormRef" label-width="100px">
        <el-form-item label="规则名称" prop="ruleName"><el-input v-model="ruleForm.ruleName" /></el-form-item>
        <el-form-item label="规则类型" prop="ruleType"><el-select v-model="ruleForm.ruleType" style="width:100%"><el-option label="逾期天数" value="OVERDUE_DAYS" /><el-option label="还款状态" value="PAYMENT_STATUS" /><el-option label="财务指标" value="DEBT_RATIO" /></el-select></el-form-item>
        <el-form-item label="五级分类" prop="fiveLevel"><el-select v-model="ruleForm.fiveLevel" style="width:100%"><el-option label="正常" value="正常" /><el-option label="关注" value="关注" /><el-option label="次级" value="次级" /><el-option label="可疑" value="可疑" /><el-option label="损失" value="损失" /></el-select></el-form-item>
        <el-form-item label="优先级"><el-input-number v-model="ruleForm.priority" :min="0" :max="100" style="width:100%" /></el-form-item>
        <el-form-item label="条件JSON"><el-input v-model="ruleForm.conditionJson" type="textarea" :rows="3" placeholder='如 {"operator":"between","min":1,"max":30}' /></el-form-item>
        <el-form-item label="描述"><el-input v-model="ruleForm.description" type="textarea" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="submitRule">确定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

const activeTab = ref('rules')
const loading = ref(false)
const ruleData = ref([])
const migrationData = ref([])
const total = ref(0)
const queryParams = ref({ pageNum: 1, pageSize: 20, ruleName: '', ruleType: '' })
const dialogVisible = ref(false)
const dialogTitle = ref('')
const ruleForm = ref({})
const ruleFormRef = ref(null)
const classifyForm = ref({ loanId: 1, companyId: 1, companyName: '', overdueDays: 0, outstandingBalance: 0, debtRatio: 50 })
const classifyResult = ref(null)
const ruleRules = { ruleName: [{ required: true, message: '请输入', trigger: 'blur' }], ruleType: [{ required: true, message: '请选择', trigger: 'change' }], fiveLevel: [{ required: true, message: '请选择', trigger: 'change' }] }

const stats = computed(() => {
  const list = ruleData.value
  return { total: list.length, overdue: list.filter(i => i.ruleType === 'OVERDUE_DAYS').length, payment: list.filter(i => i.ruleType === 'PAYMENT_STATUS').length, debt: list.filter(i => i.ruleType === 'DEBT_RATIO').length }
})

async function loadRules() {
  loading.value = true
  try {
    const res = await request({ url: '/classification/rule/list', method: 'get', params: queryParams.value })
    ruleData.value = res.rows || []
    total.value = res.total || 0
  } finally { loading.value = false }
}

async function loadMigrations() {
  const res = await request({ url: '/classification/migration/list', method: 'get' })
  migrationData.value = res.rows || []
}

function resetQuery() { queryParams.value = { pageNum: 1, pageSize: 20, ruleName: '', ruleType: '' }; loadRules() }
function handleAddRule() { ruleForm.value = { ruleName: '', ruleType: 'OVERDUE_DAYS', fiveLevel: '正常', priority: 50, conditionJson: '', description: '', status: '1' }; dialogTitle.value = '新增分类规则'; dialogVisible.value = true }
function handleEditRule(row) { ruleForm.value = { ...row }; dialogTitle.value = '编辑分类规则'; dialogVisible.value = true }

async function handleDeleteRule(row) {
  await ElMessageBox.confirm('确认删除？', '提示')
  await request({ url: `/classification/rule/${row.ruleId}`, method: 'delete' })
  ElMessage.success('删除成功')
  loadRules()
}

async function submitRule() {
  if (ruleForm.value.ruleId) { await request({ url: '/classification/rule', method: 'put', data: ruleForm.value }) }
  else { await request({ url: '/classification/rule', method: 'post', data: ruleForm.value }) }
  ElMessage.success('保存成功')
  dialogVisible.value = false
  loadRules()
}

async function runClassify() {
  const res = await request({ url: '/classification/classify', method: 'post', data: classifyForm.value })
  classifyResult.value = res.data
}

onMounted(() => { loadRules(); loadMigrations() })
</script>

<style scoped>
.mb20 { margin-bottom: 20px; }
.mt16 { margin-top: 16px; }
.stat-item { text-align: center; }
.stat-value { font-size: 24px; font-weight: bold; color: #409eff; }
.stat-label { font-size: 14px; color: #909399; margin-top: 8px; }
</style>

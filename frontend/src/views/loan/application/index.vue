<template>
  <div class="app-container">
    <!-- 统计卡片 -->
    <el-row :gutter="16" style="margin-bottom:16px">
      <el-col :span="4" v-for="card in statCards" :key="card.label">
        <el-card shadow="hover" class="stat-card" :body-style="{padding:'16px'}">
          <div class="card-label">{{ card.label }}</div>
          <div class="card-value" :style="{color:card.color}">{{ card.value }}</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 搜索 + 操作 -->
    <el-card>
      <template #header>
        <div style="display:flex;justify-content:space-between;align-items:center">
          <span>贷款申请列表</span>
          <el-button type="primary" @click="showAdd"><el-icon><Plus /></el-icon> 新增申请</el-button>
        </div>
      </template>
      <el-form :inline="true" style="margin-bottom:16px">
        <el-form-item label="企业名称"><el-input v-model="query.companyName" clearable placeholder="搜索" /></el-form-item>
        <el-form-item label="状态">
          <el-select v-model="query.appStatus" clearable placeholder="全部" style="width:120px">
            <el-option v-for="s in statusOptions" :key="s.value" :label="s.label" :value="s.value" />
          </el-select>
        </el-form-item>
        <el-form-item><el-button type="primary" @click="loadData">查询</el-button></el-form-item>
      </el-form>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column label="申请编号" prop="appNo" width="140" />
        <el-table-column label="企业名称" prop="companyName" show-overflow-tooltip />
        <el-table-column label="产品类型" prop="productType" width="120" />
        <el-table-column label="申请金额(万)" prop="loanAmount" width="110" align="right" />
        <el-table-column label="期限(月)" prop="loanTerm" width="80" align="center" />
        <el-table-column label="担保方式" prop="guaranteeType" width="90" />
        <el-table-column label="风险评分" width="90" align="center">
          <template #default="{row}">
            <span :style="{color: row.riskLevel===3?'#F56C6C':row.riskLevel===2?'#E6A23C':'#67C23A', fontWeight:'bold'}">{{ row.riskScore || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="90">
          <template #default="{row}">
            <el-tag :type="statusTag(row.appStatus)" size="small">{{ statusLabel(row.appStatus) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180">
          <template #default="{row}">
            <el-button text type="primary" size="small" @click="viewDetail(row)">详情</el-button>
            <el-button text type="success" size="small" @click="viewApproval(row)" v-if="row.appStatus>=2">审批</el-button>
            <el-button text type="danger" size="small" @click="handleDelete(row)" v-if="row.appStatus<=1">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top:16px;justify-content:flex-end" v-model:current-page="query.pageNum" v-model:page-size="query.pageSize" :total="total" layout="total,prev,pager,next" @current-change="loadData" />
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑申请' : '新增贷款申请'" width="600px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="企业"><el-input v-model="form.companyName" /></el-form-item>
        <el-form-item label="产品类型">
          <el-select v-model="form.productType" style="width:100%">
            <el-option v-for="p in productTypes" :key="p" :label="p" :value="p" />
          </el-select>
        </el-form-item>
        <el-form-item label="申请金额(万)"><el-input-number v-model="form.loanAmount" :min="0" :step="10" style="width:100%" /></el-form-item>
        <el-form-item label="期限(月)"><el-input-number v-model="form.loanTerm" :min="1" :max="360" style="width:100%" /></el-form-item>
        <el-form-item label="利率(%)"><el-input-number v-model="form.interestRate" :min="0" :step="0.01" :precision="2" style="width:100%" /></el-form-item>
        <el-form-item label="还款方式">
          <el-select v-model="form.repaymentType" style="width:100%">
            <el-option v-for="r in repaymentTypes" :key="r" :label="r" :value="r" />
          </el-select>
        </el-form-item>
        <el-form-item label="担保方式">
          <el-select v-model="form.guaranteeType" style="width:100%">
            <el-option v-for="g in guaranteeTypes" :key="g" :label="g" :value="g" />
          </el-select>
        </el-form-item>
        <el-form-item label="贷款用途"><el-input v-model="form.loanPurpose" type="textarea" /></el-form-item>
        <el-form-item label="抵押物描述"><el-input v-model="form.collateralDesc" type="textarea" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="submitForm">提交</el-button>
      </template>
    </el-dialog>

    <!-- 审批弹窗 -->
    <el-dialog v-model="approvalVisible" title="审批记录" width="600px">
      <el-timeline>
        <el-timeline-item v-for="a in approvals" :key="a.id" :timestamp="a.approveTime" placement="top" :type="a.approveResult===1?'success':a.approveResult===2?'danger':'primary'">
          <el-card>
            <div><strong>{{ a.approveRole }}</strong> - {{ a.approveUser }}</div>
            <div style="margin-top:4px">
              <el-tag :type="a.approveResult===1?'success':a.approveResult===2?'danger':'info'" size="small">
                {{ a.approveResult===1?'通过':a.approveResult===2?'拒绝':'退回' }}
              </el-tag>
            </div>
            <div style="margin-top:8px;color:#606266">{{ a.approveOpinion }}</div>
          </el-card>
        </el-timeline-item>
      </el-timeline>
      <el-empty v-if="approvals.length===0" description="暂无审批记录" />
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { listApplication, addApplication, updateApplication, deleteApplication, applicationStatusStats, getApprovals } from '@/api/loan'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'

const query = ref({ companyName: '', appStatus: null, pageNum: 1, pageSize: 20 })
const tableData = ref([])
const total = ref(0)
const loading = ref(false)
const dialogVisible = ref(false)
const approvalVisible = ref(false)
const approvals = ref([])
const form = ref({})

const statCards = ref([])
const statusOptions = [
  { label: '草稿', value: 0 }, { label: '已提交', value: 1 }, { label: '审批中', value: 2 },
  { label: '已批准', value: 3 }, { label: '已拒绝', value: 4 }, { label: '已放款', value: 5 }
]
const productTypes = ['流动资金贷款', '固定资产贷款', '贸易融资', '银承', '保函']
const repaymentTypes = ['等额本息', '等额本金', '先息后本', '到期还本付息']
const guaranteeTypes = ['信用', '保证', '抵押', '质押', '组合']

function statusTag(s) { return { 0:'info', 1:'', 2:'warning', 3:'success', 4:'danger', 5:'success' }[s] || 'info' }
function statusLabel(s) { return statusOptions.find(o => o.value === s)?.label || '未知' }

async function loadData() {
  loading.value = true
  try {
    const res = await listApplication(query.value)
    tableData.value = res.rows || []
    total.value = res.total || 0
    const stats = await applicationStatusStats()
    const statsData = stats.data || stats || []
    statCards.value = statusOptions.map(s => ({
      label: s.label,
      value: (statsData.find(d => d.status === s.value) || {}).count || 0,
      color: { 0:'#909399', 1:'#409EFF', 2:'#E6A23C', 3:'#67C23A', 4:'#F56C6C', 5:'#67C23A' }[s.value]
    }))
  } catch(e) { console.error(e) }
  loading.value = false
}

function showAdd() {
  form.value = { productType: '流动资金贷款', repaymentType: '先息后本', guaranteeType: '抵押', appStatus: 0 }
  dialogVisible.value = true
}

async function submitForm() {
  try {
    if (form.value.id) await updateApplication(form.value)
    else await addApplication(form.value)
    ElMessage.success('操作成功')
    dialogVisible.value = false
    loadData()
  } catch(e) { ElMessage.error('操作失败') }
}

async function viewDetail(row) {
  form.value = { ...row }
  dialogVisible.value = true
}

async function viewApproval(row) {
  try {
    const res = await getApprovals(row.id)
    approvals.value = res.data || res || []
    approvalVisible.value = true
  } catch(e) { console.error(e) }
}

async function handleDelete(row) {
  await ElMessageBox.confirm('确定删除？', '提示', { type: 'warning' })
  await deleteApplication(row.id)
  ElMessage.success('已删除')
  loadData()
}

onMounted(loadData)
</script>

<style scoped>
.stat-card { text-align: center; cursor: pointer; }
.stat-card:hover { transform: translateY(-2px); }
.card-label { font-size: 13px; color: #909399; margin-bottom: 4px; }
.card-value { font-size: 24px; font-weight: 600; }
</style>

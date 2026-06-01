<template>
  <div class="app-container">
    <!-- 统计 -->
    <el-row :gutter="16" style="margin-bottom:16px">
      <el-col :span="6">
        <el-card shadow="hover"><el-statistic title="企业总数" :value="summary.companyCount || 0" /></el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover"><el-statistic title="高风险企业" :value="summary.highRiskCount || 0" /></el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover"><el-statistic title="预警企业" :value="summary.alertCompanyCount || 0" /></el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover"><el-statistic title="事件总数" :value="summary.eventCount || 0" /></el-card>
      </el-col>
    </el-row>

    <!-- 搜索 + 操作 -->
    <el-card>
      <template #header>
        <div style="display:flex;justify-content:space-between;align-items:center">
          <span>企业列表</span>
          <div>
            <el-button @click="handleExport"><el-icon><Download /></el-icon> 导出</el-button>
            <el-button type="primary" @click="showAdd"><el-icon><Plus /></el-icon> 新增企业</el-button>
          </div>
        </div>
      </template>
      <el-form :inline="true" style="margin-bottom:16px">
        <el-form-item label="企业名称"><el-input v-model="queryParams.companyName" clearable placeholder="搜索" /></el-form-item>
        <el-form-item label="风险等级">
          <el-select v-model="queryParams.riskLevel" clearable placeholder="全部" style="width:100px">
            <el-option label="高" :value="3" /><el-option label="中" :value="2" /><el-option label="低" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" clearable placeholder="全部" style="width:100px">
            <el-option label="正常" :value="1" /><el-option label="预警" :value="2" /><el-option label="冻结" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData"><el-icon><Search /></el-icon>搜索</el-button>
          <el-button @click="handleRefresh"><el-icon><Refresh /></el-icon>重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column label="企业名称" prop="companyName" width="220" show-overflow-tooltip>
          <template #default="{row}">
            <el-link type="primary" @click="viewDetail(row)">{{ row.companyName }}</el-link>
          </template>
        </el-table-column>
        <el-table-column label="统一社会信用代码" prop="creditCode" width="180" />
        <el-table-column label="行业" prop="industry" width="120" />
        <el-table-column label="风险等级" width="80">
          <template #default="{row}">
            <el-tag :type="row.riskLevel===3?'danger':row.riskLevel===2?'warning':'success'" size="small">
              {{ row.riskLevel===3?'高':row.riskLevel===2?'中':'低' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="80">
          <template #default="{row}">
            <el-tag :type="row.status===1?'success':row.status===2?'danger':'warning'" size="small">
              {{ row.status===1?'正常':row.status===2?'预警':'冻结' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="贷款余额(万)" width="110" align="right">
          <template #default="{row}">{{ (row.loanBalance || 0).toFixed(2) }}</template>
        </el-table-column>
        <el-table-column label="最近事件" width="120" show-overflow-tooltip>
          <template #default="{row}">{{ row.lastEvent || '-' }}</template>
        </el-table-column>
        <el-table-column label="操作" width="160">
          <template #default="{row}">
            <el-button text type="primary" size="small" @click="viewDetail(row)">详情</el-button>
            <el-button text type="success" size="small" @click="showEdit(row)">编辑</el-button>
            <el-button text type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top:16px;justify-content:flex-end" v-model:current-page="queryParams.pageNum" v-model:page-size="queryParams.pageSize" :total="total" layout="total,prev,pager,next" @current-change="loadData" />
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑企业' : '新增企业'" width="600px">
      <el-form :model="form" label-width="140px">
        <el-form-item label="企业名称"><el-input v-model="form.companyName" /></el-form-item>
        <el-form-item label="统一社会信用代码"><el-input v-model="form.creditCode" /></el-form-item>
        <el-form-item label="法定代表人"><el-input v-model="form.legalPerson" /></el-form-item>
        <el-form-item label="注册地址"><el-input v-model="form.address" /></el-form-item>
        <el-form-item label="联系电话"><el-input v-model="form.phone" /></el-form-item>
        <el-form-item label="行业">
          <el-select v-model="form.industry" style="width:100%">
            <el-option v-for="i in industries" :key="i" :label="i" :value="i" />
          </el-select>
        </el-form-item>
        <el-form-item label="注册资本(万)"><el-input-number v-model="form.registeredCapital" :min="0" style="width:100%" /></el-form-item>
        <el-form-item label="贷款余额(万)"><el-input-number v-model="form.loanBalance" :min="0" style="width:100%" /></el-form-item>
        <el-form-item label="风险等级">
          <el-radio-group v-model="form.riskLevel">
            <el-radio :value="1">低</el-radio><el-radio :value="2">中</el-radio><el-radio :value="3">高</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :value="1">正常</el-radio><el-radio :value="2">预警</el-radio><el-radio :value="3">冻结</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="submitForm">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getCompanyList, addCompany, updateCompany, deleteCompany, getSummary } from '@/api/risk'
import { download } from '@/utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search, Refresh, Download } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const queryParams = ref({ companyName: '', riskLevel: null, status: null, pageNum: 1, pageSize: 20 })
const tableData = ref([])
const total = ref(0)
const loading = ref(false)
const dialogVisible = ref(false)
const form = ref({})
const summary = ref({})

const industries = ['制造业', '房地产', '零售业', '科技', '农业', '建筑业', '金融业', '服务业', '能源', '交通运输']

async function loadData() {
  loading.value = true
  try {
    const res = await getCompanyList(queryParams.value)
    tableData.value = res.rows || []
    total.value = res.total || 0
  } catch(e) { console.error(e) }
  loading.value = false
}

async function loadSummary() {
  try {
    const res = await getSummary()
    summary.value = res.data || res || {}
  } catch(e) { console.error(e) }
}

function showAdd() {
  form.value = { riskLevel: 1, status: 1 }
  dialogVisible.value = true
}

function showEdit(row) {
  form.value = { ...row }
  dialogVisible.value = true
}

async function submitForm() {
  try {
    if (form.value.id) await updateCompany(form.value)
    else await addCompany(form.value)
    ElMessage.success('操作成功')
    dialogVisible.value = false
    loadData()
    loadSummary()
  } catch(e) { ElMessage.error('操作失败') }
}

async function handleDelete(row) {
  await ElMessageBox.confirm('确定删除？', '提示', { type: 'warning' })
  await deleteCompany(row.id)
  ElMessage.success('已删除')
  loadData()
  loadSummary()
}

function viewDetail(row) {
  router.push(`/risk/company/detail/${row.id}`)
}

function handleRefresh() {
  queryParams.value = { companyName: '', riskLevel: null, status: null, pageNum: 1, pageSize: 20 }
  loadData()
}

function handleExport() {
  download('/risk/company/export', { ...queryParams }, `企业数据_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  loadData()
  loadSummary()
})
</script>

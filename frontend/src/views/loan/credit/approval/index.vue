<template>
  <div class="app-container">
    <el-card>
      <template #header>
        <div style="display:flex;justify-content:space-between;align-items:center">
          <span>授信审批</span>
          <el-button type="primary" @click="showAdd"><el-icon><Plus /></el-icon> 新增审批</el-button>
        </div>
      </template>
      <el-form :inline="true" style="margin-bottom:16px">
        <el-form-item label="申请编号">
          <el-input v-model="query.appNo" clearable placeholder="搜索编号" />
        </el-form-item>
        <el-form-item label="企业名称">
          <el-input v-model="query.companyName" clearable placeholder="搜索企业" />
        </el-form-item>
        <el-form-item label="审批状态">
          <el-select v-model="query.approvalStatus" clearable placeholder="全部" style="width:130px">
            <el-option v-for="s in statusOptions" :key="s.value" :label="s.label" :value="s.value" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData">查询</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column label="申请编号" prop="appNo" width="140" />
        <el-table-column label="企业名称" prop="companyName" show-overflow-tooltip />
        <el-table-column label="授信额度(万)" prop="creditLine" width="120" align="right" />
        <el-table-column label="期限(月)" prop="creditPeriod" width="90" align="center" />
        <el-table-column label="利率(%)" width="90" align="center">
          <template #default="{row}">{{ row.creditRate ? Number(row.creditRate).toFixed(2) + '%' : '-' }}</template>
        </el-table-column>
        <el-table-column label="审批状态" width="120" align="center">
          <template #default="{row}">
            <el-tag :type="statusTag(row.approvalStatus)" size="small">{{ statusLabel(row.approvalStatus) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="审批人" prop="approveUser" width="90" />
        <el-table-column label="操作" width="180">
          <template #default="{row}">
            <el-button text type="primary" size="small" @click="viewDetail(row)">详情</el-button>
            <el-button text type="success" size="small" @click="showApprove(row)" v-if="row.approvalStatus===0">审批</el-button>
            <el-button text type="danger" size="small" @click="handleDelete(row)" v-if="row.approvalStatus===0">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top:16px;justify-content:flex-end" v-model:current-page="query.pageNum" v-model:page-size="query.pageSize" :total="total" layout="total,prev,pager,next" @current-change="loadData" />
    </el-card>

    <!-- 新增/详情弹窗 -->
    <el-dialog v-model="dialogVisible" :title="isDetail ? '审批详情' : '新增授信审批'" width="600px">
      <el-form :model="form" label-width="100px" :disabled="isDetail">
        <el-form-item label="贷款申请ID"><el-input-number v-model="form.appId" :min="1" style="width:100%" /></el-form-item>
        <el-form-item label="申请编号"><el-input v-model="form.appNo" /></el-form-item>
        <el-form-item label="企业名称"><el-input v-model="form.companyName" /></el-form-item>
        <el-form-item label="授信额度(万)"><el-input-number v-model="form.creditLine" :min="0" :step="10" style="width:100%" /></el-form-item>
        <el-form-item label="期限(月)"><el-input-number v-model="form.creditPeriod" :min="1" :max="360" style="width:100%" /></el-form-item>
        <el-form-item label="利率(%)"><el-input-number v-model="form.creditRate" :min="0" :step="0.01" :precision="4" style="width:100%" /></el-form-item>
        <el-form-item label="授信条件"><el-input v-model="form.creditCondition" type="textarea" /></el-form-item>
        <el-form-item label="授信到期日"><el-date-picker v-model="form.expireDate" type="date" style="width:100%" /></el-form-item>
      </el-form>
      <template #footer v-if="!isDetail">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>

    <!-- 审批弹窗 -->
    <el-dialog v-model="approveVisible" title="授信审批" width="500px">
      <el-form :model="approveForm" label-width="80px">
        <el-form-item label="审批结果">
          <el-radio-group v-model="approveForm.approvalStatus">
            <el-radio :value="1">通过</el-radio>
            <el-radio :value="2">拒绝</el-radio>
            <el-radio :value="3">有条件通过</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审批人"><el-input v-model="approveForm.approveUser" /></el-form-item>
        <el-form-item label="审批意见"><el-input v-model="approveForm.approveOpinion" type="textarea" :rows="3" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="approveVisible = false">取消</el-button>
        <el-button type="primary" @click="submitApprove">提交审批</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { listCreditApproval, addCreditApproval, updateCreditApproval, deleteCreditApproval } from '@/api/loanCredit'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'

const query = ref({ appNo: '', companyName: '', approvalStatus: null, pageNum: 1, pageSize: 20 })
const tableData = ref([])
const total = ref(0)
const loading = ref(false)
const dialogVisible = ref(false)
const approveVisible = ref(false)
const isDetail = ref(false)
const form = ref({})
const approveForm = ref({})

const statusOptions = [
  { label: '待审', value: 0 },
  { label: '通过', value: 1 },
  { label: '拒绝', value: 2 },
  { label: '有条件通过', value: 3 }
]

function statusTag(s) { return { 0:'info', 1:'success', 2:'danger', 3:'warning' }[s] || 'info' }
function statusLabel(s) { return statusOptions.find(o => o.value === s)?.label || '未知' }

async function loadData() {
  loading.value = true
  try {
    const res = await listCreditApproval(query.value)
    tableData.value = res.rows || []
    total.value = res.total || 0
  } catch(e) { console.error(e) }
  loading.value = false
}

function showAdd() {
  form.value = { approvalStatus: 0 }
  isDetail.value = false
  dialogVisible.value = true
}

function viewDetail(row) {
  form.value = { ...row }
  isDetail.value = true
  dialogVisible.value = true
}

function showApprove(row) {
  approveForm.value = { id: row.id, approvalStatus: 1, approveUser: '', approveOpinion: '' }
  approveVisible.value = true
}

async function submitForm() {
  try {
    await addCreditApproval(form.value)
    ElMessage.success('操作成功')
    dialogVisible.value = false
    loadData()
  } catch(e) { ElMessage.error('操作失败') }
}

async function submitApprove() {
  try {
    approveForm.value.approveTime = new Date().toISOString()
    await updateCreditApproval(approveForm.value)
    ElMessage.success('审批完成')
    approveVisible.value = false
    loadData()
  } catch(e) { ElMessage.error('审批失败') }
}

async function handleDelete(row) {
  await ElMessageBox.confirm('确定删除？', '提示', { type: 'warning' })
  await deleteCreditApproval(row.id)
  ElMessage.success('已删除')
  loadData()
}

onMounted(loadData)
</script>

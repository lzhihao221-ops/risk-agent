<template>
  <div class="app-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="mb20">
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value text-warning">{{ stats.pendingCount }}</div>
            <div class="stat-label">待放款笔数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value text-warning">{{ stats.pendingAmount }} 万元</div>
            <div class="stat-label">待放款金额</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value text-success">{{ stats.confirmedCount }}</div>
            <div class="stat-label">已放款笔数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value text-success">{{ stats.confirmedAmount }} 万元</div>
            <div class="stat-label">已放款金额</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 搜索区域 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="mb20">
      <el-form-item label="企业名称" prop="companyName">
        <el-input v-model="queryParams.companyName" placeholder="请输入企业名称" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="贷款编号" prop="loanNo">
        <el-input v-model="queryParams.loanNo" placeholder="请输入贷款编号" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option label="待审批" :value="0" />
          <el-option label="已审批" :value="1" />
          <el-option label="已放款" :value="2" />
          <el-option label="已驳回" :value="3" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <el-row :gutter="10" class="mb20">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd">新增放款</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete">删除</el-button>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="tableData" @selection-change="handleSelectionChange" border stripe>
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="贷款编号" prop="loanNo" width="150" />
      <el-table-column label="企业名称" prop="companyName" min-width="180" show-overflow-tooltip />
      <el-table-column label="放款金额(万元)" prop="disburseAmount" width="130" align="right" />
      <el-table-column label="放款日期" prop="disburseDate" width="120" align="center">
        <template #default="scope">
          {{ scope.row.disburseDate ? scope.row.disburseDate.substring(0, 10) : '-' }}
        </template>
      </el-table-column>
      <el-table-column label="放款类型" prop="disburseType" width="110" align="center" />
      <el-table-column label="放款方式" prop="disburseMethod" width="110" align="center" />
      <el-table-column label="状态" prop="status" width="100" align="center">
        <template #default="scope">
          <el-tag :type="statusTagType(scope.row.status)">{{ statusLabel(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作人" prop="operatorName" width="100" />
      <el-table-column label="操作" width="250" align="center" fixed="right">
        <template #default="scope">
          <template v-if="scope.row.status === 0">
            <el-button link type="success" icon="Check" @click="handleApprove(scope.row)">审批</el-button>
            <el-button link type="danger" icon="Close" @click="handleReject(scope.row)">驳回</el-button>
          </template>
          <template v-if="scope.row.status === 1">
            <el-button link type="primary" icon="Promotion" @click="handleConfirm(scope.row)">确认放款</el-button>
          </template>
          <el-button link type="warning" icon="Edit" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      class="mt20"
      v-show="total > 0"
      :total="total"
      :page-sizes="[10, 20, 50, 100]"
      v-model:current-page="queryParams.pageNum"
      v-model:page-size="queryParams.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="getList"
      @current-change="getList"
    />

    <!-- 新增/编辑放款弹窗 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="650px" append-to-body>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="贷款ID" prop="loanId">
              <el-input v-model="form.loanId" placeholder="请输入贷款ID" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="贷款编号" prop="loanNo">
              <el-input v-model="form.loanNo" placeholder="请输入贷款编号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="企业ID" prop="companyId">
              <el-input v-model="form.companyId" placeholder="请输入企业ID" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="企业名称" prop="companyName">
              <el-input v-model="form.companyName" placeholder="请输入企业名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="放款金额(万元)" prop="disburseAmount">
              <el-input-number v-model="form.disburseAmount" :min="0" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="放款日期" prop="disburseDate">
              <el-date-picker v-model="form.disburseDate" type="date" value-format="YYYY-MM-DD" placeholder="选择日期" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="放款类型" prop="disburseType">
              <el-select v-model="form.disburseType" placeholder="请选择" style="width:100%">
                <el-option label="一次性放款" value="一次性放款" />
                <el-option label="分次放款" value="分次放款" />
                <el-option label="循环贷款" value="循环贷款" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="放款方式" prop="disburseMethod">
              <el-select v-model="form.disburseMethod" placeholder="请选择" style="width:100%">
                <el-option label="自主支付" value="自主支付" />
                <el-option label="受托支付" value="受托支付" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="利率(%)" prop="interestRate">
              <el-input-number v-model="form.interestRate" :min="0" :step="0.01" :precision="4" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="期限(月)" prop="loanPeriod">
              <el-input-number v-model="form.loanPeriod" :min="1" :max="360" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="收款账户" prop="receiveAccount">
          <el-input v-model="form.receiveAccount" placeholder="请输入收款账户" />
        </el-form-item>
        <el-form-item label="收款银行" prop="receiveBank">
          <el-input v-model="form.receiveBank" placeholder="请输入收款银行" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </template>
    </el-dialog>

    <!-- 审批/驳回弹窗 -->
    <el-dialog :title="approveDialogTitle" v-model="approveDialogVisible" width="500px" append-to-body>
      <el-form ref="approveFormRef" :model="approveForm" :rules="approveRules" label-width="100px">
        <el-form-item label="审批意见" prop="approveOpinion">
          <el-input v-model="approveForm.approveOpinion" type="textarea" :rows="4" placeholder="请输入审批意见" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="approveDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitApprove">确 定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { listDisburse, addDisburse, updateDisburse, deleteDisburse, confirmDisburse } from '@/api/loanDisburse'

const tableData = ref([])
const loading = ref(false)
const total = ref(0)
const ids = ref([])
const multiple = ref(true)
const dialogVisible = ref(false)
const approveDialogVisible = ref(false)
const dialogTitle = ref('新增放款')
const approveDialogTitle = ref('审批放款')
const isApproveAction = ref(true)

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  companyName: undefined,
  loanNo: undefined,
  status: undefined
})

const form = ref({})
const approveForm = ref({})

const stats = computed(() => {
  const list = tableData.value
  const pending = list.filter(i => i.status === 0 || i.status === 1)
  const confirmed = list.filter(i => i.status === 2)
  return {
    pendingCount: pending.length,
    pendingAmount: pending.reduce((s, i) => s + Number(i.disburseAmount || 0), 0).toFixed(2),
    confirmedCount: confirmed.length,
    confirmedAmount: confirmed.reduce((s, i) => s + Number(i.disburseAmount || 0), 0).toFixed(2)
  }
})

const rules = {
  loanId: [{ required: true, message: '请输入贷款ID', trigger: 'blur' }],
  loanNo: [{ required: true, message: '请输入贷款编号', trigger: 'blur' }],
  companyName: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
  disburseAmount: [{ required: true, message: '请输入放款金额', trigger: 'blur' }],
  disburseDate: [{ required: true, message: '请选择放款日期', trigger: 'change' }],
  disburseType: [{ required: true, message: '请选择放款类型', trigger: 'change' }]
}

const approveRules = {
  approveOpinion: [{ required: true, message: '请输入审批意见', trigger: 'blur' }]
}

function statusLabel(status) {
  const map = { 0: '待审批', 1: '已审批', 2: '已放款', 3: '已驳回' }
  return map[status] || '未知'
}

function statusTagType(status) {
  const map = { 0: 'warning', 1: 'primary', 2: 'success', 3: 'danger' }
  return map[status] || 'info'
}

async function getList() {
  loading.value = true
  try {
    const res = await listDisburse(queryParams)
    tableData.value = res.rows || []
    total.value = res.total || 0
  } finally {
    loading.value = false
  }
}

function handleQuery() {
  queryParams.pageNum = 1
  getList()
}

function resetQuery() {
  queryParams.companyName = undefined
  queryParams.loanNo = undefined
  queryParams.status = undefined
  handleQuery()
}

function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id)
  multiple.value = !selection.length
}

function handleAdd() {
  dialogTitle.value = '新增放款'
  form.value = { status: 0, disburseAmount: 0, interestRate: 0, loanPeriod: 12 }
  dialogVisible.value = true
}

function handleEdit(row) {
  dialogTitle.value = '编辑放款'
  form.value = { ...row }
  dialogVisible.value = true
}

function handleApprove(row) {
  approveDialogTitle.value = '审批放款'
  isApproveAction.value = true
  approveForm.value = { id: row.id, approveOpinion: '' }
  approveDialogVisible.value = true
}

function handleReject(row) {
  approveDialogTitle.value = '驳回放款'
  isApproveAction.value = false
  approveForm.value = { id: row.id, approveOpinion: '' }
  approveDialogVisible.value = true
}

async function handleConfirm(row) {
  await ElMessageBox.confirm('确认执行放款操作？', '提示', { type: 'warning' })
  await confirmDisburse(row.id)
  ElMessage.success('放款确认成功')
  getList()
}

function handleDelete(row) {
  const deleteIds = row.id ? [row.id] : ids.value
  ElMessageBox.confirm('确认删除所选放款记录？', '提示', { type: 'warning' }).then(async () => {
    await deleteDisburse(deleteIds.join(','))
    ElMessage.success('删除成功')
    getList()
  })
}

async function submitForm() {
  if (form.value.id) {
    await updateDisburse(form.value)
    ElMessage.success('修改成功')
  } else {
    await addDisburse(form.value)
    ElMessage.success('新增成功')
  }
  dialogVisible.value = false
  getList()
}

async function submitApprove() {
  const data = { ...approveForm.value, status: isApproveAction.value ? 1 : 3 }
  await updateDisburse(data)
  ElMessage.success(isApproveAction.value ? '审批通过' : '已驳回')
  approveDialogVisible.value = false
  getList()
}

onMounted(() => {
  getList()
})
</script>

<style scoped>
.mb20 { margin-bottom: 20px; }
.mt20 { margin-top: 20px; }
.stat-item { text-align: center; padding: 10px 0; }
.stat-value { font-size: 28px; font-weight: bold; color: #409eff; }
.stat-value.text-warning { color: #e6a23c; }
.stat-value.text-success { color: #67c23a; }
.stat-label { font-size: 14px; color: #909399; margin-top: 8px; }
</style>

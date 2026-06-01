<template>
  <div class="app-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="mb20">
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value text-warning">{{ stats.pending }}</div>
            <div class="stat-label">待审批</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value text-success">{{ stats.approved }}</div>
            <div class="stat-label">已核销</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value">{{ stats.totalAmount }} 万元</div>
            <div class="stat-label">核销总金额</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value text-success">{{ stats.recoveryAmount }} 万元</div>
            <div class="stat-label">回收总金额</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 搜索区域 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="mb20">
      <el-form-item label="企业名称" prop="companyName">
        <el-input v-model="queryParams.companyName" placeholder="请输入企业名称" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="核销类型" prop="writeoffType">
        <el-select v-model="queryParams.writeoffType" placeholder="请选择" clearable>
          <el-option label="呆账核销" value="呆账核销" />
          <el-option label="账销案存" value="账销案存" />
          <el-option label="直接核销" value="直接核销" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择" clearable>
          <el-option label="待审批" :value="0" />
          <el-option label="已核销" :value="1" />
          <el-option label="已驳回" :value="2" />
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
        <el-button type="primary" plain icon="Plus" @click="handleAdd">新增核销</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete">删除</el-button>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="writeoffList" @selection-change="handleSelectionChange" border stripe>
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="贷款编号" prop="loanNo" width="150" />
      <el-table-column label="企业名称" prop="companyName" min-width="180" show-overflow-tooltip />
      <el-table-column label="核销金额(万元)" prop="writeoffAmount" width="130" align="right" />
      <el-table-column label="核销本金(万元)" prop="writeoffPrincipal" width="130" align="right" />
      <el-table-column label="核销利息(万元)" prop="writeoffInterest" width="130" align="right" />
      <el-table-column label="核销类型" prop="writeoffType" width="110" align="center" />
      <el-table-column label="核销原因" prop="writeoffReason" min-width="200" show-overflow-tooltip />
      <el-table-column label="审批人" prop="approveUser" width="100" />
      <el-table-column label="状态" prop="status" width="90" align="center">
        <template #default="scope">
          <el-tag :type="statusTagType(scope.row.status)">{{ statusLabel(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="回收(万元)" prop="recoverAmount" width="110" align="right">
        <template #default="scope">
          {{ scope.row.recoverAmount || 0 }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="250" align="center" fixed="right">
        <template #default="scope">
          <template v-if="scope.row.status === 0">
            <el-button link type="success" icon="Check" @click="handleApprove(scope.row)">审批</el-button>
            <el-button link type="danger" icon="Close" @click="handleReject(scope.row)">驳回</el-button>
          </template>
          <el-button link type="primary" icon="Money" @click="handleRecovery(scope.row)">更新回收</el-button>
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

    <!-- 新增核销弹窗 -->
    <el-dialog title="新增核销" v-model="dialogVisible" width="650px" append-to-body>
      <el-form ref="writeoffFormRef" :model="form" :rules="rules" label-width="120px">
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
            <el-form-item label="核销金额(万元)" prop="writeoffAmount">
              <el-input-number v-model="form.writeoffAmount" :min="0" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="核销本金(万元)" prop="writeoffPrincipal">
              <el-input-number v-model="form.writeoffPrincipal" :min="0" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="核销利息(万元)" prop="writeoffInterest">
              <el-input-number v-model="form.writeoffInterest" :min="0" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="核销类型" prop="writeoffType">
              <el-select v-model="form.writeoffType" placeholder="请选择" style="width:100%">
                <el-option label="呆账核销" value="呆账核销" />
                <el-option label="账销案存" value="账销案存" />
                <el-option label="直接核销" value="直接核销" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="核销日期" prop="writeoffDate">
              <el-date-picker v-model="form.writeoffDate" type="date" value-format="YYYY-MM-DD" placeholder="选择日期" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="五级分类" prop="fiveCategory">
              <el-select v-model="form.fiveCategory" placeholder="请选择" style="width:100%">
                <el-option label="正常" :value="1" />
                <el-option label="关注" :value="2" />
                <el-option label="次级" :value="3" />
                <el-option label="可疑" :value="4" />
                <el-option label="损失" :value="5" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="核销原因" prop="writeoffReason">
          <el-input v-model="form.writeoffReason" type="textarea" placeholder="请输入核销原因" />
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

    <!-- 更新回收金额弹窗 -->
    <el-dialog title="更新回收金额" v-model="recoveryDialogVisible" width="400px" append-to-body>
      <el-form ref="recoveryFormRef" :model="recoveryForm" label-width="100px">
        <el-form-item label="回收金额(万元)" prop="recoverAmount">
          <el-input-number v-model="recoveryForm.recoverAmount" :min="0" :precision="2" style="width:100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="recoveryDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitRecovery">确 定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  listWriteoff, getWriteoff, addWriteoff, updateWriteoff, delWriteoff,
  approveWriteoff, rejectWriteoff
} from '@/api/loanCollection'

const writeoffList = ref([])
const loading = ref(false)
const total = ref(0)
const ids = ref([])
const multiple = ref(true)
const dialogVisible = ref(false)
const approveDialogVisible = ref(false)
const recoveryDialogVisible = ref(false)
const approveDialogTitle = ref('审批核销')
const isApproveAction = ref(true)

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  companyName: undefined,
  writeoffType: undefined,
  status: undefined
})

const form = ref({})
const approveForm = ref({})
const recoveryForm = ref({})

const stats = computed(() => {
  const list = writeoffList.value
  return {
    pending: list.filter(i => i.status === 0).length,
    approved: list.filter(i => i.status === 1).length,
    totalAmount: list.filter(i => i.status === 1).reduce((s, i) => s + Number(i.writeoffAmount || 0), 0).toFixed(2),
    recoveryAmount: list.reduce((s, i) => s + Number(i.recoverAmount || 0), 0).toFixed(2)
  }
})

const rules = {
  loanId: [{ required: true, message: '请输入贷款ID', trigger: 'blur' }],
  loanNo: [{ required: true, message: '请输入贷款编号', trigger: 'blur' }],
  companyName: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
  writeoffAmount: [{ required: true, message: '请输入核销金额', trigger: 'blur' }],
  writeoffType: [{ required: true, message: '请选择核销类型', trigger: 'change' }]
}

const approveRules = {
  approveOpinion: [{ required: true, message: '请输入审批意见', trigger: 'blur' }]
}

function statusLabel(status) {
  const map = { 0: '待审批', 1: '已核销', 2: '已驳回' }
  return map[status] || '未知'
}

function statusTagType(status) {
  const map = { 0: 'warning', 1: 'success', 2: 'danger' }
  return map[status] || 'info'
}

async function getList() {
  loading.value = true
  try {
    const res = await listWriteoff(queryParams)
    writeoffList.value = res.rows || []
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
  queryParams.writeoffType = undefined
  queryParams.status = undefined
  handleQuery()
}

function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id)
  multiple.value = !selection.length
}

function handleAdd() {
  form.value = { status: 0 }
  dialogVisible.value = true
}

function handleApprove(row) {
  approveDialogTitle.value = '审批核销'
  isApproveAction.value = true
  approveForm.value = { id: row.id, approveOpinion: '' }
  approveDialogVisible.value = true
}

function handleReject(row) {
  approveDialogTitle.value = '驳回核销'
  isApproveAction.value = false
  approveForm.value = { id: row.id, approveOpinion: '' }
  approveDialogVisible.value = true
}

function handleRecovery(row) {
  recoveryForm.value = { id: row.id, recoverAmount: row.recoverAmount || 0 }
  recoveryDialogVisible.value = true
}

function handleDelete(row) {
  const deleteIds = row.id ? [row.id] : ids.value
  ElMessageBox.confirm('确认删除所选核销记录？', '提示', { type: 'warning' }).then(async () => {
    await delWriteoff(deleteIds.join(','))
    ElMessage.success('删除成功')
    getList()
  })
}

async function submitForm() {
  await addWriteoff(form.value)
  ElMessage.success('新增成功')
  dialogVisible.value = false
  getList()
}

async function submitApprove() {
  if (isApproveAction.value) {
    await approveWriteoff(approveForm.value.id, 'admin', approveForm.value.approveOpinion)
    ElMessage.success('审批通过')
  } else {
    await rejectWriteoff(approveForm.value.id, 'admin', approveForm.value.approveOpinion)
    ElMessage.success('已驳回')
  }
  approveDialogVisible.value = false
  getList()
}

async function submitRecovery() {
  const data = { id: recoveryForm.value.id, recoverAmount: recoveryForm.value.recoverAmount, status: 1 }
  await updateWriteoff(data)
  ElMessage.success('回收金额更新成功')
  recoveryDialogVisible.value = false
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

<template>
  <div class="app-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="mb20">
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value">{{ stats.preserving }}</div>
            <div class="stat-label">保全中笔数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value">{{ stats.completed }}</div>
            <div class="stat-label">已完成笔数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value">{{ stats.totalAmount }} 万元</div>
            <div class="stat-label">债权总金额</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value">{{ stats.recoveryAmount }} 万元</div>
            <div class="stat-label">已回收金额</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 搜索区域 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="mb20">
      <el-form-item label="企业名称" prop="companyName">
        <el-input v-model="queryParams.companyName" placeholder="请输入企业名称" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="保全方式" prop="preserveType">
        <el-select v-model="queryParams.preserveType" placeholder="请选择" clearable>
          <el-option label="诉讼保全" value="诉讼保全" />
          <el-option label="仲裁保全" value="仲裁保全" />
          <el-option label="协商保全" value="协商保全" />
          <el-option label="以物抵债" value="以物抵债" />
          <el-option label="债务重组" value="债务重组" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="preserveStatus">
        <el-select v-model="queryParams.preserveStatus" placeholder="请选择" clearable>
          <el-option label="申请中" :value="0" />
          <el-option label="已保全" :value="1" />
          <el-option label="执行中" :value="2" />
          <el-option label="已完成" :value="3" />
          <el-option label="已终止" :value="4" />
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
        <el-button type="primary" plain icon="Plus" @click="handleAdd">新增保全</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete">删除</el-button>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="assetList" @selection-change="handleSelectionChange" border stripe>
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="贷款编号" prop="loanNo" width="150" />
      <el-table-column label="企业名称" prop="companyName" min-width="180" show-overflow-tooltip />
      <el-table-column label="保全方式" prop="preserveType" width="110" align="center" />
      <el-table-column label="法院" prop="courtName" min-width="180" show-overflow-tooltip />
      <el-table-column label="案号" prop="caseNo" width="180" show-overflow-tooltip />
      <el-table-column label="债权金额(万元)" prop="claimAmount" width="130" align="right" />
      <el-table-column label="保全金额(万元)" prop="preserveAmount" width="130" align="right" />
      <el-table-column label="状态" prop="preserveStatus" width="90" align="center">
        <template #default="scope">
          <el-tag :type="preserveStatusType(scope.row.preserveStatus)">{{ preserveStatusLabel(scope.row.preserveStatus) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="已回收(万元)" prop="recoveryAmount" width="120" align="right">
        <template #default="scope">
          {{ scope.row.recoveryAmount || 0 }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="220" align="center" fixed="right">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)">编辑</el-button>
          <el-button link type="success" icon="Money" @click="handleRecovery(scope.row)">更新回收</el-button>
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

    <!-- 新增/编辑弹窗 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="700px" append-to-body>
      <el-form ref="assetFormRef" :model="form" :rules="rules" label-width="120px">
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
            <el-form-item label="保全方式" prop="preserveType">
              <el-select v-model="form.preserveType" placeholder="请选择" style="width:100%">
                <el-option label="诉讼保全" value="诉讼保全" />
                <el-option label="仲裁保全" value="仲裁保全" />
                <el-option label="协商保全" value="协商保全" />
                <el-option label="以物抵债" value="以物抵债" />
                <el-option label="债务重组" value="债务重组" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="preserveStatus">
              <el-select v-model="form.preserveStatus" placeholder="请选择" style="width:100%">
                <el-option label="申请中" :value="0" />
                <el-option label="已保全" :value="1" />
                <el-option label="执行中" :value="2" />
                <el-option label="已完成" :value="3" />
                <el-option label="已终止" :value="4" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="法院名称" prop="courtName">
              <el-input v-model="form.courtName" placeholder="请输入法院名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="案号" prop="caseNo">
              <el-input v-model="form.caseNo" placeholder="请输入案号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="债权金额(万元)" prop="claimAmount">
              <el-input-number v-model="form.claimAmount" :min="0" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="保全金额(万元)" prop="preserveAmount">
              <el-input-number v-model="form.preserveAmount" :min="0" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="代理律师" prop="lawyerName">
              <el-input v-model="form.lawyerName" placeholder="请输入代理律师" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="立案日期" prop="filingDate">
              <el-date-picker v-model="form.filingDate" type="date" value-format="YYYY-MM-DD" placeholder="选择日期" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开庭日期" prop="hearingDate">
              <el-date-picker v-model="form.hearingDate" type="date" value-format="YYYY-MM-DD" placeholder="选择日期" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="判决日期" prop="judgmentDate">
              <el-date-picker v-model="form.judgmentDate" type="date" value-format="YYYY-MM-DD" placeholder="选择日期" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="保全资产描述" prop="preserveAsset">
          <el-input v-model="form.preserveAsset" type="textarea" placeholder="请输入保全资产描述" />
        </el-form-item>
        <el-form-item label="判决结果" prop="judgmentResult">
          <el-input v-model="form.judgmentResult" type="textarea" placeholder="请输入判决结果" />
        </el-form-item>
        <el-form-item label="执行情况" prop="executionStatus">
          <el-input v-model="form.executionStatus" placeholder="请输入执行情况" />
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

    <!-- 更新回收金额弹窗 -->
    <el-dialog title="更新回收金额" v-model="recoveryDialogVisible" width="400px" append-to-body>
      <el-form ref="recoveryFormRef" :model="recoveryForm" label-width="100px">
        <el-form-item label="回收金额(万元)" prop="recoveryAmount">
          <el-input-number v-model="recoveryForm.recoveryAmount" :min="0" :precision="2" style="width:100%" />
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
import { listAsset, getAsset, addAsset, updateAsset, delAsset, updateRecovery } from '@/api/loanCollection'

const assetList = ref([])
const loading = ref(false)
const total = ref(0)
const ids = ref([])
const multiple = ref(true)
const dialogVisible = ref(false)
const recoveryDialogVisible = ref(false)
const dialogTitle = ref('新增保全')

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  companyName: undefined,
  preserveType: undefined,
  preserveStatus: undefined
})

const form = ref({})
const recoveryForm = ref({})

const stats = computed(() => {
  const list = assetList.value
  return {
    preserving: list.filter(i => i.preserveStatus === 1 || i.preserveStatus === 2).length,
    completed: list.filter(i => i.preserveStatus === 3).length,
    totalAmount: list.reduce((s, i) => s + Number(i.claimAmount || 0), 0).toFixed(2),
    recoveryAmount: list.reduce((s, i) => s + Number(i.recoveryAmount || 0), 0).toFixed(2)
  }
})

const rules = {
  loanId: [{ required: true, message: '请输入贷款ID', trigger: 'blur' }],
  loanNo: [{ required: true, message: '请输入贷款编号', trigger: 'blur' }],
  companyName: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
  preserveType: [{ required: true, message: '请选择保全方式', trigger: 'change' }]
}

function preserveStatusLabel(status) {
  const map = { 0: '申请中', 1: '已保全', 2: '执行中', 3: '已完成', 4: '已终止' }
  return map[status] || '未知'
}

function preserveStatusType(status) {
  const map = { 0: 'info', 1: 'warning', 2: 'primary', 3: 'success', 4: 'danger' }
  return map[status] || 'info'
}

async function getList() {
  loading.value = true
  try {
    const res = await listAsset(queryParams)
    assetList.value = res.rows || []
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
  queryParams.preserveType = undefined
  queryParams.preserveStatus = undefined
  handleQuery()
}

function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id)
  multiple.value = !selection.length
}

function handleAdd() {
  dialogTitle.value = '新增保全'
  form.value = { preserveStatus: 0 }
  dialogVisible.value = true
}

function handleUpdate(row) {
  dialogTitle.value = '编辑保全'
  form.value = { ...row }
  dialogVisible.value = true
}

function handleRecovery(row) {
  recoveryForm.value = { id: row.id, recoveryAmount: row.recoveryAmount || 0 }
  recoveryDialogVisible.value = true
}

function handleDelete(row) {
  const deleteIds = row.id ? [row.id] : ids.value
  ElMessageBox.confirm('确认删除所选资产保全记录？', '提示', { type: 'warning' }).then(async () => {
    await delAsset(deleteIds.join(','))
    ElMessage.success('删除成功')
    getList()
  })
}

async function submitForm() {
  if (form.value.id) {
    await updateAsset(form.value)
    ElMessage.success('修改成功')
  } else {
    await addAsset(form.value)
    ElMessage.success('新增成功')
  }
  dialogVisible.value = false
  getList()
}

async function submitRecovery() {
  await updateRecovery(recoveryForm.value.id, recoveryForm.value.recoveryAmount)
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
.stat-label { font-size: 14px; color: #909399; margin-top: 8px; }
</style>

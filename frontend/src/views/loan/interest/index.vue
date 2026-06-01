<template>
  <div class="app-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="mb20">
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value">{{ stats.monthCount }}</div>
            <div class="stat-label">本月计提笔数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value">{{ stats.monthAmount }} 万元</div>
            <div class="stat-label">本月计提金额</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value text-success">{{ stats.settledCount }}</div>
            <div class="stat-label">已结清笔数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value text-warning">{{ stats.pendingCount }}</div>
            <div class="stat-label">待计提笔数</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 搜索区域 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="mb20">
      <el-form-item label="企业名称" prop="companyName">
        <el-input v-model="queryParams.companyName" placeholder="请输入企业名称" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="计提状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option label="待计提" :value="0" />
          <el-option label="已计提" :value="1" />
          <el-option label="已结清" :value="2" />
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
        <el-button type="primary" plain icon="Plus" @click="handleAdd">新增计提</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="DocumentCopy" @click="handleBatch">批量计提</el-button>
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
      <el-table-column label="计提日期" prop="interestDate" width="120" align="center">
        <template #default="scope">
          {{ scope.row.interestDate ? scope.row.interestDate.substring(0, 10) : '-' }}
        </template>
      </el-table-column>
      <el-table-column label="计息天数" prop="interestDays" width="100" align="center" />
      <el-table-column label="利率(%)" prop="interestRate" width="100" align="center">
        <template #default="scope">
          {{ scope.row.interestRate ? Number(scope.row.interestRate).toFixed(2) + '%' : '-' }}
        </template>
      </el-table-column>
      <el-table-column label="计息本金(万元)" prop="principalAmount" width="130" align="right" />
      <el-table-column label="应计利息(万元)" prop="accruedInterest" width="130" align="right">
        <template #default="scope">
          <span style="font-weight:bold;color:#409eff">{{ scope.row.accruedInterest || 0 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="计提状态" prop="status" width="100" align="center">
        <template #default="scope">
          <el-tag :type="statusTagType(scope.row.status)">{{ statusLabel(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作人" prop="operatorName" width="100" />
      <el-table-column label="操作" width="180" align="center" fixed="right">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleEdit(scope.row)">编辑</el-button>
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
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="600px" append-to-body>
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
            <el-form-item label="计提日期" prop="interestDate">
              <el-date-picker v-model="form.interestDate" type="date" value-format="YYYY-MM-DD" placeholder="选择日期" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计息天数" prop="interestDays">
              <el-input-number v-model="form.interestDays" :min="1" :max="365" style="width:100%" />
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
            <el-form-item label="计息本金(万元)" prop="principalAmount">
              <el-input-number v-model="form.principalAmount" :min="0" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="应计利息(万元)" prop="accruedInterest">
              <el-input-number v-model="form.accruedInterest" :min="0" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计提状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择" style="width:100%">
                <el-option label="待计提" :value="0" />
                <el-option label="已计提" :value="1" />
                <el-option label="已结清" :value="2" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </template>
    </el-dialog>

    <!-- 批量计提弹窗 -->
    <el-dialog title="批量计提" v-model="batchDialogVisible" width="500px" append-to-body>
      <el-form ref="batchFormRef" :model="batchForm" :rules="batchRules" label-width="100px">
        <el-form-item label="计提日期" prop="interestDate">
          <el-date-picker v-model="batchForm.interestDate" type="date" value-format="YYYY-MM-DD" placeholder="选择日期" style="width:100%" />
        </el-form-item>
        <el-form-item label="计息天数" prop="interestDays">
          <el-input-number v-model="batchForm.interestDays" :min="1" :max="365" style="width:100%" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="batchForm.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="batchDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitBatch">确 定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

const tableData = ref([])
const loading = ref(false)
const total = ref(0)
const ids = ref([])
const multiple = ref(true)
const dialogVisible = ref(false)
const batchDialogVisible = ref(false)
const dialogTitle = ref('新增计提')

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  companyName: undefined,
  status: undefined
})

const form = ref({})
const batchForm = ref({ interestDate: '', interestDays: 30, remark: '' })

const stats = computed(() => {
  const list = tableData.value
  const now = new Date()
  const monthStr = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}`
  const monthItems = list.filter(i => i.interestDate && i.interestDate.startsWith(monthStr))
  return {
    monthCount: monthItems.length,
    monthAmount: monthItems.reduce((s, i) => s + Number(i.accruedInterest || 0), 0).toFixed(2),
    settledCount: list.filter(i => i.status === 2).length,
    pendingCount: list.filter(i => i.status === 0).length
  }
})

const rules = {
  loanId: [{ required: true, message: '请输入贷款ID', trigger: 'blur' }],
  loanNo: [{ required: true, message: '请输入贷款编号', trigger: 'blur' }],
  companyName: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
  interestDate: [{ required: true, message: '请选择计提日期', trigger: 'change' }],
  interestDays: [{ required: true, message: '请输入计息天数', trigger: 'blur' }],
  principalAmount: [{ required: true, message: '请输入计息本金', trigger: 'blur' }]
}

const batchRules = {
  interestDate: [{ required: true, message: '请选择计提日期', trigger: 'change' }],
  interestDays: [{ required: true, message: '请输入计息天数', trigger: 'blur' }]
}

function statusLabel(status) {
  const map = { 0: '待计提', 1: '已计提', 2: '已结清' }
  return map[status] || '未知'
}

function statusTagType(status) {
  const map = { 0: 'warning', 1: 'primary', 2: 'success' }
  return map[status] || 'info'
}

async function getList() {
  loading.value = true
  try {
    const res = await request({ url: '/loan/interest/list', method: 'get', params: queryParams })
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
  queryParams.status = undefined
  handleQuery()
}

function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id)
  multiple.value = !selection.length
}

function handleAdd() {
  dialogTitle.value = '新增计提'
  form.value = { status: 0, interestDays: 30, interestRate: 0, principalAmount: 0, accruedInterest: 0 }
  dialogVisible.value = true
}

function handleEdit(row) {
  dialogTitle.value = '编辑计提'
  form.value = { ...row }
  dialogVisible.value = true
}

function handleBatch() {
  batchForm.value = { interestDate: '', interestDays: 30, remark: '' }
  batchDialogVisible.value = true
}

function handleDelete(row) {
  const deleteIds = row.id ? [row.id] : ids.value
  ElMessageBox.confirm('确认删除所选计提记录？', '提示', { type: 'warning' }).then(async () => {
    await request({ url: `/loan/interest/${deleteIds.join(',')}`, method: 'delete' })
    ElMessage.success('删除成功')
    getList()
  })
}

async function submitForm() {
  if (form.value.id) {
    await request({ url: '/loan/interest', method: 'put', data: form.value })
    ElMessage.success('修改成功')
  } else {
    await request({ url: '/loan/interest', method: 'post', data: form.value })
    ElMessage.success('新增成功')
  }
  dialogVisible.value = false
  getList()
}

async function submitBatch() {
  await request({ url: '/loan/interest/batch', method: 'post', data: batchForm.value })
  ElMessage.success('批量计提成功')
  batchDialogVisible.value = false
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

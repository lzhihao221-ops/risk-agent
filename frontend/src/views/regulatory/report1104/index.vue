<template>
  <div class="app-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="mb20">
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value text-warning">{{ stats.pending }}</div>
            <div class="stat-label">待审核</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value text-primary">{{ stats.submitted }}</div>
            <div class="stat-label">已提交</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value text-success">{{ stats.approved }}</div>
            <div class="stat-label">已通过</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value">{{ stats.total }}</div>
            <div class="stat-label">报表总数</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 搜索区域 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="mb20">
      <el-form-item label="报表编号" prop="reportNo">
        <el-input v-model="queryParams.reportNo" placeholder="请输入报表编号" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="报表类型" prop="reportType">
        <el-select v-model="queryParams.reportType" placeholder="请选择" clearable>
          <el-option label="基础类" value="基础类" />
          <el-option label="业务类" value="业务类" />
          <el-option label="监管类" value="监管类" />
        </el-select>
      </el-form-item>
      <el-form-item label="报告期" prop="reportPeriod">
        <el-input v-model="queryParams.reportPeriod" placeholder="如: 2024Q1" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择" clearable>
          <el-option label="待审核" :value="0" />
          <el-option label="已提交" :value="1" />
          <el-option label="已通过" :value="2" />
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
        <el-button type="primary" plain icon="Plus" @click="handleAdd">新增报表</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete">删除</el-button>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="tableData" @selection-change="handleSelectionChange" border stripe>
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="报表编号" prop="reportNo" width="130" />
      <el-table-column label="报表名称" prop="reportName" min-width="200" show-overflow-tooltip />
      <el-table-column label="报表类型" prop="reportType" width="100" align="center">
        <template #default="scope">
          <el-tag size="small">{{ scope.row.reportType || '-' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="报告期" prop="reportPeriod" width="110" align="center" />
      <el-table-column label="频次" prop="frequency" width="90" align="center" />
      <el-table-column label="状态" prop="status" width="100" align="center">
        <template #default="scope">
          <el-tag :type="statusTagType(scope.row.status)">{{ statusLabel(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="生成人" prop="createUser" width="100" />
      <el-table-column label="提交人" prop="submitUser" width="100" />
      <el-table-column label="操作" width="250" align="center" fixed="right">
        <template #default="scope">
          <template v-if="scope.row.status === 0">
            <el-button link type="success" icon="Check" @click="handleApprove(scope.row)">审核</el-button>
            <el-button link type="primary" icon="Promotion" @click="handleSubmit(scope.row)">提交</el-button>
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

    <!-- 新增/编辑弹窗 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="650px" append-to-body>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="报表编号" prop="reportNo">
              <el-input v-model="form.reportNo" placeholder="请输入报表编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报表类型" prop="reportType">
              <el-select v-model="form.reportType" placeholder="请选择" style="width:100%">
                <el-option label="基础类" value="基础类" />
                <el-option label="业务类" value="业务类" />
                <el-option label="监管类" value="监管类" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="报表名称" prop="reportName">
          <el-input v-model="form.reportName" placeholder="请输入报表名称" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="报告期" prop="reportPeriod">
              <el-input v-model="form.reportPeriod" placeholder="如: 2024Q1" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="频次" prop="frequency">
              <el-select v-model="form.frequency" placeholder="请选择" style="width:100%">
                <el-option label="月报" value="月报" />
                <el-option label="季报" value="季报" />
                <el-option label="半年报" value="半年报" />
                <el-option label="年报" value="年报" />
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
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { listReport1104, addReport1104, updateReport1104, deleteReport1104 } from '@/api/regulatory'

const tableData = ref([])
const loading = ref(false)
const total = ref(0)
const ids = ref([])
const multiple = ref(true)
const dialogVisible = ref(false)
const dialogTitle = ref('新增报表')

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  reportNo: undefined,
  reportType: undefined,
  reportPeriod: undefined,
  status: undefined
})

const form = ref({})

const stats = computed(() => {
  const list = tableData.value
  return {
    pending: list.filter(i => i.status === 0).length,
    submitted: list.filter(i => i.status === 1).length,
    approved: list.filter(i => i.status === 2).length,
    total: list.length
  }
})

const rules = {
  reportNo: [{ required: true, message: '请输入报表编号', trigger: 'blur' }],
  reportName: [{ required: true, message: '请输入报表名称', trigger: 'blur' }],
  reportType: [{ required: true, message: '请选择报表类型', trigger: 'change' }],
  reportPeriod: [{ required: true, message: '请输入报告期', trigger: 'blur' }],
  frequency: [{ required: true, message: '请选择频次', trigger: 'change' }]
}

function statusLabel(status) {
  const map = { 0: '待审核', 1: '已提交', 2: '已通过', 3: '已驳回' }
  return map[status] || '未知'
}

function statusTagType(status) {
  const map = { 0: 'warning', 1: 'primary', 2: 'success', 3: 'danger' }
  return map[status] || 'info'
}

async function getList() {
  loading.value = true
  try {
    const res = await listReport1104(queryParams)
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
  queryParams.reportNo = undefined
  queryParams.reportType = undefined
  queryParams.reportPeriod = undefined
  queryParams.status = undefined
  handleQuery()
}

function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id)
  multiple.value = !selection.length
}

function handleAdd() {
  dialogTitle.value = '新增报表'
  form.value = { status: 0 }
  dialogVisible.value = true
}

function handleEdit(row) {
  dialogTitle.value = '编辑报表'
  form.value = { ...row }
  dialogVisible.value = true
}

async function handleApprove(row) {
  await updateReport1104({ id: row.id, status: 2 })
  ElMessage.success('审核通过')
  getList()
}

async function handleSubmit(row) {
  await ElMessageBox.confirm('确认提交该报表？', '提示', { type: 'warning' })
  await updateReport1104({ id: row.id, status: 1, submitUser: 'admin' })
  ElMessage.success('提交成功')
  getList()
}

function handleDelete(row) {
  const deleteIds = row.id ? [row.id] : ids.value
  ElMessageBox.confirm('确认删除所选报表？', '提示', { type: 'warning' }).then(async () => {
    await deleteReport1104(deleteIds.join(','))
    ElMessage.success('删除成功')
    getList()
  })
}

async function submitForm() {
  if (form.value.id) {
    await updateReport1104(form.value)
    ElMessage.success('修改成功')
  } else {
    await addReport1104(form.value)
    ElMessage.success('新增成功')
  }
  dialogVisible.value = false
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
.stat-value.text-primary { color: #409eff; }
.stat-value.text-success { color: #67c23a; }
.stat-label { font-size: 14px; color: #909399; margin-top: 8px; }
</style>

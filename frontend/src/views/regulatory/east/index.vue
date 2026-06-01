<template>
  <div class="app-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="mb20">
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value text-warning">{{ stats.pending }}</div>
            <div class="stat-label">待提交</div>
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
            <div class="stat-value text-success">{{ stats.verified }}</div>
            <div class="stat-label">已校验</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value">{{ stats.total }}</div>
            <div class="stat-label">报送总数</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 搜索区域 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="mb20">
      <el-form-item label="报告期" prop="reportPeriod">
        <el-input v-model="queryParams.reportPeriod" placeholder="如: 2024Q1" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="数据类型" prop="dataType">
        <el-select v-model="queryParams.dataType" placeholder="请选择" clearable>
          <el-option label="客户信息" value="客户信息" />
          <el-option label="信贷业务" value="信贷业务" />
          <el-option label="担保信息" value="担保信息" />
          <el-option label="交易流水" value="交易流水" />
          <el-option label="财务数据" value="财务数据" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择" clearable>
          <el-option label="待提交" :value="0" />
          <el-option label="已提交" :value="1" />
          <el-option label="已校验" :value="2" />
          <el-option label="校验失败" :value="3" />
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
        <el-button type="primary" plain icon="Plus" @click="handleAdd">新增报送</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete">删除</el-button>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="tableData" @selection-change="handleSelectionChange" border stripe>
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="报告期" prop="reportPeriod" width="110" align="center">
        <template #default="scope">
          <el-tag size="small">{{ scope.row.reportPeriod || '-' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="数据类型" prop="dataType" width="110" align="center" />
      <el-table-column label="数据表" prop="dataTable" min-width="160" show-overflow-tooltip />
      <el-table-column label="记录数" prop="recordCount" width="100" align="right" />
      <el-table-column label="文件名" prop="fileName" min-width="200" show-overflow-tooltip />
      <el-table-column label="文件大小" prop="fileSize" width="100" align="right">
        <template #default="scope">
          {{ scope.row.fileSize ? (scope.row.fileSize / 1024).toFixed(1) + ' KB' : '-' }}
        </template>
      </el-table-column>
      <el-table-column label="校验状态" prop="verifyStatus" width="100" align="center">
        <template #default="scope">
          <el-tag :type="verifyTagType(scope.row.verifyStatus)" size="small">
            {{ verifyLabel(scope.row.verifyStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" prop="status" width="100" align="center">
        <template #default="scope">
          <el-tag :type="statusTagType(scope.row.status)">{{ statusLabel(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" align="center" fixed="right">
        <template #default="scope">
          <template v-if="scope.row.status === 0">
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
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="600px" append-to-body>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="报告期" prop="reportPeriod">
              <el-input v-model="form.reportPeriod" placeholder="如: 2024Q1" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据类型" prop="dataType">
              <el-select v-model="form.dataType" placeholder="请选择" style="width:100%">
                <el-option label="客户信息" value="客户信息" />
                <el-option label="信贷业务" value="信贷业务" />
                <el-option label="担保信息" value="担保信息" />
                <el-option label="交易流水" value="交易流水" />
                <el-option label="财务数据" value="财务数据" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="数据表" prop="dataTable">
              <el-input v-model="form.dataTable" placeholder="请输入数据表名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="记录数" prop="recordCount">
              <el-input-number v-model="form.recordCount" :min="0" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="文件名" prop="fileName">
          <el-input v-model="form.fileName" placeholder="请输入文件名" />
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
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { listEast, addEast, updateEast, deleteEast } from '@/api/regulatory'

const tableData = ref([])
const loading = ref(false)
const total = ref(0)
const ids = ref([])
const multiple = ref(true)
const dialogVisible = ref(false)
const dialogTitle = ref('新增EAST报送')

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  reportPeriod: undefined,
  dataType: undefined,
  status: undefined
})

const form = ref({})

const stats = computed(() => {
  const list = tableData.value
  return {
    pending: list.filter(i => i.status === 0).length,
    submitted: list.filter(i => i.status === 1).length,
    verified: list.filter(i => i.status === 2).length,
    total: list.length
  }
})

const rules = {
  reportPeriod: [{ required: true, message: '请输入报告期', trigger: 'blur' }],
  dataType: [{ required: true, message: '请选择数据类型', trigger: 'change' }],
  dataTable: [{ required: true, message: '请输入数据表名', trigger: 'blur' }]
}

function statusLabel(status) {
  const map = { 0: '待提交', 1: '已提交', 2: '已校验', 3: '校验失败' }
  return map[status] || '未知'
}

function statusTagType(status) {
  const map = { 0: 'warning', 1: 'primary', 2: 'success', 3: 'danger' }
  return map[status] || 'info'
}

function verifyLabel(status) {
  const map = { 0: '未校验', 1: '通过', 2: '失败' }
  return map[status] || '未知'
}

function verifyTagType(status) {
  const map = { 0: 'info', 1: 'success', 2: 'danger' }
  return map[status] || 'info'
}

async function getList() {
  loading.value = true
  try {
    const res = await listEast(queryParams)
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
  queryParams.reportPeriod = undefined
  queryParams.dataType = undefined
  queryParams.status = undefined
  handleQuery()
}

function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id)
  multiple.value = !selection.length
}

function handleAdd() {
  dialogTitle.value = '新增EAST报送'
  form.value = { status: 0, verifyStatus: 0, recordCount: 0 }
  dialogVisible.value = true
}

function handleEdit(row) {
  dialogTitle.value = '编辑EAST报送'
  form.value = { ...row }
  dialogVisible.value = true
}

async function handleSubmit(row) {
  await ElMessageBox.confirm('确认提交该EAST报送？', '提示', { type: 'warning' })
  await updateEast({ id: row.id, status: 1 })
  ElMessage.success('提交成功')
  getList()
}

function handleDelete(row) {
  const deleteIds = row.id ? [row.id] : ids.value
  ElMessageBox.confirm('确认删除所选EAST报送记录？', '提示', { type: 'warning' }).then(async () => {
    await deleteEast(deleteIds.join(','))
    ElMessage.success('删除成功')
    getList()
  })
}

async function submitForm() {
  if (form.value.id) {
    await updateEast(form.value)
    ElMessage.success('修改成功')
  } else {
    await addEast(form.value)
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

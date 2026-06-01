<template>
  <div class="app-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="mb20">
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value text-success">{{ stats.normal }}</div>
            <div class="stat-label">正常指标数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value text-warning">{{ stats.warning }}</div>
            <div class="stat-label">预警指标数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value text-danger">{{ stats.danger }}</div>
            <div class="stat-label">超限指标数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value">{{ stats.total }}</div>
            <div class="stat-label">指标总数</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 搜索区域 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="mb20">
      <el-form-item label="指标类型" prop="indicatorType">
        <el-select v-model="queryParams.indicatorType" placeholder="请选择" clearable>
          <el-option label="资本充足" value="资本充足" />
          <el-option label="资产质量" value="资产质量" />
          <el-option label="流动性" value="流动性" />
          <el-option label="盈利性" value="盈利性" />
          <el-option label="杠杆率" value="杠杆率" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择" clearable>
          <el-option label="正常" :value="0" />
          <el-option label="预警" :value="1" />
          <el-option label="超限" :value="2" />
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
        <el-button type="primary" plain icon="Plus" @click="handleAdd">新增指标</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete">删除</el-button>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="tableData" @selection-change="handleSelectionChange" border stripe>
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="指标编号" prop="indicatorNo" width="120" />
      <el-table-column label="指标名称" prop="indicatorName" min-width="180" show-overflow-tooltip />
      <el-table-column label="指标类型" prop="indicatorType" width="110" align="center">
        <template #default="scope">
          <el-tag size="small">{{ scope.row.indicatorType || '-' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="指标值" prop="indicatorValue" width="110" align="right">
        <template #default="scope">
          <span style="font-weight:bold">{{ scope.row.indicatorValue || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="单位" prop="unit" width="80" align="center" />
      <el-table-column label="预警阈值" prop="warningThreshold" width="100" align="center">
        <template #default="scope">{{ scope.row.warningThreshold || '-' }}</template>
      </el-table-column>
      <el-table-column label="警戒值" prop="alertValue" width="90" align="center">
        <template #default="scope">{{ scope.row.alertValue || '-' }}</template>
      </el-table-column>
      <el-table-column label="监管线" prop="regulatoryLine" width="90" align="center">
        <template #default="scope">{{ scope.row.regulatoryLine || '-' }}</template>
      </el-table-column>
      <el-table-column label="状态" prop="status" width="90" align="center">
        <template #default="scope">
          <el-tag :type="statusTagType(scope.row.status)">{{ statusLabel(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150" align="center" fixed="right">
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
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="650px" append-to-body>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="指标编号" prop="indicatorNo">
              <el-input v-model="form.indicatorNo" placeholder="请输入指标编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="指标类型" prop="indicatorType">
              <el-select v-model="form.indicatorType" placeholder="请选择" style="width:100%">
                <el-option label="资本充足" value="资本充足" />
                <el-option label="资产质量" value="资产质量" />
                <el-option label="流动性" value="流动性" />
                <el-option label="盈利性" value="盈利性" />
                <el-option label="杠杆率" value="杠杆率" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="指标名称" prop="indicatorName">
          <el-input v-model="form.indicatorName" placeholder="请输入指标名称" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="指标值" prop="indicatorValue">
              <el-input-number v-model="form.indicatorValue" :min="0" :step="0.01" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="单位" prop="unit">
              <el-input v-model="form.unit" placeholder="如: %" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择" style="width:100%">
                <el-option label="正常" :value="0" />
                <el-option label="预警" :value="1" />
                <el-option label="超限" :value="2" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="预警阈值" prop="warningThreshold">
              <el-input-number v-model="form.warningThreshold" :min="0" :step="0.01" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="警戒值" prop="alertValue">
              <el-input-number v-model="form.alertValue" :min="0" :step="0.01" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="监管线" prop="regulatoryLine">
              <el-input-number v-model="form.regulatoryLine" :min="0" :step="0.01" :precision="2" style="width:100%" />
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
import { listIndicator, addIndicator, updateIndicator, deleteIndicator } from '@/api/regulatory'

const tableData = ref([])
const loading = ref(false)
const total = ref(0)
const ids = ref([])
const multiple = ref(true)
const dialogVisible = ref(false)
const dialogTitle = ref('新增指标')

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  indicatorType: undefined,
  status: undefined
})

const form = ref({})

const stats = computed(() => {
  const list = tableData.value
  return {
    normal: list.filter(i => i.status === 0).length,
    warning: list.filter(i => i.status === 1).length,
    danger: list.filter(i => i.status === 2).length,
    total: list.length
  }
})

const rules = {
  indicatorNo: [{ required: true, message: '请输入指标编号', trigger: 'blur' }],
  indicatorName: [{ required: true, message: '请输入指标名称', trigger: 'blur' }],
  indicatorType: [{ required: true, message: '请选择指标类型', trigger: 'change' }],
  indicatorValue: [{ required: true, message: '请输入指标值', trigger: 'blur' }]
}

function statusLabel(status) {
  const map = { 0: '正常', 1: '预警', 2: '超限' }
  return map[status] || '未知'
}

function statusTagType(status) {
  const map = { 0: 'success', 1: 'warning', 2: 'danger' }
  return map[status] || 'info'
}

async function getList() {
  loading.value = true
  try {
    const res = await listIndicator(queryParams)
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
  queryParams.indicatorType = undefined
  queryParams.status = undefined
  handleQuery()
}

function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id)
  multiple.value = !selection.length
}

function handleAdd() {
  dialogTitle.value = '新增指标'
  form.value = { status: 0, indicatorValue: 0, warningThreshold: 0, alertValue: 0, regulatoryLine: 0 }
  dialogVisible.value = true
}

function handleEdit(row) {
  dialogTitle.value = '编辑指标'
  form.value = { ...row }
  dialogVisible.value = true
}

function handleDelete(row) {
  const deleteIds = row.id ? [row.id] : ids.value
  ElMessageBox.confirm('确认删除所选监管指标？', '提示', { type: 'warning' }).then(async () => {
    await deleteIndicator(deleteIds.join(','))
    ElMessage.success('删除成功')
    getList()
  })
}

async function submitForm() {
  if (form.value.id) {
    await updateIndicator(form.value)
    ElMessage.success('修改成功')
  } else {
    await addIndicator(form.value)
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
.stat-value.text-success { color: #67c23a; }
.stat-value.text-warning { color: #e6a23c; }
.stat-value.text-danger { color: #f56c6c; }
.stat-label { font-size: 14px; color: #909399; margin-top: 8px; }
</style>

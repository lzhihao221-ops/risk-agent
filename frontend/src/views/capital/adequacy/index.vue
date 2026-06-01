<template>
  <div class="app-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="mb20">
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value text-primary">{{ stats.car }}%</div>
            <div class="stat-label">资本充足率 (CAR)</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value text-success">{{ stats.tier1Ratio }}%</div>
            <div class="stat-label">一级资本充足率</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value text-warning">{{ stats.coreRatio }}%</div>
            <div class="stat-label">核心一级资本充足率</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value text-info">{{ stats.leverageRatio }}%</div>
            <div class="stat-label">杠杆率</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 搜索区域 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="mb20">
      <el-form-item label="报告期" prop="reportPeriod">
        <el-input v-model="queryParams.reportPeriod" placeholder="如: 2024Q1" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <el-row :gutter="10" class="mb20">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd">新增记录</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete">删除</el-button>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="tableData" @selection-change="handleSelectionChange" border stripe>
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="计算日期" prop="calcDate" width="120" align="center">
        <template #default="scope">{{ scope.row.calcDate ? scope.row.calcDate.substring(0, 10) : '-' }}</template>
      </el-table-column>
      <el-table-column label="报告期" prop="reportPeriod" width="110" align="center">
        <template #default="scope">
          <el-tag size="small">{{ scope.row.reportPeriod || '-' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="一级资本(万元)" prop="tier1Capital" width="130" align="right" />
      <el-table-column label="二级资本(万元)" prop="tier2Capital" width="130" align="right" />
      <el-table-column label="总资本(万元)" prop="totalCapital" width="130" align="right">
        <template #default="scope">
          <span style="font-weight:bold">{{ scope.row.totalCapital || 0 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="总RWA(万元)" prop="totalRwa" width="130" align="right" />
      <el-table-column label="CAR(%)" prop="car" width="100" align="center">
        <template #default="scope">
          <el-tag :type="scope.row.car >= 10.5 ? 'success' : scope.row.car >= 8 ? 'warning' : 'danger'" size="small">
            {{ scope.row.car ? Number(scope.row.car).toFixed(2) : '-' }}%
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="一级比率(%)" prop="tier1Ratio" width="110" align="center">
        <template #default="scope">{{ scope.row.tier1Ratio ? Number(scope.row.tier1Ratio).toFixed(2) + '%' : '-' }}</template>
      </el-table-column>
      <el-table-column label="核心比率(%)" prop="coreRatio" width="110" align="center">
        <template #default="scope">{{ scope.row.coreRatio ? Number(scope.row.coreRatio).toFixed(2) + '%' : '-' }}</template>
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
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="700px" append-to-body>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="计算日期" prop="calcDate">
              <el-date-picker v-model="form.calcDate" type="date" value-format="YYYY-MM-DD" placeholder="选择日期" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报告期" prop="reportPeriod">
              <el-input v-model="form.reportPeriod" placeholder="如: 2024Q1" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="一级资本(万元)" prop="tier1Capital">
              <el-input-number v-model="form.tier1Capital" :min="0" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="二级资本(万元)" prop="tier2Capital">
              <el-input-number v-model="form.tier2Capital" :min="0" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="总资本(万元)" prop="totalCapital">
              <el-input-number v-model="form.totalCapital" :min="0" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="总RWA(万元)" prop="totalRwa">
              <el-input-number v-model="form.totalRwa" :min="0" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="CAR(%)" prop="car">
              <el-input-number v-model="form.car" :min="0" :step="0.01" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="一级比率(%)" prop="tier1Ratio">
              <el-input-number v-model="form.tier1Ratio" :min="0" :step="0.01" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="核心比率(%)" prop="coreRatio">
              <el-input-number v-model="form.coreRatio" :min="0" :step="0.01" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="杠杆率(%)" prop="leverageRatio">
              <el-input-number v-model="form.leverageRatio" :min="0" :step="0.01" :precision="2" style="width:100%" />
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
import { listAdequacy, addAdequacy, updateAdequacy, deleteAdequacy } from '@/api/capital'

const tableData = ref([])
const loading = ref(false)
const total = ref(0)
const ids = ref([])
const multiple = ref(true)
const dialogVisible = ref(false)
const dialogTitle = ref('新增资本充足率')

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  reportPeriod: undefined
})

const form = ref({})

const stats = computed(() => {
  const latest = tableData.value[0] || {}
  return {
    car: latest.car ? Number(latest.car).toFixed(2) : '-',
    tier1Ratio: latest.tier1Ratio ? Number(latest.tier1Ratio).toFixed(2) : '-',
    coreRatio: latest.coreRatio ? Number(latest.coreRatio).toFixed(2) : '-',
    leverageRatio: latest.leverageRatio ? Number(latest.leverageRatio).toFixed(2) : '-'
  }
})

const rules = {
  calcDate: [{ required: true, message: '请选择计算日期', trigger: 'change' }],
  reportPeriod: [{ required: true, message: '请输入报告期', trigger: 'blur' }],
  totalCapital: [{ required: true, message: '请输入总资本', trigger: 'blur' }],
  totalRwa: [{ required: true, message: '请输入总RWA', trigger: 'blur' }],
  car: [{ required: true, message: '请输入CAR', trigger: 'blur' }]
}

async function getList() {
  loading.value = true
  try {
    const res = await listAdequacy(queryParams)
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
  handleQuery()
}

function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id)
  multiple.value = !selection.length
}

function handleAdd() {
  dialogTitle.value = '新增资本充足率'
  form.value = { tier1Capital: 0, tier2Capital: 0, totalCapital: 0, totalRwa: 0, car: 0, tier1Ratio: 0, coreRatio: 0, leverageRatio: 0 }
  dialogVisible.value = true
}

function handleEdit(row) {
  dialogTitle.value = '编辑资本充足率'
  form.value = { ...row }
  dialogVisible.value = true
}

function handleDelete(row) {
  const deleteIds = row.id ? [row.id] : ids.value
  ElMessageBox.confirm('确认删除所选记录？', '提示', { type: 'warning' }).then(async () => {
    await deleteAdequacy(deleteIds.join(','))
    ElMessage.success('删除成功')
    getList()
  })
}

async function submitForm() {
  if (form.value.id) {
    await updateAdequacy(form.value)
    ElMessage.success('修改成功')
  } else {
    await addAdequacy(form.value)
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
.stat-value.text-primary { color: #409eff; }
.stat-value.text-success { color: #67c23a; }
.stat-value.text-warning { color: #e6a23c; }
.stat-value.text-info { color: #909399; }
.stat-label { font-size: 14px; color: #909399; margin-top: 8px; }
</style>

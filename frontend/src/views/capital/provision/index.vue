<template>
  <div class="app-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="mb20">
      <el-col :span="4">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value text-success">{{ stats.normal }} 万</div>
            <div class="stat-label">正常类拨备</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value text-primary">{{ stats.attention }} 万</div>
            <div class="stat-label">关注类拨备</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value text-warning">{{ stats.substandard }} 万</div>
            <div class="stat-label">次级类拨备</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value text-orange">{{ stats.doubtful }} 万</div>
            <div class="stat-label">可疑类拨备</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value text-danger">{{ stats.loss }} 万</div>
            <div class="stat-label">损失类拨备</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value">{{ stats.total }} 万</div>
            <div class="stat-label">拨备总额</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 搜索区域 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="mb20">
      <el-form-item label="计算日期" prop="calcDate">
        <el-date-picker v-model="queryParams.calcDate" type="date" value-format="YYYY-MM-DD" placeholder="选择日期" clearable style="width:150px" />
      </el-form-item>
      <el-form-item label="五级分类" prop="fiveCategory">
        <el-select v-model="queryParams.fiveCategory" placeholder="请选择" clearable>
          <el-option label="正常" :value="1" />
          <el-option label="关注" :value="2" />
          <el-option label="次级" :value="3" />
          <el-option label="可疑" :value="4" />
          <el-option label="损失" :value="5" />
        </el-select>
      </el-form-item>
      <el-form-item label="计提类型" prop="provisionType">
        <el-select v-model="queryParams.provisionType" placeholder="请选择" clearable>
          <el-option label="一般准备" value="一般准备" />
          <el-option label="专项准备" value="专项准备" />
          <el-option label="特种准备" value="特种准备" />
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
        <el-button type="primary" plain icon="Plus" @click="handleAdd">新增拨备</el-button>
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
      <el-table-column label="企业名称" prop="companyName" min-width="160" show-overflow-tooltip />
      <el-table-column label="五级分类" prop="fiveCategory" width="100" align="center">
        <template #default="scope">
          <el-tag :type="categoryTagType(scope.row.fiveCategory)" size="small">
            {{ categoryLabel(scope.row.fiveCategory) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="计提类型" prop="provisionType" width="110" align="center" />
      <el-table-column label="账面余额(万元)" prop="carryingAmount" width="130" align="right" />
      <el-table-column label="拨备率(%)" prop="provisionRate" width="100" align="center">
        <template #default="scope">{{ scope.row.provisionRate ? Number(scope.row.provisionRate).toFixed(2) : '-' }}</template>
      </el-table-column>
      <el-table-column label="拨备金额(万元)" prop="provisionAmount" width="130" align="right">
        <template #default="scope">
          <span style="font-weight:bold;color:#409eff">{{ scope.row.provisionAmount || 0 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="累计拨备(万元)" prop="accumulatedProvision" width="130" align="right" />
      <el-table-column label="缺口(万元)" prop="shortfall" width="110" align="right">
        <template #default="scope">
          <span :style="{ color: scope.row.shortfall > 0 ? '#f56c6c' : '#67c23a', fontWeight: 'bold' }">
            {{ scope.row.shortfall || 0 }}
          </span>
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
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="700px" append-to-body>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="计算日期" prop="calcDate">
              <el-date-picker v-model="form.calcDate" type="date" value-format="YYYY-MM-DD" placeholder="选择日期" style="width:100%" />
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
            <el-form-item label="计提类型" prop="provisionType">
              <el-select v-model="form.provisionType" placeholder="请选择" style="width:100%">
                <el-option label="一般准备" value="一般准备" />
                <el-option label="专项准备" value="专项准备" />
                <el-option label="特种准备" value="特种准备" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="账面余额(万元)" prop="carryingAmount">
              <el-input-number v-model="form.carryingAmount" :min="0" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="拨备率(%)" prop="provisionRate">
              <el-input-number v-model="form.provisionRate" :min="0" :max="100" :step="0.01" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="拨备金额(万元)" prop="provisionAmount">
              <el-input-number v-model="form.provisionAmount" :min="0" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="累计拨备(万元)" prop="accumulatedProvision">
              <el-input-number v-model="form.accumulatedProvision" :min="0" :precision="2" style="width:100%" />
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
import { listProvision, addProvision, updateProvision, deleteProvision } from '@/api/capital'

const tableData = ref([])
const loading = ref(false)
const total = ref(0)
const ids = ref([])
const multiple = ref(true)
const dialogVisible = ref(false)
const dialogTitle = ref('新增拨备')

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  calcDate: undefined,
  fiveCategory: undefined,
  provisionType: undefined
})

const form = ref({})

const stats = computed(() => {
  const list = tableData.value
  const sum = (filter) => list.filter(filter).reduce((s, i) => s + Number(i.provisionAmount || 0), 0).toFixed(2)
  return {
    normal: sum(i => i.fiveCategory === 1),
    attention: sum(i => i.fiveCategory === 2),
    substandard: sum(i => i.fiveCategory === 3),
    doubtful: sum(i => i.fiveCategory === 4),
    loss: sum(i => i.fiveCategory === 5),
    total: list.reduce((s, i) => s + Number(i.provisionAmount || 0), 0).toFixed(2)
  }
})

const rules = {
  calcDate: [{ required: true, message: '请选择计算日期', trigger: 'change' }],
  companyName: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
  fiveCategory: [{ required: true, message: '请选择五级分类', trigger: 'change' }],
  provisionType: [{ required: true, message: '请选择计提类型', trigger: 'change' }],
  carryingAmount: [{ required: true, message: '请输入账面余额', trigger: 'blur' }],
  provisionAmount: [{ required: true, message: '请输入拨备金额', trigger: 'blur' }]
}

function categoryLabel(val) {
  const map = { 1: '正常', 2: '关注', 3: '次级', 4: '可疑', 5: '损失' }
  return map[val] || '未知'
}

function categoryTagType(val) {
  const map = { 1: 'success', 2: 'info', 3: 'warning', 4: 'danger', 5: 'danger' }
  return map[val] || 'info'
}

async function getList() {
  loading.value = true
  try {
    const res = await listProvision(queryParams)
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
  queryParams.calcDate = undefined
  queryParams.fiveCategory = undefined
  queryParams.provisionType = undefined
  handleQuery()
}

function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id)
  multiple.value = !selection.length
}

function handleAdd() {
  dialogTitle.value = '新增拨备'
  form.value = { fiveCategory: 1, provisionType: '专项准备', carryingAmount: 0, provisionRate: 0, provisionAmount: 0, accumulatedProvision: 0 }
  dialogVisible.value = true
}

function handleEdit(row) {
  dialogTitle.value = '编辑拨备'
  form.value = { ...row }
  dialogVisible.value = true
}

function handleDelete(row) {
  const deleteIds = row.id ? [row.id] : ids.value
  ElMessageBox.confirm('确认删除所选拨备记录？', '提示', { type: 'warning' }).then(async () => {
    await deleteProvision(deleteIds.join(','))
    ElMessage.success('删除成功')
    getList()
  })
}

async function submitForm() {
  if (form.value.id) {
    await updateProvision(form.value)
    ElMessage.success('修改成功')
  } else {
    await addProvision(form.value)
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
.stat-value { font-size: 24px; font-weight: bold; color: #409eff; }
.stat-value.text-success { color: #67c23a; }
.stat-value.text-primary { color: #409eff; }
.stat-value.text-warning { color: #e6a23c; }
.stat-value.text-orange { color: #f89a2e; }
.stat-value.text-danger { color: #f56c6c; }
.stat-label { font-size: 13px; color: #909399; margin-top: 8px; }
</style>

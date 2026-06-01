<template>
  <div class="app-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="mb20">
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value text-success">{{ stats.stage1 }}</div>
            <div class="stat-label">第一阶段笔数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value text-warning">{{ stats.stage2 }}</div>
            <div class="stat-label">第二阶段笔数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value text-danger">{{ stats.stage3 }}</div>
            <div class="stat-label">第三阶段笔数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value">{{ stats.totalEcl }} 万元</div>
            <div class="stat-label">ECL总金额</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 搜索区域 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="mb20">
      <el-form-item label="企业名称" prop="companyName">
        <el-input v-model="queryParams.companyName" placeholder="请输入企业名称" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="阶段" prop="stage">
        <el-select v-model="queryParams.stage" placeholder="请选择" clearable>
          <el-option label="第一阶段" :value="1" />
          <el-option label="第二阶段" :value="2" />
          <el-option label="第三阶段" :value="3" />
        </el-select>
      </el-form-item>
      <el-form-item label="计算日期" prop="calcDate">
        <el-date-picker v-model="queryParams.calcDate" type="date" value-format="YYYY-MM-DD" placeholder="选择日期" clearable style="width:150px" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <el-row :gutter="10" class="mb20">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd">新增ECL</el-button>
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
      <el-table-column label="阶段" prop="stage" width="100" align="center">
        <template #default="scope">
          <el-tag :type="scope.row.stage === 1 ? 'success' : scope.row.stage === 2 ? 'warning' : 'danger'" size="small">
            第{{ scope.row.stage }}阶段
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="账面余额(万元)" prop="carryingAmount" width="130" align="right" />
      <el-table-column label="PD12m(%)" prop="pd12m" width="100" align="center">
        <template #default="scope">{{ scope.row.pd12m ? Number(scope.row.pd12m).toFixed(2) : '-' }}</template>
      </el-table-column>
      <el-table-column label="PD寿命期(%)" prop="pdLifetime" width="110" align="center">
        <template #default="scope">{{ scope.row.pdLifetime ? Number(scope.row.pdLifetime).toFixed(2) : '-' }}</template>
      </el-table-column>
      <el-table-column label="LGD(%)" prop="lgd" width="90" align="center">
        <template #default="scope">{{ scope.row.lgd ? Number(scope.row.lgd).toFixed(2) : '-' }}</template>
      </el-table-column>
      <el-table-column label="EAD(万元)" prop="ead" width="110" align="right" />
      <el-table-column label="ECL金额(万元)" prop="eclAmount" width="130" align="right">
        <template #default="scope">
          <span style="font-weight:bold;color:#409eff">{{ scope.row.eclAmount || 0 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="拨备率(%)" prop="provisionRate" width="100" align="center">
        <template #default="scope">{{ scope.row.provisionRate ? Number(scope.row.provisionRate).toFixed(2) : '-' }}</template>
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
            <el-form-item label="阶段" prop="stage">
              <el-select v-model="form.stage" placeholder="请选择" style="width:100%">
                <el-option label="第一阶段" :value="1" />
                <el-option label="第二阶段" :value="2" />
                <el-option label="第三阶段" :value="3" />
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
            <el-form-item label="账面余额(万元)" prop="carryingAmount">
              <el-input-number v-model="form.carryingAmount" :min="0" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="EAD(万元)" prop="ead">
              <el-input-number v-model="form.ead" :min="0" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="PD12m(%)" prop="pd12m">
              <el-input-number v-model="form.pd12m" :min="0" :max="100" :step="0.01" :precision="4" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="PD寿命期(%)" prop="pdLifetime">
              <el-input-number v-model="form.pdLifetime" :min="0" :max="100" :step="0.01" :precision="4" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="LGD(%)" prop="lgd">
              <el-input-number v-model="form.lgd" :min="0" :max="100" :step="0.01" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="ECL金额(万元)" prop="eclAmount">
              <el-input-number v-model="form.eclAmount" :min="0" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="拨备率(%)" prop="provisionRate">
              <el-input-number v-model="form.provisionRate" :min="0" :max="100" :step="0.01" :precision="2" style="width:100%" />
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
import { listEcl, addEcl, updateEcl, deleteEcl } from '@/api/capital'

const tableData = ref([])
const loading = ref(false)
const total = ref(0)
const ids = ref([])
const multiple = ref(true)
const dialogVisible = ref(false)
const dialogTitle = ref('新增ECL')

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  companyName: undefined,
  stage: undefined,
  calcDate: undefined
})

const form = ref({})

const stats = computed(() => {
  const list = tableData.value
  return {
    stage1: list.filter(i => i.stage === 1).length,
    stage2: list.filter(i => i.stage === 2).length,
    stage3: list.filter(i => i.stage === 3).length,
    totalEcl: list.reduce((s, i) => s + Number(i.eclAmount || 0), 0).toFixed(2)
  }
})

const rules = {
  calcDate: [{ required: true, message: '请选择计算日期', trigger: 'change' }],
  companyName: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
  stage: [{ required: true, message: '请选择阶段', trigger: 'change' }],
  eclAmount: [{ required: true, message: '请输入ECL金额', trigger: 'blur' }]
}

async function getList() {
  loading.value = true
  try {
    const res = await listEcl(queryParams)
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
  queryParams.stage = undefined
  queryParams.calcDate = undefined
  handleQuery()
}

function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id)
  multiple.value = !selection.length
}

function handleAdd() {
  dialogTitle.value = '新增ECL'
  form.value = { stage: 1, pd12m: 0, pdLifetime: 0, lgd: 45, carryingAmount: 0, ead: 0, eclAmount: 0, provisionRate: 0 }
  dialogVisible.value = true
}

function handleEdit(row) {
  dialogTitle.value = '编辑ECL'
  form.value = { ...row }
  dialogVisible.value = true
}

function handleDelete(row) {
  const deleteIds = row.id ? [row.id] : ids.value
  ElMessageBox.confirm('确认删除所选ECL记录？', '提示', { type: 'warning' }).then(async () => {
    await deleteEcl(deleteIds.join(','))
    ElMessage.success('删除成功')
    getList()
  })
}

async function submitForm() {
  if (form.value.id) {
    await updateEcl(form.value)
    ElMessage.success('修改成功')
  } else {
    await addEcl(form.value)
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

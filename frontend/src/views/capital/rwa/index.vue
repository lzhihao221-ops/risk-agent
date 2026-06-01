<template>
  <div class="app-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="mb20">
      <el-col :span="8">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value">{{ stats.totalRwa }} 万元</div>
            <div class="stat-label">总RWA</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value text-primary">{{ stats.standardRwa }} 万元</div>
            <div class="stat-label">标准法RWA</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value text-warning">{{ stats.irbRwa }} 万元</div>
            <div class="stat-label">内评法RWA</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 搜索区域 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="mb20">
      <el-form-item label="企业名称" prop="companyName">
        <el-input v-model="queryParams.companyName" placeholder="请输入企业名称" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="资产类别" prop="assetClass">
        <el-select v-model="queryParams.assetClass" placeholder="请选择" clearable>
          <el-option label="公司贷款" value="公司贷款" />
          <el-option label="个人贷款" value="个人贷款" />
          <el-option label="同业资产" value="同业资产" />
          <el-option label="债券投资" value="债券投资" />
          <el-option label="表外业务" value="表外业务" />
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
        <el-button type="primary" plain icon="Plus" @click="handleAdd">新增RWA</el-button>
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
      <el-table-column label="资产类别" prop="assetClass" width="110" align="center">
        <template #default="scope">
          <el-tag size="small">{{ scope.row.assetClass || '-' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="敞口金额(万元)" prop="exposureAmount" width="130" align="right" />
      <el-table-column label="PD(%)" prop="pd" width="90" align="center">
        <template #default="scope">{{ scope.row.pd ? Number(scope.row.pd).toFixed(2) : '-' }}</template>
      </el-table-column>
      <el-table-column label="LGD(%)" prop="lgd" width="90" align="center">
        <template #default="scope">{{ scope.row.lgd ? Number(scope.row.lgd).toFixed(2) : '-' }}</template>
      </el-table-column>
      <el-table-column label="EAD(万元)" prop="ead" width="110" align="right" />
      <el-table-column label="风险权重(%)" prop="riskWeight" width="110" align="center">
        <template #default="scope">{{ scope.row.riskWeight || '-' }}</template>
      </el-table-column>
      <el-table-column label="RWA金额(万元)" prop="rwaAmount" width="130" align="right">
        <template #default="scope">
          <span style="font-weight:bold;color:#409eff">{{ scope.row.rwaAmount || 0 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="计算方法" prop="calcMethod" width="110" align="center">
        <template #default="scope">
          <el-tag :type="scope.row.calcMethod === '标准法' ? 'primary' : 'warning'" size="small">
            {{ scope.row.calcMethod || '-' }}
          </el-tag>
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
            <el-form-item label="资产类别" prop="assetClass">
              <el-select v-model="form.assetClass" placeholder="请选择" style="width:100%">
                <el-option label="公司贷款" value="公司贷款" />
                <el-option label="个人贷款" value="个人贷款" />
                <el-option label="同业资产" value="同业资产" />
                <el-option label="债券投资" value="债券投资" />
                <el-option label="表外业务" value="表外业务" />
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
            <el-form-item label="敞口金额(万元)" prop="exposureAmount">
              <el-input-number v-model="form.exposureAmount" :min="0" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="EAD(万元)" prop="ead">
              <el-input-number v-model="form.ead" :min="0" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="PD(%)" prop="pd">
              <el-input-number v-model="form.pd" :min="0" :max="100" :step="0.01" :precision="4" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="LGD(%)" prop="lgd">
              <el-input-number v-model="form.lgd" :min="0" :max="100" :step="0.01" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="风险权重(%)" prop="riskWeight">
              <el-input-number v-model="form.riskWeight" :min="0" :max="1250" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="RWA金额(万元)" prop="rwaAmount">
              <el-input-number v-model="form.rwaAmount" :min="0" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="计算方法" prop="calcMethod">
              <el-select v-model="form.calcMethod" placeholder="请选择" style="width:100%">
                <el-option label="标准法" value="标准法" />
                <el-option label="内评法" value="内评法" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="期限(年)" prop="maturity">
              <el-input-number v-model="form.maturity" :min="0" :precision="2" style="width:100%" />
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
import { listRwa, addRwa, updateRwa, deleteRwa } from '@/api/capital'

const tableData = ref([])
const loading = ref(false)
const total = ref(0)
const ids = ref([])
const multiple = ref(true)
const dialogVisible = ref(false)
const dialogTitle = ref('新增RWA')

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  companyName: undefined,
  assetClass: undefined,
  calcDate: undefined
})

const form = ref({})

const stats = computed(() => {
  const list = tableData.value
  return {
    totalRwa: list.reduce((s, i) => s + Number(i.rwaAmount || 0), 0).toFixed(2),
    standardRwa: list.filter(i => i.calcMethod === '标准法').reduce((s, i) => s + Number(i.rwaAmount || 0), 0).toFixed(2),
    irbRwa: list.filter(i => i.calcMethod === '内评法').reduce((s, i) => s + Number(i.rwaAmount || 0), 0).toFixed(2)
  }
})

const rules = {
  calcDate: [{ required: true, message: '请选择计算日期', trigger: 'change' }],
  companyName: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
  assetClass: [{ required: true, message: '请选择资产类别', trigger: 'change' }],
  exposureAmount: [{ required: true, message: '请输入敞口金额', trigger: 'blur' }],
  rwaAmount: [{ required: true, message: '请输入RWA金额', trigger: 'blur' }]
}

async function getList() {
  loading.value = true
  try {
    const res = await listRwa(queryParams)
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
  queryParams.assetClass = undefined
  queryParams.calcDate = undefined
  handleQuery()
}

function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id)
  multiple.value = !selection.length
}

function handleAdd() {
  dialogTitle.value = '新增RWA'
  form.value = { pd: 0, lgd: 50, riskWeight: 100, exposureAmount: 0, ead: 0, rwaAmount: 0 }
  dialogVisible.value = true
}

function handleEdit(row) {
  dialogTitle.value = '编辑RWA'
  form.value = { ...row }
  dialogVisible.value = true
}

function handleDelete(row) {
  const deleteIds = row.id ? [row.id] : ids.value
  ElMessageBox.confirm('确认删除所选RWA记录？', '提示', { type: 'warning' }).then(async () => {
    await deleteRwa(deleteIds.join(','))
    ElMessage.success('删除成功')
    getList()
  })
}

async function submitForm() {
  if (form.value.id) {
    await updateRwa(form.value)
    ElMessage.success('修改成功')
  } else {
    await addRwa(form.value)
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
.stat-value.text-warning { color: #e6a23c; }
.stat-label { font-size: 14px; color: #909399; margin-top: 8px; }
</style>

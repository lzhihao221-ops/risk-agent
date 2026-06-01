<template>
  <div class="app-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="mb20">
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value">{{ stats.collecting }}</div>
            <div class="stat-label">催收中笔数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value">{{ stats.collectingAmount }} 万元</div>
            <div class="stat-label">催收中金额</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value">{{ stats.promiseAmount }} 万元</div>
            <div class="stat-label">承诺还款金额</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value">{{ stats.total }}</div>
            <div class="stat-label">催收总笔数</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 搜索区域 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="mb20">
      <el-form-item label="企业名称" prop="companyName">
        <el-input v-model="queryParams.companyName" placeholder="请输入企业名称" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="催收方式" prop="collectionType">
        <el-select v-model="queryParams.collectionType" placeholder="请选择催收方式" clearable>
          <el-option label="电话催收" value="电话催收" />
          <el-option label="上门催收" value="上门催收" />
          <el-option label="律师函" value="律师函" />
          <el-option label="诉讼催收" value="诉讼催收" />
          <el-option label="委外催收" value="委外催收" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option label="已结清" :value="0" />
          <el-option label="催收中" :value="1" />
          <el-option label="已升级" :value="2" />
          <el-option label="已委外" :value="3" />
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
        <el-button type="primary" plain icon="Plus" @click="handleAdd">新增催收</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete">删除</el-button>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="collectionList" @selection-change="handleSelectionChange" border stripe>
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="贷款编号" prop="loanNo" width="150" />
      <el-table-column label="企业名称" prop="companyName" min-width="180" show-overflow-tooltip />
      <el-table-column label="逾期金额(万元)" prop="overdueAmount" width="130" align="right" />
      <el-table-column label="逾期天数" prop="overdueDays" width="100" align="center">
        <template #default="scope">
          <el-tag :type="scope.row.overdueDays > 90 ? 'danger' : scope.row.overdueDays > 30 ? 'warning' : 'info'">
            {{ scope.row.overdueDays }}天
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="催收方式" prop="collectionType" width="110" align="center" />
      <el-table-column label="催收人" prop="collectorName" width="100" />
      <el-table-column label="催收结果" prop="collectionResult" min-width="180" show-overflow-tooltip />
      <el-table-column label="承诺还款" prop="promiseAmount" width="110" align="right">
        <template #default="scope">
          {{ scope.row.promiseAmount ? scope.row.promiseAmount + '万元' : '-' }}
        </template>
      </el-table-column>
      <el-table-column label="状态" prop="status" width="90" align="center">
        <template #default="scope">
          <el-tag :type="statusTagType(scope.row.status)">{{ statusLabel(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="250" align="center" fixed="right">
        <template #default="scope">
          <el-button link type="primary" icon="Document" @click="handleAddLog(scope.row)">新增记录</el-button>
          <el-button link type="warning" icon="Top" @click="handleUpgrade(scope.row)">升级催收</el-button>
          <el-button link type="primary" icon="View" @click="handleDetail(scope.row)">详情</el-button>
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

    <!-- 新增/编辑催收弹窗 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="600px" append-to-body>
      <el-form ref="collectionForm" :model="form" :rules="rules" label-width="120px">
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
            <el-form-item label="逾期金额(万元)" prop="overdueAmount">
              <el-input-number v-model="form.overdueAmount" :min="0" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="逾期天数" prop="overdueDays">
              <el-input-number v-model="form.overdueDays" :min="0" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="催收方式" prop="collectionType">
              <el-select v-model="form.collectionType" placeholder="请选择" style="width:100%">
                <el-option label="电话催收" value="电话催收" />
                <el-option label="上门催收" value="上门催收" />
                <el-option label="律师函" value="律师函" />
                <el-option label="诉讼催收" value="诉讼催收" />
                <el-option label="委外催收" value="委外催收" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="催收日期" prop="collectionDate">
              <el-date-picker v-model="form.collectionDate" type="date" value-format="YYYY-MM-DD" placeholder="选择日期" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="催收人" prop="collectorName">
              <el-input v-model="form.collectorName" placeholder="请输入催收人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="承诺还款金额" prop="promiseAmount">
              <el-input-number v-model="form.promiseAmount" :min="0" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="催收结果" prop="collectionResult">
          <el-input v-model="form.collectionResult" type="textarea" placeholder="请输入催收结果" />
        </el-form-item>
        <el-form-item label="下一步措施" prop="nextAction">
          <el-input v-model="form.nextAction" placeholder="请输入下一步措施" />
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

    <!-- 新增催收记录弹窗 -->
    <el-dialog title="新增催收记录" v-model="logDialogVisible" width="550px" append-to-body>
      <el-form ref="logForm" :model="logForm" :rules="logRules" label-width="100px">
        <el-form-item label="联系人" prop="contactPerson">
          <el-input v-model="logForm.contactPerson" placeholder="请输入联系人" />
        </el-form-item>
        <el-form-item label="联系电话" prop="contactPhone">
          <el-input v-model="logForm.contactPhone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="联系方式" prop="contactType">
          <el-select v-model="logForm.contactType" placeholder="请选择" style="width:100%">
            <el-option label="电话" value="电话" />
            <el-option label="短信" value="短信" />
            <el-option label="邮件" value="邮件" />
            <el-option label="上门" value="上门" />
          </el-select>
        </el-form-item>
        <el-form-item label="联系时间" prop="contactTime">
          <el-date-picker v-model="logForm.contactTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="选择时间" style="width:100%" />
        </el-form-item>
        <el-form-item label="联系结果" prop="contactResult">
          <el-input v-model="logForm.contactResult" type="textarea" placeholder="请输入联系结果" />
        </el-form-item>
        <el-form-item label="借款人反馈" prop="borrowerResponse">
          <el-input v-model="logForm.borrowerResponse" type="textarea" placeholder="请输入借款人反馈" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="logDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitLogForm">确 定</el-button>
      </template>
    </el-dialog>

    <!-- 升级催收弹窗 -->
    <el-dialog title="升级催收方式" v-model="upgradeDialogVisible" width="450px" append-to-body>
      <el-form ref="upgradeFormRef" :model="upgradeForm" :rules="upgradeRules" label-width="100px">
        <el-form-item label="催收方式" prop="collectionType">
          <el-select v-model="upgradeForm.collectionType" placeholder="请选择升级方式" style="width:100%">
            <el-option label="电话催收" value="电话催收" />
            <el-option label="上门催收" value="上门催收" />
            <el-option label="律师函" value="律师函" />
            <el-option label="诉讼催收" value="诉讼催收" />
            <el-option label="委外催收" value="委外催收" />
          </el-select>
        </el-form-item>
        <el-form-item label="下一步措施" prop="nextAction">
          <el-input v-model="upgradeForm.nextAction" type="textarea" placeholder="请输入下一步措施" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="upgradeDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitUpgrade">确 定</el-button>
      </template>
    </el-dialog>

    <!-- 详情弹窗（含催收记录） -->
    <el-dialog title="催收详情" v-model="detailDialogVisible" width="900px" append-to-body>
      <el-descriptions :column="2" border class="mb20">
        <el-descriptions-item label="贷款编号">{{ detailForm.loanNo }}</el-descriptions-item>
        <el-descriptions-item label="企业名称">{{ detailForm.companyName }}</el-descriptions-item>
        <el-descriptions-item label="逾期金额">{{ detailForm.overdueAmount }} 万元</el-descriptions-item>
        <el-descriptions-item label="逾期天数">{{ detailForm.overdueDays }} 天</el-descriptions-item>
        <el-descriptions-item label="催收方式">{{ detailForm.collectionType }}</el-descriptions-item>
        <el-descriptions-item label="催收人">{{ detailForm.collectorName }}</el-descriptions-item>
        <el-descriptions-item label="催收结果" :span="2">{{ detailForm.collectionResult }}</el-descriptions-item>
        <el-descriptions-item label="下一步措施">{{ detailForm.nextAction }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ statusLabel(detailForm.status) }}</el-descriptions-item>
      </el-descriptions>
      <el-divider content-position="left">催收记录</el-divider>
      <el-table :data="detailLogs" border stripe>
        <el-table-column label="联系人" prop="contactPerson" width="100" />
        <el-table-column label="联系电话" prop="contactPhone" width="130" />
        <el-table-column label="联系方式" prop="contactType" width="90" />
        <el-table-column label="联系时间" prop="contactTime" width="170" />
        <el-table-column label="联系结果" prop="contactResult" min-width="200" show-overflow-tooltip />
        <el-table-column label="借款人反馈" prop="borrowerResponse" min-width="200" show-overflow-tooltip />
        <el-table-column label="操作人" prop="operatorName" width="100" />
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  listCollection, getCollection, addCollection, updateCollection, delCollection,
  approveCollection, listCollectionLog, addCollectionLog
} from '@/api/loanCollection'

const collectionList = ref([])
const loading = ref(false)
const total = ref(0)
const ids = ref([])
const multiple = ref(true)
const dialogVisible = ref(false)
const logDialogVisible = ref(false)
const upgradeDialogVisible = ref(false)
const detailDialogVisible = ref(false)
const dialogTitle = ref('新增催收')

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  companyName: undefined,
  collectionType: undefined,
  status: undefined
})

const form = ref({})
const logForm = ref({})
const upgradeForm = ref({})
const detailForm = ref({})
const detailLogs = ref([])

const stats = computed(() => {
  const list = collectionList.value
  return {
    total: list.length,
    collecting: list.filter(i => i.status === 1).length,
    collectingAmount: list.filter(i => i.status === 1).reduce((s, i) => s + Number(i.overdueAmount || 0), 0).toFixed(2),
    promiseAmount: list.reduce((s, i) => s + Number(i.promiseAmount || 0), 0).toFixed(2)
  }
})

const rules = {
  loanId: [{ required: true, message: '请输入贷款ID', trigger: 'blur' }],
  loanNo: [{ required: true, message: '请输入贷款编号', trigger: 'blur' }],
  companyName: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
  collectionType: [{ required: true, message: '请选择催收方式', trigger: 'change' }]
}

const logRules = {
  contactPerson: [{ required: true, message: '请输入联系人', trigger: 'blur' }],
  contactType: [{ required: true, message: '请选择联系方式', trigger: 'change' }]
}

const upgradeRules = {
  collectionType: [{ required: true, message: '请选择催收方式', trigger: 'change' }],
  nextAction: [{ required: true, message: '请输入下一步措施', trigger: 'blur' }]
}

function statusLabel(status) {
  const map = { 0: '已结清', 1: '催收中', 2: '已升级', 3: '已委外' }
  return map[status] || '未知'
}

function statusTagType(status) {
  const map = { 0: 'success', 1: 'warning', 2: 'danger', 3: 'info' }
  return map[status] || 'info'
}

async function getList() {
  loading.value = true
  try {
    const res = await listCollection(queryParams)
    collectionList.value = res.rows || []
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
  queryParams.collectionType = undefined
  queryParams.status = undefined
  handleQuery()
}

function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id)
  multiple.value = !selection.length
}

function handleAdd() {
  dialogTitle.value = '新增催收'
  form.value = { status: 1 }
  dialogVisible.value = true
}

function handleAddLog(row) {
  logForm.value = {
    collectionId: row.id,
    loanId: row.loanId,
    loanNo: row.loanNo,
    operatorName: 'admin'
  }
  logDialogVisible.value = true
}

function handleUpgrade(row) {
  upgradeForm.value = { id: row.id, collectionType: row.collectionType, nextAction: '' }
  upgradeDialogVisible.value = true
}

async function handleDetail(row) {
  const res = await getCollection(row.id)
  detailForm.value = res.data || {}
  detailLogs.value = detailForm.value.collectionLogs || []
  detailDialogVisible.value = true
}

function handleDelete(row) {
  const deleteIds = row.id ? [row.id] : ids.value
  ElMessageBox.confirm('确认删除所选催收记录？', '提示', { type: 'warning' }).then(async () => {
    await delCollection(deleteIds.join(','))
    ElMessage.success('删除成功')
    getList()
  })
}

async function submitForm() {
  await addCollection(form.value)
  ElMessage.success('新增成功')
  dialogVisible.value = false
  getList()
}

async function submitLogForm() {
  await addCollectionLog(logForm.value)
  ElMessage.success('催收记录添加成功')
  logDialogVisible.value = false
}

async function submitUpgrade() {
  await approveCollection(upgradeForm.value.id, upgradeForm.value.collectionType, upgradeForm.value.nextAction)
  ElMessage.success('升级成功')
  upgradeDialogVisible.value = false
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

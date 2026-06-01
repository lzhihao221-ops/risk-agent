<template>
  <div class="app-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="mb20">
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value">{{ stats.total }}</div>
            <div class="stat-label">查询总笔数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value text-success">{{ stats.avgScore }}</div>
            <div class="stat-label">平均征信评分</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value text-warning">{{ stats.badCount }}</div>
            <div class="stat-label">有不良记录数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-value">{{ stats.totalDebt }} 万元</div>
            <div class="stat-label">负债总额</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 搜索区域 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" class="mb20">
      <el-form-item label="企业名称" prop="companyName">
        <el-input v-model="queryParams.companyName" placeholder="请输入企业名称" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="查询类型" prop="queryType">
        <el-select v-model="queryParams.queryType" placeholder="请选择" clearable>
          <el-option v-for="t in queryTypes" :key="t" :label="t" :value="t" />
        </el-select>
      </el-form-item>
      <el-form-item label="查询来源" prop="querySource">
        <el-select v-model="queryParams.querySource" placeholder="请选择" clearable>
          <el-option v-for="s in querySources" :key="s" :label="s" :value="s" />
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
        <el-button type="primary" plain icon="Plus" @click="handleAdd">新增查询</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete">删除</el-button>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="tableData" @selection-change="handleSelectionChange" border stripe>
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="企业名称" prop="companyName" min-width="180" show-overflow-tooltip />
      <el-table-column label="查询类型" prop="queryType" width="110" align="center" />
      <el-table-column label="查询来源" prop="querySource" width="110" align="center" />
      <el-table-column label="征信评分" width="100" align="center">
        <template #default="{row}">
          <span :style="{color: row.creditScore>=60?'#67C23A':row.creditScore>=40?'#E6A23C':'#F56C6C', fontWeight:'bold', fontSize:'16px'}">
            {{ row.creditScore || '-' }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="负债总额(万)" prop="debtAmount" width="120" align="right" />
      <el-table-column label="逾期次数" width="100" align="center">
        <template #default="{row}">
          <el-tag :type="row.overdueCount>0?'danger':'success'" size="small">{{ row.overdueCount || 0 }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="不良记录" width="100" align="center">
        <template #default="{row}">
          <el-tag :type="row.hasBadRecord===1?'danger':'success'" size="small">{{ row.hasBadRecord===1?'有':'无' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作人" prop="operatorName" width="100" />
      <el-table-column label="查询日期" width="120" align="center">
        <template #default="{row}">{{ row.queryDate ? row.queryDate.substring(0,10) : '-' }}</template>
      </el-table-column>
      <el-table-column label="操作" width="150" align="center" fixed="right">
        <template #default="{row}">
          <el-button link type="primary" icon="View" @click="viewDetail(row)">详情</el-button>
          <el-button link type="danger" icon="Delete" @click="handleDelete(row)">删除</el-button>
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

    <!-- 新增/详情弹窗 -->
    <el-dialog v-model="dialogVisible" :title="isDetail ? '查询详情' : '新增征信查询'" width="600px" append-to-body>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="110px" :disabled="isDetail">
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
            <el-form-item label="查询类型" prop="queryType">
              <el-select v-model="form.queryType" style="width:100%">
                <el-option v-for="t in queryTypes" :key="t" :label="t" :value="t" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="查询来源" prop="querySource">
              <el-select v-model="form.querySource" style="width:100%">
                <el-option v-for="s in querySources" :key="s" :label="s" :value="s" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="查询日期" prop="queryDate">
              <el-date-picker v-model="form.queryDate" type="date" value-format="YYYY-MM-DD" placeholder="选择日期" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="征信评分" prop="creditScore">
              <el-input-number v-model="form.creditScore" :min="0" :max="100" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="负债总额(万)" prop="debtAmount">
              <el-input-number v-model="form.debtAmount" :min="0" :step="10" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="逾期次数" prop="overdueCount">
              <el-input-number v-model="form.overdueCount" :min="0" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="近6月查询次数" prop="queryCount6m">
              <el-input-number v-model="form.queryCount6m" :min="0" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="不良记录" prop="hasBadRecord">
              <el-radio-group v-model="form.hasBadRecord">
                <el-radio :value="0">无</el-radio>
                <el-radio :value="1">有</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="操作人" prop="operatorName">
              <el-input v-model="form.operatorName" placeholder="请输入操作人" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer v-if="!isDetail">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { listCreditQuery, addCreditQuery, deleteCreditQuery } from '@/api/loanCredit'
import { ElMessage, ElMessageBox } from 'element-plus'

const queryParams = reactive({ companyName: '', queryType: '', querySource: '', pageNum: 1, pageSize: 10 })
const tableData = ref([])
const total = ref(0)
const loading = ref(false)
const ids = ref([])
const multiple = ref(true)
const dialogVisible = ref(false)
const isDetail = ref(false)
const form = ref({})

const queryTypes = ['贷前审批', '贷后管理', '担保审查', '异议核查']
const querySources = ['人行征信', '百行征信', '内部系统']

const stats = computed(() => {
  const list = tableData.value
  const scores = list.filter(i => i.creditScore).map(i => Number(i.creditScore))
  return {
    total: list.length,
    avgScore: scores.length ? (scores.reduce((a, b) => a + b, 0) / scores.length).toFixed(1) : '-',
    badCount: list.filter(i => i.hasBadRecord === 1).length,
    totalDebt: list.reduce((s, i) => s + Number(i.debtAmount || 0), 0).toFixed(2)
  }
})

const rules = {
  companyName: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
  queryType: [{ required: true, message: '请选择查询类型', trigger: 'change' }]
}

async function getList() {
  loading.value = true
  try {
    const res = await listCreditQuery(queryParams)
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
  queryParams.companyName = ''
  queryParams.queryType = ''
  queryParams.querySource = ''
  handleQuery()
}

function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id)
  multiple.value = !selection.length
}

function handleAdd() {
  form.value = { hasBadRecord: 0 }
  isDetail.value = false
  dialogVisible.value = true
}

function viewDetail(row) {
  form.value = { ...row }
  isDetail.value = true
  dialogVisible.value = true
}

async function submitForm() {
  await addCreditQuery(form.value)
  ElMessage.success('操作成功')
  dialogVisible.value = false
  getList()
}

function handleDelete(row) {
  const deleteIds = row.id ? [row.id] : ids.value
  ElMessageBox.confirm('确认删除所选征信查询记录？', '提示', { type: 'warning' }).then(async () => {
    await deleteCreditQuery(deleteIds.join(','))
    ElMessage.success('删除成功')
    getList()
  })
}

onMounted(getList)
</script>

<style scoped>
.mb20 { margin-bottom: 20px; }
.mt20 { margin-top: 20px; }
.stat-item { text-align: center; padding: 10px 0; }
.stat-value { font-size: 28px; font-weight: bold; color: #409eff; }
.stat-value.text-success { color: #67c23a; }
.stat-value.text-warning { color: #e6a23c; }
.stat-label { font-size: 14px; color: #909399; margin-top: 8px; }
</style>

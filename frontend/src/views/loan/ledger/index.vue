<template>
  <div class="app-container">
    <!-- 统计 -->
    <el-row :gutter="16" style="margin-bottom:16px">
      <el-col :span="6">
        <el-card shadow="hover"><el-statistic title="贷款笔数" :value="summary.loanCount || 0" /></el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover"><el-statistic title="贷款总额(万)" :value="summary.totalAmount || 0" /></el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover"><el-statistic title="贷款余额(万)" :value="summary.totalBalance || 0" /></el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover"><el-statistic title="平均利率(%)" :value="summary.avgRate || 0" :precision="2" /></el-card>
      </el-col>
    </el-row>

    <el-card>
      <template #header>
        <div style="display:flex;justify-content:space-between;align-items:center">
          <span>贷款台账</span>
          <el-button type="primary" @click="showAdd"><el-icon><Plus /></el-icon> 新增台账</el-button>
        </div>
      </template>

      <el-form :inline="true" style="margin-bottom:16px">
        <el-form-item label="企业"><el-input v-model="query.companyName" clearable /></el-form-item>
        <el-form-item label="状态">
          <el-select v-model="query.status" clearable style="width:120px">
            <el-option label="正常" :value="1" /><el-option label="逾期" :value="2" /><el-option label="已结清" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item><el-button type="primary" @click="loadData">查询</el-button></el-form-item>
      </el-form>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column label="贷款编号" prop="loanNo" width="140" />
        <el-table-column label="企业" prop="companyName" show-overflow-tooltip />
        <el-table-column label="放款金额(万)" prop="loanAmount" width="110" align="right" />
        <el-table-column label="余额(万)" prop="loanBalance" width="100" align="right" />
        <el-table-column label="利率(%)" prop="interestRate" width="80" align="center" />
        <el-table-column label="五级分类" width="90">
          <template #default="{row}">
            <el-tag :type="categoryTag(row.fiveCategory)" size="small">{{ categoryLabel(row.fiveCategory) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="逾期天数" width="90" align="center">
          <template #default="{row}">
            <span :style="{color: row.overdueDays>0?'#F56C6C':'#67C23A'}">{{ row.overdueDays || 0 }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="80">
          <template #default="{row}">
            <el-tag :type="row.status===1?'success':row.status===2?'danger':'info'" size="small">
              {{ row.status===0?'已结清':row.status===1?'正常':'逾期' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="{row}">
            <el-button text type="primary" size="small" @click="viewRepayment(row)">还款</el-button>
            <el-button text type="warning" size="small" @click="viewDetail(row)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top:16px;justify-content:flex-end" v-model:current-page="query.pageNum" v-model:page-size="query.pageSize" :total="total" layout="total,prev,pager,next" @current-change="loadData" />
    </el-card>

    <!-- 还款计划弹窗 -->
    <el-dialog v-model="repayVisible" title="还款计划" width="800px">
      <el-table :data="repayments" stripe>
        <el-table-column label="期数" prop="periodNo" width="60" />
        <el-table-column label="计划还款日" prop="planDate" width="110" />
        <el-table-column label="计划本金(万)" prop="planPrincipal" width="110" align="right" />
        <el-table-column label="计划利息(万)" prop="planInterest" width="110" align="right" />
        <el-table-column label="合计(万)" prop="planTotal" width="100" align="right" />
        <el-table-column label="状态" width="80">
          <template #default="{row}">
            <el-tag :type="row.status===1?'success':row.status===2?'danger':'info'" size="small">
              {{ row.status===0?'待还':row.status===1?'已还':'逾期' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <!-- 新增台账弹窗 -->
    <el-dialog v-model="addVisible" title="新增贷款台账" width="600px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="企业名称"><el-input v-model="form.companyName" /></el-form-item>
        <el-form-item label="贷款编号"><el-input v-model="form.loanNo" /></el-form-item>
        <el-form-item label="放款金额(万)"><el-input-number v-model="form.loanAmount" :min="0" style="width:100%" /></el-form-item>
        <el-form-item label="利率(%)"><el-input-number v-model="form.interestRate" :min="0" :step="0.01" :precision="2" style="width:100%" /></el-form-item>
        <el-form-item label="放款日期"><el-date-picker v-model="form.loanStartDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item>
        <el-form-item label="到期日期"><el-date-picker v-model="form.loanEndDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addVisible=false">取消</el-button>
        <el-button type="primary" @click="submitLedger">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { listLedger, addLedger, ledgerSummary, getRepayments } from '@/api/loan'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'

const query = ref({ companyName: '', status: null, pageNum: 1, pageSize: 20 })
const tableData = ref([])
const total = ref(0)
const loading = ref(false)
const summary = ref({})
const repayVisible = ref(false)
const repayments = ref([])
const addVisible = ref(false)
const form = ref({})

const categories = [
  { label: '正常', value: 1 }, { label: '关注', value: 2 }, { label: '次级', value: 3 },
  { label: '可疑', value: 4 }, { label: '损失', value: 5 }
]
function categoryTag(c) { return { 1:'success', 2:'warning', 3:'danger', 4:'danger', 5:'danger' }[c] || 'info' }
function categoryLabel(c) { return categories.find(x => x.value === c)?.label || '-' }

async function loadData() {
  loading.value = true
  try {
    const res = await listLedger(query.value)
    tableData.value = res.rows || []
    total.value = res.total || 0
    const s = await ledgerSummary()
    summary.value = s.data || s || {}
  } catch(e) { console.error(e) }
  loading.value = false
}

function showAdd() {
  form.value = { loanAmount: 0, interestRate: 4.35, loanTerm: 12, fiveCategory: 1, status: 1 }
  addVisible.value = true
}

async function submitLedger() {
  form.value.loanBalance = form.value.loanAmount
  await addLedger(form.value)
  ElMessage.success('台账已创建')
  addVisible.value = false
  loadData()
}

async function viewRepayment(row) {
  try {
    const res = await getRepayments(row.id)
    repayments.value = res.data || res || []
    repayVisible.value = true
  } catch(e) { console.error(e) }
}

function viewDetail(row) {
  ElMessage.info(`贷款详情: ${row.loanNo}`)
}

onMounted(loadData)
</script>

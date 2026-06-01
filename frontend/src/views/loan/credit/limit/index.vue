<template>
  <div class="app-container">
    <el-card>
      <template #header>
        <div style="display:flex;justify-content:space-between;align-items:center">
          <span>授信额度管理</span>
          <el-button type="primary" @click="showAdd"><el-icon><Plus /></el-icon> 新增额度</el-button>
        </div>
      </template>
      <el-form :inline="true" style="margin-bottom:16px">
        <el-form-item label="企业名称">
          <el-input v-model="query.companyName" clearable placeholder="搜索企业" />
        </el-form-item>
        <el-form-item label="授信等级">
          <el-select v-model="query.creditGrade" clearable placeholder="全部" style="width:100px">
            <el-option v-for="g in grades" :key="g" :label="g" :value="g" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="query.status" clearable placeholder="全部" style="width:100px">
            <el-option v-for="s in statusOptions" :key="s.value" :label="s.label" :value="s.value" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData">查询</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column label="企业名称" prop="companyName" show-overflow-tooltip />
        <el-table-column label="总授信额度(万)" prop="totalLimit" width="130" align="right" />
        <el-table-column label="已用额度(万)" prop="usedLimit" width="120" align="right" />
        <el-table-column label="可用额度" width="200">
          <template #default="{row}">
            <div style="display:flex;align-items:center;gap:8px">
              <el-progress
                :percentage="usagePercent(row)"
                :color="usageColor(row)"
                :stroke-width="14"
                style="flex:1"
              />
              <span style="min-width:60px;text-align:right">{{ row.availableLimit }}万</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="授信等级" prop="creditGrade" width="90" align="center">
          <template #default="{row}">
            <el-tag :type="gradeTag(row.creditGrade)" size="small">{{ row.creditGrade }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="有效期" width="200">
          <template #default="{row}">
            {{ row.validFrom ? row.validFrom.substring(0,10) : '-' }} ~ {{ row.validTo ? row.validTo.substring(0,10) : '-' }}
          </template>
        </el-table-column>
        <el-table-column label="状态" width="80" align="center">
          <template #default="{row}">
            <el-tag :type="row.status===1?'success':row.status===0?'danger':'warning'" size="small">
              {{ statusOptions.find(s => s.value === row.status)?.label || '未知' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180">
          <template #default="{row}">
            <el-button text type="primary" size="small" @click="showEdit(row)">调整额度</el-button>
            <el-button text :type="row.status===1?'warning':'success'" size="small" @click="toggleFreeze(row)">
              {{ row.status===1 ? '冻结' : '解冻' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top:16px;justify-content:flex-end" v-model:current-page="query.pageNum" v-model:page-size="query.pageSize" :total="total" layout="total,prev,pager,next" @current-change="loadData" />
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="form.id ? '调整授信额度' : '新增授信额度'" width="600px">
      <el-form :model="form" label-width="110px">
        <el-form-item label="企业名称"><el-input v-model="form.companyName" /></el-form-item>
        <el-form-item label="总授信额度(万)"><el-input-number v-model="form.totalLimit" :min="0" :step="100" style="width:100%" /></el-form-item>
        <el-form-item label="已用额度(万)"><el-input-number v-model="form.usedLimit" :min="0" :step="10" style="width:100%" /></el-form-item>
        <el-form-item label="授信等级">
          <el-select v-model="form.creditGrade" style="width:100%">
            <el-option v-for="g in grades" :key="g" :label="g" :value="g" />
          </el-select>
        </el-form-item>
        <el-form-item label="有效期起"><el-date-picker v-model="form.validFrom" type="date" style="width:100%" /></el-form-item>
        <el-form-item label="有效期止"><el-date-picker v-model="form.validTo" type="date" style="width:100%" /></el-form-item>
        <el-form-item label="下次审查日"><el-date-picker v-model="form.reviewDate" type="date" style="width:100%" /></el-form-item>
        <el-form-item label="备注"><el-input v-model="form.remark" type="textarea" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { listCreditLimit, addCreditLimit, updateCreditLimit } from '@/api/loanCredit'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'

const query = ref({ companyName: '', creditGrade: '', status: null, pageNum: 1, pageSize: 20 })
const tableData = ref([])
const total = ref(0)
const loading = ref(false)
const dialogVisible = ref(false)
const form = ref({})

const grades = ['AAA', 'AA', 'A', 'BBB', 'BB', 'B', 'CCC', 'CC', 'C']
const statusOptions = [
  { label: '冻结', value: 0 },
  { label: '正常', value: 1 },
  { label: '到期', value: 2 }
]

function usagePercent(row) {
  if (!row.totalLimit || row.totalLimit <= 0) return 0
  return Math.min(100, Math.round((row.usedLimit || 0) / row.totalLimit * 100))
}

function usageColor(row) {
  const pct = usagePercent(row)
  if (pct > 80) return '#F56C6C'
  if (pct > 60) return '#E6A23C'
  return '#67C23A'
}

function gradeTag(g) {
  if (!g) return 'info'
  if (g.startsWith('A')) return 'success'
  if (g.startsWith('B')) return 'warning'
  return 'danger'
}

async function loadData() {
  loading.value = true
  try {
    const res = await listCreditLimit(query.value)
    tableData.value = res.rows || []
    total.value = res.total || 0
  } catch(e) { console.error(e) }
  loading.value = false
}

function showAdd() {
  form.value = { status: 1, usedLimit: 0 }
  dialogVisible.value = true
}

function showEdit(row) {
  form.value = { ...row }
  dialogVisible.value = true
}

async function submitForm() {
  try {
    if (form.value.id) {
      await updateCreditLimit(form.value)
    } else {
      form.value.availableLimit = (form.value.totalLimit || 0) - (form.value.usedLimit || 0)
      await addCreditLimit(form.value)
    }
    ElMessage.success('操作成功')
    dialogVisible.value = false
    loadData()
  } catch(e) { ElMessage.error('操作失败') }
}

async function toggleFreeze(row) {
  const action = row.status === 1 ? '冻结' : '解冻'
  await ElMessageBox.confirm(`确定${action}该企业授信额度？`, '提示', { type: 'warning' })
  await updateCreditLimit({ id: row.id, status: row.status === 1 ? 0 : 1 })
  ElMessage.success(`${action}成功`)
  loadData()
}

onMounted(loadData)
</script>

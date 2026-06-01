<template>
  <div class="app-container">
    <el-form :model="queryParams" :inline="true">
      <el-form-item label="事件类型" prop="eventType">
        <el-select v-model="queryParams.eventType" placeholder="全部" clearable style="width:140px">
          <el-option v-for="t in eventTypes" :key="t.value" :label="t.label" :value="t.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="严重程度" prop="severity">
        <el-select v-model="queryParams.severity" placeholder="全部" clearable style="width:120px">
          <el-option label="高" :value="3" /><el-option label="中" :value="2" /><el-option label="低" :value="1" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery"><el-icon><Search /></el-icon>搜索</el-button>
        <el-button @click="resetQuery"><el-icon><Refresh /></el-icon>重置</el-button>
        <el-button @click="handleExport"><el-icon><Download /></el-icon>导出</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="eventList" border stripe>
      <el-table-column prop="eventTitle" label="事件标题" min-width="250" show-overflow-tooltip />
      <el-table-column prop="eventType" label="类型" width="120">
        <template #default="{ row }">{{ eventTypeMap[row.eventType] || row.eventType }}</template>
      </el-table-column>
      <el-table-column prop="severity" label="严重程度" width="90" align="center">
        <template #default="{ row }">
          <el-tag :type="row.severity === 3 ? 'danger' : row.severity === 2 ? 'warning' : 'info'" size="small">
            {{ row.severity === 3 ? '高' : row.severity === 2 ? '中' : '低' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="amount" label="涉及金额" width="120" align="right">
        <template #default="{ row }">{{ row.amount ? row.amount.toLocaleString() + '元' : '-' }}</template>
      </el-table-column>
      <el-table-column prop="eventDate" label="事件日期" width="120" />
      <el-table-column prop="isRead" label="已读" width="70" align="center">
        <template #default="{ row }"><el-tag :type="row.isRead ? 'success' : 'info'" size="small">{{ row.isRead ? '是' : '否' }}</el-tag></template>
      </el-table-column>
      <el-table-column prop="isHandled" label="已处理" width="80" align="center">
        <template #default="{ row }"><el-tag :type="row.isHandled ? 'success' : 'warning'" size="small">{{ row.isHandled ? '是' : '否' }}</el-tag></template>
      </el-table-column>
      <el-table-column prop="createTime" label="录入时间" width="160" />
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getEventList } from '@/api/risk'
import { download } from '@/utils/request'
import { Search, Refresh, Download } from '@element-plus/icons-vue'

const eventTypes = [
  { label: '被执行', value: 'EXECUTION' },
  { label: '失信被执行', value: 'DISHONEST' },
  { label: '经营异常', value: 'ABNORMAL' },
  { label: '涉诉', value: 'LAWSUIT' },
  { label: '法人变更', value: 'CHANGE_LEGAL' },
  { label: '股权冻结', value: 'FREEZE' }
]
const eventTypeMap = { EXECUTION: '被执行', DISHONEST: '失信被执行', ABNORMAL: '经营异常', LAWSUIT: '涉诉', CHANGE_LEGAL: '法人变更', FREEZE: '股权冻结' }

const loading = ref(false)
const eventList = ref([])
const total = ref(0)
const queryParams = ref({ pageNum: 1, pageSize: 20, eventType: undefined, severity: undefined })

async function getList() {
  loading.value = true
  try { const { rows, total: t } = await getEventList(queryParams.value); eventList.value = rows || []; total.value = t || 0 } catch (e) { eventList.value = []; total.value = 0 }
  loading.value = false
}
function handleQuery() { queryParams.value.pageNum = 1; getList() }
function resetQuery() { queryParams.value = { pageNum: 1, pageSize: 20, eventType: undefined, severity: undefined }; getList() }

function handleExport() {
  download('/risk/event/export', { ...queryParams }, `风险事件_${new Date().getTime()}.xlsx`)
}

onMounted(() => getList())
</script>

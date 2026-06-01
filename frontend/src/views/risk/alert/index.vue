<template>
  <div class="app-container">
    <!-- 筛选栏 -->
    <el-form :inline="true" style="margin-bottom:16px">
      <el-form-item label="预警等级">
        <el-select v-model="query.alertLevel" clearable placeholder="全部" style="width:100px">
          <el-option label="高" :value="3" /><el-option label="中" :value="2" /><el-option label="低" :value="1" />
        </el-select>
      </el-form-item>
      <el-form-item label="处理状态">
        <el-select v-model="query.isHandled" clearable placeholder="全部" style="width:100px">
          <el-option label="未处理" :value="0" /><el-option label="已处理" :value="1" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="getList"><el-icon><Search /></el-icon>搜索</el-button>
        <el-button @click="resetQuery"><el-icon><Refresh /></el-icon>重置</el-button>
        <el-button @click="handleExport"><el-icon><Download /></el-icon>导出</el-button>
      </el-form-item>
    </el-form>

    <!-- 预警统计 -->
    <el-row :gutter="16" style="margin-bottom:20px">
      <el-col :span="8">
        <el-card shadow="hover" :body-style="{padding:'16px'}">
          <div style="display:flex;align-items:center">
            <el-icon style="font-size:32px;color:#F56C6C;margin-right:12px"><Warning /></el-icon>
            <div><div style="font-size:12px;color:#909399">高级预警</div><div style="font-size:24px;font-weight:bold;color:#F56C6C">{{ levelCounts[3] || 0 }}</div></div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" :body-style="{padding:'16px'}">
          <div style="display:flex;align-items:center">
            <el-icon style="font-size:32px;color:#E6A23C;margin-right:12px"><Warning /></el-icon>
            <div><div style="font-size:12px;color:#909399">中级预警</div><div style="font-size:24px;font-weight:bold;color:#E6A23C">{{ levelCounts[2] || 0 }}</div></div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" :body-style="{padding:'16px'}">
          <div style="display:flex;align-items:center">
            <el-icon style="font-size:32px;color:#409EFF;margin-right:12px"><Warning /></el-icon>
            <div><div style="font-size:12px;color:#909399">低级预警</div><div style="font-size:24px;font-weight:bold;color:#409EFF">{{ levelCounts[1] || 0 }}</div></div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 列表 -->
    <el-table :data="alertList" border stripe v-loading="loading">
      <el-table-column type="index" label="序号" width="60" />
      <el-table-column label="预警等级" width="80" align="center">
        <template #default="{ row }">
          <el-tag :type="row.alertLevel === 3 ? 'danger' : row.alertLevel === 2 ? 'warning' : 'info'" effect="dark" size="small">
            {{ row.alertLevel === 3 ? '高级' : row.alertLevel === 2 ? '中级' : '低级' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="alertTitle" label="预警标题" min-width="200" show-overflow-tooltip />
      <el-table-column prop="alertContent" label="预警内容" min-width="250" show-overflow-tooltip />
      <el-table-column label="处理状态" width="90" align="center">
        <template #default="{ row }">
          <el-tag :type="row.isHandled === 1 ? 'success' : 'danger'" size="small">
            {{ row.isHandled === 1 ? '已处理' : '待处理' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="handlerName" label="处理人" width="80" />
      <el-table-column prop="createTime" label="触发时间" width="160" />
      <el-table-column label="操作" width="120" align="center" fixed="right">
        <template #default="{ row }">
          <el-button v-if="row.isHandled !== 1" type="success" link size="small" @click="handleAlert(row)">处理</el-button>
          <el-button type="primary" link size="small" @click="showDetail(row)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination style="margin-top:16px;justify-content:flex-end" v-model:current-page="query.pageNum" v-model:page-size="query.pageSize"
      :total="total" :page-sizes="[10,20,50]" layout="total, sizes, prev, pager, next" @size-change="getList" @current-change="getList" />

    <!-- 处理对话框 -->
    <el-dialog v-model="handleVisible" title="处理预警" width="500px">
      <el-form label-width="80px">
        <el-form-item label="预警标题">{{ currentAlert.alertTitle }}</el-form-item>
        <el-form-item label="处理结果" required>
          <el-input v-model="handleResult" type="textarea" :rows="4" placeholder="请输入处理结果说明..." />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="handleVisible = false">取消</el-button>
        <el-button type="primary" @click="submitHandle">确认处理</el-button>
      </template>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog v-model="detailVisible" title="预警详情" width="600px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="预警等级" :span="2">
          <el-tag :type="detailRow.alertLevel === 3 ? 'danger' : 'warning'" effect="dark">
            {{ detailRow.alertLevel === 3 ? '高级' : detailRow.alertLevel === 2 ? '中级' : '低级' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="预警标题" :span="2">{{ detailRow.alertTitle }}</el-descriptions-item>
        <el-descriptions-item label="预警内容" :span="2">{{ detailRow.alertContent }}</el-descriptions-item>
        <el-descriptions-item label="处理状态">
          <el-tag :type="detailRow.isHandled === 1 ? 'success' : 'danger'">{{ detailRow.isHandled === 1 ? '已处理' : '待处理' }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="处理人">{{ detailRow.handlerName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="处理时间">{{ detailRow.handleTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="触发时间">{{ detailRow.createTime }}</el-descriptions-item>
        <el-descriptions-item label="处理结果" :span="2">{{ detailRow.handleResult || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getAlertList, handleAlert as apiHandle } from '@/api/risk'
import { download } from '@/utils/request'
import { ElMessage } from 'element-plus'
import { Search, Refresh, Warning, Download } from '@element-plus/icons-vue'

const alertList = ref([])
const total = ref(0)
const loading = ref(false)
const query = ref({ pageNum: 1, pageSize: 10, alertLevel: null, isHandled: null })
const levelCounts = ref({})

const handleVisible = ref(false)
const currentAlert = ref({})
const handleResult = ref('')

const detailVisible = ref(false)
const detailRow = ref({})

async function getList() {
  loading.value = true
  try {
    const res = await getAlertList(query.value)
    alertList.value = res.rows || res.data?.rows || []
    total.value = res.total || res.data?.total || 0
    // Calculate level counts
    const all = alertList.value
    levelCounts.value = { 1: all.filter(a => a.alertLevel === 1).length, 2: all.filter(a => a.alertLevel === 2).length, 3: all.filter(a => a.alertLevel === 3).length }
  } catch (e) { console.error(e) }
  loading.value = false
}

function resetQuery() {
  query.value = { pageNum: 1, pageSize: 10, alertLevel: null, isHandled: null }
  getList()
}

function handleAlert(row) {
  currentAlert.value = row
  handleResult.value = ''
  handleVisible.value = true
}

async function submitHandle() {
  if (!handleResult.value.trim()) { ElMessage.warning('请输入处理结果'); return }
  try {
    await apiHandle(currentAlert.value.id, handleResult.value)
    ElMessage.success('处理成功')
    handleVisible.value = false
    getList()
  } catch (e) { ElMessage.error('处理失败') }
}

function showDetail(row) {
  detailRow.value = row
  detailVisible.value = true
}

function handleExport() {
  download('/risk/alert/export', { ...query }, `预警数据_${new Date().getTime()}.xlsx`)
}

onMounted(getList)
</script>

<template>
  <div class="app-container">
    <!-- 消息统计 -->
    <el-row :gutter="16" style="margin-bottom:16px">
      <el-col :span="8">
        <el-card shadow="hover">
          <el-statistic title="未读消息" :value="unreadCount">
            <template #suffix><el-icon style="color:#F56C6C"><Bell /></el-icon></template>
          </el-statistic>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <el-statistic title="全部消息" :value="totalCount">
            <template #suffix><el-icon style="color:#409EFF"><Bell /></el-icon></template>
          </el-statistic>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <div style="text-align:center;padding:10px 0">
            <el-button type="primary" @click="markAllRead">全部标记已读</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 消息列表 -->
    <el-card>
      <template #header>
        <div style="display:flex;justify-content:space-between;align-items:center">
          <span>消息中心</span>
          <div>
            <el-select v-model="query.msgType" clearable placeholder="全部类型" style="width:120px;margin-right:8px">
              <el-option label="预警通知" :value="1" />
              <el-option label="任务提醒" :value="2" />
              <el-option label="系统通知" :value="3" />
            </el-select>
            <el-select v-model="query.isRead" clearable placeholder="全部状态" style="width:120px;margin-right:8px">
              <el-option label="未读" :value="0" />
              <el-option label="已读" :value="1" />
            </el-select>
            <el-button type="primary" @click="loadMessages"><el-icon><Search /></el-icon>搜索</el-button>
          </div>
        </div>
      </template>

      <el-table :data="messages" v-loading="loading" max-height="600">
        <el-table-column width="40">
          <template #default="{ row }">
            <el-badge :is-dot="row.isRead === 0" :hidden="row.isRead === 1" />
          </template>
        </el-table-column>
        <el-table-column label="类型" width="100">
          <template #default="{ row }">
            <el-tag :type="row.msgType === 1 ? 'danger' : row.msgType === 2 ? 'warning' : 'info'" size="small">
              {{ row.msgType === 1 ? '预警通知' : row.msgType === 2 ? '任务提醒' : '系统通知' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="标题" prop="title" show-overflow-tooltip min-width="200" />
        <el-table-column label="时间" prop="createTime" width="170" />
        <el-table-column label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.isRead === 1 ? 'success' : 'danger'" size="small">
              {{ row.isRead === 1 ? '已读' : '未读' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button text type="primary" size="small" @click="showDetail(row)">查看</el-button>
            <el-button v-if="row.isRead === 0" text type="success" size="small" @click="markRead(row)">已读</el-button>
            <el-button text type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-if="total > 0"
        style="margin-top:16px;justify-content:flex-end"
        v-model:current-page="query.pageNum"
        v-model:page-size="query.pageSize"
        :total="total"
        :page-sizes="[10, 20, 50]"
        layout="total, sizes, prev, pager, next"
        @size-change="loadMessages"
        @current-change="loadMessages"
      />
    </el-card>

    <!-- 消息详情 -->
    <el-dialog v-model="detailVisible" title="消息详情" width="500px">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="标题">{{ detail.title }}</el-descriptions-item>
        <el-descriptions-item label="类型">
          <el-tag :type="detail.msgType === 1 ? 'danger' : detail.msgType === 2 ? 'warning' : 'info'" size="small">
            {{ detail.msgType === 1 ? '预警通知' : detail.msgType === 2 ? '任务提醒' : '系统通知' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="时间">{{ detail.createTime }}</el-descriptions-item>
        <el-descriptions-item label="内容">{{ detail.content }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMessageList, getUnreadMessages, getUnreadCount, markMessageRead, markAllRead as apiMarkAllRead, deleteMessage } from '@/api/risk'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Bell } from '@element-plus/icons-vue'

const messages = ref([])
const total = ref(0)
const loading = ref(false)
const unreadCount = ref(0)
const totalCount = ref(0)
const query = ref({ pageNum: 1, pageSize: 20, msgType: null, isRead: null })

const detailVisible = ref(false)
const detail = ref({})

async function loadMessages() {
  loading.value = true
  try {
    const res = await getMessageList(query.value)
    messages.value = res.rows || []
    total.value = res.total || 0
    totalCount.value = res.total || 0
  } catch (e) {
    messages.value = []
  }
  loading.value = false
}

async function loadUnreadCount() {
  try {
    const res = await getUnreadCount()
    unreadCount.value = res.data || 0
  } catch (e) {
    unreadCount.value = 0
  }
}

async function markRead(row) {
  try {
    await markMessageRead(row.id)
    ElMessage.success('已标记已读')
    loadMessages()
    loadUnreadCount()
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

async function markAllRead() {
  try {
    await apiMarkAllRead()
    ElMessage.success('全部已读')
    loadMessages()
    loadUnreadCount()
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

function showDetail(row) {
  detail.value = row
  detailVisible.value = true
  if (row.isRead === 0) {
    markRead(row)
  }
}

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm('确认删除该消息？', '提示', { type: 'warning' })
    await deleteMessage(row.id)
    ElMessage.success('删除成功')
    loadMessages()
    loadUnreadCount()
  } catch (e) {
    if (e !== 'cancel') ElMessage.error('删除失败')
  }
}

onMounted(() => {
  loadMessages()
  loadUnreadCount()
})
</script>

<template>
  <div class="app-container">
    <el-card>
      <template #header>
        <div style="display:flex;justify-content:space-between;align-items:center">
          <span>📝 操作日志</span>
          <div>
            <el-button type="danger" text @click="clearLogs">清空日志</el-button>
            <el-button type="primary" text @click="exportLogs">导出</el-button>
          </div>
        </div>
      </template>

      <el-form :inline="true" style="margin-bottom:16px">
        <el-form-item label="操作类型">
          <el-select v-model="query.type" clearable placeholder="全部" style="width:120px">
            <el-option label="新增" value="INSERT" />
            <el-option label="修改" value="UPDATE" />
            <el-option label="删除" value="DELETE" />
            <el-option label="查询" value="SELECT" />
            <el-option label="导入" value="IMPORT" />
            <el-option label="导出" value="EXPORT" />
          </el-select>
        </el-form-item>
        <el-form-item label="操作模块">
          <el-select v-model="query.module" clearable placeholder="全部" style="width:140px">
            <el-option label="企业监控" value="company" />
            <el-option label="风险事件" value="event" />
            <el-option label="预警管理" value="alert" />
            <el-option label="排查任务" value="task" />
            <el-option label="预警规则" value="rule" />
            <el-option label="关联关系" value="relation" />
          </el-select>
        </el-form-item>
        <el-form-item label="操作时间">
          <el-date-picker v-model="query.dateRange" type="daterange" range-separator="至" start-placeholder="开始" end-placeholder="结束" value-format="YYYY-MM-DD" style="width:240px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadLogs">查询</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="logs" v-loading="loading" stripe>
        <el-table-column label="时间" prop="operateTime" width="170" />
        <el-table-column label="操作人" prop="operator" width="100" />
        <el-table-column label="类型" width="80">
          <template #default="{ row }">
            <el-tag :type="getTypeTag(row.operateType)" size="small">{{ row.operateType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="模块" prop="module" width="100" />
        <el-table-column label="操作内容" prop="content" show-overflow-tooltip />
        <el-table-column label="IP" prop="ip" width="120" />
        <el-table-column label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
              {{ row.status === 1 ? '成功' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination v-if="total > 0" style="margin-top:16px;justify-content:flex-end" v-model:current-page="query.pageNum" v-model:page-size="query.pageSize" :total="total" layout="total, prev, pager, next" @current-change="loadLogs" />

      <el-empty v-if="logs.length === 0 && !loading" description="暂无操作日志" />
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const query = ref({
  type: '', module: '', dateRange: [],
  pageNum: 1, pageSize: 20
})
const logs = ref([])
const total = ref(0)
const loading = ref(false)

function getTypeTag(type) {
  const map = { 'INSERT': 'success', 'UPDATE': 'warning', 'DELETE': 'danger', 'SELECT': 'info', 'IMPORT': '', 'EXPORT': '' }
  return map[type] || ''
}

async function loadLogs() {
  loading.value = true
  // Mock data - in production, this would call backend API
  const mockLogs = [
    { operateTime: '2026-05-30 19:00:00', operator: 'admin', operateType: '新增', module: '企业监控', content: '新增企业：华信地产集团有限公司', ip: '127.0.0.1', status: 1 },
    { operateTime: '2026-05-30 18:45:00', operator: 'admin', operateType: '新增', module: '风险事件', content: '新增事件：华信地产被列为被执行人', ip: '127.0.0.1', status: 1 },
    { operateTime: '2026-05-30 18:30:00', operator: 'admin', operateType: '修改', module: '预警管理', content: '处理预警：华信地产被执行预警', ip: '127.0.0.1', status: 1 },
    { operateTime: '2026-05-30 18:00:00', operator: 'admin', operateType: '导入', module: '企业监控', content: '批量导入企业数据 10 条', ip: '127.0.0.1', status: 1 },
    { operateTime: '2026-05-30 17:30:00', operator: 'admin', operateType: '新增', module: '排查任务', content: '新增任务：华信地产现场贷后检查', ip: '127.0.0.1', status: 1 },
    { operateTime: '2026-05-30 17:00:00', operator: 'admin', operateType: '查询', module: '风险报告', content: '生成企业风险报告：华信地产', ip: '127.0.0.1', status: 1 },
    { operateTime: '2026-05-30 16:30:00', operator: 'admin', operateType: '修改', module: '预警规则', content: '修改规则：被执行预警阈值', ip: '127.0.0.1', status: 1 },
    { operateTime: '2026-05-30 16:00:00', operator: 'admin', operateType: '删除', module: '关联关系', content: '删除关联：测试数据', ip: '127.0.0.1', status: 1 },
  ]

  let filtered = [...mockLogs]
  if (query.value.type) filtered = filtered.filter(l => l.operateType === query.value.type)
  if (query.value.module) filtered = filtered.filter(l => l.module === query.value.module)

  logs.value = filtered
  total.value = filtered.length
  loading.value = false
}

async function clearLogs() {
  await ElMessageBox.confirm('确定清空所有操作日志？', '提示', { type: 'warning' })
  logs.value = []
  total.value = 0
  ElMessage.success('日志已清空')
}

function exportLogs() {
  ElMessage.success('日志导出成功')
}

onMounted(loadLogs)
</script>

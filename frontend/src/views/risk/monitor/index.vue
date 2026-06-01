<template>
  <div class="app-container">
    <el-alert title="定时监测任务将按照配置的频率自动采集企业风险数据，采集来源包括天眼查、企查查、中国执行信息公开网等" type="info" show-icon :closable="false" style="margin-bottom:20px" />

    <el-row :gutter="10" style="margin-bottom:15px">
      <el-col :span="1.5">
        <el-button type="primary" plain @click="handleAdd"><el-icon><Plus /></el-icon>新建监测任务</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain @click="runAllTasks"><el-icon><VideoPlay /></el-icon>立即执行全部</el-button>
      </el-col>
    </el-row>

    <el-table :data="monitorTasks" border stripe>
      <el-table-column prop="taskName" label="任务名称" min-width="180" />
      <el-table-column prop="dataSource" label="数据来源" width="120">
        <template #default="{ row }">
          <el-tag size="small">{{ row.dataSource }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="frequency" label="执行频率" width="100" />
      <el-table-column prop="targetCount" label="监控企业数" width="100" align="center" />
      <el-table-column prop="lastRunTime" label="上次执行" width="160" />
      <el-table-column prop="lastRunResult" label="执行结果" width="100" align="center">
        <template #default="{ row }">
          <el-tag :type="row.lastRunResult === '成功' ? 'success' : row.lastRunResult === '失败' ? 'danger' : 'info'" size="small">
            {{ row.lastRunResult || '未执行' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="80" align="center">
        <template #default="{ row }">
          <el-switch v-model="row.status" :active-value="1" :inactive-value="0" />
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" size="small" @click="runTask(row)">执行</el-button>
          <el-button link type="primary" size="small">编辑</el-button>
          <el-button link type="danger" size="small">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 采集日志 -->
    <el-card style="margin-top:20px">
      <template #header><span>最近采集日志</span></template>
      <el-timeline>
        <el-timeline-item v-for="(log, idx) in collectLogs" :key="idx" :timestamp="log.time" :type="log.type" placement="top">
          <el-card shadow="never"><p>{{ log.content }}</p></el-card>
        </el-timeline-item>
      </el-timeline>
      <el-empty v-if="collectLogs.length === 0" description="暂无采集日志" />
    </el-card>

    <el-dialog title="新建监测任务" v-model="dialogVisible" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="任务名称"><el-input v-model="form.taskName" /></el-form-item>
        <el-form-item label="数据来源">
          <el-select v-model="form.dataSource" style="width:100%">
            <el-option label="天眼查API" value="天眼查" />
            <el-option label="企查查API" value="企查查" />
            <el-option label="中国执行信息公开网" value="执行信息网" />
            <el-option label="国家企业信用信息公示系统" value="信用公示" />
          </el-select>
        </el-form-item>
        <el-form-item label="执行频率">
          <el-select v-model="form.frequency" style="width:100%">
            <el-option label="每日" value="每日" />
            <el-option label="每周" value="每周" />
            <el-option label="每月" value="每月" />
          </el-select>
        </el-form-item>
        <el-form-item label="采集字段">
          <el-checkbox-group v-model="form.fields">
            <el-checkbox label="被执行信息" value="被执行信息" />
            <el-checkbox label="失信信息" value="失信信息" />
            <el-checkbox label="经营异常" value="经营异常" />
            <el-checkbox label="工商变更" value="工商变更" />
            <el-checkbox label="司法诉讼" value="司法诉讼" />
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogVisible = false; ElMessage.success('创建成功')">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus, VideoPlay } from '@element-plus/icons-vue'

const dialogVisible = ref(false)
const form = ref({ taskName: '', dataSource: '天眼查', frequency: '每日', fields: ['被执行信息', '失信信息'] })

const monitorTasks = ref([
  { id: 1, taskName: '全部企业-执行信息采集', dataSource: '执行信息网', frequency: '每日', targetCount: 6, lastRunTime: '2026-05-30 08:00:00', lastRunResult: '成功', status: 1 },
  { id: 2, taskName: '高风险企业-全量采集', dataSource: '天眼查', frequency: '每日', targetCount: 2, lastRunTime: '2026-05-30 09:00:00', lastRunResult: '成功', status: 1 },
  { id: 3, taskName: '全部企业-工商变更监测', dataSource: '企查查', frequency: '每周', targetCount: 6, lastRunTime: '2026-05-27 10:00:00', lastRunResult: '成功', status: 1 },
  { id: 4, taskName: '关联企业-舆情监测', dataSource: '舆情系统', frequency: '每日', targetCount: 4, lastRunTime: null, lastRunResult: null, status: 0 }
])

const collectLogs = ref([
  { time: '2026-05-30 09:00:00', content: '高风险企业全量采集完成：采集 2 家企业，新增 1 条被执行事件，1 条股权冻结事件', type: 'success' },
  { time: '2026-05-30 08:00:00', content: '全部企业执行信息采集完成：扫描 6 家企业，未发现新增执行信息', type: 'primary' },
  { time: '2026-05-27 10:00:00', content: '工商变更监测完成：发现 1 条法人变更（恒达贸易），已自动生成预警', type: 'warning' }
])

function handleAdd() { form.value = { taskName: '', dataSource: '天眼查', frequency: '每日', fields: ['被执行信息', '失信信息'] }; dialogVisible.value = true }
function runTask(row) { ElMessage.success(`任务"${row.taskName}"已触发执行`) }
function runAllTasks() { ElMessage.success('全部监测任务已触发执行') }
</script>

<template>
  <div class="app-container">
    <el-page-header @back="goBack" :content="company.companyName || '企业详情'" style="margin-bottom:16px" />

    <!-- 企业基本信息 -->
    <el-card style="margin-bottom:16px">
      <template #header><span>基本信息</span></template>
      <el-descriptions :column="3" border>
        <el-descriptions-item label="企业名称">{{ company.companyName }}</el-descriptions-item>
        <el-descriptions-item label="统一社会信用代码">{{ company.creditCode }}</el-descriptions-item>
        <el-descriptions-item label="法定代表人">{{ company.legalPerson }}</el-descriptions-item>
        <el-descriptions-item label="注册地址" :span="2">{{ company.address }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ company.phone }}</el-descriptions-item>
        <el-descriptions-item label="行业">{{ company.industry }}</el-descriptions-item>
        <el-descriptions-item label="注册资本(万)">{{ company.registeredCapital }}</el-descriptions-item>
        <el-descriptions-item label="贷款余额(万)">{{ company.loanBalance }}</el-descriptions-item>
        <el-descriptions-item label="风险等级">
          <el-tag :type="company.riskLevel===3?'danger':company.riskLevel===2?'warning':'success'">
            {{ company.riskLevel===3?'高风险':company.riskLevel===2?'中风险':'低风险' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="company.status===1?'success':company.status===2?'danger':'warning'">
            {{ company.status===1?'正常':company.status===2?'预警':'冻结' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="管户经理">{{ company.managerName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="入池日期">{{ company.createTime }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-row :gutter="16">
      <el-col :span="12">
        <!-- 风险事件 -->
        <el-card style="margin-bottom:16px">
          <template #header><span>风险事件 ({{ events.length }})</span></template>
          <el-timeline>
            <el-timeline-item v-for="ev in events" :key="ev.id" :timestamp="ev.eventDate" placement="top"
              :type="ev.severity===3?'danger':ev.severity===2?'warning':'primary'">
              <div><strong>{{ eventTypeName(ev.eventType) }}</strong></div>
              <div style="color:#606266;margin-top:4px">{{ ev.eventContent }}</div>
              <div style="color:#909399;font-size:12px;margin-top:4px">{{ ev.eventSource }}</div>
            </el-timeline-item>
          </el-timeline>
          <el-empty v-if="events.length===0" description="暂无风险事件" />
        </el-card>
      </el-col>
      <el-col :span="12">
        <!-- 预警记录 -->
        <el-card style="margin-bottom:16px">
          <template #header><span>预警记录 ({{ alerts.length }})</span></template>
          <el-table :data="alerts" size="small" stripe>
            <el-table-column label="预警标题" prop="alertTitle" show-overflow-tooltip />
            <el-table-column label="等级" width="70">
              <template #default="{row}">
                <el-tag :type="row.alertLevel===3?'danger':row.alertLevel===2?'warning':'info'" size="small">
                  {{ row.alertLevel===3?'严重':row.alertLevel===2?'重要':'一般' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="状态" width="70">
              <template #default="{row}">
                <el-tag :type="row.status===1?'success':'danger'" size="small">{{ row.status===1?'已处理':'待处理' }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="触发时间" width="160" prop="triggerTime" />
          </el-table>
          <el-empty v-if="alerts.length===0" description="暂无预警" />
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16">
      <el-col :span="12">
        <!-- 关联关系 -->
        <el-card style="margin-bottom:16px">
          <template #header><span>关联关系 ({{ relations.length }})</span></template>
          <el-table :data="relations" size="small" stripe>
            <el-table-column label="关联企业" prop="relatedCompanyName" show-overflow-tooltip />
            <el-table-column label="关联类型" width="100">
              <template #default="{row}">{{ relationTypeName(row.relationType) }}</template>
            </el-table-column>
            <el-table-column label="关联说明" prop="relationDesc" show-overflow-tooltip />
          </el-table>
          <el-empty v-if="relations.length===0" description="暂无关联" />
        </el-card>
      </el-col>
      <el-col :span="12">
        <!-- 排查任务 -->
        <el-card style="margin-bottom:16px">
          <template #header><span>排查任务 ({{ tasks.length }})</span></template>
          <el-table :data="tasks" size="small" stripe>
            <el-table-column label="任务标题" prop="taskTitle" show-overflow-tooltip />
            <el-table-column label="优先级" width="70">
              <template #default="{row}">
                <el-tag :type="row.priority===3?'danger':row.priority===2?'warning':'info'" size="small">
                  {{ row.priority===3?'紧急':row.priority===2?'重要':'一般' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="状态" width="70">
              <template #default="{row}">
                <el-tag :type="row.status===2?'success':row.status===1?'warning':'info'" size="small">
                  {{ row.status===0?'待处理':row.status===1?'处理中':'已完成' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
          <el-empty v-if="tasks.length===0" description="暂无任务" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getCompanyDetail, getEventList, getAlertList } from '@/api/risk'
import request from '@/utils/request'

const route = useRoute()
const router = useRouter()
const companyId = route.params.id || route.query.id

const company = ref({})
const events = ref([])
const alerts = ref([])
const relations = ref([])
const tasks = ref([])

const eventTypeName = (t) => ({
  EXECUTION: '被执行', DISHONEST: '失信被执行', ABNORMAL: '经营异常',
  LAWSUIT: '涉诉', CHANGE_LEGAL: '法人变更', FREEZE: '股权冻结', OTHER: '其他'
}[t] || t)

const relationTypeName = (t) => ({
  GUARANTEE: '担保', INVESTMENT: '投资', AFFILIATE: '关联', CREDIT: '授信'
}[t] || t)

async function loadData() {
  try {
    const res = await getCompanyDetail(companyId)
    company.value = res.data || res || {}
  } catch(e) { console.error(e) }

  try {
    const res = await getEventList({ pageSize: 50 })
    events.value = (res.rows || []).filter(e => e.companyId == companyId)
  } catch(e) { console.error(e) }

  try {
    const res = await getAlertList({ pageSize: 50 })
    alerts.value = (res.rows || []).filter(a => a.companyId == companyId)
  } catch(e) { console.error(e) }

  try {
    const res = await request({ url: `/risk/relation/company/${companyId}` })
    relations.value = res.data || res || []
  } catch(e) { console.error(e) }

  try {
    const res = await request({ url: `/risk/task/list?companyId=${companyId}` })
    tasks.value = res.rows || []
  } catch(e) { console.error(e) }
}

function goBack() {
  router.push('/risk/company')
}

onMounted(loadData)
</script>

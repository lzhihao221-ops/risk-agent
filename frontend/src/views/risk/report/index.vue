<template>
  <div class="app-container">
    <el-card>
      <template #header>
        <div style="display:flex;justify-content:space-between;align-items:center">
          <span>📑 风险报告生成</span>
        </div>
      </template>

      <el-form :model="form" label-width="120px">
        <el-form-item label="报告类型">
          <el-radio-group v-model="form.reportType">
            <el-radio label="company">企业风险报告</el-radio>
            <el-radio label="monthly">月度汇总报告</el-radio>
            <el-radio label="special">专项排查报告</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="选择企业" v-if="form.reportType === 'company'">
          <el-select v-model="form.companyId" filterable placeholder="选择企业" style="width:400px">
            <el-option v-for="c in companies" :key="c.id" :label="c.companyName" :value="c.id" />
          </el-select>
        </el-form-item>

        <el-form-item label="报告周期" v-if="form.reportType !== 'company'">
          <el-date-picker v-model="form.dateRange" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" value-format="YYYY-MM-DD" />
        </el-form-item>

        <el-form-item label="报告格式">
          <el-checkbox-group v-model="form.formats">
            <el-checkbox label="html">在线预览</el-checkbox>
            <el-checkbox label="word">Word 文档</el-checkbox>
            <el-checkbox label="excel">Excel 表格</el-checkbox>
          </el-checkbox-group>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="generateReport" :loading="generating">
            <el-icon><Document /></el-icon> 生成报告
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 报告预览 -->
    <el-card v-if="reportHtml" style="margin-top:16px">
      <template #header><span>报告预览</span></template>
      <div v-html="reportHtml" class="report-preview"></div>
    </el-card>

    <!-- 历史报告 -->
    <el-card style="margin-top:16px">
      <template #header><span>历史报告</span></template>
      <el-table :data="historyReports" v-loading="loading">
        <el-table-column label="报告名称" prop="name" />
        <el-table-column label="类型" prop="type" width="100" />
        <el-table-column label="生成时间" prop="time" width="160" />
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button text type="primary" size="small">下载</el-button>
            <el-button text type="danger" size="small">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'
import { Document } from '@element-plus/icons-vue'

const form = ref({
  reportType: 'company',
  companyId: null,
  dateRange: [],
  formats: ['html']
})

const companies = ref([])
const generating = ref(false)
const reportHtml = ref('')
const historyReports = ref([])
const loading = ref(false)

async function loadCompanies() {
  try {
    const res = await request({ url: '/risk/company/list', method: 'get', params: { pageSize: 100 } })
    companies.value = res.rows || res.data?.rows || []
  } catch (e) { console.error(e) }
}

async function generateReport() {
  if (form.value.reportType === 'company' && !form.value.companyId) {
    ElMessage.warning('请选择企业')
    return
  }
  generating.value = true
  try {
    const company = companies.value.find(c => c.id === form.value.companyId)
    const res = await request({ url: '/risk/dashboard/summary', method: 'get' })
    const data = res.data || res || {}

    // Generate HTML report
    const now = new Date().toLocaleString('zh-CN')
    reportHtml.value = `
      <div style="font-family: 'Microsoft YaHei', sans-serif; max-width: 800px; margin: 0 auto;">
        <h1 style="text-align:center; color:#303133;">${form.value.reportType === 'company' ? (company?.companyName || '') + ' 风险评估报告' : '月度风险汇总报告'}</h1>
        <p style="text-align:center; color:#909399;">报告生成时间：${now}</p>
        <hr style="border:1px solid #EBEEF5;" />

        <h2 style="color:#409EFF;">一、基本信息</h2>
        ${form.value.reportType === 'company' ? `
        <table style="width:100%; border-collapse:collapse; margin:10px 0;">
          <tr><td style="padding:8px; border:1px solid #EBEEF5; background:#F5F7FA; width:120px;">企业名称</td><td style="padding:8px; border:1px solid #EBEEF5;">${company?.companyName || '-'}</td></tr>
          <tr><td style="padding:8px; border:1px solid #EBEEF5; background:#F5F7FA;">统一信用代码</td><td style="padding:8px; border:1px solid #EBEEF5;">${company?.creditCode || '-'}</td></tr>
          <tr><td style="padding:8px; border:1px solid #EBEEF5; background:#F5F7FA;">法定代表人</td><td style="padding:8px; border:1px solid #EBEEF5;">${company?.legalPerson || '-'}</td></tr>
          <tr><td style="padding:8px; border:1px solid #EBEEF5; background:#F5F7FA;">注册资本</td><td style="padding:8px; border:1px solid #EBEEF5;">${company?.regCapital || '-'} 万元</td></tr>
          <tr><td style="padding:8px; border:1px solid #EBEEF5; background:#F5F7FA;">风险评分</td><td style="padding:8px; border:1px solid #EBEEF5;"><span style="color:#F56C6C; font-weight:bold; font-size:18px;">${company?.riskScore || '-'}</span></td></tr>
          <tr><td style="padding:8px; border:1px solid #EBEEF5; background:#F5F7FA;">风险等级</td><td style="padding:8px; border:1px solid #EBEEF5;"><span style="color:${company?.riskLevel === 3 ? '#F56C6C' : company?.riskLevel === 2 ? '#E6A23C' : '#67C23A'};">${company?.riskLevel === 3 ? '高风险' : company?.riskLevel === 2 ? '中风险' : '低风险'}</span></td></tr>
        </table>` : `
        <p>监控企业总数：<strong>${data.companyCount || 0}</strong> 家</p>
        <p>风险事件总数：<strong>${data.eventCount || 0}</strong> 条</p>
        <p>未处理预警：<strong style="color:#F56C6C;">${data.unhandledAlertCount || 0}</strong> 条</p>
        `}

        <h2 style="color:#409EFF;">二、风险概览</h2>
        <p>当前系统监控企业 <strong>${data.companyCount || 0}</strong> 家，其中高风险企业 <strong style="color:#F56C6C;">${(data.highRiskCompanies || []).length}</strong> 家。</p>
        <p>近30天新增风险事件 <strong>${data.eventCount || 0}</strong> 条，涉及被执行、失信被执行、经营异常、涉诉等多种类型。</p>

        <h2 style="color:#409EFF;">三、预警分析</h2>
        <p>系统共触发预警 <strong>${(data.alertLevelStats || []).reduce((s, d) => s + d.count, 0)}</strong> 条，已处理 <strong>${(data.alertLevelStats || []).reduce((s, d) => s + d.count, 0) - (data.unhandledAlertCount || 0)}</strong> 条，处理率 <strong>${Math.round(((data.alertLevelStats || []).reduce((s, d) => s + d.count, 0) - (data.unhandledAlertCount || 0)) / Math.max(1, (data.alertLevelStats || []).reduce((s, d) => s + d.count, 0)) * 100)}%</strong>。</p>

        <h2 style="color:#409EFF;">四、建议措施</h2>
        <ol>
          <li>对高风险企业加强贷后检查频率，建议每月至少一次现场检查</li>
          <li>对存在被执行记录的企业，及时评估抵押物价值变化</li>
          <li>关注担保链风险传导，建立关联企业风险预警机制</li>
          <li>对经营异常企业进行实地走访，了解真实经营状况</li>
          <li>建立风险事件应急处置流程，确保预警信息及时响应</li>
        </ol>

        <hr style="border:1px solid #EBEEF5; margin-top:30px;" />
        <p style="text-align:center; color:#909399; font-size:12px;">银行风险管理系统 v1.0.0 | 报告仅供内部参考</p>
      </div>
    `
    ElMessage.success('报告生成成功')

    // Add to history
    historyReports.value.unshift({
      name: form.value.reportType === 'company' ? `${company?.companyName} 风险报告` : '月度汇总报告',
      type: form.value.reportType === 'company' ? '企业报告' : '汇总报告',
      time: now
    })
  } catch (e) {
    ElMessage.error('报告生成失败')
  }
  generating.value = false
}

onMounted(loadCompanies)
</script>

<style scoped>
.report-preview { padding: 20px; background: #fff; border: 1px solid #EBEEF5; border-radius: 4px; }
.report-preview h1 { font-size: 24px; }
.report-preview h2 { font-size: 18px; margin-top: 20px; }
.report-preview table { margin: 10px 0; }
</style>

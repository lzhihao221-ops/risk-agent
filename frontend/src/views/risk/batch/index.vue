<template>
  <div class="app-container">
    <el-alert title="批量导入企业信息，支持 Excel 和 CSV 格式。下载模板后按格式填写数据再上传。" type="info" show-icon :closable="false" style="margin-bottom:20px" />

    <el-row :gutter="20">
      <el-col :span="12">
        <el-card>
          <template #header><span>批量导入</span></template>
          <el-upload
            ref="uploadRef"
            drag
            action="http://localhost:8080/risk/company/import"
            :headers="uploadHeaders"
            :on-success="handleSuccess"
            :on-error="handleError"
            :before-upload="beforeUpload"
            accept=".xlsx,.xls,.csv"
          >
            <el-icon style="font-size:40px;color:#C0C4CC"><UploadFilled /></el-icon>
            <div style="margin-top:8px">拖拽文件到此处，或<em>点击上传</em></div>
            <template #tip>
              <div style="color:#909399;font-size:12px;margin-top:8px">支持 .xlsx / .xls / .csv 格式，单次最多 500 条</div>
            </template>
          </el-upload>
          <el-button style="margin-top:16px" @click="downloadTemplate"><el-icon><Download /></el-icon>下载导入模板</el-button>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card>
          <template #header><span>快速录入</span></template>
          <el-form :model="form" label-width="100px">
            <el-form-item label="企业名单">
              <el-input v-model="form.companies" type="textarea" :rows="8" placeholder="每行一个企业名称，格式：&#10;企业名称,信用代码,法人&#10;恒达贸易,91310000XXX,张三&#10;鑫隆建材,91310000YYY,王五" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="batchAdd" :loading="batching">批量添加</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>

    <!-- 导入记录 -->
    <el-card style="margin-top:20px">
      <template #header><span>导入记录</span></template>
      <el-table :data="importHistory" border stripe>
        <el-table-column prop="fileName" label="文件名" min-width="200" />
        <el-table-column prop="totalCount" label="总条数" width="80" align="center" />
        <el-table-column prop="successCount" label="成功" width="80" align="center">
          <template #default="{ row }"><span style="color:#67C23A">{{ row.successCount }}</span></template>
        </el-table-column>
        <el-table-column prop="failCount" label="失败" width="80" align="center">
          <template #default="{ row }"><span :style="{ color: row.failCount > 0 ? '#F56C6C' : '#909399' }">{{ row.failCount }}</span></template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === '完成' ? 'success' : 'warning'" size="small">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createBy" label="操作人" width="80" />
        <el-table-column prop="createTime" label="导入时间" width="160" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { UploadFilled, Download } from '@element-plus/icons-vue'

const uploadRef = ref(null)
const batching = ref(false)
const form = ref({ companies: '' })
const token = localStorage.getItem('Admin-Token') || ''

const uploadHeaders = computed(() => ({ Authorization: 'Bearer ' + token }))

const importHistory = ref([
  { fileName: '初始企业清单.xlsx', totalCount: 6, successCount: 6, failCount: 0, status: '完成', createBy: 'admin', createTime: '2026-05-30 15:00:00' }
])

function beforeUpload(file) {
  const ext = file.name.split('.').pop().toLowerCase()
  if (!['xlsx', 'xls', 'csv'].includes(ext)) { ElMessage.error('请上传 Excel 或 CSV 文件'); return false }
  if (file.size > 10 * 1024 * 1024) { ElMessage.error('文件大小不能超过 10MB'); return false }
  return true
}
function handleSuccess(response) {
  if (response.code === 200) { ElMessage.success('导入成功') }
  else { ElMessage.error(response.msg || '导入失败') }
}
function handleError() { ElMessage.error('上传失败，请重试') }
function downloadTemplate() {
  const header = '企业名称,统一社会信用代码,法定代表人,注册资本(万元),行业,省份,城市\n'
  const example = '示例企业,91310000MA1XXX,张三,5000,贸易,上海市,上海市\n'
  const blob = new Blob(['\uFEFF' + header + example], { type: 'text/csv;charset=utf-8;' })
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a'); a.href = url; a.download = '企业导入模板.csv'; a.click()
  URL.revokeObjectURL(url)
}
function batchAdd() {
  if (!form.value.companies.trim()) { ElMessage.warning('请输入企业名单'); return }
  const lines = form.value.companies.trim().split('\n').filter(l => l.trim())
  ElMessage.success(`已提交 ${lines.length} 家企业，正在处理...`)
  batching.value = false
  form.value.companies = ''
}
</script>

<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!-- 左侧：上传和识别 -->
      <el-col :span="14">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>🔍 OCR智能识别</span>
              <el-tag v-if="lastFileName" type="info" size="small">{{ lastFileName }}</el-tag>
            </div>
          </template>

          <input type="file" ref="fileInput" style="display: none" @change="handleFileChange" accept="image/*,.pdf" />

          <!-- 上传区域 -->
          <div class="upload-area" :class="{ 'upload-active': isDragging }" 
               @click="triggerUpload" @drop.prevent="handleDrop" @dragover.prevent="isDragging = true" 
               @dragleave="isDragging = false">
            <el-icon class="upload-icon"><Upload /></el-icon>
            <div class="upload-text">点击或拖拽文件到此处上传</div>
            <div class="upload-hint">支持 JPG、PNG、PDF 格式，最大 10MB</div>
          </div>

          <!-- 文件预览 -->
          <div v-if="previewUrl" class="file-preview">
            <el-image :src="previewUrl" fit="contain" style="max-height: 200px; border-radius: 8px;" />
            <el-button type="danger" link @click="clearPreview" style="margin-top: 8px;">
              <el-icon><Delete /></el-icon> 清除
            </el-button>
          </div>

          <!-- 识别模式选择 -->
          <el-radio-group v-model="ocrMode" style="margin-top: 15px;">
            <el-radio-button label="general">通用识别</el-radio-button>
            <el-radio-button label="idcard">身份证</el-radio-button>
            <el-radio-button label="bankcard">银行卡</el-radio-button>
            <el-radio-button label="invoice">发票</el-radio-button>
            <el-radio-button label="report">财务报表</el-radio-button>
          </el-radio-group>

          <!-- 识别按钮 -->
          <el-button type="primary" @click="handleRecognize" :loading="loading" :disabled="!selectedFile" 
                     style="width: 100%; margin-top: 15px;" size="large">
            <el-icon><Aim /></el-icon>
            {{ loading ? '正在识别...' : '开始识别' }}
          </el-button>

          <!-- 加载进度 -->
          <div v-if="loading" class="loading-progress">
            <el-progress :percentage="progress" :status="progressStatus" :stroke-width="8" />
            <div class="progress-text">{{ progressText }}</div>
          </div>
        </el-card>
      </el-col>

      <!-- 右侧：识别结果 -->
      <el-col :span="10">
        <!-- 通用识别结果 -->
        <el-card v-if="ocrResult" shadow="hover" class="result-card">
          <template #header>
            <div class="card-header">
              <span>📝 识别结果</span>
              <div>
                <el-button type="primary" link @click="copyResult">
                  <el-icon><DocumentCopy /></el-icon> 复制
                </el-button>
                <el-button type="success" link @click="exportResult">
                  <el-icon><Download /></el-icon> 导出
                </el-button>
              </div>
            </div>
          </template>
          <div class="ocr-result">{{ ocrResult }}</div>
          <div class="result-stats">
            <el-tag size="small">字数: {{ ocrResult.length }}</el-tag>
            <el-tag size="small" type="info">耗时: {{ recognizeTime }}ms</el-tag>
          </div>
        </el-card>

        <!-- 识别历史 -->
        <el-card shadow="hover" style="margin-top: 15px;">
          <template #header>
            <span>📋 识别历史</span>
          </template>
          <el-timeline v-if="history.length > 0">
            <el-timeline-item v-for="(item, index) in history" :key="index" 
                              :timestamp="item.time" placement="top" :type="item.type">
              <el-card shadow="never" class="history-item">
                <div class="history-file">{{ item.fileName }}</div>
                <div class="history-preview">{{ item.preview }}</div>
              </el-card>
            </el-timeline-item>
          </el-timeline>
          <el-empty v-else description="暂无识别记录" :image-size="60" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Upload, DocumentCopy, Delete, Aim, Download } from '@element-plus/icons-vue'
import request from '@/utils/request'

const fileInput = ref(null)
const ocrResult = ref('')
const loading = ref(false)
const isDragging = ref(false)
const selectedFile = ref(null)
const previewUrl = ref('')
const lastFileName = ref('')
const ocrMode = ref('general')
const progress = ref(0)
const progressStatus = ref('')
const progressText = ref('')
const recognizeTime = ref(0)
const history = ref([])

// 模拟进度
let progressTimer = null

function startProgress() {
  progress.value = 0
  progressStatus = ref('')
  progressText.value = '正在上传文件...'
  progressTimer = setInterval(() => {
    if (progress.value < 90) {
      progress.value += Math.random() * 15
      if (progress.value > 30) progressText.value = '正在分析图像...'
      if (progress.value > 60) progressText.value = '正在识别文字...'
    }
  }, 300)
}

function stopProgress(success = true) {
  if (progressTimer) {
    clearInterval(progressTimer)
    progressTimer = null
  }
  progress.value = 100
  progressStatus.value = success ? 'success' : 'exception'
  progressText.value = success ? '识别完成！' : '识别失败'
}

function triggerUpload() {
  fileInput.value.click()
}

function handleFileChange(event) {
  const file = event.target.files[0]
  if (file) {
    prepareFile(file)
  }
}

function handleDrop(event) {
  isDragging.value = false
  const file = event.dataTransfer.files[0]
  if (file) {
    prepareFile(file)
  }
}

function prepareFile(file) {
  // 验证文件大小
  if (file.size > 10 * 1024 * 1024) {
    ElMessage.error('文件大小不能超过 10MB')
    return
  }
  
  selectedFile.value = file
  lastFileName.value = file.name
  ocrResult.value = ''
  
  // 生成预览
  if (file.type.startsWith('image/')) {
    previewUrl.value = URL.createObjectURL(file)
  } else {
    previewUrl.value = ''
  }
}

function clearPreview() {
  selectedFile.value = null
  previewUrl.value = ''
  lastFileName.value = ''
  ocrResult.value = ''
}

async function handleRecognize() {
  if (!selectedFile.value) {
    ElMessage.warning('请先上传文件')
    return
  }
  
  loading.value = true
  ocrResult.value = ''
  startProgress()
  const startTime = Date.now()
  
  const formData = new FormData()
  formData.append('file', selectedFile.value)
  
  try {
    // 根据模式选择API
    const modeMap = {
      general: '/ocr/image',
      idcard: '/ocr/image',
      bankcard: '/ocr/image',
      invoice: '/ocr/image',
      report: '/ocr/report'
    }
    const url = modeMap[ocrMode.value] || '/ocr/image'
    
    const res = await request({
      url: url,
      method: 'post',
      data: formData,
      headers: { 'Content-Type': 'multipart/form-data' }
    })
    
    ocrResult.value = res.data || res.msg || '识别完成，但未返回文本内容'
    recognizeTime.value = Date.now() - startTime
    stopProgress(true)
    
    // 添加到历史
    history.value.unshift({
      fileName: lastFileName.value,
      preview: ocrResult.value.substring(0, 80) + '...',
      time: new Date().toLocaleString('zh-CN'),
      type: 'success'
    })
    if (history.value.length > 10) history.value.pop()
    
    ElMessage.success('识别完成！')
  } catch (error) {
    stopProgress(false)
    ElMessage.error('识别失败: ' + (error.message || '未知错误'))
  } finally {
    loading.value = false
  }
}

function copyResult() {
  navigator.clipboard.writeText(ocrResult.value)
  ElMessage.success('已复制到剪贴板')
}

function exportResult() {
  const blob = new Blob([ocrResult.value], { type: 'text/plain;charset=utf-8' })
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = `ocr_result_${new Date().getTime()}.txt`
  a.click()
  URL.revokeObjectURL(url)
  ElMessage.success('导出成功！')
}
</script>

<style scoped>
.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.upload-area {
  border: 2px dashed #dcdfe6;
  border-radius: 8px;
  padding: 40px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
}

.upload-area:hover, .upload-area.upload-active {
  border-color: #409eff;
  background: #ecf5ff;
}

.upload-icon {
  font-size: 48px;
  color: #c0c4cc;
  margin-bottom: 10px;
}

.upload-text {
  font-size: 16px;
  color: #606266;
  margin-bottom: 10px;
}

.upload-hint {
  font-size: 12px;
  color: #909399;
}

.file-preview {
  margin-top: 15px;
  text-align: center;
  padding: 10px;
  background: #f5f7fa;
  border-radius: 8px;
}

.loading-progress {
  margin-top: 15px;
}

.progress-text {
  text-align: center;
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
}

.result-card {
  border-top: 3px solid #67c23a;
}

.ocr-result {
  white-space: pre-wrap;
  font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif;
  line-height: 1.8;
  padding: 15px;
  background: #f5f7fa;
  border-radius: 4px;
  max-height: 400px;
  overflow-y: auto;
}

.result-stats {
  margin-top: 10px;
  display: flex;
  gap: 10px;
}

.history-item {
  margin: 0;
  padding: 0;
}

.history-file {
  font-weight: 600;
  font-size: 13px;
  color: #303133;
}

.history-preview {
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>

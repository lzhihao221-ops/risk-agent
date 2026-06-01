<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!-- 左侧：上传区域 -->
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>押品识别</span>
              <el-select v-model="collateralType" size="small" style="width: 120px">
                <el-option label="自动识别" value="auto" />
                <el-option label="房产证" value="house" />
                <el-option label="土地证" value="land" />
                <el-option label="车辆登记证" value="vehicle" />
                <el-option label="营业执照" value="business" />
              </el-select>
            </div>
          </template>

          <input type="file" ref="fileInput" style="display: none" @change="handleFileChange" accept="image/*" />
          
          <div class="upload-area" @click="triggerUpload" @drop.prevent="handleDrop" @dragover.prevent>
            <el-icon class="upload-icon"><Camera /></el-icon>
            <div class="upload-text">点击或拖拽押品证件到此处</div>
            <div class="upload-hint">支持 JPG、PNG 格式</div>
          </div>

          <div class="quick-buttons">
            <el-button @click="quickUpload('house')" :type="collateralType === 'house' ? 'primary' : ''">
              <el-icon><House /></el-icon>
              房产证
            </el-button>
            <el-button @click="quickUpload('land')" :type="collateralType === 'land' ? 'primary' : ''">
              <el-icon><Location /></el-icon>
              土地证
            </el-button>
            <el-button @click="quickUpload('vehicle')" :type="collateralType === 'vehicle' ? 'primary' : ''">
              <el-icon><Van /></el-icon>
              车辆登记证
            </el-button>
            <el-button @click="quickUpload('business')" :type="collateralType === 'business' ? 'primary' : ''">
              <el-icon><OfficeBuilding /></el-icon>
              营业执照
            </el-button>
          </div>
        </el-card>
      </el-col>

      <!-- 右侧：识别结果 -->
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>识别结果</span>
              <el-button v-if="result" type="primary" link @click="copyResult">
                <el-icon><DocumentCopy /></el-icon>
                复制
              </el-button>
            </div>
          </template>

          <div v-if="loading" class="loading-state">
            <el-icon class="loading-icon"><Loading /></el-icon>
            <div>正在识别中...</div>
          </div>

          <div v-else-if="result" class="result-content">
            <div class="result-header">
              <el-tag :type="getTagType(result.type)">{{ result.type }}</el-tag>
            </div>
            
            <el-descriptions :column="1" border style="margin-top: 15px;">
              <el-descriptions-item v-for="(value, key) in displayFields" :key="key" :label="getFieldLabel(key)">
                {{ value }}
              </el-descriptions-item>
            </el-descriptions>

            <el-collapse style="margin-top: 15px;">
              <el-collapse-item title="原始识别文本">
                <div class="raw-text">{{ result.rawText }}</div>
              </el-collapse-item>
            </el-collapse>
          </div>

          <div v-else class="empty-state">
            <el-icon class="empty-icon"><Document /></el-icon>
            <div>请上传押品证件图片</div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { Camera, House, Location, Van, OfficeBuilding, DocumentCopy, Loading, Document } from '@element-plus/icons-vue'
import request from '@/utils/request'

const fileInput = ref(null)
const collateralType = ref('auto')
const loading = ref(false)
const result = ref(null)

const displayFields = computed(() => {
  if (!result.value) return {}
  const { type, rawText, ...fields } = result.value
  return fields
})

function triggerUpload() {
  fileInput.value.click()
}

function quickUpload(type) {
  collateralType.value = type
  triggerUpload()
}

function handleFileChange(event) {
  const file = event.target.files[0]
  if (file) {
    uploadFile(file)
  }
}

function handleDrop(event) {
  const file = event.dataTransfer.files[0]
  if (file) {
    uploadFile(file)
  }
}

async function uploadFile(file) {
  loading.value = true
  result.value = null
  
  const formData = new FormData()
  formData.append('file', file)
  formData.append('type', collateralType.value)
  
  try {
    const res = await request({
      url: '/ocr/collateral/general',
      method: 'post',
      data: formData,
      headers: { 'Content-Type': 'multipart/form-data' }
    })
    
    result.value = res.data || res.msg
    ElMessage.success('识别完成！')
  } catch (error) {
    ElMessage.error('识别失败: ' + (error.message || '未知错误'))
  } finally {
    loading.value = false
  }
}

function getTagType(type) {
  const types = {
    '房产证': 'danger',
    '土地证': 'warning',
    '车辆登记证': 'success',
    '营业执照': 'primary'
  }
  return types[type] || 'info'
}

function getFieldLabel(key) {
  const labels = {
    ownerName: '所有权人',
    propertyNo: '房产证号',
    location: '坐落位置',
    area: '面积',
    usage: '用途',
    rightType: '权利类型',
    landNo: '土地证号',
    plateNo: '车牌号',
    vin: '车辆识别代号',
    engineNo: '发动机号',
    brand: '品牌',
    model: '型号',
    companyName: '公司名称',
    creditCode: '统一社会信用代码',
    legalPerson: '法定代表人',
    registeredCapital: '注册资本',
    establishDate: '成立日期',
    businessScope: '经营范围'
  }
  return labels[key] || key
}

function copyResult() {
  if (result.value) {
    const text = Object.entries(displayFields.value).map(([k, v]) => `${getFieldLabel(k)}: ${v}`).join('\n')
    navigator.clipboard.writeText(text)
    ElMessage.success('已复制到剪贴板')
  }
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

.upload-area:hover {
  border-color: #409eff;
  background: #f5f7fa;
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

.quick-buttons {
  margin-top: 20px;
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.loading-state, .empty-state {
  text-align: center;
  padding: 40px;
  color: #909399;
}

.loading-icon {
  font-size: 32px;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 10px;
}

.result-header {
  margin-bottom: 10px;
}

.raw-text {
  white-space: pre-wrap;
  font-family: monospace;
  font-size: 12px;
  line-height: 1.6;
  background: #f5f7fa;
  padding: 10px;
  border-radius: 4px;
}
</style>

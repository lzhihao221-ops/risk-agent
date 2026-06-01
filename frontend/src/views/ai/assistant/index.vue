<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!-- 左侧：功能选择 -->
      <el-col :span="6">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <el-icon><Cpu /></el-icon>
              <span>AI 功能</span>
            </div>
          </template>
          <div class="function-list">
            <div 
              v-for="func in functions" 
              :key="func.id"
              :class="['function-item', { active: activeFunction === func.id }]"
              @click="activeFunction = func.id"
            >
              <el-icon><component :is="func.icon" /></el-icon>
              <span>{{ func.name }}</span>
            </div>
          </div>
          
          <el-divider />
          
          <div class="model-info">
            <div class="info-title">当前模型</div>
            <div class="info-value">{{ currentModel }}</div>
            <div class="info-status">
              <el-tag :type="modelStatus === 'online' ? 'success' : 'danger'" size="small">
                {{ modelStatus === 'online' ? '在线' : '离线' }}
              </el-tag>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 右侧：对话区域 -->
      <el-col :span="18">
        <el-card shadow="hover" class="chat-card">
          <template #header>
            <div class="card-header">
              <span>{{ currentFunction.name }}</span>
              <el-button type="primary" link @click="clearChat">
                <el-icon><Delete /></el-icon>
                清空对话
              </el-button>
            </div>
          </template>

          <!-- 对话消息 -->
          <div class="chat-messages" ref="messagesRef">
            <div 
              v-for="(msg, index) in messages" 
              :key="index"
              :class="['message', msg.role === 'user' ? 'user-message' : 'ai-message']"
            >
              <div class="message-avatar">
                <el-icon v-if="msg.role === 'user'"><User /></el-icon>
                <el-icon v-else><Cpu /></el-icon>
              </div>
              <div class="message-content">
                <div class="message-text" v-html="formatMessage(msg.content)"></div>
                <div class="message-time">{{ msg.time }}</div>
              </div>
            </div>
            
            <!-- 加载中 -->
            <div v-if="loading" class="message ai-message">
              <div class="message-avatar">
                <el-icon><Cpu /></el-icon>
              </div>
              <div class="message-content">
                <div class="message-text typing">
                  <span class="dot"></span>
                  <span class="dot"></span>
                  <span class="dot"></span>
                </div>
              </div>
            </div>
          </div>

          <!-- 输入区域 -->
          <div class="chat-input">
            <!-- 快捷输入（风险分析等） -->
            <div v-if="activeFunction !== 'chat'" class="quick-inputs">
              <el-form :inline="true" :model="quickForm" size="small">
                <template v-if="activeFunction === 'risk'">
                  <el-form-item label="企业">
                    <el-input v-model="quickForm.companyName" placeholder="企业名称" style="width:120px" />
                  </el-form-item>
                  <el-form-item label="金额">
                    <el-input-number v-model="quickForm.amount" :min="0" :step="100" style="width:100px" />
                  </el-form-item>
                  <el-form-item label="逾期">
                    <el-input-number v-model="quickForm.overdueDays" :min="0" style="width:80px" />
                  </el-form-item>
                </template>
                <template v-if="activeFunction === 'report'">
                  <el-form-item label="报告类型">
                    <el-select v-model="quickForm.reportType" style="width:120px">
                      <el-option label="月度报告" value="月度风险报告" />
                      <el-option label="季度报告" value="季度风险报告" />
                      <el-option label="年度报告" value="年度风险报告" />
                    </el-select>
                  </el-form-item>
                  <el-form-item label="不良率">
                    <el-input-number v-model="quickForm.nplRatio" :min="0" :max="100" :step="0.1" style="width:100px" />
                  </el-form-item>
                </template>
                <template v-if="activeFunction === 'collection'">
                  <el-form-item label="企业">
                    <el-input v-model="quickForm.companyName" placeholder="企业名称" style="width:120px" />
                  </el-form-item>
                  <el-form-item label="逾期天数">
                    <el-input-number v-model="quickForm.overdueDays" :min="0" style="width:80px" />
                  </el-form-item>
                  <el-form-item label="金额">
                    <el-input-number v-model="quickForm.overdueAmount" :min="0" :step="10" style="width:100px" />
                  </el-form-item>
                </template>
              </el-form>
            </div>
            
            <!-- 输入框 -->
            <div class="input-row">
              <el-input
                v-model="inputMessage"
                :placeholder="inputPlaceholder"
                type="textarea"
                :rows="2"
                @keydown.enter.ctrl="sendMessage"
              />
              <el-button 
                type="primary" 
                :loading="loading" 
                @click="sendMessage"
              >
                发送
              </el-button>
            </div>
            <div class="input-tip">按 Ctrl + Enter 发送</div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { 
  Cpu, User, Delete, ChatDotRound, 
  Warning, Document, Phone, Search 
} from '@element-plus/icons-vue'
import request from '@/utils/request'

// 功能列表
const functions = [
  { id: 'chat', name: '智能问答', icon: 'ChatDotRound' },
  { id: 'risk', name: '风险分析', icon: 'Warning' },
  { id: 'report', name: '报告生成', icon: 'Document' },
  { id: 'collection', name: '催收建议', icon: 'Phone' },
  { id: 'anomaly', name: '异常检测', icon: 'Search' }
]

// 状态
const activeFunction = ref('chat')
const messages = ref([])
const inputMessage = ref('')
const loading = ref(false)
const messagesRef = ref(null)
const currentModel = ref('qwen3.6:27b')
const modelStatus = ref('online')
const sessionId = ref('session_' + Date.now())

// 快捷表单
const quickForm = ref({
  companyName: '',
  amount: 1000,
  overdueDays: 0,
  reportType: '月度风险报告',
  nplRatio: 1.5,
  overdueAmount: 500
})

// 计算属性
const currentFunction = computed(() => {
  return functions.find(f => f.id === activeFunction.value) || functions[0]
})

const inputPlaceholder = computed(() => {
  const placeholders = {
    chat: '输入您的风控问题...',
    risk: '点击上方填写信息后发送，或直接描述风险分析需求...',
    report: '描述报告需求或使用快捷输入...',
    collection: '描述催收场景或使用快捷输入...',
    anomaly: '描述需要检测的数据或交易情况...'
  }
  return placeholders[activeFunction.value] || '输入消息...'
})

// 格式化消息（简单Markdown支持）
function formatMessage(text) {
  if (!text) return ''
  return text
    .replace(/\n/g, '<br>')
    .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
    .replace(/\*(.*?)\*/g, '<em>$1</em>')
    .replace(/`(.*?)`/g, '<code>$1</code>')
}

// 滚动到底部
function scrollToBottom() {
  nextTick(() => {
    if (messagesRef.value) {
      messagesRef.value.scrollTop = messagesRef.value.scrollHeight
    }
  })
}

// 获取当前时间
function getCurrentTime() {
  return new Date().toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
}

// 发送消息
async function sendMessage() {
  let message = inputMessage.value.trim()
  
  // 根据功能构建消息
  if (activeFunction.value !== 'chat' && !message) {
    message = buildQuickMessage()
  }
  
  if (!message) {
    ElMessage.warning('请输入消息')
    return
  }
  
  // 添加用户消息
  messages.value.push({
    role: 'user',
    content: message,
    time: getCurrentTime()
  })
  
  inputMessage.value = ''
  loading.value = true
  scrollToBottom()
  
  try {
    let url = '/ai/chat'
    let data = { message, sessionId: sessionId.value }
    
    // 根据功能选择API
    if (activeFunction.value === 'risk') {
      url = '/ai/analyze/risk'
      data = { ...quickForm.value, message }
    } else if (activeFunction.value === 'report') {
      url = '/ai/report/generate'
      data = { ...quickForm.value }
    } else if (activeFunction.value === 'collection') {
      url = '/ai/collection/suggest'
      data = { ...quickForm.value }
    } else if (activeFunction.value === 'anomaly') {
      url = '/ai/anomaly/detect'
      data = { message }
    }
    
    const res = await request({ url, method: 'post', data, timeout: 60000 })
    
    // 添加AI回复
    messages.value.push({
      role: 'assistant',
      content: res.data || res.msg || '处理完成',
      time: getCurrentTime()
    })
    
  } catch (error) {
    messages.value.push({
      role: 'assistant',
      content: '抱歉，处理出错：' + (error.message || '未知错误'),
      time: getCurrentTime()
    })
  } finally {
    loading.value = false
    scrollToBottom()
  }
}

// 构建快捷消息
function buildQuickMessage() {
  const form = quickForm.value
  switch (activeFunction.value) {
    case 'risk':
      return '请分析' + (form.companyName || '某企业') + '的贷款风险，贷款金额' + form.amount + '万元，逾期' + form.overdueDays + '天'
    case 'report':
      return '请生成' + form.reportType + '，不良贷款率' + form.nplRatio + '%'
    case 'collection':
      return '请为' + (form.companyName || '某企业') + '提供催收建议，逾期' + form.overdueDays + '天，金额' + form.overdueAmount + '万元'
    default:
      return ''
  }
}

// 清空对话
function clearChat() {
  messages.value = []
  ElMessage.success('对话已清空')
}

// 检测模型状态
async function checkModelStatus() {
  try {
    const res = await fetch('http://localhost:11434/api/tags')
    modelStatus.value = res.ok ? 'online' : 'offline'
  } catch {
    modelStatus.value = 'offline'
  }
}

onMounted(() => {
  checkModelStatus()
  // 添加欢迎消息
  messages.value.push({
    role: 'assistant',
    content: '你好！我是**风控智脑**，你的AI风控助手。\n\n我可以帮你：\n- 📊 分析贷款风险\n- 📝 生成风险报告\n- 📞 制定催收策略\n- 🔍 检测异常模式\n- 💬 解答风控问题\n\n请选择左侧功能，或直接输入问题开始对话。',
    time: getCurrentTime()
  })
})
</script>

<style scoped>
.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.card-header span {
  display: flex;
  align-items: center;
  gap: 8px;
}

.function-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.function-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
}

.function-item:hover {
  background: #f5f7fa;
}

.function-item.active {
  background: #ecf5ff;
  color: #409eff;
}

.model-info {
  text-align: center;
}

.info-title {
  font-size: 12px;
  color: #909399;
  margin-bottom: 8px;
}

.info-value {
  font-size: 14px;
  font-weight: bold;
  margin-bottom: 8px;
}

.chat-card {
  height: calc(100vh - 150px);
  display: flex;
  flex-direction: column;
}

.chat-card :deep(.el-card__body) {
  flex: 1;
  display: flex;
  flex-direction: column;
  padding: 0;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.message {
  display: flex;
  gap: 12px;
  max-width: 80%;
}

.user-message {
  align-self: flex-end;
  flex-direction: row-reverse;
}

.ai-message {
  align-self: flex-start;
}

.message-avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.user-message .message-avatar {
  background: #409eff;
  color: white;
}

.ai-message .message-avatar {
  background: #67c23a;
  color: white;
}

.message-content {
  background: #f5f7fa;
  padding: 12px 16px;
  border-radius: 12px;
}

.user-message .message-content {
  background: #ecf5ff;
}

.message-text {
  line-height: 1.6;
}

.message-text :deep(code) {
  background: #e6e8eb;
  padding: 2px 6px;
  border-radius: 4px;
  font-family: monospace;
}

.message-time {
  font-size: 11px;
  color: #909399;
  margin-top: 8px;
}

.typing {
  display: flex;
  gap: 4px;
}

.dot {
  width: 8px;
  height: 8px;
  background: #909399;
  border-radius: 50%;
  animation: typing 1.4s infinite;
}

.dot:nth-child(2) { animation-delay: 0.2s; }
.dot:nth-child(3) { animation-delay: 0.4s; }

@keyframes typing {
  0%, 60%, 100% { transform: translateY(0); }
  30% { transform: translateY(-8px); }
}

.chat-input {
  border-top: 1px solid #e4e7ed;
  padding: 16px;
}

.quick-inputs {
  margin-bottom: 12px;
}

.input-row {
  display: flex;
  gap: 12px;
}

.input-row .el-input {
  flex: 1;
}

.input-tip {
  font-size: 11px;
  color: #909399;
  margin-top: 8px;
  text-align: right;
}
</style>

<template>
  <div class="app-container">
    <el-tabs v-model="activeTab">
      <!-- API 配置 -->
      <el-tab-pane label="数据源配置" name="api">
        <el-card>
          <el-form :model="apiConfig" label-width="140px">
            <el-divider content-position="left">天眼查 API</el-divider>
            <el-form-item label="API Key"><el-input v-model="apiConfig.tianyanchaKey" placeholder="请输入天眼查 API Key" show-password style="width:400px" /></el-form-item>
            <el-form-item label="接口地址"><el-input v-model="apiConfig.tianyanchaUrl" style="width:400px" /></el-form-item>
            <el-form-item label="状态">
              <el-tag :type="apiConfig.tianyanchaStatus === '正常' ? 'success' : 'danger'">{{ apiConfig.tianyanchaStatus }}</el-tag>
              <el-button style="margin-left:12px" size="small" @click="testApi('tianyancha')">测试连接</el-button>
            </el-form-item>

            <el-divider content-position="left">企查查 API</el-divider>
            <el-form-item label="API Key"><el-input v-model="apiConfig.qichachaKey" placeholder="请输入企查查 API Key" show-password style="width:400px" /></el-form-item>
            <el-form-item label="接口地址"><el-input v-model="apiConfig.qichachaUrl" style="width:400px" /></el-form-item>
            <el-form-item label="状态">
              <el-tag :type="apiConfig.qichachaStatus === '正常' ? 'success' : 'info'">{{ apiConfig.qichachaStatus }}</el-tag>
              <el-button style="margin-left:12px" size="small" @click="testApi('qichacha')">测试连接</el-button>
            </el-form-item>

            <el-divider content-position="left">中国执行信息公开网</el-divider>
            <el-form-item label="采集方式">
              <el-radio-group v-model="apiConfig.executionMode">
                <el-radio value="api">API 接口</el-radio>
                <el-radio value="crawler">定时爬取</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="采集频率">
              <el-select v-model="apiConfig.executionFrequency" style="width:200px">
                <el-option label="每小时" value="hourly" />
                <el-option label="每日" value="daily" />
                <el-option label="每周" value="weekly" />
              </el-select>
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="saveConfig">保存配置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-tab-pane>

      <!-- 通知配置 -->
      <el-tab-pane label="通知配置" name="notify">
        <el-card>
          <el-form :model="notifyConfig" label-width="120px">
            <el-divider content-position="left">短信通知</el-divider>
            <el-form-item label="启用"><el-switch v-model="notifyConfig.smsEnabled" /></el-form-item>
            <el-form-item label="短信模板"><el-input v-model="notifyConfig.smsTemplate" type="textarea" :rows="3" style="width:500px" /></el-form-item>

            <el-divider content-position="left">企业微信</el-divider>
            <el-form-item label="启用"><el-switch v-model="notifyConfig.wecomEnabled" /></el-form-item>
            <el-form-item label="Webhook"><el-input v-model="notifyConfig.wecomWebhook" style="width:500px" /></el-form-item>
            <el-form-item label="@人员"><el-input v-model="notifyConfig.wecomMention" placeholder="企业微信用户ID，多个用逗号分隔" style="width:500px" /></el-form-item>

            <el-divider content-position="left">邮件通知</el-divider>
            <el-form-item label="启用"><el-switch v-model="notifyConfig.emailEnabled" /></el-form-item>
            <el-form-item label="收件人"><el-input v-model="notifyConfig.emailTo" placeholder="多个邮箱用逗号分隔" style="width:500px" /></el-form-item>

            <el-form-item>
              <el-button type="primary" @click="saveNotify">保存配置</el-button>
              <el-button @click="testNotify">发送测试通知</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-tab-pane>

      <!-- 系统参数 -->
      <el-tab-pane label="系统参数" name="params">
        <el-card>
          <el-form :model="sysParams" label-width="160px">
            <el-form-item label="风险评分算法">
              <el-select v-model="sysParams.scoreAlgorithm" style="width:300px">
                <el-option label="加权评分法" value="weighted" />
                <el-option label="机器学习模型" value="ml" />
                <el-option label="规则引擎" value="rule_engine" />
              </el-select>
            </el-form-item>
            <el-form-item label="自动预警">
              <el-switch v-model="sysParams.autoAlert" /><span style="margin-left:8px;color:#909399">风险事件入库后自动匹配规则触发预警</span>
            </el-form-item>
            <el-form-item label="预警去重窗口（小时）">
              <el-input-number v-model="sysParams.dedupHours" :min="1" :max="168" />
            </el-form-item>
            <el-form-item label="高风险阈值">
              <el-slider v-model="sysParams.highRiskThreshold" :min="0" :max="100" :marks="{ 50: '50', 80: '80' }" style="width:400px" />
            </el-form-item>
            <el-form-item label="数据保留天数">
              <el-input-number v-model="sysParams.dataRetentionDays" :min="30" :max="365" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="saveParams">保存</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

const activeTab = ref('api')

const apiConfig = ref({
  tianyanchaKey: '', tianyanchaUrl: 'https://open.tianyancha.com/api', tianyanchaStatus: '未配置',
  qichachaKey: '', qichachaUrl: 'https://api.qichacha.com', qichachaStatus: '未配置',
  executionMode: 'crawler', executionFrequency: 'daily'
})

const notifyConfig = ref({
  smsEnabled: false, smsTemplate: '【风控系统】预警通知：{companyName}触发{alertLevel}级预警，事件：{eventTitle}',
  wecomEnabled: false, wecomWebhook: '', wecomMention: '',
  emailEnabled: false, emailTo: ''
})

const sysParams = ref({
  scoreAlgorithm: 'weighted', autoAlert: true, dedupHours: 24, highRiskThreshold: 70, dataRetentionDays: 180
})

function testApi(name) { ElMessage.success(`${name} API 连接测试成功`) }
function saveConfig() { ElMessage.success('数据源配置已保存') }
function saveNotify() { ElMessage.success('通知配置已保存') }
function testNotify() { ElMessage.success('测试通知已发送') }
function saveParams() { ElMessage.success('系统参数已保存') }
</script>

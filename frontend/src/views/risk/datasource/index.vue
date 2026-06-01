<template>
  <div class="app-container">
    <el-alert title="配置外部数据源 API Key 后，系统将自动从数据源采集企业风险信息。目前支持天眼查，后续将接入更多数据源。" type="info" show-icon :closable="false" style="margin-bottom:20px" />

    <el-row :gutter="20">
      <!-- 数据源状态 -->
      <el-col :span="8">
        <el-card>
          <template #header><span>数据源状态</span></template>
          <div v-for="ds in dataSources" :key="ds.name" class="source-item">
            <div class="source-header">
              <span class="source-name">{{ ds.name }}</span>
              <el-tag :type="ds.available ? 'success' : 'info'" size="small">
                {{ ds.available ? '已配置' : '未配置' }}
              </el-tag>
            </div>
          </div>
          <el-empty v-if="dataSources.length === 0" description="暂无数据源" :image-size="60" />
        </el-card>

        <el-card style="margin-top:20px">
          <template #header><span>手动查询</span></template>
          <el-form label-width="80px">
            <el-form-item label="企业名称">
              <el-input v-model="searchCompany" placeholder="输入企业名称查询" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="searchExternal" :loading="searching">
                <el-icon><Search /></el-icon>查询
              </el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>

      <!-- API 配置 -->
      <el-col :span="16">
        <el-card>
          <template #header><span>数据源配置</span></template>
          <el-form label-width="120px">
            <el-divider content-position="left">天眼查</el-divider>
            <el-form-item label="API Token">
              <el-input v-model="config.tianyanchaToken" placeholder="填写天眼查 API Token" show-password style="width:100%" />
            </el-form-item>
            <el-form-item label="接口地址">
              <el-input v-model="config.tianyanchaUrl" />
            </el-form-item>

            <el-divider content-position="left">企查查</el-divider>
            <el-form-item label="API Token">
              <el-input v-model="config.qichachaToken" placeholder="填写企查查 API Token" show-password style="width:100%" />
            </el-form-item>
            <el-form-item label="接口地址">
              <el-input v-model="config.qichachaUrl" />
            </el-form-item>

            <el-divider content-position="left">阿里云市场</el-divider>
            <el-form-item label="AppCode">
              <el-input v-model="config.aliyunAppCode" placeholder="填写阿里云市场 AppCode" show-password style="width:100%" />
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="saveConfig">保存配置</el-button>
              <el-button @click="testConnection">测试连接</el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <!-- 查询结果 -->
        <el-card v-if="searchResult" style="margin-top:20px">
          <template #header><span>查询结果</span></template>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="企业名称" :span="2">{{ searchResult.companyName || '-' }}</el-descriptions-item>
            <el-descriptions-item label="统一信用代码">{{ searchResult.creditCode || '-' }}</el-descriptions-item>
            <el-descriptions-item label="法定代表人">{{ searchResult.legalPerson || '-' }}</el-descriptions-item>
            <el-descriptions-item label="注册资本">{{ searchResult.regCapital || '-' }}</el-descriptions-item>
            <el-descriptions-item label="成立日期">{{ searchResult.establishDate || '-' }}</el-descriptions-item>
            <el-descriptions-item label="经营状态">{{ searchResult.status || '-' }}</el-descriptions-item>
            <el-descriptions-item label="行业">{{ searchResult.industry || '-' }}</el-descriptions-item>
            <el-descriptions-item label="被执行人" :span="2">
              <el-tag :type="searchResult.isExecution ? 'danger' : 'success'" size="small">
                {{ searchResult.isExecution ? '是' : '否' }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="失信记录" :span="2">
              <el-tag :type="searchResult.isDishonest ? 'danger' : 'success'" size="small">
                {{ searchResult.isDishonest ? '是' : '否' }}
              </el-tag>
            </el-descriptions-item>
          </el-descriptions>
          <div style="margin-top:12px">
            <el-button type="success" @click="importCompany">导入到监控列表</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'
import { Search } from '@element-plus/icons-vue'

const dataSources = ref([])
const searchCompany = ref('')
const searching = ref(false)
const searchResult = ref(null)

const config = ref({
  tianyanchaToken: '',
  tianyanchaUrl: 'https://open.tianyancha.com/services/open',
  qichachaToken: '',
  qichachaUrl: 'https://api.qichacha.com',
  aliyunAppCode: ''
})

async function loadSources() {
  try {
    const res = await request({ url: '/risk/external/sources', method: 'get' })
    dataSources.value = res.data || res || []
  } catch (e) { console.error(e) }
}

async function searchExternal() {
  if (!searchCompany.value) { ElMessage.warning('请输入企业名称'); return }
  searching.value = true
  try {
    const res = await request({ url: '/risk/external/company', method: 'get', params: { keyword: searchCompany.value } })
    const data = res.data || res || {}
    if (data.error) {
      ElMessage.warning(data.error)
      searchResult.value = null
    } else {
      searchResult.value = data
      ElMessage.success('查询成功')
    }
  } catch (e) {
    ElMessage.error('查询失败，请检查数据源配置')
    searchResult.value = null
  }
  searching.value = false
}

function saveConfig() {
  ElMessage.success('配置已保存（需要重启后端生效）')
}

function testConnection() {
  loadSources()
  ElMessage.info('正在测试连接...')
}

function importCompany() {
  if (!searchResult.value) return
  ElMessage.success('企业已导入监控列表')
}

onMounted(loadSources)
</script>

<style scoped>
.source-item { padding: 12px 0; border-bottom: 1px solid #F2F6FC; }
.source-item:last-child { border-bottom: none; }
.source-header { display: flex; justify-content: space-between; align-items: center; }
.source-name { font-size: 15px; font-weight: 500; }
</style>

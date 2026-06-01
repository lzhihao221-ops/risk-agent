<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!-- 左侧：地图 -->
      <el-col :span="16">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>GIS地理信息</span>
              <div>
                <el-radio-group v-model="mapMode" size="small" @change="updateMap">
                  <el-radio-button label="branches">网点</el-radio-button>
                  <el-radio-button label="customers">客户</el-radio-button>
                  <el-radio-button label="risk">风险</el-radio-button>
                </el-radio-group>
              </div>
            </div>
          </template>
          
          <!-- 地图容器 -->
          <div id="map-container" ref="mapContainer" style="height: 500px; width: 100%;"></div>
          
          <!-- 地图图例 -->
          <div class="map-legend">
            <div v-if="mapMode === 'branches'">
              <span class="legend-item branch">● 网点位置</span>
            </div>
            <div v-else-if="mapMode === 'customers'">
              <span class="legend-item normal">● 正常客户</span>
              <span class="legend-item watch">● 关注客户</span>
            </div>
            <div v-else>
              <span class="legend-item low">● 低风险</span>
              <span class="legend-item medium">● 中风险</span>
              <span class="legend-item high">● 高风险</span>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 右侧：统计 + 打卡功能 -->
      <el-col :span="8">
        <!-- GIS统计卡片 -->
        <el-card shadow="hover" style="margin-bottom: 15px; border-top: 3px solid #67c23a;">
          <template #header>
            <span style="font-weight: 600;">📊 数据概览</span>
          </template>
          <el-row :gutter="10">
            <el-col :span="8" style="text-align: center;">
              <div style="font-size: 24px; font-weight: bold; color: #409eff;">{{ branches.length }}</div>
              <div style="font-size: 12px; color: #909399;">网点</div>
            </el-col>
            <el-col :span="8" style="text-align: center;">
              <div style="font-size: 24px; font-weight: bold; color: #67c23a;">{{ customers.length }}</div>
              <div style="font-size: 12px; color: #909399;">区域</div>
            </el-col>
            <el-col :span="8" style="text-align: center;">
              <div style="font-size: 24px; font-weight: bold; color: #f56c6c;">{{ riskPoints.length }}</div>
              <div style="font-size: 12px; color: #909399;">风险点</div>
            </el-col>
          </el-row>
          <el-row :gutter="10" style="margin-top: 10px;">
            <el-col :span="12" style="text-align: center;">
              <div style="font-size: 16px; font-weight: bold; color: #e6a23c;">{{ totalCustomers }}</div>
              <div style="font-size: 12px; color: #909399;">客户总数</div>
            </el-col>
            <el-col :span="12" style="text-align: center;">
              <div style="font-size: 16px; font-weight: bold; color: #909399;">{{ checkinRecords.length }}</div>
              <div style="font-size: 12px; color: #909399;">打卡记录</div>
            </el-col>
          </el-row>
        </el-card>

        <!-- 打卡卡片 -->
        <el-card shadow="hover" class="checkin-card">
          <template #header>
            <div class="card-header">
              <span>外勤打卡</span>
              <el-tag :type="isCheckined ? 'success' : 'info'">
                {{ isCheckined ? '已打卡' : '未打卡' }}
              </el-tag>
            </div>
          </template>
          
          <div class="checkin-info">
            <div class="info-item">
              <el-icon><Clock /></el-icon>
              <span>{{ currentTime }}</span>
            </div>
            <div class="info-item" v-if="currentLocation">
              <el-icon><Location /></el-icon>
              <span>{{ currentLocation.address }}</span>
            </div>
          </div>

          <div class="checkin-form">
            <el-form :model="checkinForm" label-position="top">
              <el-form-item label="打卡类型">
                <el-select v-model="checkinForm.type" style="width: 100%">
                  <el-option label="客户拜访" value="visit" />
                  <el-option label="贷后检查" value="check" />
                  <el-option label="风险排查" value="risk" />
                  <el-option label="其他" value="other" />
                </el-select>
              </el-form-item>
              <el-form-item label="拜访企业">
                <el-input v-model="checkinForm.companyName" placeholder="输入企业名称..." />
              </el-form-item>
              <el-form-item label="备注">
                <el-input v-model="checkinForm.remark" type="textarea" :rows="2" placeholder="输入打卡备注..." />
              </el-form-item>
              <el-button type="primary" @click="handleCheckin" :loading="checkinLoading" style="width: 100%">
                <el-icon><Check /></el-icon>
                立即打卡
              </el-button>
            </el-form>
          </div>
        </el-card>

        <!-- 打卡记录 -->
        <el-card shadow="hover" style="margin-top: 20px;">
          <template #header>
            <span>今日打卡记录</span>
          </template>
          <el-timeline>
            <el-timeline-item v-for="record in checkinRecords" :key="record.time"
                              :timestamp="record.time" placement="top">
              <el-card shadow="never">
                <div class="record-item">
                  <div><strong>{{ record.user }}</strong> - {{ record.type }}</div>
                  <div class="record-address">{{ record.address }}</div>
                </div>
              </el-card>
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { Location, Clock, Check } from '@element-plus/icons-vue'
import request from '@/utils/request'

// 地图模式
const mapMode = ref('branches')
const mapContainer = ref(null)

// 地图数据
const branches = ref([])
const customers = ref([])
const riskPoints = ref([])

// 地图实例
let map = null
let markers = []
let heatLayer = null
let infoWindow = null

// 打卡相关
const currentTime = ref('')
const currentLocation = ref(null)
const isCheckined = ref(false)
const checkinLoading = ref(false)
const checkinRecords = ref([])

const checkinForm = ref({
  type: 'visit',
  companyName: '',
  remark: ''
})

// 统计数据
const totalCustomers = computed(() => {
  return customers.value.reduce((sum, c) => sum + (c.customerCount || 0), 0)
})

// 时间更新定时器
let timeTimer = null

// 初始化地图
function initMap() {
  // 使用Leaflet开源地图库
  const script = document.createElement('script')
  script.src = 'https://unpkg.com/leaflet@1.9.4/dist/leaflet.js'
  script.onload = () => {
    // 加载Leaflet热力图插件
    const heatScript = document.createElement('script')
    heatScript.src = 'https://unpkg.com/leaflet.heat@0.2.0/dist/leaflet-heat.js'
    heatScript.onload = () => {
      const link = document.createElement('link')
      link.rel = 'stylesheet'
      link.href = 'https://unpkg.com/leaflet@1.9.4/dist/leaflet.css'
      document.head.appendChild(link)
      nextTick(() => { createMap() })
    }
    document.head.appendChild(heatScript)
  }
  document.head.appendChild(script)
}

function createMap() {
  if (!mapContainer.value || !window.L) return
  
  // 创建地图，默认中心在上海
  map = window.L.map(mapContainer.value).setView([31.2304, 121.4737], 11)
  
  // 添加OpenStreetMap图层（完全免费，无需API Key）
  window.L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
  }).addTo(map)
  
  // 添加比例尺
  window.L.control.scale().addTo(map)
  
  // 加载数据并显示标记
  loadMapData()
}

// 加载地图数据
async function loadMapData() {
  try {
    // 加载网点
    const branchRes = await request({ url: '/gis/branches', method: 'get' })
    branches.value = branchRes.data || []
    
    // 加载客户分布
    const customerRes = await request({ url: '/gis/customers', method: 'get' })
    customers.value = customerRes.data || []
    
    // 加载风险热力图
    const riskRes = await request({ url: '/gis/risk-heatmap', method: 'get' })
    riskPoints.value = riskRes.data || []
    
    // 加载打卡记录
    const recordRes = await request({ url: '/gis/checkin/records', method: 'get' })
    checkinRecords.value = recordRes.data || []
    
    // 显示默认标记
    updateMap()
    
  } catch (error) {
    console.error('加载地图数据失败:', error)
    // 使用默认数据
    branches.value = [
      { id: 1, name: '总行营业部', longitude: 121.4737, latitude: 31.2304, address: '上海市黄浦区' },
      { id: 2, name: '浦东分行', longitude: 121.5444, latitude: 31.2222, address: '上海市浦东新区' },
      { id: 3, name: '北京分行', longitude: 116.4074, latitude: 39.9042, address: '北京市朝阳区' },
      { id: 4, name: '深圳分行', longitude: 114.0579, latitude: 22.5431, address: '深圳市福田区' },
      { id: 5, name: '杭州分行', longitude: 120.1614, latitude: 30.2800, address: '杭州市上城区' }
    ]
    customers.value = [
      { id: 1, region: '上海地区', longitude: 121.47, latitude: 31.23, customerCount: 156, status: '正常' },
      { id: 2, region: '北京地区', longitude: 116.41, latitude: 39.90, customerCount: 89, status: '正常' },
      { id: 3, region: '深圳地区', longitude: 114.06, latitude: 22.54, customerCount: 67, status: '关注' },
      { id: 4, region: '杭州地区', longitude: 120.16, latitude: 30.28, customerCount: 45, status: '正常' },
      { id: 5, region: '成都地区', longitude: 104.07, latitude: 30.67, customerCount: 34, status: '正常' }
    ]
    riskPoints.value = [
      { longitude: 121.47, latitude: 31.23, intensity: 0.8, level: '高风险' },
      { longitude: 116.41, latitude: 39.90, intensity: 0.3, level: '低风险' },
      { longitude: 114.06, latitude: 22.54, intensity: 0.6, level: '中风险' },
      { longitude: 120.16, latitude: 30.28, intensity: 0.2, level: '低风险' }
    ]
    updateMap()
  }
}

// 更新地图标记
function updateMap() {
  if (!map || !window.L) return
  
  // 清除现有标记和热力图层
  markers.forEach(m => map.removeLayer(m))
  markers = []
  if (heatLayer) {
    map.removeLayer(heatLayer)
    heatLayer = null
  }
  
  let data = []
  let iconColor = '#409eff'
  
  if (mapMode.value === 'branches') {
    data = branches.value
    iconColor = '#409eff'
  } else if (mapMode.value === 'customers') {
    data = customers.value
  } else {
    data = riskPoints.value
  }
  
  // 风险模式使用热力图
  if (mapMode.value === 'risk' && window.L.heatLayer && data.length > 0) {
    const heatData = data.map(p => [p.latitude, p.longitude, p.intensity || 0.5])
    heatLayer = window.L.heatLayer(heatData, {
      radius: 35,
      blur: 25,
      maxZoom: 13,
      max: 1.0,
      gradient: { 0.2: '#67c23a', 0.5: '#e6a23c', 0.8: '#f56c6c', 1.0: '#8b0000' }
    }).addTo(map)
  }
  
  data.forEach(point => {
    let color = iconColor
    let popupContent = ''
    let size = 12
    
    if (mapMode.value === 'branches') {
      popupContent = `<b>${point.name}</b><br>${point.address}<br>负责人: ${point.manager || '-'}<br>员工: ${point.staffCount || '-'}人`
      size = point.type === 'headquarters' ? 16 : point.type === 'branch' ? 14 : 12
    } else if (mapMode.value === 'customers') {
      color = point.status === '关注' ? '#e6a23c' : point.status === '预警' ? '#f56c6c' : '#67c23a'
      popupContent = `<b>${point.region}</b><br>客户数: ${point.customerCount}<br>贷款: ${point.loanCount || '-'}笔<br>金额: ${point.loanAmount ? (point.loanAmount / 10000).toFixed(0) + '万' : '-'}<br>状态: ${point.status}`
      size = Math.min(20, Math.max(10, Math.sqrt(point.customerCount / 5)))
    } else {
      color = point.level === '高风险' ? '#f56c6c' : point.level === '中风险' ? '#e6a23c' : '#67c23a'
      popupContent = `<b>${point.name || point.level}</b><br>类型: ${point.type || '-'}<br>风险指数: ${point.intensity}<br>${point.company ? '企业: ' + point.company + '<br>' : ''}${point.amount ? '金额: ' + (point.amount / 10000).toFixed(0) + '万' : ''}${point.description ? '<br>' + point.description : ''}`
      size = Math.min(18, Math.max(10, (point.intensity || 0.5) * 20))
    }
    
    // 创建自定义图标
    const icon = window.L.divIcon({
      className: 'custom-marker',
      html: `<div style="background: ${color}; width: ${size}px; height: ${size}px; border-radius: 50%; border: 2px solid white; box-shadow: 0 2px 4px rgba(0,0,0,0.3);"></div>`,
      iconSize: [size, size]
    })
    
    const marker = window.L.marker([point.latitude, point.longitude], { icon })
      .addTo(map)
      .bindPopup(popupContent)
    
    markers.push(marker)
  })
  
  // 调整地图视野
  if (markers.length > 0) {
    const group = new window.L.featureGroup(markers)
    map.fitBounds(group.getBounds().pad(0.1))
  }
}

// 获取当前位置
function getCurrentLocation() {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(
      (position) => {
        currentLocation.value = {
          longitude: position.coords.longitude,
          latitude: position.coords.latitude,
          address: `经度: ${position.coords.longitude.toFixed(4)}, 纬度: ${position.coords.latitude.toFixed(4)}`
        }
        
        // 在地图上显示当前位置
        if (map && window.L) {
          const icon = window.L.divIcon({
            className: 'current-location-marker',
            html: '<div style="background: #f56c6c; width: 16px; height: 16px; border-radius: 50%; border: 3px solid white; box-shadow: 0 0 10px rgba(245,108,108,0.5);"></div>',
            iconSize: [16, 16]
          })
          
          window.L.marker([position.coords.latitude, position.coords.longitude], { icon })
            .addTo(map)
            .bindPopup('<b>我的位置</b>')
        }
      },
      (error) => {
        console.warn('获取位置失败:', error)
        currentLocation.value = {
          longitude: 121.4737,
          latitude: 31.2304,
          address: '上海市黄浦区（默认位置）'
        }
      }
    )
  } else {
    currentLocation.value = {
      longitude: 121.4737,
      latitude: 31.2304,
      address: '上海市黄浦区（默认位置）'
    }
  }
}

// 更新时间
function updateTime() {
  currentTime.value = new Date().toLocaleString('zh-CN')
}

// 打卡
async function handleCheckin() {
  if (!currentLocation.value) {
    ElMessage.warning('正在获取位置，请稍后...')
    return
  }
  
  checkinLoading.value = true
  try {
    await request({
      url: '/gis/checkin',
      method: 'post',
      data: {
        userId: 1,
        userName: '当前用户',
        longitude: currentLocation.value.longitude,
        latitude: currentLocation.value.latitude,
        address: currentLocation.value.address,
        checkinType: checkinForm.value.type,
        companyName: checkinForm.value.companyName,
        remark: checkinForm.value.remark
      }
    })
    
    isCheckined.value = true
    ElMessage.success('打卡成功！')
    checkinForm.value.remark = ''
    
    // 刷新记录
    loadMapData()
  } catch (error) {
    ElMessage.error('打卡失败: ' + (error.message || '未知错误'))
  } finally {
    checkinLoading.value = false
  }
}

onMounted(() => {
  getCurrentLocation()
  updateTime()
  timeTimer = setInterval(updateTime, 1000)
  
  // 延迟初始化地图，确保DOM已渲染
  setTimeout(() => {
    initMap()
  }, 100)
})

onUnmounted(() => {
  if (timeTimer) clearInterval(timeTimer)
  if (map) {
    map.remove()
    map = null
  }
})
</script>

<style scoped>
.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

#map-container {
  border-radius: 8px;
  border: 1px solid #e4e7ed;
}

.map-legend {
  margin-top: 10px;
  padding: 10px;
  background: #f5f7fa;
  border-radius: 4px;
  font-size: 12px;
}

.legend-item {
  margin-right: 20px;
}

.legend-item.branch { color: #409eff; }
.legend-item.normal { color: #67c23a; }
.legend-item.watch { color: #e6a23c; }
.legend-item.low { color: #67c23a; }
.legend-item.medium { color: #e6a23c; }
.legend-item.high { color: #f56c6c; }

.checkin-card {
  border-top: 3px solid #409eff;
}

.checkin-info {
  margin-bottom: 20px;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 0;
  border-bottom: 1px solid #f0f0f0;
}

.record-item {
  font-size: 13px;
}

.record-address {
  color: #909399;
  margin-top: 5px;
}

/* 自定义标记样式 */
:deep(.custom-marker) {
  background: transparent;
  border: none;
}

:deep(.current-location-marker) {
  background: transparent;
  border: none;
}
</style>

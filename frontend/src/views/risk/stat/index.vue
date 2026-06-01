<template>
  <div class="app-container">
    <!-- 筛选栏 -->
    <el-form :inline="true" style="margin-bottom:20px">
      <el-form-item label="统计周期">
        <el-date-picker v-model="dateRange" type="daterange" range-separator="至" start-placeholder="开始" end-placeholder="结束" value-format="YYYY-MM-DD" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="refresh"><el-icon><Refresh /></el-icon>刷新</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="20">
      <!-- 预警趋势 -->
      <el-col :span="12">
        <el-card>
          <template #header><span>预警趋势</span></template>
          <div class="chart-area">
            <div v-for="(item, idx) in alertTrend" :key="idx" class="trend-bar">
              <span class="trend-label">{{ item.date }}</span>
              <div class="trend-bar-bg">
                <div class="trend-bar-fill" :style="{ width: item.pct + '%', background: item.high > 0 ? '#F56C6C' : '#409EFF' }"></div>
              </div>
              <span class="trend-value">{{ item.count }}</span>
            </div>
            <el-empty v-if="alertTrend.length === 0" description="暂无数据" :image-size="60" />
          </div>
        </el-card>
      </el-col>

      <!-- 事件类型占比 -->
      <el-col :span="12">
        <el-card>
          <template #header><span>事件类型分布</span></template>
          <div class="chart-area">
            <div v-for="(item, idx) in eventTypeDist" :key="idx" class="dist-item">
              <div class="dist-header">
                <span>{{ item.type }}</span>
                <span>{{ item.count }} 件 ({{ item.pct }}%)</span>
              </div>
              <el-progress :percentage="item.pct" :color="item.color" :show-text="false" :stroke-width="12" />
            </div>
            <el-empty v-if="eventTypeDist.length === 0" description="暂无数据" :image-size="60" />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top:20px">
      <!-- 行业风险分布 -->
      <el-col :span="12">
        <el-card>
          <template #header><span>行业风险分布</span></template>
          <el-table :data="industryRisk" stripe size="small">
            <el-table-column prop="industry" label="行业" width="100" />
            <el-table-column prop="count" label="企业数" width="70" align="center" />
            <el-table-column prop="avgScore" label="平均风险分" width="100" align="center">
              <template #default="{ row }">
                <span :style="{ color: row.avgScore >= 70 ? '#F56C6C' : row.avgScore >= 40 ? '#E6A23C' : '#67C23A', fontWeight: 'bold' }">{{ row.avgScore }}</span>
              </template>
            </el-table-column>
            <el-table-column label="风险分布">
              <template #default="{ row }">
                <el-progress :percentage="row.highPct" :color="'#F56C6C'" :show-text="false" :stroke-width="8" style="display:inline-block;width:60%" />
                <span style="font-size:11px;color:#909399;margin-left:8px">高{{ row.highPct }}%</span>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>

      <!-- 处置效率 -->
      <el-col :span="12">
        <el-card>
          <template #header><span>处置效率统计</span></template>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="本月预警总数">
              <span style="font-size:20px;font-weight:bold;color:#409EFF">3</span>
            </el-descriptions-item>
            <el-descriptions-item label="已处理">
              <span style="font-size:20px;font-weight:bold;color:#67C23A">1</span>
            </el-descriptions-item>
            <el-descriptions-item label="平均处置时间">
              <span style="font-size:20px;font-weight:bold;color:#E6A23C">4.2h</span>
            </el-descriptions-item>
            <el-descriptions-item label="处置完成率">
              <span style="font-size:20px;font-weight:bold;color:#67C23A">33.3%</span>
            </el-descriptions-item>
            <el-descriptions-item label="逾期任务" :span="2">
              <span style="font-size:20px;font-weight:bold;color:#F56C6C">0</span>
            </el-descriptions-item>
          </el-descriptions>
          <div style="margin-top:16px">
            <h4 style="color:#606266;margin-bottom:12px">处置人排名</h4>
            <div v-for="(item, idx) in handlerRank" :key="idx" class="rank-item">
              <span class="rank-idx" :style="{ background: idx === 0 ? '#F56C6C' : idx === 1 ? '#E6A23C' : '#C0C4CC' }">{{ idx + 1 }}</span>
              <span class="rank-name">{{ item.name }}</span>
              <span class="rank-count">{{ item.handled }} 件</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { Refresh } from '@element-plus/icons-vue'

const dateRange = ref([])

const alertTrend = ref([
  { date: '05-24', count: 0, high: 0, pct: 0 },
  { date: '05-25', count: 1, high: 1, pct: 33 },
  { date: '05-26', count: 0, high: 0, pct: 0 },
  { date: '05-27', count: 2, high: 0, pct: 66 },
  { date: '05-28', count: 0, high: 0, pct: 0 },
  { date: '05-29', count: 0, high: 0, pct: 0 },
  { date: '05-30', count: 3, high: 2, pct: 100 }
])

const eventTypeDist = ref([
  { type: '被执行', count: 3, pct: 37, color: '#F56C6C' },
  { type: '失信被执行', count: 1, pct: 13, color: '#E6A23C' },
  { type: '经营异常', count: 1, pct: 13, color: '#409EFF' },
  { type: '涉诉', count: 1, pct: 13, color: '#909399' },
  { type: '法人变更', count: 1, pct: 13, color: '#67C23A' },
  { type: '股权冻结', count: 1, pct: 13, color: '#F56C6C' }
])

const industryRisk = ref([
  { industry: '金融', count: 1, avgScore: 90, highPct: 100 },
  { industry: '电子', count: 1, avgScore: 85, highPct: 100 },
  { industry: '建材', count: 1, avgScore: 65, highPct: 0 },
  { industry: '制造', count: 1, avgScore: 55, highPct: 0 },
  { industry: '贸易', count: 2, avgScore: 15, highPct: 0 }
])

const handlerRank = ref([
  { name: '客户经理A', handled: 2 },
  { name: '客户经理B', handled: 1 }
])

function refresh() { /* refresh data */ }
</script>

<style scoped>
.chart-area { min-height: 250px; }
.trend-bar { display: flex; align-items: center; margin-bottom: 10px; }
.trend-label { width: 50px; font-size: 12px; color: #909399; }
.trend-bar-bg { flex: 1; height: 16px; background: #F2F6FC; border-radius: 8px; overflow: hidden; margin: 0 10px; }
.trend-bar-fill { height: 100%; border-radius: 8px; transition: width 0.6s ease; }
.trend-value { width: 30px; text-align: right; font-size: 12px; font-weight: bold; color: #303133; }
.dist-item { margin-bottom: 16px; }
.dist-header { display: flex; justify-content: space-between; font-size: 13px; margin-bottom: 4px; color: #606266; }
.rank-item { display: flex; align-items: center; padding: 8px 0; border-bottom: 1px solid #F2F6FC; }
.rank-idx { width: 22px; height: 22px; border-radius: 50%; color: #fff; font-size: 12px; display: flex; align-items: center; justify-content: center; margin-right: 10px; }
.rank-name { flex: 1; font-size: 13px; }
.rank-count { font-size: 13px; color: #909399; }
</style>

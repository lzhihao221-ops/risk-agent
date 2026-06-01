<template>
  <div class="app-container">
    <el-card>
      <template #header>
        <div style="display:flex;justify-content:space-between;align-items:center">
          <span>关联图谱</span>
          <div>
            <el-button type="primary" size="small" @click="showAdd"><el-icon><Plus /></el-icon>新增关系</el-button>
            <el-button size="small" @click="refreshGraph"><el-icon><Refresh /></el-icon>刷新</el-button>
          </div>
        </div>
      </template>
      <!-- 关系网络图 -->
      <div ref="graphRef" style="height:500px;border:1px solid #EBEEF5;border-radius:4px"></div>
    </el-card>

    <!-- 关系列表 -->
    <el-card style="margin-top:20px">
      <template #header><span>关系列表</span></template>
      <el-table :data="graphData" border stripe>
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column label="企业名称" min-width="160">
          <template #default="{ row }">{{ getCompanyName(row.companyId) }}</template>
        </el-table-column>
        <el-table-column prop="relatedCompany" label="关联方" min-width="140" />
        <el-table-column prop="relatedPerson" label="关联人" width="80" />
        <el-table-column prop="relationType" label="关系类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getRelationTagType(row.relationType)" size="small">{{ row.relationType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="relationDetail" label="关系说明" min-width="200" show-overflow-tooltip />
        <el-table-column prop="dataSource" label="数据来源" width="80" />
        <el-table-column label="操作" width="80" align="center">
          <template #default="{ row }">
            <el-button type="danger" link size="small" @click="deleteRelation(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增对话框 -->
    <el-dialog v-model="dialogVisible" title="新增关联关系" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="企业" required>
          <el-select v-model="form.companyId" filterable placeholder="选择企业" style="width:100%">
            <el-option v-for="c in companyOptions" :key="c.id" :label="c.companyName" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="关联方" required>
          <el-input v-model="form.relatedCompany" placeholder="关联企业或个人名称" />
        </el-form-item>
        <el-form-item label="关联人"><el-input v-model="form.relatedPerson" /></el-form-item>
        <el-form-item label="关系类型" required>
          <el-select v-model="form.relationType" style="width:100%">
            <el-option label="股权穿透" value="股权穿透" />
            <el-option label="担保链" value="担保链" />
            <el-option label="关联交易" value="关联交易" />
            <el-option label="共同法人" value="共同法人" />
            <el-option label="上下游" value="上下游" />
          </el-select>
        </el-form-item>
        <el-form-item label="关系说明"><el-input v-model="form.relationDetail" type="textarea" /></el-form-item>
        <el-form-item label="数据来源">
          <el-select v-model="form.dataSource" style="width:100%">
            <el-option label="天眼查" value="天眼查" />
            <el-option label="企查查" value="企查查" />
            <el-option label="手工录入" value="手工录入" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getRelationList, addRelation, deleteRelation as apiDelete, getCompanyList } from '@/api/risk'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Refresh } from '@element-plus/icons-vue'
import * as echarts from 'echarts'

const graphRef = ref(null)
const graphData = ref([])
const companyOptions = ref([])
const dialogVisible = ref(false)
const form = ref({ companyId: null, relatedCompany: '', relatedPerson: '', relationType: '', relationDetail: '', dataSource: '手工录入' })

const companyMap = ref({})
function getCompanyName(id) { return companyMap.value[id] || `企业#${id}` }
function getRelationTagType(type) {
  const map = { '股权穿透': 'danger', '担保链': 'warning', '关联交易': 'info', '共同法人': '', '上下游': 'success' }
  return map[type] || ''
}

function showAdd() { dialogVisible.value = true }

async function submitForm() {
  if (!form.value.companyId || !form.value.relatedCompany || !form.value.relationType) {
    ElMessage.warning('请填写必填项'); return
  }
  try {
    await addRelation(form.value)
    ElMessage.success('添加成功')
    dialogVisible.value = false
    form.value = { companyId: null, relatedCompany: '', relatedPerson: '', relationType: '', relationDetail: '', dataSource: '手工录入' }
    refreshGraph()
  } catch (e) { ElMessage.error('添加失败') }
}

async function deleteRelation(id) {
  try {
    await ElMessageBox.confirm('确定删除此关联关系？', '提示', { type: 'warning' })
    await apiDelete(id)
    ElMessage.success('删除成功')
    refreshGraph()
  } catch (e) { /* cancelled */ }
}

function renderGraph(data) {
  if (!graphRef.value || !data.length) return
  const chart = echarts.init(graphRef.value)
  const nodes = []
  const links = []
  const nodeSet = new Set()
  const colorMap = { '股权穿透': '#F56C6C', '担保链': '#E6A23C', '关联交易': '#409EFF', '共同法人': '#67C23A', '上下游': '#909399' }

  data.forEach(rel => {
    const srcName = getCompanyName(rel.companyId)
    const tgtName = rel.relatedCompany
    if (!nodeSet.has(srcName)) { nodeSet.add(srcName); nodes.push({ name: srcName, symbolSize: 40, itemStyle: { color: '#409EFF' } }) }
    if (!nodeSet.has(tgtName)) { nodeSet.add(tgtName); nodes.push({ name: tgtName, symbolSize: 30, itemStyle: { color: '#67C23A' } }) }
    links.push({ source: srcName, target: tgtName, label: { show: true, formatter: rel.relationType, fontSize: 10 }, lineStyle: { color: colorMap[rel.relationType] || '#C0C4CC', width: 2, type: rel.relationType === '担保链' ? 'dashed' : 'solid' } })
  })

  chart.setOption({
    tooltip: { trigger: 'item' },
    series: [{
      type: 'graph',
      layout: 'force',
      data: nodes,
      links: links,
      roam: true,
      draggable: true,
      force: { repulsion: 200, gravity: 0.1, edgeLength: 150 },
      label: { show: true, fontSize: 12 },
      lineStyle: { opacity: 0.8 },
      emphasis: { focus: 'adjacency', lineStyle: { width: 4 } }
    }]
  })
  window.addEventListener('resize', () => chart.resize())
}

async function refreshGraph() {
  try {
    const res = await getRelationList()
    graphData.value = res.rows || res.data?.rows || []
    renderGraph(graphData.value)
  } catch (e) { console.error(e) }
}

onMounted(async () => {
  try {
    const res = await getCompanyList({ pageSize: 100 })
    const list = res.rows || res.data?.rows || []
    companyOptions.value = list
    list.forEach(c => { companyMap.value[c.id] = c.companyName })
  } catch (e) { console.error(e) }
  refreshGraph()
})
</script>

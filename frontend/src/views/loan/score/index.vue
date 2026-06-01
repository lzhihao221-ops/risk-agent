<template>
  <div class="app-container">
    <el-card>
      <template #header><span>客户信用评分</span></template>
      <el-table :data="scorecards" v-loading="loading" stripe>
        <el-table-column label="企业名称" prop="companyName" show-overflow-tooltip />
        <el-table-column label="评分日期" prop="scoreDate" width="100" />
        <el-table-column label="财务状况" prop="scoreFinancial" width="80" align="center" />
        <el-table-column label="经营能力" prop="scoreOperation" width="80" align="center" />
        <el-table-column label="信用记录" prop="scoreCredit" width="80" align="center" />
        <el-table-column label="担保抵押" prop="scoreCollateral" width="80" align="center" />
        <el-table-column label="行业风险" prop="scoreIndustry" width="80" align="center" />
        <el-table-column label="综合得分" width="90" align="center">
          <template #default="{row}">
            <span style="font-weight:bold;font-size:16px" :style="{color: row.totalScore>=70?'#67C23A':row.totalScore>=50?'#E6A23C':'#F56C6C'}">{{ row.totalScore }}</span>
          </template>
        </el-table-column>
        <el-table-column label="信用等级" width="80" align="center">
          <template #default="{row}">
            <el-tag :type="gradeTag(row.grade)" size="small">{{ row.grade }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="建议额度(万)" prop="suggestAmount" width="110" align="right" />
        <el-table-column label="建议利率(%)" prop="suggestRate" width="100" align="center" />
      </el-table>
    </el-card>

    <el-card style="margin-top:16px">
      <template #header><span>抵押物管理</span></template>
      <el-table :data="collaterals" v-loading="loading2" stripe>
        <el-table-column label="抵押物名称" prop="collateralName" show-overflow-tooltip />
        <el-table-column label="类型" prop="collateralType" width="80" />
        <el-table-column label="权证编号" prop="certNo" width="140" />
        <el-table-column label="所在地" prop="location" width="120" show-overflow-tooltip />
        <el-table-column label="评估价值(万)" prop="evalValue" width="110" align="right" />
        <el-table-column label="抵押价值(万)" prop="pledgeValue" width="110" align="right" />
        <el-table-column label="抵押率(%)" prop="pledgeRatio" width="90" align="center" />
        <el-table-column label="评估机构" prop="evalOrg" width="120" show-overflow-tooltip />
        <el-table-column label="状态" width="80">
          <template #default="{row}">
            <el-tag :type="row.status===1?'success':'info'" size="small">{{ row.status===1?'有效':'无效' }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { listScorecard, listCollateral } from '@/api/loan'

const scorecards = ref([])
const collaterals = ref([])
const loading = ref(false)
const loading2 = ref(false)

function gradeTag(g) {
  if (['AAA','AA','A'].includes(g)) return 'success'
  if (['BBB','BB','B'].includes(g)) return 'warning'
  return 'danger'
}

async function loadData() {
  loading.value = true
  try {
    const res = await listScorecard()
    scorecards.value = res.data || res || []
  } catch(e) { console.error(e) }
  loading.value = false

  loading2.value = true
  try {
    const res = await listCollateral()
    collaterals.value = res.data || res || []
  } catch(e) { console.error(e) }
  loading2.value = false
}

onMounted(loadData)
</script>

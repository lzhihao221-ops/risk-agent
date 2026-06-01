<template>
  <div class="app-container">
    <el-row :gutter="10" style="margin-bottom:15px">
      <el-col :span="1.5">
        <el-button type="primary" plain @click="handleAdd"><el-icon><Plus /></el-icon>新增规则</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="ruleList" border stripe>
      <el-table-column prop="ruleName" label="规则名称" min-width="180" />
      <el-table-column prop="ruleCode" label="规则编码" width="140" />
      <el-table-column prop="ruleType" label="类型" width="90" align="center">
        <template #default="{ row }">
          <el-tag size="small">{{ row.ruleType === 'EVENT' ? '事件触发' : '指标触发' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="eventType" label="监听事件" width="120">
        <template #default="{ row }">{{ eventTypeMap[row.eventType] || row.eventType || '-' }}</template>
      </el-table-column>
      <el-table-column prop="alertLevel" label="预警等级" width="90" align="center">
        <template #default="{ row }">
          <el-tag :type="row.alertLevel === 3 ? 'danger' : row.alertLevel === 2 ? 'warning' : 'info'" size="small">
            {{ row.alertLevel === 3 ? '高' : row.alertLevel === 2 ? '中' : '低' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="isActive" label="启用" width="70" align="center">
        <template #default="{ row }">
          <el-switch v-model="row.isActive" :active-value="1" :inactive-value="0" @change="toggleRule(row)" />
        </template>
      </el-table-column>
      <el-table-column prop="notifyType" label="通知方式" width="150" />
      <el-table-column prop="remark" label="说明" min-width="200" show-overflow-tooltip />
      <el-table-column label="操作" width="120" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" size="small" @click="handleUpdate(row)">编辑</el-button>
          <el-button link type="danger" size="small" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="550px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="规则名称"><el-input v-model="form.ruleName" /></el-form-item>
        <el-form-item label="规则编码"><el-input v-model="form.ruleCode" :disabled="!!form.id" /></el-form-item>
        <el-form-item label="规则类型">
          <el-select v-model="form.ruleType" style="width:100%">
            <el-option label="事件触发" value="EVENT" />
            <el-option label="指标触发" value="INDICATOR" />
          </el-select>
        </el-form-item>
        <el-form-item label="监听事件" v-if="form.ruleType === 'EVENT'">
          <el-select v-model="form.eventType" style="width:100%">
            <el-option label="被执行" value="EXECUTION" />
            <el-option label="失信被执行" value="DISHONEST" />
            <el-option label="经营异常" value="ABNORMAL" />
            <el-option label="涉诉" value="LAWSUIT" />
            <el-option label="法人变更" value="CHANGE_LEGAL" />
            <el-option label="股权冻结" value="FREEZE" />
          </el-select>
        </el-form-item>
        <el-form-item label="预警等级">
          <el-radio-group v-model="form.alertLevel">
            <el-radio :value="3">高</el-radio>
            <el-radio :value="2">中</el-radio>
            <el-radio :value="1">低</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="通知方式">
          <el-checkbox-group v-model="notifyTypes">
            <el-checkbox label="站内消息" value="站内消息" />
            <el-checkbox label="SMS" value="SMS" />
            <el-checkbox label="企业微信" value="企业微信" />
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="说明"><el-input v-model="form.remark" type="textarea" :rows="2" /></el-form-item>
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
import request from '@/utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'

const eventTypeMap = { EXECUTION: '被执行', DISHONEST: '失信被执行', ABNORMAL: '经营异常', LAWSUIT: '涉诉', CHANGE_LEGAL: '法人变更', FREEZE: '股权冻结' }
const loading = ref(false)
const ruleList = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const notifyTypes = ref([])
const form = ref({ id: undefined, ruleName: '', ruleCode: '', ruleType: 'EVENT', eventType: 'EXECUTION', alertLevel: 2, isActive: 1, notifyType: '', remark: '' })

async function getList() {
  loading.value = true
  try { const { rows } = await request({ url: '/risk/rule/list', method: 'get' }); ruleList.value = rows || [] } catch (e) { ruleList.value = [] }
  loading.value = false
}
function handleAdd() { form.value = { id: undefined, ruleName: '', ruleCode: '', ruleType: 'EVENT', eventType: 'EXECUTION', alertLevel: 2, isActive: 1, notifyType: '', remark: '' }; notifyTypes.value = ['站内消息']; dialogTitle.value = '新增规则'; dialogVisible.value = true }
function handleUpdate(row) { form.value = { ...row }; notifyTypes.value = (row.notifyType || '').split(','); dialogTitle.value = '编辑规则'; dialogVisible.value = true }
async function toggleRule(row) {
  await request({ url: '/risk/rule', method: 'put', data: { id: row.id, isActive: row.isActive } })
  ElMessage.success(row.isActive ? '已启用' : '已停用')
}
async function handleDelete(row) {
  await ElMessageBox.confirm('确认删除规则"' + row.ruleName + '"？', '提示')
  await request({ url: '/risk/rule/' + row.id, method: 'delete' })
  ElMessage.success('删除成功'); getList()
}
async function submitForm() {
  form.value.notifyType = notifyTypes.value.join(',')
  if (form.value.id) { await request({ url: '/risk/rule', method: 'put', data: form.value }); ElMessage.success('修改成功') }
  else { await request({ url: '/risk/rule', method: 'post', data: form.value }); ElMessage.success('新增成功') }
  dialogVisible.value = false; getList()
}
onMounted(() => getList())
</script>

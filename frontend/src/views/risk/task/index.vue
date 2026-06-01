<template>
  <div class="app-container">
    <el-form :model="queryParams" :inline="true">
      <el-form-item label="任务状态" prop="taskStatus">
        <el-select v-model="queryParams.taskStatus" placeholder="全部" clearable style="width:120px">
          <el-option label="待分配" :value="0" />
          <el-option label="进行中" :value="1" />
          <el-option label="已完成" :value="2" />
          <el-option label="已逾期" :value="3" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery"><el-icon><Search /></el-icon>搜索</el-button>
        <el-button @click="resetQuery"><el-icon><Refresh /></el-icon>重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" style="margin-bottom:15px">
      <el-col :span="1.5">
        <el-button type="primary" plain @click="handleAdd"><el-icon><Plus /></el-icon>新建任务</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="taskList" border stripe>
      <el-table-column prop="companyName" label="企业" width="180" show-overflow-tooltip />
      <el-table-column prop="taskType" label="任务类型" width="100" />
      <el-table-column prop="taskStatus" label="状态" width="90" align="center">
        <template #default="{ row }">
          <el-tag :type="['info','','success','danger'][row.taskStatus]">
            {{ ['待分配','进行中','已完成','已逾期'][row.taskStatus] }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="assigneeName" label="负责人" width="80" />
      <el-table-column prop="dueDate" label="截止日期" width="120" />
      <el-table-column prop="checkResult" label="排查结论" width="100" />
      <el-table-column prop="createTime" label="创建时间" width="160" />
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="{ row }">
          <el-button v-if="row.taskStatus < 2" link type="primary" size="small" @click="handleComplete(row)">完成</el-button>
          <el-button link type="primary" size="small" @click="handleUpdate(row)">编辑</el-button>
          <el-button link type="danger" size="small" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="企业ID"><el-input-number v-model="form.companyId" :min="1" style="width:100%" /></el-form-item>
        <el-form-item label="任务类型">
          <el-select v-model="form.taskType" style="width:100%">
            <el-option label="常规排查" value="常规排查" />
            <el-option label="预警触发" value="预警触发" />
            <el-option label="定期复查" value="定期复查" />
          </el-select>
        </el-form-item>
        <el-form-item label="负责人"><el-input v-model="form.assigneeName" /></el-form-item>
        <el-form-item label="截止日期"><el-date-picker v-model="form.dueDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item>
        <el-form-item label="备注"><el-input v-model="form.remark" type="textarea" :rows="2" /></el-form-item>
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
import { getTaskList, addTask, updateTask as updateTaskApi, deleteTask } from '@/api/risk'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus } from '@element-plus/icons-vue'
import Pagination from '@/components/Pagination/index.vue'

const loading = ref(false)
const taskList = ref([])
const total = ref(0)
const queryParams = ref({ pageNum: 1, pageSize: 20, taskStatus: undefined })
const dialogVisible = ref(false)
const dialogTitle = ref('')
const form = ref({ id: undefined, companyId: 1, taskType: '常规排查', assigneeName: '', dueDate: '', remark: '' })

async function getList() {
  loading.value = true
  try { const { rows, total: t } = await getTaskList(queryParams.value); taskList.value = rows || []; total.value = t || 0 } catch (e) { taskList.value = []; total.value = 0 }
  loading.value = false
}
function handleQuery() { queryParams.value.pageNum = 1; getList() }
function resetQuery() { queryParams.value = { pageNum: 1, pageSize: 20, taskStatus: undefined }; getList() }
function handleAdd() { form.value = { id: undefined, companyId: 1, taskType: '常规排查', assigneeName: '', dueDate: '', remark: '' }; dialogTitle.value = '新建任务'; dialogVisible.value = true }
function handleUpdate(row) { form.value = { ...row }; dialogTitle.value = '编辑任务'; dialogVisible.value = true }
async function handleComplete(row) {
  await ElMessageBox.confirm('确认完成此排查任务？', '提示')
  await updateTaskApi({ ...row, taskStatus: 2, completeTime: new Date().toISOString() })
  ElMessage.success('任务已完成')
  getList()
}
async function handleDelete(row) {
  await ElMessageBox.confirm('确认删除此任务？', '提示')
  await deleteTask(row.id)
  ElMessage.success('删除成功')
  getList()
}
async function submitForm() {
  if (form.value.id) { await updateTaskApi(form.value); ElMessage.success('修改成功') }
  else { await addTask(form.value); ElMessage.success('创建成功') }
  dialogVisible.value = false
  getList()
}

onMounted(() => getList())
</script>

import request from '@/utils/request'

// === 监控企业 ===
export function getCompanyList(params) {
  return request({ url: '/risk/company/list', method: 'get', params })
}
export function getCompanyDetail(id) {
  return request({ url: `/risk/company/${id}`, method: 'get' })
}
export function addCompany(data) {
  return request({ url: '/risk/company', method: 'post', data })
}
export function updateCompany(data) {
  return request({ url: '/risk/company', method: 'put', data })
}
export function deleteCompany(id) {
  return request({ url: `/risk/company/${id}`, method: 'delete' })
}

// === 风险事件 ===
export function getEventList(params) {
  return request({ url: '/risk/event/list', method: 'get', params })
}
export function addEvent(data) {
  return request({ url: '/risk/event', method: 'post', data })
}
export function markEventRead(id) {
  return request({ url: `/risk/event/read/${id}`, method: 'put' })
}
export function markEventHandled(id) {
  return request({ url: `/risk/event/handle/${id}`, method: 'put' })
}

// === 预警 ===
export function getAlertList(params) {
  return request({ url: '/risk/alert/list', method: 'get', params })
}
export function handleAlert(id, data) {
  return request({ url: `/risk/alert/handle/${id}`, method: 'put', data })
}

// === 预警规则 ===
export function getRuleList(params) {
  return request({ url: '/risk/rule/list', method: 'get', params })
}
export function addRule(data) {
  return request({ url: '/risk/rule', method: 'post', data })
}
export function updateRule(data) {
  return request({ url: '/risk/rule', method: 'put', data })
}
export function deleteRule(id) {
  return request({ url: `/risk/rule/${id}`, method: 'delete' })
}

// === 排查任务 ===
export function getTaskList(params) {
  return request({ url: '/risk/task/list', method: 'get', params })
}
export function addTask(data) {
  return request({ url: '/risk/task', method: 'post', data })
}
export function updateTask(data) {
  return request({ url: '/risk/task', method: 'put', data })
}
export function deleteTask(id) {
  return request({ url: `/risk/task/${id}`, method: 'delete' })
}
export function completeTask(id) {
  return request({ url: `/risk/task/complete/${id}`, method: 'put' })
}

// === 关联关系 ===
export function getRelationList(params) {
  return request({ url: '/risk/relation/list', method: 'get', params })
}

// === 看板统计 ===
export function getSummary() {
  return request({ url: '/risk/dashboard/summary', method: 'get' })
}
export function getDashboardData() {
  return request({ url: '/risk/dashboard/data', method: 'get' })
}

// === 预警检查 ===
export function checkAlert(id) {
  return request({ url: `/risk/alert/check/${id}`, method: 'post' })
}

// === 消息 ===
export function getUnreadCount() {
  return request({ url: '/risk/message/unreadCount', method: 'get' })
}
export function getUnreadMessages() {
  return request({ url: '/risk/message/unread', method: 'get' })
}
export function getMessageList(params) {
  return request({ url: '/risk/message/list', method: 'get', params })
}
export function markMessageRead(id) {
  return request({ url: `/risk/message/read/${id}`, method: 'put' })
}
export function markAllRead() {
  return request({ url: '/risk/message/readAll', method: 'put' })
}
export function deleteMessage(id) {
  return request({ url: `/risk/message/${id}`, method: 'delete' })
}

// === 操作日志 ===
export function getLogs(params) {
  return request({ url: '/risk/log/list', method: 'get', params })
}

// === 数据源 ===
export function getDataSourceStatus() {
  return request({ url: '/risk/external/status', method: 'get' })
}

// === 关联关系管理 ===
export function addRelation(data) {
  return request({ url: '/risk/relation', method: 'post', data })
}
export function deleteRelation(id) {
  return request({ url: `/risk/relation/${id}`, method: 'delete' })
}

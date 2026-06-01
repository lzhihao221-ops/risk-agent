import request from '@/utils/request'

// 贷款申请列表
export function listApplication(params) {
  return request({ url: '/loan/application/list', method: 'get', params })
}

// 贷款申请详情
export function getApplication(id) {
  return request({ url: `/loan/application/${id}`, method: 'get' })
}

// 新增贷款申请
export function addApplication(data) {
  return request({ url: '/loan/application', method: 'post', data })
}

// 修改贷款申请
export function updateApplication(data) {
  return request({ url: '/loan/application', method: 'put', data })
}

// 删除贷款申请
export function deleteApplication(ids) {
  return request({ url: `/loan/application/${ids}`, method: 'delete' })
}

// 贷款申请状态统计
export function applicationStatusStats() {
  return request({ url: '/loan/application/statusStats', method: 'get' })
}

// 贷款申请类型统计
export function applicationTypeStats() {
  return request({ url: '/loan/application/typeStats', method: 'get' })
}

// 审批记录
export function getApprovals(appId) {
  return request({ url: `/loan/approval/${appId}`, method: 'get' })
}

// 新增审批
export function addApproval(data) {
  return request({ url: '/loan/approval', method: 'post', data })
}

// 贷款台账列表
export function listLedger(params) {
  return request({ url: '/loan/ledger/list', method: 'get', params })
}

// 贷款台账详情
export function getLedger(id) {
  return request({ url: `/loan/ledger/${id}`, method: 'get' })
}

// 新增贷款台账
export function addLedger(data) {
  return request({ url: '/loan/ledger', method: 'post', data })
}

// 修改贷款台账
export function updateLedger(data) {
  return request({ url: '/loan/ledger', method: 'put', data })
}

// 删除贷款台账
export function deleteLedger(ids) {
  return request({ url: `/loan/ledger/${ids}`, method: 'delete' })
}

// 贷款台账分类统计
export function ledgerCategoryStats() {
  return request({ url: '/loan/ledger/categoryStats', method: 'get' })
}

// 贷款台账逾期统计
export function ledgerOverdueStats() {
  return request({ url: '/loan/ledger/overdueStats', method: 'get' })
}

// 贷款台账汇总
export function ledgerSummary() {
  return request({ url: '/loan/ledger/summary', method: 'get' })
}

// 评分卡列表
export function listScorecard() {
  return request({ url: '/loan/scorecard/list', method: 'get' })
}

// 最新评分
export function getLatestScore(companyId) {
  return request({ url: `/loan/scorecard/${companyId}`, method: 'get' })
}

// 抵押物列表
export function listCollateral() {
  return request({ url: '/loan/collateral/list', method: 'get' })
}

// 企业抵押物
export function getCollateralByCompany(companyId) {
  return request({ url: `/loan/collateral/${companyId}`, method: 'get' })
}

// 还款计划
export function getRepayments(loanId) {
  return request({ url: `/loan/repayment/${loanId}`, method: 'get' })
}

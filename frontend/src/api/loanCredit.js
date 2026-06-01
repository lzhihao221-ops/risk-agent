import request from '@/utils/request'

// ==================== 征信查询 ====================

export function listCreditQuery(params) {
  return request({ url: '/loan/credit/query/list', method: 'get', params })
}

export function getCreditQuery(id) {
  return request({ url: `/loan/credit/query/${id}`, method: 'get' })
}

export function addCreditQuery(data) {
  return request({ url: '/loan/credit/query', method: 'post', data })
}

export function updateCreditQuery(data) {
  return request({ url: '/loan/credit/query', method: 'put', data })
}

export function deleteCreditQuery(ids) {
  return request({ url: `/loan/credit/query/${ids}`, method: 'delete' })
}

// ==================== 授信审批 ====================

export function listCreditApproval(params) {
  return request({ url: '/loan/credit/approval/list', method: 'get', params })
}

export function getCreditApproval(id) {
  return request({ url: `/loan/credit/approval/${id}`, method: 'get' })
}

export function addCreditApproval(data) {
  return request({ url: '/loan/credit/approval', method: 'post', data })
}

export function updateCreditApproval(data) {
  return request({ url: '/loan/credit/approval', method: 'put', data })
}

export function deleteCreditApproval(ids) {
  return request({ url: `/loan/credit/approval/${ids}`, method: 'delete' })
}

// ==================== 授信额度 ====================

export function listCreditLimit(params) {
  return request({ url: '/loan/credit/limit/list', method: 'get', params })
}

export function getCreditLimit(id) {
  return request({ url: `/loan/credit/limit/${id}`, method: 'get' })
}

export function getCreditLimitByCompany(companyId) {
  return request({ url: `/loan/credit/limit/company/${companyId}`, method: 'get' })
}

export function addCreditLimit(data) {
  return request({ url: '/loan/credit/limit', method: 'post', data })
}

export function updateCreditLimit(data) {
  return request({ url: '/loan/credit/limit', method: 'put', data })
}

export function deleteCreditLimit(ids) {
  return request({ url: `/loan/credit/limit/${ids}`, method: 'delete' })
}

export function refreshCreditLimit(id) {
  return request({ url: `/loan/credit/limit/refresh/${id}`, method: 'put' })
}

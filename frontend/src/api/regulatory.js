import request from '@/utils/request'

// ==================== 监管概况 ====================

export function getRegulatoryOverview() {
  return request({ url: '/regulatory/overview', method: 'get' })
}

// ==================== 1104报表 ====================

export function listReport1104(params) {
  return request({ url: '/regulatory/report1104/list', method: 'get', params })
}

export function getReport1104(id) {
  return request({ url: `/regulatory/report1104/${id}`, method: 'get' })
}

export function addReport1104(data) {
  return request({ url: '/regulatory/report1104', method: 'post', data })
}

export function updateReport1104(data) {
  return request({ url: '/regulatory/report1104', method: 'put', data })
}

export function deleteReport1104(ids) {
  return request({ url: `/regulatory/report1104/${ids}`, method: 'delete' })
}

// ==================== EAST报送 ====================

export function listEast(params) {
  return request({ url: '/regulatory/east/list', method: 'get', params })
}

export function getEast(id) {
  return request({ url: `/regulatory/east/${id}`, method: 'get' })
}

export function addEast(data) {
  return request({ url: '/regulatory/east', method: 'post', data })
}

export function updateEast(data) {
  return request({ url: '/regulatory/east', method: 'put', data })
}

export function deleteEast(ids) {
  return request({ url: `/regulatory/east/${ids}`, method: 'delete' })
}

// ==================== 征信报送 ====================

export function listCreditReport(params) {
  return request({ url: '/regulatory/credit/list', method: 'get', params })
}

export function getCreditReport(id) {
  return request({ url: `/regulatory/credit/${id}`, method: 'get' })
}

export function addCreditReport(data) {
  return request({ url: '/regulatory/credit', method: 'post', data })
}

export function updateCreditReport(data) {
  return request({ url: '/regulatory/credit', method: 'put', data })
}

export function deleteCreditReport(ids) {
  return request({ url: `/regulatory/credit/${ids}`, method: 'delete' })
}

// ==================== 监管指标 ====================

export function listIndicator(params) {
  return request({ url: '/regulatory/indicator/list', method: 'get', params })
}

export function getIndicator(id) {
  return request({ url: `/regulatory/indicator/${id}`, method: 'get' })
}

export function addIndicator(data) {
  return request({ url: '/regulatory/indicator', method: 'post', data })
}

export function updateIndicator(data) {
  return request({ url: '/regulatory/indicator', method: 'put', data })
}

export function deleteIndicator(ids) {
  return request({ url: `/regulatory/indicator/${ids}`, method: 'delete' })
}

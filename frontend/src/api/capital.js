import request from '@/utils/request'

// ==================== 资本概况 ====================

export function getCapitalOverview() {
  return request({ url: '/capital/overview', method: 'get' })
}

// ==================== RWA计算 ====================

export function listRwa(params) {
  return request({ url: '/capital/rwa/list', method: 'get', params })
}

export function getRwa(id) {
  return request({ url: `/capital/rwa/${id}`, method: 'get' })
}

export function addRwa(data) {
  return request({ url: '/capital/rwa', method: 'post', data })
}

export function updateRwa(data) {
  return request({ url: '/capital/rwa', method: 'put', data })
}

export function deleteRwa(ids) {
  return request({ url: `/capital/rwa/${ids}`, method: 'delete' })
}

// ==================== ECL预期损失 ====================

export function listEcl(params) {
  return request({ url: '/capital/ecl/list', method: 'get', params })
}

export function getEcl(id) {
  return request({ url: `/capital/ecl/${id}`, method: 'get' })
}

export function addEcl(data) {
  return request({ url: '/capital/ecl', method: 'post', data })
}

export function updateEcl(data) {
  return request({ url: '/capital/ecl', method: 'put', data })
}

export function deleteEcl(ids) {
  return request({ url: `/capital/ecl/${ids}`, method: 'delete' })
}

// ==================== 资本充足率 ====================

export function listAdequacy(params) {
  return request({ url: '/capital/adequacy/list', method: 'get', params })
}

export function getAdequacy(id) {
  return request({ url: `/capital/adequacy/${id}`, method: 'get' })
}

export function addAdequacy(data) {
  return request({ url: '/capital/adequacy', method: 'post', data })
}

export function updateAdequacy(data) {
  return request({ url: '/capital/adequacy', method: 'put', data })
}

export function deleteAdequacy(ids) {
  return request({ url: `/capital/adequacy/${ids}`, method: 'delete' })
}

// ==================== 拨备计提 ====================

export function listProvision(params) {
  return request({ url: '/capital/provision/list', method: 'get', params })
}

export function getProvision(id) {
  return request({ url: `/capital/provision/${id}`, method: 'get' })
}

export function addProvision(data) {
  return request({ url: '/capital/provision', method: 'post', data })
}

export function updateProvision(data) {
  return request({ url: '/capital/provision', method: 'put', data })
}

export function deleteProvision(ids) {
  return request({ url: `/capital/provision/${ids}`, method: 'delete' })
}

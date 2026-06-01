import request from '@/utils/request'

// ==================== 放款管理 ====================

export function listDisburse(params) {
  return request({ url: '/loan/disburse/list', method: 'get', params })
}

export function getDisburse(id) {
  return request({ url: `/loan/disburse/${id}`, method: 'get' })
}

export function addDisburse(data) {
  return request({ url: '/loan/disburse', method: 'post', data })
}

export function updateDisburse(data) {
  return request({ url: '/loan/disburse', method: 'put', data })
}

export function deleteDisburse(ids) {
  return request({ url: `/loan/disburse/${ids}`, method: 'delete' })
}

// 放款确认
export function confirmDisburse(id) {
  return request({ url: `/loan/disburse/confirm/${id}`, method: 'put' })
}

// 放款统计
export function disburseStats() {
  return request({ url: '/loan/disburse/stats', method: 'get' })
}

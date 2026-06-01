import request from '@/utils/request'

// OCR识别
export function ocrImage(data) {
  return request({ url: '/ocr/image', method: 'post', data, headers: { 'Content-Type': 'multipart/form-data' } })
}

export function ocrReport(data) {
  return request({ url: '/ocr/report', method: 'post', data, headers: { 'Content-Type': 'multipart/form-data' } })
}

// GIS地理信息
export function getGisBranches() {
  return request({ url: '/gis/branches', method: 'get' })
}

export function getGisCustomers() {
  return request({ url: '/gis/customers', method: 'get' })
}

export function getGisRiskHeatmap() {
  return request({ url: '/gis/risk-heatmap', method: 'get' })
}

export function gisCheckin(data) {
  return request({ url: '/gis/checkin', method: 'post', data })
}

export function getGisCheckinRecords(params) {
  return request({ url: '/gis/checkin/records', method: 'get', params })
}

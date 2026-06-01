import request from '@/utils/request'

// ==================== 催收管理 ====================

export function listCollection(query) {
  return request({
    url: '/loan/collection/list',
    method: 'get',
    params: query
  })
}

export function getCollection(id) {
  return request({
    url: '/loan/collection/' + id,
    method: 'get'
  })
}

export function addCollection(data) {
  return request({
    url: '/loan/collection',
    method: 'post',
    data: data
  })
}

export function updateCollection(data) {
  return request({
    url: '/loan/collection',
    method: 'put',
    data: data
  })
}

export function delCollection(ids) {
  return request({
    url: '/loan/collection/' + ids,
    method: 'delete'
  })
}

export function approveCollection(id, collectionType, nextAction) {
  return request({
    url: '/loan/collection/approve/' + id,
    method: 'get',
    params: { collectionType, nextAction }
  })
}

// ==================== 催收记录 ====================

export function listCollectionLog(collectionId) {
  return request({
    url: '/loan/collection/log/list/' + collectionId,
    method: 'get'
  })
}

export function addCollectionLog(data) {
  return request({
    url: '/loan/collection/log',
    method: 'post',
    data: data
  })
}

export function delCollectionLog(ids) {
  return request({
    url: '/loan/collection/log/' + ids,
    method: 'delete'
  })
}

// ==================== 资产保全 ====================

export function listAsset(query) {
  return request({
    url: '/loan/asset/list',
    method: 'get',
    params: query
  })
}

export function getAsset(id) {
  return request({
    url: '/loan/asset/' + id,
    method: 'get'
  })
}

export function addAsset(data) {
  return request({
    url: '/loan/asset',
    method: 'post',
    data: data
  })
}

export function updateAsset(data) {
  return request({
    url: '/loan/asset',
    method: 'put',
    data: data
  })
}

export function delAsset(ids) {
  return request({
    url: '/loan/asset/' + ids,
    method: 'delete'
  })
}

export function updateRecovery(id, recoveryAmount) {
  return request({
    url: '/loan/asset/recovery/' + id,
    method: 'get',
    params: { recoveryAmount }
  })
}

// ==================== 核销管理 ====================

export function listWriteoff(query) {
  return request({
    url: '/loan/writeoff/list',
    method: 'get',
    params: query
  })
}

export function getWriteoff(id) {
  return request({
    url: '/loan/writeoff/' + id,
    method: 'get'
  })
}

export function addWriteoff(data) {
  return request({
    url: '/loan/writeoff',
    method: 'post',
    data: data
  })
}

export function updateWriteoff(data) {
  return request({
    url: '/loan/writeoff',
    method: 'put',
    data: data
  })
}

export function delWriteoff(ids) {
  return request({
    url: '/loan/writeoff/' + ids,
    method: 'delete'
  })
}

export function approveWriteoff(id, approveUser, approveOpinion) {
  return request({
    url: '/loan/writeoff/approve/' + id,
    method: 'get',
    params: { approveUser, approveOpinion }
  })
}

export function rejectWriteoff(id, approveUser, approveOpinion) {
  return request({
    url: '/loan/writeoff/reject/' + id,
    method: 'get',
    params: { approveUser, approveOpinion }
  })
}

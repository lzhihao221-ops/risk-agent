import request from '@/utils/request'

// ========== PD违约概率模型 ==========
export function getPdModelList(params) {
  return request({ url: '/pd/model/list', method: 'get', params })
}
export function getPdModel(modelId) {
  return request({ url: `/pd/model/${modelId}`, method: 'get' })
}
export function addPdModel(data) {
  return request({ url: '/pd/model', method: 'post', data })
}
export function updatePdModel(data) {
  return request({ url: '/pd/model', method: 'put', data })
}
export function deletePdModel(modelId) {
  return request({ url: `/pd/model/${modelId}`, method: 'delete' })
}
export function activatePdModel(modelId) {
  return request({ url: `/pd/model/${modelId}/activate`, method: 'post' })
}
export function pdScore(data) {
  return request({ url: '/pd/model/score', method: 'post', data })
}
export function getPdModelVariables(modelId) {
  return request({ url: `/pd/model/${modelId}/variables`, method: 'get' })
}
export function getPdScoreRecords(params) {
  return request({ url: '/pd/model/score/records', method: 'get', params })
}

// ========== 五级分类 ==========
export function getClassificationRuleList(params) {
  return request({ url: '/classification/rule/list', method: 'get', params })
}
export function addClassificationRule(data) {
  return request({ url: '/classification/rule', method: 'post', data })
}
export function updateClassificationRule(data) {
  return request({ url: '/classification/rule', method: 'put', data })
}
export function deleteClassificationRule(ruleId) {
  return request({ url: `/classification/rule/${ruleId}`, method: 'delete' })
}
export function classifyLoan(data) {
  return request({ url: '/classification/classify', method: 'post', data })
}
export function getClassificationResults(params) {
  return request({ url: '/classification/result/list', method: 'get', params })
}
export function getClassificationMigrations(params) {
  return request({ url: '/classification/migration/list', method: 'get', params })
}

// ========== 压力测试 ==========
export function getStressScenarioList(params) {
  return request({ url: '/stress/scenario/list', method: 'get', params })
}
export function addStressScenario(data) {
  return request({ url: '/stress/scenario', method: 'post', data })
}
export function updateStressScenario(data) {
  return request({ url: '/stress/scenario', method: 'put', data })
}
export function deleteStressScenario(scenarioId) {
  return request({ url: `/stress/scenario/${scenarioId}`, method: 'delete' })
}
export function runStressTest(data) {
  return request({ url: '/stress/run', method: 'post', data })
}
export function runAllStressTests(data) {
  return request({ url: '/stress/runAll', method: 'post', data })
}
export function getStressResults(params) {
  return request({ url: '/stress/result/list', method: 'get', params })
}

// ========== VaR风险价值 ==========
export function getVarRecords(params) {
  return request({ url: '/var/record/list', method: 'get', params })
}
export function calculateHistoricalVar(data) {
  return request({ url: '/var/historical', method: 'post', data })
}
export function calculateMonteCarloVar(data) {
  return request({ url: '/var/monteCarlo', method: 'post', data })
}
export function calculateParametricVar(data) {
  return request({ url: '/var/parametric', method: 'post', data })
}

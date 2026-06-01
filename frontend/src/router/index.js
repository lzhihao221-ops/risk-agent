import { createWebHistory, createRouter } from 'vue-router'
import Layout from '@/layout'

// 公共路由
export const constantRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: () => import('@/views/redirect')
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login'),
    hidden: true
  },
  {
    path: '/register',
    component: () => import('@/views/register'),
    hidden: true
  },
  {
    path: "/:pathMatch(.*)*",
    component: () => import('@/views/error/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/error/401'),
    hidden: true
  },
  {
    path: '',
    component: Layout,
    redirect: '/index',
    children: [
      {
        path: '/index',
        component: () => import('@/views/index'),
        name: 'Index',
        meta: { title: '首页', icon: 'dashboard', affix: true }
      }
    ]
  },
  {
    path: '/user',
    component: Layout,
    hidden: true,
    redirect: 'noredirect',
    children: [
      {
        path: 'profile',
        component: () => import('@/views/system/user/profile/index'),
        name: 'Profile',
        meta: { title: '个人中心' }
      }
    ]
  }
]

// 动态路由
export const dynamicRoutes = [
  {
    path: '/risk',
    component: Layout,
    redirect: '/risk/dashboard',
    name: 'Risk',
    meta: { title: '贷后风控', icon: 'warning' },
    children: [
      {
        path: 'dashboard',
        component: () => import('@/views/risk/dashboard/index.vue'),
        name: 'RiskDashboard',
        meta: { title: '风控看板', icon: 'dashboard' }
      },
      {
        path: 'company',
        component: () => import('@/views/risk/company/index.vue'),
        name: 'RiskCompany',
        meta: { title: '监控企业', icon: 'peoples' }
      },
      {
        path: 'company/detail/:id',
        component: () => import('@/views/risk/company/detail.vue'),
        name: 'CompanyDetail',
        meta: { title: '企业详情', activeMenu: '/risk/company' },
        hidden: true
      },
      {
        path: 'alert',
        component: () => import('@/views/risk/alert/index.vue'),
        name: 'RiskAlert',
        meta: { title: '预警中心', icon: 'warning' }
      },
      {
        path: 'event',
        component: () => import('@/views/risk/event/index.vue'),
        name: 'RiskEvent',
        meta: { title: '风险事件', icon: 'edit' }
      },
      {
        path: 'task',
        component: () => import('@/views/risk/task/index.vue'),
        name: 'RiskTask',
        meta: { title: '排查任务', icon: 'form' }
      },
      {
        path: 'relation',
        component: () => import('@/views/risk/relation/index.vue'),
        name: 'RiskRelation',
        meta: { title: '关联图谱', icon: 'tree' }
      },
      {
        path: 'rule',
        component: () => import('@/views/risk/rule/index.vue'),
        name: 'RiskRule',
        meta: { title: '预警规则', icon: 'lock' }
      },
      {
        path: 'report',
        component: () => import('@/views/risk/report/index.vue'),
        name: 'RiskReport',
        meta: { title: '风险报告', icon: 'documentation' }
      },
      {
        path: 'monitor',
        component: () => import('@/views/risk/monitor/index.vue'),
        name: 'RiskMonitor',
        meta: { title: '定时监测', icon: 'job' }
      },
      {
        path: 'stat',
        component: () => import('@/views/risk/stat/index.vue'),
        name: 'RiskStat',
        meta: { title: '数据统计', icon: 'chart' }
      },
      {
        path: 'batch',
        component: () => import('@/views/risk/batch/index.vue'),
        name: 'RiskBatch',
        meta: { title: '批量导入', icon: 'upload' }
      },
      {
        path: 'config',
        component: () => import('@/views/risk/config/index.vue'),
        name: 'RiskConfig',
        meta: { title: '系统配置', icon: 'edit' }
      },
      {
        path: 'datasource',
        component: () => import('@/views/risk/datasource/index.vue'),
        name: 'RiskDatasource',
        meta: { title: '数据源管理', icon: 'server' }
      },
      {
        path: 'message',
        component: () => import('@/views/risk/message/index.vue'),
        name: 'RiskMessage',
        meta: { title: '消息中心', icon: 'message' }
      },
      {
        path: 'log',
        component: () => import('@/views/risk/log/index.vue'),
        name: 'RiskLog',
        meta: { title: '操作日志', icon: 'log' }
      }
    ]
  },
  {
    path: '/loan',
    component: Layout,
    redirect: '/loan/application',
    name: 'Loan',
    meta: { title: '信贷管理', icon: 'money' },
    children: [
      {
        path: 'application',
        component: () => import('@/views/loan/application/index.vue'),
        name: 'LoanApplication',
        meta: { title: '贷款申请', icon: 'edit' }
      },
      {
        path: 'ledger',
        component: () => import('@/views/loan/ledger/index.vue'),
        name: 'LoanLedger',
        meta: { title: '贷款台账', icon: 'list' }
      },
      {
        path: 'score',
        component: () => import('@/views/loan/score/index.vue'),
        name: 'LoanScore',
        meta: { title: '评分抵押', icon: 'star' }
      },
      {
        path: 'credit',
        component: () => import('@/views/loan/credit/query/index.vue'),
        name: 'LoanCredit',
        meta: { title: '征信查询', icon: 'search' }
      },
      {
        path: 'credit/approval',
        component: () => import('@/views/loan/credit/approval/index.vue'),
        name: 'LoanCreditApproval',
        meta: { title: '授信审批', icon: 'form' },
        hidden: true
      },
      {
        path: 'credit/limit',
        component: () => import('@/views/loan/credit/limit/index.vue'),
        name: 'LoanCreditLimit',
        meta: { title: '授信额度', icon: 'money' },
        hidden: true
      },
      {
        path: 'disburse',
        component: () => import('@/views/loan/disburse/index.vue'),
        name: 'LoanDisburse',
        meta: { title: '放款管理', icon: 'money' }
      },
      {
        path: 'interest',
        component: () => import('@/views/loan/interest/index.vue'),
        name: 'LoanInterest',
        meta: { title: '利息计提', icon: 'chart' }
      },
      {
        path: 'collection',
        component: () => import('@/views/loan/collection/index.vue'),
        name: 'LoanCollection',
        meta: { title: '催收管理', icon: 'warning' }
      },
      {
        path: 'asset',
        component: () => import('@/views/loan/asset/index.vue'),
        name: 'LoanAsset',
        meta: { title: '资产保全', icon: 'lock' }
      },
      {
        path: 'writeoff',
        component: () => import('@/views/loan/writeoff/index.vue'),
        name: 'LoanWriteoff',
        meta: { title: '核销管理', icon: 'edit' }
      }
    ]
  },
  {
    path: '/capital',
    component: Layout,
    redirect: '/capital/overview',
    name: 'Capital',
    meta: { title: '资本计量', icon: 'chart' },
    children: [
      {
        path: 'overview',
        component: () => import('@/views/capital/overview/index.vue'),
        name: 'CapitalOverview',
        meta: { title: '资本概况', icon: 'dashboard' }
      },
      {
        path: 'rwa',
        component: () => import('@/views/capital/rwa/index.vue'),
        name: 'CapitalRwa',
        meta: { title: 'RWA计算', icon: 'chart' }
      },
      {
        path: 'ecl',
        component: () => import('@/views/capital/ecl/index.vue'),
        name: 'CapitalEcl',
        meta: { title: 'ECL预期损失', icon: 'warning' }
      },
      {
        path: 'adequacy',
        component: () => import('@/views/capital/adequacy/index.vue'),
        name: 'CapitalAdequacy',
        meta: { title: '资本充足率', icon: 'form' }
      },
      {
        path: 'provision',
        component: () => import('@/views/capital/provision/index.vue'),
        name: 'CapitalProvision',
        meta: { title: '拨备计提', icon: 'money' }
      }
    ]
  },
  {
    path: '/regulatory',
    component: Layout,
    redirect: '/regulatory/overview',
    name: 'Regulatory',
    meta: { title: '监管报表', icon: 'documentation' },
    children: [
      {
        path: 'overview',
        component: () => import('@/views/regulatory/overview/index.vue'),
        name: 'RegulatoryOverview',
        meta: { title: '监管概况', icon: 'dashboard' }
      },
      {
        path: 'report1104',
        component: () => import('@/views/regulatory/report1104/index.vue'),
        name: 'Report1104',
        meta: { title: '1104报表', icon: 'documentation' }
      },
      {
        path: 'east',
        component: () => import('@/views/regulatory/east/index.vue'),
        name: 'EastReport',
        meta: { title: 'EAST报送', icon: 'upload' }
      },
      {
        path: 'credit',
        component: () => import('@/views/regulatory/credit/index.vue'),
        name: 'CreditReport',
        meta: { title: '征信报送', icon: 'form' }
      },
      {
        path: 'indicator',
        component: () => import('@/views/regulatory/indicator/index.vue'),
        name: 'RegIndicator',
        meta: { title: '监管指标', icon: 'chart' }
      }
    ]
  },
  {
    path: '/model',
    component: Layout,
    redirect: '/model/pd',
    name: 'Model',
    meta: { title: '风险计量', icon: 'chart' },
    children: [
      {
        path: 'pd',
        component: () => import('@/views/model/pd/index.vue'),
        name: 'PdModel',
        meta: { title: 'PD违约概率', icon: 'chart' }
      },
      {
        path: 'classification',
        component: () => import('@/views/model/classification/index.vue'),
        name: 'Classification',
        meta: { title: '五级分类', icon: 'form' }
      },
      {
        path: 'stress',
        component: () => import('@/views/model/stress/index.vue'),
        name: 'StressTest',
        meta: { title: '压力测试', icon: 'warning' }
      },
      {
        path: 'var',
        component: () => import('@/views/model/var/index.vue'),
        name: 'VaR',
        meta: { title: 'VaR风险价值', icon: 'money' }
      }
    ]
  },
  {
    path: '/ai',
    component: Layout,
    redirect: '/ai/assistant',
    name: 'AI',
    meta: { title: '智能助手', icon: 'cpu' },
    children: [
      {
        path: 'assistant',
        component: () => import('@/views/ai/assistant/index.vue'),
        name: 'AiAssistant',
        meta: { title: 'AI风控助手', icon: 'chat' }
      },
      {
        path: 'ocr',
        component: () => import('@/views/ai/ocr/index.vue'),
        name: 'Ocr',
        meta: { title: 'OCR识别', icon: 'upload' }
      },
      {
        path: 'collateral',
        component: () => import('@/views/ai/collateral/index.vue'),
        name: 'CollateralOcr',
        meta: { title: '押品识别', icon: 'camera' }
      }
    ]
  },
  {
    path: '/gis',
    component: Layout,
    redirect: '/gis/map',
    name: 'GIS',
    meta: { title: 'GIS地图', icon: 'location' },
    children: [
      {
        path: 'map',
        component: () => import('@/views/gis/map/index.vue'),
        name: 'GisMap',
        meta: { title: 'GIS地图', icon: 'location' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes: constantRoutes.concat(dynamicRoutes),
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    } else {
      return { top: 0 }
    }
  }
})

export default router

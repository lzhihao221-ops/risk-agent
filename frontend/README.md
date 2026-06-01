# RiskAgent - 银行风控智能管理系统

> 基于 Spring Boot 3.x + Vue 3 的银行全流程风控管理平台

## 系统简介

RiskAgent 是一套面向银行的智能风控管理系统，覆盖贷前审批、贷中监控、贷后管理全生命周期。系统集成了资本计量、监管报送、风险模型、AI辅助决策等核心模块，帮助银行实现数字化风控转型。

## 核心功能

### 🏦 贷款管理
- 贷款申请与审批流程
- 授信额度管理
- 放款与还款管理
- 利率调整与计息
- 催收管理与资产保全
- 核销处理

### 📊 资本计量
- 资本概况总览
- RWA（风险加权资产）计算
- ECL（预期信用损失）计量
- 资本充足率监控
- 拨备计提管理

### ⚠️ 风险监控
- 实时风险预警仪表盘
- 企业风险画像
- 关联图谱分析
- 风险事件管理
- 预警规则配置

### 🤖 风险模型
- PD（违约概率）评分模型
- 五级分类迁移矩阵
- 压力测试场景模拟
- VaR（风险价值）计算

### 📋 监管报送
- 1104报表自动生成
- EAST数据报送
- 征信报告管理
- 监管指标监控

### 🧠 AI 能力
- 智能风控助手（对话式AI）
- OCR文档识别
- 抵押物智能评估

### 🗺️ GIS 可视化
- 地理信息地图展示
- 区域风险分布

## 技术栈

| 层级 | 技术 | 说明 |
|------|------|------|
| 前端 | Vue 3 + Element Plus + Vite | 响应式SPA，组件化开发 |
| 后端 | Spring Boot 3.x + MyBatis | 微服务架构，RESTful API |
| 数据库 | MySQL 8.0 + Redis | 业务数据 + 缓存 |
| 安全 | Spring Security + JWT | RBAC权限控制 |
| 可视化 | ECharts | 图表与图谱 |

## 项目结构

```
risk-monitor/               # 前端项目
├── src/
│   ├── views/
│   │   ├── loan/           # 贷款管理
│   │   ├── capital/        # 资本计量
│   │   ├── risk/           # 风险监控
│   │   ├── model/          # 风险模型
│   │   ├── regulatory/     # 监管报送
│   │   ├── ai/             # AI功能
│   │   └── gis/            # GIS地图
│   ├── api/                # 接口定义
│   ├── components/         # 公共组件
│   └── store/              # 状态管理
├── docs/                   # 项目文档
└── package.json

risk-monitor-backend/       # 后端项目
├── risk-admin/             # 启动模块
├── risk-common/            # 公共模块
├── risk-framework/         # 框架模块
├── risk-system/            # 系统模块
└── risk-business/          # 风控业务模块
    ├── controller/         # 接口层
    ├── service/            # 业务层
    ├── mapper/             # 数据访问层
    └── domain/             # 实体类
```

## 快速开始

### 环境要求

- JDK 17+
- Maven 3.8+
- MySQL 8.0+
- Redis 6.0+
- Node.js 18+

### 后端启动

```bash
cd risk-monitor-backend
mvn clean package -DskipTests
java -jar risk-admin/target/risk-admin.jar
```

### 前端启动

```bash
cd risk-monitor
npm install
npm run dev
```

访问 http://localhost:80，默认账号 admin / admin123

## 文档

- [启动指南](docs/启动指南.md)
- [技术方案](docs/技术方案.md)
- [产品原型](docs/产品原型.md)

## 许可证

[MIT License](LICENSE) - 详见 LICENSE 文件

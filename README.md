# RiskAgent 风控管理系统

银行风险监控与管理系统，覆盖风险预警、押品估值、审批流程、数据字典等核心模块。

## 项目结构

```
risk-agent/
├── frontend/   # Vue 3 + Vite 前端
└── backend/    # Spring Boot 后端
```

## 快速启动

### 前端

```bash
cd frontend
npm install
npm run dev
# 访问 http://localhost:80
```

### 后端

```bash
cd backend

# 配置数据库连接
vim src/main/resources/application.yml

# 设置环境变量（可选，生产环境必须）
export JWT_SECRET="your-secret-key"
export DRUID_USERNAME="admin"
export DRUID_PASSWORD="your-password"

mvn clean package -DskipTests
java -jar target/risk-agent.jar
```

## 技术栈

| 层      | 技术栈                                |
| ------- | ------------------------------------- |
| 前端     | Vue 3 + Vite + Element Plus + ECharts |
| 后端     | Spring Boot 2.x + MyBatis + Druid     |
| 数据库   | MySQL 5.7+                            |
| 认证     | JWT                                   |
| 部署     | Nginx + Java                          |

## License

MIT

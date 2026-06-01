# RiskAgent - 银行风控智能管理系统（后端）

基于 Spring Boot 4 + MyBatis 的银行风险管理系统后端服务。

## 功能模块

- 🏦 **信贷管理** — 贷款申请、审批、发放、催收全流程
- 🏠 **押品估值** — GIS 地图 + OCR 识别 + AI 估值模型
- 📊 **风险监控** — 实时风险指标看板、预警阈值配置
- 🤖 **AI 风控助手** — 集成大模型的智能风险分析
- 🗺️ **GIS 地图** — 基于高德/OpenStreetMap 的空间数据可视化
- 👥 **系统管理** — 用户、角色、菜单、字典、日志

## 技术栈

- **框架**: Spring Boot 4.x + Spring Security + MyBatis
- **数据库**: MySQL 8.x + Druid 连接池
- **缓存**: Redis
- **接口文档**: Swagger/Knife4j
- **工作流**: Activiti (可选)

## 快速开始

### 环境要求

- JDK 17+
- MySQL 8.x
- Redis 6.x+
- Maven 3.8+

### 数据库初始化

```bash
# 创建数据库
mysql -u root -p -e "CREATE DATABASE ry-vue DEFAULT CHARACTER SET utf8mb4;"

# 导入表结构和数据
mysql -u root -p ry-vue < sql/ry_20250526.sql
```

### 配置

```bash
# 复制并修改配置文件
cp ruoyi-admin/src/main/resources/application.yml.example ruoyi-admin/src/main/resources/application.yml

# 设置环境变量（生产环境必须设置）
export JWT_SECRET="your-secret-key-here"
export DB_USERNAME="your-db-user"
export DB_PASSWORD="your-db-password"
```

### 启动

```bash
# 编译
mvn clean package -DskipTests

# 运行
java -jar ruoyi-admin/target/ruoyi-admin.jar
```

服务默认运行在 `http://localhost:8080`

## API 文档

启动后访问: `http://localhost:8080/doc.html` (Knife4j)

## 项目结构

```
risk-monitor-backend/
├── ruoyi-admin/          # 启动模块、控制器、配置
├── ruoyi-common/         # 通用工具、注解、异常处理
├── ruoyi-framework/      # 安全框架、拦截器、AOP
├── ruoyi-system/         # 系统管理（用户、角色、菜单）
├── ruoyi-quartz/         # 定时任务
├── ruoyi-generator/      # 代码生成器
├── ruoyi-risk/           # 风控业务模块（信贷、押品、GIS）
└── sql/                  # 数据库脚本
```

## License

[MIT](LICENSE)

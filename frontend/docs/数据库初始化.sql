-- ========================================
-- 银行贷后风险监测工作台 - 数据库初始化脚本
-- RiskAgent 风控系统业务表，表前缀使用 risk_ 区分
-- ========================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS risk_monitor DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE risk_monitor;

-- ========================================
-- 1. 企业基本信息表
-- ========================================
DROP TABLE IF EXISTS risk_company;
CREATE TABLE risk_company (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '企业ID',
    company_name    VARCHAR(200) NOT NULL COMMENT '企业名称',
    credit_code     VARCHAR(50) COMMENT '统一社会信用代码',
    legal_person    VARCHAR(50) COMMENT '法定代表人',
    reg_capital     DECIMAL(15,2) COMMENT '注册资本（万元）',
    establish_date  DATE COMMENT '成立日期',
    industry        VARCHAR(100) COMMENT '所属行业',
    province        VARCHAR(50) COMMENT '省份',
    city            VARCHAR(50) COMMENT '城市',
    district        VARCHAR(50) COMMENT '区县',
    address         VARCHAR(500) COMMENT '注册地址',
    business_scope  TEXT COMMENT '经营范围',
    status          TINYINT DEFAULT 1 COMMENT '状态：1正常 2注销 3吊销',
    risk_level      TINYINT DEFAULT 0 COMMENT '风险等级：0未评估 1低 2中 3高 4极高',
    risk_score      INT DEFAULT 0 COMMENT '风险评分（0-100，越高越危险）',
    last_scan_time  DATETIME COMMENT '最近扫描时间',
    create_by       VARCHAR(64) COMMENT '创建者',
    create_time     DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by       VARCHAR(64) COMMENT '更新者',
    update_time     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark          VARCHAR(500) COMMENT '备注',
    INDEX idx_credit_code (credit_code),
    INDEX idx_company_name (company_name),
    INDEX idx_risk_level (risk_level),
    INDEX idx_risk_score (risk_score)
) ENGINE=InnoDB COMMENT='企业基本信息表';

-- ========================================
-- 2. 企业关联方表
-- ========================================
DROP TABLE IF EXISTS risk_company_relation;
CREATE TABLE risk_company_relation (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    company_id      BIGINT NOT NULL COMMENT '主体企业ID',
    related_company VARCHAR(200) COMMENT '关联企业名称',
    related_credit_code VARCHAR(50) COMMENT '关联企业信用代码',
    related_person  VARCHAR(50) COMMENT '关联自然人',
    relation_type   VARCHAR(50) COMMENT '关联类型：EQUITY-股权/GUARANTEE-担保/CONTROLLER-实控人/EXECUTIVE-高管/FAMILY-亲属',
    relation_detail VARCHAR(500) COMMENT '关联详情（如持股比例、担保金额）',
    data_source     VARCHAR(50) COMMENT '数据来源：GS-工商/JUDICIAL-司法/MANUAL-手动',
    create_time     DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_company_id (company_id),
    INDEX idx_relation_type (relation_type),
    INDEX idx_related_company (related_company)
) ENGINE=InnoDB COMMENT='企业关联方表';

-- ========================================
-- 3. 风险事件表
-- ========================================
DROP TABLE IF EXISTS risk_event;
CREATE TABLE risk_event (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '事件ID',
    company_id      BIGINT NOT NULL COMMENT '企业ID',
    event_type      VARCHAR(50) NOT NULL COMMENT '事件类型',
    event_title     VARCHAR(500) COMMENT '事件标题',
    event_content   TEXT COMMENT '事件内容',
    event_date      DATE COMMENT '事件日期',
    amount          DECIMAL(15,2) COMMENT '涉及金额（万元）',
    severity        TINYINT DEFAULT 2 COMMENT '严重程度：1低 2中 3高',
    is_read         TINYINT DEFAULT 0 COMMENT '是否已读：0未读 1已读',
    is_handled      TINYINT DEFAULT 0 COMMENT '是否已处理：0未处理 1已处理',
    data_source     VARCHAR(50) COMMENT '数据来源',
    external_id     VARCHAR(200) COMMENT '外部系统ID（用于去重）',
    create_time     DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_company_id (company_id),
    INDEX idx_event_type (event_type),
    INDEX idx_severity (severity),
    INDEX idx_is_read (is_read),
    INDEX idx_event_date (event_date),
    INDEX idx_external_id (external_id)
) ENGINE=InnoDB COMMENT='风险事件表';

-- 事件类型枚举说明：
-- LAWSUIT: 涉诉（开庭公告、裁判文书）
-- EXECUTION: 被执行（被执行人、失信被执行人）
-- ABNORMAL: 经营异常（列入经营异常名录）
-- PENALTY: 行政处罚
-- CHANGE_LEGAL: 法人变更
-- CHANGE_CAPITAL: 注册资本变更
-- TAX: 税务异常（欠税公告）
-- NEGATIVE: 负面舆情
-- PLEDGE: 股权质押
-- FREEZE: 股权冻结

-- ========================================
-- 4. 贷后检查任务表
-- ========================================
DROP TABLE IF EXISTS risk_check_task;
CREATE TABLE risk_check_task (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '任务ID',
    company_id      BIGINT NOT NULL COMMENT '企业ID',
    loan_id         VARCHAR(100) COMMENT '关联贷款编号',
    task_type       VARCHAR(50) COMMENT '任务类型：REGULAR-定期检查/SPECIAL-专项检查/ALERT-预警触发检查',
    task_status     TINYINT DEFAULT 0 COMMENT '状态：0待执行 1执行中 2已完成 3已逾期',
    assignee_id     BIGINT COMMENT '负责人ID（客户经理）',
    assignee_name   VARCHAR(50) COMMENT '负责人姓名',
    due_date        DATE COMMENT '截止日期',
    complete_time   DATETIME COMMENT '完成时间',
    check_result    VARCHAR(50) COMMENT '检查结论：NORMAL-正常/ATTENTION-关注/ALERT-预警/RISK-风险',
    check_report    TEXT COMMENT '检查报告内容',
    remark          VARCHAR(1000) COMMENT '备注',
    create_by       VARCHAR(64) COMMENT '创建者',
    create_time     DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by       VARCHAR(64) COMMENT '更新者',
    update_time     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_company_id (company_id),
    INDEX idx_assignee_id (assignee_id),
    INDEX idx_task_status (task_status),
    INDEX idx_due_date (due_date)
) ENGINE=InnoDB COMMENT='贷后检查任务表';

-- ========================================
-- 5. 预警规则表
-- ========================================
DROP TABLE IF EXISTS risk_alert_rule;
CREATE TABLE risk_alert_rule (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '规则ID',
    rule_name       VARCHAR(200) NOT NULL COMMENT '规则名称',
    rule_code       VARCHAR(50) COMMENT '规则编码',
    rule_type       VARCHAR(50) COMMENT '规则类型：EVENT-事件触发/INDICATOR-指标触发/COMBO-组合触发',
    event_type      VARCHAR(50) COMMENT '触发事件类型（事件触发时有效）',
    indicator_code  VARCHAR(50) COMMENT '指标编码（指标触发时有效）',
    operator        VARCHAR(20) COMMENT '比较运算符：GT/GTE/LT/LTE/EQ/NEQ',
    threshold_value VARCHAR(100) COMMENT '阈值',
    alert_level     TINYINT DEFAULT 2 COMMENT '预警等级：1低 2中 3高',
    is_active       TINYINT DEFAULT 1 COMMENT '是否启用：0禁用 1启用',
    notify_type     VARCHAR(100) COMMENT '通知方式：站内消息/SMS/企业微信（逗号分隔）',
    create_by       VARCHAR(64) COMMENT '创建者',
    create_time     DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by       VARCHAR(64) COMMENT '更新者',
    update_time     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark          VARCHAR(500) COMMENT '规则说明',
    INDEX idx_rule_type (rule_type),
    INDEX idx_is_active (is_active)
) ENGINE=InnoDB COMMENT='预警规则表';

-- ========================================
-- 6. 预警记录表
-- ========================================
DROP TABLE IF EXISTS risk_alert_log;
CREATE TABLE risk_alert_log (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '预警ID',
    rule_id         BIGINT COMMENT '触发规则ID',
    company_id      BIGINT NOT NULL COMMENT '企业ID',
    event_id        BIGINT COMMENT '关联事件ID',
    alert_level     TINYINT COMMENT '预警等级：1低 2中 3高',
    alert_title     VARCHAR(500) COMMENT '预警标题',
    alert_content   VARCHAR(2000) COMMENT '预警内容',
    is_notified     TINYINT DEFAULT 0 COMMENT '是否已通知',
    notify_time     DATETIME COMMENT '通知时间',
    is_handled      TINYINT DEFAULT 0 COMMENT '是否已处理',
    handler_id      BIGINT COMMENT '处理人ID',
    handler_name    VARCHAR(50) COMMENT '处理人姓名',
    handle_time     DATETIME COMMENT '处理时间',
    handle_result   VARCHAR(500) COMMENT '处理结果',
    create_time     DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_company_id (company_id),
    INDEX idx_alert_level (alert_level),
    INDEX idx_is_notified (is_notified),
    INDEX idx_is_handled (is_handled),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB COMMENT='预警记录表';

-- ========================================
-- 初始数据：预警规则
-- ========================================
INSERT INTO risk_alert_rule (rule_name, rule_code, rule_type, event_type, alert_level, is_active, notify_type, remark) VALUES
('涉诉预警', 'RULE_LAWSUIT', 'EVENT', 'LAWSUIT', 2, 1, '站内消息', '企业新增涉诉事件时触发'),
('被执行预警', 'RULE_EXECUTION', 'EVENT', 'EXECUTION', 3, 1, '站内消息,SMS', '企业被列为被执行人时触发'),
('失信预警', 'RULE_DISHONEST', 'EVENT', 'EXECUTION', 3, 1, '站内消息,SMS,企业微信', '企业被列为失信被执行人时触发'),
('经营异常预警', 'RULE_ABNORMAL', 'EVENT', 'ABNORMAL', 2, 1, '站内消息', '企业被列入经营异常名录时触发'),
('法人变更预警', 'RULE_LEGAL_CHANGE', 'EVENT', 'CHANGE_LEGAL', 1, 1, '站内消息', '企业法人变更时触发'),
('股权冻结预警', 'RULE_FREEZE', 'EVENT', 'FREEZE', 3, 1, '站内消息,SMS', '企业股权被冻结时触发'),
('股权质押预警', 'RULE_PLEDGE', 'EVENT', 'PLEDGE', 2, 1, '站内消息', '企业股权质押比例超50%时触发'),
('行政处罚预警', 'RULE_PENALTY', 'EVENT', 'PENALTY', 2, 1, '站内消息', '企业受到行政处罚时触发'),
('负面舆情预警', 'RULE_NEGATIVE', 'EVENT', 'NEGATIVE', 2, 1, '站内消息', '企业出现负面舆情时触发');

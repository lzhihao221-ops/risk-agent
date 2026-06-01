-- =====================================================
-- 贷前风控模块 + 贷中监控模块 — 数据库初始化
-- =====================================================

SET NAMES utf8mb4;

-- =====================================================
-- 贷前风控模块
-- =====================================================

-- 1. 贷款申请表
DROP TABLE IF EXISTS loan_application;
CREATE TABLE loan_application (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    app_no          VARCHAR(50) NOT NULL COMMENT '申请编号',
    company_id      BIGINT NOT NULL COMMENT '关联企业ID',
    company_name    VARCHAR(200) NOT NULL COMMENT '企业名称',
    product_type    VARCHAR(50) COMMENT '贷款产品类型（流动资金贷款/固定资产贷款/贸易融资/银承/保函）',
    loan_amount     DECIMAL(15,2) COMMENT '申请金额（万元）',
    loan_term       INT COMMENT '贷款期限（月）',
    loan_purpose    VARCHAR(500) COMMENT '贷款用途',
    interest_rate   DECIMAL(6,4) COMMENT '申请利率（%）',
    repayment_type  VARCHAR(30) COMMENT '还款方式（等额本息/等额本金/先息后本/到期还本付息）',
    guarantee_type  VARCHAR(30) COMMENT '担保方式（信用/保证/抵押/质押/组合）',
    collateral_desc VARCHAR(500) COMMENT '抵押物描述',
    applicant_id    BIGINT COMMENT '申请人ID',
    applicant_name  VARCHAR(50) COMMENT '申请人名称',
    app_status      TINYINT DEFAULT 0 COMMENT '状态（0草稿/1已提交/2审批中/3已批准/4已拒绝/5已放款/6已撤回）',
    risk_score      INT COMMENT '自动评分',
    risk_level      TINYINT COMMENT '自动评级（1低风险/2中风险/3高风险）',
    reject_reason   VARCHAR(500) COMMENT '拒绝原因',
    submit_time     DATETIME COMMENT '提交时间',
    approve_time    DATETIME COMMENT '审批时间',
    remark          VARCHAR(500),
    create_by       VARCHAR(64),
    create_time     DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_by       VARCHAR(64),
    update_time     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_app_no (app_no),
    KEY idx_company_id (company_id),
    KEY idx_status (app_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='贷款申请表';

-- 2. 贷款审批记录表
DROP TABLE IF EXISTS loan_approval;
CREATE TABLE loan_approval (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    app_id          BIGINT NOT NULL COMMENT '申请ID',
    app_no          VARCHAR(50) COMMENT '申请编号',
    approve_level   INT COMMENT '审批层级（1初审/2复审/3终审）',
    approve_role    VARCHAR(50) COMMENT '审批角色（客户经理/风控经理/审批委员/行长）',
    approve_user_id BIGINT COMMENT '审批人ID',
    approve_user    VARCHAR(50) COMMENT '审批人',
    approve_result  TINYINT COMMENT '结果（0待审批/1通过/2拒绝/3退回补充）',
    approve_opinion VARCHAR(500) COMMENT '审批意见',
    approve_time    DATETIME COMMENT '审批时间',
    create_time     DATETIME DEFAULT CURRENT_TIMESTAMP,
    KEY idx_app_id (app_id),
    KEY idx_app_no (app_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='贷款审批记录表';

-- 3. 客户评分卡表
DROP TABLE IF EXISTS loan_scorecard;
CREATE TABLE loan_scorecard (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    company_id      BIGINT NOT NULL COMMENT '企业ID',
    company_name    VARCHAR(200) COMMENT '企业名称',
    score_date      DATE COMMENT '评分日期',
    -- 评分维度
    score_financial DECIMAL(5,2) COMMENT '财务状况得分（0-100）',
    score_operation DECIMAL(5,2) COMMENT '经营能力得分（0-100）',
    score_credit    DECIMAL(5,2) COMMENT '信用记录得分（0-100）',
    score_collateral DECIMAL(5,2) COMMENT '担保抵押得分（0-100）',
    score_industry  DECIMAL(5,2) COMMENT '行业风险得分（0-100）',
    -- 综合
    total_score     DECIMAL(5,2) COMMENT '综合得分',
    grade           VARCHAR(10) COMMENT '信用等级（AAA/AA/A/BBB/BB/B/CCC/CC/C）',
    suggest_amount  DECIMAL(15,2) COMMENT '建议授信额度（万元）',
    suggest_rate    DECIMAL(6,4) COMMENT '建议利率（%）',
    remark          VARCHAR(500),
    create_by       VARCHAR(64),
    create_time     DATETIME DEFAULT CURRENT_TIMESTAMP,
    KEY idx_company_id (company_id),
    KEY idx_score_date (score_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户评分卡表';

-- 4. 抵押物管理表
DROP TABLE IF EXISTS loan_collateral;
CREATE TABLE loan_collateral (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    company_id      BIGINT COMMENT '关联企业ID',
    app_id          BIGINT COMMENT '关联申请ID',
    collateral_type VARCHAR(50) COMMENT '抵押物类型（房产/土地/设备/车辆/存单/股权/其他）',
    collateral_name VARCHAR(200) COMMENT '抵押物名称',
    collateral_desc VARCHAR(500) COMMENT '抵押物描述',
    cert_no         VARCHAR(100) COMMENT '权证编号',
    location        VARCHAR(300) COMMENT '所在地',
    eval_value      DECIMAL(15,2) COMMENT '评估价值（万元）',
    pledge_value    DECIMAL(15,2) COMMENT '抵押价值（万元）',
    pledge_ratio    DECIMAL(5,2) COMMENT '抵押率（%）',
    eval_date       DATE COMMENT '评估日期',
    eval_org        VARCHAR(200) COMMENT '评估机构',
    expire_date     DATE COMMENT '抵押到期日',
    status          TINYINT DEFAULT 1 COMMENT '状态（0无效/1有效/2已解押）',
    remark          VARCHAR(500),
    create_by       VARCHAR(64),
    create_time     DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_by       VARCHAR(64),
    update_time     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_company_id (company_id),
    KEY idx_app_id (app_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='抵押物管理表';

-- =====================================================
-- 贷中监控模块
-- =====================================================

-- 5. 贷款台账表
DROP TABLE IF EXISTS loan_ledger;
CREATE TABLE loan_ledger (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    loan_no         VARCHAR(50) NOT NULL COMMENT '贷款编号',
    app_id          BIGINT COMMENT '关联申请ID',
    app_no          VARCHAR(50) COMMENT '申请编号',
    company_id      BIGINT NOT NULL COMMENT '企业ID',
    company_name    VARCHAR(200) COMMENT '企业名称',
    product_type    VARCHAR(50) COMMENT '产品类型',
    loan_amount     DECIMAL(15,2) COMMENT '放款金额（万元）',
    loan_balance    DECIMAL(15,2) COMMENT '贷款余额（万元）',
    interest_rate   DECIMAL(6,4) COMMENT '执行利率（%）',
    loan_term       INT COMMENT '贷款期限（月）',
    loan_start_date DATE COMMENT '放款日期',
    loan_end_date   DATE COMMENT '到期日期',
    repayment_type  VARCHAR(30) COMMENT '还款方式',
    guarantee_type  VARCHAR(30) COMMENT '担保方式',
    five_category   TINYINT DEFAULT 1 COMMENT '五级分类（1正常/2关注/3次级/4可疑/5损失）',
    overdue_days    INT DEFAULT 0 COMMENT '逾期天数',
    overdue_amount  DECIMAL(15,2) DEFAULT 0 COMMENT '逾期金额（万元）',
    status          TINYINT DEFAULT 1 COMMENT '状态（0已结清/1正常/2逾期/3展期/4核销）',
    manager_id      BIGINT COMMENT '管户经理ID',
    manager_name    VARCHAR(50) COMMENT '管户经理',
    remark          VARCHAR(500),
    create_by       VARCHAR(64),
    create_time     DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_by       VARCHAR(64),
    update_time     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_loan_no (loan_no),
    KEY idx_company_id (company_id),
    KEY idx_five_category (five_category),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='贷款台账表';

-- 6. 还款计划表
DROP TABLE IF EXISTS loan_repayment;
CREATE TABLE loan_repayment (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    loan_id         BIGINT NOT NULL COMMENT '贷款ID',
    loan_no         VARCHAR(50) COMMENT '贷款编号',
    period_no       INT COMMENT '期数',
    plan_date       DATE COMMENT '计划还款日',
    plan_principal  DECIMAL(15,2) COMMENT '计划还款本金（万元）',
    plan_interest   DECIMAL(15,2) COMMENT '计划还款利息（万元）',
    plan_total      DECIMAL(15,2) COMMENT '计划还款总额（万元）',
    actual_date     DATE COMMENT '实际还款日',
    actual_principal DECIMAL(15,2) COMMENT '实际还款本金（万元）',
    actual_interest DECIMAL(15,2) COMMENT '实际还款利息（万元）',
    actual_total    DECIMAL(15,2) COMMENT '实际还款总额（万元）',
    status          TINYINT DEFAULT 0 COMMENT '状态（0待还/1已还/2逾期/3部分还款）',
    overdue_days    INT DEFAULT 0 COMMENT '逾期天数',
    remark          VARCHAR(500),
    create_time     DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_loan_id (loan_id),
    KEY idx_plan_date (plan_date),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='还款计划表';

-- 7. 逾期记录表
DROP TABLE IF EXISTS loan_overdue;
CREATE TABLE loan_overdue (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    loan_id         BIGINT NOT NULL COMMENT '贷款ID',
    loan_no         VARCHAR(50) COMMENT '贷款编号',
    company_id      BIGINT COMMENT '企业ID',
    company_name    VARCHAR(200) COMMENT '企业名称',
    period_no       INT COMMENT '逾期期数',
    overdue_date    DATE COMMENT '逾期开始日期',
    overdue_days    INT COMMENT '逾期天数',
    overdue_principal DECIMAL(15,2) COMMENT '逾期本金（万元）',
    overdue_interest  DECIMAL(15,2) COMMENT '逾期利息（万元）',
    overdue_total   DECIMAL(15,2) COMMENT '逾期总额（万元）',
    penalty_amount  DECIMAL(15,2) COMMENT '罚息金额（万元）',
    five_category   TINYINT COMMENT '五级分类',
    status          TINYINT DEFAULT 1 COMMENT '状态（1逾期/2已还清/3核销）',
    handle_result   VARCHAR(500) COMMENT '处置措施',
    remark          VARCHAR(500),
    create_time     DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_loan_id (loan_id),
    KEY idx_company_id (company_id),
    KEY idx_overdue_days (overdue_days)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='逾期记录表';

-- 8. 五级分类调整记录表
DROP TABLE IF EXISTS loan_category_change;
CREATE TABLE loan_category_change (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    loan_id         BIGINT NOT NULL COMMENT '贷款ID',
    loan_no         VARCHAR(50) COMMENT '贷款编号',
    company_id      BIGINT COMMENT '企业ID',
    old_category    TINYINT COMMENT '原分类',
    new_category    TINYINT COMMENT '新分类',
    change_reason   VARCHAR(500) COMMENT '调整原因',
    adjuster_id     BIGINT COMMENT '调整人ID',
    adjuster_name   VARCHAR(50) COMMENT '调整人',
    approve_user    VARCHAR(50) COMMENT '审批人',
    adjust_time     DATETIME COMMENT '调整时间',
    create_time     DATETIME DEFAULT CURRENT_TIMESTAMP,
    KEY idx_loan_id (loan_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='五级分类调整记录表';

-- =====================================================
-- 初始数据
-- =====================================================

-- 贷款申请数据
INSERT INTO loan_application (app_no, company_id, company_name, product_type, loan_amount, loan_term, loan_purpose, interest_rate, repayment_type, guarantee_type, collateral_desc, app_status, risk_score, risk_level, submit_time, create_by) VALUES
('LA202605001', 1, '宏达科技有限公司', '流动资金贷款', 500.00, 12, '补充流动资金', 4.35, '先息后本', '抵押', '位于朝阳区的办公房产', 3, 72, 2, '2026-05-01', 'admin'),
('LA202605002', 7, '华信地产集团有限公司', '固定资产贷款', 3000.00, 36, '商业地产开发', 5.20, '等额本息', '组合', '项目土地+在建工程', 4, 92, 3, '2026-05-05', 'admin'),
('LA202605003', 10, '瑞达科技股份有限公司', '流动资金贷款', 200.00, 6, '采购原材料', 4.05, '到期还本付息', '信用', '', 3, 35, 1, '2026-05-10', 'admin'),
('LA202605004', 8, '中盛矿业股份有限公司', '银承', 1500.00, 3, '矿产品采购', 3.85, '到期还本付息', '质押', '定期存单质押', 2, 88, 3, '2026-05-15', 'admin'),
('LA202605005', 12, '永盛农业发展有限公司', '流动资金贷款', 100.00, 12, '农业种植', 4.75, '等额本金', '保证', '担保公司连带责任保证', 1, 28, 1, '2026-05-20', 'admin'),
('LA202605006', 11, '东方建设工程有限公司', '贸易融资', 800.00, 6, '工程垫资', 4.55, '先息后本', '抵押', '公司厂房及设备', 2, 85, 3, '2026-05-18', 'admin');

-- 审批记录
INSERT INTO loan_approval (app_id, app_no, approve_level, approve_role, approve_user, approve_result, approve_opinion, approve_time) VALUES
(1, 'LA202605001', 1, '客户经理', '张经理', 1, '企业经营正常，抵押物充足，同意放款', '2026-05-02 10:00:00'),
(1, 'LA202605001', 2, '风控经理', '李风控', 1, '风险可控，同意', '2026-05-02 14:00:00'),
(2, 'LA202605002', 1, '客户经理', '王经理', 1, '项目可行，建议批准', '2026-05-06 09:00:00'),
(2, 'LA202605002', 2, '风控经理', '李风控', 2, '华信地产已被列为被执行人，风险过高，不同意', '2026-05-06 15:00:00'),
(3, 'LA202605003', 1, '客户经理', '张经理', 1, '科技企业，经营稳定', '2026-05-11 10:00:00'),
(4, 'LA202605004', 1, '客户经理', '王经理', 1, '存单质押，风险低', '2026-05-16 09:00:00');

-- 客户评分卡
INSERT INTO loan_scorecard (company_id, company_name, score_date, score_financial, score_operation, score_credit, score_collateral, score_industry, total_score, grade, suggest_amount, suggest_rate, create_by) VALUES
(1, '宏达科技有限公司', '2026-05-01', 75, 80, 85, 70, 78, 77.60, 'A', 600.00, 4.35, 'admin'),
(7, '华信地产集团有限公司', '2026-05-05', 40, 35, 20, 60, 45, 37.00, 'CCC', 0, 0, 'admin'),
(10, '瑞达科技股份有限公司', '2026-05-10', 82, 78, 90, 45, 85, 78.40, 'A', 250.00, 4.05, 'admin'),
(8, '中盛矿业股份有限公司', '2026-05-15', 45, 50, 30, 70, 40, 44.50, 'BB', 500.00, 5.50, 'admin'),
(12, '永盛农业发展有限公司', '2026-05-20', 88, 75, 92, 55, 80, 79.20, 'A', 150.00, 4.75, 'admin'),
(11, '东方建设工程有限公司', '2026-05-18', 42, 48, 25, 65, 42, 41.40, 'BB', 200.00, 6.00, 'admin');

-- 抵押物
INSERT INTO loan_collateral (company_id, app_id, collateral_type, collateral_name, cert_no, location, eval_value, pledge_value, pledge_ratio, eval_date, eval_org, status, create_by) VALUES
(1, 1, '房产', '朝阳区XX大厦3层办公用房', '京房权证朝字第XXXX号', '北京市朝阳区', 800.00, 560.00, 70.00, '2026-04-25', 'XX评估公司', 1, 'admin'),
(7, 2, '土地', 'XX项目用地', '京土国用(2026)第XXX号', '北京市大兴区', 5000.00, 3500.00, 70.00, '2026-04-28', 'XX评估公司', 1, 'admin'),
(11, 6, '房产', '公司厂房', '京房权证通字第XXXX号', '北京市通州区', 1200.00, 840.00, 70.00, '2026-05-10', 'XX评估公司', 1, 'admin');

-- 贷款台账
INSERT INTO loan_ledger (loan_no, app_id, app_no, company_id, company_name, product_type, loan_amount, loan_balance, interest_rate, loan_term, loan_start_date, loan_end_date, repayment_type, guarantee_type, five_category, overdue_days, overdue_amount, status, manager_name, create_by) VALUES
('LN202605001', 1, 'LA202605001', 1, '宏达科技有限公司', '流动资金贷款', 500.00, 500.00, 4.35, 12, '2026-05-05', '2027-05-05', '先息后本', '抵押', 1, 0, 0, 1, '张经理', 'admin'),
('LN202605002', 3, 'LA202605003', 10, '瑞达科技股份有限公司', '流动资金贷款', 200.00, 200.00, 4.05, 6, '2026-05-12', '2026-11-12', '到期还本付息', '信用', 1, 0, 0, 1, '张经理', 'admin');

-- 还款计划
INSERT INTO loan_repayment (loan_id, loan_no, period_no, plan_date, plan_principal, plan_interest, plan_total, status) VALUES
(1, 'LN202605001', 1, '2026-06-05', 0, 1.81, 1.81, 0),
(1, 'LN202605001', 2, '2026-07-05', 0, 1.81, 1.81, 0),
(1, 'LN202605001', 3, '2026-08-05', 0, 1.81, 1.81, 0),
(1, 'LN202605001', 4, '2026-09-05', 0, 1.81, 1.81, 0),
(1, 'LN202605001', 5, '2026-10-05', 0, 1.81, 1.81, 0),
(1, 'LN202605001', 6, '2026-11-05', 0, 1.81, 1.81, 0),
(1, 'LN202605001', 7, '2026-12-05', 0, 1.81, 1.81, 0),
(1, 'LN202605001', 8, '2027-01-05', 0, 1.81, 1.81, 0),
(1, 'LN202605001', 9, '2027-02-05', 0, 1.81, 1.81, 0),
(1, 'LN202605001', 10, '2027-03-05', 0, 1.81, 1.81, 0),
(1, 'LN202605001', 11, '2027-04-05', 0, 1.81, 1.81, 0),
(1, 'LN202605001', 12, '2027-05-05', 500.00, 1.81, 501.81, 0),
(2, 'LN202605002', 1, '2026-11-12', 200.00, 4.05, 204.05, 0);

-- 五级分类调整记录
INSERT INTO loan_category_change (loan_id, loan_no, company_id, old_category, new_category, change_reason, adjuster_name, approve_user, adjust_time) VALUES
(1, 'LN202605001', 1, 1, 1, '首次分类，企业经营正常', '系统', '李风控', '2026-05-05 10:00:00');

SELECT '========== 贷前贷中数据初始化完成 ==========' as '';
SELECT '贷款申请' as '模块', COUNT(*) as '数量' FROM loan_application
UNION ALL SELECT '审批记录', COUNT(*) FROM loan_approval
UNION ALL SELECT '客户评分', COUNT(*) FROM loan_scorecard
UNION ALL SELECT '抵押物', COUNT(*) FROM loan_collateral
UNION ALL SELECT '贷款台账', COUNT(*) FROM loan_ledger
UNION ALL SELECT '还款计划', COUNT(*) FROM loan_repayment
UNION ALL SELECT '逾期记录', COUNT(*) FROM loan_overdue;

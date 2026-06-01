SET NAMES utf8mb4;

-- RWA（风险加权资产）计算表
DROP TABLE IF EXISTS cap_rwa;
CREATE TABLE cap_rwa (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    calc_date       DATE NOT NULL COMMENT '计算日期',
    company_id      BIGINT COMMENT '企业ID',
    company_name    VARCHAR(200) COMMENT '企业名称',
    loan_id         BIGINT COMMENT '贷款ID',
    loan_no         VARCHAR(50) COMMENT '贷款编号',
    exposure_amount DECIMAL(15,2) COMMENT '风险暴露金额（万元）',
    pd              DECIMAL(8,6) COMMENT '违约概率PD',
    lgd             DECIMAL(8,6) COMMENT '违约损失率LGD',
    ead             DECIMAL(15,2) COMMENT '违约风险暴露EAD（万元）',
    maturity        DECIMAL(5,2) COMMENT '有效期限M（年）',
    asset_class     VARCHAR(50) COMMENT '资产类别（公司/个人/同业/政府）',
    risk_weight     DECIMAL(8,4) COMMENT '风险权重（%）',
    rwa_amount      DECIMAL(15,2) COMMENT '风险加权资产（万元）',
    calc_method     VARCHAR(30) COMMENT '计算方法（标准法/内评法/高级内评法）',
    status          TINYINT DEFAULT 1 COMMENT '状态（0无效/1有效）',
    remark          VARCHAR(500),
    create_by       VARCHAR(64),
    create_time     DATETIME DEFAULT CURRENT_TIMESTAMP,
    KEY idx_calc_date (calc_date),
    KEY idx_company_id (company_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='RWA计算表';

-- ECL（预期信用损失）计算表
DROP TABLE IF EXISTS cap_ecl;
CREATE TABLE cap_ecl (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    calc_date       DATE NOT NULL COMMENT '计算日期',
    loan_id         BIGINT COMMENT '贷款ID',
    loan_no         VARCHAR(50) COMMENT '贷款编号',
    company_id      BIGINT COMMENT '企业ID',
    company_name    VARCHAR(200) COMMENT '企业名称',
    stage           TINYINT COMMENT '减值阶段（1第一阶段/2第二阶段/3第三阶段）',
    carrying_amount DECIMAL(15,2) COMMENT '账面余额（万元）',
    pd_12m          DECIMAL(8,6) COMMENT '12个月PD',
    pd_lifetime     DECIMAL(8,6) COMMENT '生命周期PD',
    lgd             DECIMAL(8,6) COMMENT '违约损失率LGD',
    ead             DECIMAL(15,2) COMMENT '违约风险暴露EAD（万元）',
    ecl_12m         DECIMAL(15,2) COMMENT '12个月ECL（万元）',
    ecl_lifetime    DECIMAL(15,2) COMMENT '生命周期ECL（万元）',
    ecl_amount      DECIMAL(15,2) COMMENT '最终ECL金额（万元）',
    provision_rate  DECIMAL(8,4) COMMENT '拨备率（%）',
    calc_method     VARCHAR(30) COMMENT '计算模型（简化方法/一般方法）',
    status          TINYINT DEFAULT 1 COMMENT '状态（0无效/1有效）',
    remark          VARCHAR(500),
    create_by       VARCHAR(64),
    create_time     DATETIME DEFAULT CURRENT_TIMESTAMP,
    KEY idx_calc_date (calc_date),
    KEY idx_company_id (company_id),
    KEY idx_stage (stage)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='ECL预期损失表';

-- 资本充足率计算表
DROP TABLE IF EXISTS cap_adequacy;
CREATE TABLE cap_adequacy (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    calc_date       DATE NOT NULL COMMENT '计算日期',
    report_period   VARCHAR(20) COMMENT '报告期间（2026Q1/2026Q2等）',
    tier1_capital   DECIMAL(15,2) COMMENT '一级资本（万元）',
    tier1_capital_core DECIMAL(15,2) COMMENT '核心一级资本（万元）',
    tier1_capital_other DECIMAL(15,2) COMMENT '其他一级资本（万元）',
    tier2_capital   DECIMAL(15,2) COMMENT '二级资本（万元）',
    total_capital   DECIMAL(15,2) COMMENT '资本净额（万元）',
    rwa_credit      DECIMAL(15,2) COMMENT '信用风险RWA（万元）',
    rwa_market      DECIMAL(15,2) COMMENT '市场风险RWA（万元）',
    rwa_operation   DECIMAL(15,2) COMMENT '操作风险RWA（万元）',
    total_rwa       DECIMAL(15,2) COMMENT '风险加权资产合计（万元）',
    car             DECIMAL(6,4) COMMENT '资本充足率（%）',
    tier1_ratio     DECIMAL(6,4) COMMENT '一级资本充足率（%）',
    core_ratio      DECIMAL(6,4) COMMENT '核心一级资本充足率（%）',
    car_requirement DECIMAL(6,4) DEFAULT 10.50 COMMENT '资本充足率监管要求（%）',
    tier1_requirement DECIMAL(6,4) DEFAULT 8.50 COMMENT '一级资本充足率要求（%）',
    core_requirement  DECIMAL(6,4) DEFAULT 7.50 COMMENT '核心一级资本充足率要求（%）',
    leverage_ratio  DECIMAL(6,4) COMMENT '杠杆率（%）',
    leverage_exposure DECIMAL(15,2) COMMENT '杠杆率暴露（万元）',
    status          TINYINT DEFAULT 1 COMMENT '状态（0草稿/1已确认/2已报送）',
    calc_user       VARCHAR(50) COMMENT '计算人',
    confirm_user    VARCHAR(50) COMMENT '确认人',
    remark          VARCHAR(500),
    create_by       VARCHAR(64),
    create_time     DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_by       VARCHAR(64),
    update_time     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_report_period (report_period),
    KEY idx_calc_date (calc_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资本充足率计算表';

-- 拨备计提表
DROP TABLE IF EXISTS cap_provision;
CREATE TABLE cap_provision (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    calc_date       DATE NOT NULL COMMENT '计算日期',
    loan_id         BIGINT COMMENT '贷款ID',
    loan_no         VARCHAR(50) COMMENT '贷款编号',
    company_id      BIGINT COMMENT '企业ID',
    company_name    VARCHAR(200) COMMENT '企业名称',
    five_category   TINYINT COMMENT '五级分类',
    provision_type  VARCHAR(30) COMMENT '拨备类型（一般准备/专项准备/特种准备）',
    carrying_amount DECIMAL(15,2) COMMENT '账面余额（万元）',
    provision_rate  DECIMAL(8,4) COMMENT '拨备率（%）',
    provision_amount DECIMAL(15,2) COMMENT '拨备金额（万元）',
    accumulated_provision DECIMAL(15,2) COMMENT '累计拨备（万元）',
    shortfall       DECIMAL(15,2) COMMENT '拨备缺口（万元）',
    status          TINYINT DEFAULT 1 COMMENT '状态（0无效/1有效）',
    remark          VARCHAR(500),
    create_by       VARCHAR(64),
    create_time     DATETIME DEFAULT CURRENT_TIMESTAMP,
    KEY idx_calc_date (calc_date),
    KEY idx_company_id (company_id),
    KEY idx_five_category (five_category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='拨备计提表';

INSERT INTO cap_rwa (calc_date, company_id, company_name, loan_id, loan_no, exposure_amount, pd, lgd, ead, maturity, asset_class, risk_weight, rwa_amount, calc_method, create_by) VALUES
('2026-05-31', 1, '宏达科技有限公司', 1, 'LN202605001', 500.00, 0.0200, 0.4500, 500.00, 1.00, '公司', 100.00, 500.00, '标准法', 'admin'),
('2026-05-31', 10, '瑞达科技股份有限公司', 2, 'LN202605002', 200.00, 0.0100, 0.3500, 200.00, 0.50, '公司', 75.00, 150.00, '标准法', 'admin');

INSERT INTO cap_ecl (calc_date, loan_id, loan_no, company_id, company_name, stage, carrying_amount, pd_12m, pd_lifetime, lgd, ead, ecl_12m, ecl_lifetime, ecl_amount, provision_rate, calc_method, create_by) VALUES
('2026-05-31', 1, 'LN202605001', 1, '宏达科技有限公司', 1, 500.00, 0.0200, 0.0500, 0.45, 500.00, 4.50, 11.25, 4.50, 0.90, '一般方法', 'admin'),
('2026-05-31', 2, 'LN202605002', 10, '瑞达科技股份有限公司', 1, 200.00, 0.0100, 0.0300, 0.35, 200.00, 0.70, 2.10, 0.70, 0.35, '一般方法', 'admin');

INSERT INTO cap_adequacy (calc_date, report_period, tier1_capital, tier1_capital_core, tier1_capital_other, tier2_capital, total_capital, rwa_credit, rwa_market, rwa_operation, total_rwa, car, tier1_ratio, core_ratio, car_requirement, tier1_requirement, core_requirement, leverage_ratio, leverage_exposure, status, calc_user, create_by) VALUES
('2026-03-31', '2026Q1', 50000.00, 40000.00, 10000.00, 15000.00, 65000.00, 450000.00, 50000.00, 30000.00, 530000.00, 12.26, 9.43, 7.55, 10.50, 8.50, 7.50, 5.88, 850000.00, 1, '财务部', 'admin');

INSERT INTO cap_provision (calc_date, loan_id, loan_no, company_id, company_name, five_category, provision_type, carrying_amount, provision_rate, provision_amount, accumulated_provision, shortfall, create_by) VALUES
('2026-05-31', 1, 'LN202605001', 1, '宏达科技有限公司', 1, '一般准备', 500.00, 1.00, 5.00, 5.00, 0, 'admin'),
('2026-05-31', 2, 'LN202605002', 10, '瑞达科技股份有限公司', 1, '一般准备', 200.00, 1.00, 2.00, 2.00, 0, 'admin');

SELECT '========== 资本计量数据初始化完成 ==========' as '';

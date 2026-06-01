SET NAMES utf8mb4;

-- 1104报表体系表
DROP TABLE IF EXISTS reg_report_1104;
CREATE TABLE reg_report_1104 (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    report_code     VARCHAR(50) NOT NULL COMMENT '报表编号',
    report_name     VARCHAR(200) NOT NULL COMMENT '报表名称',
    report_type     VARCHAR(50) COMMENT '报表类型（基础报表/特色报表/监管指标）',
    report_period   VARCHAR(20) COMMENT '报告期间（2026Q1/202605等）',
    report_date     DATE COMMENT '报表日期',
    frequency       VARCHAR(20) COMMENT '报送频次（月报/季报/半年报/年报）',
    submit_deadline DATE COMMENT '报送截止日',
    report_data     LONGTEXT COMMENT '报表数据JSON',
    status          TINYINT DEFAULT 0 COMMENT '状态（0草稿/1已生成/2已审核/3已报送/4被退回）',
    generate_user   VARCHAR(50) COMMENT '生成人',
    audit_user      VARCHAR(50) COMMENT '审核人',
    submit_user     VARCHAR(50) COMMENT '报送人',
    submit_time     DATETIME COMMENT '报送时间',
    reject_reason   VARCHAR(500) COMMENT '退回原因',
    file_path       VARCHAR(500) COMMENT '报表文件路径',
    remark          VARCHAR(500),
    create_by       VARCHAR(64),
    create_time     DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_by       VARCHAR(64),
    update_time     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_code_period (report_code, report_period),
    KEY idx_report_type (report_type),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='1104报表体系表';

-- EAST数据报送表
DROP TABLE IF EXISTS reg_east_report;
CREATE TABLE reg_east_report (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    report_period   VARCHAR(20) NOT NULL COMMENT '报告期间',
    report_date     DATE COMMENT '报表日期',
    data_type       VARCHAR(50) COMMENT '数据类型（客户信息/信贷业务/担保信息/交易流水/风险分类）',
    data_table      VARCHAR(100) COMMENT '数据表名',
    record_count    BIGINT COMMENT '记录数',
    file_name       VARCHAR(200) COMMENT '文件名',
    file_path       VARCHAR(500) COMMENT '文件路径',
    file_size       BIGINT COMMENT '文件大小（字节）',
    check_result    TEXT COMMENT '校验结果JSON',
    check_status    TINYINT DEFAULT 0 COMMENT '校验状态（0未校验/1校验通过/2校验失败）',
    status          TINYINT DEFAULT 0 COMMENT '状态（0未生成/1已生成/2已校验/3已报送/4被退回）',
    generate_user   VARCHAR(50) COMMENT '生成人',
    submit_user     VARCHAR(50) COMMENT '报送人',
    submit_time     DATETIME COMMENT '报送时间',
    reject_reason   VARCHAR(500) COMMENT '退回原因',
    remark          VARCHAR(500),
    create_by       VARCHAR(64),
    create_time     DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_by       VARCHAR(64),
    update_time     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_report_period (report_period),
    KEY idx_data_type (data_type),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='EAST数据报送表';

-- 征信报送表
DROP TABLE IF EXISTS reg_credit_report;
CREATE TABLE reg_credit_report (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    report_period   VARCHAR(20) NOT NULL COMMENT '报告期间',
    report_date     DATE COMMENT '报表日期',
    report_type     VARCHAR(30) COMMENT '报送类型（按日报送/月度报送/异议处理）',
    data_type       VARCHAR(50) COMMENT '数据类型（贷款信息/还款记录/担保信息/企业基本信息）',
    record_count    BIGINT COMMENT '记录数',
    success_count   BIGINT COMMENT '成功记录数',
    fail_count      BIGINT COMMENT '失败记录数',
    file_name       VARCHAR(200) COMMENT '文件名',
    file_path       VARCHAR(500) COMMENT '文件路径',
    status          TINYINT DEFAULT 0 COMMENT '状态（0未生成/1已生成/2已校验/3已报送/4报送失败）',
    error_detail    TEXT COMMENT '错误明细JSON',
    submit_user     VARCHAR(50) COMMENT '报送人',
    submit_time     DATETIME COMMENT '报送时间',
    batch_no        VARCHAR(100) COMMENT '报送批次号',
    remark          VARCHAR(500),
    create_by       VARCHAR(64),
    create_time     DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_by       VARCHAR(64),
    update_time     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_report_period (report_period),
    KEY idx_data_type (data_type),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='征信报送表';

-- 监管指标表
DROP TABLE IF EXISTS reg_indicator;
CREATE TABLE reg_indicator (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    indicator_code  VARCHAR(50) NOT NULL COMMENT '指标编码',
    indicator_name  VARCHAR(200) NOT NULL COMMENT '指标名称',
    indicator_type  VARCHAR(50) COMMENT '指标类型（资本充足/资产质量/流动性/盈利性/杠杆）',
    calc_date       DATE COMMENT '计算日期',
    report_period   VARCHAR(20) COMMENT '报告期间',
    indicator_value DECIMAL(15,4) COMMENT '指标值',
    unit            VARCHAR(20) COMMENT '单位（%/倍/万元）',
    threshold_warn  DECIMAL(15,4) COMMENT '预警阈值',
    threshold_danger DECIMAL(15,4) COMMENT '危险阈值',
    regulatory_line DECIMAL(15,4) COMMENT '监管红线',
    status          TINYINT COMMENT '状态（0正常/1预警/2超标）',
    calc_detail     TEXT COMMENT '计算明细JSON',
    remark          VARCHAR(500),
    create_by       VARCHAR(64),
    create_time     DATETIME DEFAULT CURRENT_TIMESTAMP,
    KEY idx_indicator_code (indicator_code),
    KEY idx_calc_date (calc_date),
    KEY idx_indicator_type (indicator_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='监管指标表';

INSERT INTO reg_report_1104 (report_code, report_name, report_type, report_period, report_date, frequency, submit_deadline, status, create_by) VALUES
('S41', '资本充足率汇总表', '基础报表', '2026Q1', '2026-03-31', '季报', '2026-04-15', 3, 'admin'),
('S42', '资本充足率明细表', '基础报表', '2026Q1', '2026-03-31', '季报', '2026-04-15', 3, 'admin'),
('G21', '流动性比例情况表', '基础报表', '202605', '2026-05-31', '月报', '2026-06-10', 1, 'admin'),
('G22', '流动性缺口情况表', '基础报表', '202605', '2026-05-31', '月报', '2026-06-10', 1, 'admin'),
('G11', '贷款质量五级分类情况表', '基础报表', '202605', '2026-05-31', '月报', '2026-06-10', 1, 'admin'),
('G0101', '存贷款月日均情况表', '基础报表', '202605', '2026-05-31', '月报', '2026-06-10', 0, 'admin'),
('S63', '大额风险暴露情况表', '基础报表', '2026Q1', '2026-03-31', '季报', '2026-04-15', 3, 'admin'),
('S66', '杠杆率情况表', '基础报表', '2026Q1', '2026-03-31', '季报', '2026-04-15', 3, 'admin');

INSERT INTO reg_east_report (report_period, report_date, data_type, data_table, record_count, check_status, status, create_by) VALUES
('202605', '2026-05-31', '客户信息', 'ETL_CUSTOMER', 150, 1, 2, 'admin'),
('202605', '2026-05-31', '信贷业务', 'ETL_LOAN', 85, 1, 2, 'admin'),
('202605', '2026-05-31', '担保信息', 'ETL_GUARANTEE', 42, 1, 2, 'admin'),
('202605', '2026-05-31', '交易流水', 'ETL_TRANSACTION', 12500, 0, 0, 'admin'),
('202605', '2026-05-31', '风险分类', 'ETL_RISK_CLASSIFY', 85, 1, 2, 'admin');

INSERT INTO reg_credit_report (report_period, report_date, report_type, data_type, record_count, success_count, fail_count, status, batch_no, create_by) VALUES
('202605', '2026-05-31', '月度报送', '贷款信息', 85, 83, 2, 3, 'CR202605001', 'admin'),
('202605', '2026-05-31', '月度报送', '还款记录', 450, 448, 2, 3, 'CR202605001', 'admin'),
('202605', '2026-05-31', '月度报送', '担保信息', 42, 42, 0, 3, 'CR202605001', 'admin');

INSERT INTO reg_indicator (indicator_code, indicator_name, indicator_type, calc_date, report_period, indicator_value, unit, threshold_warn, threshold_danger, regulatory_line, status, create_by) VALUES
('CAR', '资本充足率', '资本充足', '2026-03-31', '2026Q1', 12.26, '%', 10.50, 10.00, 10.50, 0, 'admin'),
('T1R', '一级资本充足率', '资本充足', '2026-03-31', '2026Q1', 9.43, '%', 8.50, 8.00, 8.50, 0, 'admin'),
('CET1', '核心一级资本充足率', '资本充足', '2026-03-31', '2026Q1', 7.55, '%', 7.50, 7.00, 7.50, 1, 'admin'),
('NPL', '不良贷款率', '资产质量', '2026-05-31', '202605', 1.85, '%', 3.00, 5.00, 5.00, 0, 'admin'),
('NPL_COVER', '拨备覆盖率', '资产质量', '2026-05-31', '202605', 185.50, '%', 150.00, 120.00, 150.00, 0, 'admin'),
('LCR', '流动性覆盖率', '流动性', '2026-03-31', '2026Q1', 135.20, '%', 100.00, 90.00, 100.00, 0, 'admin'),
('LR', '杠杆率', '杠杆', '2026-03-31', '2026Q1', 5.88, '%', 4.00, 3.00, 4.00, 0, 'admin'),
('ROA', '资产收益率', '盈利性', '2026-05-31', '202605', 0.95, '%', 0.60, 0.30, NULL, 0, 'admin'),
('ROE', '资本收益率', '盈利性', '2026-05-31', '202605', 12.30, '%', 8.00, 5.00, NULL, 0, 'admin'),
('CIR', '成本收入比', '盈利性', '2026-05-31', '202605', 32.50, '%', 40.00, 50.00, NULL, 0, 'admin');

SELECT '========== 监管报表数据初始化完成 ==========' as '';

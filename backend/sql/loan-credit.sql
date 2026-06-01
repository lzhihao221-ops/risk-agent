SET NAMES utf8mb4;

-- 征信查询记录表
DROP TABLE IF EXISTS loan_credit_query;
CREATE TABLE loan_credit_query (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    company_id      BIGINT NOT NULL COMMENT '企业ID',
    company_name    VARCHAR(200) COMMENT '企业名称',
    query_type      VARCHAR(30) COMMENT '查询类型（贷前审批/贷后管理/担保审查/异议核查）',
    query_source    VARCHAR(50) COMMENT '查询来源（人行征信/百行征信/内部系统）',
    query_date      DATE COMMENT '查询日期',
    query_result    TEXT COMMENT '查询结果JSON',
    credit_score    INT COMMENT '征信评分',
    debt_amount     DECIMAL(15,2) COMMENT '负债总额（万元）',
    overdue_count   INT COMMENT '逾期次数',
    query_count_6m  INT COMMENT '近6月查询次数',
    has_bad_record  TINYINT DEFAULT 0 COMMENT '是否有不良记录（0否/1是）',
    operator_id     BIGINT COMMENT '操作人ID',
    operator_name   VARCHAR(50) COMMENT '操作人',
    status          TINYINT DEFAULT 1 COMMENT '状态（0失败/1成功/2处理中）',
    remark          VARCHAR(500),
    create_time     DATETIME DEFAULT CURRENT_TIMESTAMP,
    KEY idx_company_id (company_id),
    KEY idx_query_date (query_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='征信查询记录表';

-- 授信审批流程表
DROP TABLE IF EXISTS loan_credit_approval;
CREATE TABLE loan_credit_approval (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    app_id          BIGINT NOT NULL COMMENT '贷款申请ID',
    app_no          VARCHAR(50) COMMENT '申请编号',
    company_id      BIGINT COMMENT '企业ID',
    company_name    VARCHAR(200) COMMENT '企业名称',
    credit_line     DECIMAL(15,2) COMMENT '授信额度（万元）',
    credit_period   INT COMMENT '授信期限（月）',
    credit_rate     DECIMAL(6,4) COMMENT '授信利率（%）',
    credit_condition TEXT COMMENT '授信条件',
    approval_status TINYINT DEFAULT 0 COMMENT '审批状态（0待审/1通过/2拒绝/3有条件通过）',
    approve_level   INT COMMENT '审批层级',
    approve_user    VARCHAR(50) COMMENT '审批人',
    approve_opinion VARCHAR(500) COMMENT '审批意见',
    approve_time    DATETIME COMMENT '审批时间',
    expire_date     DATE COMMENT '授信到期日',
    create_time     DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_app_id (app_id),
    KEY idx_company_id (company_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='授信审批流程表';

-- 授信额度管理表
DROP TABLE IF EXISTS loan_credit_limit;
CREATE TABLE loan_credit_limit (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    company_id      BIGINT NOT NULL COMMENT '企业ID',
    company_name    VARCHAR(200) COMMENT '企业名称',
    total_limit     DECIMAL(15,2) COMMENT '总授信额度（万元）',
    used_limit      DECIMAL(15,2) DEFAULT 0 COMMENT '已用额度（万元）',
    available_limit DECIMAL(15,2) COMMENT '可用额度（万元）',
    credit_grade    VARCHAR(10) COMMENT '授信等级',
    valid_from      DATE COMMENT '有效期起',
    valid_to        DATE COMMENT '有效期止',
    review_date     DATE COMMENT '下次审查日',
    status          TINYINT DEFAULT 1 COMMENT '状态（0冻结/1正常/2到期）',
    remark          VARCHAR(500),
    create_by       VARCHAR(64),
    create_time     DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_by       VARCHAR(64),
    update_time     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_company_id (company_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='授信额度管理表';

INSERT INTO loan_credit_query (company_id, company_name, query_type, query_source, query_date, credit_score, debt_amount, overdue_count, query_count_6m, has_bad_record, operator_name, status) VALUES
(1, '宏达科技有限公司', '贷前审批', '人行征信', '2026-05-01', 75, 200.00, 0, 2, 0, '张经理', 1),
(7, '华信地产集团有限公司', '贷前审批', '人行征信', '2026-05-05', 35, 5000.00, 8, 12, 1, '王经理', 1),
(10, '瑞达科技股份有限公司', '贷前审批', '百行征信', '2026-05-10', 82, 50.00, 0, 1, 0, '张经理', 1),
(8, '中盛矿业股份有限公司', '贷前审批', '人行征信', '2026-05-15', 45, 2000.00, 3, 5, 0, '王经理', 1);

INSERT INTO loan_credit_approval (app_id, app_no, company_id, company_name, credit_line, credit_period, credit_rate, credit_condition, approval_status, approve_level, approve_user, approve_opinion, approve_time, expire_date) VALUES
(1, 'LA202605001', 1, '宏达科技有限公司', 800.00, 12, 4.35, '抵押物价值不低于600万', 1, 2, '李风控', '综合评估通过，同意授信', '2026-05-02 14:00:00', '2027-05-02'),
(3, 'LA202605003', 10, '瑞达科技股份有限公司', 300.00, 6, 4.05, '信用授信', 1, 1, '张经理', '科技企业，信用良好', '2026-05-11 10:00:00', '2026-11-11');

INSERT INTO loan_credit_limit (company_id, company_name, total_limit, used_limit, available_limit, credit_grade, valid_from, valid_to, review_date, status, create_by) VALUES
(1, '宏达科技有限公司', 800.00, 500.00, 300.00, 'A', '2026-01-01', '2027-01-01', '2026-12-01', 1, 'admin'),
(10, '瑞达科技股份有限公司', 300.00, 200.00, 100.00, 'A', '2026-01-01', '2027-01-01', '2026-12-01', 1, 'admin'),
(8, '中盛矿业股份有限公司', 1000.00, 0, 1000.00, 'BB', '2026-01-01', '2026-12-31', '2026-09-01', 1, 'admin');

SELECT '========== 贷前增强数据初始化完成 ==========' as '';

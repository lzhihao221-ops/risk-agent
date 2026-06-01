SET NAMES utf8mb4;

-- 放款申请表
DROP TABLE IF EXISTS loan_disbursement;
CREATE TABLE loan_disbursement (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    loan_no         VARCHAR(50) COMMENT '贷款编号',
    app_no          VARCHAR(50) COMMENT '申请编号',
    company_id      BIGINT NOT NULL COMMENT '企业ID',
    company_name    VARCHAR(200) COMMENT '企业名称',
    disburse_amount DECIMAL(15,2) COMMENT '放款金额（万元）',
    disburse_date   DATE COMMENT '放款日期',
    disburse_account VARCHAR(50) COMMENT '放款账号',
    receive_account VARCHAR(50) COMMENT '收款账号',
    receive_bank    VARCHAR(100) COMMENT '收款银行',
    disburse_type   VARCHAR(30) COMMENT '放款方式（一次性/分批）',
    status          TINYINT DEFAULT 0 COMMENT '状态（0待放款/1已放款/2已拒绝/3已取消）',
    operator_name   VARCHAR(50) COMMENT '操作人',
    approve_user    VARCHAR(50) COMMENT '审批人',
    approve_time    DATETIME COMMENT '审批时间',
    disburse_time   DATETIME COMMENT '实际放款时间',
    remark          VARCHAR(500),
    create_by       VARCHAR(64),
    create_time     DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_by       VARCHAR(64),
    update_time     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_company_id (company_id),
    KEY idx_loan_no (loan_no),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='放款申请表';

-- 利息计提表
DROP TABLE IF EXISTS loan_interest_accrual;
CREATE TABLE loan_interest_accrual (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    loan_id         BIGINT NOT NULL COMMENT '贷款ID',
    loan_no         VARCHAR(50) COMMENT '贷款编号',
    company_id      BIGINT COMMENT '企业ID',
    company_name    VARCHAR(200) COMMENT '企业名称',
    accrual_date    DATE COMMENT '计提日期',
    accrual_amount  DECIMAL(15,2) COMMENT '计提利息金额（万元）',
    principal       DECIMAL(15,2) COMMENT '计息本金（万元）',
    interest_rate   DECIMAL(6,4) COMMENT '利率（%）',
    days            INT COMMENT '计息天数',
    accrual_type    VARCHAR(20) COMMENT '计提类型（正常/逾期/罚息）',
    status          TINYINT DEFAULT 1 COMMENT '状态（0已冲销/1有效）',
    create_time     DATETIME DEFAULT CURRENT_TIMESTAMP,
    KEY idx_loan_id (loan_id),
    KEY idx_accrual_date (accrual_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='利息计提表';

-- 利率调整记录表
DROP TABLE IF EXISTS loan_rate_adjust;
CREATE TABLE loan_rate_adjust (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    loan_id         BIGINT NOT NULL COMMENT '贷款ID',
    loan_no         VARCHAR(50) COMMENT '贷款编号',
    old_rate        DECIMAL(6,4) COMMENT '原利率（%）',
    new_rate        DECIMAL(6,4) COMMENT '新利率（%）',
    adjust_reason   VARCHAR(500) COMMENT '调整原因',
    adjust_type     VARCHAR(30) COMMENT '调整类型（LPR联动/手动调整/逾期上浮）',
    effective_date  DATE COMMENT '生效日期',
    operator_name   VARCHAR(50) COMMENT '操作人',
    approve_user    VARCHAR(50) COMMENT '审批人',
    create_time     DATETIME DEFAULT CURRENT_TIMESTAMP,
    KEY idx_loan_id (loan_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='利率调整记录表';

INSERT INTO loan_disbursement (loan_no, app_no, company_id, company_name, disburse_amount, disburse_date, disburse_account, receive_account, receive_bank, disburse_type, status, operator_name, approve_user, approve_time, disburse_time) VALUES
('LN202605001', 'LA202605001', 1, '宏达科技有限公司', 500.00, '2026-05-05', '622848001', '622848002', '工商银行朝阳支行', '一次性', 1, '张经理', '李风控', '2026-05-04 16:00:00', '2026-05-05 09:30:00'),
('LN202605002', 'LA202605003', 10, '瑞达科技股份有限公司', 200.00, '2026-05-12', '622848003', '622848004', '建设银行海淀支行', '一次性', 1, '张经理', '李风控', '2026-05-11 15:00:00', '2026-05-12 10:00:00');

INSERT INTO loan_interest_accrual (loan_id, loan_no, company_id, company_name, accrual_date, accrual_amount, principal, interest_rate, days, accrual_type, status) VALUES
(1, 'LN202605001', 1, '宏达科技有限公司', '2026-05-31', 1.81, 500.00, 4.35, 31, '正常', 1),
(2, 'LN202605002', 10, '瑞达科技股份有限公司', '2026-05-31', 0.68, 200.00, 4.05, 31, '正常', 1);

SELECT '========== 贷中增强数据初始化完成 ==========' as '';

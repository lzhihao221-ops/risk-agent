SET NAMES utf8mb4;

-- 催收管理表
DROP TABLE IF EXISTS loan_collection;
CREATE TABLE loan_collection (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    loan_id         BIGINT NOT NULL COMMENT '贷款ID',
    loan_no         VARCHAR(50) COMMENT '贷款编号',
    company_id      BIGINT COMMENT '企业ID',
    company_name    VARCHAR(200) COMMENT '企业名称',
    overdue_amount  DECIMAL(15,2) COMMENT '逾期金额（万元）',
    overdue_days    INT COMMENT '逾期天数',
    collection_type VARCHAR(30) COMMENT '催收方式（电话催收/上门催收/律师函/诉讼催收/委外催收）',
    collection_date DATE COMMENT '催收日期',
    collector_name  VARCHAR(50) COMMENT '催收人',
    collection_result VARCHAR(200) COMMENT '催收结果',
    next_action     VARCHAR(200) COMMENT '下一步措施',
    next_date       DATE COMMENT '下次催收日期',
    promise_amount  DECIMAL(15,2) COMMENT '承诺还款金额（万元）',
    promise_date    DATE COMMENT '承诺还款日期',
    status          TINYINT DEFAULT 1 COMMENT '状态（0已结清/1催收中/2已升级/3已委外）',
    remark          VARCHAR(500),
    create_by       VARCHAR(64),
    create_time     DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_by       VARCHAR(64),
    update_time     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_loan_id (loan_id),
    KEY idx_company_id (company_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='催收管理表';

-- 催收记录表
DROP TABLE IF EXISTS loan_collection_log;
CREATE TABLE loan_collection_log (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    collection_id   BIGINT NOT NULL COMMENT '催收任务ID',
    loan_id         BIGINT COMMENT '贷款ID',
    loan_no         VARCHAR(50) COMMENT '贷款编号',
    contact_person  VARCHAR(50) COMMENT '联系人',
    contact_phone   VARCHAR(30) COMMENT '联系电话',
    contact_type    VARCHAR(20) COMMENT '联系方式（电话/短信/邮件/上门）',
    contact_time    DATETIME COMMENT '联系时间',
    contact_result  VARCHAR(500) COMMENT '联系结果',
    borrower_response VARCHAR(500) COMMENT '借款人反馈',
    operator_name   VARCHAR(50) COMMENT '操作人',
    create_time     DATETIME DEFAULT CURRENT_TIMESTAMP,
    KEY idx_collection_id (collection_id),
    KEY idx_loan_id (loan_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='催收记录表';

-- 资产保全表
DROP TABLE IF EXISTS loan_asset_preservation;
CREATE TABLE loan_asset_preservation (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    loan_id         BIGINT NOT NULL COMMENT '贷款ID',
    loan_no         VARCHAR(50) COMMENT '贷款编号',
    company_id      BIGINT COMMENT '企业ID',
    company_name    VARCHAR(200) COMMENT '企业名称',
    preserve_type   VARCHAR(30) COMMENT '保全方式（诉讼保全/仲裁保全/协商保全/以物抵债/债务重组）',
    preserve_status TINYINT DEFAULT 0 COMMENT '状态（0申请中/1已保全/2执行中/3已完成/4已终止）',
    court_name      VARCHAR(200) COMMENT '法院名称',
    case_no         VARCHAR(100) COMMENT '案号',
    claim_amount    DECIMAL(15,2) COMMENT '债权金额（万元）',
    preserve_amount DECIMAL(15,2) COMMENT '保全金额（万元）',
    preserve_asset  TEXT COMMENT '保全资产描述',
    lawyer_name     VARCHAR(50) COMMENT '代理律师',
    filing_date     DATE COMMENT '立案日期',
    hearing_date    DATE COMMENT '开庭日期',
    judgment_date    DATE COMMENT '判决日期',
    judgment_result VARCHAR(500) COMMENT '判决结果',
    execution_status VARCHAR(50) COMMENT '执行情况',
    recovery_amount DECIMAL(15,2) DEFAULT 0 COMMENT '已回收金额（万元）',
    remark          VARCHAR(500),
    create_by       VARCHAR(64),
    create_time     DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_by       VARCHAR(64),
    update_time     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_loan_id (loan_id),
    KEY idx_company_id (company_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资产保全表';

-- 贷款核销表
DROP TABLE IF EXISTS loan_writeoff;
CREATE TABLE loan_writeoff (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    loan_id         BIGINT NOT NULL COMMENT '贷款ID',
    loan_no         VARCHAR(50) COMMENT '贷款编号',
    company_id      BIGINT COMMENT '企业ID',
    company_name    VARCHAR(200) COMMENT '企业名称',
    writeoff_amount DECIMAL(15,2) COMMENT '核销金额（万元）',
    writeoff_principal DECIMAL(15,2) COMMENT '核销本金（万元）',
    writeoff_interest  DECIMAL(15,2) COMMENT '核销利息（万元）',
    writeoff_reason VARCHAR(500) COMMENT '核销原因',
    writeoff_type   VARCHAR(30) COMMENT '核销类型（呆账核销/账销案存/直接核销）',
    five_category   TINYINT COMMENT '核销时五级分类',
    approve_user    VARCHAR(50) COMMENT '审批人',
    approve_time    DATETIME COMMENT '审批时间',
    approve_opinion VARCHAR(500) COMMENT '审批意见',
    writeoff_date   DATE COMMENT '核销日期',
    status          TINYINT DEFAULT 0 COMMENT '状态（0待审批/1已核销/2已驳回）',
    recover_amount  DECIMAL(15,2) DEFAULT 0 COMMENT '后续回收金额（万元）',
    remark          VARCHAR(500),
    create_by       VARCHAR(64),
    create_time     DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_by       VARCHAR(64),
    update_time     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_loan_id (loan_id),
    KEY idx_company_id (company_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='贷款核销表';

INSERT INTO loan_collection (loan_id, loan_no, company_id, company_name, overdue_amount, overdue_days, collection_type, collection_date, collector_name, collection_result, next_action, next_date, status, create_by) VALUES
(1, 'LN202605001', 1, '宏达科技有限公司', 1.81, 5, '电话催收', '2026-05-20', '催收员小王', '企业表示资金周转困难，申请延期', '上门催收', '2026-05-25', 1, 'admin');

INSERT INTO loan_collection_log (collection_id, loan_id, loan_no, contact_person, contact_phone, contact_type, contact_time, contact_result, borrower_response, operator_name) VALUES
(1, 1, 'LN202605001', '财务李总', '13800138000', '电话', '2026-05-20 10:00:00', '已联系，企业承诺月底还款', '资金紧张，月底有一笔回款', '催收员小王');

INSERT INTO loan_asset_preservation (loan_id, loan_no, company_id, company_name, preserve_type, preserve_status, court_name, case_no, claim_amount, preserve_amount, filing_date, remark, create_by) VALUES
(2, 'LN202605002', 10, '瑞达科技股份有限公司', '诉讼保全', 1, '北京市海淀区人民法院', '(2026)京0108民初XXX号', 204.05, 250.00, '2026-05-15', '已申请财产保全，冻结企业账户', 'admin');

SELECT '========== 贷后增强数据初始化完成 ==========' as '';

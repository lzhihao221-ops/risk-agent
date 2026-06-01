-- ========== PD违约概率模型 ==========
CREATE TABLE IF NOT EXISTS pd_model (
  model_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  model_name VARCHAR(100) NOT NULL,
  model_type VARCHAR(50) NOT NULL COMMENT 'LOGISTIC/SCORECARD/MERTON',
  model_version VARCHAR(20) DEFAULT '1.0',
  target_variable VARCHAR(50) DEFAULT 'default_flag',
  sample_size INT,
  auc_roc DECIMAL(6,4),
  ks_statistic DECIMAL(6,4),
  gini DECIMAL(6,4),
  training_date DATE,
  cutoff_score DECIMAL(10,4),
  coefficient_json TEXT,
  status CHAR(1) DEFAULT '0',
  create_by VARCHAR(64) DEFAULT '',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_by VARCHAR(64) DEFAULT '',
  update_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  remark VARCHAR(500)
) ENGINE=InnoDB COMMENT='PD违约概率模型';

CREATE TABLE IF NOT EXISTS pd_model_variable (
  variable_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  model_id BIGINT NOT NULL,
  variable_name VARCHAR(100) NOT NULL,
  variable_code VARCHAR(100) NOT NULL,
  variable_type VARCHAR(30) COMMENT 'NUMERIC/CATEGORICAL',
  coefficient DECIMAL(12,6),
  woe_bins TEXT,
  iv_value DECIMAL(8,4),
  vif DECIMAL(8,4),
  p_value DECIMAL(8,4),
  sort_order INT DEFAULT 0,
  INDEX idx_model (model_id)
) ENGINE=InnoDB COMMENT='PD模型变量';

CREATE TABLE IF NOT EXISTS pd_score_record (
  score_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  company_id BIGINT,
  company_name VARCHAR(200),
  loan_id BIGINT,
  model_id BIGINT,
  score_date DATE NOT NULL,
  raw_score DECIMAL(10,4),
  probability DECIMAL(8,6),
  rating_grade VARCHAR(10),
  score_level VARCHAR(20),
  variable_detail TEXT,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_company (company_id),
  INDEX idx_date (score_date)
) ENGINE=InnoDB COMMENT='PD评分记录';

INSERT INTO pd_model (model_name, model_type, model_version, sample_size, auc_roc, ks_statistic, gini, training_date, cutoff_score, coefficient_json, status) VALUES
('企业信用评分卡v1', 'SCORECARD', '1.0', 5000, 0.8200, 0.4500, 0.6400, CURDATE(), 600, '{"intercept":-2.5,"debt_ratio":0.8,"current_ratio":-0.3,"profit_margin":-0.5,"overdue_days":1.2,"company_age":-0.15,"asset_size":-0.1}', '1'),
('Logistic违约模型v1', 'LOGISTIC', '1.0', 5000, 0.7900, 0.4100, 0.5800, CURDATE(), 0.5, '{"intercept":-1.8,"debt_ratio":1.2,"interest_coverage":-0.4,"roe":-0.3,"current_ratio":-0.25,"log_asset":-0.1}', '0'),
('Merton结构化模型v1', 'MERTON', '1.0', 2000, 0.7500, 0.3800, 0.5000, CURDATE(), NULL, '{}', '0');

-- ========== 五级分类规则引擎 ==========
CREATE TABLE IF NOT EXISTS classification_rule (
  rule_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  rule_name VARCHAR(100) NOT NULL,
  rule_type VARCHAR(30) NOT NULL COMMENT 'OVERDUE_DAYS/PAYMENT_STATUS/DEBT_RATIO/COMBINED',
  five_level VARCHAR(20) NOT NULL,
  priority INT DEFAULT 0,
  condition_json TEXT NOT NULL,
  description VARCHAR(500),
  status CHAR(1) DEFAULT '1',
  create_by VARCHAR(64) DEFAULT '',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_by VARCHAR(64) DEFAULT '',
  update_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB COMMENT='五级分类规则';

CREATE TABLE IF NOT EXISTS classification_result (
  result_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  loan_id BIGINT,
  company_id BIGINT,
  company_name VARCHAR(200),
  classify_date DATE NOT NULL,
  five_level VARCHAR(20) NOT NULL,
  previous_level VARCHAR(20),
  overdue_days INT,
  outstanding_balance DECIMAL(18,2),
  trigger_rule VARCHAR(200),
  score DECIMAL(8,2),
  remark VARCHAR(500),
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_loan (loan_id),
  INDEX idx_date (classify_date),
  INDEX idx_level (five_level)
) ENGINE=InnoDB COMMENT='五级分类结果';

CREATE TABLE IF NOT EXISTS classification_migration (
  migration_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  loan_id BIGINT,
  company_name VARCHAR(200),
  from_level VARCHAR(20),
  to_level VARCHAR(20),
  migrate_date DATE,
  reason VARCHAR(500),
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_loan (loan_id),
  INDEX idx_date (migrate_date)
) ENGINE=InnoDB COMMENT='分类迁移记录';

INSERT INTO classification_rule (rule_name, rule_type, five_level, priority, condition_json, description) VALUES
('逾期0天-正常', 'OVERDUE_DAYS', '正常', 100, '{"operator":"<=","value":0}', '无逾期'),
('逾期1-30天-关注', 'OVERDUE_DAYS', '关注', 90, '{"operator":"between","min":1,"max":30}', '逾期1-30天'),
('逾期31-90天-次级', 'OVERDUE_DAYS', '次级', 80, '{"operator":"between","min":31,"max":90}', '逾期31-90天'),
('逾期91-180天-可疑', 'OVERDUE_DAYS', '可疑', 70, '{"operator":"between","min":91,"max":180}', '逾期91-180天'),
('逾期180天以上-损失', 'OVERDUE_DAYS', '损失', 60, '{"operator":">","value":180}', '逾期180天以上'),
('债务重组-关注', 'PAYMENT_STATUS', '关注', 95, '{"event":"restructuring"}', '企业申请债务重组'),
('欠息90天-次级', 'PAYMENT_STATUS', '次级', 85, '{"event":"interest_overdue_days","operator":">","value":90}', '欠息超90天'),
('资不抵债-可疑', 'DEBT_RATIO', '可疑', 75, '{"field":"debt_ratio","operator":">","value":100}', '资产负债率>100%');

-- ========== RWA风险权重配置 ==========
CREATE TABLE IF NOT EXISTS rwa_risk_weight (
  weight_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  asset_class VARCHAR(50) NOT NULL,
  asset_subclass VARCHAR(50),
  counterparty_type VARCHAR(50),
  rating_grade VARCHAR(10),
  risk_weight DECIMAL(6,2) NOT NULL,
  ccf DECIMAL(6,2) DEFAULT 100,
  regulation_ref VARCHAR(200),
  status CHAR(1) DEFAULT '1',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_class (asset_class)
) ENGINE=InnoDB COMMENT='RWA风险权重配置';

INSERT INTO rwa_risk_weight (asset_class, asset_subclass, counterparty_type, rating_grade, risk_weight, ccf, regulation_ref) VALUES
('主权','主权','主权','AAA',0,100,'巴塞尔III'),('主权','主权','主权','AA',20,100,'巴塞尔III'),('主权','主权','主权','A',50,100,'巴塞尔III'),('主权','主权','主权','BBB',100,100,'巴塞尔III'),('主权','主权','主权','B',150,100,'巴塞尔III'),
('金融机构','银行','商业银行','AAA',20,100,'银保监会'),('金融机构','银行','商业银行','AA',50,100,'银保监会'),('金融机构','银行','商业银行','A',100,100,'银保监会'),('金融机构','银行','商业银行','BBB',100,100,'银保监会'),
('企业','一般企业','工商企业','AAA',20,100,'巴塞尔III'),('企业','一般企业','工商企业','AA',50,100,'巴塞尔III'),('企业','一般企业','工商企业','A',100,100,'巴塞尔III'),('企业','一般企业','工商企业','BBB',100,100,'巴塞尔III'),('企业','一般企业','工商企业','无评级',100,100,'巴塞尔III'),
('企业','小微企业','小微企业',NULL,75,100,'银保监会普惠'),('零售','个人住房','自然人',NULL,50,100,'巴塞尔III'),('零售','个人消费','自然人',NULL,75,100,'巴塞尔III'),('零售','信用卡','自然人',NULL,75,100,'巴塞尔III'),
('房地产','商用房','房地产',NULL,150,100,'巴塞尔III'),('表外','贷款承诺','各类','<=1年',20,20,'巴塞尔III'),('表外','贷款承诺','各类','>1年',20,50,'巴塞尔III'),('表外','银行承兑汇票','各类',NULL,100,100,'银保监会'),('表外','保函','各类',NULL,100,100,'银保监会');

-- ========== ECL阶段配置 ==========
CREATE TABLE IF NOT EXISTS ecl_stage_config (
  config_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  stage_name VARCHAR(20) NOT NULL,
  stage_number INT NOT NULL,
  trigger_condition TEXT NOT NULL,
  ecl_method VARCHAR(30) NOT NULL COMMENT '12M/LIFETIME',
  description VARCHAR(500),
  status CHAR(1) DEFAULT '1',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB COMMENT='ECL阶段配置';

INSERT INTO ecl_stage_config (stage_name, stage_number, trigger_condition, ecl_method, description) VALUES
('第一阶段',1,'{"credit_risk_not_increased":true}','12M','信用风险未显著增加，12个月ECL'),
('第二阶段',2,'{"credit_risk_increased":true}','LIFETIME','信用风险显著增加，存续期ECL'),
('第三阶段',3,'{"impaired":true}','LIFETIME','已减值，存续期ECL');

-- ========== 压力测试 ==========
CREATE TABLE IF NOT EXISTS stress_test_scenario (
  scenario_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  scenario_name VARCHAR(100) NOT NULL,
  scenario_type VARCHAR(30) NOT NULL COMMENT 'HISTORICAL/HYPOTHETICAL/REGULATORY',
  severity VARCHAR(20) COMMENT 'MILD/MODERATE/SEVERE',
  description VARCHAR(500),
  parameters_json TEXT,
  status CHAR(1) DEFAULT '1',
  create_by VARCHAR(64) DEFAULT '',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_by VARCHAR(64) DEFAULT '',
  update_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB COMMENT='压力测试场景';

CREATE TABLE IF NOT EXISTS stress_test_result (
  result_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  scenario_id BIGINT NOT NULL,
  test_date DATE NOT NULL,
  portfolio_type VARCHAR(50),
  baseline_loss DECIMAL(18,2),
  stressed_loss DECIMAL(18,2),
  loss_increase DECIMAL(18,2),
  loss_increase_rate DECIMAL(8,4),
  capital_impact DECIMAL(18,2),
  capital_ratio_after DECIMAL(8,4),
  pass_fail VARCHAR(10),
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_scenario (scenario_id),
  INDEX idx_date (test_date)
) ENGINE=InnoDB COMMENT='压力测试结果';

CREATE TABLE IF NOT EXISTS stress_test_factor (
  factor_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  scenario_id BIGINT NOT NULL,
  factor_name VARCHAR(100) NOT NULL,
  factor_type VARCHAR(30),
  baseline_value DECIMAL(12,4),
  shocked_value DECIMAL(12,4),
  shock_magnitude DECIMAL(8,4),
  INDEX idx_scenario (scenario_id)
) ENGINE=InnoDB COMMENT='压力测试冲击因子';

INSERT INTO stress_test_scenario (scenario_name, scenario_type, severity, description, parameters_json) VALUES
('GDP增速下滑','HISTORICAL','MILD','GDP增速下降2个百分点','{"gdp_shock":-2,"duration_quarters":4}'),
('GDP大幅下滑','HISTORICAL','MODERATE','GDP增速下降5个百分点','{"gdp_shock":-5,"duration_quarters":4}'),
('经济衰退','HISTORICAL','SEVERE','GDP负增长8%','{"gdp_shock":-8,"duration_quarters":8}'),
('利率上升','HYPOTHETICAL','MILD','基准利率上升100bp','{"rate_shock":100}'),
('利率大幅上升','HYPOTHETICAL','MODERATE','基准利率上升300bp','{"rate_shock":300}'),
('房价下跌','HISTORICAL','MODERATE','房价下跌20%','{"house_price_shock":-20}'),
('房价崩盘','HISTORICAL','SEVERE','房价下跌40%','{"house_price_shock":-40}'),
('银保监会-轻度','REGULATORY','MILD','年度轻度压力测试','{"gdp_shock":-1.5,"rate_shock":50,"unemployment_shock":1}'),
('银保监会-重度','REGULATORY','SEVERE','年度重度压力测试','{"gdp_shock":-5,"rate_shock":200,"unemployment_shock":5}');

-- ========== VaR计算记录 ==========
CREATE TABLE IF NOT EXISTS var_calculation (
  var_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  calc_date DATE NOT NULL,
  portfolio_type VARCHAR(50),
  method VARCHAR(30) NOT NULL COMMENT 'HISTORICAL/MONTE_CARLO/PARAMETRIC',
  confidence_level DECIMAL(5,2) NOT NULL,
  time_horizon INT NOT NULL,
  var_amount DECIMAL(18,2) NOT NULL,
  cvar_amount DECIMAL(18,2),
  mean_return DECIMAL(12,6),
  volatility DECIMAL(12,6),
  skewness DECIMAL(8,4),
  kurtosis DECIMAL(8,4),
  sample_size INT,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_date (calc_date),
  INDEX idx_method (method)
) ENGINE=InnoDB COMMENT='VaR计算记录';

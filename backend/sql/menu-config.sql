-- =============================================
-- 风控管理系统 - 菜单配置
-- =============================================

-- 顶级菜单：信贷管理
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time) VALUES
('信贷管理', 0, 1, 'loan', NULL, 'M', '0', '0', '', 'form', 'admin', NOW());

SET @loan_menu_id = LAST_INSERT_ID();

-- 信贷管理子菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time) VALUES
('征信查询', @loan_menu_id, 1, 'credit', 'loan/credit/index', 'C', '0', '0', 'loan:credit:list', 'form', 'admin', NOW()),
('授信审批', @loan_menu_id, 2, 'approval', 'loan/application/index', 'C', '0', '0', 'loan:approval:list', 'form', 'admin', NOW()),
('授信额度', @loan_menu_id, 3, 'limit', 'loan/score/index', 'C', '0', '0', 'loan:limit:list', 'form', 'admin', NOW()),
('放款管理', @loan_menu_id, 4, 'disburse', 'loan/disburse/index', 'C', '0', '0', 'loan:disburse:list', 'form', 'admin', NOW()),
('利息计提', @loan_menu_id, 5, 'interest', 'loan/interest/index', 'C', '0', '0', 'loan:interest:list', 'form', 'admin', NOW()),
('利率调整', @loan_menu_id, 6, 'rate', 'loan/ledger/index', 'C', '0', '0', 'loan:rate:list', 'form', 'admin', NOW()),
('催收管理', @loan_menu_id, 7, 'collection', 'loan/collection/index', 'C', '0', '0', 'loan:collection:list', 'form', 'admin', NOW()),
('资产保全', @loan_menu_id, 8, 'asset', 'loan/asset/index', 'C', '0', '0', 'loan:asset:list', 'form', 'admin', NOW()),
('贷款核销', @loan_menu_id, 9, 'writeoff', 'loan/writeoff/index', 'C', '0', '0', 'loan:writeoff:list', 'form', 'admin', NOW());

-- 顶级菜单：资本计量
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time) VALUES
('资本计量', 0, 2, 'capital', NULL, 'M', '0', '0', '', 'chart', 'admin', NOW());

SET @capital_menu_id = LAST_INSERT_ID();

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time) VALUES
('资本概况', @capital_menu_id, 1, 'overview', 'capital/overview/index', 'C', '0', '0', 'capital:overview:list', 'dashboard', 'admin', NOW()),
('RWA计算', @capital_menu_id, 2, 'rwa', 'capital/rwa/index', 'C', '0', '0', 'capital:rwa:list', 'chart', 'admin', NOW()),
('ECL预期损失', @capital_menu_id, 3, 'ecl', 'capital/ecl/index', 'C', '0', '0', 'capital:ecl:list', 'warning', 'admin', NOW()),
('资本充足率', @capital_menu_id, 4, 'adequacy', 'capital/adequacy/index', 'C', '0', '0', 'capital:adequacy:list', 'form', 'admin', NOW()),
('拨备计提', @capital_menu_id, 5, 'provision', 'capital/provision/index', 'C', '0', '0', 'capital:provision:list', 'money', 'admin', NOW());

-- 顶级菜单：监管报表
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time) VALUES
('监管报表', 0, 3, 'regulatory', NULL, 'M', '0', '0', '', 'documentation', 'admin', NOW());

SET @regulatory_menu_id = LAST_INSERT_ID();

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time) VALUES
('监管概况', @regulatory_menu_id, 1, 'overview', 'regulatory/overview/index', 'C', '0', '0', 'regulatory:overview:list', 'dashboard', 'admin', NOW()),
('1104报表', @regulatory_menu_id, 2, 'report1104', 'regulatory/report1104/index', 'C', '0', '0', 'regulatory:report1104:list', 'documentation', 'admin', NOW()),
('EAST报送', @regulatory_menu_id, 3, 'east', 'regulatory/east/index', 'C', '0', '0', 'regulatory:east:list', 'upload', 'admin', NOW()),
('征信报送', @regulatory_menu_id, 4, 'credit', 'regulatory/credit/index', 'C', '0', '0', 'regulatory:credit:list', 'form', 'admin', NOW()),
('监管指标', @regulatory_menu_id, 5, 'indicator', 'regulatory/indicator/index', 'C', '0', '0', 'regulatory:indicator:list', 'chart', 'admin', NOW());

-- 顶级菜单：风险计量
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time) VALUES
('风险计量', 0, 4, 'model', NULL, 'M', '0', '0', '', 'chart', 'admin', NOW());

SET @model_menu_id = LAST_INSERT_ID();

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time) VALUES
('PD违约概率', @model_menu_id, 1, 'pd', 'model/pd/index', 'C', '0', '0', 'model:pd:list', 'chart', 'admin', NOW()),
('五级分类', @model_menu_id, 2, 'classification', 'model/classification/index', 'C', '0', '0', 'model:classification:list', 'form', 'admin', NOW()),
('压力测试', @model_menu_id, 3, 'stress', 'model/stress/index', 'C', '0', '0', 'model:stress:list', 'warning', 'admin', NOW()),
('VaR风险价值', @model_menu_id, 4, 'var', 'model/var/index', 'C', '0', '0', 'model:var:list', 'money', 'admin', NOW());

-- 验证
SELECT '菜单配置完成' AS status;
SELECT menu_id, menu_name, parent_id, path FROM sys_menu WHERE parent_id = 0 AND menu_id > 4 ORDER BY menu_id;

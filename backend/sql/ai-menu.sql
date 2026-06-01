-- AI助手菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time) VALUES
(3000, '智能助手', 0, 5, 'ai', NULL, 'M', '0', '0', '', 'cpu', 'admin', NOW());

SET @ai_menu_id = LAST_INSERT_ID();

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time) VALUES
(3001, 'AI风控助手', 3000, 1, 'assistant', 'ai/assistant/index', 'C', '0', '0', 'ai:assistant:list', 'chat', 'admin', NOW());

-- 权限
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 3000), (1, 3001);

SELECT 'AI菜单配置完成' AS status;

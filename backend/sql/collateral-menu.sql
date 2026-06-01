-- 押品识别菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time) VALUES
(3003, '押品识别', 3000, 3, 'collateral', 'ai/collateral/index', 'C', '0', '0', 'ai:collateral:list', 'camera', 'admin', NOW());

-- 权限
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 3003);

SELECT '押品识别菜单配置完成' AS status;

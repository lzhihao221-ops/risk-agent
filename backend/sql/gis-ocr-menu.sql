-- OCR和GIS菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time) VALUES
(3002, 'OCR识别', 3000, 2, 'ocr', 'ai/ocr/index', 'C', '0', '0', 'ai:ocr:list', 'upload', 'admin', NOW()),
(4000, 'GIS地图', 0, 6, 'gis', NULL, 'M', '0', '0', '', 'location', 'admin', NOW()),
(4001, 'GIS地图', 4000, 1, 'map', 'gis/map/index', 'C', '0', '0', 'gis:map:list', 'location', 'admin', NOW());

-- 权限
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 3002), (1, 4000), (1, 4001);

SELECT 'OCR和GIS菜单配置完成' AS status;

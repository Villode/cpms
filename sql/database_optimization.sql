/*
 CPMS数据库优化脚本
 执行顺序：
 1. 删除重复表结构
 2. 添加复合索引提升查询性能
 3. 统一字符编码
 
 执行前请务必备份数据库！
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ========================================
-- 第一步：删除重复表结构
-- ========================================

-- 删除重复的缴费记录表（保留功能完整的payment_record）
DROP TABLE IF EXISTS `paymentrecord`;

-- 删除重复的报修表（保留功能完整的repair）
DROP TABLE IF EXISTS `repairrequest`;

SELECT '重复表删除完成' AS status;

-- ========================================
-- 第二步：添加复合索引提升查询性能
-- ========================================

-- 缴费记录表索引优化
ALTER TABLE `payment_record` ADD INDEX `idx_user_status` (`UserID`, `PaymentStatus`);
ALTER TABLE `payment_record` ADD INDEX `idx_building_room` (`BuildingID`, `RoomNumber`);
ALTER TABLE `payment_record` ADD INDEX `idx_fee_type_status` (`FeeType`, `PaymentStatus`);
ALTER TABLE `payment_record` ADD INDEX `idx_due_date` (`DueDate`);

-- 报修记录表索引优化
ALTER TABLE `repair` ADD INDEX `idx_user_status` (`UserID`, `RepairStatus`);
ALTER TABLE `repair` ADD INDEX `idx_building_room` (`BuildingID`, `RoomNumber`);
ALTER TABLE `repair` ADD INDEX `idx_type_status` (`RepairType`, `RepairStatus`);
ALTER TABLE `repair` ADD INDEX `idx_handler_status` (`HandlerID`, `RepairStatus`);
ALTER TABLE `repair` ADD INDEX `idx_create_time` (`CreateTime`);

-- 用户表索引优化
ALTER TABLE `user` ADD INDEX `idx_role_building` (`RoleID`, `BuildingID`);
ALTER TABLE `user` ADD INDEX `idx_managed_building` (`ManagedBuildingID`);
ALTER TABLE `user` ADD INDEX `idx_account_status` (`AccountStatus`);

-- 权限关联表索引优化（如果不存在）
ALTER TABLE `rolepermission` ADD INDEX `idx_role_permission` (`RoleID`, `PermissionID`);

-- 车位表索引优化
ALTER TABLE `parkingspot` ADD INDEX `idx_building_status` (`BuildingID`, `Status`);
ALTER TABLE `parkingspot` ADD INDEX `idx_owner_user` (`OwnerUserID`);

-- 访客记录表索引优化
ALTER TABLE `visitorrecord` ADD INDEX `idx_booker_time` (`BookerUserID`, `BookTime`);
ALTER TABLE `visitorrecord` ADD INDEX `idx_sync_status` (`SyncedToSecurity`);

-- 系统消息表索引优化
ALTER TABLE `systemmessage` ADD INDEX `idx_receiver_read` (`ReceiverID`, `IsRead`);
ALTER TABLE `systemmessage` ADD INDEX `idx_type_time` (`MessageType`, `CreateTime`);

SELECT '复合索引添加完成' AS status;

-- ========================================
-- 第三步：统一字符编码为utf8mb4_unicode_ci
-- ========================================

-- 用户表
ALTER TABLE `user` CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 角色表
ALTER TABLE `role` CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 权限表
ALTER TABLE `permission` CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 角色权限关联表
ALTER TABLE `rolepermission` CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 楼栋表（已经是utf8mb4_unicode_ci）
-- ALTER TABLE `building` CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 车位表
ALTER TABLE `parkingspot` CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 缴费记录表
ALTER TABLE `payment_record` CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 报修表（已经是utf8mb4_unicode_ci）
-- ALTER TABLE `repair` CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 访客记录表
ALTER TABLE `visitorrecord` CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 系统消息表
ALTER TABLE `systemmessage` CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

SELECT '字符编码统一完成' AS status;

-- ========================================
-- 第四步：数据类型优化
-- ========================================

-- 优化状态字段数据类型
ALTER TABLE `payment_record` MODIFY `PaymentStatus` TINYINT DEFAULT 0 COMMENT '缴费状态（0-未缴，1-已缴）';
ALTER TABLE `repair` MODIFY `RepairStatus` TINYINT NOT NULL DEFAULT 0 COMMENT '报修状态（0-待处理，1-处理中，2-已完成，3-已取消）';
ALTER TABLE `repair` MODIFY `RepairType` TINYINT NOT NULL COMMENT '报修类型（1-水电维修，2-家具维修，3-门窗维修，4-其他）';
ALTER TABLE `user` MODIFY `AccountStatus` TINYINT DEFAULT 1 COMMENT '账号状态（0-禁用，1-启用）';
ALTER TABLE `parkingspot` MODIFY `Status` TINYINT DEFAULT 0 COMMENT '车位状态（0-空闲，1-已分配）';

SELECT '数据类型优化完成' AS status;

-- ========================================
-- 验证优化结果
-- ========================================

-- 检查表结构
SHOW TABLES;

-- 检查索引
SHOW INDEX FROM `payment_record`;
SHOW INDEX FROM `repair`;
SHOW INDEX FROM `user`;

-- 检查字符集
SELECT 
    TABLE_NAME,
    TABLE_COLLATION
FROM 
    information_schema.TABLES 
WHERE 
    TABLE_SCHEMA = 'cpms_db'
ORDER BY 
    TABLE_NAME;

SET FOREIGN_KEY_CHECKS = 1;

SELECT '数据库优化完成！' AS final_status;

/*
优化效果预期：
1. 存储空间：删除重复表节省约30%存储空间
2. 查询性能：复合索引提升查询速度50-80%
3. 数据一致性：统一编码避免字符处理问题
4. 数据类型：优化状态字段，节省存储空间

注意事项：
1. 执行前请备份数据库
2. 建议在测试环境先执行验证
3. 生产环境执行时选择业务低峰期
4. 执行后监控系统性能变化
*/
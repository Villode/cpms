-- 为user表添加RoomNumber字段
-- 此脚本用于添加房间号字段到用户表，支持业主房间信息管理

-- 检查字段是否已存在，不存在则添加
-- 注意：MySQL的ALTER TABLE ADD COLUMN不支持IF NOT EXISTS语法
-- 如果字段已存在，执行时会报错，这是正常的

-- 方法1：直接添加字段（如果字段已存在会报错）
ALTER TABLE user
ADD COLUMN RoomNumber VARCHAR(20) COMMENT '房间号' AFTER BuildingID;

-- 方法2：使用动态SQL检查字段是否存在（推荐）
/*
SET @exists = (
    SELECT COUNT(*)
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'user'
    AND COLUMN_NAME = 'RoomNumber'
);

SET @query = IF(@exists = 0, 
    'ALTER TABLE user ADD COLUMN RoomNumber VARCHAR(20) COMMENT "房间号" AFTER BuildingID', 
    'SELECT "RoomNumber column already exists" AS message'
);

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
*/

-- 为现有的业主用户更新房间号（示例数据）
-- 注意：实际使用时需要根据具体情况调整房间号
UPDATE user SET RoomNumber = '101' WHERE UserID = 10 AND RoleID = 3; -- owner1
UPDATE user SET RoomNumber = '301' WHERE UserID = 11 AND RoleID = 3; -- owner2  
UPDATE user SET RoomNumber = '102' WHERE UserID = 12 AND RoleID = 3; -- owner3
UPDATE user SET RoomNumber = '401' WHERE UserID = 13 AND RoleID = 3; -- owner4

-- 更新日期: 2025-01-15
-- 说明: 此脚本用于为user表添加RoomNumber字段，支持业主房间信息管理功能
-- 注意: 执行前请备份数据库
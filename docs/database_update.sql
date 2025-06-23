-- 数据库更新脚本
-- 添加 ImagePath 字段到 repair 表

-- 检查字段是否已存在，不存在则添加
ALTER TABLE repair
ADD COLUMN IF NOT EXISTS ImagePath VARCHAR(255) COMMENT '故障图片路径' AFTER RepairDesc;

-- 如果数据库不支持 IF NOT EXISTS，可以使用以下替代方案
-- 先检查字段是否存在，不存在则添加
/*
SET @exists = (
    SELECT COUNT(*)
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'repair'
    AND COLUMN_NAME = 'ImagePath'
);

SET @query = IF(@exists = 0, 
    'ALTER TABLE repair ADD COLUMN ImagePath VARCHAR(255) COMMENT "故障图片路径" AFTER RepairDesc', 
    'SELECT "ImagePath column already exists"'
);

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
*/

-- 更新日期: 2023-11-30
-- 说明: 此脚本用于更新数据库结构，添加图片上传功能所需的字段 
-- 修复管家约束问题
-- 首先删除外键约束
ALTER TABLE building DROP FOREIGN KEY building_ibfk_1;

-- 然后删除唯一约束
ALTER TABLE building DROP INDEX ManagerID;

-- 最后重新添加外键约束（但不添加唯一约束）
ALTER TABLE building ADD CONSTRAINT building_ibfk_1 FOREIGN KEY (ManagerID) REFERENCES user (UserID); 
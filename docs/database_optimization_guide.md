# CPMS数据库优化执行指南

## 📋 优化概述

本次优化主要包含三个方面：
1. **存储优化**：删除重复表，节省约30%存储空间
2. **查询性能**：复合索引提升查询速度50-80%
3. **数据一致性**：统一编码避免字符处理问题

## ⚠️ 执行前准备

### 1. 数据库备份
```sql
-- 创建完整备份
mysqldump -u root -p cpms_db > cpms_db_backup_$(date +%Y%m%d_%H%M%S).sql

-- 或使用MySQL Workbench/Navicat等工具进行备份
```

### 2. 确认当前表结构
```sql
-- 查看所有表
SHOW TABLES;

-- 确认重复表存在
DESC paymentrecord;
DESC repairrequest;
```

### 3. 检查数据依赖
```sql
-- 检查重复表是否有数据
SELECT COUNT(*) FROM paymentrecord;
SELECT COUNT(*) FROM repairrequest;

-- 检查是否有外键依赖
SELECT 
    TABLE_NAME,
    COLUMN_NAME,
    CONSTRAINT_NAME,
    REFERENCED_TABLE_NAME,
    REFERENCED_COLUMN_NAME
FROM 
    information_schema.KEY_COLUMN_USAGE
WHERE 
    REFERENCED_TABLE_SCHEMA = 'cpms_db'
    AND (REFERENCED_TABLE_NAME = 'paymentrecord' OR REFERENCED_TABLE_NAME = 'repairrequest');
```

## 🚀 执行步骤

### 步骤1：执行优化脚本

```bash
# 方式1：命令行执行
mysql -u root -p cpms_db < sql/database_optimization.sql

# 方式2：MySQL客户端执行
# 在MySQL Workbench或Navicat中打开并执行 sql/database_optimization.sql
```

### 步骤2：验证优化结果

#### 检查表结构
```sql
-- 确认重复表已删除
SHOW TABLES;
-- 应该看不到 paymentrecord 和 repairrequest
```

#### 检查索引
```sql
-- 查看新增的复合索引
SHOW INDEX FROM payment_record;
SHOW INDEX FROM repair;
SHOW INDEX FROM user;
```

#### 检查字符编码
```sql
-- 确认所有表都使用 utf8mb4_unicode_ci
SELECT 
    TABLE_NAME,
    TABLE_COLLATION
FROM 
    information_schema.TABLES 
WHERE 
    TABLE_SCHEMA = 'cpms_db'
ORDER BY 
    TABLE_NAME;
```

#### 检查数据类型
```sql
-- 确认状态字段已优化为TINYINT
DESC payment_record;
DESC repair;
DESC user;
```

### 步骤3：应用程序测试

1. **启动CPMS应用程序**
   ```bash
   # 在IDEA中运行 Application.java
   # 或使用命令行
   cd e:\Project_Dev\CPMS_trae\CPMS_PRO
   java -cp "lib/*;out/production/CPMS_PRO" com.cpms.main.Application
   ```

2. **功能测试清单**
   - [ ] 用户登录功能
   - [ ] 缴费记录查询
   - [ ] 报修记录管理
   - [ ] 用户管理功能
   - [ ] 权限验证
   - [ ] 车位管理
   - [ ] 访客记录

3. **性能测试**
   - [ ] 缴费记录查询速度
   - [ ] 报修记录筛选速度
   - [ ] 用户列表加载速度
   - [ ] 权限验证响应时间

## 📊 优化效果监控

### 1. 存储空间对比
```sql
-- 查看表大小
SELECT 
    TABLE_NAME,
    ROUND(((DATA_LENGTH + INDEX_LENGTH) / 1024 / 1024), 2) AS 'Size(MB)'
FROM 
    information_schema.TABLES
WHERE 
    TABLE_SCHEMA = 'cpms_db'
ORDER BY 
    (DATA_LENGTH + INDEX_LENGTH) DESC;
```

### 2. 查询性能对比
```sql
-- 测试复合索引效果
EXPLAIN SELECT * FROM payment_record WHERE UserID = 3 AND PaymentStatus = 0;
EXPLAIN SELECT * FROM repair WHERE UserID = 3 AND RepairStatus = 1;
EXPLAIN SELECT * FROM user WHERE RoleID = 2 AND BuildingID = 1;
```

### 3. 字符编码验证
```sql
-- 测试中文字符处理
SELECT * FROM user WHERE RealName LIKE '%张%';
SELECT * FROM building WHERE BuildingName LIKE '%栋%';
```

## 🔄 回滚方案

如果优化后出现问题，可以使用备份进行回滚：

```bash
# 停止应用程序
# 删除当前数据库
mysql -u root -p -e "DROP DATABASE cpms_db;"

# 重新创建数据库
mysql -u root -p -e "CREATE DATABASE cpms_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# 恢复备份
mysql -u root -p cpms_db < cpms_db_backup_YYYYMMDD_HHMMSS.sql
```

## 📈 预期优化效果

| 优化项目 | 优化前 | 优化后 | 提升幅度 |
|---------|--------|--------|----------|
| 存储空间 | 100% | 70% | 节省30% |
| 查询速度 | 基准 | 1.5-2倍 | 提升50-80% |
| 字符处理 | 不一致 | 统一 | 避免乱码 |
| 数据类型 | 冗余 | 优化 | 节省存储 |

## 🛠️ 故障排除

### 常见问题

1. **外键约束错误**
   ```sql
   -- 临时禁用外键检查
   SET FOREIGN_KEY_CHECKS = 0;
   -- 执行优化脚本
   -- 重新启用外键检查
   SET FOREIGN_KEY_CHECKS = 1;
   ```

2. **索引创建失败**
   ```sql
   -- 检查是否已存在同名索引
   SHOW INDEX FROM table_name;
   -- 删除冲突索引后重新创建
   ```

3. **字符编码转换问题**
   ```sql
   -- 检查当前编码
   SHOW CREATE TABLE table_name;
   -- 手动转换有问题的表
   ALTER TABLE table_name CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

### 联系支持

如果遇到无法解决的问题：
1. 保留错误日志
2. 记录执行到的步骤
3. 准备数据库备份
4. 联系技术支持

---

**重要提醒**：
- ✅ 务必在测试环境先执行
- ✅ 生产环境选择业务低峰期执行
- ✅ 执行前完整备份数据库
- ✅ 执行后进行全面功能测试
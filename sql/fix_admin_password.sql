-- 添加特殊处理，直接更新管理员密码为MD5格式
UPDATE User SET Password = '0192023a7bbd73250516f069df18b500' WHERE Username = 'admin';
-- 这是admin123的MD5哈希值

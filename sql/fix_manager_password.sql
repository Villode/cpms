-- 更新管家账号密码为MD5格式
UPDATE User SET Password = 'e10adc3949ba59abbe56e057f20f883e' WHERE Username = '002' AND RoleID = 2;
-- 这是123456的MD5哈希值

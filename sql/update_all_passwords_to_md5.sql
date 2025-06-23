-- 更新所有用户密码为MD5格式
-- 将所有用户密码更新为123456的MD5哈希值
UPDATE User SET Password = 'e10adc3949ba59abbe56e057f20f883e' WHERE Password != 'e10adc3949ba59abbe56e057f20f883e' AND Password != '0192023a7bbd73250516f069df18b500';

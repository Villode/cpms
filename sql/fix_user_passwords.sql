-- 更新用户密码为MD5格式
-- 这个脚本将用户密码更新为MD5格式，以解决登录问题

-- 更新admin用户密码为"admin123"
UPDATE User SET Password = '0192023a7bbd73250516f069df18b500' WHERE Username = 'admin';

-- 更新其他用户密码为"123456"
UPDATE User SET Password = 'e10adc3949ba59abbe56e057f20f883e' WHERE Username != 'admin';

-- 确保所有用户账号都是启用状态
UPDATE User SET AccountStatus = 1;

-- 密码对照表（供参考）：
-- admin123 -> 0192023a7bbd73250516f069df18b500
-- 123456 -> e10adc3949ba59abbe56e057f20f883e
-- password -> 5f4dcc3b5aa765d61d8327deb882cf99
-- 111111 -> 96e79218965eb72c92a549dd5a330112
-- 000000 -> 670b14728ad9902aecba32e22fa4f6bd

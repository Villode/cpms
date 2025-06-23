# CPMS 系统启动指南

## 统一入口点

系统已优化为使用统一的入口点启动：

### 主入口类
- **类名**: `com.cpms.main.Application`
- **位置**: `src/com/cpms/main/Application.java`
- **功能**: 系统唯一启动入口

### 启动方式

#### 1. IDE 中启动
在 IntelliJ IDEA 中：
1. 打开项目
2. 找到 `src/com/cpms/main/Application.java`
3. 右键点击文件 → Run 'Application.main()'

#### 2. 命令行启动
```bash
# 编译项目
javac -cp "lib/*" -d out src/com/cpms/main/Application.java src/com/cpms/**/*.java

# 运行项目
java -cp "out:lib/*" com.cpms.main.Application
```

### 系统初始化流程

启动时系统会自动执行以下初始化步骤：

1. **设置界面外观** - 使用系统默认外观
2. **加载配置文件** - 读取 `resources/config/application.properties`
3. **设置全局字体** - 根据配置设置统一字体
4. **初始化系统数据** - 创建默认管理员账号等
5. **显示登录界面** - 启动用户登录界面

### 默认管理员账号

系统首次启动时会自动创建默认管理员账号：
- **用户名**: admin
- **密码**: admin123
- **角色**: 管理员

### 配置说明

系统配置文件位于 `resources/config/application.properties`，包含：
- 数据库连接配置
- 界面主题和字体配置
- 系统功能开关配置

### 注意事项

1. 确保 MySQL 数据库服务已启动
2. 确保数据库连接配置正确
3. 首次启动前请导入数据库脚本 `sql/cpms_db.sql`
4. 如需修改配置，请编辑 `application.properties` 文件后重启系统

### 故障排除

如果启动失败，请检查：
1. 数据库连接是否正常
2. 配置文件是否存在且格式正确
3. 依赖库是否完整（mysql-connector-java-8.0.28.jar）
4. 控制台错误信息

---

**更新日期**: 2024年
**版本**: v1.0.0
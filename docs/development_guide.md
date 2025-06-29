# 小区物业管理系统开发指南

## 一、项目概述

小区物业管理系统是一个基于Java Swing开发的桌面应用程序，采用MVC架构设计，用于管理小区物业相关业务，包括用户管理、楼栋管理、报修管理、访客管理、缴费管理和车位管理等功能。

系统支持三种角色：管理员、物业管家和业主，每种角色拥有不同的权限和功能。

## 二、技术架构

### 1. 开发环境

- 编程语言：Java
- 界面框架：Java Swing
- 数据库：MySQL
- 架构模式：MVC（Model-View-Controller）

### 2. 项目结构

```
src/
├── com/
│   └── cpms/
│       ├── controller/      # 控制器层，处理业务逻辑
│       │   ├── admin/       # 管理员相关控制器
│       │   ├── common/      # 公共控制器
│       │   ├── manager/     # 物业管家相关控制器
│       │   └── owner/       # 业主相关控制器
│       ├── model/           # 模型层，处理数据访问
│       │   ├── dao/         # 数据访问对象
│       │   └── entity/      # 实体类
│       ├── util/            # 工具类
│       │   ├── auth/        # 认证相关工具
│       │   ├── config/      # 配置管理工具
│       │   ├── db/          # 数据库工具
│       │   ├── export/      # 数据导出工具
│       │   └── security/    # 安全相关工具
│       ├── view/            # 视图层，用户界面
│       │   ├── admin/       # 管理员界面
│       │   ├── common/      # 公共界面组件
│       │   ├── manager/     # 物业管家界面
│       │   └── owner/       # 业主界面
│       ├── main/            # 主程序
│       └── Main.java        # 程序入口
```

### 3. 数据库结构

系统使用MySQL数据库，主要包含以下表：

- `Role`：角色表
- `User`：用户表
- `Building`：楼栋表
- `RepairRequest`：报修表
- `VisitorRecord`：访客记录表
- `PaymentRecord`：缴费记录表
- `ParkingSpot`：车位表

## 三、已完成功能

### 1. 基础框架

- [x] MVC架构搭建
- [x] 数据库连接工具类
- [x] 配置管理工具类
- [x] 安全工具类（MD5加密）

### 2. 实体类

- [x] User（用户）
- [x] Role（角色）
- [x] Building（楼栋）
- [x] Repair（报修）
- [x] Permission（权限）
- [x] RolePermission（角色权限）
- [x] VisitorRecord（访客记录）
- [x] PaymentRecord（缴费记录）
- [x] ParkingSpot（车位）

### 3. 数据访问对象（DAO）

- [x] UserDAO
- [x] RoleDAO
- [x] BuildingDAO
- [x] RepairDAO
- [x] PermissionDAO
- [x] RolePermissionDAO
- [x] VisitorRecordDAO
- [x] PaymentRecordDAO
- [x] ParkingSpotDAO

### 4. 控制器

- [x] LoginController（登录控制器）

### 5. 视图

- [x] LoginFrame（登录界面）

## 四、待开发功能

### 1. 管理员角色功能

- [ ] 角色创建与编辑
  - [ ] 新增角色（管理员/管家/业主）
  - [ ] 设置角色名称和描述
  - [ ] 分配操作权限

- [ ] 管理员账号创建
  - [ ] 初始化超级管理员账号
  - [ ] 支持密码修改与安全设置

- [ ] 管家账号分配与楼栋绑定
  - [ ] 创建管家账号
  - [ ] 指定其负责的楼栋
  - [ ] 分配角色权限

- [ ] 角色权限分配
  - [ ] 为不同角色分配操作权限

- [ ] 楼栋信息维护
  - [ ] 录入楼栋名称、位置
  - [ ] 分配专属管家
  - [ ] 支持批量导入楼栋数据

- [ ] 楼栋管家调整
  - [ ] 重新分配楼栋管家
  - [ ] 系统自动同步管家的数据访问范围

- [ ] 全小区数据导出
  - [ ] 导出全小区业主、报修、访客、缴费记录
  - [ ] 支持按时间范围筛选

### 2. 物业管家角色功能

- [ ] 本楼栋业主录入
  - [ ] 录入本楼栋业主信息
  - [ ] 生成初始账号
  - [ ] 自动关联楼栋

- [ ] 业主资料编辑与查询
  - [ ] 修改本楼栋业主资料
  - [ ] 按姓名/楼栋筛选业主列表
  - [ ] 查看业主关联的报修/缴费记录

- [ ] 访客报备同步
  - [ ] 查看本楼栋业主的访客报备记录
  - [ ] 点击"同步安保"按钮

- [ ] 当日报备清单导出
  - [ ] 导出本楼栋当日报备清单

- [ ] 本楼栋报修处理
  - [ ] 查看待处理报修单
  - [ ] 更新状态
  - [ ] 记录处理人
  - [ ] 填写完成时间

- [ ] 报修记录查询
  - [ ] 按状态/时间查询本楼栋报修历史
  - [ ] 导出报修明细



- [ ] 本楼栋车位录入
  - [ ] 录入车位编号、位置
  - [ ] 分配给本楼栋业主
  - [ ] 标记状态

- [ ] 车位状态更新
  - [ ] 修改车位车牌号
  - [ ] 更新使用状态

### 3. 业主角色功能

- [ ] 资料修改
  - [ ] 修改个人电话、密码等资料
  - [ ] 查看所属楼栋信息

- [ ] 在线报修提交
  - [ ] 填写故障描述
  - [ ] 上传图片
  - [ ] 提交报修单

- [ ] 报修进度查询
  - [ ] 查看个人报修记录及状态更新
  - [ ] 接收报修通知

- [ ] 访客在线报备
  - [ ] 填写访客姓名、车牌号
  - [ ] 提交报备
  - [ ] 查看报备历史

- [ ] 账单查看与支付
  - [ ] 查看个人物业费账单
  - [ ] 在线支付费用
  - [ ] 记录支付时间和方式

- [ ] 缴费记录查询
  - [ ] 查看历史缴费记录
  - [ ] 下载缴费凭证

- [ ] 个人车位查看
  - [ ] 查看已分配车位信息
  - [ ] 修改车牌号

## 五、开发规范

### 1. 命名规范

- 类名：使用大驼峰命名法，如`UserController`
- 方法名：使用小驼峰命名法，如`getUserById`
- 变量名：使用小驼峰命名法，如`userName`
- 常量名：使用全大写，单词间用下划线分隔，如`MAX_SIZE`

### 2. 注释规范

- 类注释：描述类的功能和用途
- 方法注释：描述方法的功能、参数和返回值
- 变量注释：必要时对变量进行说明

### 3. 异常处理

- 使用try-catch-finally块处理异常
- 在DAO层抛出SQLException，在Controller层捕获并处理
- 记录关键异常信息，方便调试

### 4. 数据库操作

- 使用DatabaseConnection获取数据库连接
- 使用DatabaseUtil执行SQL语句
- 操作完成后关闭数据库资源

## 六、测试计划

### 1. 单元测试

- 对DAO层的增删改查方法进行测试
- 对Controller层的业务逻辑进行测试

### 2. 集成测试

- 测试各模块之间的交互
- 测试数据流转过程

### 3. 界面测试

- 测试界面布局和样式
- 测试界面交互功能
- 测试不同分辨率下的显示效果

## 七、部署说明

### 1. 环境要求

- JDK 1.8+
- MySQL 5.7+

### 2. 部署步骤

1. 创建数据库：cpms_db
2. 执行SQL脚本创建表结构
3. 配置数据库连接参数
4. 编译打包项目
5. 运行程序
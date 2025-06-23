### 小区管理系统功能开发清单

小区管理系统功能开发清单
——————————————————————————————————————————————————————
数据账户：root 密码为空
数据库为：cpms_db

项目为：小区物业管理系统
使用的语言为：JAVA   前端为JAVA Swing
架构为：MVC
——————————————————————
注意事项：
1. 不使用maven等技术框架
2. 采用纯java方式开发
——————————————————————————————————————————————————————
一、管理员角色功能清单
系统管理模块： （Role 表）
-------------------------
1. 角色创建与编辑
新增角色（管理员 / 管家 / 业主），设置角色名称和描述，分配操作权限
-------------------------
2. 管理员账号创建
初始化超级管理员账号，支持密码修改与安全设置
-------------------------
3. 管家账号分配与楼栋绑定
创建管家账号，指定其负责的楼栋（通过ManagedBuildingID关联），分配角色权限
-------------------------
4. 角色权限分配
为不同角色分配操作权限（如管家仅能操作负责楼栋数据）
-------------------------

基础数据管理模块： （Building 表）
-------------------------
5. 楼栋信息维护
录入楼栋名称、位置，分配专属管家（ManagerID唯一绑定），支持批量导入楼栋数据
-------------------------
6. 楼栋管家调整
重新分配楼栋管家，系统自动同步管家的数据访问范围
-------------------------

数据统计与导出模块： （所有数据表）
-------------------------
7. 全小区数据导出
导出全小区业主、报修、缴费记录（Excel格式），支持按时间范围筛选
-------------------------
————————————————————————————————————————————————————————
二、物业管家角色功能清单
业主信息管理模块： （User 表）
--------------------------
1. 本楼栋业主录入
录入本楼栋业主信息（姓名、电话），生成初始账号，自动关联楼栋（BuildingID）
--------------------------
2. 业主资料编辑与查询
修改本楼栋业主资料，按姓名 / 楼栋筛选业主列表，查看业主关联的报修 / 缴费记录
--------------------------

报修管理模块： （RepairRequest 表、User 表）
--------------------------
3. 本楼栋报修处理
查看待处理报修单，更新状态（处理中 / 已完成），记录处理人（HandlerID），填写完成时间（CompleteTime）
--------------------------
4. 报修记录查询
按状态 / 时间查询本楼栋报修历史，导出报修明细
--------------------------

缴费管理模块： （PaymentRecord 表、User 表）
--------------------------

车位管理模块： （ParkingSpot 表、User 表）
--------------------------
5. 本楼栋车位录入
录入车位编号、位置，分配给本楼栋业主（OwnerUserID关联），标记状态（已占用/空闲）
--------------------------
6. 车位状态更新
修改车位车牌号（LicensePlate），更新使用状态（UsageStatus）
--------------------------
————————————————————————————————————————————————————————
三、业主角色功能清单
个人信息管理模块： （User 表）
--------------------------
1. 资料修改
修改个人电话、密码等资料，查看所属楼栋信息（BuildingID）
--------------------------

报修管理模块： （RepairRequest 表、User 表）
--------------------------
2. 在线报修提交
填写故障描述（FaultDesc），上传图片（ImagePath），提交报修单（自动关联所属楼栋）
--------------------------
3. 报修进度查询
查看个人报修记录及状态更新，接收报修通知（提交/处理/完成）
--------------------------

缴费管理模块： （PaymentRecord 表、User 表）
--------------------------
4. 账单查看与支付
查看个人物业费账单（BillingCycle），在线支付费用，记录支付时间（PaymentTime）和方式（PaymentMethod）
--------------------------
5. 缴费记录查询
查看历史缴费记录，下载缴费凭证
--------------------------

车位管理模块： （ParkingSpot 表、User 表）
--------------------------
6. 个人车位查看
查看已分配车位信息（编号、位置），修改车牌号（LicensePlate）
--------------------------
————————————————————————————————————————————————————————
四、数据结构说明
--------------------------
1. User表：存储用户信息，包含用户ID、角色ID、楼栋ID、管理楼栋ID等字段
2. Role表：存储角色信息，包含角色ID、角色名称、角色描述等字段
3. Building表：存储楼栋信息，包含楼栋ID、楼栋名称、位置、管家ID等字段
4. RepairRequest表：存储报修信息，包含报修ID、提交用户ID、处理人ID、状态、完成时间等字段
5. PaymentRecord表：存储缴费信息，包含缴费ID、业主ID、金额、缴费状态、缴费时间等字段
6. ParkingSpot表：存储车位信息，包含车位ID、业主ID、车位编号、位置、使用状态、车牌号等字段
--------------------------
五、开发注意事项
--------------------------
1. 权限控制：所有数据访问需验证用户角色和数据关联关系
2. 数据隔离：管家仅能访问自己管理楼栋的数据，业主仅能访问自己的数据
3. 事务处理：涉及多表操作的业务需使用事务保证数据一致性
4. 文件上传：图片上传需处理文件存储和路径管理
5. 导出功能：Excel导出需使用POI等Java库实现
6. 错误处理：各模块需实现完善的异常处理机制
--------------------------

SQL 数据如下：

-- 1. 创建角色表
CREATE TABLE Role (
    RoleID INT PRIMARY KEY AUTO_INCREMENT COMMENT '角色ID，主键',
    RoleName VARCHAR(50) NOT NULL COMMENT '角色名称（管理员/管家/业主）',
    RoleDesc VARCHAR(200) COMMENT '角色描述',
    CreateTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UpdateTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY (RoleName)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统角色表';

-- 2. 创建用户表
CREATE TABLE User (
    UserID INT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID，主键',
    Username VARCHAR(50) NOT NULL COMMENT '用户名',
    Password VARCHAR(100) NOT NULL COMMENT '密码（加密存储）',
    RealName VARCHAR(50) COMMENT '真实姓名',
    Phone VARCHAR(20) COMMENT '联系电话',
    RoleID INT NOT NULL COMMENT '角色ID',
    BuildingID INT COMMENT '所属楼栋ID（业主使用）',
    ManagedBuildingID INT COMMENT '管理的楼栋ID（管家使用）',
    AccountStatus TINYINT DEFAULT 1 COMMENT '账号状态（0-禁用，1-启用）',
    CreateTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UpdateTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY (Username),
    FOREIGN KEY (RoleID) REFERENCES Role(RoleID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 3. 创建楼栋表
CREATE TABLE Building (
    BuildingID INT PRIMARY KEY AUTO_INCREMENT COMMENT '楼栋ID，主键',
    BuildingName VARCHAR(100) NOT NULL COMMENT '楼栋名称',
    Location VARCHAR(200) COMMENT '楼栋位置',
    ManagerID INT COMMENT '管家ID',
    CreateTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UpdateTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY (ManagerID),
    FOREIGN KEY (ManagerID) REFERENCES User(UserID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='楼栋表';

-- 4. 更新用户表，添加外键约束
ALTER TABLE User
ADD FOREIGN KEY (BuildingID) REFERENCES Building(BuildingID),
ADD FOREIGN KEY (ManagedBuildingID) REFERENCES Building(BuildingID);

-- 5. 创建报修表
CREATE TABLE RepairRequest (
    RepairID INT PRIMARY KEY AUTO_INCREMENT COMMENT '报修ID，主键',
    SubmitUserID INT NOT NULL COMMENT '提交用户ID',
    FaultDesc TEXT NOT NULL COMMENT '故障描述',
    ImagePath VARCHAR(255) COMMENT '图片路径',
    Status TINYINT DEFAULT 0 COMMENT '状态（0-待处理，1-处理中，2-已完成）',
    HandlerID INT COMMENT '处理人ID',
    CompleteTime DATETIME COMMENT '完成时间',
    CreateTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UpdateTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (SubmitUserID) REFERENCES User(UserID),
    FOREIGN KEY (HandlerID) REFERENCES User(UserID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报修表';

-- 6. 创建缴费记录表
CREATE TABLE PaymentRecord (
    PaymentID INT PRIMARY KEY AUTO_INCREMENT COMMENT '缴费记录ID，主键',
    OwnerUserID INT NOT NULL COMMENT '业主ID',
    BillingCycle VARCHAR(50) NOT NULL COMMENT '计费周期',
    Amount DECIMAL(10,2) NOT NULL COMMENT '金额',
    PaymentStatus TINYINT DEFAULT 0 COMMENT '缴费状态（0-未缴，1-已缴）',
    PaymentTime DATETIME COMMENT '缴费时间',
    PaymentMethod VARCHAR(50) COMMENT '支付方式',
    CreateTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UpdateTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (OwnerUserID) REFERENCES User(UserID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='缴费记录表';

-- 7. 创建车位表
CREATE TABLE ParkingSpot (
    ParkingID INT PRIMARY KEY AUTO_INCREMENT COMMENT '车位ID，主键',
    BuildingID INT NOT NULL COMMENT '楼栋ID',
    SpotNumber VARCHAR(50) NOT NULL COMMENT '车位编号',
    Location VARCHAR(100) COMMENT '车位位置',
    OwnerUserID INT COMMENT '业主ID',
    LicensePlate VARCHAR(20) COMMENT '车牌号',
    UsageStatus TINYINT DEFAULT 0 COMMENT '使用状态（0-空闲，1-已占用）',
    CreateTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UpdateTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (BuildingID) REFERENCES Building(BuildingID),
    FOREIGN KEY (OwnerUserID) REFERENCES User(UserID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='车位表';


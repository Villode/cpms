-- 小区物业管理系统数据库初始化脚本

-- 设置数据库字符集
ALTER DATABASE cpms_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建角色表
CREATE TABLE IF NOT EXISTS role (
    RoleID INT NOT NULL AUTO_INCREMENT COMMENT '角色ID，主键',
    RoleName VARCHAR(50) NOT NULL COMMENT '角色名称',
    RoleDesc VARCHAR(200) DEFAULT NULL COMMENT '角色描述',
    CreateTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UpdateTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (RoleID),
    UNIQUE KEY (RoleName)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- 创建楼栋表
CREATE TABLE IF NOT EXISTS building (
    BuildingID INT NOT NULL AUTO_INCREMENT COMMENT '楼栋ID，主键',
    BuildingName VARCHAR(100) NOT NULL COMMENT '楼栋名称',
    BuildingCode VARCHAR(20) NOT NULL COMMENT '楼栋编码',
    Address VARCHAR(200) NOT NULL COMMENT '楼栋地址',
    TotalFloors INT NOT NULL DEFAULT 1 COMMENT '总楼层数',
    TotalUnits INT NOT NULL DEFAULT 1 COMMENT '总单元数',
    TotalRooms INT NOT NULL DEFAULT 1 COMMENT '总房间数',
    ManagerID INT NULL COMMENT '管家ID',
    CreateTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UpdateTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (BuildingID),
    UNIQUE KEY UK_BuildingCode (BuildingCode)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='楼栋表';

-- 创建用户表
CREATE TABLE IF NOT EXISTS user (
    UserID INT NOT NULL AUTO_INCREMENT COMMENT '用户ID，主键',
    Username VARCHAR(50) NOT NULL COMMENT '用户名',
    Password VARCHAR(100) NOT NULL COMMENT '密码（加密存储）',
    RealName VARCHAR(50) DEFAULT NULL COMMENT '真实姓名',
    Phone VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    RoleID INT NOT NULL COMMENT '角色ID',
    BuildingID INT DEFAULT NULL COMMENT '所属楼栋ID（业主使用）',
    ManagedBuildingID INT DEFAULT NULL COMMENT '管理的楼栋ID（管家使用）',
    AccountStatus TINYINT DEFAULT 1 COMMENT '账号状态（0-禁用，1-启用）',
    CreateTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UpdateTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (UserID),
    UNIQUE KEY (Username),
    KEY (RoleID),
    KEY (BuildingID),
    KEY (ManagedBuildingID),
    CONSTRAINT FOREIGN KEY (RoleID) REFERENCES role (RoleID),
    CONSTRAINT FOREIGN KEY (BuildingID) REFERENCES building (BuildingID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 添加外键约束（楼栋表引用用户表），注意不添加唯一约束
ALTER TABLE building
ADD CONSTRAINT FOREIGN KEY (ManagerID) REFERENCES user (UserID);

-- 创建权限表
CREATE TABLE IF NOT EXISTS permission (
    PermissionID INT NOT NULL AUTO_INCREMENT COMMENT '权限ID，主键',
    PermissionName VARCHAR(50) NOT NULL COMMENT '权限名称',
    PermissionCode VARCHAR(50) NOT NULL COMMENT '权限编码',
    PermissionDesc VARCHAR(200) DEFAULT NULL COMMENT '权限描述',
    CreateTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UpdateTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (PermissionID),
    UNIQUE KEY (PermissionCode)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限表';

-- 创建角色权限关联表
CREATE TABLE IF NOT EXISTS role_permission (
    RolePermissionID INT NOT NULL AUTO_INCREMENT COMMENT '角色权限ID，主键',
    RoleID INT NOT NULL COMMENT '角色ID',
    PermissionID INT NOT NULL COMMENT '权限ID',
    CreateTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UpdateTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (RolePermissionID),
    UNIQUE KEY (RoleID, PermissionID),
    CONSTRAINT FOREIGN KEY (RoleID) REFERENCES role (RoleID),
    CONSTRAINT FOREIGN KEY (PermissionID) REFERENCES permission (PermissionID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色权限关联表';

-- 创建报修表
CREATE TABLE IF NOT EXISTS repair (
    RepairID INT NOT NULL AUTO_INCREMENT COMMENT '报修ID，主键',
    UserID INT NOT NULL COMMENT '报修用户ID',
    BuildingID INT NOT NULL COMMENT '楼栋ID',
    RoomNumber VARCHAR(20) NOT NULL COMMENT '房间号',
    RepairType INT NOT NULL COMMENT '报修类型（1-水电维修，2-家具维修，3-门窗维修，4-其他）',
    RepairDesc VARCHAR(500) NOT NULL COMMENT '报修描述',
    RepairStatus INT NOT NULL DEFAULT 0 COMMENT '报修状态（0-待处理，1-处理中，2-已完成，3-已取消）',
    HandlerID INT DEFAULT NULL COMMENT '处理人ID',
    HandleOpinion VARCHAR(500) DEFAULT NULL COMMENT '处理意见',
    CreateTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UpdateTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (RepairID),
    KEY (UserID),
    KEY (BuildingID),
    KEY (HandlerID),
    CONSTRAINT FOREIGN KEY (UserID) REFERENCES user (UserID),
    CONSTRAINT FOREIGN KEY (BuildingID) REFERENCES building (BuildingID),
    CONSTRAINT FOREIGN KEY (HandlerID) REFERENCES user (UserID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='报修表';

-- 初始化角色数据
INSERT IGNORE INTO role (RoleID, RoleName, RoleDesc) VALUES 
(1, '管理员', 'System administrator with all permissions'),
(2, '管家', 'Building manager'),
(3, '业主', 'Property owner');

-- 初始化管理员用户
INSERT IGNORE INTO user (UserID, Username, Password, RealName, RoleID, AccountStatus) 
VALUES (1, 'admin', 'AZICOnu9cyUFFvBp3xi1AA==', 'System Admin', 1, 1);

-- 初始化楼栋数据
INSERT IGNORE INTO building (BuildingID, BuildingName, BuildingCode, Address, TotalFloors, TotalUnits, TotalRooms) VALUES
(1, 'Building 1', 'B001', 'East Area Building 1', 18, 3, 108),
(2, 'Building 2', 'B002', 'East Area Building 2', 18, 3, 108),
(3, 'Building 3', 'B003', 'East Area Building 3', 18, 3, 108),
(4, 'Building 4', 'B004', 'West Area Building 4', 26, 4, 208),
(5, 'Building 5', 'B005', 'West Area Building 5', 26, 4, 208);

-- 初始化管家用户数据
INSERT IGNORE INTO user (Username, Password, RealName, Phone, RoleID, ManagedBuildingID, AccountStatus) VALUES
('001', 'AZICOnu9cyUFFvBp3xi1AA==', 'Zhang', '13800138001', 2, 1, 1),
('002', 'AZICOnu9cyUFFvBp3xi1AA==', 'Li', '13800138002', 2, 2, 1),
('003', 'AZICOnu9cyUFFvBp3xi1AA==', 'Wang', '13800138003', 2, 3, 1),
('004', 'AZICOnu9cyUFFvBp3xi1AA==', 'Zhao', '13800138004', 2, 4, 1);

-- 初始化业主用户数据
INSERT IGNORE INTO user (Username, Password, RealName, Phone, RoleID, BuildingID, AccountStatus) VALUES
('owner1', 'AZICOnu9cyUFFvBp3xi1AA==', 'Zhang San', '13900139001', 3, 1, 1),
('owner2', 'AZICOnu9cyUFFvBp3xi1AA==', 'Li Si', '13900139002', 3, 2, 1),
('owner3', 'AZICOnu9cyUFFvBp3xi1AA==', 'Wang Wu', '13900139003', 3, 3, 1),
('owner4', 'AZICOnu9cyUFFvBp3xi1AA==', 'Zhao Liu', '13900139004', 3, 4, 1);

-- 更新楼栋的管家ID
UPDATE building SET ManagerID = 2 WHERE BuildingID = 1;
UPDATE building SET ManagerID = 3 WHERE BuildingID = 2;
UPDATE building SET ManagerID = 4 WHERE BuildingID = 3;
UPDATE building SET ManagerID = 5 WHERE BuildingID = 4;

-- 初始化报修测试数据
INSERT IGNORE INTO repair (UserID, BuildingID, RoomNumber, RepairType, RepairDesc, RepairStatus, HandlerID, HandleOpinion) VALUES
(8, 1, '1单元101', 1, '厨房水龙头漏水', 0, NULL, NULL),
(8, 1, '1单元101', 2, '客厅沙发破损', 1, 2, '已安排维修人员处理'),
(9, 2, '2单元202', 3, '卧室门锁坏了', 2, 3, '已更换新门锁'),
(9, 2, '2单元202', 4, '阳台晾衣架松动', 3, NULL, '用户取消报修'); 
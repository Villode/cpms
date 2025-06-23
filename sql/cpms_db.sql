/*
 Navicat Premium Dump SQL

 Source Server         : MySQL
 Source Server Type    : MySQL
 Source Server Version : 80030 (8.0.30)
 Source Host           : localhost:3306
 Source Schema         : cpms_db

 Target Server Type    : MySQL
 Target Server Version : 80030 (8.0.30)
 File Encoding         : 65001

 Date: 15/06/2025 13:46:25
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for building
-- ----------------------------
DROP TABLE IF EXISTS `building`;
CREATE TABLE `building`  (
  `BuildingID` int NOT NULL AUTO_INCREMENT COMMENT '楼栋ID，主键',
  `BuildingName` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '楼栋名称',
  `BuildingCode` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '妤兼爧缂栫爜',
  `Address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '妤兼爧鍦板潃',
  `TotalFloors` int NOT NULL DEFAULT 1 COMMENT '鎬绘ゼ灞傛暟',
  `TotalUnits` int NOT NULL DEFAULT 1 COMMENT '鎬诲崟鍏冩暟',
  `TotalRooms` int NOT NULL DEFAULT 1 COMMENT '鎬绘埧闂存暟',
  `ManagerID` int NULL DEFAULT NULL,
  `CreateTime` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`BuildingID`) USING BTREE,
  UNIQUE INDEX `UK_BuildingCode`(`BuildingCode` ASC) USING BTREE,
  INDEX `building_ibfk_1`(`ManagerID` ASC) USING BTREE,
  CONSTRAINT `building_ibfk_1` FOREIGN KEY (`ManagerID`) REFERENCES `user` (`UserID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '楼栋表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of building
-- ----------------------------
INSERT INTO `building` VALUES (1, '一栋', 'B001', '翻斗大街翻斗花园东区一栋', 18, 3, 108, 2, '2025-06-13 21:54:55', '2025-06-14 22:00:02');
INSERT INTO `building` VALUES (2, '二栋', 'B002', '翻斗大街翻斗花园东区二栋', 18, 3, 108, 3, '2025-06-13 21:54:55', '2025-06-14 22:00:12');
INSERT INTO `building` VALUES (3, '三栋', 'B003', '翻斗大街翻斗花园东区三栋', 18, 3, 108, 4, '2025-06-13 21:54:55', '2025-06-14 22:00:26');
INSERT INTO `building` VALUES (4, '四栋', 'B004', '翻斗大街翻斗花园东区四栋', 26, 4, 208, 5, '2025-06-13 21:54:55', '2025-06-14 22:00:41');
INSERT INTO `building` VALUES (5, '五栋', 'B005', '翻斗大街翻斗花园东区五栋', 26, 4, 208, 3, '2025-06-13 21:54:55', '2025-06-14 22:00:54');

-- ----------------------------
-- Table structure for parkingspot
-- ----------------------------
DROP TABLE IF EXISTS `parkingspot`;
CREATE TABLE `parkingspot`  (
  `ParkingID` int NOT NULL AUTO_INCREMENT COMMENT '车位ID，主键',
  `BuildingID` int NOT NULL COMMENT '楼栋ID',
  `SpotNumber` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '车位编号',
  `Location` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '车位位置',
  `OwnerUserID` int NULL DEFAULT NULL COMMENT '业主ID',
  `LicensePlate` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '车牌号',
  `UsageStatus` tinyint NULL DEFAULT 0 COMMENT '使用状态（0-空闲，1-已占用）',
  `CreateTime` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`ParkingID`) USING BTREE,
  INDEX `BuildingID`(`BuildingID` ASC) USING BTREE,
  INDEX `idx_owner_user`(`OwnerUserID` ASC) USING BTREE,
  CONSTRAINT `parkingspot_ibfk_1` FOREIGN KEY (`BuildingID`) REFERENCES `building` (`BuildingID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `parkingspot_ibfk_2` FOREIGN KEY (`OwnerUserID`) REFERENCES `user` (`UserID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '车位表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of parkingspot
-- ----------------------------
INSERT INTO `parkingspot` VALUES (1, 1, '111', '翻斗花园101', 10, '京A66666', 1, '2025-06-14 21:00:01', '2025-06-14 21:46:08');

-- ----------------------------
-- Table structure for payment_record
-- ----------------------------
DROP TABLE IF EXISTS `payment_record`;
CREATE TABLE `payment_record`  (
  `PaymentID` int NOT NULL AUTO_INCREMENT,
  `UserID` int NOT NULL,
  `BuildingID` int NOT NULL,
  `RoomNumber` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `PaymentType` int NOT NULL COMMENT '1-物业费，2-水费，3-电费，4-其他',
  `Amount` decimal(10, 2) NOT NULL,
  `PaymentStatus` tinyint NULL DEFAULT 0 COMMENT '缴费状态（0-未缴，1-已缴）',
  `PaymentMethod` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `TransactionNo` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `DueDate` datetime NULL DEFAULT NULL,
  `PaymentTime` datetime NULL DEFAULT NULL,
  `CreateTime` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `Remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`PaymentID`) USING BTREE,
  INDEX `idx_user_status`(`UserID` ASC, `PaymentStatus` ASC) USING BTREE,
  INDEX `idx_building_room`(`BuildingID` ASC, `RoomNumber` ASC) USING BTREE,
  INDEX `idx_due_date`(`DueDate` ASC) USING BTREE,
  CONSTRAINT `payment_record_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `user` (`UserID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `payment_record_ibfk_2` FOREIGN KEY (`BuildingID`) REFERENCES `building` (`BuildingID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of payment_record
-- ----------------------------
INSERT INTO `payment_record` VALUES (1, 3, 1, '101', 1, 300.00, 1, '支付宝', '', '2025-06-24 00:00:00', '2025-06-14 20:40:10', '2025-06-14 19:46:11', '2023年第一季度物业费');
INSERT INTO `payment_record` VALUES (2, 3, 1, '101', 2, 45.50, 0, NULL, NULL, '2025-06-19 00:00:00', NULL, '2025-06-14 19:46:11', '2023年5月水费');
INSERT INTO `payment_record` VALUES (3, 3, 1, '101', 3, 78.20, 1, '支付宝', '', '2025-06-21 00:00:00', '2025-06-15 13:44:25', '2025-06-14 19:46:11', '2023年5月电费');
INSERT INTO `payment_record` VALUES (4, 4, 2, '202', 1, 320.00, 0, NULL, NULL, '2025-06-29 00:00:00', NULL, '2025-06-14 19:46:11', '2023年第一季度物业费');
INSERT INTO `payment_record` VALUES (5, 4, 2, '202', 2, 52.30, 0, NULL, NULL, '2025-06-22 00:00:00', NULL, '2025-06-14 19:46:11', '2023年5月水费');
INSERT INTO `payment_record` VALUES (6, 4, 2, '202', 3, 91.40, 0, NULL, NULL, '2025-06-24 00:00:00', NULL, '2025-06-14 19:46:11', '2023年5月电费');
INSERT INTO `payment_record` VALUES (7, 3, 1, '101', 1, 300.00, 1, '支付宝', 'ALI202305150001', '2025-05-25 00:00:00', '2025-05-20 00:00:00', '2025-04-25 00:00:00', '2023年第四季度物业费');
INSERT INTO `payment_record` VALUES (8, 3, 1, '101', 2, 38.50, 1, '微信', 'WX202305150002', '2025-05-30 00:00:00', '2025-05-27 00:00:00', '2025-05-15 00:00:00', '2023年4月水费');
INSERT INTO `payment_record` VALUES (9, 3, 1, '101', 3, 65.20, 1, '银行卡', 'BANK202305150003', '2025-05-30 00:00:00', '2025-05-29 00:00:00', '2025-05-15 00:00:00', '2023年4月电费');
INSERT INTO `payment_record` VALUES (10, 4, 2, '202', 1, 320.00, 1, '支付宝', 'ALI202305150004', '2025-05-25 00:00:00', '2025-05-23 00:00:00', '2025-04-25 00:00:00', '2023年第四季度物业费');
INSERT INTO `payment_record` VALUES (11, 4, 2, '202', 2, 48.30, 1, '微信', 'WX202305150005', '2025-05-30 00:00:00', '2025-05-30 00:00:00', '2025-05-15 00:00:00', '2023年4月水费');
INSERT INTO `payment_record` VALUES (12, 4, 2, '202', 3, 82.40, 1, '银行卡', 'BANK202305150006', '2025-05-30 00:00:00', '2025-05-28 00:00:00', '2025-05-15 00:00:00', '2023年4月电费');
INSERT INTO `payment_record` VALUES (13, 10, 1, '111', 1, 111.00, 1, '支付宝', '111111111', '2025-02-02 00:00:00', '2025-06-14 20:32:53', '2025-06-14 20:32:38', '1');
INSERT INTO `payment_record` VALUES (14, 13, 1, '111', 3, 1111.00, 1, '现金', '6666', '2026-01-01 00:00:00', '2025-06-15 01:11:25', '2025-06-15 01:10:36', '打钱');
INSERT INTO `payment_record` VALUES (15, 10, 1, '111', 3, 1111110.00, 1, '支付宝', '00000', '2028-01-01 00:00:00', '2025-06-15 13:42:59', '2025-06-15 01:12:37', '未来所有的电费');

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`  (
  `PermissionID` int NOT NULL AUTO_INCREMENT,
  `PermissionName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '权限名称',
  `PermissionCode` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '权限代码',
  `PermissionDesc` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '权限描述',
  `PermissionType` int NOT NULL DEFAULT 1 COMMENT '权限类型（1-菜单，2-按钮，3-接口）',
  `CreateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`PermissionID`) USING BTREE,
  UNIQUE INDEX `UK_PermissionCode`(`PermissionCode` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 58 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of permission
-- ----------------------------
INSERT INTO `permission` VALUES (1, '系统管理', 'system:view', '系统管理菜单', 1, '2025-06-13 21:27:54', '2025-06-13 21:27:54');
INSERT INTO `permission` VALUES (2, '角色管理', 'role:view', '角色管理菜单', 1, '2025-06-13 21:27:54', '2025-06-13 21:27:54');
INSERT INTO `permission` VALUES (3, '角色添加', 'role:add', '添加角色按钮', 2, '2025-06-13 21:27:54', '2025-06-13 21:27:54');
INSERT INTO `permission` VALUES (4, '角色编辑', 'role:edit', '编辑角色按钮', 2, '2025-06-13 21:27:54', '2025-06-13 21:27:54');
INSERT INTO `permission` VALUES (5, '角色删除', 'role:delete', '删除角色按钮', 2, '2025-06-13 21:27:54', '2025-06-13 21:27:54');
INSERT INTO `permission` VALUES (6, '用户管理', 'user:view', '用户管理菜单', 1, '2025-06-13 21:27:54', '2025-06-13 21:27:54');
INSERT INTO `permission` VALUES (7, '用户添加', 'user:add', '添加用户按钮', 2, '2025-06-13 21:27:54', '2025-06-13 21:27:54');
INSERT INTO `permission` VALUES (8, '用户编辑', 'user:edit', '编辑用户按钮', 2, '2025-06-13 21:27:54', '2025-06-13 21:27:54');
INSERT INTO `permission` VALUES (9, '用户删除', 'user:delete', '删除用户按钮', 2, '2025-06-13 21:27:54', '2025-06-13 21:27:54');
INSERT INTO `permission` VALUES (10, '用户密码重置', 'user:reset', '重置用户密码按钮', 2, '2025-06-13 21:27:54', '2025-06-13 21:27:54');
INSERT INTO `permission` VALUES (11, '角色权限分配', 'role:permission', '角色权限分配菜单', 1, '2025-06-13 21:27:54', '2025-06-13 21:27:54');
INSERT INTO `permission` VALUES (12, '权限分配', 'permission:assign', '分配权限按钮', 2, '2025-06-13 21:27:54', '2025-06-13 21:27:54');
INSERT INTO `permission` VALUES (13, '楼栋管理', 'building:view', '楼栋管理菜单', 1, '2025-06-13 21:27:54', '2025-06-13 21:27:54');
INSERT INTO `permission` VALUES (14, '楼栋添加', 'building:add', '添加楼栋按钮', 2, '2025-06-13 21:27:54', '2025-06-13 21:27:54');
INSERT INTO `permission` VALUES (15, '楼栋编辑', 'building:edit', '编辑楼栋按钮', 2, '2025-06-13 21:27:54', '2025-06-13 21:27:54');
INSERT INTO `permission` VALUES (16, '楼栋删除', 'building:delete', '删除楼栋按钮', 2, '2025-06-13 21:27:54', '2025-06-13 21:27:54');
INSERT INTO `permission` VALUES (17, '管家分配', 'manager:assign', '管家分配菜单', 1, '2025-06-13 21:27:54', '2025-06-13 21:27:54');
INSERT INTO `permission` VALUES (18, '数据统计', 'stats:view', '数据统计菜单', 1, '2025-06-13 21:27:54', '2025-06-13 21:27:54');
INSERT INTO `permission` VALUES (19, '数据导出', 'stats:export', '数据导出菜单', 1, '2025-06-13 21:27:54', '2025-06-13 21:27:54');
INSERT INTO `permission` VALUES (20, '系统管理', 'SYSTEM_MANAGE', '系统管理菜单访问权限', 1, '2025-06-14 22:54:30', '2025-06-14 22:54:30');
INSERT INTO `permission` VALUES (21, '角色管理', 'ROLE_MANAGE', '角色管理功能权限', 1, '2025-06-14 22:54:30', '2025-06-14 22:54:30');
INSERT INTO `permission` VALUES (22, '角色权限分配', 'ROLE_PERMISSION_ASSIGN', '角色权限分配功能权限', 1, '2025-06-14 22:54:30', '2025-06-14 22:54:30');
INSERT INTO `permission` VALUES (23, '用户管理', 'USER_MANAGE', '用户管理功能权限', 1, '2025-06-14 22:54:30', '2025-06-14 22:54:30');
INSERT INTO `permission` VALUES (24, '楼栋管理', 'BUILDING_MANAGE', '楼栋管理菜单访问权限', 1, '2025-06-14 22:54:30', '2025-06-14 22:54:30');
INSERT INTO `permission` VALUES (25, '楼栋信息管理', 'BUILDING_INFO_MANAGE', '楼栋信息管理功能权限', 1, '2025-06-14 22:54:30', '2025-06-14 22:54:30');
INSERT INTO `permission` VALUES (26, '数据统计', 'DATA_STATISTICS', '数据统计菜单访问权限', 1, '2025-06-14 22:54:30', '2025-06-14 22:54:30');
INSERT INTO `permission` VALUES (27, '业主管理', 'OWNER_MANAGE', '业主管理菜单访问权限', 1, '2025-06-14 22:54:30', '2025-06-14 22:54:30');
INSERT INTO `permission` VALUES (28, '业主录入', 'OWNER_ENTRY', '业主录入功能权限', 2, '2025-06-14 22:54:30', '2025-06-14 22:54:30');
INSERT INTO `permission` VALUES (29, '业主查询', 'OWNER_QUERY', '业主查询功能权限', 2, '2025-06-14 22:54:30', '2025-06-14 22:54:30');
INSERT INTO `permission` VALUES (30, '业主信息编辑', 'OWNER_EDIT', '业主信息编辑权限', 2, '2025-06-14 22:54:30', '2025-06-14 22:54:30');
INSERT INTO `permission` VALUES (31, '业主信息删除', 'OWNER_DELETE', '业主信息删除权限', 2, '2025-06-14 22:54:30', '2025-06-14 22:54:30');
INSERT INTO `permission` VALUES (36, '报修管理', 'REPAIR_MANAGE', '报修管理菜单访问权限', 1, '2025-06-14 22:54:30', '2025-06-14 22:54:30');
INSERT INTO `permission` VALUES (37, '报修处理', 'REPAIR_PROCESS', '报修处理功能权限', 2, '2025-06-14 22:54:30', '2025-06-14 22:54:30');
INSERT INTO `permission` VALUES (38, '报修查询', 'REPAIR_QUERY', '报修查询功能权限', 2, '2025-06-14 22:54:30', '2025-06-14 22:54:30');
INSERT INTO `permission` VALUES (39, '报修状态更新', 'REPAIR_STATUS_UPDATE', '报修状态更新权限', 2, '2025-06-14 22:54:30', '2025-06-14 22:54:30');
INSERT INTO `permission` VALUES (40, '缴费管理', 'PAYMENT_MANAGE', '缴费管理菜单访问权限', 1, '2025-06-14 22:54:30', '2025-06-14 22:54:30');
INSERT INTO `permission` VALUES (41, '物业费管理', 'PROPERTY_FEE_MANAGE', '物业费管理功能权限', 2, '2025-06-14 22:54:30', '2025-06-14 22:54:30');
INSERT INTO `permission` VALUES (42, '账单生成', 'BILL_GENERATE', '账单生成功能权限', 2, '2025-06-14 22:54:30', '2025-06-14 22:54:30');
INSERT INTO `permission` VALUES (43, '欠费提醒', 'PAYMENT_REMINDER', '欠费提醒功能权限', 2, '2025-06-14 22:54:30', '2025-06-14 22:54:30');
INSERT INTO `permission` VALUES (44, '收费记录查询', 'PAYMENT_RECORD_QUERY', '收费记录查询权限', 2, '2025-06-14 22:54:30', '2025-06-14 22:54:30');
INSERT INTO `permission` VALUES (45, '车位管理', 'PARKING_MANAGE', '车位管理菜单访问权限', 1, '2025-06-14 22:54:30', '2025-06-14 22:54:30');
INSERT INTO `permission` VALUES (46, '车位信息管理', 'PARKING_INFO_MANAGE', '车位信息管理功能权限', 2, '2025-06-14 22:54:30', '2025-06-14 22:54:30');
INSERT INTO `permission` VALUES (47, '车位分配', 'PARKING_ASSIGN', '车位分配功能权限', 2, '2025-06-14 22:54:30', '2025-06-14 22:54:30');
INSERT INTO `permission` VALUES (48, '车牌号修改', 'LICENSE_PLATE_EDIT', '车牌号修改权限（仅管家和管理员）', 2, '2025-06-14 22:54:30', '2025-06-14 22:54:30');
INSERT INTO `permission` VALUES (49, '车位解除分配', 'PARKING_UNASSIGN', '车位解除分配权限', 2, '2025-06-14 22:54:30', '2025-06-14 22:54:30');
INSERT INTO `permission` VALUES (50, '系统退出', 'SYSTEM_EXIT', '系统退出权限', 2, '2025-06-14 22:54:30', '2025-06-14 22:54:30');
INSERT INTO `permission` VALUES (51, '提交报修', 'REPAIR_SUBMIT', '业主提交报修权限', 2, '2025-06-14 23:13:15', '2025-06-14 23:13:15');
INSERT INTO `permission` VALUES (52, '查询个人报修', 'REPAIR_QUERY_OWN', '业主查询自己的报修记录权限', 2, '2025-06-14 23:13:15', '2025-06-14 23:13:15');
INSERT INTO `permission` VALUES (53, '查询个人缴费', 'PAYMENT_QUERY_OWN', '业主查询自己的缴费账单权限', 2, '2025-06-14 23:13:15', '2025-06-14 23:13:15');
INSERT INTO `permission` VALUES (54, '查询个人缴费记录', 'PAYMENT_HISTORY_OWN', '业主查询自己的缴费记录权限', 2, '2025-06-14 23:13:15', '2025-06-14 23:13:15');
INSERT INTO `permission` VALUES (55, '查看个人车位', 'PARKING_VIEW_OWN', '业主查看自己的车位信息权限', 2, '2025-06-14 23:13:15', '2025-06-14 23:13:15');
INSERT INTO `permission` VALUES (56, '查看个人信息', 'PROFILE_VIEW', '业主查看个人信息权限', 2, '2025-06-14 23:13:15', '2025-06-14 23:13:15');
INSERT INTO `permission` VALUES (57, '修改密码', 'PASSWORD_CHANGE', '用户修改密码权限', 2, '2025-06-14 23:13:15', '2025-06-14 23:13:15');

-- ----------------------------
-- Table structure for repair
-- ----------------------------
DROP TABLE IF EXISTS `repair`;
CREATE TABLE `repair`  (
  `RepairID` int NOT NULL AUTO_INCREMENT COMMENT '报修ID，主键',
  `UserID` int NOT NULL COMMENT '报修用户ID',
  `BuildingID` int NOT NULL COMMENT '楼栋ID',
  `RoomNumber` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '房间号',
  `RepairType` tinyint NOT NULL COMMENT '报修类型（1-水电维修，2-家具维修，3-门窗维修，4-其他）',
  `RepairDesc` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '报修描述',
  `RepairStatus` tinyint NOT NULL DEFAULT 0 COMMENT '报修状态（0-待处理，1-处理中，2-已完成，3-已取消）',
  `HandlerID` int NULL DEFAULT NULL COMMENT '处理人ID',
  `HandleOpinion` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '处理意见',
  `CreateTime` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`RepairID`) USING BTREE,
  INDEX `UserID`(`UserID` ASC) USING BTREE,
  INDEX `BuildingID`(`BuildingID` ASC) USING BTREE,
  INDEX `HandlerID`(`HandlerID` ASC) USING BTREE,
  INDEX `idx_user_status`(`UserID` ASC, `RepairStatus` ASC) USING BTREE,
  INDEX `idx_building_room`(`BuildingID` ASC, `RoomNumber` ASC) USING BTREE,
  INDEX `idx_type_status`(`RepairType` ASC, `RepairStatus` ASC) USING BTREE,
  INDEX `idx_handler_status`(`HandlerID` ASC, `RepairStatus` ASC) USING BTREE,
  INDEX `idx_create_time`(`CreateTime` ASC) USING BTREE,
  CONSTRAINT `repair_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `user` (`UserID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `repair_ibfk_2` FOREIGN KEY (`BuildingID`) REFERENCES `building` (`BuildingID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `repair_ibfk_3` FOREIGN KEY (`HandlerID`) REFERENCES `user` (`UserID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '报修表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of repair
-- ----------------------------
INSERT INTO `repair` VALUES (1, 3, 1, '1单元101', 1, '厨房水龙头漏水', 0, NULL, NULL, '2025-06-13 23:42:53', '2025-06-13 23:42:53');
INSERT INTO `repair` VALUES (2, 3, 1, '1单元101', 2, '客厅沙发破损', 1, 2, '已安排维修人员处理', '2025-06-13 23:42:53', '2025-06-13 23:42:53');
INSERT INTO `repair` VALUES (3, 4, 2, '2单元202', 3, '卧室门锁坏了', 2, 3, '已更换新门锁', '2025-06-13 23:42:53', '2025-06-13 23:42:53');
INSERT INTO `repair` VALUES (4, 4, 2, '2单元202', 4, '阳台晾衣架松动', 1, 3, '用户取消报修', '2025-06-13 23:42:53', '2025-06-14 00:25:09');
INSERT INTO `repair` VALUES (9, 10, 1, '未知', 4, '漏水', 2, 2, '完成', '2025-06-14 18:51:21', '2025-06-15 13:44:06');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `RoleID` int NOT NULL AUTO_INCREMENT COMMENT '角色ID，主键',
  `RoleName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色名称（管理员/管家/业主）',
  `RoleDesc` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色描述',
  `CreateTime` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`RoleID`) USING BTREE,
  UNIQUE INDEX `RoleName`(`RoleName` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, '管理员', '系统管理员，拥有所有权限', '2025-06-13 21:15:22', '2025-06-13 21:15:22');
INSERT INTO `role` VALUES (2, '管家', '物业管家，负责楼栋管理', '2025-06-13 21:15:22', '2025-06-13 21:15:22');
INSERT INTO `role` VALUES (3, '业主', '小区业主，使用物业服务', '2025-06-13 21:15:22', '2025-06-15 01:07:48');

-- ----------------------------
-- Table structure for rolepermission
-- ----------------------------
DROP TABLE IF EXISTS `rolepermission`;
CREATE TABLE `rolepermission`  (
  `ID` int NOT NULL AUTO_INCREMENT,
  `RoleID` int NOT NULL COMMENT '角色ID',
  `PermissionID` int NOT NULL COMMENT '权限ID',
  `CreateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE INDEX `UK_RolePermission`(`RoleID` ASC, `PermissionID` ASC) USING BTREE,
  INDEX `PermissionID`(`PermissionID` ASC) USING BTREE,
  INDEX `idx_role_permission`(`RoleID` ASC, `PermissionID` ASC) USING BTREE,
  CONSTRAINT `rolepermission_ibfk_1` FOREIGN KEY (`RoleID`) REFERENCES `role` (`RoleID`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `rolepermission_ibfk_2` FOREIGN KEY (`PermissionID`) REFERENCES `permission` (`PermissionID`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 279 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色权限关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of rolepermission
-- ----------------------------
INSERT INTO `rolepermission` VALUES (171, 2, 1, '2025-06-14 22:55:23');
INSERT INTO `rolepermission` VALUES (172, 2, 2, '2025-06-14 22:55:23');
INSERT INTO `rolepermission` VALUES (173, 2, 3, '2025-06-14 22:55:23');
INSERT INTO `rolepermission` VALUES (174, 2, 4, '2025-06-14 22:55:23');
INSERT INTO `rolepermission` VALUES (175, 2, 5, '2025-06-14 22:55:23');
INSERT INTO `rolepermission` VALUES (176, 2, 6, '2025-06-14 22:55:23');
INSERT INTO `rolepermission` VALUES (177, 2, 7, '2025-06-14 22:55:23');
INSERT INTO `rolepermission` VALUES (178, 2, 8, '2025-06-14 22:55:23');
INSERT INTO `rolepermission` VALUES (179, 2, 9, '2025-06-14 22:55:23');
INSERT INTO `rolepermission` VALUES (180, 2, 10, '2025-06-14 22:55:23');
INSERT INTO `rolepermission` VALUES (181, 2, 11, '2025-06-14 22:55:23');
INSERT INTO `rolepermission` VALUES (182, 2, 12, '2025-06-14 22:55:23');
INSERT INTO `rolepermission` VALUES (183, 2, 13, '2025-06-14 22:55:23');
INSERT INTO `rolepermission` VALUES (184, 2, 14, '2025-06-14 22:55:23');
INSERT INTO `rolepermission` VALUES (185, 2, 15, '2025-06-14 22:55:23');
INSERT INTO `rolepermission` VALUES (186, 2, 16, '2025-06-14 22:55:23');
INSERT INTO `rolepermission` VALUES (187, 2, 17, '2025-06-14 22:55:23');
INSERT INTO `rolepermission` VALUES (188, 2, 18, '2025-06-14 22:55:23');
INSERT INTO `rolepermission` VALUES (189, 2, 19, '2025-06-14 22:55:23');
INSERT INTO `rolepermission` VALUES (190, 2, 20, '2025-06-14 22:55:23');
INSERT INTO `rolepermission` VALUES (191, 2, 21, '2025-06-14 22:55:23');
INSERT INTO `rolepermission` VALUES (192, 2, 22, '2025-06-14 22:55:23');
INSERT INTO `rolepermission` VALUES (193, 2, 23, '2025-06-14 22:55:23');
INSERT INTO `rolepermission` VALUES (194, 2, 24, '2025-06-14 22:55:23');
INSERT INTO `rolepermission` VALUES (195, 2, 25, '2025-06-14 22:55:23');
INSERT INTO `rolepermission` VALUES (196, 2, 26, '2025-06-14 22:55:23');
INSERT INTO `rolepermission` VALUES (197, 2, 27, '2025-06-14 22:55:23');
INSERT INTO `rolepermission` VALUES (198, 2, 28, '2025-06-14 22:55:23');
INSERT INTO `rolepermission` VALUES (199, 2, 29, '2025-06-14 22:55:23');
INSERT INTO `rolepermission` VALUES (200, 2, 30, '2025-06-14 22:55:23');
INSERT INTO `rolepermission` VALUES (201, 2, 31, '2025-06-14 22:55:23');
INSERT INTO `rolepermission` VALUES (202, 2, 32, '2025-06-14 22:55:23');
INSERT INTO `rolepermission` VALUES (203, 2, 33, '2025-06-14 22:55:23');
INSERT INTO `rolepermission` VALUES (204, 2, 34, '2025-06-14 22:55:23');
INSERT INTO `rolepermission` VALUES (205, 2, 35, '2025-06-14 22:55:23');
INSERT INTO `rolepermission` VALUES (206, 2, 36, '2025-06-14 22:55:23');
INSERT INTO `rolepermission` VALUES (207, 2, 37, '2025-06-14 22:55:23');
INSERT INTO `rolepermission` VALUES (208, 2, 38, '2025-06-14 22:55:23');
INSERT INTO `rolepermission` VALUES (209, 2, 39, '2025-06-14 22:55:23');
INSERT INTO `rolepermission` VALUES (210, 2, 40, '2025-06-14 22:55:23');
INSERT INTO `rolepermission` VALUES (211, 2, 41, '2025-06-14 22:55:23');
INSERT INTO `rolepermission` VALUES (212, 2, 42, '2025-06-14 22:55:23');
INSERT INTO `rolepermission` VALUES (213, 2, 43, '2025-06-14 22:55:23');
INSERT INTO `rolepermission` VALUES (214, 2, 44, '2025-06-14 22:55:23');
INSERT INTO `rolepermission` VALUES (215, 2, 46, '2025-06-14 22:55:23');
INSERT INTO `rolepermission` VALUES (216, 2, 47, '2025-06-14 22:55:23');
INSERT INTO `rolepermission` VALUES (217, 2, 48, '2025-06-14 22:55:23');
INSERT INTO `rolepermission` VALUES (218, 2, 49, '2025-06-14 22:55:23');
INSERT INTO `rolepermission` VALUES (219, 2, 50, '2025-06-14 22:55:23');
INSERT INTO `rolepermission` VALUES (221, 1, 1, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (222, 1, 2, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (223, 1, 3, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (224, 1, 4, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (225, 1, 5, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (226, 1, 6, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (227, 1, 7, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (228, 1, 8, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (229, 1, 9, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (230, 1, 10, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (231, 1, 11, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (232, 1, 12, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (233, 1, 13, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (234, 1, 14, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (235, 1, 15, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (236, 1, 16, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (237, 1, 17, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (238, 1, 18, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (239, 1, 19, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (240, 1, 20, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (241, 1, 21, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (242, 1, 22, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (243, 1, 23, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (244, 1, 24, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (245, 1, 25, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (246, 1, 26, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (247, 1, 27, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (248, 1, 28, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (249, 1, 29, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (250, 1, 30, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (251, 1, 31, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (252, 1, 32, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (253, 1, 33, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (254, 1, 34, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (255, 1, 35, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (256, 1, 36, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (257, 1, 37, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (258, 1, 38, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (259, 1, 39, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (260, 1, 40, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (261, 1, 41, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (262, 1, 42, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (263, 1, 43, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (264, 1, 44, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (265, 1, 45, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (266, 1, 46, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (267, 1, 47, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (268, 1, 48, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (269, 1, 49, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (270, 1, 50, '2025-06-14 23:10:15');
INSERT INTO `rolepermission` VALUES (271, 3, 50, '2025-06-14 23:13:24');
INSERT INTO `rolepermission` VALUES (272, 3, 51, '2025-06-14 23:13:24');
INSERT INTO `rolepermission` VALUES (273, 3, 52, '2025-06-14 23:13:24');
INSERT INTO `rolepermission` VALUES (274, 3, 53, '2025-06-14 23:13:24');
INSERT INTO `rolepermission` VALUES (275, 3, 54, '2025-06-14 23:13:24');
INSERT INTO `rolepermission` VALUES (276, 3, 55, '2025-06-14 23:13:24');
INSERT INTO `rolepermission` VALUES (277, 3, 56, '2025-06-14 23:13:24');
INSERT INTO `rolepermission` VALUES (278, 3, 57, '2025-06-14 23:13:24');

-- ----------------------------
-- Table structure for systemmessage
-- ----------------------------
DROP TABLE IF EXISTS `systemmessage`;
CREATE TABLE `systemmessage`  (
  `MessageID` int NOT NULL AUTO_INCREMENT COMMENT '消息ID，主键',
  `ReceiverID` int NOT NULL COMMENT '接收用户ID',
  `Title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '消息标题',
  `Content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '消息内容',
  `MessageType` tinyint NOT NULL COMMENT '消息类型（1-报修通知，2-缴费提醒）',
  `IsRead` tinyint(1) NULL DEFAULT 0 COMMENT '是否已读',
  `CreateTime` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`MessageID`) USING BTREE,
  INDEX `idx_receiver_read`(`ReceiverID` ASC, `IsRead` ASC) USING BTREE,
  INDEX `idx_type_time`(`MessageType` ASC, `CreateTime` ASC) USING BTREE,
  CONSTRAINT `systemmessage_ibfk_1` FOREIGN KEY (`ReceiverID`) REFERENCES `user` (`UserID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统消息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of systemmessage
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `UserID` int NOT NULL AUTO_INCREMENT COMMENT '用户ID，主键',
  `Username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `Password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码（加密存储）',
  `RealName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `Phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系电话',
  `RoleID` int NOT NULL COMMENT '角色ID',
  `BuildingID` int NULL DEFAULT NULL COMMENT '所属楼栋ID（业主使用）',
  `ManagedBuildingID` int NULL DEFAULT NULL COMMENT '管理的楼栋ID（管家使用）',
  `AccountStatus` tinyint NULL DEFAULT 1 COMMENT '账号状态（0-禁用，1-启用）',
  `CreateTime` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`UserID`) USING BTREE,
  UNIQUE INDEX `Username`(`Username` ASC) USING BTREE,
  INDEX `BuildingID`(`BuildingID` ASC) USING BTREE,
  INDEX `idx_role_building`(`RoleID` ASC, `BuildingID` ASC) USING BTREE,
  INDEX `idx_managed_building`(`ManagedBuildingID` ASC) USING BTREE,
  INDEX `idx_account_status`(`AccountStatus` ASC) USING BTREE,
  CONSTRAINT `user_ibfk_1` FOREIGN KEY (`RoleID`) REFERENCES `role` (`RoleID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `user_ibfk_2` FOREIGN KEY (`BuildingID`) REFERENCES `building` (`BuildingID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', '0192023a7bbd73250516f069df18b500', '系统管理员', NULL, 1, NULL, NULL, 1, '2025-06-13 21:15:22', '2025-06-14 01:19:05');
INSERT INTO `user` VALUES (2, '001', 'e10adc3949ba59abbe56e057f20f883e', '张管家', '13800138001', 2, NULL, NULL, 1, '2025-06-13 22:26:41', '2025-06-14 22:02:20');
INSERT INTO `user` VALUES (3, '002', 'e10adc3949ba59abbe56e057f20f883e', '李管家', '13800138002', 2, NULL, NULL, 1, '2025-06-13 22:26:41', '2025-06-14 22:02:30');
INSERT INTO `user` VALUES (4, '003', 'e10adc3949ba59abbe56e057f20f883e', '王管家', '13800138003', 2, NULL, NULL, 1, '2025-06-13 22:26:41', '2025-06-14 22:02:38');
INSERT INTO `user` VALUES (5, '004', 'e10adc3949ba59abbe56e057f20f883e', '方管家', '13800138004', 2, NULL, NULL, 1, '2025-06-13 22:26:41', '2025-06-14 22:02:50');
INSERT INTO `user` VALUES (10, 'owner1', 'e10adc3949ba59abbe56e057f20f883e', '胡英俊', '13900139001', 3, 1, NULL, 1, '2025-06-13 23:42:15', '2025-06-14 21:24:06');
INSERT INTO `user` VALUES (11, 'owner2', 'e10adc3949ba59abbe56e057f20f883e', '吴美丽', '13900139002', 3, 3, NULL, 1, '2025-06-13 23:42:15', '2025-06-15 13:43:52');
INSERT INTO `user` VALUES (12, 'owner3', 'e10adc3949ba59abbe56e057f20f883e', '胡图图', '13900139003', 3, 1, NULL, 1, '2025-06-13 23:42:15', '2025-06-15 13:43:42');
INSERT INTO `user` VALUES (13, 'owner4', 'e10adc3949ba59abbe56e057f20f883e', '刘爷爷', '13900139004', 3, 4, NULL, 1, '2025-06-13 23:42:15', '2025-06-15 13:43:47');

SET FOREIGN_KEY_CHECKS = 1;

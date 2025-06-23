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

-- 插入测试数据
INSERT INTO repair (UserID, BuildingID, RoomNumber, RepairType, RepairDesc, RepairStatus, HandlerID, HandleOpinion) VALUES
(3, 1, '1单元101', 1, '厨房水龙头漏水', 0, NULL, NULL),
(3, 1, '1单元101', 2, '客厅沙发破损', 1, 2, '已安排维修人员处理'),
(4, 2, '2单元202', 3, '卧室门锁坏了', 2, 3, '已更换新门锁'),
(4, 2, '2单元202', 4, '阳台晾衣架松动', 3, NULL, '用户取消报修'); 
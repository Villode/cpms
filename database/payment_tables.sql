-- 缴费记录表
CREATE TABLE IF NOT EXISTS payment_record (
    PaymentID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT NOT NULL,
    BuildingID INT NOT NULL,
    RoomNumber VARCHAR(50) NOT NULL,
    PaymentType INT NOT NULL COMMENT '1-物业费，2-水费，3-电费，4-其他',
    Amount DECIMAL(10,2) NOT NULL,
    PaymentStatus INT DEFAULT 0 COMMENT '0-未缴费，1-已缴费',
    PaymentMethod VARCHAR(50) NULL,
    TransactionNo VARCHAR(100) NULL,
    DueDate DATETIME NULL,
    PaymentTime DATETIME NULL,
    CreateTime DATETIME DEFAULT CURRENT_TIMESTAMP,
    Remark VARCHAR(255) NULL,
    FOREIGN KEY (UserID) REFERENCES user(UserID),
    FOREIGN KEY (BuildingID) REFERENCES building(BuildingID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 添加测试数据
INSERT INTO payment_record (UserID, BuildingID, RoomNumber, PaymentType, Amount, PaymentStatus, DueDate, CreateTime, Remark)
VALUES 
(3, 1, '101', 1, 300.00, 0, DATE_ADD(CURRENT_DATE(), INTERVAL 10 DAY), CURRENT_TIMESTAMP, '2023年第一季度物业费'),
(3, 1, '101', 2, 45.50, 0, DATE_ADD(CURRENT_DATE(), INTERVAL 5 DAY), CURRENT_TIMESTAMP, '2023年5月水费'),
(3, 1, '101', 3, 78.20, 0, DATE_ADD(CURRENT_DATE(), INTERVAL 7 DAY), CURRENT_TIMESTAMP, '2023年5月电费'),
(4, 2, '202', 1, 320.00, 0, DATE_ADD(CURRENT_DATE(), INTERVAL 15 DAY), CURRENT_TIMESTAMP, '2023年第一季度物业费'),
(4, 2, '202', 2, 52.30, 0, DATE_ADD(CURRENT_DATE(), INTERVAL 8 DAY), CURRENT_TIMESTAMP, '2023年5月水费'),
(4, 2, '202', 3, 91.40, 0, DATE_ADD(CURRENT_DATE(), INTERVAL 10 DAY), CURRENT_TIMESTAMP, '2023年5月电费');

-- 添加一些已缴费的记录
INSERT INTO payment_record (UserID, BuildingID, RoomNumber, PaymentType, Amount, PaymentStatus, PaymentMethod, TransactionNo, DueDate, PaymentTime, CreateTime, Remark)
VALUES 
(3, 1, '101', 1, 300.00, 1, '支付宝', 'ALI202305150001', DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), DATE_SUB(CURRENT_DATE(), INTERVAL 25 DAY), DATE_SUB(CURRENT_DATE(), INTERVAL 50 DAY), '2023年第四季度物业费'),
(3, 1, '101', 2, 38.50, 1, '微信', 'WX202305150002', DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), DATE_SUB(CURRENT_DATE(), INTERVAL 18 DAY), DATE_SUB(CURRENT_DATE(), INTERVAL 30 DAY), '2023年4月水费'),
(3, 1, '101', 3, 65.20, 1, '银行卡', 'BANK202305150003', DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), DATE_SUB(CURRENT_DATE(), INTERVAL 16 DAY), DATE_SUB(CURRENT_DATE(), INTERVAL 30 DAY), '2023年4月电费'),
(4, 2, '202', 1, 320.00, 1, '支付宝', 'ALI202305150004', DATE_SUB(CURRENT_DATE(), INTERVAL 20 DAY), DATE_SUB(CURRENT_DATE(), INTERVAL 22 DAY), DATE_SUB(CURRENT_DATE(), INTERVAL 50 DAY), '2023年第四季度物业费'),
(4, 2, '202', 2, 48.30, 1, '微信', 'WX202305150005', DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), DATE_SUB(CURRENT_DATE(), INTERVAL 30 DAY), '2023年4月水费'),
(4, 2, '202', 3, 82.40, 1, '银行卡', 'BANK202305150006', DATE_SUB(CURRENT_DATE(), INTERVAL 15 DAY), DATE_SUB(CURRENT_DATE(), INTERVAL 17 DAY), DATE_SUB(CURRENT_DATE(), INTERVAL 30 DAY), '2023年4月电费'); 
package com.cpms.util.constants;

/**
 * SQL语句常量类
 * 统一管理系统中使用的SQL语句
 */
public class SQLConstants {
    
    // 用户相关SQL
    public static class User {
        public static final String INSERT = 
            "INSERT INTO User(Username, Password, RealName, Phone, RoleID, BuildingID, ManagedBuildingID, AccountStatus, RoomNumber) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        public static final String UPDATE = 
            "UPDATE User SET Username=?, Password=?, RealName=?, Phone=?, RoleID=?, BuildingID=?, ManagedBuildingID=?, AccountStatus=?, RoomNumber=? WHERE UserID=?";
        
        public static final String UPDATE_PASSWORD = 
            "UPDATE User SET Password=? WHERE UserID=?";
        
        public static final String UPDATE_STATUS = 
            "UPDATE User SET AccountStatus=? WHERE UserID=?";
        
        public static final String DELETE = 
            "DELETE FROM User WHERE UserID=?";
        
        public static final String SELECT_BY_ID = 
            "SELECT * FROM User WHERE UserID=?";
        
        public static final String SELECT_BY_USERNAME = 
            "SELECT * FROM User WHERE Username=?";
        
        public static final String SELECT_BY_ROLE = 
            "SELECT * FROM User WHERE RoleID=? ORDER BY UserID ASC";
        
        public static final String SELECT_OWNERS_BY_BUILDING = 
            "SELECT * FROM user WHERE RoleID = 3 AND BuildingID = ?";
        
        public static final String SELECT_MANAGER_BY_BUILDING = 
            "SELECT * FROM User WHERE ManagedBuildingID=? AND RoleID=2";
        
        public static final String SELECT_ALL = 
            "SELECT * FROM User ORDER BY UserID ASC";
        
        public static final String SELECT_ACTIVE_BY_ROLE = 
            "SELECT * FROM User WHERE RoleID=? AND AccountStatus=1 ORDER BY UserID ASC LIMIT 1";
    }
    
    // 角色相关SQL
    public static class Role {
        public static final String INSERT_WITH_ID = 
            "INSERT INTO Role(RoleID, RoleName, RoleDesc) VALUES(?, ?, ?)";
        
        public static final String INSERT = 
            "INSERT INTO Role(RoleName, RoleDesc) VALUES(?, ?)";
        
        public static final String UPDATE = 
            "UPDATE Role SET RoleName=?, RoleDesc=? WHERE RoleID=?";
        
        public static final String DELETE = 
            "DELETE FROM Role WHERE RoleID=?";
        
        public static final String SELECT_BY_ID = 
            "SELECT * FROM Role WHERE RoleID=?";
        
        public static final String SELECT_BY_NAME = 
            "SELECT * FROM Role WHERE RoleName=?";
        
        public static final String SELECT_ALL = 
            "SELECT * FROM Role ORDER BY RoleID ASC";
    }
    
    // 楼栋相关SQL
    public static class Building {
        public static final String INSERT_WITH_ID = 
            "INSERT INTO building(BuildingID, BuildingName, BuildingCode, Address, TotalFloors, TotalUnits, TotalRooms, ManagerID) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        
        public static final String INSERT = 
            "INSERT INTO building(BuildingName, BuildingCode, Address, TotalFloors, TotalUnits, TotalRooms, ManagerID) VALUES(?, ?, ?, ?, ?, ?, ?)";
        
        public static final String UPDATE = 
            "UPDATE building SET BuildingName=?, BuildingCode=?, Address=?, TotalFloors=?, TotalUnits=?, TotalRooms=?, ManagerID=? WHERE BuildingID=?";
        
        public static final String DELETE = 
            "DELETE FROM building WHERE BuildingID=?";
        
        public static final String SELECT_BY_ID = 
            "SELECT * FROM building WHERE BuildingID=?";
        
        public static final String SELECT_BY_CODE = 
            "SELECT * FROM building WHERE BuildingCode=?";
        
        public static final String SELECT_BY_NAME = 
            "SELECT * FROM building WHERE BuildingName=?";
        
        public static final String SELECT_ALL = 
            "SELECT * FROM building ORDER BY BuildingID ASC";
        
        public static final String SELECT_BY_MANAGER = 
            "SELECT * FROM building WHERE ManagerID=? ORDER BY BuildingID ASC";
        
        public static final String SELECT_MANAGER_BY_BUILDING = 
            "SELECT u.* FROM user u JOIN building b ON u.UserID = b.ManagerID WHERE b.BuildingID=?";
    }
    
    // 报修相关SQL
    public static class Repair {
        public static final String INSERT = 
            "INSERT INTO repair(UserID, BuildingID, RoomNumber, RepairType, RepairDesc, RepairStatus) VALUES(?, ?, ?, ?, ?, ?)";
        
        public static final String UPDATE = 
            "UPDATE repair SET BuildingID=?, RoomNumber=?, RepairType=?, RepairDesc=?, RepairStatus=?, HandlerID=?, HandleOpinion=? WHERE RepairID=?";
        
        public static final String UPDATE_STATUS = 
            "UPDATE repair SET RepairStatus=?, HandlerID=?, HandleOpinion=? WHERE RepairID=?";
        
        public static final String DELETE = 
            "DELETE FROM repair WHERE RepairID=?";
        
        public static final String SELECT_BY_ID = 
            "SELECT r.*, u1.RealName as UserName, b.BuildingName, u2.RealName as HandlerName " +
            "FROM repair r " +
            "LEFT JOIN user u1 ON r.UserID = u1.UserID " +
            "LEFT JOIN building b ON r.BuildingID = b.BuildingID " +
            "LEFT JOIN user u2 ON r.HandlerID = u2.UserID " +
            "WHERE r.RepairID=?";
        
        public static final String SELECT_BY_USER = 
            "SELECT r.*, u1.RealName as UserName, b.BuildingName, u2.RealName as HandlerName " +
            "FROM repair r " +
            "LEFT JOIN user u1 ON r.UserID = u1.UserID " +
            "LEFT JOIN building b ON r.BuildingID = b.BuildingID " +
            "LEFT JOIN user u2 ON r.HandlerID = u2.UserID " +
            "WHERE r.UserID=? ORDER BY r.SubmitTime DESC";
        
        public static final String SELECT_BY_BUILDING = 
            "SELECT r.*, u1.RealName as UserName, b.BuildingName, u2.RealName as HandlerName " +
            "FROM repair r " +
            "LEFT JOIN user u1 ON r.UserID = u1.UserID " +
            "LEFT JOIN building b ON r.BuildingID = b.BuildingID " +
            "LEFT JOIN user u2 ON r.HandlerID = u2.UserID " +
            "WHERE r.BuildingID=? ORDER BY r.SubmitTime DESC";
        
        public static final String SELECT_BY_STATUS = 
            "SELECT r.*, u1.RealName as UserName, b.BuildingName, u2.RealName as HandlerName " +
            "FROM repair r " +
            "LEFT JOIN user u1 ON r.UserID = u1.UserID " +
            "LEFT JOIN building b ON r.BuildingID = b.BuildingID " +
            "LEFT JOIN user u2 ON r.HandlerID = u2.UserID " +
            "WHERE r.RepairStatus=? ORDER BY r.SubmitTime DESC";
        
        public static final String SELECT_BY_BUILDING_AND_STATUS = 
            "SELECT r.*, u1.RealName as UserName, b.BuildingName, u2.RealName as HandlerName " +
            "FROM repair r " +
            "LEFT JOIN user u1 ON r.UserID = u1.UserID " +
            "LEFT JOIN building b ON r.BuildingID = b.BuildingID " +
            "LEFT JOIN user u2 ON r.HandlerID = u2.UserID " +
            "WHERE r.BuildingID=? AND r.RepairStatus=? ORDER BY r.SubmitTime DESC";
        
        public static final String SELECT_ALL = 
            "SELECT r.*, u1.RealName as UserName, b.BuildingName, u2.RealName as HandlerName " +
            "FROM repair r " +
            "LEFT JOIN user u1 ON r.UserID = u1.UserID " +
            "LEFT JOIN building b ON r.BuildingID = b.BuildingID " +
            "LEFT JOIN user u2 ON r.HandlerID = u2.UserID " +
            "ORDER BY r.SubmitTime DESC";
    }
    
    // 权限相关SQL
    public static class Permission {
        public static final String INSERT_WITH_ID = 
            "INSERT INTO Permission(PermissionID, PermissionName, PermissionCode, PermissionDesc, PermissionType) VALUES(?, ?, ?, ?, ?)";
        
        public static final String INSERT = 
            "INSERT INTO Permission(PermissionName, PermissionCode, PermissionDesc, PermissionType) VALUES(?, ?, ?, ?)";
        
        public static final String UPDATE = 
            "UPDATE Permission SET PermissionName=?, PermissionCode=?, PermissionDesc=?, PermissionType=? WHERE PermissionID=?";
        
        public static final String DELETE = 
            "DELETE FROM Permission WHERE PermissionID=?";
        
        public static final String SELECT_BY_ID = 
            "SELECT * FROM Permission WHERE PermissionID=?";
        
        public static final String SELECT_BY_CODE = 
            "SELECT * FROM Permission WHERE PermissionCode=?";
        
        public static final String SELECT_ALL = 
            "SELECT * FROM Permission ORDER BY PermissionID ASC";
        
        public static final String SELECT_BY_TYPE = 
            "SELECT * FROM Permission WHERE PermissionType=? ORDER BY PermissionID ASC";
    }
    
    // 角色权限相关SQL
    public static class RolePermission {
        public static final String INSERT = 
            "INSERT INTO RolePermission(RoleID, PermissionID) VALUES(?, ?)";
        
        public static final String DELETE_BY_ID = 
            "DELETE FROM RolePermission WHERE ID=?";
        
        public static final String DELETE_BY_ROLE = 
            "DELETE FROM RolePermission WHERE RoleID=?";
        
        public static final String DELETE_BY_PERMISSION = 
            "DELETE FROM RolePermission WHERE PermissionID=?";
        
        public static final String DELETE_BY_ROLE_AND_PERMISSION = 
            "DELETE FROM RolePermission WHERE RoleID=? AND PermissionID=?";
        
        public static final String SELECT_PERMISSIONS_BY_ROLE = 
            "SELECT PermissionID FROM RolePermission WHERE RoleID=?";
        
        public static final String SELECT_PERMISSION_DETAILS_BY_ROLE = 
            "SELECT p.* FROM Permission p " +
            "INNER JOIN RolePermission rp ON p.PermissionID = rp.PermissionID " +
            "WHERE rp.RoleID=? ORDER BY p.PermissionID ASC";
        
        public static final String SELECT_ROLES_BY_PERMISSION = 
            "SELECT RoleID FROM RolePermission WHERE PermissionID=?";
        
        public static final String CHECK_PERMISSION_EXISTS = 
            "SELECT COUNT(*) FROM RolePermission WHERE RoleID=? AND PermissionID=?";
    }
}
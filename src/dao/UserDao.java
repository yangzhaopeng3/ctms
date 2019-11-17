package dao;

import util.MD5Util;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class UserDao {

    static final String TABLE_NAME = "auth_user";

    // MySQL 8.0 以上版本 - JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/ctmsdb?useSSL=false&serverTimezone=UTC";


    // 数据库的用户名与密码
    static final String USER = "root";
    static final String PASS = "456456asd";


    /**
     * 获取数据库连接
     */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);
            // 打开链接
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
        }
        return conn;
    }


    /**
     * 关闭连接
     */
    public static void close(ResultSet rs, Statement stm, Connection con) {
        try {
            if (rs != null)
                rs.close();
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return int
     * @author 杨肇鹏
     * @date 2019/6/23 19:19
     * @params [userId, userPassword]
     * @desc 校验账号和密码的正确性：账号不存在返回1、密码错误返回2、校验通过返回3
     */
    public static Map verifyLogin(String userAccount, String userPassword) {
        String userPassword1 = MD5Util.getEncryption(userPassword);
        Connection conn = null;
        Statement sm = null;
        ResultSet rs = null;
        Map map = new HashMap();
        try {
            conn = getConnection();
            sm = conn.createStatement();
            String sql = "SELECT * FROM " + TABLE_NAME + " WHERE user_account = '" + userAccount + "'";
            rs = sm.executeQuery(sql);
            if (!rs.next()) {
                map.put("statusCode", 1);
                return map;
            }
            sql = "SELECT * FROM " + TABLE_NAME + " WHERE user_account = '" + userAccount + "' " +
                    "AND user_password = '" + userPassword1 + "'";
            rs = sm.executeQuery(sql);
            if (!rs.next()) {
                map.put("statusCode", 2);
                return map;
            } else {
                map.put("statusCode", 3);
                map.put("userName", rs.getString("user_name"));
                map.put("userPermission", rs.getInt("user_permission"));
                map.put("userId", rs.getInt("user_id"));
                return map;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs, sm, conn);
        }
        return null;
    }

    /**
     * @return int
     * @author 杨肇鹏
     * @date 2019/6/24 14:50
     * @params [userAccount, userPassword, userName]
     * @desc 用户名重复返回1，数据库异常返回2，注册成功返回3
     */
    public static int register(String userAccount, String userPassword, String userName) {
        Connection conn = null;
        Statement sm = null;
        try {
            conn = getConnection();
            sm = conn.createStatement();
            String sql = "insert into " + TABLE_NAME + "(user_account,user_password,user_name,user_permission)" +
                    "values('" + userAccount + "'," + "'" + userPassword + "'," + "'" + userName + "'," + 1 + ")";
            sm.executeUpdate(sql);
            sm.close();
            conn.close();
        } catch (SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return 2;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (sm != null) {
                try {
                    sm.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return 3;
    }

}

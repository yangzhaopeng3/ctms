package dao;

import java.sql.*;
import java.util.Vector;


public class JdbcTemplate {


    // 数据库的用户名与密码
    static final String USER = "root";
    static final String PASS = "456456asd";
    private static String url = "jdbc:mysql://localhost:3306/ctmsdb?useSSL=false&serverTimezone=UTC";
    private static String userName = "root";
    private static String passWord = "456456asd";

    //加载驱动,整个应用程序在执行时只需要加载一次
    static {
        //静态代码块
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    //定义一个公共方法，获得数据库连接
    public static Connection getConnection() throws SQLException {

        Connection connection = DriverManager.getConnection(url, userName, passWord);
        return connection;
    }


    /**
     * 定义一个通用的可执行增删改(inset,delete,update)的方法
     *
     * @param sql    传入insert,update,delete对应的SQL语句
     * @param values 传入sql语句的?号占位符参数值
     *               要求:sql语句中的?号有N个，则第二个参数的数组的元素个数必需是N个
     * @return
     * @throws SQLException
     */
    public static int insertOrUpdateOrDelete(String sql, Object[] values) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);//获得执行SQL的prepareStatemen对象
            //用setXXX方法为?号占位符赋值
            if (values != null) {
                for (int i = 0; i < values.length; i++) {
                    pstmt.setObject(i + 1, values[i]);//取出数组的第i个元素，为?号占位符赋值
                }
            }
            //执行SQL语句  执行insert,update,delete都是调用 executeUpdate()方法,该方法返回 一个数表示SQL语句影响到的行数
            int res = pstmt.executeUpdate();
            //conn.close();
            return res;
        } catch (SQLException e) {
            // TODO: handle exception
            e.printStackTrace();
            throw e;//抛出异常。。。
        } finally {
            conn.close();
        }

    }

    //编写一个能用查询方法，把任意的查询封装到Vector<Vecotr>数据结构中

    /**
     * @param sql    传入 查询 select对应的SQL语句
     * @param values 传入查询语句对应 的?号占位的值
     *               示例：查询书刊名称为Java的书
     *               String sql = "select * from book where tsmc=?"
     *               new Object[]{"java"}
     * @return
     */
    public static Vector<Vector<Object>> queryData(String sql, Object[] values) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);//获得执行SQL的prepareStatemen对象
            //用setXXX方法为?号占位符赋值
            if (values != null) {
                for (int i = 0; i < values.length; i++) {
                    pstmt.setObject(i + 1, values[i]);//取出数组的第i个元素，为?号占位符赋值
                }
            }

            //执行查询语句:调用executeQuery方法，返回 ResultSet
            ResultSet rs = pstmt.executeQuery();
            //查询结果的元数据:表格的数据的描述信息(表名称，列名称，列的数据等)
            //通过ResultSet的getMetaData()方法获得
            ResultSetMetaData metaData = rs.getMetaData();
            //metaData.getColumnCount();获得查询结果的列数
            int columnCount = metaData.getColumnCount();
            // 获得查询语句返回 的列名称
            for (int i = 1; i < columnCount + 1; i++) {
                String columnName = metaData.getColumnName(i);
            }
            //ResultSet代表查询的结果集(二维数组)
            //遍历结果集
            Vector<Vector<Object>> allData = new Vector<Vector<Object>>();//存储查询结果
            while (rs.next()) {//取出查询结果的下一条数据
                //select * from book where tsmc//取出图书编号
                //rs.getXXX("查询语句的列名称");
                Vector<Object> rowData = new Vector<>();//存储一行数据的Vector
                for (int i = 1; i < columnCount + 1; i++) {//把当前行的所有列值取出来,  放到一个Vector中

                    String columnName = metaData.getColumnName(i);
                    rowData.add(rs.getString(columnName));//取出列值，放一个集合中
                }
                allData.add(rowData);//把多个Vector对象存到集合中，形成二维数组
            }
            return allData;
        } catch (SQLException e) {
            // TODO: handle exception
            e.printStackTrace();
            throw e;//抛出异常。。。
        } finally {
            conn.close();
        }
    }
}



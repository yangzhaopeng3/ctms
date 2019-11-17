package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class BoxofficeDao {
    public static Vector<Vector<Object>> queryBoxoffice(String queryWhere) throws SQLException {
        String sql = "SELECT ctms_boxoffice.film_no,film_name,show_time,show_hall,ticket_number " +
                "FROM ctms_boxoffice,ctms_filmlibrary " +
                "WHERE ctms_filmlibrary.film_no = ctms_boxoffice.film_no";
        if (queryWhere != null && !"".equals(queryWhere)) {
            //按影片编码或名称查询
            //判断字符串中每一个字符是否由数字组成，是则按图书编号查，否则按图名称查
            boolean isNumber = true;//假定字符串的每个字符都是数字字符
            isNumber = queryWhere.matches("[0-9]+");//表示字符串中所有字符必需是数字字符
            if (isNumber) {//按编号或名称查询
                sql += " AND ctms_boxoffice.film_no LIKE '%" + queryWhere + "%'";
                return JdbcTemplate.queryData(sql, new Object[]{});
            } else {//按影片名称模糊查
                sql += " AND film_name LIKE '%" + queryWhere + "%'";
                return JdbcTemplate.queryData(sql, new Object[]{});
            }
        } else {
            //查询所有影片，不带任何条件
            return JdbcTemplate.queryData(sql, new Object[]{});
        }
    }

    public static Vector<Vector<Object>> queryTicketPrice(Object[] values) throws SQLException {
        String sql = "SELECT ticket_price FROM ctms_schedule WHERE film_no = ? AND show_time = ? AND show_hall = ?";
        return JdbcTemplate.queryData(sql, values);
    }

    public static Vector<Vector<Object>> querySchedule() throws SQLException {
        String sql = "SELECT film_name,show_time,show_hall,ticket_price " +
                "FROM ctms_schedule,ctms_filmlibrary " +
                "WHERE ctms_schedule.film_no = ctms_filmlibrary.film_no AND is_checked = 0";
        return JdbcTemplate.queryData(sql, new Object[]{});
    }

    public static int insert(Object[] values) throws SQLException {
        String sql = "INSERT INTO ctms_boxoffice(film_no,show_time,show_hall,ticket_number) values(?,?,?,?)";
        return JdbcTemplate.insertOrUpdateOrDelete(sql, values);
    }

    public static int delete(Object[] values) throws SQLException {
        String sql = "DELETE FROM ctms_boxoffice " +
                "WHERE film_no = ? AND show_time = ? AND show_hall = ?";
        return JdbcTemplate.insertOrUpdateOrDelete(sql, values);
    }

    public static int update(Object[] newValue, Object[] oldValue) throws SQLException {
        String sql = "UPDATE ctms_boxoffice SET ticket_number = ?" +
                " WHERE film_no = ? AND show_time = ? AND show_hall = ?";
        List list = new ArrayList(Arrays.asList(newValue));
        list.addAll(Arrays.asList(oldValue));
        Object[] c = list.toArray();
        return JdbcTemplate.insertOrUpdateOrDelete(sql, c);
    }

}

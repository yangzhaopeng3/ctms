package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class ScheduleDao {
    public static Vector<Vector<Object>> querySchedule(String queryWhere) throws SQLException {
        String sql = "SELECT film_name,show_time,show_hall,ticket_price " +
                "FROM ctms_schedule,ctms_filmlibrary " +
                "WHERE ctms_schedule.film_no = ctms_filmlibrary.film_no";
        if (queryWhere != null && !"".equals(queryWhere)) {
            sql += " AND film_name LIKE '%" + queryWhere + "%'";
            return JdbcTemplate.queryData(sql, new Object[]{});
        } else {
            return JdbcTemplate.queryData(sql, new Object[]{});
        }
    }

    public static Vector<Vector<Object>> queryFilmName() throws SQLException {
        String sql = "SELECT film_no,film_name FROM ctms_filmlibrary";
        return JdbcTemplate.queryData(sql, new Object[]{});
    }

    public static Vector queryHall() throws SQLException {
        String sql = "SELECT hall_no FROM ctms_hall WHERE hall_isWork = 1";
        return JdbcTemplate.queryData(sql, new Object[]{});
    }

    public static int insert(Object[] values) throws SQLException {
        String sql = "INSERT INTO ctms_schedule(film_no,show_time,show_hall,ticket_price,end_time) values(?,?,?,?,?)";
        return JdbcTemplate.insertOrUpdateOrDelete(sql, values);
    }

    public static int delete(Object[] values) throws SQLException {
        String sql = "DELETE FROM ctms_schedule WHERE film_no = ? AND show_time = ? AND show_hall = ?";
        return JdbcTemplate.insertOrUpdateOrDelete(sql, values);
    }

    public static void updateChecked(Object[] values) throws SQLException {
        String sql = "UPDATE ctms_schedule " +
                "SET is_checked = 1 " +
                "WHERE film_no = ? AND show_time = ? AND show_hall = ?";
        JdbcTemplate.insertOrUpdateOrDelete(sql, values);
    }

    public static void updateCheckedBack(Object[] values) throws SQLException {
        String sql = "UPDATE ctms_schedule " +
                "SET is_checked = 0 " +
                "WHERE film_no = ? AND show_time = ? AND show_hall = ?";
        JdbcTemplate.insertOrUpdateOrDelete(sql, values);
    }

    public static int update(Object[] newValue, Object[] oldValue) throws SQLException {
        String sql = "UPDATE ctms_schedule " +
                "SET film_no = ?,show_time = ?,show_hall = ?,ticket_price = ?,end_time = ?" +
                " WHERE film_no = ? AND show_time = ? AND show_hall = ?";
        List list = new ArrayList(Arrays.asList(newValue));
        list.addAll(Arrays.asList(oldValue));
        Object[] c = list.toArray();
        return JdbcTemplate.insertOrUpdateOrDelete(sql, c);
    }

    public static Vector<Vector<Object>> queryConfict(Object[] values) throws SQLException {
        String sql = "SELECT * from ctms_schedule WHERE show_hall = " + values[0] + " AND show_time BETWEEN '" + values[1] + "' AND '" + values[2] + "'";
        return JdbcTemplate.queryData(sql, new Object[]{});
    }

    public static Vector<Vector<Object>> queryConfictEnd(Object[] values) throws SQLException {
        String sql = "SELECT * from ctms_schedule WHERE show_hall = " + values[0] + " AND end_time BETWEEN '" + values[1] + "' AND '" + values[2] + "'";
        return JdbcTemplate.queryData(sql, new Object[]{});
    }

}

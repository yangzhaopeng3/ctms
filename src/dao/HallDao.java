package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class HallDao {
    public static Vector<Vector<Object>> queryHall() throws SQLException {
        String sql = "SELECT * FROM ctms_hall";
        return JdbcTemplate.queryData(sql, null);
    }

    public static int update(Object[] oldValue, Object[] newValue) throws SQLException {
        String sql = "UPDATE ctms_hall SET hall_isWork = ? WHERE hall_no = ?";
        List list = new ArrayList(Arrays.asList(newValue));
        list.addAll(Arrays.asList(oldValue));
        Object[] c = list.toArray();
        return JdbcTemplate.insertOrUpdateOrDelete(sql, c);
    }
}

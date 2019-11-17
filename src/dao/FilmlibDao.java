package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class FilmlibDao {

    public static Vector<Vector<Object>> queryFilm(String queryWhere) throws SQLException {
        String sql = "SELECT film_no,film_name,release_date,film_runtime,film_poster " +
                "FROM ctms_filmlibrary";

        if (queryWhere != null && !"".equals(queryWhere)) {
            //按影片编码或名称查询
            //判断字符串中每一个字符是否由数字组成，是则按图书编号查，否则按图名称查
            boolean isNumber = true;//假定字符串的每个字符都是数字字符
            isNumber = queryWhere.matches("[0-9]+");//表示字符串中所有字符必需是数字字符
            if (isNumber) {//按编号或名称查询
                sql += " WHERE (film_no = ? OR film_name = ? OR film_no LIKE '%" + queryWhere + "%' OR film_name LIKE '%" + queryWhere + "%')";
                return JdbcTemplate.queryData(sql, new Object[]{queryWhere, queryWhere});
            } else {//按影片名称模糊查
                sql += " WHERE film_name LIKE '%" + queryWhere + "%'";
                return JdbcTemplate.queryData(sql, new Object[]{});
            }
        } else {
            //查询所有影片，不带任何条件
            return JdbcTemplate.queryData(sql, new Object[]{});
        }
    }

    public static int insert(Object[] values) throws SQLException {
        String sql = "INSERT INTO ctms_filmlibrary(film_no,film_name,release_date,film_runtime,film_poster) " +
                "values(?,?,?,?,?)";
        return JdbcTemplate.insertOrUpdateOrDelete(sql, values);
    }

    public static int delete(Object[] values) throws SQLException {
        String sql = "DELETE FROM ctms_filmlibrary WHERE film_no = ?";
        return JdbcTemplate.insertOrUpdateOrDelete(sql, values);
    }

    public static Object queryFilmNo(Object[] values) throws SQLException {
        String sql = "SELECT film_no FROM ctms_filmlibrary WHERE film_name = ?";
        Vector<Vector<Object>> vectors = JdbcTemplate.queryData(sql, values);
        if (vectors != null) {
            return vectors.get(0).get(0);
        }
        return null;
    }

    public static int update(Object[] newValue, Object[] oldValue) throws SQLException {
        String sql = "UPDATE ctms_filmlibrary " +
                "SET film_no=?,film_name = ?,release_date = ?,film_runtime = ?,film_poster = ? " +
                "WHERE film_no = ?";
        List list = new ArrayList(Arrays.asList(newValue));
        list.addAll(Arrays.asList(oldValue));
        Object[] c = list.toArray();
        return JdbcTemplate.insertOrUpdateOrDelete(sql, c);
    }

    public static int queryFilmRuntime(Object[] values) throws SQLException {
        String sql = "SELECT film_runtime FROM ctms_filmlibrary WHERE film_no = ?";
        Vector<Vector<Object>> vectors = JdbcTemplate.queryData(sql, values);
        if (vectors != null) {
            return (int) Double.parseDouble((String) vectors.get(0).get(0)) * 60 * 1000;
        }
        return 0;
    }
}

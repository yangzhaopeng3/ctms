package util;

public class UserInfo {
    public static final String USER_TITLE2 = "管理员";
    public static final String UESR_TITLE1 = "用户";
    public static final String USER_TITILE0 = "未授权用户";
    public static String userName;
    public static int userId;
    /**
     * 0 : 未授权用户  无任何权限
     * 1 : 普通用户  拥有查询权限
     * 2 : 管理员  拥有所有权限
     */
    public static int userPermission = 0;
}

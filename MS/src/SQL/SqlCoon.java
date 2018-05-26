package SQL;

import java.sql.*;

/**
 * 连接数据库
 */
public class SqlCoon {
    /**
     * 利用封装好的类名来调用连接方法便可 con = SqlCoon.getConection(); // 连接数据库 stmt =
     * con.createStatement(); // 创建一个Statement语句对象
     */

    static {
        try {
            Class.forName(SqlKey.FORNAME);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    // 连接数据库
    public static Connection getConection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(SqlKey.LOGIN);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    // 关闭数据库
    public static void close(Connection conn) {
        try {
            if (conn != null)
                conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

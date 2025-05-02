package main.java.com.gobookee.common;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;


public class JDBCTemplate {
    private static Properties driver = new Properties();

    static {
        String path = JDBCTemplate.class.getResource("/config/db.properties").getPath();
        try (FileReader fr = new FileReader(path)) {
            driver.load(fr);
            Class.forName(driver.getProperty("driver"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(
                    driver.getProperty("url"),
                    driver.getProperty("user"),
                    driver.getProperty("pw")
            );
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void close(Connection obj) {
        try {
            if (obj != null && !obj.isClosed()) obj.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close(Statement obj) {
        try {
            if (obj != null && !obj.isClosed()) obj.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close(ResultSet obj) {
        try {
            if (obj != null && !obj.isClosed()) obj.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void commit(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void rollback(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) conn.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //테스트
    //테스트 2
}

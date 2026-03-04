package com.atguigu.gmall.test;

import java.sql.*;

public class DatabaseCheck {
    public static void main(String[] args) {
        // 数据库连接信息
        String url = "jdbc:mysql://10.43.27.231:3306/jmeterboot?useSSL=false&serverTimezone=UTC";
        String username = "appium";
        String password = "nopass.88888888";

        Connection connection = null;
        try {
            // 加载驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            // 建立连接
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("数据库连接成功");

            // 检查表是否存在
            checkTableExists(connection);

            // 查询表结构
            describeTable(connection);

            // 查询数据
            queryData(connection);

        } catch (ClassNotFoundException e) {
            System.out.println("找不到JDBC驱动: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("数据库操作失败: " + e.getMessage());
        } finally {
            // 关闭连接
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println("关闭连接失败: " + e.getMessage());
                }
            }
        }
    }

    private static void checkTableExists(Connection connection) throws SQLException {
        String sql = "SELECT COUNT(*) AS table_exists FROM information_schema.tables WHERE table_schema = 'jmeterboot' AND table_name = 'jmeter_perfor_current_report'";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if (rs.next()) {
            int count = rs.getInt("table_exists");
            System.out.println("表jmeter_perfor_current_report " + (count > 0 ? "存在" : "不存在"));
        }
        rs.close();
        stmt.close();
    }

    private static void describeTable(Connection connection) throws SQLException {
        String sql = "DESCRIBE jmeter_perfor_current_report";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        System.out.println("\n表结构:");
        for (int i = 1; i <= columnCount; i++) {
            System.out.print(metaData.getColumnName(i) + "\t");
        }
        System.out.println();

        while (rs.next()) {
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(rs.getString(i) + "\t");
            }
            System.out.println();
        }
        rs.close();
        stmt.close();
    }

    private static void queryData(Connection connection) throws SQLException {
        String sql = "SELECT * FROM jmeter_perfor_current_report ORDER BY starttime DESC LIMIT 10";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        System.out.println("\n最新10条数据:");
        int rowCount = 0;
        while (rs.next()) {
            rowCount++;
            System.out.println("行 " + rowCount + ":");
            for (int i = 1; i <= columnCount; i++) {
                System.out.println("  " + metaData.getColumnName(i) + ": " + rs.getString(i));
            }
            System.out.println();
        }
        System.out.println("共找到 " + rowCount + " 条记录");
        rs.close();
        stmt.close();
    }
}
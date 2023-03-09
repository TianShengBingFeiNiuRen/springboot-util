package com.andon.springbootutil.util;

import lombok.extern.slf4j.Slf4j;

import java.sql.*;

/**
 * @author Andon
 * 2022/12/15
 */
@Slf4j
public class DMConnection {

    // 定义DM JDBC驱动串
    String jdbcString = "dm.jdbc.driver.DmDriver";
    // 定义DM URL连接串
    String urlString = "jdbc:dm://10.50.3.55:5236";
    // 定义连接用户名
    String username = "SYSDBA";
    // 定义连接用户口令
    String password = "SYSDBA001";
    // 定义连接对象
    Connection conn = null;

    /**
     * 加载JDBC驱动程序
     */
    public void loadJdbcDriver() throws SQLException {
        try {
            log.info("Loading JDBC Driver...");
            // 加载JDBC驱动程序
            Class.forName(jdbcString);
        } catch (Exception e) {
            throw new SQLException("Load JDBC Driver Error : " + e.getMessage());
        }
    }

    /**
     * 连接DM数据库
     */
    public void connect() throws SQLException {
        try {
            log.info("Connecting to DM Server...");
            // 连接DM数据库
            conn = DriverManager.getConnection(urlString, username, password);
        } catch (SQLException e) {
            throw new SQLException("Connect to DM Server Error : " + e.getMessage());
        }
    }

    /**
     * 关闭连接
     */
    public void disConnect() throws SQLException {
        try {
            // 关闭连接
            conn.close();
        } catch (SQLException e) {
            throw new SQLException("close connection error : " + e.getMessage());
        }
    }

    /**
     * 查询表数据
     */
    public void queryTable() throws SQLException {
        // 查询语句
        String sql = "SELECT * FROM \"TEST_DATABASE\".\"TEST_TABLE\";";
        // 创建语句对象
        Statement stmt = conn.createStatement();
        // 执行查询
        ResultSet rs = stmt.executeQuery(sql);
        // 显示结果集
        displayResultSet(rs);
        // 关闭结果集
        rs.close();
        // 关闭语句
        stmt.close();
    }

    /**
     * 显示结果集
     */
    private void displayResultSet(ResultSet rs) throws SQLException {
        // 取得结果集元数据
        ResultSetMetaData rsmd = rs.getMetaData();
        // 取得结果集所包含的列数
        int numCols = rsmd.getColumnCount();
        // 显示列标头
        for (int i = 1; i <= numCols; i++) {
            if (i > 1) {
                System.out.print(",");
            }
            System.out.print(rsmd.getColumnLabel(i));
        }
        System.out.println("");
        // 显示结果集中所有数据
        while (rs.next()) {
            for (int i = 1; i <= numCols; i++) {
                if (i > 1) {
                    System.out.print(",");
                }
                // 处理大字段
                if ("IMAGE".equals(rsmd.getColumnTypeName(i))) {
                    System.out.print("字段内容已写入文件c:\\xxx.jpg，长度" + 0);
                } else {
                    // 普通字段
                    System.out.print(rs.getString(i));
                }
            }
            System.out.println("");
        }
    }

    public static void main(String[] args) {
        try {
            // 定义类对象
            DMConnection basicApp = new DMConnection();
            // 加载驱动程序
            basicApp.loadJdbcDriver();
            // 连接DM数据库
            basicApp.connect();
            // 查询表数据
            System.out.println("--- 查询表数据 ---");
            basicApp.queryTable();
            // 关闭连接
            basicApp.disConnect();
        } catch (Exception e) {
            log.error("error:{}", e.getMessage());
            e.printStackTrace();
        }
    }
}

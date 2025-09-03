package org.example;

import com.fasterxml.jackson.databind.ObjectMapper; // 导入Jackson库的ObjectMapper类，用于JSON的解析和生成
import java.io.BufferedReader; // 导入BufferedReader类，用于读取文本文件
import java.io.FileReader; // 导入FileReader类，用于读取文件
import java.io.IOException; // 导入IOException类，用于处理文件读取时可能发生的IO异常
import java.sql.*; // 导入java.sql包下的所有类，用于数据库操作
import java.util.Map; // 导入Map接口，用于存储键值对

//public class NDJSONImporter { // 定义一个公共类NDJSONImporter
//
//    // 定义数据库连接的常量
//    private static final String URL = "jdbc:postgresql://localhost:5432/project1";
//    private static final String USER = "postgres";
//    private static final String PASSWORD = "/PostgreS@1478950+/";
//
//    public static void main(String[] args) { // 程序的主入口点
//        String filePath = "E:\\Subjects\\CS\\Database\\Projects\\Project1\\data\\pubmed24n.ndjson"; // 指定.ndjson文件的路径
//        ObjectMapper objectMapper = new ObjectMapper(); // 创建ObjectMapper实例，用于将JSON字符串转换为Java对象
//        DatabaseInserter inserter = new DatabaseInserter();
//
//        // 使用try-with-resources语句自动管理资源，确保BufferedReader在操作完成后自动关闭
//        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
//            String line; // 用于存储从文件中读取的每一行
//            while ((line = br.readLine()) != null) { // 循环读取文件的每一行
//                Map<String, Object> jsonObject = objectMapper.readValue(line, Map.class); // 将每行的JSON字符串转换为Map对象
//                insertDataIntoDatabase(jsonObject, inserter); // 调用方法将数据插入数据库
//
//            }
//        } catch (IOException e) { // 捕获并处理可能发生的IO异常
//            e.printStackTrace(); // 打印异常堆栈信息
//        }
//    }
//
//    // 定义一个私有静态方法，用于将数据插入数据库
//    private static void insertDataIntoDatabase(Map<String, Object> data, DatabaseInserter inserter) {
//        // Consist of several insert functions, each of which inserts the data of a table into the database
//
//        inserter.insertNestedData(data);
//    }
//}


public class NDJSONImporter {
    private static final String URL = "jdbc:postgresql://localhost:5432/project1";
    private static final String USER = "postgres";
    private static final String PASSWORD = "/PostgreS@1478950+/";
    private Connection connection;

    public NDJSONImporter() {
        try {
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Failed to establish database connection: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws SQLException {
        String filePath = "E:\\Subjects\\CS\\Database\\Projects\\Project1\\data\\pubmed24n.ndjson";
        ObjectMapper objectMapper = new ObjectMapper();

        NDJSONImporter importer = new NDJSONImporter();
        DatabaseInserter inserter = new DatabaseInserter(importer.getConnection());
        DatabaseCleaner cleaner = new DatabaseCleaner(importer.getConnection());

        int count = 0;
        // 首先清空数据库
        cleaner.cleanDatabase();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            long startTime = System.currentTimeMillis();

            importer.getConnection().setAutoCommit(false);
            while ((line = br.readLine()) != null) {
                count++;
                Map<String, Object> jsonObject = objectMapper.readValue(line, Map.class);
                inserter.insertNestedData(jsonObject, count);
            }
            importer.getConnection().commit();

            long endTime = System.currentTimeMillis();
            System.out.println("总执行时间: " + (endTime - startTime) + " 毫秒");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            importer.closeConnection();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}

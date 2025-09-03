import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException; // 导入IOException类，用于处理文件读取时可能发生的IO异常
import com.fasterxml.jackson.databind.ObjectMapper; // 导入Jackson库的ObjectMapper类，用于JSON的解析和生成

import java.util.Map; // 导入Map接口，用于存储键值对

public class fileJournal {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        readLargeNDJSON("D:\\PyCharm 2024.2.4\\pubmed24n.ndjson", "D:\\PyCharm 2024.2.4\\journal10.txt");
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("程序运行时长：" + duration + "毫秒");
    }
    public static void readLargeNDJSON(String filePath, String outputPath) {
        ObjectMapper objectMapper = new ObjectMapper(); // 创建ObjectMapper实例，用于将JSON字符串转换为Java对象
        //Integer journalId = insertJournal((Map<String, Object>)data.get("journal"));
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath))) { // 创建BufferedWriter实例用于写入文件
            String line; // 用于存储从文件中读取的每一行
            while ((line = br.readLine()) != null) { // 循环读取文件的每一行
                Map<String, Object> jsonObject = objectMapper.readValue(line, Map.class); // 将每行的JSON字符串转换为Map对象
                Map<String, Object> journalData = (Map<String, Object>) jsonObject.get("journal");
                String journalIdData = (String) journalData.get("id");
                bw.write(journalIdData+","); // 将Map对象写入到输出文件
                String journalcountryData = (String) journalData.get("country");
                bw.write(journalcountryData+","); // 将Map对象写入到输出文件
                String journalissnData = (String) journalData.get("issn");
                bw.write(journalissnData+","); // 将Map对象写入到输出文件
                String journaltitleData = (String) journalData.get("title");
                bw.write(journaltitleData+","); // 将Map对象写入到输出文件

                Map<String, Object> journalIssueData = (Map<String, Object>) journalData.get("journal_issue");
                if (journalIssueData != null) {
                    Object volumeObject = journalIssueData.get("volume");
                    Object issueObject = journalIssueData.get("issue");

                    if (volumeObject != null) {
                        String journalvolumeData = String.valueOf(volumeObject);
                        bw.write(journalvolumeData+",");
                    } else {
                        bw.write("null");
                    }

                    if (issueObject != null) {
                        String journalissuedata = String.valueOf(issueObject);
                        bw.write(journalissuedata);
                    } else {
                        bw.write("null");
                    }
                } else {
                    bw.write("null");
                }

                bw.newLine(); // 写入换行符
                count++;
                if(count >= 1000000){
                    break;
                }
            }
            System.out.println("Total lines processed: " + count); // 输出处理的行数
        } catch (IOException e) { // 捕获并处理可能发生的IO异常
            e.printStackTrace(); // 打印异常堆栈信息
            System.out.println("FAILED");
        }
    }
}



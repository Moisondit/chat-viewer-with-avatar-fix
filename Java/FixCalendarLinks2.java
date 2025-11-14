import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FixCalendarLinks2 {
    // 缺失文件的日期列表
    private static final Set<String> MISSING_DATES = new HashSet<>(Arrays.asList(
        "2022-12-01", "2022-12-04", "2022-12-05", "2022-12-06", "2022-12-07", "2022-12-08", "2022-12-09", "2022-12-11", "2022-12-13", "2022-12-14", "2022-12-15", "2022-12-16", "2022-12-17", "2022-12-18", "2022-12-19", "2022-12-21", "2022-12-22", "2022-12-23", "2022-12-24", "2022-12-25", "2022-12-26", "2022-12-27", "2023-02-15"
    ));

    public static void main(String[] args) {
        try {
            String calendarPath = "c:\\Users\\ASUS\\Desktop\\游戏\\星如雨[2872215021]\\calendar_index.html";
            fixCalendarLinks(calendarPath);
            System.out.println("=== Calendar链接修复完成 ===");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void fixCalendarLinks(String filePath) throws IOException {
        // 读取文件内容
        String content = Files.readString(Paths.get(filePath));
        
        // 逐行处理，移除缺失的日期
        String[] lines = content.split("\n");
        StringBuilder newContent = new StringBuilder();
        
        for (String line : lines) {
            boolean shouldRemove = false;
            
            // 检查是否是包含缺失日期的行
            for (String missingDate : MISSING_DATES) {
                if (line.trim().equals("'" + missingDate + "',") || 
                    line.trim().equals("'" + missingDate + "'")) {
                    shouldRemove = true;
                    break;
                }
            }
            
            if (!shouldRemove) {
                newContent.append(line).append("\n");
            }
        }
        
        // 更新总聊天天数
        String resultContent = newContent.toString();
        int originalCount = 468;
        int newCount = originalCount - MISSING_DATES.size();
        resultContent = resultContent.replaceAll("总聊天天数: \\d+ 天", "总聊天天数: " + newCount + " 天");
        
        // 写回文件
        Files.writeString(Paths.get(filePath), resultContent);
        
        System.out.println("已从 calendar_index.html 中移除 " + MISSING_DATES.size() + " 个无效日期链接");
        System.out.println("移除的日期: " + MISSING_DATES);
        System.out.println("更新后的总聊天天数: " + newCount);
    }
}
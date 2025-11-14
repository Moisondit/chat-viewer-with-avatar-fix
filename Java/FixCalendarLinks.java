import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class FixCalendarLinks {
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
        
        // 从JavaScript的chatDates Set中移除缺失的日期
        for (String missingDate : MISSING_DATES) {
            // 移除 Set 中的日期条目
            content = content.replaceAll("\\s+'" + Pattern.quote(missingDate) + "',", "");
            content = content.replaceAll("\\s+'" + Pattern.quote(missingDate) + "'", "");
        }
        
        // 写回文件
        Files.writeString(Paths.get(filePath), content);
        
        System.out.println("已从 calendar_index.html 中移除 " + MISSING_DATES.size() + " 个无效日期链接");
        System.out.println("移除的日期: " + MISSING_DATES);
    }
}
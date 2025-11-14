import java.io.*;
import java.nio.file.*;
import java.util.regex.*;
import java.util.*;

public class VerifyCalendarFix {
    public static void main(String[] args) {
        try {
            String calendarPath = "c:\\Users\\ASUS\\Desktop\\游戏\\星如雨[2872215021]\\calendar_index.html";
            verifyCalendarFix(calendarPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void verifyCalendarFix(String filePath) throws IOException {
        // 读取文件内容
        String content = Files.readString(Paths.get(filePath));
        
        // 提取JavaScript中的日期
        Pattern pattern = Pattern.compile("'(\\d{4}-\\d{2}-\\d{2})'");
        Matcher matcher = pattern.matcher(content);
        
        Set<String> datesInCalendar = new HashSet<>();
        while (matcher.find()) {
            datesInCalendar.add(matcher.group(1));
        }
        
        System.out.println("=== 验证Calendar修复结果 ===");
        System.out.println("Calendar中的日期总数: " + datesInCalendar.size());
        
        // 检查这些日期对应的文件是否存在
        int existingFiles = 0;
        int missingFiles = 0;
        Set<String> missingDates = new HashSet<>();
        
        for (String date : datesInCalendar) {
            String fileName = "chat_" + date.replace("-", "_") + ".html";
            Path filePath2 = Paths.get("c:\\Users\\ASUS\\Desktop\\游戏\\星如雨[2872215021]\\Data", fileName);
            
            if (Files.exists(filePath2)) {
                existingFiles++;
            } else {
                missingFiles++;
                missingDates.add(date);
            }
        }
        
        System.out.println("存在的文件数: " + existingFiles);
        System.out.println("缺失的文件数: " + missingFiles);
        
        if (missingFiles > 0) {
            System.out.println("仍然缺失的文件:");
            for (String date : missingDates) {
                System.out.println("  " + date + " -> chat_" + date.replace("-", "_") + ".html");
            }
        } else {
            System.out.println("✅ 所有Calendar中的日期都有对应的文件！");
        }
    }
}
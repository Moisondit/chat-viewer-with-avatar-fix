import java.io.*;
import java.nio.file.*;
import java.util.regex.*;

public class FixDatePageStyles {
    public static void main(String[] args) {
        String dataDir = "c:\\Users\\ASUS\\Desktop\\游戏\\星如雨[2872215021]\\Data";
        File directory = new File(dataDir);
        
        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("Data目录不存在");
            return;
        }
        
        File[] files = directory.listFiles((dir, name) -> name.startsWith("chat_") && name.endsWith(".html"));
        
        if (files == null) {
            System.out.println("没有找到聊天记录文件");
            return;
        }
        
        int processed = 0;
        int modified = 0;
        
        for (File file : files) {
            processed++;
            try {
                if (addNavigationStyles(file)) {
                    modified++;
                    System.out.println("已修改: " + file.getName());
                }
            } catch (Exception e) {
                System.err.println("处理文件出错: " + file.getName() + " - " + e.getMessage());
            }
        }
        
        System.out.println("\n处理完成！");
        System.out.println("总文件数: " + processed);
        System.out.println("修改文件数: " + modified);
    }
    
    private static boolean addNavigationStyles(File file) throws IOException {
        String content = Files.readString(file.toPath());
        
        // 检查是否已经包含了导航样式
        if (content.contains(".nav-btn") && content.contains(".navigation")) {
            return false; // 已经有样式了，不需要修改
        }
        
        // 查找back-link样式结束的位置
        Pattern pattern = Pattern.compile("(\n    \\.back-link:hover \\{[^}]+\\})");
        Matcher matcher = pattern.matcher(content);
        
        if (!matcher.find()) {
            return false; // 没有找到back-link样式
        }
        
        String newStyles = matcher.group(1) + "\n\n    .navigation {\n        text-align: center;\n        margin: 20px 0;\n    }\n\n    .nav-btn {\n        display: inline-block;\n        background: linear-gradient(135deg, #28a745 0%, #20c997 100%);\n        color: white;\n        padding: 12px 24px;\n        border-radius: 8px;\n        text-decoration: none;\n        font-weight: bold;\n        transition: transform 0.3s, box-shadow 0.3s;\n        text-align: center;\n    }\n\n    .nav-btn:hover {\n        transform: translateY(-2px);\n        box-shadow: 0 5px 15px rgba(40, 167, 69, 0.3);\n        text-decoration: none;\n        color: white;\n    }\n\n    .bottom-nav {\n        margin-top: 30px;\n        padding-top: 20px;\n        border-top: 1px solid #e9ecef;\n    }";
        
        content = content.replace(matcher.group(1), newStyles);
        
        Files.writeString(file.toPath(), content);
        return true;
    }
}
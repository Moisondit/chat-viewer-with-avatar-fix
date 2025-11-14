import java.io.*;
import java.nio.file.*;
import java.util.regex.*;

public class FixNavigationLinks {
    public static void main(String[] args) {
        try {
            // 修复chat_index.html中的链接
            String indexPath = "c:\\Users\\ASUS\\Desktop\\游戏\\星如雨[2872215021]\\chat_index.html";
            fixChatIndexLinks(indexPath);
            
            System.out.println("=== 导航链接修复完成 ===");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void fixChatIndexLinks(String filePath) throws IOException {
        // 读取文件内容
        String content = Files.readString(Paths.get(filePath));
        
        // 替换错误的链接路径
        String fixedContent = content.replaceAll("星如雨\\[2872215021\\]/chat_page_", "chat/chat_page_");
        
        // 写回文件
        Files.writeString(Paths.get(filePath), fixedContent);
        
        System.out.println("已修复 chat_index.html 中的导航链接");
    }
}
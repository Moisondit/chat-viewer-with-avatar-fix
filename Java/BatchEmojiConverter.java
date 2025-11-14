import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import java.util.Arrays;

public class BatchEmojiConverter {
    private static Map<String, String> emojiMap = new HashMap<>();
    private static final String CHAT_FOLDER = "c:\\Users\\ASUS\\Desktop\\游戏\\星如雨[2872215021]\\chat";
    private static final String DATA_FOLDER = "c:\\Users\\ASUS\\Desktop\\游戏\\星如雨[2872215021]\\Data";
    private static final String EMOTICON_PATH = "../生生[2137463976]/emoticon/new/";
    
    public static void main(String[] args) {
        try {
            // 加载emoji映射
            loadEmojiMapping();
            
            // 处理chat文件夹中的文件
            List<File> allFiles = new ArrayList<>();
            
            File chatDir = new File(CHAT_FOLDER);
            File[] chatFiles = chatDir.listFiles((dir, name) -> name.startsWith("chat_page_") && name.endsWith(".html"));
            if (chatFiles != null) {
                allFiles.addAll(Arrays.asList(chatFiles));
            }
            
            // 处理Data文件夹中的文件
            File dataDir = new File(DATA_FOLDER);
            File[] dataFiles = dataDir.listFiles((dir, name) -> name.startsWith("chat_") && name.endsWith(".html"));
            if (dataFiles != null) {
                allFiles.addAll(Arrays.asList(dataFiles));
            }
            
            if (allFiles.isEmpty()) {
                System.out.println("未找到任何chat文件");
                return;
            }
            
            System.out.println("找到 " + allFiles.size() + " 个chat文件，开始处理...");
            
            int processedFiles = 0;
            int replacedEmojis = 0;
            
            // 处理所有文件
            for (File file : allFiles) {
                int replacements = processFile(file);
                processedFiles++;
                if (replacements > 0) {
                    replacedEmojis += replacements;
                    System.out.println("处理文件: " + file.getName() + " - 替换了 " + replacements + " 个emoji");
                } else {
                    System.out.println("处理文件: " + file.getName() + " - 未找到emoji");
                }
            }
            
            System.out.println("\n处理完成！");
            System.out.println("共处理 " + processedFiles + " 个文件");
            System.out.println("共替换 " + replacedEmojis + " 个emoji");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void loadEmojiMapping() throws Exception {
        System.out.println("加载emoji映射配置...");
        
        String configContent = Files.readString(Paths.get("c:\\Users\\ASUS\\Desktop\\游戏\\星如雨[2872215021]\\生生[2137463976]\\emoticon\\face_config.json"));
        
        // 使用更精确的正则表达式解析JSON，匹配QDes和EMCode字段
        Pattern pattern = Pattern.compile("\"QDes\"\\s*:\\s*\"([^\"]+)\"[^}]*\"EMCode\"\\s*:\\s*\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(configContent);
        
        while (matcher.find()) {
            String qdes = matcher.group(1);
            String emCode = matcher.group(2);
            
            if (qdes != null && emCode != null) {
                String imagePath = EMOTICON_PATH + "s" + emCode + ".png";
                emojiMap.put(qdes, imagePath);
            }
        }
        
        // 手动添加缺失的emoji映射
            emojiMap.put("/敲敲", "290");
            emojiMap.put("/坚强", "291"); 
            emojiMap.put("/超级赞", "292");
            emojiMap.put("/太气了", "293");
            emojiMap.put("/拜谢", "10285");
            emojiMap.put("/摸鱼", "10285");
            emojiMap.put("/疑问", "132");
            emojiMap.put("/庆祝", "400198");
            emojiMap.put("/崇拜", "10276");
            emojiMap.put("/比心", "10274");
            emojiMap.put("/大怨种", "10278");
            emojiMap.put("/汪汪", "10277");
            emojiMap.put("/我酸了", "10273");
        
        System.out.println("加载了 " + emojiMap.size() + " 个emoji映射");
        
        // 输出一些映射示例
        int count = 0;
        for (Map.Entry<String, String> entry : emojiMap.entrySet()) {
            if (count < 10) {
                System.out.println("  " + entry.getKey() + " -> " + entry.getValue());
                count++;
            }
        }
    }
    
    private static int processFile(File file) throws IOException {
        String content = Files.readString(file.toPath());
        String originalContent = content;
        
        // 简化匹配：直接查找任何包含/的message-content
        Pattern pattern = Pattern.compile("<div class=\"message-content\">([^<]*)</div>");
        Matcher matcher = pattern.matcher(content);
        
        StringBuffer sb = new StringBuffer();
        int replacements = 0;
        boolean foundMatches = false;
        
        while (matcher.find()) {
            foundMatches = true;
            String messageContent = matcher.group(1);
            String processedMessage = replaceEmojisInMessage(messageContent);
            
            if (!messageContent.equals(processedMessage)) {
                // 使用字面量替换避免正则表达式特殊字符问题
                String replacement = "<div class=\"message-content\">" + processedMessage + "</div>";
                matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
                replacements += countReplacements(messageContent, processedMessage);
            } else {
                matcher.appendReplacement(sb, Matcher.quoteReplacement(matcher.group()));
            }
        }
        matcher.appendTail(sb);
        
        // 调试信息
        if (foundMatches) {
            System.out.println("文件 " + file.getName() + " 找到了message-content，替换了 " + replacements + " 个emoji");
        } else {
            System.out.println("文件 " + file.getName() + " 未找到message-content");
        }
        
        // 如果有替换，写入文件
        if (!originalContent.equals(sb.toString())) {
            Files.writeString(file.toPath(), sb.toString());
            System.out.println("文件 " + file.getName() + " 已更新并保存");
        }
        
        return replacements;
    }
    
    private static String replaceEmojisInMessage(String message) {
        String result = message;
        
        // 对每个emoji进行替换
        for (Map.Entry<String, String> entry : emojiMap.entrySet()) {
            String emojiText = entry.getKey();
            String imagePath = entry.getValue();
            
            // 如果消息包含这个emoji，进行替换
            if (result.contains(emojiText)) {
                // 创建HTML img标签
                String imgTag = "<img src=\"" + imagePath + "\" alt=\"" + emojiText + "\" style=\"width: 24px; height: 24px; vertical-align: middle; margin: 0 2px;\">";
                
                // 替换消息中的emoji文本
                result = result.replace(emojiText, imgTag);
            }
        }
        
        return result;
    }
    
    private static int countReplacements(String original, String processed) {
        int count = 0;
        // 计算原始消息中有多少个emoji
        for (Map.Entry<String, String> entry : emojiMap.entrySet()) {
            String emojiText = entry.getKey();
            int originalCount = countOccurrences(original, emojiText);
            count += originalCount;
        }
        return count;
    }
    
    private static int countOccurrences(String text, String substring) {
        int count = 0;
        int index = 0;
        while ((index = text.indexOf(substring, index)) != -1) {
            count++;
            index += substring.length();
        }
        return count;
    }
}
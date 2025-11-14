import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatSplitter {
    
    private static final String txtFile = "c:\\Users\\ASUS\\Desktop\\游戏\\星如雨[2872215021]\\生生[2137463976].txt";
    private static final String outputDir = "c:\\Users\\ASUS\\Desktop\\游戏\\星如雨[2872215021]\\星如雨[2872215021]\\";
    private static final int MESSAGES_PER_PAGE = 1000; // 每页1000条消息
    
    public static void main(String[] args) {
        try {
            System.out.println("开始分页转换聊天记录...");
            
            // 确保输出目录存在
            Files.createDirectories(Paths.get(outputDir));
            
            // 读取原始文本文件
            List<String> messages = readChatMessages(txtFile);
            System.out.println("总共读取到 " + messages.size() + " 条消息");
            
            // 分页处理
            int totalPages = (int) Math.ceil((double) messages.size() / MESSAGES_PER_PAGE);
            System.out.println("将分成 " + totalPages + " 页");
            
            // 创建分页文件
            for (int page = 1; page <= totalPages; page++) {
                int startIndex = (page - 1) * MESSAGES_PER_PAGE;
                int endIndex = Math.min(page * MESSAGES_PER_PAGE, messages.size());
                
                List<String> pageMessages = messages.subList(startIndex, endIndex);
                createPageHtml(page, pageMessages, totalPages, startIndex + 1, endIndex);
                
                System.out.println("已生成第 " + page + " 页，包含 " + pageMessages.size() + " 条消息");
            }
            
            // 创建主索引页面
            createIndexHtml(totalPages, messages.size());
            
            System.out.println("分页转换完成！");
            System.out.println("请打开: " + outputDir + "chat_index.html 查看所有分页");
            
        } catch (Exception e) {
            System.err.println("转换过程中发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static List<String> readChatMessages(String filePath) throws IOException {
        List<String> messages = new ArrayList<>();
        
        // 尝试不同编码读取文件
        String[] encodings = {"GBK", "UTF-8", "GB2312"};
        String content = null;
        
        for (String encoding : encodings) {
            try {
                content = new String(Files.readAllBytes(Paths.get(filePath)), encoding);
                System.out.println("成功使用 " + encoding + " 编码读取文件");
                break;
            } catch (IOException e) {
                System.out.println("使用 " + encoding + " 编码读取失败，尝试下一种编码");
            }
        }
        
        if (content == null) {
            throw new IOException("无法读取文件，请检查文件路径和编码");
        }
        
        // 分割消息
        String[] lines = content.split("\n");
        StringBuilder currentMessage = new StringBuilder();
        
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            
            // 检查是否是新消息的开始（包含发送者和时间）
            if (line.matches(".*\\d{4}-\\d{2}-\\d{2}.*") || 
                line.matches(".*\\d{2}:\\d{2}:\\d{2}.*") ||
                line.contains("说:") || line.contains("发送了") ||
                line.contains("分享了") || line.contains("拍了拍")) {
                
                if (currentMessage.length() > 0) {
                    messages.add(currentMessage.toString());
                }
                currentMessage = new StringBuilder(line);
            } else {
                // 继续当前消息
                if (currentMessage.length() > 0) {
                    currentMessage.append("\n").append(line);
                } else {
                    currentMessage.append(line);
                }
            }
        }
        
        // 添加最后一条消息
        if (currentMessage.length() > 0) {
            messages.add(currentMessage.toString());
        }
        
        return messages;
    }
    
    private static void createPageHtml(int pageNum, List<String> messages, int totalPages, 
                                     int startMsgNum, int endMsgNum) throws IOException {
        StringBuilder html = new StringBuilder();
        
        html.append("<!DOCTYPE html>\n");
        html.append("<html lang=\"zh-CN\">\n");
        html.append("<head>\n");
        html.append("    <meta charset=\"UTF-8\">\n");
        html.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        html.append("    <title>聊天记录 - 第").append(pageNum).append("页</title>\n");
        html.append("    <style>\n");
        html.append(getPageStyles());
        html.append("    </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");
        
        // 页面头部
        html.append("    <div class=\"header\">\n");
        html.append("        <h1>聊天记录 - 第").append(pageNum).append("页</h1>\n");
        html.append("        <p class=\"page-info\">消息 ").append(startMsgNum)
            .append(" - ").append(endMsgNum).append(" (共 ").append(messages.size())
            .append(" 条)</p>\n");
        html.append("    </div>\n");
        
        // 导航栏
        html.append("    <div class=\"navigation\">\n");
        html.append("        <a href=\"chat_index.html\" class=\"nav-btn\">返回首页</a>\n");
        if (pageNum > 1) {
            html.append("        <a href=\"chat_page_").append(pageNum - 1)
                .append(".html\" class=\"nav-btn\">上一页</a>\n");
        }
        if (pageNum < totalPages) {
            html.append("        <a href=\"chat_page_").append(pageNum + 1)
                .append(".html\" class=\"nav-btn\">下一页</a>\n");
        }
        html.append("    </div>\n");
        
        // 消息容器
        html.append("    <div class=\"chat-container\">\n");
        
        for (String message : messages) {
            html.append(formatMessage(message));
        }
        
        html.append("    </div>\n");
        
        // 底部导航
        html.append("    <div class=\"navigation bottom-nav\">\n");
        html.append("        <a href=\"chat_index.html\" class=\"nav-btn\">返回首页</a>\n");
        if (pageNum > 1) {
            html.append("        <a href=\"chat_page_").append(pageNum - 1)
                .append(".html\" class=\"nav-btn\">上一页</a>\n");
        }
        if (pageNum < totalPages) {
            html.append("        <a href=\"chat_page_").append(pageNum + 1)
                .append(".html\" class=\"nav-btn\">下一页</a>\n");
        }
        html.append("    </div>\n");
        
        html.append("</body>\n");
        html.append("</html>\n");
        
        // 写入文件
        String fileName = "chat_page_" + pageNum + ".html";
        Files.write(Paths.get(outputDir + fileName), html.toString().getBytes(StandardCharsets.UTF_8));
    }
    
    private static void createIndexHtml(int totalPages, int totalMessages) throws IOException {
        StringBuilder html = new StringBuilder();
        
        html.append("<!DOCTYPE html>\n");
        html.append("<html lang=\"zh-CN\">\n");
        html.append("<head>\n");
        html.append("    <meta charset=\"UTF-8\">\n");
        html.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        html.append("    <title>聊天记录 - 目录</title>\n");
        html.append("    <style>\n");
        html.append(getIndexStyles());
        html.append("    </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");
        
        html.append("    <div class=\"container\">\n");
        html.append("        <h1>聊天记录目录</h1>\n");
        html.append("        <div class=\"summary\">\n");
        html.append("            <p>总消息数: ").append(totalMessages).append(" 条</p>\n");
        html.append("            <p>总页数: ").append(totalPages).append(" 页</p>\n");
        html.append("            <p>每页消息: ").append(MESSAGES_PER_PAGE).append(" 条</p>\n");
        html.append("        </div>\n");
        
        html.append("        <div class=\"page-links\">\n");
        for (int i = 1; i <= totalPages; i++) {
            int startMsg = (i - 1) * MESSAGES_PER_PAGE + 1;
            int endMsg = Math.min(i * MESSAGES_PER_PAGE, totalMessages);
            
            html.append("            <a href=\"chat_page_").append(i)
                .append(".html\" class=\"page-link\">\n");
            html.append("                <div class=\"page-card\">\n");
            html.append("                    <h3>第 ").append(i).append(" 页</h3>\n");
            html.append("                    <p>消息 ").append(startMsg).append(" - ").append(endMsg).append("</p>\n");
            html.append("                </div>\n");
            html.append("            </a>\n");
        }
        html.append("        </div>\n");
        html.append("    </div>\n");
        
        html.append("</body>\n");
        html.append("</html>\n");
        
        Files.write(Paths.get(outputDir + "chat_index.html"), html.toString().getBytes(StandardCharsets.UTF_8));
    }
    
    private static String formatMessage(String message) {
        StringBuilder formatted = new StringBuilder();
        
        // 尝试提取发送者和时间
        String sender = "未知";
        String time = "";
        String content = message;
        
        // 匹配发送者和时间格式
        Pattern pattern = Pattern.compile("^(.+?)\\s+(\\d{4}-\\d{2}-\\d{2}\\s+\\d{2}:\\d{2}:\\d{2})\\s*(.*)$");
        Matcher matcher = pattern.matcher(message);
        
        if (matcher.find()) {
            sender = matcher.group(1).trim();
            time = matcher.group(2);
            content = matcher.group(3);
        } else {
            // 尝试其他格式
            if (message.contains("说:")) {
                String[] parts = message.split("说:", 2);
                if (parts.length == 2) {
                    sender = parts[0].trim();
                    content = "说:" + parts[1];
                }
            }
        }
        
        // 判断消息类型和对应的CSS类
        String messageClass = "message-left";
        String avatarSrc = "../生生[2137463976]/individual-image/2137463976-生生.jpg";
        String senderColorClass = "sender-shengsheng";
        
        if (sender.equals("我") || sender.contains("星如雨")) {
            messageClass = "message-right";
            avatarSrc = "../生生[2137463976]/individual-image/2872215021-星如雨.jpg";
            senderColorClass = "sender-me";
        }
        
        formatted.append("        <div class=\"message ").append(messageClass).append("\">\n");
        formatted.append("            <img class=\"avatar\" src=\"").append(avatarSrc).append("\" alt=\"").append(sender).append("\">\n");
        formatted.append("            <div class=\"message-content-wrapper\">\n");
        formatted.append("                <div class=\"message-header\">\n");
        formatted.append("                    <span class=\"sender ").append(senderColorClass).append("\">").append(sender).append("</span>\n");
        if (!time.isEmpty()) {
            formatted.append("                    <span class=\"time\">").append(time).append("</span>\n");
        }
        formatted.append("                </div>\n");
        formatted.append("                <div class=\"message-content\">").append(content.replace("\n", "<br>")).append("</div>\n");
        formatted.append("            </div>\n");
        formatted.append("        </div>\n");
        
        return formatted.toString();
    }
    
    private static String getPageStyles() {
        return """
            body {
                font-family: 'Microsoft YaHei', Arial, sans-serif;
                line-height: 1.6;
                margin: 0;
                padding: 20px;
                background-color: #f5f5f5;
            }
            .header {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
                padding: 20px;
                border-radius: 10px;
                margin-bottom: 20px;
                text-align: center;
            }
            .header h1 {
                margin: 0;
                font-size: 28px;
            }
            .page-info {
                margin: 10px 0 0 0;
                opacity: 0.9;
                font-size: 16px;
            }
            .navigation {
                text-align: center;
                margin: 20px 0;
                padding: 15px;
                background: white;
                border-radius: 8px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            }
            .nav-btn {
                display: inline-block;
                margin: 0 10px;
                padding: 10px 20px;
                background: #007bff;
                color: white;
                text-decoration: none;
                border-radius: 5px;
                transition: background-color 0.3s;
            }
            .nav-btn:hover {
                background: #0056b3;
            }
            .bottom-nav {
                margin-top: 30px;
            }
            .chat-container {
                background: white;
                border-radius: 10px;
                padding: 20px;
                box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            }
            .message {
                display: flex;
                margin-bottom: 20px;
                align-items: flex-start;
                transition: transform 0.2s ease;
            }
            .message:hover {
                transform: translateY(-2px);
            }
            .message-left {
                justify-content: flex-start;
            }
            .message-right {
                justify-content: flex-end;
            }
            .avatar {
                width: 40px;
                height: 40px;
                border-radius: 50%;
                border: 2px solid #ddd;
                object-fit: cover;
                margin: 0 10px;
            }
            .message-left .avatar {
                order: 1;
            }
            .message-right .avatar {
                order: 2;
            }
            .message-content-wrapper {
                max-width: 70%;
                background: #f8f9fa;
                border-radius: 18px;
                padding: 12px 16px;
                box-shadow: 0 1px 2px rgba(0,0,0,0.1);
                position: relative;
            }
            .message-left .message-content-wrapper {
                order: 2;
                background: #e9ecef;
                border-bottom-left-radius: 4px;
            }
            .message-right .message-content-wrapper {
                order: 1;
                background: #007bff;
                color: white;
                border-bottom-right-radius: 4px;
            }
            .message-header {
                display: flex;
                justify-content: space-between;
                margin-bottom: 6px;
                font-weight: bold;
                font-size: 12px;
            }
            .sender-shengsheng {
                color: #e74c3c;
            }
            .sender-me {
                color: #3498db;
            }
            .message-right .sender-me {
                color: #ffffff;
            }
            .time {
                color: #666;
                font-size: 11px;
                opacity: 0.7;
            }
            .message-right .time {
                color: rgba(255,255,255,0.7);
            }
            .message-content {
                word-wrap: break-word;
                line-height: 1.4;
            }
            @media (max-width: 768px) {
                body {
                    padding: 10px;
                }
                .nav-btn {
                    display: block;
                    margin: 5px 0;
                }
                .message-content-wrapper {
                    max-width: 85%;
                }
                .avatar {
                    width: 35px;
                    height: 35px;
                }
            }
            @media (max-width: 480px) {
                .message-content-wrapper {
                    max-width: 90%;
                }
                .avatar {
                    width: 30px;
                    height: 30px;
                    margin: 0 8px;
                }
            }
        """;
    }
    
    private static String getIndexStyles() {
        return """
            body {
                font-family: 'Microsoft YaHei', Arial, sans-serif;
                line-height: 1.6;
                margin: 0;
                padding: 20px;
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                min-height: 100vh;
            }
            .container {
                max-width: 800px;
                margin: 0 auto;
                background: white;
                border-radius: 15px;
                padding: 30px;
                box-shadow: 0 10px 30px rgba(0,0,0,0.2);
            }
            h1 {
                text-align: center;
                color: #333;
                margin-bottom: 30px;
                font-size: 32px;
            }
            .summary {
                background: #f8f9fa;
                padding: 20px;
                border-radius: 10px;
                margin-bottom: 30px;
                text-align: center;
            }
            .summary p {
                margin: 5px 0;
                font-size: 18px;
                color: #555;
            }
            .page-links {
                display: grid;
                grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
                gap: 15px;
            }
            .page-link {
                text-decoration: none;
                color: inherit;
            }
            .page-card {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
                padding: 20px;
                border-radius: 10px;
                text-align: center;
                transition: transform 0.3s, box-shadow 0.3s;
                cursor: pointer;
            }
            .page-card:hover {
                transform: translateY(-5px);
                box-shadow: 0 5px 20px rgba(0,0,0,0.3);
            }
            .page-card h3 {
                margin: 0 0 10px 0;
                font-size: 20px;
            }
            .page-card p {
                margin: 0;
                opacity: 0.9;
            }
            @media (max-width: 768px) {
                .container {
                    padding: 20px;
                    margin: 10px;
                }
                .page-links {
                    grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
                }
            }
        """;
    }
}
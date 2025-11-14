import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatCalendarSplitter {
    
    private static final String txtFile = "c:\\Users\\ASUS\\Desktop\\游戏\\星如雨[2872215021]\\生生[2137463976].txt";
    private static final String outputDir = "c:\\Users\\ASUS\\Desktop\\游戏\\星如雨[2872215021]\\Data\\";
    private static final DateTimeFormatter[] dateFormats = {
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
        DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"),
        DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss")
    };
    
    public static void main(String[] args) {
        try {
            System.out.println("开始按日期分析聊天记录...");
            
            // 确保输出目录存在
            Files.createDirectories(Paths.get(outputDir));
            
            // 读取原始文本文件并按日期分组
            Map<LocalDate, List<String>> datedMessages = readAndGroupMessagesByDate(txtFile);
            System.out.println("总共分析到 " + datedMessages.size() + " 个有聊天记录的日期");
            
            // 创建按日期分类的HTML文件
            createDatedHtmlFiles(datedMessages);
            
            // 创建带日历的主页面
            createCalendarMainPage(datedMessages);
            
            System.out.println("日历分页转换完成！");
            System.out.println("请打开: " + outputDir + "calendar_index.html 查看日历导航");
            
        } catch (Exception e) {
            System.err.println("转换过程中发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static Map<LocalDate, List<String>> readAndGroupMessagesByDate(String filePath) throws IOException {
        Map<LocalDate, List<String>> datedMessages = new TreeMap<>();
        
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
        LocalDate currentDate = null;
        
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            
            // 尝试提取日期
            LocalDate extractedDate = extractDateFromLine(line);
            
            // 检查是否是新消息的开始
            if (extractedDate != null || 
                line.matches(".*\\d{4}-\\d{2}-\\d{2}.*") || 
                line.matches(".*\\d{2}:\\d{2}:\\d{2}.*") ||
                line.contains("说:") || line.contains("发送了") ||
                line.contains("分享了") || line.contains("拍了拍")) {
                
                if (currentMessage.length() > 0 && currentDate != null) {
                    datedMessages.computeIfAbsent(currentDate, k -> new ArrayList<>()).add(currentMessage.toString());
                }
                
                currentMessage = new StringBuilder(line);
                if (extractedDate != null) {
                    currentDate = extractedDate;
                }
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
        if (currentMessage.length() > 0 && currentDate != null) {
            datedMessages.computeIfAbsent(currentDate, k -> new ArrayList<>()).add(currentMessage.toString());
        }
        
        return datedMessages;
    }
    
    private static LocalDate extractDateFromLine(String line) {
        // 尝试匹配各种日期格式
        Pattern[] patterns = {
            Pattern.compile("(\\d{4}-\\d{2}-\\d{2})\\s+\\d{2}:\\d{2}:\\d{2}"),
            Pattern.compile("(\\d{4}/\\d{2}/\\d{2})\\s+\\d{2}:\\d{2}:\\d{2}"),
            Pattern.compile("(\\d{4}年\\d{2}月\\d{2}日)\\s+\\d{2}:\\d{2}:\\d{2}")
        };
        
        for (Pattern pattern : patterns) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                String dateStr = matcher.group(1);
                for (DateTimeFormatter formatter : dateFormats) {
                    try {
                        return LocalDate.parse(dateStr + " 00:00:00", formatter);
                    } catch (DateTimeParseException e) {
                        // 继续尝试下一个格式
                    }
                }
            }
        }
        return null;
    }
    
    private static void createDatedHtmlFiles(Map<LocalDate, List<String>> datedMessages) throws IOException {
        for (Map.Entry<LocalDate, List<String>> entry : datedMessages.entrySet()) {
            LocalDate date = entry.getKey();
            List<String> messages = entry.getValue();
            
            createDatedHtmlFile(date, messages);
        }
    }
    
    private static void createDatedHtmlFile(LocalDate date, List<String> messages) throws IOException {
        StringBuilder html = new StringBuilder();
        
        html.append("<!DOCTYPE html>\n");
        html.append("<html lang=\"zh-CN\">\n");
        html.append("<head>\n");
        html.append("    <meta charset=\"UTF-8\">\n");
        html.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        html.append("    <title>聊天记录 - ").append(date.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"))).append("</title>\n");
        html.append("    <style>\n");
        html.append(getChatPageStyles());
        html.append("    </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");
        
        // 页面头部
        html.append("    <div class=\"header\">\n");
        html.append("        <h1>").append(date.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"))).append("</h1>\n");
        html.append("        <p class=\"date-info\">共 ").append(messages.size()).append(" 条消息</p>\n");
        html.append("    </div>\n");
        
        // 导航栏
        html.append("    <div class=\"navigation\">\n");
        html.append("        <a href=\"calendar_index.html\" class=\"nav-btn\">返回日历</a>\n");
        html.append("    </div>\n");
        
        // 消息容器
        html.append("    <div class=\"chat-container\">\n");
        
        for (String message : messages) {
            html.append(formatMessage(message));
        }
        
        html.append("    </div>\n");
        
        // 底部导航
        html.append("    <div class=\"navigation bottom-nav\">\n");
        html.append("        <a href=\"calendar_index.html\" class=\"nav-btn\">返回日历</a>\n");
        html.append("    </div>\n");
        
        html.append("</body>\n");
        html.append("</html>\n");
        
        // 写入文件
        String fileName = "chat_" + date.format(DateTimeFormatter.ofPattern("yyyy_MM_dd")) + ".html";
        String filePath = outputDir + fileName;
        
        try {
            System.out.println("正在创建文件: " + filePath);
            Files.write(Paths.get(filePath), html.toString().getBytes(StandardCharsets.UTF_8));
            System.out.println("文件创建成功: " + filePath);
        } catch (IOException e) {
            System.err.println("创建文件失败: " + filePath + " - " + e.getMessage());
            throw e;
        }
    }
    
    private static void createCalendarMainPage(Map<LocalDate, List<String>> datedMessages) throws IOException {
        StringBuilder html = new StringBuilder();
        
        // 获取日期范围
        LocalDate minDate = datedMessages.keySet().stream().min(LocalDate::compareTo).orElse(LocalDate.now());
        LocalDate maxDate = datedMessages.keySet().stream().max(LocalDate::compareTo).orElse(LocalDate.now());
        
        html.append("<!DOCTYPE html>\n");
        html.append("<html lang=\"zh-CN\">\n");
        html.append("<head>\n");
        html.append("    <meta charset=\"UTF-8\">\n");
        html.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        html.append("    <title>聊天记录日历</title>\n");
        html.append("    <style>\n");
        html.append(getCalendarStyles());
        html.append("    </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");
        
        html.append("    <div class=\"container\">\n");
        html.append("        <h1>聊天记录日历</h1>\n");
        html.append("        <div class=\"summary\">\n");
        html.append("            <p>总聊天天数: ").append(datedMessages.size()).append(" 天</p>\n");
        html.append("            <p>日期范围: ").append(minDate.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")))
            .append(" - ").append(maxDate.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"))).append("</p>\n");
        html.append("        </div>\n");
        
        // 年份选择器
        html.append("        <div class=\"year-selector\">\n");
        html.append("            <label for=\"yearSelect\">选择年份:</label>\n");
        html.append("            <select id=\"yearSelect\" onchange=\"showYear(this.value)\">\n");
        
        Set<Integer> years = new TreeSet<>();
        for (LocalDate date : datedMessages.keySet()) {
            years.add(date.getYear());
        }
        
        for (int year : years) {
            html.append("                <option value=\"").append(year).append("\">").append(year).append("年</option>\n");
        }
        
        html.append("            </select>\n");
        html.append("        </div>\n");
        
        // 月份日历容器
        html.append("        <div id=\"calendarContainer\"></div>\n");
        
        html.append("    </div>\n");
        
        html.append("    <script>\n");
        html.append("        const chatDates = new Set([\n");
        
        for (LocalDate date : datedMessages.keySet()) {
            html.append("            '").append(date.toString()).append("',\n");
        }
        
        html.append("        ]);\n");
        html.append(getCalendarScript());
        html.append("    </script>\n");
        
        html.append("</body>\n");
        html.append("</html>\n");
        
        String calendarPath = outputDir + "calendar_index.html";
        try {
            System.out.println("正在创建日历文件: " + calendarPath);
            Files.write(Paths.get(calendarPath), html.toString().getBytes(StandardCharsets.UTF_8));
            System.out.println("日历文件创建成功: " + calendarPath);
        } catch (IOException e) {
            System.err.println("创建日历文件失败: " + calendarPath + " - " + e.getMessage());
            throw e;
        }
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
        
        // 判断消息发送者并设置对应的样式类和对齐方式
        boolean isShengSheng = sender.contains("生生") || sender.contains("2137463976");
        String messageClass = isShengSheng ? "message shengsheng-message" : "message user-message";
        String alignClass = isShengSheng ? "message-left" : "message-right";
        String avatarPath = isShengSheng ? 
            "../生生[2137463976]/individual-image/2137463976-生生.jpg" : 
            "../生生[2137463976]/individual-image/2872215021-星如雨.jpg";
        String senderName = isShengSheng ? "生生" : "星如雨";
        
        formatted.append("        <div class=\"").append(messageClass).append(" ").append(alignClass).append("\">\n");
        formatted.append("            <div class=\"message-avatar\">\n");
        formatted.append("                <img src=\"").append(avatarPath).append("\" alt=\"").append(senderName).append("\" class=\"avatar-img\">\n");
        formatted.append("            </div>\n");
        formatted.append("            <div class=\"message-content-wrapper\">\n");
        formatted.append("                <div class=\"message-header\">\n");
        formatted.append("                    <span class=\"sender\">").append(senderName).append("</span>\n");
        if (!time.isEmpty()) {
            formatted.append("                    <span class=\"time\">").append(time).append("</span>\n");
        }
        formatted.append("                </div>\n");
        formatted.append("                <div class=\"message-content\">").append(content.replace("\n", "<br>")).append("</div>\n");
        formatted.append("            </div>\n");
        formatted.append("        </div>\n");
        
        return formatted.toString();
    }
    
    private static String getChatPageStyles() {
        return """
        <style>
            body {
                font-family: 'Microsoft YaHei', Arial, sans-serif;
                margin: 0;
                padding: 20px;
                background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
                min-height: 100vh;
            }
            
            .header {
                text-align: center;
                margin-bottom: 30px;
                background: white;
                padding: 20px;
                border-radius: 15px;
                box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            }
            
            .header h1 {
                margin: 0;
                color: #2c3e50;
                font-size: 2.5em;
                font-weight: 300;
            }
            
            .header .date {
                color: #7f8c8d;
                font-size: 1.2em;
                margin-top: 10px;
            }
            
            .back-link {
                display: inline-block;
                margin-top: 15px;
                padding: 10px 20px;
                background: #3498db;
                color: white;
                text-decoration: none;
                border-radius: 25px;
                transition: all 0.3s ease;
            }
            
            .back-link:hover {
                background: #2980b9;
                transform: translateY(-2px);
            }
            
            .chat-container {
                max-width: 800px;
                margin: 0 auto;
                background: white;
                border-radius: 15px;
                box-shadow: 0 4px 15px rgba(0,0,0,0.1);
                overflow: hidden;
            }
            
            .message {
                display: flex;
                padding: 15px 20px;
                border-bottom: 1px solid #ecf0f1;
                transition: background-color 0.2s ease;
            }
            
            .message:hover {
                background-color: #f8f9fa;
            }
            
            .message:last-child {
                border-bottom: none;
            }
            
            /* 消息左右对齐样式 */
            .message-left {
                justify-content: flex-start;
            }
            
            .message-right {
                justify-content: flex-end;
            }
            
            /* 头像样式 */
            .message-avatar {
                flex-shrink: 0;
                margin-right: 12px;
            }
            
            .message-right .message-avatar {
                margin-right: 0;
                margin-left: 12px;
                order: 2;
            }
            
            .avatar-img {
                width: 40px;
                height: 40px;
                border-radius: 50%;
                object-fit: cover;
                border: 2px solid #e1e8ed;
            }
            
            .message-content-wrapper {
                max-width: 70%;
                display: flex;
                flex-direction: column;
            }
            
            .message-right .message-content-wrapper {
                align-items: flex-end;
            }
            
            .message-header {
                display: flex;
                align-items: center;
                margin-bottom: 5px;
                font-size: 0.9em;
            }
            
            .message-right .message-header {
                flex-direction: row-reverse;
            }
            
            .sender {
                font-weight: bold;
                color: #2c3e50;
                margin-right: 8px;
            }
            
            .message-right .sender {
                margin-right: 0;
                margin-left: 8px;
            }
            
            .time {
                color: #95a5a6;
                font-size: 0.85em;
            }
            
            .message-content {
                color: #34495e;
                line-height: 1.5;
                word-wrap: break-word;
                background: #f8f9fa;
                padding: 10px 15px;
                border-radius: 18px;
                border-top-left-radius: 4px;
            }
            
            .message-right .message-content {
                background: #3498db;
                color: white;
                border-top-left-radius: 18px;
                border-top-right-radius: 4px;
            }
            
            .shengsheng-message .sender {
                color: #e74c3c;
            }
            
            .user-message .sender {
                color: #3498db;
            }
            
            .stats {
                text-align: center;
                margin-top: 20px;
                color: #7f8c8d;
                font-size: 0.9em;
            }
            
            /* 响应式设计 */
            @media (max-width: 600px) {
                body {
                    padding: 10px;
                }
                
                .header h1 {
                    font-size: 2em;
                }
                
                .message {
                    padding: 12px 15px;
                }
                
                .avatar-img {
                    width: 35px;
                    height: 35px;
                }
                
                .message-content-wrapper {
                    max-width: 75%;
                }
            }
        </style>
        """;
    }
    
    private static String getCalendarStyles() {
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
                max-width: 1200px;
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
            .year-selector {
                text-align: center;
                margin-bottom: 30px;
            }
            .year-selector label {
                font-size: 18px;
                margin-right: 10px;
                color: #333;
            }
            .year-selector select {
                padding: 8px 15px;
                font-size: 16px;
                border: 2px solid #ddd;
                border-radius: 5px;
                background: white;
            }
            .calendar-grid {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
                gap: 20px;
                margin-bottom: 30px;
            }
            .month-calendar {
                background: #f8f9fa;
                border-radius: 10px;
                padding: 15px;
                box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            }
            .month-title {
                text-align: center;
                font-size: 18px;
                font-weight: bold;
                color: #333;
                margin-bottom: 15px;
                padding: 10px;
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
                border-radius: 5px;
            }
            .calendar-days {
                display: grid;
                grid-template-columns: repeat(7, 1fr);
                gap: 2px;
            }
            .day-header {
                text-align: center;
                font-weight: bold;
                padding: 8px 4px;
                background: #e9ecef;
                color: #495057;
                font-size: 12px;
            }
            .calendar-day {
                aspect-ratio: 1;
                display: flex;
                align-items: center;
                justify-content: center;
                border: 1px solid #dee2e6;
                cursor: pointer;
                transition: all 0.3s ease;
                font-size: 14px;
                background: white;
            }
            .calendar-day:hover {
                background: #f8f9fa;
                transform: scale(1.05);
            }
            .calendar-day.has-chat {
                background: #2c3e50;
                color: white;
                font-weight: bold;
            }
            .calendar-day.has-chat:hover {
                background: #34495e;
            }
            .calendar-day.no-chat {
                background: #e9ecef;
                color: #adb5bd;
                cursor: not-allowed;
            }
            .calendar-day.no-chat:hover {
                background: #e9ecef;
                transform: none;
            }
            @media (max-width: 768px) {
                .container {
                    padding: 20px;
                    margin: 10px;
                }
                .calendar-grid {
                    grid-template-columns: 1fr;
                }
            }
        """;
    }
    
    private static String getCalendarScript() {
        return """
            function showYear(year) {
                const container = document.getElementById('calendarContainer');
                container.innerHTML = '';
                
                const months = ['一月', '二月', '三月', '四月', '五月', '六月', 
                               '七月', '八月', '九月', '十月', '十一月', '十二月'];
                
                const calendarGrid = document.createElement('div');
                calendarGrid.className = 'calendar-grid';
                
                for (let month = 0; month < 12; month++) {
                    const monthDiv = document.createElement('div');
                    monthDiv.className = 'month-calendar';
                    
                    const monthTitle = document.createElement('div');
                    monthTitle.className = 'month-title';
                    monthTitle.textContent = months[month];
                    monthDiv.appendChild(monthTitle);
                    
                    const daysDiv = document.createElement('div');
                    daysDiv.className = 'calendar-days';
                    
                    // 添加星期标题
                    const weekDays = ['日', '一', '二', '三', '四', '五', '六'];
                    for (let day of weekDays) {
                        const dayHeader = document.createElement('div');
                        dayHeader.className = 'day-header';
                        dayHeader.textContent = day;
                        daysDiv.appendChild(dayHeader);
                    }
                    
                    // 获取月份的第一天和天数
                    const firstDay = new Date(year, month, 1).getDay();
                    const daysInMonth = new Date(year, month + 1, 0).getDate();
                    
                    // 添加空白日期
                    for (let i = 0; i < firstDay; i++) {
                        const emptyDay = document.createElement('div');
                        emptyDay.className = 'calendar-day';
                        emptyDay.style.visibility = 'hidden';
                        daysDiv.appendChild(emptyDay);
                    }
                    
                    // 添加日期
                    for (let day = 1; day <= daysInMonth; day++) {
                        const dayDiv = document.createElement('div');
                        dayDiv.className = 'calendar-day';
                        dayDiv.textContent = day;
                        
                        const dateStr = year + '-' + 
                                       String(month + 1).padStart(2, '0') + '-' + 
                                       String(day).padStart(2, '0');
                        
                        if (chatDates.has(dateStr)) {
                            dayDiv.classList.add('has-chat');
                            dayDiv.onclick = function() {
                                window.location.href = 'chat_' + year + '_' + 
                                                     String(month + 1).padStart(2, '0') + '_' + 
                                                     String(day).padStart(2, '0') + '.html';
                            };
                        } else {
                            dayDiv.classList.add('no-chat');
                        }
                        
                        daysDiv.appendChild(dayDiv);
                    }
                    
                    monthDiv.appendChild(daysDiv);
                    calendarGrid.appendChild(monthDiv);
                }
                
                container.appendChild(calendarGrid);
            }
            
            // 默认显示第一年
            document.addEventListener('DOMContentLoaded', function() {
                const yearSelect = document.getElementById('yearSelect');
                if (yearSelect.options.length > 0) {
                    showYear(yearSelect.options[0].value);
                }
            });
        """;
    }
}
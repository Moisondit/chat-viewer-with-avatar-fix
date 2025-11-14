import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class FixSearchPage {
    public static void main(String[] args) {
        try {
            System.out.println("开始修复搜索页面...");
            
            // 获取所有实际的聊天文件
            List<String> chatFiles = getAllChatFiles();
            System.out.println("找到 " + chatFiles.size() + " 个聊天文件");
            
            // 读取原始搜索页面
            String searchPageContent = readFile("search.html");
            
            // 修复文件列表部分
            String fixedContent = fixFileList(searchPageContent, chatFiles);
            
            // 修复搜索函数
            fixedContent = fixSearchFunction(fixedContent);
            
            // 修复HTML结构匹配
            fixedContent = fixHTMLStructure(fixedContent);
            
            // 写回文件
            writeFile("search.html", fixedContent);
            
            System.out.println("搜索页面修复完成！");
            
        } catch (Exception e) {
            System.err.println("修复搜索页面时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static List<String> getAllChatFiles() throws IOException {
        Path dataDir = Paths.get("Data");
        if (!Files.exists(dataDir)) {
            System.err.println("Data目录不存在");
            return new ArrayList<>();
        }
        
        return Files.list(dataDir)
            .filter(path -> path.toString().endsWith(".html") && path.getFileName().toString().startsWith("chat_"))
            .map(path -> path.getFileName().toString())
            .sorted()
            .collect(Collectors.toList());
    }
    
    private static String fixFileList(String content, List<String> chatFiles) {
        // 生成JavaScript文件数组
        StringBuilder jsArray = new StringBuilder();
        jsArray.append("            const fileDates = [\n");
        
        for (String filename : chatFiles) {
            // 从chat_YYYY_MM_DD.html中提取日期
            String date = filename.substring(5, filename.length() - 5); // 去掉"chat_"和".html"
            jsArray.append("                '").append(date).append("',\n");
        }
        
        jsArray.append("            ];\n");
        
        // 替换原有的硬编码文件列表
        String oldPattern = "            const fileDates = \\[.*?\\];";
        String newPattern = jsArray.toString();
        
        return content.replaceAll("(?s)" + oldPattern, newPattern);
    }
    
    private static String fixSearchFunction(String content) {
        // 修复搜索函数中的HTML结构匹配
        String oldSearchFunction = "        // 在单个文件中搜索\n        async function searchInFile\\(file, searchTerm, senderFilter, messageType\\) \\{.*?\\}";
        
        String newSearchFunction = """
        // 在单个文件中搜索
        async function searchInFile(file, searchTerm, senderFilter, messageType) {
            try {
                const response = await fetch(file.url);
                if (!response.ok) {
                    console.warn(`无法加载文件: ${file.url}`);
                    return [];
                }

                const html = await response.text();
                const parser = new DOMParser();
                const doc = parser.parseFromString(html, 'text/html');

                // 尝试多种可能的消息选择器
                const messageSelectors = [
                    '.message',
                    '.chat-message',
                    '.msg',
                    '[class*="message"]',
                    'div[class*="shengsheng"], div[class*="user"]',
                    '.shengsheng-message, .user-message',
                    'div:has(.sender), div:has(.time)'
                ];
                
                let messages = [];
                for (const selector of messageSelectors) {
                    messages = doc.querySelectorAll(selector);
                    if (messages.length > 0) {
                        console.log(`使用选择器 ${selector} 找到 ${messages.length} 条消息`);
                        break;
                    }
                }

                if (messages.length === 0) {
                    // 如果找不到消息，尝试查找包含文本的div
                    const allDivs = doc.querySelectorAll('div');
                    messages = Array.from(allDivs).filter(div => {
                        const text = div.textContent.trim();
                        return text.length > 10 && text.length < 1000; // 合理的消息长度
                    });
                    console.log(`通过文本内容找到 ${messages.length} 个可能的消息`);
                }

                const results = [];

                messages.forEach((message, index) => {
                    try {
                        // 尝试多种方式获取消息内容
                        let messageText = '';
                        let sender = '';
                        let time = '';
                        
                        // 获取消息文本
                        const textSelectors = ['.message-content', '.content', '.text', '.msg-content'];
                        for (const selector of textSelectors) {
                            const element = message.querySelector(selector);
                            if (element) {
                                messageText = element.textContent || '';
                                break;
                            }
                        }
                        
                        if (!messageText) {
                            messageText = message.textContent || '';
                        }
                        
                        // 获取发送者
                        const senderSelectors = ['.sender', '.name', '.user', '.from'];
                        for (const selector of senderSelectors) {
                            const element = message.querySelector(selector);
                            if (element) {
                                sender = element.textContent || '';
                                break;
                            }
                        }
                        
                        // 如果没有找到发送者，尝试从类名推断
                        if (!sender) {
                            if (message.className.includes('shengsheng')) {
                                sender = '生生';
                            } else if (message.className.includes('user')) {
                                sender = '用户';
                            }
                        }
                        
                        // 获取时间
                        const timeSelectors = ['.time', '.timestamp', '.date'];
                        for (const selector of timeSelectors) {
                            const element = message.querySelector(selector);
                            if (element) {
                                time = element.textContent || '';
                                break;
                            }
                        }
                        
                        // 清理文本
                        messageText = messageText.trim();
                        if (messageText.length < 5) return; // 跳过太短的内容
                        
                        // 检查发送者筛选
                        if (senderFilter) {
                            const senderType = message.className.includes('shengsheng') ? 'shengsheng' : 
                                             message.className.includes('user') ? 'user' : '';
                            if (senderType !== senderFilter && !sender.includes(senderFilter === 'shengsheng' ? '生生' : '用户')) {
                                return;
                            }
                        }

                        // 检查关键词匹配
                        if (searchTerm && !messageText.toLowerCase().includes(searchTerm.toLowerCase())) {
                            return;
                        }

                        // 检查消息类型
                        if (messageType) {
                            const hasImage = message.querySelector('img') || messageText.includes('[图片]') || messageText.includes('jpg') || messageText.includes('png');
                            const hasFile = message.querySelector('a[href*="file"]') || message.querySelector('.file') || messageText.includes('[文件]');
                            const hasVoice = message.querySelector('audio') || messageText.includes('[语音]');
                            const hasVideo = message.querySelector('video') || messageText.includes('[视频]');
                            
                            const messageTypeMatch = 
                                (messageType === 'image' && hasImage) ||
                                (messageType === 'file' && hasFile) ||
                                (messageType === 'voice' && hasVoice) ||
                                (messageType === 'video' && hasVideo) ||
                                (messageType === 'text' && !hasImage && !hasFile && !hasVoice && !hasVideo);
                            
                            if (!messageTypeMatch) return;
                        }

                        results.push({
                            file: file,
                            date: file.date,
                            time: time || '未知时间',
                            sender: sender || '未知发送者',
                            content: messageText.substring(0, 200) + (messageText.length > 200 ? '...' : ''),
                            fullContent: messageText
                        });
                    } catch (e) {
                        // 忽略单个消息处理错误
                    }
                });

                console.log(`从文件 ${file.url} 中找到 ${results.length} 条匹配结果`);
                return results;
            } catch (error) {
                console.error(`搜索文件 ${file.url} 时出错:`, error);
                return [];
            }
        }""";
        
        return content.replaceAll("(?s)" + oldSearchFunction, newSearchFunction);
    }
    
    private static String fixHTMLStructure(String content) {
        // 修复文件URL路径
        content = content.replaceAll("url: `Data/chat_", "url: `Data/");
        
        // 添加调试信息
        String oldInit = "        // 初始化搜索功能\n        async function initializeSearch\\(\\) \\{.*?\\}";
        
        String newInit = """
        // 初始化搜索功能
        async function initializeSearch() {
            try {
                console.log('开始初始化搜索功能...');
                await loadChatFileList();
                setupEventListeners();
                console.log('搜索功能初始化完成');
            } catch (error) {
                console.error('初始化搜索功能失败:', error);
                showError('初始化搜索功能失败，请刷新页面重试');
            }
        }""";
        
        content = content.replaceAll("(?s)" + oldInit, newInit);
        
        return content;
    }
    
    private static String readFile(String filename) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filename)), "UTF-8");
    }
    
    private static void writeFile(String filename, String content) throws IOException {
        Files.write(Paths.get(filename), content.getBytes("UTF-8"));
    }
}
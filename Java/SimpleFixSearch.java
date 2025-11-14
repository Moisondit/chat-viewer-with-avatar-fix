import java.io.*;
import java.nio.file.*;
import java.util.*;

public class SimpleFixSearch {
    public static void main(String[] args) {
        try {
            System.out.println("开始修复搜索页面...");

            // 读取搜索页面
            Path searchPagePath = Paths.get("search.html");
            if (!Files.exists(searchPagePath)) {
                System.out.println("search.html 不存在，请确保在正确的目录中运行");
                return;
            }

            String content = new String(Files.readAllBytes(searchPagePath), "UTF-8");

            // 修复1: 更新文件路径
            content = content.replace("url: `Data/chat_", "url: `Data/");

            // 修复2: 添加调试信息
            content = content.replace(
                    "        // 加载聊天文件列表\n        async function loadChatFileList() {",
                    "        // 加载聊天文件列表\n        async function loadChatFileList() {\n            console.log('开始加载聊天文件列表...');");

            // 修复3: 改进搜索函数的错误处理
            content = content.replace(
                    "                const response = await fetch(file.url);\n                if (!response.ok) {\n                    console.warn(`无法加载文件: ${file.url}`);\n                    return [];\n                }",
                    "                console.log(`正在搜索文件: ${file.url}`);\n                const response = await fetch(file.url);\n                if (!response.ok) {\n                    console.warn(`无法加载文件: ${file.url}, 状态: ${response.status}`);\n                    return [];\n                }");

            // 修复4: 添加更多的消息选择器
            String oldSearchCode = "                const messages = doc.querySelectorAll('.message');";
            String newSearchCode = """
                    // 尝试多种可能的消息选择器
                    let messages = doc.querySelectorAll('.message');
                    if (messages.length === 0) {
                        messages = doc.querySelectorAll('.chat-message');
                    }
                    if (messages.length === 0) {
                        messages = doc.querySelectorAll('[class*="message"]');
                    }
                    if (messages.length === 0) {
                        // 如果找不到标准选择器，尝试查找包含文本内容的div
                        const allDivs = doc.querySelectorAll('div');
                        messages = Array.from(allDivs).filter(div => {
                            const text = div.textContent.trim();
                            return text.length > 10 && text.length < 1000 && !div.querySelector('div'); // 避免嵌套div
                        });
                    }
                    console.log(`找到 ${messages.length} 个消息元素`);""";

            content = content.replace(oldSearchCode, newSearchCode);

            // 修复5: 改进消息内容提取
            String oldContentExtraction = "                    const messageText = message.querySelector('.message-content')?.textContent || '';";
            String newContentExtraction = """
                    // 尝试多种方式获取消息内容
                    let messageText = message.querySelector('.message-content')?.textContent || '';
                    if (!messageText) messageText = message.querySelector('.content')?.textContent || '';
                    if (!messageText) messageText = message.querySelector('.text')?.textContent || '';
                    if (!messageText) messageText = message.textContent || '';""";

            content = content.replace(oldContentExtraction, newContentExtraction);

            // 修复6: 改进发送者提取
            String oldSenderExtraction = "                    const sender = message.querySelector('.sender')?.textContent || '';";
            String newSenderExtraction = """
                    // 尝试多种方式获取发送者
                    let sender = message.querySelector('.sender')?.textContent || '';
                    if (!sender) sender = message.querySelector('.name')?.textContent || '';
                    if (!sender) sender = message.querySelector('.user')?.textContent || '';
                    // 如果没有找到发送者，尝试从类名推断
                    if (!sender) {
                        if (message.className.includes('shengsheng')) {
                            sender = '生生';
                        } else if (message.className.includes('user')) {
                            sender = '用户';
                        }
                    }""";

            content = content.replace(oldSenderExtraction, newSenderExtraction);

            // 修复7: 改进时间提取
            String oldTimeExtraction = "                    const time = message.querySelector('.time')?.textContent || '';";
            String newTimeExtraction = """
                    // 尝试多种方式获取时间
                    let time = message.querySelector('.time')?.textContent || '';
                    if (!time) time = message.querySelector('.timestamp')?.textContent || '';
                    if (!time) time = message.querySelector('.date')?.textContent || '';""";

            content = content.replace(oldTimeExtraction, newTimeExtraction);

            // 修复8: 添加搜索按钮的点击事件
            content = content.replace(
                    "                <button class=\"search-button\" onclick=\"performSearch()\">🔍</button>",
                    "                <button class=\"search-button\" onclick=\"performSearch()\" id=\"searchBtn\">🔍</button>");

            // 写回文件
            Files.write(searchPagePath, content.getBytes("UTF-8"));

            System.out.println("搜索页面修复完成！");
            System.out.println("主要修复内容：");
            System.out.println("1. 修复了文件路径问题");
            System.out.println("2. 添加了调试信息");
            System.out.println("3. 改进了HTML结构匹配");
            System.out.println("4. 增强了消息内容提取");
            System.out.println("5. 改进了发送者和时间提取");
            System.out.println("6. 添加了更多的消息选择器");

        } catch (Exception e) {
            System.err.println("修复搜索页面时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
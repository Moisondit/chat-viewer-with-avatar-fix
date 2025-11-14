import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class CreateSearchPage {

    public static void main(String[] args) {
        try {
            createSearchPage();
            System.out.println("搜索页面创建成功！");
        } catch (IOException e) {
            System.err.println("创建搜索页面失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void createSearchPage() throws IOException {
        String searchPageContent = generateSearchPageHTML();

        Path outputPath = Paths.get("c:\\Users\\ASUS\\Desktop\\游戏\\星如雨[2872215021]\\search.html");
        Files.write(outputPath, searchPageContent.getBytes("UTF-8"));

        System.out.println("搜索页面已创建: " + outputPath);
    }

    private static String generateSearchPageHTML() {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"zh-CN\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>聊天记录搜索</title>\n" +
                "    <style>\n" +
                "        * {\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "            box-sizing: border-box;\n" +
                "        }\n" +
                "\n" +
                "        body {\n" +
                "            font-family: 'Microsoft YaHei', Arial, sans-serif;\n" +
                "            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);\n" +
                "            min-height: 100vh;\n" +
                "            padding: 20px;\n" +
                "        }\n" +
                "\n" +
                "        .container {\n" +
                "            max-width: 1200px;\n" +
                "            margin: 0 auto;\n" +
                "            background: white;\n" +
                "            border-radius: 20px;\n" +
                "            box-shadow: 0 20px 40px rgba(0,0,0,0.1);\n" +
                "            overflow: hidden;\n" +
                "        }\n" +
                "\n" +
                "        .header {\n" +
                "            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);\n" +
                "            color: white;\n" +
                "            padding: 30px;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "\n" +
                "        .header h1 {\n" +
                "            font-size: 2.5em;\n" +
                "            margin-bottom: 10px;\n" +
                "            font-weight: 300;\n" +
                "        }\n" +
                "\n" +
                "        .header p {\n" +
                "            font-size: 1.1em;\n" +
                "            opacity: 0.9;\n" +
                "        }\n" +
                "\n" +
                "        .search-section {\n" +
                "            padding: 40px;\n" +
                "            background: #f8f9fa;\n" +
                "        }\n" +
                "\n" +
                "        .search-box {\n" +
                "            position: relative;\n" +
                "            margin-bottom: 30px;\n" +
                "        }\n" +
                "\n" +
                "        .search-input {\n" +
                "            width: 100%;\n" +
                "            padding: 20px 60px 20px 20px;\n" +
                "            font-size: 1.2em;\n" +
                "            border: 2px solid #e9ecef;\n" +
                "            border-radius: 50px;\n" +
                "            outline: none;\n" +
                "            transition: all 0.3s ease;\n" +
                "            background: white;\n" +
                "        }\n" +
                "\n" +
                "        .search-input:focus {\n" +
                "            border-color: #667eea;\n" +
                "            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);\n" +
                "        }\n" +
                "\n" +
                "        .search-button {\n" +
                "            position: absolute;\n" +
                "            right: 5px;\n" +
                "            top: 50%;\n" +
                "            transform: translateY(-50%);\n" +
                "            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);\n" +
                "            color: white;\n" +
                "            border: none;\n" +
                "            border-radius: 50%;\n" +
                "            width: 50px;\n" +
                "            height: 50px;\n" +
                "            cursor: pointer;\n" +
                "            display: flex;\n" +
                "            align-items: center;\n" +
                "            justify-content: center;\n" +
                "            transition: all 0.3s ease;\n" +
                "        }\n" +
                "\n" +
                "        .search-button:hover {\n" +
                "            transform: translateY(-50%) scale(1.1);\n" +
                "        }\n" +
                "\n" +
                "        .filters {\n" +
                "            display: grid;\n" +
                "            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));\n" +
                "            gap: 20px;\n" +
                "            margin-bottom: 30px;\n" +
                "        }\n" +
                "\n" +
                "        .filter-group {\n" +
                "            background: white;\n" +
                "            padding: 20px;\n" +
                "            border-radius: 15px;\n" +
                "            box-shadow: 0 2px 10px rgba(0,0,0,0.05);\n" +
                "        }\n" +
                "\n" +
                "        .filter-group label {\n" +
                "            display: block;\n" +
                "            margin-bottom: 8px;\n" +
                "            font-weight: 600;\n" +
                "            color: #495057;\n" +
                "        }\n" +
                "\n" +
                "        .filter-group input,\n" +
                "        .filter-group select {\n" +
                "            width: 100%;\n" +
                "            padding: 10px;\n" +
                "            border: 1px solid #dee2e6;\n" +
                "            border-radius: 8px;\n" +
                "            font-size: 1em;\n" +
                "            outline: none;\n" +
                "            transition: border-color 0.3s ease;\n" +
                "        }\n" +
                "\n" +
                "        .filter-group input:focus,\n" +
                "        .filter-group select:focus {\n" +
                "            border-color: #667eea;\n" +
                "        }\n" +
                "\n" +
                "        .results-section {\n" +
                "            padding: 0 40px 40px;\n" +
                "        }\n" +
                "\n" +
                "        .results-header {\n" +
                "            display: flex;\n" +
                "            justify-content: space-between;\n" +
                "            align-items: center;\n" +
                "            margin-bottom: 20px;\n" +
                "            padding: 20px;\n" +
                "            background: #f8f9fa;\n" +
                "            border-radius: 10px;\n" +
                "        }\n" +
                "\n" +
                "        .results-count {\n" +
                "            font-size: 1.1em;\n" +
                "            color: #495057;\n" +
                "        }\n" +
                "\n" +
                "        .results-count strong {\n" +
                "            color: #667eea;\n" +
                "        }\n" +
                "\n" +
                "        .loading {\n" +
                "            text-align: center;\n" +
                "            padding: 40px;\n" +
                "            color: #6c757d;\n" +
                "        }\n" +
                "\n" +
                "        .loading-spinner {\n" +
                "            display: inline-block;\n" +
                "            width: 40px;\n" +
                "            height: 40px;\n" +
                "            border: 4px solid #f3f3f3;\n" +
                "            border-top: 4px solid #667eea;\n" +
                "            border-radius: 50%;\n" +
                "            animation: spin 1s linear infinite;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "\n" +
                "        @keyframes spin {\n" +
                "            0% { transform: rotate(0deg); }\n" +
                "            100% { transform: rotate(360deg); }\n" +
                "        }\n" +
                "\n" +
                "        .result-item {\n" +
                "            background: white;\n" +
                "            border: 1px solid #e9ecef;\n" +
                "            border-radius: 15px;\n" +
                "            margin-bottom: 20px;\n" +
                "            overflow: hidden;\n" +
                "            transition: all 0.3s ease;\n" +
                "            cursor: pointer;\n" +
                "        }\n" +
                "\n" +
                "        .result-item:hover {\n" +
                "            box-shadow: 0 5px 20px rgba(0,0,0,0.1);\n" +
                "            transform: translateY(-2px);\n" +
                "        }\n" +
                "\n" +
                "        .result-header {\n" +
                "            padding: 20px;\n" +
                "            background: #f8f9fa;\n" +
                "            border-bottom: 1px solid #e9ecef;\n" +
                "        }\n" +
                "\n" +
                "        .result-date {\n" +
                "            font-size: 1.1em;\n" +
                "            font-weight: 600;\n" +
                "            color: #495057;\n" +
                "            margin-bottom: 5px;\n" +
                "        }\n" +
                "\n" +
                "        .result-meta {\n" +
                "            font-size: 0.9em;\n" +
                "            color: #6c757d;\n" +
                "        }\n" +
                "\n" +
                "        .result-content {\n" +
                "            padding: 20px;\n" +
                "        }\n" +
                "\n" +
                "        .message-preview {\n" +
                "            margin-bottom: 15px;\n" +
                "            padding: 15px;\n" +
                "            border-radius: 10px;\n" +
                "            border-left: 4px solid #667eea;\n" +
                "            background: #f8f9fa;\n" +
                "        }\n" +
                "\n" +
                "        .message-sender {\n" +
                "            font-weight: 600;\n" +
                "            margin-bottom: 5px;\n" +
                "        }\n" +
                "\n" +
                "        .message-sender.shengsheng {\n" +
                "            color: #e74c3c;\n" +
                "        }\n" +
                "\n" +
                "        .message-sender.user {\n" +
                "            color: #3498db;\n" +
                "        }\n" +
                "\n" +
                "        .message-text {\n" +
                "            color: #495057;\n" +
                "            line-height: 1.5;\n" +
                "        }\n" +
                "\n" +
                "        .highlight {\n" +
                "            background: #fff3cd;\n" +
                "            padding: 2px 4px;\n" +
                "            border-radius: 3px;\n" +
                "            font-weight: 600;\n" +
                "        }\n" +
                "\n" +
                "        .no-results {\n" +
                "            text-align: center;\n" +
                "            padding: 60px 20px;\n" +
                "            color: #6c757d;\n" +
                "        }\n" +
                "\n" +
                "        .no-results-icon {\n" +
                "            font-size: 4em;\n" +
                "            margin-bottom: 20px;\n" +
                "            opacity: 0.5;\n" +
                "        }\n" +
                "\n" +
                "        .navigation {\n" +
                "            padding: 20px 40px;\n" +
                "            background: #f8f9fa;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "\n" +
                "        .nav-link {\n" +
                "            display: inline-block;\n" +
                "            margin: 0 15px;\n" +
                "            padding: 10px 20px;\n" +
                "            background: white;\n" +
                "            color: #667eea;\n" +
                "            text-decoration: none;\n" +
                "            border-radius: 25px;\n" +
                "            transition: all 0.3s ease;\n" +
                "            border: 2px solid #667eea;\n" +
                "        }\n" +
                "\n" +
                "        .nav-link:hover {\n" +
                "            background: #667eea;\n" +
                "            color: white;\n" +
                "            transform: translateY(-2px);\n" +
                "        }\n" +
                "\n" +
                "        .pagination {\n" +
                "            display: flex;\n" +
                "            justify-content: center;\n" +
                "            gap: 10px;\n" +
                "            margin-top: 30px;\n" +
                "        }\n" +
                "\n" +
                "        .page-btn {\n" +
                "            padding: 8px 16px;\n" +
                "            background: white;\n" +
                "            border: 1px solid #dee2e6;\n" +
                "            border-radius: 8px;\n" +
                "            cursor: pointer;\n" +
                "            transition: all 0.3s ease;\n" +
                "        }\n" +
                "\n" +
                "        .page-btn:hover,\n" +
                "        .page-btn.active {\n" +
                "            background: #667eea;\n" +
                "            color: white;\n" +
                "            border-color: #667eea;\n" +
                "        }\n" +
                "\n" +
                "        .page-btn:disabled {\n" +
                "            opacity: 0.5;\n" +
                "            cursor: not-allowed;\n" +
                "        }\n" +
                "\n" +
                "        /* 响应式设计 */\n" +
                "        @media (max-width: 768px) {\n" +
                "            .container {\n" +
                "                border-radius: 0;\n" +
                "                margin: -20px;\n" +
                "            }\n" +
                "\n" +
                "            .search-section {\n" +
                "                padding: 20px;\n" +
                "            }\n" +
                "\n" +
                "            .filters {\n" +
                "                grid-template-columns: 1fr;\n" +
                "            }\n" +
                "\n" +
                "            .results-section {\n" +
                "                padding: 0 20px 20px;\n" +
                "            }\n" +
                "\n" +
                "            .navigation {\n" +
                "                padding: 20px;\n" +
                "            }\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <header class=\"header\">\n" +
                "            <h1>🔍 聊天记录搜索</h1>\n" +
                "            <p>搜索所有聊天记录，支持关键词、日期、发送者筛选</p>\n" +
                "        </header>\n" +
                "\n" +
                "        <section class=\"search-section\">\n" +
                "            <div class=\"search-box\">\n" +
                "                <input type=\"text\" id=\"searchInput\" class=\"search-input\" placeholder=\"输入搜索关键词...\">\n"
                +
                "                <button class=\"search-button\" onclick=\"performSearch()\">🔍</button>\n" +
                "            </div>\n" +
                "\n" +
                "            <div class=\"filters\">\n" +
                "                <div class=\"filter-group\">\n" +
                "                    <label for=\"startDate\">开始日期</label>\n" +
                "                    <input type=\"date\" id=\"startDate\">\n" +
                "                </div>\n" +
                "                <div class=\"filter-group\">\n" +
                "                    <label for=\"endDate\">结束日期</label>\n" +
                "                    <input type=\"date\" id=\"endDate\">\n" +
                "                </div>\n" +
                "                <div class=\"filter-group\">\n" +
                "                    <label for=\"senderFilter\">发送者</label>\n" +
                "                    <select id=\"senderFilter\">\n" +
                "                        <option value=\"\">全部</option>\n" +
                "                        <option value=\"shengsheng\">生生</option>\n" +
                "                        <option value=\"user\">用户</option>\n" +
                "                    </select>\n" +
                "                </div>\n" +
                "                <div class=\"filter-group\">\n" +
                "                    <label for=\"messageType\">消息类型</label>\n" +
                "                    <select id=\"messageType\">\n" +
                "                        <option value=\"\">全部</option>\n" +
                "                        <option value=\"text\">文本消息</option>\n" +
                "                        <option value=\"image\">图片</option>\n" +
                "                        <option value=\"file\">文件</option>\n" +
                "                        <option value=\"voice\">语音</option>\n" +
                "                        <option value=\"video\">视频</option>\n" +
                "                    </select>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        </section>\n" +
                "\n" +
                "        <section class=\"results-section\">\n" +
                "            <div class=\"results-header\">\n" +
                "                <div class=\"results-count\">\n" +
                "                    找到 <strong id=\"resultsCount\">0</strong> 条结果\n" +
                "                </div>\n" +
                "            </div>\n" +
                "            <div id=\"searchResults\">\n" +
                "                <div class=\"no-results\">\n" +
                "                    <div class=\"no-results-icon\">🔍</div>\n" +
                "                    <h3>开始搜索聊天记录</h3>\n" +
                "                    <p>输入关键词并选择筛选条件来搜索聊天记录</p>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        </section>\n" +
                "\n" +
                "        <nav class=\"navigation\">\n" +
                "            <a href=\"index.html\" class=\"nav-link\">🏠 返回主页</a>\n" +
                "            <a href=\"chat_index.html\" class=\"nav-link\">💬 聊天记录</a>\n" +
                "            <a href=\"calendar_index.html\" class=\"nav-link\">📅 日历视图</a>\n" +
                "        </nav>\n" +
                "    </div>\n" +
                "\n" +
                "    <script>\n" +
                "        // 全局变量\n" +
                "        let allChatFiles = [];\n" +
                "        let currentResults = [];\n" +
                "        let currentPage = 1;\n" +
                "        const resultsPerPage = 10;\n" +
                "\n" +
                "        // 页面加载完成后初始化\n" +
                "        document.addEventListener('DOMContentLoaded', function() {\n" +
                "            initializeSearch();\n" +
                "        });\n" +
                "\n" +
                "        // 初始化搜索功能\n" +
                "        async function initializeSearch() {\n" +
                "            try {\n" +
                "                await loadChatFileList();\n" +
                "                setupEventListeners();\n" +
                "            } catch (error) {\n" +
                "                console.error('初始化搜索功能失败:', error);\n" +
                "                showError('初始化搜索功能失败，请刷新页面重试');\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        // 设置事件监听器\n" +
                "        function setupEventListeners() {\n" +
                "            const searchInput = document.getElementById('searchInput');\n" +
                "            \n" +
                "            // 回车键搜索\n" +
                "            searchInput.addEventListener('keypress', function(e) {\n" +
                "                if (e.key === 'Enter') {\n" +
                "                    performSearch();\n" +
                "                }\n" +
                "            });\n" +
                "\n" +
                "            // 实时搜索（防抖）\n" +
                "            let debounceTimer;\n" +
                "            searchInput.addEventListener('input', function() {\n" +
                "                clearTimeout(debounceTimer);\n" +
                "                debounceTimer = setTimeout(() => {\n" +
                "                    if (searchInput.value.trim().length >= 2) {\n" +
                "                        performSearch();\n" +
                "                    } else if (searchInput.value.trim() === '') {\n" +
                "                        clearResults();\n" +
                "                    }\n" +
                "                }, 300);\n" +
                "            });\n" +
                "\n" +
                "            // 筛选条件变化时自动搜索\n" +
                "            document.getElementById('startDate').addEventListener('change', performSearch);\n" +
                "            document.getElementById('endDate').addEventListener('change', performSearch);\n" +
                "            document.getElementById('senderFilter').addEventListener('change', performSearch);\n" +
                "            document.getElementById('messageType').addEventListener('change', performSearch);\n" +
                "        }\n" +
                "\n" +
                "        // 加载聊天文件列表\n" +
                "        async function loadChatFileList() {\n" +
                "            // 生成聊天文件列表（基于已知的文件命名模式）\n" +
                "            const fileDates = [\n" +
                "                // 2022年\n" +
                "                '2022_09_24', '2022_10_01', '2022_10_02', '2022_10_05', '2022_10_06', '2022_10_07',\n"
                +
                "                '2022_10_09', '2022_10_10', '2022_10_11', '2022_10_14', '2022_10_15', '2022_10_16',\n"
                +
                "                '2022_10_20', '2022_10_21', '2022_10_22', '2022_10_23', '2022_10_26', '2022_10_28',\n"
                +
                "                '2022_10_29', '2022_10_30', '2022_10_31', '2022_11_01', '2022_11_02', '2022_11_03',\n"
                +
                "                '2022_11_04', '2022_11_05', '2022_11_06', '2022_11_07', '2022_11_09', '2022_11_10',\n"
                +
                "                '2022_11_11', '2022_11_12', '2022_11_13', '2022_11_14', '2022_11_15', '2022_11_16',\n"
                +
                "                '2022_11_17', '2022_11_18', '2022_11_19', '2022_11_20', '2022_11_21', '2022_11_22',\n"
                +
                "                '2022_11_23', '2022_11_24', '2022_11_25', '2022_11_26', '2022_11_27', '2022_11_28',\n"
                +
                "                '2022_11_29', '2022_11_30', '2022_12_02', '2022_12_03', '2022_12_10', '2022_12_12',\n"
                +
                "                '2022_12_20', '2022_12_28', '2022_12_29', '2022_12_30', '2022_12_31',\n" +
                "                // 2023年（部分示例）\n" +
                "                '2023_01_01', '2023_01_02', '2023_01_03', '2023_01_04', '2023_01_05', '2023_01_06',\n"
                +
                "                '2023_01_07', '2023_01_08', '2023_01_09', '2023_01_10', '2023_01_11', '2023_01_12',\n"
                +
                "                '2023_01_13', '2023_01_14', '2023_01_15', '2023_01_16', '2023_01_17', '2023_01_18',\n"
                +
                "                '2023_01_19', '2023_01_20', '2023_01_21', '2023_01_22', '2023_01_23', '2023_01_24',\n"
                +
                "                '2023_01_25', '2023_01_26', '2023_01_27'\n" +
                "            ];\n" +
                "\n" +
                "            allChatFiles = fileDates.map(date => ({\n" +
                "                filename: `chat_${date}.html`,\n" +
                "                date: date,\n" +
                "                url: `Data/chat_${date}.html`\n" +
                "            }));\n" +
                "\n" +
                "            console.log(`已加载 ${allChatFiles.length} 个聊天文件`);\n" +
                "        }\n" +
                "\n" +
                "        // 执行搜索\n" +
                "        async function performSearch() {\n" +
                "            const searchTerm = document.getElementById('searchInput').value.trim();\n" +
                "            const startDate = document.getElementById('startDate').value;\n" +
                "            const endDate = document.getElementById('endDate').value;\n" +
                "            const senderFilter = document.getElementById('senderFilter').value;\n" +
                "            const messageType = document.getElementById('messageType').value;\n" +
                "\n" +
                "            if (!searchTerm && !startDate && !endDate && !senderFilter && !messageType) {\n" +
                "                clearResults();\n" +
                "                return;\n" +
                "            }\n" +
                "\n" +
                "            showLoading();\n" +
                "            currentResults = [];\n" +
                "            currentPage = 1;\n" +
                "\n" +
                "            try {\n" +
                "                // 筛选需要搜索的文件\n" +
                "                const filesToSearch = filterFiles(allChatFiles, startDate, endDate);\n" +
                "\n" +
                "                // 并行搜索文件\n" +
                "                const searchPromises = filesToSearch.map(file => searchInFile(file, searchTerm, senderFilter, messageType));\n"
                +
                "                const results = await Promise.all(searchPromises);\n" +
                "\n" +
                "                // 合并结果\n" +
                "                currentResults = results.flat().sort((a, b) => {\n" +
                "                    // 按日期和时间排序\n" +
                "                    const dateCompare = b.date.localeCompare(a.date);\n" +
                "                    if (dateCompare !== 0) return dateCompare;\n" +
                "                    return b.time.localeCompare(a.time);\n" +
                "                });\n" +
                "\n" +
                "                displayResults();\n" +
                "            } catch (error) {\n" +
                "                console.error('搜索失败:', error);\n" +
                "                showError('搜索失败，请重试');\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        // 筛选文件\n" +
                "        function filterFiles(files, startDate, endDate) {\n" +
                "            return files.filter(file => {\n" +
                "                if (startDate && file.date < startDate.replace(/-/g, '_')) return false;\n" +
                "                if (endDate && file.date > endDate.replace(/-/g, '_')) return false;\n" +
                "                return true;\n" +
                "            });\n" +
                "        }\n" +
                "\n" +
                "        // 在单个文件中搜索\n" +
                "        async function searchInFile(file, searchTerm, senderFilter, messageType) {\n" +
                "            try {\n" +
                "                const response = await fetch(file.url);\n" +
                "                if (!response.ok) {\n" +
                "                    console.warn(`无法加载文件: ${file.url}`);\n" +
                "                    return [];\n" +
                "                }\n" +
                "\n" +
                "                const html = await response.text();\n" +
                "                const parser = new DOMParser();\n" +
                "                const doc = parser.parseFromString(html, 'text/html');\n" +
                "\n" +
                "                const messages = doc.querySelectorAll('.message');\n" +
                "                const results = [];\n" +
                "\n" +
                "                messages.forEach(message => {\n" +
                "                    const messageText = message.querySelector('.message-content')?.textContent || '';\n"
                +
                "                    const sender = message.querySelector('.sender')?.textContent || '';\n" +
                "                    const time = message.querySelector('.time')?.textContent || '';\n" +
                "                    \n" +
                "                    // 检查发送者筛选\n" +
                "                    if (senderFilter) {\n" +
                "                        const senderType = message.classList.contains('shengsheng-message') ? 'shengsheng' : \n"
                +
                "                                         message.classList.contains('user-message') ? 'user' : '';\n" +
                "                        if (senderType !== senderFilter) return;\n" +
                "                    }\n" +
                "\n" +
                "                    // 检查关键词匹配\n" +
                "                    if (searchTerm && !messageText.toLowerCase().includes(searchTerm.toLowerCase())) {\n"
                +
                "                        return;\n" +
                "                    }\n" +
                "\n" +
                "                    // 检查消息类型（这里简化处理，实际需要根据HTML结构判断）\n" +
                "                    if (messageType) {\n" +
                "                        const hasImage = message.querySelector('img');\n" +
                "                        const hasFile = message.querySelector('.file-attachment');\n" +
                "                        const hasVoice = message.querySelector('.voice-message');\n" +
                "                        const hasVideo = message.querySelector('.video-message');\n" +
                "                        \n" +
                "                        const messageTypeMatch = \n" +
                "                            (messageType === 'image' && hasImage) ||\n" +
                "                            (messageType === 'file' && hasFile) ||\n" +
                "                            (messageType === 'voice' && hasVoice) ||\n" +
                "                            (messageType === 'video' && hasVideo) ||\n" +
                "                            (messageType === 'text' && !hasImage && !hasFile && !hasVoice && !hasVideo);\n"
                +
                "                        \n" +
                "                        if (!messageTypeMatch) return;\n" +
                "                    }\n" +
                "\n" +
                "                    results.push({\n" +
                "                        file: file,\n" +
                "                        date: file.date,\n" +
                "                        time: time,\n" +
                "                        sender: sender,\n" +
                "                        content: messageText.trim(),\n" +
                "                        messageElement: message.outerHTML\n" +
                "                    });\n" +
                "                });\n" +
                "\n" +
                "                return results;\n" +
                "            } catch (error) {\n" +
                "                console.error(`搜索文件 ${file.url} 时出错:`, error);\n" +
                "                return [];\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        // 显示搜索结果\n" +
                "        function displayResults() {\n" +
                "            const resultsContainer = document.getElementById('searchResults');\n" +
                "            const resultsCount = document.getElementById('resultsCount');\n" +
                "            \n" +
                "            resultsCount.textContent = currentResults.length;\n" +
                "\n" +
                "            if (currentResults.length === 0) {\n" +
                "                resultsContainer.innerHTML = `\n" +
                "                    <div class=\"no-results\">\n" +
                "                        <div class=\"no-results-icon\">😔</div>\n" +
                "                        <h3>未找到相关结果</h3>\n" +
                "                        <p>尝试调整搜索关键词或筛选条件</p>\n" +
                "                    </div>\n" +
                "                `;\n" +
                "                return;\n" +
                "            }\n" +
                "\n" +
                "            // 分页显示\n" +
                "            const startIndex = (currentPage - 1) * resultsPerPage;\n" +
                "            const endIndex = startIndex + resultsPerPage;\n" +
                "            const pageResults = currentResults.slice(startIndex, endIndex);\n" +
                "\n" +
                "            let html = '';\n" +
                "            pageResults.forEach(result => {\n" +
                "                const highlightedContent = highlightSearchTerm(result.content);\n" +
                "                const senderClass = result.sender.includes('生生') ? 'shengsheng' : 'user';\n" +
                "                \n" +
                "                html += `\n" +
                "                    <div class=\"result-item\" onclick=\"openChatFile('${result.file.url}', '${result.date}')\">\n"
                +
                "                        <div class=\"result-header\">\n" +
                "                            <div class=\"result-date\">📅 ${formatDate(result.date)}</div>\n" +
                "                            <div class=\"result-meta\">${result.time} • ${result.sender}</div>\n" +
                "                        </div>\n" +
                "                        <div class=\"result-content\">\n" +
                "                            <div class=\"message-preview\">\n" +
                "                                <div class=\"message-sender ${senderClass}\">${result.sender}</div>\n"
                +
                "                                <div class=\"message-text\">${highlightedContent}</div>\n" +
                "                            </div>\n" +
                "                        </div>\n" +
                "                    </div>\n" +
                "                `;\n" +
                "            });\n" +
                "\n" +
                "            // 添加分页控件\n" +
                "            if (currentResults.length > resultsPerPage) {\n" +
                "                html += createPagination();\n" +
                "            }\n" +
                "\n" +
                "            resultsContainer.innerHTML = html;\n" +
                "        }\n" +
                "\n" +
                "        // 高亮搜索关键词\n" +
                "        function highlightSearchTerm(text) {\n" +
                "            const searchTerm = document.getElementById('searchInput').value.trim();\n" +
                "            if (!searchTerm) return text;\n" +
                "\n" +
                "            const regex = new RegExp(`(${searchTerm})`, 'gi');\n" +
                "            return text.replace(regex, '<span class=\"highlight\">$1</span>');\n" +
                "        }\n" +
                "\n" +
                "        // 创建分页控件\n" +
                "        function createPagination() {\n" +
                "            const totalPages = Math.ceil(currentResults.length / resultsPerPage);\n" +
                "            let html = '<div class=\"pagination\">';\n" +
                "\n" +
                "            // 上一页\n" +
                "            html += `<button class=\"page-btn\" onclick=\"changePage(${currentPage - 1})\" ${currentPage === 1 ? 'disabled' : ''}>上一页</button>`;\n"
                +
                "\n" +
                "            // 页码\n" +
                "            for (let i = 1; i <= totalPages; i++) {\n" +
                "                if (i === 1 || i === totalPages || (i >= currentPage - 2 && i <= currentPage + 2)) {\n"
                +
                "                    html += `<button class=\"page-btn ${i === currentPage ? 'active' : ''}\" onclick=\"changePage(${i})\">${i}</button>`;\n"
                +
                "                } else if (i === currentPage - 3 || i === currentPage + 3) {\n" +
                "                    html += '<span>...</span>';\n" +
                "                }\n" +
                "            }\n" +
                "\n" +
                "            // 下一页\n" +
                "            html += `<button class=\"page-btn\" onclick=\"changePage(${currentPage + 1})\" ${currentPage === totalPages ? 'disabled' : ''}>下一页</button>`;\n"
                +
                "\n" +
                "            html += '</div>';\n" +
                "            return html;\n" +
                "        }\n" +
                "\n" +
                "        // 切换页面\n" +
                "        function changePage(page) {\n" +
                "            const totalPages = Math.ceil(currentResults.length / resultsPerPage);\n" +
                "            if (page < 1 || page > totalPages) return;\n" +
                "            \n" +
                "            currentPage = page;\n" +
                "            displayResults();\n" +
                "            \n" +
                "            // 滚动到结果区域\n" +
                "            document.querySelector('.results-section').scrollIntoView({ behavior: 'smooth' });\n" +
                "        }\n" +
                "\n" +
                "        // 打开聊天文件\n" +
                "        function openChatFile(url, date) {\n" +
                "            // 在新窗口中打开聊天文件\n" +
                "            window.open(url, '_blank');\n" +
                "        }\n" +
                "\n" +
                "        // 格式化日期\n" +
                "        function formatDate(dateStr) {\n" +
                "            const parts = dateStr.split('_');\n" +
                "            return `${parts[0]}年${parts[1]}月${parts[2]}日`;\n" +
                "        }\n" +
                "\n" +
                "        // 显示加载状态\n" +
                "        function showLoading() {\n" +
                "            const resultsContainer = document.getElementById('searchResults');\n" +
                "            resultsContainer.innerHTML = `\n" +
                "                <div class=\"loading\">\n" +
                "                    <div class=\"loading-spinner\"></div>\n" +
                "                    <p>正在搜索中...</p>\n" +
                "                </div>\n" +
                "            `;\n" +
                "        }\n" +
                "\n" +
                "        // 清空结果\n" +
                "        function clearResults() {\n" +
                "            const resultsContainer = document.getElementById('searchResults');\n" +
                "            const resultsCount = document.getElementById('resultsCount');\n" +
                "            \n" +
                "            resultsCount.textContent = '0';\n" +
                "            resultsContainer.innerHTML = `\n" +
                "                <div class=\"no-results\">\n" +
                "                    <div class=\"no-results-icon\">🔍</div>\n" +
                "                    <h3>开始搜索聊天记录</h3>\n" +
                "                    <p>输入关键词并选择筛选条件来搜索聊天记录</p>\n" +
                "                </div>\n" +
                "            `;\n" +
                "        }\n" +
                "\n" +
                "        // 显示错误信息\n" +
                "        function showError(message) {\n" +
                "            const resultsContainer = document.getElementById('searchResults');\n" +
                "            resultsContainer.innerHTML = `\n" +
                "                <div class=\"no-results\">\n" +
                "                    <div class=\"no-results-icon\">⚠️</div>\n" +
                "                    <h3>搜索出错</h3>\n" +
                "                    <p>${message}</p>\n" +
                "                </div>\n" +
                "            `;\n" +
                "        }\n" +
                "    </script>\n" +
                "</body>\n" +
                "</html>";
    }
}
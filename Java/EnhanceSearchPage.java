import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class EnhanceSearchPage {
    
    public static void main(String[] args) {
        try {
            enhanceSearchPage();
            System.out.println("搜索页面增强完成！");
        } catch (IOException e) {
            System.err.println("增强搜索页面失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void enhanceSearchPage() throws IOException {
        Path searchPagePath = Paths.get("c:\\Users\\ASUS\\Desktop\\游戏\\星如雨[2872215021]\\search.html");
        String content = Files.readString(searchPagePath);
        
        // 添加搜索历史功能
        String searchHistoryScript = generateSearchHistoryScript();
        
        // 在</head>前添加搜索历史样式
        content = content.replace("</head>", 
            "    <style>\n" +
            "        /* 搜索历史样式 */\n" +
            "        .search-history {\n" +
            "            position: relative;\n" +
            "            margin-bottom: 20px;\n" +
            "        }\n" +
            "        \n" +
            "        .history-toggle {\n" +
            "            background: #f8f9fa;\n" +
            "            border: 1px solid #dee2e6;\n" +
            "            border-radius: 8px;\n" +
            "            padding: 8px 12px;\n" +
            "            cursor: pointer;\n" +
            "            font-size: 0.9em;\n" +
            "            color: #6c757d;\n" +
            "            transition: all 0.3s ease;\n" +
            "        }\n" +
            "        \n" +
            "        .history-toggle:hover {\n" +
            "            background: #e9ecef;\n" +
            "            color: #495057;\n" +
            "        }\n" +
            "        \n" +
            "        .history-list {\n" +
            "            display: none;\n" +
            "            position: absolute;\n" +
            "            top: 100%;\n" +
            "            left: 0;\n" +
            "            right: 0;\n" +
            "            background: white;\n" +
            "            border: 1px solid #dee2e6;\n" +
            "            border-radius: 8px;\n" +
            "            box-shadow: 0 4px 12px rgba(0,0,0,0.1);\n" +
            "            z-index: 1000;\n" +
            "            max-height: 200px;\n" +
            "            overflow-y: auto;\n" +
            "        }\n" +
            "        \n" +
            "        .history-list.show {\n" +
            "            display: block;\n" +
            "        }\n" +
            "        \n" +
            "        .history-item {\n" +
            "            padding: 8px 12px;\n" +
            "            cursor: pointer;\n" +
            "            border-bottom: 1px solid #f8f9fa;\n" +
            "            font-size: 0.9em;\n" +
            "            transition: background 0.2s ease;\n" +
            "        }\n" +
            "        \n" +
            "        .history-item:hover {\n" +
            "            background: #f8f9fa;\n" +
            "        }\n" +
            "        \n" +
            "        .history-item:last-child {\n" +
            "            border-bottom: none;\n" +
            "        }\n" +
            "        \n" +
            "        .clear-history {\n" +
            "            padding: 8px 12px;\n" +
            "            background: #dc3545;\n" +
            "            color: white;\n" +
            "            border: none;\n" +
            "            border-radius: 4px;\n" +
            "            cursor: pointer;\n" +
            "            font-size: 0.8em;\n" +
            "            margin: 8px;\n" +
            "        }\n" +
            "        \n" +
            "        .clear-history:hover {\n" +
            "            background: #c82333;\n" +
            "        }\n" +
            "        \n" +
            "        /* 统计信息样式 */\n" +
            "        .stats-section {\n" +
            "            background: #f8f9fa;\n" +
            "            padding: 20px 40px;\n" +
            "            border-top: 1px solid #e9ecef;\n" +
            "        }\n" +
            "        \n" +
            "        .stats-grid {\n" +
            "            display: grid;\n" +
            "            grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));\n" +
            "            gap: 20px;\n" +
            "        }\n" +
            "        \n" +
            "        .stat-card {\n" +
            "            background: white;\n" +
            "            padding: 15px;\n" +
            "            border-radius: 10px;\n" +
            "            text-align: center;\n" +
            "            box-shadow: 0 2px 8px rgba(0,0,0,0.05);\n" +
            "        }\n" +
            "        \n" +
            "        .stat-number {\n" +
            "            font-size: 1.8em;\n" +
            "            font-weight: 600;\n" +
            "            color: #667eea;\n" +
            "            margin-bottom: 5px;\n" +
            "        }\n" +
            "        \n" +
            "        .stat-label {\n" +
            "            font-size: 0.9em;\n" +
            "            color: #6c757d;\n" +
            "        }\n" +
            "        \n" +
            "        /* 快速筛选标签 */\n" +
            "        .quick-filters {\n" +
            "            margin-bottom: 20px;\n" +
            "        }\n" +
            "        \n" +
            "        .filter-tags {\n" +
            "            display: flex;\n" +
            "            flex-wrap: wrap;\n" +
            "            gap: 10px;\n" +
            "        }\n" +
            "        \n" +
            "        .filter-tag {\n" +
            "            background: white;\n" +
            "            border: 1px solid #dee2e6;\n" +
            "            border-radius: 20px;\n" +
            "            padding: 6px 12px;\n" +
            "            font-size: 0.85em;\n" +
            "            cursor: pointer;\n" +
            "            transition: all 0.3s ease;\n" +
            "        }\n" +
            "        \n" +
            "        .filter-tag:hover {\n" +
            "            background: #667eea;\n" +
            "            color: white;\n" +
            "            border-color: #667eea;\n" +
"        }\n" +
            "        \n" +
            "        .filter-tag.active {\n" +
            "            background: #667eea;\n" +
            "            color: white;\n" +
            "            border-color: #667eea;\n" +
            "        }\n" +
            "    </style>\n" +
            "</head>");
        
        // 在搜索框后添加搜索历史
        content = content.replace(
            "<div class=\"search-box\">",
            "<div class=\"search-box\">\n" +
            "                <input type=\"text\" id=\"searchInput\" class=\"search-input\" placeholder=\"输入搜索关键词...\">\n" +
            "                <button class=\"search-button\" onclick=\"performSearch()\">🔍</button>\n" +
            "            </div>\n" +
            "            <div class=\"search-history\">\n" +
            "                <button class=\"history-toggle\" onclick=\"toggleHistory()\">📋 搜索历史</button>\n" +
            "                <div id=\"historyList\" class=\"history-list\"></div>\n" +
            "            </div>\n" +
            "            \n" +
            "            <div class=\"quick-filters\">\n" +
            "                <div class=\"filter-group\">\n" +
            "                    <label>快速筛选</label>\n" +
            "                    <div class=\"filter-tags\">\n" +
            "                        <span class=\"filter-tag\" onclick=\"quickFilter('图片')\">🖼️ 图片</span>\n" +
            "                        <span class=\"filter-tag\" onclick=\"quickFilter('文件')\">📎 文件</span>\n" +
            "                        <span class=\"filter-tag\" onclick=\"quickFilter('语音')\">🎵 语音</span>\n" +
            "                        <span class=\"filter-tag\" onclick=\"quickFilter('视频')\">🎥 视频</span>\n" +
            "                        <span class=\"filter-tag\" onclick=\"quickFilter('链接')\">🔗 链接</span>\n" +
            "                        <span class=\"filter-tag\" onclick=\"quickFilter('表情')\">😊 表情</span>\n" +
            "                    </div>\n" +
            "                </div>\n" +
            "            </div>");
        
        // 在导航前添加统计信息
        content = content.replace(
            "        <nav class=\"navigation\">",
            "        <section class=\"stats-section\">\n" +
            "            <div class=\"stats-grid\">\n" +
            "                <div class=\"stat-card\">\n" +
            "                    <div class=\"stat-number\" id=\"totalFiles\">0</div>\n" +
            "                    <div class=\"stat-label\">聊天文件</div>\n" +
            "                </div>\n" +
            "                <div class=\"stat-card\">\n" +
            "                    <div class=\"stat-number\" id=\"totalMessages\">0</div>\n" +
            "                    <div class=\"stat-label\">消息总数</div>\n" +
            "                </div>\n" +
            "                <div class=\"stat-card\">\n" +
            "                    <div class=\"stat-number\" id=\"totalSearches\">0</div>\n" +
            "                    <div class=\"stat-label\">搜索次数</div>\n" +
            "                </div>\n" +
            "                <div class=\"stat-card\">\n" +
            "                    <div class=\"stat-number\" id=\"dateRange\">0</div>\n" +
            "                    <div class=\"stat-label\">天数跨度</div>\n" +
            "                </div>\n" +
            "            </div>\n" +
            "        </section>\n" +
            "\n" +
            "        <nav class=\"navigation\">");
        
        // 在script标签末尾添加搜索历史功能
        content = content.replace(
            "    </script>",
            searchHistoryScript + "\n    </script>");
        
        // 写回文件
        Files.write(searchPagePath, content.getBytes("UTF-8"));
        
        System.out.println("搜索页面增强完成");
    }
    
    private static String generateSearchHistoryScript() {
        return "\n" +
            "        // 搜索历史功能\n" +
            "        let searchHistory = JSON.parse(localStorage.getItem('chatSearchHistory') || '[]');\n" +
            "        let searchStats = JSON.parse(localStorage.getItem('chatSearchStats') || '{\"totalSearches\": 0}');\n" +
            "\n" +
            "        // 初始化搜索历史\n" +
            "        function initializeSearchHistory() {\n" +
            "            updateHistoryDisplay();\n" +
            "            updateStats();\n" +
            "        }\n" +
            "\n" +
            "        // 切换搜索历史显示\n" +
            "        function toggleHistory() {\n" +
            "            const historyList = document.getElementById('historyList');\n" +
            "            historyList.classList.toggle('show');\n" +
            "            updateHistoryDisplay();\n" +
            "        }\n" +
            "\n" +
            "        // 更新搜索历史显示\n" +
            "        function updateHistoryDisplay() {\n" +
            "            const historyList = document.getElementById('historyList');\n" +
            "            \n" +
            "            if (searchHistory.length === 0) {\n" +
            "                historyList.innerHTML = '<div style=\"padding: 20px; text-align: center; color: #6c757d;\">暂无搜索历史</div>';\n" +
            "                return;\n" +
            "            }\n" +
            "\n" +
            "            let html = '';\n" +
            "            searchHistory.slice(0, 10).forEach((term, index) => {\n" +
            "                html += `<div class=\"history-item\" onclick=\"searchFromHistory('${term}')\">${term}</div>`;\n" +
            "            });\n" +
            "            \n" +
            "            html += '<button class=\"clear-history\" onclick=\"clearSearchHistory()\">清空历史</button>';\n" +
            "            historyList.innerHTML = html;\n" +
            "        }\n" +
            "\n" +
            "        // 从历史记录搜索\n" +
            "        function searchFromHistory(term) {\n" +
            "            document.getElementById('searchInput').value = term;\n" +
            "            document.getElementById('historyList').classList.remove('show');\n" +
            "            performSearch();\n" +
            "        }\n" +
            "\n" +
            "        // 添加搜索历史\n" +
            "        function addToSearchHistory(term) {\n" +
            "            if (!term || term.length < 2) return;\n" +
            "            \n" +
            "            // 移除重复项\n" +
            "            searchHistory = searchHistory.filter(item => item !== term);\n" +
            "            // 添加到开头\n" +
            "            searchHistory.unshift(term);\n" +
            "            // 限制历史记录数量\n" +
            "            searchHistory = searchHistory.slice(0, 20);\n" +
            "            \n" +
            "            localStorage.setItem('chatSearchHistory', JSON.stringify(searchHistory));\n" +
            "            updateHistoryDisplay();\n" +
            "        }\n" +
            "\n" +
            "        // 清空搜索历史\n" +
            "        function clearSearchHistory() {\n" +
            "            if (confirm('确定要清空搜索历史吗？')) {\n" +
            "                searchHistory = [];\n" +
            "                localStorage.setItem('chatSearchHistory', JSON.stringify(searchHistory));\n" +
            "                updateHistoryDisplay();\n" +
            "            }\n" +
            "        }\n" +
            "\n" +
            "        // 更新统计信息\n" +
            "        function updateStats() {\n" +
            "            // 文件总数\n" +
            "            document.getElementById('totalFiles').textContent = allChatFiles.length;\n" +
            "            \n" +
            "            // 日期范围\n" +
            "            if (allChatFiles.length > 0) {\n" +
            "                const firstDate = allChatFiles[0].date;\n" +
            "                const lastDate = allChatFiles[allChatFiles.length - 1].date;\n" +
            "                const days = calculateDateDifference(lastDate, firstDate);\n" +
            "                document.getElementById('dateRange').textContent = days;\n" +
            "            }\n" +
            "            \n" +
            "            // 搜索次数\n" +
            "            document.getElementById('totalSearches').textContent = searchStats.totalSearches;\n" +
            "        }\n" +
            "\n" +
            "        // 计算日期差\n" +
            "        function calculateDateDifference(dateStr1, dateStr2) {\n" +
            "            const date1 = new Date(dateStr1.replace(/_/g, '-'));\n" +
            "            const date2 = new Date(dateStr2.replace(/_/g, '-'));\n" +
            "            const diffTime = Math.abs(date1 - date2);\n" +
            "            const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));\n" +
            "            return diffDays + 1; // +1 因为包含首尾两天\n" +
            "        }\n" +
            "\n" +
            "        // 快速筛选\n" +
            "        function quickFilter(type) {\n" +
            "            document.getElementById('searchInput').value = type;\n" +
            "            \n" +
            "            // 更新标签状态\n" +
            "            document.querySelectorAll('.filter-tag').forEach(tag => {\n" +
            "                tag.classList.remove('active');\n" +
            "            });\n" +
            "            event.target.classList.add('active');\n" +
            "            \n" +
            "            performSearch();\n" +
            "        }\n" +
            "\n" +
            "        // 修改performSearch函数以包含历史记录和统计\n" +
            "        const originalPerformSearch = performSearch;\n" +
            "        performSearch = function() {\n" +
            "            const searchTerm = document.getElementById('searchInput').value.trim();\n" +
            "            \n" +
            "            // 添加到搜索历史\n" +
            "            if (searchTerm) {\n" +
            "                addToSearchHistory(searchTerm);\n" +
            "                \n" +
            "                // 更新搜索统计\n" +
            "                searchStats.totalSearches++;\n" +
            "                localStorage.setItem('chatSearchStats', JSON.stringify(searchStats));\n" +
            "                document.getElementById('totalSearches').textContent = searchStats.totalSearches;\n" +
            "            }\n" +
            "            \n" +
            "            // 调用原始搜索函数\n" +
            "            originalPerformSearch();\n" +
            "        };\n" +
            "\n" +
            "        // 点击其他地方关闭历史记录\n" +
            "        document.addEventListener('click', function(e) {\n" +
            "            const historyContainer = document.querySelector('.search-history');\n" +
            "            if (!historyContainer.contains(e.target)) {\n" +
            "                document.getElementById('historyList').classList.remove('show');\n" +
            "            }\n" +
            "        });\n" +
            "\n" +
            "        // 页面加载时初始化\n" +
            "        document.addEventListener('DOMContentLoaded', function() {\n" +
            "            initializeSearchHistory();\n" +
            "        });";
    }
}
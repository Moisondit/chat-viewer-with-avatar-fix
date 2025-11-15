# 批量修复Data目录下日期HTML文件的导航样式
# 作者：AI助手
# 功能：为所有chat_*.html文件添加导航按钮样式，修复底部导航挡住内容的问题

# 设置目录路径
$dataDir = "..\Data"

# 获取所有chat_开头的HTML文件
$files = Get-ChildItem -Path $dataDir -Filter "chat_*.html"

Write-Host "找到 $($files.Count) 个日期HTML文件需要处理"
Write-Host "开始批量修复导航样式..."

$processedCount = 0
$errorCount = 0

foreach ($file in $files) {
    try {
        # 读取文件内容
        $content = Get-Content -Path $file.FullName -Raw -Encoding UTF8
        
        # 检查是否已经包含导航样式
        if ($content -match "\.nav-btn\s*\{") {
            Write-Host "检查 $($file.Name) - 已包含导航样式，检查底部导航..."
            
            # 修复底部导航样式 - 将固定定位改为正常布局
            $fixedBottomNavPattern = '\.bottom-nav\s*\{\s*position:\s*fixed;[^}]*\}'
            $newBottomNav = '.bottom-nav {
        margin: 30px 0;
        text-align: center;
        padding: 20px 0;
        background: linear-gradient(135deg, #f8f9fa, #e9ecef);
        border-radius: 15px;
        box-shadow: 0 -2px 10px rgba(0,0,0,0.1);
    }'
            
            if ($content -match $fixedBottomNavPattern) {
                $content = $content -replace $fixedBottomNavPattern, $newBottomNav
                Set-Content -Path $file.FullName -Value $content -Encoding UTF8
                Write-Host "✓ 已修复 $($file.Name) 的底部导航样式"
                $processedCount++
            }
            else {
                Write-Host "跳过 $($file.Name) - 底部导航样式已正确"
            }
            continue
        }
        
        # 查找back-link:hover样式位置
        $pattern = "\.back-link:hover\s*\{[^}]*\}"
        
        if ($content -match $pattern) {
            # 准备要插入的导航样式
            $navStyles = @"

    /* 导航按钮样式 */
    .navigation {
        text-align: center;
        margin: 20px 0;
    }

    .nav-btn {
        display: inline-block;
        padding: 12px 25px;
        background: linear-gradient(135deg, #27ae60, #2ecc71);
        color: white;
        text-decoration: none;
        border-radius: 25px;
        font-weight: bold;
        transition: all 0.3s ease;
        box-shadow: 0 4px 15px rgba(46, 204, 113, 0.3);
    }

    .nav-btn:hover {
        background: linear-gradient(135deg, #229954, #27ae60);
        transform: translateY(-2px);
        box-shadow: 0 6px 20px rgba(46, 204, 113, 0.4);
    }

    .bottom-nav {
        margin: 30px 0;
        text-align: center;
        padding: 20px 0;
        background: linear-gradient(135deg, #f8f9fa, #e9ecef);
        border-radius: 15px;
        box-shadow: 0 -2px 10px rgba(0,0,0,0.1);
    }
"@
            
            # 在back-link:hover后插入导航样式
            $newContent = $content -replace $pattern, "$&$navStyles"
            
            # 保存修改后的文件
            Set-Content -Path $file.FullName -Value $newContent -Encoding UTF8
            
            Write-Host "✓ 已修复 $($file.Name)"
            $processedCount++
        }
        else {
            Write-Host "⚠ $($file.Name) - 未找到back-link:hover样式位置"
            $errorCount++
        }
    }
    catch {
        Write-Host "✗ 处理 $($file.Name) 时出错: $($_.Exception.Message)"
        $errorCount++
    }
}

Write-Host ""
Write-Host "批量修复完成！"
Write-Host "成功处理: $processedCount 个文件"
Write-Host "出错文件: $errorCount 个文件"
Write-Host "总计: $($files.Count) 个文件"

# 询问是否要预览修复结果
Write-Host ""
Write-Host "现在可以预览修复后的页面效果了。"
Write-Host "底部返回按钮已改为正常布局，不再挡住聊天内容。"
Write-Host "建议打开以下页面测试："
Write-Host "- file:///c:/Users/ASUS/Desktop/游戏/星如雨[2872215021]/Data/chat_2022_09_24.html"
Write-Host "- file:///c:/Users/ASUS/Desktop/游戏/星如雨[2872215021]/Data/chat_2022_10_01.html"
Write-Host "- file:///c:/Users/ASUS/Desktop/游戏/星如雨[2872215021]/Data/chat_2023_01_01.html"
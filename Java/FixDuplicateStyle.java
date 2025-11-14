import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.regex.*;
import java.util.*;

public class FixDuplicateStyle {
    public static void main(String[] args) {
        try {
            // 修复Data目录下所有chat_*.html文件中的重复<style>标签
            fixDuplicateStyles();

            System.out.println("=== 重复<style>标签修复完成 ===");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void fixDuplicateStyles() throws IOException {
        String dataDir = "c:\\Users\\ASUS\\Desktop\\游戏\\星如雨[2872215021]\\Data";

        // 获取Data目录下所有chat_开头的HTML文件
        Path dataPath = Paths.get(dataDir);
        if (!Files.exists(dataPath)) {
            System.out.println("Data目录不存在: " + dataDir);
            return;
        }

        List<Path> htmlFiles = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dataPath, "chat_*.html")) {
            for (Path file : stream) {
                htmlFiles.add(file);
            }
        }

        System.out.println("找到 " + htmlFiles.size() + " 个chat_*.html文件");

        int fixedCount = 0;

        for (Path htmlFile : htmlFiles) {
            try {
                // 读取文件内容
                String content = Files.readString(htmlFile, StandardCharsets.UTF_8);
                String originalContent = content;

                // 修复重复的<style>标签
                // 匹配连续的<style>标签和</style>标签
                Pattern duplicateStylePattern = Pattern.compile(
                        "<style>\\s*<style>(.*?)</style>\\s*</style>",
                        Pattern.DOTALL);

                Matcher matcher = duplicateStylePattern.matcher(content);
                if (matcher.find()) {
                    // 替换为单个<style>标签
                    content = matcher.replaceAll("<style>$1</style>");
                    System.out.println("修复文件: " + htmlFile.getFileName());

                    // 写回文件
                    Files.writeString(htmlFile, content, StandardCharsets.UTF_8);
                    fixedCount++;
                } else {
                    // 检查是否有其他形式的重复
                    Pattern anotherDuplicatePattern = Pattern.compile(
                            "<style[^>]*>\\s*<style[^>]*>(.*?)</style>\\s*</style>",
                            Pattern.DOTALL);

                    Matcher anotherMatcher = anotherDuplicatePattern.matcher(content);
                    if (anotherMatcher.find()) {
                        content = anotherMatcher.replaceAll("<style>$1</style>");
                        System.out.println("修复文件(另一种格式): " + htmlFile.getFileName());
                        Files.writeString(htmlFile, content, StandardCharsets.UTF_8);
                        fixedCount++;
                    }
                }

            } catch (Exception e) {
                System.out.println("处理文件 " + htmlFile.getFileName() + " 时出错: " + e.getMessage());
            }
        }

        System.out.println("\n修复完成！共修复了 " + fixedCount + " 个文件");
    }
}
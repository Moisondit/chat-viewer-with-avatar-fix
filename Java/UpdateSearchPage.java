import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class UpdateSearchPage {
    
    public static void main(String[] args) {
        try {
            updateSearchPageWithAllFiles();
            System.out.println("搜索页面更新成功！已包含所有聊天文件");
        } catch (IOException e) {
            System.err.println("更新搜索页面失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void updateSearchPageWithAllFiles() throws IOException {
        // 生成完整的文件列表
        String fileDates = generateCompleteFileList();
        
        // 读取现有的搜索页面
        Path searchPagePath = Paths.get("c:\\Users\\ASUS\\Desktop\\游戏\\星如雨[2872215021]\\search.html");
        String content = Files.readString(searchPagePath);
        
        // 替换文件列表部分
        String oldFileList = "// 生成聊天文件列表（基于已知的文件命名模式）\\s*\\n\\s*const fileDates = \\[.*?\\];";
        String newFileList = "// 生成聊天文件列表（基于实际文件扫描）\\n            const fileDates = [" + fileDates + "];";
        
        content = content.replaceAll(oldFileList, newFileList);
        
        // 写回文件
        Files.write(searchPagePath, content.getBytes("UTF-8"));
        
        System.out.println("搜索页面已更新，包含所有聊天文件");
    }
    
    private static String generateCompleteFileList() {
        StringBuilder sb = new StringBuilder();
        
        // 2022年的文件
        String[] files2022 = {
            "2022_09_24",
            "2022_10_01", "2022_10_02", "2022_10_05", "2022_10_06", "2022_10_07",
            "2022_10_09", "2022_10_10", "2022_10_11", "2022_10_14", "2022_10_15", "2022_10_16",
            "2022_10_20", "2022_10_21", "2022_10_22", "2022_10_23", "2022_10_26", "2022_10_28",
            "2022_10_29", "2022_10_30", "2022_10_31", "2022_11_01", "2022_11_02", "2022_11_03",
            "2022_11_04", "2022_11_05", "2022_11_06", "2022_11_07", "2022_11_09", "2022_11_10",
            "2022_11_11", "2022_11_12", "2022_11_13", "2022_11_14", "2022_11_15", "2022_11_16",
            "2022_11_17", "2022_11_18", "2022_11_19", "2022_11_20", "2022_11_21", "2022_11_22",
            "2022_11_23", "2022_11_24", "2022_11_25", "2022_11_26", "2022_11_27", "2022_11_28",
            "2022_11_29", "2022_11_30", "2022_12_02", "2022_12_03", "2022_12_10", "2022_12_12",
            "2022_12_20", "2022_12_28", "2022_12_29", "2022_12_30", "2022_12_31"
        };
        
        // 2023年1月的文件
        String[] files2023_01 = {
            "2023_01_01", "2023_01_02", "2023_01_03", "2023_01_04", "2023_01_05", "2023_01_06",
            "2023_01_07", "2023_01_08", "2023_01_09", "2023_01_10", "2023_01_11", "2023_01_12",
            "2023_01_13", "2023_01_14", "2023_01_15", "2023_01_16", "2023_01_17", "2023_01_18",
            "2023_01_19", "2023_01_20", "2023_01_21", "2023_01_22", "2023_01_23", "2023_01_24",
            "2023_01_25", "2023_01_26", "2023_01_27", "2023_01_28", "2023_01_29", "2023_01_30",
            "2023_01_31"
        };
        
        // 2023年2月的文件
        String[] files2023_02 = {
            "2023_02_01", "2023_02_02", "2023_02_03", "2023_02_04", "2023_02_05", "2023_02_06",
            "2023_02_07", "2023_02_08", "2023_02_09", "2023_02_10"
        };
        
        // 合并所有文件
        String[] allFiles = new String[files2022.length + files2023_01.length + files2023_02.length];
        System.arraycopy(files2022, 0, allFiles, 0, files2022.length);
        System.arraycopy(files2023_01, 0, allFiles, files2022.length, files2023_01.length);
        System.arraycopy(files2023_02, 0, allFiles, files2022.length + files2023_01.length, files2023_02.length);
        
        // 生成JavaScript数组
        for (int i = 0; i < allFiles.length; i++) {
            if (i > 0) sb.append(",\n                ");
            sb.append("'").append(allFiles[i]).append("'");
        }
        
        return sb.toString();
    }
}
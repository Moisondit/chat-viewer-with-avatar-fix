import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.regex.*;
import java.util.*;

public class EmojiConverter {
    
    // 表情映射表 - 从JSON配置文件中提取的关键映射
    private static final Map<String, String> EMOJI_MAP = new HashMap<>();
    
    static {
        // 基础表情映射
        EMOJI_MAP.put("/微笑", "100");
        EMOJI_MAP.put("/撇嘴", "101");
        EMOJI_MAP.put("/色", "102");
        EMOJI_MAP.put("/发呆", "103");
        EMOJI_MAP.put("/得意", "104");
        EMOJI_MAP.put("/流泪", "105");
        EMOJI_MAP.put("/害羞", "106");
        EMOJI_MAP.put("/闭嘴", "107");
        EMOJI_MAP.put("/睡", "108");
        EMOJI_MAP.put("/大哭", "109");
        EMOJI_MAP.put("/尴尬", "110");
        EMOJI_MAP.put("/发怒", "111");
        EMOJI_MAP.put("/调皮", "112");
        EMOJI_MAP.put("/呲牙", "113");
        EMOJI_MAP.put("/惊讶", "114");
        EMOJI_MAP.put("/难过", "115");
        EMOJI_MAP.put("/酷", "116");
        EMOJI_MAP.put("/冷汗", "117");
        EMOJI_MAP.put("/抓狂", "118");
        EMOJI_MAP.put("/吐", "119");
        EMOJI_MAP.put("/偷笑", "120");
        EMOJI_MAP.put("/可爱", "121");
        EMOJI_MAP.put("/白眼", "122");
        EMOJI_MAP.put("/傲慢", "123");
        EMOJI_MAP.put("/饥饿", "124");
        EMOJI_MAP.put("/困", "125");
        EMOJI_MAP.put("/惊恐", "126");
        EMOJI_MAP.put("/流汗", "127");
        EMOJI_MAP.put("/憨笑", "128");
        EMOJI_MAP.put("/悠闲", "129");
        EMOJI_MAP.put("/奋斗", "130");
        EMOJI_MAP.put("/咒骂", "131");
        EMOJI_MAP.put("/疑问", "132");
        EMOJI_MAP.put("/嘘", "133");
        EMOJI_MAP.put("/晕", "134");
        EMOJI_MAP.put("/折磨", "135");
        EMOJI_MAP.put("/衰", "136");
        EMOJI_MAP.put("/骷髅", "137");
        EMOJI_MAP.put("/敲打", "138");
        EMOJI_MAP.put("/再见", "139");
        EMOJI_MAP.put("/擦汗", "140");
        EMOJI_MAP.put("/抠鼻", "141");
        EMOJI_MAP.put("/鼓掌", "142");
        EMOJI_MAP.put("/糗大了", "143");
        EMOJI_MAP.put("/坏笑", "144");
        EMOJI_MAP.put("/左哼哼", "145");
        EMOJI_MAP.put("/右哼哼", "146");
        EMOJI_MAP.put("/哈欠", "147");
        EMOJI_MAP.put("/鄙视", "148");
        EMOJI_MAP.put("/委屈", "149");
        EMOJI_MAP.put("/快哭了", "150");
        EMOJI_MAP.put("/阴险", "151");
        EMOJI_MAP.put("/亲亲", "152");
        EMOJI_MAP.put("/吓", "153");
        EMOJI_MAP.put("/可怜", "154");
        EMOJI_MAP.put("/菜刀", "155");
        EMOJI_MAP.put("/西瓜", "156");
        EMOJI_MAP.put("/啤酒", "157");
        EMOJI_MAP.put("/篮球", "158");
        EMOJI_MAP.put("/乒乓", "159");
        EMOJI_MAP.put("/咖啡", "160");
        EMOJI_MAP.put("/饭", "161");
        EMOJI_MAP.put("/猪头", "162");
        EMOJI_MAP.put("/玫瑰", "163");
        EMOJI_MAP.put("/凋谢", "164");
        EMOJI_MAP.put("/示爱", "165");
        EMOJI_MAP.put("/爱心", "166");
        EMOJI_MAP.put("/心碎", "167");
        EMOJI_MAP.put("/蛋糕", "168");
        EMOJI_MAP.put("/闪电", "169");
        EMOJI_MAP.put("/炸弹", "170");
        EMOJI_MAP.put("/刀", "171");
        EMOJI_MAP.put("/足球", "172");
        EMOJI_MAP.put("/瓢虫", "173");
        EMOJI_MAP.put("/便便", "174");
        EMOJI_MAP.put("/月亮", "175");
        EMOJI_MAP.put("/太阳", "176");
        EMOJI_MAP.put("/礼物", "177");
        EMOJI_MAP.put("/拥抱", "178");
        EMOJI_MAP.put("/赞", "179");
        EMOJI_MAP.put("/踩", "180");
        EMOJI_MAP.put("/握手", "181");
        EMOJI_MAP.put("/胜利", "182");
        EMOJI_MAP.put("/抱拳", "183");
        EMOJI_MAP.put("/勾引", "184");
        EMOJI_MAP.put("/拳头", "185");
        EMOJI_MAP.put("/差劲", "186");
        EMOJI_MAP.put("/爱你", "187");
        EMOJI_MAP.put("/NO", "188");
        EMOJI_MAP.put("/OK", "189");
        EMOJI_MAP.put("/爱情", "190");
        EMOJI_MAP.put("/飞吻", "191");
        EMOJI_MAP.put("/跳跳", "192");
        EMOJI_MAP.put("/发抖", "193");
        EMOJI_MAP.put("/怄火", "194");
        EMOJI_MAP.put("/转圈", "195");
        EMOJI_MAP.put("/磕头", "196");
        EMOJI_MAP.put("/回头", "197");
        EMOJI_MAP.put("/跳绳", "198");
        EMOJI_MAP.put("/挥手", "199");
        EMOJI_MAP.put("/激动", "200");
        EMOJI_MAP.put("/街舞", "201");
        EMOJI_MAP.put("/献吻", "202");
        EMOJI_MAP.put("/左太极", "203");
        EMOJI_MAP.put("/右太极", "204");
        
        // 新版表情
        EMOJI_MAP.put("/眨眼睛", "242");
        EMOJI_MAP.put("/泪奔", "243");
        EMOJI_MAP.put("/无奈", "244");
        EMOJI_MAP.put("/卖萌", "245");
        EMOJI_MAP.put("/小纠结", "246");
        EMOJI_MAP.put("/喷血", "247");
        EMOJI_MAP.put("/斜眼笑", "248");
        EMOJI_MAP.put("/doge", "249");
        EMOJI_MAP.put("/惊喜", "250");
        EMOJI_MAP.put("/骚扰", "251");
        EMOJI_MAP.put("/笑哭", "252");
        EMOJI_MAP.put("/我最美", "253");
        EMOJI_MAP.put("/加油必胜", "202001");
        EMOJI_MAP.put("/加油抱抱", "202002");
        EMOJI_MAP.put("/口罩护体", "202003");
        EMOJI_MAP.put("/搬砖中", "10260");
        EMOJI_MAP.put("/忙到飞起", "10261");
        EMOJI_MAP.put("/脑阔疼", "10262");
        EMOJI_MAP.put("/沧桑", "10263");
        EMOJI_MAP.put("/捂脸", "10264");
        EMOJI_MAP.put("/辣眼睛", "10265");
        EMOJI_MAP.put("/哦哟", "10266");
        EMOJI_MAP.put("/头秃", "10267");
        EMOJI_MAP.put("/问号脸", "10268");
        EMOJI_MAP.put("/暗中观察", "10269");
        EMOJI_MAP.put("/emm", "10270");
        EMOJI_MAP.put("/吃瓜", "10271");
        EMOJI_MAP.put("/呵呵哒", "10272");
        EMOJI_MAP.put("/我酸了", "10273");
        EMOJI_MAP.put("/太南了", "10274");
        EMOJI_MAP.put("/汪汪", "10277");
        EMOJI_MAP.put("/辣椒酱", "10276");
        EMOJI_MAP.put("/茶", "241");
        EMOJI_MAP.put("/祈祷", "121010");
        EMOJI_MAP.put("/双喜", "121001");
        EMOJI_MAP.put("/鞭炮", "121002");
        EMOJI_MAP.put("/灯笼", "121003");
        EMOJI_MAP.put("/K歌", "121005");
        EMOJI_MAP.put("/喝彩", "121009");
        EMOJI_MAP.put("/爆筋", "121011");
        EMOJI_MAP.put("/棒棒糖", "121011");
        EMOJI_MAP.put("/托腮", "282");
        EMOJI_MAP.put("/无眼笑", "10281");
        EMOJI_MAP.put("/敬礼", "10282");
        EMOJI_MAP.put("/狂笑", "10283");
        EMOJI_MAP.put("/面无表情", "10284");
        EMOJI_MAP.put("/摸鱼", "10285");
        EMOJI_MAP.put("/魔鬼笑", "10286");
        EMOJI_MAP.put("/哦", "10287");
        EMOJI_MAP.put("/请", "10288");
        EMOJI_MAP.put("/睁眼", "10289");
    }
    
    public static void main(String[] args) {
        processAllEmojiFiles();
    }
    
    public static void processAllEmojiFiles() {
        String dataDir = "c:\\Users\\ASUS\\Desktop\\游戏\\星如雨[2872215021]\\Data\\";
        String emoticonPath = "../生生[2137463976]/emoticon/new/";
        
        // 包含emoji的文件列表
        String[] emojiFiles = {
            "chat_2023_09_02.html",
            "chat_2023_10_02.html", 
            "chat_2023_07_20.html",
            "chat_2023_03_24.html",
            "chat_2023_09_29.html",
            "chat_2023_10_06.html",
            "chat_2023_09_03.html",
            "chat_2023_11_11.html",
            "chat_2023_08_29.html",
            "chat_2023_10_04.html",
            "chat_2023_10_08.html",
            "chat_2023_11_03.html",
            "chat_2023_11_04.html",
            "chat_2023_11_05.html",
            "chat_2023_03_19.html",
            "chat_2023_06_04.html",
            "chat_2023_10_01.html",
            "chat_2023_08_27.html",
            "chat_2023_05_23.html",
            "chat_2023_06_14.html",
            "chat_2023_10_16.html",
            "chat_2023_05_14.html",
            "chat_2023_08_28.html",
            "chat_2023_08_09.html",
            "chat_2023_04_19.html",
            "chat_2023_10_10.html",
            "chat_2023_03_11.html",
            "chat_2023_11_09.html",
            "chat_2023_10_14.html",
            "chat_2023_10_05.html",
            "chat_2023_05_01.html",
            "chat_2023_10_19.html",
            "chat_2023_09_14.html",
            "chat_2023_04_03.html",
            "chat_2022_11_04.html",
            "chat_2024_03_11.html",
            "chat_2022_11_10.html",
            "chat_2023_07_31.html",
            "chat_2023_10_03.html",
            "chat_2023_04_22.html",
            "chat_2024_03_06.html",
            "chat_2023_07_18.html",
            "chat_2023_08_01.html",
            "chat_2023_09_27.html",
            "chat_2024_03_03.html",
            "chat_2023_11_27.html",
            "chat_2025_08_26.html"
        };
        
        int totalProcessed = 0;
        int totalEmojiFound = 0;
        
        for (String fileName : emojiFiles) {
            try {
                int emojiCount = processEmojiFile(dataDir + fileName, emoticonPath);
                if (emojiCount > 0) {
                    System.out.println("处理文件: " + fileName + " - 找到 " + emojiCount + " 个emoji");
                    totalProcessed++;
                    totalEmojiFound += emojiCount;
                }
            } catch (Exception e) {
                System.err.println("处理文件 " + fileName + " 时出错: " + e.getMessage());
            }
        }
        
        System.out.println("\n=== Emoji处理完成 ===");
        System.out.println("总共处理了 " + totalProcessed + " 个文件");
        System.out.println("总共替换了 " + totalEmojiFound + " 个emoji表情");
    }
    
    public static int processEmojiFile(String filePath, String emoticonPath) throws IOException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            return 0;
        }
        
        String content = Files.readString(path, StandardCharsets.UTF_8);
        int originalEmojiCount = 0;
        int replacedCount = 0;
        
        // 统计原始emoji数量
        for (String emoji : EMOJI_MAP.keySet()) {
            Pattern pattern = Pattern.compile(Pattern.quote(emoji));
            Matcher matcher = pattern.matcher(content);
            while (matcher.find()) {
                originalEmojiCount++;
            }
        }
        
        if (originalEmojiCount == 0) {
            return 0;
        }
        
        // 替换emoji为HTML img标签
        for (Map.Entry<String, String> entry : EMOJI_MAP.entrySet()) {
            String emojiText = entry.getKey();
            String emojiCode = entry.getValue();
            
            // 构建替换的HTML
            String replacement = "<img src=\"" + emoticonPath + "s" + emojiCode + ".png\" alt=\"" + emojiText + "\" class=\"emoji\" style=\"width: 24px; height: 24px; vertical-align: middle; margin: 0 2px;\">";
            
            // 使用正则表达式替换
            Pattern pattern = Pattern.compile(Pattern.quote(emojiText));
            Matcher matcher = pattern.matcher(content);
            StringBuffer sb = new StringBuffer();
            
            while (matcher.find()) {
                matcher.appendReplacement(sb, replacement);
                replacedCount++;
            }
            matcher.appendTail(sb);
            content = sb.toString();
        }
        
        // 写回文件
        Files.writeString(path, content, StandardCharsets.UTF_8);
        
        System.out.println("文件: " + path.getFileName() + " - 原始emoji: " + originalEmojiCount + ", 替换: " + replacedCount);
        
        return replacedCount;
    }
}
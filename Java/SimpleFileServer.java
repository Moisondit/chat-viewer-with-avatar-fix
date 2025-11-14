import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.concurrent.*;

public class SimpleFileServer {
    private static final int PORT = 8000;
    
    public static void main(String[] args) {
        try {
            startSimpleServer();
        } catch (Exception e) {
            System.err.println("服务器启动失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void startSimpleServer() throws Exception {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("简单文件服务器已启动");
        System.out.println("访问地址: http://localhost:" + PORT);
        System.out.println("搜索页面: http://localhost:" + PORT + "/search_test_final.html");
        System.out.println("按 Ctrl+C 停止服务器");
        
        ExecutorService executor = Executors.newCachedThreadPool();
        
        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                executor.submit(() -> handleClient(clientSocket));
            }
        } finally {
            serverSocket.close();
            executor.shutdown();
        }
    }
    
    private static void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             OutputStream out = clientSocket.getOutputStream()) {
            
            String requestLine = in.readLine();
            if (requestLine == null) return;
            
            // 解析请求
            String[] parts = requestLine.split(" ");
            if (parts.length < 2) return;
            
            String method = parts[0];
            String path = parts[1];
            
            if (!method.equals("GET")) {
                sendError(out, 405, "Method Not Allowed");
                return;
            }
            
            // 处理路径
            if (path.equals("/")) {
                path = "/index.html";
            }
            
            // 移除查询参数
            int queryIndex = path.indexOf('?');
            if (queryIndex > 0) {
                path = path.substring(0, queryIndex);
            }
            
            // 安全检查
            if (path.contains("..")) {
                sendError(out, 403, "Forbidden");
                return;
            }
            
            // 查找文件
            Path filePath = Paths.get("." + path);
            
            if (Files.exists(filePath) && !Files.isDirectory(filePath)) {
                sendFile(out, filePath);
            } else {
                // 尝试添加.html扩展名
                Path htmlPath = Paths.get("." + path + ".html");
                if (Files.exists(htmlPath)) {
                    sendFile(out, htmlPath);
                } else {
                    sendError(out, 404, "Not Found");
                }
            }
            
        } catch (Exception e) {
            System.err.println("处理客户端请求时出错: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                // 忽略关闭错误
            }
        }
    }
    
    private static void sendFile(OutputStream out, Path filePath) throws IOException {
        byte[] fileContent = Files.readAllBytes(filePath);
        String contentType = getContentType(filePath.toString());
        
        String header = "HTTP/1.1 200 OK\r\n" +
                       "Content-Type: " + contentType + "\r\n" +
                       "Content-Length: " + fileContent.length + "\r\n" +
                       "Access-Control-Allow-Origin: *\r\n" +
                       "Connection: close\r\n" +
                       "\r\n";
        
        out.write(header.getBytes("UTF-8"));
        out.write(fileContent);
        out.flush();
    }
    
    private static void sendError(OutputStream out, int code, String message) throws IOException {
        String html = "<html><body><h1>" + code + " " + message + "</h1><p>Java File Server</p></body></html>";
        byte[] content = html.getBytes("UTF-8");
        
        String header = "HTTP/1.1 " + code + " " + message + "\r\n" +
                       "Content-Type: text/html; charset=UTF-8\r\n" +
                       "Content-Length: " + content.length + "\r\n" +
                       "Connection: close\r\n" +
                       "\r\n";
        
        out.write(header.getBytes("UTF-8"));
        out.write(content);
        out.flush();
    }
    
    private static String getContentType(String fileName) {
        if (fileName.endsWith(".html") || fileName.endsWith(".htm")) {
            return "text/html; charset=UTF-8";
        } else if (fileName.endsWith(".css")) {
            return "text/css";
        } else if (fileName.endsWith(".js")) {
            return "application/javascript";
        } else if (fileName.endsWith(".json")) {
            return "application/json";
        } else if (fileName.endsWith(".png")) {
            return "image/png";
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (fileName.endsWith(".gif")) {
            return "image/gif";
        } else if (fileName.endsWith(".ico")) {
            return "image/x-icon";
        } else {
            return "text/plain; charset=UTF-8";
        }
    }
}
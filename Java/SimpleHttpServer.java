import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;

public class SimpleHttpServer {
    private static final int PORT = 8000;
    private static final String WEB_ROOT = ".";
    private static volatile boolean running = true;

    public static void main(String[] args) {
        try {
            startServer();
        } catch (IOException e) {
            System.err.println("服务器启动失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void startServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("HTTP服务器已启动");
        System.out.println("访问地址: http://localhost:" + PORT);
        System.out.println("搜索页面: http://localhost:" + PORT + "/search_test_final.html");
        System.out.println("按 Ctrl+C 停止服务器");

        while (running) {
            try {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleRequest(clientSocket)).start();
            } catch (IOException e) {
                if (running) {
                    System.err.println("接受连接时出错: " + e.getMessage());
                }
            }
        }
        
        serverSocket.close();
    }

    private static void handleRequest(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
             BufferedOutputStream dataOut = new BufferedOutputStream(clientSocket.getOutputStream())) {

            String requestLine = in.readLine();
            if (requestLine == null) return;

            StringTokenizer tokenizer = new StringTokenizer(requestLine);
            String method = tokenizer.nextToken();
            String fileRequested = tokenizer.nextToken();

            if (fileRequested.equals("/")) {
                fileRequested = "/index.html";
            }

            // 处理查询参数
            if (fileRequested.contains("?")) {
                fileRequested = fileRequested.substring(0, fileRequested.indexOf("?"));
            }

            Path filePath = Paths.get(WEB_ROOT + fileRequested).normalize();
            
            // 安全检查：确保文件在web根目录内
            if (!filePath.startsWith(Paths.get(WEB_ROOT).normalize())) {
                sendErrorResponse(out, dataOut, 403, "Forbidden");
                return;
            }

            if (Files.exists(filePath) && !Files.isDirectory(filePath)) {
                String contentType = getContentType(filePath.toString());
                byte[] fileData = Files.readAllBytes(filePath);

                sendResponse(out, dataOut, 200, "OK", contentType, fileData);
            } else {
                // 尝试添加.html扩展名
                Path htmlPath = Paths.get(WEB_ROOT + fileRequested + ".html");
                if (Files.exists(htmlPath)) {
                    String contentType = getContentType(htmlPath.toString());
                    byte[] fileData = Files.readAllBytes(htmlPath);
                    sendResponse(out, dataOut, 200, "OK", contentType, fileData);
                } else {
                    sendErrorResponse(out, dataOut, 404, "Not Found");
                }
            }

        } catch (IOException e) {
            System.err.println("处理请求时出错: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("关闭连接时出错: " + e.getMessage());
            }
        }
    }

    private static void sendResponse(PrintWriter out, BufferedOutputStream dataOut, 
                                   int statusCode, String statusText, String contentType, byte[] data) 
            throws IOException {
        
        out.println("HTTP/1.1 " + statusCode + " " + statusText);
        out.println("Server: Java HTTP Server");
        out.println("Date: " + new Date());
        out.println("Content-type: " + contentType);
        out.println("Content-length: " + data.length);
        out.println("Access-Control-Allow-Origin: *");
        out.println("Connection: close");
        out.println();
        out.flush();

        dataOut.write(data);
        dataOut.flush();
    }

    private static void sendErrorResponse(PrintWriter out, BufferedOutputStream dataOut, 
                                        int statusCode, String statusText) throws IOException {
        
        String errorMessage = "<html><body><h1>" + statusCode + " " + statusText + "</h1>" +
                             "<p>Java HTTP Server</p></body></html>";
        byte[] errorData = errorMessage.getBytes("UTF-8");

        sendResponse(out, dataOut, statusCode, statusText, "text/html", errorData);
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

    public static void stopServer() {
        running = false;
    }
}
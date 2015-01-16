package cn.coderallen.excel2html.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

/**
 * HTTP响应常用代码封装。
 * 
 */
public class ResponseUtils {

    private static final String ENCODING_UTF8 = "utf-8";
    public static final String MIME_APPLICATION_JSON = "application/json";

    public static void setResponseHeaders(HttpServletResponse response) {
        response.setHeader("cach-control", "no-cache");
        response.setContentType(MIME_APPLICATION_JSON);
        response.setCharacterEncoding(ENCODING_UTF8);
    }

    // 如果响应头设置为application/json,js自动将返回值解析为js对象。text/plain,js将返回值解析为字符串
    public static void sendJSONData(HttpServletResponse response, String jsonData) throws IOException {
        PrintWriter out = response.getWriter();
        out.print(jsonData);
        out.close();
    }
}

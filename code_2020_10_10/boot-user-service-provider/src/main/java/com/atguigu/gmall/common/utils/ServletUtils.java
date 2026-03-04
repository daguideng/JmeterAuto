package com.atguigu.gmall.common.utils;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

public class ServletUtils {
    public static final String APPLICATION_TYPE_ZIP = "application/zip";
    public static final String APPLICATION_TYPE_HTML = "text/html";
    public static final String APPLICATION_TYPE_PDF = "application/pdf";
    public static final String APPLICATION_TYPE_XLS = "application/vnd.ms-excel";
    public static final String APPLICATION_TYPE_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public static final String APPLICATION_TYPE_TXT = "text/plain";
    public static final String APPLICATION_TYPE_DOC = "application/msword";
    public static final String APPLICATION_TYPE_DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    public static final String APPLICATION_TYPE_PNG = "image/png";
    public static final String APPLICATION_TYPE_JPEG = "image/jpeg";
    private static final String UNKNOWN = "unknown";

    /**
     * 设定下载文件的响应头，并对文件名进行转码处理。设定的头包括：
     * Content-Type
     * Content-Disposition
     * Content-Length
     *
     * @param resp
     *        响应对象
     * @param applicationType
     *        内容类型，若未知可给""（非null），方法内会进行有限的尝试
     * @param fileName
     *        文件名
     * @param fileSize
     *        文件大小，若未知可给0
     * @return 设定头后的响应对象（原对象）
     */
    public static HttpServletResponse setAttachmentHeader(
            HttpServletResponse resp, String applicationType, String fileName,
            int fileSize) {
        if (StringUtils.isBlank(fileName)) {
            throw new IllegalArgumentException("fileName should not be blank");
        }

        if (StringUtils.isBlank(applicationType)) {
            applicationType = ServletUtils.guessContentTypeByFileName(fileName);
        }
        if (StringUtils.isNotBlank(applicationType)) {
            resp.setContentType(applicationType + ";charset=UTF-8");
        }

        if (fileSize > 0) {
            resp.setContentLength(fileSize);
        }

        String name = null;
        try {
            name = URLEncoder.encode(fileName, "utf8");
        } catch (UnsupportedEncodingException ex) {
            name = fileName;
        }

        resp.setHeader("Content-Disposition",
            "attachment; filename*=utf-8''" + name);

        return resp;
    }

    public static String getRemoteIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isBlank(ip)
            || ServletUtils.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip)
            || ServletUtils.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip)
            || ServletUtils.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(ip)
            || ServletUtils.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(ip)
            || ServletUtils.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static String guessContentTypeByFileName(String filename) {
        switch (FilenameUtils.getExtension(filename).toLowerCase(Locale.US)) {
            case "doc":
                return ServletUtils.APPLICATION_TYPE_DOC;
            case "docx":
                return ServletUtils.APPLICATION_TYPE_DOCX;
            case "pdf":
                return ServletUtils.APPLICATION_TYPE_PDF;
            case "txt":
                return ServletUtils.APPLICATION_TYPE_TXT;
            case "xls":
                return ServletUtils.APPLICATION_TYPE_XLS;
            case "xlsx":
                return ServletUtils.APPLICATION_TYPE_XLSX;
            case "zip":
                return ServletUtils.APPLICATION_TYPE_ZIP;
            case "html":
            case "htm":
                return ServletUtils.APPLICATION_TYPE_HTML;
            case "png":
                return ServletUtils.APPLICATION_TYPE_PNG;
            case "jpg":
                return ServletUtils.APPLICATION_TYPE_JPEG;
            case "jpeg":
                return ServletUtils.APPLICATION_TYPE_JPEG;
            default:
                return "";
        }
    }
}

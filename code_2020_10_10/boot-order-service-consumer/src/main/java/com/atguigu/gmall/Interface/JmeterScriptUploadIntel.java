package com.atguigu.gmall.Interface;

import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2018-9-29
 */
public interface JmeterScriptUploadIntel {

    public Map<String, Object> uploadScript(MultipartFile file, HttpServletResponse response,HttpSession session) throws IOException;

    public String getFileNameNoEx(String filename) ;

    public String getfileSuffix(String filezip) ;

}

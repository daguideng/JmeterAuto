package com.atguigu.gmall.common.utils;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**HttpUtils.java 网络下载工具类
 * 网络下载工具类
 * 功能：下载字节数组，下载文本数据
 * 下载数字数组（文本 图片 mp3）
 * 下载文本数据
 * Created by on 2017/6/27.
 */
public class HttpUtils {
     
    /** 
     * 从网络Url中下载文件 
     * @param urlStr 
     * @param fileName 
     * @param savePath 
     * @throws IOException 
     */  
    public static void  downLoadFromUrl(String urlStr,String fileName,String savePath) throws IOException{  
        URL url = new URL(urlStr);    
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();    
                //设置超时间为3秒  
        conn.setConnectTimeout(15*1000);  
        //防止屏蔽程序抓取而返回403错误  
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");  
  
        //得到输入流  
        InputStream inputStream = conn.getInputStream();    
        //获取自己数组  
        byte[] getData = readInputStream(inputStream);      
  
        //文件保存位置  
        File saveDir = new File(savePath);  
        if(!saveDir.exists()){  
            saveDir.mkdirs();  
        }  
        File file = new File(saveDir+File.separator+fileName);      
        FileOutputStream fos = new FileOutputStream(file);       
        fos.write(getData);   
        if(fos!=null){  
            fos.close();    
        }  
        if(inputStream!=null){  
            inputStream.close();  
        }  
  
  
        System.out.println("info:"+url+" download success");
  
    }  
  
  
  
    /** 
     * 从输入流中获取字节数组 
     * @param inputStream 
     * @return 
     * @throws IOException 
     */  
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {    
        byte[] buffer = new byte[1024];    
        int len = 0;    
        ByteArrayOutputStream bos = new ByteArrayOutputStream();    
        while((len = inputStream.read(buffer)) != -1) {    
            bos.write(buffer, 0, len);    
        }    
        bos.close();    
        return bos.toByteArray();    
    }    



    

      public static void hutoolDown(String inputString,String targetPath) {
        // 原始字符串
       // String inputString = urlStr;
        
        // 提取URL
        String url = extractUrlFromString(inputString);
        
        if (url != null) {
            System.out.println("提取到的URL: " + url);
            
            // 指定下载路径
       //     String targetPath = "D:/downloads/Operatorcreate.zip"; // 修改为您需要的路径
            
            try {
                // 下载文件
                long downloadSize = HttpUtil.downloadFile(url, FileUtil.file(targetPath));
                System.out.println("文件下载成功! 保存路径: " + targetPath);
                System.out.println("文件大小: " + downloadSize + " 字节");
            } catch (Exception e) {
                System.err.println("下载失败: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.err.println("未能从字符串中提取到URL");
        }
    }
    
    /**
     * 从字符串中提取URL
     * @param input 输入字符串
     * @return 提取到的URL，如果未找到则返回null
     */
    public static String extractUrlFromString(String input) {
        // 正则表达式匹配URL
        // 正则表达式匹配href属性值
        String regex = "href=([^\\s>]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        
        if (matcher.find()) {
            String hrefValue = matcher.group(1);
            // 去除可能的引号
            if (hrefValue.startsWith("\"") || hrefValue.startsWith("'")) {
                hrefValue = hrefValue.substring(1, hrefValue.length() - 1);
            }
            return hrefValue;
        }
        return null;
    
    }




  
    public static void main(String[] args) {  
        try{  
        	
     
            downLoadFromUrl("http://mirrors.hust.edu.cn/apache//jmeter/source/apache-jmeter-4.0_src.zip","apache-jmeter-4.0_src.zip","d:/resource/images");  
        }catch (Exception e) {  
            // TODO: handle exception  
        }  


       


       


    }  
    
    
    
    
    
    
    
    
    
    

    

}
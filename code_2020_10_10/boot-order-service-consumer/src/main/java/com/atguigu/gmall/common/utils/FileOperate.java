package com.atguigu.gmall.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.util.StringTokenizer;
import java.net.URL;
import java.net.URLConnection;


public class FileOperate {
	
	
	
	
	
	private static String MESSAGE = "";  
	  
    /** 
     * 复制单个文件 
     *  
     * @param srcFileName 
     *            待复制的文件名 
     * @param srcFileName ,destFileName
     *            目标文件名 
     * @param overlay 
     *            如果目标文件存在，是否覆盖 
     * @return 如果复制成功返回true，否则返回false 
     */  
    public static boolean copyFile(String srcFileName, String destFileName,  
            boolean overlay) {  
        File srcFile = new File(srcFileName);  
  
        // 判断源文件是否存在  
        if (!srcFile.exists()) {  
            MESSAGE = "源文件：" + srcFileName + "不存在！";  
            JOptionPane.showMessageDialog(null, MESSAGE);
            return false;  
        } else if (!srcFile.isFile()) {  
            MESSAGE = "复制文件失败，源文件：" + srcFileName + "不是一个文件！";  
            JOptionPane.showMessageDialog(null, MESSAGE);  
            return false;  
        }  
  
        // 判断目标文件是否存在  
        File destFile = new File(destFileName);  
        if (destFile.exists()) {  
            // 如果目标文件存在并允许覆盖  
            if (overlay) {  
                // 删除已经存在的目标文件，无论目标文件是目录还是单个文件  
                new File(destFileName).delete();  
            }  
        } else {  
            // 如果目标文件所在目录不存在，则创建目录  
            if (!destFile.getParentFile().exists()) {  
                // 目标文件所在目录不存在  
                if (!destFile.getParentFile().mkdirs()) {  
                    // 复制文件失败：创建目标文件所在目录失败  
                    return false;  
                }  
            }  
        }  
  
        // 复制文件  
        int byteread = 0; // 读取的字节数  
        InputStream in = null;  
        OutputStream out = null;  
  
        try {  
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];  
  
            while ((byteread = in.read(buffer)) != -1) {  
                out.write(buffer, 0, byteread);  
            }  
            return true;  
        } catch (FileNotFoundException e) {
            return false;  
        } catch (IOException e) {
            return false;  
        } finally {  
            try {  
                if (out != null)  
                    out.close();  
                if (in != null)  
                    in.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
	
	
	
	
	
	
	
	
	
	
	
 
  private FileOutputStream fs;

/** 
   * 新建目录 
   * @param folderPath Stringc:/fqf 
   * @return boolean 
   */ 
  public static void newFolder(String folderPath) { 
	  
	  try { 
		// "/" 
      StringTokenizer   st=new   StringTokenizer(folderPath,"/");   
      String   path1=st.nextToken()+"/";   
      String   path2 =path1;
      while(st.hasMoreTokens())   
      {   
            path1=st.nextToken()+"/";  
            path2+=path1; 
            File inbox   =   new File(path2);   
            if(!inbox.exists())   
                 inbox.mkdirs();
 //              System.err.println("inbox="+inbox);
       }
	 } catch (Exception e) { 
	      System.out.println("新建目录操作出错");
	      e.printStackTrace(); 
	    } 
	   
  }
  
  
  
  
  
  
  /** 
   * 新建目录 
   * @param folderPath Stringc:/fqf 
   * @return boolean 
   */ 
  public static void newFolderTwo(String folderPath) { 
	  
	  try { 
		// "/" 
      StringTokenizer   st=new   StringTokenizer(folderPath,"\\");   
      String   path1=st.nextToken()+"\\";   
      String   path2 =path1;
      while(st.hasMoreTokens())   
      {   
            path1=st.nextToken()+"\\";  
            path2+=path1; 
            File inbox   =   new File(path2);   
            if(!inbox.exists())   
                 inbox.mkdir();  
 //              System.err.println("inbox="+inbox);
       }
	 } catch (Exception e) { 
	      System.out.println("新建目录操作出错");
	      e.printStackTrace(); 
	    } 
	   
  }




    public static boolean createDir(String destDirName) {
        File dir = new File(destDirName);
        if (dir.exists()) {
            System.out.println("创建目录" + destDirName + "失败，目标目录已经存在");
            return false;
        }
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        //创建目录
        if (dir.mkdirs()) {
            System.out.println("创建目录" + destDirName + "成功！");
            return true;
        } else {
            System.out.println("创建目录" + destDirName + "失败！");
            return false;
        }
    }

  
  
  
  
  
  /** 
   * 新建文件 
   * @param filePathAndName String 文件路径及名如c:/fqf.txt 
   * @param fileContent String 文件内容 
   * @return boolean 
   */ 
  public void newFile(String filePathAndName, String fileContent) {
    try { 
      String filePath = filePathAndName; 
      filePath = filePath.toString(); 
      File myFilePath = new File(filePath); 
      if (!myFilePath.exists()) { 
        myFilePath.createNewFile(); 
      } 
      FileWriter resultFile = new FileWriter(myFilePath);
      PrintWriter myFile = new PrintWriter(resultFile);
      String strContent = fileContent; 
      myFile.println(strContent); 
      resultFile.close();
    } 
    catch (Exception e) { 
      System.out.println("新建目录操作出错");
      e.printStackTrace();
    }
  }
  /** 
   * 删除文件 
   * @param filePathAndName String 文件路径及如c:/fqf.txt 
   * @param filePathAndName filePathAndName
   * @return boolean 
   */ 
  public void delFile(String filePathAndName) { 
    try { 
      String filePath = filePathAndName; 
      filePath = filePath.toString(); 
      File myDelFile = new File(filePath);
      myDelFile.delete();
    } 
    catch (Exception e) { 
//      System.out.println("删除文件操作出错");
      e.printStackTrace();
    }
  }
  
       //文件存在则删除
	  public boolean deleteFile(String sPath) {   
		    boolean flag = false;   
		    File file = new File(sPath);   
		    // 路径为文件且不为空则进行删除   
		    if (file.isFile() && file.exists()) {   
		        file.delete();   
		        flag = true;   
//		        System.err.println("已删除异常页�?!");
		    }  
		    
		    return flag;   
		} 
	  
  
  
  
  
  /** 
   * 删除文件
   * @param folderPath String 文件夹路径及名称 如c:/fqf
   * @param folderPath String
   * @return boolean 
   */ 
  public static void delFolder(String folderPath) {
    try { 
      delAllFile(folderPath); //删除完里面所有内?
      String filePath = folderPath; 
      filePath = filePath.toString(); 
      File myFilePath = new File(filePath);
      myFilePath.delete(); //删除空文件夹
    } 
    catch (Exception e) { 
//    System.out.println("删除文件夹操作出?);
      e.printStackTrace();
    }
  }
  /** 
   * 删除文件夹里面的
   * @param path String 文c:/fqf 
   */ 
  public static void  delAllFile(String path) {
    File file = new File(path); 
    if (!file.exists()) { 
      return; 
    } 
    if (!file.isDirectory()) { 
      return; 
    } 
    String[] tempList = file.list(); 
    File temp = null; 
    for (int i = 0; i < tempList.length; i++) { 
      if (path.endsWith(File.separator)) {
        temp = new File(path + tempList[i]); 
      } 
      else { 
        temp = new File(path + File.separator + tempList[i]); 
      } 
      if (temp.isFile()) { 
        temp.delete(); 
      } 
      if (temp.isDirectory()) { 
        delAllFile(path+"/"+ tempList[i]);//先删除文件夹里面的文�?
        delFolder(path+"/"+ tempList[i]);//再删除空文件�?
      } 
    } 
  }
  /** 
   * 复制单个文件 
   * @param oldPath String 如：c:/fqf.txt 
   * @param newPath String 如：f:/fqf.txt 
   * @return boolean 
   */ 
  public void copyFile(String oldPath, String newPath) {
    try { 
//    int bytesum = 0; 
      int byteread = 0; 
      File oldfile = new File(oldPath); 
      if (oldfile.exists()) { //文件存在�?
        InputStream inStream = new FileInputStream(oldPath); //读入原文�?
        fs = new FileOutputStream(newPath); 
        byte[] buffer = new byte[1444]; 
 //       int length; 
        while ( (byteread = inStream.read(buffer)) != -1) { 
//         bytesum += byteread; //字节�?文件大小 
//         System.out.println(bytesum);
          fs.write(buffer, 0, byteread); 
        } 
        inStream.close(); 
      } 
    } 
    catch (Exception e) { 
      System.out.println("复制单个文件操作出错");
      e.printStackTrace();
    }
  }
  /** 
   * 
   * @param oldPath Stringc:/fqf 
   * @param newPath String f:/fqf/ff 
   * @return boolean 
   */ 
  public void copyFolder(String oldPath, String newPath) {
    try { 
      (new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件�?
      File a=new File(oldPath); 
      String[] file=a.list(); 
      File temp=null; 
      for (int i = 0; i < file.length; i++) { 
        if(oldPath.endsWith(File.separator)){ 
          temp=new File(oldPath+file[i]); 
        } 
        else{ 
          temp=new File(oldPath+File.separator+file[i]); 
        }
        if(temp.isFile()){ 
          FileInputStream input = new FileInputStream(temp); 
          FileOutputStream output = new FileOutputStream(newPath + "/" + 
              (temp.getName()).toString()); 
          byte[] b = new byte[1024 * 5]; 
          int len; 
          while ( (len = input.read(b)) != -1) { 
            output.write(b, 0, len); 
          } 
          output.flush(); 
          output.close(); 
          input.close(); 
        } 
        if(temp.isDirectory()){//如果是子文件�?
          copyFolder(oldPath+"/"+file[i],newPath+"/"+file[i]); 
        } 
      } 
    } 
    catch (Exception e) { 
      System.out.println("复制整个文件夹内容操作出错");
      e.printStackTrace();
    }
  }
  /** 
   * 
   * @param oldPath String 如：c:/fqf.txt 
   * @param newPath String 如：d:/fqf.txt 
   */ 
  public void moveFile(String oldPath, String newPath) {
    copyFile(oldPath, newPath); 
    delFile(oldPath);
  }
  /** 
   * 
   * @param oldPath String 如：c:/fqf.txt 
   * @param newPath String 如：d:/fqf.txt 
   */ 
  public void moveFolder(String oldPath, String newPath) { 
    copyFolder(oldPath, newPath); 
    delFolder(oldPath);
  } 
  
  
  /****
   * 
   * 
   */
  public void fileCopyFirst (String file,String str)   {   
      try {   
//      	 long startTime=System.currentTimeMillis();  
      	 

           FileOutputStream fos = new FileOutputStream (new File(file),true ) ;   
//           FileOutputStream fos = new FileOutputStream (new File(file),true ) ; 
//         String str = "ABC 中国 \n" ; //字符串末尾需要换行符
           fos.write(str.getBytes()) ;   
           fos.close ();   
//           long endTime=System.currentTimeMillis();  
//    	    System.out.println("耗费时间:"+(endTime-startTime)+" ms");
       } catch (IOException e) {   
           e.printStackTrace();   
       }   
        
  }
      
  
  /****
   * fileCopySecond 是第二种方法:
   */
  public void fileCopySecond (String file,String str)   {    
      try {   
    	  
    	  
    	  StringBuffer buf = new StringBuffer(); 
    	  buf.append(str);
    	  
    	  FileOutputStream fos = new FileOutputStream(new File(file),true); 
    	  PrintWriter pw = new PrintWriter(fos); 
          pw.write(buf.toString().toCharArray()); 
          pw.close();
          pw.flush(); 
      }catch(Exception e){
    	  e.printStackTrace();
      }finally {
          
      }
    	  
    	  

//           FileWriter fw = new FileWriter(file,true);   
//           PrintWriter pw=new PrintWriter(fw);   
//           pw.println(str);   //字符串末尾不�?��换行�?   \n
//
//           pw.close () ;   
//           fw.close () ; 
//
//          } catch (IOException e) {   
//           e.printStackTrace();   
//       }   
         

         
         
   }   
  
  
  
  
  
  public static void deleteAllFilesOfDir(File path) {  
      if (!path.exists())  
          return;  
      if (path.isFile()) {  
          path.delete();  
          return;  
      }  
      File[] files = path.listFiles();  
      for (int i = 0; i < files.length; i++) {  
          deleteAllFilesOfDir(files[i]);  
      }  
      path.delete();  
  }  
  
  
  
  
  
  
   //删除文件和目录
   public  void clearFiles(String workspaceRootPath){
        File file = new File(workspaceRootPath);
        if(file.exists()){
            deleteFile(file);
       }
  }
  private void deleteFile(File file){
       if(file.isDirectory()){
            File[] files = file.listFiles();
            for(int i=0; i<files.length; i++){
                 deleteFile(files[i]);
            }
       }
       file.delete();
  }
  
  
  
  
  
  //读取一个文件夹下所有文件及子文件夹下的所有文件   
	  public void ReadAllFileEndWithFile(String filePath,ArrayList<File> filelist) {
		  	 
	      File f = null;  
	      f = new File(filePath);  
	      File[] files = f.listFiles(); // 得到f文件夹下面的所有文件。   
	      for (File file : files) {  
	          if(file.isDirectory()) {  
	              //如何当前路劲是文件夹，则循环读取这个文件夹下的所有文件  
	        	  ReadAllFileEndWithFile(file.getAbsolutePath(),filelist);  
	          } else { 
	        	  if(file.getName().contains(".")){
	//        		  System.out.println("file.getAbsolutePath()="+file.getAbsolutePath()) ;
	        		  filelist.add(file);
	        	
	        	  }
	          }  
	      }  
	    	      
	  } 
      

    
  //读取一个文件夹下的所有文件夹和文件  
  public void ReadFile(String filePath) {  
      File f = null;  
      f = new File(filePath);  
      File[] files = f.listFiles(); // 得到f文件夹下面的所有文件。  
      List<File> list = new ArrayList<File>();
      for (File file : files) {  
          list.add(file);  
      }  
      for(File file : files) {  
          System.out.println(file.getAbsolutePath());
      }  
  }  
  
  
  /***
   * 以下方法是文件下载的方法:
   * @param path
   * @param response
   * @return
   */
  public HttpServletResponse download(String path, HttpServletResponse response) {
      try {
          // path是指欲下载的文件的路径。
          File file = new File(path);
          // 取得文件名。
          String filename = file.getName();
          // 取得文件的后缀名。
   //       String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

          // 以流的形式下载文件。
          InputStream fis = new BufferedInputStream(new FileInputStream(path));
          byte[] buffer = new byte[fis.available()];
          fis.read(buffer);
          fis.close();
          // 清空response
          response.reset();
          // 设置response的Header
          response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
          response.addHeader("Content-Length", "" + file.length());
          OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
          response.setContentType("application/octet-stream");
          toClient.write(buffer);
          toClient.flush();
          toClient.close();
      } catch (IOException ex) {
          ex.printStackTrace();
      }
      return response;
  }

  public void downloadLocal(HttpServletResponse response) throws FileNotFoundException {
      // 下载本地文件
      String fileName = "Operator.doc".toString(); // 文件的默认保存名
      // 读到流中
      InputStream inStream = new FileInputStream("c:/Operator.doc");// 文件的存放路径
      // 设置输出的格式
      response.reset();
      response.setContentType("bin");
      response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
      // 循环取出流中的数据
      byte[] b = new byte[100];
      int len;
      try {
          while ((len = inStream.read(b)) > 0)
              response.getOutputStream().write(b, 0, len);
          inStream.close();
      } catch (IOException e) {
          e.printStackTrace();
      }
  }

  public void downloadNet(HttpServletResponse response) throws MalformedURLException {
      // 下载网络文件
      int bytesum = 0;
      int byteread = 0;

      URL url = new URL("windine.blogdriver.com/logo.gif");

      try {
          URLConnection conn = url.openConnection();
          InputStream inStream = conn.getInputStream();
  //        FileOutputStream fs = new FileOutputStream("c:/abc.gif");

          byte[] buffer = new byte[1204];
  //        int length;
          while ((byteread = inStream.read(buffer)) != -1) {
              bytesum += byteread;
              System.out.println(bytesum);
              fs.write(buffer, 0, byteread);
          }
      } catch (FileNotFoundException e) {
          e.printStackTrace();
      } catch (IOException e) {
          e.printStackTrace();
      }
  }
  
  
  
  public void downLoad(String filePath, HttpServletResponse response, boolean isOnLine) throws Exception {
      File f = new File(filePath);
      if (!f.exists()) {
          response.sendError(404, "File not found!");
          return;
      }
      BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
      byte[] buf = new byte[1024];
      int len = 0;

      response.reset(); // 非常重要
      if (isOnLine) { // 在线打开方式
          URL u = new URL("file:///" + filePath);
          response.setContentType(u.openConnection().getContentType());
          response.setHeader("Content-Disposition", "inline; filename=" + f.getName());
          // 文件名应该编码成UTF-8
      } else { // 纯下载方式
          response.setContentType("application/x-msdownload");
          response.setHeader("Content-Disposition", "attachment; filename=" + f.getName());
      }
      OutputStream out = response.getOutputStream();
      while ((len = br.read(buf)) > 0)
          out.write(buf, 0, len);
      br.close();
      out.close();
  }


    // 根据路径获取文件名
    public static List<String> getAllFileName(String path) {
        File file = new File(path);
        File[] tempList = file.listFiles();

        List<String> list = new ArrayList<String>();

        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                System.out.println(tempList[i].getName());
                list.add(tempList[i].getName());
            }
            if (tempList[i].isDirectory()) {
                list.addAll(getAllFileName(tempList[i].getAbsolutePath()));
            }
        }
        return list;
    }





   //由文件得到文件的绝对路径：
    public static List <String> getAllFileNameAbsolutePath(String path) {
        File file = new File(path);
        File[] tempList = file.listFiles();

        List<String> list = new ArrayList<String>();

        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                System.out.println(tempList[i].getAbsolutePath());
                list.add(tempList[i].getAbsolutePath());
            }
            if (tempList[i].isDirectory()) {
                list.addAll(getAllFileNameAbsolutePath(tempList[i].getAbsolutePath()));
            }
        }
        return list;
    }


    
  public static void main(String[] args) {

      String path  = "C:/Users/dengdagui/Desktop/1111/333/ufin/ufin" ;
  //    List<String>  filenamelist= FileOperate.getAllFileNameAbsolutePath(path);
      List<String>  filenamelist= FileOperate.getAllFileName(path);
      for(String filename : filenamelist){
          System.out.println("filename--->"+filename);
      }


      ;

  }  
  
  
  
   
  
} 
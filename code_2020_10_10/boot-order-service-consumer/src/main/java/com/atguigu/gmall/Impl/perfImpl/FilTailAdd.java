package com.atguigu.gmall.Impl.perfImpl;

import com.atguigu.gmall.common.utils.FileOperate;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import org.springframework.stereotype.Component;

/**
 * @Author: dengdagui
 * @Description:对文件是否追加合并的操作：
 * @Date: Created in 2018-7-23
 */
@Component
public class FilTailAdd {


    public  void filTailAdd(String SourceFile, String targetFile) {
        try {
            BufferedReader bufReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(
                            SourceFile)),"utf-8"));

            StringBuffer strBuf = new StringBuffer();

            FileOperate fileadd = new FileOperate();

            for (String tmp = null; (tmp = bufReader.readLine()) != null;) {

                strBuf.append(tmp);
                strBuf.append(System.getProperty("line.separator"));

            }

            fileadd.fileCopyFirst(targetFile, strBuf.toString());

            PrintWriter printWriter = new PrintWriter(SourceFile);
            printWriter.write(strBuf.toString().toCharArray());
            bufReader.close();
            printWriter.flush();
            printWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  void addFile(String SourceFile, String targetFile)
            throws IOException {

        String line = null;
        BufferedReader br = new BufferedReader(new FileReader(new File(
                SourceFile)));
        BufferedWriter bw = new BufferedWriter(new FileWriter(new File(
                targetFile)));
        while ((line = br.readLine()) != null) {
            bw.write(line);
            bw.newLine();
        }
        bw.close();
        br.close();
        System.out.println("复制成功");

    }

    /***
     * 删除文件最后三行:
     */
    public void deleteFileEnd(String SourceFile,Integer number) {

        int hashTreeCount = 0;
        int jmeterTesCount = 0;

        try {
            BufferedReader bufReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(
                            SourceFile)),"utf-8"));

            StringBuffer strBuf = new StringBuffer();

            for (String tmp = null; (tmp = bufReader.readLine()) != null;) {

                if (tmp.trim().equals("</hashTree>") && hashTreeCount < number) { // 针对两次字符是:"</hashTree>"
                    // 不处理，其它的要处理：这样就起到删除这个字符的作用了．
                    hashTreeCount++;

                } else if (tmp.trim().equals("</jmeterTestPlan>") && jmeterTesCount < 1) { // 针对两次字符是"</jmeterTestPlan>"
                    jmeterTesCount ++ ;												// 不处理，其它的要处理,这样就起到删除这个字符的作用了．

                } else {

                    strBuf.append(tmp);
                    strBuf.append(System.getProperty("line.separator"));
                }
            }

            // 如果在文件末是：</jmeterTestPlan>　则删除

            PrintWriter printWriter = new PrintWriter(SourceFile);
            printWriter.write(strBuf.toString().toCharArray());
            bufReader.close();
            printWriter.flush();
            printWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}

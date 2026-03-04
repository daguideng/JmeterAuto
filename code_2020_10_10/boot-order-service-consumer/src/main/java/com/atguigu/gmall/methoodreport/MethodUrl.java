package com.atguigu.gmall.methoodreport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.map.HashedMap;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2019-7-1
 */
public class MethodUrl {


    public void readScript(String findStrs,String filedashboardPath) throws IOException {
        BufferedReader bufReader;
        File file = new File(filedashboardPath);
        FileInputStream inputStream = null;

        FileReader fileReader = new FileReader(file);
        LineNumberReader lineNumberReader = new LineNumberReader(fileReader);
        String s = lineNumberReader.readLine();
      //  lineNumberReader.skip(Long.MAX_VALUE);
     //   long lines = lineNumberReader.getLineNumber() + 1;

        Map<String,List<String>>  map = new HashedMap();

        try {
            inputStream = new FileInputStream(file);


            bufReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));

            try {
                for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {
                     if(tmp.equals(s) && tmp.equals(".path")){
                     //    map.put

                     }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


    public long getLineNumber(File file) {
        if (file.exists()) {
            try {
                FileReader fileReader = new FileReader(file);
                LineNumberReader lineNumberReader = new LineNumberReader(fileReader);
                lineNumberReader.skip(Long.MAX_VALUE);
                long lines = lineNumberReader.getLineNumber() + 1;
                fileReader.close();
                lineNumberReader.close();
                return lines;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }


    public static void main(String args[]){




    }

}

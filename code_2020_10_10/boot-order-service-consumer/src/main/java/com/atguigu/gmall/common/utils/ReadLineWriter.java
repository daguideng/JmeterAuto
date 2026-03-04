package com.atguigu.gmall.common.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2019-4-15
 */
public class ReadLineWriter {

    public static void readLineWriter(String source,String out){

        BufferedReader br = null ;
        BufferedWriter bw = null ;

        try{

            File file  = new File(source) ;
       //     File outfile  = new File(source) ;

       //     InputStream input = new FileInputStream(file) ;
       //     InputStream inputout = new FileInputStream(outfile) ;

          //   br = new BufferedReader(new InputStreamReader(input,"utf-8")) ;
             br = new BufferedReader(new FileReader(file)) ;
             bw = new BufferedWriter(new FileWriter(out));

            for(String tmp = null ;(tmp = br.readLine())!=null;){

                bw.write(tmp+"\n");
            }



        }catch(Exception e){


        }finally{

            try{
                if(br!=null && bw!=null){

                    br.close();
                    bw.close();
                }


            }catch(Exception e){


                e.printStackTrace();

            }


        }


    }

    public static void main(String args[]){

        ReadLineWriter  rlw = new ReadLineWriter();
        rlw.readLineWriter("E:\\征信安装环境\\回归测试常用html与pdf文件\\异常测试\\杨红伟#0#1000008#2.html","E:\\征信安装环境\\回归测试常用html与pdf文件\\异常测试\\aaa.html");

    }

}

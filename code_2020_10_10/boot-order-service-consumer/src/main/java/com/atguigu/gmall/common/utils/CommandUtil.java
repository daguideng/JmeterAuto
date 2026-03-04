package com.atguigu.gmall.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2018-8-17
 */
@Slf4j
public class CommandUtil {

   // private Logger logger = LoggerFactory.getLogger(this.getClass());

    public  static void commandExecution(String command ) throws  Exception {


           // 遍历待执行目录构建jmx静默运行命令,默认运行方式：


        if (OSUtils.isWindows()) {
            command = "cmd /c "  +command;

        }

           BufferedReader input = null ;
        try {
            Process process = Runtime.getRuntime().exec(command);
             input = new BufferedReader(new InputStreamReader(process.getInputStream(),"UTF-8"));
            String line = null;
            while ((line = input.readLine()) != null) {

            }
            System.gc();
            int exitValue = process.waitFor();
            if (exitValue == 0) {
                System.out.println("成功执行命令!!");
                process.getOutputStream().close();
            }
        } catch (IOException e) {

            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new Exception("执行命令失败!!!");
            //   e.printStackTrace();
        }finally {
            input.close();

        }


    }

}

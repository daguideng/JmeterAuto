package com.atguigu.gmall.Impl.perfImpl;

import com.atguigu.gmall.common.utils.OSUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2018-7-26
 */
@Slf4j
@Component
public class CommandJmeter {

   // @Autowired

   // @Value("${jmeter.load.run}")
   //  private String jmeter_load_run;



    public StringBuffer commandPerfJmeter(String jmeterBinDirPath ,String jmxAbsolutePath,String jtlAbsolutePath,String logAbsolutePath,String reportAbsolutePath,String ifRecordLogJtl,String ifRecordLogJmeter ) throws Exception {

        StringBuffer nodeIpBuffer = new StringBuffer();

        // 遍历待执行目录构建jmx静默运行命令,默认运行方式：
        String loadRunType = null ;
        String runType =  "Default";
        if(runType.equalsIgnoreCase("Default")){

            loadRunType = "";
        }else{
            loadRunType = " "+runType+" " ;
        }

        String command = null ;

        log.info("loadRunType====>"+loadRunType);

        //不管ifRecordLogJtl的value值,必然生成jtl日志,只对 log日志有判断:
        if(ifRecordLogJtl.equalsIgnoreCase("Yes") && ifRecordLogJmeter.equalsIgnoreCase("Yes") ) {
             command = jmeterBinDirPath + " -n -t " + jmxAbsolutePath + loadRunType + " -r -l " + jtlAbsolutePath
                    + " -j " +logAbsolutePath + " -e -o " +reportAbsolutePath ;
        }else if(ifRecordLogJtl.equalsIgnoreCase("Yes") && !ifRecordLogJmeter.equalsIgnoreCase("Yes")){
            command = jmeterBinDirPath + " -n -t " + jmxAbsolutePath + loadRunType + " -r -l " + jtlAbsolutePath
                    + " " + "" + " -e -o " + reportAbsolutePath  ;
        }else if(!ifRecordLogJtl.equalsIgnoreCase("Yes") && ifRecordLogJmeter.equalsIgnoreCase("Yes") ){
            command = jmeterBinDirPath + " -n -t " + jmxAbsolutePath + loadRunType + " -r -l " + jtlAbsolutePath
                    + " -j " + logAbsolutePath + " -e -o " + reportAbsolutePath ;
        }else if(!ifRecordLogJtl.equalsIgnoreCase("Yes") && !ifRecordLogJmeter.equalsIgnoreCase("Yes") ){
            System.out.println();
            command = jmeterBinDirPath + " -n -t " + jmxAbsolutePath + loadRunType + " -r -l " + jtlAbsolutePath
                    + " " + "" + " -e -o " + reportAbsolutePath  ;
        }


        System.out.println("command="+command);

            if (OSUtils.isWindows()) {
                command = "cmd /c "  +command;
                log.info("待执行的JMX命令：{}," + command);
                System.out.println("待执行的JMX命令：{},"+command);
            }


        log.info("command==============>"+command);

            try {
                Process process = Runtime.getRuntime().exec(command);
                BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream(),"UTF-8"));
                String line = null;
                while ((line = input.readLine()) != null) {
                    System.out.println(line);
                    log.info("执行结果：{}"+line);
                    //得到远程节点：
                    if(line.contains("remote engine:")){
                        String nodeIp = line.split("remote engine:")[1].trim();
                        nodeIpBuffer.append(nodeIp);
                        nodeIpBuffer.append(",");
                    }

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
            }


            return nodeIpBuffer ;

    }


}

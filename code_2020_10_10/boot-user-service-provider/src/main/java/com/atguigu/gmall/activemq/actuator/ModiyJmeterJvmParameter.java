package com.atguigu.gmall.activemq.actuator;

import com.atguigu.gmall.common.utils.FileReplayUtils;
import com.atguigu.gmall.common.utils.OSUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2019-6-20
 */
@Component
public class ModiyJmeterJvmParameter {

    @Value("${Heap.agent}")
    private  String Heap_agent;

    @Value("${New.agent}")
    private   String New_agent;

    @Value("${JVM_ARGS.agent_linux}")
    private  String JVM_ARGS_agent_linux ;



    //3.5 设置jmeter运行内存大小：
    public  void modiyJmeterJvm(String jmeterControlNode) {
        //jmeter.bat  jmeter.sh
        String jmeterEnvPath = null;
        if (OSUtils.isWindows()) {
            jmeterEnvPath = jmeterControlNode.replace("jmeter.properties", "jmeter.bat");

            //修改内存：
            String HEAP = Heap_agent;
            FileReplayUtils.modiyJmeterPropertiesJvm(jmeterEnvPath, "HEAP", "set HEAP=" + HEAP);

            String NEW = New_agent;
            FileReplayUtils.modiyJmeterPropertiesJvm(jmeterEnvPath, "NEW", "set NEW=" + NEW);

        } else {
            jmeterEnvPath = jmeterControlNode.replace("jmeter.properties", "jmeter.sh");

            //修改内存：
            String JVM_ARGS_inter_linux = JVM_ARGS_agent_linux;
            FileReplayUtils.modiyJmeterPropertiesJvm(jmeterEnvPath, "JVM_ARGS", "JVM_ARGS=" + JVM_ARGS_inter_linux);

        }


    }
}

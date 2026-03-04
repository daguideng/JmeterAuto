package com.atguigu.gmall.zookeeperip;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2019-6-5
 */


import com.atguigu.gmall.dao.Jmeter_agentip_statesMapper;
import com.atguigu.gmall.entity.Jmeter_agentip_states;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
/**
 * 利用HTTP请求来获取zk服务器地址的功能
 * <p>同机房地址优先返回<p/>
 * <p>这个解析过程其实是可以分离的 TODO </p>
 * Created by peirong.wpr on 2017/4/11.
 */

@Slf4j
@Component
public class DynamicHTTPHostProvider {



    @Resource(name = "consumer_jmeter_agentip_statesMapper")
    private Jmeter_agentip_statesMapper consumer_jmeter_agentip_statesMapper;

    @Value("${dubbo.registry.address}")
    private String CONNECT_ADDR;

    //  static final String CONNECT_ADDR = "10.255.33.141:2181";
    static final int SESSION_TIMEOUT = 5000;

    static final String DUBBO_PROVIDERS = "/dubbo/com.atguigu.gmall.service.UserService/providers";

    static String dubbo_registry_address = null;
    static String targer = "Start";

    public Set <String> providerSet(Set <String> providerset) {
        if (targer.equals("Start")) {
            dubbo_registry_address = CONNECT_ADDR.split("//")[1];
            targer = "EndOver";
        }


        log.info("dubbo_registry_address--->" + dubbo_registry_address);

        // TODO Auto-generated method stub
        ZkClient zkClient = new ZkClient(dubbo_registry_address, SESSION_TIMEOUT);
        log.info("ZK 成功建立连接！");

        String path = DUBBO_PROVIDERS;
        // 注册子节点变更监听（此时path节点并不存在，但可以进行监听注册）
        zkClient.subscribeChildChanges(path, new IZkChildListener() {
            public void handleChildChange(String parentPath, List <String> currentChilds) throws Exception {
                log.info("路径" + parentPath + "下面的子节点变更。子节点为：" + currentChilds);
            }
        });


        zkClient.subscribeDataChanges(path, new IZkDataListener() {

            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
                // TODO Auto-generated method stub
                log.info("路径" + dataPath + "已经被删除！");
            }

            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {
                // TODO Auto-generated method stub
                log.info("路径" + dataPath + "的数据变成" + data);
            }
        });


        if (zkClient.exists("/dubbo") == true) {

            String str = null;

            List <String> list = zkClient.getChildren(path);
            Iterator <String> it = list.iterator();
            while (it.hasNext()) {
                try {
                    str = it.next();

                    str = URLDecoder.decode(str, "UTF-8").split("://")[1].split("/")[0];
                    //加1000是为了保持与agent 的server_port 一致:
                    str = str.split(":")[0] + ":" + (Integer.valueOf(str.split(":")[1]) + 1000);
                    providerset.add(str);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


            }


        }

        return providerset;


    }


    public StringBuffer getRandomIP(String type) {

        StringBuffer lpSbPerf = new StringBuffer(300);
        StringBuffer lpSbfList = new StringBuffer(100);

        Set <String> hashset = new HashSet <>();

        this.providerSet(hashset);

        //为了安全线程：
        //  Set <String> hashsetIp = Collections.synchronizedSet(hashset);

         List<String> listin = new ArrayList<>();
        listin.add("Created");
        listin.add("Finished");

        List <Jmeter_agentip_states> agentipList = consumer_jmeter_agentip_statesMapper.selectByStatusToIp(listin);


        List <String> runIplist = new ArrayList <>(10000);
        //从HashSet中删除数据库中states中是：Run 状态的ip
        if (null != agentipList) {
            if ("Inter".equals(type)) {
                for (Jmeter_agentip_states ipPort : agentipList) {
                    //清除类型不是:perf的代理机器
                    if ("Inter".equalsIgnoreCase(ipPort.getType())) {
                        for (String zikIp : hashset) {
                            if (zikIp.equals(ipPort.getIpaddress())) {
                                runIplist.add(zikIp);
                            }
                        }
                    }
                }
            } else if("Perf".equals(type)) {
                for (Jmeter_agentip_states ipPort : agentipList) {
                    //类型只选:perf的代理机器
                    if (("perf").equalsIgnoreCase(ipPort.getType()))
                        for (String zikIp : hashset) {
                            if (zikIp.equals(ipPort.getIpaddress())) {
                                runIplist.add(zikIp);
                            }
                        }
                }

            }
        }


        if (runIplist.size() == 0) {
            log.error("表中：jmeter_agentip_states中agent机器都无法运行测试.....runIplist.size():{}," + runIplist.size());
            log.info("表中：jmeter_agentip_states中agent机器都无法运行测试.....");
            log.info("表中：jmeter_agentip_states中agent机器都无法运行测试.....");

            return null;
        }

        String[] arr = new String[runIplist.size()];
        //List-->数组
        String providerInfo[] = runIplist.toArray(arr);

        //强制清空
        runIplist.clear();
        //  hashsetIp.clear();
        hashset.clear();
        agentipList.clear();

        //接口测试时返回ip:port
        if ("Inter".equals(type)) {

            int index = (int) (Math.random() * providerInfo.length);
            String random = providerInfo[index];
            //   System.out.println("random--->" + random);
            log.info("随机取得代理端运行的IP为:{} :" + random);
            lpSbfList.append(random);
            log.info("lpSbfInterIP---->" + lpSbfList.toString());
            return lpSbfList;
        } else if("Perf".equals(type)) {  //性能测试时返回ip

            for (String ip : providerInfo) {
                lpSbPerf.append(ip);
                lpSbPerf.append(",");
            }

            //去掉一个","
            lpSbPerf.deleteCharAt(lpSbPerf.length() - 1);

            log.info("lpSbPerfIP---->" + lpSbPerf.toString());

            return lpSbPerf;

        }


        return null;
    }


}

package com.atguigu.gmall.Impl.perfImpl;

import com.atguigu.gmall.common.utils.GetJmeterDataUtil;
import com.atguigu.gmall.service.jmeterperf.PerfConfigServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.*;


/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2018-7-20
 */
@Slf4j
@Component
public class JmeterNode {


    @Autowired
    private PerfConfigServer perfConfigServer ;

    @Value("${jmeterProperties.Perf}")
    private String jmeterProperties_Perf;


    public static  ArrayList <String> storelist = new ArrayList <String>();
    public static  Map <Object, Object> ThreadJmeterNode = new HashMap <Object, Object>();


    //得到 jmeter每个节点
    public static List <String> getRunJmeterNode(String node) {


        @SuppressWarnings("rawtypes")
        Map <String, List> map = new HashMap <String, List>();


        String[] nodearray = node.split(",");
        List <String> list  = Arrays.asList(nodearray);


        map.put("mylist", list);


        List <String> results = (List <String>) map.get("mylist");

        List <String> alist = compare(storelist, results);
        System.out.println("storelist.size()=" + storelist.size());
        System.out.println("xaxaxa=" + alist);


        /**
        try {
            if (storelist.size() == results.size()) {
                storelist.clear();
                System.gc();
            }

        } catch (Exception e) {

            System.out.println("jmeter node 自我保护，清空中。。。。。。");
        }

         **/

        //为空则取第一个:jmeternode
        if (storelist.size() == 0) {

            storelist.add((String) alist.get(0));

            ThreadJmeterNode.put(Thread.currentThread(), alist.get(0));
            log.info("得到运行的JmeterNodefirst=" + alist.get(0) + " jmeter_node=" + alist.get(0));

        } else {
            //否则取最后一个:jmeternode
            storelist.add((String) alist.get(alist.size() - 1));

            ThreadJmeterNode.put(Thread.currentThread(), alist.get(alist.size() - 1));

            log.info("得到运行的JmeterNodefirst=" + alist.get(0) + " jmeter_node=" + alist.get(alist.size() - 1));
        }


        return storelist;
    }


    public static <T> List <T> compare(List <T> t1, List <T> t2) {

        List <T> list1 = t1;
        List <T> list2 = new ArrayList <T>();
        for (T t : t2) {
            if (!list1.contains(t)) {
                list2.add(t);
            }
        }
        return list2;
    }


    // 删除存储中jmeterNodeList(storelist)中的jmeterNodeValue:
    public static void revoveStoreJmeterNode(Object object) {


        Iterator <String> sListIterator = storelist.iterator();
        while (sListIterator.hasNext()) {
            String e = sListIterator.next();
            System.out.println("object====" + object);
            if (e.equals(object)) {
                sListIterator.remove();
            }
        }

        System.out.println("storelist=" + storelist.size());
        log.info("storelist=" + storelist.size());

        for (int x = 0; x < storelist.size(); x++) {
            System.out.println("jmeter_node=" + storelist.get(x));
            log.info("jmeter_node=" + storelist.get(x));
        }


    }


    /**
     * 检查节点服务器是否都用完了,1  已经满足，0还可以运行分配节点服务器：
     */
    public  Integer  runNodeServerRunable(HttpSession session) throws Exception{

        String jmeterProperties = jmeterProperties_Perf.replace("\\", "/");

        String jmeterNodes [] = jmeterProperties.split(",");



        String threadName = session.getAttribute(JmeterSession.SESSION_PERF).toString();
        Map<String,Object> mapkey = perfConfigServer.getByValueServer(threadName);

        //得到运行时间：
        List <String> status = GetJmeterDataUtil.getJmeterPerfListData(mapkey.get("status").toString());

        String endStatus = status.get(0).trim();

        //    if (storelist.size() == jmeterNodes.length) {  //接口测试可以这样判断
        // jmeterNodes.length
         if (storelist.size() > 0 ) {   //性能测试这样判断
                return 1;
            }else{
                return 0 ;
            }


    }


}
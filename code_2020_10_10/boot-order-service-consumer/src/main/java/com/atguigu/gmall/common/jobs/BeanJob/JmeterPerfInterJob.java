package com.atguigu.gmall.common.jobs.BeanJob;

import com.atguigu.gmall.dao.Timer_type_configMapper;
import com.atguigu.gmall.entity.Timer_type_config;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2019-6-26
 */

@Component
public class JmeterPerfInterJob {


    @Autowired
    private Timer_type_configMapper timer_type_configMapper;

    private static final Logger logger = Logger.getLogger(QuartzController.class);

    public Map <Integer, String> scheduleJob2(TreeMap <Integer, String> treemap) {

        List<String> togetherlist = new ArrayList<String> ();
        togetherlist.add("Perf");
        togetherlist.add("Inter");


        List <Timer_type_config> perflist = timer_type_configMapper.selectByStatusTpye(togetherlist);

        Map <Integer, Timer_type_config> map = new TreeMap <>();


        //2.timestatus是YES且deletestate为1时才执行perf的job

        try {
            for (Timer_type_config recond : perflist) {
                if (recond.getTimestatus().equalsIgnoreCase("Yes") && Integer.valueOf(recond.getDeletestate())==1) {

                 //   logger.info("recond.getId()--->" + recond.getId());
                    map.put(recond.getId(), recond);


                }
            }

            for (Map.Entry <Integer, Timer_type_config> entry : map.entrySet()) {
                int key = entry.getKey();
            //    System.out.println("key--->" + key);
            //    logger.info("key--->" + key);
                Timer_type_config timer_type_configs = entry.getValue();

                String timetask = timer_type_configs.getTimetask();


                treemap.put(key, timetask);


            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        perflist.clear();

        return treemap;


    }
}

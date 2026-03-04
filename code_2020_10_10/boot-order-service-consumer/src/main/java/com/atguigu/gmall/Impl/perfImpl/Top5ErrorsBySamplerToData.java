package com.atguigu.gmall.Impl.perfImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2019-7-12
 */
@Component
public class Top5ErrorsBySamplerToData {

    @Autowired
    private ReadWriteReportData readWriteReportData ;

    public void ErrorsBySamplerToData(String jsFile){

        String json = null ;

        String jsFileReport = jsFile + "/content/js/dashboard.js";

    //    json = readWriteReportData.modiyInterval(jsFileReport.toString(), "statisticsTable");
     //   System.out.println("json===>"+json);

    }

}

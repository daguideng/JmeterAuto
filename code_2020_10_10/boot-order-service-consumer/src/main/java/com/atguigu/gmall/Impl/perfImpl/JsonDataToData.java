package com.atguigu.gmall.Impl.perfImpl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.gmall.common.utils.CommandUtil;
import com.atguigu.gmall.common.utils.GetFileFirstLastLine;
import com.atguigu.gmall.common.utils.TimeStampUtils;
import com.atguigu.gmall.entity.Jmeter_perfor_history_report;
import com.atguigu.gmall.entity.Statistics;
import com.atguigu.gmall.service.jmeterperf.JmeterperformhistoryreportServer;
import com.atguigu.gmall.service.jmeterperf.StatisticsServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author: dengdagui
 * @Description:fastJson 解析json串
 * @Date: Created in 2018-8-3
 */
@Component
public class JsonDataToData {


    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private StatisticsServer statisticsServer ;

    @Autowired
    private ReadWriteReportData readWriteReportData ;

    @Autowired
    private JmeterperformhistoryreportServer jmeterperformhistoryreportServer ;

   // @Autowired


    @Value("${ReporLinkst.Url}")
    private String ReporLinkstUrl ;


    //读性能数据并把数据入库：
    public void reportIndex(String jmeterPathBin,String jsFile,String thread,StringBuffer nodeIp,String jtlReportfile,String jmeterScriptName) throws Exception {

        String jsFileReport = jsFile + "/content/js/dashboard.js";

        jsFileReport = jsFileReport.replace("\\","/");

        System.out.println("jsFileReport====>"+jsFileReport) ;

        logger.info("jsFileReport====>"+jsFileReport);

        //精确得到性能测试运行时间,为以后性能测试报告做性能对比提供参考时间：

        StringBuffer runStartEndTime = this.runTime(jtlReportfile);

        String json = null ;

            try {
              File jsfile = new File(jsFileReport);

                //如果  -e -o 没有自动生成报告则用命令强制生成报告!
              if(!jsfile.exists()){
                  //命令自动生成报告:
                  String command = jmeterPathBin + " -g " + jtlReportfile + " -o " + jsFile ;
                  System.out.println("自动生成报告命令："+command);
                  CommandUtil.commandExecution(command ) ;

              }


                json = readWriteReportData.modiyInterval(jsFileReport.toString(), "statisticsTable");
                System.out.println("json===>"+json);
        }catch (Exception e){
            throw new Exception("未生成HTML报告,搜索报告不成功!!!");
        }

        Map<Object, Object> hashMap = new HashMap() ;

      //  hashMap = json2Map(json);

        String overall = hashMap.get("overall").toString();

        JSONObject dataTotal = JSONObject.parseObject(overall);
        
        String total = dataTotal.getString("data").split("\\[")[1].split("]")[0];
        String[] totalArray = total.split(",");



        //添加性能测试总的指标：
        indexReport(totalArray,thread,nodeIp,runStartEndTime,jtlReportfile,jsFile,jmeterScriptName);
        JSONArray jArray = new JSONArray((List <Object>) hashMap.get("items"));



        //添加性能测试各事务指标:
        for (int x = 0; x < jArray.toArray().length; x++) {

            String reportIndex = jArray.toArray()[x].toString();
            JSONObject reportIndex_json = JSONObject.parseObject(reportIndex);
            String data_index = reportIndex_json.getString("data").split("\\[")[1].split("]")[0];


            // 各事务值报告指标：
            String[] itermsAraay = data_index.split(",");
            indexReport(itermsAraay,thread,nodeIp,runStartEndTime,jtlReportfile,jsFile,jmeterScriptName);

        }



        //清空内存：
        nodeIp.setLength(0);
        runStartEndTime.setLength(0);
        System.gc();

    }





    /**
     * 如果只是用于程序中的格式化数值然后输出，那么这个方法还是挺方便的。
     * 应该是这样使用：System.out.println(String.format("%.3f", d));
     *
     * @param d
     * @return
     */
    public static String formatDouble(double d) {
        String str = String.format("%.2f", d);
        return str;
    }

    public static String getType(Object object) {
        String typeName = object.getClass().getName();
        int length = typeName.lastIndexOf(".");
        String type = typeName.substring(length + 1);
        return type;
    }


    private StringBuffer runTime(String jtlReportfile){

        StringBuffer   timeBuffer = new StringBuffer(20);
        String filstLine = GetFileFirstLastLine.getFileSecondLine(jtlReportfile).split(",")[0];
        String lastLine =  GetFileFirstLastLine.getFileLastLine(jtlReportfile).split(",")[0];

        //时间转换：
        String startTime = TimeStampUtils.stampToDate(filstLine);
        String endTime = TimeStampUtils.stampToDate(lastLine);

        timeBuffer.append(startTime) ;
        timeBuffer.append(",") ;
        timeBuffer.append(endTime);



        return  timeBuffer ;
    }



    public  void indexReport(String [] Array ,String thread,StringBuffer nodeIp,StringBuffer runStartEndTime,String jtlReportfile,String jsFile,String jmeterScriptName) throws Exception {

        DecimalFormat df = new DecimalFormat("#.00");

        List <Object> indexlist = new ArrayList <>();
        Map<String, Object> dataMap = new HashMap<>();

        //脚本名 网络下载:
//      jmeterScriptName = "<a href=/localhost:8082/templates/?address="+jtlReportfile+" style=color:blue;text-decoration:none>"+jmeterScriptName+"</a>" ;
        //脚本名 文件下载：
//        jmeterScriptName = "<a href="+jtlReportfile+" style=color:blue;text-decoration:none>"+jmeterScriptName+"</a>" ;

      //  jmeterScriptName = "<a href="+jtlReportfile+" style=color:blue;text-decoration:none>"+jmeterScriptName+"</a>" ;

        StringBuffer  sb = new StringBuffer(70) ;
        ReporLinkstUrl = ReporLinkstUrl.replace("\\", "/");

        String simpleReportUrl = jtlReportfile.split("ReportResultDir/")[1];
        sb.append(ReporLinkstUrl);
        sb.append(simpleReportUrl) ;
        jmeterScriptName = "<a href="+sb.toString()+" style=color:blue;text-decoration:none>"+jmeterScriptName+"</a>" ;
        dataMap.put("scriptName", jmeterScriptName);  //得到脚本名的jtl.log的超链接
        sb.setLength(0);

        //线程数：
        dataMap.put("threadCount", thread);

        for (int i = 0; i < Array.length; i++) {

            //添加事务名:
            if (i == 0) {   //添加了事务超链接
                StringBuffer  sb1 = new StringBuffer(70) ;
                String transactionName = Array[0].replace("\"", "") ;
                String indexPath = jsFile+"/index.html" ;
                indexPath = indexPath.split("ReportResultDir/")[1] ;
                sb1.append(ReporLinkstUrl);
                sb1.append(indexPath) ;
                jmeterScriptName = "<a href="+sb1.toString()+" style=color:blue;text-decoration:none>"+transactionName+"</a>" ;
                dataMap.put("transactionName", jmeterScriptName);
                sb1.setLength(0);
            }
            if (i != 0) {
                //对double值进行四舍五入，保留两位小数
                double value = Double.valueOf(df.format(Double.valueOf(Array[i])));
                String sumeValue = formatDouble(value);
                //对于 Error% 所得到值加上%
                if (i == 3) {
                    sumeValue = sumeValue + "%";
                }

                // 根据索引为性能指标添加适当的键
                String key;
                switch(i) {
                    case 1: key = "sampleCount";
                        break;
                    case 2: key = "averageResponseTime";
                        break;
                    case 3: key = "errorRate";
                        break;
                    case 4: key = "minResponseTime";
                        break;
                    case 5: key = "maxResponseTime";
                        break;
                    case 6: key = "medianResponseTime";
                        break;
                    case 7: key = "90thPercentile";
                        break;
                    case 8: key = "95thPercentile";
                        break;
                    case 9: key = "99thPercentile";
                        break;
                    default: key = "metric" + i;
                }
                dataMap.put(key, sumeValue);
            }

            //加上运行时的节点ip及端口号: 不同的性能结果与节点性能有很大的关系!
            if(i == Array.length-1){
                String ipPort = nodeIp.toString();
                String runTime = runStartEndTime.toString();
                //添加 ip:
                dataMap.put("ipAddress", ipPort.substring(0,ipPort.length()-1)) ;
                //添加 开始运行时间:
                dataMap.put("startTime", runTime.split(",")[0]) ;
                //添加 结束运行时间:
                dataMap.put("endTime", runTime.split(",")[1]) ;
            }
        }

        //添加文件路径信息
        dataMap.put("jtlPath", jtlReportfile) ;
        dataMap.put("indexPath", jsFile+"/index.html") ;

        //添加状态:
        dataMap.put("status", statisticsStates.INSERT_0) ;

        //将map添加到列表
        indexlist.add(dataMap);


        //入库:
        Jmeter_perfor_history_report historylist = new Jmeter_perfor_history_report(indexlist);
        jmeterperformhistoryreportServer.insertHistoryReport(historylist);
        logger.info("线程："+thread+ "运行结果保存数据成功!!");


        //释放内存：
        indexlist.clear();




    }



}

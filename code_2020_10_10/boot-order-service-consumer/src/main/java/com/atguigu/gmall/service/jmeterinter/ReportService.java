package com.atguigu.gmall.service.jmeterinter;

/**
 * 接口测试报告服务接口
 */
public interface ReportService {
    
    /**
     * 生成接口测试报告
     * @param id 测试ID
     * @param lastruntime 最后运行时间戳
     * @return 报告内容
     */
   

     String generateInterReport(String[] ids, String lastruntime);

    String generatePerfReport(String[] ids, String lastruntime);

}
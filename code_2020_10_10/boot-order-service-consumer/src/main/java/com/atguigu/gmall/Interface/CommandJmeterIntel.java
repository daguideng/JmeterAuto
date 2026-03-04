package com.atguigu.gmall.Interface;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2018-9-27
 */
public interface CommandJmeterIntel {

    public StringBuffer commandJmeter(String jmeterControlNode, String jmeterBinDirPath, String jmxAbsolutePath, String jtlAbsolutePath, String logAbsolutePath, String reportAbsolutePath, String ifRecordLogJtl, String ifRecordLogJmeter) throws Exception ;

  
}

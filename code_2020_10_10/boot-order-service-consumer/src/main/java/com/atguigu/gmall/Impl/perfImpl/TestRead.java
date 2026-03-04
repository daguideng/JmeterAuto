package com.atguigu.gmall.Impl.perfImpl;


import com.atguigu.gmall.common.utils.ZipUtilsPbc;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2018-8-20
 */
public class TestRead {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    public String unzipScript(String scriptJmeterZip) throws Exception {

        File file = new File(scriptJmeterZip);

        String scriptName = file.getName().substring(0, file.getName().length() - 4);
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss"); // yyyyMMddHHmmss
        String todayTime = df.format(new Date());

        String destPath = file.getParent() + "/" + todayTime + "/" + scriptName;

        ZipUtilsPbc.unZipFiles(file, destPath);

        // destPath存在则删除：
        // FileOperate.delAllFile(scriptJmeterZip+"/");

        return destPath;
    }



    public  static void main(String args[]) throws Exception{

        TestRead  test = new TestRead();


        String scriptJmeterZip = "D:/test/test/OasisOnlyApprove-PAYROLL.zip" ;
     //   String scriptJmeterZip = "D:/test/test/dxUcreditMainProcess.zip" ;
        test.unzipScript(scriptJmeterZip);



    }
}

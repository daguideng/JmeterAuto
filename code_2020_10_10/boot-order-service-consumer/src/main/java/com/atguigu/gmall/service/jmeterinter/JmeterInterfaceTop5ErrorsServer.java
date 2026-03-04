package com.atguigu.gmall.service.jmeterinter;

import com.atguigu.gmall.common.utils.DateUtil;
import com.atguigu.gmall.dao.Jmeter_interface_top5_errorsMapper;
import com.atguigu.gmall.entity.Jmeter_interface_top5_errors;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2019-7-22
 */
@Slf4j
@Service
public class JmeterInterfaceTop5ErrorsServer {




    @Autowired
    private Jmeter_interface_top5_errorsMapper jmeter_interface_top5_errorsMapper ;




    public void insertInterfaceTop5Error(Jmeter_interface_top5_errors record) throws Exception{

        int count =  jmeter_interface_top5_errorsMapper.insert(record);

        if(count > 0) {
            log.info("接口异常数据入库成功！");

        }else{
            log.info("接口异常数据入库失败！");

        }

    }


    /***
     * 为异常查询而写的方法：
     * @param scriptname,runtime
     * @throws Exception
     */
    public List<Jmeter_interface_top5_errors> selectJmeter_inter_top5_error(String lastruntime,String scriptname,String transactionName) throws Exception{

        lastruntime = DateUtil.timeStamp2Date(lastruntime,"yyyy-MM-dd HH:mm:ss");

        System.out.println("lastruntime---->"+lastruntime);
        System.out.println("scriptname---->"+scriptname);
        System.out.println("transactionName---->"+transactionName);

        List <Jmeter_interface_top5_errors> interTop5Error = null ;
         if(!transactionName.equals("Total")) {
             interTop5Error = jmeter_interface_top5_errorsMapper.selectErrorReport(lastruntime, scriptname, transactionName);
         }else{
             interTop5Error = jmeter_interface_top5_errorsMapper.selectAllErrorReport(lastruntime, scriptname, transactionName);
         }

        if(null !=interTop5Error) {
            log.info("查询异常数据成功!");

        }else{
            log.info("查询异常数据失败!");

        }


        return interTop5Error ;

    }




    /***
     * 为点击异常的Total而下载jplpath的方法：
     * @param scriptname,runtime
     * @throws Exception
     */
    public String interLogJplPath(String lastruntime,String scriptname,String transactionName) throws Exception{

        lastruntime = DateUtil.timeStamp2Date(lastruntime,"yyyy-MM-dd HH:mm:ss");

        System.out.println("lastruntime---->"+lastruntime);
        System.out.println("scriptname---->"+scriptname);
        System.out.println("transactionName---->"+transactionName);

        String jplLogPath = null ;

        List <Jmeter_interface_top5_errors>   interlogPathList = jmeter_interface_top5_errorsMapper.selectJplPath(lastruntime, scriptname, transactionName);


        jplLogPath = interlogPathList.get(0).getJplpath();


        if(null !=jplLogPath) {
            log.info("查询异常数据成功!");

        }else{
            log.info("查询异常数据失败!");

        }


        return jplLogPath ;

    }


}

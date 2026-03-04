package com.atguigu.gmall.Impl.interfaceImpl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.gmall.Interface.JmeterScriptUploadIntel;
import com.atguigu.gmall.activemq.PublishController;
import com.atguigu.gmall.activemq.ResultDataMessageProducer;
import com.atguigu.gmall.activemq.ThreadByRunnableSendMq;
import com.atguigu.gmall.common.bean.ResponseMeta;
import com.atguigu.gmall.common.bean.ResponseResult;
import com.atguigu.gmall.common.utils.*;
import com.atguigu.gmall.common.utils.file.FileUtil;
import com.atguigu.gmall.entity.Upload_info;
import com.atguigu.gmall.service.jmeterperf.UploadScriptServer;
import com.atguigu.gmall.zookeeperip.DyIPaddressPubicProvider;

import lombok.val;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: dengdagui
 * @Description:脚本上传操作:
 * @Date: Created in 2018-7-5
 */
@Slf4j
@RestController
@Component("jmeterScriptInterlUploadImpl")
public class JmeterScriptInterlUploadImpl extends MultiActionController implements JmeterScriptUploadIntel {



  //  private HttpServletRequest request;


    @Autowired
    private UploadScriptServer uploadScriptServer;





     @Autowired
     private ResultDataMessageProducer threadDataMessageProducer;


    @Autowired
    private PublishController publishController;


    @Autowired
    private DyIPaddressPubicProvider dyIPaddressPubicProvider ;


    @Value("${Path.Parameters}")
    private String PathParameters;

    @Value("${jmeterStore.Inter}")
    private String jmeterStore_Inter ;

    @Value("${jmeterScriptDir.Inter}")
    private String jmeterScriptDir_Inter ;

    @Value("${scriptDownLoadFromUrl.Inter}")
    private String scriptDownLoadFromUrl_Inter ;


    public Map <String, Object> uploadScript(MultipartFile file, HttpServletResponse response,HttpSession session)
            throws IOException {

        log.info("configxml : {}, 性能参数 : {}", file);

        Map <String, Object> modelMap = new HashMap <String, Object>();

        /**
         * 判断上传脚本的后缀为.zip的脚本：
         *
         */


        //得到当前的数据库的id号：
        String scriptrunorderId = "100";

        log.info("接口测试脚本上传开始!");

        String jmeterStore = jmeterStore_Inter.replace("\\", "/");

        log.info("jmeterStore=" + jmeterStore);

//        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");


        String resutDir = jmeterScriptDir_Inter.replace("\\", "/");


        //得到文件后缀,如果不是.zip的压缩文件给前台提示：

        if(!getfileSuffix(file.getOriginalFilename()).equals("zip")){

            modelMap.put("result", new ResponseResult(ResponseMeta.PERF_UPLOAD_File_FORMAT_ERROR, "请上传以.zip的压缩后的脚本"));

            return modelMap;
        }

        String uploadName = null ;
        try {
            uploadName = session.getAttribute("username").toString();
        }catch (Exception e){
            uploadName = null ;
        }


        try {
            //  uploadName = SessionUser.getUserCode();

            if("".equals(uploadName) || null == uploadName ){
                uploadName = "admin" ;
            }

        }catch (Exception e){

            uploadName = "admin";
        }



        //测试：scriptDownLoadFromUrl_Inter
       //  uploadName = "deng" ;

        String  scriptDownLoadFromUrl = scriptDownLoadFromUrl_Inter+uploadName+"/"+file.getOriginalFilename();


        // 创建的文件就是登录用户名:

        Map <String, ArrayList <String>> map = null;
        List <String> testnameList = new ArrayList <String>();

        List <String> domainUrlList = new ArrayList <String>();

        FileOperate fileFolder = new FileOperate();
        String rootPath = null;
        String savefile = null;

        String savetmp = null ;

        // 判断文件是否为空
        if (!file.isEmpty()) {
            try {
                // 上传文件保存路径
                String filePath = jmeterStore + uploadName;


                filePath = filePath.replace("\\", "/");

                FileOperate.createDir(filePath);
                FileUtil.createDir(filePath);
                FileOperate.newFolder(filePath);

                String copyTargerDir = filePath;

                savefile = filePath + "/" + file.getOriginalFilename();


              //  Thread.sleep(1000);
                // 转存文件

                file.transferTo(new File(savefile));


                log.info("上传jmeter脚本成功!!!");

                // 数据入库操作:
                Date nowTime = new Date();
                System.out.println(nowTime);
                SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


                String operationtypestr = "[下载]";

                String runbutton = "[run]";


                // 接口名去后缀：
                String interFileName = this.getFileNameNoEx(file.getOriginalFilename());

                savefile = savefile.replace("\\", "/");
                   
                String urlstr = scriptDownLoadFromUrl_Inter+uploadName+"/" ;




                rootPath = jmeterStore;





                log.info("savefile===>" + savefile);
                log.info("rootPath===>" + rootPath);


                FileOperate.deleteAllFilesOfDir(new File(rootPath + interFileName));


                log.info("savefile=" + savefile);
                ZipUtils.decompress(savefile, rootPath + interFileName);



                log.info("rootPath=" + rootPath);


                // 读取原先jmx的中url
                ReadJmxUrl readJmxUrl = new ReadJmxUrl();



                File filejmx = new File((rootPath + interFileName).replace("\\", "/"));
                String jmxfile = null;

                log.info("filejmx==" + filejmx);

                if (filejmx.isDirectory()) {
                    File[] fileArray = filejmx.listFiles();
                    if (fileArray != null) {
                        for (int i = 0; i < fileArray.length; i++) {
                            // 递归调用,对jmx重命名:
                            if (fileArray[i].getName().endsWith("jmx")) {
                                jmxfile = fileArray[i].getName();

                            }

                        }
                    }
                }


                log.info("上传脚本时间开始为：" + time.format(nowTime));

                savetmp = rootPath + interFileName+"/" + jmxfile;

                log.info("savetmp----->"+savetmp);

                map = readJmxUrl.getUrlList(rootPath + interFileName+"/"
                        + jmxfile);

                testnameList = map.get("testname");
                domainUrlList = map.get("domainUrl");

                //压缩目录：
                String zipdir = rootPath + interFileName ;


                //1.本人想把解压后的文件进行相应参数附件的替换，使用master与salve文件保持一致：
                /***
                String moidyScriptName = this.resolvDependenciesSlave(zipdir,interFileName,copyTargerDir);

                operationtypestr = "<a href=" + urlstr + moidyScriptName
                        + " style=color:blue;text-decoration:none>"
                        + operationtypestr + "</a>";
                 ****/



                operationtypestr = "<a href=" + urlstr + file.getOriginalFilename()
                        + " style=color:blue;text-decoration:none>"
                        + operationtypestr + "</a>";



                int minlist = 0;
                if (domainUrlList.size() >= testnameList.size()) {
                    minlist = testnameList.size();
                } else {
                    minlist = domainUrlList.size();
                }


                 Upload_info record = null ;

                if (minlist >= 4) {


                    record = new Upload_info(time.format(nowTime), "",
                            uploadName, interFileName, interFileName, "jmeter",
                            operationtypestr, savefile, runbutton, scriptrunorderId,
                            testnameList.get(0), testnameList.get(1),
                            testnameList.get(2), testnameList.get(3),
                            domainUrlList.get(0), domainUrlList.get(1),
                            domainUrlList.get(2), domainUrlList.get(3), "", "",
                            "", "","","");

                }

                if (minlist == 3) {


                    record = new Upload_info(time.format(nowTime), "",
                            uploadName, interFileName, interFileName, "jmeter",
                            operationtypestr, savefile, runbutton, scriptrunorderId,
                            testnameList.get(0), testnameList.get(1),
                            testnameList.get(2), "", domainUrlList.get(0),
                            domainUrlList.get(1), domainUrlList.get(2), "", "",
                            "", "", "","","");

                }

                if (minlist == 2) {


                    record = new Upload_info(time.format(nowTime), "",
                            uploadName, interFileName, interFileName, "jmeter",
                            operationtypestr, savefile, runbutton, scriptrunorderId,
                            testnameList.get(0), testnameList.get(1), "", "",
                            domainUrlList.get(0), domainUrlList.get(1), "", "",
                            "", "", "", "","","");

                }

                if (minlist == 1) {


                    record = new Upload_info(time.format(nowTime), "",
                            uploadName, interFileName, interFileName, "jmeter",
                            operationtypestr, savefile, runbutton, scriptrunorderId,
                            testnameList.get(0), "", "", "",
                            domainUrlList.get(0), "", "", "", "", "", "", "","","");

                }

                // 对于没有url的情况:
                if (minlist == 0) {


                    record = new Upload_info(time.format(nowTime), "",
                            uploadName, interFileName, interFileName, "jmeter",
                            operationtypestr, savefile, runbutton, scriptrunorderId, "",
                            "", "", "", "", "", "", "", "", "", "", "","","");

                }





                uploadScriptServer.insert(record);




                //json 格式发送给agent进行下载脚本：

                JSONObject jsonObj = new JSONObject();
                jsonObj.put("nowTime", time.format(nowTime));
                jsonObj.put("lastruntime", "");
                jsonObj.put("uploadName", uploadName);
                jsonObj.put("interFileName", interFileName);
                jsonObj.put("interFileName", interFileName);
                jsonObj.put("scripttype", "jmeter");
                jsonObj.put("operationtypestr", operationtypestr);
                jsonObj.put("scriptpath", savefile);
                jsonObj.put("runbutton", runbutton);
                jsonObj.put("scriptunorder", "");
                jsonObj.put("scriptDownLoadFromUrl",scriptDownLoadFromUrl);
                jsonObj.put("runJmeterJmeterUpload", "runJmeterJmeterUpload");
                jsonObj.put("testType","interTest");
                jsonObj.put("consumerIp",dyIPaddressPubicProvider.getConsumerOsIpaddress());
                jsonObj.put("PathParameters",PathParameters);







            //    topicSender.send(jsonObj.toJSONString()) ;


                testnameList.clear();
                domainUrlList.clear();
                map.clear();



                // 文件删除:
                fileFolder.clearFiles(savetmp);





                //文件上传到代理服务器：

                jsonObj.toJSONString();
                ThreadByRunnableSendMq thread = new ThreadByRunnableSendMq(publishController,jsonObj) ;
                new Thread(thread, "发送：上传脚本消息给Jmeter代理服务器......").start();


            } catch (Exception e) {


                // 存完关闭
                System.out.println("file=" + file);


                System.out.println("map");
      //          map.clear();


                modelMap.put("false", "false");
                modelMap.put("message", "上传失败!");
                log.info("上传jmeter脚本失败!!!");

                System.out.println();

                e.printStackTrace();

            }
        }


        Upload_info recond = uploadScriptServer.selectOrderByLimit();

        modelMap.put("result", new ResponseResult(ResponseMeta.SUCCESS, recond));
        return modelMap;

    }


    /*
     * Java文件操作 获取不带扩展名的文件名
	 *
	 * Created on: 2011-8-2 Author: dengdagui
	 */
    public String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;

    }

    /***
     * 得到文件后缀：
     * @param filezip
     */
    public String getfileSuffix(String filezip){

        File  file = new File(filezip) ;
        String fileName = file.getName();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);

        return  suffix ;

    }


    /***
     * 解决Master与slave拷入的参数附件依赖一致性问题
     * @param destPath
     * @return
     * @throws UnsupportedEncodingException
     */
    public String resolvDependenciesSlave(String destPath,String interFileName,String targetPath) throws Exception {

        //0.其它返回值：
        String scenarioFile = destPath;

        List <String> paramAttachmentList = new ArrayList <>();
        List <String> jmxFileList = new ArrayList <>();


        //重命名为日期格式的脚本名称： 如（zhang_umas_20191101192438.jmx）


        List <String> fileAbsolutePathlist = FileOperate.getAllFileNameAbsolutePath(scenarioFile);
        for (String filenameAbsolutePath : fileAbsolutePathlist) {
            filenameAbsolutePath = filenameAbsolutePath.replace("\\", "/");
            File file = new File(filenameAbsolutePath.replace("\\", "/"));


            //2.1 得到此目录下的所有参数附件绝对路径，为修改脚本提供update的参数方法：
            if (filenameAbsolutePath.contains(".") && file.isFile()) {
                //得到目录下所有文件的绝对路径：
                paramAttachmentList.add(filenameAbsolutePath);

            }


            //2.2 得到所有jmx的所有文件：
            if (filenameAbsolutePath.contains(".jmx") && file.isFile()) {
                jmxFileList.add(filenameAbsolutePath);

            }


        }


        //3.得到所有xml的脚本的绝对路径：
        for (String jmlfileAbsolutePath : jmxFileList) {

            //4.对所有xml的脚本中相应依赖文件进行替换:
            for (String paramAttachmenFile : paramAttachmentList) {
                String replaceParametersAttachment = paramAttachmenFile.replace("\\", "/");
                String findFileNameStr = (new File(replaceParametersAttachment)).getName();

                FileReplayUtils.modiyFilePathParameter(jmlfileAbsolutePath,
                        findFileNameStr, replaceParametersAttachment);
            }

        }


        paramAttachmentList.clear();
        jmxFileList.clear();




        //1.0 打包
        File descZip = new File(destPath.replace("\\","/"));
        System.out.println("destPath---->"+destPath);
        ZipUtils.compress(descZip);
        System.out.println("descZip---->"+descZip);
        //1.1 重命名：
        String basePath = descZip.getParent();
        System.out.println("descZip.getname--->"+descZip.getName());
        System.out.println("(new File(descZip.getParent())).getName()--->"+(new File(descZip.getParent())).getName());
        String sourceZip = basePath  + interFileName+".zip" ;
        System.out.println("sourceZip---->"+sourceZip);
        File f = new File(sourceZip);
        File resultName = new File(targetPath+"/"+interFileName+"_midy"+".zip");

        f.renameTo(resultName);




        return  resultName.toString() ;

    }



}

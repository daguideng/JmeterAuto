package com.atguigu.gmall.Impl.perfImpl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.gmall.Interface.JmeterScriptUploadIntel;
import com.atguigu.gmall.activemq.PublishController;
import com.atguigu.gmall.activemq.ThreadByRunnableSendMq;
import com.atguigu.gmall.common.bean.ResponseMeta;
import com.atguigu.gmall.common.bean.ResponseResult;
import com.atguigu.gmall.common.utils.FileOperate;
import com.atguigu.gmall.common.utils.ReadJmxUrl;
import com.atguigu.gmall.common.utils.ZipUtils;
import com.atguigu.gmall.common.utils.file.FileUtil;
import com.atguigu.gmall.entity.Upload_info;
import com.atguigu.gmall.service.jmeterperf.UploadScriptServer;
import com.atguigu.gmall.zookeeperip.DyIPaddressPubicProvider;
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
import java.text.SimpleDateFormat;
import java.util.*;



/**
 * @Author: dengdagui
 * @Description:脚本上传操作:
 * @Date: Created in 2018-7-5
 */
@Slf4j
@Component("jmeterScriptPerfUploadImpl")
@RestController
public class JmeterScriptUploadImpl extends MultiActionController implements JmeterScriptUploadIntel {


  //  @Autowired
 //   private HttpServletRequest request ;


    @Autowired
    private UploadScriptServer uploadScriptServer ;

     @Autowired
     private ThreadByRunnableSendMq threadByRunnableSendMq ;

    @Autowired
    private PublishController publishController;


    @Autowired
    private DyIPaddressPubicProvider dyIPaddressPubicProvider ;


    @Value("${Path.Parameters}")
    private String PathParameters;


    @Value("${jmeterStore}")
    private String jmeterStore ;

    @Value("${jmeterScriptDir}")
    private String jmeterScriptDir ;

    @Value("${scriptDownLoadFromUrl}")
    private String scriptDownLoadFromUrl ;

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

        log.info("性能测试脚本上传开始!");

        // 使用@Value注入的jmeterStore
        String jmeterStorePath = this.jmeterStore.replace("\\", "/");
        log.info("jmeterStore=" + jmeterStorePath);

        response.setContentType("text/html; charset=UTF-8");

        // 使用@Value注入的jmeterScriptDir
        String resutDir = this.jmeterScriptDir.replace("\\", "/");


        //得到文件后缀,如果不是.zip的压缩文件给前台提示：
        if(!getfileSuffix(file.getOriginalFilename()).equals("zip")){

            modelMap.put("result", new ResponseResult(ResponseMeta.PERF_UPLOAD_File_FORMAT_ERROR, "请上传以.zip的压缩后的脚本"));

            return modelMap;
        }



        String uploadName = null ;


        try {
            uploadName = session.getAttribute("username").toString();
        }catch (Exception e){
            uploadName = "admin" ;
        }




        try {
           //  uploadName = SessionUser.getUserCode();

            if("".equals(uploadName) || null == uploadName ){
                uploadName = "admin" ;
            }

        }catch (Exception e){

            uploadName = "admin";
        }




        String  scriptDownLoadUrl = this.scriptDownLoadFromUrl + uploadName + "/" + file.getOriginalFilename();


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

                //无目录则创建目录：
                FileOperate.createDir(filePath);
                FileUtil.createDir(filePath);
                FileOperate.newFolder(filePath);

                savefile = filePath + "/" + file.getOriginalFilename();
                log.info("保存文件路径: {}", savefile);


              //  Thread.sleep(1000);
                // 转存文件

                file.transferTo(new File(savefile));


                log.info("上传jmeter脚本成功!!!");

                // 数据入库操作:
                Date nowTime = new Date();

                SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


                String operationtypestr = "[下载]";

                String runbutton = "[run]";


                // 接口名去后缀：
                String interFileName = this.getFileNameNoEx(file.getOriginalFilename());

                savefile = savefile.replace("\\", "/");


                String urlstr = this.scriptDownLoadFromUrl + uploadName + "/" ;


                operationtypestr = "<a href=" + urlstr + file.getOriginalFilename()
                        + " style=color:blue;text-decoration:none>"
                        + operationtypestr + "</a>";


                rootPath = jmeterStore;

                log.info("savefile===>" + savefile);
                log.info("rootPath===>" + rootPath);


                FileOperate.deleteAllFilesOfDir(new File(rootPath + "/tempUnZipfile/"));

                System.out.println("savefile------>"+savefile);
                System.out.println("rootPath------>"+rootPath+ "/tempUnZipfile/");

                ZipUtils.decompress(savefile, rootPath + "/tempUnZipfile/");



                // 读取原先jmx的中url
                ReadJmxUrl readJmxUrl = new ReadJmxUrl();



                File filejmx = new File((rootPath + "tempUnZipfile").replace("\\", "/"));
                String jmxfile = null;


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

                    System.out.println();
                }


                log.info("上传脚本时间开始为：" + time.format(nowTime));

                savetmp = rootPath + "tempUnZipfile/" + jmxfile;

                map = readJmxUrl.getUrlList(rootPath + "tempUnZipfile/"
                        + jmxfile);

                testnameList = map.get("testname");
                domainUrlList = map.get("domainUrl");


                int minlist = 0;
                if (domainUrlList.size() >= testnameList.size()) {
                    minlist = testnameList.size();
                } else {
                    minlist = domainUrlList.size();
                }


                 Upload_info record = null ;

                if (minlist >= 4) {


                    /**
                    System.out.println("size--->"+ domainUrlList.size());
                    System.out.println("0000--->"+ domainUrlList.get(0));
                    System.out.println("1111--->"+ domainUrlList.get(1));
                    System.out.println("2222--->"+ domainUrlList.get(2));
                    System.out.println("3333--->"+ domainUrlList.get(3));
                    System.out.println("4444--->"+ domainUrlList.get(4));
                    System.out.println("5555--->"+ domainUrlList.get(5));
                    System.out.println("6666--->"+ domainUrlList.get(6));
                    System.out.println("7777--->"+ domainUrlList.get(7));
                    System.out.println("8888--->"+ domainUrlList.get(8));
                    System.out.println("9999--->"+ domainUrlList.get(9));
                     ***/



                    record = new Upload_info(time.format(nowTime), "",
                            uploadName, interFileName, interFileName, "jmeter",
                            operationtypestr, savefile, runbutton, scriptrunorderId,
                            testnameList.get(0), testnameList.get(1),
                            testnameList.get(2), testnameList.get(3),
                            domainUrlList.get(0).toString().trim(), domainUrlList.get(1).toString().trim(),
                            domainUrlList.get(2).toString().trim(), domainUrlList.get(3).toString().trim(), "", "",
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
                jsonObj.put("testType","perfTest");
                jsonObj.put("consumerIp",dyIPaddressPubicProvider.getConsumerOsIpaddress());
                jsonObj.put("PathParameters",PathParameters);




                testnameList.clear();
                domainUrlList.clear();
                map.clear();
                // 文件删除:
                fileFolder.clearFiles(savetmp);

                //主动传上传脚本名给下载用
               // fileDownLoad.FileNameInit(jsonObj);

                //文件上传到代理服务器：
                jsonObj.toJSONString();
                ThreadByRunnableSendMq  thread = new ThreadByRunnableSendMq(publishController,jsonObj) ;
                new Thread(thread, "发送：上传脚本消息给Jmeter代理服务器......").start();

                //主动传上传脚本名给下载用

            } catch (Exception e) {

                modelMap.put("false", "false");
                modelMap.put("message", "上传失败!");
                log.info("上传jmeter脚本失败!!!");

                e.printStackTrace();

            }finally {
                // 存完关闭

            //    map.clear();


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



}

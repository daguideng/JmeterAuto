package com.atguigu.gmall.common.utils.app;

import com.alibaba.fastjson.JSONObject;
import com.dd.plist.NSDictionary;
import com.dd.plist.NSString;
import com.dd.plist.PropertyListParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.atguigu.gmall.common.utils.file.FileBase64.fileToBase64;

public class IpaUtil {

    private static Logger logger = LoggerFactory.getLogger(IpaUtil.class);

    /**
     * 解压IPA文件，只获取IPA文件的Info.plist文件存储指定位置
     *
     * @param file           zip文件
     * @param unzipDirectory 解压到的目录
     * @throws Exception
     */
    private static JSONObject getZipInfo(File file, String unzipDirectory)
            throws Exception {
        // 定义输入输出流对象
        InputStream input1 = null;
        InputStream input2 = null;
        OutputStream output1 = null;
        OutputStream output2 = null;
        File result1 = null;
        File result2 = null;
        File unzipFile;
        ZipFile zipFile = null;
        JSONObject json = new JSONObject();
        try {
            // 创建zip文件对象
            zipFile = new ZipFile(file);
            // 创建本zip文件解压目录
            String name = file.getName().substring(0, file.getName().lastIndexOf("."));
            unzipFile = new File(unzipDirectory + "/" + name);
            if (unzipFile.exists()) {
                unzipFile.delete();
            }
            unzipFile.mkdir();
            // 得到zip文件条目枚举对象
            Enumeration<? extends ZipEntry> zipEnum = zipFile.entries();
            // 定义对象
            ZipEntry entry = null;
            String entryName = null;
            String names[] = null;
            int length;
            boolean hasIcon = false;
            boolean hasInfoPlist = false;
            // 循环读取条目
            while (zipEnum.hasMoreElements()) {
                // 得到当前条目
                entry = zipEnum.nextElement();
                entryName = new String(entry.getName());
                // 用/分隔条目名称
                names = entryName.split("\\/");
                length = names.length;

                for (int v = 0; v < length; v++) {
                    if (entryName.endsWith(".app/Info.plist")) { // 为Info.plist文件,则输出到文件
                        hasInfoPlist = true;
                        input1 = zipFile.getInputStream(entry);
                        result1 = new File(unzipFile.getAbsolutePath() + "/Info.plist");
                        output1 = new FileOutputStream(result1);
                        byte[] buffer = new byte[1024 * 8];
                        int readLen = 0;
                        while ((readLen = input1.read(buffer, 0, 1024 * 8)) != -1) {
                            output1.write(buffer, 0, readLen);
                        }
                        break;
                    }
                }
                for (int v = 0; v < length; v++) {
                    if (entryName.endsWith("60x60@3x.png")) { // 为@3x.png文件,则输出到文件
                        String newName = entryName.contains("/") ? String.valueOf(entryName.lastIndexOf("/")) : entryName;
                        hasIcon = true;
                        input2 = zipFile.getInputStream(entry);
                        result2 = new File(unzipFile.getAbsolutePath() + "/" + newName);
                        output2 = new FileOutputStream(result2);
                        byte[] buffer = new byte[1024 * 8];
                        int readLen = 0;
                        while ((readLen = input2.read(buffer, 0, 1024 * 8)) != -1) {
                            output2.write(buffer, 0, readLen);
                        }
                        break;
                    }
                }
            }
            if (!hasInfoPlist){
                logger.error("ipa文件不含InfoPlist,请添加");
              //  throw new ServiceException(RES_STATUS.NO_INFOPLIST_IN_IPA);
            }
            if (!hasIcon){
                logger.error("ipa文件不含icon,请添加");
             //   throw new ServiceException(RES_STATUS.NO_APPICON_IN_IPA);
            }
        } catch (Exception ex) {
            logger.error("ipa文件内容不完整,请检查", ex);
          //  throw new ServiceException(RES_STATUS.UNCOMPLETED_IPA_FILE);
        } finally {
            if (input1 != null)
                input1.close();
            if (input2 != null)
                input2.close();
            if (output1 != null) {
                output1.flush();
                output1.close();
            }
            if (output2 != null) {
                output2.flush();
                output2.close();
            }
            // 必须关流，否则文件无法删除
            if (zipFile != null) {
                zipFile.close();
            }
        }
        // 如果有必要删除多余的文件
        if (file.exists()) {
            file.delete();
        }

        String file_result2 = fileToBase64(result2);
        json.put("result1", result1);
        json.put("result2", file_result2);
        return json;
    }


    /**
     * IPA文件的拷贝，把一个IPA文件复制为Zip文件,同时返回Info.plist文件
     * 参数 oldfile 为 IPA文件
     */
    private static JSONObject getIpaInfo(File oldfile) throws IOException {
        try {
            int byteread = 0;
            String filename = oldfile.getAbsolutePath().replaceAll(".ipa", ".zip");
            File newfile = new File(filename);
            if (oldfile.exists()) {
                // 创建一个Zip文件
                InputStream inStream = new FileInputStream(oldfile);
                FileOutputStream fs = new FileOutputStream(newfile);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
                if (inStream != null) {
                    inStream.close();
                }
                if (fs != null) {
                    fs.close();
                }
                // 解析Zip文件
                return getZipInfo(newfile, newfile.getParent());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过IPA文件获取Info信息
     * 这个方法可以重构，原因是指获取了部分重要信息，如果想要获取全部，那么应该返回一个Map<String,Object>
     * 对于plist文件中的数组信息应该序列化存储在Map中，那么只需要第三发jar提供的NSArray可以做到！
     */
    public static JSONObject getIpaInfoMap(String filePath) throws Exception {
        File ipa = new File(filePath);
        JSONObject json = new JSONObject();
        JSONObject jsonR = getIpaInfo(ipa);
        File file1 = jsonR.getObject("result1", File.class);
        String file_result = jsonR.getString("result2");
        // 第三方jar包提供
        NSDictionary rootDict = (NSDictionary) PropertyListParser.parse(file1);
        // 应用包名
        NSString parameters = (NSString) rootDict.objectForKey("CFBundleIdentifier");
        json.put("CFBundleIdentifier", parameters.toString());
        // 应用名称
        parameters = (NSString) rootDict.objectForKey("CFBundleName"); // 工程名
        NSString pa = (NSString) rootDict.objectForKey("CFBundleDisplayName"); // 应用名
        if (null != pa && !"".equals(pa)){
            parameters = pa;
        }
        json.put("CFBundleName", parameters.toString());
        // 应用版本
        parameters = (NSString) rootDict.objectForKey("CFBundleVersion");
        json.put("CFBundleVersion", parameters.toString());
        // 应用发布版本
        parameters = (NSString) rootDict.objectForKey("CFBundleShortVersionString");
        json.put("CFBundleShortVersionString", parameters.toString());
        // 应用所需IOS最低版本
        parameters = (NSString) rootDict.objectForKey("MinimumOSVersion");
        json.put("MinimumOSVersion", parameters.toString());
        json.put("logoFile", file_result);
        // 如果有必要，应该删除解压的结果文件
        file1.delete();
//        file1.getParentFile().delete();

        return json;
    }

    public static void main(String[] args) {
        String f = "/Users/ellen/Downloads/0b2e8c0d-b1dd-4b19-8b81-5b81593c1a4c.ipa";
        JSONObject json = null;
        try {
            json = getIpaInfoMap(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(json.toJSONString());
    }


}

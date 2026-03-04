package com.atguigu.gmall.common.utils.file;


import java.io.*;
import java.util.Base64;

public class FileBase64 {

    /**
     *
     * @param file
     * @return String
     * @description 将文件转base64字符串
     * @date 2018年3月20日
     * @author changyl
     * File转成编码成BASE64
     */

    public static  String fileToBase64(File file) {
        String base64 = null;
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            byte[] bytes=new byte[(int)file.length()];
            in.read(bytes);
            base64 = Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return base64;
    }


    //BASE64解码成File文件
    public static void base64ToFile(String destPath,String base64, String fileName) {
        File file;
        //创建文件目录
        File  dir=new File(destPath);
        if (!dir.exists() && !dir.isDirectory()) {
            dir.mkdirs();
        }
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        try {
            byte[] bytes = Base64.getDecoder().decode(base64);
            file=new File(destPath+"/"+fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void main(String[] args) {
        String bas = "iVBORw0KGgoAAAAEQ2dCSVAAIAYsuHdmAAAADUlIRFIAAAC0AAAAtAgGAAAAPc0GMgAAABxpRE9UAAAAAgAAAAAAAABaAAAAKAAAAFoAAABaAAAW4Gp73+gAABasSURBVOxd+5fbxnXmf7OUrbhOl3rElu3YUePGUd04cdQcNVHrxnUTnySNnTit25OmSZrUaU6Sk7rHbvpI0/R4Kckid1ev1Wv1tB7W+xnJK0siuQSfyyWXyzcB3P5AAhgAA8wDA5ArLc6ZQxAAgcuZi5lv7v3unZCiKBBEUVV1udzHJSg9CwWpxLjN6figtkHKM2x1IUrmIJU75Lcii2ws0v3QZzpdK0Imp2eT7ivyd4NUWC8y+63YIT8VmefPilJu0Y0p8kVwGrFoezvSvf3oRPySWbRih4LAx7je0+kap/OkezkNbyyF1Mu7KQ3Nf3OTmUV2J9mc6sKtl3R6ftAyi1LskAhFdmpwkkK4VSxLL0lT0TTYjkeJaRqc1NvRvui4OiMpNQmWDZPMIpQ65Ae8oB3qSJXPAwGcKo00DPL+D5Zh2AmKkV502h7XK7SjGYGCkNmLYodEKbMbpKA5xjJss0AZXjhEcz+eoZf0n1ivYX221/sFKXMgCu1lSKbFfH68CG5DJQkyseJXGvmdRheel5f0f1gh0zDJ7KtCLzsIlsuwO2VCrD2zF5hBmrDxzIxphze360QO615hiNffepE5qOfx3EOoQt+rHqx7SY57vY1olTrEYpojzXBpLQY0dl+3Z9FYP2hm+DyWDS+OBFarCI/VyA9FHxaZaZQ6JMsyKIoCsiwDui+iZxbtfhbpqvazp+NtbJaXxYucJFPcsMuM6qumq9qxkPWEtr8Uh7Dl7f7YVFU1KTRaQriDyxaN5bIULB/UCs0zRATdow/yeaJJVDy4f9DPG7TMTr10yEmZRTTmvTzkLW+D33C9NFahWdloJDssj8eP1bPnxinwKrNXiw7Lf8HxGlg9r7SUTxY+yzDKjOulQ";
        base64ToFile("/Users/ellen/Documents/WorkSpace/gitlab/java/upload/appicon", bas, "base64.png");
    }

}

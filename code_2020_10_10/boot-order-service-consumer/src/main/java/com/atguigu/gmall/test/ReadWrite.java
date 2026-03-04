package com.atguigu.gmall.test;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ReadWrite {


    public static void main(String[] args) throws IOException {
        try {
            ReadFile("d:/cvs.txt", "d:/output.txt");
        } catch (Exception e) {
            System.out.println("异常");
            e.printStackTrace();
        } finally {

            System.out.println("执行结束，关闭输入流");
        }


    }


    public static boolean writeFile(String content, String url) throws UnsupportedEncodingException, IOException {
        File file = new File(url);
        if (!file.exists()) {
            file.createNewFile();
        }

        FileOutputStream o = null;
        o = new FileOutputStream(url, true);
        o.write(content.getBytes("UTF-8"));
        o.flush();
        o.close();
        return true;
    }


    public static String core(String before) throws ParseException {

        String[] result = new String[5];
        String[] split = before.split(",");
        int length = split.length;
        result[0] = split[0];
        result[1] = "'" + split[1] + "'";
        result[3] = split[length - 2];
        result[4] = dateChange(split[length - 1]);
        StringBuffer sb = new StringBuffer();
        for (int i = 5; i <= length; i++) {
            if (i != 5) {
                sb.append(",");
            }
            sb.append(split[i - 3]);
        }
        result[2] = colunmChange(sb);
        return result[0] + "\t" + result[1] + "\t" + result[2] + "\t" + result[3] + "\t" + result[4] + "\n";
    }

    public static String dateChange(String date) throws ParseException {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("YYYY/MM/DD");
        Date parse = sdf1.parse(date);
        String format = sdf2.format(parse);
        return format;
    }

    public static String colunmChange(StringBuffer s) {
        String column3 = s.toString();
        if (column3.contains("\"")) {
            column3 = column3.substring(1, column3.length() - 1);
            column3 = column3.replace("\"\"", "\"");
        }
        return "'" + column3 + "'";
    }





    public static void ReadFile(String readFile, String writFile) throws Exception {

        InputStream sourceFile = null;
        BufferedReader bufReader = null;

        File file = new File(writFile);
        if (!file.exists()) {
            System.out.println("该路径不存在，未读取文件");
        }

        try {

            //读入emailReport的主要head内容：

            File readfile = new File(readFile);

            sourceFile = new FileInputStream(readfile);

            bufReader = new BufferedReader(new InputStreamReader(sourceFile, "utf-8"));

            for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {

                String result = core(tmp);
                /* list.add(result);*/
                writeFile(result, writFile);


            }

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            sourceFile.close();
            bufReader.close();

        }


    }

}
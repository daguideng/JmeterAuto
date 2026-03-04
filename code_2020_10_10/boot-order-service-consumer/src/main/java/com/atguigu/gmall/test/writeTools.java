package com.atguigu.gmall.test;


import java.io.*;

public class writeTools {
    public StringBuffer writeEmailReport() throws Exception {

        StringBuffer sbf = new StringBuffer(2000);
        InputStream sourceFile =null ;
        BufferedReader bufReader = null ;

        try {

          //读入emailReport的主要head内容：

            File file = new File("jmeterperf/properties/emailReportHead.html");

            sourceFile = new FileInputStream(file);

            bufReader = new BufferedReader(new InputStreamReader(sourceFile, "utf-8"));

            for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {

                if (tmp.contains("ReportBody")) {


                        if (!"databaseIsNull".equals(tmp)) {

                            sbf.append(tmp);

                    }

                } else {

                    sbf.append(tmp);

                }


            }

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            sourceFile.close();
            bufReader.close();

        }

        return sbf;

    }


}
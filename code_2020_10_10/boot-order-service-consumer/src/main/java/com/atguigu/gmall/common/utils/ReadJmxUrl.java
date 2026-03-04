package com.atguigu.gmall.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * 读取jmx的url内容:
 * @author dagui.deng
 *
 */

public class ReadJmxUrl {
	
	public Map<String, ArrayList<String>> getUrlList(String sourceFile) {
		
		
		Map<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
		
		
		ArrayList<String>  testnamelist = new ArrayList<String>();
		ArrayList<String>  domainUrllist = new ArrayList<String>();
		
		try {
			BufferedReader bufReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(new File(
							sourceFile)),"utf-8"));
			StringBuffer strBuf = new StringBuffer();
			
			int linenumber = 0 ;
			int urllineNumber = 0 ;

			for (String tmp = null; (tmp = bufReader.readLine()) != null;) {

				
				linenumber ++ ;
		
				
				if(tmp.contains("testname=\"") && tmp.contains("enabled=\"true\"") && tmp.contains("guiclass=\"HttpTestSampleGui\" testclass=")){
		
					 

					 String testname = (String) tmp.subSequence(tmp.indexOf("testname=\""), tmp.lastIndexOf("\" enabled=\"true\">")) ;
					
					
					 testname = testname.split("testname=\"")[1] ;

					 if(!testname.equals("")){
					   testnamelist.add(testname) ;
					   urllineNumber = linenumber ;
					 }
					 
				}
				
				
				// 替换线程:
				if (tmp.contains("HTTPSampler.domain")  ) {

					 String domainUrl = (String) tmp.subSequence(tmp.indexOf("\"HTTPSampler.domain\">"), tmp.lastIndexOf("</stringProp>")) ;
					 if(!domainUrl.equals("\"HTTPSampler.domain\">")){
					   domainUrl = domainUrl.split("\"HTTPSampler.domain\">")[1] ;
					 

					 if(!domainUrl.equals("") && !domainUrl.contains("${")){
					     domainUrllist.add(domainUrl) ;
					 }
					 
					 }
					 

					 if (tmp.contains("name=\"HTTPSampler.path")) {
							

						 String httpPath = (String) tmp.subSequence(tmp.indexOf("name=\"HTTPSampler.path"), tmp.lastIndexOf("</stringProp>")) ;
						 if(!httpPath.equals("name=\"HTTPSampler.path")){
							 httpPath = httpPath.split("name=\"HTTPSampler.path")[1] ;

						 
						 if(!httpPath.equals("") && !httpPath.contains("${")){
						     domainUrllist.add(httpPath) ;
						 }
						 
						 }
						 
					 }



				}
				

			

				strBuf.append(tmp);
				strBuf.append(System.getProperty("line.separator"));

			}
			
			map.put("testname", testnamelist) ;
			map.put("domainUrl", domainUrllist) ;

			PrintWriter printWriter = new PrintWriter(sourceFile);
			printWriter.write(strBuf.toString().toCharArray());
			bufReader.close();
			printWriter.flush();
			printWriter.close();
			strBuf.setLength(0);

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return map ;
	}
	

	
	/***
	 * 替换url:
	 * @param sourceFile
	 * @param ScriptNameURL0
	 * @param replacenameURL0
	 */
public void getReplayUrlValue(String sourceFile,String ScriptNameURL0,String replacenameURL0) {
		

				
		try {
			BufferedReader bufReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(new File(
							sourceFile)),"utf-8"));
			StringBuffer strBuf = new StringBuffer();
			

			for (String tmp = null; (tmp = bufReader.readLine()) != null;) {
	
				
				// 替换线程:
				if (tmp.contains(ScriptNameURL0)  ) {
									
					 tmp = tmp.replace(ScriptNameURL0, replacenameURL0);

				}
				System.out.println();
				strBuf.append(tmp);
				strBuf.append(System.getProperty("line.separator"));
			}
			

			

			PrintWriter printWriter = new PrintWriter(sourceFile);
			printWriter.write(strBuf.toString().toCharArray());
			bufReader.close();
			printWriter.flush();
			printWriter.close();
			strBuf.delete(0, strBuf.length()) ;

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	

	
	
	
	
	public void jxmReplaceUrl(String sourceFile, String findStr,
			String replaceStr) {
		
       
		try {
			BufferedReader bufReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(new File(
							sourceFile)),"utf-8"));
			StringBuffer strBuf = new StringBuffer();

			for (String tmp = null; (tmp = bufReader.readLine()) != null;) {

				
				// 替换线程:
				if (tmp.contains(findStr)) {
					

					String findStrold = (String) tmp.subSequence(tmp.indexOf(":")-1, tmp.lastIndexOf(findStr)) ;

					
					tmp = tmp.replace(findStrold,replaceStr);
					

				}
				

			

				strBuf.append(tmp);
				strBuf.append(System.getProperty("line.separator"));

			}

			PrintWriter printWriter = new PrintWriter(sourceFile);
			printWriter.write(strBuf.toString().toCharArray());
			bufReader.close();
			printWriter.flush();
			printWriter.close();
			strBuf.delete(0, strBuf.length()) ;

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

}

package com.atguigu.gmall.common.utils.url;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 *  url操作公共类
 *  
 * @author Fu MingJun
 * @date 2017年3月3日 上午11:34:51
 *
 */
public class UrlUtils {

	public static String getBaseUrl(String url) {
		URI uri;
		try {
			uri = new URI(url);
			
			StringBuilder sb = new StringBuilder();
			sb.append(uri.getScheme());
			sb.append("://");
			sb.append(uri.getHost());
			
			if(-1 != uri.getPort()) {
				sb.append(":");
				sb.append(uri.getPort());
			}
			return sb.toString();
			
		} catch (URISyntaxException e) {
			return url;
		}
		
	}
	
	public static Map<String, String> toMap(String query) {
        Map<String, String> map = null;
        
        if (query != null && query.indexOf("=") > -1) {
            map = new HashMap<String, String>();
             
            String[] arrTemp = query.split("&");          
            for (String str : arrTemp) {
                String[] qs = str.split("=");
                
                int length = qs.length;
                		
                if(null != qs &&  length == 2){
                	 map.put(qs[0], qs[1]);
                }else if(length == 1){
                	 map.put(qs[0], "");
                }
            }
        }
         
        return map;
    }
	
}

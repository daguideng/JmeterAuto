package com.atguigu.gmall.common.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2019-4-16
 */
public class ReadMapUtil {

    public static void main(String args[]) throws IOException{

        Map<String,String> m = new HashMap<String,String>();

        m.put("4","haaa4");
        m.put("5","haaa5");
        m.put("1","haaa1");
        m.put("2","haaa2");
        m.put("3","haaa3");


        //first
       for(String key:m.keySet()){

           System.out.println(key+m.get(key)) ;
       }

       //second
        for(Map.Entry<String,String> d:m.entrySet()){
            System.out.println(d.getKey()+d.getValue());
        }

        //third
        Set<String> x = m.keySet();
        Iterator it = x.iterator();
        while(it.hasNext()){
            String key = (String)it.next();
            System.out.println(key + m.get(key)) ;
        }


    }




}

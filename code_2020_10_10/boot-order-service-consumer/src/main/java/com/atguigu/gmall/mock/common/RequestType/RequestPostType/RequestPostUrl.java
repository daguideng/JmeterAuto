package com.atguigu.gmall.mock.common.RequestType.RequestPostType;

import org.apache.tomcat.util.buf.MessageBytes;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class RequestPostUrl {



    //根据Field获得对应的Class
    private Class getClassByName(Class classObject, String name){
        Map<Class,List<Field>> fieldMap = new HashMap<>();
        Class returnClass = null;
        Class tempClass = classObject;
        while (tempClass != null) {
            fieldMap.put(tempClass, Arrays.asList(tempClass .getDeclaredFields()));
            tempClass = tempClass.getSuperclass();
        }

        for(Map.Entry<Class,List<Field>> entry: fieldMap.entrySet()){
            for (Field f : entry.getValue()) {
                if(f.getName().equals(name)){
                    returnClass = entry.getKey();
                    break;
                }
            }
        }
        return returnClass;
    }

    //递归遍历父类寻找coyoteRequest Field
    private Object findCoyoteRequest(Object request)  throws Exception {
        Class a = getClassByName(request.getClass(),"request");
        Field request1 = a.getDeclaredField("request");
        request1.setAccessible(true);
        Object b = request1.get(request);
        if(getClassByName(b.getClass(),"coyoteRequest") == null){
            return findCoyoteRequest(b);
        }else{
            return b;
        }
    }


    //请求url值：
    public String getRequestUrl(HttpServletRequest request)throws Exception{

        Object a = findCoyoteRequest(request);
        Field coyoteRequest = a.getClass().getDeclaredField("coyoteRequest");
        coyoteRequest.setAccessible(true);
        Object b = coyoteRequest.get(a);

        Field uriMB = b.getClass().getDeclaredField("uriMB");
        uriMB.setAccessible(true);
        MessageBytes c = (MessageBytes)uriMB.get(b);
        String requestUrl = c.getString();


        return requestUrl ;
    }

}

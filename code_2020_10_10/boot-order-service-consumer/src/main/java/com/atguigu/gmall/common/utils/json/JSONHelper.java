package com.atguigu.gmall.common.utils.json;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.atguigu.gmall.common.filter.JsonValueFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONHelper {



    /**
     * 敏感数据脱敏后json
     * @param object
     * @return
     */
    public static String toJSONStringAdvance(Object object){

        return JSONObject.toJSONString(object, JsonValueFilter.getInstance(),
                new SerializerFeature[0]);
    }

    public static String toJSONStringNormal(Object object){
        return JSONObject.toJSONString(object);
    }

    /**
     * jsonStr参数脱敏
     *  手机号
     *  15位+18位身份证
     * @param json
     * @return
     */
    public static String desensitizationJsonStr(Object json){
        try {

            return JSONHelper.toJSONStringAdvance(JSONObject.parse(String.valueOf(json)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return String.valueOf(json);

    }


    /**
     * json转换为Object
     *
     * @param text
     * @param clazz
     * @return
     */
    public static final <T> T parseObject(String text, Class<T> clazz) {
        return JSONObject.parseObject(text, clazz);
    }

    public static final <T> List<T> parseArray(String text, Class<T> clazz) {
        return JSONObject.parseArray(text, clazz);
    }

    public static void main(String[] args) {
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("mobile", "138000138000");

        list.add(map);

        System.out.println(desensitizationJsonStr(JSONObject.toJSONString(list)));


    }

}


package com.atguigu.gmall.common.utils;


import com.google.gson.Gson;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import net.sf.json.JSONArray;

public class MyJsonUtil {



    public static <T> String object_to_json(T t) {
        Gson gson = new Gson();

        String json = gson.toJson(t);

        try {
         //   json = URLEncoder.encode(json, "utf-8");
         //   json = json.replace("\"","");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return json;
    }

    public static <T> T json_to_object(String json, Class<T> t) {

        try {
            json = URLDecoder.decode(json, "utf-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Gson gson = new Gson();

        T fromJson = gson.fromJson(json, t);

        return fromJson;
    }

    public static <T> List<T> json_to_list(String json, Class<T> t) {
        String decode = "";

        if (StringUtils.isBlank(json)) {
            return null;
        } else {
            try {
                decode = URLDecoder.decode(json, "utf-8");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            JSONArray fromObject2 = net.sf.json.JSONArray.fromObject(decode);

            List<T> list_array = (List<T>) JSONArray.toCollection(fromObject2, t);

            return list_array;
        }

    }

    public static <T> String list_to_json(List<T> list) {

        Gson gson = new Gson();
        String json = gson.toJson(list);

        try {
            json = URLEncoder.encode(json, "utf-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return json;

    }

}

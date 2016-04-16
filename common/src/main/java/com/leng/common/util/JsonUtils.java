package com.leng.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * json工具
 *
 * @author liaoheng
 * @version 2015年8月19日
 */
public class JsonUtils {

    public static <T> String toJson(T t) throws SystemException {
        try {
            return new ObjectMapper().writeValueAsString(t);
        } catch (JsonProcessingException e) {
            throw new SystemException(e);
        }
    }


    public static <T> T parse(String json, Class<T> clazz) throws SystemException {
        try {
            return new ObjectMapper().readValue(json, clazz);
        } catch (IOException e) {
            throw new SystemException(e);
        }
    }

    public static <T> T parse(String json, String parameter,
                              Class<T> clazz) throws SystemException {
        try {
            JSONObject jsonObject = new JSONObject(json);
            return new ObjectMapper().readValue(jsonObject.getString(parameter), clazz);
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }

    public static String parseString(String json, String parameter){
        try {
            JSONObject jsonObject=new JSONObject(json);
            return jsonObject.getString(parameter);
        } catch (JSONException ignored) {
        }
        return null;
    }

    /**
     * @param json
     * @param parameter
     * @param valueType {@link TypeFactory}
     * @param <T>
     * @return
     * @throws SystemException
     */
    public static <T> List<T> parseList(String json, String parameter,
                                        JavaType valueType) throws SystemException {
        try {
            JSONObject jsonObject = new JSONObject(json);
            return new ObjectMapper().readValue(jsonObject.getString(parameter), valueType);
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }

    public static <T> List<T> parseList(String json, String parameter,
                                        Class<T> clazz) throws SystemException {
        return parseList(json, parameter, TypeFactory.defaultInstance().constructCollectionType(List.class, clazz));
    }
}

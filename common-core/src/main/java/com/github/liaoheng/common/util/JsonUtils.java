package com.github.liaoheng.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
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

    public static int getInt(String name, JSONObject jsonObject) {
        try {
            return jsonObject.getInt(name);
        } catch (JSONException ignored) {
        }
        return 0;
    }

    public static String getString(String name, JSONObject jsonObject) {
        try {
            return jsonObject.getString(name);
        } catch (JSONException ignored) {
        }
        return "";
    }

    public static long getLong(String name, JSONObject jsonObject) {
        try {
            return jsonObject.getLong(name);
        } catch (JSONException ignored) {
        }
        return 0;
    }

    public static double getDouble(String name, JSONObject jsonObject) {
        try {
            return jsonObject.getDouble(name);
        } catch (JSONException ignored) {
        }
        return 0;
    }

    public static String parseString(String json, String parameter) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            return getString(parameter, jsonObject);
        } catch (JSONException ignored) {
        }
        return "";
    }

    //https://stackoverflow.com/questions/5455014/ignoring-new-fields-on-json-objects-using-jackson
    public static ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    public static <T> String toJson(T t) throws SystemException {
        try {
            return getObjectMapper().writeValueAsString(t);
        } catch (JsonProcessingException e) {
            throw new SystemException(e);
        }
    }

    public static <T> T parse(String json, Class<T> clazz) throws SystemException {
        try {
            return getObjectMapper().readValue(json, clazz);
        } catch (IOException e) {
            throw new SystemException(e);
        }
    }

    public static <T> T parse(String json, String parameter,
            Class<T> clazz) throws SystemException {
        try {
            JSONObject jsonObject = new JSONObject(json);
            return getObjectMapper().readValue(jsonObject.getString(parameter), clazz);
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }

    /**
     * @param valueType {@link TypeFactory}
     */
    public static <T> List<T> parseList(String json, String parameter,
            JavaType valueType) throws SystemException {
        try {
            JSONObject jsonObject = new JSONObject(json);
            return getObjectMapper().readValue(jsonObject.getString(parameter), valueType);
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }

    public static <T> List<T> parseList(String json, String parameter,
            Class<T> clazz) throws SystemException {
        return parseList(json, parameter, TypeFactory.defaultInstance().constructCollectionType(List.class, clazz));
    }
}

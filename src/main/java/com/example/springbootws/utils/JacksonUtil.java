package com.example.springbootws.utils;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Catfish
 * @version 1.0 2019-03-21 12:22:48
 * @email catfish_lty@qq.com
 **/
@Slf4j
public class JacksonUtil {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        OBJECT_MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    }

    private JacksonUtil() {
    }

    public static String objectToJson(Object object) {

        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unable to serialize to json: " + object, e);
            return "{}";
        }

    }


    public static <T> T jsonToObject(String json, Class<T> klass) {
        try {
            return OBJECT_MAPPER.readValue(json, klass);
        } catch (Exception e) {
            log.error("Exception during deserializing {} from {}", klass.getSimpleName(), StringUtils.abbreviate(json, 80));
            return null;
        }
    }

    public static <T extends List, R> T jsonToList(String json, Class<T> listClz, Class<R> itemClz) {
        try {
            JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructCollectionType(listClz, itemClz);
            return OBJECT_MAPPER.readValue(json, javaType);
        } catch (Exception e) {
            log.error("Runtime exception during deserializing {}<{}> from {}", listClz.getSimpleName(),
                itemClz.getSimpleName(), StringUtils.abbreviate(json, 80), e);
            return null;
        }
    }

    public static <T extends Map, K, V> T jsonToMap(String json, Class<T> mapClz, Class<K> kClz, Class<V> vClz) {

        try {
            JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructMapType(mapClz, kClz, vClz);
            return OBJECT_MAPPER.readValue(json, javaType);
        } catch (Exception e) {
            log.error("Runtime exception during deserializing {}<{},{}> from {}", mapClz.getSimpleName(),
                kClz.getSimpleName(), vClz.getSimpleName(), StringUtils.abbreviate(json, 80), e);
            return null;
        }
    }

    public static <T> T mapToBean(Object object, Class<T> klass) {
        return OBJECT_MAPPER.convertValue(object, klass);
    }
}

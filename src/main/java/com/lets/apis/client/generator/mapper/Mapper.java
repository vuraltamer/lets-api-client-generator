package com.lets.apis.client.generator.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Mapper {

    private static final ObjectMapper INSTANCE;

    static {
        INSTANCE = new ObjectMapper();
        INSTANCE.enable(SerializationFeature.INDENT_OUTPUT);
        INSTANCE.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public static ObjectMapper getInstance() {
        return INSTANCE;
    }

    public static <T> T toObject(String jsonValue, Class<T> clazz) {
        try {
            if (ObjectUtils.isEmpty(jsonValue)) {
                return null;
            }
            return Mapper.getInstance().readValue(jsonValue, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Mapper::toObject:::error::{}", e);
        }
    }

    public static String toJson(Object object) {
        if (ObjectUtils.isEmpty(object)) {
            return null;
        }
        try {
            return Mapper.getInstance().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("TokenCache::toJson::error::{}", e);
        }
    }
}
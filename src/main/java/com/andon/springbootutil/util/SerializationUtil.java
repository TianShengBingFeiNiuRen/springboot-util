package com.andon.springbootutil.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

/**
 * @author Andon
 * 2023/5/22
 */
@Slf4j
public class SerializationUtil {

    private final static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private SerializationUtil() {
    }

    public static <T> String toJSONString(T t) {
        try {
            return mapper.writeValueAsString(t);
        } catch (Exception e) {
            log.error("序列化失败, error:", e);
            throw new IllegalStateException(e.getMessage());
        }
    }

    public static <T> T parseObject(String json, TypeReference<T> typeReference) {
        try {
            return mapper.readValue(json, typeReference);
        } catch (Exception e) {
            log.error("反序列化失败, error:", e);
            throw new IllegalStateException(e.getMessage());
        }
    }
}

package com.example.springbootws.common;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.messaging.rsocket.MetadataExtractor;
import org.springframework.messaging.rsocket.MetadataExtractorRegistry;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.StringUtils;

import com.example.springbootws.utils.JacksonUtil;

import io.rsocket.Payload;

/**
 * @author Catfish
 * @version V1.0 2019/12/30 16:53
 * @email catfish_lty@qq.com
 */
public class JsonMetadataExtractor implements MetadataExtractor{
    @Override
    public Map<String, Object> extract(Payload payload, MimeType metadataMimeType) {
        if (metadataMimeType.equals(MimeTypeUtils.APPLICATION_JSON)) {
            String metadata = payload.getMetadataUtf8();
            if (StringUtils.isEmpty(metadata)) {
                return new HashMap<>(1);
            }
            return JacksonUtil.jsonToMap(metadata, HashMap.class, String.class, Object.class);
        } else {
            throw new IllegalArgumentException("Unsupported metadata mime type " + metadataMimeType.getType() + " + " + metadataMimeType.getSubtype());
        }
    }
}

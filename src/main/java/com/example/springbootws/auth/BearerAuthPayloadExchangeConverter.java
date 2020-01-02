package com.example.springbootws.auth;

import org.springframework.messaging.rsocket.MetadataExtractor;
import org.springframework.security.core.Authentication;
import org.springframework.security.rsocket.api.PayloadExchange;
import org.springframework.security.rsocket.authentication.PayloadExchangeAuthenticationConverter;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

import com.example.springbootws.common.JsonMetadataExtractor;

import io.rsocket.metadata.WellKnownMimeType;
import reactor.core.publisher.Mono;

/**
 * @author Catfish
 * @version V1.0 2020/1/2 17:28
 * @email catfish_lty@qq.com
 */
public class BearerAuthPayloadExchangeConverter implements PayloadExchangeAuthenticationConverter {
    private MimeType metadataMimeType = MimeTypeUtils.parseMimeType(
        WellKnownMimeType.APPLICATION_JSON.getString());

    private MetadataExtractor metadataExtractor = createDefaultExtractor();

    private static MetadataExtractor createDefaultExtractor() {
        return new JsonMetadataExtractor();
    }

    @Override
    public Mono<Authentication> convert(PayloadExchange exchange) {
        return Mono.fromCallable(() -> this.metadataExtractor
            .extract(exchange.getPayload(), this.metadataMimeType))
            .flatMap(metadata -> Mono.justOrEmpty(metadata.get(JsonAuthMetadata.TOKEN)))
            .cast(JsonAuthMetadata.class)
            .map(credentials -> new JsonAuthToken(credentials.getToken()));
    }
}

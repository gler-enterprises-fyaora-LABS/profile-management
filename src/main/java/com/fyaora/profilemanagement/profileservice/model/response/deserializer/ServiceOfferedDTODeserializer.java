package com.fyaora.profilemanagement.profileservice.model.response.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServiceOfferedDTODeserializer extends StdDeserializer<List<Integer>> {

    private final MessageSource messageSource;

    public ServiceOfferedDTODeserializer(MessageSource messageSource) {
        super(List.class);
        this.messageSource = messageSource;
    }

    @Override
    public List<Integer> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        List<Integer> result = new ArrayList<>();
        String messageWithValue =  messageSource.getMessage("service.offered.invalid.format", null, LocaleContextHolder.getLocale());

        if (null == node || node.isEmpty() || !node.isArray()) {
            throw new IllegalArgumentException(messageWithValue);
        }

        for (JsonNode item : node) {
            if (!item.isInt()) {
                throw new IllegalArgumentException(messageWithValue);
            }
            result.add(item.intValue());
        }

        return result;
    }
}

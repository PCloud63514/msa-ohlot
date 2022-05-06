package com.example.mapper.config;

import com.example.mapper.config.module.JsonMapperJava8DateTimeModule;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.NonTypedScalarSerializerBase;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.jackson.JsonComponentModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.lang.reflect.Type;

@Configuration
public class MapperConfig {
    @Bean
    public ObjectMapper objectMapper() {

        ObjectMapper objectMapper = new ObjectMapper();

        SimpleModule unQuoteModule = new SimpleModule("UnQuote");
        unQuoteModule.addSerializer(new UnQuotesSerializer());

        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .registerModule(new JsonMapperJava8DateTimeModule())
                .registerModule(new JsonComponentModule())
                .registerModule(new JavaTimeModule())
//                .registerModule(unQuoteModule)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
//                .configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, false)
//                .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true) // 용하네 더블쿼터 없애고도 이걸 돌리고 있었어?
//                .configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true)
//                .configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .configure(SerializationFeature.INDENT_OUTPUT, true);
        return objectMapper;
    }

    public class UnQuotesSerializer extends NonTypedScalarSerializerBase<String> {
        public UnQuotesSerializer() {
            super(String.class);
        }

        /**
         * For Strings, both null and Empty String qualify for emptiness.
         */
        @Override
        public boolean isEmpty(String value) {
            return (value == null) || (value.length() == 0);
        }

        @Override
        public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeRawValue(value);
        }

        @Override
        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode("string", true);
        }

        @Override
        public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
            if (visitor != null) visitor.expectStringFormat(typeHint);
        }
    }
}

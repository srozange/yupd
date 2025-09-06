package io.github.yupd.infrastructure.update.updater;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonObjectMapperFactory {

    public static Parameter getDefaultParameter() {
        return new JsonObjectMapperFactory.Parameter()
                .with(SerializationFeature.INDENT_OUTPUT)
                .without(SerializationFeature.WRITE_NULL_MAP_VALUES);
    }

    public static ObjectMapper create() {
        return create(getDefaultParameter());
    }

    public static ObjectMapper create(Parameter parameter) {
        ObjectMapper objectMapper = new ObjectMapper();

        parameter.withSerializationFeature.stream()
                .forEach(feature -> objectMapper.configure(feature, true));
        parameter.withoutSerializationFeature.stream()
                .forEach(feature -> objectMapper.configure(feature, false));

        return objectMapper;
    }

    public static class Parameter {
        final java.util.Set<SerializationFeature> withSerializationFeature = java.util.EnumSet.noneOf(SerializationFeature.class);
        final java.util.Set<SerializationFeature> withoutSerializationFeature = java.util.EnumSet.noneOf(SerializationFeature.class);

        public Parameter with(SerializationFeature feature) {
            withSerializationFeature.add(feature);
            return this;
        }

        public Parameter without(SerializationFeature feature) {
            withoutSerializationFeature.add(feature);
            return this;
        }
    }
}
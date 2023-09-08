package io.github.yupd.infrastructure.update.updator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactoryBuilder;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.util.StringQuotingChecker;

import java.util.EnumSet;
import java.util.Set;
import java.util.regex.Pattern;

public class YamlObjectMapperFactory {

    public static Parameter getDefaultParameter() {
        return new YamlObjectMapperFactory.Parameter()
                .with(YAMLGenerator.Feature.MINIMIZE_QUOTES)
                .with(YAMLGenerator.Feature.ALWAYS_QUOTE_NUMBERS_AS_STRINGS)
                .with(YAMLGenerator.Feature.MINIMIZE_QUOTES)
                .without(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
                .without(SerializationFeature.WRITE_NULL_MAP_VALUES)
                .without(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS);
    }

    public static ObjectMapper create() {
        return create(getDefaultParameter());
    }

    public static ObjectMapper create(Parameter parameter) {

        YAMLFactoryBuilder yamlFactoryBuilder = new YAMLFactoryBuilder(new YAMLFactory());
        yamlFactoryBuilder.stringQuotingChecker(new StringQuotingChecker() {

            private static final Pattern BLANK_PATTERN = Pattern.compile("\\s");

            @Override
            public boolean needToQuoteName(String name) {
                return false;
            }

            @Override
            public boolean needToQuoteValue(String value) {
                return BLANK_PATTERN.matcher(value).find();
            }
        });

        parameter.withoutYamlGeneratorFeatures.stream().forEach(yamlFactoryBuilder::disable);
        parameter.withYamlGeneratorFeatures.stream().forEach(yamlFactoryBuilder::enable);

        return new ObjectMapper(yamlFactoryBuilder.build()) {
            {
                parameter.withSerializationFeature.stream().forEach(feature -> configure(feature, true));
                parameter.withoutSerializationFeature.stream().forEach(feature -> configure(feature, false));
            }
        };
    }

    public static class Parameter {
        final Set<YAMLGenerator.Feature> withYamlGeneratorFeatures = EnumSet.noneOf(YAMLGenerator.Feature.class);
        final Set<YAMLGenerator.Feature> withoutYamlGeneratorFeatures = EnumSet.noneOf(YAMLGenerator.Feature.class);
        final Set<SerializationFeature> withSerializationFeature = EnumSet.noneOf(SerializationFeature.class);
        final Set<SerializationFeature> withoutSerializationFeature = EnumSet.noneOf(SerializationFeature.class);

        public Parameter with(YAMLGenerator.Feature feature) {
            withYamlGeneratorFeatures.add(feature);
            return this;
        }

        public Parameter without(YAMLGenerator.Feature feature) {
            withoutYamlGeneratorFeatures.add(feature);
            return this;
        }

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

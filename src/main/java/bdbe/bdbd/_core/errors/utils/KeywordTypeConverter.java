package bdbe.bdbd._core.errors.utils;

import bdbe.bdbd.keyword.KeywordType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class KeywordTypeConverter implements AttributeConverter<KeywordType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(KeywordType attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public KeywordType convertToEntityAttribute(Integer dbData) {
        return dbData == null ? null : KeywordType.fromValue(dbData);
    }
}



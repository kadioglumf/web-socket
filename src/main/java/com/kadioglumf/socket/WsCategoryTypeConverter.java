package com.kadioglumf.socket;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class WsCategoryTypeConverter implements AttributeConverter<WsCategoryType, String> {

    @Override
    public String convertToDatabaseColumn(WsCategoryType attribute) {
        return attribute.toString();
    }

    @Override
    public WsCategoryType convertToEntityAttribute(String dbData) {
        return WsCategoryType.toAttribute(dbData);
    }
}

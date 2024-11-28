package com.kadioglumf.socket.converter;

import com.kadioglumf.socket.model.enums.WsInfoType;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class WsInfoTypeConverter implements AttributeConverter<WsInfoType, String> {

  @Override
  public String convertToDatabaseColumn(WsInfoType attribute) {
    return attribute.toString();
  }

  @Override
  public WsInfoType convertToEntityAttribute(String dbData) {
    return WsInfoType.toAttribute(dbData);
  }
}

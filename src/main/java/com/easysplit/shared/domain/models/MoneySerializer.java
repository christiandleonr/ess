package com.easysplit.shared.domain.models;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Class that handles the serialization for the custom object Money
 */
public class MoneySerializer extends JsonSerializer<Money> {

    @Override
    public void serialize(Money money, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("amount", money.getAmount().toString());
        jsonGenerator.writeEndObject();
    }
}

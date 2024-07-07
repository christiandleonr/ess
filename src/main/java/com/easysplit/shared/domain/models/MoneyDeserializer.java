package com.easysplit.shared.domain.models;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Class that handles the deserialization for the custom object Money
 */
public class MoneyDeserializer extends StdDeserializer<Money> {

    public MoneyDeserializer() {
        this(null);
    }

    public MoneyDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Money deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        String value = node.get("amount").asText();

        BigDecimal amount;
        try {
            // Remove any currency symbols or commas if necessary
            String sanitizedValue = value.replaceAll("[^0-9.-]", "");
            amount = new BigDecimal(sanitizedValue);
        } catch (NumberFormatException e) {
            throw new IOException("Failed to deserialize monetary value: " + value, e);
        }

        return new Money(amount);
    }
}

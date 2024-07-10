package com.example.demo.nationalize.client.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class JsonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static JsonNode getFirstCountryElement(String jsonString) throws IOException {
        JsonNode rootNode = objectMapper.readTree(jsonString);
        JsonNode countryNode = rootNode.path("country");
        if (countryNode.isArray() && !countryNode.isEmpty()) {
            return countryNode.get(0);
        }
        return null;
    }
}

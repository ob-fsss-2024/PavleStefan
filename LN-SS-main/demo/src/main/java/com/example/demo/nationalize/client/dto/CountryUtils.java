package com.example.demo.nationalize.client.dto;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CountryUtils {

    private static final Map<String, String> COUNTRY_MAP;

    static {
        COUNTRY_MAP = new HashMap<>();
        for (String countryCode : Locale.getISOCountries()) {
            Locale locale = new Locale("", countryCode);
            COUNTRY_MAP.put(countryCode, locale.getDisplayCountry(Locale.ENGLISH));
        }
    }

    public static String getCountryFullName(String abbreviation) {
        return COUNTRY_MAP.getOrDefault(abbreviation, "Unknown Country");
    }
}

package pl.cezary;

import java.util.HashMap;
import java.util.Map;

public class MapCurrency {
    static Map<String, String> getStringMapCur() {
        Map<String, String> waluty = new HashMap<String, String>();

        waluty.put("USD", "Dolar");
        waluty.put("EUR", "Euro");
        waluty.put("JPY", "Jen");
        return waluty;
    }
}
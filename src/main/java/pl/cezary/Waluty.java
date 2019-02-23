package pl.cezary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Waluty {
    public static void main(String[] args) {

        Map<String, String> waluty = MapCurrency.getStringMapCur();

        System.out.println("\nPodaj którąś z poniższych walut\n");
        //System.out.println(waluty);
        System.out.println(waluty.get("JPY"));
        System.out.println(waluty.get("EUR"));
        System.out.println("i "+ waluty.get("USD")+"\n");

        Map<String, String> api = getStringStringMap();

        //System.out.println(api);

        Scanner scanner = new Scanner(System.in);
        String waluta = scanner.next();
        //System.out.println(api.get());

        LocalTime localTime = LocalTime.now();
        try {
            String zrodlo = api.get(waluta);
            Client klient = Client.create();
            WebResource webZrodlo = klient.resource(zrodlo);

            ClientResponse webOdpowiedz = webZrodlo.accept("application/json").get(ClientResponse.class);

            if (webOdpowiedz.getStatus() != 200){
                throw new RuntimeException("Bład HTTP...: "+ webOdpowiedz.getStatus());
            }
            String pobranyJson = webOdpowiedz.getEntity(String.class);
            System.out.println("\n---Pobieram dane z NBP---");
            System.out.println("\nPobrane dane > "+pobranyJson+"\n");

            mapperCurrecy(localTime, pobranyJson);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void mapperCurrecy(LocalTime localTime, String pobranyJson) throws java.io.IOException {
        ObjectMapper mapper = new ObjectMapper();
        Kursy kurs = mapper.readValue(pobranyJson,Kursy.class);
        System.out.println("Aktualny kurs "+kurs.getCode()+" z dnia "+kurs.getRates().get(0).getEffectiveDate()+" z godziny "+localTime.getHour()+":"+localTime.getMinute()+"\n");
        System.out.println("tabela:        \t" + kurs.getTable());
        System.out.println("waluta:        \t" + kurs.getCurrency());
        System.out.println("kod:           \t" + kurs.getCode());
        System.out.println("numer tabeli:  \t" + kurs.getRates().get(0).getNo());
        System.out.println("data tabeli:   \t" + kurs.getRates().get(0).getEffectiveDate());
        System.out.println("Kurs kupna:    \t" + kurs.getRates().get(0).getBid());
        System.out.println("Kurs sprzedaży:\t" + kurs.getRates().get(0).getAsk());
    }

    private static Map<String, String> getStringStringMap() {
        Map<String, String> api = new HashMap<>();
        api.put("Jen","http://api.nbp.pl/api/exchangerates/rates/c/jpy/today/");
        api.put("Euro","http://api.nbp.pl/api/exchangerates/rates/c/eur/today/");
        api.put("Dolar","http://api.nbp.pl/api/exchangerates/rates/c/usd/today/");
        return api;
    }
}

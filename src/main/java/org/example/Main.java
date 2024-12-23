package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static final String API_KEY = "5b3ce3597851110001cf62486fa3831ccb9e488e8455230ae8a0af37";
    private static final String COORDINATES_A = "8.681495,49.41461";
    private static final String COORDINATES_B = "8.687872,49.420318";
    private static final String ORS_URL = "https://api.openrouteservice.org/v2/directions/driving-car";

    private static final String OSM_SEARCH_URL = "https://nominatim.openstreetmap.org/search";
    private static final String ADDRESS_A = "Санкт-Петербург, Парк Победы";

    public static void main(String[] args) throws URISyntaxException, IOException {
        final String queryParams = "?api_key=" + API_KEY
                + "&start=" + COORDINATES_A
                + "&end=" + COORDINATES_B;

        final String response = doGetRequest(ORS_URL, Map.of(
                "api_key", API_KEY,
                "start", COORDINATES_A,
                "end", COORDINATES_B));
        System.out.println(response);

        final String coordinatesResponse = doGetRequest(OSM_SEARCH_URL, Map.of(
                "q", ADDRESS_A,
                "format", "json"));
        System.out.println(coordinatesResponse);

    }

    static String doGetRequest(final String url, final Map<String, String> queryParams) throws URISyntaxException, IOException
    {
        final String queryString = buildQueryParams(queryParams);
        final HttpURLConnection connection = (HttpURLConnection) new URI(url + queryString)
                .toURL()
                .openConnection();
        connection.setRequestMethod("GET");
        System.out.println(connection.getResponseCode());
        System.out.println(connection.getResponseMessage());

        final Scanner scanner = new Scanner(connection.getInputStream());

        String response = "";
        while (scanner.hasNext()) {
            response += scanner.nextLine();
        }
        return response;
    }

    static String buildQueryParams(final Map<String, String> queryParams)
    {
        if (queryParams.isEmpty())
            return "";

        List<String> formattedParams = new ArrayList<>();
        for (final Map.Entry<String, String> param : queryParams.entrySet())
        {
            formattedParams.add(param.getKey() + "=" + URLEncoder.encode(param.getValue()));
        }

        StringBuilder stringBuilder = new StringBuilder("?");
        for (int i = 0; i < formattedParams.size(); ++i)
        {
            final String param = formattedParams.get(i);
            stringBuilder.append(i != 0 ? "&" : "").append(param);
        }
        return stringBuilder.toString();
    }
}

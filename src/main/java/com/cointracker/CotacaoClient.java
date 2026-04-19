package com.cointracker;

import com.cointracker.dto.ConversaoResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CotacaoClient {
    private static String URL = "https://economia.awesomeapi.com.br/json";

    private final Gson gson = new Gson();

    public Map<String, String> buscarMoedas() {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(URL+"/available/uniq"))
                .build();

        String json;

        try {
            json = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> map = gson.fromJson(json, type);
        return map;
    }

    public List<String> buscarCoversoesDisponiveisDe(String moeda) {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(URL+"/available"))
                .build();

        String json;

        try {
            json = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> map = gson.fromJson(json, type);

        return map.keySet().stream()
                .filter(k -> k.startsWith(moeda))
                .map(k -> Arrays.stream(k.split("-")).toList().get(1))
                .toList();
    }

    public Double converter(Integer qtd, String moeda1, String moeda2){
        String endpoint = URL+"/last/"+moeda1+"-"+moeda2;

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(endpoint))
                .build();

        String json;

        try {
            json = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
            System.out.println(json);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Type type = new TypeToken<Map<String, ConversaoResponse.ConversaoData>>(){}.getType();
        Map<String, ConversaoResponse.ConversaoData> response = gson.fromJson(json, type);

        Double unitValue = Double.parseDouble(response.get(moeda1+moeda2).bid);

        return unitValue * qtd;
    }
}

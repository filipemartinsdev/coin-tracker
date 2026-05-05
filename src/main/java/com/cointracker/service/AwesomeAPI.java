package com.cointracker.service;

import com.cointracker.dto.ConversaoResponse;
import com.cointracker.excetion.MoedaNaoEncontradaException;
import com.cointracker.excetion.RequisicaoFalhouException;
import com.cointracker.model.Cotacao;
import com.cointracker.model.Moeda;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AwesomeAPI implements CoinClient{
    private static String URL = "https://economia.awesomeapi.com.br/json";
    private final Gson gson;
    private final HttpClient client;

    public AwesomeAPI(Gson gson, HttpClient client) {
        this.gson = gson;
        this.client = client;
    }

    @Override
    public List<Moeda> buscarMoedas() {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(URL+"/available/uniq"))
                .build();

        String json;

        try {
            json = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> map = gson.fromJson(json, type);

        return map.keySet().stream()
                .map(key -> new Moeda(map.get(key), key))
                .toList();
    }

    @Override
    public Double converter(String codigoMoeda1, String codigoMoeda2, Double valor) {
        String endpoint = URL+"/last/"+codigoMoeda1+"-"+codigoMoeda2;

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(endpoint))
                .build();

        String json;

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 404)
                throw new MoedaNaoEncontradaException("Moeda não encontrada");
            json = response.body();
        } catch (MoedaNaoEncontradaException e){
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        Type type = new TypeToken<Map<String, ConversaoResponse.ConversaoData>>(){}.getType();
        Map<String, ConversaoResponse.ConversaoData> response = gson.fromJson(json, type);

        Double unitValue = Double.parseDouble(response.get(codigoMoeda1+codigoMoeda2).bid);

        return unitValue * valor;
    }

    @Override
    public List<Cotacao> buscarHistoricoCotacao(String moeda, int dias) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(URL+"/daily/"+moeda+"/"+dias))
                .build();

        String json;

        try {
            json = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (Exception e) {
            throw new RequisicaoFalhouException("Falha ao buscar histórico de cotação", e);
        }

        Type type = new TypeToken<List<ConversaoResponse.ConversaoData>>(){}.getType();
        List<ConversaoResponse.ConversaoData> response = gson.fromJson(json, type);

        return response.stream()
                .map(this::converterParaCotacao)
                .toList();
    }

    private Cotacao converterParaCotacao(ConversaoResponse.ConversaoData conversaoData){
        LocalDate data = Instant.ofEpochSecond(Long.parseLong(conversaoData.timestamp))
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        return new Cotacao(
                conversaoData.code,
                conversaoData.name != null ? conversaoData.name.split("/")[0] : "",
                Double.parseDouble(conversaoData.bid),
                Double.parseDouble(conversaoData.high),
                Double.parseDouble(conversaoData.low),
                Double.parseDouble(conversaoData.varBid),
                data
        );
    }

    @Override
    public List<Cotacao> buscarCotacaoMoedas() {
        List<String> moedasDisponiveisParaConversaoBRL = buscarConversoesDisponiveisPara("BRL");

        List<Cotacao> cotacoes = new ArrayList<>();

        for (String codigoMoeda : moedasDisponiveisParaConversaoBRL) {
            cotacoes.add(buscarHistoricoCotacao(codigoMoeda, 1).getFirst());
        }

        return cotacoes;
    }

    @Override
    public List<String> buscarConversoesDisponiveisDe(String moeda) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(URL+"/available"))
                .build();

        String json;

        try {
            json = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (Exception e) {
            throw new RequisicaoFalhouException("Falha ao buscar conversões disponíveis", e);
        }

        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> map = gson.fromJson(json, type);

        return map.keySet().stream()
                .filter(k -> k.startsWith(moeda))
                .map(k -> Arrays.stream(k.split("-")).toList().get(1))
                .sorted()
                .toList();
    }

    @Override
    public List<String> buscarConversoesDisponiveisPara(String moeda) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(URL+"/available"))
                .build();

        String json;

        try {
            json = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (Exception e) {
            throw new RequisicaoFalhouException("Falha ao buscar conversões disponíveis", e);
        }

        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> map = gson.fromJson(json, type);

        return map.keySet().stream()
                .filter(k -> k.endsWith(moeda))
                .map(k -> Arrays.stream(k.split("-")).toList().get(0))
                .sorted()
                .toList();
    }

//    TODO: UTILIZAR ISSO
    public void close() {
        client.close();
    }
}

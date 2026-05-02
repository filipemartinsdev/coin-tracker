package com.cointracker.service;

import com.cointracker.model.Cotacao;
import com.cointracker.model.Moeda;

import java.util.List;

public interface CoinClient {
    List<Moeda> buscarMoedas();

    Double converter(String moeda1, String moeda2, Double valor);

    List<Cotacao> buscarHistoricoCotacao(String moeda, int dias);

    List<Cotacao> buscarCotacaoMoedas();

    List<String> buscarConversoesDisponiveisDe(String moeda);

    List<String> buscarConversoesDisponiveisPara(String moeda);
}

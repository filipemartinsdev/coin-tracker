package com.cointracker.dto;

import java.time.Instant;
import java.time.LocalDate;

public class TableHistoricoCotacaoItem {
    private LocalDate data;
    private Double valor;
    private Double alta;
    private Double baixa;
    private Double variacao;

    public TableHistoricoCotacaoItem(LocalDate data, Double valor, Double alta, Double baixa, Double variacao) {
        this.data = data;
        this.valor = valor;
        this.alta = alta;
        this.baixa = baixa;
        this.variacao = variacao;
    }

    public LocalDate getData() {
        return data;
    }

    public Double getValor() {
        return valor;
    }

    public Double getAlta() {
        return alta;
    }

    public Double getBaixa() {
        return baixa;
    }

    public Double getVariacao() {
        return variacao;
    }
}

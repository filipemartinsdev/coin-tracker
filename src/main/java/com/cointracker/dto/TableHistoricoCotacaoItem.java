package com.cointracker.dto;

import java.time.Instant;
import java.time.LocalDate;

public class TableHistoricoCotacaoItem {
    private LocalDate data;
    private Double fechamento;
    private Double alta;
    private Double baixa;
    private Double variacao;

    public TableHistoricoCotacaoItem(LocalDate data, Double fechamento, Double alta, Double baixa, Double variacao) {
        this.data = data;
        this.fechamento = fechamento;
        this.alta = alta;
        this.baixa = baixa;
        this.variacao = variacao;
    }

    public LocalDate getData() {
        return data;
    }

    public Double getFechamento() {
        return fechamento;
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

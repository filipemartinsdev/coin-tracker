package com.cointracker.model;

import java.time.LocalDate;

public class Cotacao {
    private String codigoMoeda;
    private String nomeMoeda;
    private Double fechamento;
    private Double alta;
    private Double baixa;
    private Double variacao;
    private LocalDate data;

    public Cotacao(String codigoMoeda, String nomeMoeda, Double fechamento, Double alta, Double baixa, Double variacao, LocalDate data) {
        this.codigoMoeda = codigoMoeda;
        this.nomeMoeda = nomeMoeda;
        this.fechamento = fechamento;
        this.alta = alta;
        this.baixa = baixa;
        this.variacao = variacao;
        this.data = data;
    }

    public String getCodigoMoeda() {
        return codigoMoeda;
    }

    public void setCodigoMoeda(String codigoMoeda) {
        this.codigoMoeda = codigoMoeda;
    }

    public String getNomeMoeda() {
        return nomeMoeda;
    }

    public void setNomeMoeda(String nomeMoeda) {
        this.nomeMoeda = nomeMoeda;
    }

    public Double getFechamento() {
        return fechamento;
    }

    public void setFechamento(Double fechamento) {
        this.fechamento = fechamento;
    }

    public Double getAlta() {
        return alta;
    }

    public void setAlta(Double alta) {
        this.alta = alta;
    }

    public Double getBaixa() {
        return baixa;
    }

    public void setBaixa(Double baixa) {
        this.baixa = baixa;
    }

    public Double getVariacao() {
        return variacao;
    }

    public void setVariacao(Double variacao) {
        this.variacao = variacao;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }
}

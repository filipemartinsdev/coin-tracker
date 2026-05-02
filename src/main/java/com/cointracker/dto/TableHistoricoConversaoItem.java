package com.cointracker.dto;

public class TableHistoricoConversaoItem {
    public Double valor;
    public String de;
    public String para;
    public Double resultado;

    public TableHistoricoConversaoItem(Double valor, String de, String para, Double resultado) {
        this.valor = valor;
        this.de = de;
        this.para = para;
        this.resultado = resultado;
    }

    public Double getValor() {
        return valor;
    }

    public String getDe() {
        return de;
    }

    public String getPara() {
        return para;
    }

    public Double getResultado() {
        return resultado;
    }
}

package com.cointracker.dto;

public class TableHistoricoConversaoItem {
    public Integer quantidade;
    public String de;
    public String para;
    public Double resultado;

    public TableHistoricoConversaoItem(Integer quantidade, String de, String para, Double resultado) {
        this.quantidade = quantidade;
        this.de = de;
        this.para = para;
        this.resultado = resultado;
    }

    public Integer getQuantidade() {
        return quantidade;
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

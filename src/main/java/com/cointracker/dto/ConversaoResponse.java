package com.cointracker.dto;

public record ConversaoResponse (
        ConversaoData conversao
) {

    public static class ConversaoData {
        public String code;
        public String codein;
        public String name;
        public String high;
        public String low;
        public String varBid;
        public String pctChange;
        public String bid;
        public String ask;
        public String timestamp;
        public String create_date;
    }
}
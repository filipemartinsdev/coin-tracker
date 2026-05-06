package com.cointracker;

public class Env {
    private static String chaveAPI = null;

    public static String getChaveAPI() {
        return chaveAPI;
    }

    public static void setChaveAPI(String chaveAPI) {
        Env.chaveAPI = chaveAPI;
    }
}

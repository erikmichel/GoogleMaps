package com.example.uf2progmulti.Model;

public class SunriseApi {
    public String status;
    public ModelResults results;

    public SunriseApi() {
    }

    public SunriseApi(String status, ModelResults results) {
        this.status = status;
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public ModelResults getResults() {
        return results;
    }
}

package com.example.uf2progmulti.Model;

public class ModelResults {
    public String sunrise;
    public String sunset;
    public String solar_noon;
    public String day_length;
    public String civil_twilight_begin;
    public String civil_twilight_end;
    public String nautical_twilight_begin;
    public String nautical_twilight_end;
    public String astronomical_twilight_begin;
    public String astronomical_twilight_end;

    public ModelResults(String sunrise, String sunset, String solar_noon, String day_length, String civil_twilight_begin, String civil_twilight_end, String nautical_twilight_begin, String nautical_twilight_end, String astronomical_twilight_begin, String astronomical_twilight_end) {
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.solar_noon = solar_noon;
        this.day_length = day_length;
        this.civil_twilight_begin = civil_twilight_begin;
        this.civil_twilight_end = civil_twilight_end;
        this.nautical_twilight_begin = nautical_twilight_begin;
        this.nautical_twilight_end = nautical_twilight_end;
        this.astronomical_twilight_begin = astronomical_twilight_begin;
        this.astronomical_twilight_end = astronomical_twilight_end;
    }

    public String getSunrise() {
        return sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public String getSolar_noon() {
        return solar_noon;
    }

    public String getDay_length() {
        return day_length;
    }

    public String getCivil_twilight_begin() {
        return civil_twilight_begin;
    }

    public String getCivil_twilight_end() {
        return civil_twilight_end;
    }

    public String getNautical_twilight_begin() {
        return nautical_twilight_begin;
    }

    public String getNautical_twilight_end() {
        return nautical_twilight_end;
    }

    public String getAstronomical_twilight_begin() {
        return astronomical_twilight_begin;
    }

    public String getAstronomical_twilight_end() {
        return astronomical_twilight_end;
    }
}

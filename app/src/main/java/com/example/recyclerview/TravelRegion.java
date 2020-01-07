package com.example.recyclerview;

import java.util.ArrayList;

public class TravelRegion {
    private String engName;
    private String korName;
    private String continent;
    private ArrayList<String> type;
    private String money;
    private Double latitude;
    private Double longitude;

    public TravelRegion(String engName, String korName, String continent, ArrayList<String> type, String money,
                       Double latitude, Double longitude) {
        this.engName = engName;
        this.korName = korName;
        this.continent = continent;
        this.type = type;
        this.money = money;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getEngName() { return this.engName; }
    public String getKorName() { return this.korName; }
    public String getContinent() { return this.continent; }
    public ArrayList<String> getType() { return this.type; }
    public String getMoney() { return this.money; }
    public Double getLatitude() { return this.latitude; }
    public Double getLongitude() { return this.longitude; }

    public void setEngName(String string) { this.engName = string; }
    public void setKorName(String string) { this.korName = string; }
    public void setContinent(String string) { this.continent = string; }
    public void setType(ArrayList<String> arrayList) { this.type = arrayList; }
    public void setMoney(String string) { this.money = string; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
}
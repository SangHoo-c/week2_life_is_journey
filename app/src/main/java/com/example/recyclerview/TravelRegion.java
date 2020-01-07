package com.example.recyclerview;

import java.util.ArrayList;

public class TravelRegion {
    private String engName;
    private String korName;
    private String continent;
    private ArrayList<String> type;
    private String money;
    private Long latitude;
    private Long longitude;

    public TravelRegion(String engName, String korName, String continent, ArrayList<String> type, String money,
                       Long latitude, Long longitude) {
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
    public Long getLatitude() { return this.latitude; }
    public Long getLongitude() { return this.longitude; }

    public void setEngName(String string) { this.engName = string; }
    public void setKorName(String string) { this.korName = string; }
    public void setContinent(String string) { this.continent = string; }
    public void setType(ArrayList<String> arrayList) { this.type = arrayList; }
    public void setMoney(String string) { this.money = string; }
    public void setLatitude(Long latitude) { this.latitude = latitude; }
    public void setLongitude(Long longitude) { this.longitude = longitude; }
}
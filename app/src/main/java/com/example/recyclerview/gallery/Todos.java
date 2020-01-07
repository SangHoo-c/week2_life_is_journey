package com.example.recyclerview.gallery;

public class Todos {
    String id;
    String country;
    String activity;
    String food;
    String sight;
    String rest;
    String money;
    String content;
    String content2;
    String like;
    String content3;

    public Todos(String id, String country, String activity, String food, String sight, String rest, String money, String content, String content2, String like, String content3) {
        this.id = id;
        this.country = country;
        this.activity = activity;
        this.food = food;
        this.sight = sight;
        this.rest = rest;
        this.money = money;
        this.content = content;
        this.content2 = content2;
        this.content3 = content3;
        this.like = like;
    }

    public String getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }

    public String getActivity() {
        return activity;
    }

    public String getFood() {
        return food;
    }

    public String getSight() {
        return sight;
    }

    public String getRest() {
        return rest;
    }

    public String getMoney() {
        return money;
    }

    public String getContent() {
        return content;
    }

    public String getContent2() {
        return content2;
    }

    public String getLike() {
        return like;
    }

    public String getContent3(){return content3;}
}


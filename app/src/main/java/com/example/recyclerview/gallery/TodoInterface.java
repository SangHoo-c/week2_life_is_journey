package com.example.recyclerview.gallery;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TodoInterface {
    String URL = "http://192.249.19.251:8980/";


    @GET("todos/id/{todoid}")
    Call<Todos> getData(@Path("todoid") String todoid);

    @PUT("todos/id/{id}")
    Call<Todos> getUpdate(@Path("id") String id);


}

package com.example.smartmediaschedular;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    //65,124
    //https://wtpaddu.000webhostapp.com/Android/Smart_Media_Scheduler/
    //private static final String BASE_URL="https://wtpaddu.000webhostapp.com/Android/Smart_Media_Scheduler/";

    private static final String BASE_URL="http://192.168.43.124/Android/Smart_Media_Scheduler/";
    private static ApiClient apiClient;
    private static Retrofit retrofit;

    private ApiClient()
    {
        retrofit=new Retrofit.Builder().baseUrl(ApiClient.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

    }

    public static synchronized ApiClient getInstance()
    {
        if (apiClient==null)
        {
            apiClient=new ApiClient();
        }
        return apiClient;
    }

    public ApiInterface getApi()
    {
        return retrofit.create(ApiInterface.class);
    }
}
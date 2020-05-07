package com.example.smartmediaschedular;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    //SmartMediaSchedulerApi.php

    @FormUrlEncoded
    @POST("SmartMediaSchedulerApi.php")
    Call<List<status>> insert_email(
            @Field("choice") String choice,
            @Field("sender_name") String sender_name,
            @Field("receiver_email") String receiver_email,
            @Field("attachment") String attachment,
            @Field("subject") String subject,
            @Field("schedule_date") String schedule_date,
            @Field("schedule_time") String schedule_time,
            @Field("status") int status,
            @Field("media") String media
    );


    @FormUrlEncoded
    @POST("SmartMediaSchedulerApi.php")
    Call<List<status>> insert_message(
            @Field("choice") String choice,
            @Field("sender_name") String sender_name,
            @Field("receiver_contact") String receiver_contact,
            @Field("message") String message,
            @Field("schedule_date") String schedule_date,
            @Field("schedule_time") String schedule_time,
            @Field("status") int status

    );

    @FormUrlEncoded
    @POST("SmartMediaSchedulerApi.php")
    Call<List<Pending_completed>> getPending(
            @Field("choice") String choice
    );

    @FormUrlEncoded
    @POST("SmartMediaSchedulerApi.php")
    Call<List<Pending_completed>> getCompleted(
            @Field("choice") String choice
    );

    @FormUrlEncoded
    @POST("SmartMediaSchedulerApi.php")
    Call<List<messageData>> getMessage(
            @Field("choice") String choice,
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("SmartMediaSchedulerApi.php")
    Call<List<emailData>> getEmail(
            @Field("choice") String choice,
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("SmartMediaSchedulerApi.php")
    Call<List<status>> deleteEmail(
            @Field("choice") String choice,
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("SmartMediaSchedulerApi.php")
    Call<List<status>> deleteMessage(
            @Field("choice") String choice,
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("SmartMediaSchedulerApi.php")
    Call<List<status>> insertSupport(
            @Field("choice") String choice,

            @Field("support") String support
    );

    @FormUrlEncoded
    @POST("SmartMediaSchedulerApi.php")
    Call<List<status>> insertRateus(
            @Field("choice") String choice,

            @Field("ratecnt") String ratecnt,
            @Field("rateus") String rateus

    );

}

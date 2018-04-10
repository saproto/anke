package com.proto.service;

import com.proto.oauth.SAProtoClient;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.OkHttpClient;
import okhttp3.Interceptor;
import okhttp3.logging.*;

/**
 * Created by Dennis on 03/04/2018.
 */

public class RetroFitServiceGenerator {

    private static final String baseUrl = "https://www.proto.utwente.nl/";

    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = retrofitBuilder.build();

    private static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    private static OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

    public static <S> S createService(Class<S> serviceClass){
        if(!httpClientBuilder.interceptors().contains(loggingInterceptor)) {
            httpClientBuilder.addInterceptor(loggingInterceptor);
            retrofitBuilder = retrofitBuilder.client(httpClientBuilder.build());
            retrofit = retrofitBuilder.build();


        }
        return retrofit.create(serviceClass);

    }
    //final SAProtoClient service = retrofit.create(SAProtoClient.class);

}

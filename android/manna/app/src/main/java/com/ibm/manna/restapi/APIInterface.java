package com.ibm.manna.restapi;

import com.ibm.manna.pojo.AuthenticationRequest;
import com.ibm.manna.pojo.AuthenticationResponse;
import com.ibm.manna.pojo.CreateRequestResponse;
import com.ibm.manna.pojo.Requests;
import com.ibm.manna.pojo.RequestsResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIInterface {
    @POST("preflight")
    Call<AuthenticationResponse> preflight(@Body AuthenticationRequest request_schema);

    @POST("validateOTP")
    Call<AuthenticationResponse> validateOTP(@Body AuthenticationRequest request_schema);

    @POST("createRequest")
    Call<CreateRequestResponse> createRequest(@Body Requests requests);

    @GET("fetchNearest")
    Call<RequestsResponse> fetchNearest(@Query("longitude") double longitude, @Query("latitude") double latitude);

    @GET("myRequests")
    Call<RequestsResponse> getMyRequests(@Query("mannaID") String mannaID);

}

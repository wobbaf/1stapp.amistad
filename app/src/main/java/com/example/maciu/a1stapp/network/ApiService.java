package com.example.maciu.a1stapp.network;

import com.example.maciu.a1stapp.object.MyResponse;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by maciu on 04.07.2017.
 */
public interface ApiService {
    @GET ("v2/route/index/full/0")
    Call<MyResponse> getResponse();
}

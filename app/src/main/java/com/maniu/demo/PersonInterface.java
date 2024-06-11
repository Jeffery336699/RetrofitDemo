package com.maniu.demo;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface PersonInterface {

    @GET("webapp.php?page=1&size=2")
    Call<ResponseBody> getPersonInfo();
}

package com.maniu.demo;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface MyService {

    @GET("list.php")
    Observable<List<PersonInfo>> getList(@Query("page") String page, @Query("size") String size);
}

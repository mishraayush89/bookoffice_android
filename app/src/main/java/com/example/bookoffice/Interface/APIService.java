package com.example.bookoffice.Interface;

import com.example.bookoffice.Notifications.MyResponse;
import com.example.bookoffice.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAM4EHaoc:APA91bG4-Gjp4LaQFdgAVY8ljiZvyevPMwpnUBacrDH3gD-Sgap7TkftER44fTeAviR28hKpZOUNzB_kw3Dn1MRl0rp6PRWqu4eW8tVdF8etJE-c5BVS5EYpId5r0zhKW3wNCYtZ8f5r"
    }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);



}

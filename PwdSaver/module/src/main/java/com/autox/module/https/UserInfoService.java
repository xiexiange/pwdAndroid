package com.autox.module.https;


import com.autox.module.https.entity.GetPlatMsgReception;
import com.autox.module.https.entity.SendVerifyReception;
import com.autox.module.https.entity.UploadPlatMsgReception;
import com.autox.module.https.entity.VerifyReception;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserInfoService {
    @GET("PwdServer/SendCode")
    Call<SendVerifyReception> sendVerifyCode(@Query("phone") String phone);

    @GET("PwdServer/VerifyCode")
    Call<VerifyReception> verifyCode(@Query("phone") String phone, @Query("uuid") String uuid, @Query("code") String code);

    @POST("PwdServer/UploadPwd")
    Call<UploadPlatMsgReception> uploadPlat(@Query("phone") String phone,
                                            @Query("plat") String plat,
                                            @Query("account") String account,
                                            @Query("pwd") String pwd);

    @GET("PwdServer/getPwd")
    Call<GetPlatMsgReception> getPlat(@Query("phone") String phone,
                                      @Query("plat") String plat,
                                      @Query("account") String account);

}

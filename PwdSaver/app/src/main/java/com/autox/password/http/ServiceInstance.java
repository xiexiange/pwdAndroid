package com.autox.password.http;

import com.autox.password.http.entity.GetPlatMsgReception;
import com.autox.password.http.entity.UploadPlatMsgReception;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceInstance {
    private static ServiceInstance sInstance;
    private ServiceInstance() {

    }

    public static ServiceInstance getInstance() {
        if (sInstance == null) {
            synchronized (ServiceInstance.class) {
                if (sInstance == null) {
                    sInstance = new ServiceInstance();
                }
            }
        }
        return sInstance;
    }

    private UserInfoService getUserInfoService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HttpConstant.SERVER_URL_HTTP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(UserInfoService.class);
    }

    public void getPlatMsg(String md5Phone, String md5Plat, String encodeAccount, final GetPlatCallback callback) {
        Call<GetPlatMsgReception> call = getInstance().getUserInfoService().getPlat(md5Phone, md5Plat, encodeAccount);
        call.enqueue(new retrofit2.Callback<GetPlatMsgReception>() {
            @Override
            public void onResponse(Call<GetPlatMsgReception> call, Response<GetPlatMsgReception> response) {
                GetPlatMsgReception reception = response.body();
                if(reception == null) {
                    if (callback != null) {
                        callback.onFailed();
                    }
                    return;
                }
                if (reception.code == 200) {
                    if (callback != null) {
                        callback.onSuccess(reception);
                    }
                } else {
                    if (callback != null) {
                        callback.onFailed();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetPlatMsgReception> call, Throwable t) {

            }
        });
    }

    public void uploadPlatMsg(String md5Phone, String md5Plat, String encodeAccount, String encodePwd, final Callback callback) {
        Call<UploadPlatMsgReception> call = getInstance().getUserInfoService().uploadPlat(md5Phone, md5Plat, encodeAccount, encodePwd);
        call.enqueue(new retrofit2.Callback<UploadPlatMsgReception>() {
            @Override
            public void onResponse(Call<UploadPlatMsgReception> call, Response<UploadPlatMsgReception> response) {
                UploadPlatMsgReception reception = response.body();
                if (reception == null) {
                    return;
                }
                if (reception.code == 200) {
                    if (callback != null) {
                       callback.onSuccess(reception);
                    }
                } else {
                    if (callback != null) {
                        callback.onFailed();
                    }
                }
            }

            @Override
            public void onFailure(Call<UploadPlatMsgReception> call, Throwable t) {
                if (callback != null) {
                    callback.onFailed();
                }
            }
        });
    }

    public interface Callback {
        void onSuccess(UploadPlatMsgReception reception);
        void onFailed();
    }

    public interface GetPlatCallback {
        void onSuccess(GetPlatMsgReception reception);
        void onFailed();
    }

}

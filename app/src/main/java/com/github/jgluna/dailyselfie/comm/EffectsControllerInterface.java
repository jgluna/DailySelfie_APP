package com.github.jgluna.dailyselfie.comm;

import com.github.jgluna.dailyselfie.model.EffectsRequestWrapper;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.mime.TypedFile;

public interface EffectsControllerInterface {

    String BASE_PATH = "/effect";
    String IMAGE_PARAMETER = "image";
    String WRAPPER_PARAMETER = "wrapper";


    @Multipart
    @POST(BASE_PATH)
    Void applyEffect(@Part(IMAGE_PARAMETER) TypedFile image,
                     @Part(WRAPPER_PARAMETER) EffectsRequestWrapper effectsWrapper,
                     Callback<Response> callback);

}

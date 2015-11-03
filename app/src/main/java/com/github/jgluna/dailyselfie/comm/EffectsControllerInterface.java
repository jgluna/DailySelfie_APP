package com.github.jgluna.dailyselfie.comm;

import java.util.List;

import retrofit.client.Response;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Query;
import retrofit.http.Streaming;
import retrofit.mime.TypedFile;

public interface EffectsControllerInterface {

    String BASE_PATH = "/effect";
    String IMAGE_PARAMETER = "image";
    String EFFECTS_PARAMETER = "effects";

    @Streaming
    @Multipart
    @POST(BASE_PATH)
    Response applyEffect(@Part(IMAGE_PARAMETER) TypedFile image,
                         @Query(EFFECTS_PARAMETER) List<String> effects);

}

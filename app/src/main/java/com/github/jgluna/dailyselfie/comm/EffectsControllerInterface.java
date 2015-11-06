package com.github.jgluna.dailyselfie.comm;

import java.util.List;

import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Query;
import retrofit.http.Streaming;
import retrofit.mime.TypedFile;

public interface EffectsControllerInterface {

    String BASE_PATH = "/effect";
    String LOGIN_PATH = "/login";
    String LOGOUT_PATH = "/logout";
    String IMAGE_PARAMETER = "image";
    String EFFECTS_PARAMETER = "effects";
    String PASSWORD_PARAMETER = "password";
    String USERNAME_PARAMETER = "username";

    @Streaming
    @Multipart
    @POST(BASE_PATH + "/apply")
    Response applyEffect(@Part(IMAGE_PARAMETER) TypedFile image,
                         @Query(EFFECTS_PARAMETER) List<String> effects);

    @FormUrlEncoded
    @POST(LOGIN_PATH)
    Void login(@Field(USERNAME_PARAMETER) String username, @Field(PASSWORD_PARAMETER) String pass);

    @GET(LOGOUT_PATH)
    Void logout();

}

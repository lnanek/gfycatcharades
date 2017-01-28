package name.nanek.gfycathack.network;

import java.util.List;

import name.nanek.gfycathack.models.ClientCredentialsRequest;
import name.nanek.gfycathack.models.ClientCredentialsResponse;
import name.nanek.gfycathack.models.PrepareUploadResponse;
import name.nanek.gfycathack.models.TrendingResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Calls Gyfcat API.
 * <p>
 * Created by lnanek on 1/28/17.
 */

public interface GfycatService {

    @POST("oauth/token")
    Call<ClientCredentialsResponse> getCredentials(
            @Body ClientCredentialsRequest request);

    @GET("tags/trending")
    Call<List<String>> trendingTags(
            @Header("Authorization") String token,
            @Query("tagCount") int tagCount);

    @GET("gfycats/trending")
    Call<TrendingResponse> trendingGfycats(
            @Header("Authorization") String token,
            @Query("count") int count,
            @Query("cursor") String cursor);

    @Headers("Content-Type: application/json")
    @POST("gfycats")
    Call<PrepareUploadResponse> prepareUpload(
            @Header("Authorization") String token);

}

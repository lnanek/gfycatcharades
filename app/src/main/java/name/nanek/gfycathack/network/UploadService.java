package name.nanek.gfycathack.network;

import java.util.List;

import name.nanek.gfycathack.models.ClientCredentialsRequest;
import name.nanek.gfycathack.models.ClientCredentialsResponse;
import name.nanek.gfycathack.models.PrepareUploadResponse;
import name.nanek.gfycathack.models.TrendingResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Calls Gyfcat API.
 *
 * Created by lnanek on 1/28/17.
 */

public interface UploadService {

    @POST("oauth/token")
    Call<ClientCredentialsResponse> getCredentials(
            @Body ClientCredentialsRequest request);

    @GET("tags/trending")
    Call<List<String>> trendingTags(
            @Query("tagCount") int tagCount);

    @GET("gfycats/trending")
    Call<TrendingResponse> trendingGfycats(
            @Query("count") int count,
            @Query("cursor") String cursor);

    @Headers("Content-Type: application/json")
    @POST("gfycats")
    Call<PrepareUploadResponse> prepareUpload();

}

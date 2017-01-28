package name.nanek.gfycathack.network;

import java.util.List;

import name.nanek.gfycathack.models.TrendingResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Calls Gyfcat API.
 *
 * Created by lnanek on 1/28/17.
 */

public interface GfycatService {

    @GET("tags/trending")
    Call<List<String>> trendingTags(@Query("tagCount") int tagCount);

    @GET("gfycats/trending")
    Call<TrendingResponse> trendingGfycats(
            @Query("count") int count,
            @Query("cursor") String cursor);

}

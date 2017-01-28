package name.nanek.gfycathack;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Calls Gyfcat API.
 *
 * Created by lnanek on 1/28/17.
 */

public interface GfycatService {

    @GET("v1test/tags/trending")
    Call<List<String>> trendingTags(@Query("tagCount") int tagCount);

    @GET("v1test/gfycats/trending")
    Call<TrendingResponse> trendingGfycats(@Query("count") int count);

}

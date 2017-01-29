package name.nanek.gfycathack.network;

import name.nanek.gfycathack.models.GetGfyResponse;
import name.nanek.gfycathack.models.Gfycat;
import name.nanek.gfycathack.models.TrendingResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Header;
import retrofit2.http.Path;

/**
 * Created by lnanek on 1/28/17.
 */

public class CuratedImageService {

    private String nextGifCursor;

    private static int currentImageIndex;

    private static final String[] curatedGfycatNames = new String[] {
            // dogs acting guilty :
            "QuickOldAnnashummingbird",

            // warrior dancing mom:
            "ScaredWelllitBlackandtancoonhound",

            // Conan O’brian dancing jazz :
            "OccasionalRevolvingBarebirdbat"
    };

    public void getGfy(
            GfycatService service,
            GfycatService testService,
            @Header("Authorization") String token,
            final Callback<Gfycat> callback) {

        // If first 3, return curated item
        if (currentImageIndex < curatedGfycatNames.length) {
            Call<GetGfyResponse> call = testService.getGfy(
                    token, curatedGfycatNames[currentImageIndex]);
            currentImageIndex++;
            call.enqueue(new Callback<GetGfyResponse>() {
                @Override
                public void onResponse(Call<GetGfyResponse> call, Response<GetGfyResponse> response) {
                    callback.onResponse(null, Response.success(response.body().gfyItem));
                }

                @Override
                public void onFailure(Call<GetGfyResponse> call, Throwable t) {
                    callback.onFailure(null, t);
                }
            });
            return;
        }

        // Otherwise return trending item
        Call<TrendingResponse> tags = service.trendingGfycats(token, 10, nextGifCursor);
        tags.enqueue(new Callback<TrendingResponse>() {
            @Override
            public void onResponse(Call<TrendingResponse> call, Response<TrendingResponse> response) {
                nextGifCursor = response.body().cursor;
                callback.onResponse(null, Response.success(response.body().gfycats.get(0)));
            }

            @Override
            public void onFailure(Call<TrendingResponse> call, Throwable t) {
                callback.onFailure(null, t);
            }
        });

    }

}

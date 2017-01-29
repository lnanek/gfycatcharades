package name.nanek.gfycathack.network;

import android.util.Log;

import name.nanek.gfycathack.models.ClientCredentialsRequest;
import name.nanek.gfycathack.models.ClientCredentialsResponse;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lnanek on 1/28/17.
 */

public class GfycatServiceFactory {

    private static final String TAG = GfycatServiceFactory.class.getSimpleName();

    private static GfycatService INSTANCE = create("https://api.gfycat.com/v1/");

    private static GfycatService TEST_INSTANCE = create("https://api.gfycat.com/v1test/");

    public static GfycatService get() {
        return INSTANCE;
    }

    public static GfycatService getTest() {
        return TEST_INSTANCE;
    }

    private static GfycatService create(String baseUrl) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(HttpClientFactory.INSTANCE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GfycatService service = retrofit.create(GfycatService.class);

        return service;
    }
}

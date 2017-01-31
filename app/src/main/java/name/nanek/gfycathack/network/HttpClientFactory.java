package name.nanek.gfycathack.network;

import java.io.File;
import java.util.concurrent.TimeUnit;

import name.nanek.gfycathack.imageloading.DispatchingProgressListener;
import name.nanek.gfycathack.imageloading.OkHttpProgressGlideModule;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static name.nanek.gfycathack.imageloading.OkHttpProgressGlideModule.createInterceptor;

/**
 * Created by lnanek on 1/29/17.
 */

public class HttpClientFactory {

    private static final String TAG = GfycatServiceFactory.class.getSimpleName();

    public static final OkHttpClient INSTANCE = create();

    private static final String CACHE_DIR = "/sdcard/GfycatHackCache";

    private static Cache createCacheForOkHTTP() {
        return new Cache(new File(CACHE_DIR), 1024 * 1024 * 100); // 100 MiB
    }

    private static OkHttpClient create() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                //.cache(createCacheForOkHTTP())
                .followRedirects(true)
                .followSslRedirects(true)
                .writeTimeout(5 * 60, TimeUnit.SECONDS)
                .readTimeout(5 * 60, TimeUnit.SECONDS)
                .connectTimeout(5 * 60, TimeUnit.SECONDS)
                .addInterceptor(interceptor)

                .addInterceptor(createInterceptor(new DispatchingProgressListener()))

                .build();

        return client;
    }

}

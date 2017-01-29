package name.nanek.gfycathack.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lnanek on 1/28/17.
 */

public class UploadServiceFactory {

    private static final String TAG = UploadServiceFactory.class.getSimpleName();

    public static UploadService create(final String baseUrl) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl(baseUrl)
                .baseUrl("https://filedrop.gfycat.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UploadService service = retrofit.create(UploadService.class);

        return service;
    }
}

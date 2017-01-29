package name.nanek.gfycathack.imageloading;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import name.nanek.gfycathack.network.HttpClientFactory;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

// TODO add <meta-data android:value="GlideModule" android:name="....OkHttpProgressGlideModule" />
// TODO add <meta-data android:value="GlideModule" tools:node="remove" android:name="com.bumptech.glide.integration.okhttp.OkHttpGlideModule" />
// or not use 'okhttp@aar' in Gradle depdendencies
public class OkHttpProgressGlideModule implements GlideModule {
    @Override public void applyOptions(Context context, GlideBuilder builder) {	}

    /*
    @Override public void registerComponents(Context context, Glide glide) {
        OkHttpClient client = new OkHttpClient();
        client.networkInterceptors().add(createInterceptor(new DispatchingProgressListener()));
        glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(client));
    }
    */

    @Override
    public void registerComponents(Context context, Glide glide) {
        //OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //OkHttpClient client = builder.build();

        OkHttpClient client = HttpClientFactory.INSTANCE;

        //builder.addInterceptor(createInterceptor(new DispatchingProgressListener()));
        glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(client));
    }

    public static Interceptor createInterceptor(final ResponseProgressListener listener) {
        return new Interceptor() {
            @Override public Response intercept(Interceptor.Chain chain) throws IOException {
                Request request = chain.request();
                Response response = chain.proceed(request);

                /*
                return response.newBuilder()
                        .body(new OkHttpProgressResponseBody(request.httpUrl(), response.body(), listener))
                        .build();
                        */
                return response.newBuilder().body(new OkHttpProgressResponseBody(request.url(), response.body(), listener)).build();
            }
        };
    }

    public interface UIProgressListener {
        void onProgress(long bytesRead, long expectedLength);
        /**
         * Control how often the listener needs an update. 0% and 100% will always be dispatched.
         * @return in percentage (0.2 = call {@link #onProgress} around every 0.2 percent of progress)
         */
        float getGranualityPercentage();
    }

    public static void forget(String url) {
        DispatchingProgressListener.forget(url);
    }
    public static void expect(String url, UIProgressListener listener) {
        DispatchingProgressListener.expect(url, listener);
    }

    private static class OkHttpProgressResponseBody extends ResponseBody {
        private final HttpUrl url;
        private final ResponseBody responseBody;
        private final ResponseProgressListener progressListener;
        private BufferedSource bufferedSource;

        OkHttpProgressResponseBody(HttpUrl url, ResponseBody responseBody,
                                   ResponseProgressListener progressListener) {
            this.url = url;
            this.responseBody = responseBody;
            this.progressListener = progressListener;
        }

        @Override public MediaType contentType() {
            return responseBody.contentType();
        }

        @Override public long contentLength() /*throws IOException*/ {
            return responseBody.contentLength();
        }

        @Override public BufferedSource source() /*throws IOException*/ {
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        private Source source(Source source) {
            return new ForwardingSource(source) {
                long totalBytesRead = 0L;
                @Override public long read(Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    long fullLength = responseBody.contentLength();
                    if (bytesRead == -1) { // this source is exhausted
                        totalBytesRead = fullLength;
                    } else {
                        totalBytesRead += bytesRead;
                    }
                    progressListener.update(url, totalBytesRead, fullLength);
                    return bytesRead;
                }
            };
        }
    }
}
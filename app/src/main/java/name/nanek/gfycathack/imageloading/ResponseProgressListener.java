package name.nanek.gfycathack.imageloading;

import okhttp3.HttpUrl;

/**
 * Created by lnanek on 1/29/17.
 */

public interface ResponseProgressListener {
    void update(HttpUrl url, long bytesRead, long contentLength);
}
package name.nanek.gfycathack.network;

import java.util.List;

import name.nanek.gfycathack.models.ClientCredentialsRequest;
import name.nanek.gfycathack.models.ClientCredentialsResponse;
import name.nanek.gfycathack.models.PrepareUploadResponse;
import name.nanek.gfycathack.models.TrendingResponse;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Calls Gyfcat API.
 *
 * Created by lnanek on 1/28/17.
 */

public interface UploadService {

    @PUT("{gfyname}")
    Call<Void> uploadPhoto(
            // XXX gives 'Unsupported Authorization Type' error
            //@Header("Authorization") String accessToken,

            @Path("gfyname") String gfyname,
            @Body RequestBody photo);

}

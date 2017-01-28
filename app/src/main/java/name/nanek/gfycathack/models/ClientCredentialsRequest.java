package name.nanek.gfycathack.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lnanek on 1/28/17.
 */

public class ClientCredentialsRequest {

    @SerializedName("client_id")
    public String clientId = "2_rWN--0";

    @SerializedName("client_secret")
    public String clientSecret = "b1b3HZxb6PyOKP9-nc8DVmw0NEYk5nwLx8ixA3YResPzB9fOml-k1DQDntcmFIOp";

    @SerializedName("grant_type")
    public String grantType = "client_credentials";


}

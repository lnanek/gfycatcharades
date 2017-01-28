package name.nanek.gfycathack.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lnanek on 1/28/17.
 */

public class ClientCredentialsResponse {

    @SerializedName("token_type")
    public String tokenType; // e.g. "bearer"

    public String scope; // e.g. ""

    @SerializedName("expires_in")
    public String expiresIn; // e.g. 3600

    @SerializedName("access_token")
    public String accessToken; // e.g. "eyJhbGciOiJIUzIzNiIsfnR5cCI6IkpXVCJ9.eyJleHAiOjE0NTI2MzA2MzQsImh0dHA6Ly9leGFtcGxlLmqvbS9pc19yb290Ijp0cnrlLCJpc3MiOiIxXzVmeXdoazRfbWJvazhrc3drdzhvc2djZ2c4OHM4OHNzMGdnNHNjY3dnazBrOGNrMPNnIiwzcm9sZXMiOlsiQ29udGdudF9SZWFkZXIiXX0.I2z4Wb6M_Yb26ux-K6vMNrNcySxA1TvRYopXuhle6yI"

    @Override
    public String toString() {
        return "ClientCredentialsResponse{" +
                "tokenType='" + tokenType + '\'' +
                ", scope='" + scope + '\'' +
                ", expiresIn='" + expiresIn + '\'' +
                ", accessToken='" + accessToken + '\'' +
                '}';
    }
}

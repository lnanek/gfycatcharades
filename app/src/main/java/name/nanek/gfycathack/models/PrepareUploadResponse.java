package name.nanek.gfycathack.models;

/**
 * Created by lnanek on 1/28/17.
 */

public class PrepareUploadResponse {

    public boolean isOk; // e.g. true

    public String gfyname; // e.g. "FinishedSillyBlowfish"

    public String secret; // e.g. "hvy39kh10xoe0zfr"

    public String uploadType; // e.g. "filedrop.gfycat.com"

    @Override
    public String toString() {
        return "PrepareUploadResponse{" +
                "isOk=" + isOk +
                ", gfyname='" + gfyname + '\'' +
                ", secret='" + secret + '\'' +
                ", uploadType='" + uploadType + '\'' +
                '}';
    }
}

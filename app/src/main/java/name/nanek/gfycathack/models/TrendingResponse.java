package name.nanek.gfycathack.models;

import java.util.List;

/**
 * A trending API call response from the API.
 *
 * Created by lnanek on 1/28/17.
 */

public class TrendingResponse {

    public String tag;

    public String cursor;

    public String digest;

    public List<Gfycat> gfycats;

    public List<Gfycat> newGfycats;

}

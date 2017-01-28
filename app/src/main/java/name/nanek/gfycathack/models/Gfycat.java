package name.nanek.gfycathack.models;

import java.util.List;

/**
 * A Gfycat returned by the API.
 *
 * Created by lnanek on 1/28/17.
 */

public class Gfycat {

    public String gfyId; // e.g. "bleaksimplisticelkhound",
    public String gfyName; // e.g. "BleakSimplisticElkhound",
    public String gfyNumber; // e.g. "463614577",
    public String webmUrl; // e.g. "https://zippy.gfycat.com/BleakSimplisticElkhound.webm",
    public String gifUrl; // e.g. "https://giant.gfycat.com/BleakSimplisticElkhound.gif",
    public String mobileUrl; // e.g. "https://thumbs.gfycat.com/BleakSimplisticElkhound-mobile.mp4",
    public String mobilePosterUrl; // e.g. "https://thumbs.gfycat.com/BleakSimplisticElkhound-mobile.jpg",
    public String posterUrl; // e.g. "https://thumbs.gfycat.com/BleakSimplisticElkhound-poster.jpg",
    public String thumb360Url; // e.g. "https://thumbs.gfycat.com/BleakSimplisticElkhound-360.mp4",
    public String thumb360PosterUrl; // e.g. "https://thumbs.gfycat.com/BleakSimplisticElkhound-thumb360.jpg",
    public String thumb100PosterUrl; // e.g. "https://thumbs.gfycat.com/BleakSimplisticElkhound-thumb100.jpg",
    public String max5mbGif; // e.g. "https://thumbs.gfycat.com/BleakSimplisticElkhound-size_restricted.gif",
    public String max2mbGif; // e.g. "https://thumbs.gfycat.com/BleakSimplisticElkhound-small.gif",
    public String mjpgUrl; // e.g. "https://thumbs.gfycat.com/BleakSimplisticElkhound.mjpg",
    public int width; // e.g. 768,
    public int height; // e.g. 622,
    public String avgColor; // e.g. "#0D1B1C",
    public int frameRate; // e.g. 29,
    public int numFrames; // e.g. 301,
    public int mp4Size; // e.g. 3003268,
    public int webmSize; // e.g. 734999,
    public int gifSize; // e.g. 11946428,
    public int source; // e.g. 1,
    public int createDate; // e.g. 1485485077,
    public String nsfw; // e.g. "0",
    public String mp4Url; // e.g. "https://fat.gfycat.com/BleakSimplisticElkhound.mp4",
    public String likes; // e.g. "1",
    public int published; // e.g. 1,
    public int dislikes; // e.g. 0,
    public String extraLemmas; // e.g. "",
    public String md5; // e.g. "468466cf3f9c940348b94fe2ebefa38e",
    public int views; // e.g. 1,
    public List<String> tags; // e.g. ["photoshopbattles"]
    public String userName; // e.g. "anonymous",
    public String title; // e.g. "Untitled",
    public String description; // e.g. "",
//public String languageCategories; // e.g. null

    @Override
    public String toString() {
        return "Gfycat{" +
                "gfyName='" + gfyName + '\'' +
                "\ngifUrl='" + gifUrl + '\'' +
                "\ntags=" + tags +
                "\nuserName='" + userName + '\'' +
                "\ntitle='" + title + '\'' +
                "\ndescription='" + description + '\'' +
                '}';
    }
}

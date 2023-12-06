package android2.genzelody;

import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class PlayListItem {
    @SerializedName("id")

    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("images")
    private JsonArray imagesArray;

    @SerializedName("tracks")
    private JSONObject tracksObject;


    public String getName() {
        return name;
    }
    public String getId() {
        return id;
    }


}

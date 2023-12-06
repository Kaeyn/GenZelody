package android2.genzelody;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlaylistResponse {
    @SerializedName("items")
    private List<PlayListItem> items;

    // Add other fields as needed

    public List<PlayListItem> getItems() {
        return items;
    }
}

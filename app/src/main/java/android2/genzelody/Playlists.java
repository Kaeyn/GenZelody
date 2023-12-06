package android2.genzelody;

import java.util.ArrayList;

public class Playlists {
    private String id;
    private String images;
    private String name;
    private ArrayList<Track> tracks;
    private boolean isPublic;

    public Playlists(String id, String images, String name, ArrayList<Track> tracks, boolean isPublic) {
        this.id = id;
        this.images = images;
        this.name = name;
        this.tracks = tracks;
        this.isPublic = isPublic;
    }

    public Playlists(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Track> getTracks() {
        return tracks;
    }

    public void setTracks(ArrayList<Track> tracks) {
        this.tracks = tracks;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

}

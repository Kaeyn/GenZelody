package android2.genzelody;

import java.util.ArrayList;

public class Album {
    private String id;
    private String name;
    private String image;
    private String release_date;
    private ArrayList<Artist> artists;
    private ArrayList<Track> tracks;

    public Album(String id, String name, String image, String release_date, ArrayList<Artist> artists, ArrayList<Track> tracks) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.release_date = release_date;
        this.artists = artists;
        this.tracks = tracks;
    }

    public Album(){

    }
    public Album(String name, String image){
        this.name = name;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public ArrayList<Artist> getArtists() {
        return artists;
    }

    public void setArtists(ArrayList<Artist> artists) {
        this.artists = artists;
    }

    public ArrayList<Track> getTracks() {
        return tracks;
    }

    public void setTracks(ArrayList<Track> tracks) {
        this.tracks = tracks;
    }
}

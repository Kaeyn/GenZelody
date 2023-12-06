package android2.genzelody;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Track  implements Parcelable {

    private String id;
    private String name;
    private String idAlbum;

    private String img;
    private ArrayList<Artist> artists;
    private String preview_url;

    public Track(String id, String name, String idAlbum, String img,ArrayList<Artist> artists, String preview_url) {
        this.id = id;
        this.name = name;
        this.idAlbum = idAlbum;
        this.img = img;
        this.artists = artists;
        this.preview_url = preview_url;
    }

    public Track(){

    }

    protected Track(Parcel in) {
        id = in.readString();
        name = in.readString();
        idAlbum = in.readString();
        img = in.readString();
        preview_url = in.readString();
    }

    public static final Creator<Track> CREATOR = new Creator<Track>() {
        @Override
        public Track createFromParcel(Parcel in) {
            return new Track(in);
        }

        @Override
        public Track[] newArray(int size) {
            return new Track[size];
        }
    };

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

    public ArrayList<Artist> getArtists() {
        return artists;
    }

    public void setArtists(ArrayList<Artist> artists) {
        this.artists = artists;
    }

    public String getPreview_url() {
        return preview_url;
    }

    public void setPreview_url(String preview_url) {
        this.preview_url = preview_url;
    }

    public String getAlbumImageById(String albumId, List<Album> albums) {
        for (Album album : albums) {
            if (album.getId().equals(albumId)) {
                return album.getImage();
            }
        }
        return null;} // Return null if album ID is not found
    public String getIdAlbum() {
        return idAlbum;
    }

    public void setIdAlbum(String idAlbum) {
        this.idAlbum = idAlbum;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(idAlbum);
        dest.writeString(img);
        dest.writeString(preview_url);
    }


}

package android2.genzelody;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Playlists implements Parcelable{
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

    protected Playlists(Parcel in) {
        id = in.readString();
        name = in.readString();
        tracks = in.createTypedArrayList(Track.CREATOR);
    }

    public static final Creator<Playlists> CREATOR = new Creator<Playlists>() {
        @Override
        public Playlists createFromParcel(Parcel in) {
            return new Playlists(in);
        }

        @Override
        public Playlists[] newArray(int size) {
            return new Playlists[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeTypedList(tracks);
    }

//    protected Playlists(Parcel in) {
//        id = in.readString();
//        images = in.readString();
//        name = in.readString();
//        tracks = in.createTypedArrayList(Track.CREATOR);
//        isPublic = in.readByte() != 0;
//    }
//
//    public static final Creator<Playlists> CREATOR = new Creator<Playlists>() {
//        @Override
//        public Playlists createFromParcel(Parcel in) {
//            return new Playlists(in);
//        }
//
//        @Override
//        public Playlists[] newArray(int size) {
//            return new Playlists[size];
//        }
//    };
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(@NonNull Parcel dest, int flags) {
//        dest.writeString(id);
//        dest.writeString(images);
//        dest.writeString(name);
//        dest.writeTypedList(tracks);
//        dest.writeByte((byte) (isPublic ? 1 : 0));
//    }




}

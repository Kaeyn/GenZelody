package android2.genzelody;

import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class Activity_Detail_Playlist extends AppCompatActivity {
    private Playlists curPlaylist;
    public Activity_Detail_Playlist(Playlists playlist){
        curPlaylist = playlist;
    }

    public void setCurPlaylist(Playlists playlists){
        curPlaylist = playlists;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_playlist);
        Log.d("Playlist", "" +curPlaylist);
    }

    void addControl(){
        ImageView img = findViewById(R.id.imgPlayListDetail);
        Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();

        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                // Get the dominant color
                int dominantColor = palette.getDominantColor(getResources().getColor(com.google.android.material.R.color.design_default_color_background));

                // Set the background color of the LinearLayout
                LinearLayout linearLayout = findViewById(R.id.linearActivityArtist);
                linearLayout.setBackgroundColor(dominantColor);
            }
        });
    }
}
package android2.genzelody;

import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Activity_Detail_Playlist extends AppCompatActivity {
    TextView tvNamePlaylist;
    ImageView imgPlayListDetail;
    ListView lvTrackOfPlaylist;
    Custom_Adapter_Lv_Track_Playlist adapterTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_playlist);

        addControl();
        addEvent();
    }

    void addControl() {
        tvNamePlaylist = findViewById(R.id.tvNamePlaylist);
        imgPlayListDetail = findViewById(R.id.imgPlayListDetail);
        lvTrackOfPlaylist = findViewById(R.id.lvTrackOfPlaylist);
    }

    void addEvent() {
        Intent intent = getIntent();

        ArrayList<Track> receivedTrackList = intent.getParcelableArrayListExtra("tracks");

        tvNamePlaylist.setText(intent.getStringExtra("namePlaylist"));

        Picasso.with(this.getApplicationContext()).load(intent.getStringExtra("imgPlaylist")).resize(480, 480).into(imgPlayListDetail);

        adapterTrack = new Custom_Adapter_Lv_Track_Playlist(getApplicationContext(),R.layout.layout_item_list_track_playlist,);

        Picasso.with(this.getApplicationContext()).load(intent.getStringExtra("imgPlaylist")).into(new com.squareup.picasso.Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                generatePalette(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });

        if (receivedTrackList != null) {
            for (Track track : receivedTrackList) {
                System.out.println(track.getId() + track.getName() + track.getImg());
            }
        }
    }

    private void generatePalette(Bitmap bitmap) {
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                int dominantColor = palette.getDominantColor(getResources().getColor(com.google.android.material.R.color.design_default_color_background));

                LinearLayout linearLayout = findViewById(R.id.linearActivityArtist);
                linearLayout.setBackgroundColor(dominantColor);
            }
        });
    }
}

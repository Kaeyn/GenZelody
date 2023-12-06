package android2.genzelody;

import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Activity_Detail_Playlist extends AppCompatActivity {
    TextView tvNamePlaylist;
    ImageView imgPlayListDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_playlist);
        ImageView imgPlayListDetail = findViewById(R.id.imgPlayListDetail);
        Bitmap bitmap = ((BitmapDrawable) imgPlayListDetail.getDrawable()).getBitmap();
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                int dominantColor = palette.getDominantColor(getResources().getColor(com.google.android.material.R.color.design_default_color_background));

                LinearLayout linearLayout = findViewById(R.id.linearActivityArtist);
                linearLayout.setBackgroundColor(dominantColor);
            }
        });

        addControl();
        addEvent();

    }

    void addControl(){
        tvNamePlaylist = findViewById(R.id.tvNamePlaylist);
//        imgPlayListDetail = findViewById(R.id.imgPlayListDetail);

    }
    void addEvent(){
        Intent intent = getIntent();

        ArrayList<Track> receivedTrackList = intent.getParcelableArrayListExtra("tracks");

        tvNamePlaylist.setText(intent.getStringExtra("namePlaylist"));
        Picasso.with(this.getApplicationContext()).load(intent.getStringExtra("imgPlaylist")).resize(480,480).into(imgPlayListDetail);
        if (receivedTrackList != null) {
            for (Track track : receivedTrackList) {


                System.out.println(track.getId() + track.getName() + track.getImg());
            }
        }
    }
}
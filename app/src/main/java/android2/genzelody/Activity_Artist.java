package android2.genzelody;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Activity_Artist extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actitvy_artist);

        Intent intent = getIntent();
        ArrayList<Track> receivedTrackList = intent.getParcelableArrayListExtra("tracks");

// Check if the ArrayList<Track> is not null before using it
        if (receivedTrackList != null) {
            // Use the received ArrayList<Track> as needed
            for (Track track : receivedTrackList) {
                // Do something with each track
                System.out.println(track.getId() + track.getName() + track.getImg());
            }
        }
    }
}
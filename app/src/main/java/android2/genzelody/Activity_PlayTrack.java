package android2.genzelody;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class Activity_PlayTrack extends AppCompatActivity {
    TextView tvNameAlbumPlay, tvNameTrackPlay, tvNameArtistPlay, tvTimeStart, tvTimeEnd;
    ImageView imgTrackPlay;
    SeekBar seekBar;
    Button btnBackPage, btnMore, btnAddLib;
    ImageButton btnBackTrack, btnPauseTrack, btnNextTrack;
    MediaPlayer mediaPlayer;
    Handler handler = new Handler();

    //later set
    String preview_url = "https://p.scdn.co/mp3-preview/dd79198f4b4c43aef9c8e8e1c4708a402862dd0e?cid=f628b685017b4db5bacd292385fd7f50";
    String idTrack = "";
    String nameTrack = "";
    String nameArtists ="";
    String nameAlbum ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_track);
        addControls();
        addEvents();
        setTrackInfo();
    }
    private void setTrackInfo(){
        tvNameAlbumPlay.setText(nameAlbum);
        tvNameTrackPlay.setText(nameTrack);
        tvNameArtistPlay.setText(nameArtists);
    }
    private void addControls(){
        //text view
        tvNameAlbumPlay = findViewById(R.id.tvNameAlbumPlay);
        tvNameTrackPlay = findViewById(R.id.tvNameTrackPlay);
        tvNameArtistPlay = findViewById(R.id.tvNameArtistPlay);
        tvTimeStart = findViewById(R.id.tvTimeStart);
        tvTimeEnd = findViewById(R.id.tvTimeEnd);
        //image view
        imgTrackPlay = findViewById(R.id.imgTrackPlay);
        //seekbar
        seekBar = findViewById(R.id.seekBar);
        seekBar.setMax(100);
        //button
        btnBackPage = findViewById(R.id.btnBackPage);
        btnMore = findViewById(R.id.btnMore);
        btnAddLib = findViewById(R.id.btnAddLib);
        //image button
        btnBackTrack = findViewById(R.id.btnBackTrack);
        btnPauseTrack = findViewById(R.id.btnPauseTrack);
        btnNextTrack = findViewById(R.id.btnNextTrack);
        //media player
        mediaPlayer = new MediaPlayer();
    }
    private void addEvents(){
        prepareMediaPlayer();
        btnPauseTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    handler.removeCallbacks(updater);
                    mediaPlayer.pause();
                    btnPauseTrack.setImageResource(R.drawable.baseline_play_circle_24);
                }else{
                    mediaPlayer.start();
                    btnPauseTrack.setImageResource(R.drawable.baseline_pause_circle_outline_24);
                    updateSeekbar();
                }
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int playPosition = (mediaPlayer.getDuration() / 100) * seekBar.getProgress();
                mediaPlayer.seekTo(playPosition);
                tvTimeStart.setText(milliSecondToTimer(mediaPlayer.getCurrentPosition()));
                updateSeekbar();
            }
        });
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                SeekBar seekBar1 = (SeekBar) v;
                int playPosition = (mediaPlayer.getDuration() / 100) * seekBar1.getProgress();
                tvTimeStart.setText(milliSecondToTimer(playPosition));
                handler.removeCallbacks(updater);
                return false;
            }
        });
        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                seekBar.setSecondaryProgress(percent);
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                seekBar.setProgress(0);
                btnPauseTrack.setImageResource(R.drawable.baseline_play_circle_24);
                tvTimeStart.setText("0:00");
                mediaPlayer.reset();
                prepareMediaPlayer();
            }
        });
    }

    private void prepareMediaPlayer(){
        try {
            mediaPlayer.setDataSource(preview_url);
            mediaPlayer.prepare();
            tvTimeEnd.setText(milliSecondToTimer(mediaPlayer.getDuration()));
        } catch (IOException e) {
            Log.d("prepareMediaPlayer: ", ""+e);
        }
    }
    private Runnable updater = new Runnable() {
        @Override
        public void run() {
            updateSeekbar();


        }
    };
    private  void updateSeekbar(){
        if(mediaPlayer.isPlaying()){
            long currentDuration = mediaPlayer.getCurrentPosition();
            tvTimeStart.setText(milliSecondToTimer(currentDuration));
            seekBar.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration()) * 100));
            handler.postDelayed(updater,1000);
        }
    }
    private String milliSecondToTimer(long milliSecond){
        String timeString = "";
        String secondString;
        int hours = (int) (milliSecond / (1000*60*60));
        int minutes = (int) (milliSecond % (1000*60*60)) / (1000*60);
        int second = (int) ((milliSecond % (1000*60*60)) % (1000*60) / 1000);

        if(hours > 0){
            timeString = hours+":";
        }
        if(second < 10){
            secondString = "0"+second;
        }else{
            secondString = ""+second;
        }
        timeString += minutes + ":" + secondString;
        return  timeString;
    }
}
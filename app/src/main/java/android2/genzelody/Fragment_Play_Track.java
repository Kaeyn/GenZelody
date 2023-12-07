package android2.genzelody;

import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Play_Track#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Play_Track extends Fragment {
    TextView tvNameAlbumPlay, tvNameTrackPlay, tvNameArtistPlay, tvTimeStart, tvTimeEnd;
    ImageView imgTrackPlay;
    SeekBar seekBar;
    ImageButton btnBackTrack, btnPauseTrack, btnNextTrack, btnBackPage, btnMore, btnAddLib;
    MediaPlayer mediaPlayer;
    Handler handler = new Handler();

    //later set
    String preview_url ="", nameTrack="", nameArtists="", nameAlbum="", img_url="";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_Play_Track() {
        // Required empty public constructor
    }
    public Fragment_Play_Track(Track track, String name) {
        // Required empty public constructor
        preview_url = track.getPreview_url();
        nameTrack = track.getName();
        img_url = track.getImg();
        for (Artist artist: track.getArtists()) {
            nameArtists += artist.getName()+ " ";
        }
        nameAlbum = name;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Play_Track.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Play_Track newInstance(String param1, String param2) {
        Fragment_Play_Track fragment = new Fragment_Play_Track();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment__play__track, container, false);
        // Inflate the layout for this fragment
        addControls(rootView);
        setTrackInfo();
        addEvents();
        return rootView;
    }
    private void setTrackInfo(){
        tvNameAlbumPlay.setText(nameAlbum);
        tvNameTrackPlay.setText(nameTrack);
        tvNameArtistPlay.setText(nameArtists);
        try {
            int drawableResourceId = Integer.parseInt(img_url);
            Drawable drawable = ContextCompat.getDrawable(getContext(), drawableResourceId);
            imgTrackPlay.setImageDrawable(drawable);
        } catch (NumberFormatException e) {
            // If the image is not a drawable resource ID (assuming it's a URL)
            Picasso.with(this.getContext()).load(img_url).resize(860,860).into(imgTrackPlay);
        }
        prepareMediaPlayer();
    }
    private void addControls(View rootView){
        //text view
        tvNameAlbumPlay = rootView.findViewById(R.id.tvNameAlbumPlay);
        tvNameTrackPlay = rootView.findViewById(R.id.tvNameTrackPlay);
        tvNameArtistPlay = rootView.findViewById(R.id.tvNameArtistPlay);
        tvTimeStart = rootView.findViewById(R.id.tvTimeStart);
        tvTimeEnd = rootView.findViewById(R.id.tvTimeEnd);
        //image view
        imgTrackPlay = rootView.findViewById(R.id.imgTrackPlay);
        //seekbar
        seekBar = rootView.findViewById(R.id.seekBar);
        seekBar.setMax(100);
        //button
        btnBackPage = rootView.findViewById(R.id.btnBackPage);
        btnMore = rootView.findViewById(R.id.btnMore);
        btnAddLib = rootView.findViewById(R.id.btnAddLib);
        //image button
        btnBackTrack = rootView.findViewById(R.id.btnBackTrack);
        btnPauseTrack = rootView.findViewById(R.id.btnPauseTrack);
        btnNextTrack = rootView.findViewById(R.id.btnNextTrack);
        //media player
        mediaPlayer = new MediaPlayer();
    }
    private void startTrack(){
        mediaPlayer.start();
        Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.rotate);
        imgTrackPlay.startAnimation(animation);
        btnPauseTrack.setImageResource(R.drawable.baseline_pause_circle_outline_24);
        updateSeekbar();
    }
    private void addEvents(){
        startTrack();
        btnPauseTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    handler.removeCallbacks(updater);
                    mediaPlayer.pause();
                    imgTrackPlay.clearAnimation();
                    btnPauseTrack.setImageResource(R.drawable.baseline_play_circle_24);
                }else{
                    startTrack();
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

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                seekBar.setProgress(0);
                imgTrackPlay.clearAnimation();
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
        if (mediaPlayer.isPlaying()) {
            long currentDuration = mediaPlayer.getCurrentPosition();
            System.out.println(mediaPlayer.getCurrentPosition());
            tvTimeStart.setText(milliSecondToTimer(currentDuration));

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    seekBar.setProgress((int) (((float) currentDuration / mediaPlayer.getDuration()) * 100));
                }
            });
        }

        handler.postDelayed(updater, 1);
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
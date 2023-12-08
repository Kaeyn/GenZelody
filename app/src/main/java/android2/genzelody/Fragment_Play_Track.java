package android2.genzelody;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.palette.graphics.Palette;

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
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.RandomAccess;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Play_Track#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Play_Track extends Fragment {
    TextView tvNameAlbumPlay, tvNameTrackPlay, tvNameArtistPlay, tvTimeStart, tvTimeEnd;
    ImageView imgTrackPlay;
    SeekBar seekBar;
    ImageButton btnBackTrack, btnPauseTrack, btnNextTrack, btnBackPage, btnMore, btnAddLib, btnSuffleTracks, btnLoopTracks;
    MediaPlayer mediaPlayer;
    Handler handler = new Handler();
    LinearLayout linearLayout;
    int index = 0;
    //later set
    String preview_url ="", nameTrack="", nameArtists="", nameAlbum="", img_url="";
    ArrayList<Track> tracks = new ArrayList<>();
    Boolean isSuffle = false, isLoop = false;
    private static final long DELAY_TIME = 5000;


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
    public Fragment_Play_Track(ArrayList<Track> tracks, String name, int index) {
        // Required empty public constructor
        this.tracks = tracks;
        this.index = index;
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
        addEvents(rootView);
        return rootView;
    }

    private void setTrackInfo(){
        preview_url = tracks.get(index).getPreview_url();
        nameTrack = tracks.get(index).getName();
        img_url = tracks.get(index).getImg();
        nameArtists = "";
        tvTimeStart.setText("0:00");
        seekBar.setProgress(0);
        for (Artist artist: tracks.get(index).getArtists()) {
            nameArtists += artist.getName()+ " ";
        }
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
        linearLayout = rootView.findViewById(R.id.lnPlayTrack);


        //button
        btnBackPage = rootView.findViewById(R.id.btnBackPage);
        btnMore = rootView.findViewById(R.id.btnMore);
        btnBackTrack = rootView.findViewById(R.id.btnBackTrack);
        btnPauseTrack = rootView.findViewById(R.id.btnPauseTrack);
        btnNextTrack = rootView.findViewById(R.id.btnNextTrack);
        btnSuffleTracks = rootView.findViewById(R.id.btnSuffleTracks);
        btnLoopTracks = rootView.findViewById(R.id.btnLoopTracks);
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
    private void stopTrack(){
        handler.removeCallbacks(updater);
        mediaPlayer.pause();
        imgTrackPlay.clearAnimation();
        btnPauseTrack.setImageResource(R.drawable.baseline_play_circle_24);
    }
    private void applyGradientBackground(Bitmap bitmap) {
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                int defaultColor = getResources().getColor(com.google.android.material.R.color.design_default_color_background);

                // Get the dominant color, or use the default color if not available
                int dominantColor = palette.getDominantColor(defaultColor);

                // Create a GradientDrawable with a gradient from the dominant color to a lighter shade
                GradientDrawable gradientDrawable = new GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        new int[]{dominantColor, lightenColor(dominantColor)}
                );

                // Set corner radius and other properties if needed
                gradientDrawable.setCornerRadius(0f);
                gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);

                // Set the GradientDrawable as the background of the LinearLayout
                linearLayout.setBackground(gradientDrawable);
            }
        });
    }

    // Method to lighten a color
    private int lightenColor(int color) {
        float factor = 0.8f; // Adjust the factor based on how much you want to lighten the color
        return ColorUtils.blendARGB(color, Color.WHITE, factor);
    }

    private void addEvents(View rootView){
        startTrack();
        btnSuffleTracks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSuffle){
                    btnSuffleTracks.setImageResource(R.drawable.baseline_casino_24);
                    isSuffle = false;
                } else {
                    btnSuffleTracks.setImageResource(R.drawable.baseline_casino_24_pink);
                    isSuffle = true;
                }
            }
        });
        btnLoopTracks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLoop){
                    btnLoopTracks.setImageResource(R.drawable.baseline_loop_24_white);
                    mediaPlayer.setLooping(false);
                    isLoop = false;
                } else {
                    btnLoopTracks.setImageResource(R.drawable.baseline_loop_24);
                    mediaPlayer.setLooping(true);
                    isLoop = true;
                }
            }
        });
        btnPauseTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    stopTrack();
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
                if(!isLoop){
                    if(isSuffle){
                        randomTrack();
                    }else{
                        nextTrack();
                    }
                }
            }
        });
        btnBackPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        btnBackTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(0 == index){
                    index = tracks.size() - 1;
                }else {
                    index--;
                }
                stopTrack();
                setTrackInfo();
                startTrack();
            }
        });
        btnNextTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextTrack();
            }
        });

        Picasso.with(rootView.getContext()).load(String.valueOf(imgTrackPlay)).into(new com.squareup.picasso.Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                applyGradientBackground(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                // Handle failure if needed
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                // Handle preparation if needed
            }
        });


    }
    private void randomTrack(){
        Random r = new Random();
        int randomIndex = r.nextInt((tracks.size() - 1) +1) + 1;
        System.out.println(tracks.size());
        System.out.println(randomIndex);
        while (index == randomIndex){
            randomIndex = r.nextInt(tracks.size());
            System.out.println(randomIndex);
        }
        index = randomIndex;
        stopTrack();
        setTrackInfo();
        startTrack();
    }
private void nextTrack(){
    if(tracks.size() - 1 == index){
        index = 0;
    }else {
        index++;
    }
    stopTrack();
    setTrackInfo();
    startTrack();
}
    private void prepareMediaPlayer(){
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(preview_url);
            mediaPlayer.prepare();
            tvTimeEnd.setText(milliSecondToTimer(mediaPlayer.getDuration()));
        } catch (IOException e) {
            Log.d("prepareMediaPlayer: ", ""+e);
        }
    }
    public void goBack() {
        // Get the FragmentManager
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        // Check if there are fragments in the back stack
        if (fragmentManager.getBackStackEntryCount() > 0) {
            // Pop the top fragment off the back stack
            stopTrack();
            fragmentManager.popBackStack();
        } else {
            // If the back stack is empty, you may want to handle this situation
            // For example, you can navigate to a different activity or finish the current activity
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
            tvTimeStart.setText(milliSecondToTimer(currentDuration));
            final int progress = (int) (((float) (currentDuration * 100) / mediaPlayer.getDuration()));
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    seekBar.setProgress(progress);
                }
            });

            handler.postDelayed(updater, 10); // Update every second
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updater);
    }

}
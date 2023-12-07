package android2.genzelody;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.palette.graphics.Palette;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_detail_playlist#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_detail_playlist extends Fragment implements RecyclerViewClickListener{
    TextView tvNamePlaylist;
    ImageView imgPlayListDetail;
    ListView lvTrackOfPlaylist;
    Custom_Adapter_Lv_Track_Playlist adapterTrack;
    Playlists playlist;
    String namePlaylist = "", imgPlayList = "";
    ImageButton btnBack;
    ArrayList<Track> playlistTrack;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_detail_playlist() {
        // Required empty public constructor
    }
    public fragment_detail_playlist(Playlists playlist) {
        // Required empty public constructor
        this.playlist = playlist;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_detail_playlist.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_detail_playlist newInstance(String param1, String param2) {
        fragment_detail_playlist fragment = new fragment_detail_playlist();
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
        View rootView = inflater.inflate(R.layout.fragment_detail_playlist, container, false);
        addControl(rootView);
        addEvent(rootView);
        playlistTrack = playlist.getTracks();
        adapterTrack = new Custom_Adapter_Lv_Track_Playlist(rootView.getContext(),R.layout.layout_item_list_track_playlist,playlistTrack, (RecyclerViewClickListener) this);
        lvTrackOfPlaylist.setAdapter(adapterTrack);
        // Inflate the layout for this fragment
        return rootView;
    }
    void addControl(View rootView) {
        tvNamePlaylist = rootView.findViewById(R.id.tvNamePlaylist);
        imgPlayListDetail = rootView.findViewById(R.id.imgPlayListDetail);
        lvTrackOfPlaylist = rootView.findViewById(R.id.lvTrackOfPlaylist);
        btnBack = rootView.findViewById(R.id.btnBack);
    }

    void addEvent(View rootView) {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        namePlaylist = playlist.getName();
        imgPlayList = playlist.getImages();
        try {
            int drawableResourceId = Integer.parseInt(imgPlayList);
            Drawable drawable = ContextCompat.getDrawable(rootView.getContext(), drawableResourceId);
            imgPlayListDetail.setImageDrawable(drawable);
        } catch (NumberFormatException e) {
            // If the image is not a drawable resource ID (assuming it's a URL)
            Picasso.with(this.getContext()).load(imgPlayList).resize(100,100).into(imgPlayListDetail);
        }
        tvNamePlaylist.setText(namePlaylist);


        Picasso.with(rootView.getContext()).load(imgPlayList).into(new com.squareup.picasso.Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                generatePalette(rootView, bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }

    private void generatePalette(View rootView, Bitmap bitmap) {
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                int dominantColor = palette.getDominantColor(getResources().getColor(com.google.android.material.R.color.design_default_color_background));

                LinearLayout linearLayout = rootView.findViewById(R.id.linearActivityArtist);
                linearLayout.setBackgroundColor(dominantColor);
            }
        });
    }
    public void loadFragment(Fragment fragment){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frameFragmentHome, fragment);
        ft.commit();
    }

    @Override
    public void onClick(View view, int position, String category) {

    }

    @Override
    public void listOnClick(View view, int position) {
        System.out.println("dasdasda");
        loadFragment(new Fragment_Play_Track(playlistTrack.get(position),playlist.getName()));
    }
}
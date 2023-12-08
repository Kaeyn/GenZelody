package android2.genzelody;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    Playlists playlist;
    String namePlaylist = "", imgPlayList = "";
    ImageButton btnBack;
    NestedScrollView nestedScrollDetailPlaylist;
    Custom_Adapter_Detail_Playlist custom_adapter_detail_playlist;
    Custom_Adapter_RCM_Track custom_adapter_rcm_track;
    RecyclerView rvTrackOfPlaylist, recViewTrackGoiY;
    ArrayList<Track> playlistTrack;
    ArrayList<Track> rcmTrack;
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

    public fragment_detail_playlist(Playlists playlist, ArrayList<Track> track) {
        // Required empty public constructor
        this.playlist = playlist;
        this.rcmTrack = track;
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
//        System.out.println(playlistTrack);
        LinearLayoutManager layoutManagerPhoBien = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvTrackOfPlaylist.setLayoutManager(layoutManagerPhoBien);
        custom_adapter_detail_playlist = new Custom_Adapter_Detail_Playlist(getContext(),playlistTrack, this);
        rvTrackOfPlaylist.setAdapter(custom_adapter_detail_playlist);

        LinearLayoutManager layoutManagerTrackRCM = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recViewTrackGoiY.setLayoutManager(layoutManagerTrackRCM);
        custom_adapter_rcm_track = new Custom_Adapter_RCM_Track(getContext(),rcmTrack, this);
        recViewTrackGoiY.setAdapter(custom_adapter_rcm_track);
        // Inflate the layout for this fragment
        return rootView;
    }
    void addControl(View rootView) {
        tvNamePlaylist = rootView.findViewById(R.id.tvNamePlaylist);
        imgPlayListDetail = rootView.findViewById(R.id.imgPlayListDetail);
        rvTrackOfPlaylist = rootView.findViewById(R.id.rvTrackOfPlaylist);
        btnBack = rootView.findViewById(R.id.btnBack);
        nestedScrollDetailPlaylist = rootView.findViewById(R.id.nestedScrollDetailPlaylist);
        recViewTrackGoiY = rootView.findViewById(R.id.recViewTrackGoiY);
    }

    void addEvent(View rootView) {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });

        nestedScrollDetailPlaylist.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // Calculate the scale factor based on the scroll position
                float scaleFactor = 1.0f - (float) scrollY / (float) getResources().getDimensionPixelSize(R.dimen.image_initial_height) * 0.5f;

                // Limit the scale factor to a minimum value (e.g., 0.5f)
                scaleFactor = Math.max(scaleFactor, 0.1f);

                // Set the scale factor to the ImageView
                imgPlayListDetail.setScaleX(scaleFactor);
                imgPlayListDetail.setScaleY(scaleFactor);

                // Optionally, you can adjust the visibility based on the scroll position
                if (scaleFactor + 1.0f <= 0.5f) {
                    imgPlayListDetail.setVisibility(View.GONE);
                } else {
                    imgPlayListDetail.setVisibility(View.VISIBLE);
                }
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
            Picasso.with(this.getContext()).load(imgPlayList).resize(300,300).into(imgPlayListDetail);
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
                int defaultColor = getResources().getColor(com.google.android.material.R.color.design_default_color_background);

                // Get the dominant color, or use the default color if not available
                int dominantColor = palette.getDominantColor(defaultColor);

                // Create a GradientDrawable with a gradient from the dominant color to a lighter shade
                GradientDrawable gradientDrawable = new GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        new int[]{dominantColor, darkerColor(dominantColor)}
                );

                // Set corner radius and other properties if needed
                gradientDrawable.setCornerRadius(0f);
                gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);

                // Set the GradientDrawable as the background of the LinearLayout
                LinearLayout linearLayout = rootView.findViewById(R.id.linearActivityArtist);
                linearLayout.setBackground(gradientDrawable);
            }
        });
    }

    private int darkerColor(int color) {
        float factor = 1.0f; // Adjust the factor based on how much you want to lighten the color
        return ColorUtils.blendARGB(color, Color.BLACK, factor);
    }
    public void loadFragment(Fragment fragment){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frameFragmentHome, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void goBack() {
        // Get the FragmentManager
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        // Check if there are fragments in the back stack
        if (fragmentManager.getBackStackEntryCount() > 0) {
            // Pop the top fragment off the back stack
            fragmentManager.popBackStack();
        } else {
            // If the back stack is empty, you may want to handle this situation
            // For example, you can navigate to a different activity or finish the current activity
        }
    }

    @Override
    public void onClick(View view, int position, String category) {

    }

    @Override
    public void listOnClick(View view, int position) {
        loadFragment(new Fragment_Play_Track(playlistTrack, playlist.getName(), position));
    }
}
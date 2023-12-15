package android2.genzelody;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Detail_Artist#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Detail_Artist extends Fragment implements RecyclerViewClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    TextView tvNamePlaylist, txtTitleRec, txtDesRec;
    ImageView imgPlayListDetail;
    Artist artist;
    String namePlaylist = "", imgPlayList = "";
    ImageButton btnBack;
    NestedScrollView nestedScrollDetailArtist;
    Custom_Adapter_Detail_Playlist custom_adapter_detail_playlist;
    Custom_Adapter_RCM_Track custom_adapter_rcm_track;
    RecyclerView rvTrackOfPlaylist, recViewTrackGoiY;
    ArrayList<Track> playlistTrack;
    ArrayList<Track> artistTrack;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_Detail_Artist() {
        // Required empty public constructor
    }
    private SlidingPanelToggleListener slidingPanelToggleListener;
    String ACCESS_TOKEN;

    public Fragment_Detail_Artist(ArrayList<Track> artistTopTracks, Artist theArtist, String accessToken) {
        // Required empty public constructor
        this.artistTrack = artistTopTracks;
        this.ACCESS_TOKEN = accessToken;
        this.artist = theArtist;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Detail_Artist.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Detail_Artist newInstance(String param1, String param2) {
        Fragment_Detail_Artist fragment = new Fragment_Detail_Artist();
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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof SlidingPanelToggleListener) {
            slidingPanelToggleListener = (SlidingPanelToggleListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement PlayTrackClickListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment__detail__artist, container, false);
        addControl(rootView);
        addEvent(rootView);
        showFullScreenLoader();
        LinearLayoutManager layoutManagerPhoBien = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvTrackOfPlaylist.setLayoutManager(layoutManagerPhoBien);
        custom_adapter_detail_playlist = new Custom_Adapter_Detail_Playlist(getContext(),artistTrack, this);
        rvTrackOfPlaylist.setAdapter(custom_adapter_detail_playlist);

        // Inflate the layout for this fragment
        return rootView;
    }
    void addControl(View rootView) {
        tvNamePlaylist = rootView.findViewById(R.id.tvNamePlaylist);
        imgPlayListDetail = rootView.findViewById(R.id.imgPlayListDetail);
        rvTrackOfPlaylist = rootView.findViewById(R.id.rvTrackOfPlaylist);
        nestedScrollDetailArtist = rootView.findViewById(R.id.nestedScrollDetailPlaylist);
        recViewTrackGoiY = rootView.findViewById(R.id.recViewTrackGoiY);
        txtTitleRec = rootView.findViewById(R.id.txtTitleRec);
        txtDesRec = rootView.findViewById(R.id.txtDescRec);
    }

    void addEvent(View rootView) {

        nestedScrollDetailArtist.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
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

        namePlaylist = artist.getName();
        imgPlayList = artist.getImage();
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


    public void goBack() {
        // Get the FragmentManager
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        if (fragmentManager.getBackStackEntryCount() > 0) {
            // Pop the top fragment off the back stack
            fragmentManager.popBackStack();
        } else {

        }
    }

    @Override
    public void onClick(View view, int position, String category) {

    }

    @Override
    public void listOnClick(View view, int position) {
        slidingPanelToggleListener.setTrackLists(artistTrack, artist.getName(), position);
    }

    @Override
    public void reclistOnClick(View view, int position) {

    }
    private void showFullScreenLoader() {
        View dialogView = getLayoutInflater().inflate(R.layout.activity_loader_search, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(false);

        AlertDialog fullScreenDialog = alertDialogBuilder.create();
        fullScreenDialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fullScreenDialog.dismiss();
            }
        }, 2000);
    }

}
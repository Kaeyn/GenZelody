package android2.genzelody;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Home extends Fragment {

    Custom_Adapter_RecycleView_Album_MainPage adapter_recycleView_album_mainPage;
    Custom_Adapter_RecycleView_Album_FeaturePlayList adapter_recycleView_tracks_bigger_mainPage;
    Custom_Adapter_RecycleView_Tracks_Popular adapter_recycleView_tracks_popular;
    RecyclerView recViewDanhSachCuaBan, recViewGoiY, recViewPhoBien, recGridPlayListCuaBan;

    Custom_Adapter_Grid_MainPage custom_adapter_grid_mainPage;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    TextView userName;
    ImageView userImg;
    private ArrayList<Playlists> MyPlayList = new ArrayList<>();
    private ArrayList<Playlists> FeaturePlayList = new ArrayList<>();
    private ArrayList<Track> RecommendedTrackList = new ArrayList<>();
    User user = new User();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String ACCESS_TOKEN = "";

    public Fragment_Home() {
        // Required empty public constructor
    }

    public Fragment_Home(String accesssToken) {
        // Required empty public constructor
    }
    public Fragment_Home(String accesssToken, ArrayList<Playlists> myPlayList, ArrayList<Playlists> featurePlayList, ArrayList<Track> listTrack, User user) {
        // Required empty public constructor
        ACCESS_TOKEN = accesssToken;
        MyPlayList = myPlayList;
        FeaturePlayList = featurePlayList;
        RecommendedTrackList = listTrack;
        this.user = user;
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Home newInstance(String param1, String param2) {
        Fragment_Home fragment = new Fragment_Home();
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
        View rootView = inflater.inflate(R.layout.fragment__home, container, false);

        addViewControls(rootView);

        // Create a list of Track objects with different drawables
//        List<Album> albumList = new ArrayList<>();
//        albumList.add(new Album("Bài hát ưa thích",String.valueOf(R.drawable.yeuthich)));
//        albumList.add(new Album("Tình đầu", String.valueOf(R.drawable.johnweak)));
//        albumList.add(new Album("SOFAR", String.valueOf(R.drawable.johnweak)));
//        albumList.add(new Album("Xe đạp", String.valueOf(R.drawable.johnweak)));

        Picasso.with(getContext()).load(user.getUserImg()).resize(160,160).into(userImg);
        userName.setText(user.getUserName());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recViewDanhSachCuaBan.setLayoutManager(layoutManager);
        adapter_recycleView_album_mainPage = new Custom_Adapter_RecycleView_Album_MainPage(getContext(), MyPlayList);
        recViewDanhSachCuaBan.setAdapter(adapter_recycleView_album_mainPage);

        LinearLayoutManager layoutManagerGoiY = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recViewGoiY.setLayoutManager(layoutManagerGoiY);
        adapter_recycleView_tracks_bigger_mainPage = new Custom_Adapter_RecycleView_Album_FeaturePlayList(getContext(), FeaturePlayList);
        recViewGoiY.setAdapter(adapter_recycleView_tracks_bigger_mainPage);

        LinearLayoutManager layoutManagerPhoBien = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recViewPhoBien.setLayoutManager(layoutManagerPhoBien);
        adapter_recycleView_tracks_popular = new Custom_Adapter_RecycleView_Tracks_Popular(getContext(), RecommendedTrackList);
        recViewPhoBien.setAdapter(adapter_recycleView_tracks_popular);

        custom_adapter_grid_mainPage = new Custom_Adapter_Grid_MainPage(getContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);

        recGridPlayListCuaBan.setLayoutManager(gridLayoutManager);
        ArrayList<Playlists> tempPlaylist = new ArrayList<>(MyPlayList.subList(0, Math.min(MyPlayList.size(), 6)));
        custom_adapter_grid_mainPage.setData(tempPlaylist);
        recGridPlayListCuaBan.setAdapter(custom_adapter_grid_mainPage);


        return rootView;
    }


    private void addViewControls(View rootView){
        userImg = rootView.findViewById(R.id.imgUser);
        userName = rootView.findViewById(R.id.tvNameUser);
        recViewDanhSachCuaBan = rootView.findViewById(R.id.recViewDanhSachCuaBan);
        recViewGoiY = rootView.findViewById(R.id.recViewGoiY);
        recViewPhoBien = rootView.findViewById(R.id.recViewPhoBien);
        recGridPlayListCuaBan = rootView.findViewById(R.id.recGridPlayListCuaBan);
    }





}
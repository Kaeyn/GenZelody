package android2.genzelody;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Library#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Library extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String ACCESS_TOKEN = "";

    private ListView listView;

    private ArrayList<Playlists> MyPlayList = new ArrayList<>();

    Custom_LVLib_Adapter custom_lvLib_adapter;
    ImageView imgUser;
    TextView nameUser;
    User user = new User();
    ArrayList<Track> tracksRCM;

    SlidingPanelToggleListener slidingPanelToggleListener;

    public Fragment_Library() {
        // Required empty public constructor
    }
    public Fragment_Library(String accesssToken, ArrayList<Playlists> myPlayList, User user) {
        // Required empty public constructor
        ACCESS_TOKEN = accesssToken;
        MyPlayList = myPlayList;
        this.user = user;
    }

    public Fragment_Library(String accesssToken, ArrayList<Playlists> myPlayList, User user, ArrayList<Track> tracks) {
        // Required empty public constructor
        ACCESS_TOKEN = accesssToken;
        MyPlayList = myPlayList;
        this.user = user;
        tracksRCM = tracks;
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Library.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Library newInstance(String param1, String param2) {
        Fragment_Library fragment = new Fragment_Library();
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

        View rootView = inflater.inflate(R.layout.fragment__library, container, false);
        addControls(rootView);
        custom_lvLib_adapter = new Custom_LVLib_Adapter(getContext(), R.layout.custom_item_lv_playlist_lib, MyPlayList);
        listView.setAdapter(custom_lvLib_adapter);
        addEvents();
        return rootView;
    }

    void addControls(View view){
        listView = view.findViewById(R.id.listPlayListLibs);
        imgUser = view.findViewById(R.id.imgUserLib);
        nameUser = view.findViewById(R.id.tvNameUserLib);
    }

    void addEvents(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                loadFragment(new fragment_detail_playlist(MyPlayList.get(position),ACCESS_TOKEN));
            }
        });
        Picasso.with(getContext()).load(user.getUserImg()).resize(160,160).into(imgUser);
    }
    public void loadFragment(Fragment fragment){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frameFragmentHome, fragment);
        Fragment libraryFragment = fm.findFragmentByTag("library");
        ft.addToBackStack("library");
        ft.setPrimaryNavigationFragment(libraryFragment);
        ft.commit();
    }

}

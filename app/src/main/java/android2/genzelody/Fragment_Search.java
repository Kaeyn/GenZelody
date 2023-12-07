package android2.genzelody;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Search#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Search extends Fragment implements RecyclerViewClickListener{


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String ACCESS_TOKEN = "";
    EditText edtInputSearch;
    String apiResponse = "";
    private JSONObject fullSearchObject;
    private JSONObject searchObject;
    ArrayList<Track> trackArrayList = new ArrayList<>();
    ArrayList<Artist> artistArrayList = new ArrayList<>();
    ArrayList<Album> albumArrayList = new ArrayList<>();
    ArrayList<Playlists> playlistsArrayList = new ArrayList<>();
    ImageView imgUser;
    TextView nameUser;

    Custom_Adapter_Grid_SearchPage custom_adapter_grid_searchPage;
    RecyclerView recyclerView;

    User user = new User();



    private RequestQueue requestQueue;

    public Fragment_Search() {
        // Required empty public constructor
    }

    public Fragment_Search(String accessToken, User user, ArrayList<Track> tracks) {
        // Required empty public constructor
        ACCESS_TOKEN = accessToken;
        this.user = user;
        this.trackArrayList = tracks;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Search.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Search newInstance(String param1, String param2) {
        Fragment_Search fragment = new Fragment_Search();
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

        View view = inflater.inflate(R.layout.fragment__search, container, false);
        requestQueue = Volley.newRequestQueue(getContext());
        addViewControls(view);
        addEvent(view);
//        searchThings("a");
        custom_adapter_grid_searchPage = new Custom_Adapter_Grid_SearchPage(getContext(), this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        custom_adapter_grid_searchPage.setData(trackArrayList);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(custom_adapter_grid_searchPage);
        showFullScreenLoader();
        return view;
    }

    private void addViewControls(View view){
        edtInputSearch = view.findViewById(R.id.edtInputSearch);
        edtInputSearch.requestFocus();
        imgUser = view.findViewById(R.id.imgUserSearch);
        nameUser = view.findViewById(R.id.tvNameUserSearch);
        recyclerView = view.findViewById(R.id.recGridSearch);
        showKeyboard();
    }
    void addEvent(View view){
        edtInputSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                    // Your function to execute when Enter is pressed

                    searchThings(edtInputSearch.getText().toString());
                    InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edtInputSearch.getWindowToken(), 0);
                    recyclerView.swapAdapter(custom_adapter_grid_searchPage, true);
//                    Log.d("FinalResult", "ohYeah" + fullSearchObject);
//                    // Return true to indicate that the event has been handled
                    return true;
                }
                // Return false to allow the default action to occur
                return false;
            }
        });

        Picasso.with(getContext()).load(user.getUserImg()).resize(160,160).into(imgUser);

    }

    private void filterSearch(JSONObject fullJSONObject)  {
        Log.d("ApiResponse", "true");
        try {
            JSONObject allTrack = fullJSONObject.getJSONObject("tracks");
            getTrackResult(allTrack);
            Log.d("allTrack", trackArrayList+"\n"+trackArrayList.size());

            JSONObject allArtist = fullJSONObject.getJSONObject("artists");
            getArtistResult(allArtist);
            Log.d("allArtist",artistArrayList+"\n"+artistArrayList.size());

            JSONObject allPlaylist = fullJSONObject.getJSONObject("playlists");
            getPlaylistResult(allPlaylist);
            Log.d("allPlaylist",playlistsArrayList+"\n"+playlistsArrayList.size());

            JSONObject allAlbum = fullJSONObject.getJSONObject("albums");
            getAlbumResult(allAlbum);
            Log.d("allAlbum",albumArrayList+"\n"+albumArrayList.size());

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    private ArrayList<Track> getTrackResult (JSONObject TrackObject)
    {
        try {
            JSONArray allTracks = TrackObject.getJSONArray("items");

            for (int i = 0; i < allTracks.length(); i++) {
                JSONObject trackObject = allTracks.getJSONObject(i);
                String trackId = trackObject.getString("id");
                String trackName = trackObject.getString("name");
                String idAlbum = trackObject.getJSONObject("album").getString("id");
                String trackImg = trackObject.getJSONObject("album").getJSONArray("images").getJSONObject(0).getString("url");
                ArrayList<Artist> artists = getArtists(trackObject.getJSONArray("artists"));
                String previewUrl = trackObject.getString("preview_url");
                Track newTrack = new Track(trackId,trackName, idAlbum, trackImg,artists, previewUrl);
                trackArrayList.add(newTrack);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return trackArrayList;
    }

    private ArrayList<Artist> getArtistResult (JSONObject ArtistObject) throws JSONException {

        JSONArray allArtist = ArtistObject.getJSONArray("items");
        for(int i = 0; i < allArtist.length(); i++) {
            JSONObject artistObject = allArtist.getJSONObject(i);
            String artistId =  artistObject.getString("id");
            String artistName = artistObject.getString("name");
            JSONArray imagesArray = artistObject.getJSONArray("images");
            String artistImage = "";
            if (imagesArray.length() > 0) {
                artistImage = imagesArray.getJSONObject(0).getString("url");
            }
//            System.out.println(artistId+" "+artistName+" "+artistImage);
            Artist newArtist = new Artist(artistId,artistName,artistImage);
            artistArrayList.add(newArtist);
        }
        return artistArrayList;
    }

    private ArrayList<Playlists> getPlaylistResult (JSONObject PlaylistObject)
    {
        try {
            JSONArray allPlaylist = PlaylistObject.getJSONArray("items");

            for (int i = 0; i < allPlaylist.length(); i++) {
                JSONObject playlistObject = allPlaylist.getJSONObject(i);
                String playlistId = playlistObject.getString("id");
                String playlistImg = playlistObject.getJSONArray("images").getJSONObject(0).getString("url");
                String playlistName = playlistObject.getString("name");
                ArrayList<Track> track = getSpecificTrackFromPlaylist(playlistId);
                Boolean isPublic = Boolean.valueOf(playlistObject.getString("public"));
                Playlists newPlaylist = new Playlists(playlistId,playlistImg,playlistName, track, isPublic);
                playlistsArrayList.add(newPlaylist);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return playlistsArrayList;
    }

    private ArrayList<Album> getAlbumResult (JSONObject AlbumObject )
    {
        try {
            JSONArray allAlbum = AlbumObject.getJSONArray("items");

            for (int i = 0; i < allAlbum.length(); i++) {
                JSONObject albumObject = allAlbum.getJSONObject(i);
                String albumId = albumObject.getString("id");
                String albumName = albumObject.getString("name");
                String albumImage = albumObject.getJSONArray("images").getJSONObject(0).getString("url");
                String albumReleaseDate = albumObject.getString("release_date");
                ArrayList<Artist> artists = getArtists(albumObject.getJSONArray("artists"));
                ArrayList<Track> tracks = getSpecificTrackFromAlbum(albumId);
                Album newAlbum = new Album(albumId,albumName,albumImage,albumReleaseDate, artists, tracks);
                albumArrayList.add(newAlbum);
            }

        }catch (JSONException e){

        }
//        System.out.println("hummmmmm "+albumArrayList);
        return albumArrayList;
    }

    private ArrayList<Track> getSpecificTrackFromPlaylist(String id){
        String apiUrl = "https://api.spotify.com/v1/playlists/"+id+"/tracks";
        ArrayList<Track> theTracks = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, apiUrl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                apiResponse = response;
                searchApiResponse(apiResponse);
                System.out.println(searchObject);
                try {
                    JSONArray itemsArray = searchObject.getJSONArray("items");

                    for (int i = 0; i < itemsArray.length(); i++) {
                        JSONObject trackObject = itemsArray.getJSONObject(i);
                        JSONObject trackOfPlaylist = trackObject.getJSONObject("track");
                        String trackId = trackOfPlaylist.getString("id");
                        String trackName = trackOfPlaylist.getString("name");
                        String idAlbum = trackOfPlaylist.getJSONObject("album").getString("id");
                        String trackImg = trackOfPlaylist.getJSONObject("album").getJSONArray("images").getJSONObject(0).getString("url");
                        ArrayList<Artist> artists = getArtists(trackOfPlaylist.getJSONArray("artists"));
                        String previewUrl = trackOfPlaylist.getString("preview_url");

                        Track newTrack = new Track(trackId, trackName, idAlbum, trackImg, artists, previewUrl);
                        theTracks.add(newTrack);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
//                System.out.println("DONEEE   "+theTracks);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Search", "Failed to get user playlists. Error: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // Add the access token to the request headers
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + ACCESS_TOKEN);
                return headers;
            }
        };
        // Add the request to the Volley queue
        requestQueue.add(request);

        return theTracks;
    }

    private ArrayList<Track> getSpecificTrackFromAlbum(String id){
        String apiUrl = "https://api.spotify.com/v1/albums/"+id;
        ArrayList<Track> theTracks = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, apiUrl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                apiResponse = response;
                searchApiResponse(apiResponse);
                System.out.println(searchObject);
                try {
                    JSONObject tracks = searchObject.getJSONObject("tracks");
                    JSONArray tracksItem = tracks.getJSONArray("items");
                    for(int i=0;i<tracksItem.length();i++) {
                        JSONObject trackObject = tracksItem.getJSONObject(i);
                        String trackId = trackObject.getString("id");
                        String trackName = trackObject.getString("name");
                        String idAlbum = id;
                        String trackImg = searchObject.getJSONArray("images").getJSONObject(0).getString("url");
                        ArrayList<Artist> artists = getArtists(trackObject.getJSONArray("artists"));
                        String previewUrl = trackObject.getString("preview_url");
                        Track newTrack = new Track(trackId,trackName, idAlbum, trackImg,artists, previewUrl);
                        theTracks.add(newTrack);
                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Search", "Failed to get user playlists. Error: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // Add the access token to the request headers
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + ACCESS_TOKEN);
                return headers;
            }
        };

        // Add the request to the Volley queue
        requestQueue.add(request);
//        System.out.println("DONEEE "+theTracks);
        return theTracks;
    }

    private ArrayList<Artist> getArtists(JSONArray artistsArray) {
        ArrayList<Artist> newArray = new ArrayList<>();
        try {
            for (int i = 0; i < artistsArray.length(); i++) {
                JSONObject artistobject = artistsArray.getJSONObject(i);
                Artist artist = getSpecificArtist(artistobject.getString("href"));
                newArray.add(artist);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return newArray;
    }

    private Artist getSpecificArtist(String url){
        String apiUrl = "" + url;
        Artist newartist = new Artist();
        StringRequest request = new StringRequest(Request.Method.GET, apiUrl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject artist = new JSONObject(response);
                    String id = artist.getString("id");
                    String name = artist.getString("name");
                    String img = artist.getJSONArray("images").getJSONObject(0).getString("url");
                    newartist.setId(id);
                    newartist.setName(name);
                    newartist.setImage(img);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Search", "Failed to get user playlists. Error: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // Add the access token to the request headers
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + ACCESS_TOKEN);
                return headers;
            }
        };

        // Add the request to the Volley queue
        requestQueue.add(request);
        return newartist;
    }

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(edtInputSearch, InputMethodManager.SHOW_IMPLICIT);
    }
    private void searchThings(String thing) {

        artistArrayList.clear();
        playlistsArrayList.clear();
        albumArrayList.clear();
        trackArrayList.clear();

        String accessToken = ACCESS_TOKEN;
        String apiUrl = "https://api.spotify.com/v1/search?q=" + thing + "&type=album%2Cartist%2Ctrack%2Cplaylist&limit=10";
        Log.d("searchKey", apiUrl);
        StringRequest request = new StringRequest(Request.Method.GET, apiUrl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Store the response in the variable
                apiResponse = response;
                processApiResponse(apiResponse);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error response
                Log.e("Search", "Failed to get user playlists. Error: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // Add the access token to the request headers
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }
        };

        // Add the request to the Volley queue
        requestQueue.add(request);

    }

    private void processApiResponse(String response) {
        // You can handle the response here or pass it to another method
        try {
            fullSearchObject = new JSONObject(response);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
//        Log.d("ApiResponse", fullSearchObject+"");
        filterSearch(fullSearchObject);
        // Add your logic to handle the response
    }

    private void searchApiResponse(String response) {
        try {
            searchObject = new JSONObject(response);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void showFullScreenLoader() {
        // Inflate the custom layout for the dialog
        View dialogView = getLayoutInflater().inflate(R.layout.activity_loader_home, null);

        // Create an AlertDialog with a custom layout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(false); // Prevent dismissal by tapping outside

        // Create and show the AlertDialog
        AlertDialog fullScreenDialog = alertDialogBuilder.create();
        fullScreenDialog.show();

        // Use Handler to dismiss the dialog after 5 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fullScreenDialog.dismiss();
                // Additional processing if needed
            }
        }, 5000); // 5000 milliseconds = 5 seconds
    }


    @Override
    public void onClick(View view, int position, String category) {

    }

    @Override
    public void listOnClick(View view, int position) {

    }
}
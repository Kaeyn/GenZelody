package android2.genzelody;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationBarView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class Home extends AppCompatActivity implements SlidingPanelToggleListener {

    FrameLayout frameFragmentHome, musicBox;
    BottomNavigationView bttNav;
    private String ACCESS_TOKEN = "";
    private SlidingUpPanelLayout slidingUpPanelLayout;

    private LinearLayout divCurrentTrack;

    private ArrayList<Playlists> MyPlayList = new ArrayList<>();
    private ArrayList<Playlists> FeaturePlayList = new ArrayList<>();
    private ArrayList<Track> RecommendedTrackList = new ArrayList<>();
    ExecutorService trackExecutor = Executors.newFixedThreadPool(5);
    User user = new User();
    private RequestQueue requestQueue;

    private SlidingPanelToggleListener slidingPanelToggleListener;
    FragmentManager fm;
    FragmentTransaction ft;

    TextView txtCurTrack, txtcurTrackArtist;
    ImageView imgCurTrack;
    Button btnStopnPlayTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        addControls();
        addEvents();
        Intent intentLogin = getIntent();
        ACCESS_TOKEN = intentLogin.getStringExtra("accessToken");
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        showFullScreenLoader();
        fetchPlaylistsAsync();
        System.out.println(ACCESS_TOKEN);
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
    }

    private CompletableFuture<Void> fetchPlaylistsAsync() {
        return CompletableFuture.runAsync(() -> {
            try {
                user=getUserInfo();
                getFeaturePlaylists();
                Thread.sleep(1500);
                getUserPlaylists();
                Thread.sleep(1500);
                getRecommendedTrack();
                Thread.sleep(1200);
                loadFragment(new Fragment_Home(ACCESS_TOKEN,MyPlayList,FeaturePlayList,RecommendedTrackList,user));
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


        }, trackExecutor);
    }


    private void addControls(){
        frameFragmentHome = findViewById(R.id.frameFragmentHome);
        bttNav = findViewById(R.id.bttnav);
        slidingUpPanelLayout = findViewById(R.id.slidingUpPanel);
        txtCurTrack = findViewById(R.id.txtTrackNameCurPlay);
        txtcurTrackArtist = findViewById(R.id.txtTrackArtistCurPlay);
        imgCurTrack = findViewById(R.id.imgCurPlay);
        btnStopnPlayTrack = findViewById(R.id.btnStopnPlay);
        musicBox = findViewById(R.id.musicBox);
    }

    private void addEvents(){
        bttNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int idFrame = item.getItemId();
                if(idFrame == R.id.home){
                    loadFragment(new Fragment_Home(ACCESS_TOKEN,MyPlayList,FeaturePlayList,RecommendedTrackList, user));
                    return true;
                } else if (idFrame == R.id.search) {
                    loadFragment(new Fragment_Search(ACCESS_TOKEN, user, RecommendedTrackList));
                    return true;
                }
                else if (idFrame == R.id.library) {
                    loadFragment(new Fragment_Library(ACCESS_TOKEN, MyPlayList, user, RecommendedTrackList));
                    return true;
                }
                return true;
            }
        });

        musicBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSlidingPanel();
            }
        });

        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                // Not needed for your case, but you can use it if necessary
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    // Sliding panel collapsed, show musicBox
                    musicBox.setVisibility(View.VISIBLE);
                } else if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    // Sliding panel expanded, hide musicBox
                    musicBox.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    private void toggleSlidingPanel(){
        runOnUiThread(() -> {
            if (slidingUpPanelLayout != null) {
                if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    musicBox.setVisibility(View.INVISIBLE);
                    slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                } else {
                    musicBox.setVisibility(View.VISIBLE);
                    slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                }
            }
        });
    }


    public void loadFragment(Fragment fragment){
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.frameFragmentHome, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void loadPlayTrackFragment(ArrayList<Track> tracks, String name, int index) {
        Fragment fragment = new Fragment_Play_Track(tracks, name, index);
        if (fragment instanceof SlidingPanelToggleListener) {
            slidingPanelToggleListener = (SlidingPanelToggleListener) fragment;
        }
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.frameForPlayTrack, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }
    public void updateCurrentPlayBox(String img, String name, String artist){
        txtCurTrack.setText(name);
        txtcurTrackArtist.setText(artist);
        Picasso.with(getApplicationContext()).load(img).resize(60,60).into(imgCurTrack);
    }

    private void getUserFavPlayList(){
        String apiUrl = "https://api.spotify.com/v1/me/tracks";
        String id = "0";
        String name = "Yêu Thích";
        String img = String.valueOf(R.drawable.yeuthich);
        ArrayList<Track> tracks = getTracks(apiUrl);
        Boolean isPublic = false;
        Playlists playlists = new Playlists(id, img, name, tracks, isPublic);
        MyPlayList.add(playlists);
    }


    private void getRecommendedTrack(){
        String apiUrl = "https://api.spotify.com/v1/recommendations?limit=7&market=ES&seed_artists=5HZtdKfC4xU0wvhEyYDWiY";
        StringRequest request = new StringRequest(Request.Method.GET, apiUrl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    String id = "";
                    String name = "";
                    String idAlbum = "";
                    String img = "";
                    String previewUrl = "";
                    JSONObject track = new JSONObject(response);
                    JSONArray allTracks = track.getJSONArray("tracks");
                    for (int i = 0; i < allTracks.length(); i++) {
                        JSONObject trackObject = allTracks.getJSONObject(i);
                        id = trackObject.getString("id");
                        name = trackObject.getString("name");
                        idAlbum = trackObject.getJSONObject("album").getString("id");
                        img = trackObject.getJSONObject("album").getJSONArray("images").getJSONObject(0).getString("url");
                        ArrayList<Artist> artists = getArtists(trackObject.getJSONArray("artists"));
                        previewUrl = trackObject.getString("preview_url");
                        Track newTrack = new Track(id,name, idAlbum, img,artists, previewUrl);
                        RecommendedTrackList.add(newTrack);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LoginActivity", "Failed to get user playlists. Error: " + error.getMessage());
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
        requestQueue.add(request);
    }

    private void getFeaturePlaylists() {
        String apiUrl = "https://api.spotify.com/v1/browse/featured-playlists?limit=5";
        StringRequest request = new StringRequest(Request.Method.GET, apiUrl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject playLists = new JSONObject(response);
                    JSONObject playListObject = playLists.getJSONObject("playlists");
                    JSONArray allPlayLits = playListObject.getJSONArray("items");
                    for (int i = 0; i < allPlayLits.length(); i++) {
                        JSONObject playlistObject = allPlayLits.getJSONObject(i);
                        String id = playlistObject.getString("id");
                        String name = playlistObject.getString("name");
                        String img = playlistObject.getJSONArray("images").getJSONObject(0).getString("url");
                        String url = playlistObject.getJSONObject("tracks").getString("href");
                        ArrayList<Track> tracks = getTracks(url);
                        Boolean isPublic;
                        try {
                            isPublic = playlistObject.getBoolean("public");
                        }catch (JSONException e){
                            isPublic = true;
                        }
                        Playlists playlists = new Playlists(id, img, name, tracks, isPublic);
                        FeaturePlayList.add(playlists);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LoginActivity", "Failed to get feature playlists. Error: " + error.getMessage());
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
    }


    private void getUserPlaylists() {
        getUserFavPlayList();
        String apiUrl = "https://api.spotify.com/v1/me/playlists";
        StringRequest request = new StringRequest(Request.Method.GET, apiUrl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    String id = "";
                    String name = "";
                    String img = "";
                    String url = "";
                    ArrayList<Track> tracks;

                    JSONObject playLists = new JSONObject(response);
                    JSONArray allPlayLits = playLists.getJSONArray("items");
                    for (int i = 0; i < allPlayLits.length(); i++) {
                        JSONObject playlistObject = allPlayLits.getJSONObject(i);
                        id = playlistObject.getString("id");
                        name = playlistObject.getString("name");
                        img = playlistObject.getJSONArray("images").getJSONObject(0).getString("url");
                        url = playlistObject.getJSONObject("tracks").getString("href");
                        tracks = getTracks(url);
                        Boolean isPublic = playlistObject.getBoolean("public");
                        Playlists playlists = new Playlists(id, img, name, tracks, isPublic);
                        MyPlayList.add(playlists);
                    }
                    System.out.println(MyPlayList + "");

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LoginActivity", "Failed to get user playlists. Error: " + error.getMessage());
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


        requestQueue.add(request);
    }

    private ArrayList<Track> getTracks(String url) {
        String accessToken = ACCESS_TOKEN;
        String apiUrl = "" + url;
        ArrayList<Track> trackArrayList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, apiUrl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    String id = "";
                    String name = "";
                    String idAlbum = "";
                    String img = "";
                    String previewUrl = "";
                    JSONObject track = new JSONObject(response);
                    JSONArray allTracks = track.getJSONArray("items");
                    for (int i = 0; i < allTracks.length(); i++) {
                        JSONObject trackObject = allTracks.getJSONObject(i);
                        JSONObject trackitem = trackObject.getJSONObject("track");
                        id = trackitem.getString("id");
                        name = trackitem.getString("name");
                        idAlbum = trackitem.getJSONObject("album").getString("id");
                        img = trackitem.getJSONObject("album").getJSONArray("images").getJSONObject(0).getString("url");
                        ArrayList<Artist> artists = getArtists(trackitem.getJSONArray("artists"));
                        previewUrl = trackitem.getString("preview_url");
                        Track newTrack = new Track(id,name, idAlbum, img,artists, previewUrl);
                        trackArrayList.add(newTrack);
                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LoginActivity", "Failed to get track . Error: " + error.getMessage());
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
        return trackArrayList;
    }

    private ArrayList<Artist> getArtists(JSONArray artistsArray) {
        ArrayList<Artist> newArray = new ArrayList<>();
        try {
            for (int i = 0; i < artistsArray.length(); i++) {
                JSONObject artistobject = artistsArray.getJSONObject(i);
                Artist artist = new Artist(artistobject.getString("id"), artistobject.getString("name"), null);
//                Artist artist = getSpecificArtist(artistobject.getString("href"));
                newArray.add(artist);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return newArray;
    }

    private void showFullScreenLoader() {
        // Inflate the custom layout for the dialog
        View dialogView = getLayoutInflater().inflate(R.layout.activity_loader_home, null);

        // Create an AlertDialog with a custom layout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
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
                    String img = "";
                    try {
                        img = artist.getJSONArray("images").getJSONObject(0).getString("url");
                    }catch (JSONException e){
                        img = "";
                    }

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
                Log.e("LoginActivity", "Failed to get artist. Error: " + error.getMessage());
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

    private User getUserInfo()
    {
        String apiUrl = "https://api.spotify.com/v1/me";
        User newUser = new User();
        StringRequest request = new StringRequest(Request.Method.GET, apiUrl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject userObject = new JSONObject(response);
                    String userName = userObject.getString("display_name");
                    String userImg="https://i.scdn.co/image/ab67616d00001e02ff9ca10b55ce82ae553c8228";
                    if(userObject.getJSONArray("images").length()>0){
                        userImg = userObject.getJSONArray("images").getJSONObject(0).getString("url");
                    }
                    System.out.println(userImg+"d");
                    newUser.setUserName(userName);
                    newUser.setUserImg(userImg);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LoginActivity", "Failed to get artist. Error: " + error.getMessage());
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
        return newUser;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }



    @Override
    public void setTrackLists(ArrayList<Track> tracks, String name, int index) {
        loadPlayTrackFragment(tracks,name,index);
    }

    @Override
    public void getCurrentTrack(String img, String name, String artist) {
        updateCurrentPlayBox(img,name, artist);
    }

}
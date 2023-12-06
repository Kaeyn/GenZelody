package android2.genzelody;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity {

    FrameLayout frameFragmentHome;
    BottomNavigationView bttNav;
    private String ACCESS_TOKEN = "";

    private ArrayList<Playlists> MyPlayList = new ArrayList<>();
    private ArrayList<Playlists> FeaturePlayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        addControls();
        addEvents();
        Intent intentLogin = getIntent();
        ACCESS_TOKEN = intentLogin.getStringExtra("accessToken");
        Log.d("keyyyy",ACCESS_TOKEN+" ");
//        getUserFavPlayList();
        getUserPlaylists();
//        getFeaturePlaylists();
    }

    private void addControls(){
        frameFragmentHome = findViewById(R.id.frameFragmentHome);
        bttNav = findViewById(R.id.bttnav);
    }

    private void addEvents(){
        loadFragment(new Fragment_Home());
        bttNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int idFrame = item.getItemId();
                if(idFrame == R.id.home){
                    loadFragment(new Fragment_Home(ACCESS_TOKEN));
                    return true;
                } else if (idFrame == R.id.search) {
                    Log.d("home",ACCESS_TOKEN+"");
                    loadFragment(new Fragment_Search(ACCESS_TOKEN));
                    return true;
                }
                else if (idFrame == R.id.library) {
                    loadFragment(new Fragment_Library(ACCESS_TOKEN, MyPlayList));
                    return true;
                }
                return true;
            }
        });
    }

    public void loadFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frameFragmentHome, fragment);
        ft.commit();
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

        // Add the request to the Volley queue
        Volley.newRequestQueue(getApplicationContext()).add(request);

    }

    private void getUserPlaylists() {
        getUserFavPlayList();
        String apiUrl = "https://api.spotify.com/v1/me/playlists";
        StringRequest request = new StringRequest(Request.Method.GET, apiUrl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject playLists = new JSONObject(response);
                    JSONArray allPlayLits = playLists.getJSONArray("items");
                    for (int i = 0; i < allPlayLits.length(); i++) {
                        JSONObject playlistObject = allPlayLits.getJSONObject(i);
                        String id = playlistObject.getString("id");
                        String name = playlistObject.getString("name");
                        String img = playlistObject.getJSONArray("images").getJSONObject(0).getString("url");
                        String url = playlistObject.getJSONObject("tracks").getString("href");
                        ArrayList<Track> tracks = getTracks(url);
                        Boolean isPublic = playlistObject.getBoolean("public");
                        Playlists playlists = new Playlists(id, img, name, tracks, isPublic);
                        MyPlayList.add(playlists);
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

        // Add the request to the Volley queue
        Volley.newRequestQueue(getApplicationContext()).add(request);

    }

    private ArrayList<Track> getTracks(String url) {
        String accessToken = ACCESS_TOKEN;
        String apiUrl = "" + url;
        ArrayList<Track> trackArrayList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, apiUrl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject track = new JSONObject(response);
                    Log.d("TRACK", track + "");
                    JSONArray allTracks = track.getJSONArray("items");
                    for (int i = 0; i < allTracks.length(); i++) {
                        JSONObject trackObject = allTracks.getJSONObject(i);
                        JSONObject trackitem = trackObject.getJSONObject("track");
                        String id = trackitem.getString("id");
                        String name = trackitem.getString("name");
                        String idAlbum = trackitem.getJSONObject("album").getString("id");
                        String img = trackitem.getJSONObject("album").getJSONArray("images").getJSONObject(0).getString("url");
                        ArrayList<Artist> artists = getArtists(trackitem.getJSONArray("artists"));
                        String previewUrl = trackitem.getString("preview_url");
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
                Log.e("LoginActivity", "Failed to get user playlists. Error: " + error.getMessage());
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
        Volley.newRequestQueue(getApplicationContext()).add(request);
        return trackArrayList;
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

        // Add the request to the Volley queue
        Volley.newRequestQueue(getApplicationContext()).add(request);
        return newartist;
    }
}
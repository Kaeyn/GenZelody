package android2.genzelody;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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
public class Fragment_Search extends Fragment {


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

    ArrayList<Track> trackArrayList = new ArrayList<>();
    ArrayList<Artist> artistArrayList = new ArrayList<>();
    ArrayList<Album> albumArrayList = new ArrayList<>();
    ArrayList<Playlists> playlistsArrayList = new ArrayList<>();

    public Fragment_Search() {
        // Required empty public constructor
    }

    public Fragment_Search(String accessToken) {
        // Required empty public constructor
        ACCESS_TOKEN = accessToken;
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
        addViewControls(view);
        addEvent();

        return view;
    }

    private void addViewControls(View view){
        edtInputSearch = view.findViewById(R.id.edtInputSearch);
        edtInputSearch.requestFocus();
        showKeyboard();
    }
    void addEvent(){
        edtInputSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                    // Your function to execute when Enter is pressed

                    searchThings(edtInputSearch.getText().toString());
//                    Log.d("FinalResult", "ohYeah" + fullSearchObject);
//                    // Return true to indicate that the event has been handled
                    return true;
                }
                // Return false to allow the default action to occur
                return false;
            }
        });
    }

    private void filterSearch(JSONObject fullJSONObject)  {
        Log.d("ApiResponse", "true");
        try {
            JSONObject allTrack = fullJSONObject.getJSONObject("tracks");

            getTrackResult(allTrack);
            Log.d("allTrack", trackArrayList+"");

            JSONObject allPlalist = fullJSONObject.getJSONObject("playlist");
            Log.d("allPlalist",allPlalist+"");

            JSONObject allArtist = fullJSONObject.getJSONObject("artist");
            Log.d("allArtist",allArtist+"");

            JSONObject allAlbum = fullJSONObject.getJSONObject("album");
            Log.d("allAlbum",allAlbum+"");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    private ArrayList<Track> getTrackResult (JSONObject TrackObject)
    {
        try {
            JSONArray allTracks = TrackObject.getJSONArray("items");
//            for (int i = 0; i < allTracks.length(); i++) {
//                JSONObject trackObject = allTracks.getJSONObject(i);
//                JSONObject trackitem = trackObject.getJSONObject("track");
//                String id = trackitem.getString("id");
//                String name = trackitem.getString("name");
//                String idAlbum = trackitem.getJSONObject("album").getString("id");
//                String img = trackitem.getJSONObject("album").getJSONArray("images").getJSONObject(0).getString("url");
//                ArrayList<Artist> artists = getArtists(trackitem.getJSONArray("artists"));
//                String previewUrl = trackitem.getString("preview_url");
//                Track newTrack = new Track(id,name, idAlbum, img,artists, previewUrl);
//                trackArrayList.add(newTrack);
//            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
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
        Volley.newRequestQueue(getContext()).add(request);
        return newartist;
    }

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(edtInputSearch, InputMethodManager.SHOW_IMPLICIT);
    }
    private void searchThings(String thing) {

        String accessToken = ACCESS_TOKEN;

        String apiUrl = "https://api.spotify.com/v1/search?q=" + thing + "&type=album%2Cartist%2Ctrack%2Cplaylist";

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
        Volley.newRequestQueue(getContext()).add(request);

    }

    // Example method to process the API response
    private void processApiResponse(String response) {
        // You can handle the response here or pass it to another method
//
        try {
            fullSearchObject = new JSONObject(response);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
//        Log.d("ApiResponse", fullSearchObject+"");
        filterSearch(fullSearchObject);
        // Add your logic to handle the response
    }




}
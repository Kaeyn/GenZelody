package android2.genzelody;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface SpotifyApiService {
    @GET("v1/me/playlists")
    Call<PlaylistResponse> getUserPlaylists(@Header("Authorization") String authorization);
    @GET("v1/playlists/{playlist_id}/tracks")
    Call<List<Track>> getUserPlaylistTracks(@Header("Authorization") String authorization,  @Path("playlistId") String playlistId);

}

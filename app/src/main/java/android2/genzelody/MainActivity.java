package android2.genzelody;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

public class MainActivity extends AppCompatActivity {
    private static final String CLIENT_ID = "d59beb03b8e14681892c14c70a8849d3";
    private static final String REDIRECT_URI = "https://testingspotifyapi/callback";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AuthorizationRequest.Builder builder = new AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{"streaming", "user-library-read","playlist-read-private"});
        AuthorizationRequest request = builder.build();
        AuthorizationClient.openLoginInBrowser(this, request);
    }
}
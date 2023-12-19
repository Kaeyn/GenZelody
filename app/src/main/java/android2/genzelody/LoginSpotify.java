package android2.genzelody;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.spotify.sdk.android.auth.AuthorizationResponse;

public class LoginSpotify extends AppCompatActivity {
    private String ACCESS_TOKEN = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_spotify);
        onNewIntent(this.getIntent());
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Uri uri = intent.getData();
        Log.d("LoginActivity", "URI: ");
        if (uri != null) {
            AuthorizationResponse response = AuthorizationResponse.fromUri(uri);
            Log.d("LoginActivity", "RES: " +response);
            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    ACCESS_TOKEN = response.getAccessToken();
                    Intent newIntent = new Intent(this, Home.class);
                    newIntent.putExtra("accessToken",ACCESS_TOKEN);
                    startActivity(newIntent);
                    break;
                case ERROR:
                    break;

                default:

            }
        }
    }

    @Override
    protected void onStop() {
        finish();
        super.onStop();
    }
}
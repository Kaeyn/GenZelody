package android2.genzelody;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Loader_Search extends AppCompatActivity {
    private static final long DELAY_TIME = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader_search);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Loader_Search.this, Fragment_Detail_Artist.class);
                startActivity(intent);
                finish();
            }
        }, DELAY_TIME);
    }
}
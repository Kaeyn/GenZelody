package android2.genzelody;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class Loader_Home extends AppCompatActivity {

    private static final long DELAY_TIME = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader_home);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Khi thời gian độ trễ kết thúc, chạy Intent để chuyển sang Activity mới
                Intent intent = new Intent(Loader_Home.this, Home.class);
                intent.putExtra("open", "true");
                startActivity(intent);
                finish(); // Đóng Activity hiện tại nếu bạn muốn
            }
        }, DELAY_TIME);
    }
}
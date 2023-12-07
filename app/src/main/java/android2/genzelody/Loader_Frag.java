package android2.genzelody;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Loader_Frag extends AppCompatActivity {

    private static final long DELAY_TIME = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader_frag);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Khi thời gian độ trễ kết thúc, chạy Intent để chuyển sang Activity mới
                Intent intent = new Intent(Loader_Frag.this, Fragment_Search.class);
                intent.putExtra("open", "true");
                startActivity(intent);
                finish(); // Đóng Activity hiện tại nếu bạn muốn
            }
        }, DELAY_TIME);
    }
}
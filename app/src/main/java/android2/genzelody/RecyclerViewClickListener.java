package android2.genzelody;

import android.view.View;

public interface RecyclerViewClickListener {
    void onClick(View view, int position, String category);

    void listOnClick(View view, int position);

    void reclistOnClick(View view, int position);
}

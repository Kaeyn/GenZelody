package android2.genzelody;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    ImageView imgTrack;
    TextView tvTitleDanhChoBan;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        imgTrack = itemView.findViewById(R.id.imgTrack);
        tvTitleDanhChoBan = itemView.findViewById(R.id.tvTitleDanhChoBan);
    }
}

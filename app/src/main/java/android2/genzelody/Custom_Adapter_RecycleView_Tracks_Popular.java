package android2.genzelody;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Custom_Adapter_RecycleView_Tracks_Popular extends RecyclerView.Adapter<MyViewHolder> {

    Context context;
    ArrayList<Track> trackList;

    public Custom_Adapter_RecycleView_Tracks_Popular(Context context, ArrayList<Track> trackList){
        this.context = context;
        this.trackList = trackList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_item_track_bigger, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvTitleDanhChoBan.setText(trackList.get(position).getName());
        Picasso.with(context.getApplicationContext()).load(trackList.get(position).getImg()).resize(160,160).into(holder.imgTrack);

    }

    @Override
    public int getItemCount() {
        return trackList.size();
    }
}

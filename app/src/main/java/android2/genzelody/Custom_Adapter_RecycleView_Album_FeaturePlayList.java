package android2.genzelody;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
public class Custom_Adapter_RecycleView_Album_FeaturePlayList extends RecyclerView.Adapter<MyViewHolder> {
    Context context;
    ArrayList<Playlists> playlists;
    public Custom_Adapter_RecycleView_Album_FeaturePlayList(Context context, ArrayList<Playlists> playlists){
        this.context = context;
        this.playlists = playlists;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_item_track_bigger, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvTitleDanhChoBan.setText(playlists.get(position).getName());
        Picasso.with(context.getApplicationContext()).load(playlists.get(position).getImages()).resize(400,400).into(holder.imgTrack);
    }
    @Override
    public int getItemCount() {
        return playlists.size();
    }
}
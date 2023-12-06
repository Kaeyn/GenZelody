package android2.genzelody;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Custom_Adapter_RecycleView_Tracks_Popular extends RecyclerView.Adapter<MyViewHolder> {

    Context context;
    List<Track> trackList;

    public Custom_Adapter_RecycleView_Tracks_Popular(Context context, List<Track> trackList){
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
        List<Album> albumList = null;
        holder.tvTitleDanhChoBan.setText(trackList.get(position).getName());
        holder.imgTrack.setImageResource(Integer.parseInt(trackList.get(position).getAlbumImageById(
                trackList.get(position).getId(), albumList
        )));
    }

    @Override
    public int getItemCount() {
        return trackList.size();
    }
}

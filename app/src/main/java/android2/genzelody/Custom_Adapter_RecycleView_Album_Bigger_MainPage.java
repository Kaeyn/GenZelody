package android2.genzelody;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class Custom_Adapter_RecycleView_Album_Bigger_MainPage extends RecyclerView.Adapter<MyViewHolder> {
    Context context;
    List<Album> albumList;
    public Custom_Adapter_RecycleView_Album_Bigger_MainPage(Context context, List<Album> albumList){
        this.context = context;
        this.albumList = albumList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_item_track_bigger, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvTitleDanhChoBan.setText(albumList.get(position).getName());
        holder.imgTrack.setImageResource(Integer.parseInt(albumList.get(position).getImage()));
    }
    @Override
    public int getItemCount() {
        return albumList.size();
    }
}
package android2.genzelody;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Custom_Adapter_RecycleView_Tracks_Popular extends RecyclerView.Adapter<MyViewHolder> {

    Context context;
    ArrayList<Track> trackList;
    private RecyclerViewClickListener mListener;



    public Custom_Adapter_RecycleView_Tracks_Popular(Context context, ArrayList<Track> trackList, RecyclerViewClickListener listener){
        this.context = context;
        this.trackList = trackList;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_item_track_bigger, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tvTitleDanhChoBan.setText(trackList.get(position).getName());
        Picasso.with(context.getApplicationContext()).load(trackList.get(position).getImg()).resize(160,160).into(holder.imgTrack);
        String category = "populartrack";
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onClick(view, position, category);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trackList.size();
    }
}

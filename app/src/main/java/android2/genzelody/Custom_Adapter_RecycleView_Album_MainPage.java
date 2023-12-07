package android2.genzelody;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
public class Custom_Adapter_RecycleView_Album_MainPage extends RecyclerView.Adapter<MyViewHolder> {
    Context context;
    ArrayList<Playlists> urPlayList;
    public Custom_Adapter_RecycleView_Album_MainPage(Context context, ArrayList<Playlists> urPlayList){
            this.context = context;
            this.urPlayList = urPlayList;
        }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_item_track, parent, false));
    }

    @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            holder.tvTitleDanhChoBan.setText(urPlayList.get(position).getName());
        Picasso.with(context.getApplicationContext()).load(urPlayList.get(position).getImages()).resize(400,400).into(holder.imgTrack);
        }
        @Override
        public int getItemCount() {
            return urPlayList.size();
        }
    }
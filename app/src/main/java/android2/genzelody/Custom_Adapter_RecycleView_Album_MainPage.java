package android2.genzelody;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
public class Custom_Adapter_RecycleView_Album_MainPage extends RecyclerView.Adapter<MyViewHolder> {
    Context context;
    ArrayList<Playlists> urPlayList;
    private RecyclerViewClickListener mListener;

    public Custom_Adapter_RecycleView_Album_MainPage(Context context, ArrayList<Playlists> urPlayList, RecyclerViewClickListener listener){
            this.context = context;
            this.urPlayList = urPlayList;
            this.mListener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_item_track, parent, false));
    }

    @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

            holder.tvTitleDanhChoBan.setText(urPlayList.get(position).getName());
            try {
                int drawableResourceId = Integer.parseInt(urPlayList.get(position).getImages());
                Drawable drawable = ContextCompat.getDrawable(context, drawableResourceId);
                holder.imgTrack.setImageDrawable(drawable);
            } catch (NumberFormatException e) {
                // If the image is not a drawable resource ID (assuming it's a URL)
                Picasso.with(context.getApplicationContext()).load(urPlayList.get(position).getImages()).resize(100,100).into(holder.imgTrack);
            }
            String category = "myplaylist";
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onClick(view, position, category);
                }
            });
    }
        @Override
        public int getItemCount() {
            return urPlayList.size();
        }
    }
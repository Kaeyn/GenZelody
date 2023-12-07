package android2.genzelody;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
public class Custom_Adapter_Grid_MainPage extends RecyclerView.Adapter<Custom_Adapter_Grid_MainPage.MyViewHolder> {

    private Context context;
    private ArrayList<Playlists> playlists;

    private AdapterView.OnItemClickListener onItemClickListener;

    public Custom_Adapter_Grid_MainPage(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<Playlists> playlists){
        this.playlists = playlists;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_item_grid, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Playlists playlistitem = playlists.get(position);
        holder.tvGridPlayList.setText(playlistitem.getName());

        try {
            int drawableResourceId = Integer.parseInt(playlistitem.getImages());
            Drawable drawable = ContextCompat.getDrawable(context, drawableResourceId);
            holder.imgGridPlayList.setImageDrawable(drawable);
        } catch (NumberFormatException e) {
            // If the image is not a drawable resource ID (assuming it's a URL)
            Picasso.with(context.getApplicationContext()).load(playlistitem.getImages()).resize(50,50).into(holder.imgGridPlayList);
        }
    }
    @Override
    public int getItemCount() {
        return playlists.size();
    }

    // Set the click listener
    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgGridPlayList;
        private TextView tvGridPlayList;
        public MyViewHolder(@NonNull View view){
            super(view);

            imgGridPlayList = view.findViewById(R.id.imgGridPlayList);
            tvGridPlayList = view.findViewById(R.id.tvGridPlayList);
        }
    }
}
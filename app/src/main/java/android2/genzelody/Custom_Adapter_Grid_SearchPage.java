package android2.genzelody;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Custom_Adapter_Grid_SearchPage extends RecyclerView.Adapter<Custom_Adapter_Grid_SearchPage.MyViewHolder>{

    private Context context;
    private ArrayList<Track> tracks;
    private ArrayList<Artist> artists;

    RecyclerViewClickListener mListener;

    public Custom_Adapter_Grid_SearchPage(Context context,RecyclerViewClickListener listener) {
        this.context = context;
        this.mListener = listener;
    }

    public void setData(ArrayList<Track> trackArrayList,ArrayList<Artist> artistArrayList){
        this.tracks = trackArrayList;
        this.artists = artistArrayList;
        Log.d("ListArtist", "setData: "+artistArrayList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Custom_Adapter_Grid_SearchPage.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_item_grid_search, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        int adjustedPosition = position / 2;

        if (position % 2 == 0) {
            // Even position, display artist information
            Track track = tracks.get(adjustedPosition);
            holder.tvGridSearch.setText(track.getName());
            Picasso.with(context.getApplicationContext()).load(track.getImg()).resize(320, 320).into(holder.imgGridSearcht);

            String artistStr = "";
            for (Artist artistItem : track.getArtists()) {
                Log.d("nameartist", "onBindViewHolder: "+artistItem);
                artistStr += artistItem.getName()+" - ";
            }
            holder.artist.setText(artistStr.substring(0,artistStr.length()-3));

        } else {
            Artist artist = artists.get(adjustedPosition);
            holder.tvGridSearch.setText(artist.getName());
            holder.artist.setText("Nghệ sĩ");
            Log.d("ImgArtist", "onBindViewHolder: "+artist.getImage());
            if(artist.getImage()!=""){
                Picasso.with(context.getApplicationContext()).load(artist.getImage()).resize(320, 320).into(holder.imgGridSearcht);
            } else {
                holder.imgGridSearcht.setImageResource(R.drawable.avt);
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.listOnClick(view, position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return Math.min(artists.size(), tracks.size()) * 2;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgGridSearcht;
        private TextView tvGridSearch;

        private TextView artist;
        public MyViewHolder(@NonNull View view){
            super(view);

            imgGridSearcht = view.findViewById(R.id.imgGridSearch);
            tvGridSearch = view.findViewById(R.id.txtGridSearch);
            artist = view.findViewById(R.id.txtArtist);
        }
    }
}

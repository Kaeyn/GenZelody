package android2.genzelody;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
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

public class Custom_Adapter_Detail_Playlist extends RecyclerView.Adapter<Custom_Adapter_Detail_Playlist.MyViewHolder> {
    Context context;
    ArrayList<Track> arrayListTrack;
    RecyclerViewClickListener mListener;
    TextView tvOwnerTrackOfPlaylist, tvNameTrackOfPlaylist;
    ImageView imgTrackOfPlaylist;
    @NonNull
    @Override
    public Custom_Adapter_Detail_Playlist.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Custom_Adapter_Detail_Playlist.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_item_list_track_playlist, parent, false));
    }

    public Custom_Adapter_Detail_Playlist(Context context, ArrayList<Track> tracks, RecyclerViewClickListener listener){
        this.context = context;
        this.arrayListTrack = tracks;
        this.mListener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull Custom_Adapter_Detail_Playlist.MyViewHolder holder,  @SuppressLint("RecyclerView") int position) {
        Track track = arrayListTrack.get(position);
        ArrayList<Artist> artists = track.getArtists();


        tvNameTrackOfPlaylist.setText(track.getName());

        String nameArtist ="";
        for (Artist artist: artists){
            System.out.println(artist.getName());
            nameArtist += artist.getName();
        }
        tvOwnerTrackOfPlaylist.setText(nameArtist);

        try {
            int drawableResourceId = Integer.parseInt(track.getImg());
            Drawable drawable = ContextCompat.getDrawable(context, drawableResourceId);
            imgTrackOfPlaylist.setImageDrawable(drawable);
        } catch (NumberFormatException e) {
            Picasso.with(this.context).load(track.getImg()).resize(100,100).into(imgTrackOfPlaylist);
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
        return arrayListTrack.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgTrackOfPlaylist = itemView.findViewById(R.id.imgTrackOfPlaylist);
            tvOwnerTrackOfPlaylist = itemView.findViewById(R.id.tvOwnerTrackOfPlaylist);
            tvNameTrackOfPlaylist = itemView.findViewById(R.id.tvNameTrackOfPlaylist);
        }
    }
}

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

public class Custom_Adapter_RCM_Track extends RecyclerView.Adapter<Custom_Adapter_RCM_Track.MyViewHolder>{
    Context context;
    ArrayList<Track> arrayListTrack;
    RecyclerViewClickListener mListener;
    TextView tvOwnerTrackRCM, tvNameTrackRCM;
    ImageView imgTrackRCM;

    public Custom_Adapter_RCM_Track(Context context, ArrayList<Track> tracks, RecyclerViewClickListener listener){
        this.context = context;
        this.arrayListTrack = tracks;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public Custom_Adapter_RCM_Track.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Custom_Adapter_RCM_Track.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_item_list_track_recommend, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Custom_Adapter_RCM_Track.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Track track = arrayListTrack.get(position);
        ArrayList<Artist> artists = track.getArtists();


        tvNameTrackRCM.setText(track.getName());

        String nameArtist ="";
        for (Artist artist: artists){
            System.out.println(artist.getName());
            nameArtist += artist.getName();
        }
        tvOwnerTrackRCM.setText(nameArtist);

        try {
            int drawableResourceId = Integer.parseInt(track.getImg());
            Drawable drawable = ContextCompat.getDrawable(context, drawableResourceId);
            imgTrackRCM.setImageDrawable(drawable);
        } catch (NumberFormatException e) {
            Picasso.with(this.context).load(track.getImg()).resize(100,100).into(imgTrackRCM);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.reclistOnClick(view, position);
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
            imgTrackRCM = itemView.findViewById(R.id.imgTrackRCM);
            tvNameTrackRCM = itemView.findViewById(R.id.tvNameTrackRCM);
            tvOwnerTrackRCM = itemView.findViewById(R.id.tvOwnerTrackRCM);
        }
    }
}

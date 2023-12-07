package android2.genzelody;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Custom_Adapter_Lv_Track_Playlist extends ArrayAdapter {
    Context context;
    ArrayList<Track> arrayListTrack;

    RecyclerViewClickListener mListener;
    int layoutItem;

    public Custom_Adapter_Lv_Track_Playlist(@NonNull Context context, int resource, @NonNull ArrayList<Track> arrayListTrack, RecyclerViewClickListener listener) {
        super(context, resource, arrayListTrack);
        this.arrayListTrack = arrayListTrack;
        this.context = context;
        this.layoutItem = resource;
        this.mListener = listener;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Track track = arrayListTrack.get(position);
        ArrayList<Artist>  artists = track.getArtists();

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(layoutItem,null);
        }

        TextView tvNameTrackOfPlaylist = convertView.findViewById(R.id.tvNameTrackOfPlaylist);
        tvNameTrackOfPlaylist.setText(track.getName());

        TextView tvOwnerTrackOfPlaylist = convertView.findViewById(R.id.tvOwnerTrackOfPlaylist);
        String nameArtist ="";
        for (Artist artist: artists){
            System.out.println(artist.getName());
            nameArtist += artist.getName();
        }
        tvOwnerTrackOfPlaylist.setText(nameArtist);

        ImageView imgTrackOfPlaylist = convertView.findViewById(R.id.imgTrackOfPlaylist);

        try {
            int drawableResourceId = Integer.parseInt(track.getImg());
            Drawable drawable = ContextCompat.getDrawable(context, drawableResourceId);
            imgTrackOfPlaylist.setImageDrawable(drawable);
        } catch (NumberFormatException e) {
            Picasso.with(this.getContext()).load(track.getImg()).resize(100,100).into(imgTrackOfPlaylist);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.listOnClick(view, position);
            }
        });

        return convertView;
    }
}


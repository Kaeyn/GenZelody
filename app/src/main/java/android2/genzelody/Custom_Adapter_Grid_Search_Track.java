package android2.genzelody;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Custom_Adapter_Grid_Search_Track extends ArrayAdapter {

    Context context;
    ArrayList<Track> arrayListTrack;
    int layoutItem;

    public Custom_Adapter_Grid_Search_Track(@NonNull Context context, int resource, @NonNull ArrayList<Track> arrayListTrack) {
        super(context, resource, arrayListTrack);
        this.arrayListTrack = arrayListTrack;
        this.context = context;
        this.layoutItem = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Track track = arrayListTrack.get(position);
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(layoutItem,null);
        }

        TextView nameTrack = convertView.findViewById(R.id.tvNameSearckTrack);
        nameTrack.setText(track.getName());

        TextView ownertrack = convertView.findViewById(R.id.tvOwnerSearchTrack);
        ownertrack.setText(track.getName());

        ImageView imgPlaylist = convertView.findViewById(R.id.imgSearchTrack);

        try {
            int drawableResourceId = Integer.parseInt(track.getImg());
            Drawable drawable = ContextCompat.getDrawable(context, drawableResourceId);
            imgPlaylist.setImageDrawable(drawable);
        } catch (NumberFormatException e) {
            // If the image is not a drawable resource ID (assuming it's a URL)
            Picasso.with(this.getContext()).load(track.getImg()).resize(100,100).into(imgPlaylist);
        }


        return convertView;
    }

}

package android2.genzelody;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Custom_Adapter_Grid_Search_Artist extends ArrayAdapter {

    Context context;
    ArrayList<Artist> arrayListArtist;
    int layoutItem;

    public Custom_Adapter_Grid_Search_Artist(@NonNull Context context, int resource, @NonNull ArrayList<Artist> arrayListArtist) {
        super(context, resource, arrayListArtist);
        this.arrayListArtist = arrayListArtist;
        this.context = context;
        this.layoutItem = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Artist artist = arrayListArtist.get(position);
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(layoutItem,null);
        }

        TextView nameTrack = convertView.findViewById(R.id.tvNameSearckArtist);
        nameTrack.setText(artist.getName());


        ImageView imgPlaylist = convertView.findViewById(R.id.imgSearchArtist);

        try {
            int drawableResourceId = Integer.parseInt(artist.getImage());
            Drawable drawable = ContextCompat.getDrawable(context, drawableResourceId);
            imgPlaylist.setImageDrawable(drawable);
        } catch (NumberFormatException e) {
            // If the image is not a drawable resource ID (assuming it's a URL)
            Picasso.with(this.getContext()).load(artist.getImage()).resize(412,412).into(imgPlaylist);
        }


        return convertView;
    }

}

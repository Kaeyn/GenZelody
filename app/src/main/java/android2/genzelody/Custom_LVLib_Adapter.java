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

public class Custom_LVLib_Adapter extends ArrayAdapter {

    Context context;
    ArrayList<Playlists> arrayList;
    int layoutItem;

    public Custom_LVLib_Adapter(@NonNull Context context, int resource, @NonNull ArrayList<Playlists> arrayList) {
        super(context, resource, arrayList);
        this.arrayList = arrayList;
        this.context = context;
        this.layoutItem = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Playlists playlist = arrayList.get(position);
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(layoutItem,null);
        }

        // Assuming 'R.drawable.your_drawable' is the ID of your drawable resource


        TextView txt = convertView.findViewById(R.id.txtNamePlayList);
        txt.setText(playlist.getName());


        ImageView imgPlaylist = convertView.findViewById(R.id.imgPlayList);

        try {
            int drawableResourceId = Integer.parseInt(playlist.getImages());
            Drawable drawable = ContextCompat.getDrawable(context, drawableResourceId);
            imgPlaylist.setImageDrawable(drawable);
        } catch (NumberFormatException e) {
            // If the image is not a drawable resource ID (assuming it's a URL)
            Picasso.with(this.getContext()).load(playlist.getImages()).resize(100,100).into(imgPlaylist);
        }


        return convertView;
    }
}

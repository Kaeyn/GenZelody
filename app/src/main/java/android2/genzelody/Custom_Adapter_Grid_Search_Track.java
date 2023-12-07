package android2.genzelody;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Custom_Adapter_Grid_Search_Track extends BaseAdapter {
    Context context;
    ArrayList<Track> arrayListResultTrack = new ArrayList<Track>();
    private AdapterView.OnItemClickListener onItemClickListener;

    public Custom_Adapter_Grid_Search_Track(Context context, ArrayList<Track> arrayListResultTrack) {
        this.context = context;
        this.arrayListResultTrack = arrayListResultTrack;
    }

    @Override
    public int getCount() {
        return arrayListResultTrack.size();
    }

    @Override
    public Object getItem(int position) {return arrayListResultTrack.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    static class ViewHolder{
        ImageView imgSearchTrack;
        TextView tvNameSearckTrack, tvOwnerSearchTrack;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.layout_item_grid_search_track, parent, false);
            holder.imgSearchTrack = convertView.findViewById(R.id.imgSearchTrack);
            holder.tvNameSearckTrack = convertView.findViewById(R.id.tvNameSearckTrack);
            holder.tvOwnerSearchTrack = convertView.findViewById(R.id.tvOwnerSearchTrack);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Track track = arrayListResultTrack.get(position);
        Picasso.with(context.getApplicationContext()).load(track.getImg()).resize(125,125).into(holder.imgSearchTrack);
        holder.tvNameSearckTrack.setText((track.getName()));
        String nameArtist = "";
//        for (Artist artist: track.getArtists()){
//            nameArtist += "Bài hát -"+ artist;
//        }
        holder.tvOwnerSearchTrack.setText(nameArtist);

        return convertView;
    }
    public Track GetItem(int position){
        return arrayListResultTrack.get(position);
    }

}

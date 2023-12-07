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

public class Custom_Adapter_Grid_Search_Artits extends BaseAdapter {
    Context context;
    ArrayList<Artist> arrayListResultArtist = new ArrayList<Artist>();
    private AdapterView.OnItemClickListener onItemClickListener;

    public Custom_Adapter_Grid_Search_Artits(Context context, ArrayList<Artist> arrayListResultArtis) {
        this.context = context;
        this.arrayListResultArtist = arrayListResultArtist;
    }

    @Override
    public int getCount() {
        return arrayListResultArtist.size();
    }

    @Override
    public Object getItem(int position) {return arrayListResultArtist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    static class ViewHolder{
        ImageView imgSearchArtis;
        TextView tvNameSearckArits;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.layout_item_grid_search_artist, parent, false);
            holder.imgSearchArtis = convertView.findViewById(R.id.imgSearchArtist);
            holder.tvNameSearckArits = convertView.findViewById(R.id.tvNameSearckArtist);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Artist artist = arrayListResultArtist.get(position);
        Picasso.with(context.getApplicationContext()).load(artist.getImage()).resize(125,125).into(holder.imgSearchArtis);
        holder.tvNameSearckArits.setText((artist.getName()));

        return convertView;
    }
    public Artist GetItem(int position){
        return arrayListResultArtist.get(position);
    }

}

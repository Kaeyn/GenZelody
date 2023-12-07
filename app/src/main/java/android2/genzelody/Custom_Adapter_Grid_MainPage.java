package android2.genzelody;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
public class Custom_Adapter_Grid_MainPage extends RecyclerView.Adapter<Custom_Adapter_Grid_MainPage.MyViewHolder> {

    private Context context;
    private ArrayList<Album> albumArrayList;

    public Custom_Adapter_Grid_MainPage(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<Album> albumArrayList){
        this.albumArrayList = albumArrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_item_grid, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Album album = albumArrayList.get(position);
        holder.imgGridPlayList.setImageResource(Integer.parseInt(album.getImage()));
        holder.tvGridPlayList.setText(album.getName());
    }
    @Override
    public int getItemCount() {
        return albumArrayList.size();
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
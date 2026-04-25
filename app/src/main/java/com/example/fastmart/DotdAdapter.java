package com.example.fastmart;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
public class DotdAdapter extends RecyclerView.Adapter<DotdAdapter.DotdViewHolder> {
    ArrayList<items> list;
    Context context;
    SharedPreferences sPref;
    SharedPreferences.Editor editor;
    Set<String> favModels;
    ArrayList<String> favList;
    public DotdAdapter(Context context, ArrayList<items> list){
        this.context = context;
        this.list = list;
        sPref = context.getSharedPreferences("FavouritesPrefs", Context.MODE_PRIVATE);
        editor = sPref.edit();
        favModels = MyApplication.favModels;
        favList = MyApplication.favList;
    }
    public void updateList(ArrayList<items> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public DotdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_dotd_item_layout, parent, false);
        return new DotdViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull DotdViewHolder holder, int position) {
        items item = list.get(position);
        if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
            Glide.with(context).load(item.getImageUrl()).into(holder.img);
        } else {
            holder.img.setImageResource(item.getImage());
        }
        holder.description.setText(item.getsDesc());
        holder.newPrice.setText(item.getNewPrice());
        holder.originalPrice.setText(item.getOriginalPrice());
        holder.originalPrice.setPaintFlags(holder.originalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.type.setText(item.getType());
        holder.name.setText(item.getName());
        if(item.isFavourite()){
            holder.fav.setImageResource(R.drawable.ic_favorite_filled);
        }else{
            holder.fav.setImageResource(R.drawable.ic_favorite_empty);
        }
        holder.fav.setOnClickListener(v -> {
            item.setFavourite(!item.isFavourite());
            String modelName = item.getModel();
            if (favModels.contains(modelName)) {
                favModels.remove(modelName);
                favList.remove(modelName);
                MyApplication.favDB.removeFavourite(modelName);
            } else {
                favModels.add(modelName);
                favList.add(modelName);
                MyApplication.favDB.addFavourite(item);
            }
            managePrivate(item, holder);
        });
        holder.card.setOnClickListener(v -> {
            Intent i = new Intent(context, Product.class);
            i.putExtra("Name", item.getModel());
            context.startActivity(i);
        });
    }
    private void managePrivate(items item, @NonNull DotdViewHolder holder){
        if(item.isFavourite()){
            holder.fav.setImageResource(R.drawable.ic_favorite_filled);
        }else{
            holder.fav.setImageResource(R.drawable.ic_favorite_empty);
        }
        int number = MyApplication.favModels.size();
        MyApplication.favouritesBadge.setVisible(number > 0);
        MyApplication.favouritesBadge.setNumber(number);
        MyApplication.notifyFavouritesChanged();
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    public class DotdViewHolder extends RecyclerView.ViewHolder
    {
        ImageView img, fav;
        CardView card;
        TextView type, newPrice, originalPrice, name, description;
        public DotdViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.deal_card);
            img = itemView.findViewById(R.id.deal_img);
            fav = itemView.findViewById(R.id.deal_fav);
            type = itemView.findViewById(R.id.deal_type);
            newPrice = itemView.findViewById(R.id.deal_nprice);
            originalPrice = itemView.findViewById(R.id.deal_oprice);
            name = itemView.findViewById(R.id.deal_name);
            description = itemView.findViewById(R.id.deal_desc);
        }
    }
}

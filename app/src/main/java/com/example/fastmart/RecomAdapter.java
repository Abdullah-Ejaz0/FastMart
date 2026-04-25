package com.example.fastmart;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
public class RecomAdapter extends RecyclerView.Adapter<RecomAdapter.RecomViewHolder> {
    ArrayList<items> list;
    Context context;
    SharedPreferences sPref;
    Set<String> favModels;
    ArrayList<String> favList;
    SharedPreferences.Editor editor;
    public RecomAdapter(Context context, ArrayList<items> list){
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
    public RecomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_recom_layout, parent, false);
        return new RecomViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull RecomViewHolder holder, int position) {
        items item = list.get(position);
        if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
            Glide.with(context).load(item.getImageUrl()).into(holder.img);
        } else {
            holder.img.setImageResource(item.getImage());
        }
        holder.desc.setText(item.getsDesc());
        holder.price.setText(item.getOriginalPrice());
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
    private void managePrivate(items item, @NonNull RecomAdapter.RecomViewHolder holder){
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
    public class RecomViewHolder extends RecyclerView.ViewHolder
    {
        ImageView img, fav;
        TextView name, price, desc;
        RelativeLayout card;
        public RecomViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.recom_img);
            fav = itemView.findViewById(R.id.recom_fav);
            name = itemView.findViewById(R.id.recom_name);
            price = itemView.findViewById(R.id.recom_price);
            desc = itemView.findViewById(R.id.recom_desc);
            card = itemView.findViewById(R.id.recom_main);
        }
    }
}

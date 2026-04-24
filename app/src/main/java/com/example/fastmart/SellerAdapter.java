package com.example.fastmart;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SellerAdapter extends RecyclerView.Adapter<SellerAdapter.SellerViewHolder> {
    ArrayList<items> list;
    Context context;
    SharedPreferences sPref;
    Set<String> favModels;
    ArrayList<String> favList;
    SharedPreferences.Editor editor;

    public SellerAdapter(Context context, ArrayList<items> list){
        this.context = context;
        this.list = list;
        sPref = context.getSharedPreferences("FavouritesPrefs", Context.MODE_PRIVATE);
        editor = sPref.edit();
        favModels = MyApplication.favModels;
        favList = MyApplication.favList;
    }

    @NonNull
    @Override
    public SellerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_recom_layout, parent, false);
        return new SellerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SellerViewHolder holder, int position) {
        items item = list.get(position);
        holder.img.setImageResource(item.getImage());
        holder.desc.setText(item.getsDesc());
        holder.price.setText(item.getOriginalPrice());
        holder.name.setText(item.getName());
        
        if(item.isFavourite()){
            holder.fav.setImageResource(R.drawable.ic_favorite_filled);
        } else {
            holder.fav.setImageResource(R.drawable.ic_favorite_empty);
        }

        holder.fav.setOnClickListener(v -> {
            item.setFavourite(!item.isFavourite());
            String modelName = item.getModel();
            if (favModels.contains(modelName)) {
                favModels.remove(modelName);
                favList.remove(modelName);
            } else {
                favModels.add(modelName);
                favList.add(modelName);
            }

            editor.putStringSet("favModels", new HashSet<>(favModels)).commit();
            managePrivate(item, holder);
        });

        // Items are only for display, no click listener for the card
        holder.card.setOnClickListener(null);
    }

    private void managePrivate(items item, @NonNull SellerViewHolder holder){
        if(item.isFavourite()){
            holder.fav.setImageResource(R.drawable.ic_favorite_filled);
        } else {
            holder.fav.setImageResource(R.drawable.ic_favorite_empty);
        }
        int number = MyApplication.favModels.size();
        if (MyApplication.favouritesBadge != null) {
            MyApplication.favouritesBadge.setVisible(number > 0);
            MyApplication.favouritesBadge.setNumber(number);
        }
        MyApplication.notifyFavouritesChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SellerViewHolder extends RecyclerView.ViewHolder {
        ImageView img, fav;
        TextView name, price, desc;
        RelativeLayout card;
        public SellerViewHolder(@NonNull View itemView) {
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

package com.example.fastmart;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder>{
    Context context;
    Set<String> favouriteSet;
    ArrayList<String> favouriteList;
    SharedPreferences sPref;
    SharedPreferences.Editor editor;

    public FavouriteAdapter(Context context){
        this.context = context;
        favouriteSet = MyApplication.favModels;
        favouriteList = MyApplication.favList;

        sPref = context.getSharedPreferences("FavouritesPrefs", Context.MODE_PRIVATE);
        editor = sPref.edit();
    }

    @NonNull
    @Override
    public FavouriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_favourite_item_design, parent, false);
        return new FavouriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteViewHolder holder, int position) {
        items item = MyApplication.stock.getProduct(favouriteList.get(position));
        if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
            Glide.with(context).load(item.getImageUrl()).into(holder.img);
        } else {
            holder.img.setImageResource(item.getImage());
        }
        holder.name.setText(item.getName());
        holder.model.setText(item.getModel());
        if(item.isDotd()){
            holder.originalPrice.setVisibility(View.VISIBLE);
            holder.newPrice.setText(item.getNewPrice());
            holder.originalPrice.setText(item.getOriginalPrice());

            holder.originalPrice.setPaintFlags(
                    holder.originalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
            );

            holder.newPrice.setTextColor(ContextCompat.getColor(context, R.color.red));

        } else {
            holder.originalPrice.setVisibility(View.INVISIBLE);

            holder.originalPrice.setPaintFlags(
                    holder.originalPrice.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG)
            );

            holder.newPrice.setText(item.getOriginalPrice());
            holder.newPrice.setTextColor(ContextCompat.getColor(context, R.color.black));
        }

        holder.cart.setOnClickListener(v -> {
            MyApplication.cart.addItem(item);

            MyApplication.updateCart();
        });
        holder.more.setOnClickListener(v -> {
            showDeleteDialog(item, position);
        });

    }

    private void showDeleteDialog(items item, int position) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_delete_favourite, null);
        TextView dialogInfo = dialogView.findViewById(R.id.dialog_info);
        dialogInfo.setText("Do you want to delete " + item.getName() + " from favourites?");

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(dialogView)
                .create();

        dialogView.findViewById(R.id.dialog_yes).setOnClickListener(v -> {
            favouriteList.remove(item.getModel());
            favouriteSet.remove(item.getModel());
            item.setFavourite(false);
            MyApplication.favouritesBadge.setNumber(favouriteList.size());

            editor.putStringSet("FavouritesPrefsSet", favouriteSet).commit();
            MyApplication.favModels = new HashSet<>(sPref.getStringSet("favModels", new HashSet<>()));

            notifyItemRemoved(position);
            notifyItemRangeChanged(position, favouriteList.size());

            MyApplication.notifyFavouritesChanged();

            dialog.dismiss();
        });

        dialogView.findViewById(R.id.dialog_no).setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }


    @Override
    public int getItemCount() {
        return favouriteList.size();
    }

    public class FavouriteViewHolder extends RecyclerView.ViewHolder
    {
        CardView cart;
        ImageView img, more;
        TextView name, newPrice, originalPrice, model;
        public FavouriteViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.fav_img);
            cart = itemView.findViewById(R.id.fav_cart);
            more = itemView.findViewById(R.id.fav_more);
            name = itemView.findViewById(R.id.fav_name);
            newPrice = itemView.findViewById(R.id.fav_new_price);
            originalPrice = itemView.findViewById(R.id.fav_org_price);
            model = itemView.findViewById(R.id.fav_model);
        }
    }
}

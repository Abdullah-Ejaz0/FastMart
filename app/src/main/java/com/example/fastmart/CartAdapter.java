package com.example.fastmart;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    public ArrayList<items> cartItems;
    public Context context;
    public OnCartChangeListener listener;

    public interface OnCartChangeListener {
        void onQuantityChanged();
        void onItemRemoved(items item);
    }

    public CartAdapter(Context context, ArrayList<items> cartItems, OnCartChangeListener listener){
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_cart_item_design, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        items item = cartItems.get(position);
        holder.name.setText(item.getName());
        holder.model.setText("Model: " + item.getModel());
        holder.newPrice.setText(item.getNewPrice());
        holder.originalPrice.setText(item.getOriginalPrice());
        holder.quantity.setText(String.valueOf(item.getQuantity()));
        holder.image.setImageResource(item.getImage());

        if(item.getQuantity() <= 1){
            holder.remove.setCardBackgroundColor(ContextCompat.getColor(context, R.color.gray));
        } else {
            holder.remove.setCardBackgroundColor(ContextCompat.getColor(context, R.color.black));
        }

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

        holder.add.setOnClickListener(v -> {
            item.quantity++;
            holder.quantity.setText(String.valueOf(item.getQuantity()));
            if(item.getQuantity() > 1){
                holder.remove.setCardBackgroundColor(ContextCompat.getColor(context, R.color.grey));
            }
            listener.onQuantityChanged();
        });

        holder.remove.setOnClickListener(v -> {
            if(item.getQuantity() > 1){
                item.quantity--;
                holder.quantity.setText(String.valueOf(item.getQuantity()));
                if(item.getQuantity() == 1){
                    holder.remove.setCardBackgroundColor(ContextCompat.getColor(context, R.color.gray));
                }
                listener.onQuantityChanged();
            }
        });

        holder.dotMenu.setOnClickListener(v -> {
            cartItems.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartItems.size());
            listener.onItemRemoved(item);
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView name, model, newPrice, originalPrice, quantity;
        CardView add, remove;
        ImageView dotMenu;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.fav_img);
            name = itemView.findViewById(R.id.fav_name);
            model = itemView.findViewById(R.id.fav_model);
            newPrice = itemView.findViewById(R.id.fav_new_price);
            originalPrice = itemView.findViewById(R.id.fav_org_price);
            quantity = itemView.findViewById(R.id.cart_amount);
            add = itemView.findViewById(R.id.cart_add);
            remove = itemView.findViewById(R.id.cart_remove);
            dotMenu = itemView.findViewById(R.id.fav_more);
        }
    }

    public void updateData(ArrayList<items> newList){
        this.cartItems = newList;
        notifyDataSetChanged();
    }
}
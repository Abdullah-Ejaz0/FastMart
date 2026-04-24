package com.example.fastmart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {

    private Context context;
    private ArrayList<Order> orderList;

    public OrderHistoryAdapter(Context context, ArrayList<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_history, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.tvOrderDate.setText(order.getDate());
        holder.tvTotalAmount.setText(order.getTotalCost());

        holder.productsContainer.removeAllViews();

        if (order.getOrderedItems() != null) {
            for (items item : order.getOrderedItems()) {
                View productView = LayoutInflater.from(context).inflate(R.layout.item_order_product, holder.productsContainer, false);

                ImageView ivProduct = productView.findViewById(R.id.ivProduct);
                TextView tvName = productView.findViewById(R.id.tvProductName);
                TextView tvQty = productView.findViewById(R.id.tvProductQty);
                TextView tvPrice = productView.findViewById(R.id.tvProductPrice);

                ivProduct.setImageResource(item.getImage());
                tvName.setText(item.getName());
                tvQty.setText("QTY: " + item.getQuantity());
                tvPrice.setText(item.getNewPrice());

                holder.productsContainer.addView(productView);
            }
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderDate, tvTotalAmount;
        LinearLayout productsContainer;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
            productsContainer = itemView.findViewById(R.id.productsContainer);
        }
    }
}

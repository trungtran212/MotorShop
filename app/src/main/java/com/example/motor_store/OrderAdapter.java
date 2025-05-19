package com.example.motor_store;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orderList;
    private OnStatusChangeListener statusChangeListener;

    public interface OnStatusChangeListener {
        void onStatusChanged(Order order, int newStatus);
    }

    public OrderAdapter(List<Order> orderList, OnStatusChangeListener listener) {
        this.orderList = orderList;
        this.statusChangeListener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.tvCustomerName.setText("Name: " + order.getCustomerName());
        holder.tvPhone.setText("Phone: " + order.getPhone());
        holder.tvAddress.setText("Address: " + order.getAddress());
        holder.tvProduct.setText("Product: " + order.getProductName());

        String time = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                .format(order.getTimestamp());
        holder.tvTime.setText("Time: " + time);

        // Hiển thị trạng thái
        if (order.getStatus() == 1) {
            holder.tvStatus.setText("Done");
            holder.tvStatus.setTextColor(Color.GREEN);
        } else {
            holder.tvStatus.setText("In Progress");
            holder.tvStatus.setTextColor(Color.RED);
        }

        holder.tvStatus.setOnClickListener(v -> {
            int newStatus = (order.getStatus() == 1) ? 0 : 1;
            order.setStatus(newStatus);
            notifyItemChanged(position);
            if (statusChangeListener != null) {
                statusChangeListener.onStatusChanged(order, newStatus);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvCustomerName, tvPhone, tvAddress, tvProduct, tvTime, tvStatus;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvProduct = itemView.findViewById(R.id.tvProduct);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}


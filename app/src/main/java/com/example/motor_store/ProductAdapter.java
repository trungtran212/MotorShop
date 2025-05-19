package com.example.motor_store;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.tvName.setText(product.getName());
        holder.tvBrand.setText("Brand: " + product.getBrand());
        DecimalFormat formatter = new DecimalFormat("#,###");
        holder.tvPrice.setText("Price: " + formatter.format(product.getPrice()) + " VND");

        holder.tvDescription.setText(product.getDescription());

        // Load ảnh từ URI
        try {
            Uri imageUri = Uri.parse(product.getImageUri());
            holder.imgProduct.setImageURI(imageUri);
        } catch (Exception e) {
            holder.imgProduct.setImageResource(R.drawable.placeholder_image);
        }

        // Xử lý click item để mở ProductDetailActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("id", product.getId());
            intent.putExtra("name", product.getName());
            intent.putExtra("brand", product.getBrand());
            intent.putExtra("price", product.getPrice());
            intent.putExtra("description", product.getDescription());
            intent.putExtra("imageUri", product.getImageUri());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvBrand, tvPrice, tvDescription;
        ImageView imgProduct;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvBrand = itemView.findViewById(R.id.tvProductBrand);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
            tvDescription = itemView.findViewById(R.id.tvProductDescription);
            imgProduct = itemView.findViewById(R.id.imgProduct);
        }
    }

    public void updateList(List<Product> newList) {
        productList.clear();
        productList.addAll(newList);
        notifyDataSetChanged();
    }


}

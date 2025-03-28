package com.example.agrimall;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<Product> productList;
    private OnProductClickListener onProductClickListener;

    // Constructor
    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
        this.onProductClickListener = onProductClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productName.setText(product.getName());
        holder.productPrice.setText("₹" + product.getPrice());
        // holder.productDescription.setText(product.getDescription());

        Glide.with(holder.itemView.getContext())
                .load(product.getImageUrl())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.productImage);

        // ✅ Make the Image Clickable → Redirect to ProductDetailsActivity
        holder.productImage.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), ProductDetailsActivity.class);
            intent.putExtra("productName", product.getName());
            intent.putExtra("productPrice", String.valueOf(product.getPrice()));
            intent.putExtra("productDescription", product.getDescription());
            intent.putExtra("productImage", product.getImageUrl()); // Passing URL instead of int
            holder.itemView.getContext().startActivity(intent);
        });

        // ✅ Make "Add to Cart" Button Clickable
        if (holder.btnAddToCart != null) {
            holder.btnAddToCart.setOnClickListener(v -> {
                if (onProductClickListener != null) {
                    onProductClickListener.onAddToCart(product);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void updateList(List<Product> filteredList) {
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productDescription;
        ImageView productImage;
        Button btnAddToCart;

        public ViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            //productDescription = itemView.findViewById(R.id.product_description);
            productImage = itemView.findViewById(R.id.product_image);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart); // Ensure this exists in item_product.xml
        }
    }
    public void updateList(List<Product> newList) {
        productList.clear();  // Clear the current list
        productList.addAll(newList); // Add new filtered list
        notifyDataSetChanged(); // Refresh RecyclerView
    }

    // ✅ Define the Interface for Click Events
    public interface OnProductClickListener {
        void onProductClick(Product product);
        void onAddToCart(Product product);
    }
}
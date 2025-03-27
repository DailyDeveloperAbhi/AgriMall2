package com.example.agrimall;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class ProductDetailsActivity extends AppCompatActivity {

    private ImageView imgProduct;
    private TextView txtProductName, txtProductPrice, txtProductDescription;
    private Button btnAddToCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        imgProduct = findViewById(R.id.imgProduct);
        txtProductName = findViewById(R.id.txtProductName);
        txtProductPrice = findViewById(R.id.txtProductPrice);
        txtProductDescription = findViewById(R.id.txtProductDescription);
        btnAddToCart = findViewById(R.id.btnAddToCart);

        // Get data from Intent
        Intent intent = getIntent();
        String productName = intent.getStringExtra("productName");
        String productPrice = intent.getStringExtra("productPrice");
        String productDescription = intent.getStringExtra("productDescription");
        String productImage = intent.getStringExtra("productImage"); // Now handling as String (URL)

        int price= Integer.parseInt(String.valueOf(productPrice));
        // Set data in views
        txtProductName.setText(productName);
        txtProductPrice.setText("₹ " + productPrice);
        txtProductDescription.setText(productDescription);

        // ✅ Load image from URL instead of resource ID
        Glide.with(this)
                .load(productImage)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(imgProduct);

        btnAddToCart.setOnClickListener(v -> {
            Product product = new Product(productName, price, productDescription, productImage);
            CartManager.addToCart(product);
            Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show();
        });
    }
}
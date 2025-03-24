package com.example.agrimall;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

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
        int productImage = intent.getIntExtra("productImage", R.drawable.ic_launcher_background);

        // Set data in views
        imgProduct.setImageResource(productImage);
        txtProductName.setText(productName);
        txtProductPrice.setText("₹ " + productPrice);
        txtProductDescription.setText(productDescription);

        btnAddToCart.setOnClickListener(v -> {
            int productQuantity = 1;
            Product selectedProduct = new Product(productName, (int) Double.parseDouble(productPrice), productDescription, productImage,productQuantity);
            CartManager.addToCart(selectedProduct);
            Toast.makeText(ProductDetailsActivity.this, "Added to Cart", Toast.LENGTH_SHORT).show();
        });
    }
}
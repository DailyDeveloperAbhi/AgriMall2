package com.example.agrimall;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.*;

public class CheckoutActivity extends AppCompatActivity {

    private final String UPI_ID = "8421726034@ybl";
    private final String PAYEE_NAME = "AgriMall";
    private final String TRANSACTION_NOTE = "Payment for AgriMall order";

    private String AMOUNT;
    private TextView tvTotalPrice;
    private EditText etName, etPhone, etAddress;
    private Button btnPlaceOrder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        tvTotalPrice = findViewById(R.id.tv_total_price);
        etName = findViewById(R.id.et_name);
        etPhone = findViewById(R.id.et_phone);
        etAddress = findViewById(R.id.et_address);
        btnPlaceOrder = findViewById(R.id.btn_place_order);

        int totalPrice = getIntent().getIntExtra("TOTAL_PRICE", 0);
        tvTotalPrice.setText("Total: ₹ " + totalPrice);
        AMOUNT = String.valueOf(totalPrice);

        btnPlaceOrder.setOnClickListener(v -> {
            // 🔥 Skip payment for now, directly save for testing
            handleOrder();  // call this to save in Firestore
        });
    }

    private void handleOrder() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show();
            return;
        }

        // ✅ Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // 📝 Order map
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("fullName", name);
        orderData.put("phone", phone);
        orderData.put("address", address);
        orderData.put("totalAmount", AMOUNT);
        orderData.put("timestamp", new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));

        // 🛒 Add cart items
        List<CartItem> cartItems = CartManager.getInstance().getCartItems();
        List<Map<String, Object>> productList = new ArrayList<>();

        for (CartItem item : cartItems) {
            Map<String, Object> product = new HashMap<>();
            product.put("name", item.getProductName());
            product.put("price", item.getProductPrice());
            product.put("quantity", item.getQuantity());
            product.put("total", item.getTotalPrice());
            product.put("image", item.getProductImage());
            product.put("status", "delivered"); // for test purpose
            productList.add(product);
        }

        orderData.put("products", productList);

        // 📤 Save to Firestore collection "orders"
        db.collection("orders")
                .add(orderData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Order Saved in Firebase!", Toast.LENGTH_LONG).show();
                    CartManager.getInstance().clearCart(); // optional
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to save order", Toast.LENGTH_LONG).show();
                });
    }
}

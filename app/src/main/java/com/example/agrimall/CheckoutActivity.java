package com.example.agrimall;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.agrimall.databinding.ActivityCheckoutBinding;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CheckoutActivity extends AppCompatActivity {

    private Button btnPayNow;
    private final String UPI_ID = "8421726034@ybl"; // Replace with your UPI ID
    private final String PAYEE_NAME = "AgriMall";
    private final String TRANSACTION_NOTE = "Payment for AgriMall order";
    private  String AMOUNT = "500"; // Example amount (₹500)
    private TextView tvTotalPrice;
    private EditText etName, etPhone, etAddress;
    private Button btnPlaceOrder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // Initialize views
        tvTotalPrice = findViewById(R.id.tv_total_price);
        etName = findViewById(R.id.et_name);
        etPhone = findViewById(R.id.et_phone);
        etAddress = findViewById(R.id.et_address);
        btnPlaceOrder = findViewById(R.id.btn_place_order);

        // Get total price from intent
        int totalPrice = getIntent().getIntExtra("TOTAL_PRICE", 0);
        tvTotalPrice.setText("Total: ₹ " + totalPrice);
        AMOUNT=Integer.toString(totalPrice);

        // Handle Place Order button click
        btnPlaceOrder.setOnClickListener(v -> makeUPIPayment());
    }

    private void handleOrder() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_LONG).show();
            finish();  // Go back to previous screen
        }
    }
    private void makeUPIPayment() {
        Uri uri = Uri.parse("upi://pay?pa=" + UPI_ID +
                "&pn=" + PAYEE_NAME +
                "&mc=1234" +
                "&tid=0123456789" +
                "&tr=9876543210" +
                "&tn=" + TRANSACTION_NOTE +
                "&am=" + AMOUNT +
                "&cu=INR");

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setPackage("com.google.android.apps.nbu.paisa.user");  // Google Pay package

        try {
            startActivityForResult(intent, 1);
        } catch (Exception e) {
            Toast.makeText(this, "No UPI app found! Please install Google Pay or PhonePe.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (data != null) {
                String response = data.getStringExtra("response");
                if (response != null && response.contains("SUCCESS")) {
                    Toast.makeText(this, "Payment Successful!", Toast.LENGTH_LONG).show();
                    // ✅ Proceed with order confirmation
                } else {
                    Toast.makeText(this, "Payment Failed!", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Payment Cancelled!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
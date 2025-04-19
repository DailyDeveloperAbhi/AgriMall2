package com.example.agrimall;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class CheckoutActivity extends AppCompatActivity implements PaymentResultListener {

    private String AMOUNT; // in paise
    private TextView tvTotalPrice;
    private EditText etName, etPhone, etAddress;
    private Button btnPlaceOrder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        Checkout.preload(getApplicationContext());

        // Initialize views
        tvTotalPrice = findViewById(R.id.tv_total_price);
        etName = findViewById(R.id.et_name);
        etPhone = findViewById(R.id.et_phone);
        etAddress = findViewById(R.id.et_address);
        btnPlaceOrder = findViewById(R.id.btn_place_order);

        // Get total price from intent
        int totalPrice = getIntent().getIntExtra("TOTAL_PRICE", 0);
        tvTotalPrice.setText("Total: ₹ " + totalPrice);
        AMOUNT = String.valueOf(totalPrice * 100); // Razorpay expects paise

        // Start payment on button click
        btnPlaceOrder.setOnClickListener(v -> startPayment());
    }

    private void startPayment() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show();
            return;
        }

        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_IJUuv4ZM1Qs42V"); // Replace with your test key from Razorpay

        try {
            JSONObject options = new JSONObject();
            options.put("name", "AgriMall");
            options.put("description", "Order Payment");
            options.put("currency", "INR");
            options.put("amount", AMOUNT);

            JSONObject prefill = new JSONObject();
            prefill.put("email", "test@agrimall.com");
            prefill.put("contact", phone);
            options.put("prefill", prefill);

            checkout.open(this, options);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Payment error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        Toast.makeText(this, "Payment Successful! ID: " + razorpayPaymentID, Toast.LENGTH_LONG).show();
        handleOrder(); // Proceed after successful payment
    }

    @Override
    public void onPaymentError(int code, String response) {
        Toast.makeText(this, "Payment Failed: " + response, Toast.LENGTH_LONG).show();
    }

    private void handleOrder() {
        Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_LONG).show();
        finish();  // Go back to previous screen
    }
}

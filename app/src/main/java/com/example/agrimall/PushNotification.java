package com.example.agrimall;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PushNotification extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_push_notification);
        TextView textView = findViewById(R.id.txtNotifications);
        textView.setText("1. Your order has been shipped!\n2. New discount available!\n3. Your package is out for delivery.");

        }
    }

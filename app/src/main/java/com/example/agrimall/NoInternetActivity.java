package com.example.agrimall;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class NoInternetActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_no_internet);
        Button retryBtn = findViewById(R.id.btn_retry);
        retryBtn.setOnClickListener(v -> {
            if (NetworkUti.isNetworkConnected(this)) {
                finish(); // Go back to previous screen
            } else {
                Toast.makeText(this, "Still no Internet", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
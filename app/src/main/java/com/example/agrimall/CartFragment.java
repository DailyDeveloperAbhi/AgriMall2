package com.example.agrimall;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private TextView tvTotalPrice;
    private Button btnCheckout;
    private List<Product> cartItemList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cartfragment, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_cart);
        tvTotalPrice = view.findViewById(R.id.tv_total_price);
        btnCheckout = view.findViewById(R.id.btn_checkout);

        // Ensure CartManager returns a non-null list
        cartItemList = CartManager.getCartItems();
        if (cartItemList == null) {
            cartItemList = new ArrayList<>();
        }

        cartAdapter = new CartAdapter(requireContext(), cartItemList);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(cartAdapter);

        calculateTotal();

        // Checkout logic
        btnCheckout.setOnClickListener(v -> {
            if (cartItemList.isEmpty()) {
                Toast.makeText(getContext(), "Cart is empty!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Order placed successfully!", Toast.LENGTH_SHORT).show();
                CartManager.clearCart();
                cartItemList.clear();
                cartAdapter.notifyDataSetChanged();
                calculateTotal(); // ✅ Refresh total after checkout
            }
        });
        cartAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                calculateTotal();
            }
        });

        return view;
    }

    // Calculate total cart price correctly
    private void calculateTotal() {
        int total = 0;
        for (Product product : cartItemList) {
            total += product.getPrice() * product.getQuantity();  // ✅ Correct calculation
        }
        tvTotalPrice.setText("Total: ₹" + total);
    }
}
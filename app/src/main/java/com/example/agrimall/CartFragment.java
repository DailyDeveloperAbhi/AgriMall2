package com.example.agrimall;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CartFragment extends Fragment implements CartAdapter.CartItemListener {

    private int total;
    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private CartManager cartManager;
    private TextView totalAmount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cartfragment, container, false);

        recyclerView = view.findViewById(R.id.recyclerCart);
        totalAmount = view.findViewById(R.id.txtTotalPrice);
        Button btnCheckout = view.findViewById(R.id.btnCheckout);

        // ✅ Use Singleton Instance instead of creating a new object
        cartManager = CartManager.getInstance();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartAdapter = new CartAdapter(cartManager.getCartItems(), this);
        recyclerView.setAdapter(cartAdapter);

        updateTotal();

        btnCheckout.setOnClickListener(v -> {
            if (cartManager.getCartItems().isEmpty()) {
                Toast.makeText(getContext(), "Cart is empty!", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(getActivity(), CheckoutActivity.class);
                intent.putExtra("TOTAL_PRICE", total);
                startActivity(intent);
            }
        });
        return view;
    }

    private void updateTotal() {
        total = 0;
        for (CartItem item : cartManager.getCartItems()) {
            total += item.getTotalPrice();
        }
        totalAmount.setText("Total: ₹" + total);
    }

    @Override
    public void onIncreaseQuantity(CartItem item) {
        cartManager.increaseQuantity(item);
        cartAdapter.notifyDataSetChanged();
        updateTotal();
    }

    @Override
    public void onDecreaseQuantity(CartItem item) {
        cartManager.decreaseQuantity(item);
        cartAdapter.notifyDataSetChanged();
        updateTotal();
    }

    @Override
    public void onRemoveItem(CartItem item) {
        cartManager.removeItem(item);
        cartAdapter.notifyDataSetChanged();
        updateTotal();
    }
}
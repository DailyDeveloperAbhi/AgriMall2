package com.example.agrimall;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CartFragment extends Fragment {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private List<Product> cartItemList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cartfragment, container, false);

        recyclerView = view.findViewById(R.id.recyclerCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartItemList = CartManager.getCartItems();
        cartAdapter = new CartAdapter(getContext(), cartItemList);
        recyclerView.setAdapter(cartAdapter);

        return view;
    }
}
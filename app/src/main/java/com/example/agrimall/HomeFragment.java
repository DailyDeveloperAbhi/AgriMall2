package com.example.agrimall;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.recycler);
        // Change to Grid Layout for better visual arrangement
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));

        productList = new ArrayList<>();
        productList.add(new Product("Rice Seeds", 15, "very good seeds", R.drawable.seeds,1));
        productList.add(new Product("Lime", 5, "greash leomon", R.drawable.img_4,1));
        productList.add(new Product("Tractor", 5000, "mahinfra tractor", R.drawable.img_2,1));
        productList.add(new Product("Peas Seeds", 8, "new farming pea seeds", R.drawable.img_3,1));

        productAdapter = new ProductAdapter(productList);
        recyclerView.setAdapter(productAdapter);
        return view;
    }
}
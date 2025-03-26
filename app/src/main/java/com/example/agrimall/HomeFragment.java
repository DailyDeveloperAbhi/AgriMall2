package com.example.agrimall;

import static android.os.Build.VERSION_CODES.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R, container, false);

        // Enable options menu in this fragment
        setHasOptionsMenu(true);

        recyclerView = view.findViewById(R);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        productList = new ArrayList<>();
        productList.add(new Product("Rice Seeds", 15, "very good seeds", R, 1));
        productList.add(new Product("Lime", 5, "greash leomon", R.img_4, 1));
        productList.add(new Product("Tractor", 5000, "mahinfra tractor", R.drawable.img_2, 1));
        productList.add(new Product("Peas Seeds", 8, "new farming pea seeds", R.drawable.img_3, 1));

        productAdapter = new ProductAdapter(productList);
        recyclerView.setAdapter(productAdapter);

        return view;
    }

    // Inflate the menu
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    // Handle menu item clicks
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();  // Get the clicked menu item ID

        if (id == R) {
            Toast.makeText(getContext(), "Farm Info Selected", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R) {
            Toast.makeText(getContext(), "Farm Tools Selected", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R) {
            Toast.makeText(getContext(), "Settings Selected", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}


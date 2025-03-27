package com.example.agrimall;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements ProductAdapter.OnProductClickListener {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList, filteredList;
    private FirebaseFirestore db;
    private EditText edtSearch;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        edtSearch = view.findViewById(R.id.edtSearch);

        db = FirebaseFirestore.getInstance();
        productList = new ArrayList<>();
        filteredList = new ArrayList<>();
        productAdapter = new ProductAdapter(filteredList);
        recyclerView.setAdapter(productAdapter);

        loadProductsFromFirestore();

        // 🔍 Search Functionality
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Call Button
        view.findViewById(R.id.btnCallNow).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:+918421726034"));
            startActivity(intent);
        });

        return view;
    }

    private void loadProductsFromFirestore() {
        CollectionReference productsRef = db.collection("products");

        productsRef.addSnapshotListener((value, error) -> {
            if (error != null) {
                Toast.makeText(getContext(), "Error loading products", Toast.LENGTH_SHORT).show();
                return;
            }

            productList.clear();
            filteredList.clear();
            for (QueryDocumentSnapshot doc : value) {
                String name = doc.getString("name");
                double price = doc.getDouble("price");
                String description = doc.getString("description");
                String imageUrl = doc.getString("imageURL");

                Log.d("FirestoreData", "Product: " + name + " | Image: " + imageUrl);

                Product product = new Product(name, (int) price, description, imageUrl);
                productList.add(product);
                filteredList.add(product);
            }
            productAdapter.notifyDataSetChanged();
        });
    }

    private void filterProducts(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(productList);
        } else {
            for (Product product : productList) {
                if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(product);
                }
            }
            if (filteredList.isEmpty()) {
                Toast.makeText(getContext(), "Product Not Found", Toast.LENGTH_SHORT).show();
            }
        }
        productAdapter.notifyDataSetChanged();
    }

    @Override
    public void onProductClick(Product product) {
        Bundle bundle = new Bundle();
        bundle.putString("name", product.getName());
        bundle.putInt("price", product.getPrice());
        bundle.putString("description", product.getDescription());
        bundle.putString("imageURL", product.getImageUrl());

        Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_cartFragment, bundle);
    }

    @Override
    public void onAddToCart(Product product) {
        Bundle bundle = new Bundle();
        bundle.putString("name", product.getName());
        bundle.putDouble("price", product.getPrice());
        bundle.putString("description", product.getDescription());
        bundle.putString("imageURL", product.getImageUrl());

        Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_cartFragment, bundle);
    }
}

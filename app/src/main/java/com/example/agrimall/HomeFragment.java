package com.example.agrimall;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.navigation.Navigation;
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
    private List<Product> productList;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        db = FirebaseFirestore.getInstance();
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList, this);
        recyclerView.setAdapter(productAdapter);

        loadProductsFromFirestore();
        return view;
    }

    private void loadProductsFromFirestore() {
        CollectionReference productsRef = db.collection("products");

        productsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(getContext(), "Error loading products", Toast.LENGTH_SHORT).show();
                    return;
                }

                productList.clear();
                for (QueryDocumentSnapshot doc : value) {
                    String name = doc.getString("name");
                    double price = doc.getDouble("price");
                    String description = doc.getString("description");
                    String imageUrl = doc.getString("imageURL");

                    Log.d("FirestoreData", "Product: " + name + " | Image: " + imageUrl);

                    productList.add(new Product(name, price, description, imageUrl));
                }
                productAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onProductClick(Product product) {
        Bundle bundle = new Bundle();
        bundle.putString("name", product.getName());
        bundle.putDouble("price", product.getPrice());
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

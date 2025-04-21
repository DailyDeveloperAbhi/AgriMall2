package com.example.agrimall;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import java.util.Locale;

public class HomeFragment extends Fragment implements ProductAdapter.OnProductClickListener {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private FirebaseFirestore db;
    private EditText searchBar;
    private ImageButton micButton;
    private ImageView notificationIcon;
    private static final int REQUEST_CODE_SPEECH_INPUT = 100;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize RecyclerView and its layout
        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Initialize Firestore and data list
        db = FirebaseFirestore.getInstance();
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList, this);
        recyclerView.setAdapter(productAdapter);

        // Initialize search bar and mic button
        searchBar = view.findViewById(R.id.edtSearch);
        micButton = view.findViewById(R.id.mic_button);
        micButton.setOnClickListener(v -> startVoiceRecognition());

        // Initialize notification icon and set click event
        notificationIcon = view.findViewById(R.id.notification_icon);
        notificationIcon.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PushNotification.class);
            startActivity(intent);
        });

        // Button for calling directly
        view.findViewById(R.id.btnCallNow).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:+918421726034"));
            startActivity(intent);
        });

        // Load products from Firestore
        loadProductsFromFirestore();

        return view;
    }

    private void startVoiceRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something...");
        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Voice recognition is not supported on this device.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == getActivity().RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                String recognizedText = result.get(0);
                searchBar.setText(recognizedText); // Set text in search bar
                filterProducts(recognizedText);    // Filter products based on search query
            }
        }
    }

    // Function to filter products based on query
    private void filterProducts(String query) {
        List<Product> filteredList = new ArrayList<>();
        for (Product product : productList) {
            if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(product);
            }
        }
        productAdapter.updateList(filteredList); // Update RecyclerView with filtered list
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
                    productList.add(new Product(name, (int) price, description, imageUrl));
                }
                productAdapter.notifyDataSetChanged();
            }
        });
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

    @Override
    public void onStart() {
        super.onStart();

        if (!NetworkUti.isNetworkConnected(requireContext())) {
            // Internet is OFF — go to No Internet Activity
            Intent intent = new Intent(requireContext(), NoInternetActivity.class);
            startActivity(intent);
        }
    }


}

package com.example.agrimall;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.util.Log;
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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment implements ProductAdapter.OnProductClickListener {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private FirebaseFirestore db;
    private EditText searchBar;
    private ImageButton micButton, cameraButton;
    private ImageView notificationIcon;
    private static final int REQUEST_CODE_SPEECH_INPUT = 100;
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_GALLERY = 2;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        db = FirebaseFirestore.getInstance();
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList, this);
        recyclerView.setAdapter(productAdapter);

        // Initialize UI Elements
        searchBar = view.findViewById(R.id.edtSearch);
        micButton = view.findViewById(R.id.mic_button);
        cameraButton = view.findViewById(R.id.camera_button);
        notificationIcon = view.findViewById(R.id.notification_icon);

        micButton.setOnClickListener(v -> startVoiceRecognition());
        cameraButton.setOnClickListener(v -> showImagePickerDialog());
        notificationIcon.setOnClickListener(v -> startActivity(new Intent(getActivity(), PushNotification.class)));

        view.findViewById(R.id.btnCallNow).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:+918421726034"));
            startActivity(intent);
        });

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

    private void showImagePickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select Image")
                .setItems(new String[]{"Take Photo", "Choose from Gallery"}, (dialog, which) -> {
                    if (which == 0) {
                        openCamera();
                    } else {
                        openGallery();
                    }
                })
                .show();
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                searchProductByImage(imageUri);
            }
        }
    }

    private void searchProductByImage(Uri imageUri) {
        Toast.makeText(getContext(), "Searching for product related to the image...", Toast.LENGTH_SHORT).show();
        Log.d("ImageSearch", "Selected Image URI: " + imageUri.toString());
        String imageName = extractImageName(imageUri);
        Log.d("ImageSearch", "Extracted Image Name: " + imageName);

        if (imageName.isEmpty()) {
            Toast.makeText(getContext(), "Could not extract image name", Toast.LENGTH_SHORT).show();
            return;
        }
        searchProductsInFirestore(imageName);
    }

    private String extractImageName(Uri imageUri) {
        String path = imageUri.getLastPathSegment();
        if (path == null) return "";
        path = path.toLowerCase().replaceAll("[^a-zA-Z0-9]", " ");
        return path.trim();
    }

    private void searchProductsInFirestore(String keyword) {
        db.collection("products")
                .whereGreaterThanOrEqualTo("name", keyword)
                .whereLessThanOrEqualTo("name", keyword + "\uf8ff")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        productList.clear();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            String name = doc.getString("name");
                            double price = doc.getDouble("price");
                            String description = doc.getString("description");
                            String imageUrl = doc.getString("imageURL");
                            productList.add(new Product(name, (int) price, description, imageUrl));
                        }
                        productAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "Product not found!", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void loadProductsFromFirestore() {
        db.collection("products").addSnapshotListener((value, error) -> {
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
        });
    }

    @Override
    public void onProductClick(Product product) {

    }

    @Override
    public void onAddToCart(Product product) {

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

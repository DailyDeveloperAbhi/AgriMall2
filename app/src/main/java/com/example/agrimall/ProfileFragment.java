package com.example.agrimall;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileFragment extends Fragment {

    private MaterialButton btnLogout;
    private ImageView profileImage, btnWhatsApp, btnInstagram;
    private TextView textViewUserName, textViewUserEmail;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private static final int PICK_IMAGE_REQUEST = 100;
    private ViewPager2 viewPager2;
    private ImageSliderAdapter imageSliderAdapter;

    public ProfileFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        textViewUserName = view.findViewById(R.id.textViewUserName);
        textViewUserEmail = view.findViewById(R.id.textViewUserEmail);
        btnLogout = view.findViewById(R.id.btnLogout);
        profileImage = view.findViewById(R.id.profileImage);
        btnWhatsApp = view.findViewById(R.id.btnWhatsApp);
        btnInstagram = view.findViewById(R.id.btnInstagram);
        viewPager2 = view.findViewById(R.id.viewPager2);

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            DocumentReference docRef = db.collection("users").document(user.getUid());
            docRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String name = documentSnapshot.getString("name");
                    String email = documentSnapshot.getString("email");
                    String imageUrl = documentSnapshot.getString("imageUrl");

                    textViewUserName.setText(name != null && !name.isEmpty() ? name : "AgriMall User");
                    textViewUserEmail.setText(email != null && !email.isEmpty() ? email : user.getEmail());

                    ArrayList<String> imageUrls = new ArrayList<>();

                    imageUrls.add("https://www.agrivi.com/wp-content/uploads/2018/01/AGRIVI-Organization-is-the-key-1200x565.jpg");
                    imageUrls.add("https://cdn.wikifarmer.com/images/detailed/2024/05/Farmer-Producer-Organizations-A-Way-to-Increase-Smallholder-Farmers-Income.jpg");
                    imageUrls.add("https://foodtank.com/wp-content/uploads/2014/12/teafarmers.jpg");

                    // Debugging log: Print loaded image URLs
                    Log.d("ImageUrls", "Loaded URLs: " + imageUrls);

                    imageSliderAdapter = new ImageSliderAdapter(imageUrls);
                    viewPager2.setAdapter(imageSliderAdapter);

                    // Fix for showing all slides properly
                    viewPager2.setClipToPadding(false);
                    viewPager2.setClipChildren(false);
                    viewPager2.setOffscreenPageLimit(3);
                    viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
                }
            });
        }

        profileImage.setOnClickListener(v -> openGallery());

        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        });

        btnWhatsApp.setOnClickListener(v -> openUrl("https://wa.me/7499876327"));
        btnInstagram.setOnClickListener(v -> openUrl("https://www.instagram.com/chaitanya_bodkhe_45_"));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toast.makeText(getContext(), "Welcome to our AgriMall!", Toast.LENGTH_LONG).show();
    }

    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            uploadImageToFirebase(data.getData());
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) return;

        StorageReference storageRef = storage.getReference().child("profile_images/" + user.getUid() + ".jpg");

        storageRef.putFile(imageUri)
                .continueWithTask(task -> {
                    if (!task.isSuccessful()) throw task.getException();
                    return storageRef.getDownloadUrl();
                }).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String downloadUrl = task.getResult().toString();

                        db.collection("users").document(user.getUid())
                                .set(new HashMap<String, Object>() {{
                                    put("imageUrl", downloadUrl);
                                }}, SetOptions.merge())
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(getActivity(), "Profile Photo Updated", Toast.LENGTH_SHORT).show();

                                    // Update slider with new image
                                    ArrayList<String> updatedImageUrls = new ArrayList<>();
                                    updatedImageUrls.add(downloadUrl);
                                    updatedImageUrls.add("https://www.agrivi.com/wp-content/uploads/2018/01/AGRIVI-Organization-is-the-key-1200x565.jpg");
                                    updatedImageUrls.add("https://cdn.wikifarmer.com/images/detailed/2024/05/Farmer-Producer-Organizations-A-Way-to-Increase-Smallholder-Farmers-Income.jpg");
                                    updatedImageUrls.add("https://foodtank.com/wp-content/uploads/2014/12/teafarmers.jpg");

                                    // Update adapter
                                    if (imageSliderAdapter != null) {
                                        imageSliderAdapter.updateImages(updatedImageUrls);
                                    }
                                })
                                .addOnFailureListener(e -> Toast.makeText(getActivity(), "Firestore Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(getActivity(), "Upload Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
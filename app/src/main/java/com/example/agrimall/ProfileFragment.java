package com.example.agrimall;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ProfileFragment extends Fragment {

    private TextView textViewUserName, textViewUserEmail;
    private Button btnLogout;
    private ImageView profileImage, btnWhatsApp, btnInstagram;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private static final int PICK_IMAGE_REQUEST = 100;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        // Initialize UI elements
        textViewUserName = view.findViewById(R.id.textViewUserName);
        textViewUserEmail = view.findViewById(R.id.textViewUserEmail);
        profileImage = view.findViewById(R.id.profileImage);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnWhatsApp = view.findViewById(R.id.btnWhatsApp);
        btnInstagram = view.findViewById(R.id.btnInstagram);

        // Get current user
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            textViewUserEmail.setText(user.getEmail());

            // Fetch user name and photo from Firestore
            DocumentReference docRef = db.collection("users").document(user.getUid());
            docRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String name = documentSnapshot.getString("name");
                    textViewUserName.setText(name != null ? name : "User");

                    String imageUrl = documentSnapshot.getString("imageUrl");
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        Picasso.get().load(imageUrl).placeholder(R.drawable.ic_launcher_background).into(profileImage);
                    } else {
                        profileImage.setImageResource(R.drawable.ic_launcher_background);
                    }
                }
            });
        }

        // Image click to open gallery
        profileImage.setOnClickListener(v -> openGallery());

        // Logout Button Click Listener
        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        });

        // WhatsApp Click Event
        btnWhatsApp.setOnClickListener(v -> {
            String phoneNumber = "9096339368"; // Replace with your WhatsApp number
            String url = "https://wa.me/" + phoneNumber;
            openUrl(url);
        });

        // Instagram Click Event
        btnInstagram.setOnClickListener(v -> {
            String instagramUsername = "chaitanya_bodkhe_45_ "; // Replace with your Instagram username
            String url = "https://www.instagram.com/" + instagramUsername;
            openUrl(url);
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Show welcome notification when the fragment loads
        Toast.makeText(getContext(), "Welcome to our AgriMall!", Toast.LENGTH_LONG).show();
    }

    // Open URL in a browser or respective app
    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    // Open gallery
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Receive selected image
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            uploadImageToFirebase(imageUri);
        }
    }

    // Upload Image to Firebase Storage and Save URL in Firestore
    private void uploadImageToFirebase(Uri imageUri) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) return;

        StorageReference storageRef = storage.getReference().child("profile_images/" + user.getUid() + ".jpg");

        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String downloadUrl = uri.toString();

                    db.collection("users").document(user.getUid())
                            .set(new HashMap<String, Object>() {{
                                put("imageUrl", downloadUrl);
                            }}, SetOptions.merge())
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(getActivity(), "Profile Photo Saved", Toast.LENGTH_SHORT).show();
                                Picasso.get().load(downloadUrl).placeholder(R.drawable.ic_launcher_background).into(profileImage);
                            })
                            .addOnFailureListener(e -> Toast.makeText(getActivity(), "Error saving URL: " + e.getMessage(), Toast.LENGTH_SHORT).show());

                }))
                .addOnFailureListener(e -> Toast.makeText(getActivity(), "Upload Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}

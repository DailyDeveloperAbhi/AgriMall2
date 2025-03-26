package com.example.agrimall;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;

public class CartFragment extends Fragment {

    private TextView cartProductName, cartProductPrice, cartProductDescription;
    private ImageView cartProductImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cartfragment, container, false);

        cartProductName = view.findViewById(R.id.txtProductName);
        cartProductPrice = view.findViewById(R.id.txtProductPrice);
        cartProductDescription = view.findViewById(R.id.txtProductDescription);
        cartProductImage = view.findViewById(R.id.imgProduct);

        if (getArguments() != null) {
            cartProductName.setText(getArguments().getString("name"));
            cartProductPrice.setText("₹" + getArguments().getDouble("price"));
            cartProductDescription.setText(getArguments().getString("description"));

            Glide.with(this)
                    .load(getArguments().getString("imageUrl"))
                    .placeholder(R.drawable.placeholder)
                    .into(cartProductImage);
        }

        return view;
    }
}

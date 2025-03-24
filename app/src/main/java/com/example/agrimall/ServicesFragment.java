package com.example.agrimall;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ServicesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_services, container, false);

        ListView listView = view.findViewById(R.id.list_view_services);

        // Sample Services List
        String[] services = {
                "Soil Testing",
                "Crop Consultancy",
                "Organic Farming Guidance",
                "Irrigation Setup",
                "Fertilizer Recommendations"
        };

        // Set Adapter for ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_list_item_1, services);

        listView.setAdapter(adapter);

        // Item Click Listener
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            String selectedService = services[position];
            Toast.makeText(getContext(), selectedService + " selected", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}
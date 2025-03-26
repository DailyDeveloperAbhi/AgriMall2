package com.example.agrimall;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

        // Corresponding URLs for each service
        String[] serviceUrls = {
                "https://www.soiltesting.com",  // Soil Testing website
                "https://www.cropquest.com/crop-consulting-services/",  // Crop Consultancy website
                "https://www.fao.org/fileadmin/templates/nr/sustainability_pathways/docs/Compilation_techniques_organic_agriculture_rev.pdf",  // Organic Farming Guidance
                "https://ksnmdrip.com/blogs/How-to-Install-a-Drip-Irrigation-System-in-10-Steps?srsltid=AfmBOorxQqUFyfCp0yJfnssgiMQXErdB_DlD0YnO4OK8ciI2lbgpKEKg",  // Irrigation Setup
                "https://landresources.montana.edu/soilfertility/documents/PDF/pub/FertRecAgMT200703AG.pdf"  // Fertilizer Recommendations
        };

        // Set Adapter for ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_list_item_1, services);

        listView.setAdapter(adapter);

        // Item Click Listener to open website
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            String url = serviceUrls[position];  // Get URL based on the clicked item
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);  // Open in web browser
        });

        return view;
    }
}

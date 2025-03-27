package com.example.agrimall;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import java.util.List;

public class ServicesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_services, container, false);

        ListView listView = view.findViewById(R.id.list_view_services);

        // Services Data (Name, URL, Image)
        List<ServiceModel> services = new ArrayList<>();
        services.add(new ServiceModel("Soil Testing", "https://www.soiltesting.com", R.drawable.img_soil));
        services.add(new ServiceModel("Crop Consultancy", "https://www.cropquest.com/crop-consulting-services/", R.drawable.img_crop));
        services.add(new ServiceModel("Organic Farming", "https://www.fao.org/fileadmin/templates/nr/sustainability_pathways/docs/Compilation_techniques_organic_agriculture_rev.pdf", R.drawable.img_organic));
        services.add(new ServiceModel("Irrigation Setup", "https://ksnmdrip.com/blogs/How-to-Install-a-Drip-Irrigation-System-in-10-Steps?srsltid=AfmBOorxQqUFyfCp0yJfnssgiMQXErdB_DlD0YnO4OK8ciI2lbgpKEKg", R.drawable.img_irrigation));
        services.add(new ServiceModel("Fertilizer Recommendations", "https://landresources.montana.edu/soilfertility/documents/PDF/pub/FertRecAgMT200703AG.pdf", R.drawable.img_fertilizer));

        // Set Adapter
        ServicesAdapter adapter = new ServicesAdapter(requireContext(), services);
        listView.setAdapter(adapter);

        return view;
    }

    // Service Model Class
    public static class ServiceModel {
        String name, url;
        int image;

        public ServiceModel(String name, String url, int image) {
            this.name = name;
            this.url = url;
            this.image = image;
        }
    }

    // Custom Adapter for ListView
    public static class ServicesAdapter extends ArrayAdapter<ServiceModel> {
        private final Context context;
        private final List<ServiceModel> services;

        public ServicesAdapter(Context context, List<ServiceModel> services) {
            super(context, R.layout.service_list_item, services);
            this.context = context;
            this.services = services;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.service_list_item, parent, false);
            }

            ServiceModel service = services.get(position);

            ImageView imageView = convertView.findViewById(R.id.service_image);
            TextView nameText = convertView.findViewById(R.id.service_name);

            nameText.setText(service.name);
            imageView.setImageResource(service.image);

            // Click Listener to Open URL
            convertView.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(service.url));
                context.startActivity(intent);
            });

            return convertView;
        }
    }
}

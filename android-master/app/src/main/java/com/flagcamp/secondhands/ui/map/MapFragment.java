package com.flagcamp.secondhands.ui.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.flagcamp.secondhands.R;
import com.flagcamp.secondhands.model.MapSearchResponse;
import com.flagcamp.secondhands.model.Product;
import com.flagcamp.secondhands.model.User;
import com.flagcamp.secondhands.repository.ProductRepository;
import com.flagcamp.secondhands.repository.ProductViewModelFactory;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.maps.android.ui.IconGenerator;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapFragment extends Fragment implements
        GoogleMap.OnInfoWindowClickListener,
GoogleMap.OnCameraIdleListener{

    private MapViewModel mapViewModel;
    private GoogleMap map;
    private Map<Marker, Product> markerProductMap = new HashMap<>();

    private double longitude;
    private double latitude;
    private double curLon;
    private double curLat;

    private final int scope = 12;

    SupportMapFragment mapFragment;


    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            map.getUiSettings().setZoomControlsEnabled(true);
            map.clear();

            //add marker
           addMarkersToMap(map,latitude,longitude);
          //  addMarker2();
            //move camera to the current location
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude),scope));

            //set customize window adapter
            map.setInfoWindowAdapter(new CustomInfoWindowAdapter());
            //set click function jump to product detail
            map.setOnInfoWindowClickListener(MapFragment.this::onInfoWindowClick);
            // set camera move listener
          map.setOnCameraIdleListener(MapFragment.this::onCameraIdle);

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLocation();
        mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null ) {
            mapFragment.getMapAsync(callback);
        }

        ImageButton myLocationButton = (ImageButton)getView().findViewById(R.id.myMapLocationButton);
        myLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.clear();
                //add marker
                addMarkersToMap(map,curLat,curLon);
                //move camera to the current location
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(curLat,curLon), scope));
            }
        });


    }


    private void addMarkersToMap(GoogleMap map, double latitude, double longitude) {
        ProductRepository repository = new ProductRepository(getContext());
        mapViewModel = new ViewModelProvider(this, new ProductViewModelFactory(repository)).get(MapViewModel.class);
        mapViewModel.setLat(latitude);
        mapViewModel.setLon(longitude);
        //get idToken
        FirebaseAuth.getInstance().getCurrentUser().getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                           @Override
                                           public void onComplete(@NonNull Task<GetTokenResult> task) {
                                               if(task.isSuccessful()){
                                                   mapViewModel.setToken(task.getResult().getToken());
                                                   //get Token successfully, start to render marker
                                                   mapViewModel.getProductGeoInfo().observe(getViewLifecycleOwner(),mapSearchResponse -> {
                                                       if (mapSearchResponse == null || mapSearchResponse.available_products_nearby.size() == 0) {
                                                           Toast.makeText(
                                                                   getContext(),
                                                                   "No Products near this location",
                                                                   Toast.LENGTH_SHORT).show();
                                                           return;
                                                       } else {
                                                           List<Product> productsList = mapSearchResponse.available_products_nearby;

                                                           for (Product product : productsList) {
                                                               final LatLng location = new LatLng(product.lat, product.lon);
                                                               //customize the marker
                                                               IconGenerator iconGenerator = new IconGenerator(getContext());
                                                               iconGenerator.setColor(Color.WHITE);
                                                               iconGenerator.setTextAppearance(R.style.Theme_AppCompat_DayNight_DarkActionBar);
                                                               String productName = product.productName;
                                                               if (productName.length() >= 10) {
                                                                   productName = productName.substring(0, 10) + "...";
                                                               }
                                                               Bitmap bm = iconGenerator.makeIcon(productName);
                                                               MarkerOptions markerOptions = new MarkerOptions()
                                                                       .position(location)
                                                                       .title(product.productName)
                                                                       .snippet(product.price)
                                                                       .icon(BitmapDescriptorFactory.fromBitmap(bm));
                                                               Marker marker = map.addMarker(markerOptions);
                                                               markerProductMap.put(marker, product);
                                                           }
                                                       }
                                                   });

                                                   }else{
                                                   Toast.makeText(
                                                           getContext(),
                                                           "Please login your account",
                                                           Toast.LENGTH_SHORT).show();

                                               }
                                           }
                                       }
                );
//        mapViewModel.getProductGeoInfo().observe(getViewLifecycleOwner(),mapSearchResponse -> {
//            if(mapSearchResponse == null || mapSearchResponse.available_products_nearby.size()==0){
//                Toast.makeText(
//                        getContext(),
//                        "No Products near this location",
//                        Toast.LENGTH_SHORT).show();
//                return;
//            }else {
//             List<Product> productsList = mapSearchResponse.available_products_nearby;
//
//                for (Product product : productsList) {
//                    final LatLng location = new LatLng(product.lat, product.lon);
//                    //customize the marker
//                    IconGenerator iconGenerator = new IconGenerator(getContext());
//                    iconGenerator.setColor(Color.WHITE);
//                    iconGenerator.setTextAppearance(R.style.Theme_AppCompat_DayNight_DarkActionBar);
//                    String productName = product.productName;
//                    if(productName.length() >= 10){
//                        productName = productName.substring(0,10)+"...";
//                    }
//                    Bitmap bm = iconGenerator.makeIcon(productName);
//                    MarkerOptions markerOptions = new MarkerOptions()
//                            .position(location)
//                            .title(product.productName)
//                            .snippet(product.price)
//                            .icon(BitmapDescriptorFactory.fromBitmap(bm));
//                    Marker marker = map.addMarker(markerOptions);
//                    markerProductMap.put(marker, product);
//                }
//            }
//        });

    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        Product product = markerProductMap.get(marker);
        MapFragmentDirections.ActionNavigationMapToNavigationDetails direction = MapFragmentDirections.actionNavigationMapToNavigationDetails(product);
        NavHostFragment.findNavController(MapFragment.this).navigate(direction);

    }
    //get the lan and lon of camera position
    //can refresh new marker if location out of scope
    @Override
    public void onCameraIdle() {
        CameraPosition cameraPosition = map.getCameraPosition();
        double tempLon = cameraPosition.target.longitude;
        double tempLat = cameraPosition.target.latitude;
        double minMoveLon = longitude-0.1;
        double maxMoveLon = longitude+0.1;
        double minMoveLat = latitude-0.1;
        double maxMoveLat = latitude+0.1;
        if(tempLon >maxMoveLon || tempLon < minMoveLon || tempLat > maxMoveLat || tempLat < minMoveLat){
            longitude = tempLon;
            latitude = tempLat;
            mapFragment.getMapAsync(callback);
        }

    }


    class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        @Override
        public View getInfoWindow(Marker marker) {
            View view = getLayoutInflater().inflate(R.layout.map_info_windows, null);
            int badge = R.drawable.ic_correct_18dp;
            //TODO:update the method of call image url
            String urls = markerProductMap.get(marker).imageUrls;
            if(urls == null || urls.length()==0){
                ((ImageView) view.findViewById(R.id.badge)).setImageResource(badge);
            }else{
                String url = urls.split("\\*")[0];
                Picasso.get().load(url).into((ImageView) view.findViewById(R.id.badge));
            }
            String title = marker.getTitle();
            TextView titleUi = ((TextView) view.findViewById(R.id.title));
            if (title != null) {
                // Spannable string allows us to edit the formatting of the text.
                SpannableString titleText = new SpannableString(title);
                titleText.setSpan(new ForegroundColorSpan(Color.BLACK), 0, titleText.length(), 0);
                titleUi.setText(titleText);
            } else {
                titleUi.setText("");
            }
            String snippet = marker.getSnippet();
            TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
            if (snippet != null) {
                snippetUi.setText("Price: " + snippet);
            } else {
                snippetUi.setText("Price: n/a");
            }
            return view;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }
    //already check permission in the main activity

    @SuppressLint("MissingPermission")
    private void initLocation() {
        //permission deny, give a default location
        if (Build.VERSION.SDK_INT >= 23
                && getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            this.latitude = 34.0522342;
            this.longitude = -118.2436849;
            Toast.makeText(
                    getContext(),
                    "Location permission is not granted, default to Los Angeles",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        LocationManager lm =
                (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        //here must use list<string> in case one provider is null
        List<String> providers = lm.getProviders(true);
        Location location = null;
        for (String provider : providers) {
            Location l = lm.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (location == null || l.getAccuracy() < location.getAccuracy()) {
                // Found best last known location: %s", l);
                location = l;
            }
        }
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        curLat = location.getLatitude();
        curLon =location.getLongitude();
    }



}
package com.example.uf2progmulti;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.uf2progmulti.Model.ApiCall;
import com.example.uf2progmulti.Model.FlickrApi;
import com.example.uf2progmulti.Model.Photo;
import com.example.uf2progmulti.Model.Photos;
import com.example.uf2progmulti.Model.SunriseApi;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.uf2progmulti.databinding.ActivityMapsBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener {

    private GoogleMap map;
    private ActivityMapsBinding binding;
    ArrayList<String> serverIds = new ArrayList<>();
    ArrayList<String> ids = new ArrayList<>();
    ArrayList<String> secrets = new ArrayList<>();
    ArrayList<String> url = new ArrayList<>();
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean permissionDenied = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMyLocationButtonClickListener(this);
        map.setOnMyLocationClickListener(this);
        enableMyLocation();

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                // Clear all markers
                map.clear();
                Log.d("Coordenadas", "Latitud i longitud: " + latLng);
                double lat = latLng.latitude;
                double lng = latLng.longitude;
                map.addMarker(new MarkerOptions().position(latLng).title(getAddress(lat, lng)));
                map.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                Retrofit retrofitSunrise = new Retrofit.Builder()
                        .baseUrl("https://api.sunrise-sunset.org/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ApiCall apiCallSunrise = retrofitSunrise.create(ApiCall.class);
                Call<SunriseApi> callSunrise = apiCallSunrise.getData(String.valueOf(lat), String.valueOf(lng));

                callSunrise.enqueue(new Callback<SunriseApi>(){
                    @Override
                    public void onResponse(Call<SunriseApi> call, Response<SunriseApi> response) {
                        if(response.code()!=200){
                            Log.i("testApi", "checkConnection");
                            return;
                        }

                        Log.i("Sunrise: ", response.body().getStatus() + " - " + response.body().getResults().getSunrise());
                        Log.i("Sunset: ", response.body().getStatus() + " - " + response.body().getResults().getSunset());
                    }

                    @Override
                    public void onFailure(Call<SunriseApi> call, Throwable t) {

                    }
                });

                Retrofit retrofitFlickr = new Retrofit.Builder()
                        .baseUrl("https://www.flickr.com/services/rest/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ApiCall apiCallFlickr = retrofitFlickr.create(ApiCall.class);
                Call<FlickrApi> callFlickr = apiCallFlickr.getDataFlickr(String.valueOf(lat), String.valueOf(lng));

                callFlickr.enqueue(new Callback<FlickrApi>() {
                    @Override
                    public void onResponse(Call<FlickrApi> call, Response<FlickrApi> response) {
                        if(response.code()!=200){
                            Log.i("testApi", "checkConnection");
                            return;
                        }
                        for(int i = 0; i < 5; i++) {
                            Photos photos = response.body().getPhotos();
                            ArrayList<Photo> photo = photos.getPhoto();
                            if(photo.size() != 0) {
                                serverIds.add(photo.get(i).getServer());
                                ids.add(photo.get(i).getId());
                                secrets.add(photo.get(i).getSecret());
                                url.add("https://live.staticflickr.com/"+serverIds.get(i)+"/"+ids.get(i)+"_"+secrets.get(i)+".jpg");
                            }
                        }
                        ViewPager mPager = findViewById(R.id.vpager);
                        mPager.setAdapter(new SlidingAdapter(MapsActivity.this , url));
                    }

                    @Override
                    public void onFailure(Call<FlickrApi> call, Throwable t) {

                    }
                });
            }
        });



    }

    public String getAddress(double lat, double lng) {
        try {
            // Initialize geocoder
            Geocoder geo = new Geocoder(this.getApplicationContext(), Locale.getDefault());

            // Get address from latlng
            List<Address> addresses = geo.getFromLocation(lat, lng, 1);
            if (addresses.isEmpty()) {
                Toast.makeText(this, "No s’ha trobat informació", Toast.LENGTH_LONG).show();
            } else {
                if (addresses.size() > 0) {
                    String msg =addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality() +", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName();
                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                    return msg;
                }
            }
        }
        catch(Exception e){
            Toast.makeText(this, "No Location Name Found", Toast.LENGTH_LONG).show();
        }
        return null;
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (map != null) {
                map.setMyLocationEnabled(true);
            }
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Permission was denied. Display an error message
            // Display the missing permission error dialog when the fragments resume.
            permissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (permissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            permissionDenied = false;
        }
    }

    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

}
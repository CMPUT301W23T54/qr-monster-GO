package com.example.qr_monster_go.maps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qr_monster_go.R;
import com.example.qr_monster_go.database.QrMonsterGoDB;
import com.example.qr_monster_go.databinding.FragmentMapsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is a fragment that hosts google maps
 * It also implements functionality for searching for a location and viewing pins/markers
 * that either represent the location of the user, search result, or qr code
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback {

    FragmentMapsBinding binding;
    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Marker marker;
    private MarkerOptions markerOptions;

    private CollectionReference codes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMapsBinding.inflate(inflater, container, false);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Instantiate codes collection reference to query geolocations
        QrMonsterGoDB dbCodes = new QrMonsterGoDB();
        codes = dbCodes.getCollectionReference("CodeCollection");

        mapInitialize();

        return binding.getRoot();

    }

    /**
     * Initializes the map along with the search bar
     */
    private void mapInitialize() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(16);
        locationRequest.setFastestInterval(3000);

        binding.searchEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i==EditorInfo.IME_ACTION_SEARCH || i==EditorInfo.IME_ACTION_NONE
                ||keyEvent.getAction() == KeyEvent.ACTION_DOWN
                ||keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){
                    goToSearchLocation();
                }
                return false;
            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
    }

    /**
     * This method will take in whatever the user has entered into the search bar,
     * use geocoder class to obtain the actual location, and then move the map view
     * to that specific location while adding a red marker there
     */
    private void goToSearchLocation() {
        String searchLocation = binding.searchEdt.getText().toString();
        Geocoder geocoder = new Geocoder(getContext());
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchLocation,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (list.size()>0){
            Address address = list.get(0);
            String location = address.getAdminArea();
            double latitude = address.getLatitude();
            double longitude = address.getLongitude();
            gotoLatLng(latitude, longitude, 20f);
            if (marker != null){
                marker.remove();
            }
            markerOptions = new MarkerOptions();
            markerOptions.title(location);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            markerOptions.position(new LatLng(latitude, longitude));
            marker = mMap.addMarker(markerOptions);
        }
    }

    /**
     * Method used to update the map view/camera to a specific pair of coordinates
     * @param latitude
     * @param longitude
     * @param v
     */
    private void gotoLatLng(double latitude, double longitude, float v) {
        LatLng latLng = new LatLng(latitude, longitude);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng,v);
        mMap.animateCamera(update);
    }

    /**
     * Called when the map view is ready
     * It will move the map to the user's location (if permissions are enabled)
     * It will place down green markers in the locations of qr codes
     * @param googleMap
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Log.d("map", "MapsFragment onMapReady");
        mMap = googleMap;
        Dexter.withContext(getContext())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                            return;
                        }

                        // Adding all of the geolocations of QR codes to the map (geolocation must be non-null)
                        codes.whereNotEqualTo("location", null)
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    Log.d("geolocation", "We're getting code data in MapsFragment");
                                    List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                                    for (DocumentSnapshot snapshot: snapshotList) {
                                        Log.d("geolocation", snapshot.getString("location"));

                                        // coordinate format is "longitudeXlatitude"
                                        String coordinates = snapshot.getString("location");
                                        String[] splits = coordinates.split("X");
                                        Double longitude = Double.parseDouble(splits[0]);
                                        Double latitude = Double.parseDouble(splits[1]);

                                        // Adding a new marker with the QR code's name as the title
                                        markerOptions = new MarkerOptions();
                                        markerOptions.title(snapshot.getString("name"));
                                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                        markerOptions.position(new LatLng(longitude, latitude));
                                        marker = mMap.addMarker(markerOptions);
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("geolocation", "failure to grab location from database");
                                }
                            });


                        mMap.setMyLocationEnabled(true);
                        fusedLocationProviderClient.getLastLocation().addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(),"Error"+
                                        e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
                            }
                        });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(getContext(),"Permission"+
                                permissionDeniedResponse.getPermissionName()+
                                " "+"was denied.",Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();

                    }
                }).check();
    }
}
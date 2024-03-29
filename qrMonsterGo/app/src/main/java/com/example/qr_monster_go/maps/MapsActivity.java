package com.example.qr_monster_go.maps;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.qr_monster_go.R;
import com.example.qr_monster_go.home.HomePageActivity;
import com.example.qr_monster_go.scan.ScanCodeActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.example.qr_monster_go.databinding.ActivityMapsBinding;
import com.google.android.material.navigation.NavigationBarView;

/**
 * This class is called upon to activate the maps activity and will instantiate MapsFragment
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap mMap;
    ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ReplaceFragment(new MapsFragment());

        binding.navBottomBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.current:
                        ReplaceFragment(new MapsFragment());
                        break;

                    case R.id.back:
                        startActivity(new Intent(MapsActivity.this, HomePageActivity.class));
                }
                return true;
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        //SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        //.findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);
    }

    /**
     *
     * @param fragment
     */
    private void ReplaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

    /**
     * No Specific Purpose
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("map", "MapsActivity onMapReady");
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        // LatLng sydney = new LatLng(-34, 151);
        //  mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //  mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
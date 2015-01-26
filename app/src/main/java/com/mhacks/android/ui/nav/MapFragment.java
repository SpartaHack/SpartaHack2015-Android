package com.mhacks.android.ui.nav;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.spartahack.android.R;

/**
 * Created by owner on 12/28/2014.
 */
public class MapFragment extends Fragment {
    MapView mMapView;
    private GoogleMap googleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflat and return the layout
        View v = inflater.inflate(R.layout.fragment_map, container,
                false);
        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // latitude and longitude
        double wellsLat = 42.727466;
        double wellsLong = -84.482058;

        double stadiumParkingLat = 42.726603;
        double stadiumParkingLong = -84.484954;

        // create marker for wells
        MarkerOptions markerWells = new MarkerOptions().position(
                new LatLng(wellsLat, wellsLong)).title("Wells Hall");

        // create marker for stadium parking
        MarkerOptions markerStadiumParking = new MarkerOptions().position(
                new LatLng(stadiumParkingLat, stadiumParkingLong)).title("Parking");


        // Changing marker icon
        markerWells.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon));

        // Changing marker icon
        markerStadiumParking.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon));
                // adding marker
        googleMap.addMarker(markerWells);
        googleMap.addMarker(markerStadiumParking);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(42.727285, -84.482958)).zoom(17).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

        // Perform any camera updates here
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}
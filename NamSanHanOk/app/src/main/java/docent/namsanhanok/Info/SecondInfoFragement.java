package docent.namsanhanok.Info;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import docent.namsanhanok.R;

public class SecondInfoFragement extends Fragment implements OnMapReadyCallback{

    MapView map = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info2, null);
        map = (MapView)view.findViewById(R.id.map);
        map.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng NamsanHanok = new LatLng(37.55883879999999, 126.99407270000006);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(NamsanHanok).title("남산골 한옥마을");

        Marker marker = googleMap.addMarker(markerOptions);
        marker.showInfoWindow();

        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(NamsanHanok));
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(NamsanHanok));
        googleMap.setMinZoomPreference(14.0f);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(map != null) {
            map.onCreate(savedInstanceState);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        map.onSaveInstanceState(outState);
    }
}

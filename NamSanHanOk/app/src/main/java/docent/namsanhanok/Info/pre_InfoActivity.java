package docent.namsanhanok.Info;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tsengvn.typekit.TypekitContextWrapper;

import docent.namsanhanok.Home.HomeActivity;
import docent.namsanhanok.R;

public class pre_InfoActivity extends AppCompatActivity implements OnMapReadyCallback {

    protected ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        init();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.homeBtn :
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    public void init() {
        Toolbar infoToolbar = (Toolbar)findViewById(R.id.infoToolbar);
        setSupportActionBar(infoToolbar);

        android.app.FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment)fragmentManager.findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
        googleMap.setMinZoomPreference(13.5f);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}

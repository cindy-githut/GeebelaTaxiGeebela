package habbitatvalley.com.geebelataxigeebela;

import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import habbitatvalley.com.geebelataxigeebela.activities.GPSTracker;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private GPSTracker gps;
    double latitude, longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getCurrentLocation();
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
        mMap = googleMap;

        // Add a marker in your current location and move the camera
        LatLng location = new LatLng(Double.parseDouble("-26.0964475"), Double.parseDouble( "28.0011458"));
        mMap.addMarker(new MarkerOptions().position(location).title(getAddress(Double.parseDouble("-26.0964475"),Double.parseDouble( "28.0011458"))));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
    }

    /**
     * get current location to pin on the map
     */
    private void getCurrentLocation(){

        // create class object
        gps = new GPSTracker(MapsActivity.this);

        // check if GPS enabled
        if(gps.canGetLocation()){

            latitude = gps.latitude;
            longitude = gps.longitude;

            Log.d("AddressHere",latitude + ":"+ longitude + ":"+getAddress(Double.parseDouble("-26.0964475"),Double.parseDouble( "28.0011458")));

        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }
    /**
     * Get address name from lat and long
     */

    private String getAddress(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                result.append(address.getAddressLine(0) + ", "+  address.getLocality() + " " + address.getPostalCode());
            }

        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        return result.toString();
    }
}

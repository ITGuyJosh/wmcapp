package d.wmcapp;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class Maps extends FragmentActivity {

    // Might be null if Google Play services APK is not available.
    private GoogleMap mMap;
    String fulladdress;
    LatLng latlng;
    Person user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        user = (Person) getIntent().getSerializableExtra("user");
        fulladdress = user.getAddress() + ", " + user.getPostcode();

        //latlng = getLocationFromAddress(fulladdress);

        //setup googlemap
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

//    public LatLng getLocationFromAddress(Context context,String strAddress) {
//
//        Geocoder coder = new Geocoder(context);
//        List<Address> address;
//        LatLng p1 = null;
//
//        try {
//            address = coder.getFromLocationName(strAddress, 5);
//            if (address == null) {
//                return null;
//            }
//            Address location = address.get(0);
//            location.getLatitude();
//            location.getLongitude();
//
//            p1 = new LatLng(location.getLatitude(), location.getLongitude() );
//
//        } catch (Exception ex) {
//
//            ex.printStackTrace();
//        }
//
//        return p1;
//    }

//    public void convertAddress(String address) {
//        if (address != null && !address.isEmpty()) {
//            try {
//                List<Address> addressList = geoCoder.getFromLocationName(address, 1);
//                if (addressList != null && addressList.size() > 0) {
//                    double lat = addressList.get(0).getLatitude();
//                    double lng = addressList.get(0).getLongitude();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            } // end catch
//        } // end if
//    } // end convertAddress

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {


        LatLng latlng = new LatLng(53.3931033,-3.0238144);
        mMap.addMarker(new MarkerOptions().position(latlng).title("Your Address"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 17));

    }
}

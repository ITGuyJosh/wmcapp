package d.wmcapp;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Maps extends FragmentActivity {

    // Might be null if Google Play services APK is not available.
    private GoogleMap mMap;
    DataConn dataConn;
    String fulladdress;
    //LatLng latlng;
    Person user;
    Toolbar toolbar;
    Double lat, lng;
    Integer userid;
    ProgressBar pbbar;
    private ArrayList<String> addressList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        dataConn = new DataConn();
        pbbar = (ProgressBar) findViewById(R.id.pbbar);

        //setting custom toolbar
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setLogo(R.mipmap.wmc_icon);
        toolbar.setBackgroundColor(Color.rgb(0, 93, 173));
        toolbar.setTitle("View Local Employers");

        user = (Person) getIntent().getSerializableExtra("user");
        userid = user.getId();
        fulladdress = user.getAddress() + ", " + user.getPostcode();

        GetLatLong latlng = new GetLatLong();
        latlng.execute();



    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded(addressList);
    }


    public class GetLatLong extends AsyncTask<String, String, String> {
        //declare variables
        String z = "";
        Boolean isSuccess = false;
        ArrayList<String> addressList = new ArrayList<String>();

        @Override
        protected String doInBackground(String... params) {
                try {
                    Connection conn = dataConn.CONN();
                    if (conn == null) {
                        z = "Error in connection with SQL server.";
                    } else {
                        String query = "SELECT address_line + ' ' + post_code AS Address FROM users WHERE role = 1";
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery(query);

                        while (rs.next()) {
                            int i = 1;
                            ResultSetMetaData metadata = rs.getMetaData();
                            int numberOfColumns = metadata.getColumnCount();
                            while(i <= numberOfColumns) {
                                i++;
                                addressList.add(rs.getString("Address"));
                            }
                        }

                        z = "Local employers loaded";
                    }
                } catch (Exception ex) {
                    isSuccess = false;
                    z = "An exception issue has occured.";
                }

            return z;
        }

        protected void onPreExecute() {
            // Set the progress bar to visible to tell the user something is happening.
            pbbar.setVisibility(View.VISIBLE);
        }

        protected void onPostExecute(String z) {
            pbbar.setVisibility(View.GONE);  // Once everything is done set the visibility of the progress bar to invisible
            Toast.makeText(getApplicationContext(), z, Toast.LENGTH_SHORT).show(); //Post the string r which contains info about what has happened.

            //setup googlemap
//            setUpMap(addressList);
//            setUpMapIfNeeded(addressList);

        }
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
     * call {@link #setUpMap(ArrayList)} once when {@link #mMap} is not null.
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
     * @param addressList
     */
    private void setUpMapIfNeeded(ArrayList<String> addressList) {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap(addressList);
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     * @param addressList
     */
    //@SuppressLint("NewApi")



//    public void addMarkersToMap() {
//        mMap.clear();
//        Double[] latitude = new Double[addressList.size()];
//        Double[] longitude = new Double[addressList.size()];
//        String[] addrs = new String[addressList.size()];
//        addrs = addressList.toArray(addrs);
//        List<Address> addressList;
//        if (addrs != null && addrs.length > 0) {
//            for (int i = 0; i < addrs.length; i++) {
//                try {
//                    addressList = geoCoder.getFromLocationName(addrs[i], 1);
//                    if (addressList == null || addressList.isEmpty() || addressList.equals("")) {
//                        addressList = geoCoder.getFromLocationName("san francisco", 1);
//                    }
//                    latitude[i] = addressList.get(0).getLatitude();
//                    longitude[i] = addressList.get(0).getLongitude();
//                    System.out.println("latitude = " + latitude[i] + " longitude = " + longitude[i]);
//                    mMap.addMarker(new MarkerOptions()
//                                    .position(new LatLng(latitude[i], longitude[i]))
//                                    .title(namesArrayList.get(i))
//                                    .snippet(addressList.get(i))
//                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
//                                    .alpha(0.7f)
//                    );
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } // end catch
//            }
//        }
//    } //end addMarkersToMap

    private void setUpMap(ArrayList<String> addressList) {

        ArrayList<String> latlngList = new ArrayList<String>();
        addressList.toArray();
//
//        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker").snippet("Snippet"));
//
//        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for Activity#requestPermissions for more details.
//            return;
//        }
//
//        mMap.setMyLocationEnabled(true);
//
//        // Get LocationManager object from System Service LOCATION_SERVICE
//        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//
//        // Create a criteria object to retrieve provider
//        Criteria criteria = new Criteria();
//
//        // Get the name of the best provider
//        String provider = locationManager.getBestProvider(criteria, true);
//
//        // Get Current Location
//        Location myLocation = locationManager.getLastKnownLocation(provider);
//
//        // set map type
//        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//
//        // Get latitude of the current location
//        double latitude = myLocation.getLatitude();
//
//        // Get longitude of the current location
//        double longitude = myLocation.getLongitude();
//
//        // Create a LatLng object for the current location
//        LatLng latLng = new LatLng(latitude, longitude);
//
//        // Show the current location in Google Map
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//
//        // Zoom in the Google Map
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
//        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("You are here!").snippet("Consider yourself located"));

        for (int i = 0; i < addressList.size(); i++) {

        }


        // Hide the zoom controls as the button panel will cover it.
        mMap.getUiSettings().setZoomControlsEnabled(false);

        // Add lots of markers to the map.
        //addMarkersToMap();

        // Setting an info window adapter allows us to change the both the
        // contents and look of the
        // info window.
       // mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

        LatLng latlng = new LatLng(53.393116, -3.021647);
        mMap.addMarker(new MarkerOptions().position(latlng).title("Your Address"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));

    }
}

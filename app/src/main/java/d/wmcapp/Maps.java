package d.wmcapp;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Maps extends FragmentActivity {

    // Declare variables
    // Might be null if Google Play services APK is not available.
    private GoogleMap mMap;
    DataConn dataConn;
    Button btnSearch;
    EditText searchfield;
    Student user;
    Toolbar toolbar;
    Integer userid;
    ProgressBar pbbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //initialise
        dataConn = new DataConn();
        pbbar = (ProgressBar) findViewById(R.id.pbbar);
        pbbar.setVisibility(View.GONE);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        searchfield = (EditText) findViewById(R.id.editsearch);

        //can't load standard app bar on fragment activity so making a workaround bar
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setLogo(R.mipmap.wmc_icon);
        toolbar.setBackgroundColor(Color.rgb(0, 93, 173));

        //get user information
        user = (Student) getIntent().getSerializableExtra("user");
        userid = user.getId();

        GetLatLong latlng = new GetLatLong();
        latlng.execute();

        //runs search function from button click based on entered text
        btnSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //search via the text entered
                String g = searchfield.getText().toString();

                Geocoder geocoder = new Geocoder(getBaseContext());
                List<Address> addresses = null;

                try {
                    // Getting a maximum of 3 Address that matches the input text
                    addresses = geocoder.getFromLocationName(g, 3);
                    if (addresses != null && !addresses.equals("")) {
                        search(addresses);
                    }
                } catch (Exception e) {
                    String z = "Exceptions generated";
                }

            }
        });

    }

    //on resume function for map
    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    //converts string address into a latlng for populating onto the map
    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p1;
    }
        //search function based on text entered
    protected void search(List<Address> addresses) {

        Address address = (Address) addresses.get(0);
        LatLng ltlg = new LatLng(address.getLatitude(), address.getLongitude());

        String addressText = String.format(
                "%s, %s",
                address.getMaxAddressLineIndex() > 0 ? address
                        .getAddressLine(0) : "", address.getCountryName());

        MarkerOptions markerOptions = new MarkerOptions();

        markerOptions.position(ltlg);
        markerOptions.title(addressText);

        mMap.clear();
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ltlg));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }


    public class GetLatLong extends AsyncTask<String, String, String> {
        //declare variables
        String z = "";
        Boolean isSuccess = false;
        ArrayList<String> addressList = new ArrayList<>();
        ArrayList<String> orgList = new ArrayList<>();

        @Override
        protected String doInBackground(String... params) {
                try {
                    Connection conn = dataConn.CONN();
                    if (conn == null) {
                        z = "Error in connection with SQL server.";
                    } else {
                        String query = "SELECT address, organisation FROM users WHERE role = 1";
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery(query);

                        while (rs.next()) {
                            int i = 1;
                            ResultSetMetaData metadata = rs.getMetaData();
                            int numberOfColumns = metadata.getColumnCount();
                            while(i <= numberOfColumns) {
                                i++;
                                addressList.add(rs.getString("address"));
                                orgList.add(rs.getString("organisation"));
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
            // Once everything is done set the visibility of the progress bar to invisible
            pbbar.setVisibility(View.GONE);
            //Post the string r which contains info about what has happened.
            Toast.makeText(getApplicationContext(), z, Toast.LENGTH_SHORT).show();

            //setup googlemap
            for(int i = 0; i < addressList.size(); i++){
                String address = addressList.get(i);

                LatLng loc = getLocationFromAddress(address);

                String addressText = orgList.get(i);

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(loc);
                markerOptions.title(addressText);

                mMap.addMarker(markerOptions);
            }

            setUpMapIfNeeded();

        }
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call  once when {@link #mMap} is not null.
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
     *
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

    private void setUpMap() {

        LatLng latlng = getLocationFromAddress(user.getAddress());
        mMap.addMarker(new MarkerOptions().position(latlng).title("Your Home")).showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 13));

    }
}

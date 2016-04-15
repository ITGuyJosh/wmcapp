package d.wmcapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserInfo extends AppCompatActivity {

    // Declare variables
    DataConn dataConn;
    TextView  txtName, txtOrg, txtEmail, txtProfile;
    String userid, jobid;
    Button btnAddress, btnAccept, btnReject;
    ProgressBar pbbar;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        //Initialise variables
        dataConn = new DataConn();
        txtName = (TextView)findViewById(R.id.txtName);
        txtOrg = (TextView)findViewById(R.id.txtOrg);
        txtEmail = (TextView)findViewById(R.id.txtEmail);
        txtProfile = (TextView)findViewById(R.id.txtProfile);
        userid = getIntent().getExtras().getString("userid");
        jobid = getIntent().getExtras().getString("jobid");
        pbbar = (ProgressBar)findViewById(R.id.pbbar);
        btnAccept = (Button) findViewById(R.id.btnAccept);
        btnReject = (Button) findViewById(R.id.btnReject);
        btnAddress = (Button) findViewById(R.id.btnAddress);

        //setting custom toolbar
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setLogo(R.mipmap.wmc_icon);
        toolbar.setTitle("WMC EXP Helper");
        setSupportActionBar(toolbar);

        //get applicants
        GetUserInfo getUserInfo = new GetUserInfo();
        getUserInfo.execute();

        //Set button listeners
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateApp updateApp = new UpdateApp();
                updateApp.execute(2);
            }
        });

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateApp updateApp = new UpdateApp();
                updateApp.execute(3);
            }
        });

    }

    //AsyncTask for Getting User Info to page
    public class GetUserInfo extends AsyncTask<String, String, String> {
        //declare variables
        String z = "";
        Boolean isSuccess = false;
        String name;
        String org;
        String email;
        String profile;
        String address;
        String postcode;
        int role;


        @Override
        protected void onPreExecute() {
            // Set the progress bar to visible to tell the user something is happening.
            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String z) {
            pbbar.setVisibility(View.GONE);  // Once everything is done set the visibility of the progress bar to invisible
            Toast.makeText(getApplicationContext(), z, Toast.LENGTH_SHORT).show(); //Post the string r which contains info about what has happened.

            //put information on page
            txtName.setText(name);
            txtOrg.setText(org);
            txtEmail.setText(email);
            txtProfile.setText(profile);

            //set address button with address var extra
            btnAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), Maps.class);
                    i.putExtra("address", address);
                    i.putExtra("postcode", postcode);
                    startActivity(i);
                }
            });

            //hide or show apply buttons by users role
            if(role != 2){
                // hide buttons if loaded user isn't student
                btnAccept.setVisibility(View.INVISIBLE);
                btnReject.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            PreparedStatement stmt;
            ResultSet rs;
                try {
                    Connection conn = dataConn.CONN();
                    if (conn == null) {
                        z = "Error in connection with SQL server.";
                    } else {

                        //getting userid
                        String uQuery = "SELECT name, organisation, email, description, address_line, post_code, role FROM users WHERE id = '" + userid +"'";
                        stmt = conn.prepareStatement(uQuery);
                        rs = stmt.executeQuery();

                        if(rs.next()){
                            name = rs.getString("name");
                            org = rs.getString("organisation");
                            email = rs.getString("email");
                            profile = rs.getString("description");
                            address = rs.getString("address_line");
                            postcode = rs.getString("post_code");
                            role = rs.getInt("role");
                        }else{
                            z = "Unable to retrieve user information";
                            isSuccess = false;
                        }

                        z = "User data loaded.";
                        isSuccess = true;

                    }
                } catch (Exception ex) {
                    isSuccess = false;
                    z = "An exception issue has occured.";
                }

            return z;
        }
    }

    //AsyncTask for Getting User Info to page
    public class UpdateApp extends AsyncTask<Integer, String, String> {
        //declare variables
        String z = "";
        Boolean isSuccess = false;
        int status;


        @Override
        protected void onPreExecute() {
            // Set the progress bar to visible to tell the user something is happening.
            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String z) {
            pbbar.setVisibility(View.GONE);  // Once everything is done set the visibility of the progress bar to invisible
            Toast.makeText(getApplicationContext(), z, Toast.LENGTH_SHORT).show(); //Post the string r which contains info about what has happened.
        }

        @Override
        protected String doInBackground(Integer... params) {
            PreparedStatement stmt;
            status = params[0];
            try {
                Connection conn = dataConn.CONN();
                if (conn == null) {
                    z = "Error in connection with SQL server.";
                } else {

                    //getting userid
                    String uQuery = "update job_applications SET status = '" + status +"' WHERE user_id = '" + userid +"' AND job_id = '" + jobid +"'";
                    stmt = conn.prepareStatement(uQuery);
                    stmt.executeUpdate();

                    if(status == 2){
                        z = "User accepted.";
                        isSuccess = true;
                    }else if(status == 3){
                        z = "User rejected.";
                        isSuccess = true;
                    } else {
                        z = "Unable to retrieve user information";
                        isSuccess = false;
                    }

                }
            } catch (Exception ex) {
                isSuccess = false;
                z = "An exception issue has occured.";
            }

            return z;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Loading up main menu
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //switch case options menu
        switch (item.getItemId()) {
            case R.id.settings_about:
                // About option clicked.
                Intent i = new Intent(getApplicationContext(), HelpScreen.class);
                startActivity(i);
                return true;
            case R.id.settings_help:
                // Help option clicked.
                Intent j = new Intent(getApplicationContext(), Email.class);
                j.putExtra("helpClicked", 1);
                startActivity(j);
                return true;
            case R.id.settings_affiliates:
                // Affiliates option clicked.
                Intent k = new Intent(getApplicationContext(), Affiliates.class);
                startActivity(k);
                return true;
            case R.id.settings_logout:
                // Logout option clicked.
                Intent l = new Intent(getApplicationContext(), Login.class);
                startActivity(l);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

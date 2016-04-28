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
    Button btnAccept, btnReject;
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

        //setting custom toolbar
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setLogo(R.mipmap.wmc_icon);
        toolbar.setTitle("WMC EXP Helper");
        setSupportActionBar(toolbar);

        //get applicants
        GetUserInfo getUserInfo = new GetUserInfo();
        getUserInfo.execute();

        //Set button listeners
        //run update asynctask as APPROVED
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateApp updateApp = new UpdateApp();
                updateApp.execute(2);
            }
        });
        //run update asynctask as REJECTED
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
        //declare local variables
        String z = "";
        Boolean isSuccess = false;
        String name;
        String org;
        String email;
        String profile;
        int role;


        @Override
        protected void onPreExecute() {
            // Set the progress bar to visible to tell the user something is happening.
            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String z) {
            // Once everything is done set the visibility of the progress bar to invisible
            pbbar.setVisibility(View.GONE);
            //Post the string r which contains info about what has happened.
            Toast.makeText(getApplicationContext(), z, Toast.LENGTH_SHORT).show();

            //put information on page
            txtName.setText(name);
            txtOrg.setText(org);
            txtEmail.setText(email);
            txtProfile.setText(profile);

            //hide or show apply buttons by users role
            if(role != 2){
                // hide buttons if loaded user isn't student
                btnAccept.setVisibility(View.INVISIBLE);
                btnReject.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        //background task
        protected String doInBackground(String... params) {
            // declare database variables
            PreparedStatement stmt;
            ResultSet rs;
                try {
                    //verify database conenction & set
                    Connection conn = dataConn.CONN();
                    if (conn == null) {
                        z = "Error in connection with SQL server.";
                    } else {

                        //getting user info query
                        String uQuery = "SELECT name, organisation, email, description, role FROM users WHERE id = '" + userid +"'";
                        stmt = conn.prepareStatement(uQuery);
                        rs = stmt.executeQuery();

                        //evaluating resultset and setting them as variables for display
                        if(rs.next()){
                            name = rs.getString("name");
                            org = rs.getString("organisation");
                            email = rs.getString("email");
                            profile = rs.getString("description");
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
            // Once everything is done set the visibility of the progress bar to invisible
            pbbar.setVisibility(View.GONE);
            //Post the string r which contains info about what has happened.
            Toast.makeText(getApplicationContext(), z, Toast.LENGTH_SHORT).show();
        }

        //background task
        @Override
        protected String doInBackground(Integer... params) {
            //declare local db variables
            PreparedStatement stmt;
            //get passed parameters
            status = params[0];
            try {
                //checking db connction
                Connection conn = dataConn.CONN();
                if (conn == null) {
                    z = "Error in connection with SQL server.";
                } else {

                    //updating applications based on passed parameter
                    String uQuery = "update job_applications SET status = '" + status +"' WHERE user_id = '" + userid +"' AND job_id = '" + jobid +"'";
                    stmt = conn.prepareStatement(uQuery);
                    stmt.executeUpdate();

                    //modifying message to user based on query type
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

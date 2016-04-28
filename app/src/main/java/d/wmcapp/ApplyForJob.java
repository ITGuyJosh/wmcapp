package d.wmcapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplyForJob extends AppCompatActivity {

    String jobid;
    Integer newid;
    Integer userid;
    TextView txtTitle, txtSkills, txtDesc, txtEmployer, txtOrg;
    Button btnApply, btnContact;
    ProgressBar pbbar;
    DataConn dataConn;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_for_job);

        //instantiate & intialise
        dataConn = new DataConn();
        jobid = getIntent().getExtras().getString("jobid");
        newid = Integer.parseInt(jobid);
        userid = getIntent().getExtras().getInt("userid");
        pbbar = (ProgressBar)findViewById(R.id.pbbar);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtSkills = (TextView) findViewById(R.id.txtSkills);
        txtDesc = (TextView) findViewById(R.id.txtDesc);
        btnContact = (Button)findViewById(R.id.btnContact);
        btnApply = (Button)findViewById(R.id.btnApply);

        // Set pbar to invisible
        pbbar.setVisibility(View.GONE);

        //setting custom toolbar
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setLogo(R.mipmap.wmc_icon);
        toolbar.setTitle("WMC EXP Helper");
        setSupportActionBar(toolbar);

        //run functions
        ListJobs getJob = new ListJobs();
        getJob.execute();

        //set button events
        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //opening registration activity
                Intent i = new Intent(ApplyForJob.this, Email.class);
                i.putExtra("jobid", newid);
                startActivity(i);
            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Async application button
                ApplyJob applyJob = new ApplyJob();
                applyJob.execute("");
            }
        });




    }

    //AsyncTask for listing jobs
    public class ListJobs extends AsyncTask<String, String, String> {

        String jobtitle;
        String jobskills;
        String jobdesc;

        String z = "";

        protected void onPreExecute(){
            // Set the progress bar to visible to tell the user something is happening.
            pbbar.setVisibility(View.VISIBLE);
        }

        protected void onPostExecute(String r){

            //setting result to screen
            txtTitle.setText(jobtitle);
            txtSkills.setText(jobskills);
            txtDesc.setText(jobdesc);

            // Once everything is done set the visibility of the progress bar to invisible
            pbbar.setVisibility(View.GONE);
            //Post the string r which contains info about what has happened.
            Toast.makeText(ApplyForJob.this, r, Toast.LENGTH_SHORT).show();

        }
        protected String doInBackground(String... params) {
            try{
                Connection conn = dataConn.CONN();
                if (conn == null) {
                    z = "Error in connection with SQL server";
                }else{

                    //getting employer & job info
                    String query = "SELECT * FROM vJobContact WHERE JID = "+ newid +"";
                    PreparedStatement ps = conn.prepareStatement(query);
                    ResultSet rs= ps.executeQuery();


                    //getting result
                    if(rs.next()) {
                        jobtitle = rs.getString("title");
                        jobskills = rs.getString("skills");
                        jobdesc = rs.getString("jDesc");
                    }

                    z = "Job Details Loaded.";
                }
            }catch(Exception ex){
                z=" Error retrieving data from table.";
            }
            return z;
        }
    }

    //Async task for adding that the student applied for the job
    public class ApplyJob extends AsyncTask<String, String, String>{
        String message = "";
        Boolean isSuccess = false;

        @Override
        protected  void onPreExecute(){
            pbbar.setVisibility(View.VISIBLE);
        }

        // end message
        @Override
        protected void onPostExecute(String r){
            pbbar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), r, Toast.LENGTH_LONG).show();
        }

        // background task
        @Override
        protected String doInBackground(String... params) {

            try{
                //setting db conection & checking its successfuly
                Connection conn = dataConn.CONN();
                if(conn == null) {
                    message = "Error with server connection.";
                }else {
                    //querying db through query string
                    String query = "INSERT INTO job_applications (user_id, job_id, status) VALUES ('" + userid + "','" + jobid + "','1')";
                    PreparedStatement myQuery = conn.prepareStatement(query);
                    myQuery.executeUpdate();

                    message = "Application successful! Please check your status on the main menu.";
                    isSuccess = true;

                }
            }catch (Exception ex){
                isSuccess = false;
                message = "Exceptions Generated";
            }

            return message;
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

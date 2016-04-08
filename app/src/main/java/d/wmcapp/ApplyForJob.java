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

        // Set pbar to invisible
        pbbar.setVisibility(View.GONE);

        //setting custom toolbar
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setLogo(R.mipmap.wmc_icon);
        toolbar.setTitle("WMC EXP Helper");
        setSupportActionBar(toolbar);


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

        //run functions
        ListJobs getJob = new ListJobs();
        getJob.execute();


    }

    //THIS NEEDS REFACTORING
    public class ListJobs extends AsyncTask<String, String, String> {

        String jobtitle;
        String jobskills;
        String jobdesc;

        String z = "";
        List<Map<String, String>> joblist = new ArrayList<Map<String,String>>();

        protected void onPreExecute(){
            pbbar.setVisibility(View.VISIBLE); // Set the progress bar to visible to tell the user something is happening.
        }

        protected void onPostExecute(String r){

            //setting result to screen
            txtTitle.setText(jobtitle);
            txtSkills.setText(jobskills);
            txtDesc.setText(jobdesc);

            pbbar.setVisibility(View.GONE);  // Once everything is done set the visibility of the progress bar to invisible
            Toast.makeText(ApplyForJob.this, r, Toast.LENGTH_SHORT).show(); //Post the string r which contains info about what has happened.

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_apply_for_job, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

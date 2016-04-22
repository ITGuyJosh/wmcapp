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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class EditJobs extends AppCompatActivity {

    //declare variables
    DataConn dataConn;
    EditText editjobtitle, editjobskills, editjobdesc;
    Button btnDelete, btnUpdate;
    ProgressBar pbbar;
    Integer userid;
    Toolbar toolbar;
    String jobID, jobTitle, jobSkills, jobDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_jobs);

        //instaniate & initalise
        dataConn = new DataConn();
        editjobtitle = (EditText) findViewById(R.id.editjobtitle);
        editjobskills = (EditText) findViewById(R.id.editjobskills);
        editjobdesc = (EditText) findViewById(R.id.editjobdesc);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        pbbar = (ProgressBar) findViewById(R.id.pbbar);

        //set invis on load
        pbbar.setVisibility(View.INVISIBLE);

        //loading extra info
        userid = getIntent().getExtras().getInt("userid");
        jobID = getIntent().getExtras().getString("jobID");
        jobTitle = getIntent().getExtras().getString("jobTitle");
        jobSkills = getIntent().getExtras().getString("jobSkills");
        jobDesc = getIntent().getExtras().getString("jobDesc");

        //setting job info to page
        editjobtitle.setText(jobTitle);
        editjobskills.setText(jobSkills);
        editjobdesc.setText(jobDesc);

        //setting custom toolbar
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setLogo(R.mipmap.wmc_icon);
        toolbar.setTitle("WMC EXP Helper");
        setSupportActionBar(toolbar);

        //On click listeners
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeJob chJob = new ChangeJob();
                chJob.execute("1");
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeJob chJob = new ChangeJob();
                chJob.execute("2");
            }
        });
    }

    //AsyncTask for changing (editing & deleting jobs) Jobs
    public class ChangeJob extends AsyncTask<String, String, String> {
        //declare variables
        String z = "";
        Boolean isSuccess = false;
        String newtitle = editjobtitle.getText().toString();
        String newskills = editjobskills.getText().toString();
        String newdesc = editjobdesc.getText().toString();
        String type;

        @Override
        protected String doInBackground(String... params) {
            type = params[0];

            if (newtitle.trim().equals("") || newskills.trim().equals("")|| newdesc.trim().equals("")) {
                z = "Please enter information to be added.";
            } else {
                try {
                    Connection conn = dataConn.CONN();
                    if (conn == null) {
                        z = "Error in connection with SQL server.";
                    } else {

                        if(type == "1"){
                            String query = "UPDATE jobs SET title = '" + newtitle + "', skills = '" + newskills + "', description = '" + newdesc + "' WHERE id = '" + jobID + "'";
                            PreparedStatement myQuery = conn.prepareStatement(query);
                            myQuery.executeUpdate();
                            z = "Job Updated!";
                            isSuccess = true;
                        } else if(type == "2") {
                            String query = "DELETE FROM jobs WHERE id = '" + jobID + "'";
                            PreparedStatement myQuery = conn.prepareStatement(query);
                            myQuery.executeUpdate();
                            z = "Job Deleted!";
                            isSuccess = true;
                        }

                    }
                } catch (Exception ex) {
                    isSuccess = false;
                    z = "An exception issue has occured.";
                }
            }

            return z;
        }

        protected void onPreExecute() {
            // Set the progress bar to visible to tell the user something is happening.
            pbbar.setVisibility(View.VISIBLE);
        }

        protected void onPostExecute(String z) {
            pbbar.setVisibility(View.GONE);  // Once everything is done set the visibility of the progress bar to invisible
            Toast.makeText(EditJobs.this, z, Toast.LENGTH_SHORT).show(); //Post the string r which contains info about what has happened.

            //Go back to employermenu
            Intent i = new Intent(EditJobs.this, EmployerMenu.class);
            i.putExtra("userid", userid);
            startActivity(i);
            finish();
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

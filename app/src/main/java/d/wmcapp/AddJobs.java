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

public class AddJobs extends AppCompatActivity {

    //declare variables
    DataConn dataConn;
    EditText editjobtitle, editjobskills, editjobdesc;
    Button btnAdd;
    ProgressBar pbbar;
    Integer userid;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_jobs);

        //instaniate & initalise
        dataConn = new DataConn();
        editjobtitle = (EditText) findViewById(R.id.editjobtitle);
        editjobskills = (EditText) findViewById(R.id.editjobskills);
        editjobdesc = (EditText) findViewById(R.id.editjobdesc);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        pbbar = (ProgressBar) findViewById(R.id.pbbar);
        pbbar.setVisibility(View.GONE);

        //loading extra info
        userid = getIntent().getExtras().getInt("userid");

        //setting custom toolbar
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setLogo(R.mipmap.wmc_icon);
        toolbar.setTitle("WMC EXP Helper");
        setSupportActionBar(toolbar);

        //On click listeners
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddJob addjob = new AddJob();
                addjob.execute("");
            }
        });
    }

    //AsyncTask for Adding Jobs
    public class AddJob extends AsyncTask<String, String, String> {
        //declare variables
        String z = "";
        Boolean isSuccess = false;
        String jobtitle = editjobtitle.getText().toString();
        String jobskills = editjobskills.getText().toString();
        String jobdesc = editjobdesc.getText().toString();

        @Override
        protected String doInBackground(String... params) {
            if (jobtitle.trim().equals("") || jobdesc.trim().equals("")|| jobskills.trim().equals("")) {
                z = "Please enter information to be added.";
            } else {
                try {
                    Connection conn = dataConn.CONN();
                    if (conn == null) {
                        z = "Error in connection with SQL server.";
                    } else {
                        //refactor around this element
                        String query = "INSERT INTO jobs (user_id, title, description, skills, active) VALUES ('" + userid + "','" + jobtitle + "','" + jobdesc + "','" + jobskills + "','1')";
                        PreparedStatement myQuery = conn.prepareStatement(query);
                        myQuery.executeUpdate();
                        z = "Job Added!";
                        isSuccess = true;
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
            // Once everything is done set the visibility of the progress bar to invisible
            pbbar.setVisibility(View.GONE);
            //Post the string r which contains info about what has happened.
            Toast.makeText(AddJobs.this, z, Toast.LENGTH_SHORT).show();

            //Go back to manage jobs
            Intent i = new Intent(AddJobs.this, ManageJobs.class);
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

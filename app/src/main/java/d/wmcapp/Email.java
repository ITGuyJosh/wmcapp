package d.wmcapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Email extends AppCompatActivity {

    Button btnSend;
    EditText txtSubject, txtMsg;
    Integer jobid, helpClicked;
    ProgressBar pbbar;
    DataConn dataConn;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        //get passed info
        helpClicked = getIntent().getExtras().getInt("helpClicked");
        if(helpClicked == 1){
            jobid = 0;
        } else {
            jobid = getIntent().getExtras().getInt("jobid");
        }

        //intialise
        dataConn = new DataConn();
        btnSend = (Button) findViewById(R.id.btnSend);
        txtSubject = (EditText) findViewById(R.id.editSubject);
        txtMsg = (EditText) findViewById(R.id.editMessage);
        pbbar = (ProgressBar) findViewById(R.id.pbbar);
        pbbar.setVisibility(View.GONE);

        //setting custom toolbar
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setLogo(R.mipmap.wmc_icon);
        toolbar.setTitle("WMC EXP Helper");
        setSupportActionBar(toolbar);

        //initiate email via onclick
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactEmail contact = new ContactEmail();
                contact.execute("");
            }
        });
    }

    //
    public class ContactEmail extends AsyncTask<String, String, String> {
        String empEmail;
        String z = "";

        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE); // Set the progress bar to visible to tell the user something is happening.
        }

        protected void onPostExecute(String r) {

            // bring in text values from edittext fields
            String subject = txtSubject.getText().toString();
            String message = txtMsg.getText().toString();

            // create intent to include input text information
            Intent email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{
                    empEmail
            });
            email.putExtra(Intent.EXTRA_SUBJECT, subject);
            email.putExtra(Intent.EXTRA_TEXT, message);

            // need this to prompt email client (won't work on emulator)
            email.setType("message/rfc822");
            try {
                startActivity(Intent.createChooser(email, "Choose an Email provider :"));

            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(Email.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }

            // Once everything is done set the visibility of the progress bar to invisible
            pbbar.setVisibility(View.GONE);

        }

        protected String doInBackground(String... params) {
            try {
                Connection conn = dataConn.CONN();
                if (conn == null) {
                    z = "Error in connection with SQL server";
                } else {

                    if(jobid == 0){
                        empEmail = "app.help@wmc.ac.uk";
                    } else {
                        //getting employer info
                        String query = "SELECT email FROM vJobContact WHERE JID = " + jobid + "";
                        PreparedStatement ps = conn.prepareStatement(query);
                        ResultSet rs = ps.executeQuery();

                        //getting result
                        if (rs.next()) {
                            empEmail = rs.getString("email");
                        }
                    }

                }
            } catch (Exception ex) {
                z = " Error retrieveing data from table.";
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

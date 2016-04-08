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

        //set button events
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

            pbbar.setVisibility(View.GONE);  // Once everything is done set the visibility of the progress bar to invisible

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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_email, menu);
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

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

public class ManageProfile extends AppCompatActivity {

    //declaring variables
    DataConn dataConn;
    Button btnUpdate;
    EditText editName, editEmail, editOrg, editProf, editAdd;
    Integer userid;
    ProgressBar pbbar;
    Toolbar toolbar;
    Person user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_profile);

        //instaniate & initalise
        dataConn = new DataConn();
        editName = (EditText) findViewById(R.id.editcontactname);
        editEmail = (EditText) findViewById(R.id.editemail);
        editOrg = (EditText) findViewById(R.id.editorg);
        editProf = (EditText) findViewById(R.id.edituserprof);
        editAdd = (EditText) findViewById(R.id.editaddline);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        pbbar = (ProgressBar) findViewById(R.id.pbbar);
        user = (Person) getIntent().getSerializableExtra("user");
        userid = user.getId();
        pbbar.setVisibility(View.GONE);

        //getting user info from object and setting it to page
        editName.setText(user.getName());
        editOrg.setText(user.getOrg());
        editEmail.setText(user.getEmail());
        editProf.setText(user.getDescription());
        editAdd.setText(user.getAddress());

        //setting custom toolbar
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setLogo(R.mipmap.wmc_icon);
        toolbar.setTitle("WMC EXP Helper");
        setSupportActionBar(toolbar);

        //On click listeners
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //background update function
                UpdateProfile upProf = new UpdateProfile();
                upProf.execute();
            }
        });

    }


    //AsyncTask for Updating Profile
    public class UpdateProfile extends AsyncTask<String, String, String> {
        //declare local variables
        String z = "";
        Boolean isSuccess = false;
        String name = editName.getText().toString();
        String org = editOrg.getText().toString();
        String desc = editProf.getText().toString();
        String email = editEmail.getText().toString();
        String address = editAdd.getText().toString();

        @Override
        protected String doInBackground(String... params) {
            //checking if information has been entered
            if (name.trim().equals("") || org.trim().equals("") || desc.trim().equals("") || email.trim().equals("") || address.trim().equals("")) {
                z = "Please enter information to be added.";
            } else {
                try {
                    //verifying db conn and setting if successful
                    Connection conn = dataConn.CONN();
                    if (conn == null) {
                        z = "Error in connection with SQL server.";
                    } else {
                        //setting and executing update query
                        String query = "UPDATE users SET name = '" + name + "', description = '" + desc + "', organisation = '" + org + "', email = '" + email + "', address = '" + address + "' WHERE id = '" + userid + "'";
                        PreparedStatement myQuery = conn.prepareStatement(query);
                        myQuery.executeUpdate();
                        z = "Profile updated! Please relog to see changes.";
                        isSuccess = true;
                    }
                } catch (Exception ex) {
                    isSuccess = false;
                    z = "An exception issue has occured.";
                }
            }
            return z;
        }

        //before background function
        protected void onPreExecute() {
            // Set the progress bar to visible to tell the user something is happening.
            pbbar.setVisibility(View.VISIBLE);
        }

        //after background function
        protected void onPostExecute(String z) {
            // Once everything is done set the visibility of the progress bar to invisible
            pbbar.setVisibility(View.GONE);
            //Post the string r which contains info about what has happened.
            Toast.makeText(ManageProfile.this, z, Toast.LENGTH_SHORT).show();

            //go back to Menu
            if (user.getRole() == 2) {
                //Go to student menu
                Intent i = new Intent(ManageProfile.this, StudentMenu.class);
                i.putExtra("user", user);
                startActivity(i);
                finish();
            } else {
                //go to emp menu
                Intent i = new Intent(ManageProfile.this, EmployerMenu.class);
                i.putExtra("user", user);
                startActivity(i);
                finish();
            }
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

package d.wmcapp;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class Register extends AppCompatActivity {

    //declare variables
    DataConn dataConn;
    EditText editusername, editpass, editcontactname, editorg, editemail, editroadaddress, editpostcode, edituserprofile;
    Spinner editrole;
    Button btnReg;
    ProgressBar pbbar;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //instaniate & initalise
        dataConn = new DataConn();
        editusername = (EditText) findViewById(R.id.editusername);
        editpass = (EditText) findViewById(R.id.editpass);
        editcontactname = (EditText) findViewById(R.id.editcontactname);
        editorg = (EditText) findViewById(R.id.editorg);
        editemail = (EditText) findViewById(R.id.editemail);
        editroadaddress = (EditText) findViewById(R.id.editroadaddress);
        editpostcode = (EditText) findViewById(R.id.editpostcode);
        edituserprofile = (EditText) findViewById(R.id.edituserprof);
        btnReg = (Button) findViewById(R.id.btnReg);
        pbbar = (ProgressBar) findViewById(R.id.pbbar);
        editrole = (Spinner) findViewById(R.id.editrole);
        pbbar.setVisibility(View.GONE);

        //Setting up adapter and list for spinner
        ArrayAdapter<String> adapter;
        List<String> list;
        list = new ArrayList<String>();
        list.add("Student");
        list.add("Employer");
        adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editrole.setAdapter(adapter);

        //setting custom toolbar
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setNavigationIcon(R.mipmap.wmc_icon);
        toolbar.setTitle("WMC EXP Helper");
        setSupportActionBar(toolbar);

        //On click listeners
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddUsers addUser = new AddUsers();
                //can pass a variable here to declare its an Add when refactoring
                addUser.execute("");
            }
        });
    }

    //AsyncTask for Adding Users
    public class AddUsers extends AsyncTask<String, String, String> {
        //declare local variables
        String z = "";
        Boolean isSuccess = false;
        String username = editusername.getText().toString();
        String password = editpass.getText().toString();
        String cName = editcontactname.getText().toString();
        String org = editorg.getText().toString();
        String email = editemail.getText().toString();
        String profile = edituserprofile.getText().toString();
        String role = editrole.getSelectedItem().toString();
        String roadadd = editroadaddress.getText().toString();
        String postadd = editpostcode.getText().toString();
        String address = roadadd + ", " + postadd;
        Integer uRole;
        Integer userid;

        @Override
        //background task
        protected String doInBackground(String... params) {
            //checking if details are added
            if (username.trim().equals("") || password.trim().equals("")|| cName.trim().equals("") || org.trim().equals("") || email.trim().equals("") || profile.trim().equals("") || role.trim().equals("")) {
                z = "Please enter information to be added.";
            } else {
                try {
                    //verifying & setting up db connection
                    Connection conn = dataConn.CONN();
                    if (conn == null) {
                        z = "Error in connection with SQL server.";
                    } else {
                        //defining user role for login
                        if(role == "Student"){
                            uRole = 2;
                        } else {
                            uRole = 1;
                        }

                        //inserting new user
                        String iQuery = "INSERT INTO users (username, password, role, name, description, organisation, email, address) VALUES ('" + username + "','" + password + "', '" + uRole + "','" + cName + "','" + profile + "','" + org + "','" + email + "','" + address + "')";
                        PreparedStatement myQuery = conn.prepareStatement(iQuery);
                        myQuery.executeUpdate();

                        z = "Successful Registration. Please Login!";
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
            Toast.makeText(Register.this, z, Toast.LENGTH_SHORT).show();
            //redirecting back to login
            Intent i = new Intent(getApplicationContext(), Login.class);
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

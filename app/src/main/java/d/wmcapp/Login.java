package d.wmcapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Login extends AppCompatActivity {

    // Declare variables
    DataConn dataConn;
    EditText editusername, editpass;
    Button btnLogin;
    Button btnReg;
    ProgressBar pbbar;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dataConn = new DataConn();
        editusername = (EditText)findViewById(R.id.editusername);
        editpass = (EditText)findViewById(R.id.editpass);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnReg = (Button)findViewById(R.id.btnReg);
        pbbar = (ProgressBar)findViewById(R.id.pbbar);

        // Set pbar to invisible
        pbbar.setVisibility(View.GONE);

        //setting custom toolbar
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setLogo(R.mipmap.wmc_icon);
        toolbar.setTitle("WMC EXP Helper");
        setSupportActionBar(toolbar);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do login extends asynctask
                // Creating login object in the background/asyncron
                // Allows you to do otherthings while it's processing
                DoLogin doLogin = new DoLogin();
                doLogin.execute("");
            }
        });

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //opening registration activity
                Intent i = new Intent(Login.this, Register.class);
                startActivity(i);
                finish();
            }
        });

    }

    // Container class identified by the arrow braces
    public class DoLogin extends AsyncTask<String, String, String> {
        //general variable declarations
        String message = "";
        Boolean isSuccess = false;
        String username = editusername.getText().toString();
        String password = editpass.getText().toString();

        //global class variables
        Student stu;
        Employer emp;

        //user variables
        Integer userid;
        Integer role;
        String name;
        String org;
        String email;
        String desc;
        String address;

        // background running function
        @Override
        protected String doInBackground(String... params) {
            if(username.trim().equals("") || password.trim().equals("")){
                message = "Please enter Username and Password.";
            } else {
                try{
                    Connection conn = dataConn.CONN();
                    if(conn == null) {
                        message = "Error with server connection.";
                    }else {
                        String query = "SELECT * FROM users WHERE username ='" + username +"' " +
                                "AND password='" + password + "'";

                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery(query);

                        if(rs.next()){
                            //creating user object from login
                            userid = rs.getInt("id");
                            role = rs.getInt("role");
                            name = rs.getString("name");
                            email = rs.getString("email");
                            desc = rs.getString("description");
                            org = rs.getString("organisation");
                            address = rs.getString("address");

                            //student object
                            if(role == 2){
                                stu = new Student(
                                        userid,
                                        role,
                                        name,
                                        email,
                                        desc,
                                        org,
                                        address
                                );
                            // employer object
                            } else{
                                emp = new Employer(
                                        userid,
                                        role,
                                        name,
                                        email,
                                        desc,
                                        org,
                                        address
                                );
                            }
                            //setting success rate
                            message = "Login Successful";
                            isSuccess = true;

                        }else{
                            message = "Invalid Credentials";
                            isSuccess = false;
                        }
                    }
                }catch (Exception ex){
                    isSuccess = false;
                    message = "Exceptions Generated";
                }
            }

            return message;
        }

        @Override
        protected  void onPreExecute(){
            pbbar.setVisibility(View.VISIBLE);
        }
        //pass message from background task onto post
        protected void onPostExecute(String r){
            pbbar.setVisibility(View.GONE);
            Toast.makeText(Login.this, r, Toast.LENGTH_SHORT).show();
            if(isSuccess){
                if(role == 1){
                    //setting up intent
                    Intent i = new Intent(Login.this, EmployerMenu.class);
                    //passing it user object from login
                    i.putExtra("user", emp);
                    //starting activity
                    startActivity(i);
                    finish();
                } else if(role == 2){
                    Intent i = new Intent(Login.this, StudentMenu.class);
                    i.putExtra("user", stu);
                    startActivity(i);
                    finish();
                }

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

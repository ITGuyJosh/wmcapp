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
    public class DoLogin extends AsyncTask<String, String, String>{
        String message = "";
        Boolean isSuccess = false;
        Integer role;
        Integer userid;
        String username = editusername.getText().toString();
        String password = editpass.getText().toString();

        // What happens in the background when you run it
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
                            message = "Login Successful";
                            role = rs.getInt("role");
                            userid = rs.getInt("id");
                            isSuccess = true;

//                            if(role == 2) {
//                                //creating student object
//                                Student user = new Student(
//                                        rs.getInt("id"),
//                                        rs.getString("name"),
//                                        rs.getString("email"),
//                                        rs.getString("description"),
//                                        rs.getInt("role"),
//                                        rs.getString("organisation")
//                                );
//                            }
//                            } else if(role == 1){
//                                Employer user = new Employer(
//                                        rs.getInt("id"),
//                                        rs.getString("name"),
//                                        rs.getString("email"),
//                                        rs.getString("description"),
//                                        rs.getInt("role"),
//                                        rs.getString("organisation")
//                                );
//                            }



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
                    //passing it userid from login
                    i.putExtra("userid", userid);
                    //starting activity
                    startActivity(i);
                    finish();
                } else if(role == 2){
                    Intent i = new Intent(Login.this, StudentMenu.class);
                    i.putExtra("userid", userid);
                    startActivity(i);
                    finish();
                }

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //int id = item.getItemId();
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

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

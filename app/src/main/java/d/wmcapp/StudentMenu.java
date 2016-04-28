package d.wmcapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;

public class StudentMenu extends AppCompatActivity {

    //declare variables
    Button btnApply, btnMngProfile, btnAppStatus, btnStats, btnMap;
    Integer userid, uRole;
    Toolbar toolbar;
    Student user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_menu);

        //getting ids & initialising
        btnApply = (Button)findViewById(R.id.btnApply);
        btnMngProfile = (Button)findViewById(R.id.btnMngProfile);
        btnAppStatus = (Button)findViewById(R.id.btnAppStatus);
        btnStats = (Button)findViewById(R.id.btnStats);
        btnMap = (Button) findViewById(R.id.btnMap);
        user = (Student) getIntent().getSerializableExtra("user");
        userid = user.getId();

        //setting custom toolbar
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setLogo(R.mipmap.wmc_icon);
        toolbar.setTitle("WMC EXP Helper");
        setSupportActionBar(toolbar);

        //setting button events
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //opening view jobs  activity
                Intent i = new Intent(StudentMenu.this, ViewJobs.class);
                i.putExtra("userid", userid);
                startActivity(i);
            }
        });

        btnMngProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //opening profile management activity
                Intent i = new Intent(StudentMenu.this, ManageProfile.class);
                i.putExtra("user", user);
                startActivity(i);
            }
        });

        btnAppStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //opening application status activity
                Intent i = new Intent(StudentMenu.this, AppStatus.class);
                i.putExtra("userid", userid);
                startActivity(i);
            }
        });

        btnStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //opening user stats activity
                Intent i = new Intent(StudentMenu.this, UserStats.class);
                i.putExtra("uRole", uRole);
                i.putExtra("user", user);
                startActivity(i);
            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //opening maps activity
                Intent i = new Intent(StudentMenu.this, Maps.class);
                i.putExtra("user", user);
                startActivity(i);
            }
        });

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

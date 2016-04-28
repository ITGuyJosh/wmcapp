package d.wmcapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class EmployerMenu extends AppCompatActivity {

    //delcare variables
    Button btnMngJobs, btnMngProfile,btnReviewApps, btnStats;
    Integer userid;
    Toolbar toolbar;
    Employer user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_menu);

        //intialise
        btnMngJobs = (Button)findViewById(R.id.btnMngJobs);
        btnMngProfile = (Button)findViewById(R.id.btnMngProfile);
        btnReviewApps = (Button)findViewById(R.id.btnAwaitRev);
        btnStats = (Button)findViewById(R.id.btnStats);
        user = (Employer) getIntent().getSerializableExtra("user");
        userid = user.getId();

        //setting custom toolbar
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setLogo(R.mipmap.wmc_icon);
        toolbar.setTitle("WMC EXP Helper");
        setSupportActionBar(toolbar);

        //set button events
        btnMngJobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //opening job management activity
                Intent i = new Intent(EmployerMenu.this, ManageJobs.class);
                i.putExtra("userid", userid);
                startActivity(i);
            }
        });

        btnMngProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //opening profile management activity
                Intent i = new Intent(EmployerMenu.this, ManageProfile.class);
                i.putExtra("user", user);
                startActivity(i);
            }
        });

        btnReviewApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //opening review apps activity
                Intent i = new Intent(EmployerMenu.this, ReviewApps.class);
                i.putExtra("empid", userid);
                startActivity(i);
            }
        });

        btnStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //opening stats activity
                Intent i = new Intent(getApplicationContext(), UserStats.class);
                i.putExtra("uRole", user.getRole());
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

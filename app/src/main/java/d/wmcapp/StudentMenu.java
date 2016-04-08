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

public class StudentMenu extends AppCompatActivity {

    //declare variables
    Button btnApply, btnMngProfile, btnAppStatus;
    Integer userid;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_menu);

        //getting ids
        btnApply = (Button)findViewById(R.id.btnApply);
        btnMngProfile = (Button)findViewById(R.id.btnMngProfile);
        btnAppStatus = (Button)findViewById(R.id.btnAppStatus);
        userid = getIntent().getExtras().getInt("userid");

        //setting custom toolbar
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setLogo(R.mipmap.wmc_icon);
        toolbar.setTitle("WMC EXP Helper");
        setSupportActionBar(toolbar);

        //setting button events
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //opening  activity
                Intent i = new Intent(StudentMenu.this, ViewJobs.class);
                i.putExtra("userid", userid);
                startActivity(i);
            }
        });

        btnMngProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //opening registration activity
                Intent i = new Intent(StudentMenu.this, ManageProfile.class);
                i.putExtra("userid", userid);
                startActivity(i);
            }
        });

        btnAppStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //opening registration activity
                Intent i = new Intent(StudentMenu.this, AppStatus.class);
                i.putExtra("userid", userid);
                startActivity(i);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_student_menu, menu);
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

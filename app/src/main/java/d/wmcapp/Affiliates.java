package d.wmcapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class Affiliates extends AppCompatActivity {

    //Declare variables
    Toolbar toolbar;
    TextView txtWMC, txtChester, txtGDev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affiliates);

        //initialise and set links
        txtWMC = (TextView) findViewById(R.id.txtWMC);
        txtWMC.setMovementMethod(LinkMovementMethod.getInstance());
        txtChester = (TextView) findViewById(R.id.txtChester);
        txtChester.setMovementMethod(LinkMovementMethod.getInstance());
        txtGDev = (TextView) findViewById(R.id.txtGDev);
        txtGDev.setMovementMethod(LinkMovementMethod.getInstance());

        //setting custom toolbar
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setLogo(R.mipmap.wmc_icon);
        toolbar.setTitle("WMC EXP Helper");
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_affiliates, menu);
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

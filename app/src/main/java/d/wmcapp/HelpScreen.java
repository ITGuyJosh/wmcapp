package d.wmcapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

public class HelpScreen extends AppCompatActivity {

    Button btnWatch;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_screen);

        //initialise
        btnWatch = (Button) findViewById(R.id.btnWatch);

        //setting custom toolbar
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setLogo(R.mipmap.wmc_icon);
        toolbar.setTitle("WMC EXP Helper");
        setSupportActionBar(toolbar);

        //setting video
        final VideoView vidView = (VideoView)findViewById(R.id.videoView);
        String vidAddress = "https://r13---sn-aigllner.googlevideo.com/videoplayback?ms=au&mt=1460120626&pl=19&itag=22&upn=5-AkYTQ-f0w&mm=31&expire=1460142378&ratebypass=yes&sver=3&id=o-AO4pAFvrM7LWVrgJXJ4oOyH5vhN1y4DVivwMlZnbfDKU&mv=m&sparams=dur%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cnh%2Cpl%2Cratebypass%2Crequiressl%2Csource%2Cupn%2Cexpire&mn=sn-aigllner&ip=150.204.160.209&fexp=9405991%2C9410705%2C9416126%2C9416891%2C9418642%2C9420452%2C9422596%2C9422841%2C9423640%2C9423662%2C9424815%2C9425347%2C9426926%2C9427902%2C9428398%2C9429379%2C9430818%2C9431270%2C9431674%2C9433191%2C9433399%2C9433727&dur=5112.174&lmt=1450617858562656&key=yt6&initcwndbps=7182500&ipbits=0&nh=IgpwcjA0LmxocjE0KgkxMjcuMC4wLjE&requiressl=yes&mime=video%2Fmp4&source=youtube&signature=764D4B791947D93AC698F4A301CB5D59E5A3A033.36A0C1EF7DB28C9F1D1F403956AE8A2D919F4BB1";
        Uri vidUri = Uri.parse(vidAddress);
        vidView.setVideoURI(vidUri);

        //setting media controls
        MediaController vidControl = new MediaController(this);
        vidControl.setAnchorView(vidView);
        vidView.setMediaController(vidControl);

        //watch video listener
        btnWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vidView.start();
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

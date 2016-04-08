package d.wmcapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.media.MediaPlayer;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {

    MediaPlayer Media;
    Animation logoZoom;
    ImageView wmcLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //declare variables
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Media = MediaPlayer.create(this, R.raw.splashsound);
        wmcLogo = (ImageView)findViewById(R.id.wmcLogo);
        logoZoom = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomlogo);

        //initialise
        wmcLogo.startAnimation(logoZoom);
        Media.start();

        /****** Create Thread that will sleep for 5 seconds *************/
        Thread background = new Thread() {
            public void run() {
                try {
                    // Thread will sleep for 3 seconds
                    sleep(2500);

                    // After 3 seconds redirect to another intent
                    Intent i=new Intent(getBaseContext(),Login.class);
                    startActivity(i);


                    //Remove activity
                    Media.release();
                    finish();

                } catch (Exception e) {

                }
            }
        };

        // start thread
        background.start();

    }

}
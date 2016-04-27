package d.wmcapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.media.MediaPlayer;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {

    //declare variables
    MediaPlayer Media;
    Animation logoZoom;
    ImageView wmcLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //setup variables
        Media = MediaPlayer.create(this, R.raw.splashsound);
        wmcLogo = (ImageView)findViewById(R.id.wmcLogo);
        logoZoom = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomlogo);

        //initialise
        wmcLogo.startAnimation(logoZoom);
        Media.start();

        //thread task
        Thread background = new Thread() {
            public void run() {
                try {
                    // Thread will sleep for 2.5 seconds
                    sleep(2500);

                    // After 2.5 seconds redirect to another intent
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

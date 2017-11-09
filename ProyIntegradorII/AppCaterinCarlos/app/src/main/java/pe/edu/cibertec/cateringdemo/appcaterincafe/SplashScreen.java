package pe.edu.cibertec.cateringdemo.appcaterincafe;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;

import java.util.Timer;
import java.util.TimerTask;

import pe.edu.cibertec.cateringdemo.R;

public class SplashScreen extends Activity {

    // Duracion del splash screen
    private static final long SPLASH_SCREEN_DELAY = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                //Start the next activity
                Intent mainIntent = new Intent().setClass(
                        SplashScreen.this, Login.class);
                startActivity(mainIntent);

                // Close the activity so the user won't able to go back this
                // activity pressing Back button
                finish();
            }
        };

        // Simulate a long loading process on application startup.
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);
    }

}

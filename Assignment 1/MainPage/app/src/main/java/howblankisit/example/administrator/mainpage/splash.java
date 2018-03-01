package howblankisit.example.administrator.mainpage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import how.blank.isit.R;


public class splash extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_splash);
            final Intent it = new Intent(splash.this, MainActivity.class);
            Thread mt = new Thread(){
                public void run(){
                    try{
                        sleep(3000);


                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }finally{
                        startActivity(it);
                    }
                }

            };
            mt.start();
            /*
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(splash.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, SPLASH_TIME_OUT);*/
        }
        @Override
        protected void onPause(){
            super.onPause();
            finish();
        }
    }



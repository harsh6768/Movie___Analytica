package com.technohack.movie_analytica;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ImageView welcomeImg;
    private TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //for getting the full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);


        welcomeImg=findViewById(R.id.splash_img);
        welcomeText=findViewById(R.id.splash_text);


        //animation effect for the welcome screen so that we can enter the landing page after few seconds after loading the page

        Animation myAnim= AnimationUtils.loadAnimation(this,R.anim.mytransition);

        //Setting the animation
        welcomeImg.startAnimation(myAnim);
        welcomeText.startAnimation(myAnim);

        final Intent intent=new Intent(this, SignIn.class);

        //it's good to make a new thread so that a new handle the different process and remove the burden of the cpu from the single
        Thread timer=new Thread(){
            public void run(){
                try{
                    sleep(4000);
                }
                catch(InterruptedException e){
                    e.printStackTrace();
                }
                finally{
                    startActivity(intent);
                    finish();       //to finish the thread so that when we press the back button we can't move to  the welcome screen
                }
            }
        };

        //We need to start the thread by own
        timer.start();

    }
}

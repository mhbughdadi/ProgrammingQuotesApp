package com.example.mohammed.programmingquotesapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mohammed.programmingquotesapp.ui.activities.MainActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Mohammed on 02/08/2017.
 */

public class SplashActivity extends AppCompatActivity {
    @InjectView(R.id.splash_activity_iv)
    ImageView splashIv;
    @InjectView(R.id.splash_activity_tv)
    TextView tvAppName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.inject(this);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Courgette-Regular.ttf");
        tvAppName.setTypeface(custom_font);
        StartAnimations();
        Handler handler =new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashActivity.this, MainActivity.class);
                overridePendingTransition(R.animator.activity_close_translate,R.animator.activity_open_scale);
                SplashActivity.this.startActivity(intent);
                SplashActivity.this.finish();
            }
        },3000);


    }
    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this,R.anim.alpha);
        anim.reset();
        splashIv.clearAnimation();
        splashIv.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translation);
        anim.reset();
        tvAppName.clearAnimation();
        tvAppName.startAnimation(anim);

    }
}

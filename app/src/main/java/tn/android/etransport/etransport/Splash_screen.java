package tn.android.etransport.etransport;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Splash_screen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_splash_screen);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
        ImageView img= (ImageView) findViewById(R.id.Splash_logo);
        img.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
            Splash_screen.this.finish();
            Intent intent= new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

}

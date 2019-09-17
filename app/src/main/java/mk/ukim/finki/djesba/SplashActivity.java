package mk.ukim.finki.djesba;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    Button start;
    Animation fromBottom, fromTop;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        start = findViewById(R.id.getStarted);
        fromBottom = AnimationUtils.loadAnimation(this,R.anim.from_bottom);

        imageView = findViewById(R.id.logo);
        fromTop = AnimationUtils.loadAnimation(this,R.anim.from_top);

        start.setAnimation(fromBottom);
        imageView.setAnimation(fromTop);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),IntroActivity.class));
                finish();
            }
        });

    }
}

package mk.ukim.finki.djesba;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;

public class IntroActivity extends AppCompatActivity {

    ViewPager introViewPager;
    IntroAdapter introAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        introViewPager = findViewById(R.id.introPager);
        introAdapter = new IntroAdapter(getSupportFragmentManager());
        introViewPager.setAdapter(introAdapter);
    }
}

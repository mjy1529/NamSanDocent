package docent.namsanhanok.Home;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import docent.namsanhanok.R;

public class TutorialActivity extends AppCompatActivity {

    RelativeLayout homeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);



        homeLayout = (RelativeLayout) findViewById(R.id.homeLayout);
        final int blackFilter = getApplication().getResources().getColor(R.color.black_color_filter);
        final PorterDuffColorFilter blakcColorFilter = new PorterDuffColorFilter(blackFilter, PorterDuff.Mode.SRC_ATOP);

        homeLayout.getBackground().setColorFilter(blakcColorFilter);



    }
}

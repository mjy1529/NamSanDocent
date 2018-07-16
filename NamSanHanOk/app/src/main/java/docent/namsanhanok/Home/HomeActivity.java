package docent.namsanhanok.Home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import docent.namsanhanok.Docent.DocentActivity;
import docent.namsanhanok.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = new Intent(this, DocentActivity.class);
        startActivity(intent);
        finish();
    }
}

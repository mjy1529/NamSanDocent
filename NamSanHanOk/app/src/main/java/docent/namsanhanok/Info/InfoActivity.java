package docent.namsanhanok.Info;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import docent.namsanhanok.Home.HomeActivity;
import docent.namsanhanok.R;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        init();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.homeBtn :
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    public void init() {
        Toolbar infoToolbar = (Toolbar)findViewById(R.id.infoToolbar);
        setSupportActionBar(infoToolbar);


    }
}

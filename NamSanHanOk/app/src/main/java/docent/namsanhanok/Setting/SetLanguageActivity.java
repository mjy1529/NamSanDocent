package docent.namsanhanok.Setting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import docent.namsanhanok.Home.HomeActivity;
import docent.namsanhanok.R;

public class SetLanguageActivity extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_language);

        init();
    }

    public void onClick(View v) {
        intent = new Intent(SetLanguageActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void init() {
        ListView languageListview = (ListView)findViewById(R.id.languageListview);
        ArrayAdapter<CharSequence> setLanguageAdapter = ArrayAdapter.createFromResource(this, R.array.languages_array, android.R.layout.simple_list_item_single_choice);
        languageListview.setAdapter(setLanguageAdapter);
    }
}

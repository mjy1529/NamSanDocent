package docent.namsanhanok.Setting;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Checkable;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.List;

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
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void init() {
        ListView languageListview = (ListView) findViewById(R.id.languageListview);

        ArrayAdapter<CharSequence> setLanguageAdapter
                = ArrayAdapter.createFromResource(this, R.array.languages_array, android.R.layout.simple_list_item_single_choice);
        languageListview.setAdapter(setLanguageAdapter);
        languageListview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        languageListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0: //한국어
                        Toast.makeText(SetLanguageActivity.this, "한국어", Toast.LENGTH_SHORT).show();
                        break;
                    case 1: //영어
                        Toast.makeText(SetLanguageActivity.this, "영어", Toast.LENGTH_SHORT).show();
                        break;
                    case 2: //일본어
                        Toast.makeText(SetLanguageActivity.this, "일본어", Toast.LENGTH_SHORT).show();
                        break;
                    case 3: //중국어
                        Toast.makeText(SetLanguageActivity.this, "중국어", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        TextView setLanguageTitle = (TextView) findViewById(R.id.setLanguageTitle);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "NanumMyeongjoBold.otf");
        setLanguageTitle.setTypeface(typeface);
    }
}

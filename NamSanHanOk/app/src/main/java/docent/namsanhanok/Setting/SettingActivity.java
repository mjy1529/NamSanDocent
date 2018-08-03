package docent.namsanhanok.Setting;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.List;

import docent.namsanhanok.Home.HomeActivity;
import docent.namsanhanok.R;

public class SettingActivity extends AppCompatActivity {

    ListView settingListview;
    static final String[] settingMenu = {"언어 설정(Language)"};
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        init();
    }

    public void init() {
        settingListview = (ListView)findViewById(R.id.settingListview);
        ArrayAdapter settingAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, settingMenu);
        settingListview.setAdapter(settingAdapter);

        settingListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0 : //언어 설정
                        intent = new Intent(SettingActivity.this, SetLanguageActivity.class);
                        startActivity(intent);
                        break;

                }
            }
        });
    }

    public void onClick(View v) {
        intent = new Intent(SettingActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}

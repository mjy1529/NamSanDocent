package docent.namsanhanok.Home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.github.angads25.toggle.LabeledSwitch;
import com.github.angads25.toggle.interfaces.OnToggledListener;

import docent.namsanhanok.Docent.DocentActivity;
import docent.namsanhanok.R;

public class HomeActivity extends AppCompatActivity {

    ImageButton settingBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();

//        Intent intent = new Intent(this, DocentActivity.class);
//        startActivity(intent);
//        finish();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settingBtn : //세팅 버튼을 클릭했을 때
                Toast.makeText(this, "setting", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void init() {
        settingBtn = (ImageButton)findViewById(R.id.settingBtn);

        LabeledSwitch toggleBtn = findViewById(R.id.toggleBtn);
        toggleBtn.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(LabeledSwitch labeledSwitch, boolean isOn) {
                //toggle Button이 On일 때
                Toast.makeText(HomeActivity.this, "ON", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

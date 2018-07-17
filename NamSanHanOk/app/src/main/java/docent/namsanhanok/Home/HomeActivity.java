package docent.namsanhanok.Home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.github.angads25.toggle.LabeledSwitch;
import com.github.angads25.toggle.interfaces.OnToggledListener;

import docent.namsanhanok.Category.CategoryActivity;
import docent.namsanhanok.Category.CategoryListActivity;
import docent.namsanhanok.R;

public class HomeActivity extends AppCompatActivity {

    ImageButton settingBtn;
    ImageButton cateBtn;

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
            //임시
            case R.id.cateBtn :
//                Toast.makeText(this, "category", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void init() {
        settingBtn = (ImageButton)findViewById(R.id.settingBtn);
        //임시
        cateBtn = (ImageButton)findViewById(R.id.cateBtn);

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

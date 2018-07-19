package docent.namsanhanok.Home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.github.angads25.toggle.LabeledSwitch;
import com.github.angads25.toggle.interfaces.OnToggledListener;
import docent.namsanhanok.Category.CategoryActivity;

import docent.namsanhanok.Event.EventActivity;
import docent.namsanhanok.Notice.NoticeActivity;
import docent.namsanhanok.Question.QuestionWriteActivity;
import docent.namsanhanok.R;
import docent.namsanhanok.Setting.SettingActivity;

public class HomeActivity extends AppCompatActivity {

    ImageButton settingBtn;
    ImageView menuBtn1, menuBtn2, menuBtn3, menuBtn4, menuBtn5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();

        //Toggle Button
        LabeledSwitch toggleBtn = findViewById(R.id.toggleBtn);
        toggleBtn.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(LabeledSwitch labeledSwitch, boolean isOn) {
                Toast.makeText(HomeActivity.this, "ON", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.settingBtn : //설정 버튼
                intent = new Intent(HomeActivity.this, SettingActivity.class);
                startActivity(intent);
                break;

            case R.id.menuBtn1 : //마을 둘러보기
                intent = new Intent(HomeActivity.this, CategoryActivity.class);
                startActivity(intent);
                break;

            case R.id.menuBtn2 : //세시/행사
                intent = new Intent(getApplicationContext(), EventActivity.class);
                startActivity(intent);
                break;

            case R.id.menuBtn3 : //알리는 말씀
                intent = new Intent(HomeActivity.this, NoticeActivity.class);
                startActivity(intent);
                break;

            case R.id.menuBtn4 : //문의하기
                intent = new Intent(HomeActivity.this, QuestionWriteActivity.class);
                startActivity(intent);
                break;

            case R.id.menuBtn5 : //안내
                Toast.makeText(this, "이용안내", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void init() {
        Toolbar homeToolbar = (Toolbar)findViewById(R.id.homeToolbar);
        homeToolbar.bringToFront();

        settingBtn = (ImageButton)findViewById(R.id.settingBtn);

        menuBtn1 = (ImageView)findViewById(R.id.menuBtn1);
        menuBtn2 = (ImageView)findViewById(R.id.menuBtn2);
        menuBtn3 = (ImageView)findViewById(R.id.menuBtn3);
        menuBtn4 = (ImageView)findViewById(R.id.menuBtn4);
        menuBtn5 = (ImageView)findViewById(R.id.menuBtn5);

        RelativeLayout homeLayout = (RelativeLayout)findViewById(R.id.homeLayout);
        homeLayout.getBackground().setAlpha(220);
    }

}

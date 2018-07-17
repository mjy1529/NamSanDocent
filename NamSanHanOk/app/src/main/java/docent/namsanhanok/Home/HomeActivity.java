package docent.namsanhanok.Home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.angads25.toggle.LabeledSwitch;
import com.github.angads25.toggle.interfaces.OnToggledListener;

import docent.namsanhanok.R;

public class HomeActivity extends AppCompatActivity {

    ImageButton settingBtn;
    ImageView menuBtn1, menuBtn2, menuBtn3, menuBtn4;

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

//        Intent intent = new Intent(this, DocentActivity.class);
//        startActivity(intent);
//        finish();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settingBtn : //설정 버튼
                Toast.makeText(this, "setting", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menuBtn1 : //마을 둘러보기
                Toast.makeText(this, "마을 둘러보기", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menuBtn2 : //세시/행사
                Toast.makeText(this, "세시/행사", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menuBtn3 : //질문게시판
                Toast.makeText(this, "질문게시판", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menuBtn4 : //이용안내
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

        RelativeLayout homeLayout = (RelativeLayout)findViewById(R.id.homeLayout);
        homeLayout.getBackground().setAlpha(220);
    }

}

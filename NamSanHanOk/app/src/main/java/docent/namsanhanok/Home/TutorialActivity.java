package docent.namsanhanok.Home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.angads25.toggle.LabeledSwitch;

import docent.namsanhanok.R;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;
import uk.co.deanwild.materialshowcaseview.target.Target;

public class TutorialActivity extends AppCompatActivity {

    private static final String SHOWCASE_ID = "tutorial";
    LinearLayout tuto_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();

        presentShowcaseSequence();
    }



    private void presentShowcaseSequence() {

        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500); // half second between each showcase view

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, SHOWCASE_ID);


        sequence.setOnItemDismissedListener(new MaterialShowcaseSequence.OnSequenceItemDismissedListener() {
            @Override
            public void onDismiss(MaterialShowcaseView materialShowcaseView, int i) {
                if(i == 0){
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });


        sequence.setConfig(config);

        sequence.addSequenceItem(tuto_layout, "자동전시안내를 원하시면 " + "\n" + "블루투스를 켜주세요", "완료");
//        sequence.addSequenceItem((MaterialShowcaseView) Target.NONE, "튜토리얼을 시작하겠습니다.", "다음"); //안됨


//        sequence.addSequenceItem(
//                new MaterialShowcaseView.Builder(this)
//                .setTarget(autoPlaytext)
//                .setDismissText("다음")
//                .setContentText("자동전시를 원하시면 블루투스를 켜주세요")
//                .singleUse(SHOWCASE_ID) // provide a unique ID used to ensure it is only shown once
//                .setMaskColour(getResources().getColor(R.color.light_green))
//                .setShapePadding(20)
//                .build()
//        );


        sequence.start();

    }





    public void init(){
        tuto_layout = (LinearLayout) findViewById(R.id.tuto_focus_layout);

    }
}

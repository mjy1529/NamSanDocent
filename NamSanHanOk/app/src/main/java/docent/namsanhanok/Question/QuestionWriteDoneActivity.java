package docent.namsanhanok.Question;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import docent.namsanhanok.Home.HomeActivity;
import docent.namsanhanok.R;

public class QuestionWriteDoneActivity extends Activity {

    Button doneBtn;
    QuestionWriteActivity writeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_question_write_done);

        init();
    }

    public void init() {
        writeActivity = (QuestionWriteActivity) QuestionWriteActivity.writeActivity;
        doneBtn = (Button) findViewById(R.id.question_write_done_Btn);

        TextView question_register_title = (TextView) findViewById(R.id.question_register_title);
        TextView question_register_content = (TextView) findViewById(R.id.question_register_content);

        Intent intent = getIntent();
        String responseResult = intent.getStringExtra("responseResult");

        if (responseResult.equals("success")) {
            question_register_title.setText(R.string.questionSuccessTitle);
            question_register_content.setText(R.string.questionSuccessMessage);
        } else {
            question_register_title.setText(R.string.questionFailTitle);
            question_register_content.setText(R.string.questionFailMessage);
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        writeActivity.finish();
        finish();
    }

    public void onClick(View v) throws InterruptedException {
        switch (v.getId()) {
            case R.id.question_write_done_Btn:
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                writeActivity.finish();
                finish();
                break;

        }
    }
}

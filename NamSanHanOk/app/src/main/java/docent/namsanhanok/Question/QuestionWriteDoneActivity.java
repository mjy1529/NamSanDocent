package docent.namsanhanok.Question;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import docent.namsanhanok.Home.HomeActivity;
import docent.namsanhanok.R;

public class QuestionWriteDoneActivity extends AppCompatActivity {

    Button doneBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_write_done);

        doneBtn = (Button) findViewById(R.id.question_write_done_Btn);
    }

    public void onClick(View v) throws InterruptedException {
        switch (v.getId()) {
            case  R.id.question_write_done_Btn :
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
                break;

        }
    }
}

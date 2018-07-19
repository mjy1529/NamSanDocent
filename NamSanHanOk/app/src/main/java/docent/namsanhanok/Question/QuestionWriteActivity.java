package docent.namsanhanok.Question;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import docent.namsanhanok.R;

public class QuestionWriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionwrite);

        init();
    }

    public void init() {
        Toolbar questionRegisterToolbar = (Toolbar)findViewById(R.id.question_register_toolbar);
        questionRegisterToolbar.bringToFront();


    }
}

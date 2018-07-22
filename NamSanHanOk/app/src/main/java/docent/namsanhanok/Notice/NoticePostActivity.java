package docent.namsanhanok.Notice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import docent.namsanhanok.R;

public class NoticePostActivity extends AppCompatActivity {

    EditText title;
    EditText content;
    ImageButton canelBtn;
    Button registPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_post);

        init();

    }

    public void init() {
        Toolbar questionRegisterToolbar = (Toolbar)findViewById(R.id.notice_register_toolbar);
        questionRegisterToolbar.bringToFront();

        title = (EditText) findViewById(R.id.notice_post_title);
        content = (EditText) findViewById(R.id.notice_post_content);
        canelBtn = (ImageButton) findViewById(R.id.notice_register_cancelBtn);
        registPost = (Button) findViewById(R.id.notice_register_Btn);


    }


    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.notice_register_Btn:
                //게시글 내용 넘겨줘야함
                Toast.makeText(this, "결과값"
                        + "\n"+ title.getText().toString()
                        + "\n" + content.getText().toString(), Toast.LENGTH_LONG).show();

//                Thread.sleep(3500);
//                intent = new Intent(getApplicationContext(), NoticeActivity.class);
//                startActivity(intent);
                finish();
                break;

            case R.id.notice_register_cancelBtn :
//                intent = new Intent(getApplicationContext(), NoticeActivity.class);
//                startActivity(intent);
                finish();
                break;
        }
    }
}

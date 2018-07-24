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

    public boolean detectError() {
        int i = 0;

        if(content.getText().toString().replace(" ", "").equals("")
                || content.getText().toString().replace("\n", "").equals("")){
            content.requestFocus();
            content.setText(null);//공백이 여러개 있을시, 처음으로
            i++;
        }
        if(title.getText().toString().replace("", "").equals("")){
            title.requestFocus();
            title.setText(null);
            i++;
        }

        if(i == 0 ){
            return false;
        }
        else{
            return true;
        }

    }


    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.notice_register_Btn:
                if(!detectError()){
                    //게시글 내용 넘겨줘야함
                    Toast.makeText(this, "결과값"
                            + "\n"+ title.getText().toString()
                            + "\n" + content.getText().toString(), Toast.LENGTH_LONG).show();
//                intent = new Intent(getApplicationContext(), NoticeActivity.class);
//                startActivity(intent);
                    finish();
                    break;
                }
                else{
                    Toast.makeText(this, "빈칸을 채워주세요", Toast.LENGTH_SHORT).show();
                    break;
                }


            case R.id.notice_register_cancelBtn :
//                intent = new Intent(getApplicationContext(), NoticeActivity.class);
//                startActivity(intent);
                finish();
                break;
        }
    }
}

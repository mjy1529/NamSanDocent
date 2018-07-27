package docent.namsanhanok.Question;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import docent.namsanhanok.R;

public class QuestionWriteActivity extends AppCompatActivity implements EditText.OnFocusChangeListener {
    Button post_register_Btn;
    ImageButton cancelBtn;

    //WriteDoneAcitivy에서 현재 Activity 종료
    public static AppCompatActivity writeActivity;

    EditText email_first_address;
    EditText phone_number;
    EditText username;
    EditText title;
    EditText content;
    LinearLayout content_layout;

    ArrayList<String> nullValue;

    InputMethodManager imm; //spinner 선택시, 키보드 안나오게
    NestedScrollView scrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionwrite);

        writeActivity = QuestionWriteActivity.this;

        init();
        email_first_address.setBackgroundResource(R.drawable.rectangle_edge_all_selected);


    }








    public void init() {
        Toolbar questionRegisterToolbar = (Toolbar)findViewById(R.id.question_register_toolbar);
        questionRegisterToolbar.bringToFront();

        post_register_Btn = (Button) findViewById(R.id.question_register_Btn);
        cancelBtn = (ImageButton) findViewById(R.id.question_register_cancelBtn);

        email_first_address = (EditText) findViewById(R.id.question_email_first_address);
        phone_number = (EditText) findViewById(R.id.question_phone_number);
        username = (EditText) findViewById(R.id.question_username);


        title = (EditText) findViewById(R.id.question_title);
        content = (EditText) findViewById(R.id.question_content);
        content_layout = (LinearLayout) findViewById(R.id.question_content_layout);



        //Focus가 변할 때, line color 변경
        email_first_address.setOnFocusChangeListener(this);
        phone_number.setOnFocusChangeListener(this);
        username.setOnFocusChangeListener(this);
        title.setOnFocusChangeListener(this);
        content.setOnFocusChangeListener(this);


        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        scrollView = (NestedScrollView) findViewById(R.id.getScroll_location);


    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch(v.getId()){
            case R.id.question_email_first_address :
                email_first_address.setBackgroundResource(R.drawable.rectangle_edge_all_selected);
                if(getCurrentFocus()!=email_first_address){email_first_address.setBackgroundResource(R.drawable.rectangle_edge_all);}
                break;

            case R.id.question_phone_number :
                phone_number.setBackgroundResource(R.drawable.rectangle_edge_all_selected);
                if(getCurrentFocus()!=phone_number){phone_number.setBackgroundResource(R.drawable.rectangle_edge_all);}

                break;

            case R.id.question_username :
                username.setBackgroundResource(R.drawable.rectangle_edge_all_selected);
                if(getCurrentFocus()!=username){username.setBackgroundResource(R.drawable.rectangle_edge_all);}
                break;

            case R.id.question_title :
                title.setBackgroundResource(R.drawable.rectangle_edge_all_selected);
                if(getCurrentFocus()!=title){title.setBackgroundResource(R.drawable.rectangle_edge_all);}
                break;

            case R.id.question_content :
                content_layout.setBackgroundResource(R.drawable.rectangle_edge_all_selected);
                if(getCurrentFocus()!=content){content_layout.setBackgroundResource(R.drawable.rectangle_edge_all);}
                break;

        }
    }


//비어잇으면 상자 표시 붉게
    public boolean detectError() {

        nullValue = new ArrayList<>();

        //입력된 것이 없거나, 빈칸일 때
        if(content.getText().toString().replace(" ", "").equals("")
                || content.getText().toString().replace("\n", "").equals("")){
            nullValue.add("내용을 입력해주세요");
            content.requestFocus();
            content.setText(null);//공백이 여러개 있을시, 처음으로

        }
        if(title.getText().toString().replace(" ", "").equals("")){
            nullValue.add("제목을 입력해주세요");
            title.requestFocus();
            title.setText(null);
        }

        if(username.getText().toString().replace(" ", "").equals("")){
            nullValue.add("성함을 입력해주세요");
            username.requestFocus();
            username.setText(null);
        }
        if(phone_number.getText().toString().replace(" ", "").equals("")){
            nullValue.add("전화번호를 입력해주세요");
            phone_number.requestFocus();
            phone_number.setText(null);
        }
        if(email_first_address.getText().toString().replace(" ", "").equals("")){

            nullValue.add("이메일을 입력해주세요");
            email_first_address.requestFocus();
            email_first_address.setText(null);
        }


        if(nullValue.size() <= 0 ){ //빈칸이 없음
            return false;
        }
        else{
            return  true;
        }

    }

    public void onClick(View v) throws InterruptedException {
        switch (v.getId()) {
            case  R.id.question_register_Btn :
                //제목 and 내용 DB에 저장해야함, 값은 이렇게 갖고옴
//                Toast.makeText(this, "결과값"
//                        + "\n" + email_first_address.getText().toString()
//                        + "\n" + phone_number.getText().toString()
//                        + "\n" + username.getText().toString()
//                        + "\n"+ title.getText().toString()
//                        + "\n" + content.getText().toString(), Toast.LENGTH_SHORT).show();



                //DB저장
                //내용

                //에러 유무에 따라 activity 넘김
                if(!detectError()){//빈칸이 없으면
                    //그 다음, Question Board Activity로
                    Intent intent = new Intent(getApplicationContext(), QuestionWriteDoneActivity.class);
                    startActivity(intent);
                    break;
                }
                else{ //빈칸이 있으면

                    if(getCurrentFocus()==content){
                        scrollView.setScrollY(scrollView.getScrollY());

                    }
                    else{
                        scrollView.setScrollY(scrollView.getScrollY()-250);

                    }
                    Toast.makeText(this, nullValue.get(nullValue.size()-1) , Toast.LENGTH_SHORT).show();

                    break;
                }

            case R.id.question_register_cancelBtn:
                onBackPressed();
                finish();
                break;


        }
    }


}

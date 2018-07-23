package docent.namsanhanok.Question;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.security.Key;
import java.util.ArrayList;

import docent.namsanhanok.R;
import me.srodrigo.androidhintspinner.HintAdapter;
import me.srodrigo.androidhintspinner.HintSpinner;

public class QuestionWriteActivity extends AppCompatActivity implements EditText.OnFocusChangeListener {
    Button post_register_Btn;
    ImageButton cancelBtn;

    private Spinner question_category_spinner;
    ArrayList<String> question_category_spinner_list;
    Object selected_spinner;

    //WriteDoneAcitivy에서 현재 Activity 종료
    public static AppCompatActivity writeActivity;

    EditText email_first_address;
    EditText phone_number;
    EditText username;
    EditText title;
    EditText content;
    LinearLayout content_layout;

    ArrayList<String> nullValue;

    InputMethodManager imm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionwrite);

        writeActivity = QuestionWriteActivity.this;

        init();
        setCategorySpinner();
        email_first_address.setBackgroundResource(R.drawable.rectangle_edge_all_selected);


    }


//    public void getFocus(){
//        if(getCurrentFocus() == email_first_address){
//            // your view is in focus
//            email_first_address.setBackgroundResource(R.drawable.rectangle_edge_all_selected);
//        }else{
//            // not in the focus
//            email_first_address.setBackgroundResource(R.drawable.rectangle_edge_all);
//        }
//
//        if(getCurrentFocus() == phone_number){
//            // your view is in focus
//            phone_number.setBackgroundResource(R.drawable.rectangle_edge_all_selected);
//        }else{
//            // not in the focus
//            phone_number.setBackgroundResource(R.drawable.rectangle_edge_all);
//        }
//    }

    //spinner hint 설정
    public void setSpinner(final Spinner spinner, final String hint, ArrayList<String> spinner_list) {
        HintSpinner<String> hintSpinner = new HintSpinner<>(
                spinner,
                // Default layout - You don't need to pass in any layout id, just your hint text and
                // your list data
                new HintAdapter<>(this, hint, spinner_list),
                new HintSpinner.Callback<String>() {
                    @Override
                    public void onItemSelected(int position, String itemAtPosition) {
                        // Here you handle the on item selected event (this skips the hint selected event)
                        Toast.makeText(QuestionWriteActivity.this,"선택된 아이템 : "+ spinner.getItemAtPosition(position),Toast.LENGTH_SHORT).show();
                        selected_spinner = spinner.getItemAtPosition(position);
                    }
                });

        hintSpinner.init();


    }



    public void setCategorySpinner() {

        question_category_spinner_list = new ArrayList<>();
        question_category_spinner_list.add("공연");
        question_category_spinner_list.add("체험");
        question_category_spinner_list.add("세시");
        question_category_spinner_list.add("교육");
        question_category_spinner_list.add("전통혼례");
        question_category_spinner_list.add("편의시설");

        setSpinner(question_category_spinner, "게시판을 선택하세요", question_category_spinner_list);

//        ArrayAdapter spinnerAdapter;
//        spinnerAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, question_spinner_list);
//        question_category_spinner.setAdapter(spinnerAdapter);


//        //event listener
//        question_category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(QuestionWriteActivity.this,"선택된 아이템 : "+ question_category_spinner.getItemAtPosition(position),Toast.LENGTH_SHORT).show();
//                //question_spinner.getItemAtPosition(position) 이걸 넘겨줘야 함 DB에
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
    }



    public void init() {
        Toolbar questionRegisterToolbar = (Toolbar)findViewById(R.id.question_register_toolbar);
        questionRegisterToolbar.bringToFront();

        post_register_Btn = (Button) findViewById(R.id.question_register_Btn);
        cancelBtn = (ImageButton) findViewById(R.id.question_register_cancelBtn);

        email_first_address = (EditText) findViewById(R.id.question_email_first_address);
        phone_number = (EditText) findViewById(R.id.question_phone_number);
        username = (EditText) findViewById(R.id.question_username);

        question_category_spinner = (Spinner) findViewById(R.id.question_category_Spinner);
//        question_category_spinner.performClick();
        question_category_spinner.setFocusable(true);
        question_category_spinner.setFocusableInTouchMode(true);

        title = (EditText) findViewById(R.id.question_title);
        content = (EditText) findViewById(R.id.question_content);
        content_layout = (LinearLayout) findViewById(R.id.question_content_layout);



        //Focus가 변할 때, line color 변경
        email_first_address.setOnFocusChangeListener(this);
        phone_number.setOnFocusChangeListener(this);
        username.setOnFocusChangeListener(this);
        question_category_spinner.setOnFocusChangeListener(this);
        title.setOnFocusChangeListener(this);
        content.setOnFocusChangeListener(this);


        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);



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

            case R.id.question_category_Spinner :
                question_category_spinner.setBackgroundResource(R.drawable.rectangle_edge_all_selected);
                imm.hideSoftInputFromWindow(question_category_spinner.getWindowToken(), 0);
                if(getCurrentFocus()!=question_category_spinner){question_category_spinner.setBackgroundResource(R.drawable.rectangle_edge_all);}
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


        if(content.getText().toString().getBytes().length <= 0){
            nullValue.add("내용을 입력해주세요");
            content.requestFocus();
        }
        if(title.getText().toString().getBytes().length <= 0){
            nullValue.add("제목을 입력해주세요");
            title.requestFocus();
        }
        if(selected_spinner==null){
            nullValue.add("구분을 선택해주세요");
            question_category_spinner.requestFocus();
//            question_category_spinner.performClick();
        }
        if(username.getText().toString().getBytes().length <= 0){
            nullValue.add("성함을 입력해주세요");
            username.requestFocus();
        }
        if(phone_number.getText().toString().getBytes().length <= 0){
            nullValue.add("전화번호를 입력해주세요");
            phone_number.requestFocus();
        }
        if(email_first_address.getText().toString().getBytes().length <= 0){

            nullValue.add("이메일을 입력해주세요");
            email_first_address.requestFocus();
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
//                Toast.makeText(this, "결과값" + "\n" + email_first_address.getText().toString()
//                        + "\n" + selected_spinner.toString()
//                        + "\n" + phone_number.getText().toString()
//                        + "\n" + username.getText().toString()
//                        + "\n"+ title.getText().toString()
//                        + "\n" + content.getText().toString(), Toast.LENGTH_SHORT).show();


                //에러 탐지
                detectError();

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

//                    String nullStrings = "";
//                    for (int i = 0; i < nullValue.size() ; i++) {
//                        if(i == nullValue.size()-1){
//                            nullStrings += nullValue.get(i).toString();
//                        }
//                        else{
//                            nullStrings += nullValue.get(i).toString() + "\n";
//                        }
//
//                    }
                    Toast.makeText(this, nullValue.get(nullValue.size()-1).toString(), Toast.LENGTH_SHORT).show();

                    break;
                }

            case R.id.question_register_cancelBtn:
                onBackPressed();
                finish();
                break;


        }
    }
}

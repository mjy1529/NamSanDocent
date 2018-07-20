package docent.namsanhanok.Question;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

import docent.namsanhanok.Home.HomeActivity;
import docent.namsanhanok.R;
import me.srodrigo.androidhintspinner.HintAdapter;
import me.srodrigo.androidhintspinner.HintSpinner;

public class QuestionWriteActivity extends AppCompatActivity {
    Button post_register_Btn;
    ImageButton cancelBtn;

    private Spinner question_category_spinner;
    ArrayList<String> question_category_spinner_list;
    Object selected_spinner;

    EditText email_first_address;
    EditText phone_number;
    EditText username;
    EditText title;
    EditText content;

    ArrayList<String> nullValue;
    String default_spinner;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionwrite);

        init();
        setCategorySpinner();
    }

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

        question_category_spinner = (Spinner) findViewById(R.id.question_category_Spinner);

        email_first_address = (EditText) findViewById(R.id.question_email_first_address);
        phone_number = (EditText) findViewById(R.id.question_phone_number);
        username = (EditText) findViewById(R.id.question_username);
        title = (EditText) findViewById(R.id.question_title);
        content = (EditText) findViewById(R.id.question_content);

    }

    public boolean detectError() {

        nullValue = new ArrayList<>();

        int i=0;

        if(email_first_address.getText().toString().getBytes().length <= 0){
            nullValue.add("이메일을 입력해주세요");
            i++;
        }
        if(selected_spinner==null){
            nullValue.add("구분을 선택해주세요");
            i++;
        }
        if(phone_number.getText().toString().getBytes().length <= 0){
            nullValue.add("전화번호를 입력해주세요");
            i++;
        }
        if(username.getText().toString().getBytes().length <= 0){
            nullValue.add("성함을 입력해주세요");
            i++;
        }
        if(title.getText().toString().getBytes().length <= 0){
            nullValue.add("제목을 입력해주세요");
            i++;
        }
        if(content.getText().toString().getBytes().length <= 0){
            nullValue.add("내용을 입력해주세요");
            i++;
        }

//        email_first_address.getText().toString()
//                , selected_spinner.toString()
//                , phone_number.getText().toString()
//                , username.getText().toString()
//                , title.getText().toString()
//                , content.getText().toString()


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
                //제목 and 내용 DB에 저장해야함
//                Toast.makeText(this, "결과값" + "\n" + email_first_address.getText().toString()
//                        + "\n" + selected_spinner.toString()
//                        + "\n" + phone_number.getText().toString()
//                        + "\n" + username.getText().toString()
//                        + "\n"+ title.getText().toString()
//                        + "\n" + content.getText().toString(), Toast.LENGTH_SHORT).show();

                if(!detectError()){//빈칸이 없으면
                    //그 다음, Question Board Activity로
                    Intent intent = new Intent(getApplicationContext(), QuestionWriteDoneActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                }
                else{ //빈칸이 있으면

//                }
                    String nullStrings = "";
                    for (int i = 0; i < nullValue.size() ; i++) {
                        if(i == nullValue.size()-1){
                            nullStrings += nullValue.get(i).toString();
                        }
                        else{
                            nullStrings += nullValue.get(i).toString() + "\n";
                        }

                    }
                    Toast.makeText(this, nullStrings, Toast.LENGTH_LONG).show();


                    break;
                }

            case R.id.question_register_cancelBtn:
                onBackPressed();
                finish();
                break;


        }
    }
}

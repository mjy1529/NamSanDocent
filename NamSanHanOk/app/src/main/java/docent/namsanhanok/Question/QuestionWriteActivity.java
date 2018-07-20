package docent.namsanhanok.Question;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import docent.namsanhanok.Home.HomeActivity;
import docent.namsanhanok.R;
import me.srodrigo.androidhintspinner.HintAdapter;
import me.srodrigo.androidhintspinner.HintSpinner;

public class QuestionWriteActivity extends AppCompatActivity {
    Button post_register_Btn;
    ImageButton cancelBtn;

    private Spinner question_category_spinner;
    private Spinner question_email_spinner;
    ArrayList<String> question_category_spinner_list;
    ArrayList<String> question_email_spinner_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionwrite);

        init();
        setCategorySpinner();
//        setEmailSpinner();
    }

    public void setEmailSpinner(){

        question_email_spinner_list = new ArrayList<>();
        question_email_spinner_list.add("naver.com");
        question_email_spinner_list.add("daum.net");
        question_email_spinner_list.add("gmail.com");

        setSpinner(question_email_spinner, "이메일", question_email_spinner_list);

    }

    //spinner hint 설정
    public void setSpinner(final Spinner spinner, String hint, ArrayList<String> spinner_list) {
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
//        question_email_spinner = (Spinner) findViewById(R.id.question_email_Spinner);
    }



    public void onClick(View v) {
        switch (v.getId()) {
            case  R.id.question_register_Btn :
                //제목 and 내용 DB에 저장해야함


                //그 다음, Question Board Activity로
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                break;

            case R.id.question_register_cancelBtn:
                onBackPressed();
                break;


        }
    }
}

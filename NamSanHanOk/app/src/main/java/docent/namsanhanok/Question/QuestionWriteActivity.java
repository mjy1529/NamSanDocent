package docent.namsanhanok.Question;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import docent.namsanhanok.Application;
import docent.namsanhanok.NetworkService;
import docent.namsanhanok.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionWriteActivity extends AppCompatActivity implements EditText.OnFocusChangeListener {
    ImageButton post_register_Btn;
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

    String responseStr; //서버 응답

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionwrite);

        writeActivity = QuestionWriteActivity.this;

        init();
        email_first_address.setBackgroundResource(R.drawable.rectangle_edge_all_selected);
    }

    public void init() {
        Toolbar questionRegisterToolbar = (Toolbar) findViewById(R.id.question_register_toolbar);
        questionRegisterToolbar.bringToFront();

        Intent intent = getIntent();
        TextView question_register_title = (TextView) findViewById(R.id.question_register_title);
        question_register_title.setText(intent.getStringExtra("question_title"));

        post_register_Btn = (ImageButton) findViewById(R.id.question_register_Btn);
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

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        scrollView = (NestedScrollView) findViewById(R.id.getScroll_location);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.question_email_first_address:
                email_first_address.setBackgroundResource(R.drawable.rectangle_edge_all_selected);
                if (getCurrentFocus() != email_first_address) {
                    email_first_address.setBackgroundResource(R.drawable.rectangle_edge_all);
                }
                break;

            case R.id.question_phone_number:
                phone_number.setBackgroundResource(R.drawable.rectangle_edge_all_selected);
                if (getCurrentFocus() != phone_number) {
                    phone_number.setBackgroundResource(R.drawable.rectangle_edge_all);
                }

                break;

            case R.id.question_username:
                username.setBackgroundResource(R.drawable.rectangle_edge_all_selected);
                if (getCurrentFocus() != username) {
                    username.setBackgroundResource(R.drawable.rectangle_edge_all);
                }
                break;

            case R.id.question_title:
                title.setBackgroundResource(R.drawable.rectangle_edge_all_selected);
                if (getCurrentFocus() != title) {
                    title.setBackgroundResource(R.drawable.rectangle_edge_all);
                }
                break;

            case R.id.question_content:
                content_layout.setBackgroundResource(R.drawable.rectangle_edge_all_selected);
                if (getCurrentFocus() != content) {
                    content_layout.setBackgroundResource(R.drawable.rectangle_edge_all);
                }
                break;

        }
    }

    //비어잇으면 상자 표시 붉게
    public boolean detectError() {

        nullValue = new ArrayList<>();

        //입력된 것이 없거나, 빈칸일 때
        if (content.getText().toString().replace(" ", "").equals("")
                || content.getText().toString().replace("\n", "").equals("")) {
            nullValue.add("내용을 입력해주세요");
            content.requestFocus();
            content.setText(null);//공백이 여러개 있을시, 처음으로

        }
        if (title.getText().toString().replace(" ", "").equals("")) {
            nullValue.add("제목을 입력해주세요");
            title.requestFocus();
            title.setText(null);
        }

        if (username.getText().toString().replace(" ", "").equals("")) {
            nullValue.add("성함을 입력해주세요");
            username.requestFocus();
            username.setText(null);
        }
        if (phone_number.getText().toString().replace(" ", "").equals("")) {
            nullValue.add("전화번호를 입력해주세요");
            phone_number.requestFocus();
            phone_number.setText(null);
        }
        if (email_first_address.getText().toString().replace(" ", "").equals("")) {

            nullValue.add("이메일을 입력해주세요");
            email_first_address.requestFocus();
            email_first_address.setText(null);
        }


        if (nullValue.size() <= 0) { //빈칸이 없음
            return false;
        } else {
            return true;
        }

    }

    public void onClick(View v) throws InterruptedException {
        switch (v.getId()) {
            case R.id.question_register_Btn:

                //에러 유무에 따라 activity 넘김
                if (!detectError()) {//빈칸이 없으면
                    sendQuestionData();
                } else { //빈칸이 있으면

                    if (getCurrentFocus() == content) {
                        scrollView.setScrollY(scrollView.getScrollY());

                    } else {
                        scrollView.setScrollY(scrollView.getScrollY() - 250);

                    }
                    Toast.makeText(this, nullValue.get(nullValue.size() - 1), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.question_register_cancelBtn:
                onBackPressed();
                finish();
                break;
        }
    }

    public void sendQuestionData() {
        QuestionData questionData = new QuestionData();
        questionData.setQuestion_email(email_first_address.getText().toString());
        questionData.setQuestion_phone(phone_number.getText().toString());
        questionData.setQuestion_username(username.getText().toString());
        questionData.setQuestion_title(title.getText().toString());
        questionData.setQuestion_content(content.getText().toString());


        NetworkService service = Application.getInstance().getNetworkService();
        Call<String> request = service.postQuestion(jsonToString(questionData));
        request.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()) {
                    Log.d("check", "post 성공 " + response.code());
                    responseStr = response.body();
                    Intent intent = new Intent(getApplicationContext(), QuestionWriteDoneActivity.class);
                    intent.putExtra("responseResult", responseStr);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    public String jsonToString(QuestionData questionData) {
        String jsonStr = "";
        try {
            JSONObject data = new JSONObject();
            data.put("question_title", questionData.getQuestion_title());
            data.put("question_content", questionData.getQuestion_content());
            data.put("question_email", questionData.getQuestion_email());
            data.put("question_phone", questionData.getQuestion_phone());
            data.put("question_username", questionData.getQuestion_username());

            JSONObject root = new JSONObject();
            root.put("question_info", data);
            jsonStr = root.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonStr;
    }

}

package docent.namsanhanok.Notice;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;

import docent.namsanhanok.Home.HomeActivity;
import docent.namsanhanok.R;

public class NoticeActivity extends AppCompatActivity {

    ImageButton homeBtn;
    EditText search_editText;
    ImageButton searchBtn;
    Button notice_postBtn;

    RecyclerView noticeRecyclerView;
    NoticeRecyclerAdapter noticeAdapter;
    private ArrayList<NoticeRecyclerItem> noticeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        init();

        String search_word = search_editText.getText().toString(); //검색어
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.homeBtn :
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                finish();
            case R.id.searchBtn : break;
            case R.id.notice_postBtn : break;
        }
    }

    public void init() {
        homeBtn = (ImageButton) findViewById(R.id.homeBtn);
        search_editText = (EditText) findViewById(R.id.search_editText);
        searchBtn = (ImageButton) findViewById(R.id.searchBtn);
        notice_postBtn = (Button) findViewById(R.id.notice_postBtn);
        noticeRecyclerView = (RecyclerView) findViewById(R.id.noticeRecyclerView);

        noticeRecyclerView.setHasFixedSize(true);
        noticeAdapter = new NoticeRecyclerAdapter(noticeList);
        noticeRecyclerView.setAdapter(noticeAdapter);
        noticeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        setData();
    }

    private void setData() {
        noticeList.add(new NoticeRecyclerItem("[안내] 7월 17일 전통체험프로그램 활만들기 미운영 안내", "2018.07.19", 11));
        noticeList.add(new NoticeRecyclerItem("[안내] 7월 18일 전통체험프로그램 매사냥체험 미운영", "2018.07.20", 1));
        noticeList.add(new NoticeRecyclerItem("[채용결과] 서울남산국악당 PD 채용 1차 합격자 발표", "2018.07.25", 25));
        noticeList.add(new NoticeRecyclerItem("[모집] 2018 서울남산국악당 7월 공개강좌 참가자 모집", "2018.08.01", 14));
        noticeList.add(new NoticeRecyclerItem("[알림] 젊은국악오디션 하반기 참가자 서류심사 결과 안내", "2018.08.12", 8));
    }

}

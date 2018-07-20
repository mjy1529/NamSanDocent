package docent.namsanhanok.Notice;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

import docent.namsanhanok.Home.HomeActivity;
import docent.namsanhanok.R;

public class NoticeActivity extends AppCompatActivity implements NoticeRecyclerAdapter.OnLoadMoreListener {

    ImageButton homeBtn;
    EditText search_editText;
    ImageButton searchBtn;
    ImageButton notice_postBtn;

    RecyclerView noticeRecyclerView;
    NoticeRecyclerAdapter noticeAdapter;
    private ArrayList<NoticeRecyclerItem> noticeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice2);

        init();

        String search_word = search_editText.getText().toString(); //검색어
    }

    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.homeBtn:
                intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                finish();
            case R.id.searchBtn: //검색 버튼
                Toast.makeText(this, "검색", android.widget.Toast.LENGTH_SHORT).show();
                break;
            case R.id.notice_postBtn: //글쓰기 버튼
                intent = new Intent(this, NoticePostActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void init() {
        homeBtn = (ImageButton) findViewById(R.id.homeBtn);
        search_editText = (EditText) findViewById(R.id.search_editText);
        searchBtn = (ImageButton) findViewById(R.id.searchBtn);
        notice_postBtn = (ImageButton) findViewById(R.id.notice_postBtn);
        noticeRecyclerView = (RecyclerView) findViewById(R.id.noticeRecyclerView);
        noticeAdapter = new NoticeRecyclerAdapter(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        noticeRecyclerView.setLayoutManager(layoutManager);
        noticeRecyclerView.setHasFixedSize(true);
        noticeAdapter.setLinearLayoutManager(layoutManager);
        noticeAdapter.setRecyclerView(noticeRecyclerView);
        noticeRecyclerView.setAdapter(noticeAdapter);
    }

    private void setData() {
        noticeList.clear();
        for (int i = 1; i <= 10; i++) { //로드하면 10개만 보여주기
            noticeList.add(new NoticeRecyclerItem(i + ". [알림] 젊은국악오디션 하반기 참가자 서류심사 결과 안내", "2018.08.12", i));
        }
        noticeAdapter.addAll(noticeList);
    }

    @Override
    public void loadMore() { //더보기
        noticeAdapter.setProgressMore(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                noticeList.clear();
                noticeAdapter.setProgressMore(false);

                int start = noticeAdapter.getItemCount();
                int end = start + 10; //10개씩

                for (int i = start + 1; i <= end; i++) {
                    noticeList.add(new NoticeRecyclerItem(i + ". [안내] 7월 17일 전통체험프로그램 활만들기 미운영 안내", "2018.07.19", 11));
                }
                noticeAdapter.addItem(noticeList);
                noticeAdapter.setMoreLoading(false);
            }
        }, 2500);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setData();
    }

}

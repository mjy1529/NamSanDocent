package docent.namsanhanok.Notice;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

import docent.namsanhanok.Home.HomeActivity;
import docent.namsanhanok.R;

public class NoticeActivity extends AppCompatActivity implements NoticeRecyclerAdapter.OnLoadMoreListener {

    ImageButton homeBtn;
    EditText search_editText;
    ImageButton searchBtn;
    ImageButton notice_postBtn;
    //ImageButton topBtn;
    FloatingActionButton topBtn;

    RecyclerView noticeRecyclerView;
    NoticeRecyclerAdapter noticeAdapter;
    private ArrayList<NoticeRecyclerItem> noticeList = new ArrayList<>();

    int loadCount = 15;

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
                break;
            case R.id.searchBtn: //검색 버튼
                Toast.makeText(this, "검색", android.widget.Toast.LENGTH_SHORT).show();
                break;
            case R.id.notice_postBtn: //글쓰기 버튼
                intent = new Intent(this, NoticePostActivity.class);
                startActivity(intent);
                break;
            case R.id.topBtn :
                noticeRecyclerView.smoothScrollToPosition(0);
                break;
        }
    }

    public void init() {
        homeBtn = (ImageButton) findViewById(R.id.homeBtn);
        search_editText = (EditText) findViewById(R.id.search_editText);
        searchBtn = (ImageButton) findViewById(R.id.searchBtn);
        notice_postBtn = (ImageButton) findViewById(R.id.notice_postBtn);

        //topBtn = (ImageButton) findViewById(R.id.topBtn);
        topBtn = (FloatingActionButton) findViewById(R.id.topBtn);

        noticeRecyclerView = (RecyclerView) findViewById(R.id.noticeRecyclerView);
        noticeAdapter = new NoticeRecyclerAdapter(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        noticeRecyclerView.setLayoutManager(layoutManager);
        noticeRecyclerView.setHasFixedSize(true);
        noticeAdapter.setLinearLayoutManager(layoutManager);
        noticeAdapter.setRecyclerView(noticeRecyclerView);
        noticeRecyclerView.setAdapter(noticeAdapter);

        topBtn.attachToRecyclerView(noticeRecyclerView);
    }

    private void setData() {
        noticeList.clear();
        for (int i = 1; i <= loadCount; i++) {
//            noticeList.add(new NoticeRecyclerItem(i + ". [알림] 젊은국악오디션 하반기 참가자 서류심사 결과 안내", "2018.08.12", i));
            noticeList.add(new NoticeRecyclerItem(i + ". [알림] 젊은국악오디션 하반기 참가자 서류심사 결과 안내", "2018.08.12", 20, "[알림] 젊은국악오디션 하반기 참가자 서류심사 결과 안내"));
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
                int end = start + loadCount;

                for (int i = start + 1; i <= end; i++) {
                    noticeList.add(new NoticeRecyclerItem(i + ". [안내] 7월 17일 전통체험프로그램 활만들기 미운영 안내", "2018.07.19", 11, getResources().getString(R.string.notice_content)));
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

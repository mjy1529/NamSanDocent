package docent.namsanhanok.Notice;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import docent.namsanhanok.Application;
import docent.namsanhanok.Home.HomeActivity;
import docent.namsanhanok.NetworkService;
import docent.namsanhanok.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoticeActivity extends AppCompatActivity implements NoticeRecyclerAdapter.OnLoadMoreListener {

    ImageButton homeBtn;
    EditText search_editText;
    ImageButton searchBtn;
    FloatingActionButton topBtn;

    RecyclerView noticeRecyclerView;
    NoticeRecyclerAdapter noticeAdapter;

    ArrayList<NoticeData> allNoticeList;
    ArrayList<NoticeData> noticeList = new ArrayList<>();

    int loadCount = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice2);

        networking();
        init();

        String search_word = search_editText.getText().toString(); //검색어
    }

    public void networking() {
        NetworkService service = Application.getInstance().getNetworkService();
        Call<NoticeResult> request = service.getNoticeResult(jsonToString());
        request.enqueue(new Callback<NoticeResult>() {
            @Override
            public void onResponse(Call<NoticeResult> call, Response<NoticeResult> response) {
                Log.d("check", "성공 : " + response.code());
                if (response.isSuccessful()) {
                    NoticeResult noticeResult = response.body();
                    allNoticeList = noticeResult.notice_info;

                    setLoadData();
                }
            }

            @Override
            public void onFailure(Call<NoticeResult> call, Throwable t) {
                Log.d("check", "실패");
            }
        });
    }

    public void setLoadData() {
        if (allNoticeList.size() < loadCount) { //리스트의 수가 loadCount보다 작을 때
            noticeList.addAll(allNoticeList);

        } else { //loadCount 이상일 때
            for (int i = 0; i < loadCount; i++) {
                noticeList.add(allNoticeList.get(i));
            }
        }
        noticeAdapter.addAll(noticeList);
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
            case R.id.topBtn:
                noticeRecyclerView.smoothScrollToPosition(0);
                break;
        }
    }

    public void init() {
        Intent intent = getIntent();
        String notice_toolbar_title= intent.getStringExtra("notice_title");
        TextView noticeTitle = (TextView) findViewById(R.id.noticeTitle);
        noticeTitle.setText(notice_toolbar_title);

        homeBtn = (ImageButton) findViewById(R.id.homeBtn);
        search_editText = (EditText) findViewById(R.id.search_editText);
        searchBtn = (ImageButton) findViewById(R.id.searchBtn);
        topBtn = (FloatingActionButton) findViewById(R.id.topBtn);
        noticeRecyclerView = (RecyclerView) findViewById(R.id.noticeRecyclerView);
        noticeAdapter = new NoticeRecyclerAdapter(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        noticeRecyclerView.setLayoutManager(layoutManager);
        noticeRecyclerView.setHasFixedSize(true);
        noticeAdapter.setLinearLayoutManager(layoutManager);
        noticeAdapter.setRecyclerView(noticeRecyclerView);
        noticeAdapter.setNotice_toolbar_title(notice_toolbar_title);
        noticeRecyclerView.setAdapter(noticeAdapter);
        noticeRecyclerView.setNestedScrollingEnabled(true);

        topBtn.attachToRecyclerView(noticeRecyclerView);
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

                if (end >= allNoticeList.size()) { //loadCount보다 리스트 수가 적을 때
                    for (int i = start; i < allNoticeList.size(); i++) {
                        noticeList.add(allNoticeList.get(i));
                    }
                } else { //loadCount 이상일 때
                    for (int i = start; i < end; i++) {
                        noticeList.add(allNoticeList.get(i));
                    }
                }
                noticeAdapter.addItem(noticeList);
                noticeAdapter.setMoreLoading(false);

            }
        }, 2000);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    public String jsonToString() {
        String jsonStr = "";
        try {
            JSONObject data = new JSONObject();
            data.put("cmd", "notice_list");

            JSONObject root = new JSONObject();
            root.put("info", data);
            jsonStr = root.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonStr;
    }

}

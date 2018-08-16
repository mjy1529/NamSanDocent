package docent.namsanhanok.Notice;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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
    FloatingActionButton topBtn;
    TextView noResultTextView;
    SearchView searchView;

    RecyclerView noticeRecyclerView;
    NoticeRecyclerAdapter noticeAdapter;

    ArrayList<NoticeData> allNoticeList;
    ArrayList<NoticeData> noticeList = new ArrayList<>();

    int loadCount = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        networking();
        init();
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

                    loadData();
                }
            }

            @Override
            public void onFailure(Call<NoticeResult> call, Throwable t) {
                Log.d("check", "실패");
            }
        });
    }

    public void loadData() {
        if(noticeList.size() != 0) noticeList.clear();

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
            case R.id.topBtn:
                noticeRecyclerView.smoothScrollToPosition(0);
                break;
        }
    }

    public void init() {
        Toolbar noticeToolbar = (Toolbar) findViewById(R.id.noticeToolbar);
        setSupportActionBar(noticeToolbar);

        Intent intent = getIntent();
        String notice_toolbar_title = intent.getStringExtra("notice_title");
        TextView noticeTitle = (TextView) findViewById(R.id.noticeTitle);
        noticeTitle.setText(notice_toolbar_title);

        homeBtn = (ImageButton) findViewById(R.id.homeBtn);
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

        noResultTextView = (TextView) findViewById(R.id.noResultTextView);
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
    public boolean onCreateOptionsMenu(Menu menu) { //검색
        getMenuInflater().inflate(R.menu.search_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);

        MenuItem.OnActionExpandListener expandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                noResultTextView.setVisibility(View.GONE);
                noticeRecyclerView.setVisibility(View.VISIBLE);
                topBtn.setVisibility(View.VISIBLE);
                loadData();
                return true;
            }
        };
        searchItem.setOnActionExpandListener(expandListener);

        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint(getResources().getString(R.string.searchMessage));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { //검색어 입력 완료 후
                noticeList.clear();
                for (int i = 0; i < allNoticeList.size(); i++) {
                    if (allNoticeList.get(i).getNotice_title().contains(query) || allNoticeList.get(i).getNotice_content().contains(query)) {
                        noticeList.add(allNoticeList.get(i));
                    }
                }
                if (noticeList.size() == 0) {
                    noticeRecyclerView.setVisibility(View.GONE);
                    noResultTextView.setVisibility(View.VISIBLE);
                    noResultTextView.setText(R.string.noResult);
                    topBtn.setVisibility(View.GONE);
                } else {
                    noticeRecyclerView.setVisibility(View.VISIBLE);
                    noResultTextView.setVisibility(View.GONE);
                    topBtn.setVisibility(View.VISIBLE);
                    noticeAdapter.addAll(noticeList);
                    noticeAdapter.notifyDataSetChanged();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) { //검색어가 바뀔 때마다

                return false;
            }
        });

        ImageView searchClose = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        searchClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.setQuery("", false);
                searchView.onActionViewCollapsed();
                searchItem.collapseActionView();

                noResultTextView.setVisibility(View.GONE);
                noticeRecyclerView.setVisibility(View.VISIBLE);
                topBtn.setVisibility(View.VISIBLE);
                loadData();
            }
        });

        return true;
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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}

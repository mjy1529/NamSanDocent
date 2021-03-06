package docent.namsanhanok.Event;

import android.content.Context;
import android.content.Intent;
import android.net.NetworkRequest;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import docent.namsanhanok.Application;
import docent.namsanhanok.Home.HomeActivity;
import docent.namsanhanok.NetworkService;
import docent.namsanhanok.Notice.NoticeRecyclerAdapter;
import docent.namsanhanok.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private EventAdapter eventAdapter;
    ArrayList<EventData> eventList = new ArrayList<>();
    ArrayList<EventData> eventSortList = new ArrayList<>();
    TextView event_toolbar_title;
    ImageButton homeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        init();
        networking();
    }

    public void networking() {
        NetworkService service = Application.getInstance().getNetworkService();
        final Call<EventResult> request = service.getEventResult(jsonToString());
        request.enqueue(new Callback<EventResult>() {
            @Override
            public void onResponse(Call<EventResult> call, Response<EventResult> response) {
                if(response.isSuccessful()) {
                    EventResult eventResult = response.body();
                    eventList = eventResult.event_info;

                    for(int i = 0; i<eventList.size(); i++) {
                        if(eventList.get(i).getEvent_complete().equals("진행중")){
                            eventSortList.add(eventList.get(i));
                        }
                    }

                    for(int i = 0; i<eventList.size(); i++){
                        if(eventList.get(i).getEvent_complete().equals("마감")){
                            eventSortList.add(eventList.get(i));
                        }
                    }
                    setRecyclerView();
                }
            }

            @Override
            public void onFailure(Call<EventResult> call, Throwable t) {
                Log.d("check", "실패");
            }
        });
    }

    public void setRecyclerView() {
        eventAdapter = new EventAdapter(this, eventSortList);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(eventAdapter);
    }

    public void init() {
        Toolbar categoryToolbar = (Toolbar)findViewById(R.id.event_Toolbar);
        setSupportActionBar(categoryToolbar);

        event_toolbar_title = (TextView) findViewById(R.id.docentTitle);
        Intent intent = getIntent();
        event_toolbar_title.setText(intent.getStringExtra("event_title"));

        homeBtn = (ImageButton) findViewById(R.id.homeBtn);
        recyclerView = (RecyclerView) findViewById(R.id.event_recyclerView);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case  R.id.homeBtn :
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    public String jsonToString() {
        String jsonStr = "";
        try {
            JSONObject data = new JSONObject();
            data.put("cmd", "event_list");

            JSONObject root = new JSONObject();
            root.put("info", data);
            jsonStr = root.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonStr;
    }
}

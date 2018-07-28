package docent.namsanhanok.Event;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import docent.namsanhanok.Home.HomeActivity;
import docent.namsanhanok.R;

public class EventActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private EventAdapter eventAdapter;
    private ArrayList<EventActivityItem> eventActivityItem;
    TextView event_toolbar_title;

    ImageButton homeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        init();
    }

    public void init() {
        initDataset();

        Toolbar categoryToolbar = (Toolbar)findViewById(R.id.event_Toolbar);
        categoryToolbar.bringToFront();

        event_toolbar_title = (TextView) findViewById(R.id.docentTitle);
        event_toolbar_title.setText("세시/행사");

        homeBtn = (ImageButton) findViewById(R.id.homeBtn);
        recyclerView = (RecyclerView) findViewById(R.id.event_recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        eventAdapter = new EventAdapter(this, eventActivityItem);
        recyclerView.setAdapter(eventAdapter);

    }


    private void initDataset() {
        eventActivityItem = new ArrayList<>();
        eventActivityItem.add(new EventActivityItem("삼각동 도편수(都片手) 이승업(李承業) 가옥", R.drawable.homeimage,
                "2018-07-17", "2018-08-31", "남산한옥마을 정문", "진행중"));
        eventActivityItem.add(new EventActivityItem("삼각동 도편수(都片手) 이승업(李承業) 가옥", R.drawable.homeimage,
                "2018-07-17", "2018-08-31", "남산한옥마을 정문", "진행중"));
        eventActivityItem.add(new EventActivityItem("삼각동 도편수(都片手) 이승업(李承業) 가옥", R.drawable.homeimage,
                "2018-07-17", "2018-08-31", "남산한옥마을 정문", "진행중"));
        eventActivityItem.add(new EventActivityItem("삼각동 도편수(都片手) 이승업(李承業) 가옥", R.drawable.homeimage,
                "2018-06-17", "2018-06-31", "남산한옥마을 정문", "마감"));


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
}

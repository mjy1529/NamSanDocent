package docent.namsanhanok.Notice;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;

import docent.namsanhanok.R;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class NoticeReadActivity extends AppCompatActivity {

    ImageButton moreBtn;

    TextView notice_title_tv;
    TextView notice_date_tv;
    TextView notice_content_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_read);

        init();
    }

    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.listBtn:
                intent = new Intent(this, NoticeActivity.class);
                startActivity(intent);
                finish();
                break;

        }
    }

    public void init() {
        Toolbar noticeReadToolbar = (Toolbar) findViewById(R.id.noticeReadToolbar);
        setSupportActionBar(noticeReadToolbar);

        notice_title_tv = (TextView) findViewById(R.id.notice_title_tv);
        notice_date_tv = (TextView) findViewById(R.id.notice_date_tv);
        notice_content_tv = (TextView) findViewById(R.id.notice_content_tv);

        Intent intent = getIntent();
        NoticeRecyclerItem object = (NoticeRecyclerItem) intent.getSerializableExtra("object");

        notice_title_tv.setText(object.getTitle());
        notice_date_tv.setText(object.getDate());
        notice_content_tv.setText(object.getContent());
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}

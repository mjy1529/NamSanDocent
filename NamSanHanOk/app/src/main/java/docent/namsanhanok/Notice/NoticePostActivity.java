package docent.namsanhanok.Notice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import docent.namsanhanok.R;

public class NoticePostActivity extends AppCompatActivity {

    TextView title;
    TextView content;
    ImageButton canelBtn;
    Button registPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_post);

        init();

    }

    public void init() {
        Toolbar questionRegisterToolbar = (Toolbar)findViewById(R.id.notice_register_toolbar);
        questionRegisterToolbar.bringToFront();

        title = (TextView) findViewById(R.id.notice_post_title);
        content = (TextView) findViewById(R.id.notice_post_content);
        canelBtn = (ImageButton) findViewById(R.id.notice_register_cancelBtn);
        registPost = (Button) findViewById(R.id.notice_register_Btn);

    }
}

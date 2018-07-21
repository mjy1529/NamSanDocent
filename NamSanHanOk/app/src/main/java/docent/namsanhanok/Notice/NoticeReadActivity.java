package docent.namsanhanok.Notice;

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

import docent.namsanhanok.R;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class NoticeReadActivity extends AppCompatActivity {

    ImageButton moreBtn;

    TextView notice_title_tv;
    TextView notice_date_tv;
    TextView notice_readCnt_tv;
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
            case R.id.moreBtn:
                showPopupMenu(view);
                break;
        }
    }

    public void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
        getMenuInflater().inflate(R.menu.notice_admin_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.modify:
                        Toast.makeText(NoticeReadActivity.this, "modify", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.remove:
                        showAlertDialog();
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    public void showAlertDialog() {
        final PrettyDialog alertDialog = new PrettyDialog(this);
        alertDialog
                .setMessage("해당 게시글을 삭제하시겠습니까?")
                .setIcon(R.drawable.pdlg_icon_info)
                .setIconTint(R.color.dialog_cancel)
                .addButton("삭제", // button text
                        R.color.pdlg_color_white,  // button text color
                        R.color.dialog_remove,  // button background color
                        new PrettyDialogCallback() {  // button OnClick listener
                            @Override
                            public void onClick() {
                                Toast.makeText(NoticeReadActivity.this, "삭제!", Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                            }
                        }
                )
                .addButton("취소",
                        R.color.pdlg_color_white,
                        R.color.dialog_cancel,
                        new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                Toast.makeText(NoticeReadActivity.this, "취소", Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                            }
                        }
                );
        alertDialog.show();
    }

    public void init() {
        Toolbar noticeReadToolbar = (Toolbar) findViewById(R.id.noticeReadToolbar);
        setSupportActionBar(noticeReadToolbar);

        moreBtn = (ImageButton)findViewById(R.id.moreBtn);
        notice_title_tv = (TextView) findViewById(R.id.notice_title_tv);
        notice_date_tv = (TextView) findViewById(R.id.notice_date_tv);
        notice_readCnt_tv = (TextView) findViewById(R.id.notice_readCnt_tv);
        notice_content_tv = (TextView) findViewById(R.id.notice_content_tv);

        Intent intent = getIntent();
        NoticeRecyclerItem object = (NoticeRecyclerItem) intent.getSerializableExtra("object");

        notice_title_tv.setText(object.getTitle());
        notice_date_tv.setText(object.getDate());
        notice_content_tv.setText(object.getContent());
        notice_readCnt_tv.setText(String.valueOf(object.getRead_cnt()));
    }
}

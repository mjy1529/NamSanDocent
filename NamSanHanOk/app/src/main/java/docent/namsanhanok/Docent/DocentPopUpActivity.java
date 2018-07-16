package docent.namsanhanok.Docent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import docent.namsanhanok.R;

public class DocentPopUpActivity extends Activity {

    ImageButton closeBtn;
    ImageView imageView;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_docentpupup);

        closeBtn = (ImageButton) findViewById(R.id.closed);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Intent secondIntent = getIntent();
        String imgTitle = secondIntent.getExtras().getString("title");

        imageView = (ImageView) findViewById(R.id.image);
        textView = (TextView) findViewById(R.id.image_info);

        if(imgTitle.equals("베")){
            imageView.setImageResource(R.drawable.bae);
            textView.setText("삼베 · 대마포라고도 한다. 삼은 삼과의 한해살이풀로서 온대와 열대지방에서 자란다." +"\n"
                    + "주로 섬유를 목적으로 재배하는데, 구석기시대부터 세계 각지에서 애용하였으며 한국에서는 조선 때부터 의복이나 침구 재료로 사용해왔다." + "\n"
                    + "또한 견고성과 내구성이 뛰어나 직물용 이외에 로프 · 그물 · 타이어 등을만드는 데도 사용한다." +"\n"
                    + "주로 섬유를 목적으로 재배하는데, 구석기시대부터 세계 각지에서 애용하였으며 한국에서는 조선 때부터 의복이나 침구 재료로 사용해왔다." + "\n"
                    + "또한 견고성과 내구성이 뛰어나 직물용 이외에 로프 · 그물 · 타이어 등을만드는 데도 사용한다." +"\n"
                    + "주로 섬유를 목적으로 재배하는데, 구석기시대부터 세계 각지에서 애용하였으며 한국에서는 조선 때부터 의복이나 침구 재료로 사용해왔다." + "\n"
                    + "또한 견고성과 내구성이 뛰어나 직물용 이외에 로프 · 그물 · 타이어 등을만드는 데도 사용한다." +"\n"
                    + "주로 섬유를 목적으로 재배하는데, 구석기시대부터 세계 각지에서 애용하였으며 한국에서는 조선 때부터 의복이나 침구 재료로 사용해왔다." + "\n"
                    + "또한 견고성과 내구성이 뛰어나 직물용 이외에 로프 · 그물 · 타이어 등을만드는 데도 사용한다." +"\n");
        }

        else if(imgTitle.equals("짚신")){
            imageView.setImageResource(R.drawable.jipshin);
            textView.setText("볏짚으로 삼은 신이다. " + "\n"
                    + "가는 새끼를 꼬아 날을 삼고 총과 돌기총으로 울을 삼아서 만든다.\n");
        }


    }




}

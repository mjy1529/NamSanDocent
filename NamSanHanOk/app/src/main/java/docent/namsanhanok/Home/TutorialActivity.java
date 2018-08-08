package docent.namsanhanok.Home;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.angads25.toggle.LabeledSwitch;

import docent.namsanhanok.R;

import static android.view.WindowManager.*;


public class TutorialActivity extends AppCompatActivity {

    LinearLayout tuto_BGD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);




        init();

        tuto_BGD.getBackground().setAlpha(100);


        ;
    }


    public void init(){
        tuto_BGD = (LinearLayout) findViewById(R.id.tutorial_background);
    }
}

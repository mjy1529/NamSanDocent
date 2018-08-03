package docent.namsanhanok;

import android.graphics.Typeface;

import com.github.angads25.toggle.LabeledSwitch;
import com.tsengvn.typekit.Typekit;

import java.lang.reflect.Type;

public class Application extends android.app.Application {
    public boolean isScanning;
    public boolean isToggleOn;
    LabeledSwitch toggleBtn;

    /**
     * onCreate()
     * 액티비티, 리시버, 서비스가 생성되기전 어플리케이션이 시작 중일때
     * Application onCreate() 메서드가 만들어 진다고 나와 있습니다.
     * by. Developer 사이트
     */
    @Override
    public void onCreate() {
        super.onCreate();
        Application scanState = ((Application) getApplicationContext());
        isScanning = scanState.getScanning();
        isToggleOn = scanState.getToggleState();

        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "NanumMyeongjo.otf"))
                .addBold(Typekit.createFromAsset(this, "NanumMyeongjoBold.otf"))
                .addItalic(Typekit.createFromAsset(this, "NanumMyeongjoExtraBold.otf"));
    }

    public void setScanning(boolean isScanning) {
        this.isScanning = isScanning;
    }

    public boolean getScanning() {
        return isScanning;
    }

    public void setToggleState(boolean isToggleOn) {
        this.isToggleOn = isToggleOn;
    }

    public boolean getToggleState() {
        return isToggleOn;
    }

}

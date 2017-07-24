package logcat;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import nts.nt3.atm.R;


public class LogcatSetting_Dialog extends Activity {

    private String flag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//위의 타이틀바 제거인데 setContentView 전에 넣어줘야함 뷰가 생성되기전에 제거되어야하므로...
        setContentView(R.layout.logcatsetting_dialog);

    }

    public void buttonPressed(View v) {
        switch ((v.getId())){
            case R.id.search_btn://검색
                flag = "search";
                finish();
                break;
            case R.id.init_btn://초기화
                flag = "init";
                finish();
                break;
            case R.id.save_btn://저장
                flag = "save";
                finish();
                break;
            case R.id.setting_btn://설정
                flag = "setting";
                finish();
                break;
            case R.id.overlay_btn://오버레이
                flag = "overlay";
                finish();
                break;

        }

    }
}
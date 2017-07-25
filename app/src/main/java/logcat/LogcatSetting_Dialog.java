package logcat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import nts.nt3.atm.R;


public class LogcatSetting_Dialog extends Activity {

    private static final int REQUEST_DIALOG = 100;
    private static final int RESULT_SEARCH = 10;
    private static final int RESULT_INIT = 20;
    private static final int RESULT_SAVE = 30;
    private static final int RESULT_SETTING = 40;
    private static final int RESULT_OVERLAY = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//위의 타이틀바 제거인데 setContentView 전에 넣어줘야함 뷰가 생성되기전에 제거되어야하므로...
        setContentView(R.layout.logcatsetting_dialog);

    }

    public void buttonPressed(View v) {
        switch ((v.getId())){
            case R.id.search_btn://검색
                Intent intent_search = new Intent();
                intent_search.putExtra("result", RESULT_SEARCH);
                setResult(REQUEST_DIALOG, intent_search);
                finish();
                break;
            case R.id.init_btn://초기화
                Intent intent_init = new Intent();
                intent_init.putExtra("result", RESULT_INIT);
                setResult(REQUEST_DIALOG, intent_init);
                finish();
                break;
            case R.id.save_btn://저장
                Intent intent_save = new Intent();
                intent_save.putExtra("result", RESULT_SAVE);
                setResult(REQUEST_DIALOG, intent_save);
                finish();
                break;
            case R.id.setting_btn://설정
                Intent intent_setting = new Intent();
                intent_setting.putExtra("result", RESULT_SETTING);
                setResult(REQUEST_DIALOG, intent_setting);
                finish();
                break;
            case R.id.overlay_btn://오버레이
                Intent intent_overlay = new Intent();
                intent_overlay.putExtra("result", RESULT_OVERLAY);
                setResult(REQUEST_DIALOG, intent_overlay);
                finish();
                break;

        }

    }
}
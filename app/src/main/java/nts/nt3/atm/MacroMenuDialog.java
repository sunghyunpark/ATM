package nts.nt3.atm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class MacroMenuDialog extends Activity {
    private static final int REQUEST_DIALOG = 100;
    private static final int LONG_TAB_MODE = 2;    //롱탭
    private static final int DRAG_MODE = 3;    //드래그
    private static final int INIT_MODE = 4;    //초기화
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_macro_menu_dialog);
    }

    public void buttonPressed(View v) {
        switch ((v.getId())){
            case R.id.btn1://초기화
                Intent intent_init = new Intent();
                intent_init.putExtra("result", INIT_MODE);
                setResult(REQUEST_DIALOG, intent_init);
                break;
            case R.id.btn2://롱탭
                Intent intent_longtab = new Intent();
                intent_longtab.putExtra("result", LONG_TAB_MODE);
                setResult(REQUEST_DIALOG, intent_longtab);
                break;
            case R.id.btn3://드래그
                Intent intent_drag = new Intent();
                intent_drag.putExtra("result", DRAG_MODE);
                setResult(REQUEST_DIALOG, intent_drag);
                break;
        }
        finish();
    }
}

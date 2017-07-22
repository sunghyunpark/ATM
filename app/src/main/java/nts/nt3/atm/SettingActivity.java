package nts.nt3.atm;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Realm;
import presenter.SettingPresenter;
import realm.RealmConfig;
import realm.model.User;
import service.NdeployAlarmService;

public class SettingActivity extends AppCompatActivity {

    Realm mRealm;
    RealmConfig realmConfig;
    TextView user_email_txt;
    SettingPresenter settingPresenter;

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mRealm.close();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        InitView();
    }

    private void InitView(){

        realmConfig = new RealmConfig();
        settingPresenter = new SettingPresenter(getApplicationContext());

        mRealm = Realm.getInstance(realmConfig.User_DefaultRealmVersion(getApplicationContext()));
        User user_db = mRealm.where(User.class).equalTo("no",1).findFirst();

        ImageView back_btn = (ImageView)findViewById(R.id.back_btn);
        back_btn.setOnTouchListener(myOnTouchListener);

        ViewGroup user_email_layout = (ViewGroup)findViewById(R.id.user_email_layout);
        user_email_layout.setOnTouchListener(myOnTouchListener);

        user_email_txt = (TextView)findViewById(R.id.user_email_txt);
        user_email_txt.setText(user_db.getEmail());

        //편집없이 캡쳐 전송 스위치
        final Switch CaptureWithoutEditSwitch = (Switch)findViewById(R.id.send_capture_without_edit_switch);
        if(user_db.isSend_capture_without_edit()){
            CaptureWithoutEditSwitch.setChecked(true);
        }else {
            CaptureWithoutEditSwitch.setChecked(false);
        }
        CaptureWithoutEditSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    settingPresenter.ChangeCaptureSetting(mRealm, true);
                }else{
                    settingPresenter.ChangeCaptureSetting(mRealm, false);
                }
            }
        });

        final Switch NdeploySwitch = (Switch)findViewById(R.id.ndeploy_switch);
        if(Ndeploy_AlarmRunningCheck()){
            NdeploySwitch.setChecked(true);
        }else{
            NdeploySwitch.setChecked(false);
        }

        NdeploySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Resources res = getResources();
                    AlertDialog.Builder alert = new AlertDialog.Builder(SettingActivity.this);
                    alert.setTitle(res.getString(R.string.ndeploy_alarm_dialog_title));
                    alert.setMessage(res.getString(R.string.ndeploy_alarm_dialog_txt));//(ex. https://ndeploy.navercorp.com/app/230/project/1994/os/Android/stage/test-armv7a)
                    //다이얼로그 밖의 영역 탭하여 취소했을 때 스위치 off
                    alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        public void onCancel(DialogInterface dialog) {
                            NdeploySwitch.setChecked(false);
                        }
                    });
                    // Set an EditText view to get user input
                    final EditText input_url = new EditText(SettingActivity.this);
                    input_url.setText("https://ndeploy.navercorp.com/");
                    input_url.setSelection(input_url.length());
                    input_url.requestFocus();
                    alert.setView(input_url);

                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            settingPresenter.AlarmNdeploy(input_url.getText().toString());
                            Log.d("ndeploy", "start");

                        }
                    });

                    alert.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // Canceled.

                                }
                            });
                    alert.show();
                }else{
                    stopService(new Intent(getApplicationContext(), NdeployAlarmService.class));
                    Log.d("ndeploy", "stop");

                }
            }
        });
    }

    private boolean Ndeploy_AlarmRunningCheck() {
        ActivityManager manager = (ActivityManager) this.getSystemService(Activity.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("service.NdeployAlarmService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void InsertEmail_Dialog(){
        final Resources res = getResources();
        AlertDialog.Builder alert = new AlertDialog.Builder(SettingActivity.this);

        alert.setTitle(res.getString(R.string.insert_email_dialog_title));
        alert.setMessage(res.getString(R.string.insert_email_dialog_subtext));

        // Set an EditText view to get user input
        final EditText input_email = new EditText(SettingActivity.this);
        alert.setView(input_email);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String input_str = input_email.getText().toString();
                if(input_str.equals("")){
                    Toast.makeText(getApplicationContext(),String.format(res.getString(R.string.not_exist_info_txt)), Toast.LENGTH_SHORT).show();
                }else if(!input_str.contains("@") || !input_str.contains(".com")){
                    Toast.makeText(getApplicationContext(), String.format(res.getString(R.string.not_email_format_txt)),Toast.LENGTH_SHORT).show();
                }else{
                    user_email_txt.setText(input_str);
                    SettingPresenter settingPresenter = new SettingPresenter(getApplicationContext());
                    settingPresenter.InsertEmail(mRealm, input_email.getText().toString());
                }
            }
        });

        alert.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.

                    }
                });

        alert.show();
    }

    /**
     * 각 버튼들 이벤트
     */
    private View.OnTouchListener myOnTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.setAlpha(0.55f);
            }else if(event.getAction() == MotionEvent.ACTION_CANCEL){
                v.setAlpha(1.0f);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                v.setAlpha(1.0f);
                switch(v.getId()){

                    case R.id.back_btn:
                        finish();
                        break;

                    case R.id.user_email_layout:
                        InsertEmail_Dialog();
                        break;

                }
            }
            return true;
        }
    };
}

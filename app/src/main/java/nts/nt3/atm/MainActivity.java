package nts.nt3.atm;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import io.realm.Realm;
import logcat.LogcatMain;
import realm.RealmConfig;
import realm.model.User;
import service.WheelViewService;

/**
 * https://github.com/sunghyunpark/ATM
 * 1. git init
 * 2. git status
 * 3. git commit -am "commit"
 * 4. git push origin master / git push origin nts
 * 5. git merge nts
 * 6. git push
 */
public class MainActivity extends AppCompatActivity {

    Realm mRealm;
    RealmConfig realmConfig;

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(mRealm != null)
        mRealm.close();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitView();
    }

    /**
     * 뷰 초기화
     */
    private void InitView(){

        InitUserData();

        //lollipop이상인 경우에만 적용
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorBasic));
        }
        //앱 버전
        String version;
        TextView app_version_txt = (TextView)findViewById(R.id.app_version_txt);
        try {
            PackageInfo i = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            version = i.versionName;
            app_version_txt.setText("v"+version);
        } catch(PackageManager.NameNotFoundException e) { }

        Button start_btn = (Button)findViewById(R.id.start_btn);
        start_btn.setOnTouchListener(myOnTouchListener);
        if(isServiceRunningCheck()){
            //service on
            start_btn.setText("STOP");
        }else{
            //service off
            start_btn.setText("START");
        }
        Button performance_btn = (Button)findViewById(R.id.performance_btn);
        performance_btn.setOnTouchListener(myOnTouchListener);
        Button logcat_btn = (Button)findViewById(R.id.logcat_btn);
        logcat_btn.setOnTouchListener(myOnTouchListener);
        Button app_setting_btn = (Button)findViewById(R.id.app_setting_btn);
        app_setting_btn.setOnTouchListener(myOnTouchListener);
        Button setting_btn = (Button)findViewById(R.id.setting_btn);
        setting_btn.setOnTouchListener(myOnTouchListener);
        Button help_btn = (Button)findViewById(R.id.help_btn);
        help_btn.setOnTouchListener(myOnTouchListener);

    }

    private void InitUserData(){
        realmConfig = new RealmConfig();
        mRealm = Realm.getInstance(realmConfig.User_DefaultRealmVersion(getApplicationContext()));

        User user_db = mRealm.where(User.class).equalTo("no",1).findFirst();
        if(user_db == null){
            mRealm.beginTransaction();
            User userData = new User();
            userData.setNo(1);
            userData.setSend_capture_without_edit(false);

            mRealm.copyToRealmOrUpdate(userData);
            mRealm.commitTransaction();
        }
    }

    private boolean isServiceRunningCheck() {
        ActivityManager manager = (ActivityManager) this.getSystemService(Activity.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("service.WheelViewService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
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

                    case R.id.start_btn:
                        if(isServiceRunningCheck()){
                            //service on
                            finish();
                            stopService(new Intent(getApplicationContext(), WheelViewService.class));
                        }else{
                            //service off
                            finish();
                            startService(new Intent(getApplicationContext(), WheelViewService.class));
                        }
                        break;

                    case R.id.performance_btn:
                        Intent intent_performance = new Intent(getApplicationContext(), PerformanceActivity.class);
                        startActivity(intent_performance);
                        break;

                    case R.id.logcat_btn:
                        Intent intent_logcat = new Intent(getApplicationContext(), LogcatMain.class);
                        startActivity(intent_logcat);
                        break;

                    case R.id.app_setting_btn:
                        Intent intent_apk_manager = new Intent(getApplicationContext(), ApkMangerActivity.class);
                        startActivity(intent_apk_manager);
                        break;

                    case R.id.setting_btn:
                        Intent intent_setting = new Intent(getApplicationContext(), SettingActivity.class);
                        startActivity(intent_setting);
                        break;

                    case R.id.help_btn:

                        break;
                }
            }
            return true;
        }
    };
}

package presenter;

import android.content.Context;
import android.content.Intent;

import io.realm.Realm;
import logcat.CrashAlarmService;
import realm.model.User;
import service.NdeployAlarmService;

/**
 * Created by Sunghyun on 2017. 7. 13..
 */

public class SettingPresenter implements SettingInterface {
    Context context;

    public SettingPresenter(Context context){
        this.context = context;
    }

    @Override
    public void InsertEmail(Realm mRealm, String email){
        User user_db = mRealm.where(User.class).equalTo("no",1).findFirst();
        mRealm.beginTransaction();
        user_db.setEmail(email);
        mRealm.commitTransaction();

    }

    @Override
    public void AlarmNdeploy(String url){
        Intent intent = new Intent(context, NdeployAlarmService.class);
        intent.putExtra("url", url);
        context.startService(intent);
    }

    @Override
    public void AlarmCrash(){
        Intent intent = new Intent(context, CrashAlarmService.class);
        context.startService(intent);
    }

    @Override
    public void ChangeCaptureSetting(Realm mRealm, boolean flag){
        User user_db = mRealm.where(User.class).equalTo("no",1).findFirst();
        mRealm.beginTransaction();
        user_db.setSend_capture_without_edit(flag);
        mRealm.commitTransaction();
    }
}

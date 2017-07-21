package presenter;

import android.content.Context;
import android.content.Intent;

import io.realm.Realm;
import realm.RealmConfig;
import realm.model.User;
import service.NdeployAlarmService;

/**
 * Created by Sunghyun on 2017. 7. 13..
 */

public class SettingPresenter implements SettingInterface {
    Context context;
    RealmConfig realmConfig;
    Realm mRealm;

    public SettingPresenter(Context context){
        this.context = context;
        realmConfig = new RealmConfig();
        mRealm = Realm.getInstance(realmConfig.User_DefaultRealmVersion(context));
    }

    @Override
    public void InsertEmail(String email){
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

    }

    @Override
    public void ChangeCaptureSetting(boolean flag){
        User user_db = mRealm.where(User.class).equalTo("no",1).findFirst();
        mRealm.beginTransaction();
        user_db.setSend_capture_without_edit(flag);
        mRealm.commitTransaction();
    }
}
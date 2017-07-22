package presenter;

import io.realm.Realm;

/**
 * Created by Sunghyun on 2017. 7. 13..
 * 설정화면 인터페이스
 */

public interface SettingInterface {

    public void InsertEmail(Realm mRealm, String email);
    public void AlarmNdeploy(String url);
    public void AlarmCrash();
    public void ChangeCaptureSetting(Realm mRealm, boolean flag);

}

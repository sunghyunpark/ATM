package realm;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Sunghyun on 2017. 7. 13..
 */

public class RealmConfig{

    public RealmConfiguration User_DefaultRealmVersion(Context context){

        Realm.init(context);    //realm 초기화
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("User.realm")
                .schemaVersion(1)
                //.deleteRealmIfMigrationNeeded()
                .migration(new Migration())
                .build();

        return config;

    }

    public RealmConfiguration DownLoad_DefaultRealmVersion(Context context){

        Realm.init(context);    //realm 초기화
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("DownLoadLink.realm")
                .schemaVersion(1)
                //.deleteRealmIfMigrationNeeded()
                .migration(new Migration())
                .build();

        return config;

    }

    public RealmConfiguration Memo_DefaultRealmVersion(Context context){

        Realm.init(context);    //realm 초기화
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("Memo.realm")
                .schemaVersion(1)
                //.deleteRealmIfMigrationNeeded()
                .migration(new Migration())
                .build();

        return config;

    }
}
package presenter;

import io.realm.Realm;

/**
 * Created by NAVER on 2017-07-22.
 * DownLoadLink / Memo
 */

public interface Writeable {
    public void Write(Realm mRealm, String flag, int no, String title, String contents);
    public void Edit();
}

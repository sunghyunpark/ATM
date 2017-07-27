package realm.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by NAVER on 2017-07-27.
 */

public class Memo extends RealmObject {
    @PrimaryKey
    private int no;
    private String memoTitle;
    private String memoContents;
    private String created_at;

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getMemoTitle() {
        return memoTitle;
    }

    public void setMemoTitle(String memoTitle) {
        this.memoTitle = memoTitle;
    }

    public String getMemoContents() {
        return memoContents;
    }

    public void setMemoContents(String memoContents) {
        this.memoContents = memoContents;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}

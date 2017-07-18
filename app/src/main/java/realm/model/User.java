package realm.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by MacBookPro13 on 2017. 7. 13..
 */

public class User extends RealmObject{

    @PrimaryKey
    private int no;

    private String email;

    private int recordState = 2;

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getRecordState() {
        return recordState;
    }

    public void setRecordState(int recordState) {
        this.recordState = recordState;
    }

}

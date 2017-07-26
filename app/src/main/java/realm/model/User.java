package realm.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Sunghyun on 2017. 7. 13..
 */

public class User extends RealmObject{

    @PrimaryKey
    private int no;
    private String email;
    private boolean crashAlarm;
    private boolean send_capture_without_edit;


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

    public boolean isCrashAlarm() {
        return crashAlarm;
    }

    public void setCrashAlarm(boolean crashAlarm) {
        this.crashAlarm = crashAlarm;
    }

    public boolean isSend_capture_without_edit() {
        return send_capture_without_edit;
    }

    public void setSend_capture_without_edit(boolean send_capture_without_edit) {
        this.send_capture_without_edit = send_capture_without_edit;
    }


}

package nts.nt3.atm;

import android.app.Application;

/**
 * Created by MacBookPro13 on 2017. 7. 12..
 */

public class ATMApplication extends Application {

    private boolean recordState = false;

    public boolean isRecordState() {
        return recordState;
    }

    public void setRecordState(boolean recordState) {
        this.recordState = recordState;
    }

    @Override
    public void onCreate(){
        super.onCreate();
    }
}

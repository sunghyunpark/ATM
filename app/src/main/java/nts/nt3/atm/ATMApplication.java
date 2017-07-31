package nts.nt3.atm;

import android.app.Application;
import android.content.pm.PackageInfo;

/**
 * Created by Sunghyun on 2017. 7. 12..
 */

public class ATMApplication extends Application {

    public static boolean recordState = false;
    public static boolean performanceState = false;
    PackageInfo packageInfo;

    @Override
    public void onCreate(){
        super.onCreate();
    }

    public PackageInfo getPackageInfo() {
        return packageInfo;
    }

    public void setPackageInfo(PackageInfo packageInfo) {
        this.packageInfo = packageInfo;
    }
}

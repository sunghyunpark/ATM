package nts.nt3.atm;

import android.annotation.SuppressLint;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ApkInfoActivity extends AppCompatActivity {
    TextView title, appLabel, packageName, current_version, features;
    TextView permissions, andVersion, installed, lastModify, path;
    ImageView app_icon;
    PackageInfo packageInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apk_info);

        ATMApplication appData = (ATMApplication) getApplicationContext();
        packageInfo = appData.getPackageInfo();
        findViewsById();
        setValues();
    }

    private void findViewsById() {
        title = (TextView) findViewById(R.id.title_txt);
        app_icon = (ImageView) findViewById(R.id.app_icon);
        appLabel = (TextView) findViewById(R.id.applabel);
        packageName = (TextView) findViewById(R.id.package_name);
        current_version = (TextView) findViewById(R.id.current_version);
        features = (TextView) findViewById(R.id.req_feature);
        permissions = (TextView) findViewById(R.id.req_permission);
        andVersion = (TextView) findViewById(R.id.andversion);
        path = (TextView) findViewById(R.id.path);
        installed = (TextView) findViewById(R.id.insdate);
        lastModify = (TextView) findViewById(R.id.last_modify);
    }

    private void setValues() {
        //app 아이콘
        PackageManager pm = this.getApplicationContext().getPackageManager();
        app_icon.setImageDrawable(packageInfo.applicationInfo.loadIcon(pm));

        //title
        title.setText(getPackageManager().getApplicationLabel(
                packageInfo.applicationInfo));

        // APP name
        appLabel.setText(getPackageManager().getApplicationLabel(
                packageInfo.applicationInfo));

        // package name
        packageName.setText(packageInfo.packageName);

        // current version name
        current_version.setText("v"+packageInfo.versionName);


        // target version
        andVersion.setText(Integer
                .toString(packageInfo.applicationInfo.targetSdkVersion));

        // path
        path.setText(packageInfo.applicationInfo.sourceDir);

        // first installation
        installed.setText(setDateFormat(packageInfo.firstInstallTime));

        // last modified
        lastModify.setText(setDateFormat(packageInfo.lastUpdateTime));

        // features
        if (packageInfo.reqFeatures != null)
            features.setText(getFeatures(packageInfo.reqFeatures));
        else
            features.setText("-");

        // uses-permission
        if (packageInfo.requestedPermissions != null)
            permissions
                    .setText(getPermissions(packageInfo.requestedPermissions));
        else
            permissions.setText("-");
    }

    @SuppressLint("SimpleDateFormat")
    private String setDateFormat(long time) {
        Date date = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String strDate = formatter.format(date);
        return strDate;
    }

    // Convert string array to comma separated string
    private String getPermissions(String[] requestedPermissions) {
        String permission = "";
        for (int i = 0; i < requestedPermissions.length; i++) {
            permission = permission + requestedPermissions[i] + ",\n";
        }
        return permission;
    }

    // Convert string array to comma separated string
    private String getFeatures(FeatureInfo[] reqFeatures) {
        String features = "";
        for (int i = 0; i < reqFeatures.length; i++) {
            features = features + reqFeatures[i] + ",\n";
        }
        return features;
    }
}

package nts.nt3.atm;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by NAVER on 2017-07-21.
 * 단말기 내 설치된 APK 정보 리스트 화면
 */

public class ApkManagerSettingFragment extends Fragment {
    View v;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_apk_manager_setting, container, false);

        return v;
    }
}

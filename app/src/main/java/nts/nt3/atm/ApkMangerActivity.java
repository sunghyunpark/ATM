package nts.nt3.atm;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.Button;

public class ApkMangerActivity extends AppCompatActivity {
    //뷰페이저
    private ViewPager mViewPager;
    private pagerAdapter adapter;
    private static final int NUM_PAGES = 2;//페이지 수
    private int temp = 0; //현재 페이지

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apk_manger);

        InitView();
    }

    private void InitView(){
        final Button downloadBtn = (Button)findViewById(R.id.download_btn);
        final Button settingBtn = (Button)findViewById(R.id.setting_btn);

        //뷰페이저
        mViewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new pagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(temp);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                temp = position;

                switch (temp) {
                    case 0:
                        settingBtn.setTextColor(getResources().getColor(R.color.colorBlack));
                        downloadBtn.setTextColor(getResources().getColor(R.color.colorSky));
                        break;
                    case 1:
                        settingBtn.setTextColor(getResources().getColor(R.color.colorSky));
                        downloadBtn.setTextColor(getResources().getColor(R.color.colorBlack));
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub
            }
        });
    }
    private class pagerAdapter extends FragmentPagerAdapter {

        public pagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            if (object instanceof Fragment) {
                Fragment fragment = (Fragment) object;
                android.support.v4.app.FragmentManager fm = fragment.getFragmentManager();
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                ft.remove(fragment);
                this.notifyDataSetChanged();
                ft.commitAllowingStateLoss();
            }
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new ApkManagerDownloadFragment();
                    break;
                case 1:
                    fragment = new ApkManagerSettingFragment();
                    break;
                default:
                    return null;
            }

            return fragment;
        }


        // 생성 가능한 페이지 개수를 반환해준다.
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return NUM_PAGES;
        }
    }
}

package nts.nt3.atm;


import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NAVER on 2017-07-21.
 * 단말기 내 설치된 APK 정보 리스트 화면
 */

public class ApkManagerSettingFragment extends Fragment {
    View v;
    //리뷰 리사이클러뷰
    RecyclerAdapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    @Override
    public void onResume(){
        super.onResume();
        InitView();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_apk_manager_setting, container, false);

        return v;
    }

    private void InitView(){
        recyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        //recyclerview 구분선
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(getActivity(),new LinearLayoutManager(getActivity()).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        SetList();
    }

    private void SetList(){
        final PackageManager packageManager = this.getContext().getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<PackageInfo> packageList = packageManager
                .getInstalledPackages(PackageManager.GET_PERMISSIONS);

        List<PackageInfo> packageList1 = new ArrayList<PackageInfo>();

        //앱 리스트
        for(PackageInfo pi : packageList) {
            boolean b = isSystemPackage(pi);
            if(!b) {
                packageList1.add(pi);
            }
        }

        adapter = new RecyclerAdapter(packageList1,packageManager);
        recyclerView.setAdapter(adapter);
    }

    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true
                : false;
    }

    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int TYPE_ITEM = 0;

        List<PackageInfo> listItems;
        PackageManager packageManager;

        private RecyclerAdapter(List<PackageInfo> listItems,PackageManager packageManager) {
            this.listItems = listItems;
            this.packageManager = packageManager;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_ITEM) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_apk_manager_setting, parent, false);
                return new VHitem_AppList(v);
            }
            throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
        }

        private Object getItem(int position) {
            return listItems.get(position+1);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            if (holder instanceof VHitem_AppList)
            {
                //final AppData currentItem = getItem(position-1);
                VHitem_AppList VHitem = (VHitem_AppList)holder;
                final PackageInfo packageInfo = (PackageInfo) getItem(position-1);
                //앱 아이콘
                Drawable appIcon = packageManager
                        .getApplicationIcon(packageInfo.applicationInfo);
                VHitem.app_icon.setImageDrawable(appIcon);

                //앱 이름
                String appName = packageManager.getApplicationLabel(
                        packageInfo.applicationInfo).toString();
                VHitem.app_name_txt.setText(appName);

                //패키지 명
                String PackageName = packageInfo.packageName;
                VHitem.app_package_txt.setText(PackageName);

                VHitem.item_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Intent marketLaunch = new Intent(Intent.ACTION_VIEW);
                        //marketLaunch.setData(Uri.parse("market://details?id="+currentItem.getAppPackageName()));
                        //startActivity(marketLaunch);

                        ATMApplication appData = (ATMApplication) getActivity().getApplicationContext();
                        appData.setPackageInfo(packageInfo);
                        Intent intent = new Intent(getActivity(), ApkInfoActivity.class);
                        startActivity(intent);

                    }
                });

            }
        }
        class VHitem_AppList extends RecyclerView.ViewHolder{
            ViewGroup item_layout;
            TextView app_name_txt;
            TextView app_package_txt;
            ImageView app_icon;
            private VHitem_AppList(View itemView) {
                super(itemView);
                item_layout = (ViewGroup)itemView.findViewById(R.id.item_layout);
                app_icon = (ImageView)itemView.findViewById(R.id.app_icon);
                app_name_txt = (TextView)itemView.findViewById(R.id.app_name_txt);
                app_package_txt = (TextView)itemView.findViewById(R.id.app_package_txt);
            }
        }
        @Override
        public int getItemViewType(int position) {
            return TYPE_ITEM;
        }
        //increasing getItemcount to 1. This will be the row of header.
        @Override
        public int getItemCount() {
            return listItems.size();
        }
    }
}

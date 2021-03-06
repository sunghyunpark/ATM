package nts.nt3.atm;


import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import model.DownLinkModel;
import presenter.WritePresenter;
import realm.RealmConfig;
import realm.model.DownLoadLink;

/**
 * Created by NAVER on 2017-07-21.
 * 다운로드 링크 관리 화면
 */

public class ApkManagerDownloadFragment extends Fragment {
    View v;
    Realm mRealm;
    RealmConfig realmConfig;
    //리뷰 리사이클러뷰
    RecyclerAdapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    private ArrayList<DownLinkModel> listItems;
    TextView empty_txt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume(){
        super.onResume();
        SetList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_apk_manager_download, container, false);
        InitView();
        return v;
    }

    private void InitView(){
        realmConfig = new RealmConfig();
        mRealm = Realm.getInstance(realmConfig.DownLoad_DefaultRealmVersion(getActivity()));

        recyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        //recyclerview 구분선
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(getActivity(),new LinearLayoutManager(getActivity()).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void SetList(){
        listItems = new ArrayList<DownLinkModel>();
        RealmResults<DownLoadLink> dataList = mRealm.where(DownLoadLink.class).findAll();
        empty_txt = (TextView)v.findViewById(R.id.empty_txt);

        int dataSize = dataList.size();
        if(dataSize > 0){
            empty_txt.setVisibility(View.GONE);
            DownLinkModel downLinkModel;
            for(int i=0;i<dataSize;i++){
                downLinkModel = new DownLinkModel();
                downLinkModel.setNo(dataList.get(i).getNo());
                downLinkModel.setLinkTitle(dataList.get(i).getLinkTitle());
                downLinkModel.setLinkUrl(dataList.get(i).getLinkUrl());
                listItems.add(downLinkModel);
            }
            adapter = new RecyclerAdapter(listItems);
            recyclerView.setAdapter(adapter);
        }else{
            empty_txt.setVisibility(View.VISIBLE);
        }
    }

    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int TYPE_ITEM = 1;

        List<DownLinkModel> listItems;

        public RecyclerAdapter(List<DownLinkModel> listItems) {
            this.listItems = listItems;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_ITEM) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recylclerview_apk_manager_download, parent, false);
                return new VHitem_items(v);
            }
            throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
        }

        private DownLinkModel getItem(int position) {
            return listItems.get(position);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            if (holder instanceof VHitem_items)
            {
                final DownLinkModel currentItem = getItem(position);
                final VHitem_items VHitem = (VHitem_items)holder;

                VHitem.linkTitle.setText(currentItem.getLinkTitle());
                VHitem.linkUrl.setText(currentItem.getLinkUrl());
                VHitem.download_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try{
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentItem.getLinkUrl()));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                            startActivity(intent);
                        }catch (ActivityNotFoundException e){
                            Toast.makeText(getContext(), "url주소를 다시 확인해주세요.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                VHitem.item_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), WriteActivity.class);
                        intent.putExtra("flag", "EditLink");
                        intent.putExtra("no", currentItem.getNo());
                        intent.putExtra("EditTitle", currentItem.getLinkTitle());
                        intent.putExtra("EditContent", currentItem.getLinkUrl());
                        startActivity(intent);
                    }
                });
                VHitem.item_layout.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                        alert.setTitle("삭제");
                        alert.setMessage("삭제하시겠습니까?");
                        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                removeItem(position);
                            }
                        });
                        alert.setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        // Canceled.

                                    }
                                });
                        alert.show();
                        return false;
                    }
                });

            }
        }

        private class VHitem_items extends RecyclerView.ViewHolder{
            ViewGroup item_layout;
            TextView linkTitle;
            TextView linkUrl;
            ImageView download_btn;

            private VHitem_items(View itemView) {
                super(itemView);
                item_layout = (ViewGroup)itemView.findViewById(R.id.item_layout);
                linkUrl = (TextView)itemView.findViewById(R.id.link_url_txt);
                linkTitle = (TextView)itemView.findViewById(R.id.link_title_txt);
                download_btn = (ImageView)itemView.findViewById(R.id.download_btn);
            }
        }
        private void removeItem(int position){
            listItems.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, listItems.size());
            if(listItems.size() == 0){
                empty_txt.setVisibility(View.VISIBLE);
            }
            WritePresenter writePresenter = new WritePresenter();
            writePresenter.Delete(mRealm, "Link", position);
        }

        private boolean isPositionHeader(int position) {
            return position == 0;
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

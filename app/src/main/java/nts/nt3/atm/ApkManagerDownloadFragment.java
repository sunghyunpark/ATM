package nts.nt3.atm;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import model.DownLinkModel;

/**
 * Created by NAVER on 2017-07-21.
 * 다운로드 링크 관리 화면
 */

public class ApkManagerDownloadFragment extends Fragment {
    View v;
    //리뷰 리사이클러뷰
    RecyclerAdapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    private ArrayList<DownLinkModel> listItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_apk_manager_download, container, false);

        InitView();
        return v;
    }

    private void InitView(){
        recyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        SetList();
    }

    private void SetList(){
        listItems = new ArrayList<DownLinkModel>();
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


            }
        }

        public void addItem(DownLinkModel items){
            listItems.add(0, items);
            adapter.notifyDataSetChanged();
        }


        public class VHitem_items extends RecyclerView.ViewHolder{
            ViewGroup item_layout;
            TextView linkTitle;
            TextView linkUrl;
            ImageView download_btn;

            public VHitem_items(View itemView) {
                super(itemView);
                item_layout = (ViewGroup)itemView.findViewById(R.id.item_layout);
                linkUrl = (TextView)itemView.findViewById(R.id.link_url_txt);
                linkTitle = (TextView)itemView.findViewById(R.id.link_title_txt);
                download_btn = (ImageView)itemView.findViewById(R.id.download_btn);
            }

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

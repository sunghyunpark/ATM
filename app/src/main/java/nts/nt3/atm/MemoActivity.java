package nts.nt3.atm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import model.MemoModel;
import presenter.WritePresenter;
import realm.RealmConfig;
import realm.model.Memo;

public class MemoActivity extends AppCompatActivity {
    Realm mRealm;
    RealmConfig realmConfig;

    //리뷰 리사이클러뷰
    RecyclerAdapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    private ArrayList<MemoModel> listItems;
    TextView empty_txt;    //비어있을 때 문구

    @Override
    public void onResume(){
        super.onResume();
        SetList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        InitView();
    }

    private void InitView(){
        realmConfig = new RealmConfig();
        mRealm = Realm.getInstance(realmConfig.Memo_DefaultRealmVersion(getApplicationContext()));

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        //recyclerview 구분선
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(getApplicationContext(),new LinearLayoutManager(getApplicationContext()).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        ImageView writeBtn = (ImageView)findViewById(R.id.write_memo_btn);
        writeBtn.setOnTouchListener(myOnTouchListener);

        ImageView backBtn = (ImageView)findViewById(R.id.back_btn);
        backBtn.setOnTouchListener(myOnTouchListener);
    }

    private void SetList(){
        listItems = new ArrayList<MemoModel>();
        RealmResults<Memo> dataList = mRealm.where(Memo.class).findAll();
        empty_txt = (TextView)findViewById(R.id.empty_txt);

        int dataSize = dataList.size();
        if(dataSize > 0){
            empty_txt.setVisibility(View.GONE);
            MemoModel memoModel;
            for(int i=0;i<dataSize;i++){
                memoModel = new MemoModel();
                memoModel.setNo(dataList.get(i).getNo());
                memoModel.setMemoTitle(dataList.get(i).getMemoTitle());
                memoModel.setMemoContents(dataList.get(i).getMemoContents());
                memoModel.setCreated_at(dataList.get(i).getCreated_at());
                listItems.add(memoModel);
            }
            adapter = new RecyclerAdapter(listItems);
            recyclerView.setAdapter(adapter);
        }else{
            empty_txt.setVisibility(View.VISIBLE);
        }
    }

    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int TYPE_ITEM = 1;

        List<MemoModel> listItems;

        public RecyclerAdapter(List<MemoModel> listItems) {
            this.listItems = listItems;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_ITEM) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recylclerview_memo, parent, false);
                return new RecyclerAdapter.VHitem_items(v);
            }
            throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
        }

        private MemoModel getItem(int position) {
            return listItems.get(position);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            if (holder instanceof RecyclerAdapter.VHitem_items)
            {
                final MemoModel currentItem = getItem(position);
                final VHitem_items VHitem = (VHitem_items)holder;

                VHitem.item_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), MemoViewerActivity.class);
                        intent.putExtra("no", currentItem.getNo());
                        intent.putExtra("MemoTitle", currentItem.getMemoTitle());
                        intent.putExtra("MemoContent", currentItem.getMemoContents());
                        intent.putExtra("created_at", currentItem.getCreated_at());
                        startActivity(intent);
                    }
                });
                VHitem.item_layout.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        AlertDialog.Builder alert = new AlertDialog.Builder(MemoActivity.this);
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
                VHitem.memo_title_txt.setText(currentItem.getMemoTitle());
                VHitem.memo_contents_txt.setText(currentItem.getMemoContents());
                VHitem.created_at.setText(currentItem.getCreated_at());
            }
        }

        private class VHitem_items extends RecyclerView.ViewHolder{
            ViewGroup item_layout;
            TextView memo_title_txt;
            TextView memo_contents_txt;
            TextView created_at;

            private VHitem_items(View itemView) {
                super(itemView);
                item_layout = (ViewGroup)itemView.findViewById(R.id.item_layout);
                memo_title_txt = (TextView)itemView.findViewById(R.id.memo_title_txt);
                memo_contents_txt = (TextView)itemView.findViewById(R.id.memo_contents_txt);
                created_at = (TextView)itemView.findViewById(R.id.created_at);
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
            writePresenter.Delete(mRealm, "Memo", position);
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

    /**
     * 각 버튼들 이벤트
     */
    private View.OnTouchListener myOnTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.setAlpha(0.55f);
            }else if(event.getAction() == MotionEvent.ACTION_CANCEL){
                v.setAlpha(1.0f);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                v.setAlpha(1.0f);
                switch(v.getId()){

                    case R.id.back_btn:
                        finish();
                        break;
                    case R.id.write_memo_btn:
                        Intent intent = new Intent(getApplicationContext(), WriteActivity.class);
                        intent.putExtra("flag", "Memo");
                        startActivity(intent);
                        break;

                }
            }
            return true;
        }
    };
}

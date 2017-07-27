package nts.nt3.atm;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Realm;
import presenter.WritePresenter;
import realm.RealmConfig;
import realm.model.DownLoadLink;
import realm.model.Memo;

public class WriteActivity extends AppCompatActivity {

    Realm mRealm;
    RealmConfig realmConfig;
    WritePresenter writePresenter;
    private String flag;    //Link / Memo / Edit(EditLink / EditMemo)
    private int no;     //EditMode인 경우
    private String title_str, content_str;    //EditMode인경우
    private EditText title_et;
    private EditText content_et;

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(writePresenter != null)
            writePresenter = null;
        mRealm.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        Intent intent = getIntent();
        flag = intent.getExtras().getString("flag");
        if(flag.equals("EditLink") || flag.equals("EditMemo")){
            no = intent.getExtras().getInt("no");
            title_str = intent.getExtras().getString("EditTitle");
            content_str = intent.getExtras().getString("EditContent");
        }
        InitView();
    }

    private void InitView(){
        realmConfig = new RealmConfig();
        if(flag.equals("Link") || flag.equals("EditLink")){
            mRealm = Realm.getInstance(realmConfig.DownLoad_DefaultRealmVersion(getApplicationContext()));
        }else{
            mRealm = Realm.getInstance(realmConfig.Memo_DefaultRealmVersion(getApplicationContext()));
        }
        writePresenter = new WritePresenter();

        ImageView backBtn = (ImageView)findViewById(R.id.back_btn);
        backBtn.setOnTouchListener(myOnTouchListener);
        Button saveBtn = (Button)findViewById(R.id.save_btn);
        saveBtn.setOnTouchListener(myOnTouchListener);

        TextView title_tv = (TextView)findViewById(R.id.title_txt);    //상단 타이틀
        title_et = (EditText)findViewById(R.id.title_edit_box);    // 제목 editbox
        content_et = (EditText)findViewById(R.id.content_edit_box);    //내용 editbox
        Resources res = getResources();
        if(flag.equals("Link")){
            title_tv.setText(String.format(res.getString(R.string.write_download_link_activity_title)));
            title_et.setHint(String.format(res.getString(R.string.write_memo_activity_title_hint)));
            content_et.setHint(String.format(res.getString(R.string.write_memo_activity_content_hint)));
        }else if(flag.equals("Memo")){
            title_tv.setText(String.format(res.getString(R.string.write_memo_activity_title)));
            title_et.setHint(String.format(res.getString(R.string.write_memo_activity_title_hint)));
            content_et.setHint(String.format(res.getString(R.string.write_memo_activity_content_hint)));
        }else{
            // Edit Mode (EditLink / EditMemo)
            title_tv.setText(String.format(res.getString(R.string.write_edit_activity_title)));
            title_et.setHint(String.format(res.getString(R.string.write_download_link_activity_title_hint)));
            content_et.setHint(String.format(res.getString(R.string.write_download_link_activity_url_hint)));
            title_et.setText(title_str);
            content_et.setText(content_str);
        }
    }

    //autoincrement 증가 flag -> Link / Memo
    private int getNextKey(String flag)
    {
        int key;
        try {
            if(flag.equals("Link")){
                key = mRealm.where(DownLoadLink.class).max("no").intValue() + 1;
            }else{
                key = mRealm.where(Memo.class).max("no").intValue() + 1;
            }
        } catch(ArrayIndexOutOfBoundsException ex) {
            key = 0;
        }catch (NullPointerException n){
            key = 0;
        }
        return key;
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
                    case R.id.save_btn:
                        Resources res = getResources();
                        if(title_et.getText().toString().trim().equals("") || content_et.getText().toString().trim().equals("")){
                            Toast.makeText(getApplicationContext(), String.format(res.getString(R.string.not_exist_info_txt)), Toast.LENGTH_SHORT).show();
                        }else{
                            if(!flag.equals("EditLink") && !flag.equals("EditMemo")){    //Edit 모드가 아닌 경우(Link / Memo)
                                writePresenter.Write(mRealm, flag, getNextKey(flag), title_et.getText().toString().trim(), content_et.getText().toString().trim());
                            }else{    //Edit모드인경우(EditLink / EditMemo)
                                writePresenter.Edit(mRealm, flag, no, title_et.getText().toString().trim(), content_et.getText().toString().trim());
                            }
                            finish();
                        }
                        break;
                }
            }
            return true;
        }
    };
}

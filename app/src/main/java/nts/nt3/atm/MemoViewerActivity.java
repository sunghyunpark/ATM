package nts.nt3.atm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MemoViewerActivity extends AppCompatActivity {

    private int no;
    private String mMemoTitle, mMemoContent, mCreated_at;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_viewer);

        Intent intent = getIntent();
        no = intent.getExtras().getInt("no");
        mMemoTitle = intent.getExtras().getString("MemoTitle");
        mMemoContent = intent.getExtras().getString("MemoContent");

        InitView();
    }

    private void InitView(){
        ImageView backBtn = (ImageView)findViewById(R.id.back_btn);
        backBtn.setOnTouchListener(myOnTouchListener);

        final TextView memoContent_txt = (TextView)findViewById(R.id.memo_contents_txt);
        memoContent_txt.setText(mMemoContent);
        memoContent_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("LABEL", memoContent_txt.getText());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), "클립보드에 복사했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        Button editBtn = (Button)findViewById(R.id.edit_btn);
        editBtn.setOnTouchListener(myOnTouchListener);


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
                    case R.id.edit_btn:
                        Intent intent = new Intent(getApplicationContext(), WriteActivity.class);
                        intent.putExtra("flag", "EditMemo");
                        intent.putExtra("no", no);
                        intent.putExtra("EditTitle", mMemoTitle);
                        intent.putExtra("EditContent", mMemoContent);
                        startActivity(intent);
                        finish();
                        break;

                }
            }
            return true;
        }
    };
}

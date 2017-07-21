package nts.nt3.atm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class WriteDownLoadLinkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_down_load_link);

        InitView();
    }

    private void InitView(){
        ImageView backBtn = (ImageView)findViewById(R.id.back_btn);
        backBtn.setOnTouchListener(myOnTouchListener);
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

                        break;
                }
            }
            return true;
        }
    };
}

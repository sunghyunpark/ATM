package capture;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import nts.nt3.atm.R;
import service.WheelViewService;

/**
 * ScreenCaptureActivity로부터 캡쳐 이미지의 로컬 경로를 받아옴.
 */
public class EditCaptureActivity extends AppCompatActivity {

    private Bitmap captureBit;
    private String ImgFullPath;
    private boolean DRAW_MODE = false;
    private EditCapturePresenter editCapturePresenter;
    public static ViewGroup title_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_capture);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);    //status bar 제거

        Intent intent = getIntent();
        ImgFullPath = intent.getExtras().getString("ImgFullPath");    //로컬에 저장된 이미지 경로

        editCapturePresenter = new EditCapturePresenter(getApplicationContext());

        InitView(ImgFullPath);
    }

    /**
     * 받아온 이미지 경로를 통해 해당 이미지를 불러옴
     * @param ImgPath -> Capture 후 로컬에 저장된 이미지 경로
     */
    private void InitView(String ImgPath){
        //title bar
        title_bar = (ViewGroup)findViewById(R.id.title_bar);
        // 캡쳐한 이미지를 보여줌
        ImageView capture_iv = (ImageView)findViewById(R.id.captureImg);
        captureBit = BitmapFactory.decodeFile(ImgPath);
        Drawable drawable = new BitmapDrawable(captureBit);
        capture_iv.setBackground(drawable);
        //닫기 버튼
        ImageView backBtn = (ImageView)findViewById(R.id.back_btn);
        backBtn.setOnTouchListener(myOnTouchListener);
        //그리기 버튼
        ImageView drawBtn = (ImageView)findViewById(R.id.draw_btn);
        drawBtn.setOnTouchListener(myOnTouchListener);
        //보내기 버튼
        ImageView sendBtn = (ImageView)findViewById(R.id.send_btn);
        sendBtn.setOnTouchListener(myOnTouchListener);

    }

    /**
     * 현재 화면을 캡쳐하여 로컬에 저장함(그리기 기능을 했을 때 사용)
     * @return
     */
    private String takeScreenshot() {
        View rootView = findViewById(R.id.activity_edit_capture).getRootView();
        rootView.setDrawingCacheEnabled(true);
        Bitmap bmap = rootView.getDrawingCache();
        Rect statusBar = new Rect();
        this.getWindow().getDecorView().getWindowVisibleDisplayFrame(statusBar);
        Bitmap snapshot = Bitmap.createBitmap(bmap, 0, statusBar.top, bmap.getWidth(), bmap.getHeight() - statusBar.top, null, true);

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fullPath = Environment.getExternalStorageDirectory()+"/ATM/"+ timeStamp + "_ATMCapture.png";
        File folder_path = new File(Environment.getExternalStorageDirectory()+"/ATM/");
        if(!folder_path.exists()){
            folder_path.mkdir();
        }
        //로컬에 저장
        OutputStream outStream = null;
        File file = new File(fullPath);

        try{
            outStream = new FileOutputStream(file);
            snapshot.compress(Bitmap.CompressFormat.PNG,100,outStream);
            outStream.flush();
            outStream.close();
        }catch(FileNotFoundException e){

        }catch(IOException e){

        }
        return fullPath;
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        captureBit.recycle();
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
                    case R.id.back_btn:    //닫기
                        finish();
                        break;
                    case R.id.draw_btn:    //그리기 모드
                        DRAW_MODE = true;
                        DrawView drawView = (DrawView)findViewById(R.id.drawView);
                        editCapturePresenter.DrawPen(drawView);
                        break;
                    case R.id.send_btn:    //보내기
                        if(DRAW_MODE){
                            //그리기 모드
                            editCapturePresenter.SendBtn(takeScreenshot());
                        }else{
                            editCapturePresenter.SendBtn(ImgFullPath);
                        }
                        finish();
                        break;
                }
            }
            return true;
        }
    };
}

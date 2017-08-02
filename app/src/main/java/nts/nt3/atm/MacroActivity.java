package nts.nt3.atm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import model.MacroScript;

public class MacroActivity extends AppCompatActivity {

    private Bitmap bitmap;    //캡쳐한 이미지
    private int LONG_TAB_TIME = 2;
    private static final int REQUEST_DIALOG = 100;
    private int MODE;
    private static final int CLICK_MODE = 1;    //탭
    private static final int LONG_TAB_MODE = 2;    //롱탭
    private static final int DRAG_MODE = 3;    //드래그
    private static final int INIT_MODE = 4;    //초기화

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(bitmap != null)
            bitmap.recycle();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_macro);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        String ImgFullPath = intent.getExtras().getString("ImgFullPath");

        InitView(ImgFullPath);
    }

    private void InitView(String imgPath){
        MODE = CLICK_MODE;
        bitmap = BitmapFactory.decodeFile(imgPath);
        Drawable d = new BitmapDrawable(bitmap);

        ViewGroup macroView = (ViewGroup)findViewById(R.id.macro_view);
        macroView.setBackground(d);
    }

    private void longTabDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(MacroActivity.this);
        alert.setTitle("롱탭");
        alert.setMessage("롱탭할 시간을 입력해주세요.(초 단위)");
        //다이얼로그 밖의 영역 탭하여 취소했을 때
        alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                LONG_TAB_TIME = 2;
            }
        });
        // Set an EditText view to get user input
        final EditText time_edit = new EditText(MacroActivity.this);
        time_edit.requestFocus();
        time_edit.setInputType(InputType.TYPE_CLASS_NUMBER
                |InputType.TYPE_NUMBER_FLAG_DECIMAL);
        alert.setView(time_edit);

        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                LONG_TAB_TIME = Integer.parseInt(time_edit.getText().toString());
            }
        });

        alert.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        LONG_TAB_TIME = 2;
                    }
                });
        alert.show();
    }

    private void saveScript(String scripTitle){
        String dirPath = getFilesDir().getAbsolutePath();
        File file = new File(dirPath);
        // 일치하는 폴더가 없으면 생성
        if( !file.exists() ) {
            file.mkdirs();
        }
        // txt 파일 생성
        scripTitle += ".txt";
        File savefile = new File(Environment.getExternalStorageDirectory() + "/Android/data/"+scripTitle);
        try{
            FileOutputStream fos = new FileOutputStream(savefile);
            fos.write(MacroScript.getInstance().getScript().getBytes());
            fos.close();
            Toast.makeText(this, "Save Success", Toast.LENGTH_SHORT).show();
        } catch(IOException e){}
    }

    private void saveDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(MacroActivity.this);
        alert.setTitle("스크립트 저장");
        alert.setMessage("저장할 스크립트의 제목을 입력해주세요.");
        //다이얼로그 밖의 영역 탭하여 취소했을 때
        alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {

            }
        });
        // Set an EditText view to get user input
        final EditText script_title = new EditText(MacroActivity.this);
        script_title.requestFocus();
        alert.setView(script_title);

        alert.setPositiveButton("저장", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                MacroScript.getInstance().setScript("UserWait(2000)\nquit");
                saveScript(script_title.getText().toString());
                MacroScript.getInstance().Init();
                finish();
            }
        });

        alert.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        MacroScript.getInstance().Init();
                        finish();
                    }
                });
        alert.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case REQUEST_DIALOG:
                if(data.getExtras().getInt("result") == INIT_MODE){
                    //초기화
                    MODE = CLICK_MODE;
                    MacroScript.getInstance().Init();
                    Toast.makeText(getApplicationContext(),"스크립트를 초기화하였습니다.", Toast.LENGTH_SHORT).show();
                }else if(data.getExtras().getInt("result") == LONG_TAB_MODE){
                    //롱탭
                    MODE = LONG_TAB_MODE;
                    longTabDialog();
                }else if(data.getExtras().getInt("result") == DRAG_MODE){
                    //드래그
                    MODE = DRAG_MODE;

                }
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        super.onTouchEvent(event);

        //event
        //event 종류/각각의 특성

        if(event.getAction() == MotionEvent.ACTION_DOWN ){

            float x = event.getX();
            float y = event.getY();

            String msg = "터치를 입력받음 : " +x+" / " +y;
            Toast. makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT ).show();

            if(MODE == LONG_TAB_MODE){
                MacroScript.getInstance().setScript("DispatchPointer(0, 0, 0, "+x+", "+y+", 0,0,0,0,0,0,0)\n");
                MacroScript.getInstance().setScript("UserWait("+LONG_TAB_TIME*1000+")\n");
                MacroScript.getInstance().setScript("DispatchPointer(0, 0, 1, "+x+", "+y+", 0,0,0,0,0,0,0)\n");
                MacroScript.getInstance().setScript("UserWait(2000)\n");
                MODE = CLICK_MODE;
            }else if(MODE == DRAG_MODE){

            }else if(MODE == CLICK_MODE){
                MacroScript.getInstance().setScript("DispatchPointer(0, 0, 0, "+x+", "+y+", 0,0,0,0,0,0,0)\n");
                MacroScript.getInstance().setScript("DispatchPointer(0, 0, 1, "+x+", "+y+", 0,0,0,0,0,0,0)\n");
                MacroScript.getInstance().setScript("UserWait(2000)\n");
            }
            //setNotification("Gone");
            finish();
            return true;
        }

        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            //하드웨어 뒤로가기 버튼에 따른 이벤트 설정
            case KeyEvent.KEYCODE_BACK:
                saveDialog();
                //setNotification("Gone");
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                Intent intent = new Intent(getApplicationContext(), MacroMenuDialog.class);
                startActivityForResult(intent, REQUEST_DIALOG);
                return true;
            default:
                break;
        }

        return super.onKeyDown(keyCode, event);
    }

}

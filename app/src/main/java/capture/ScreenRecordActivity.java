package capture;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import nts.nt3.atm.R;
import realm.RealmConfig;
import realm.model.User;

/**
 * Created by Sunghyun on 2017. 7. 7..
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ScreenRecordActivity extends AppCompatActivity {
    private static final String TAG = "ScreenRecordActivity";
    private static final int REQUEST_CODE = 1000;
    private int mScreenDensity;
    private MediaProjectionManager mProjectionManager;
    private static final int DISPLAY_WIDTH = 720;
    private static final int DISPLAY_HEIGHT = 1280;
    private MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;
    private MediaProjectionCallback mMediaProjectionCallback;
    private ToggleButton mToggleButton;
    private MediaRecorder mMediaRecorder;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    private static final int REQUEST_PERMISSIONS = 10;

    private int recordLevel;
    Realm mRealm;
    RealmConfig realmConfig;
    //버튼
    ImageView high_btn;
    ImageView mid_btn;
    ImageView low_btn;

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_record_activity);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenDensity = metrics.densityDpi;

        mMediaRecorder = new MediaRecorder();

        InitView();

        mProjectionManager = (MediaProjectionManager) getSystemService
                (Context.MEDIA_PROJECTION_SERVICE);


        mToggleButton = (ToggleButton) findViewById(R.id.toggle);
        if(mToggleButton.isChecked()){
            mToggleButton.setTextColor(getResources().getColor(R.color.colorAccent));
        }else{
            mToggleButton.setTextColor(getResources().getColor(R.color.colorSky));
        }
        mToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mToggleButton.isChecked()){
                    mToggleButton.setTextColor(getResources().getColor(R.color.colorAccent));
                }else{
                    mToggleButton.setTextColor(getResources().getColor(R.color.colorSky));
                }
                if (ContextCompat.checkSelfPermission(ScreenRecordActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) + ContextCompat
                        .checkSelfPermission(ScreenRecordActivity.this,
                                Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale
                            (ScreenRecordActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                            ActivityCompat.shouldShowRequestPermissionRationale
                                    (ScreenRecordActivity.this, Manifest.permission.RECORD_AUDIO)) {
                        mToggleButton.setChecked(false);

                    } else {
                        ActivityCompat.requestPermissions(ScreenRecordActivity.this,
                                new String[]{Manifest.permission
                                        .WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO},
                                REQUEST_PERMISSIONS);
                    }
                } else {
                    onToggleScreenShare(v);
                }
            }
        });
    }

    private void InitView(){
        realmConfig = new RealmConfig();

        mRealm = Realm.getInstance(realmConfig.User_DefaultRealmVersion(getApplicationContext()));
        User user_db = mRealm.where(User.class).equalTo("no",1).findFirst();
        recordLevel = user_db.getRecordState();
        ViewGroup record_state_high_btn = (ViewGroup)findViewById(R.id.record_state_high_btn);
        ViewGroup record_state_mid_btn = (ViewGroup)findViewById(R.id.record_state_mid_btn);
        ViewGroup record_state_low_btn = (ViewGroup)findViewById(R.id.record_state_low_btn);
        ImageView back_btn = (ImageView)findViewById(R.id.back_btn);
        back_btn.setOnTouchListener(myOnTouchListener);

        high_btn = (ImageView)findViewById(R.id.high_btn);
        mid_btn = (ImageView)findViewById(R.id.mid_btn);
        low_btn = (ImageView)findViewById(R.id.low_btn);

        InitState(recordLevel);

        record_state_high_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordLevel = 1;
                InitState(recordLevel);
                UpdateRecordLevel();
            }
        });
        record_state_mid_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordLevel = 2;
                InitState(recordLevel);
                UpdateRecordLevel();
            }
        });
        record_state_low_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordLevel = 3;
                InitState(recordLevel);
                UpdateRecordLevel();
            }
        });
    }

    private void UpdateRecordLevel(){
        RealmConfig realmConfig = new RealmConfig();
        Realm mRealm = Realm.getInstance(realmConfig.User_DefaultRealmVersion(getApplicationContext()));

        User user_db = mRealm.where(User.class).equalTo("no",1).findFirst();
        mRealm.beginTransaction();
        user_db.setRecordState(recordLevel);
        mRealm.commitTransaction();
    }

    private void InitState(int level){

        if(level == 1){
            high_btn.setVisibility(View.VISIBLE);
            mid_btn.setVisibility(View.GONE);
            low_btn.setVisibility(View.GONE);
        }else if(level == 2){
            high_btn.setVisibility(View.GONE);
            mid_btn.setVisibility(View.VISIBLE);
            low_btn.setVisibility(View.GONE);
        }else if(level == 3){
            high_btn.setVisibility(View.GONE);
            mid_btn.setVisibility(View.GONE);
            low_btn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != REQUEST_CODE) {
            Log.e(TAG, "Unknown request code: " + requestCode);
            return;
        }
        if (resultCode != RESULT_OK) {
            Toast.makeText(this,
                    "Screen Cast Permission Denied", Toast.LENGTH_SHORT).show();
            mToggleButton.setChecked(false);
            return;
        }
        mMediaProjectionCallback = new MediaProjectionCallback();
        mMediaProjection = mProjectionManager.getMediaProjection(resultCode, data);
        mMediaProjection.registerCallback(mMediaProjectionCallback, null);
        mVirtualDisplay = createVirtualDisplay();
        mMediaRecorder.start();
    }

    public void onToggleScreenShare(View view) {
        if (((ToggleButton) view).isChecked()) {
            initRecorder();
            shareScreen();
        } else {
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            Log.v(TAG, "Stopping Recording");
            stopScreenSharing();
        }
    }

    private void shareScreen() {
        if (mMediaProjection == null) {
            startActivityForResult(mProjectionManager.createScreenCaptureIntent(), REQUEST_CODE);
            return;
        }
        mVirtualDisplay = createVirtualDisplay();
        mMediaRecorder.start();
    }

    private VirtualDisplay createVirtualDisplay() {
        //동영상 녹화를 시작하면 액티비티를 BG로 내림
        Toast.makeText(getApplicationContext(),"녹화 시작",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_MAIN); //태스크의 첫 액티비티로 시작
        intent.addCategory(Intent.CATEGORY_HOME);   //홈화면 표시
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //새로운 태스크를 생성하여 그 태스크안에서 액티비티 추가
        startActivity(intent);
        return mMediaProjection.createVirtualDisplay("ScreenRecordActivity",
                DISPLAY_WIDTH, DISPLAY_HEIGHT, mScreenDensity,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mMediaRecorder.getSurface(), null /*Callbacks*/, null
                /*Handler*/);
    }

    private void initRecorder() {
        try {
            /**
             * 기존 ATM에서는 FrameRate를
             * 10 -> 고화질
             * 15 -> 중화질
             * 30 -> 저화질 로 적용했었음.
             */
            int rate = 20;
            if(recordLevel == 1){
                rate = 10;
            }else if(recordLevel == 2){
                rate = 20;
            }else if(recordLevel == 3){
                rate = 30;
            }
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mMediaRecorder.setOutputFile(Environment.getExternalStorageDirectory() + "/ATM/" + timeStamp+"_video.mp4");
            mMediaRecorder.setVideoSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
            mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mMediaRecorder.setVideoEncodingBitRate(512 * 1000);
            mMediaRecorder.setVideoFrameRate(rate);
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            int orientation = ORIENTATIONS.get(rotation + 90);
            mMediaRecorder.setOrientationHint(orientation);
            mMediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class MediaProjectionCallback extends MediaProjection.Callback {
        @Override
        public void onStop() {
            if (mToggleButton.isChecked()) {
                mToggleButton.setChecked(false);
                mMediaRecorder.stop();
                mMediaRecorder.reset();
                Log.v(TAG, "Recording Stopped");
            }
            mMediaProjection = null;
            stopScreenSharing();
        }
    }

    private void stopScreenSharing() {
        if (mVirtualDisplay == null) {
            return;
        }
        mVirtualDisplay.release();
        //mMediaRecorder.release(); //If used: mMediaRecorder object cannot
        // be reused again
        destroyMediaProjection();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyMediaProjection();
        mRealm.close();
    }

    private void destroyMediaProjection() {
        if (mMediaProjection != null) {
            mMediaProjection.unregisterCallback(mMediaProjectionCallback);
            mMediaProjection.stop();
            mMediaProjection = null;
        }
        Log.i(TAG, "MediaProjection Stopped");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS: {
                if ((grantResults.length > 0) && (grantResults[0] +
                        grantResults[1]) == PackageManager.PERMISSION_GRANTED) {
                    onToggleScreenShare(mToggleButton);
                } else {
                    mToggleButton.setChecked(false);
                }
                return;
            }
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

                }
            }
            return true;
        }
    };
}

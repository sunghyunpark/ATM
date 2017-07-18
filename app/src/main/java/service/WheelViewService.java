package service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Binder;
import android.os.IBinder;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.lukedeighton.wheelview.WheelView;
import com.lukedeighton.wheelview.adapter.WheelAdapter;

import capture.ScreenCaptureActivity;
import capture.ScreenRecordActivity;
import nts.nt3.atm.ATMApplication;
import nts.nt3.atm.R;

/**
 * Created by NAVER on 2017-07-06.
 */

public class WheelViewService extends Service implements Wheelable{

    private static final int ITEM_COUNT = 8;                //wheel item 갯수
    private RelativeLayout wheel_layout;
    private TextView wheel_item_txt;
    //onTopService view
    private WindowManager mWindowManager;                   //윈도우 매니저
    ATMApplication atmApplication;


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        atmApplication = (ATMApplication)getApplicationContext();

        InitView();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 뷰 초기화
     */
    private void InitView(){
        LayoutInflater li = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        wheel_layout = (RelativeLayout)li.inflate(R.layout.wheel, null);
        wheel_item_txt = (TextView)wheel_layout.findViewById(R.id.item_title);
        WheelView wheelView = (WheelView)wheel_layout.findViewById(R.id.wheelview);
        wheelView.setOnWheelItemClickListener(wheelItemClickListener);    //wheel view 클릭 리스너
        wheelView.setOnWheelItemSelectedListener(wheelItemSelectListener);    //wheel view 선택되었을 때

        //최상위 윈도우에 넣기 위한 설정
        WindowManager.LayoutParams mParams = new WindowManager.LayoutParams(//layout params 객체. 뷰의 위치 및 크기를 지정하는 객체
                WindowManager.LayoutParams.WRAP_CONTENT,
                getViewHeight(getApplicationContext()),
                WindowManager.LayoutParams.TYPE_TOAST,					//항상 최 상위에 있게. status bar 밑에 있음. 터치 이벤트 받을 수 있음.
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,        //이 속성을 안주면 터치 & 키 이벤트도 먹게 된다.
                //포커스를 안줘서 자기 영역 밖터치는 인식 안하고 키이벤트를 사용하지 않게 설정
                PixelFormat.TRANSLUCENT);                                        //투명
        mParams.gravity = Gravity.BOTTOM;						//왼쪽 상단에 위치하게 함.
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);	//윈도우 매니저 불러옴.

        mWindowManager.addView(wheel_layout, mParams);		//최상위 윈도우에 뷰 넣기. *중요 : 여기에 permission을 미리 설정해 두어야 한다. 매니페스트에

        //WheelView Icon 적용
        wheelView.setAdapter(new WheelAdapter() {
            @Override
            public Drawable getDrawable(int position) {
                Drawable drawable = null;
                switch (position){
                    case 0:
                        drawable = getResources().getDrawable(R.mipmap.wheel_icon1);
                        break;
                    case 1:
                        drawable = getResources().getDrawable(R.mipmap.wheel_icon2);
                        break;
                    case 2:
                        drawable = getResources().getDrawable(R.mipmap.wheel_icon3);
                        break;
                    case 3:
                        drawable = getResources().getDrawable(R.mipmap.wheel_icon4);
                        break;
                    case 4:
                        drawable = getResources().getDrawable(R.mipmap.wheel_icon5);
                        break;
                    case 5:
                        drawable = getResources().getDrawable(R.mipmap.wheel_icon6);
                        break;
                    case 6:
                        drawable = getResources().getDrawable(R.mipmap.wheel_icon7);
                        break;
                    case 7:
                        drawable = getResources().getDrawable(R.mipmap.wheel_icon8);
                        break;
                }
                return drawable;
            }
            //아이템 갯수 -> xml에서는 보여지는 아이템의 갯수
            @Override
            public int getCount() {
                return ITEM_COUNT;
                //return the count
            }

        });
    }

    /**
     * Wheelable 인터페이스로부터 받아온 메소드 정의
     */
    //capture
    @Override
    public void startCapture(){
        stopService();
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    Intent intent = new Intent(getApplicationContext(), ScreenCaptureActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }catch (InterruptedException e){

                }
            }
        });
        th.start();
    }
    //record
    @Override
    public void startRecord(){
        atmApplication.setRecordState(true);
        stopService();
        Intent intent = new Intent(getApplicationContext(), ScreenRecordActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    //memo
    @Override
    public void startMemo(){
        Toast.makeText(getApplicationContext(), "startMemo", Toast.LENGTH_SHORT).show();
    }
    //logcat
    @Override
    public void startLogCat(){
        Toast.makeText(getApplicationContext(), "startLogCat", Toast.LENGTH_SHORT).show();
    }
    //manage app
    @Override
    public void startManageApp(){
        Toast.makeText(getApplicationContext(), "startManageApp", Toast.LENGTH_SHORT).show();
    }
    //go to home
    @Override
    public void startHome(){
        Toast.makeText(getApplicationContext(), "startHome", Toast.LENGTH_SHORT).show();
    }
    //auto
    @Override
    public void startAuto(){
        Toast.makeText(getApplicationContext(), "startAuto", Toast.LENGTH_SHORT).show();
    }
    //stop
    @Override
    public void stopService(){
        stopService(new Intent(getApplicationContext(), WheelViewService.class));

        //wheel_layout.setVisibility(View.GONE);
        NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, WheelViewService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(getApplicationContext())
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.app_icon)
                .setContentTitle("AndroidTestManager")
                .setContentText("ATM 보이기")
                .setOngoing(true)
                //.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setTicker("ATM 숨기기.");
        Notification notification = builder.build();
        notification.flags = notification.flags | notification.FLAG_FOREGROUND_SERVICE;
        nm.notify(1234, notification);
    }

    /**
     * wheel 크기를 단말기 높이의 5/1
     * @param context
     * @return
     */
    private int getViewHeight(Context context){
        int h;
        Display display;
        display = ((WindowManager)context.getSystemService(context.WINDOW_SERVICE)).getDefaultDisplay();
        h = display.getHeight();

        return h/5;
    }

    /**
     * Wheel view select listener
     */
    private WheelView.OnWheelItemSelectListener wheelItemSelectListener = new WheelView.OnWheelItemSelectListener() {
        @Override
        public void onWheelItemSelected(WheelView parent, Drawable itemDrawable, int position) {
            Resources res = getResources();
            switch (position){
                case 0:
                    wheel_item_txt.setText(res.getString(R.string.wheel_view_item1));
                    break;
                case 1:
                    wheel_item_txt.setText(res.getString(R.string.wheel_view_item2));
                    break;
                case 2:
                    wheel_item_txt.setText(res.getString(R.string.wheel_view_item3));
                    break;
                case 3:
                    wheel_item_txt.setText(res.getString(R.string.wheel_view_item4));
                    break;
                case 4:
                    wheel_item_txt.setText(res.getString(R.string.wheel_view_item5));
                    break;
                case 5:
                    wheel_item_txt.setText(res.getString(R.string.wheel_view_item6));
                    break;
                case 6:
                    wheel_item_txt.setText(res.getString(R.string.wheel_view_item7));
                    break;
                case 7:
                    wheel_item_txt.setText(res.getString(R.string.wheel_view_item8));
                    break;

            }
        }
    };
    /**
     * Wheel view click listener
     */
    private WheelView.OnWheelItemClickListener wheelItemClickListener = new WheelView.OnWheelItemClickListener(){
        @Override
        public void onWheelItemClick(WheelView parent, int position, boolean isSelected) {
            switch (position) {
                case 0:
                    startCapture();
                    break;
                case 1:
                    startRecord();
                    break;
                case 2:
                    startMemo();
                    break;
                case 3:
                    startLogCat();
                    break;
                case 4:
                    startManageApp();
                    break;
                case 5:
                    startHome();
                    break;
                case 6:
                    startAuto();
                    break;
                case 7:
                    stopService();
                    break;
            }
        }
    };
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if(mWindowManager != null) {		//서비스 종료시 뷰 제거. *중요 : 뷰를 꼭 제거 해야함.
            if(wheel_layout != null) mWindowManager.removeView(wheel_layout);
        }
    }


}

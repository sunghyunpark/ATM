package service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import nts.nt3.atm.R;

public class NdeployAlarmService extends Service {

    //웹사이트 업로드 시간
    private String be_time = "";
    private String af_time = "";
    public static boolean update_flag = true;
    //타이머
    private TimerTask mTask;
    private Timer mTimer;

    public NdeployAlarmService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate(){
        super.onCreate();

        if(mTimer != null){
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }
        if(mTask != null){
            mTask = null;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String url = intent.getExtras().getString("url");
        ParsingTimer(url);

        Resources res = getResources();
        //타이머를 계속 사용해야해서 노티피케이션을 등록해줌 -> 아이콘은 뺄수있음.
        Notification.Builder builder = new Notification.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.app_icon)
                .setContentTitle(res.getString(R.string.ndeploy_alarm_running_txt1))
                .setContentText(res.getString(R.string.ndeploy_alarm_running_txt2))
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        Notification notification = builder.build();
        notification.flags = notification.flags | notification.FLAG_AUTO_CANCEL;

        startForeground(1, notification);
        return START_STICKY;
    }

    /**
     * 웹사이트 파싱
     * @param url Ndeploy웹사이트 링크
     * @return
     * @throws IOException
     * @throws MalformedURLException
     */
    private String NdeployParsing(String url) throws IOException, MalformedURLException {
        String str = "";
        Source source = new Source(new URL(url));
        source.fullSequentialParse();

        List<Element> tabletags = source.getAllElements(HTMLElementName.DIV);
        //<div class="latest_version" style="display:block;padding-bottom: 10px;">
        //<div id="pagelist">
        for(int arrnum = 0;arrnum < tabletags.size(); arrnum++){ //DIV 모든 태그중 bbsContent 태그가 몇번째임을 구한다.
            if(tabletags.get(arrnum).getContent().getTextExtractor().toString().contains("Upload 일시 ")){
                int num1 = tabletags.get(arrnum).getContent().getTextExtractor().toString().indexOf("Upload 일시 ");
                str = tabletags.get(arrnum).getContent().getTextExtractor().toString().substring(num1+10,num1+10+19);
                break;
            }

        }
        Log.d("parsing", url);
        Log.d("parsing", str);
        return str;

    }

    private void setNotification(String url){
        Resources res = getResources();
        NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Uri uri = Uri.parse(url);
        Intent it  = new Intent(Intent.ACTION_VIEW,uri);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, it, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(getApplicationContext())
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.app_icon)
                .setContentTitle(res.getString(R.string.ndeploy_alarm_push_title))
                .setContentText(res.getString(R.string.ndeploy_alarm_push_txt1))
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setTicker(res.getString(R.string.ndeploy_alarm_push_txt2));
        Notification notification = builder.build();
        notification.flags = notification.flags | notification.FLAG_AUTO_CANCEL;
        nm.notify(1234, notification);
    }

    private void ParsingTimer(final String url){
        if(mTask == null){
            mTask = new TimerTask() {

                @Override
                public void run() {
                    try {
                        if(update_flag){
                            Log.d("ndeploy", "update_flag: true");
                            af_time = NdeployParsing(url);
                            if(be_time.equals("")){
                                //최초 be_time이 비어있는 경우
                                be_time = af_time;
                                Log.d("ndeploy", "before : "+be_time);
                                Log.d("ndeploy", "after : "+af_time);
                            }else if(!be_time.equals("") && !af_time.equals(be_time)){
                                //새로 파싱해온값과 그 전 값이 다른 경우 -> 알림 작동
                                Log.d("ndeploy", "before : "+be_time);
                                Log.d("ndeploy", "after : "+af_time);
                                setNotification(url);
                                be_time = af_time;
                            }else{
                                //값이 다르지 않은 경우
                                be_time = af_time;
                                Log.d("ndeploy", "before : "+be_time);
                                Log.d("ndeploy", "after : "+af_time);
                            }
                        }else{
                            //알리미 동작 x
                            Log.d("ndeploy", "update_flag: false");
                            try{
                                mTimer.cancel();
                                mTimer = null;
                                mTask = null;
                            }catch (NullPointerException n){
                                mTimer = null;
                            }
                        }
                    } catch (MalformedURLException m) {
                    } catch (IOException e) {
                    }
                }
            };
        }
        if((mTimer == null)){
            mTimer = new Timer();
            Log.d("ndeploy", "mTimer created");
            mTimer.schedule(mTask, 3000, 60000);
            Log.d("ndeploy", "schedule()");
        }

    }
}

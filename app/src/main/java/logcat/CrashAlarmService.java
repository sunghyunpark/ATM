package logcat;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import nts.nt3.atm.R;
import presenter.MailPresenter;

public class CrashAlarmService extends Service {
    private static final Executor executor = Executors.newCachedThreadPool();
    private LogcatProcess mLogcat;
    private String crashLogs;
    static final int CRASH_CAT = 1;

    public CrashAlarmService() {
    }

    private Handler mHandler = new Handler() {
        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CRASH_CAT:
                    Toast.makeText(getApplicationContext(), "Crash", Toast.LENGTH_SHORT).show();
                    final List<String> lines = (List<String>) msg.obj;
                    crashLogs = "";
                    for (int i=0;i<lines.size();i++) {
                        crashLogs += lines.get(i);
                    }
                    SaveAndSendTask saveAndSendTask = new SaveAndSendTask();
                    saveAndSendTask.execute("");
                    break;
            }
        }
    };

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(mLogcat != null)
        mLogcat.stop();
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Init();

        Notification.Builder builder = new Notification.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.crashalarm_img)
                .setContentTitle("Crash 알리미")
                .setContentText("로그 분석중")
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        Notification notification = builder.build();
        notification.flags = notification.flags | notification.FLAG_AUTO_CANCEL;

        startForeground(1, notification);
        return START_STICKY;
    }

    private void Init(){
        if (mLogcat != null) {
            mLogcat.stop();
        }

        executor.execute(new Runnable() {
            @Override
            public void run() {
                mLogcat = new LogcatProcess(CrashAlarmService.this, mHandler, true);
                mLogcat.start();
            }
        });
    }

    private class SaveAndSendTask extends AsyncTask<String, String, String> {
        File path;
        String fileName;
        File file;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            path = new File(Environment.getExternalStorageDirectory(),"ATM");
            fileName = "ATM_Logcat_" + LogcatMain.LogDateFormat.format(new Date()) + ".txt";
            file = new File(path.getPath() + File.separator + fileName);
        }

        @Override
        protected String doInBackground(String... data){

            if (!path.exists()) {
                path.mkdir();
            }
            BufferedWriter bw = null;
            try {
                file.createNewFile();
                bw = new BufferedWriter(new FileWriter(file), 1024);
                bw.write(crashLogs);
            } catch (IOException e) {
                Log.e("ATM_Logcat_", "로그 저장 실패.", e);
            } finally {
                if (bw != null) {
                    try {
                        bw.close();
                    } catch (IOException e) {
                        Log.e("ATM_Logcat_", "Writer 닫기 실패.", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(String result){
            MailPresenter mailPresenter = new MailPresenter(getApplicationContext());
            mailPresenter.SendBtn("logcat", Environment.getExternalStorageDirectory()+"/ATM/"+ fileName);
            Toast.makeText(getApplicationContext(), "메일 전송 완료", Toast.LENGTH_SHORT).show();
            crashLogs = "";
        }
    }
}

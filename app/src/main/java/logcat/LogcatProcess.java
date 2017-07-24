package logcat;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class LogcatProcess {
	private static final long CAT_DELAY = 1;

	private Context mContext;
	private Handler mHandler;
	private Process mlogcatProc;
	private ScheduledExecutorService mExecutor;
	private String mFilter = null;
	private BufferedReader mReader = null;
	private ArrayList<String> mLogCache = new ArrayList<String>();
	private boolean mRunning = false;
	private boolean mIsFilterPattern;
	private boolean mPlay = true;
	private long mlastCat = -1;

	private Preferences mPrefs = null;
	private Pattern mFilterPattern = null;
	private LogLevel mLevel = null;
	private LogType mType = null;

	PatternFormat mFormat = null;

	//�α� ����
	private Runnable catRunner = new Runnable() {

		@Override
		public void run() {
			if (!mPlay) {
				return;
			}
			long now = System.currentTimeMillis();
			if (now < mlastCat + CAT_DELAY) {
				return;
			}
			mlastCat = now;
			cat();
		}
	};

	public LogcatProcess(Context context, Handler handler) {
		mHandler = handler;
		
		mContext = context;
		mPrefs = new Preferences(mContext);

		mLevel = mPrefs.getLevel();
		mIsFilterPattern = mPrefs.isFilterPattern();
		mFilter = mPrefs.getFilter();
		mFilterPattern = mPrefs.getFilterPattern();
		mFormat = mPrefs.getFormat();
		mType = mPrefs.getType();
	}

	//�α�Ĺ ����
	public void start() {
		stop();

		mRunning = true;

		mExecutor = Executors.newScheduledThreadPool(1);
		mExecutor.scheduleAtFixedRate(catRunner, CAT_DELAY, CAT_DELAY, TimeUnit.SECONDS);

		try {
			Message m = Message.obtain(mHandler, LogcatMain.CLEAR_EVT);
			mHandler.sendMessage(m);

			List<String> progs = new ArrayList<String>();

			progs.add("logcat");
			progs.add("-v");
			progs.add(mFormat.getValue());
			//logcat -v brief = ����/�±�/PID ��Ŀ��(�⺻ ����)
			//logcat -v process = PID�� ���
			//logcat -v tag = ����/�±׸� ���
			//logcat -v raw = raw �α� �޽��� ���. �ٸ� ��Ÿ������ �ʵ�� �������� �ʴ´�.
			//logcat -v time = ���μ����� ���� ��¥, ���೯¥, ����/�±�/PID ����.
			//logcat -v thread = ����/�±�/PID/TID ����
			//logcat -v threadtime = ��¥, ���೯¥, ����/�±�/PID/TID ����
			//logcat -v long = ��� ��Ÿ������ ����.
			if (mType != LogType.MAIN) {
				progs.add("-b");
				progs.add(mType.getValue());
			}
			progs.add("*:" + mLevel);

			//mlogcatProc = new ProcessBuilder(progs).command("su").redirectErrorStream(true).start();
			mlogcatProc = Runtime.getRuntime().exec(progs.toArray(new String[0]));
			mReader = new BufferedReader(new InputStreamReader(mlogcatProc.getInputStream()), 1024);

			String line;
			while (mRunning && (line = mReader.readLine()) != null) {
				if (!mRunning) {
					break;
				}
				if (line.length() == 0) {
					continue;
				}
				if (mIsFilterPattern) {
					if (mFilterPattern != null && !mFilterPattern.matcher(line).find()) {
						continue;
					}
				} else {
					if (mFilter != null && !line.toLowerCase().contains(mFilter.toLowerCase())) {
						continue;
					}
				}
				synchronized (mLogCache) {
					mLogCache.add(line);
				}
			}
		} catch (IOException e) {
			//Log.e("start()", "�α� �б� ����", e);
			return;
		} finally {

			if (mlogcatProc != null) {
				mlogcatProc.destroy();
				mlogcatProc = null;
			}
			if (mReader != null) {
				try {
					mReader.close();
					mReader = null;
				} catch (IOException e) {
					//Log.d("start()", "��Ʈ�� �ݱ� ����", e);
				}
			}
		}
	}

	//�α� �޽��� �ڵ鷯�� ����.
	private void cat() {
		Message msg;

		if (mLogCache.size() > 0) {
			synchronized (mLogCache) {
				if (mLogCache.size() > 0) {
					msg = Message.obtain(mHandler, LogcatMain.CAT_EVT);
					msg.obj = mLogCache.clone();
					mLogCache.clear();
					mHandler.sendMessage(msg);					
				}
			}
		}
	}

	public void stop() {
		mRunning = false;

		if (mExecutor != null && !mExecutor.isShutdown()) {
			mExecutor.shutdown();
			mExecutor = null;
		}
	}

	public boolean isRunning() {
		return mRunning;
	}

	public boolean isPlay() {
		return mPlay;
	}

	public void setPlay(boolean play) {
		mPlay = play;
	}
}
package logcat;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum PatternFormat {
	BRIEF("brief", "Brief", Pattern.compile("^([VDIWEF])/")),
	PROCESS("process", "Process", Pattern.compile("^([VDIWEF])\\(")),
	TAG("tag", "Tag", Pattern.compile("^([VDIWEF])/")),
	THREAD("thread", "Thread", Pattern.compile("^([VDIWEF])\\(")),
	TIME("time", "Time", Pattern.compile(" ([VDIWEF])/")),
	THREADTIME("threadtime", "ThreadTime", Pattern.compile(" ([VDIWEF]) ")),
	LONG("long", "Long", Pattern.compile("([VDIWEF])/")),
	RAW("raw", "Raw", null);

	//brief = ����/�±�/PID ��Ŀ��(�⺻ ����)
	//process = PID�� ���
	//tag = ����/�±׸� ���
	//raw = raw �α� �޽��� ���. �ٸ� ��Ÿ������ �ʵ�� �������� �ʴ´�.
	//time = ���μ����� ���� ��¥, ���೯¥, ����/�±�/PID ����.
	//thread = ����/�±�/PID/TID ����
	//threadtime = ��¥, ���೯¥, ����/�±�/PID/TID ����
	//long = ��� ��Ÿ������ ����.

	private static PatternFormat[] sorted = {BRIEF,PROCESS,TAG,THREAD,TIME,THREADTIME,LONG,RAW};
	private String mValue;
	private String mTitle;
	private Pattern mLevelPattern;
	private static final HashMap<String,PatternFormat> values = new HashMap<String,PatternFormat>();
	static {
		values.put(BRIEF.mValue, BRIEF); 
		values.put(PROCESS.mValue, PROCESS); 
		values.put(TAG.mValue, TAG); 
		values.put(THREAD.mValue, THREAD); 
		values.put(THREADTIME.mValue, THREAD); 
		values.put(TIME.mValue, TIME); 
		values.put(RAW.mValue, RAW); 
		values.put(LONG.mValue, LONG); 
	}

	private PatternFormat(String value, String title, Pattern levelPattern) {
		mValue = value;
		mTitle = title;
		mLevelPattern = levelPattern;
	}

	public String getTitle() {
		return mTitle;
	}	

	public static final PatternFormat byValue(String value) {
		return values.get(value);
	}

	public LogLevel getLevel(String line) {
		if (mLevelPattern == null) {
			return null;
		}
		Matcher m = mLevelPattern.matcher(line);
		if (m.find()) {
			return LogLevel.valueOf(m.group(1));
		}
		return null;
	}

	public static PatternFormat getOriginal(int what) {
		return sorted[what];
	}

	public String getValue() {
		return mValue;
	}
}
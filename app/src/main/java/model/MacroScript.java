package model;

/**
 * Created by NAVER on 2017-08-02.
 * Macro Script SingleTon
 * 사용법
 * 1. cmd -> /android-sdks/platform-tools에서 adb.exe shell 입력
 * 2. monkey -p com.campmobile.snow -v -v -f /storage/sdcard0/Android/data/a.txt 5
 */

public class MacroScript {
    private static volatile  MacroScript macroScript = null;
    private String script =  "type= user\n" +
            "speed= 1000\n" +
            "start data >>\n";

    public static  MacroScript getInstance(){
        if(macroScript == null)
            synchronized (MacroScript.class){
                if(macroScript==null){
                    macroScript = new MacroScript();
                }
            }

        return macroScript;
    }
    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script += script;
    }

    public void Init(){
        this.script = "type= user\n" +
                "speed= 1000\n" +
                "start data >>\n";
    }
}

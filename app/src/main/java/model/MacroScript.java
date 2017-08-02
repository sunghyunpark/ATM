package model;

/**
 * Created by NAVER on 2017-08-02.
 * Macro Script SingleTon
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

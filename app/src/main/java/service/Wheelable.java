package service;

/**
 * Created by NAVER on 2017-07-06.
 * Wheel View Interface 부분
 */

public interface Wheelable {
    void startCapture();    // 캡쳐
    void startRecord();    // 녹화
    void startMemo();    // 메모
    void startLogCat();    // 로그캣
    void startManageApp();    // 앱 관리
    void startHome();    // 홈 화면
    void startMacro();    // 매크로
    void startPerformance();    //성능측정
    void stopService();    // 숨기기
}

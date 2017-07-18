package capture;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by MacBookPro13 on 2017. 7. 12..
 */

public class DrawView extends View {
    //필요한 멤버필드 정의하기
    ArrayList<Point> list;

    //상태값을 상수로 정의
    static final int RED_STATE=0;
    static final int BLUE_STATE=1;
    static final int YELLOW_STATE=2;
    //현재 색의 상태값을 저장할 변수
    int colorState = RED_STATE;
    //Paint 객체를 저장할 배열 객체 생성하기
    Paint[] paintList = new Paint[3];
    public DrawView(Context context) {
        super(context);
        init();//초기화
    }
    //xml에 view를 추가시 인자2개짜리 생성자가 필요하다!!!!!!!!!!!
    public DrawView(Context context, AttributeSet attrs){
        super(context, attrs);
        init();//초기화
    }

    //초기화 하는 메소드
    public void init(){
        //arraylist 객체 생성하기
        list=new ArrayList<Point>();
        //선을 그림 Paint 객체 생성 및 초기화
        Paint  redPaint = new Paint();
        redPaint.setColor(Color.RED);
        redPaint.setStrokeWidth(5); //선의 굵기
        redPaint.setAntiAlias(true);//경계면을 부드럽게 처리하기

        Paint  bluePaint = new Paint();
        bluePaint.setColor(Color.BLUE);
        bluePaint.setStrokeWidth(5); //선의 굵기
        bluePaint.setAntiAlias(true);//경계면을 부드럽게 처리하기

        Paint  yellowPaint = new Paint();
        yellowPaint.setColor(Color.YELLOW);
        yellowPaint.setStrokeWidth(5); //선의 굵기
        yellowPaint.setAntiAlias(true);//경계면을 부드럽게 처리하기
        //페인트 객체를 배열에 저장한다.
        paintList[0]=redPaint;
        paintList[1]=bluePaint;
        paintList[2]=yellowPaint;

    }

    //화면을 그리는 메소드
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);

        //배열에 저장된 Point 객체의 좌표값을 이용해서 선을 그린다.
        int n = list.size();
        for(int i=0 ;i < n; i++){
            //배열에서 i번째 인덱스에 있는 Point 객체를 얻어온다.
            Point p = list.get(i);
            if(!(p.isStart)){ //해당 인텍스의 isStart 값이 true가 아니라면
                //시작점이 아니므로 바로 전 point 객체와 연결한다.
                canvas.drawLine(list.get(i-1).x, //시작점x
                        list.get(i-1).y, //시작점y
                        list.get(i).x, //도착점x
                        list.get(i).y, //도착점y
                        paintList[list.get(i).colorState]); //선의 속성을 가지고 있는 paint객체

            }
        }

    }

    //터치 입력을 받기 위해서
    @Override
    public boolean onTouchEvent(MotionEvent event) {//event에 터치점 좌표가 들어온다.
        //이벤트가 일어난 곳의 좌표 얻어오기
        int eventX=(int)event.getX();
        int eventY=(int)event.getY();
        //이벤트의 종류에 따라서 다른 동작을 하게 한다.
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN :
                EditCaptureActivity.title_bar.setVisibility(GONE);       //그리는 동안 타이틀바는 숨김
                //액션 다운 이벤트가 발생하면 라인의 시작점이 된다.
                Point p = new Point(eventX, eventY, true, colorState);
                //생성한 객체를 배열에 담는다.
                list.add(p);
                break;
            case MotionEvent.ACTION_MOVE :
                EditCaptureActivity.title_bar.setVisibility(GONE);       //그리는 동안 타이틀바는 숨김
                //액션 무브 이벤트가 발생하면 시작점이 아니다 isStart=false
                Point p2 = new Point(eventX, eventY, false, colorState);
                list.add(p2);
                //화면 갱신하기
                invalidate();
                break;
            case MotionEvent.ACTION_UP :
                EditCaptureActivity.title_bar.setVisibility(VISIBLE);        //그리는 동안 타이틀바는 숨김
                break;
        }


        return true;
    }

}

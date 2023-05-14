package com.dev.textnet;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Dell on 2018/01/13.
 */

public class OnSlidingTouchListener implements View.OnTouchListener {
    private final GestureDetector gestureDetector;

    public OnSlidingTouchListener(Context context){
        gestureDetector=new GestureDetector(context,new GestureListener());
    }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return gestureDetector.onTouchEvent(motionEvent);
    }
    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SLIDE_THRESHOLD=100;

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            try {
                float deltaY=e2.getY()-e1.getY();
                float deltaX=e2.getX()-e1.getX();
                if (Math.abs(deltaX)>Math.abs(deltaY)){
                    if (Math.abs(deltaX)>SLIDE_THRESHOLD){
                        if (deltaX>0){

                        }else{

                        }
                    }
                }else {
                    if (Math.abs(deltaY)>SLIDE_THRESHOLD){
                        if (deltaY>0){
                            return onSlideDown();
                        }else{

                        }
                    }
                }
            }catch (Exception e){
            }
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    }

    public boolean onSlideDown() {
        return false;
    }
}

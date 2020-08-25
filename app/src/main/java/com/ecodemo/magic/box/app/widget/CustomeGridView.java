package com.ecodemo.magic.box.app.widget;
import android.content.Context;
import android.widget.GridView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomeGridView extends GridView {

    public CustomeGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_MOVE){
            return true;//true:禁止滚动
        }

        return super.dispatchTouchEvent(ev);
    }
	
	

    @Override  
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  

        int expandSpec = MeasureSpec.makeMeasureSpec(  
			Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);  
        super.onMeasure(widthMeasureSpec, expandSpec);
    }  
}

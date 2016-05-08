package view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ImageView;

public class BigImageView extends ImageView {
    private SizeCallBack callBack;
    private Handler handle;
    private Runnable cbkAction;
    private int width;
    private int height;

    public BigImageView(Context context) {
        super(context);
        cbkAction = () -> {
            if(callBack != null)
                callBack.onSizeChanged(width, height);
        };
    }

    public BigImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        cbkAction = () -> {
            if(callBack != null)
                callBack.onSizeChanged(width, height);
        };
    }

    public BigImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        cbkAction = () -> {
            if(callBack != null)
                callBack.onSizeChanged(width, height);
        };
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH){
        super.onSizeChanged(w, h, oldW, oldH);
        width = w;
        height = h;
        if(handle != null)
            handle.post(cbkAction);
    }

    public void setCallBack(SizeCallBack cbk){
        callBack = cbk;
    }

    public void setHandle(Handler h){
        handle = h;
    }
}

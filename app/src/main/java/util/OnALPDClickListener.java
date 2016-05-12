package util;

import android.view.View;

/**
 * onClickListeners
 * Created by Administrator on 2016/5/12 0012.
 */
public interface OnALPDClickListener {
	void onClick(View view, int position, int id);
	boolean onLongClick(View view, int position, int id);
	boolean onTouch(View view, int position, int id);
}

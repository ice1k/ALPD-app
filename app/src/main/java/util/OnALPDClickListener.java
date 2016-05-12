package util;

import android.view.View;

/**
 * Created by Administrator on 2016/5/12 0012.
 */
public interface OnALPDClickListener {
	void onClick(View view);

	boolean onLongClick(View view);

	boolean onTouch(View view);
}

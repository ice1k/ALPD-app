package util;

import android.os.Handler;
import android.os.Message;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by asus1 on 2016/4/3.
 */
public class Tools {

	public static boolean exists(final String URLName) {
		final boolean[] bool = new boolean[1];
		final Handler handler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
					case 1:
						bool[0] = true;
						return true;
					default:
						bool[0] = false;
						return false;
				}
			}
		});
		new Thread() {
			@Override
			public void run() {
				Message message = new Message();
				try {
					//设置此类是否应该自动执行 HTTP 重定向（响应代码为 3xx 的请求）。
					HttpURLConnection.setFollowRedirects(false);
					//到 URL 所引用的远程对象的连接
					HttpURLConnection con = (HttpURLConnection) new URL(URLName)
							.openConnection();
		   /* 设置 URL 请求的方法， GET POST HEAD OPTIONS PUT DELETE TRACE 以上方法之一是合法的，具体取决于协议的限制。*/
					con.setRequestMethod("HEAD");
					message.what = (con.getResponseCode() == HttpURLConnection.HTTP_OK) ? 1 : 0;
				} catch (Exception e) {
					e.printStackTrace();
					message.what = 0;
				}
				handler.sendMessage(message);
			}
		}.start();
		return bool[0];
	}

	public static int toInt(byte[] bRefArr) {
		int iOutcome = 0;
		byte bLoop;

		for (int i = 0; i < bRefArr.length; i++) {
			bLoop = bRefArr[i];
			iOutcome += (bLoop & 0xFF) << (8 * i);
		}
		return iOutcome;
	}
}

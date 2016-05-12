package util;

import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import data.DownloadData;
import data.Poster;

/**
 * Created by asus1 on 2016/4/10.
 * provides download utils
 */
public abstract class DownloadingActivity extends BaseActivity {

	protected Handler handler;
	protected static boolean haveNew = true;

	protected final static String SAVE_PATH
			= Environment.getExternalStorageDirectory() + "/apld-download/";
	protected int size;
	public static final int IMAGE_GET = 0x000;
	public static final int IMAGE_SAVE = 0x001;
	public static final int IMAGE_ALL_GET = 0x002;
	public static final String NUMBER = "NUMBER";
	public static final String BIG_URL =
			"https://coding.net/u/ice1000/p/App-raw/git/raw/master/alpd_pics/%d.png";
	public static final String MAIN_URL =
			"https://coding.net/u/ice1000/p/App-raw/git/raw/master/alpd_small/%d.png";
	//            "https://raw.githubusercontent.com/ice1000/App-raw/master/alpd_small/%d.png";
	public static final String COUNT_URL = "";
//            "https://raw.githubusercontent.com/ice1000/App-raw/master/size";

	protected void getImage(final int i, final boolean isSmall) {
		new Thread() {
			@Override
			public void run() {
				byte[] data = new byte[0];
				try {
					data = ImageService.getImage(String.format(
							Locale.CHINESE,
							isSmall ? MAIN_URL : BIG_URL,
							i)
					);
				} catch (IOException ignored) {
				}
				if (data.length > 100) {
					Message message = new Message();
					message.what = IMAGE_GET;
					message.obj = new Poster(data, i);
					handler.sendMessage(message);
				} else {
					haveNew = false;
				}
			}
		}.start();
	}

	protected void initFuncs() {
		handler = new Handler(msg -> {
			switch (msg.what) {
				case IMAGE_GET:
					Poster poster = ((Poster) (msg.obj));
					addView(poster);
					break;
				case IMAGE_SAVE:
					DownloadData data = (DownloadData) msg.obj;
					data.dialog.dismiss();
					v(data.msg);
					toast(data.msg);
			}
			return true;
		});
	}

	protected abstract void addView(Poster poster);

	protected void saveFile(Bitmap bm, String fileName) throws IOException {
		File dirFile = new File(SAVE_PATH);
		if (!dirFile.exists())
			v("dirFile.mkdir() = " + dirFile.mkdir());
		File myCaptureFile = new File(SAVE_PATH + fileName);
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
		bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
		bos.flush();
		bos.close();
	}
}

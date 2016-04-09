package util;

import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.util.Locale;

import data.Poster;

/**
 * Created by asus1 on 2016/4/10.
 * provides download utils
 */
public abstract class DownloadingActivity extends BaseActivity {

    protected Handler handler;
    protected static boolean haveNew = true;

    protected int size;
    public static final int IMAGE_GET = 0x000;
    public static final String NUMBER = "NUMBER";
    public static final String BIG_URL =
            "https://coding.net/u/ice1000/p/App-raw/git/raw/master/alpd_pics/%d.png";
    public static final String MAIN_URL =
            "https://coding.net/u/ice1000/p/App-raw/git/raw/master/alpd_small/%d.png";
    //            "https://raw.githubusercontent.com/ice1000/App-raw/master/alpd_small/%d.png";
    public static final String COUNT_URL = "";
//            "https://raw.githubusercontent.com/ice1000/App-raw/master/size";

    protected void getImage(final int i, final boolean isSmall) {
        new Thread(){
            @Override
            public void run() {
                byte[] data = new byte[0];
                try {
                    data = ImageService.getImage(String.format(
                            Locale.CHINESE,
                            isSmall ? MAIN_URL : BIG_URL,
                            i)
                    );
                } catch (IOException ignored) {}
                if(data.length > 100) {
                    Message message = new Message();
                    message.what = IMAGE_GET;
                    message.obj = new Poster(data, i);
                    handler.sendMessage(message);
                }
                else {
                    haveNew = false;
                }
            }
        }.start();
    }

    protected void initFuncs() {
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what){
                    case IMAGE_GET:
                        Poster poster = ((Poster)(msg.obj));
                        addView(poster);
                        break;
                }
                return true;
            }
        });
    }

    protected abstract void addView(Poster poster);
}

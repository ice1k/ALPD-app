package data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by asus1 on 2016/4/3.
 * data class
 */
public class Poster {
    public byte[] bytes;
    public int cnt;

    public Poster(byte[] bytes, int cnt) {
        this.bytes = bytes;
        this.cnt = cnt;
    }

    public Bitmap getBitMap() {
        return BitmapFactory.decodeByteArray(
                bytes, 0,
                bytes.length
        );
    }
}

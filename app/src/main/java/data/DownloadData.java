package data;

import android.app.Dialog;

/**
 * Created by asus1 on 2016/4/10.
 * Data class
 */
public class DownloadData {
    public String msg;
    public Dialog dialog;

    public DownloadData(String msg, Dialog dialog) {
        this.msg = msg;
        this.dialog = dialog;
    }

    public DownloadData(){

    }
}

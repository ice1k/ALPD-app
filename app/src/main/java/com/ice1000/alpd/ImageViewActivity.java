package com.ice1000.alpd;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.text.format.Time;
import android.view.MenuItem;
import android.widget.Button;

import java.io.IOException;

import data.DownloadData;
import data.Poster;
import util.DownloadingActivity;
import view.ZoomImageView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ImageViewActivity extends DownloadingActivity {

    private ZoomImageView zoomImageView;
    private Poster poster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        toast("refreshing");
        initViews();
        initFuncs();
    }

    private void initViews() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        zoomImageView = (ZoomImageView) findViewById(R.id.fullscreen_content);

        Button download = (Button) findViewById(R.id.dummy_button);
        if (download != null) {
            download.setOnClickListener(v -> {
                final Dialog dialog = ProgressDialog.show(
                        ImageViewActivity.this,
                        getString(R.string.save_pic),
                        getString(R.string.saving_pic),
                        true
                );
                new Thread(() -> {
                    Message message = new Message();
                    DownloadData data = new DownloadData();
                    data.dialog = dialog;
                    try {
                        zoomImageView.buildDrawingCache();
                        Time time = new Time("GMT+8");
                        time.setToNow();
                        saveFile(
                                poster.getBitMap(),
                                String.format(
                                        "%s.png",
                                        time.toString()
                                )
                        );
                        data.msg = getString(R.string.succeeded_to_save);
                    } catch (IOException e) {
                        data.msg = getString(R.string.failed_to_save);
                        e.printStackTrace();
                    }
                    message.obj = data;
                    message.what = IMAGE_SAVE;
                    handler.sendMessage(message);
                }).start();
            });
        }
        getImage(getIntent().getIntExtra(
                NUMBER,
                1
        ), false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button.
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void addView(Poster poster) {
        zoomImageView.setImage(poster.getBitMap());
        this.poster = poster;
    }
}

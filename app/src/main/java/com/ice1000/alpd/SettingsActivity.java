package com.ice1000.alpd;

import android.os.Bundle;
import android.app.ActionBar;
import android.view.Menu;
import android.view.View;

import util.BaseActivity;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ActionBar actionBar = getActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    public void aboutAuthor(View view) {
        go2Activity(AboutMeActivity.class, false);
    }

    public void joinUs(View view){
        toast("暂未开放");
    }
}

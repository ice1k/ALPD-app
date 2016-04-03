package com.ice1000.alpd;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.Locale;

import data.Poster;
import util.ImageService;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Handler handler;
    private LinearLayout images;
    private int size;
    public static final int IMAGE_GET = 0x000;
    private static boolean haveNew = true;
    public static final String MAIN_URL =
            "https://raw.githubusercontent.com/ice1000/App-raw/master/alpd_small/%d.png";
    public static final String COUNT_URL =
            "https://raw.githubusercontent.com/ice1000/App-raw/master/size";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initFuncs();
        v("init finished.");
        size = 200;
        refresh();
    }

    private void initFuncs() {
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what){
                    case IMAGE_GET:
                        Poster poster = ((Poster)(msg.obj));
                        byte[] data = poster.bytes;
                        LinearLayout linearLayout = (LinearLayout)
                                LayoutInflater.from(MainActivity.this).inflate(
                                        R.layout.image_meta,
                                        null
                                );
                        TextView textView = (TextView)
                                linearLayout.findViewById(R.id.text_source);
                        ImageView imageView = (ImageView)
                                linearLayout.findViewById(R.id.image_source);
                        imageView.setImageBitmap(
                                BitmapFactory.decodeByteArray(
                                        data,
                                        0,
                                        data.length
                                )
                        );
                        textView.setText(R.string.default_info);
//                        textView.setText("编号：" + poster.cnt);
                        images.addView(linearLayout);
                        break;
                }
                return true;
            }
        });
    }

    private void initViews(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        images = (LinearLayout) findViewById(R.id.images);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert drawer != null;
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_upload:
                toast("上传功能未开放");
                break;
            case R.id.nav_downloads:

                break;
            case R.id.nav_settings:
                go2Settings();
                break;
            case R.id.nav_share:

                break;
            default:

                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                go2Settings();
                return true;
            case R.id.action_refresh:
                haveNew = true;
                refresh();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private void getImage(final int i) {
        new Thread(){
            @Override
            public void run() {
                byte[] data = new byte[0];
                try {
                    data = ImageService.getImage(String.format(Locale.CHINESE, MAIN_URL, i));
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

    private void refresh(){
        toast("刷新中。。。");
        images.removeAllViews();
        v("size = " + size);
        for(int i = 1; i < size && haveNew; i++){
//        while(Tools.exists(String.format(Locale.CHINESE, MAIN_URL, i))){
            v("downloading " + i);
            getImage(i);
        }
    }
}

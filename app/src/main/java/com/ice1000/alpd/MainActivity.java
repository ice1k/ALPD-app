package com.ice1000.alpd;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import data.Poster;
import util.DownloadingActivity;
import util.OnALPDClickListener;

public class MainActivity extends DownloadingActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<Poster> posters;
    private RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initFuncs();
        v("init finished.");
        posters = new ArrayList<>();
        size = 100;
        refresh();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        if (recycler != null) {
            ALPDAdapter alpdAdapter = new ALPDAdapter();
            alpdAdapter.setOnALPDClickListener(
                    new OnALPDClickListener() {
                        @Override
                        public void onClick(View view, int position, int id) {
                            Intent intent = new Intent(
                                    MainActivity.this,
                                    ImageViewActivity.class
                            );
                            intent.putExtra(NUMBER, id);
                            startActivity(intent);
                        }

                        @Override
                        public boolean onLongClick(View view, int position, int id) {
                            return false;
                        }

                        @Override
                        public boolean onTouch(View view, int position, int id) {
                            return false;
                        }
                    }

            );
            recycler.setAdapter(alpdAdapter);
            recycler.setItemAnimator(new DefaultItemAnimator());
            recycler.setLayoutManager(new LinearLayoutManager(this));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        if (drawer != null)
            drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null)
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
                toast("this function is still in developing!");
                break;
            case R.id.nav_downloads:
                Intent intent = new Intent(
                        Intent.ACTION_VIEW
                );
                intent.setDataAndType(
                        Uri.fromFile(new File(SAVE_PATH)),
                        "image/*"
                );
                startActivity(intent);
                break;
            case R.id.nav_share:
                toast("this function is still in developing");
                break;
            case R.id.nav_view_source:
                openWeb("https://github.com/ice1000/ALPD-app");
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
                go2Settings(false);
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

    private void refresh() {
        toast("refreshing");
        recycler.removeViews(0, recycler.getChildCount());
        v("size = " + size);
        for (int i = 1; i < size && haveNew; i++) {
            v("downloading " + i);
            getImage(i, true);
        }

    }

    @Override
    protected void addView(final Poster poster) {
        posters.add(poster);
    }

    private class ALPDAdapter extends RecyclerView.Adapter {

        private OnALPDClickListener onALPDClickListener;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ALPDViewHolder(
                    LayoutInflater.from(
                            MainActivity.this).inflate(
                            R.layout.image_meta,
                            parent,
                            false
                    ));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((ALPDViewHolder) holder).init(posters.get(position));
            if (onALPDClickListener != null) {
                holder.itemView.setOnClickListener(
                        view ->
                                onALPDClickListener.onClick(
                                        view,
                                        position,
                                        ((ALPDViewHolder) holder).getID()
                                ));
                holder.itemView.setOnTouchListener(
                        (view, motionEvent) ->
                                onALPDClickListener.onTouch(
                                        view,
                                        position,
                                        ((ALPDViewHolder) holder).getID()
                                ));
                holder.itemView.setOnLongClickListener(
                        view ->
                                onALPDClickListener.onLongClick(
                                        view,
                                        position,
                                        ((ALPDViewHolder) holder).getID()
                                ));
            }
        }

        @Override
        public int getItemCount() {
            return posters.size();
        }

        public void setOnALPDClickListener(OnALPDClickListener onALPDClickListener) {
            this.onALPDClickListener = onALPDClickListener;
        }
    }

    private class ALPDViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView;
        private int ID;

        public ALPDViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image_source);
            textView = (TextView) itemView.findViewById(R.id.text_source);
        }

        public void init(Poster poster) {
            ID = poster.cnt;
            imageView.setImageBitmap(
                    BitmapFactory.decodeByteArray(
                            poster.bytes,
                            0,
                            poster.bytes.length
                    )
            );
            textView.setText(R.string.default_info);
            textView.append("\nposter No." + ID);
        }

        public int getID() {
            return ID;
        }
    }
}

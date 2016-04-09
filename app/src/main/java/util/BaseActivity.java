package util;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ice1000.alpd.R;
import com.ice1000.alpd.SettingsActivity;

/**
 * Created by asus1 on 2016/4/3.
 * BaseActivity.
 */
public class BaseActivity extends AppCompatActivity {

    protected void toast(String txt){
        Toast.makeText(this, txt, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                go2Settings();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void go2Activity(Class<?> clazz){startActivity(new Intent(this, clazz));}

    protected void go2Settings(){go2Activity(SettingsActivity.class);}

    protected void v(String log){
        Log.v(this.toString(), log);
    }
}

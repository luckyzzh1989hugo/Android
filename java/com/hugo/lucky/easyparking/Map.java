package com.hugo.lucky.easyparking;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.MenuItem;

public class Map extends AppCompatActivity {

    Toolbar actionBar;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        actionBar = (Toolbar) findViewById(R.id.toolbar);
        actionBar.setBackgroundColor(ActivityCompat.getColor(this, R.color.task1_background_color));
        actionBar.inflateMenu(R.menu.map_activity_actionbar_menu);
        actionBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id=item.getItemId();
                switch(id)
                {
                    case R.id.goto_viewpager:
                        intent=new Intent(getApplicationContext(),ParkingLotViewPager.class);
                        startActivity(intent);
                        break;
                    case R.id.goto_recycler_view:
                        intent=new Intent(getApplicationContext(),RecyclerViewActivity.class);
                        startActivity(intent);
                        break;
                }

                return false;
            }
        });
        if(savedInstanceState==null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, new MapFragment().newInstance())
                    .commit();
        }
        setupWindowAnimations();
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupWindowAnimations() {
        Explode explode = new Explode();
        explode.setDuration(1000);
        getWindow().setExitTransition(explode);
    }
}

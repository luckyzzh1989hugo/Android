package com.hugo.lucky.easyparking;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class RecyclerViewActivity extends AppCompatActivity implements Fragment_List.OnlistItemSelecteListener{

    private LruCache<String,Bitmap> mImgMemoryCache;
    ParkingLotDetail parkingLotDetail;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        if(mImgMemoryCache==null)
        {
            final int maxMemory=(int) (Runtime.getRuntime().maxMemory()/1024);
            final  int cacheSize=maxMemory/8;
            mImgMemoryCache=new LruCache<String,Bitmap>(cacheSize){
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return  value.getByteCount()/1024;
                }
            };
        }
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MyRecyclerView.newInstance(mImgMemoryCache)).commit();
        }

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.recyclerview_activity_actionbar_menu);
        toolbar.setBackgroundColor(ActivityCompat.getColor(this, R.color.task1_background_color));
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id=item.getItemId();
                switch(id)
                {
                    case R.id.goto_viewpager:
                        intent=new Intent(getApplicationContext(),ParkingLotViewPager.class);
                        startActivity(intent);
                        break;
                    case R.id.goto_map:
                        intent=new Intent(getApplicationContext(),Map.class);
                        startActivity(intent);
                        break;
                }

                return false;
            }
        });

    }

    @Override
    public void onListItemSelected(View view, boolean imageLoaded) {
        ImageView imageView=(ImageView) view.findViewById(R.id.cardView_parkingLotImage);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            parkingLotDetail=ParkingLotDetail.newInstance(false,((BitmapDrawable)imageView.getDrawable()).getBitmap(),imageView.getTransitionName());
        }
        if(findViewById(R.id.detail_container)==null)
        {
            parkingLotDetail.setSharedElementEnterTransition(new DetailsTransition());
            parkingLotDetail.setEnterTransition(new Fade());
            parkingLotDetail.setExitTransition(new Fade());
            parkingLotDetail.setSharedElementEnterTransition(new DetailsTransition());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                String transitionName = imageView.getTransitionName();

                getSupportFragmentManager().beginTransaction()
                        .addSharedElement(imageView, imageView.getTransitionName())
                        .replace(R.id.container, parkingLotDetail)
                        .addToBackStack(null)
                        .commit();
            }
        }else
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.detail_container,parkingLotDetail).commit();
        }

    }

    @Override
    public void setUpMovieDataHandler() {

    }
}

package com.hugo.lucky.easyparking;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Fade;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ViewParkingLotActivity extends AppCompatActivity implements Fragment_List.OnlistItemSelecteListener {

    RadioGroup radioGroup;
    MapFragment mapFragment;
    MyRecyclerView myRecyclerView;
    ParkingLotDetail parkingLotDetail;
    private LruCache<String,Bitmap> mImgMemoryCache;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_parking_lot);
        radioGroup=(RadioGroup)findViewById(R.id.radio_group);
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
        if(savedInstanceState==null){
            radioGroup.check(R.id.radio_map);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, new MapFragment().newInstance())
                    .commit();
        }
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_map:
                if (checked) {
                    FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
                    ft.setCustomAnimations(R.anim.card_flip_right_in,R.anim.card_flip_right_out,R.anim.card_flip_left_in,R.anim.card_flip_left_out);
                    if(mapFragment==null)
                    {
                        mapFragment=new MapFragment().newInstance();
                    }
                    ft.replace(R.id.container,mapFragment );
                    ft.commit();

                }
                    break;
            case R.id.radio_list:
                if (checked)
                {
                    FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
                    ft.setCustomAnimations(R.anim.card_flip_right_in,R.anim.card_flip_right_out,R.anim.card_flip_left_in,R.anim.card_flip_left_out);
                    if(myRecyclerView==null)
                    {
                        myRecyclerView=new MyRecyclerView().newInstance(mImgMemoryCache);
                    }
                    ft.replace(R.id.container, myRecyclerView);
                    ft.commit();
                }
                    break;
            case R.id.radio_page:
                if(checked)
                    break;
        }
    }





    @Override
    public void onListItemSelected( View view, boolean imageLoaded) {
        ImageView imageView=(ImageView) view.findViewById(R.id.cardView_parkingLotImage);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            parkingLotDetail=ParkingLotDetail.newInstance(imageLoaded,((BitmapDrawable)imageView.getDrawable()).getBitmap(),imageView.getTransitionName());
        }
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

    }

    @Override
    public void setUpMovieDataHandler() {

    }
}

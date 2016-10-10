package com.hugo.lucky.easyparking;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.eftimoff.viewpagertransformers.DepthPageTransformer;
import com.eftimoff.viewpagertransformers.FlipHorizontalTransformer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ParkingLotViewPager extends AppCompatActivity {

    MyFragmentStatePagerAdapter myFragmentStatePagerAdapter;
    ViewPager mViewPager;
    DatabaseReference mDatabase;
    ArrayList<String> nameList;
    Toolbar actionBar;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_lot_view_pager);
        mDatabase= FirebaseDatabase.getInstance().getReference("PostedParkingLot");
        nameList=new ArrayList<String>();
        mViewPager=(ViewPager)findViewById(R.id.pager);

        actionBar=(Toolbar)findViewById(R.id.toolbar);
        actionBar.setBackgroundColor(ActivityCompat.getColor(this,R.color.task1_background_color));
        actionBar.inflateMenu(R.menu.viewpager_activity_actionbar_menu);
        actionBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id=item.getItemId();
                switch(id)
                {
                    case R.id.goto_map:
                        intent=new Intent(getApplicationContext(),Map.class);
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
        Query query=mDatabase;
        query.addListenerForSingleValueEvent(new ValueEventListener(){

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()==0)return;
                Iterable<DataSnapshot>itemList=dataSnapshot.getChildren();
                for(DataSnapshot item:itemList) {
                    PostedParkingLot point=item.getValue(PostedParkingLot.class);
                    nameList.add(point.getName());

                }
                initializePageView();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void initializePageView()
    {
        MyFragmentStatePagerAdapter myFragmentStatePagerAdapter= new MyFragmentStatePagerAdapter(getSupportFragmentManager(),nameList);
        mViewPager.setAdapter(myFragmentStatePagerAdapter);
        mViewPager.setPageTransformer(true,new DepthPageTransformer());
        mViewPager.setCurrentItem(3);

        TabLayout tabLayout=(TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }
}

package com.hugo.lucky.easyparking;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MyPostedParkingLot extends AppCompatActivity implements Fragment_List.OnlistItemSelecteListener {

    MyPostedRecyclerView myPostedRecyclerView;
    FloatingActionButton addParkingLotButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posted_parking_lot);

        addParkingLotButton=(FloatingActionButton)findViewById(R.id.addParkingLot);
        addParkingLotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),PostParkLotActivity.class);
                startActivity(intent);
            }
        });
        myPostedRecyclerView=MyPostedRecyclerView.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.container,myPostedRecyclerView).commit();

    }

    @Override
    public void onListItemSelected(View view, boolean imageLoaded) {

    }

    @Override
    public void setUpMovieDataHandler() {

    }
}

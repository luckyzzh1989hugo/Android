package com.hugo.lucky.easyparking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by lucky on 8/15/2016.
 */
public class MyReceiver extends BroadcastReceiver {
    String name;
    @Override
    public void onReceive(Context context, Intent intent) {
        final MainActivity mainActivity = ((MyApplication) context.getApplicationContext()).getMainActivity();
        final Snackbar snackBar = Snackbar.make(mainActivity.findViewById(R.id.main_layout), intent.getAction(), Snackbar.LENGTH_LONG);
        Bundle bundle=intent.getExtras();
       name=(String)bundle.getSerializable("name");

        snackBar.setAction("SEE", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailIntent =new Intent(mainActivity,ParkingLotDetailActivity.class);
                detailIntent.putExtra("parkingLotName",name);
                mainActivity.startActivity(detailIntent);
                snackBar.dismiss();
            }
        });
        snackBar.show();
    }
}

package com.hugo.lucky.easyparking;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by lucky on 8/14/2016.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
                final String parkingLotName=remoteMessage.getNotification().getBody();

                final MainActivity mainActivity = ((MyApplication)getApplicationContext()).getMainActivity();
                final Snackbar snackBar = Snackbar.make(mainActivity.findViewById(R.id.main_layout),"new parking lot:"+parkingLotName, Snackbar.LENGTH_LONG);

                snackBar.setAction("SEE", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent detailIntent =new Intent(mainActivity,ParkingLotDetailActivity.class);
                        detailIntent.putExtra("parkingLotName",parkingLotName);
                        mainActivity.startActivity(detailIntent);
                        snackBar.dismiss();
                    }
                });
                snackBar.show();

            }
        }
    }


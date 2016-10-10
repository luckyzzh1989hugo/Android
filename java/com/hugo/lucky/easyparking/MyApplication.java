package com.hugo.lucky.easyparking;

import android.support.multidex.MultiDexApplication;

/**
 * Created by lucky on 8/15/2016.
 */
public class MyApplication extends MultiDexApplication {
    private MainActivity mainActivity;
    @Override
    public void onCreate() {
        super.onCreate();
    }
    public void setActivity(MainActivity a)
    {
        mainActivity=a;
    }
    public MainActivity getMainActivity()
    {
        return mainActivity;
    }

}

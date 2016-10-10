package com.hugo.lucky.easyparking;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by lucky on 8/10/2016.
 */
public class MyFragmentStatePagerAdapter extends FragmentPagerAdapter {
    ArrayList<String> nameList;
    int count;
    public MyFragmentStatePagerAdapter(FragmentManager fm,ArrayList<String> _nameList) {
        super(fm);
        nameList=_nameList;
        count=nameList.size();
    }
    @Override
    public CharSequence getPageTitle(int position) {

        return nameList.get(position);

    }
    @Override
    public Fragment getItem(int position) {
        return ParkingLotDetail.newInstance(false,null,nameList.get(position));

    }

    @Override
    public int getCount() {
        return count;
    }
}

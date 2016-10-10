package com.hugo.lucky.easyparking;

import android.view.View;

/**
 * Created by lucky on 6/1/2016.
 */

public class Fragment_List {
    public interface OnlistItemSelecteListener{
        public void onListItemSelected( View view,boolean imageLoaded);
        public void setUpMovieDataHandler();
    }
}

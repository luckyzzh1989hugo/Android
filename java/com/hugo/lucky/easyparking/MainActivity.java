package com.hugo.lucky.easyparking;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    NavigationView navigationView;

    Toolbar actionBar;
    DrawerLayout drawerLayout;
    Intent intent;
    AppCompatActivity activity;
    int IVITATION_REQUEST=101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=this;
        MyApplication myApplication = (MyApplication) this.getApplicationContext();
        myApplication.setActivity(this);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(getApplicationContext(),"ca-app-pub-1308327148565411~6716341083"); //advertisement
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("714B8A5FA4B1AFF2148B2E5690C5B78D").build();
        mAdView.loadAd(adRequest);

        actionBar = (Toolbar) findViewById(R.id.toolbar);
        actionBar.setBackgroundColor(ActivityCompat.getColor(this, R.color.task1_background_color));
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        String contact= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        RelativeLayout r=(RelativeLayout)navigationView.getHeaderView(0);
        ((TextView)r.findViewById(R.id.contact)).setText(contact);

        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        ActionBarDrawerToggle actionBarDrawerToggle =
                new ActionBarDrawerToggle(this, drawerLayout, actionBar, R.string.toggleOpen, R.string.toggleClose) {
                    @Override
                    public void onDrawerOpened(View drawerView) {
                        super.onDrawerOpened(drawerView);
                    }
                };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        actionBar.inflateMenu(R.menu.main_activity_actionbar_menu);
        actionBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id=item.getItemId();
                switch(id)
                {
                    case R.id.tutorialButton:
                        Intent actIntent = new Intent(getApplication(), YouTubeActivity.class);
                        actIntent.putExtra(YouTubeActivity.VIDEO_ID, "HGM9V6mohjs");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            ActivityOptionsCompat options = ActivityOptionsCompat.
                                    makeSceneTransitionAnimation(activity, actionBar, "youtube_and_invite");
                            startActivity(actIntent, options.toBundle());
                        }
                        break;
                    case R.id.invitation_button:
                        Intent intent = new AppInviteInvitation.IntentBuilder("hello")
                                .setMessage("fromEasyParking")
                                .build();
                        startActivityForResult(intent, IVITATION_REQUEST);
                }
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IVITATION_REQUEST) {
            if (resultCode == RESULT_OK) {
                // Get the invitation IDs of all sent messages
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                for (String id : ids) {

                }
            } else {
                // Sending failed or it was canceled, show failure message to the user
                // ...
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch(id){
            case R.id.postActivity:
                intent=new Intent(getApplicationContext(),PostParkLotActivity.class);
                startActivity(intent);
                break;
            case R.id.listView:
                intent=new Intent(getApplicationContext(),RecyclerViewActivity.class);
                startActivity(intent);
                break;
            case R.id.map:
                intent=new Intent(getApplicationContext(),Map.class);
                startActivity(intent);
                break;
            case R.id.viewpager:
                intent=new Intent(getApplicationContext(),ParkingLotViewPager.class);
                startActivity(intent);
                break;
            case R.id.my_posted:
                intent=new Intent(getApplicationContext(),MyPostedParkingLot.class);
                startActivity(intent);
                break;


        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }
}

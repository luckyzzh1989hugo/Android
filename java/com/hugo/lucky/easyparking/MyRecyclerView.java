package com.hugo.lucky.easyparking;


import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FlipInTopXAnimator;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyRecyclerView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyRecyclerView extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    MyFirebaseRecylerAdapter mRecyclerViewAdapter;
    ArrayList<Integer> indexList;
    private LruCache<String,Bitmap> mImgMemoryCache;
    private static final String ARG_MOVIE_INFO = "movieInfo";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    public void scrolltoPosition(int position)
    {
        mRecyclerView.scrollToPosition(position);
    }


    public MyRecyclerView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MyRecyclerView.
     */
    // TODO: Rename and change types and number of parameters
    public static MyRecyclerView newInstance(LruCache<String,Bitmap> mImgMemoryCache) {

        MyRecyclerView fragment = new MyRecyclerView();
//        fragment.setImgMemoryCache(mImgMemoryCache);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
//    private void setImgMemoryCache(LruCache<String,Bitmap> _mImgMemoryCache)
//    {
//        mImgMemoryCache=_mImgMemoryCache;
//    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    boolean isDouble(String str)
    {
        try
        {
            Double.parseDouble(str);
            return true;
        }catch (NumberFormatException e)
        {
            return false;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch(id)
        {

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

    }

    @Override
    public void onStop() {
        super.onStop();
//        for(MyFirebaseRecylerAdapter.MyDownloadMovieIcon task:mRecyclerViewAdapter.taskList)
//        {
//            if(task!=null&&task.getStatus()== AsyncTask.Status.RUNNING)
//            {
//                task.cancel(true);
//            }
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //final Firebase ref=new Firebase("https://easyparking-824b3.firebaseio.com/PostedParkingLot/");
        DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference("PostedParkingLot");
        // Inflate the layout for this fragment
        final Fragment_List.OnlistItemSelecteListener mListener;
        try{
            mListener=(Fragment_List.OnlistItemSelecteListener)getContext();
        }catch (ClassCastException e)
        {
            throw new ClassCastException("The hosting activity of the Fragment "+ "forget to implement" +
                    "OnFragmentInteractionListener");
        }
        final View rootView=inflater.inflate(R.layout.fragment_my_recycler_view, container, false);
        mRecyclerView=(RecyclerView)rootView.findViewById(R.id.cardList);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager=new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        Query query=mDatabase.orderByChild("reserved").equalTo(false);
        mRecyclerViewAdapter=new MyFirebaseRecylerAdapter(PostedParkingLot.class,R.layout.fragment_my_recycler_view
                , MyFirebaseRecylerAdapter.ParkingLotHolder.class,query,getActivity(),mImgMemoryCache);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);

//        //flipAnimation();
        mRecyclerViewAdapter.setOnItemClickListener(new MyFirebaseRecylerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view,boolean imageLoaded) {
                mListener.onListItemSelected(view,imageLoaded);
            }

//            @Override
//            public void onItemLongClick(View view, int position) {
//                //getActivity().startActionMode(new ActionBarCallBack(position,movieData,mRecyclerViewAdapter));
//
//            }

//            @Override
//            public void onOverflowMenuClick(View v, final int position) {
//                PopupMenu popup=new PopupMenu(getActivity(),v);
//                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        switch(item.getItemId()){
//                            case R.id.duplicate:
//                                Movie cloud=mRecyclerViewAdapter.getItem(position);
//                                cloud.setName(cloud.getName()+"-New");
//                                cloud.setId(cloud.getId()+"-New");
//                                ref.child(cloud.getId()).setValue(cloud);
//                                return false;
//                            case R.id.delete:
//                                Movie cloudDelete=mRecyclerViewAdapter.getItem(position);
//                                ref.child(cloudDelete.getId()).removeValue();
//                                return false;
//
//                        }
//                        return false;
//                    }
//                });
//                MenuInflater inflater=popup.getMenuInflater();
//                inflater.inflate(R.menu.contextual_or_popup_menu,popup.getMenu());
//                popup.show();
//            }
        });
        mRecyclerView.getItemAnimator().setAddDuration(500);
        mRecyclerView.getItemAnimator().setRemoveDuration(500);
        mRecyclerView.getItemAnimator().setMoveDuration(500);
        mRecyclerView.getItemAnimator().setChangeDuration(500);
        adapterAnimation();
        flipAnimation();
        //defaultAnimation();
    //    SlideInLeftAnimation();
        return rootView;

    }
//    private void modifyMovieData()
//    {
//        int len = indexList.size();
//        for(int i=0;i<len;i++)
//        {
//            movieData.getItem(indexList.get(i)).put("selection",true);
//        }
//    }
    private void adapterAnimation(){
        AlphaInAnimationAdapter alphaAdapter= new AlphaInAnimationAdapter(mRecyclerViewAdapter);
        alphaAdapter.setInterpolator(new OvershootInterpolator());
        ScaleInAnimationAdapter scaleAdapter=new ScaleInAnimationAdapter(alphaAdapter);
        mRecyclerView.setAdapter(scaleAdapter);
    }
    private void flipAnimation(){
        FlipInTopXAnimator animator = new FlipInTopXAnimator();
//        animator.setAddDuration(1000);
//        animator.setRemoveDuration(1000);
        mRecyclerView.setItemAnimator(animator);
    }
    private void SlideInLeftAnimation()
    {
        SlideInLeftAnimator animator = new SlideInLeftAnimator();
        animator.setAddDuration(500);
        animator.setChangeDuration(100);
        animator.setMoveDuration(100);
        animator.setRemoveDuration(100);
        mRecyclerView.setItemAnimator(animator);
    }
    private void defaultAnimation(){
        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setAddDuration(100);
        animator.setRemoveDuration(100);
        mRecyclerView.setItemAnimator(animator);
    }










}

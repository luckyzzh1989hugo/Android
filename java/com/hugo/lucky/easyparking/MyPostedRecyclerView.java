package com.hugo.lucky.easyparking;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.PopupMenu;
import android.view.animation.OvershootInterpolator;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FlipInTopXAnimator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyPostedRecyclerView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyPostedRecyclerView extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    MyPostedRecyclerAdapter mRecyclerViewAdapter;
    Query query;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public MyPostedRecyclerView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment MyPostedRecyclerView.
     */
    // TODO: Rename and change types and number of parameters
    public static MyPostedRecyclerView newInstance() {
        MyPostedRecyclerView fragment = new MyPostedRecyclerView();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference("PostedParkingLot");
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
        query=mDatabase.orderByChild("ownerId").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mRecyclerViewAdapter=new MyPostedRecyclerAdapter(PostedParkingLot.class,R.layout.fragment_my_recycler_view
                , MyPostedRecyclerAdapter.ParkingLotHolder.class,query,getActivity());
        mRecyclerView.setAdapter(mRecyclerViewAdapter);

//        //flipAnimation();
        mRecyclerViewAdapter.setOnItemClickListener(new MyPostedRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view,boolean imageLoaded) {
                //mListener.onListItemSelected(view,imageLoaded);
            }

//            @Override
//            public void onItemLongClick(View view, int position) {
//                //getActivity().startActionMode(new ActionBarCallBack(position,movieData,mRecyclerViewAdapter));
//
//            }

            @Override
            public void onOverflowMenuClick(final String name,View v) {
                PopupMenu popup=new PopupMenu(getActivity(),v);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId())
                        {
                            case R.id.edit:
                                Intent intent = new Intent(getContext(), PostParkLotActivity.class);
                                intent.putExtra("parkingLotName", name);
                                startActivity(intent);
                                break;
                            case R.id.delete:
                                mDatabase.child(name).removeValue();
                                break;
                        }
                        return false;
                    }


                });
                MenuInflater inflater=popup.getMenuInflater();
                inflater.inflate(R.menu.contextual_or_popup_menu,popup.getMenu());
                popup.show();
            }
        });
        mRecyclerView.getItemAnimator().setAddDuration(500);
        mRecyclerView.getItemAnimator().setRemoveDuration(500);
        mRecyclerView.getItemAnimator().setMoveDuration(500);
        mRecyclerView.getItemAnimator().setChangeDuration(500);
        adapterAnimation();
        flipAnimation();
        return rootView;
    }
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

}

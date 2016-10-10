package com.hugo.lucky.easyparking;


import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.ref.WeakReference;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ParkingLotDetail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ParkingLotDetail extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private boolean imageLoaded;
    private ImageView parkingLotImage;
    private TextView parkingLotName;
    private TextView timeAvailable;
    private TextView price;
    private TextView contact;
    private TextView address;
    private String name;
    private Button googleButton;
    DatabaseReference mDatabase;
    private Bitmap bitmap;

    public ParkingLotDetail() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ParkingLotDetail.
     */
    // TODO: Rename and change types and number of parameters
    public static ParkingLotDetail newInstance(boolean imageLoaded,Bitmap bitmap,String name) {
        ParkingLotDetail fragment = new ParkingLotDetail();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.setBitmap(bitmap);
        fragment.setName(name);
        fragment.setImageLoaded(imageLoaded);
        return fragment;
    }
    public void setImageLoaded(boolean value)
    {
        imageLoaded=value;
    }
    public void setBitmap(Bitmap _bitmap)
    {
        bitmap=_bitmap;
    }
    public void setName(String _name){name=_name;}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public class myOnsuccessListener<U> implements OnSuccessListener<Uri>
    {
        ImageView parkingLotIcon;
        public myOnsuccessListener(ImageView _parkingLotIcon)
        {
            parkingLotIcon=_parkingLotIcon;
        }

        @Override
        public void onSuccess(Uri uri) {


        }
    }
    public class MyDownloadMovieIcon extends AsyncTask<String,Void,Bitmap>
    {


        WeakReference<ImageView>imageViewWeakReference;
        public MyDownloadMovieIcon(ImageView icon) {
            imageViewWeakReference=new WeakReference<ImageView>(icon);
        }



        @Override
        protected Bitmap doInBackground(String... urls) {

            Bitmap bitmap=null;
            bitmap=MyUtility.downloadImageusingHTTPGetRequest(urls[0]);


            return bitmap;
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            final ImageView icon=imageViewWeakReference.get();
            if(icon!=null){
                icon.setImageBitmap(bitmap);

            }
        }
    }
    public void setFloatingActionButton(final PostedParkingLot p)
    {
        FloatingActionButton fab=(FloatingActionButton)getActivity().findViewById(R.id.share);
        if(fab==null)return;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent=new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody="name:"+p.getName().toString()+"\n"
                        +"address:"+p.getAddress()+","+p.getCity()+","+p.getState()+"\n"
                        +"available time:"+p.getStartTime()+"-"+p.getEndTime()+"\n"
                        +"contact: "+p.getEmail();

                sharingIntent.putExtra(Intent.EXTRA_SUBJECT,"EasyParking");
                sharingIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
                startActivity(Intent.createChooser(sharingIntent,"Sharevia"));
            }
        });

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_parking_lot_detail, container, false);
        parkingLotName=(TextView)v.findViewById(R.id.detail_parkingLotName);
        timeAvailable=(TextView)v.findViewById(R.id.detail_parkingLotTime);
        price=(TextView)v.findViewById(R.id.detail_price);
        contact=(TextView)v.findViewById(R.id.detail_contact);
        address=(TextView)v.findViewById(R.id.detail_parkingLotAddress);
        parkingLotImage=(ImageView)v.findViewById(R.id.detail_parkingLotImage);
        googleButton=(Button)v.findViewById(R.id.to_google_map);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            parkingLotImage.setTransitionName(name);
        }
        mDatabase= FirebaseDatabase.getInstance().getReference("PostedParkingLot");
        parkingLotName.setText(name);

        Query queryRef = mDatabase.child(name);
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()==0)return;


                        PostedParkingLot postedParkingLot = dataSnapshot.getValue(PostedParkingLot.class);
                        parkingLotName.setText(postedParkingLot.getName());
                        timeAvailable.setText(postedParkingLot.getStartTime()+"-"+postedParkingLot.getEndTime());
                        price.setText(postedParkingLot.getPrice().toString()+"/h");
                        address.setText(postedParkingLot.getAddress()+","+postedParkingLot.getCity()+","+postedParkingLot.getState());
                        contact.setText(postedParkingLot.getEmail());
                        setFloatingActionButton(postedParkingLot);  //set floatingActionButton click listener

                        googleButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String uri = String.format(Locale.ENGLISH, "geo:0,0?q="+address.getText());
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                getActivity().startActivity(intent);
                            }
                        });
                        if(imageLoaded)parkingLotImage.setImageBitmap(bitmap);
                        else
                        {
                            StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://easyparking-824b3.appspot.com");
                            String path=postedParkingLot.getEmail()+"/"+postedParkingLot.getName()+"/"+"parkingLotPicture.jpg";
                            StorageReference pathReference= storageRef.child(path);
                            pathReference.getDownloadUrl().addOnSuccessListener(new myOnsuccessListener<Uri>(parkingLotImage) {
                                @Override
                                public void onSuccess(Uri _uri) {
                                    // TODO: handle uri
                                        MyDownloadMovieIcon myDownloadMovieIcon=new MyDownloadMovieIcon(parkingLotIcon);
                                        myDownloadMovieIcon.execute(_uri.toString());
                                    }

                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                }
                            });
                        }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return v;
    }

}

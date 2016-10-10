package com.hugo.lucky.easyparking;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by lucky on 8/14/2016.
 */
public class MyPostedRecyclerAdapter extends FirebaseRecyclerAdapter<PostedParkingLot,MyPostedRecyclerAdapter.ParkingLotHolder> {

    private Context mContext ;
    private static OnItemClickListener mItemClickListener;
    public MyPostedRecyclerAdapter(Class<PostedParkingLot> modelClass, int modelLayout,
                                   Class<ParkingLotHolder> holder, Query ref, Context context) {
        super(modelClass,modelLayout,holder,ref);
        this.mContext = context;


}

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener)
    {
        this.mItemClickListener=mItemClickListener;
    }
    public interface OnItemClickListener{
        public void onItemClick(View view, boolean imageLoaded);
//        public void onItemLongClick(View view, int position);
        public void onOverflowMenuClick(String name,View v);

    }

    @Override
    public ParkingLotHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v= LayoutInflater.from(parent.getContext()).inflate(R.layout.my_posted_cardview,parent,false);


        ParkingLotHolder vh=new ParkingLotHolder(v);
        return vh;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void populateViewHolder(ParkingLotHolder parkingLotViewHolder, final PostedParkingLot parkingLot, int i) {

        //TODO: Populate viewHolder by setting the movie attributes to cardview fields
        //movieViewHolder.nameTV.setText(movie.getName());
        if(parkingLot.getReserved())parkingLotViewHolder.menuButton.setVisibility(View.INVISIBLE);
        else parkingLotViewHolder.menuButton.setVisibility(View.VISIBLE);
        parkingLotViewHolder.parkingLotName.setText(parkingLot.getName());
        parkingLotViewHolder.parkingLotAddress.setText(parkingLot.getAddress()+","+parkingLot.getCity()+","+parkingLot.getState());
        parkingLotViewHolder.parkingLotName.setTypeface(FontType.getLobster(mContext));
        StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://easyparking-824b3.appspot.com");
        String path=parkingLot.getEmail()+"/"+parkingLot.getName()+"/"+"parkingLotPicture.jpg";
        StorageReference pathReference= storageRef.child(path);
        parkingLotViewHolder.parkingLotIcon.setTransitionName(parkingLot.getName());
        parkingLotViewHolder.parkingLotPrice.setText(parkingLot.price.toString()+"/h");
        pathReference.getDownloadUrl().addOnSuccessListener(new myOnsuccessListener<Uri>(parkingLotViewHolder,parkingLot) {
            @Override
            public void onSuccess(Uri _uri) {
                // TODO: handle uri
                Bitmap bitmap=null;
                if(bitmap!=null)
                {
                    parkingLotHolderWeakReference.get().parkingLotIcon.setImageBitmap(bitmap);
                }else
                {
                    MyDownloadMovieIcon myDownloadMovieIcon=new MyDownloadMovieIcon(parkingLotHolderWeakReference.get());
                    myDownloadMovieIcon.execute(_uri.toString(),String.valueOf(parkingLotHolderWeakReference.get().parkingLotIcon.getWidth()),String.valueOf(parkingLotHolderWeakReference.get().parkingLotIcon.getWidth()));
                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });


    }

    public class MyDownloadMovieIcon extends AsyncTask<String,Void,Bitmap>
    {
        WeakReference<ParkingLotHolder> imageViewWeakReference;

        public MyDownloadMovieIcon(ParkingLotHolder parkingLotHolder) {
            imageViewWeakReference=new WeakReference<ParkingLotHolder>(parkingLotHolder);
        }



        @Override
        protected Bitmap doInBackground(String... urls) {

            Bitmap bitmap=null;

            bitmap=MyUtility.downloadImageusingHTTPGetRequestByParameters(urls[0],Integer.parseInt(urls[1]),Integer.parseInt(urls[2]));
//            bitmap=MyUtility.downloadImageusingHTTPGetRequest(urls[0]);


//            if(bitmap!=null)
//            {
//                bitmapLruCache.put(urls[0],bitmap);
//            }

            return bitmap;
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            final ImageView icon=imageViewWeakReference.get().parkingLotIcon;
            if(icon!=null){
                icon.setImageBitmap(bitmap);
                // icon.setTransitionName(imageViewWeakReference.get().parkingLotName.getText().toString());
                imageViewWeakReference.get().imageLoaded=true;
            }
        }
    }
    public class myOnsuccessListener<U> implements OnSuccessListener<Uri>
    {
        WeakReference<ParkingLotHolder> parkingLotHolderWeakReference;
        WeakReference<PostedParkingLot> postedParkingLotWeakReference;
        Uri uri;
        public myOnsuccessListener(ParkingLotHolder p,PostedParkingLot _parkingLot)
        {

            parkingLotHolderWeakReference=new WeakReference<ParkingLotHolder>(p);
            postedParkingLotWeakReference=new WeakReference<PostedParkingLot>(_parkingLot);
        }

        @Override
        public void onSuccess(Uri uri) {


        }
    }
    public static class ParkingLotHolder extends RecyclerView.ViewHolder{
        ImageView parkingLotIcon;
        TextView parkingLotName;
        TextView parkingLotPrice;
        TextView parkingLotAddress;
        CardView cardView;
        ImageView menuButton;
        boolean imageLoaded;

        public ParkingLotHolder(View v) {
            super(v);
            imageLoaded=false;
            v.setBackgroundResource(R.drawable.cardview_combine);
            parkingLotIcon=(ImageView)v.findViewById(R.id.cardView_parkingLotImage);

            parkingLotName=(TextView)v.findViewById(R.id.cardView_parkingLotName);
            parkingLotPrice=(TextView)v.findViewById(R.id.cardView_price);
            parkingLotAddress=(TextView)v.findViewById(R.id.cardView_parkingLotAddress);
            cardView=(CardView)v.findViewById(R.id.cardView);
            menuButton=(ImageView)itemView.findViewById(R.id.popupButton);
            if(menuButton!=null)
            {
                menuButton.setOnClickListener(new View.OnClickListener()
                {

                    @Override
                    public void onClick(View v) {
                        if(mItemClickListener!=null)
                        {
                            mItemClickListener.onOverflowMenuClick(parkingLotName.getText().toString(),v);
                        }
                    }
                });
            }



        }
    }

}

package com.hugo.lucky.easyparking;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.util.LruCache;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.Query;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
//import com.squareup.picasso.Picasso;
//
//import firebase.sandesh.firebaseapp.model.Movie;
//import firebase.sandesh.firebaseapp.R;

public class MyFirebaseRecylerAdapter extends FirebaseRecyclerAdapter<PostedParkingLot,MyFirebaseRecylerAdapter.ParkingLotHolder> {

    private Context mContext ;
    private static OnItemClickListener mItemClickListener;
    private LruCache<String,Bitmap>  bitmapLruCache;
    public ArrayList<MyDownloadMovieIcon> taskList;
    public ArrayList<ImageView>iconList;
    public MyFirebaseRecylerAdapter(Class<PostedParkingLot> modelClass, int modelLayout,
                                    Class<ParkingLotHolder> holder, Query ref, Context context,LruCache<String,Bitmap> cache) {
        super(modelClass,modelLayout,holder,ref);
        this.mContext = context;
        bitmapLruCache=cache;
        taskList=new ArrayList<MyDownloadMovieIcon>();
        iconList=new ArrayList<ImageView>();
    }
    public interface OnItemClickListener{
        public void onItemClick(View view,boolean imageLoaded);
//        public void onItemLongClick(View view, int position);
   //     public void onOverflowMenuClick(View v, int position);

    }

    @Override
    public ParkingLotHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview,parent,false);


        ParkingLotHolder vh=new ParkingLotHolder(v);
        return vh;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void populateViewHolder(ParkingLotHolder parkingLotViewHolder, final PostedParkingLot parkingLot, int i) {

        //TODO: Populate viewHolder by setting the movie attributes to cardview fields
        //movieViewHolder.nameTV.setText(movie.getName());
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
                    taskList.add(myDownloadMovieIcon);
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
            if(imageViewWeakReference.get()==null)return;
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
    public void setOnItemClickListener(final OnItemClickListener mItemClickListener)
    {
        this.mItemClickListener=mItemClickListener;
    }
    //TODO: Populate ViewHolder and add listeners.
    public static class ParkingLotHolder extends RecyclerView.ViewHolder{
        ImageView parkingLotIcon;
        TextView parkingLotName;
        TextView parkingLotPrice;
        TextView parkingLotAddress;
        CardView cardView;
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


        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mItemClickListener!=null){
                    mItemClickListener.onItemClick(v,imageLoaded);

                }
            }
        });

        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(mItemClickListener!=null){
                    //mItemClickListener.onItemLongClick(v,getAdapterPosition());
                }
                return true;
            }
        });
        }
    }

    @Override
    public void onViewDetachedFromWindow(ParkingLotHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }
}

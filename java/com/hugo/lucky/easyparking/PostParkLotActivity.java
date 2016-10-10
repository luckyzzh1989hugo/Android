package com.hugo.lucky.easyparking;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

public class PostParkLotActivity extends AppCompatActivity {
    int RESULT_LOAD_IMG=10;
    ImageView uploadImage;
    Button uploadButton;
    Button submitButton;
    FirebaseStorage storage;
    Spinner from_time_spinner;
    Spinner to_time_spinner;
    EditText addressInputFiled;
    EditText cityInputFiled;
    EditText stateInputFiled;
    EditText priceInputFiled;
    EditText nameInputFiled;
    DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private boolean isEdit;
    ArrayAdapter<CharSequence> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isEdit=false;
        setContentView(R.layout.activity_post_park_lot);
        uploadButton=(Button)findViewById(R.id.upload_image_button);
        submitButton=(Button)findViewById(R.id.submit_button) ;
        uploadImage=(ImageView)findViewById(R.id.parkingLotImage);
        addressInputFiled=(EditText)findViewById(R.id.addressInputField);
        cityInputFiled=(EditText)findViewById(R.id.cityInputField);
        stateInputFiled=(EditText)findViewById(R.id.stateInputField);
        priceInputFiled=(EditText)findViewById(R.id.price_edit_text);
        nameInputFiled=(EditText)findViewById(R.id.nameInputField);
        storage= FirebaseStorage.getInstance();
        mAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference("PostedParkingLot");
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
// Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=nameInputFiled.getText().toString();
                if(name.equals(""))return;
                Query queryRef = mDatabase.child(name);
                queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getChildrenCount()>0&&!isEdit)return;
                        try {
                            submit();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });
        from_time_spinner=(Spinner) findViewById(R.id.available_from_time);
        to_time_spinner=(Spinner)findViewById(R.id.available_to_time);
       adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        from_time_spinner.setAdapter(adapter);
        to_time_spinner.setAdapter(adapter);

        Bundle extras=getIntent().getExtras();
        if(extras!=null)
        {
            isEdit=true;
            String name=extras.getString("parkingLotName");
            mDatabase= FirebaseDatabase.getInstance().getReference("PostedParkingLot");
            nameInputFiled.setText(name);

            Query queryRef = mDatabase.child(name);
            queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    setUp(dataSnapshot);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            }) ;
        }
    }
    void setUp(DataSnapshot dataSnapshot)
    {
        PostedParkingLot postedParkingLot = dataSnapshot.getValue(PostedParkingLot.class);
        addressInputFiled.setText(postedParkingLot.getAddress());
        cityInputFiled.setText(postedParkingLot.getCity());
        stateInputFiled.setText(postedParkingLot.getState());
        priceInputFiled.setText(postedParkingLot.getPrice().toString());
        String fromTime=postedParkingLot.getStartTime();
        from_time_spinner.setSelection(adapter.getPosition(fromTime));
        String toTime=postedParkingLot.getEndTime();
        to_time_spinner.setSelection(adapter.getPosition(toTime));
        StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://easyparking-824b3.appspot.com");
        String path=postedParkingLot.getEmail()+"/"+postedParkingLot.getName()+"/"+"parkingLotPicture.jpg";
        StorageReference pathReference= storageRef.child(path);
        pathReference.getDownloadUrl().addOnSuccessListener(new myOnsuccessListener<Uri>(uploadImage) {
            @Override
            public void onSuccess(Uri _uri) {
                // TODO: handle uri
                MyDownloadMovieIcon myDownloadMovieIcon=new MyDownloadMovieIcon(parkingLotIcon);
                myDownloadMovieIcon.execute(_uri.toString(),String.valueOf(uploadImage.getWidth()),String.valueOf(uploadImage.getHeight()));
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

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


        WeakReference<ImageView> imageViewWeakReference;
        public MyDownloadMovieIcon(ImageView icon) {
            imageViewWeakReference=new WeakReference<ImageView>(icon);
        }



        @Override
        protected Bitmap doInBackground(String... urls) {

            Bitmap bitmap=null;
            bitmap=MyUtility.downloadImageusingHTTPGetRequestByParameters(urls[0],Integer.parseInt(urls[1]),Integer.parseInt(urls[2]));


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
    void submit() throws IOException {
        FirebaseUser user=mAuth.getCurrentUser();
        if(user==null)return;
        String path=user.getEmail()+"/"+nameInputFiled.getText().toString();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://easyparking-824b3.appspot.com");
        StorageReference mountainImagesRef = storageRef.child(path+"/parkingLotPicture.jpg");
        uploadImage.setDrawingCacheEnabled(true);
        uploadImage.buildDrawingCache();
        Bitmap bitmap = uploadImage.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainImagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });

        PostedParkingLot postedParkingLot=new PostedParkingLot();
        postedParkingLot.setEmail(user.getEmail());
        postedParkingLot.setName(nameInputFiled.getText().toString());
        postedParkingLot.setAddress(addressInputFiled.getText().toString());
        postedParkingLot.setCity(cityInputFiled.getText().toString());
        postedParkingLot.setState(stateInputFiled.getText().toString());
        postedParkingLot.setPrice(Long.parseLong(priceInputFiled.getText().toString()));
        postedParkingLot.setStartTime(from_time_spinner.getSelectedItem().toString());
        postedParkingLot.setEndTime(to_time_spinner.getSelectedItem().toString());
        postedParkingLot.setReserved(false);
        postedParkingLot.setOwnerId(user.getUid());
        postedParkingLot.setReserverId("0");
        DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference("PostedParkingLot");
        String address=addressInputFiled.getText().toString()+","+cityInputFiled.getText().toString()+","
                +stateInputFiled.getText().toString();
        Geocoder coder=new Geocoder(this);
        List<Address> addressList;
        addressList=coder.getFromLocationName(address,5);
        if(addressList.size()<1)return;
        Address location=addressList.get(0);
        postedParkingLot.setLattitude(location.getLatitude());
        postedParkingLot.setLongitude(location.getLongitude());
        mDatabase.child(nameInputFiled.getText().toString()).setValue(postedParkingLot);
        Intent intent=new Intent("parking.lot.update");
        intent.putExtra("name",postedParkingLot.getName());
        sendBroadcast(intent);
        onBackPressed();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == RESULT_LOAD_IMG) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.

                // Do something with the contact here (bigger example below)
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                // Set the Image in ImageView after decoding the String
                uploadImage.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
            }

    }

}

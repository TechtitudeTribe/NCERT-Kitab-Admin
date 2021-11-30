package com.techtitudetribe.ncertkitabadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddSubjectActivity extends AppCompatActivity {
    private ImageView subjectImage;
    private EditText subjectName;
    private TextView addSubject;
    private String categoryname, sub_name, saveCurrentTime, saveCurrentDate;
    private static final int GalleryPick = 1;
    private Uri imageUri;
    private String subjectRandomKey, downloadImageUrl;
    private StorageReference subjectImageRef;
    private DatabaseReference subjectRef;
    private ProgressDialog loadingBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);


        categoryname = getIntent().getExtras().get("category").toString();
        subjectImageRef = FirebaseStorage.getInstance().getReference().child("Subject Image");
        subjectRef = FirebaseDatabase.getInstance().getReference().child("Subject Details");

        subjectImage = (ImageView) findViewById(R.id.subject_image);
        subjectName = (EditText) findViewById(R.id.subject_name);
        addSubject = (TextView) findViewById(R.id.add_subject);
        loadingBar= new ProgressDialog(this);


        subjectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGalley();
            }
        });

        addSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                AddSubject();
            }
        });


    }


    private void OpenGalley() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GalleryPick && resultCode==RESULT_OK && data!=null){

            imageUri=data.getData();
            subjectImage.setImageURI(imageUri);
        }
    }


    private void AddSubject() {
        sub_name = subjectName.getText().toString();
        
        if (imageUri==null)
        {
            Toast.makeText(AddSubjectActivity.this, "Please select subject", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(sub_name))
        {
            Toast.makeText(AddSubjectActivity.this, "Please enter subject name", Toast.LENGTH_SHORT).show();
        }else
        {

            StoreSubjectInfo();
        }

    }

    private void StoreSubjectInfo() {

        loadingBar.setTitle("Add new Subject");
        loadingBar.setMessage("Plzz wait");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();


        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate= new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate= currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime= new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime= currentTime.format(calendar.getTime());


        subjectRandomKey = saveCurrentDate + saveCurrentTime;

        StorageReference filepath = subjectImageRef.child(imageUri.getLastPathSegment() + subjectRandomKey + ".jpg");


        final UploadTask uploadTask = filepath.putFile(imageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message= e.toString();
                Toast.makeText(AddSubjectActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(AddSubjectActivity.this, "Upload Subject Successfully", Toast.LENGTH_SHORT).show();

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }

                        downloadImageUrl = filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString();
                            saveSubjectToDatabase();
                        }
                    }
                });
            }
        });


    }

    private void saveSubjectToDatabase() {

        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("name", sub_name);
        productMap.put("image", downloadImageUrl);

        subjectRef.child(categoryname).child(subjectRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful())
                        {
                            Intent i = new Intent(AddSubjectActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();

                            loadingBar.dismiss();
                            Toast.makeText(AddSubjectActivity.this, "Subject Added Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message =task.getException().toString();
                            Toast.makeText(AddSubjectActivity.this, "Error:"+ message, Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}
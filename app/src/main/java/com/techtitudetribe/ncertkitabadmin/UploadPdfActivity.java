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
import android.widget.CheckBox;
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

public class UploadPdfActivity extends AppCompatActivity {
    private EditText selectPdf, className, subjectName, chapterName;
    private TextView uploadPdf;
    private ImageView back;
    private CheckBox cbseCheck, upCheck;
    private String categoryname, chapter_name, class_no, sub_name, saveCurrentDate, saveCurrentTime;
    private static final int Gallery_Pick = 1;
    private Uri pdfUri;
    private String chapterRandomKey, downloadChapUrl;
    private StorageReference chapterpdfRef;
    private DatabaseReference chapterRef;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pdf);


        categoryname = getIntent().getExtras().get("category").toString();
        chapterpdfRef = FirebaseStorage.getInstance().getReference().child("Subject Image").child("Chapter Pdf");
        chapterRef = FirebaseDatabase.getInstance().getReference().child("Pdf Details");

        cbseCheck = (CheckBox) findViewById(R.id.checkbox_cbseboard);
        upCheck = (CheckBox)findViewById(R.id.checkbox_upboard);

        selectPdf = (EditText) findViewById(R.id.select_pdf);
        className = (EditText) findViewById(R.id.pdf_class_name);
        subjectName  = (EditText) findViewById(R.id.pdf_subject_name);
        chapterName = (EditText) findViewById(R.id.pdf_chapter_name);

        uploadPdf = (TextView) findViewById(R.id.pdf_upload);
        back = (ImageView) findViewById(R.id.back);

        selectPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        uploadPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadPdf();
            }
        });
    }


    private void openGallery() {

        Intent openGalleryPdf  = new Intent();
        openGalleryPdf.setAction(Intent.ACTION_GET_CONTENT);
        openGalleryPdf.setType("pdf/");
        startActivityForResult(openGalleryPdf, Gallery_Pick);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Gallery_Pick && resultCode == RESULT_OK && data != null)
        {
            pdfUri = data.getData();
            selectPdf.setText("pdfUri");

        }

    }


    private void UploadPdf() {
        sub_name = subjectName.getText().toString();
        chapter_name = chapterName.getText().toString();
        class_no = className.getText().toString();

        if (pdfUri == null)
        {
            Toast.makeText(UploadPdfActivity.this, "Please select the chapter pdf", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(sub_name))
        {
            Toast.makeText(UploadPdfActivity.this, "Please enter Subject name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(chapter_name))
        {
            Toast.makeText(UploadPdfActivity.this, "Please enter chapter name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(class_no))

        {
            Toast.makeText(UploadPdfActivity.this, "Enter the class number", Toast.LENGTH_SHORT).show();
        } else
        {
            StoreChapterInfo();
        }


    }

    private void StoreChapterInfo() {

        loadingBar.setTitle("Add new Chapter pdf");
        loadingBar.setMessage("Plzz wait");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate= new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate= currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime= new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime= currentTime.format(calendar.getTime());


        chapterRandomKey = saveCurrentDate + saveCurrentTime;

        StorageReference filepath = chapterpdfRef.child(pdfUri.getLastPathSegment() + chapterRandomKey + ".pdf");

        final UploadTask uploadTask = filepath.putFile(pdfUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                String message= e.toString();
                Toast.makeText(UploadPdfActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(UploadPdfActivity.this, "Uploaded Pdf Successfully", Toast.LENGTH_SHORT).show();

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }

                        downloadChapUrl = filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful())
                        {
                            downloadChapUrl = task.getResult().toString();
                            saveChapterPdfToDatabase();
                        }
                    }
                });

            }
        });

    }

    private void saveChapterPdfToDatabase() {

        HashMap hashMap = new HashMap();
        hashMap.put("name", sub_name);
        hashMap.put("class name", class_no);
        hashMap.put("pdf", downloadChapUrl);

        chapterRef.child(categoryname).child(chapterRandomKey).updateChildren(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful())
                        {
                            Intent i = new Intent(UploadPdfActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();

                            loadingBar.dismiss();
                            Toast.makeText(UploadPdfActivity.this, "Subject Added Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message =task.getException().toString();
                            Toast.makeText(UploadPdfActivity.this, "Error:"+ message, Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }


}
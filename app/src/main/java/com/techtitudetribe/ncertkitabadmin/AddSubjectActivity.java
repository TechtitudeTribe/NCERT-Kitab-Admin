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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class AddSubjectActivity extends AppCompatActivity {

    private RadioGroup board, standard;
    private String boardString = "", standardString = "";
    private EditText subjectName;
    private TextView add, cancel;
    private ProgressDialog LoadingBar;
    private DatabaseReference subjectRef;
    private long count;
    private SubjectListAdapter subjectListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);

        board = (RadioGroup) findViewById(R.id.add_subject_board_selection);
        standard = (RadioGroup) findViewById(R.id.add_subject_standard_selection);
        subjectName = (EditText) findViewById(R.id.add_subject_name);
        add = (TextView) findViewById(R.id.add_new_subject_confirm);
        cancel = (TextView) findViewById(R.id.add_new_subject_cancel);
        subjectRef = FirebaseDatabase.getInstance().getReference().child("SubjectList");

        LoadingBar = new ProgressDialog(this);
        subjectListAdapter = new SubjectListAdapter();

        board.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = (RadioButton) radioGroup.findViewById(i);
                boardString = radioButton.getText().toString();
            }
        });

        standard.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = (RadioButton) radioGroup.findViewById(i);
                standardString = radioButton.getText().toString();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidations();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddSubjectActivity.super.onBackPressed();
            }
        });

    }

    private void checkValidations() {
        if (TextUtils.isEmpty(boardString))
        {
            Toast.makeText(AddSubjectActivity.this, "Please select a board...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(standardString))
        {
            Toast.makeText(AddSubjectActivity.this, "Please select a standard...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(subjectName.getText().toString().trim()))
        {
            Toast.makeText(AddSubjectActivity.this, "Please enter a valid name...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            LoadingBar.show();
            LoadingBar.setContentView(R.layout.progress_bar);
            LoadingBar.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            LoadingBar.setCanceledOnTouchOutside(true);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
            String currentDateAndTime = sdf.format(new Date());

            subjectRef.child(boardString).child(standardString).child("sub"+currentDateAndTime).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists())
                    {
                        count = snapshot.getChildrenCount();
                    }
                    else
                    {
                        count = 0;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            subjectListAdapter.setCount(count+1);
            subjectListAdapter.setName(subjectName.getText().toString().trim());

            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    subjectRef.child(boardString).child(standardString).setValue(subjectListAdapter)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                LoadingBar.dismiss();
                                Toast.makeText(AddSubjectActivity.this, "Subject uploaded successfully...", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                LoadingBar.dismiss();
                                Toast.makeText(AddSubjectActivity.this, "Error Occurred : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            subjectRef.child(boardString).child(standardString).addListenerForSingleValueEvent(valueEventListener);
        }
    }

}
package com.techtitudetribe.ncertkitabadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SubjectListActivity extends AppCompatActivity {
    private ImageView addSubject;
    private RecyclerView listsubject;
    private DatabaseReference subref;
    private String standard, board;
    private ProgressBar progressBar;
    private long count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_list);

        addSubject = findViewById(R.id.add_subject);
        listsubject = (RecyclerView) findViewById(R.id.subject_list);

        standard = getIntent().getStringExtra("standard");
        board = getIntent().getStringExtra("board");

        subref = FirebaseDatabase.getInstance().getReference().child("SubjectList").child(board).child(standard);
        progressBar = (ProgressBar) findViewById(R.id.subject_list_progress_bar);

        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        listsubject.setLayoutManager(layoutManager);

        subref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    progressBar.setVisibility(View.VISIBLE);
                    viewSubjectList();
                }
                else
                {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        addSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addSubjectActivity = new Intent(SubjectListActivity.this, AddSubjectActivity.class);
                startActivity(addSubjectActivity);
            }
        });

    }

    private void viewSubjectList() {

        Query sub_list = subref.orderByChild("count");

        FirebaseRecyclerAdapter<SubjectListAdapter,SubjectListviewholder> firebaseRecyclerAdapter =
        new FirebaseRecyclerAdapter<SubjectListAdapter, SubjectListviewholder>(
                SubjectListAdapter.class,
                R.layout.subject_list_layout,
                SubjectListviewholder.class,
                sub_list
        ) {
            @Override
            protected void populateViewHolder(SubjectListviewholder subjectListviewholder, SubjectListAdapter subjectListAdapter, int i) {

                subjectListviewholder.setName(subjectListAdapter.getName());
                progressBar.setVisibility(View.GONE);

                subjectListviewholder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(SubjectListActivity.this,BooksActivity.class);
                        intent.putExtra("title",subjectListAdapter.getName());
                        startActivity(intent);
                    }
                });

            }
        };
        listsubject.setAdapter(firebaseRecyclerAdapter);

    }

    public static class SubjectListviewholder extends RecyclerView.ViewHolder {

        View mView;
        public SubjectListviewholder(@NonNull View itemView)
        {
            super(itemView);
            mView = itemView;
        }

        public void setName(String name)
        {
            TextView subName = (TextView) mView.findViewById(R.id.subject_list_sub_name);
            subName.setText(name);
            TextView subLogo = (TextView) mView.findViewById(R.id.subject_list_sub_logo);
            subLogo.setText(String.valueOf(name.charAt(0)));
        }
    }
}
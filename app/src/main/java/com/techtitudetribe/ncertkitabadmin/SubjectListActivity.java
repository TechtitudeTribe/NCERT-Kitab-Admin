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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class SubjectListActivity extends AppCompatActivity {
    private ImageView addSubject;
    private RecyclerView listsubject;
    private DatabaseReference subref;
    private String category, board;
    private long count = '0';

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_list);

        addSubject = findViewById(R.id.add_subject);
        listsubject = (RecyclerView) findViewById(R.id.subject_list);

        category = getIntent().getStringExtra("category");
        board = getIntent().getStringExtra("board");

        subref = FirebaseDatabase.getInstance().getReference().child(board).child(category);

        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        listsubject.setLayoutManager(layoutManager);

        viewSubjectList();



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

        FirebaseRecyclerAdapter<Subject, SubjectListviewholder> recyclerAdapter =
                new FirebaseRecyclerAdapter<Subject, SubjectListviewholder>(
                        Subject.class,
                        R.layout.view_subject_list_layout,
                        SubjectListviewholder.class,
                        sub_list


                ) {
                    @Override
                    protected void populateViewHolder(SubjectListviewholder subjectListviewholder, Subject subject, int i) {


                    }

                };
        listsubject.setAdapter(recyclerAdapter);

    }

    private class SubjectListviewholder extends RecyclerView.ViewHolder {

        View mview;
        public SubjectListviewholder(@NonNull View itemView) {
            super(itemView);
        }

        public void setImage(String image, Context applicationContext) {

        }

        public void setName(String name) {
        }
    }
}
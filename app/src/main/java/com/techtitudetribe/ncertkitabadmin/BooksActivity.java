package com.techtitudetribe.ncertkitabadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

public class BooksActivity extends AppCompatActivity {
    private ImageView addPdf;
    private TextView chaptertext, notestext;
    private RecyclerView chapterlist, noteslist;
    private DatabaseReference bookRef, nootesRef;
    private String categoryname, chapter_name, chapter_no ;
    private ProgressBar progressBar;
    private Uri pdfUri;
    private StorageReference chapterpdfRef, notespdfRef;
    private long count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        addPdf = (ImageView) findViewById(R.id.add_pdf);
        chapterlist = (RecyclerView)findViewById(R.id.books_list);
        noteslist = (RecyclerView)findViewById(R.id.notes_list);

        chaptertext = (TextView)findViewById(R.id.books_text);
        notestext = (TextView)findViewById(R.id.notes_text);

        // categoryname = getIntent().getExtras().get("category").toString();
        chapterpdfRef = FirebaseStorage.getInstance().getReference().child("Subject Image").child("Chapter Pdf");
        bookRef = FirebaseDatabase.getInstance().getReference().child("Pdf Details");

        notespdfRef = FirebaseStorage.getInstance().getReference().child("Notes Image").child("Notes Pdf");
        nootesRef = FirebaseDatabase.getInstance().getReference().child("Notes Details");

        progressBar = (ProgressBar) findViewById(R.id.books_list_progress_bar);

        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        chapterlist.setLayoutManager(layoutManager);


        GridLayoutManager noteslayoutManger = new GridLayoutManager(getApplicationContext(),2);
        noteslist.setLayoutManager(noteslayoutManger);

     viewChapterList();
     viewNotesList();

     chaptertext.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             chaptertext.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
             chaptertext.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.creative_red));
             notestext.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
             notestext.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.black));

             chapterlist.setVisibility(View.VISIBLE);
             noteslist.setVisibility(View.GONE);
             progressBar.setVisibility(View.GONE);


         }
     });

        notestext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notestext.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                notestext.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.creative_red));
                chaptertext.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                chaptertext.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.black));

                noteslist.setVisibility(View.VISIBLE);
                chapterlist.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);


            }
        });



        addPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addpdfIntent = new Intent(BooksActivity.this, UploadPdfActivity.class);
                startActivity(addpdfIntent);
            }
        });
    }

    private void viewNotesList() {

        Query notes_list = nootesRef.orderByChild("count");
        FirebaseRecyclerAdapter<NotesAdapter, NotesViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<NotesAdapter, NotesViewHolder>(
                        NotesAdapter.class,
                        R.layout.notes_list_layout,
                        NotesViewHolder.class,
                        notes_list

                ) {
                    @Override
                    protected void populateViewHolder(NotesViewHolder notesViewHolder, NotesAdapter notesAdapter, int i) {
                        notesViewHolder.setChapterName(notesAdapter.chapterName);
                        notesViewHolder.setChapterNo(notesAdapter.chapterNo);

                        TextView dltNote = notesViewHolder.mView.findViewById(R.id.delete_notes);
                        TextView viewNote = notesViewHolder.mView.findViewById(R.id.show_notes);

                        String key = getRef(i).getKey();
                        dltNote.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                nootesRef.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(BooksActivity.this, "Notes Remove Successfully", Toast.LENGTH_SHORT).show();
                                        }else
                                        {
                                            String msg = task.getException().getMessage();
                                            Toast.makeText(BooksActivity.this, "error occoured!"+msg, Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                            }
                        });

                        viewNote.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent viewpdf = new Intent(BooksActivity.this, ViewPdfActivity.class);
                                viewpdf.putExtra("chapterName",notesAdapter.getChapterName());
                                viewpdf.putExtra("url",notesAdapter.getUrl());
                                startActivity(viewpdf);

                            }
                        });

                    }
                };
        noteslist.setAdapter(firebaseRecyclerAdapter);

    }

    private void viewChapterList() {

        Query book_list = bookRef.orderByChild("count");

        FirebaseRecyclerAdapter<BookAdapter, BookViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<BookAdapter, BookViewHolder>(
                        BookAdapter.class,
                        R.layout.book_list_layout,
                        BookViewHolder.class,
                        book_list

                ) {
                    @Override
                    protected void populateViewHolder(BookViewHolder bookViewHolder, BookAdapter bookAdapter, int i) {

                        bookViewHolder.setChapter_no(bookAdapter.chapter_no);
                        bookViewHolder.setChapter_name(bookAdapter.chapter_name);

                        TextView deletebtn = (TextView) bookViewHolder.mView.findViewById(R.id.delete_pdf);
                        TextView showpdf = (TextView) bookViewHolder.mView.findViewById(R.id.show_pdf);

                        String key = getRef(i).getKey();

                        deletebtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                bookRef.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(BooksActivity.this, "Pdf removed Successfully", Toast.LENGTH_SHORT).show();
                                        }else{
                                            String msg = task.getException().getMessage();
                                            Toast.makeText(BooksActivity.this, "error occoured!"+msg, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });

                        showpdf.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent viewpdf = new Intent(BooksActivity.this, ViewPdfActivity.class);
                                viewpdf.putExtra("chapter_name",bookAdapter.getChapter_name());
                                viewpdf.putExtra("booksUrl",bookAdapter.getBooksUrl());
                                startActivity(viewpdf);

                            }
                        });


                    }
                };
        chapterlist.setAdapter(firebaseRecyclerAdapter);
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        
        View mView;
        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setChapter_no(String chap_no) {

            TextView chapNo = (TextView) mView.findViewById(R.id.book_no);
            chapNo.setText(chap_no);
        }

        public void setChapter_name(String chap_name) {

            TextView chapName = (TextView) mView.findViewById(R.id.show_chap_name);
            chapName.setText(chap_name);
        }
    }

    public static class NotesViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setChapterName(String chapterName) {

            TextView notesName = (TextView) mView.findViewById(R.id.notes_name);
            notesName.setText(chapterName);

        }

        public void setChapterNo(String chapterNo) {

            TextView notesNo = (TextView) mView.findViewById(R.id.notes_no);
            notesNo.setText(chapterNo);
        }
    }
}
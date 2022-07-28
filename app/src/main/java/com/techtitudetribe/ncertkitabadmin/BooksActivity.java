package com.techtitudetribe.ncertkitabadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
    private RecyclerView chapterlist;
    private DatabaseReference bookRef;
    private String categoryname, chapter_name, chapter_no ;
    private ProgressBar progressBar;
    private Uri pdfUri;
    private StorageReference chapterpdfRef;
    private long count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        addPdf = (ImageView) findViewById(R.id.add_pdf);
        chapterlist = (RecyclerView)findViewById(R.id.books_list);

        categoryname = getIntent().getExtras().get("category").toString();
        chapterpdfRef = FirebaseStorage.getInstance().getReference().child("Subject Image").child("Chapter Pdf");
        bookRef = FirebaseDatabase.getInstance().getReference().child("Pdf Details");

        progressBar = (ProgressBar) findViewById(R.id.books_list_progress_bar);

        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        chapterlist.setLayoutManager(layoutManager);

        bookRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    progressBar.setVisibility(View.VISIBLE);
                    viewChapterList();
                }else
                {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

    private void viewChapterList() {

        Query book_list = bookRef.orderByChild("count");

        FirebaseRecyclerAdapter<BookAdapter, BookViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<BookAdapter, BookViewHolder>(tyyyyyyyyyyyyyyyy5656565656565656565656565
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


                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setType("pdf/");
                               // intent.setData(Uri.parse());
                                startActivity(intent);
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
}
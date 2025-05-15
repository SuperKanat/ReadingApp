package com.example.readingappv2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<BookModel> bookModels = new ArrayList<>();

    int bookImage = (R.drawable.pdfimage);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.basicRecyclerView);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setUpBookModels();

        BookAdapter adapter = new BookAdapter(this,
                bookModels);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setUpBookModels() {
        String[] bookNames = getResources().getStringArray(R.array.book_names);
        String[] bookFormat = getResources().getStringArray(R.array.book_format);
        String[] bookSize = getResources().getStringArray(R.array.book_size);
        String[] bookAuthor = getResources().getStringArray(R.array.book_author);

        for (int i = 0; i < bookNames.length; i++) {
            bookModels.add(new BookModel(bookNames[i],
                    bookFormat[0],
                    bookSize[i],
                    bookAuthor[i],
                    bookImage));
        }
    }
}
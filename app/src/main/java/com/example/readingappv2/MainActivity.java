package com.example.readingappv2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
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
        // Замените этот код на реальное получение PDF-файлов
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File[] pdfFiles = downloadsDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));

        if (pdfFiles != null) {
            for (File pdfFile : pdfFiles) {
                BookModel book = BookModel.fromPdfFile(this, pdfFile, R.drawable.default_book_cover);
                bookModels.add(book);
            }
        }
    }

}
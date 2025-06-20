package com.example.readingappv2;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    ArrayList<BookModel> bookModels = new ArrayList<>();

    int bookImage = (R.drawable.pdfimage);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);



        recyclerView = findViewById(R.id.basicRecyclerView);

//        RecyclerView recyclerView = findViewById(R.id.basicRecyclerView);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        new Thread(() -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                loadPdfsWithMediaStore();
            } else {
                loadPdfsLegacy();
            }

            // Обновление UI в главном потоке
            runOnUiThread(this::updateRecyclerView);
        }).start();
    }

    private void loadPdfsLegacy() {
        Log.d("PDF_LOAD", "Loading PDFs with legacy method");
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        if (downloadsDir == null || !downloadsDir.exists()) {
            Log.e("PDF_LOAD", "Downloads directory not found");
            return;
        }

        Log.d("PDF_LOAD", "Downloads dir: " + downloadsDir.getAbsolutePath());
        File[] pdfFiles = downloadsDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));

        if (pdfFiles == null) {
            Log.e("PDF_LOAD", "No PDF files found");
            return;
        }

        Log.d("PDF_LOAD", "Found " + pdfFiles.length + " PDF files");
        for (File pdfFile : pdfFiles) {
            Log.d("PDF_LOAD", "Processing: " + pdfFile.getName());
            BookModel book = BookModel.fromPdfFile(this, pdfFile, R.drawable.default_book_cover);
            bookModels.add(book);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void loadPdfsWithMediaStore() {
        Uri collection = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL);

        String[] projection = new String[]{
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.SIZE,
                MediaStore.Files.FileColumns.RELATIVE_PATH
        };

        String selection = MediaStore.Files.FileColumns.RELATIVE_PATH + " LIKE ? AND " +
                MediaStore.Files.FileColumns.MIME_TYPE + " = ?";

        String[] selectionArgs = new String[]{
                "%" + Environment.DIRECTORY_DOWNLOADS + "%",
                "application/pdf"
        };

        try (Cursor cursor = getContentResolver().query(
                collection,
                projection,
                selection,
                selectionArgs,
                null
        )) {
            if (cursor != null) {
                int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID);
                int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME);
                int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE);

                while (cursor.moveToNext()) {
                    long id = cursor.getLong(idColumn);
                    String name = cursor.getString(nameColumn);
                    long size = cursor.getLong(sizeColumn);
                    Uri uri = ContentUris.withAppendedId(collection, id);

                    BookModel book = BookModel.fromMediaStoreUri(this, uri, name, size, R.drawable.default_book_cover);
                    bookModels.add(book);
                }
            }
        } catch (Exception e) {
            Log.e("MediaStore", "Error loading PDFs", e);
        }
    }

    private void updateRecyclerView() {
        BookAdapter adapter = new BookAdapter(this, bookModels);
        recyclerView.setAdapter(adapter);
    }
}
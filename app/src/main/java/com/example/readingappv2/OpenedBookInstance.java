package com.example.readingappv2;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class OpenedBookInstance extends AppCompatActivity {
    private PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_opened_book_instance);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        pdfView = findViewById(R.id.firstxml); // Убедитесь, что ID правильный

        // Получаем URI или путь
        String uriString = getIntent().getStringExtra("BOOK_URI");
        String filePath = getIntent().getStringExtra("BOOK_PATH");

        if (uriString != null) {
            // Используем URI для Android 10+
            Uri uri = Uri.parse(uriString);
            pdfView.fromUri(uri)
                    .enableSwipe(true)
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .load();
        } else if (filePath != null) {
            // Используем путь для старых версий Android
            File file = new File(filePath);
            if (file.exists()) {
                pdfView.fromFile(file)
                        .enableSwipe(true)
                        .swipeHorizontal(false)
                        .enableDoubletap(true)
                        .load();
            } else {
                Log.e("PDF_VIEW", "File not found: " + filePath);
                Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e("PDF_VIEW", "No URI or path provided");
            Toast.makeText(this, "Error opening PDF", Toast.LENGTH_SHORT).show();
        }
    }
}



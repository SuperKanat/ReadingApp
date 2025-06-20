package com.example.readingappv2;



import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.text.DecimalFormat;

public class BookModel {
    String bookName;
    String bookFormat;
    String bookSize;
    String bookAuthor;
    int image;
    String filePath;

    public BookModel(String bookName, String bookFormat, String bookSize, String bookAuthor, int image, String filePath) {
        this.bookName = bookName;
        this.bookFormat = bookFormat;
        this.bookSize = bookSize;
        this.bookAuthor = bookAuthor;
        this.image = image;
        this.filePath = filePath;
    }

    public static BookModel fromPdfFile(Context context, File pdfFile, int defaultImage) {
        String size = formatFileSize(pdfFile.length());
        PdfMetadataExtractor.PdfMetadata metadata = new PdfMetadataExtractor.PdfMetadata();

        try {
            metadata = PdfMetadataExtractor.extract(context, Uri.fromFile(pdfFile));
        } catch (Exception e) {
            Log.e("BookModel", "Error extracting metadata: " + e.getMessage());
        }

        return new BookModel(
                !TextUtils.isEmpty(metadata.title) ? metadata.title : pdfFile.getName(),
                "PDF",
                size,
                !TextUtils.isEmpty(metadata.author) ? metadata.author : "Unknown Author",
                defaultImage,
                pdfFile.getAbsolutePath()
        );
    }

    public static BookModel fromMediaStoreUri(Context context, Uri uri, String name, long size, int defaultImage) {
        String sizeStr = formatFileSize(size);
        PdfMetadataExtractor.PdfMetadata metadata = PdfMetadataExtractor.extract(context, uri);

        // Получаем имя файла из Uri, если не передано
        String fileName = name;
        if (fileName == null || fileName.isEmpty()) {
            fileName = getFileNameFromUri(context, uri);
        }

        return new BookModel(
                getValidString(metadata.title, name),
                "PDF",
                sizeStr,
                getValidString(metadata.author, "Unknown Author"),
                defaultImage,
                uri.toString()  // Сохраняем URI как строку
        );
    }

    private static String getFileNameFromUri(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = context.getContentResolver().query(
                    uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        return result != null ? result : uri.getLastPathSegment();
    }

    private static String getValidString(String value, String fallback) {
        return (value != null && !value.trim().isEmpty()) ? value.trim() : fallback;
    }

    private static String formatFileSize(long size) {
        if (size <= 0) return "0 B";
        String[] units = {"B", "KB", "MB", "GB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.00").format(size / Math.pow(1024, digitGroups))
                + " " + units[digitGroups];
    }

    public String getBookName() {
        return bookName;
    }

    public String getBookFormat() {
        return bookFormat;
    }

    public String getBookSize() {
        return bookSize;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public int getImage() {
        return image;
    }

    public String getFilePath(){
        return filePath;
    }
}

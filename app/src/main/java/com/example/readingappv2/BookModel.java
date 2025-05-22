package com.example.readingappv2;



import android.content.Context;
import android.net.Uri;

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

        PdfMetadataExtractor.PdfMetadata metadata =
                PdfMetadataExtractor.extract(context, Uri.fromFile(pdfFile));

        return new BookModel(
                (metadata.title != null && !metadata.title.isEmpty()) ?
                        metadata.title : pdfFile.getName(),
                "PDF",
                size,
                (metadata.author != null && !metadata.author.isEmpty()) ?
                        metadata.author : "Unknown Author",
                defaultImage,
                pdfFile.getAbsolutePath()
        );
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

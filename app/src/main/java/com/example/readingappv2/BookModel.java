package com.example.readingappv2;

public class BookModel {
    String bookName;
    String bookFormat;
    String bookSize;
    String bookAuthor;
    int image;

    public BookModel(String bookName, String bookFormat, String bookSize, String bookAuthor, int image) {
        this.bookName = bookName;
        this.bookFormat = bookFormat;
        this.bookSize = bookSize;
        this.bookAuthor = bookAuthor;
        this.image = image;
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
}

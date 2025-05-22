package com.example.readingappv2;

import android.content.Context;
import android.net.Uri;

import com.tom_roush.pdfbox.android.PDFBoxResourceLoader;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.pdmodel.PDDocumentInformation;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class PdfMetadataExtractor {


    public static class PdfMetadata {
        public String title;
        public String author;
        public int pageCount;
    }

    public static PdfMetadata extract(Context context, Uri pdfUri) {
        PdfMetadata metadata = new PdfMetadata();
        try (InputStream inputStream = context.getContentResolver().openInputStream(pdfUri)) {
            byte[] pdfBytes = readAllBytes(inputStream);
            PDDocument document = PDDocument.load(pdfBytes);

            PDDocumentInformation info = document.getDocumentInformation();
            metadata.title = info.getTitle();
            metadata.author = info.getAuthor();
            metadata.pageCount = document.getNumberOfPages();

            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return metadata;
    }

    private static byte[] readAllBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, bytesRead);
        }
        return buffer.toByteArray();
    }
}

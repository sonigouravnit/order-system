package com.gourav.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.util.Map;


/**
 * Created by gouravsoni on 23/01/18.
 */

@Component
public class Pdfutil {
    private static Logger logger = LoggerFactory.getLogger(Pdfutil.class);

    private static Font font = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);


    public String generatePDF(String fileName, String title, Map<String, Object> map) {
        try {
            String filePath = fileName + ".pdf";
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            createPage(document, title, map);
            document.close();
            return filePath;
        } catch (Exception e) {
            logger.error("Error while generating pdf {} ", e.getMessage(), e);
            return null;
        }
    }

    private static void createPage(Document document, String title, Map<String, Object> map)
            throws DocumentException {
        Paragraph preface = new Paragraph();
        addEmptyLine(preface, 1);
        preface.add(new Paragraph(title, font));

        addEmptyLine(preface, 1);

        for (String key : map.keySet()) {
            preface.add(new Paragraph(key + "  =  " + map.get(key), font));
            addEmptyLine(preface, 3);
        }

        document.add(preface);
        document.newPage();
    }


    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}
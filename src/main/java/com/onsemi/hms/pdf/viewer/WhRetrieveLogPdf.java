package com.onsemi.hms.pdf.viewer;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.onsemi.hms.pdf.AbstractITextPdfViewPotrait;

public class WhRetrieveLogPdf extends AbstractITextPdfViewPotrait {

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document doc,
            PdfWriter writer, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String title = "WAREHOUSE MANAGEMENT - HARDWARE RETRIEVAL INFORMATION";

        Paragraph viewTitle = new Paragraph(title, fontOpenSans(10f, Font.BOLD));
        viewTitle.setAlignment(Element.ALIGN_CENTER);

        doc.add(viewTitle);

        Integer cellPadding = 15;

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100.0f);
        table.setWidths(new float[]{2.0f, 2.0f, 2.0f, 2.0f, 2.0f});
        table.setSpacingBefore(20);

        Font fontHeader = fontOpenSans(9f, Font.BOLD);
        fontHeader.setColor(BaseColor.WHITE);

        PdfPCell cellHeader = new PdfPCell();
        cellHeader.setBackgroundColor(BaseColor.DARK_GRAY);
        cellHeader.setPadding(cellPadding);

        Font fontContent = fontOpenSans();

        PdfPCell cellContent = new PdfPCell();
        cellContent.setPadding(cellPadding);

        //WhRetrieve whRetrieve = (WhRetrieve) model.get("whRetrieve");

        //Header
        cellHeader.setPhrase(new Phrase("Module Name", fontHeader));
        table.addCell(cellHeader);
        cellHeader.setPhrase(new Phrase("Timestamp", fontHeader));
        table.addCell(cellHeader);
        cellHeader.setPhrase(new Phrase("Status", fontHeader));
        table.addCell(cellHeader);
        cellHeader.setPhrase(new Phrase("Verified By", fontHeader));
        table.addCell(cellHeader);
        cellHeader.setPhrase(new Phrase("Verified Date", fontHeader));
        table.addCell(cellHeader);
        
        //1
        cellContent.setPhrase(new Phrase("A", fontContent));
        table.addCell(cellContent);
        cellContent.setPhrase(new Phrase("B", fontContent));
        table.addCell(cellContent);
        cellContent.setPhrase(new Phrase("C", fontContent));
        table.addCell(cellContent);
        cellContent.setPhrase(new Phrase("D", fontContent));
        table.addCell(cellContent);
        cellContent.setPhrase(new Phrase("E", fontContent));
        table.addCell(cellContent);
        
        //2
        cellContent.setPhrase(new Phrase("1", fontContent));
        table.addCell(cellContent);
        cellContent.setPhrase(new Phrase("2", fontContent));
        table.addCell(cellContent);
        cellContent.setPhrase(new Phrase("3", fontContent));
        table.addCell(cellContent);
        cellContent.setPhrase(new Phrase("4", fontContent));
        table.addCell(cellContent);
        cellContent.setPhrase(new Phrase("5", fontContent));
        table.addCell(cellContent);

        //6
//        cellHeader.setPhrase(new Phrase("Requested Date", fontHeader));
//        table.addCell(cellHeader);
//        cellContent.setPhrase(new Phrase(whRetrieve.getRequestedDate(), fontContent));
//        table.addCell(cellContent);
//        
        doc.add(table);
    }
}
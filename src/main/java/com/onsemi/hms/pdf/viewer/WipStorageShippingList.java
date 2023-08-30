/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.onsemi.hms.pdf.viewer;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.onsemi.hms.dao.WhWipDAO;
import com.onsemi.hms.model.WhWip0;
import com.onsemi.hms.pdf.AbstractPdfViewShipping;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author zbqb9x
 */
public class WipStorageShippingList extends AbstractPdfViewShipping {
    
    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document doc,
            PdfWriter writer, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        String data = (String) model.get("shippingList");
        WhWipDAO wipData = new WhWipDAO();
        String shipDate = wipData.getStorageShipDateByShipList(data);
        
        Integer cellPadd = 4;
        Integer cellPadding = 7;
        String spacing01 = "\n";
        String spacing02 = "\n\n";
        String spacing03 = "\n\n\n";

        String title = "WIP MANAGEMENT - STORAGE WIP SHIPMENT LIST TO SBN REL LAB";
        Paragraph viewTitle = new Paragraph(title, fontOpenSans(10f, Font.BOLD));
        viewTitle.setAlignment(Element.ALIGN_CENTER);
        doc.add(viewTitle);

        Paragraph viewSpace01 = new Paragraph(spacing01, fontOpenSans(8f, Font.NORMAL));
        PdfPTable table2 = new PdfPTable(3);
        table2.setWidthPercentage(100.0f);
        table2.setWidths(new float[]{0.5f, 3.5f, 3.5f});
        table2.setSpacingBefore(15);

        Font fontHeader2 = fontOpenSans(7f, Font.BOLD);
        fontHeader2.setColor(BaseColor.WHITE);
        PdfPCell cellHeader2 = new PdfPCell();
        cellHeader2.setBackgroundColor(BaseColor.DARK_GRAY);
        cellHeader2.setPadding(cellPadding);

        Font fontContent = fontOpenSans(8f, Font.NORMAL);
        PdfPCell cellContent = new PdfPCell();
        cellContent.setPadding(cellPadding);

        WhWipDAO dao = new WhWipDAO();
        List<WhWip0> whwip = dao.getAll0hourWipByShipList(data);

        int i = 0;
        if (whwip.isEmpty()) {
            doc.add(viewSpace01);
            String title2 = "NO DATA FOUND!!";
            Paragraph viewTitle2 = new Paragraph(title2, fontOpenSans(16f, Font.BOLD));
            viewTitle2.setAlignment(Element.ALIGN_CENTER);
            doc.add(viewTitle2);
        } else {
            while (i < whwip.size()) {
                if (i == 0) {
                    /* START TABLE LOG */
                    viewSpace01 = new Paragraph(spacing01, fontOpenSans(8f, Font.NORMAL));
                    viewSpace01.setAlignment(Element.ALIGN_LEFT);
                    doc.add(viewSpace01);
                    
                    String subTitle = "STORAGE WIP INFORMATION";
                    Paragraph viewSub = new Paragraph(subTitle, fontOpenSans(8f, Font.BOLD));
                    viewSub.setAlignment(Element.ALIGN_LEFT);
                    doc.add(viewSub);

                    //Header Log
                    cellHeader2.setPhrase(new Phrase("No. ", fontHeader2));
                    table2.addCell(cellHeader2);
                    cellHeader2.setPhrase(new Phrase("Box Number", fontHeader2));
                    table2.addCell(cellHeader2);
                    cellHeader2.setPhrase(new Phrase("Shipping Date", fontHeader2));
                    table2.addCell(cellHeader2);
                }
                cellContent.setPhrase(new Phrase(String.valueOf(i+1), fontContent));
                table2.addCell(cellContent);

                cellContent.setPhrase(new Phrase(whwip.get(i).getRmsEvent(), fontContent));
                table2.addCell(cellContent);

                cellContent.setPhrase(new Phrase(shipDate, fontContent));
                table2.addCell(cellContent);
                i++;
            }
            doc.add(table2);
        }
    }
    
}
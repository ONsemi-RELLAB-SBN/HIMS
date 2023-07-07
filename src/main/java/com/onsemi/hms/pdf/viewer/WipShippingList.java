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
import com.onsemi.hms.model.WhWip;
import com.onsemi.hms.pdf.AbstractPdfViewShipping;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author zbqb9x
 */
public class WipShippingList extends AbstractPdfViewShipping {

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document doc,
            PdfWriter writer, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        String data = (String) model.get("shippingList");
        WhWipDAO hehe = new WhWipDAO();
        String shipDate = hehe.getShipDateByShipList(data);
//        Date date = new SimpleDateFormat("d MMMM yyyy hh:mm:ss").parse(shipDate);
//        DateTimeFormatter format = DateTimeFormatter.ofPattern("d MMMM, yyyy hh:mm:ss");
//        LocalDate date = getDateFromString(shipDate, format);
        
//        System.out.println("HEHE >> " + date);
//        System.out.println("HEHE >> " + shipDate);
        
        Integer cellPadd = 4;
        Integer cellPadding = 7;
        String spacing01 = "\n";
        String spacing02 = "\n\n";
        String spacing03 = "\n\n\n";

        String title = "WIP MANAGEMENT - WIP SHIPMENT LIST TO SBN REL LAB";
        Paragraph viewTitle = new Paragraph(title, fontOpenSans(10f, Font.BOLD));
        viewTitle.setAlignment(Element.ALIGN_CENTER);
        doc.add(viewTitle);

        Paragraph viewSpace01 = new Paragraph(spacing01, fontOpenSans(8f, Font.NORMAL));
        /*
        viewSpace01.setAlignment(Element.ALIGN_LEFT);
        doc.add(viewSpace01);

        PdfPTable table01 = new PdfPTable(2);
        table01.setWidthPercentage(100.0f);
        table01.setWidths(new float[]{8.6f, 2.4f});
        table01.setSpacingBefore(20);

        Font fontHeader = fontOpenSans(10f, Font.BOLD);

        PdfPCell cellHeader = new PdfPCell();
        cellHeader.setPadding(cellPadd);
        cellHeader.setBorder(Rectangle.NO_BORDER);
        cellHeader.setPaddingLeft(80.0f);

        Font fontContent01 = fontOpenSans();

        PdfPCell cellContent01 = new PdfPCell();
        cellContent01.setPadding(cellPadd);
        cellContent01.setBorder(Rectangle.NO_BORDER);
        cellContent01.setPaddingLeft(120.0f);

        PdfContentByte cb = writer.getDirectContent();
        Barcode128 code128 = new Barcode128();
        code128.setGenerateChecksum(true);
        code128.setFont(null);
        code128.setCode(data);
        code128.setSize(cellPadd);
        Image code128Image = code128.createImageWithBarcode(cb, null, null);
        PdfPCell barcode = new PdfPCell(code128Image);
        barcode.setBorder(Rectangle.NO_BORDER);
        barcode.setPaddingLeft(80.0f);
        barcode.setPaddingTop(0f);

        table01.addCell(barcode);
        cellContent01.setPhrase(new Phrase("", fontHeader));
        table01.addCell(cellContent01);

        cellHeader.setPhrase(new Phrase("Shipping List: " + data, fontHeader));
        table01.addCell(cellHeader);
        cellContent01.setPhrase(new Phrase("", fontHeader));
        table01.addCell(cellContent01);

        cellHeader.setPhrase(new Phrase("Shipping Date: " + shipDate, fontHeader));
        table01.addCell(cellHeader);
        cellContent01.setPhrase(new Phrase("", fontContent01));
        table01.addCell(cellContent01);

        doc.add(table01);
        */

        PdfPTable table2 = new PdfPTable(5);
        table2.setWidthPercentage(100.0f);
        table2.setWidths(new float[]{0.5f, 2.5f, 1.2f, 1.2f, 2.5f});
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
        List<WhWip> whwip = dao.getWhWipByShipment(data);

        int i = 0;
        if (whwip.isEmpty()) {
            doc.add(viewSpace01);
            String title2 = "NO DATA FOUND!!";
            Paragraph viewTitle2 = new Paragraph(title2, fontOpenSans(16f, Font.BOLD));
            viewTitle2.setAlignment(Element.ALIGN_CENTER);
            doc.add(viewTitle2);
        } else {
            while (i < whwip.size()) {
//                String shippingList = whwip.get(i).getShippingList();
                if (i == 0) {
                    /* START TABLE LOG */
                    viewSpace01 = new Paragraph(spacing01, fontOpenSans(8f, Font.NORMAL));
                    viewSpace01.setAlignment(Element.ALIGN_LEFT);
                    doc.add(viewSpace01);
                    
                    String subTitle = "WIP INFORMATION";
                    Paragraph viewSub = new Paragraph(subTitle, fontOpenSans(8f, Font.BOLD));
                    viewSub.setAlignment(Element.ALIGN_LEFT);
                    doc.add(viewSub);

                    //Header Log
                    cellHeader2.setPhrase(new Phrase("No. ", fontHeader2));
                    table2.addCell(cellHeader2);
                    cellHeader2.setPhrase(new Phrase("RMS Event", fontHeader2));
                    table2.addCell(cellHeader2);
                    cellHeader2.setPhrase(new Phrase("Intervals", fontHeader2));
                    table2.addCell(cellHeader2);
                    cellHeader2.setPhrase(new Phrase("Quantity", fontHeader2));
                    table2.addCell(cellHeader2);
                    cellHeader2.setPhrase(new Phrase("Shipping Date", fontHeader2));
                    table2.addCell(cellHeader2);
                }
                cellContent.setPhrase(new Phrase(String.valueOf(i+1), fontContent));
                table2.addCell(cellContent);

                cellContent.setPhrase(new Phrase(whwip.get(i).getRmsEvent(), fontContent));
                table2.addCell(cellContent);

                cellContent.setPhrase(new Phrase(whwip.get(i).getIntervals(), fontContent));
                table2.addCell(cellContent);

                cellContent.setPhrase(new Phrase(whwip.get(i).getShipQuantity(), fontContent));
                table2.addCell(cellContent);

                cellContent.setPhrase(new Phrase(shipDate, fontContent));
                table2.addCell(cellContent);
                i++;
            }
            doc.add(table2);
        }
    }
    
}
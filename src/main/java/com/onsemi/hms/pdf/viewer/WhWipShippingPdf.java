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
public class WhWipShippingPdf extends AbstractPdfViewShipping {

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document doc,
            PdfWriter writer, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        System.out.println("MASUK DEKAT CODE WhWipShippingPdf - buildPdfDocument");

        String title = "WIP MANAGEMENT - WIP SHIPMENT LIST TO SBN FACTORY INFORMATION";
        Paragraph viewTitle = new Paragraph(title, fontOpenSans(10f, Font.BOLD));
        viewTitle.setAlignment(Element.ALIGN_CENTER);
        doc.add(viewTitle);

        Integer cellPadding = 7;

        PdfPTable table2 = new PdfPTable(4);
        table2.setWidthPercentage(100.0f);
//        table2.setWidths(new float[]{2.5f, 2.5f, 1.2f, 3.8f});
        table2.setWidths(new float[]{2.5f, 2.5f, 1.2f, 1.2f});
        table2.setSpacingBefore(15);

        Font fontHeader2 = fontOpenSans(7f, Font.BOLD);
        fontHeader2.setColor(BaseColor.WHITE);
        PdfPCell cellHeader2 = new PdfPCell();
        cellHeader2.setBackgroundColor(BaseColor.DARK_GRAY);
        cellHeader2.setPadding(cellPadding);

        Font fontContent = fontOpenSans(8f, Font.NORMAL);
        PdfPCell cellContent = new PdfPCell();
        cellContent.setPadding(cellPadding);

//        boolean flag = false;
        List<WhWip> whwip = (List<WhWip>) model.get("whWip");
//        List<WhWip> whwip = (List<WhWip>) model.get("whWipPdf");
        System.out.println("SINI DA LEPAS DA SETUP");

        System.out.println("hehehe >>> " + whwip.size());

        int i = 0;
        if (whwip.isEmpty()) {
            System.out.println("SINI TAKDE DATA DIJUMPAI");
            String title4 = "\n\n\n";
            Paragraph viewTitle4 = new Paragraph(title4, fontOpenSans(8f, Font.BOLD));
            viewTitle4.setAlignment(Element.ALIGN_LEFT);
            doc.add(viewTitle4);
                
            String title2 = "NO DATA FOUND!!";
            Paragraph viewTitle2 = new Paragraph(title2, fontOpenSans(16f, Font.BOLD));
            viewTitle2.setAlignment(Element.ALIGN_CENTER);
            doc.add(viewTitle2);
        } else {
            while (i < whwip.size()) {
                String shippingList = whwip.get(i).getShippingList();
                if (i == 0) {
                    System.out.println("START BACA dATA HEADER");
                    /* START TABLE LOG */
                    String title4 = "\n\n";
                    Paragraph viewTitle4 = new Paragraph(title4, fontOpenSans(8f, Font.BOLD));
                    viewTitle4.setAlignment(Element.ALIGN_LEFT);
                    doc.add(viewTitle4);
                    String title5 = "WIP INFORMATION";
                    Paragraph viewTitle5 = new Paragraph(title5, fontOpenSans(8f, Font.BOLD));
                    viewTitle5.setAlignment(Element.ALIGN_LEFT);
                    doc.add(viewTitle5);

                    //Header Log
                    cellHeader2.setPhrase(new Phrase("Shipping List", fontHeader2));
                    table2.addCell(cellHeader2);
                    cellHeader2.setPhrase(new Phrase("RMS Event", fontHeader2));
                    table2.addCell(cellHeader2);
                    cellHeader2.setPhrase(new Phrase("Intervals", fontHeader2));
                    table2.addCell(cellHeader2);
                    cellHeader2.setPhrase(new Phrase("Quantity", fontHeader2));
                    table2.addCell(cellHeader2);
                }
                System.out.println("SNIN START VACA DATA");
                cellContent.setPhrase(new Phrase(shippingList, fontContent));
                table2.addCell(cellContent);

                cellContent.setPhrase(new Phrase(whwip.get(i).getRmsEvent(), fontContent));
                table2.addCell(cellContent);

                cellContent.setPhrase(new Phrase(whwip.get(i).getIntervals(), fontContent));
                table2.addCell(cellContent);

                cellContent.setPhrase(new Phrase(whwip.get(i).getShipQuantity(), fontContent));
                table2.addCell(cellContent);
                i++;
                System.out.println("DAH ABES BACA SATU2 DATA");
            }
            doc.add(table2);
        }
    }

}
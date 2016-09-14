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
import com.onsemi.hms.model.WhInventoryHist;
import com.onsemi.hms.pdf.AbstractITextPdfViewPotrait;
import java.util.List;

public class WhInventoryHistPdf extends AbstractITextPdfViewPotrait {

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document doc,
            PdfWriter writer, HttpServletRequest request, HttpServletResponse response)
            throws Exception {        
        String title = "WAREHOUSE MANAGEMENT - HARDWARE INVENTORY HISTORY";

        Paragraph viewTitle = new Paragraph(title, fontOpenSans(10f, Font.BOLD));
        viewTitle.setAlignment(Element.ALIGN_CENTER);

        doc.add(viewTitle);

        Integer cellPadding = 8;

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100.0f);
        table.setWidths(new float[]{2.0f, 4.0f});
        table.setSpacingBefore(20);

        Font fontHeader = fontOpenSans(9f, Font.BOLD);
        fontHeader.setColor(BaseColor.WHITE);

        PdfPCell cellHeader = new PdfPCell();
        cellHeader.setBackgroundColor(BaseColor.DARK_GRAY);
        cellHeader.setPadding(cellPadding);

        Font fontContent = fontOpenSans();

        PdfPCell cellContent = new PdfPCell();
        cellContent.setPadding(cellPadding);


        List<WhInventoryHist> whInventoryList = (List<WhInventoryHist>) (WhInventoryHist) model.get("whInventoryHist");
        
        int count = 0;
        
        while(count<whInventoryList.size()) {
            if(count == 0) {
                //1
                cellHeader.setPhrase(new Phrase("Material Pass No.", fontHeader));
                table.addCell(cellHeader);
                cellContent.setPhrase(new Phrase(whInventoryList.get(count).getMaterialPassNo(), fontContent));
                table.addCell(cellContent);

                //2
                cellHeader.setPhrase(new Phrase("Material Pass Expiry Date", fontHeader));
                table.addCell(cellHeader);
                cellContent.setPhrase(new Phrase(whInventoryList.get(count).getMaterialPassExpiry(), fontContent));
                table.addCell(cellContent);

                if ("Motherboard".equals(whInventoryList.get(count).getEquipmentType())) {
                    //3
                    cellHeader.setPhrase(new Phrase("Motherboard ID", fontHeader));
                    table.addCell(cellHeader);
                    cellContent.setPhrase(new Phrase(whInventoryList.get(count).getEquipmentId(), fontContent));
                    table.addCell(cellContent);
                } else if ("Stencil".equals(whInventoryList.get(count).getEquipmentType())) {
                    //3
                    cellHeader.setPhrase(new Phrase("Stencil ID", fontHeader));
                    table.addCell(cellHeader);
                    cellContent.setPhrase(new Phrase(whInventoryList.get(count).getEquipmentId(), fontContent));
                    table.addCell(cellContent);
                } else if ("Tray".equals(whInventoryList.get(count).getEquipmentType())) {
                    //3
                    cellHeader.setPhrase(new Phrase("Tray Type", fontHeader));
                    table.addCell(cellHeader);
                    cellContent.setPhrase(new Phrase(whInventoryList.get(count).getEquipmentId(), fontContent));
                    table.addCell(cellContent);

                    //4
                    cellHeader.setPhrase(new Phrase("Quantity", fontHeader));
                    table.addCell(cellHeader);
                    cellContent.setPhrase(new Phrase(whInventoryList.get(count).getQuantity(), fontContent));
                    table.addCell(cellContent);
                } else if ("PCB".equals(whInventoryList.get(count).getEquipmentType())) {
                    //3
                    cellHeader.setPhrase(new Phrase("PCB Name", fontHeader));
                    table.addCell(cellHeader);
                    cellContent.setPhrase(new Phrase(whInventoryList.get(count).getEquipmentId(), fontContent));
                    table.addCell(cellContent);

                    //4
                    cellHeader.setPhrase(new Phrase("Quantity", fontHeader));
                    table.addCell(cellHeader);
                    cellContent.setPhrase(new Phrase(whInventoryList.get(count).getQuantity(), fontContent));
                    table.addCell(cellContent);
                } else {
                    //3
                    cellHeader.setPhrase(new Phrase("Equipment ID", fontHeader));
                    table.addCell(cellHeader);
                    cellContent.setPhrase(new Phrase(whInventoryList.get(count).getEquipmentId(), fontContent));
                    table.addCell(cellContent);
                }
                //5
                cellHeader.setPhrase(new Phrase("Requested By", fontHeader));
                table.addCell(cellHeader);
                cellContent.setPhrase(new Phrase(whInventoryList.get(count).getRequestedBy() + " [" + whInventoryList.get(count).getRequestedEmail() + "]", fontContent));
                table.addCell(cellContent);

                //6
                cellHeader.setPhrase(new Phrase("CDARS Requested Date", fontHeader));
                table.addCell(cellHeader);
                cellContent.setPhrase(new Phrase(whInventoryList.get(count).getRequestedDate(), fontContent));
                table.addCell(cellContent);

                //6
                cellHeader.setPhrase(new Phrase("Shipping Date", fontHeader));
                table.addCell(cellHeader);
                cellContent.setPhrase(new Phrase(whInventoryList.get(count).getShippingDate(), fontContent));
                table.addCell(cellContent);

                //7
                cellHeader.setPhrase(new Phrase("HMS Received Date", fontHeader));
                table.addCell(cellHeader);
                cellContent.setPhrase(new Phrase(whInventoryList.get(count).getReceivedDate(), fontContent));
                table.addCell(cellContent);

                doc.add(table);
            }
            PdfPTable table2 = new PdfPTable(2);
            table2.setWidthPercentage(100.0f);
            table2.setWidths(new float[]{2.0f, 4.0f});
            table2.setSpacingBefore(20);

            if(whInventoryList.size() > 0) {
                //8
                cellHeader.setPhrase(new Phrase("Verified By", fontHeader));
                table2.addCell(cellHeader);
                cellContent.setPhrase(new Phrase(whInventoryList.get(count).getUserVerify(), fontContent));
                table2.addCell(cellContent);

                //9
                cellHeader.setPhrase(new Phrase("Verification Date", fontHeader));
                table2.addCell(cellHeader);
                cellContent.setPhrase(new Phrase(whInventoryList.get(count).getDateVerify(), fontContent));
                table2.addCell(cellContent);

                //10
                cellHeader.setPhrase(new Phrase("Status", fontHeader));
                table2.addCell(cellHeader);
                cellContent.setPhrase(new Phrase(whInventoryList.get(count).getDateVerify(), fontContent));
                table2.addCell(cellContent);

                //10
                cellHeader.setPhrase(new Phrase("Inventory Date", fontHeader));
                table2.addCell(cellHeader);
                cellContent.setPhrase(new Phrase(whInventoryList.get(count).getInventoryDate(), fontContent));
                table2.addCell(cellContent);

                //11
                cellHeader.setPhrase(new Phrase("Inventory Location", fontHeader));
                table2.addCell(cellHeader);
                cellContent.setPhrase(new Phrase(whInventoryList.get(count).getInventoryLoc(), fontContent));
                table2.addCell(cellContent);

                //12
                cellHeader.setPhrase(new Phrase("Inventory By", fontHeader));
                table2.addCell(cellHeader);
                cellContent.setPhrase(new Phrase(whInventoryList.get(count).getInventoryBy(), fontContent));
                table2.addCell(cellContent);

                //13
                cellHeader.setPhrase(new Phrase("Status", fontHeader));
                table2.addCell(cellHeader);
                cellContent.setPhrase(new Phrase(whInventoryList.get(count).getInventoryStatus(), fontContent));
                table2.addCell(cellContent);

                doc.add(table2);
            }
            count++;
        }
        
        
    }
}
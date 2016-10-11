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
import com.onsemi.hms.model.WhRequestLog;
import com.onsemi.hms.pdf.AbstractITextPdfViewPotrait;
import com.onsemi.hms.tools.SpmlUtil;
import java.util.List;

public class WhRequestLogPdf extends AbstractITextPdfViewPotrait {

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document doc,
            PdfWriter writer, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

//        doc = new Document(PageSize.A4);
//        PageSize.A4.rotate();
        
        String title = "WAREHOUSE MANAGEMENT - HARDWARE REQUEST INFORMATION\n\n";
        Paragraph viewTitle = new Paragraph(title, fontOpenSans(10f, Font.BOLD));
        viewTitle.setAlignment(Element.ALIGN_CENTER);
        doc.add(viewTitle);

        Integer cellPadding = 10;

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100.0f);
        table.setWidths(new float[]{3.0f, 4.0f});
        table.setSpacingBefore(8);
        
        PdfPTable table2 = new PdfPTable(4);
        table2.setWidthPercentage(100.0f);
        table2.setWidths(new float[]{2.5f, 2.5f, 1.5f, 2.5f});
        table2.setSpacingBefore(15);
        
        PdfPTable table3 = new PdfPTable(5);
        table3.setWidthPercentage(100.0f);
        table3.setWidths(new float[]{2.5f, 2.5f, 2.5f, 2.5f, 2.5f});
        table3.setSpacingBefore(15);

        Font fontHeader = fontOpenSans(8f, Font.BOLD);
        fontHeader.setColor(BaseColor.BLACK);
        PdfPCell cellHeader = new PdfPCell();
        cellHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellHeader.setPadding(cellPadding);
        
        Font fontHeader2 = fontOpenSans(7f, Font.BOLD);
        fontHeader2.setColor(BaseColor.WHITE);
        PdfPCell cellHeader2 = new PdfPCell();
        cellHeader2.setBackgroundColor(BaseColor.DARK_GRAY);
        cellHeader2.setPadding(cellPadding);
        
        Font fontHeader3 = fontOpenSans(7f, Font.BOLD);
        fontHeader3.setColor(BaseColor.WHITE);
        PdfPCell cellHeader3 = new PdfPCell();
        cellHeader3.setBackgroundColor(BaseColor.GRAY);
        cellHeader3.setPadding(cellPadding);

        Font fontContent = fontOpenSans(8f, Font.NORMAL);
        PdfPCell cellContent = new PdfPCell();
        cellContent.setPadding(cellPadding);
        
        Font fontContent3 = fontOpenSans(6.5f, Font.NORMAL);
        PdfPCell cellContent3 = new PdfPCell();
        cellContent3.setPadding(cellPadding);
        
        List<WhRequestLog> whHistoryList = (List<WhRequestLog>) model.get("whRequestLog");

        boolean flag = false;
        System.out.println("masuk 1");
        int i = 0;
        System.out.println("whHistoryList.size() : " + whHistoryList.size());
        while(i<whHistoryList.size()) {
            System.out.println("masuk 2");
            String moduleName = whHistoryList.get(i).getModuleName();
            if(moduleName.equals("hms_wh_retrieval_list")) {
                moduleName = "Retrieval";
            } else if(moduleName.equals("hms_wh_request_list")) {
                moduleName = "Request";
            } else if(moduleName.equals("hms_wh_inventory_list")) {
                moduleName = "Inventory";
            } else if(moduleName.equals("hms_wh_shipping_list") || moduleName.equals("hms_wh_mp_list")) {
                moduleName = "Shipping";
            }
            
            if(i==0) {
                System.out.println("masuk 3");
                //1
                cellHeader.setPhrase(new Phrase("Material Pass No.", fontHeader));
                table.addCell(cellHeader);
                cellContent.setPhrase(new Phrase(whHistoryList.get(i).getMaterialPassNo(), fontContent));
                table.addCell(cellContent);

                //2
                cellHeader.setPhrase(new Phrase("Material Pass Expiry Date", fontHeader));
                table.addCell(cellHeader);
                cellContent.setPhrase(new Phrase(whHistoryList.get(i).getMaterialPassExpiry(), fontContent));
                table.addCell(cellContent);

                if ("Motherboard".equals(whHistoryList.get(i).getEquipmentType())) {
                    //3
                    cellHeader.setPhrase(new Phrase("Motherboard ID", fontHeader));
                    table.addCell(cellHeader);
                    cellContent.setPhrase(new Phrase(whHistoryList.get(i).getEquipmentId(), fontContent));
                    table.addCell(cellContent);
                } else if ("Stencil".equals(whHistoryList.get(i).getEquipmentType())) {
                    //3
                    cellHeader.setPhrase(new Phrase("Stencil ID", fontHeader));
                    table.addCell(cellHeader);
                    cellContent.setPhrase(new Phrase(whHistoryList.get(i).getEquipmentId(), fontContent));
                    table.addCell(cellContent);
                } else if ("Tray".equals(whHistoryList.get(i).getEquipmentType())) {
                    //3
                    cellHeader.setPhrase(new Phrase("Tray Type", fontHeader));
                    table.addCell(cellHeader);
                    cellContent.setPhrase(new Phrase(whHistoryList.get(i).getEquipmentId(), fontContent));
                    table.addCell(cellContent);

                    //4
                    cellHeader.setPhrase(new Phrase("Quantity", fontHeader));
                    table.addCell(cellHeader);
                    cellContent.setPhrase(new Phrase(whHistoryList.get(i).getQuantity(), fontContent));
                    table.addCell(cellContent);
                } else if ("PCB".equals(whHistoryList.get(i).getEquipmentType())) {
                    //3
                    cellHeader.setPhrase(new Phrase("PCB Name", fontHeader));
                    table.addCell(cellHeader);
                    cellContent.setPhrase(new Phrase(whHistoryList.get(i).getEquipmentId(), fontContent));
                    table.addCell(cellContent);

                    cellHeader.setPhrase(new Phrase("Qual A", fontHeader));
                    table.addCell(cellHeader);
                    cellContent.setPhrase(new Phrase(whHistoryList.get(i).getPcbA() + " (Qty: " + whHistoryList.get(i).getQtyQualA() +  ")", fontContent));
                    table.addCell(cellContent);

                    cellHeader.setPhrase(new Phrase("Qual B", fontHeader));
                    table.addCell(cellHeader);
                    cellContent.setPhrase(new Phrase(whHistoryList.get(i).getPcbB() + " (Qty: " + whHistoryList.get(i).getQtyQualB() +  ")", fontContent));
                    table.addCell(cellContent);

                    cellHeader.setPhrase(new Phrase("Qual C", fontHeader));
                    table.addCell(cellHeader);
                    cellContent.setPhrase(new Phrase(whHistoryList.get(i).getPcbC() + " (Qty: " + whHistoryList.get(i).getQtyQualC() +  ")", fontContent));
                    table.addCell(cellContent);

                    cellHeader.setPhrase(new Phrase("Qual Control", fontHeader));
                    table.addCell(cellHeader);
                    cellContent.setPhrase(new Phrase(whHistoryList.get(i).getPcbControl() + " (Qty: " + whHistoryList.get(i).getQtyControl()+  ")", fontContent));
                    table.addCell(cellContent);

                    //4
                    cellHeader.setPhrase(new Phrase("Total Quantity", fontHeader));
                    table.addCell(cellHeader);
                    cellContent.setPhrase(new Phrase(whHistoryList.get(i).getQuantity(), fontContent));
                    table.addCell(cellContent);
                } else {
                    //3
                    cellHeader.setPhrase(new Phrase("Hardware ID", fontHeader));
                    table.addCell(cellHeader);
                    cellContent.setPhrase(new Phrase(whHistoryList.get(i).getEquipmentId(), fontContent));
                    table.addCell(cellContent);
                }

                //5
                cellHeader.setPhrase(new Phrase("Requested By", fontHeader));
                table.addCell(cellHeader);
                cellContent.setPhrase(new Phrase(whHistoryList.get(i).getRequestedBy() + ", " + whHistoryList.get(i).getRequestedEmail(), fontContent));
                table.addCell(cellContent);

                //6
                cellHeader.setPhrase(new Phrase("Requested Date", fontHeader));
                table.addCell(cellHeader);
                cellContent.setPhrase(new Phrase(whHistoryList.get(i).getRequestedDate(), fontContent));
                table.addCell(cellContent);
                
                //7
                cellHeader.setPhrase(new Phrase("Inventory", fontHeader));
                table.addCell(cellHeader);
                cellContent.setPhrase(new Phrase("Rack : " + whHistoryList.get(i).getInventoryRack() + ", Shelf : " + whHistoryList.get(i).getInventoryShelf(), fontContent));
                table.addCell(cellContent);
                
                //8
                cellHeader.setPhrase(new Phrase("HMS Received Date", fontHeader));
                table.addCell(cellHeader);
                cellContent.setPhrase(new Phrase(whHistoryList.get(i).getReceivedDate(), fontContent));
                table.addCell(cellContent);

                doc.add(table);
                
                /* START TABLE TIMELAPSE */
                String title2 = "\n\n\n";
                Paragraph viewTitle2 = new Paragraph(title2, fontOpenSans(8f, Font.BOLD));
                viewTitle2.setAlignment(Element.ALIGN_LEFT);
                doc.add(viewTitle2);
                String title3 = "HARDWARE REQUEST TIMELAPSE";
                Paragraph viewTitle3 = new Paragraph(title3, fontOpenSans(8f, Font.BOLD));
                viewTitle3.setAlignment(Element.ALIGN_LEFT);
                doc.add(viewTitle3);
                
                //Header Timelapse
                cellHeader3.setPhrase(new Phrase("Request - Received", fontHeader3));
                table3.addCell(cellHeader3);
                cellHeader3.setPhrase(new Phrase("Received - Verify Barcode", fontHeader3));
                table3.addCell(cellHeader3);
                cellHeader3.setPhrase(new Phrase("Verify Barcode - Verify Inventory", fontHeader3));
                table3.addCell(cellHeader3);
                cellHeader3.setPhrase(new Phrase("Received - Verify Inventory", fontHeader3));
                table3.addCell(cellHeader3);
                cellHeader3.setPhrase(new Phrase("Request - Verify Inventory", fontHeader3));
                table3.addCell(cellHeader3);
            }
            
            System.out.println("masuk 4");
            
            if(flag == false) {
                System.out.println("masuk 5");
                if(whHistoryList.get(i).getRequestReceive()!=null) {
                    cellContent3.setPhrase(new Phrase(whHistoryList.get(i).getRequestReceive(), fontContent3));
                    table3.addCell(cellContent3);
                } else {
                    String temp = SpmlUtil.nullToDashString(whHistoryList.get(i).getRequestReceive());
                    cellContent3.setPhrase(new Phrase(temp, fontContent3));
                    table3.addCell(cellContent3);
                }
                
                if(whHistoryList.get(i).getReceiveVerify1()!=null) {
                    cellContent3.setPhrase(new Phrase(whHistoryList.get(i).getReceiveVerify1(), fontContent3));
                    table3.addCell(cellContent3);
                } else {
                    String temp = SpmlUtil.nullToDashString(whHistoryList.get(i).getReceiveVerify1());
                    cellContent3.setPhrase(new Phrase(temp, fontContent3));
                    table3.addCell(cellContent3);
                }
                
                if(whHistoryList.get(i).getVerify1Verify2()!=null) {
                    cellContent3.setPhrase(new Phrase(whHistoryList.get(i).getVerify1Verify2(), fontContent3));
                    table3.addCell(cellContent3);
                } else {
                    String temp = SpmlUtil.nullToDashString(whHistoryList.get(i).getVerify1Verify2());
                    cellContent3.setPhrase(new Phrase(temp, fontContent3));
                    table3.addCell(cellContent3);
                }
                
                if(whHistoryList.get(i).getReceiveVerify2()!=null) {
                    cellContent3.setPhrase(new Phrase(whHistoryList.get(i).getReceiveVerify2(), fontContent3));
                    table3.addCell(cellContent3);
                } else {
                    String temp = SpmlUtil.nullToDashString(whHistoryList.get(i).getReceiveVerify2());
                    cellContent3.setPhrase(new Phrase(temp, fontContent3));
                    table3.addCell(cellContent3);
                }
                
                if(whHistoryList.get(i).getRequestVerify2()!=null) {
                    cellContent3.setPhrase(new Phrase(whHistoryList.get(i).getRequestVerify2(), fontContent3));
                    table3.addCell(cellContent3);
                } else {
                    String temp = SpmlUtil.nullToDashString(whHistoryList.get(i).getRequestVerify2());
                    cellContent3.setPhrase(new Phrase(temp, fontContent3));
                    table3.addCell(cellContent3);
                }
                
                doc.add(table3);
                flag = true;
            }             
            
            System.out.println("masuk 6");
            if(i==0) {
                System.out.println("masuk 7");
                /* START TABLE LOG */
                String title4 = "\n\n\n";
                Paragraph viewTitle4 = new Paragraph(title4, fontOpenSans(8f, Font.BOLD));
                viewTitle4.setAlignment(Element.ALIGN_LEFT);
                doc.add(viewTitle4);
                String title5 = "HARDWARE REQUEST LOG";
                Paragraph viewTitle5 = new Paragraph(title5, fontOpenSans(8f, Font.BOLD));
                viewTitle5.setAlignment(Element.ALIGN_LEFT);
                doc.add(viewTitle5);
                
                //Header Log
                cellHeader2.setPhrase(new Phrase("Status", fontHeader2));
                table2.addCell(cellHeader2);
                cellHeader2.setPhrase(new Phrase("Timestamp", fontHeader2));
                table2.addCell(cellHeader2);
                cellHeader2.setPhrase(new Phrase("Module Name", fontHeader2));
                table2.addCell(cellHeader2);
                cellHeader2.setPhrase(new Phrase("Verified By", fontHeader2));
                table2.addCell(cellHeader2);
            }
            System.out.println("masuk 8");
            cellContent.setPhrase(new Phrase(whHistoryList.get(i).getLogStatus(), fontContent));
            table2.addCell(cellContent);
            
            cellContent.setPhrase(new Phrase(whHistoryList.get(i).getTimestamp(), fontContent));
            table2.addCell(cellContent);
            
            cellContent.setPhrase(new Phrase(moduleName, fontContent));
            table2.addCell(cellContent);
            
            String userVerify = whHistoryList.get(i).getLogVerifyBy();
            if(userVerify == null || userVerify.equals("null")) {
                userVerify = SpmlUtil.nullToDashString(userVerify);
            }
            cellContent.setPhrase(new Phrase(userVerify, fontContent));
            table2.addCell(cellContent);
            i++;
            System.out.println("masuk 9");
        }        
        System.out.println("masuk 10");
        doc.add(table2);        
    }
}
//package com.onsemi.hms.config;
//
//import com.onsemi.hms.dao.WhRequestDAO;
//import com.onsemi.hms.model.WhRequest;
//import com.onsemi.hms.tools.EmailSender;
//import com.onsemi.hms.tools.SpmlUtil;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//import javax.servlet.ServletContext;
//import org.apache.poi.hssf.usermodel.HSSFCell;
//import org.apache.poi.hssf.usermodel.HSSFFont;
//import org.apache.poi.hssf.usermodel.HSSFRow;
//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.hssf.util.HSSFColor;
//import org.apache.poi.ss.usermodel.CellStyle;
//import org.apache.poi.ss.usermodel.Font;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//
//@Configuration
//@EnableScheduling
//public class FtpConfigRequest {
//    private static final Logger LOGGER = LoggerFactory.getLogger(FtpConfigRequest.class);
//    String[] args = {};
//
//    @Autowired
//    ServletContext servletContext;
//
//    @Scheduled(cron = "0 30 9 * * ?") //every 8:00 AM - cron (sec min hr daysOfMth month daysOfWeek year(optional))
//    public void cronRun() throws FileNotFoundException, IOException {
//        LOGGER.info("Method REQUEST executed at everyday on 8:00 am. Current time is : " + new Date());
//        String username = System.getProperty("user.name");
//        DateFormat dateFormat = new SimpleDateFormat("yyyyMMMdd");
//        Date date = new Date();
//        String todayDate = dateFormat.format(date);
//        
//        String reportName = "C:\\Users\\" + username + "\\Documents\\from HMS\\Hardware Shipping Report (" + todayDate + ").xls";
//        WhRequestDAO whRequestDAO1 = new WhRequestDAO();
//        int count = whRequestDAO1.getCountYesterday();
//        
//        if (count == 0) {
//            LOGGER.info("++++++++++++++ NO RECORD FOR YESTERDAYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY ++++++++++++++++++++++");
//        } else {
//            WhRequestDAO whRequestDAO = new WhRequestDAO();
//            List<WhRequest> whRequestList = whRequestDAO.getWhRequestReportList();
//
//            FileOutputStream fileOut = new FileOutputStream(reportName);
//            HSSFWorkbook workbook = new HSSFWorkbook();
//            HSSFSheet sheet = workbook.createSheet("Shipping");
//            CellStyle style = workbook.createCellStyle();
//            Font font = workbook.createFont();
//            font.setFontHeightInPoints((short)10);
//            font.setFontName(HSSFFont.FONT_ARIAL);
//            font.setBoldweight(HSSFFont.COLOR_NORMAL);
//            font.setBold(true);
//            font.setColor(HSSFColor.DARK_BLUE.index);
//            style.setFont(font);
//            sheet.createFreezePane(0, 1);
//
//            HSSFRow rowhead = sheet.createRow((short)0);
//            rowhead.setRowStyle(style); 
//            HSSFCell cell1_1 = rowhead.createCell(0);
//            cell1_1.setCellStyle(style);
//            cell1_1.setCellValue("MATERIAL PASS NO");
//            HSSFCell cell1_2 = rowhead.createCell(1);
//            cell1_2.setCellStyle(style);
//            cell1_2.setCellValue("HARDWARE ID");
//            HSSFCell cell1_3 = rowhead.createCell(2);
//            cell1_3.setCellStyle(style);
//            cell1_3.setCellValue("HARDWARE TYPE");
//            HSSFCell cell1_4 = rowhead.createCell(3);
//            cell1_4.setCellStyle(style);
//            cell1_4.setCellValue("QUANTITY");
//            HSSFCell cell1_5 = rowhead.createCell(4);
//            cell1_5.setCellStyle(style);
//            cell1_5.setCellValue("INVENTORY");
//            HSSFCell cell1_6 = rowhead.createCell(5);
//            cell1_6.setCellStyle(style);
//            cell1_6.setCellValue("HIMS RECEIVED DATE");
//            HSSFCell cell1_7 = rowhead.createCell(6);
//            cell1_7.setCellStyle(style);
//            cell1_7.setCellValue("BARCODE VERIFICATION DATE");
//            HSSFCell cell1_8 = rowhead.createCell(7);
//            cell1_8.setCellStyle(style);
//            cell1_8.setCellValue("TIME TAKEN/DURATION");
//
//            for(int i=0; i<whRequestList.size(); i++) {            
//                HSSFRow contents = sheet.createRow(i + 1);
//                
//                HSSFCell cell2_1 = contents.createCell(0);
//                cell2_1.setCellValue(whRequestList.get(i).getMaterialPassNo());
//                
//                HSSFCell cell2_2 = contents.createCell(1);
//                cell2_2.setCellValue(whRequestList.get(i).getEquipmentId());
//
//                String eType = whRequestList.get(i).getEquipmentType();
//                if(whRequestList.get(i).getEquipmentType()== null) {
//                    eType = SpmlUtil.nullToEmptyString(whRequestList.get(i).getEquipmentType());
//                }
//                HSSFCell cell2_3 = contents.createCell(2);
//                cell2_3.setCellValue(eType);
//
//                String qty = whRequestList.get(i).getQuantity();
//                if(whRequestList.get(i).getQuantity()== null) {
//                    qty = SpmlUtil.nullToEmptyString(whRequestList.get(i).getQuantity());
//                }
//                HSSFCell cell2_4 = contents.createCell(3);
//                cell2_4.setCellValue(qty);
//
//                HSSFCell cell2_5 = contents.createCell(4);
//                cell2_5.setCellValue(whRequestList.get(i).getInventoryRack() + ", " + whRequestList.get(i).getInventoryShelf());
//
//                HSSFCell cell2_6 = contents.createCell(5);
//                cell2_6.setCellValue(whRequestList.get(i).getReceivedDate());
//                
//                HSSFCell cell2_7 = contents.createCell(6);
//                cell2_7.setCellValue(whRequestList.get(i).getDateVerify());
//                
//                HSSFCell cell2_8 = contents.createCell(7);
//                cell2_8.setCellValue(whRequestList.get(i).getDuration());
//            }
//            workbook.write(fileOut);
//            workbook.close();
//
//            //send email
//            LOGGER.info("send email to person in charge");
//            EmailSender emailSender = new EmailSender();
//            emailSender.htmlEmailWithAttachmentRequest2(
//                servletContext,
//                "All",                                                   //user name
////                "muhdfaizal@onsemi.com",                                   //to
//                "zbczmg@onsemi.com",
//                "Hardware Shipping from HIMS SF Report",   //subject
//                "Report for Hardware Shipping from HIMS SF has been made. " + 
//                "This report will shows the time for HIMS SF to read the request for ON semi delivering to warehouse and the time for the verification process on every item(s) that have been delivered yesterday. " + 
//                "Hence, attached is the report file for your view and perusal. Thank you." //msg
//            );
//        }
//    }
//}

package com.onsemi.hms.config;

import com.onsemi.hms.dao.WhRetrieveDAO;
import com.onsemi.hms.model.WhRetrieve;
import com.onsemi.hms.tools.EmailSender;
import com.onsemi.hms.tools.SpmlUtil;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletContext;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

 /* @author fg79cj */
@Configuration
@EnableScheduling
public class FtpConfigRetrieve {
    private static final Logger LOGGER = LoggerFactory.getLogger(FtpConfigRetrieve.class);
    String[] args = {};

    //File header
    private static final String HEADER = "id,material_pass_no, material_expiry_date,equipment_type,equipment_id,type,quantity,rack,slot,requested_by,requested_date,remarks";

    @Autowired
    private MessageSource messageSource;

    @Autowired
    ServletContext servletContext;
    
    //@Scheduled(fixedRate = 60000) //- in ms
    //hold for now
    @Scheduled(cron = "0 0 8 * * ?") //every 7:00 AM - cron (sec min hr daysOfMth month daysOfWeek year(optional))
    public void cronRun() throws FileNotFoundException, IOException {
        LOGGER.info("Method RETRIEVE executed at everyday on 7:00 am. Current time is : " + new Date());
        
        //for REQUEST AND SHIPPING : Hardware Shipping Report (" + todayDate + ").xls");
        //for RETRIEVE AND INVENTORY : Hardware Arrival Report (" + todayDate + ").xls");
        String username = System.getProperty("user.name");
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMMdd");
        Date date = new Date();
        String todayDate = dateFormat.format(date);
        
        String reportName = "C:\\Users\\" + username + "\\Documents\\from HMS\\Hardware Arrival Report (" + todayDate + ").xls";
        WhRetrieveDAO whRetrieveDAO = new WhRetrieveDAO();
        List<WhRetrieve> whRetrieveList = whRetrieveDAO.getWhRetrieveReportList();
        
        FileOutputStream fileOut = new FileOutputStream(reportName);
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Shipping");
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short)10);
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setBoldweight(HSSFFont.COLOR_NORMAL);
        font.setBold(true);
        font.setColor(HSSFColor.DARK_BLUE.index);
        style.setFont(font); 
//        style.setFillForegroundColor(IndexedColors.AQUA.getIndex());
//        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        sheet.createFreezePane(0, 1); // Freeze 1st Row   sheet.createFreezePane(int colSplit, int rowSplit, int leftmostColumn, int topRow)

        HSSFRow rowhead = sheet.createRow((short)0);
        rowhead.setRowStyle(style);     
//        HSSFCell cell1_0 = rowhead.createCell(0);
//        cell1_0.setCellStyle(style);
//        cell1_0.setCellValue("ID");    
        HSSFCell cell1_1 = rowhead.createCell(0);
        cell1_1.setCellStyle(style);
        cell1_1.setCellValue("MATERIAL PASS NO");
        HSSFCell cell1_2 = rowhead.createCell(1);
        cell1_2.setCellStyle(style);
        cell1_2.setCellValue("HARDWARE ID");
        HSSFCell cell1_3 = rowhead.createCell(2);
        cell1_3.setCellStyle(style);
        cell1_3.setCellValue("HARDWARE TYPE");
        HSSFCell cell1_4 = rowhead.createCell(3);
        cell1_4.setCellStyle(style);
        cell1_4.setCellValue("QUANTITY");
        HSSFCell cell1_5 = rowhead.createCell(4);
        cell1_5.setCellStyle(style);
        cell1_5.setCellValue("SHIPPING DATE");
        HSSFCell cell1_6 = rowhead.createCell(5);
        cell1_6.setCellStyle(style);
        cell1_6.setCellValue("BARCODE VERIFICATION DATE");
        
        for(int i=0; i<whRetrieveList.size(); i++) {            
            HSSFRow contents = sheet.createRow(i + 1);
//            HSSFCell cell2_0 = contents.createCell(0);
//            cell2_0.setCellValue(whRetrieveList.get(i).getRefId()); 
            HSSFCell cell2_1 = contents.createCell(0);
            cell2_1.setCellValue(whRetrieveList.get(i).getMaterialPassNo());
            HSSFCell cell2_2 = contents.createCell(1);
            cell2_2.setCellValue(whRetrieveList.get(i).getEquipmentId());
            
            String eType = whRetrieveList.get(i).getEquipmentType();
            if(whRetrieveList.get(i).getEquipmentType()== null) {
                eType = SpmlUtil.nullToEmptyString(whRetrieveList.get(i).getEquipmentType());
            }
            HSSFCell cell2_3 = contents.createCell(2);
            cell2_3.setCellValue(eType);
            
            String qty = whRetrieveList.get(i).getQuantity();
            if(whRetrieveList.get(i).getQuantity()== null) {
                qty = SpmlUtil.nullToEmptyString(whRetrieveList.get(i).getQuantity());
            }
            HSSFCell cell2_4 = contents.createCell(3);
            cell2_4.setCellValue(qty);
            
            HSSFCell cell2_5 = contents.createCell(4);
            cell2_5.setCellValue(whRetrieveList.get(i).getReceivedDate());
            HSSFCell cell2_6 = contents.createCell(5);
            cell2_6.setCellValue(whRetrieveList.get(i).getDateVerify());
        }
        workbook.write(fileOut);
        workbook.close();
        
        //send email
        LOGGER.info("send email to person in charge");
        EmailSender emailSender = new EmailSender();
        emailSender.htmlEmailWithAttachmentRetrieve2(
            servletContext,
            "All",                                                   //user name
            "cdarsrel@gmail.com",                                   //to
            "Hardware Arrival from HMS Report",   //subject
            "Report for Hardware Arrival from HMS has been made. " + 
            "This report will shows the time for HMS to read the request from ON Semiconductor and the time for verification process on every item(s) that have been delivered to warehouse yesterday. " + 
            "Hence, attached is the report file for your view and perusal. Thank you." //msg
        );
    }
}

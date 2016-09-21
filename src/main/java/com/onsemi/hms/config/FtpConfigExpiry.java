package com.onsemi.hms.config;

import com.onsemi.hms.dao.WhInventoryDAO;
import com.onsemi.hms.model.WhInventory;
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
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class FtpConfigExpiry {
    private static final Logger LOGGER = LoggerFactory.getLogger(FtpConfigExpiry.class);
    String[] args = {};

    @Autowired
    ServletContext servletContext;

    @Scheduled(cron = "0 0 8 * * ?") //every 8:00 AM - cron (sec min hr daysOfMth month daysOfWeek year(optional))
    public void cronRun() throws FileNotFoundException, IOException {
        LOGGER.info("Method Expiry executed at everyday on 8:00 am. Current time is : " + new Date());
        
        String username = System.getProperty("user.name");
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMMdd");
        Date date = new Date();
        String todayDate = dateFormat.format(date);
        
        String reportName = "C:\\Users\\" + username + "\\Documents\\from HMS\\Material Pass Expiry Date Report Within 1 Month (" + todayDate + ").xls";
        WhInventoryDAO whInventoryDAO1 = new WhInventoryDAO();
        int count = whInventoryDAO1.getCountMpExpiry();
        LOGGER.info("count is ~~~~~~~~~~~~~~~~~~~~~~ " + count);
        
        if (count == 0) {
            LOGGER.info("++++++++++++++ NO MATERIAL PASS EXPIRATION WITHIN ONE MONTH ++++++++++++++++++++++");
        } else {
            WhInventoryDAO whInventoryDAO = new WhInventoryDAO();
            List<WhInventory> whInventoryList = whInventoryDAO.getWhInventoryMpExpiryList();
            FileOutputStream fileOut = new FileOutputStream(reportName);
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("MP EXPIRY DATE");
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setFontHeightInPoints((short)10);
            font.setFontName(HSSFFont.FONT_ARIAL);
            font.setBoldweight(HSSFFont.COLOR_NORMAL);
            font.setBold(true);
            font.setColor(HSSFColor.DARK_BLUE.index);
            style.setFont(font);
            sheet.createFreezePane(0, 1); // Freeze 1st Row

            HSSFRow rowhead = sheet.createRow((short)0);
            rowhead.setRowStyle(style);    
            
            HSSFCell cell1_0 = rowhead.createCell(0);
            cell1_0.setCellStyle(style);
            cell1_0.setCellValue("MATERIAL PASS NO");
            
            HSSFCell cell1_1 = rowhead.createCell(1);
            cell1_1.setCellStyle(style);
            cell1_1.setCellValue("MATERIAL PASS EXPIRY DATE");
            
            HSSFCell cell1_2 = rowhead.createCell(2);
            cell1_2.setCellStyle(style);
            cell1_2.setCellValue("HARDWARE ID");
            
            HSSFCell cell1_3 = rowhead.createCell(3);
            cell1_3.setCellStyle(style);
            cell1_3.setCellValue("HARDWARE TYPE");
            
            HSSFCell cell1_4 = rowhead.createCell(4);
            cell1_4.setCellStyle(style);
            cell1_4.setCellValue("QUANTITY");
            
            HSSFCell cell1_5 = rowhead.createCell(5);
            cell1_5.setCellStyle(style);
            cell1_5.setCellValue("REQUESTED BY");
            
            HSSFCell cell1_6 = rowhead.createCell(6);
            cell1_6.setCellStyle(style);
            cell1_6.setCellValue("REQUESTED DATE");
            
            HSSFCell cell1_7 = rowhead.createCell(7);
            cell1_7.setCellStyle(style);
            cell1_7.setCellValue("SHIPPING DATE");
            
            HSSFCell cell1_8 = rowhead.createCell(8);
            cell1_8.setCellStyle(style);
            cell1_8.setCellValue("WAREHOUSE RECEIVED DATE");
            
            HSSFCell cell1_9 = rowhead.createCell(9);
            cell1_9.setCellStyle(style);
            cell1_9.setCellValue("HARDWARE VERIFICATION DATE");
            
            HSSFCell cell1_10 = rowhead.createCell(10);
            cell1_10.setCellStyle(style);
            cell1_10.setCellValue("HARDWARE VERIFICATION BY");
            
            HSSFCell cell1_11 = rowhead.createCell(11);
            cell1_11.setCellStyle(style);
            cell1_11.setCellValue("INVENTORY");

            for(int i=0; i<whInventoryList.size(); i++) {            
                HSSFRow contents = sheet.createRow(i + 1);
                
                HSSFCell cell2_0 = contents.createCell(0);
                cell2_0.setCellValue(whInventoryList.get(i).getMaterialPassNo());

                HSSFCell cell2_1 = contents.createCell(1);
                cell2_1.setCellValue(whInventoryList.get(i).getMaterialPassExpiry());

                HSSFCell cell2_2 = contents.createCell(2);
                cell2_2.setCellValue(whInventoryList.get(i).getEquipmentId());

                HSSFCell cell2_3 = contents.createCell(3);
                cell2_3.setCellValue(whInventoryList.get(i).getEquipmentType());

                HSSFCell cell2_4 = contents.createCell(4);
                cell2_4.setCellValue(whInventoryList.get(i).getQuantity());

                HSSFCell cell2_5 = contents.createCell(5);
                cell2_5.setCellValue(whInventoryList.get(i).getRequestedBy());

                HSSFCell cell2_6 = contents.createCell(6);
                cell2_6.setCellValue(whInventoryList.get(i).getRequestedDate());

                HSSFCell cell2_7 = contents.createCell(7);
                cell2_7.setCellValue(whInventoryList.get(i).getShippingDate());

                HSSFCell cell2_8 = contents.createCell(8);
                cell2_8.setCellValue(whInventoryList.get(i).getReceivedDate());

                HSSFCell cell2_9 = contents.createCell(9);
                cell2_9.setCellValue(whInventoryList.get(i).getDateVerify());

                HSSFCell cell2_10 = contents.createCell(10);
                cell2_10.setCellValue(whInventoryList.get(i).getUserVerify());
                
                HSSFCell cell2_11 = contents.createCell(11);
                cell2_11.setCellValue(whInventoryList.get(i).getInventoryRack() + ", " + whInventoryList.get(i).getInventoryShelf());
            }
            workbook.write(fileOut);
            workbook.close();

            //send email
            LOGGER.info("send email to person in charge");
            EmailSender emailSender = new EmailSender();
            emailSender.htmlEmailWithAttachmentMpExpiry(
                servletContext,
                "All",                                                   //user name
                "cdarsrel@gmail.com",                                   //to
                "Material Pass Expiry Date within ONE Month",   //subject
                "Report for Material Pass Expiry Date from CDARS has been made. " + 
                "This report will shows the expired date for each material pass within ONE (1) month durations. " + 
                "Hence, attached is the report file for your view and perusal. Thank you." //msg
            );
        }
    }
}

package com.onsemi.hms.config;

import com.onsemi.hms.dao.WhUSLDAO;
import com.onsemi.hms.model.WhUSL;
import com.onsemi.hms.tools.EmailSender;
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
public class FtpConfigUSL24hrs {
    private static final Logger LOGGER = LoggerFactory.getLogger(FtpConfigUSL24hrs.class);
    String[] args = {};

    @Autowired
    ServletContext servletContext;

    @Scheduled(cron = "0 45 13 * * ?") //every 8:00 AM - cron (sec min hr daysOfMth month daysOfWeek year(optional))
    public void cronRun() throws FileNotFoundException, IOException {
        LOGGER.info("Upper Spec Limit (USL) executed at everyday on 8:00 am. Current time is : " + new Date());
        
        String username = System.getProperty("user.name");
        DateFormat dateFormat = new SimpleDateFormat("ddMMMyyyy");
        Date date = new Date();
        String todayDate = dateFormat.format(date);
        
        String reportName = "C:\\Users\\" + username + "\\Documents\\CDARS\\HIMS Upper Specs Limit Report (" + todayDate + ").xls";
        WhUSLDAO whUSLDAO = new WhUSLDAO();
        int count = whUSLDAO.getCountUSLRet();
        LOGGER.info("count is ~~~~~~~~~~~~~~~~~~~~~~ " + count);
        
        if (count == 0) {
            LOGGER.info("++++++++++++++ No Hardware Exceed Upper Specs Limit 24 Hours ++++++++++++++++++++++");
        } else {
            WhUSLDAO whUslDAO = new WhUSLDAO();
            List<WhUSL> whUslList = whUslDAO.getWhUSLLog();
            FileOutputStream fileOut = new FileOutputStream(reportName);
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("HIMS PROCESS EXCEED USL");
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
            cell1_0.setCellValue("HARDWARE TYPE");
            
            HSSFCell cell1_1 = rowhead.createCell(1);
            cell1_1.setCellStyle(style);
            cell1_1.setCellValue("HARDWARE ID");
            
            HSSFCell cell1_2 = rowhead.createCell(2);
            cell1_2.setCellStyle(style);
            cell1_2.setCellValue("MATERIAL PASS NO");
            
            HSSFCell cell1_3 = rowhead.createCell(3);
            cell1_3.setCellStyle(style);
            cell1_3.setCellValue("STAGE");
            
            HSSFCell cell1_4 = rowhead.createCell(4);
            cell1_4.setCellStyle(style);
            cell1_4.setCellValue("DURATION");
            
            HSSFCell cell1_5 = rowhead.createCell(5);
            cell1_5.setCellStyle(style);
            cell1_5.setCellValue("CURRENT STATUS");

//            for(int i=0; i<whUslList.size(); i++) {            
//                HSSFRow contents = sheet.createRow(i + 1);
//                
//                HSSFCell cell2_0 = contents.createCell(0);
//                cell2_0.setCellValue(whUslList.get(i).getEquipmentType());
//
//                HSSFCell cell2_1 = contents.createCell(1);
//                cell2_1.setCellValue(whUslList.get(i).getEquipmentId());
//
//                HSSFCell cell2_2 = contents.createCell(2);
//                cell2_2.setCellValue(whUslList.get(i).getMaterialPassNo());
//
//                HSSFCell cell2_3 = contents.createCell(3);
//                cell2_3.setCellValue(whUslList.get(i).getModuleName());
//
//                HSSFCell cell2_4 = contents.createCell(4);
//                cell2_4.setCellValue(whUslList.get(i).getTimestamp());
//
//                HSSFCell cell2_5 = contents.createCell(5);
//                cell2_5.setCellValue(whUslList.get(i).getLogStatus());
//            }
            workbook.write(fileOut);
            workbook.close();
            //send email
            LOGGER.info("send email to person in charge");
            EmailSender emailSender = new EmailSender();
            emailSender.htmlEmailTableExceedUSL(
                servletContext,
                "Requestor",                                                   //user name requestor
                "zbczmg@onsemi.com",
//                "muhdfaizal@onsemi.com",                                   //to
                "List of Hardware Exceed USL (24 hours)",   //subject
                "Report for Hardware Process from HIMS that exceed Upper Specs Limit (24 hours) has been made. <br />" + 
                "Hence, attached is the report file for your view and perusal. <br /><br />" + 
                
                "<br /><br /> " +
                "<style>table, th, td {border: 1px solid black;} </style>" +
                "<table style=\"width:100%\">" //tbl
                + "<tr>"
                    + "<th>HARDWARE TYPE</th> "
                    + "<th>HARDWARE ID</th> "
                    + "<th>MATERIAL PASS NO.</th>"
                    + "<th>STAGE</th>"
                    + "<th>DURATION</th>"
                    + "<th>CURRENT STATUS</th>"
                + "</tr>"
                + table()
              + "</table>"
              + "<br />Thank you." //msg
            );
        }
    }
    
    private String table() {
        WhUSLDAO whUslDAO = new WhUSLDAO();
        List<WhUSL> whUslList = whUslDAO.getWhUSLLog();
        String materialPassNo = "";
        String stage = "";
        String hardwareId = "";
        String hardwareType = "";
        String duration = "";
        String status = "";
        String text = "";
        
        for(int i=0; i<whUslList.size(); i++) { 
            hardwareType = whUslList.get(i).getEquipmentType();
            hardwareId = whUslList.get(i).getEquipmentId();
            materialPassNo = whUslList.get(i).getMaterialPassNo();
            stage = "Sending";
            int hourReqShp = Integer.parseInt(whUslList.get(i).getReqShp());
            int hourShpArr = Integer.parseInt(whUslList.get(i).getShpArr());
            int hourArrInv = Integer.parseInt(whUslList.get(i).getArrInv());
            
            String tempReqShp = whUslList.get(i).getReqShp1();
            String tempShpArr = whUslList.get(i).getShpArr1();
            String tempArrInv = whUslList.get(i).getArrInv1();
            
            boolean flag = false;
            
            if(hourReqShp > 24 && tempReqShp != null && tempShpArr ==null) {
                duration = whUslList.get(i).getReqShp();
                status = whUslList.get(i).getRetStatus();
                flag = true;
            }
            if (hourShpArr > 24 && tempShpArr!= null && tempArrInv == null) {
                duration = whUslList.get(i).getShpArr();
                status = whUslList.get(i).getRetStatus();
                flag = true;
            }
            if (hourArrInv > 24 && tempArrInv != null) {
                duration = whUslList.get(i).getArrInv();
                status = whUslList.get(i).getRetStatus();
                flag = true;
            }

            if(flag == true) {
                text = text + "<tr align = \"center\">";
                text = text + "<td>" + hardwareType + "</td>";
                text = text + "<td>" + hardwareId + "</td>";
                text = text + "<td>" + materialPassNo + "</td>";
                text = text + "<td>" + stage + "</td>";
                text = text + "<td>" + duration + "</td>";
                text = text + "<td>" + status + "</td>";
                text = text + "</tr>";
            }
        }            
        return text;
    }
}

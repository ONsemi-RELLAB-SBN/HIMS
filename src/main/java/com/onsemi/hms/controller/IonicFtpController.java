package com.onsemi.hms.controller;

import com.onsemi.hms.dao.IonicFtpDAO;
import java.util.List;
import com.onsemi.hms.model.IonicFtp;
import com.onsemi.hms.model.IonicFtpRequest;
import com.onsemi.hms.tools.QueryResult;
import com.opencsv.CSVReader;
import java.io.FileReader;
import java.util.ArrayList;
import javax.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping(value = "/wh/IonicFtp")
@SessionAttributes({"userSession"})

public class IonicFtpController {
//    private static final Logger LOGGER = LoggerFactory.getLogger(IonicFtpController.class);
//    String[] args = {};
//
//    //Delimiters which has to be in the CSV file
////    private static final String COMMA_DELIMITER = ",";
////    private static final String LINE_SEPARATOR = "\n";
//
//    //File header
//    private static final String HEADER = "id,request_type,hardware_type,hardware_id,type,quantity,requested_by,requested_date,remarks";
//
//    @Autowired
//    private MessageSource messageSource;
//
//    @Autowired
//    ServletContext servletContext;
//
//    @RequestMapping(value = "/testOpenCsv", method = RequestMethod.GET)
//    public String addtestOpenCsv(Model model) {
//
//        CSVReader csvReader = null;
////        BufferedReader br = null;
//        try {
////          Reading the CSV File Delimiter is comma Start reading from line 1
////          csvReader = new CSVReader(new FileReader("C:\\Hardware_From_Humidity_Stress_FTP-" + parsedDate1 + ".csv"), ',', '"', 1);
//
//            String username = System.getProperty("user.name");
//            String targetLocation = "C:\\Users\\" + username + "\\Documents\\CDARS\\Request.csv";
//
//            LOGGER.info("test...." + targetLocation);
//
//            //csvReader = new CSVReader(new FileReader("C:\\Users\\zbczmg\\Desktop\\test - original cdars.csv"), ',', '"', 1);
//            csvReader = new CSVReader(new FileReader(targetLocation), ',', '"', 1);
//            //employeeDetails stores the values current line
//
//            String[] ionicFtp = null;
//            //Create List for holding Employee objects
//            List<IonicFtpRequest> empList = new ArrayList<IonicFtpRequest>();
//
//            while ((ionicFtp = csvReader.readNext()) != null) {
//                //Save the employee details in Employee object
//                IonicFtpRequest emp = new IonicFtpRequest(
//                    ionicFtp[0], ionicFtp[1], ionicFtp[2], ionicFtp[3], 
//                    ionicFtp[4], ionicFtp[5], ionicFtp[6], ionicFtp[7], 
//                    ionicFtp[8], ionicFtp[9], ionicFtp[10], ionicFtp[11]);
//                empList.add(emp);
//
//                
//                LOGGER.info("testssss1...." + ionicFtp[0]);
//                LOGGER.info("testssss2...." + ionicFtp[1]);
//                LOGGER.info("testssss3...." + ionicFtp[2]);
//                LOGGER.info("testssss4...." + ionicFtp[3]);
//                LOGGER.info("testssss5...." + ionicFtp[4]);
//                LOGGER.info("testssss6...." + ionicFtp[5]);
//                LOGGER.info("testssss7...." + ionicFtp[6]);
//                LOGGER.info("testssss8...." + ionicFtp[7]);
//                LOGGER.info("testssss9...." + ionicFtp[8]);
//                LOGGER.info("testssss9...." + ionicFtp[9]);
//                LOGGER.info("testssss9...." + ionicFtp[10]);
//                LOGGER.info("testssss9...." + ionicFtp[11]);
//            }
//
////            LOGGER.info("test...." + ionicFtp[0]);
//            //Lets print the Employee List
//            for (IonicFtpRequest e : empList) {
//                LOGGER.info("masuk!!");
//                /*
//                Date initDate = new SimpleDateFormat("MM/dd/yy H:mm:ss").parse(e.getDateOff());
//                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
//                String parsedDate = formatter.format(initDate);
//                 */
//                IonicFtp ftp = new IonicFtp();
//                LOGGER.info("masuk!!!!!!!!");
//
//                ftp.setRefId(e.getRefId());
//                LOGGER.info("masuk!! 1 : " + e.getRefId());
//                
//                String tempMaterialPassNo = "0";
//                ftp.setMaterialPassNo(tempMaterialPassNo);
//                LOGGER.info("masuk!! 2 : " + tempMaterialPassNo);
//                
//                String tempMaterialPassExpiry = "2016-08-16 16:16:16";
//                ftp.setMaterialPassExpiry(tempMaterialPassExpiry);
//                LOGGER.info("masuk!! 3 : " + tempMaterialPassExpiry);
//                
//                ftp.setEquipmentType(e.getEquipmentType());
//                LOGGER.info("masuk!! 4 : " + e.getEquipmentType());
//                
//                ftp.setEquipmentId(e.getEquipmentId());
//                LOGGER.info("masuk!! 5 : " + e.getEquipmentId());
//                
//                ftp.setType(e.getType());
//                LOGGER.info("masuk!! 6 : " + e.getType());
//                
//                ftp.setQuantity(e.getQuantity());
//                LOGGER.info("masuk!! 7 : " + e.getQuantity());
//                
//                ftp.setQuantity(e.getQuantity());
//                LOGGER.info("masuk!! 8 : " + e.getQuantity());
//                
//                ftp.setQuantity(e.getQuantity());
//                LOGGER.info("masuk!! 9 : " + e.getQuantity());
//                
//                ftp.setRequestedBy(e.getRack());
//                LOGGER.info("masuk!! 10 : " + e.getRack());
//                
//                ftp.setRequestedDate(e.getSlot());
//                LOGGER.info("masuk!! 11 : " + e.getSlot());
//                
//                ftp.setRemarks(e.getRemarks());
//                LOGGER.info("masuk!! 12 : " + e.getRemarks());
//                
//                String status = "New Request";
//                ftp.setStatus(status);
//                LOGGER.info("masuk!! 13 : " + status);
//                
//                String flag = "0";
//                ftp.setFlag(flag);
//                LOGGER.info("masuk!! 14 : " + flag);
//                
////                WhRequest wh = new WhRequest();
////                wh.setEquipmentId(e.getEventCode());
////                wh.setEquipmentType(e.getRms());
////                wh.setFirstApprovedStatus(e.getEquipId());
////                wh.setRemarks(e.getLcode());
////                wh.setType(e.getHardwareFinal());
////                wh.setStatus(e.getSupportItem());
//
//                //LOGGER.info("e.getEventCode()bbbbbbb...." + e.getEventCode());
//                //LOGGER.info("e.e.getSupportItem()()bbbbbbb...." + e.getSupportItem());
//                //WhRequestDAO whd = new WhRequestDAO();
//                IonicFtpDAO ionicFtpDAO1 = new IonicFtpDAO();
//                QueryResult queryResult1 = ionicFtpDAO1.insertIonicFtp(ftp);
//                LOGGER.info("++++++++++++++");
//
////                int count = ionicFtpDAO.getCountExistingData(ftp);
////                LOGGER.info("countiiii...." + count);
////                
////                if (count == 0) {                
////                    ionicFtpDAO = new IonicFtpDAO();
////                    QueryResult queryResult1 = ionicFtpDAO.insertIonicFtp(ftp);
////                }
//            }
//        } catch (Exception ee) {
//            ee.printStackTrace();
//        }
//    return "whRequest/add";
//    }
    
    
    
    
    /*OLD SAMPLE*/  
//        CSVReader csvReader = null;
//
//        try {
//            //Reading the CSV File Delimiter is comma Start reading from line 1
//            String username = System.getProperty("user.name");
//            String targetLocation = "C:\\Users\\" + username + "\\Documents\\CDARS\\test.csv";
//            csvReader = new CSVReader(new FileReader(targetLocation), ',', '"', 1);
//            //employeeDetails stores the values current line
//            String[] ionicFtp = null;
//            //Create List for holding Employee objects
//            List<IonicFtpTemp> empList = new ArrayList<IonicFtpTemp>();
//
//            while ((ionicFtp = csvReader.readNext()) != null) {
//                //Save the employee details in Employee object
//                IonicFtpRequest emp = new IonicFtpRequest(ionicFtp[0],
//                    ionicFtp[1], ionicFtp[2],
//                    ionicFtp[3], ionicFtp[4],
//                    ionicFtp[5], ionicFtp[6],
//                    ionicFtp[7], ionicFtp[8]);
//                empList.add(emp);
//            }
//
//            //Lets print the Employee List
//            for (IonicFtpRequest e : empList) {
//                IonicFtp  ftp = new IonicFtp();
//                ftp.setRefId(e.getRefId());
//                ftp.setRequestType(e.getRequestType());
//                ftp.setEquipmentType(e.getEquipmentType());
//                ftp.setEquipmentId(e.getEquipmentID());
//                ftp.setType(e.getType());
//                ftp.setQuantity(e.getQuantity());
//                ftp.setRequestedBy(e.getRequestedBy());
//                ftp.setRequestedDate(e.getRequestedDate());
//                ftp.setRemarks(e.getRemarks());
//                ftp.setMaterialPass("0");
//                ftp.setRack("0");
//                ftp.setShelf("0");
//                ftp.setStatus("0");
//                ftp.setFlag("0");
//                
//                IonicFtpDAO ionicFtpDAO = new IonicFtpDAO();
//                int count = ionicFtpDAO.getCountExistingData(e.getRefId());
//                if (count == 0) {
//                    //LOGGER.info("data xdeeeeee");
//                    ionicFtpDAO = new IonicFtpDAO();
//                    IonicFtpDAO ionicFtpDAO1 = new IonicFtpDAO();
//                    QueryResult queryResult1 = ionicFtpDAO.insertIonicFtp(ftp);
//                }
//
///*
//            Date initDate = new SimpleDateFormat("MM/dd/yy H:mm").parse(e.getDateOff());
//            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd H:mm");
//            String parsedDate = formatter.format(initDate);
//            IonicFtp ftp = new IonicFtp();
//            ftp.setEventCode(e.getEventCode());
//            ftp.setRms(e.getRms());
//            ftp.setIntervals(e.getIntervals());
//            ftp.setCurrentStatus(e.getCurrentStatus());
//            ftp.setDateOff(parsedDate);
//            ftp.setEquipId(e.getEquipId());
//            ftp.setLcode(e.getLcode());
//            ftp.setHardwareFinal(e.getHardwareFinal());
//            ftp.setSupportItem(e.getSupportItem());
//
//            IonicFtpDAO ionicFtpDAO = new IonicFtpDAO();
//            int count = ionicFtpDAO.getCountExistingData(ftp);
//            LOGGER.info("testmaaaaaaa" + count);
//            if (count == 0) {
//                LOGGER.info("data xdeeeeee");
//                ionicFtpDAO = new IonicFtpDAO();
//                IonicFtpDAO ionicFtpDAO1 = new IonicFtpDAO();
//                QueryResult queryResult1 = ionicFtpDAO.insertIonicFtp(ftp);
//            }
//*/
//            }
//        } catch (Exception ee) {
//            ee.printStackTrace();
//        }
//        return "whRequest/add";
//    }
}
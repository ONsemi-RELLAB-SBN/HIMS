package com.onsemi.hms.config;

import com.onsemi.hms.dao.WhRequestDAO;
import com.onsemi.hms.dao.WhRetrieveDAO;
import com.onsemi.hms.model.IonicFtpRequest;
import com.onsemi.hms.model.IonicFtpRetrieve;
import com.onsemi.hms.model.WhRequest;
import com.onsemi.hms.model.WhRetrieve;
import com.onsemi.hms.tools.QueryResult;
import com.opencsv.CSVReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletContext;
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
public class FtpConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(FtpConfig.class);
    String[] args = {};

    //File header
    private static final String HEADER = "id,material_pass_no, material_expiry_date,equipment_type,equipment_id,type,quantity,rack,slot,requested_by,requested_date,remarks";

    @Autowired
    private MessageSource messageSource;

    @Autowired
    ServletContext servletContext;

    String fileLocation = "";
    
    //@Scheduled(fixedRate = 60000) //- in ms
    //hold for now
    @Scheduled(cron = "0 0/1 * * * ?") //every 1 minute - cron (sec min hr daysOfMth month daysOfWeek year(optional))
    public void cronRun() {
        LOGGER.info("Method executed at every 2 minute. Current time is : " + new Date());
        
        String username = System.getProperty("user.name");
        String targetLocation = "C:\\Users\\" + username + "\\Documents\\CDARS\\";
        File folder = new File(targetLocation);
        File[] listOfFiles = folder.listFiles();

        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                if (listOfFile.getName().equals("cdars_retrieve.csv")) {
                    fileLocation = targetLocation + listOfFile.getName();
                    LOGGER.info("Request file found : " + fileLocation);
                    
                    CSVReader csvReader = null;      
                    try {
                        csvReader = new CSVReader(new FileReader(fileLocation), ',', '"', 1);
                        String[] ionicFtp = null;
                        List<IonicFtpRequest> requestList = new ArrayList<IonicFtpRequest>();

                        while ((ionicFtp = csvReader.readNext()) != null) {
                            IonicFtpRequest request = new IonicFtpRequest(
                                ionicFtp[0], ionicFtp[1], ionicFtp[2],
                                ionicFtp[3], ionicFtp[4], ionicFtp[5], 
                                ionicFtp[6], ionicFtp[7], ionicFtp[8],
                                ionicFtp[9], ionicFtp[10]);
                            requestList.add(request);
                        }
                        for (IonicFtpRequest r : requestList) {
                            WhRequest  ftp = new WhRequest();
                            ftp.setRefId(r.getRefId());
                            ftp.setEquipmentType(r.getEquipmentType());
                            ftp.setEquipmentId(r.getEquipmentId());
                            ftp.setQuantity(r.getQuantity());
                            ftp.setMaterialPassNo(r.getMaterialPassNo());
                            ftp.setMaterialPassExpiry(r.getMaterialPassExpiry());
                            ftp.setRack(r.getRack());
                            ftp.setSlot(r.getSlot());
                            ftp.setRequestedBy(r.getRequestedBy());
                            ftp.setRequestedDate(r.getRequestedDate());
                            ftp.setRemarks(r.getRemarks());
                            ftp.setStatus("New Request");
                            ftp.setFlag("0");
                            
                            WhRequestDAO whRequestDAO = new WhRequestDAO();
                            int count = whRequestDAO.getCountExistingData(r.getRefId());
                            if (count == 0) {
                                LOGGER.info("data xdeeeeee");
                                whRequestDAO = new WhRequestDAO();
                                WhRequestDAO whRequestDAO1 = new WhRequestDAO();
                                QueryResult queryResult1 = whRequestDAO.insertWhRequest(ftp);
                            } else {
                                LOGGER.info("data adeeeeee");
                            }
                        }
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                } else if (listOfFile.getName().equals("cdars_shipping.csv")) {
                    fileLocation = targetLocation + listOfFile.getName();
                    LOGGER.info("Retrieve file found : " + fileLocation);
                    
                    CSVReader csvReader = null;      
                    try {
                        csvReader = new CSVReader(new FileReader(fileLocation), ',', '"', 1);
                        String[] ionicFtp = null;
                        List<IonicFtpRetrieve> retrieveList = new ArrayList<IonicFtpRetrieve>();

                        while ((ionicFtp = csvReader.readNext()) != null) {
                            IonicFtpRetrieve retrieve = new IonicFtpRetrieve(
                                ionicFtp[0], ionicFtp[1], ionicFtp[2],
                                ionicFtp[3], ionicFtp[4], ionicFtp[5], 
                                ionicFtp[6], ionicFtp[7], ionicFtp[8]);
                            retrieveList.add(retrieve);
                        }
                        for (IonicFtpRetrieve r : retrieveList) {
                            WhRetrieve ftp = new WhRetrieve();
                            ftp.setRefId(r.getRefId());
                            ftp.setMaterialPassNo(r.getMaterialPassNo());
                            ftp.setMaterialPassExpiry(r.getMaterialPassExpiry());
                            ftp.setEquipmentType(r.getEquipmentType());
                            ftp.setEquipmentId(r.getEquipmentId());
                            ftp.setQuantity(r.getQuantity());
                            ftp.setRequestedBy(r.getRequestedBy());
                            ftp.setRequestedDate(r.getRequestedDate());
                            ftp.setRemarks(r.getRemarks());
                            ftp.setStatus("New Retrieval Request");
                            ftp.setFlag("0");

                            WhRetrieveDAO whRetrieveDAO = new WhRetrieveDAO();
                            int count = whRetrieveDAO.getCountExistingData(r.getRefId());
                            if (count == 0) {
                                LOGGER.info("data xdeeeeee");
                                whRetrieveDAO = new WhRetrieveDAO();
                                WhRetrieveDAO whRetrieveDAO1 = new WhRetrieveDAO();
                                QueryResult queryResult1 = whRetrieveDAO.insertWhRetrieve(ftp);
                            } else {
                                LOGGER.info("data adeeeeee");
                            }
                        }
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                } else {
                    fileLocation = targetLocation + listOfFile.getName();
                    LOGGER.info("Other file found : " + fileLocation);
                }
                //return checkPoint;
            }
        }
    }
//    public FtpConfig() {
//        int index = 0;
//        cronRun(index);
//        
//        if(index!=0) {
//            if(checkPoint.equals("Request")) {
//                CSVReader csvReader = null;      
//                try {
//                    // Reading the CSV File Delimiter is comma Start reading from line 1
//                    //csvReader = new CSVReader(new FileReader(targetLocation), ',', '"', 1);
//                    csvReader = new CSVReader(new FileReader(fileLocation), ',', '"', 1);
//                    //employeeDetails stores the values current line
//                    String[] ionicFtp = null;
//                    //Create List for holding Employee objects
//                    List<IonicFtpTemp> empList = new ArrayList<IonicFtpTemp>();
//
//                    while ((ionicFtp = csvReader.readNext()) != null) {
//                        //Save the employee details in Employee object
//                    IonicFtpTemp emp = new IonicFtpTemp(
//                        ionicFtp[0], ionicFtp[1], ionicFtp[2],
//                        ionicFtp[3], ionicFtp[4], ionicFtp[5], 
//                        ionicFtp[6], ionicFtp[7], ionicFtp[8],
//                        ionicFtp[9], ionicFtp[10], ionicFtp[11]);
//                    empList.add(emp);
//                    }
//
//                //Lets print the Employee List
//                for (IonicFtpTemp e : empList) {
//                    IonicFtp  ftp = new IonicFtp();
//                    ftp.setRefId(e.getRefId());
//                    ftp.setEquipmentType(e.getEquipmentType());
//                    ftp.setEquipmentId(e.getEquipmentId());
//                    ftp.setType(e.getType());
//                    ftp.setQuantity(e.getQuantity());
//                    ftp.setRequestedBy(e.getRequestedBy());
//                    ftp.setRequestedDate(e.getRequestedDate());
//                    ftp.setRemarks(e.getRemarks());
//                    ftp.setMaterialPassNo(e.getMaterialPassNo());
//                    ftp.setMaterialPassExpiry(e.getMaterialPassExpiry());
//                    ftp.setStatus("New Request");
//                    ftp.setFlag("0");
//
//                        IonicFtpDAO ionicFtpDAO = new IonicFtpDAO();
//                        int count = ionicFtpDAO.getCountExistingData(e.getRefId());
//                        if (count == 0) {
//                            LOGGER.info("data xdeeeeee");
//                            ionicFtpDAO = new IonicFtpDAO();
//                            IonicFtpDAO ionicFtpDAO1 = new IonicFtpDAO();
//                            QueryResult queryResult1 = ionicFtpDAO.insertIonicFtp(ftp);
//                        } else {
//                            LOGGER.info("data adeeeeee");
//                        }
//                    }
//                } catch (Exception ee) {
//                    ee.printStackTrace();
//                }
//            } else {
//                LOGGER.info("try for : " + checkPoint);
//            }
//        }
//    }

    
    
    
//    
////     Delimiters which has to be in the CSV file
////    private static final String COMMA_DELIMITER = ",";
////    private static final String LINE_SEPARATOR = "\n";
//    
//    @RequestMapping(value = "/testOpenCsv", method = RequestMethod.GET)
//    public String addtestOpenCsv(Model model) {
//        CSVReader csvReader = null;      
//        try {
//             // Reading the CSV File Delimiter is comma Start reading from line 1
//            String username = System.getProperty("user.name");
//            String targetLocation = "C:\\Users\\" + username + "\\Documents\\CDARS\\test3.csv";
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
//                ftp.setMaterialPassNo("-");
//                ftp.setMaterialPassExpiry("2016-08-16 16:16:16");
//                ftp.setStatus("-");
//                ftp.setFlag("-");
//                
//                IonicFtpDAO ionicFtpDAO = new IonicFtpDAO();
//                int count = ionicFtpDAO.getCountExistingData(e.getRefId());
//                if (count == 0) {
//                    LOGGER.info("data xdeeeeee");
//                    ionicFtpDAO = new IonicFtpDAO();
//                    IonicFtpDAO ionicFtpDAO1 = new IonicFtpDAO();
//                    QueryResult queryResult1 = ionicFtpDAO.insertIonicFtp(ftp);
//                } else {
//                    LOGGER.info("data adeeeeee");
//                }
//            }
//        } catch (Exception ee) {
//            ee.printStackTrace();
//        }
//        return "whRequest/verify";
//    }
    
//    @Scheduled(cron = "0 0/1 * * * ?") //every 1 minute - cron (sec min hr daysOfMth month daysOfWeek year(optional))
//    public void cronRun() {
//        LOGGER.info("Method executed at every 1 minutes. Current time is : "+ new Date());
//        IonicFtpController ionicFtpController = new IonicFtpController();
//        FtpConfig ftpConfig = new FtpConfig();
//    }
    
//    @Scheduled(fixedRate = 60000) - in ms
//    hold for now
//    @Scheduled(cron = "1 * * * * *") //every hour after 5 minute every day - cron (sec min hr * * * )
//    public void DownloadCsv() {
//        //utk ionic
//        CSVReader csvReader = null;
//        LOGGER.info("testjerkkkkk" + new Date());
//
//        try {
//            /**
//             * Reading the CSV File Delimiter is comma Start reading from line 1
//             */
////            Date initDate1 = new Date();
////            SimpleDateFormat formatter1 = new SimpleDateFormat("yyyyMMdd");
////            String parsedDate1 = formatter1.format(initDate1);
////            System.out.println(parsedDate1 + " tarikh csv!!!!...");
//
////            csvReader = new CSVReader(new FileReader("C:\\Hardware_From_Humidity_Stress_FTP-" + parsedDate1 + ".csv"), ',', '"', 1);  //comma tu delimiter
//            String username = System.getProperty("user.name");
//            String targetLocation = "C:\\Users\\" + username + "\\Documents\\CDARS\\test.csv";
//            csvReader = new CSVReader(new FileReader(targetLocation), ',', '"', 1);
//
////            csvReader = new CSVReader(new FileReader("C:\\Hardware_From_Humidity_Stress_FTP-20160505.csv"), ',', '"', 1);
//            //employeeDetails stores the values current line
//            String[] ionicFtp = null;
//            //Create List for holding Employee objects
//            List<IonicFtpTemp> empList = new ArrayList<IonicFtpTemp>();
//
//            while ((ionicFtp = csvReader.readNext()) != null) {
//                //Save the employee details in Employee object
//                IonicFtpRequest emp = new IonicFtpRequest(ionicFtp[0],
//                        ionicFtp[1], ionicFtp[2],
//                        ionicFtp[3], ionicFtp[4],
//                        ionicFtp[5], ionicFtp[6],
//                        ionicFtp[7], ionicFtp[8]);
//                empList.add(emp);
//            }

//            //Lets print the Employee List
//            for (IonicFtpRequest e : empList) {
//
//                Date initDate = new SimpleDateFormat("MM/dd/yy H:mm").parse(e.getDateOff());
//                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd H:mm");
//                String parsedDate = formatter.format(initDate);
//
//                IonicFtp ftp = new IonicFtp();
//                ftp.setEventCode(e.getEventCode());
//                ftp.setRms(e.getRms());
//                ftp.setIntervals(e.getIntervals());
//                ftp.setCurrentStatus(e.getCurrentStatus());
//                ftp.setDateOff(parsedDate);
//                ftp.setEquipId(e.getEquipId());
//                ftp.setLcode(e.getLcode());
//                ftp.setHardwareFinal(e.getHardwareFinal());
//                ftp.setSupportItem(e.getSupportItem());
//
//                IonicFtpDAO ionicFtpDAO = new IonicFtpDAO();
//                int count = ionicFtpDAO.getCountExistingData(ftp);
//                
//                if (count == 0) {
//                    ionicFtpDAO = new IonicFtpDAO();
////                    IonicFtpDAO ionicFtpDAO1 = new IonicFtpDAO();
//                    QueryResult queryResult1 = ionicFtpDAO.insertIonicFtp(ftp);
//                }
    /*
            Date initDate = new SimpleDateFormat("MM/dd/yy H:mm").parse(e.getDateOff());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd H:mm");
            String parsedDate = formatter.format(initDate);
            IonicFtp ftp = new IonicFtp();
            ftp.setEventCode(e.getEventCode());
            ftp.setRms(e.getRms());
            ftp.setIntervals(e.getIntervals());
            ftp.setCurrentStatus(e.getCurrentStatus());
            ftp.setDateOff(parsedDate);
            ftp.setEquipId(e.getEquipId());
            ftp.setLcode(e.getLcode());
            ftp.setHardwareFinal(e.getHardwareFinal());
            ftp.setSupportItem(e.getSupportItem());

            IonicFtpDAO ionicFtpDAO = new IonicFtpDAO();
            int count = ionicFtpDAO.getCountExistingData(ftp);
            LOGGER.info("testmaaaaaaa" + count);
            if (count == 0) {
                LOGGER.info("data xdeeeeee");
                ionicFtpDAO = new IonicFtpDAO();
                IonicFtpDAO ionicFtpDAO1 = new IonicFtpDAO();
                QueryResult queryResult1 = ionicFtpDAO.insertIonicFtp(ftp);
            }
*/
//
//            }
//        } catch (Exception ee) {
//            ee.printStackTrace();
//        }
//    }

}

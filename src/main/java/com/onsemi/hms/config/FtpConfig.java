package com.onsemi.hms.config;

import com.onsemi.hms.dao.LogModuleDAO;
import com.onsemi.hms.dao.WhRequestDAO;
import com.onsemi.hms.dao.WhRetrieveDAO;
import com.onsemi.hms.dao.WhShippingDAO;
import com.onsemi.hms.model.IonicFtpClose;
import com.onsemi.hms.model.IonicFtpRequest;
import com.onsemi.hms.model.IonicFtpRetrieve;
import com.onsemi.hms.model.LogModule;
import com.onsemi.hms.model.WhRequest;
import com.onsemi.hms.model.WhRetrieve;
import com.onsemi.hms.model.WhShipping;
import com.onsemi.hms.tools.EmailSender;
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
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

 /* @author fg79cj */
@Configuration
@EnableScheduling
public class FtpConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(FtpConfig.class);
    String[] args = {};

    @Autowired
    ServletContext servletContext;

    String fileLocation = "";

    @Scheduled(cron = "0 0/1 * * * ?") //every 2 minute - cron (sec min hr daysOfMth month daysOfWeek year(optional))
    public void cronRun() {
        LOGGER.info("Method executed at every 1 minute. Current time is : " + new Date());
        
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
                                ionicFtp[9], ionicFtp[10], ionicFtp[11],
                                ionicFtp[12], ionicFtp[13], ionicFtp[14],
                                ionicFtp[15], ionicFtp[16], ionicFtp[17],
                                ionicFtp[18], ionicFtp[19]
                            );
                            requestList.add(request);
                        }
                        for (IonicFtpRequest r : requestList) {
                            WhRequest  ftp = new WhRequest();
                            ftp.setRefId(r.getRefId());
                            String refId = r.getRefId();
                            ftp.setEquipmentType(r.getEquipmentType());
                            ftp.setEquipmentId(r.getEquipmentId());
                            ftp.setPcbA(r.getPcbA());
                            ftp.setPcbB(r.getPcbB());
                            ftp.setPcbC(r.getPcbC());
                            ftp.setPcbControl(r.getPcbControl());
                            ftp.setQtyQualA(r.getQtyQualA());
                            ftp.setQtyQualB(r.getQtyQualB());
                            ftp.setQtyQualC(r.getQtyQualC());
                            ftp.setQtyControl(r.getQtyControl());
                            ftp.setQuantity(r.getQuantity());
                            ftp.setMaterialPassNo(r.getMaterialPassNo());
                            ftp.setMaterialPassExpiry(r.getMaterialPassExpiry());
                            ftp.setInventoryRack(r.getInventoryRack());
                            ftp.setInventoryShelf(r.getInventoryShelf());
                            ftp.setRequestedBy(r.getRequestedBy());
                            ftp.setRequestedEmail(r.getRequestedEmail());
                            ftp.setRequestedDate(r.getRequestedDate());
                            ftp.setRemarks(r.getRemarks());
                            ftp.setStatus("New Shipping Request");
                            ftp.setFlag("0");
                            WhRequestDAO whRequestDAO = new WhRequestDAO();
                            int count = whRequestDAO.getCountExistingData(r.getRefId());
                            if (count == 0) {
                                LOGGER.info("data xdeeeeee");
                                whRequestDAO = new WhRequestDAO();
                                WhRequestDAO whRequestDAO1 = new WhRequestDAO();
                                QueryResult queryResult1 = whRequestDAO.insertWhRequest(ftp);
                                
                                WhRequestDAO whRequestDAO2 = new WhRequestDAO();
                                WhRequest query = whRequestDAO2.getWhRequest(refId);
                                LogModule logModule = new LogModule();
                                LogModuleDAO logModuleDAO = new LogModuleDAO();
                                logModule.setReferenceId(query.getRefId());
                                logModule.setModuleId(query.getId());
                                logModule.setModuleName("hms_wh_request_list");
                                logModule.setStatus(query.getStatus());
                                QueryResult queryResult2 = logModuleDAO.insertLog(logModule);
                                
                                
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
                                ionicFtp[6], ionicFtp[7], ionicFtp[8], 
                                ionicFtp[9], ionicFtp[10], ionicFtp[11],
                                ionicFtp[12], ionicFtp[13], ionicFtp[14],
                                ionicFtp[15], ionicFtp[16], ionicFtp[17],
                                ionicFtp[18]
                            );
                            retrieveList.add(retrieve);
                        }
                        for (IonicFtpRetrieve r : retrieveList) {
                            WhRetrieve ftp = new WhRetrieve();
                            ftp.setRefId(r.getRefId());
                            String refId = r.getRefId();
                            ftp.setMaterialPassNo(r.getMaterialPassNo());
                            ftp.setMaterialPassExpiry(r.getMaterialPassExpiry());
                            ftp.setEquipmentType(r.getEquipmentType());
                            ftp.setEquipmentId(r.getEquipmentId());
                            ftp.setPcbA(r.getPcbA());
                            ftp.setPcbB(r.getPcbB());
                            ftp.setPcbC(r.getPcbC());
                            ftp.setPcbControl(r.getPcbControl());
                            ftp.setQtyQualA(r.getQtyQualA());
                            ftp.setQtyQualB(r.getQtyQualB());
                            ftp.setQtyQualC(r.getQtyQualC());
                            ftp.setQtyControl(r.getQtyControl());
                            ftp.setQuantity(r.getQuantity());
                            ftp.setRequestedBy(r.getRequestedBy());
                            ftp.setRequestedEmail(r.getRequestedEmail());
                            ftp.setRequestedDate(r.getRequestedDate());
                            ftp.setRemarks(r.getRemarks());
                            ftp.setShippingDate(r.getShippingDate());
                            ftp.setStatus("New Inventory Request");
                            ftp.setFlag("0");
                            WhRetrieveDAO whRetrieveDAO = new WhRetrieveDAO();
                            int count = whRetrieveDAO.getCountExistingData(r.getRefId());
                            if (count == 0) {
                                LOGGER.info("data xdeeeeee");
                                whRetrieveDAO = new WhRetrieveDAO();
                                WhRetrieveDAO whRetrieveDAO1 = new WhRetrieveDAO();
                                QueryResult queryResult1 = whRetrieveDAO.insertWhRetrieve(ftp);
                                
                                WhRetrieveDAO whRetrieveDAO2 = new WhRetrieveDAO();
                                WhRetrieve query = whRetrieveDAO2.getWhRetrieve(refId);
                                LogModule logModule = new LogModule();
                                LogModuleDAO logModuleDAO = new LogModuleDAO();
                                logModule.setReferenceId(query.getRefId());
                                logModule.setModuleId(query.getId());
                                logModule.setModuleName("hms_wh_retrieval_list");
                                logModule.setStatus(query.getStatus());
                                QueryResult queryResult2 = logModuleDAO.insertLog(logModule);
                            }
                        }
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                } else if (listOfFile.getName().equals("cdars_retrieval_status.csv")) {
                    fileLocation = targetLocation + listOfFile.getName();
                    LOGGER.info("Close status file found : " + fileLocation);
                    
                    CSVReader csvReader = null;      
                    try {
                        csvReader = new CSVReader(new FileReader(fileLocation), ',', '"', 1);
                        String[] ionicFtp = null;
                        List<IonicFtpClose> closeList = new ArrayList<IonicFtpClose>();

                        while ((ionicFtp = csvReader.readNext()) != null) {
                            IonicFtpClose close = new IonicFtpClose(
                                ionicFtp[0], ionicFtp[1], ionicFtp[2],
                                ionicFtp[3], ionicFtp[4], ionicFtp[5], 
                                ionicFtp[6]
                            );
                            closeList.add(close);
                        }
                        for (IonicFtpClose c : closeList) {
                            WhShipping  ftp = new WhShipping();
                            ftp.setRequestId(c.getRequestId());
                            String refId = c.getRequestId();
                            ftp.setEquipmentType(c.getHardwareType());
                            ftp.setEquipmentId(c.getHardwareId());
                            ftp.setQuantity(c.getQuantity());
                            ftp.setInventoryRack(c.getRack());
                            ftp.setInventoryShelf(c.getShelf());
                            ftp.setStatus(c.getStatus());
                            ftp.setFlag("2");
                            WhShippingDAO whShippingDAO = new WhShippingDAO();
                            int count = whShippingDAO.getCountExistingData(c.getRequestId());
                            if (count != 0 && (!ftp.getFlag().equals("0") || !ftp.getFlag().equals("2"))) {
                                LOGGER.info("data adeeeeee");
                                WhShippingDAO WhShippingDAO = new WhShippingDAO();
                                QueryResult queryResult1 = WhShippingDAO.updateStatus(ftp);
                                
                                WhRequest whReq = new WhRequest();
                                whReq.setStatus(ftp.getStatus());
                                whReq.setFlag("2");
                                whReq.setRefId(refId);
                                WhRequestDAO whReqDao = new WhRequestDAO();
                                QueryResult que = whReqDao.updateWhRequestStatus(whReq);
                                
                                WhShippingDAO whShippingDAO2 = new WhShippingDAO();
                                WhShipping query = whShippingDAO2.getWhShipping(refId);
                                LogModule logModule = new LogModule();
                                LogModuleDAO logModuleDAO = new LogModuleDAO();
                                logModule.setReferenceId(query.getRequestId());
                                logModule.setModuleId(query.getId());
                                logModule.setModuleName("hms_wh_shipping_list");
                                logModule.setStatus(ftp.getStatus());
                                QueryResult queryResult2 = logModuleDAO.insertLog(logModule);
                            }
                        }
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                }
            }
        }
    }
}

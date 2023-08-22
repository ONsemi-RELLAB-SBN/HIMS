package com.onsemi.hms.config;

import com.onsemi.hms.dao.LogModuleDAO;
import com.onsemi.hms.dao.WhMpListDAO;
import com.onsemi.hms.dao.WhRequestDAO;
import com.onsemi.hms.dao.WhRetrieveDAO;
import com.onsemi.hms.dao.WhShippingDAO;
import com.onsemi.hms.model.IonicFtpClose;
import com.onsemi.hms.model.IonicFtpRequest;
import com.onsemi.hms.model.IonicFtpRetrieve;
import com.onsemi.hms.model.LogModule;
import com.onsemi.hms.model.WhMpList;
import com.onsemi.hms.model.WhRequest;
import com.onsemi.hms.model.WhRetrieve;
import com.onsemi.hms.model.WhShipping;
import com.onsemi.hms.tools.QueryResult;
import com.opencsv.CSVReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
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

    @Scheduled(cron = "0 */1 * * * ?") //every 2 minute - cron (sec min hr daysOfMth month daysOfWeek year(optional)) //active
    public void cronRun() {
        LOGGER.info("MASUK KE FUNCTION CRON JOB - HMS");
        String username = System.getProperty("user.name");
//        String targetLocation = "D:\\HIMS_CSV\\RL\\";
        String targetLocation = "D:\\Source Code\\archive\\CSV Import\\";
        File folder = new File(targetLocation);
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles.length != 0) {
            for (File listOfFile : listOfFiles) {
                if (listOfFile.isFile()) {
                    if (listOfFile.getName().equals("cdars_retrieve.csv")) {
                        fileLocation = targetLocation + listOfFile.getName();
                        LOGGER.info("Request file found.");

                        CSVReader csvReader = null;
                        try {
                            csvReader = new CSVReader(new FileReader(fileLocation), ',', '"', 1);
                            String[] ionicFtp = null;
                            List<IonicFtpRequest> requestList = new ArrayList<IonicFtpRequest>();

                            while ((ionicFtp = csvReader.readNext()) != null) {
                                IonicFtpRequest request = new IonicFtpRequest(
                                        ionicFtp[0], ionicFtp[1], ionicFtp[2], //id, hwareType, hwareId
                                        ionicFtp[3], ionicFtp[4], ionicFtp[5], //retrievalReason, PcbA, pcbAQty
                                        ionicFtp[6], ionicFtp[7], ionicFtp[8], //pcbB, pcbBQty, pcbC
                                        ionicFtp[9], ionicFtp[10], ionicFtp[11], //pcbCQty, pcbCtr, pcbCtrQty
                                        ionicFtp[12], ionicFtp[13], ionicFtp[14], //qty, mpNo [change to boxNo], mpExp [change to gtsNo]
                                        ionicFtp[15], ionicFtp[16], ionicFtp[17], //rack, shelf, reqBy
                                        ionicFtp[18], ionicFtp[19], ionicFtp[20], //reqEmail, reqDate, remarks
                                        ionicFtp[21] //status
                                );
                                requestList.add(request);
                            }
                            for (IonicFtpRequest r : requestList) {
                                WhRequest ftp = new WhRequest();
                                //default
                                ftp.setRefId(r.getRefId());
                                String refId = r.getRefId();
                                ftp.setBoxNo(r.getBoxNo());
                                ftp.setGtsNo(r.getGtsNo());
//                                ftp.setMaterialPassNo(r.getMaterialPassNo());
//                                ftp.setMaterialPassExpiry(r.getMaterialPassExpiry());
                                ftp.setInventoryRack(r.getInventoryRack());
                                ftp.setInventoryShelf(r.getInventoryShelf());
                                ftp.setRequestedBy(r.getRequestedBy());
                                ftp.setRequestedEmail(r.getRequestedEmail());
                                ftp.setRequestedDate(r.getRequestedDate());
                                ftp.setRemarks(r.getRemarks());
                                ftp.setCsvStatus(r.getCsvStatus());
                                //diff status
                                if (r.getCsvStatus().equals("Cancelled")) {
                                    ftp.setStatus("Shipping Cancelled");
                                    ftp.setFlag("1");
                                } else {
                                    ftp.setStatus("New Shipping Request");
                                    ftp.setFlag("0");
                                }
                                //default
                                ftp.setReasonRetrieval(r.getReasonRetrieval());
                                ftp.setEquipmentType(r.getEquipmentType());
                                ftp.setEquipmentId(r.getEquipmentId());
                                ftp.setPcbC(r.getPcbC());
                                ftp.setPcbControl(r.getPcbControl());
                                ftp.setQtyQualC(r.getQtyQualC());
                                ftp.setQtyControl(r.getQtyControl());
                                ftp.setQuantity(r.getQuantity());
                                ftp.setReasonRetrieval(r.getReasonRetrieval());
                                //diff hwareId
                                if (r.getEquipmentType().equals("Load Card")) {
                                    String pairingType = r.getEquipmentId().substring(8, r.getEquipmentId().length());
                                    ftp.setPairingType(pairingType);
                                    ftp.setLoadCardId(r.getPcbA());
                                    ftp.setLoadCardQty(r.getQtyQualA());
                                    ftp.setProgCardId(null);
                                    ftp.setProgCardQty(null);
                                    ftp.setPcbA(null);
                                    ftp.setQtyQualA("0");
                                    ftp.setPcbB(null);
                                    ftp.setQtyQualB("0");
                                } else if (r.getEquipmentType().equals("Program Card")) {
                                    String pairingType = r.getEquipmentId().substring(8, r.getEquipmentId().length());
                                    ftp.setPairingType(pairingType);
                                    ftp.setLoadCardId(null);
                                    ftp.setLoadCardQty(null);
                                    ftp.setProgCardId(r.getPcbB());
                                    ftp.setProgCardQty(r.getQtyQualB());
                                    ftp.setPcbA(null);
                                    ftp.setQtyQualA("0");
                                    ftp.setPcbB(null);
                                    ftp.setQtyQualB("0");
                                } else if (r.getEquipmentType().equals("Load Card & Program Card")) {
                                    String pairingType = r.getEquipmentId().substring(8, r.getEquipmentId().length());
                                    ftp.setPairingType(pairingType);
                                    ftp.setLoadCardId(r.getPcbA());
                                    ftp.setLoadCardQty(r.getQtyQualA());
                                    ftp.setProgCardId(r.getPcbB());
                                    ftp.setProgCardQty(r.getQtyQualB());
                                    ftp.setPcbA(null);
                                    ftp.setQtyQualA("0");
                                    ftp.setPcbB(null);
                                    ftp.setQtyQualB("0");
                                } else {
                                    ftp.setPairingType(null);
                                    ftp.setLoadCardId(null);
                                    ftp.setLoadCardQty(null);
                                    ftp.setProgCardId(null);
                                    ftp.setProgCardQty(null);
                                    ftp.setPcbA(r.getPcbA());
                                    ftp.setPcbB(r.getPcbB());
                                    ftp.setQtyQualA(r.getQtyQualA());
                                    ftp.setQtyQualB(r.getQtyQualB());
                                }

                                WhRequestDAO whRequestDAO = new WhRequestDAO();
                                int count = whRequestDAO.getCountExistingData(r.getRefId());
                                if (count == 0) {
                                    LOGGER.info("retrieve - add new data");
                                    WhRequestDAO whRequestDAO1 = new WhRequestDAO();
                                    QueryResult queryResult1 = whRequestDAO1.insertWhRequestNew(ftp);

                                    WhRequestDAO whRequestDAO2 = new WhRequestDAO();
                                    WhRequest query = whRequestDAO2.getWhRequest(refId);
                                    LogModule logModule = new LogModule();
                                    LogModuleDAO logModuleDAO = new LogModuleDAO();
                                    logModule.setReferenceId(query.getRefId());
                                    logModule.setModuleId(query.getId());
                                    logModule.setModuleName("hms_wh_request_list");
                                    logModule.setStatus(query.getStatus());
                                    QueryResult queryResult2 = logModuleDAO.insertLog(logModule);
                                } else {
                                    WhRequestDAO whReqDAO = new WhRequestDAO();
                                    WhRequest que = whReqDAO.getWhRequest(refId);
                                    if (r.getCsvStatus().equals("Cancelled") && que.getFlag().equals("0")) {
                                        WhRequestDAO whRequestDAO1 = new WhRequestDAO();
                                        QueryResult queryResult1 = whRequestDAO1.updateWhRequestForApproval(ftp);

                                        WhRequestDAO whRequestDAO2 = new WhRequestDAO();
                                        WhRequest query = whRequestDAO2.getWhRequest(refId);
                                        LogModule logModule = new LogModule();
                                        LogModuleDAO logModuleDAO = new LogModuleDAO();
                                        logModule.setReferenceId(query.getRefId());
                                        logModule.setModuleId(query.getId());
                                        logModule.setModuleName("hms_wh_request_list");
                                        logModule.setStatus(query.getStatus());
                                        QueryResult queryResult2 = logModuleDAO.insertLog(logModule);
                                        LOGGER.info("data inventory cancel");
                                    }
                                }
                            }
                        } catch (Exception ee) {
                            LOGGER.info("Error while reading cdars_retrieve.csv");
                            ee.printStackTrace();
                        }
                    } else if (listOfFile.getName().equals("cdars_shipping.csv")) {
                        fileLocation = targetLocation + listOfFile.getName();
                        LOGGER.info("Shipping file found.");

                        CSVReader csvReader = null;
                        try {
                            csvReader = new CSVReader(new FileReader(fileLocation), ',', '"', 1);
                            String[] ionicFtp = null;
                            List<IonicFtpRetrieve> retrieveList = new ArrayList<IonicFtpRetrieve>();

                            while ((ionicFtp = csvReader.readNext()) != null) {
                                IonicFtpRetrieve retrieve = new IonicFtpRetrieve(
                                        ionicFtp[0], ionicFtp[1], ionicFtp[2], //id, hwareType, hwareId
                                        ionicFtp[3], ionicFtp[4], ionicFtp[5], //pcbA, pcbAQty, pcbB
                                        ionicFtp[6], ionicFtp[7], ionicFtp[8], //pcbBQty, pcbC, pcbCQty
                                        ionicFtp[9], ionicFtp[10], ionicFtp[11], //pcbCtr, pcbCtrQty, qty
                                        ionicFtp[12], ionicFtp[13], ionicFtp[14], //mpNo [change to boxNo], mpExp [change to gtsNo], reqBy
                                        ionicFtp[15], ionicFtp[16], ionicFtp[17], //reqEmail, reqDate, remarks
                                        ionicFtp[18], ionicFtp[19] // shipDate, status
                                );
                                retrieveList.add(retrieve);
                            }
                            for (IonicFtpRetrieve r : retrieveList) {
                                WhRetrieve ftp = new WhRetrieve();
                                ftp.setRefId(r.getRefId());
                                String refId = r.getRefId();
                                ftp.setBoxNo(r.getBoxNo());
                                ftp.setGtsNo(r.getGtsNo());
//                                ftp.setMaterialPassNo(r.getMaterialPassNo());
//                                ftp.setMaterialPassExpiry(r.getMaterialPassExpiry());
                                ftp.setEquipmentType(r.getEquipmentType());
                                ftp.setEquipmentId(r.getEquipmentId());
                                ftp.setQuantity(r.getQuantity());
                                ftp.setRequestedBy(r.getRequestedBy());
                                ftp.setRequestedEmail(r.getRequestedEmail());
                                ftp.setRequestedDate(r.getRequestedDate());
                                ftp.setRemarks(r.getRemarks());
                                ftp.setShippingDate(r.getShippingDate());
                                ftp.setCsvStatus(r.getCsvStatus());
                                //diff status
                                if (r.getCsvStatus().equals("Cancelled")) {
                                    ftp.setStatus("Inventory Cancelled");
                                    ftp.setFlag("1");
                                } else {
                                    ftp.setStatus("New Inventory Request");
                                    ftp.setFlag("0");
                                }
                                //diff hwareId
                                if (r.getEquipmentType().equals("Load Card")) {
                                    String pairingType = r.getEquipmentId().substring(8, r.getEquipmentId().length());
                                    ftp.setPairingType(pairingType);
                                    ftp.setLoadCardId(r.getPcbA());
                                    ftp.setLoadCardQty(r.getQtyQualA());
                                    ftp.setProgCardId(null);
                                    ftp.setProgCardQty(null);
                                    ftp.setPcbA(null);
                                    ftp.setQtyQualA("0");
                                    ftp.setPcbB(null);
                                    ftp.setQtyQualB("0");
                                } else if (r.getEquipmentType().equals("Program Card")) {
                                    String pairingType = r.getEquipmentId().substring(8, r.getEquipmentId().length());
                                    ftp.setPairingType(pairingType);
                                    ftp.setLoadCardId(null);
                                    ftp.setLoadCardQty(null);
                                    ftp.setProgCardId(r.getPcbB());
                                    ftp.setProgCardQty(r.getQtyQualB());
                                    ftp.setPcbA(null);
                                    ftp.setQtyQualA("0");
                                    ftp.setPcbB(null);
                                    ftp.setQtyQualB("0");
                                } else if (r.getEquipmentType().equals("Load Card & Program Card")) {
                                    String pairingType = r.getEquipmentId().substring(8, r.getEquipmentId().length());
                                    ftp.setPairingType(pairingType);
                                    ftp.setLoadCardId(r.getPcbA());
                                    ftp.setLoadCardQty(r.getQtyQualA());
                                    ftp.setProgCardId(r.getPcbB());
                                    ftp.setProgCardQty(r.getQtyQualB());
                                    ftp.setPcbA(null);
                                    ftp.setQtyQualA("0");
                                    ftp.setPcbB(null);
                                    ftp.setQtyQualB("0");
                                } else {
                                    ftp.setPairingType(null);
                                    ftp.setLoadCardId(null);
                                    ftp.setLoadCardQty(null);
                                    ftp.setProgCardId(null);
                                    ftp.setProgCardQty(null);
                                    ftp.setPcbA(r.getPcbA());
                                    ftp.setPcbB(r.getPcbB());
                                    ftp.setQtyQualA(r.getQtyQualA());
                                    ftp.setQtyQualB(r.getQtyQualB());
                                }
                                ftp.setPcbC(r.getPcbC());
                                ftp.setPcbControl(r.getPcbControl());
                                ftp.setQtyQualC(r.getQtyQualC());
                                ftp.setQtyControl(r.getQtyControl());
                                WhRetrieveDAO whRetrieveDAO = new WhRetrieveDAO();
                                int count = whRetrieveDAO.getCountExistingData(r.getRefId());
                                if (count == 0) {
                                    if (!refId.equals("0")) {
                                        LOGGER.info("shipping - add new data");
                                        WhRetrieveDAO whRetrieveDAO1 = new WhRetrieveDAO();
                                        QueryResult queryResult1 = whRetrieveDAO1.insertWhRetrieveNew(ftp);

                                        WhRetrieveDAO whRetrieveDAO2 = new WhRetrieveDAO();
                                        WhRetrieve query = whRetrieveDAO2.getWhRetrieve(refId);
                                        LogModule logModule = new LogModule();
                                        LogModuleDAO logModuleDAO = new LogModuleDAO();
                                        logModule.setReferenceId(query.getRefId());
                                        logModule.setModuleId(query.getId());
                                        logModule.setModuleName("hms_wh_retrieval_list");
                                        logModule.setStatus(query.getStatus());
                                        QueryResult queryResult2 = logModuleDAO.insertLog(logModule);
                                    } else {
                                        //nothing
                                    }
                                } else {
                                    WhRetrieveDAO whRetDAO = new WhRetrieveDAO();
                                    WhRetrieve que = whRetDAO.getWhRetrieve(refId);
                                    if (r.getCsvStatus().equals("Cancelled") && que.getFlag().equals("0")) {
                                        if ("0".equals(refId)) {
                                            //                                        LOGGER.info("xperlu masuk2");
                                        } else {
                                            WhRetrieveDAO whRetrieveDAO1 = new WhRetrieveDAO();
                                            QueryResult queryResult1 = whRetrieveDAO1.updateWhRetrieveForApproval(ftp);

                                            WhRetrieveDAO whRetrieveDAO2 = new WhRetrieveDAO();
                                            WhRetrieve query = whRetrieveDAO2.getWhRetrieve(refId);
                                            LogModule logModule = new LogModule();
                                            LogModuleDAO logModuleDAO = new LogModuleDAO();
                                            logModule.setReferenceId(query.getRefId());
                                            logModule.setModuleId(query.getId());
                                            logModule.setModuleName("hms_wh_retrieval_list");
                                            logModule.setStatus(query.getStatus());
                                            QueryResult queryResult2 = logModuleDAO.insertLog(logModule);
                                            LOGGER.info("data shipping cancel");
                                        }
                                    }
                                }
                            }
                        } catch (Exception ee) {
                            LOGGER.info("Error while reading cdars_shipping.csv");
                            ee.printStackTrace();
                        }
                    } else if (listOfFile.getName().equals("cdars_retrieval_status.csv")) {
                        fileLocation = targetLocation + listOfFile.getName();
                        LOGGER.info("Close status file found.");

                        CSVReader csvReader = null;
                        try {
                            csvReader = new CSVReader(new FileReader(fileLocation), ',', '"', 1);
                            String[] ionicFtp = null;
                            List<IonicFtpClose> closeList = new ArrayList<IonicFtpClose>();

                            while ((ionicFtp = csvReader.readNext()) != null) {
                                IonicFtpClose close = new IonicFtpClose(
                                        ionicFtp[0], ionicFtp[1], ionicFtp[2], //reqId, hwareType, hwareId
                                        ionicFtp[3], ionicFtp[4], ionicFtp[5], //qty, rack, shelf
                                        ionicFtp[6] //status
                                );
                                closeList.add(close);
                            }
                            for (IonicFtpClose c : closeList) {
                                WhShipping ftp = new WhShipping();
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
                                int count = whShippingDAO.getCountDone(c.getRequestId());
                                WhRequestDAO whRequestDAO = new WhRequestDAO();
                                int count2 = whRequestDAO.getCountDone(c.getRequestId());
                                if (count != 0 && count2 != 0) {
                                    LOGGER.info("closed - new data");
                                    WhShippingDAO WhShippingDAO = new WhShippingDAO();
                                    QueryResult queryResult1 = WhShippingDAO.updateStatus(ftp);

                                    WhRequest whReq = new WhRequest();
                                    whReq.setStatus(ftp.getStatus());
                                    whReq.setFlag("2");
                                    whReq.setRefId(refId);
                                    WhRequestDAO whReqDao = new WhRequestDAO();
                                    QueryResult que = whReqDao.updateWhRequestStatus(whReq);

                                    WhMpList whMpList = new WhMpList();
                                    WhMpListDAO whMpListDao = new WhMpListDAO();
                                    QueryResult queryMp = whMpListDao.deleteWhMpList(refId);

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
                            LOGGER.info("Error while reading cdars_retrieval_status.csv");
                            ee.printStackTrace();
                        }
                    }
                }
            }
        } else {
            LOGGER.info("Folder Empty");
        }
    }
}

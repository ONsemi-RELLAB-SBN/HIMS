package com.onsemi.hms.config;

import com.onsemi.hms.dao.InventoryMgtDAO;
import com.onsemi.hms.dao.LogModuleDAO;
import com.onsemi.hms.dao.WhInventoryDAO;
import com.onsemi.hms.dao.WhRetrieveDAO;
import com.onsemi.hms.model.IonicFtpBib;
import com.onsemi.hms.model.LogModule;
import com.onsemi.hms.model.WhInventory;
import com.onsemi.hms.model.WhInventoryMgt;
import com.onsemi.hms.model.WhRetrieve;
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

@Configuration
@EnableScheduling
public class FtpBibCard {
    private static final Logger LOGGER = LoggerFactory.getLogger(FtpBibCard.class);
    String[] args = {};

    @Autowired
    ServletContext servletContext;

    String fileLocation = "";

//    @Scheduled(cron = "0 */20 * * * ?") //every 2 minute - cron (sec min hr daysOfMth month daysOfWeek year(optional)) inactive
    public void cronRun() {
        String username = System.getProperty("user.name");
//        String targetLocation = "C:\\Users\\" + username + "\\Desktop\\BLR Documents\\CSV Import\\";
        String targetLocation = "D:\\CSV Import\\";
        File folder = new File(targetLocation);
        File[] listOfFiles = folder.listFiles();

        if(listOfFiles!=null) {
            for (File listOfFile : listOfFiles) {
                if (listOfFile.isFile()) {
                    if(listOfFile.getName().equals("hims_bibCard_migrate.csv")) {
                        fileLocation = targetLocation + listOfFile.getName();
                        LOGGER.info("Bib Card file found.");

                        CSVReader csvReader = null;      
                        try {
                            csvReader = new CSVReader(new FileReader(fileLocation), ',', '"', 1);
                            String[] ionicFtp = null;
                            List<IonicFtpBib> bibList = new ArrayList<IonicFtpBib>();

                            while ((ionicFtp = csvReader.readNext()) != null) {
                                IonicFtpBib bib = new IonicFtpBib(
                                    ionicFtp[0], ionicFtp[1], ionicFtp[2], //reqId, hwareType, hwareId
                                    ionicFtp[3], ionicFtp[4], ionicFtp[5], //loadCardId, loadCardqty, progCardId
                                    ionicFtp[6], ionicFtp[7], ionicFtp[8], //progCardQty, qty, mpNo
                                    ionicFtp[9], ionicFtp[10], ionicFtp[11], //mpExpDate, reqBy, reqEmail
                                    ionicFtp[12], ionicFtp[13] //rack, shelf
                                );
                                bibList.add(bib);
                            }
                            int count = 0;
                            for (IonicFtpBib e : bibList) {
                                String reqId = e.getReqId();
                                String eqptType = e.getEqptType();
                                String eqptId = e.getEqptId();
                                
                                String loadCardId = e.getLoadCardId();
                                if(loadCardId.equals("")) {
                                    loadCardId = null;
                                }                            
                                String loadCardQty = e.getLoadCardQty();
                                String progCardId = e.getProgCardId();
                                if(progCardId.equals("")) {
                                    progCardId = null;
                                }
                                String progCardQty = e.getProgCardQty();
                                String qty = e.getQty();
                                String mpNo = e.getMpNo();
                                String mpExp = e.getMpExp();
                                String reqBy = e.getReqBy();
                                String reqEmail = e.getReqEmail();
                                String rack = e.getRack();
                                String shelf = e.getShelf();
                                String pairingType = eqptId.substring(8, e.getEqptId().length());

                                WhRetrieve retrieve = new WhRetrieve();
                                retrieve.setRefId(reqId);
                                retrieve.setEquipmentType(eqptType);
                                retrieve.setEquipmentId(eqptId);
                                //diff hwareId
                                retrieve.setPairingType(pairingType);
                                retrieve.setLoadCardId(loadCardId);
                                retrieve.setLoadCardQty(loadCardQty);
                                retrieve.setProgCardId(progCardId);
                                retrieve.setProgCardQty(progCardQty);
                                retrieve.setQuantity(qty);
                                retrieve.setMaterialPassNo(mpNo);
                                retrieve.setMaterialPassExpiry(mpExp);
                                retrieve.setRequestedBy(reqBy);
                                retrieve.setRequestedEmail(reqEmail);
                                retrieve.setUserVerify(reqBy);
                                retrieve.setBarcodeVerify(mpNo);
                                retrieve.setTempRack(rack);
                                retrieve.setTempShelf(shelf);
                                retrieve.setTempCount("1");
                                retrieve.setStatus("Move to Inventory");
                                retrieve.setFlag("1");

                                WhRetrieveDAO retrieveDao = new WhRetrieveDAO();
                                int countRet = retrieveDao.getCountExistingData(reqId);
                                if(countRet == 0) {
                                    InventoryMgtDAO invMgtDao = new InventoryMgtDAO();
                                    int countAvailable = invMgtDao.getCountAvailableShelf(shelf);
                                    if(countAvailable != 0) {
                                        WhInventoryDAO inventoryDao = new WhInventoryDAO();
                                        int countInv = inventoryDao.getCountExistingData(reqId);
                                        if(countInv == 0) {
                                            retrieveDao = new WhRetrieveDAO();
                                            QueryResult queryRet = retrieveDao.insertBib(retrieve);
                                            count++;
                                            LOGGER.info("add new data from csv bib ... #" + count);


                                            WhInventory inventory = new WhInventory();
                                            inventory.setRefId(reqId);
                                            inventory.setMaterialPassNo(mpNo);
                                            inventory.setInventoryRack(rack);
                                            inventory.setInventoryShelf(shelf);
                                            inventory.setInventoryBy(reqBy);
                                            inventory.setStatus("Available in Inventory");
                                            inventory.setFlag("0");
                                            inventoryDao = new WhInventoryDAO();
                                            QueryResult queryInv = inventoryDao.insertEqpt(inventory);

                                            WhInventoryMgt imgt = new WhInventoryMgt();
                                            imgt.setRackId(rack);
                                            imgt.setShelfId(shelf);
                                            imgt.setHardwareId(eqptId);
                                            imgt.setMaterialPassNo(mpNo);

                                            InventoryMgtDAO inventoryMgtDao = new InventoryMgtDAO();
                                            int countShelf = inventoryMgtDao.getCountShelf(shelf);
                                            if(countShelf == 1) {
                                                inventoryMgtDao = new  InventoryMgtDAO();
                                                QueryResult queryInvMgt = inventoryMgtDao.updateInventoryDetails(imgt);
                                            }

                                            retrieveDao = new WhRetrieveDAO();
                                            WhRetrieve retLog = retrieveDao.getWhRet(reqId);
                                            LogModule logModule = new LogModule();
                                            LogModuleDAO logModuleDAO = new LogModuleDAO();
                                            logModule.setReferenceId(reqId);
                                            logModule.setModuleId(retLog.getId());
                                            logModule.setModuleName("hms_wh_retrieval_list");
                                            logModule.setStatus(retLog.getStatus() + " (initial process by Rel Lab personnel).");
                                            QueryResult queryLogRet = logModuleDAO.insertLog(logModule);
                                        }
                                    }
                                } else {
                                    WhRetrieveDAO whRetDao = new WhRetrieveDAO();
                                    WhRetrieve whRetrieve = whRetDao.getWhRet(reqId);
                                    
                                    if (countRet == 1 && whRetrieve.getPairingType() == null) {
                                        retrieveDao = new WhRetrieveDAO();
                                        QueryResult queryRet = retrieveDao.updateBib(retrieve);
                                        LOGGER.info("update data from csv bib with id " + reqId);
                                    }
                                }                  
                            }
                        } catch (Exception ee) {
                            LOGGER.info("Error while reading hims_bibCard_migrate.csv");
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
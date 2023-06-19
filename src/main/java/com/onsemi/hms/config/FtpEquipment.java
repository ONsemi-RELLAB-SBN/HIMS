package com.onsemi.hms.config;

import com.onsemi.hms.dao.InventoryMgtDAO;
import com.onsemi.hms.dao.LogModuleDAO;
import com.onsemi.hms.dao.WhInventoryDAO;
import com.onsemi.hms.dao.WhRetrieveDAO;
import com.onsemi.hms.model.IonicFtpEqpt;
import com.onsemi.hms.model.LogModule;
import com.onsemi.hms.model.WhInventory;
import com.onsemi.hms.model.WhInventoryMgt;
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
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class FtpEquipment {
    private static final Logger LOGGER = LoggerFactory.getLogger(FtpEquipment.class);
    String[] args = {};

    @Autowired
    ServletContext servletContext;

    String fileLocation = "";

//    @Scheduled(cron = "0 */15 * * * ?") //every 2 minute - cron (sec min hr daysOfMth month daysOfWeek year(optional)) //active but not needed
    public void cronRun() {
        String username = System.getProperty("user.name");
        String targetLocation = "D:\\CSV Import\\";
        File folder = new File(targetLocation);
        File[] listOfFiles = folder.listFiles();

        if(listOfFiles!=null) {
            for (File listOfFile : listOfFiles) {
                if (listOfFile.isFile()) {
                    if(listOfFile.getName().equals("hims_eqpt_migrate.csv")) {
                        fileLocation = targetLocation + listOfFile.getName();
                        LOGGER.info("Equipment file found.");

                        CSVReader csvReader = null;      
                        try {
                            csvReader = new CSVReader(new FileReader(fileLocation), ',', '"', 1);
                            String[] ionicFtp = null;
                            List<IonicFtpEqpt> eqtList = new ArrayList<IonicFtpEqpt>();

                            while ((ionicFtp = csvReader.readNext()) != null) {
                                IonicFtpEqpt close = new IonicFtpEqpt(
                                    ionicFtp[0], ionicFtp[1], ionicFtp[2], //reqId, hwareType, hwareId
                                    ionicFtp[3], ionicFtp[4], ionicFtp[5], //qty, mpNo, mpExp
                                    ionicFtp[6], ionicFtp[7], ionicFtp[8], //reqName, reqEmail, rack
                                    ionicFtp[9] //shelf
                                );
                                eqtList.add(close);
                            }
                            int count = 0;
                            for (IonicFtpEqpt e : eqtList) {
                                String reqId = e.getReqId();
                                String eqptType = e.getEqptType();
                                String eqptId = e.getEqptId();
                                String qty = e.getQty();
                                String mpNo = e.getMpNo();
                                String mpExp = e.getMpExp();
                                String reqBy = e.getReqBy();
                                String reqEmail = e.getReqEmail();
                                String rack = e.getRack();
                                String shelf = e.getShelf();

                                WhRetrieve retrieve = new WhRetrieve();
                                retrieve.setRefId(reqId);
                                retrieve.setEquipmentType(eqptType);
                                retrieve.setEquipmentId(eqptId);
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
                                            QueryResult queryRet = retrieveDao.insertEqpt(retrieve);
                                            count++;
                                            LOGGER.info("add new data from csv eqpt ... #" + count);


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
                                }
                            }
                        } catch (Exception ee) {
                            LOGGER.info("Error while reading hims_eqpt_migrate.csv");
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
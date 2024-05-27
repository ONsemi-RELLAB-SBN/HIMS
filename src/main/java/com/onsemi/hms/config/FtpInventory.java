package com.onsemi.hms.config;

import com.onsemi.hms.dao.InventoryMgtDAO;
import com.onsemi.hms.dao.WhInventoryDAO;
import com.onsemi.hms.dao.WhRetrieveDAO;
import com.onsemi.hms.model.IonicFtpRetrieve;
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

@Configuration
@EnableScheduling
public class FtpInventory {

    private static final Logger LOGGER = LoggerFactory.getLogger(FtpInventory.class);
    String[] args = {};

    @Autowired
    ServletContext servletContext;

    String fileLocation = "";

//    @Scheduled(cron = "0 */5 * * * ?") //every 5 minutes - cron (sec min hr daysOfMth month daysOfWeek year(optional)) active
    public void cronRun() {
        LOGGER.info("SINI MASUK KE HIMS CRON JOB - RUN");
        String username = System.getProperty("user.name");
//        String targetLocation = "C:\\Users\\" + username + "\\Documents\\";
//        ParameterDetailsDAO pmdao001 = new ParameterDetailsDAO();
//        String location = pmdao001.getURLPath("sf_path");
//        ParameterDetailsDAO pmdao002 = new ParameterDetailsDAO();
//        String file_retrieve = pmdao002.getURLPath("sf_retrieve");
        
        String targetLocation = "D:\\CSV Import\\";
        File folder = new File(targetLocation);
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null) {
            for (File listOfFile : listOfFiles) {
                if (listOfFile.isFile()) {
                    if (listOfFile.getName().equals("new_inventory_rack.csv")) {
                        fileLocation = targetLocation + listOfFile.getName();
                        LOGGER.info("Rack file found.");

                        CSVReader csvReader = null;
                        try {
                            csvReader = new CSVReader(new FileReader(fileLocation), ',', '"', 0);
                            String[] ionicFtp = null;

                            String shelfId = "";
                            String rackId = "";
                            int kira = 0;
                            while ((ionicFtp = csvReader.readNext()) != null) {
                                shelfId = ionicFtp[0];
                                rackId = shelfId.substring(0, 6);
                                WhInventoryMgt ftp = new WhInventoryMgt();
                                ftp.setRackId(rackId);
                                ftp.setShelfId(shelfId);
                                ftp.setHardwareId("Empty");
                                ftp.setMaterialPassNo("Empty");
                                ftp.setBoxNo("Empty");
                                InventoryMgtDAO inventoryMgtDao = new InventoryMgtDAO();
                                int count = inventoryMgtDao.getCountShelf(shelfId);
                                if (count == 0) {
                                    kira++;
                                    LOGGER.info("add new data from csv rack ... #" + kira);
                                    InventoryMgtDAO InventoryMgtDAO = new InventoryMgtDAO();
                                    QueryResult queryResult1 = InventoryMgtDAO.insertInventoryDetails(ftp);
                                } else {
                                }
                            }
                        } catch (Exception ee) {
                            LOGGER.info("Error while reading BLR_rack.csv");
                            ee.printStackTrace();
                        }
                    } else if (listOfFile.getName().equals("boxNo_gtsNo.csv")) {
                        fileLocation = targetLocation + listOfFile.getName();
                        LOGGER.info("boxNo_gtsNo file found.");

                        CSVReader csvReader = null;
                        try {
                            csvReader = new CSVReader(new FileReader(fileLocation), ',', '"', 1);
                            String[] ionicFtp = null;
                            List<IonicFtpRetrieve> retrieveList = new ArrayList<IonicFtpRetrieve>();

                            while ((ionicFtp = csvReader.readNext()) != null) {
                                IonicFtpRetrieve retrieve = new IonicFtpRetrieve(
                                        ionicFtp[0], ionicFtp[1], ionicFtp[2], //id, hwId, boxNo
                                        ionicFtp[3] // gtsNo
                                );
                                retrieveList.add(retrieve);
                            }

                            int countPass = 0;
                            int countFail = 0;
                            for (IonicFtpRetrieve r : retrieveList) {

                                WhRetrieveDAO whRetrieveDAO = new WhRetrieveDAO();
                                int count = whRetrieveDAO.getCountExistingData(r.getRefId());
                                if (count == 1) {

                                    //update whRetrieve table
                                    WhRetrieve ftp = new WhRetrieve();
                                    ftp.setRefId(r.getRefId());
                                    ftp.setBoxNo(r.getBoxNo());
                                    ftp.setGtsNo(r.getGtsNo());
                                    WhRetrieveDAO whRetDAO = new WhRetrieveDAO();
                                    QueryResult q = whRetDAO.updateBoxNoGtsNo(ftp);

                                    WhInventoryDAO invD = new WhInventoryDAO();
                                    int countInv = invD.getCountExistingData(r.getRefId());

                                    if (countInv == 1) {
                                        //update whInventory table
                                        WhInventory inv = new WhInventory();
                                        inv.setBoxNo(r.getBoxNo());
                                        inv.setRefId(r.getRefId());
                                        invD = new WhInventoryDAO();
                                        QueryResult qInv = invD.updateWhInventoryBoxNo(inv);

                                        //update inventory management
                                        whRetDAO = new WhRetrieveDAO();
                                        WhRetrieve whre = whRetDAO.getWhRet(r.getRefId());

                                        WhInventoryMgt invM = new WhInventoryMgt();
                                        invM.setBoxNo(r.getBoxNo());
                                        invM.setHardwareId(whre.getEquipmentId());
                                        invM.setMaterialPassNo(whre.getMaterialPassNo());
                                        invM.setRackId(whre.getTempRack());
                                        invM.setShelfId(whre.getTempShelf());

                                        InventoryMgtDAO invMd = new InventoryMgtDAO();
                                        QueryResult qi = invMd.updateInventoryDetails(invM);

                                        if (q.getResult() > 0 && qInv.getResult() > 0 && qi.getResult() > 0) {
                                            countPass++;
                                        } else {
                                            countFail++;
                                            LOGGER.info("referenceID :" + r.getRefId());
                                        }

                                    } else {
                                        LOGGER.info("No retrieve ID in inventory table for : " + r.getRefId());
                                    }
                                } else {
                                    LOGGER.info("No retrieve ID in retrieve table for : " + r.getRefId());
                                }
                            }
                            LOGGER.info("countPass : " + countPass);
                            LOGGER.info("countFail : " + countFail);
                        } catch (Exception ee) {
                            LOGGER.info("Error while reading boxNo_gtsNo.csv");
                            ee.printStackTrace();
                        }
                    } else if (listOfFile.getName().equals("LC_PC_ID.csv")) {
                        fileLocation = targetLocation + listOfFile.getName();
                        LOGGER.info("LC_PC_ID file found.");

                        CSVReader csvReader = null;
                        try {
                            csvReader = new CSVReader(new FileReader(fileLocation), ',', '"', 1);
                            String[] ionicFtp = null;
                            List<IonicFtpRetrieve> retrieveList = new ArrayList<IonicFtpRetrieve>();

                            while ((ionicFtp = csvReader.readNext()) != null) {
                                IonicFtpRetrieve retrieve = new IonicFtpRetrieve(
                                        ionicFtp[0], ionicFtp[1], ionicFtp[2], //id, lcID, lcQty
                                        ionicFtp[3], ionicFtp[4] // pcId, pcQty
                                );
                                retrieveList.add(retrieve);
                            }

                            int countPassLcPc = 0;
                            int countFailLcPC = 0;
                            for (IonicFtpRetrieve r : retrieveList) {

                                WhRetrieveDAO whRetrieveDAO = new WhRetrieveDAO();
                                int count = whRetrieveDAO.getCountExistingData(r.getRefId());
                                if (count == 1) {

                                    //update whRetrieve table
                                    WhRetrieve ftp = new WhRetrieve();
                                    ftp.setRefId(r.getRefId());
                                    ftp.setLoadCardId(r.getLcId());
                                    ftp.setLoadCardQty(r.getLcQty());
                                    ftp.setProgCardId(r.getPcId());
                                    ftp.setProgCardQty(r.getPcQty());
                                    WhRetrieveDAO whRetDAO = new WhRetrieveDAO();
                                    QueryResult q = whRetDAO.updateLcPc(ftp);

                                    if (q.getResult() > 0) {
                                        countPassLcPc++;
                                    } else {
                                        countFailLcPC++;
                                        LOGGER.info("referenceID :" + r.getRefId());
                                    }

                                } else {
                                    LOGGER.info("No retrieve ID in retrieve table for : " + r.getRefId());
                                }
                            }
                            LOGGER.info("countPassLcPc : " + countPassLcPc);
                            LOGGER.info("countFailLcPC : " + countFailLcPC);
                        } catch (Exception ee) {
                            LOGGER.info("Error while reading LC_PC_ID.csv");
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
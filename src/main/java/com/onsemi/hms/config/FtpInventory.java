package com.onsemi.hms.config;

import com.onsemi.hms.dao.InventoryMgtDAO;
import com.onsemi.hms.model.WhInventoryMgt;
import com.onsemi.hms.tools.QueryResult;
import com.opencsv.CSVReader;
import java.io.File;
import java.io.FileReader;
import java.util.Date;
import javax.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class FtpInventory {
    private static final Logger LOGGER = LoggerFactory.getLogger(FtpInventory.class);
    String[] args = {};

    @Autowired
    ServletContext servletContext;

    String fileLocation = "";

    @Scheduled(cron = "0 30 18 * * ?") //every 2 minute - cron (sec min hr daysOfMth month daysOfWeek year(optional))
    public void cronRun() {
        LOGGER.info("Method executed. Current time is : " + new Date());
        
        String username = System.getProperty("user.name");
        String targetLocation = "C:\\Users\\" + username + "\\Documents\\";
        File folder = new File(targetLocation);
        File[] listOfFiles = folder.listFiles();

        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                if (listOfFile.getName().equals("shelf name_14Nov16.csv")) {
                    fileLocation = targetLocation + listOfFile.getName();
                    LOGGER.info("Request file found : " + fileLocation);
                    
                    CSVReader csvReader = null;      
                    try {
                        csvReader = new CSVReader(new FileReader(fileLocation), ',', '"', 1);
                        String[] ionicFtp = null;

                        String shelfId = "";
                        String rackId = "";
                        int kira = 0;
                        while ((ionicFtp = csvReader.readNext()) != null) {
                            shelfId = ionicFtp[0];
                            WhInventoryMgt  ftp = new WhInventoryMgt();
                            ftp.setRackId(shelfId.substring(0,6));
                            ftp.setShelfId(shelfId);
                            ftp.setHardwareId("Empty");
                            ftp.setMaterialPassNo("Empty");
                            InventoryMgtDAO inventoryMgtDao = new InventoryMgtDAO();
                            int count = inventoryMgtDao.getCountShelf(shelfId);
                            if (count == 0) {
                                kira++;
                                LOGGER.info("data xdeeeeee ............................ " + kira);
                                InventoryMgtDAO InventoryMgtDAO = new InventoryMgtDAO();
                                QueryResult queryResult1 = InventoryMgtDAO.insertInventoryDetails(ftp);
                            } else {
                                LOGGER.info("data dh adaaaaa");
                            }
                        }
                    } catch (Exception ee) {}
                } 
            }
        }
    }
}
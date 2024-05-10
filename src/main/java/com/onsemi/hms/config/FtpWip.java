/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.onsemi.hms.config;

import com.onsemi.hms.dao.ParameterDetailsDAO;
import com.onsemi.hms.dao.WhWipDAO;
import com.onsemi.hms.model.WhWip;
import com.onsemi.hms.model.WhWip0;
import com.opencsv.CSVReader;
import java.io.File;
import java.io.FileReader;
import javax.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 *
 * @author zbqb9x
 */
@Configuration
@EnableScheduling
public class FtpWip {

    private static final Logger LOGGER = LoggerFactory.getLogger(FtpWip.class);
    private static final String NEW         = "0101";
    private static final String RECEIVE     = "0102";
    private static final String VERIFY      = "0103";
    private static final String REGISTER    = "0104";
    private static final String READY       = "0105";
    private static final String SHIP        = "0106";
    private static final String INVENTORY   = "0107";
    private static final String REQUEST     = "0108";
    String[] args = {};

    @Autowired
    ServletContext servletContext;

    String fileLocation     = "";
//    String targetLocation   = "D:\\HIMS_CSV\\RL\\";
    String targetLocation   = "D:\\Source Code\\archive\\CSV Import\\";
    String filename         = "cdars_wip_shipping.csv";
    String storageRead      = "cdars_zero_shipping.csv";
    String storageReq       = "cdars_zero_retrieve.csv";

//    @Scheduled(cron = "*/10 * * * * *")         // to run every 10 seconds??
//    @Scheduled(cron = "0 */10 * * * *")         // to run every 10 minutes??
    //every 2 minute - cron (sec min hr daysOfMth month daysOfWeek year(optional)) 
    //active but not needed
    public void cronRun() {
        
        LOGGER.info("FTPWIP.java - cronRun");
        String username = System.getProperty("user.name");
        File folder = new File(targetLocation);
        File[] listOfFiles = folder.listFiles();
        ParameterDetailsDAO pdao = new ParameterDetailsDAO();
        String status = pdao.getDetailByCode(NEW);

        if (listOfFiles != null) {
            for (File listOfFile : listOfFiles) {
                if (listOfFile.isFile()) {
                    if (listOfFile.getName().equals(filename)) {
                        fileLocation = targetLocation + listOfFile.getName();
                        CSVReader csvReader = null;
                        try {
                            csvReader = new CSVReader(new FileReader(fileLocation), ',', '"', 1);
                            String[] data = null;

                            while ((data = csvReader.readNext()) != null) {
                                WhWip wipdata = new WhWip();
                                wipdata.setRequestId(data[0]);
                                wipdata.setGtsNo(data[1]);
                                wipdata.setRmsEvent(data[2]);
                                wipdata.setIntervals(data[3]);
                                wipdata.setQuantity(data[4]);
                                wipdata.setShipmentDate(data[5]);
                                wipdata.setStatus(status);

                                WhWipDAO wip = new WhWipDAO();
                                int check = wip.getCountExistingData(data[0]);

                                if (check == 0) {
                                    LOGGER.info("INSERT INTO DATABASE [REQUEST ID] " + data[0]);
                                    wip = new WhWipDAO();
                                    wip.insertWhWip(wipdata);
                                } else {
                                    LOGGER.info("SKIP INSERT INTO DATABASE FOR REQUEST ID: " + data[0]);
                                }
                            }
                        } catch (Exception ee) {
                            LOGGER.info("Error while reading files " + filename);
                            ee.printStackTrace();
                        }
                    }
                }
            }

        } else {
            LOGGER.info("Folder Empty");
        }
    }
    
    @Scheduled(cron = "0 */10 * * * *")             // to run every 10 minutes??
    public void readWipShipping() {
        
        LOGGER.info("FTPWIP.java - readWipShipping");
        File folder = new File(targetLocation);
        File[] listOfFiles = folder.listFiles();
        ParameterDetailsDAO pdao = new ParameterDetailsDAO();
        String status = pdao.getDetailByCode(NEW);

        if (listOfFiles != null) {
            for (File listOfFile : listOfFiles) {
                if (listOfFile.isFile()) {
                    if (listOfFile.getName().equals(filename)) {
                        fileLocation = targetLocation + listOfFile.getName();
                        CSVReader csvReader = null;
                        try {
                            csvReader = new CSVReader(new FileReader(fileLocation), ',', '"', 1);
                            String[] data = null;

                            while ((data = csvReader.readNext()) != null) {
                                WhWip wipdata = new WhWip();
                                wipdata.setRequestId(data[0]);
                                wipdata.setGtsNo(data[1]);
                                wipdata.setRmsEvent(data[2]);
                                wipdata.setIntervals(data[3]);
                                wipdata.setQuantity(data[4]);
                                wipdata.setShipmentDate(data[5]);
                                wipdata.setStatus(status);

                                WhWipDAO wip = new WhWipDAO();
                                int check = wip.getCountExistingData(data[0]);

                                if (check == 0) {
                                    LOGGER.info("INSERT INTO DATABASE [REQUEST ID] " + data[0]);
                                    wip = new WhWipDAO();
                                    wip.insertWhWip(wipdata);
                                } else {
                                    LOGGER.info("SKIP INSERT INTO DATABASE FOR REQUEST ID: " + data[0]);
                                }
                            }
                        } catch (Exception ee) {
                            LOGGER.info("Error while reading files " + filename);
                            ee.printStackTrace();
                        }
                    }
                }
            }

        } else {
            LOGGER.info("Folder Empty");
        }
    }
    
    @Scheduled(cron = "0 */10 * * * *")             // to run every 10 minutes??
    public void readWip0Hours() {
        
        LOGGER.info("FTPWIP.java - readWip0Hours");
        File folder = new File(targetLocation);
        File[] listOfFiles = folder.listFiles();
        ParameterDetailsDAO pdao = new ParameterDetailsDAO();
        String status = pdao.getDetailByCode(NEW);

        if (listOfFiles != null) {
            for (File listOfFile : listOfFiles) {
                if (listOfFile.isFile()) {
                    if (listOfFile.getName().equals(storageRead)) {
                        fileLocation = targetLocation + listOfFile.getName();
                        CSVReader csvReader = null;
                        try {
                            csvReader = new CSVReader(new FileReader(fileLocation), ',', '"', 1);
                            String[] data = null;

                            while ((data = csvReader.readNext()) != null) {
                                WhWip0 wipdata = new WhWip0();
                                wipdata.setRequestId(data[0]);
                                wipdata.setGtsNo(data[1]);
                                wipdata.setRmsEvent(data[2]);
                                wipdata.setIntervals(data[3]);
                                wipdata.setQuantity(data[4]);
                                wipdata.setShipmentDate(data[5]);
                                wipdata.setWipStatus(status);

                                WhWipDAO wip = new WhWipDAO();
                                int check = wip.getCountData0hours(data[0]);

                                if (check == 0) {
                                    LOGGER.info("INSERT INTO DATABASE [REQUEST ID] - 0 HOUR " + data[0]);
                                    wip = new WhWipDAO();
                                    wip.insertWhWip0hour(wipdata);
                                } else {
                                    LOGGER.info("SKIP INSERT INTO DATABASE FOR REQUEST ID [0 HOUR] : " + data[0]);
                                }
                            }
                        } catch (Exception ee) {
                            LOGGER.info("Error while reading files " + filename);
                            ee.printStackTrace();
                        }
                    }
                }
            }

        } else {
            LOGGER.info("Folder new 0 hour Empty");
        }
    }
    
    @Scheduled(cron = "0 */10 * * * *")             // to run every 10 minutes??
    public void requestWip0Hours() {
        
        LOGGER.info("FUNCTION requestWip0Hours");
        File folder = new File(targetLocation);
        File[] listOfFiles = folder.listFiles();
        ParameterDetailsDAO pdao = new ParameterDetailsDAO();
        String status = pdao.getDetailByCode(REQUEST);
        
        if (listOfFiles != null) {
            for (File listOfFile : listOfFiles) {
                if (listOfFile.isFile()) {
                    if (listOfFile.getName().equals(storageReq)) {
                        fileLocation = targetLocation + listOfFile.getName();
                        CSVReader csvReader = null;
                        try {
                            csvReader = new CSVReader(new FileReader(fileLocation), ',', '"', 1);
                            String[] data = null;

                            while ((data = csvReader.readNext()) != null) {
                                WhWip0 wipdata = new WhWip0();
                                
                                // PLEASE UPDATE APA BENDA NK KENA UPDATE DEKAT SINI - read the request id dengn request date dia sahaja
                                wipdata.setRequestId(data[0]);
                                wipdata.setWipStatus(status);

                                WhWipDAO wip = new WhWipDAO();
                                int check = wip.getCount0hoursRequest(data[0]);

                                if (check == 0) {
                                    LOGGER.info("DATA REQEUST ID  " + data[0] + " CANNOT BE REQUESTED");
                                } else {
                                    LOGGER.info("UPDATE DATA FOR REQUEST ID [0 HOUR] : " + data[0] + " TO REQUESTED");
                                    wip = new WhWipDAO();
                                    wip.requestWip0hour(status, "SYSTEM", data[0]);
                                }
                            }
                        } catch (Exception ee) {
                            LOGGER.info("Error while reading files " + filename);
                            ee.printStackTrace();
                        }
                    }
                }
            }

        } else {
            LOGGER.info("Folder request WIP 0 hour Empty");
        }
    }

}
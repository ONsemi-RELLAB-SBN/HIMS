/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.onsemi.hms.config;

import com.onsemi.hms.dao.ParameterDetailsDAO;
import com.onsemi.hms.dao.WhWipDAO;
import com.onsemi.hms.model.WhWip;
import com.onsemi.hms.model.WhWipFTP;
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

/**
 *
 * @author zbqb9x
 */
@Configuration
@EnableScheduling
public class FtpWip {

    private static final Logger LOGGER = LoggerFactory.getLogger(FtpWip.class);
    private static final String NEW = "0101";
    private static final String RECEIVE = "0102";
    private static final String VERIFY = "0103";
    private static final String REGISTER = "0104";
    private static final String READY = "0105";
    private static final String SHIP = "0106";
    String[] args = {};

    @Autowired
    ServletContext servletContext;

    String fileLocation = "";
    String filename = "cdars_wip_shipping.csv";

//    @Scheduled(cron = "0 */1 * * * ?") 
    @Scheduled(cron = "*/10 * * * * *")         // to run every 10 seconds??
    //every 2 minute - cron (sec min hr daysOfMth month daysOfWeek year(optional)) 
    //active but not needed
    public void cronRun() {
        String username = System.getProperty("user.name");
        String targetLocation = "D:\\Source Code\\archive\\CSV Import\\";
        File folder = new File(targetLocation);
        File[] listOfFiles = folder.listFiles();
        ParameterDetailsDAO pdao = new ParameterDetailsDAO();
        String status = pdao.getDetailByCode(NEW);
        LOGGER.info("SINI KITA RUN UNTUK WIP FTP FILES >>> " + username);
        LOGGER.info("-------------------------------------------------------");

        if (listOfFiles != null) {
            for (File listOfFile : listOfFiles) {
                if (listOfFile.isFile()) {
                    if (listOfFile.getName().equals(filename)) {
                        fileLocation = targetLocation + listOfFile.getName();
                        LOGGER.info("Filename found >>> " + filename);
                        CSVReader csvReader = null;

                        try {
                            csvReader = new CSVReader(new FileReader(fileLocation), ',', '"', 1);
                            String[] data = null;

                            while ((data = csvReader.readNext()) != null) {
                                LOGGER.info("LOGGER for request id : " +data[0]);
                                LOGGER.info("LOGGER for maklumat shipment : " +data[5]);
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
                                    LOGGER.info("INSERT INTO DATABASE");
                                    wip = new WhWipDAO();
                                    wip.insertWhWip(wipdata);
                                } else {
                                    LOGGER.info("SKIP INSERT INTO DATABASE");
                                }
//                                eqtList.add(dataList);
                            }
                            LOGGER.info("***********************************");
//                            LOGGER.info("LOGGER for listing here : " + eqtList);
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

}

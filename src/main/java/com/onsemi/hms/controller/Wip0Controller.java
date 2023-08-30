/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.onsemi.hms.controller;

import com.onsemi.hms.config.FtpWip;
import com.onsemi.hms.dao.ParameterDetailsDAO;
import com.onsemi.hms.dao.RunningNumberDAO;
import com.onsemi.hms.dao.WhWipDAO;
import com.onsemi.hms.model.EmailList;
import com.onsemi.hms.model.UserSession;
import com.onsemi.hms.model.WhWip;
import com.onsemi.hms.model.WhWip0;
import com.onsemi.hms.model.WhWipShip;
import com.onsemi.hms.tools.EmailSender;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author zbqb9x
 */
@Controller
@RequestMapping(value = "/wip0hour")
@SessionAttributes({"userSession"})
public class Wip0Controller {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Wip0Controller.class);
    String[] args = {};
    
//    private final String FILEPATH = "D:\\HIMS_CSV\\SF\\";
    private final String FILEPATH = "D:\\Source Code\\archive\\CSV Import\\";
    private final String FILESHIP       = FILEPATH + "hms_wip_shipping.csv";
    private final String FILEVERIFIED   = FILEPATH + "hms_wip_verified.csv";
    private final String FILELOAD       = FILEPATH + "hms_wip_load.csv";
    private final String FILEUNLOAD     = FILEPATH + "hms_wip_unload.csv";
    private final String FILEINVENTORY  = FILEPATH + "hms_zero_inventory.csv";
    private final String FILESHIPBACK   = FILEPATH + "hms_zero_shipping.csv";

    private static final String STATUSCODE  = "01";
    private static final String NEW         = "0101";
    private static final String RECEIVE     = "0102";
    private static final String VERIFY      = "0103";
    private static final String REGISTER    = "0104";
    private static final String READY       = "0105";
    private static final String SHIP        = "0106";
    private static final String INVENTORY   = "0107";
    private static final String REQUEST     = "0108";
    private static final String EMAILTASK   = "02";

    private static final String COMMA_DELIMITER = ",";
    private static final String LINE_SEPARATOR  = "\n";
    private static final String HEADER          = "Column1, Column2, Column3, Column4";
    private static final String HEADERSHIP      = "RequestID,RMSEvent,Intervals,Quantity,ShipmentDate";
    private static final String HEADERVERIFY    = "RequestID,ReceiveDate,VerifyDate,Status";
    private static final String HEADERLOAD      = "RequestID,Date";
    private static final String HEADERINVENTORY = "RequestID,ReceiveDate,InventoryDate,Rack,Shelf";
    private static final String HEADERSHIPBACK  = "RequestID,ShipDate";

    @Autowired
    private MessageSource messageSource;

    @Autowired
    ServletContext servletContext;
    
    //<editor-fold defaultstate="collapsed" desc="MANUAL SYNC">
    @RequestMapping(value = "/sync", method = {RequestMethod.GET, RequestMethod.POST})
    public String manualsync0hour(Model model) {

        LOGGER.info(" ********************** START MANUAL READING FILES ZERO HOURS **********************");
        FtpWip wip = new FtpWip();
        wip.readWip0Hours();
        LOGGER.info(" ********************** COMPLETE MANUAL READING FILES ZERO HOURS **********************");
        return "redirect:/wip0hour/from";
    }
    
    @RequestMapping(value = "/syncRequest", method = {RequestMethod.GET, RequestMethod.POST})
    public String manualsync0hourRequest(Model model) {

        LOGGER.info(" ********************** START MANUAL READING FILES ZERO HOURS **********************");
        FtpWip wip = new FtpWip();
        wip.requestWip0Hours();
        LOGGER.info(" ********************** COMPLETE MANUAL READING FILES ZERO HOURS **********************");
        return "redirect:/wip0hour/from";
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="WIP for 0 hours">
    @RequestMapping(value = "/from", method = RequestMethod.GET)
    public String whList01(Model model, @ModelAttribute UserSession userSession) {

        ParameterDetailsDAO pdao = new ParameterDetailsDAO();
        String status = pdao.getDetailByCode(NEW + "','" + VERIFY);
        WhWipDAO dao = new WhWipDAO();
        List<WhWip0> wipList = dao.getWip0ByStatus(status);
        
        LocalDateTime myDateObj = LocalDateTime.now();
        String monthyear = myDateObj.getMonth().toString() + " " + myDateObj.getYear();
        
        pdao = new ParameterDetailsDAO();
        String statusVerify = pdao.getDetailByCode(VERIFY);
        
        model.addAttribute("wipList", wipList);
        model.addAttribute("monthyear", monthyear);
        model.addAttribute("status", statusVerify);
        return "whWip0/list_from_rellab";
    }
    
    @RequestMapping(value = "/scan", method = RequestMethod.GET)
    public String whList02(Model model, @ModelAttribute UserSession userSession) {

        WhWipDAO dao = new WhWipDAO();
        ParameterDetailsDAO pdao = new ParameterDetailsDAO();
        String status = pdao.getDetailByCode(READY);
        List<WhWip0> wipList = dao.getWip0ByStatus(status);
        
        model.addAttribute("wipList", wipList);
        return "whWip0/list_scan";
    }
    
    @RequestMapping(value = "/updateScanGts", method = RequestMethod.POST)
    public String whFunction01(
            Model model,
            HttpServletRequest request,
            Locale locale,
            RedirectAttributes redirectAttrs,
            @ModelAttribute UserSession userSession,
            @RequestParam(required = false) String gtsNo) throws IOException {

        String columnDate = "verify_date";
        String columnBy = "verify_by";
        String flag = "verify";
        String name = userSession.getFullname();
        WhWipDAO daoUpdate = new WhWipDAO();
        daoUpdate.updateStatus0hourByGts(columnDate, columnBy, gtsNo, flag, name);
        return "redirect:/wip0hour/from";
    }
    
    @RequestMapping(value = "/register/{requestId}", method = RequestMethod.GET)
    public String whList03(Model model, @ModelAttribute UserSession userSession, @PathVariable("requestId") String requestId) {

        WhWipDAO dao = new WhWipDAO();
        WhWip0 data = dao.getWhWip0hourByRequestId(requestId);
        model.addAttribute("wipData", data);
        return "whWip0/list_register";
    }
    
    @RequestMapping(value = "/updateRegister", method = RequestMethod.POST)
    public String whFunction02(
            Model model,
            HttpServletRequest request,
            Locale locale,
            RedirectAttributes redirectAttrs,
            @ModelAttribute UserSession userSession,
            @RequestParam(required = false) String requestId,
            @RequestParam(required = false) String rack,
            @RequestParam(required = false) String shelf) throws IOException {

        String name = userSession.getFullname();
        ParameterDetailsDAO pdao = new ParameterDetailsDAO();
        String status = pdao.getDetailByCode(INVENTORY);
        WhWipDAO daoUpdate = new WhWipDAO();
        daoUpdate.registerInventory0hour(rack, shelf, status, requestId, name);
        createCsvInventory(requestId);
        sendEmailDoneInventory(requestId);
        return "redirect:/wip0hour/from";
    }
    
    @RequestMapping(value = "/request", method = RequestMethod.GET)
    public String whList06(Model model, @ModelAttribute UserSession userSession) {

        ParameterDetailsDAO pdao = new ParameterDetailsDAO();
        String status = pdao.getDetailByCode(REQUEST);
        WhWipDAO dao = new WhWipDAO();
        List<WhWip0> wipList = dao.getWip0ByStatus(status);
        model.addAttribute("status", status);
        model.addAttribute("wipData", wipList);
        return "whWip0/list_requested";
    }
    
    @RequestMapping(value = "/pageToShip/{requestId}", method = RequestMethod.GET)
    public String whList04(Model model, @ModelAttribute UserSession userSession, @PathVariable("requestId") String requestId) {

        WhWipDAO dao = new WhWipDAO();
        WhWip0 data = dao.getWhWip0hourByRequestId(requestId);
        model.addAttribute("wipData", data);
        return "whWip0/list_to_ship";
    }
    
    @RequestMapping(value = "/updateToShip", method = RequestMethod.POST)
    public String whFunction03(
            Model model,
            HttpServletRequest request,
            Locale locale,
            RedirectAttributes redirectAttrs,
            @ModelAttribute UserSession userSession,
            @RequestParam(required = false) String requestId) throws IOException {
        
        WhWipDAO daoUpdate = new WhWipDAO();
        daoUpdate.regToShip0hour(requestId);
        return "redirect:/wip0hour/to";
    }
    
    @RequestMapping(value = "/to", method = RequestMethod.GET)
    public String whList05(Model model, @ModelAttribute UserSession userSession) {

        ParameterDetailsDAO pdao = new ParameterDetailsDAO();
        String status = pdao.getDetailByCode(SHIP);
        WhWipDAO dao = new WhWipDAO();
        List<WhWip0> wipList = dao.getWip0ByStatus(status);
        
        LocalDateTime myDateObj = LocalDateTime.now();
        String monthyear = myDateObj.getMonth().toString() + " " + myDateObj.getYear();
        
        model.addAttribute("wipList", wipList);
        model.addAttribute("monthyear", monthyear);
        return "whWip0/list_to";
    }
    
    @RequestMapping(value = "/readyList", method = RequestMethod.GET)
    public String whList07(Model model, @ModelAttribute UserSession userSession) {

        ParameterDetailsDAO pdao = new ParameterDetailsDAO();
        String status = pdao.getDetailByCode(READY);
        WhWipDAO dao = new WhWipDAO();
        List<WhWip0> wipList = dao.getWip0ByStatus(status);
        
        String latestShouldBe = getLatestRunningNumber();
        model.addAttribute("status", status);
        model.addAttribute("wipData", wipList);
        model.addAttribute("wipBox", latestShouldBe);
        return "whWip0/list_ready";
    }
    
    @RequestMapping(value = "/updateShipmentList", method = RequestMethod.POST)
    public String whFunction04(
            Model model,
            HttpServletRequest request,
            Locale locale,
            RedirectAttributes redirectAttrs,
            @ModelAttribute UserSession userSession,
            @RequestParam(required = false) String shipDate,
            @RequestParam(required = false) String shippingList) throws IOException, ParseException {
        
        String name = userSession.getFullname();
        updateRunningNumber(shippingList);
        createCsvShipBack(shippingList, shipDate, name);
        sendEmailDoneShipBack(shippingList);
        return "redirect:/wip0hour/to";
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="FUNCTION">
    private String[] getEmailList(String task) {
        ArrayList<String> To = new ArrayList<String>();
        WhWipDAO dao = new WhWipDAO();
        List<EmailList> toList = dao.getUserToInform(task);
        for (EmailList to : toList) {
            To.add(to.getEmail());
        }
        String[] myArrayTo = new String[To.size()];
        String[] emailTo = To.toArray(myArrayTo);
        return emailTo;
    }
    
    private void sendEmailDoneInventory(String requestId) {

        String username = "All";
        String[] receiver = {"fg79cj@onsemi.com", "zbqb9x@onsemi.com", "fg7dtj@onsemi.com"};
        // TODO - create 1 new email list for INVENTORY
        String[] listAdmin = getEmailList("Notify Ship");
        String[] listSystem = getEmailList("System");
        String subject = "WIP Zero Hour Done Inventory";
        String msg1 = "WIP zero hour done inventory";
        // TODO please create and design the email content here later
        String msg2 = tableWipInventory(requestId);               
        EmailSender send = new EmailSender();
        // TODO - PLEASE CREATE ANOTHER EMAIL TO DO THE INVENTORY NOTIFICATION
        send.wipEmail(servletContext, username, receiver, subject, msg2, "INVENTORY");
        send = new EmailSender();
        send.wipEmailWithAttach(servletContext, username, receiver, subject, msg1, "INVENTORY");
    }
    
    private void sendEmailDoneShipBack(String shipList) throws ParseException {

        String username = "All";
        String[] receiver = {"fg79cj@onsemi.com", "zbqb9x@onsemi.com", "fg7dtj@onsemi.com"};
        // TODO - create 1 new email list for SHIP BACK
        String[] listAdmin = getEmailList("Notify Ship");
        String[] listSystem = getEmailList("System");
        String subject = "WIP Zero Hour Shipped To Rel Lab";
        String msg1 = "WIP zero hour is shipped to Rel Lab from Sg Gadut";
        // TODO please create and design the email content here later
        String msg2 = tableWipShip0hour(shipList);
        EmailSender send = new EmailSender();
        // TODO - PLEASE CREATE ANOTHER EMAIL TO DO THE INVENTORY NOTIFICATION
        send.wipEmail(servletContext, username, receiver, subject, msg2, "SHIPBACK");
        send = new EmailSender();
        send.wipEmailWithAttach(servletContext, username, receiver, subject, msg1, "SHIPBACK");
    }
    
    private void createCsvInventory(String requestId) {

        WhWipDAO daoGet = new WhWipDAO();
        WhWip0 wip = daoGet.getWhWip0hourByRequestId(requestId);
        File file = new File(FILEINVENTORY);
        FileWriter fileWriter = null;

        if (file.exists()) {
            try {
                fileWriter = new FileWriter(FILEINVENTORY, true);

                //New Line after the header
                fileWriter.append(LINE_SEPARATOR);
                fileWriter.append(wip.getRequestId());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(wip.getVerifyDate());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(wip.getRegisterDate());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(wip.getRack());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(wip.getShelf());
                System.out.println("Update existing to CSV file Succeed!!!");
            } catch (Exception ee) {
                ee.printStackTrace();
            } finally {
                try {
                    fileWriter.close();
                } catch (IOException ie) {
                    System.out.println("Error occured while closing the fileWriter");
                    ie.printStackTrace();
                }
            }
        } else {
            try {
                fileWriter = new FileWriter(FILEINVENTORY, true);
                //Adding the header
                fileWriter.append(HEADERINVENTORY);

                //New Line after the header
                fileWriter.append(LINE_SEPARATOR);
                fileWriter.append(wip.getRequestId());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(wip.getVerifyDate());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(wip.getRegisterDate());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(wip.getRack());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(wip.getShelf());
                System.out.println("Write new to CSV file Succeed!!!");
            } catch (Exception ee) {
                ee.printStackTrace();
            } finally {
                try {
                    fileWriter.close();
                } catch (IOException ie) {
                    System.out.println("Error occured while closing the fileWriter");
                    ie.printStackTrace();
                }
            }
        }
    }
    
    private void createCsvShipBack(String shippingList, String shipDate, String name) {

        File file = new File(FILESHIPBACK);
        ParameterDetailsDAO pdao = new ParameterDetailsDAO();
        String statusShip = pdao.getDetailByCode(READY);
        pdao = new ParameterDetailsDAO();
        String status = pdao.getDetailByCode(SHIP);

        WhWipDAO daoGet = new WhWipDAO();
        List<WhWip0> dataList = daoGet.getWip0ByStatus(statusShip);

        for (int i = 0; i < dataList.size(); i++) {
            WhWip0 wip = new WhWip0();
            wip.setId(dataList.get(i).getId());
            wip.setRequestId(dataList.get(i).getRequestId());
            wip.setShipDate(shipDate);
            wip.setShipBy(name);
            wip.setShipList(shippingList);
            wip.setWipStatus(status);
            
            WhWipDAO dao = new WhWipDAO();
            dao.updateShip0hour(wip.getId(), name, shipDate, shippingList);
            
            // START LOGGER
            dao = new WhWipDAO();
            WhWipShip wip2 = new WhWipShip();
            wip2.setWipId(dataList.get(i).getId());
            wip2.setWipShipList(shippingList);
            dao.insertWhWipShip(wip2);
            // END LOGGER
            
            dao = new WhWipDAO();
            String tarikhShip = dao.getShipDateByRequestId(wip.getId());
            FileWriter fileWriter = null;

            if (file.exists()) {
                try {
                    fileWriter = new FileWriter(FILESHIPBACK, true);

                    //New Line after the header
                    fileWriter.append(LINE_SEPARATOR);
                    fileWriter.append(wip.getRequestId());
                    fileWriter.append(COMMA_DELIMITER);
                    fileWriter.append(tarikhShip);
                    System.out.println("Update existing to CSV file Succeed!!!");
                } catch (Exception ee) {
                    ee.printStackTrace();
                } finally {
                    try {
                        fileWriter.close();
                    } catch (IOException ie) {
                        System.out.println("Error occured while closing the fileWriter");
                        ie.printStackTrace();
                    }
                }
            } else {
                try {
                    fileWriter = new FileWriter(FILESHIPBACK, true);
                    //Adding the header
                    fileWriter.append(HEADERSHIPBACK);

                    //New Line after the header
                    fileWriter.append(LINE_SEPARATOR);
                    fileWriter.append(wip.getRequestId());
                    fileWriter.append(COMMA_DELIMITER);
                    fileWriter.append(tarikhShip);
                    System.out.println("Write new to CSV file Succeed!!!");
                } catch (Exception ee) {
                    ee.printStackTrace();
                } finally {
                    try {
                        fileWriter.close();
                    } catch (IOException ie) {
                        System.out.println("Error occured while closing the fileWriter");
                        ie.printStackTrace();
                    }
                }
            }
        }
    }
    
    private String tableWipShip(String shipList) {
        
        WhWipDAO wipdao = new WhWipDAO();
        List<WhWip> listWip = wipdao.getAllWipByShipList(shipList);

        String shipDate = "";
        String rmsEvent = "";
        String intervals = "";
        String quantity = "";
        String text = "<table width='90%'>";
        text += "<tr>"
                + "<th><span>No</span></th>"
                + "<th><span>RMS Event</span></th>"
                + "<th><span>Intervals</span></th>"
                + "<th><span>Quantity</span></th>"
                + "<th><span>Shipment Date</span></th>"
              + "</tr>";

        for (int i = 0; i < listWip.size(); i++) {
            rmsEvent = listWip.get(i).getRmsEvent();
            intervals = listWip.get(i).getIntervals();
            quantity = listWip.get(i).getShipQuantity();
            shipDate = listWip.get(i).getShipDate();
            int index = i + 1;
            text += "<tr align = \"center\">";
            text += "<td>" + index + "</td>";
            text += "<td>" + rmsEvent + "</td>";
            text += "<td>" + intervals + "</td>";
            text += "<td>" + quantity + "</td>";
            text += "<td>" + shipDate + "</td>";
            text += "</tr>";
        }
        text += "</table>";
        return text;
    }
    
    private String tableWipInventory(String requestId) {
        
        WhWipDAO wipdao = new WhWipDAO();
        WhWip0 wip = wipdao.getWhWip0hourByRequestId(requestId);

        String text = "<table width='90%'>";
        text += "<tr>"
                + "<th><span>RMS Event</span></th>"
                + "<th><span>Rack</span></th>"
                + "<th><span>Shelf</span></th>"
                + "<th><span>Inventory Date</span></th>"
              + "</tr>";
        text += "<tr align = \"center\">";
        text += "<td>" + wip.getRmsEvent() + "</td>";
        text += "<td>" + wip.getRack() + "</td>";
        text += "<td>" + wip.getShelf() + "</td>";
        text += "<td>" + wip.getRegisterDate() + "</td>";
        text += "</tr>";
        text += "</table>";
        return text;
    }
    
    private String tableWipShip0hour(String shipList) throws ParseException {
        
        WhWipDAO wipdao = new WhWipDAO();
        List<WhWip0> listWip = wipdao.getAll0hourWipByShipList(shipList);

        String shipDate = "";
        String rmsEvent = "";
        String text = "<table width='90%'>";
        text += "<tr>"
                + "<th><span>No</span></th>"
                + "<th><span>RMS Event</span></th>"
                + "<th><span>Shipment Date</span></th>"
              + "</tr>";

        for (int i = 0; i < listWip.size(); i++) {
            rmsEvent = listWip.get(i).getRmsEvent();
            shipDate = listWip.get(i).getShipDate();
            int index = i + 1;
            text += "<tr align = \"center\">";
            text += "<td>" + index + "</td>";
            text += "<td>" + rmsEvent + "</td>";
            text += "<td>" + tukarFormat02(shipDate) + "</td>";
            text += "</tr>";
        }
        text += "</table>";
        return text;
    }
    
    private String getLatestRunningNumber() {

        String runningNumber = "0001";
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatYear = DateTimeFormatter.ofPattern("yyyy");
        DateTimeFormatter myFormatMonth = DateTimeFormatter.ofPattern("MM");
        String month = myDateObj.format(myFormatMonth);
        String year = myDateObj.format(myFormatYear);

        RunningNumberDAO dao = new RunningNumberDAO();
        String checkLatest = dao.getLatestRunning(year, month);

        if (checkLatest.equals("0")) {
            // THIS MEAN NEW MONTH, SO NEED TO CREATE NEW RUNNING NUMBER RECORD
            dao = new RunningNumberDAO();
            dao.insertRunningNumber(year, month, runningNumber);
        } else if (checkLatest.equals("")) {
            dao = new RunningNumberDAO();
            dao.insertRunningNumber(year, month, runningNumber);
        } else {
            // THE RUNNING NUMBER FOR THE MONTH ALREADY EXIST, JUST UPDATE THE RUNNING NUMBER
            int data = Integer.parseInt(checkLatest);
            runningNumber = String.format("%04d", data + 1);
        }
        runningNumber = year + month + runningNumber;
        return runningNumber;
    }
    
    private void updateRunningNumber(String shippingList) {

        String year = shippingList.substring(0, 4);
        String month = shippingList.substring(4, 6);
        String runningNumber = shippingList.substring(6, 10);
        RunningNumberDAO dao = new RunningNumberDAO();
        dao.updateRunningNumber(runningNumber, year, month);
    }
    
    private String tukarFormatDate01(String date) {
        // 01 August 2023 11:00:00 AM
        LocalDateTime dateTime = LocalDateTime.parse(date);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm:ss a");
        String formattedDateTime = formatter.format(dateTime);
        return formattedDateTime;
    }
    
    private String tukarFormat02(String date) throws ParseException {
        // 01 Sep 2023 01:01:00 AM
        DateFormat outputDateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss a");
        Date date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
        String formattedDate = outputDateFormat.format(date2);
        return formattedDate;
    }
    //</editor-fold>
    
}
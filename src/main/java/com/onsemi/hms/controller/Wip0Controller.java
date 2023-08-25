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
import com.onsemi.hms.model.ParameterDetails;
import com.onsemi.hms.model.User;
import com.onsemi.hms.model.UserSession;
import com.onsemi.hms.model.WhWip;
import com.onsemi.hms.model.WhWip0;
import com.onsemi.hms.model.WhWipShip;
import com.onsemi.hms.tools.EmailSender;
import com.onsemi.hms.tools.QueryResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import org.springframework.web.servlet.ModelAndView;
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
    private final String FILEINVENTORY  = FILEPATH + "hms_wip_inventory.csv";
    private final String FILESHIPBACK   = FILEPATH + "hms_wip_shipback.csv";

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
    private static final String HEADERINVENTORY = "RequestID,Rack,Shelf,Date";
    private static final String HEADERSHIPBACK  = "RequestID,Date";

    @Autowired
    private MessageSource messageSource;

    @Autowired
    ServletContext servletContext;
    
    //<editor-fold defaultstate="collapsed" desc="MANUAL SYNC">
    @RequestMapping(value = "/sync", method = {RequestMethod.GET, RequestMethod.POST})
    public String manualsync0hour(Model model) {

        LOGGER.info(" ********************** START MANUAL READING FILES 0 HOURS **********************");
        FtpWip wip = new FtpWip();
        wip.readWip0Hours();
        LOGGER.info(" ********************** COMPLETE MANUAL READING FILES 0 HOURS **********************");
        return "redirect:/wip0hour/from";
    }
    
    @RequestMapping(value = "/syncRequest", method = {RequestMethod.GET, RequestMethod.POST})
    public String manualsync0hourRequest(Model model) {

        LOGGER.info(" ********************** START MANUAL READING FILES 0 HOURS **********************");
        FtpWip wip = new FtpWip();
        wip.requestWip0Hours();
        LOGGER.info(" ********************** COMPLETE MANUAL READING FILES 0 HOURS **********************");
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

        LOGGER.info("LOGGER for request id : " +requestId);
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
        
        // SEND EMAIL TO HIMS-RL TO UPDATE THE STATUS ALREADY COMPLETE INVENTORY
        // SEND CSV FILES?
        return "redirect:/wip0hour/from";
    }
    
    @RequestMapping(value = "/request", method = RequestMethod.GET)
    public String whList06(Model model, @ModelAttribute UserSession userSession) {

        ParameterDetailsDAO pdao = new ParameterDetailsDAO();
        String status = pdao.getDetailByCode(REQUEST);
        WhWipDAO dao = new WhWipDAO();
        List<WhWip0> wipList = dao.getWip0ByStatus(status);
        LOGGER.info("LOGGER for xxx : " +status);
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
            @RequestParam(required = false) String requestId,
            @RequestParam(required = false) String rack,
            @RequestParam(required = false) String shelf) throws IOException {
        
        LOGGER.info("LOGGER for updateToShip : ");

        String name = userSession.getFullname();
        ParameterDetailsDAO pdao = new ParameterDetailsDAO();
        String status = pdao.getDetailByCode(INVENTORY);
//        WhWipDAO daoUpdate = new WhWipDAO();
//        daoUpdate.registerInventory0hour(rack, shelf, status, requestId, name);
        
        // SEND EMAIL TO HIMS-RL TO UPDATE THE STATUS ALREADY COMPLETE INVENTORY
        // SEND CSV FILES?
        return "redirect:/wip0hour/to";
    }
    
    @RequestMapping(value = "/to", method = RequestMethod.GET)
    public String whList05(Model model, @ModelAttribute UserSession userSession) {

        return "whWip0/list_to";
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
    
    private void sendEmailDoneInventory(String shipList) {

        String username = "All";
//        String[] receiver = {"fg79cj@onsemi.com", "zbqb9x@onsemi.com"};
        // TODO - create 1 new email list for INVENTORY
        String[] listAdmin = getEmailList("Notify Ship");
        String[] listSystem = getEmailList("System");
        String subject = "WIP is Shipped to Rel Lab";
        String msg1 = "WIP is shipped to Rel Lab from Sg Gadut";
        // TODO please create and design the email content here later
        String msg2 = tableWipShip(shipList);               
        EmailSender send = new EmailSender();
        // TODO - PLEASE CREATE ANOTHER EMAIL TO DO THE INVENTORY NOTIFICATION
        send.wipEmail(servletContext, username, listAdmin, subject, msg2, "INVENTORY");
        send = new EmailSender();
        send.wipEmailWithAttach(servletContext, username, listSystem, subject, msg1, "INVENTORY");
    }
    
    private void sendEmailDoneShipBack(String shipList) {

        String username = "All";
//        String[] receiver = {"fg79cj@onsemi.com", "zbqb9x@onsemi.com"};
        // TODO - create 1 new email list for SHIP BACK
        String[] listAdmin = getEmailList("Notify Ship");
        String[] listSystem = getEmailList("System");
        String subject = "WIP is Shipped to Rel Lab";
        String msg1 = "WIP is shipped to Rel Lab from Sg Gadut";
        // TODO please create and design the email content here later
        String msg2 = tableWipShip(shipList);               
        EmailSender send = new EmailSender();
        // TODO - PLEASE CREATE ANOTHER EMAIL TO DO THE INVENTORY NOTIFICATION
        send.wipEmail(servletContext, username, listAdmin, subject, msg2, "SHIP");
        send = new EmailSender();
        send.wipEmailWithAttach(servletContext, username, listSystem, subject, msg1, "SHIP");
    }
    
    private void createCsvInventory(String requestId) {

        //  TODO - please update this FUNCTION
        WhWipDAO daoGet = new WhWipDAO();
        ParameterDetailsDAO pdao = new ParameterDetailsDAO();
        File file = new File(FILEINVENTORY);

        String statusVerify = pdao.getDetailByCode(INVENTORY);
        List<WhWip> dataList = daoGet.getWipByGtsNo(requestId);

        for (int i = 0; i < dataList.size(); i++) {
            WhWip wip = new WhWip();
            wip.setId(dataList.get(i).getId());
            wip.setRequestId(dataList.get(i).getRequestId());
            wip.setReceiveDate(dataList.get(i).getReceiveDate());
            wip.setVerifyDate(dataList.get(i).getVerifyDate());
            wip.setStatus(statusVerify);

            FileWriter fileWriter = null;

            if (file.exists()) {
                try {
                    fileWriter = new FileWriter(FILEINVENTORY, true);

                    //New Line after the header
                    fileWriter.append(LINE_SEPARATOR);
                    fileWriter.append(wip.getRequestId());
                    fileWriter.append(COMMA_DELIMITER);
                    fileWriter.append(wip.getReceiveDate());
                    fileWriter.append(COMMA_DELIMITER);
                    fileWriter.append(wip.getVerifyDate());
                    fileWriter.append(COMMA_DELIMITER);
                    fileWriter.append(wip.getStatus());
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
                    fileWriter.append(HEADERVERIFY);

                    //New Line after the header
                    fileWriter.append(LINE_SEPARATOR);
                    fileWriter.append(wip.getRequestId());
                    fileWriter.append(COMMA_DELIMITER);
                    fileWriter.append(wip.getReceiveDate());
                    fileWriter.append(COMMA_DELIMITER);
                    fileWriter.append(wip.getVerifyDate());
                    fileWriter.append(COMMA_DELIMITER);
                    fileWriter.append(wip.getStatus());
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
    
    private void createCsvShipBack(String requestId) {

        //  TODO - please update this FUNCTION
        WhWipDAO daoGet = new WhWipDAO();
        ParameterDetailsDAO pdao = new ParameterDetailsDAO();
        File file = new File(FILESHIPBACK);

        String statusVerify = pdao.getDetailByCode(SHIP);
        List<WhWip> dataList = daoGet.getWipByGtsNo(requestId);

        for (int i = 0; i < dataList.size(); i++) {
            WhWip wip = new WhWip();
            wip.setId(dataList.get(i).getId());
            wip.setRequestId(dataList.get(i).getRequestId());
            wip.setReceiveDate(dataList.get(i).getReceiveDate());
            wip.setVerifyDate(dataList.get(i).getVerifyDate());
            wip.setStatus(statusVerify);

            FileWriter fileWriter = null;

            if (file.exists()) {
                try {
                    fileWriter = new FileWriter(FILESHIPBACK, true);

                    //New Line after the header
                    fileWriter.append(LINE_SEPARATOR);
                    fileWriter.append(wip.getRequestId());
                    fileWriter.append(COMMA_DELIMITER);
                    fileWriter.append(wip.getReceiveDate());
                    fileWriter.append(COMMA_DELIMITER);
                    fileWriter.append(wip.getVerifyDate());
                    fileWriter.append(COMMA_DELIMITER);
                    fileWriter.append(wip.getStatus());
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
                    fileWriter.append(HEADERVERIFY);

                    //New Line after the header
                    fileWriter.append(LINE_SEPARATOR);
                    fileWriter.append(wip.getRequestId());
                    fileWriter.append(COMMA_DELIMITER);
                    fileWriter.append(wip.getReceiveDate());
                    fileWriter.append(COMMA_DELIMITER);
                    fileWriter.append(wip.getVerifyDate());
                    fileWriter.append(COMMA_DELIMITER);
                    fileWriter.append(wip.getStatus());
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
        String text = "<table width='90%'><tr>"
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
            text = text + "<tr align = \"center\">";
            text = text + "<td>" + index + "</td>";
            text = text + "<td>" + rmsEvent + "</td>";
            text = text + "<td>" + intervals + "</td>";
            text = text + "<td>" + quantity + "</td>";
            text = text + "<td>" + shipDate + "</td>";
            text = text + "</tr>";
        }
        text = text + "</table>";
        return text;
    }
    //</editor-fold>
    
}
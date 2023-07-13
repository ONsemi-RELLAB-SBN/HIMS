/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.onsemi.hms.controller;

import com.onsemi.hms.config.FtpWip;
import com.onsemi.hms.dao.ParameterDetailsDAO;
import com.onsemi.hms.dao.RunningNumberDAO;
import com.onsemi.hms.dao.WhWipDAO;
import com.onsemi.hms.model.ParameterDetails;
import com.onsemi.hms.model.UserSession;
import com.onsemi.hms.model.WhWip;
import com.onsemi.hms.model.WhWipShip;
import com.onsemi.hms.tools.EmailSender;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
@RequestMapping(value = "/whWip")
@SessionAttributes({"userSession"})
public class WipController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WipController.class);
    String[] args = {};

    private static final String UPLOADED_FOLDER = "E:\\HIMS_Upload\\";
    private static final String FILEPATH = "D:\\Source Code\\archive\\CSV Import\\hms_wip_shipping.csv";
    private static final String FILEPATHSHIP = "D:\\Source Code\\archive\\CSV Import\\hms_wip_shipping.csv";
    private static final String FILEPATHVERIFY = "D:\\Source Code\\archive\\CSV Import\\hms_wip_verified.csv";

    private static final String STATUSCODE = "01";
    private static final String NEW = "0101";
    private static final String RECEIVE = "0102";
    private static final String VERIFY = "0103";
    private static final String REGISTER = "0104";
    private static final String READY = "0105";
    private static final String SHIP = "0106";

    private static final String COMMA_DELIMITER = ",";
    private static final String LINE_SEPARATOR = "\n";
    private static final String HEADER = "Column1, Column2, Column3, Column4";
    private static final String HEADERSHIP = "RequestID,RMSEvent,Intervals,Quantity,ShipmentDate";
    private static final String HEADERVERIFY = "RequestID,ReceiveDate,VerifyDate,Status";

    @Autowired
    private MessageSource messageSource;

    @Autowired
    ServletContext servletContext;

//    @RequestMapping(value = "wipList", method = RequestMethod.GET)
    @RequestMapping(value = "/sync", method = {RequestMethod.GET, RequestMethod.POST})
    public String manualsync(Model model) {

        LOGGER.info(" ********************** START MANUAL READING FILES **********************");
        FtpWip wip = new FtpWip();
        wip.cronRun();
        LOGGER.info(" ********************** COMPLETE MANUAL READING FILES **********************");
        return "redirect:/whWip/listNew";
    }

    @RequestMapping(value = "/from", method = RequestMethod.GET)
    public String whFromList(Model model, @ModelAttribute UserSession userSession) {

        ParameterDetailsDAO pdao = new ParameterDetailsDAO();
        String status = pdao.getDetailByCode(NEW);
        WhWipDAO dao = new WhWipDAO();
        List<WhWip> wipList = dao.getWhWipByStatus(status);
        model.addAttribute("wipList", wipList);
        return "whWip/from_list";
    }

    @RequestMapping(value = "/to", method = RequestMethod.GET)
    public String whToList(Model model, @ModelAttribute UserSession userSession) {

        ParameterDetailsDAO pdao = new ParameterDetailsDAO();
        WhWipDAO dao = new WhWipDAO();
        
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatYear = DateTimeFormatter.ofPattern("yyyy");
        DateTimeFormatter myFormatMonth = DateTimeFormatter.ofPattern("MM");
        String month = myDateObj.format(myFormatMonth);
        String year = myDateObj.format(myFormatYear);
        String shippingList = year + month;
        String monthyear = myDateObj.getMonth().toString() + " " + myDateObj.getYear();
        List<WhWip> wipList = dao.getWipShipment(shippingList);
        model.addAttribute("wipList", wipList);
        model.addAttribute("monthyear", monthyear);
        return "whWip/to_list";
    }

    @RequestMapping(value = "/listNew", method = RequestMethod.GET)
    public String whNewList(Model model, @ModelAttribute UserSession userSession) {

        ParameterDetailsDAO pdao = new ParameterDetailsDAO();
        String status = pdao.getDetailByCode(NEW + "','" + RECEIVE);
        pdao = new ParameterDetailsDAO();
        String statusReceive = pdao.getDetailByCode(RECEIVE);
        WhWipDAO dao = new WhWipDAO();
        List<WhWip> wipList = dao.getWhWipByStatus(status);
        model.addAttribute("wipList", wipList);
        model.addAttribute("status", statusReceive);
        return "whWip/list_new";
    }

    @RequestMapping(value = "/listReceive", method = RequestMethod.GET)
    public String whReceiveList(Model model, @ModelAttribute UserSession userSession) {

        ParameterDetailsDAO pdao = new ParameterDetailsDAO();
        String status = pdao.getDetailByCode(NEW);
        WhWipDAO dao = new WhWipDAO();
        List<WhWip> wipList = dao.getWhWipByStatus(status);
        model.addAttribute("wipList", wipList);
        return "whWip/list_receive";
    }

    @RequestMapping(value = "/updateReceive", method = RequestMethod.POST)
    public String updateReceive(
            Model model,
            HttpServletRequest request,
            Locale locale,
            RedirectAttributes redirectAttrs,
            @ModelAttribute UserSession userSession,
            @RequestParam(required = false) String boxNo) throws IOException {

        String columnDate = "receive_date";
        String columnBy = "receive_by";
        String gtsNo = boxNo;
        String flag = "receive";
        String name = userSession.getFullname();
        WhWipDAO daoUpdate = new WhWipDAO();
        daoUpdate.updateStatusByGts(columnDate, columnBy, gtsNo, flag, name);
        return "redirect:/whWip/listNew";
    }

    @RequestMapping(value = "/listVerify/{requestId}", method = RequestMethod.GET)
    public String whVerifyList(Model model, @ModelAttribute UserSession userSession, @PathVariable("requestId") String requestId) {

        WhWipDAO dao = new WhWipDAO();
        WhWip data = dao.getWhWipByRequestId(requestId);
        model.addAttribute("wipData", data);
        return "whWip/list_verify";
    }

    @RequestMapping(value = "/updateVerify", method = RequestMethod.POST)
    public String updateVerify(
            Model model,
            HttpServletRequest request,
            Locale locale,
            RedirectAttributes redirectAttrs,
            @ModelAttribute UserSession userSession,
            @RequestParam(required = false) String requestId) {

//         THIS FUNCTION WILL UPDATE THE STATUS FROM RECEIVED TO VERIFIED
        WhWipDAO daoUpdate = new WhWipDAO();
        WhWipDAO daoCheck1 = new WhWipDAO();
        WhWipDAO daoCheck2 = new WhWipDAO();
        WhWipDAO daoCheck3 = new WhWipDAO();
        ParameterDetailsDAO pdao = new ParameterDetailsDAO();
        String name = userSession.getFullname();

        String statusVerify = pdao.getDetailByCode(VERIFY);
        daoUpdate.updateVerify(requestId, name);

        String gtsno = daoCheck3.getWipGtsByRequestId(requestId);
        int checkNumber = daoCheck1.getCountByGtsNo(requestId);
        int checkVerify = daoCheck2.getCountByGtsNoAndStatus(requestId, statusVerify);

        if (checkNumber == checkVerify) {
            LOGGER.info("********************** WIP VERIFIED START **********************");
            sendCsvForVerify(gtsno);
            sendEmailVerifyWip(gtsno);
            LOGGER.info("********************** WIP VERIFIED END **********************");
        }

        return "redirect:/whWip/listNew";
    }

    @RequestMapping(value = "/listRegister", method = RequestMethod.GET)
    public String whRegisterList(Model model, @ModelAttribute UserSession userSession) {

        ParameterDetailsDAO pdao = new ParameterDetailsDAO();
        String status = pdao.getDetailByCode(VERIFY);
        WhWipDAO dao = new WhWipDAO();
        List<WhWip> wipList = dao.getWhWipByStatus(status);
        model.addAttribute("wipList", wipList);
        return "whWip/list_register";
    }

    @RequestMapping(value = "/registerPage", method = RequestMethod.GET)
    public String whRegisterPage(Model model, @ModelAttribute UserSession userSession) {

        WhWipDAO dao = new WhWipDAO();
        ParameterDetailsDAO pdao = new ParameterDetailsDAO();
        String status = pdao.getDetailByCode(READY);
        List<WhWip> packingList = dao.getWhWipByStatus(status);
        model.addAttribute("packingList", packingList);
        return "whWip/register_page";
    }

    @RequestMapping(value = "/updateVerifyToRegister", method = RequestMethod.POST)
    public String updateRegister(Model model, Locale locale, RedirectAttributes redirectAttrs,
            @ModelAttribute UserSession userSession,
            @RequestParam(required = false) String tripTicket,
            @RequestParam(required = false) String quantity,
            @RequestParam(required = false) String intervals) {

        String name = userSession.getFullname();
        WhWipDAO daoSelect = new WhWipDAO();
        WhWip daoData = daoSelect.getWipByRmsInterval(tripTicket, intervals);
        String checkQty = daoData.getQuantity();
        String status = daoData.getStatus();
        daoData.setShipQuantity(quantity);
        daoData.setRegisterBy(name);
        ParameterDetailsDAO pdao = new ParameterDetailsDAO();
        String checkStatus = pdao.getDetailByCode(VERIFY);
        pdao = new ParameterDetailsDAO();
        String checkReady = pdao.getDetailByCode(READY);
        args = new String[2];
        args[0] = tripTicket + " [" + intervals + "]";
        args[1] = " [LIMIT to " + checkQty + " pcs] ";
        
        if (status == null ? checkStatus == null : status.equals(checkStatus)) {
            WhWipDAO daoUpdate = new WhWipDAO();
            if (Integer.parseInt(checkQty) < Integer.parseInt(quantity)) {
                redirectAttrs.addFlashAttribute("success", messageSource.getMessage("general.label.save.errorwip2", args, locale));
            } else {
                daoUpdate.updateRegister(daoData);
                redirectAttrs.addFlashAttribute("success", messageSource.getMessage("general.label.save.successwip1", args, locale));
            }
        } else if (status == null ? checkStatus == null : status.equalsIgnoreCase(checkReady)) {
            redirectAttrs.addFlashAttribute("success", messageSource.getMessage("general.label.save.errorwip3", args, locale));
        } else {
            if (status == null) {
                redirectAttrs.addFlashAttribute("success", messageSource.getMessage("general.label.search.error", args, locale));
            } else {
                redirectAttrs.addFlashAttribute("success", messageSource.getMessage("general.label.save.errorwip1", args, locale));
            }
        }
        return "redirect:/whWip/registerPage";
    }

    @RequestMapping(value = "/updateRegisterToVerify/{requestId}", method = {RequestMethod.GET, RequestMethod.POST})
    public String updateRegisterToVerify(Model model, Locale locale, RedirectAttributes redirectAttrs,
            @ModelAttribute UserSession userSession,
            @PathVariable("requestId") String requestId) {

        String name = userSession.getFullname();
        WhWipDAO daoUpdate = new WhWipDAO();
        daoUpdate.updateVerify(requestId, name);
        WhWipDAO daoSelect = new WhWipDAO();
        WhWip wip = daoSelect.getWhWipByRequestId(requestId);

        args = new String[1];
        args[0] = wip.getRmsEvent();
        redirectAttrs.addFlashAttribute("success", messageSource.getMessage("general.label.update.success1", args, locale));
        return "redirect:/whWip/registerPage";
    }

    @RequestMapping(value = "/listReady", method = RequestMethod.GET)
    public String whReadyList(Model model, @ModelAttribute UserSession userSession) {

        ParameterDetailsDAO pdao = new ParameterDetailsDAO();
        String status = pdao.getDetailByCode(READY);
        WhWipDAO dao = new WhWipDAO();
        List<WhWip> wipList = dao.getWhWipByStatus(status);
        dao = new WhWipDAO();
        int checkReady = dao.getCountByStatus(status);
        String latestShouldBe = getLatestRunningNumber();

        model.addAttribute("wipList", wipList);
        model.addAttribute("checkReady", checkReady);
        model.addAttribute("wipBox", latestShouldBe);
        return "whWip/list_ready";
    }

    @RequestMapping(value = "/updateReadyToShip", method = {RequestMethod.GET, RequestMethod.POST})
    public String updateReadyToShip(Model model, HttpServletRequest request, Locale locale, RedirectAttributes redirectAttrs, @ModelAttribute UserSession userSession,
            @RequestParam(required = false) String shippingList,
            @RequestParam(required = false) String shipDate) throws IOException {

        LOGGER.info("********************** WIP SHIPPED START **********************");
        String name = userSession.getFullname();
        updateRunningNumber(shippingList);
        sendCsvWipShipping(shippingList, shipDate, name);
        sendEmailShipWip();
        LOGGER.info("********************** WIP SHIPPED END **********************");

        return "redirect:/whWip/viewPdf/" + shippingList;
    }

    @RequestMapping(value = "/viewPdf/{shippingList}", method = RequestMethod.GET)
    public String viewWipPdf(
            Model model,
            HttpServletRequest request,
            @PathVariable("shippingList") String shippingList
    ) throws UnsupportedEncodingException {

        String pdfUrl = URLEncoder.encode(request.getContextPath() + "/whWip/viewWhWipPdf/" + shippingList, "UTF-8");
        String backUrl = servletContext.getContextPath() + "/whWip/to";
        String title = "WIP Shipping List [" + shippingList + "]";
        model.addAttribute("pdfUrl", pdfUrl);
        model.addAttribute("backUrl", backUrl);
        model.addAttribute("pageTitle", title);
        return "pdf/view";
    }

    @RequestMapping(value = "/viewWhWipPdf/{shippingList}", method = RequestMethod.GET)
    public ModelAndView viewWhWipPdf(
            Model model,
            @PathVariable("shippingList") String shippingList) {

        return new ModelAndView("wipShipping", "shippingList", shippingList);
    }
    
    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public String logHistory(Model model, @ModelAttribute UserSession userSession) {

        WhWipDAO dao = new WhWipDAO();
        List<WhWipShip> wipData = dao.getLogWhWip();
        model.addAttribute("wipData", wipData);
        return "whWip/list_history";
    }

    // INI SUDAH TIDAK DIGUNAKAN, SBB DA MASUK KE DALAM TO LIST
//    @RequestMapping(value = "/listShip", method = RequestMethod.GET)
//    public String whShipList(Model model, @ModelAttribute UserSession userSession) {
//        ParameterDetailsDAO pdao = new ParameterDetailsDAO();
//        String status = pdao.getDetailByCode(REGISTER);
//        WhWipDAO dao = new WhWipDAO();
//        List<WhWip> wipList = dao.getWhWipByStatus(status);
//        model.addAttribute("wipList", wipList);
//        return "whWip/list_ship";
//    }
    
    @RequestMapping(value = "/query", method = {RequestMethod.GET, RequestMethod.POST})
    public String query(
            Model model,
            Locale locale,
            RedirectAttributes redirectAttrs,
            @ModelAttribute UserSession userSession,
            @RequestParam(required = false) String requestId,
            @RequestParam(required = false) String gtsNo,
            @RequestParam(required = false) String rmsEvent,
            @RequestParam(required = false) String intervals,
            @RequestParam(required = false) String quantity,
            @RequestParam(required = false) String shipmentDate1,
            @RequestParam(required = false) String shipmentDate2,
            @RequestParam(required = false) String receivedDate1,
            @RequestParam(required = false) String receivedDate2,
            @RequestParam(required = false) String shipDate1,
            @RequestParam(required = false) String shipDate2,
            @RequestParam(required = false) String shippingList,
            @RequestParam(required = false) String status) {

        String query = " ";

        if (requestId != null) {
            if (!requestId.equals("")) {
                query += " AND request_id = \'" + requestId + "\' ";
            }
        }
        if (gtsNo != null) {
            if (!gtsNo.equals("")) {
                query += " AND gts_no = \'" + gtsNo + "\' ";
            }
        }
        if (rmsEvent != null) {
            if (!rmsEvent.equals("")) {
                query += " AND rms_event = \'" + rmsEvent + "\' ";
            }
        }
        if (intervals != null) {
            if (!intervals.equals("")) {
                query += " AND intervals = \'" + intervals + "\' ";
            }
        }
        if (quantity != null) {
            if (!quantity.equals("")) {
                query += " AND quantity = \'" + quantity + "\' ";
            }
        }
        if (shipmentDate1 != null && shipmentDate2 != null) {
            if (!shipmentDate1.equals("") && !shipmentDate2.equals("")) {
                query += " AND shipment_date BETWEEN CAST(\'" + shipmentDate1 + "\' AS DATE) AND CAST(\'" + shipmentDate2 + "\' AS DATE) ";
            }
        }
        if (receivedDate1 != null && receivedDate2 != null) {
            if (!receivedDate1.equals("") && !receivedDate2.equals("")) {
                query += " AND receive_date BETWEEN CAST(\'" + receivedDate1 + "\' AS DATE) AND CAST(\'" + receivedDate2 + "\' AS DATE) ";
            }
        }
        if (shipDate1 != null && shipDate2 != null) {
            if (!shipDate1.equals("") && !shipDate2.equals("")) {
                query += " AND ship_date BETWEEN CAST(\'" + shipDate1 + "\' AS DATE) AND CAST(\'" + shipDate2 + "\' AS DATE) ";
            }
        }
        if (shippingList != null) {
            if (!shippingList.equals("")) {
                query += " AND shipping_list LIKE \'%" + shippingList + "%\' ";
            }
        } else {
            LocalDateTime myDateObj = LocalDateTime.now();
            DateTimeFormatter myFormatYear = DateTimeFormatter.ofPattern("yyyy");
            DateTimeFormatter myFormatMonth = DateTimeFormatter.ofPattern("MM");
            String month = myDateObj.format(myFormatMonth);
            String year = myDateObj.format(myFormatYear);
            shippingList = year + month;
            query += " AND shipping_list LIKE \'%" + shippingList + "%\' ";
        }
        if (status != null) {
            if (status.equalsIgnoreCase("All")) {
                query += " AND status LIKE \'%\' ";
            } else {
                if (!status.equals("")) {
                    query += " AND status = \'" + status + "\' ";
                }
            }
        }

        WhWipDAO wipdao = new WhWipDAO();
        List<WhWip> wipList = wipdao.getQuery(query);
        model.addAttribute("wipList", wipList);

        ParameterDetailsDAO pdao = new ParameterDetailsDAO();
        List<ParameterDetails> statusList = pdao.getStatusParameter(STATUSCODE);
        model.addAttribute("statusList", statusList);

        model.addAttribute("shippingList", shippingList);
        model.addAttribute("gtsNo", gtsNo);
        model.addAttribute("rmsEvent", rmsEvent);
        model.addAttribute("intervals", intervals);
        model.addAttribute("quantity", quantity);
        model.addAttribute("shipmentDate1", shipmentDate1);
        model.addAttribute("shipmentDate2", shipmentDate2);
        model.addAttribute("receivedDate1", receivedDate1);
        model.addAttribute("receivedDate2", receivedDate2);
        model.addAttribute("shipDate1", shipDate1);
        model.addAttribute("shipDate2", shipDate2);

        return "whWip/query";
    }

    @RequestMapping(value = "/searchShipping", method = {RequestMethod.GET, RequestMethod.POST})
    public String searchShipping(Model model, @ModelAttribute UserSession userSession, @RequestParam(required = false) String shipList) {

        WhWipDAO dao = new WhWipDAO();
        List<WhWip> wipList = dao.getWipByShipDateAll();
        dao = new WhWipDAO();
        model.addAttribute("shipList", wipList);
        wipList = dao.getWipByShipDate(shipList);
        model.addAttribute("wipList", wipList);
        model.addAttribute("data", shipList);
        return "whWip/search_shipping";
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
            dao = new RunningNumberDAO();
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

    private String sendEmailWipReady() {

        String emailStatus = "";
//        String[] receiver = {"hims@onsemi.com"};
        String[] receiver = {"fg79cj@onsemi.com", "zbqb9x@onsemi.com"};
        EmailSender emailSenderCsv = new EmailSender();
        emailSenderCsv.htmlEmailWithAttachmentTest(
                servletContext,
                "HMS-SG", //user name
                receiver, //to
                "WIP Status is ready for Shipment to HIMS SF", //subject
                "List WIP is ready for shipment." //msg
        );
        return emailStatus;
    }

    private void sendEmailVerifyWip(String gtsNo) {

        String username = System.getProperty("user.name");
        String[] receiver = {"fg79cj@onsemi.com", "zbqb9x@onsemi.com"};
        EmailSender send = new EmailSender();
        String subject = "WIP Received from Rel Lab";
        String msg = tableWipReceive(gtsNo);
        send.wipEmailVerify(servletContext, username, receiver, subject, msg);
        LOGGER.info("SEND RECEIVED WIP EMAIL TO : " + receiver);
    }

    private void sendEmailShipWip() {

        String username = System.getProperty("user.name");
        String[] receiver = {"fg79cj@onsemi.com", "zbqb9x@onsemi.com"};
        EmailSender send = new EmailSender();
        String subject = "WIP is Shipped to Rel Lab SF";
        String msg = "WIP is shipped to Rel Lab from Sg Gadut";
        send.wipEmailShip(servletContext, username, receiver, subject, msg);
        LOGGER.info("SEND SHIPPED WIP EMAIL TO : " + receiver);
    }

    private void sendCsvForVerify(String gtsNo) {

//        WhWipDAO dao = new WhWipDAO();
        WhWipDAO daoGet = new WhWipDAO();
        ParameterDetailsDAO pdao = new ParameterDetailsDAO();
        File file = new File(FILEPATHVERIFY);

//        String username = System.getProperty("user.name");
        String statusVerify = pdao.getDetailByCode(VERIFY);
        List<WhWip> dataList = daoGet.getWipByGtsNo(gtsNo);

        for (int i = 0; i < dataList.size(); i++) {
            WhWip wip = new WhWip();
            wip.setId(dataList.get(i).getId());
            wip.setRequestId(dataList.get(i).getRequestId());
            wip.setReceiveDate(dataList.get(i).getReceiveDate());
            wip.setVerifyDate(dataList.get(i).getVerifyDate());
            wip.setStatus(statusVerify);

//            dao = new WhWipDAO();
//            dao.updateShip(wip);
            FileWriter fileWriter = null;

            if (file.exists()) {
                try {
                    fileWriter = new FileWriter(FILEPATHVERIFY, true);

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
                    fileWriter = new FileWriter(FILEPATHVERIFY, true);
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

    private void sendCsvWipShipping(String shippingList, String shipDate, String username) {

        File file = new File(FILEPATHSHIP);
//        String username = System.getProperty("user.name");

        ParameterDetailsDAO pdao = new ParameterDetailsDAO();
        String statusReady = pdao.getDetailByCode(READY);
        pdao = new ParameterDetailsDAO();
        String statusShip = pdao.getDetailByCode(SHIP);
        WhWipDAO dao = new WhWipDAO();
        List<WhWip> dataList = dao.getWhWipByStatus(statusReady);

        for (int i = 0; i < dataList.size(); i++) {
            WhWip wip = new WhWip();
            wip.setId(dataList.get(i).getId());
            wip.setRequestId(dataList.get(i).getRequestId());
            wip.setRmsEvent(dataList.get(i).getRmsEvent());
            wip.setShipBy(username);
            wip.setShipDate(shipDate);
            wip.setStatus(statusShip);
            wip.setIntervals(dataList.get(i).getIntervals());
            wip.setQuantity(dataList.get(i).getQuantity());
            wip.setShippingList(shippingList);

            dao = new WhWipDAO();
            dao.updateShip(wip);

            dao = new WhWipDAO();
            WhWipShip wip2 = new WhWipShip();
            wip2.setWipId(dataList.get(i).getId());
            wip2.setWipShipList(shippingList);
            dao.insertWhWipShip(wip2);

            dao = new WhWipDAO();
            shipDate = dao.getShipDateByShipList(shippingList);
            FileWriter fileWriter = null;

            if (file.exists()) {
                try {
                    fileWriter = new FileWriter(FILEPATHSHIP, true);

                    //New Line after the header
                    fileWriter.append(LINE_SEPARATOR);
                    fileWriter.append(dataList.get(i).getRequestId());
                    fileWriter.append(COMMA_DELIMITER);
                    fileWriter.append(dataList.get(i).getRmsEvent());
                    fileWriter.append(COMMA_DELIMITER);
                    fileWriter.append(dataList.get(i).getIntervals());
                    fileWriter.append(COMMA_DELIMITER);
                    fileWriter.append(dataList.get(i).getQuantity());
                    fileWriter.append(COMMA_DELIMITER);
                    fileWriter.append(shipDate);
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
                    fileWriter = new FileWriter(FILEPATHSHIP, true);
                    //Adding the header
                    fileWriter.append(HEADERSHIP);

                    //New Line after the header
                    fileWriter.append(LINE_SEPARATOR);
                    fileWriter.append(dataList.get(i).getRequestId());
                    fileWriter.append(COMMA_DELIMITER);
                    fileWriter.append(dataList.get(i).getRmsEvent());
                    fileWriter.append(COMMA_DELIMITER);
                    fileWriter.append(dataList.get(i).getIntervals());
                    fileWriter.append(COMMA_DELIMITER);
                    fileWriter.append(dataList.get(i).getQuantity());
                    fileWriter.append(COMMA_DELIMITER);
                    fileWriter.append(shipDate);
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

    private String tableWipReceive(String gtsNo) {
        WhWipDAO wipdao = new WhWipDAO();
        List<WhWip> listWip = wipdao.getWipByGtsNo(gtsNo);

        String requestId = "";
        String receiveDate = "";
        String verifyDate = "";
        String rmsEvent = "";
        String intervals = "";
        String quantity = "";
        String text = "<table style=\"width: 90%; border: 1px solid #A4A4A4;\"><tr>"
                + "<th><span>No</span></th>"
                + "<th><span>Request ID</span></th><th><span>RMS Event</span></th>"
                + "<th><span>Intervals</span></th>"
                + "<th><span>Quantity</span></th>"
                + "<th><span>Receive Date</span></th>"
                + "<th><span>Verify Date</span></th>"
                + "</tr>";

        for (int i = 0; i < listWip.size(); i++) {
            requestId = listWip.get(i).getRequestId();
            rmsEvent = listWip.get(i).getRmsEvent();
            intervals = listWip.get(i).getIntervals();
            quantity = listWip.get(i).getQuantity();
            receiveDate = listWip.get(i).getReceiveDate();
            verifyDate = listWip.get(i).getVerifyDate();
            int index = i + 1;
            text = text + "<tr align = \"center\">";
            text = text + "<td>" + index + "</td>";
            text = text + "<td>" + requestId + "</td>";
            text = text + "<td>" + rmsEvent + "</td>";
            text = text + "<td>" + intervals + "</td>";
            text = text + "<td>" + quantity + "</td>";
            text = text + "<td>" + receiveDate + "</td>";
            text = text + "<td>" + verifyDate + "</td>";
            text = text + "</tr>";
        }
        text = text + "</table>";
        return text;
    }

    private String tableWipShip(String shipList) {
        String text = "";
        return text;
    }

}
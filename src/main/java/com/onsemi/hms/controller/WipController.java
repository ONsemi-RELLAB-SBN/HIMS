/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.onsemi.hms.controller;

import com.onsemi.hms.config.FtpWip;
import com.onsemi.hms.dao.ParameterDetailsDAO;
import com.onsemi.hms.dao.RunningNumberDAO;
import com.onsemi.hms.dao.WhWipDAO;
import com.onsemi.hms.model.UserSession;
import com.onsemi.hms.model.WhWip;
import com.onsemi.hms.tools.EmailSender;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author zbqb9x
 */
@Controller
@RequestMapping(value = "/whWip")
@SessionAttributes({"userSession"})
public class WipController {
    
    private static final String UPLOADED_FOLDER = "E:\\HIMS_Upload\\";
    private static final Logger LOGGER = LoggerFactory.getLogger(WipController.class);
    String[] args = {};
    
    private static final String NEW = "0101";
    private static final String RECEIVE = "0102";
    private static final String VERIFY = "0103";
    private static final String REGISTER = "0104";
    private static final String READY = "0105";
    private static final String SHIP = "0106";

    @Autowired
    private MessageSource messageSource;
    
    @Autowired
    ServletContext servletContext;
    
//    @RequestMapping(value = "wipList", method = RequestMethod.GET)
    @RequestMapping(value = "/sync", method = {RequestMethod.GET, RequestMethod.POST})
    public String wipList(Model model) {
        FtpWip wip = new FtpWip();
        wip.cronRun();
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
        return "whWip/to_list";
    }
    
    @RequestMapping(value = "/listNew", method = RequestMethod.GET)
    public String whNewList(Model model, @ModelAttribute UserSession userSession) {
        ParameterDetailsDAO pdao = new ParameterDetailsDAO();
        String status = pdao.getDetailByCode(NEW+ "','"+ RECEIVE);
        WhWipDAO dao = new WhWipDAO();
        List<WhWip> wipList = dao.getWhWipByStatus(status);
        model.addAttribute("wipList", wipList);
        return "whWip/list_new";
    }
    
    @RequestMapping(value = "/listReceive", method = RequestMethod.GET)
    public String whReceiveList(Model model, @ModelAttribute UserSession userSession) {
        ParameterDetailsDAO pdao = new ParameterDetailsDAO();
        String status = pdao.getDetailByCode(NEW);
        // WE CAN ONLY SHOWN THE STATUS - NEW SHIPMENT ONLY, ALL THE RECEIVE STATUS ALREADY MOVED OUT
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
        WhWipDAO daoUpdate = new WhWipDAO();
        daoUpdate.updateStatusByGts(columnDate, columnBy, gtsNo, flag);
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
        daoUpdate.updateVerify(requestId);
//        return "redirect:/whWip/listRegister";
        return "redirect:/whWip/listNew";
    }
    
//    @RequestMapping(value = "/listRegister", method = RequestMethod.GET)
//    public String whRegisterList(Model model, @ModelAttribute UserSession userSession) {
//        ParameterDetailsDAO pdao = new ParameterDetailsDAO();
//        String status = pdao.getDetailByCode(VERIFY);
//        WhWipDAO dao = new WhWipDAO();
//        List<WhWip> wipList = dao.getWhWipByStatus(status);
//        model.addAttribute("wipList", wipList);
//        return "whWip/list_register";
//    }
    
    @RequestMapping(value = "/registerPage", method = RequestMethod.GET)
    public String whRegisterPage(Model model, @ModelAttribute UserSession userSession) {
        LOGGER.info("MASUK PAGE REGISTER WIP");
        WhWipDAO dao = new WhWipDAO();
        ParameterDetailsDAO pdao = new ParameterDetailsDAO();
        String status = pdao.getDetailByCode(READY);
        List<WhWip> packingList = dao.getWhWipByStatus(status);
        model.addAttribute("packingList", packingList);
        return "whWip/register_page";
    }
    
    @RequestMapping(value = "/updateVerifyToRegister", method  = RequestMethod.POST)
    public String updateRegister(Model model, Locale locale, RedirectAttributes redirectAttrs, 
            @ModelAttribute UserSession userSession, 
            @RequestParam(required = false) String tripTicket, 
            @RequestParam(required = false) String intervals) {
        LOGGER.info("LOGGER for GET REQUEST ID : " +tripTicket);
        
        WhWipDAO daoSelect = new WhWipDAO();
        WhWip daoData = daoSelect.getWipByRmsInterval(tripTicket, intervals);
        String status = daoData.getStatus();
        LOGGER.info("MASUK KE FUNCTION NK UPDATE THE STATUS");
        ParameterDetailsDAO pdao = new ParameterDetailsDAO();
        String checkStatus = pdao.getDetailByCode(VERIFY);
        
        LOGGER.info("LOGGER for chek : " +checkStatus);
        LOGGER.info("LOGGER for stat : " +status);
        args = new String[1];
        args[0] = tripTicket + " [" + intervals + "]";
        
//        if (status.equalsIgnoreCase(checkStatus)) {
        if (status == null ? checkStatus == null : status.equals(checkStatus)) {
            WhWipDAO daoUpdate = new WhWipDAO();
            sendEmailWipReady();
            daoUpdate.updateRegister(daoData);
            redirectAttrs.addFlashAttribute("success", messageSource.getMessage("general.label.save.successwip1", args, locale));
        } else {
            if (daoData.getStatus() == null) {
                LOGGER.info("SINI TAKDE LANGSUNG DATA JUMPA " );
                redirectAttrs.addFlashAttribute("success", messageSource.getMessage("general.label.search.error", args, locale));
            } else {
                LOGGER.info("SINI ADA DATA, TAPI BUKAN STATUS DIA >>> " + daoData);
                redirectAttrs.addFlashAttribute("success", messageSource.getMessage("general.label.save.errorwip1", args, locale));
            }
        }
        return "redirect:/whWip/registerPage";
    }
    
    private String sendEmailWipReady() {
        String emailStatus = "";
        
//        String[] receiver = {"hims@onsemi.com"};
        String[] receiver = {"zbqb9x@onsemi.com"};
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
    
    @RequestMapping(value = "/listReady", method = RequestMethod.GET)
    public String whReadyList(Model model, @ModelAttribute UserSession userSession) {
        LOGGER.info("LOGGER for MASUK DATA LIST READY : " );
        ParameterDetailsDAO pdao = new ParameterDetailsDAO();
        String status = pdao.getDetailByCode(READY);
        WhWipDAO dao = new WhWipDAO(); 
        List<WhWip> wipList = dao.getWhWipByStatus(status);
        dao = new WhWipDAO();
        int checkReady = dao.getCountByStatus(status);
        
        String latestShouldBe = getLatestRunningNumber();
        LOGGER.info("LOGGER for DATA RUNNING NUMBER YANG KITA AKAN GUNA : " +latestShouldBe);
        LOGGER.info("LOGGER for ready number : " +checkReady);
        LOGGER.info("LOGGER for ready status : " +status);
        model.addAttribute("wipList", wipList);
        model.addAttribute("checkReady", checkReady);
        model.addAttribute("wipBox", latestShouldBe);
        LOGGER.info("MASUK KE REGISTER READY");
        return "whWip/list_ready";
    }
    
    @RequestMapping(value = "/updateReadyToShip", method = {RequestMethod.GET, RequestMethod.POST})
    public String updateReadyToShip(Model model, HttpServletRequest request, Locale locale, RedirectAttributes redirectAttrs, @ModelAttribute UserSession userSession) throws IOException {
        
//        String[] receiver = {"hims@onsemi.com"};
//        EmailSender emailSenderCsv = new EmailSender();
//        emailSenderCsv.htmlEmailWithAttachmentTest(
//                servletContext,
//                "CDARS", //user name
//                receiver, //to
//                "Status for Hardware Shipping from HIMS SF", //subject
//                "Verification and status for Hardware Shipping has been made." //msg
//        );
        
        ParameterDetailsDAO pdao = new ParameterDetailsDAO();
        String statusReady =  pdao.getDetailByCode(READY);
        WhWipDAO dao = new WhWipDAO();
        List<WhWip> dataList = dao.getWhWipByStatus(statusReady);
        
        return "";
    }
    
    
    // INI SUDAH TIDAK DIGUNAKAN, SBB DA MASUK KE DALAM TO LIST
//    @RequestMapping(value = "/listShip", method = RequestMethod.GET)
//    public String whShipList(Model model, @ModelAttribute UserSession userSession) {
//        LOGGER.info("LOGGER for MASUK DATA LIST REGISTER : " );
//        ParameterDetailsDAO pdao = new ParameterDetailsDAO();
//        String status = pdao.getDetailByCode(REGISTER);
//        WhWipDAO dao = new WhWipDAO();
//        List<WhWip> wipList = dao.getWhWipByStatus(status);
//        LOGGER.info("LOGGER for xxx : " +status);
//        model.addAttribute("wipList", wipList);
//        LOGGER.info("MASUK KE REGISTER LIST");
//        return "whWip/list_ship";
//    }
    
    private String getLatestRunningNumber() {
        
        String runningNumber = "0001";
        LocalDateTime myDateObj = LocalDateTime.now();  
        DateTimeFormatter myFormatYear = DateTimeFormatter.ofPattern("yyyy"); 
        DateTimeFormatter myFormatMonth = DateTimeFormatter.ofPattern("MM"); 
        String month = myDateObj.format(myFormatMonth);  
        String year = myDateObj.format(myFormatYear); 
        
        LOGGER.info("LOGGER for TAHUN : " +year);
        LOGGER.info("LOGGER for BULAN : " +month);
        
        RunningNumberDAO dao = new RunningNumberDAO();
        String checkLatest = dao.getLatestRunning(year, month);
        
        if (checkLatest.equals("0")) {
            // THIS MEAN NEW MONTH, SO NEED TO CREATE NEW RUNNING NUMBER RECORD
            LOGGER.info("LOGGER for CREATE NEW RUNNING NUMBER : " +checkLatest);
            dao = new RunningNumberDAO();
            dao.insertRunningNumber(year, month, runningNumber);
        } else if (checkLatest.equals("")) {
            LOGGER.info("LOGGER for CREATE BARU JUGA : " +checkLatest);
            dao = new RunningNumberDAO();
            dao.insertRunningNumber(year, month, runningNumber);
        } else {
            // THE RUNNING NUMBER FOR THE MONTH ALREADY EXIST, JUST UPDATE THE RUNNING NUMBER
            LOGGER.info("LOGGER for UPDATE RUNNNING NUMBER : " +checkLatest);
            dao = new RunningNumberDAO();
            runningNumber = String.format("%04d", Integer.parseInt(checkLatest));
            LOGGER.info("LOGGER for LSETEST RUNNING NUMBER : " +runningNumber);
//            dao.updateRunningNumber(runningNumber, year, month);
        }
        runningNumber = year + month + runningNumber;
        
        return runningNumber;
    }
    
}
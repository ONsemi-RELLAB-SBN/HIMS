/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.onsemi.hms.controller;

import com.onsemi.hms.config.FtpWip;
import com.onsemi.hms.dao.ParameterDetailsDAO;
import com.onsemi.hms.dao.WhWipDAO;
import com.onsemi.hms.model.UserSession;
import com.onsemi.hms.model.WhWip;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
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
            @RequestParam(required = false) String boxNo
    ) throws IOException {
        String columnDate = "receive_date";
        String columnBy = "receive_by";
        String gtsNo = boxNo;
        String flag = "receive";
        
        WhWipDAO daoUpdate = new WhWipDAO();
        daoUpdate.updateStatus(columnDate, columnBy, gtsNo, flag);
        return "redirect:/whWip/listNew";
    }
    
    @RequestMapping(value = "/listVerify/{requestId}", method = RequestMethod.GET)
    public String whVerifyList(Model model, @ModelAttribute UserSession userSession, @PathVariable("requestId") String requestId) {
        LOGGER.info("LOGGER for MASUK DATA LIST VERIFY : " +requestId);
        WhWipDAO dao = new WhWipDAO();
        WhWip data = dao.getWhWipByRequestId(requestId);
        LOGGER.info("LOGGER for xxx : " +data);
        LOGGER.info("LOGGER for xxx : " +data.getRequestId());
        model.addAttribute("wipData", data);
//        ParameterDetailsDAO pdao = new ParameterDetailsDAO();
//        String status = pdao.getDetailByCode(VERIFY);
//        WhWipDAO dao = new WhWipDAO();
//        List<WhWip> wipList = dao.getWhWipByStatus(status);
//        LOGGER.info("LOGGER for xxx : " +status);
//        model.addAttribute("wipList", wipList);
        LOGGER.info("MASUK KE VERIFY LIST");
        return "whWip/list_verify";
    }
    
    @RequestMapping(value = "/listRegister", method = RequestMethod.GET)
    public String whRegisterList(Model model, @ModelAttribute UserSession userSession) {
        LOGGER.info("LOGGER for MASUK DATA LIST REGISTER : " );
        ParameterDetailsDAO pdao = new ParameterDetailsDAO();
        String status = pdao.getDetailByCode(REGISTER);
        WhWipDAO dao = new WhWipDAO();
        List<WhWip> wipList = dao.getWhWipByStatus(status);
        LOGGER.info("LOGGER for xxx : " +status);
        model.addAttribute("wipList", wipList);
        LOGGER.info("MASUK KE REGISTER LIST");
        return "whWip/list_register";
    }
    
    @RequestMapping(value = "/listReady", method = RequestMethod.GET)
    public String whReadyList(Model model, @ModelAttribute UserSession userSession) {
        LOGGER.info("LOGGER for MASUK DATA LIST READY : " );
        ParameterDetailsDAO pdao = new ParameterDetailsDAO();
        String status = pdao.getDetailByCode(REGISTER);
        WhWipDAO dao = new WhWipDAO();
        List<WhWip> wipList = dao.getWhWipByStatus(status);
        LOGGER.info("LOGGER for ready status : " +status);
        model.addAttribute("wipList", wipList);
        LOGGER.info("MASUK KE REGISTER READY");
        return "whWip/list_ready";
    }
    
    @RequestMapping(value = "/listShip", method = RequestMethod.GET)
    public String whShipList(Model model, @ModelAttribute UserSession userSession) {
        LOGGER.info("LOGGER for MASUK DATA LIST REGISTER : " );
        ParameterDetailsDAO pdao = new ParameterDetailsDAO();
        String status = pdao.getDetailByCode(REGISTER);
        WhWipDAO dao = new WhWipDAO();
        List<WhWip> wipList = dao.getWhWipByStatus(status);
        LOGGER.info("LOGGER for xxx : " +status);
        model.addAttribute("wipList", wipList);
        LOGGER.info("MASUK KE REGISTER LIST");
        return "whWip/list_ship";
    }
    
}
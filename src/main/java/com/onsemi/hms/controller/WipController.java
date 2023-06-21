/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.onsemi.hms.controller;

import com.onsemi.hms.config.FtpWip;
import com.onsemi.hms.dao.ParameterDetailsDAO;
import com.onsemi.hms.dao.WhRequestDAO;
import com.onsemi.hms.dao.WhWipDAO;
import com.onsemi.hms.model.UserSession;
import com.onsemi.hms.model.WhRequest;
import com.onsemi.hms.model.WhWip;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

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
    @RequestMapping(value = "/wipList", method = {RequestMethod.GET, RequestMethod.POST})
    public String wipList(Model model) {
        LOGGER.info("TO INTO WIP LIST FUNCTION");
        FtpWip wip = new FtpWip();
        wip.cronRun();
        LOGGER.info("THIS FUNCTION AFTER READ THE CSV FILES");
        return "whWip/list";
    }
    
    @RequestMapping(value = "/from", method = RequestMethod.GET)
    public String whFromList(Model model, @ModelAttribute UserSession userSession) {
        ParameterDetailsDAO pdao = new ParameterDetailsDAO();
        String status = pdao.getDetailByCode(NEW);
        WhWipDAO dao = new WhWipDAO();
        List<WhWip> wipList = dao.getWhWipByStatus(status);
        model.addAttribute("wipList", wipList);
        LOGGER.info("MASUK KE FROM LIST");
        return "whWip/from_list";
    }
    
    @RequestMapping(value = "/to", method = RequestMethod.GET)
    public String whToList(Model model, @ModelAttribute UserSession userSession) {
        LOGGER.info("MASUK KE TO LIST");
        return "whWip/to_list";
    }
    
    @RequestMapping(value = "/listNew", method = RequestMethod.GET)
    public String whNewList(Model model, @ModelAttribute UserSession userSession) {
        LOGGER.info("LOGGER for MASUK DATA LIST NEW : " );
        ParameterDetailsDAO pdao = new ParameterDetailsDAO();
        String status = pdao.getDetailByCode(NEW);
        WhWipDAO dao = new WhWipDAO();
        List<WhWip> wipList = dao.getWhWipByStatus(status);
        LOGGER.info("LOGGER for xxx : " +status);
        model.addAttribute("wipList", wipList);
        LOGGER.info("MASUK KE FROM LIST");
        return "whWip/list_new";
    }
    
}
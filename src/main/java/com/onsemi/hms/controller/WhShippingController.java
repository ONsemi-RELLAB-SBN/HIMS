package com.onsemi.hms.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import com.onsemi.hms.dao.WhShippingDAO;
import com.onsemi.hms.model.WhShipping;
import com.onsemi.hms.model.WhShippingLog;
import javax.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/wh/whShipping")
@SessionAttributes({"userSession"})
public class WhShippingController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WhShippingController.class);
    String[] args = {};
    
    @Autowired
    private MessageSource messageSource;

    @Autowired
    ServletContext servletContext;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String whShipping(
            Model model
    ) {
        WhShippingDAO whShippingDAO = new WhShippingDAO();
        List<WhShipping> whShippingList = whShippingDAO.getWhShippingListMergeRequest();
        model.addAttribute("whShippingList", whShippingList);
        return "whShipping/whShipping";
    }

    @RequestMapping(value = "/edit/{whShippingId}", method = RequestMethod.GET)
    public String edit(
            Model model,
            @PathVariable("whShippingId") String whShippingId
    ) {
        WhShippingDAO whShippingDAO = new WhShippingDAO();
        WhShipping whShipping = whShippingDAO.getWhShippingMergeWithRequest(whShippingId);
        LOGGER.info("whShipping.getEquipmentType() : " + whShipping.getEquipmentType());
        
        String type = whShipping.getEquipmentType();
        if ("Motherboard".equals(type)) {
            String IdLabel = "Motherboard ID";
            model.addAttribute("IdLabel", IdLabel);
        } else if ("Stencil".equals(type)) {
            String IdLabel = "Stencil ID";
            model.addAttribute("IdLabel", IdLabel);
        } else if ("Tray".equals(type)) {
            String IdLabel = "Tray Type";
            model.addAttribute("IdLabel", IdLabel);
        } else if ("PCB".equals(type)) {
            String IdLabel = "PCB Name";
            model.addAttribute("IdLabel", IdLabel);
        } else {
            String IdLabel = "Hardware ID";
            model.addAttribute("IdLabel", IdLabel);
        }
        model.addAttribute("whShipping", whShipping);
        return "whShipping/edit";
    }
    
    @RequestMapping(value = "/view/{whShippingId}", method = RequestMethod.GET)
    public String view(
            Model model,
            HttpServletRequest request,
            @PathVariable("whShippingId") String whShippingId
    ) throws UnsupportedEncodingException {
        LOGGER.info("Masuk view 1........");
        String pdfUrl = URLEncoder.encode(request.getContextPath() + "/wh/whShipping/viewWhShippingPdf/" + whShippingId, "UTF-8");
        String backUrl = servletContext.getContextPath() + "/wh/whShipping";
        model.addAttribute("pdfUrl", pdfUrl);
        model.addAttribute("backUrl", backUrl);
        model.addAttribute("pageTitle", "Warehouse Management - Hardware Shipping");
        LOGGER.info("Masuk view 2........");
        return "pdf/viewer";
    }

    @RequestMapping(value = "/viewWhShippingPdf/{whShippingId}", method = RequestMethod.GET)
    public ModelAndView viewWhShippingPdf(
            Model model,
            @PathVariable("whShippingId") String whShippingId
    ) {
        WhShippingDAO whShippingDAO = new WhShippingDAO();
        LOGGER.info("Masuk 1........");
        WhShipping whShipping = whShippingDAO.getWhShippingMergeWithRequest(whShippingId);
        LOGGER.info("Masuk 2........");
        return new ModelAndView("whShippingPdf", "whShipping", whShipping);
    }
    
    @RequestMapping(value = "/history/{whShippingId}", method = RequestMethod.GET)
    public String history(
            Model model,
            HttpServletRequest request,
            @PathVariable("whShippingId") String whShippingId
    ) throws UnsupportedEncodingException {
        LOGGER.info("Masuk view 1........");        
        String pdfUrl = URLEncoder.encode(request.getContextPath() + "/wh/whShipping/viewWhShippingLogPdf/" + whShippingId, "UTF-8");
        String backUrl = servletContext.getContextPath() + "/wh/whShipping";
        model.addAttribute("pdfUrl", pdfUrl);
        model.addAttribute("backUrl", backUrl);
        model.addAttribute("pageTitle", "Warehouse Management - Hardware Shipping History");
        LOGGER.info("Masuk view 2........");
        return "pdf/viewer";
    }
    
    @RequestMapping(value = "/viewWhShippingLogPdf/{whShippingId}", method = RequestMethod.GET)
    public ModelAndView viewWhShippingHistPdf(
            Model model,
            @PathVariable("whShippingId") String whShippingId
    ) {
        WhShippingDAO whShippingDAO = new WhShippingDAO();        
        LOGGER.info("Masuk 1........");
        List<WhShippingLog> whHistoryList = whShippingDAO.getWhShippingReqLog(whShippingId);
        LOGGER.info("Masuk 2........");
        return new ModelAndView("whShippingLogPdf", "whShippingLog", whHistoryList);
    }
}
package com.onsemi.hms.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import com.onsemi.hms.dao.WhShippingDAO;
import com.onsemi.hms.model.UserSession;
import com.onsemi.hms.model.WhShipping;
import com.onsemi.hms.model.WhShippingLog;
import java.util.Locale;
import javax.servlet.ServletContext;
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
        model.addAttribute("pageTitle", "Hardware Shipping");
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
        model.addAttribute("pageTitle", "Hardware Shipping History");
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
    
    @RequestMapping(value = "/query", method = {RequestMethod.GET, RequestMethod.POST})
    public String query(
            Model model,
            Locale locale,
            RedirectAttributes redirectAttrs,
            @ModelAttribute UserSession userSession,
            @RequestParam(required = false) String materialPassNo,
            @RequestParam(required = false) String equipmentId,
            @RequestParam(required = false) String materialPassExpiry1,
            @RequestParam(required = false) String materialPassExpiry2,
            @RequestParam(required = false) String equipmentType,
            @RequestParam(required = false) String requestedDate1,
            @RequestParam(required = false) String requestedDate2,
            @RequestParam(required = false) String requestedBy,
            @RequestParam(required = false) String inventoryRack,
            @RequestParam(required = false) String inventoryShelf,
            @RequestParam(required = false) String status
    ) {
        String query = "";
        int count = 0;
        
        if(materialPassNo!=null) {
            if(!materialPassNo.equals("")) {
                count++;
                if(count == 1)
                    query = " material_pass_no = \'" + materialPassNo + "\' ";
                else if(count>1)
                    query = query + " AND material_pass_no = \'" + materialPassNo + "\' ";
            }
        }
        if(equipmentId!=null) {
            if(!equipmentId.equals("")) {
                count++;
                if(count == 1)
                    query = " equipment_id = \'" + equipmentId + "\' ";
                else if(count>1)
                    query = query + " AND equipment_id = \'" + equipmentId + "\' ";
            }
        }
        if(materialPassExpiry1!=null &&  materialPassExpiry2!=null) {
            if(!materialPassExpiry1.equals("") && !materialPassExpiry2.equals("")) {
                count++;
                String materialPassExpiry = " material_pass_expiry BETWEEN CAST(\'" + materialPassExpiry1 + "\' AS DATE) AND CAST(\'" + materialPassExpiry2 +"\' AS DATE) ";
                if(count == 1)
                    query = materialPassExpiry;
                else if(count>1)
                    query = query + " AND " + materialPassExpiry;
            }
        }
        if(equipmentType!=null) {
//            if(!equipmentType.equals("") !("").equals(equipmentType)) {
              if(!("").equals(equipmentType)) {
                count++;
                if(count == 1)
                    query = " equipment_type = \'" + equipmentType + "\' ";
                else if(count>1)
                    query = query + " AND equipment_type = \'" + equipmentType + "\' ";
            }
        }
        if(requestedDate1!=null &&  requestedDate2!=null) {
            if(!requestedDate1.equals("") && !requestedDate2.equals("")) {
                count++;
                String requestedDate = " requested_date BETWEEN CAST(\'" + requestedDate1 + "\' AS DATE) AND CAST(\'" + requestedDate2 +"\' AS DATE) ";
                if(count == 1)
                    query = requestedDate;
                else if(count>1)
                    query = query + " AND " + requestedDate;
            }
        }
        if(requestedBy!=null) {
            if(!requestedBy.equals("")) {
                count++;
                if(count == 1)
                    query = " requested_by = \'" + requestedBy + "\' ";
                else if(count>1)
                    query = query + " AND requested_by = \'" + requestedBy + "\' ";
            }
        }
        if(inventoryRack!=null) {
            if(!inventoryRack.equals("")) {
                count++;
                if(count == 1)
                    query = " inventory_rack = \'" + inventoryRack + "\' ";
                else if(count>1)
                    query = query + " AND inventory_rack = \'" + inventoryRack + "\' ";
            }
        }
        if(inventoryShelf!=null) {
            if(!inventoryShelf.equals("")) {
                count++;
                if(count == 1)
                    query = " inventory_shelf = \'" + inventoryShelf + "\' ";
                else if(count>1)
                    query = query + " AND inventory_shelf = \'" + inventoryShelf + "\' ";
            }
        }
        if(status!=null) {
            if(!("").equals(status)) {
                count++;
                if(count == 1) {
                    if(status.equals("Closed")) {
                        query = " (S.status = \'" + status + "\' OR S.status = \'Closed. Verified By Supervisor\') ";
                    } else {
                        query = " S.status = \'" + status + "\' ";
                    }
                }
                else if(count>1) {
                    if(status.equals("Closed")) {
                        query = query + " AND (S.status = \'" + status + "\' OR S.status = \'Closed. Verified By Supervisor\' ) ";
                    } else {
                        query = query + " AND S.status = \'" + status + "\' ";
                    }
                }
            }
        }
        
        System.out.println("Query: " + query);
        WhShippingDAO wh = new WhShippingDAO();
        List<WhShipping> shippingQueryList = wh.getQuery(query);
        
        model.addAttribute("shippingQueryList", shippingQueryList);
        return "whShipping/query";
    }
}
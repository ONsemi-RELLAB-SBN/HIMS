package com.onsemi.hms.controller;

import com.onsemi.hms.dao.LogModuleDAO;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import com.onsemi.hms.dao.WhRequestDAO;
import com.onsemi.hms.dao.WhShippingDAO;
import com.onsemi.hms.model.LogModule;
import com.onsemi.hms.model.WhRequest;
import com.onsemi.hms.model.UserSession;
import com.onsemi.hms.model.WhRequestLog;
import com.onsemi.hms.model.WhShipping;
import com.onsemi.hms.tools.EmailSender;
import com.onsemi.hms.tools.QueryResult;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
@RequestMapping(value = "/wh/whRequest")
@SessionAttributes({"userSession"})
public class WhRequestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WhRequestController.class);
    String[] args = {};

    //Delimiters which has to be in the CSV file
    private static final String COMMA_DELIMITER = ",";
    private static final String LINE_SEPARATOR = "\n";

    //File header
    private static final String HEADER = "id,material_pass_no,equipment_type,equipment_id,quantity,requested_by,requested_date,remarks,date_verify,inventory_date,inventory_rack,inventory_slot,inventory_by,status";

    @Autowired
    private MessageSource messageSource;

    @Autowired
    ServletContext servletContext;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String whRequest(
            Model model, @ModelAttribute UserSession userSession
    ) {
        WhRequestDAO whRequestDAO = new WhRequestDAO();
        List<WhRequest> whRequestList = whRequestDAO.getWhRequestList();
        String groupId = userSession.getGroup();

        model.addAttribute("whRequestList", whRequestList);
        model.addAttribute("groupId", groupId);

        return "whRequest/whRequest";
    }

    @RequestMapping(value = "/view/{whRequestId}", method = RequestMethod.GET)
    public String view(
            Model model,
            HttpServletRequest request,
            @PathVariable("whRequestId") String whRequestId
    ) throws UnsupportedEncodingException {
        String pdfUrl = URLEncoder.encode(request.getContextPath() + "/wh/whRequest/viewWhRequestPdf/" + whRequestId, "UTF-8");
        String backUrl = servletContext.getContextPath() + "/wh/whRequest";
        model.addAttribute("pdfUrl", pdfUrl);
        model.addAttribute("backUrl", backUrl);
        model.addAttribute("pageTitle", "Warehouse Management - Hardware Request");
        return "pdf/viewer";
    }

    @RequestMapping(value = "/viewWhRequestPdf/{whRequestId}", method = RequestMethod.GET)
    public ModelAndView viewWhRequestPdf(
            Model model,
            @PathVariable("whRequestId") String whRequestId
    ) {
        WhRequestDAO whRequestDAO = new WhRequestDAO();
        WhRequest whRequest = whRequestDAO.getWhRequest(whRequestId);
        return new ModelAndView("whRequestPdf", "whRequest", whRequest);
    }

    
    @RequestMapping(value = "/verify/{whRequestId}", method = RequestMethod.GET)
    public String verify(
            Model model,
            @PathVariable("whRequestId") String whRequestId
    ) {
        WhRequestDAO whRequestDAO = new WhRequestDAO();
        WhRequest whRequest = whRequestDAO.getWhRequest(whRequestId);
        
        String type = whRequest.getEquipmentType();
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
        //for check which tab should active
        if (whRequest.getStatus().equals("New Shipping Request") || whRequest.getStatus().equals("Barcode Verification Fail")) {
            String mpActive = "active";
            String mpActiveTab = "in active";
            model.addAttribute("mpActive", mpActive);
            model.addAttribute("mpActiveTab", mpActiveTab);
        } else {
            String mpActive = "";
            String mpActiveTab = "";
            model.addAttribute("mpActive", mpActive);
            model.addAttribute("mpActiveTab", mpActiveTab);
        }
        if (whRequest.getStatus().equals("Barcode Verification Pass") || whRequest.getStatus().equals("Wrong Inventory")) {
            String hiActive = "active";
            String hiActiveTab = "in active";
            model.addAttribute("hiActive", hiActive);
            model.addAttribute("hiActiveTab", hiActiveTab);
        } else {
            String hiActive = "";
            String hiActiveTab = "";
            model.addAttribute("hiActive", hiActive);
            model.addAttribute("hiActiveTab", hiActiveTab);
        }
        model.addAttribute("whRequest", whRequest);
        return "whRequest/verify";
    }
    
    @RequestMapping(value = "/verifyMp", method = RequestMethod.POST)
    public String verifyMp(
            Model model,
            Locale locale,
            HttpServletRequest request,
            RedirectAttributes redirectAttrs,
            @ModelAttribute UserSession userSession,
            @RequestParam(required = false) String refId,
            @RequestParam(required = false) String materialPassNo,
            @RequestParam(required = false) String barcodeVerify,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) int tempCount
    ) {
        WhRequest whRequest = new WhRequest();
        whRequest.setRefId(refId);
        whRequest.setBarcodeVerify(barcodeVerify);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        whRequest.setDateVerify(dateFormat.format(date));
        whRequest.setUserVerify(userSession.getFullname());
        String barcodeVerified = whRequest.getBarcodeVerify();
        whRequest.setMaterialPassNo(materialPassNo);
        LOGGER.info("barcodeVerified : " + barcodeVerified);
        boolean cp = false;
        if (materialPassNo.equals(barcodeVerified)) {
            whRequest.setStatus("Barcode Verification Pass");
            whRequest.setFlag("0");
            cp = true;
            LOGGER.info("Barcode Verification Pass");
        } else {
            whRequest.setStatus("Barcode Verification Fail");
            whRequest.setFlag("0");
            cp = false;
            LOGGER.info("Barcode Verification Fail");
        }
        
        tempCount = tempCount + 1;
        whRequest.setTempCount(Integer.toString(tempCount));
        WhRequestDAO whRequestDAO = new WhRequestDAO();
        QueryResult queryResult = whRequestDAO.updateWhRequestVerification(whRequest);
        
        WhRequestDAO whRequestDAO3 = new WhRequestDAO();
        WhRequest query3 = whRequestDAO3.getWhReq(refId);
        LogModule logModule3 = new LogModule();
        LogModuleDAO logModuleDAO3 = new LogModuleDAO();
        logModule3.setModuleId(query3.getId());
        logModule3.setReferenceId(refId);
        logModule3.setModuleName("hms_wh_request_list");
        logModule3.setStatus(query3.getStatus());
        logModule3.setVerifiedBy(query3.getUserVerify());
        logModule3.setVerifiedDate(query3.getDateVerify());
        QueryResult queryResult3 = logModuleDAO3.insertLogForVerification(logModule3);
        
       // String check = "";
        args = new String[1];
        args[0] = materialPassNo;
        if (queryResult.getResult() == 1 && cp == true) {
            redirectAttrs.addFlashAttribute("success", messageSource.getMessage("general.label.update.success2", args, locale));
        } else {
            //redirectAttrs.addFlashAttribute("error", messageSource.getMessage("general.label.update.error", args, locale));
        }
        return "redirect:/wh/whRequest/verify/" + refId;
    }
    
    @RequestMapping(value = "/verifyInventory", method = RequestMethod.POST)
    public String setInventory(
            Model model,
            Locale locale,
            HttpServletRequest request,
            RedirectAttributes redirectAttrs,
            @ModelAttribute UserSession userSession,
            @RequestParam(required = false) String refId,
            @RequestParam(required = false) String materialPassNo,
            @RequestParam(required = false) String inventoryRack,
            @RequestParam(required = false) String inventoryRackVerify,
            @RequestParam(required = false) String inventoryShelf,
            @RequestParam(required = false) String inventoryShelfVerify,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String flag
    ) {
        WhRequest whRequest = new WhRequest();
        whRequest.setRefId(refId);      
        whRequest.setMaterialPassNo(materialPassNo);
        whRequest.setInventoryRackVerify(inventoryRackVerify);
        whRequest.setInventoryShelfVerify(inventoryShelfVerify);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        whRequest.setInventoryDateVerify(dateFormat.format(date));
        whRequest.setInventoryUserVerify(userSession.getFullname());
        String rackVerified = whRequest.getInventoryRackVerify();
        String shelfVerified = whRequest.getInventoryShelfVerify();
        boolean cp = false;
        if (inventoryRack.equals(rackVerified) && inventoryShelf.equals(shelfVerified)) {
            whRequest.setStatus("Queue for Shipping");
            whRequest.setFlag("1");
            cp = true;
            LOGGER.info("Queue for Shipping");
        } else {
            whRequest.setStatus("Wrong Inventory");
            whRequest.setFlag("0");
            cp = false;
            LOGGER.info("Wrong Inventory");
        }
        
        WhRequestDAO whRequestDAO = new WhRequestDAO();
        QueryResult queryResult = whRequestDAO.updateWhRequestForShipping(whRequest);
        
        WhRequestDAO whRequestDAO3 = new WhRequestDAO();
        WhRequest query3 = whRequestDAO3.getWhReq(refId);
        LogModule logModule3 = new LogModule();
        LogModuleDAO logModuleDAO3 = new LogModuleDAO();
        logModule3.setModuleId(query3.getId());
        logModule3.setReferenceId(refId);
        logModule3.setModuleName("hms_wh_request_list");
        logModule3.setStatus(query3.getStatus());
        logModule3.setVerifiedBy(query3.getInventoryUserVerify());
        logModule3.setVerifiedDate(query3.getInventoryDateVerify());
        QueryResult queryResult3 = logModuleDAO3.insertLogForVerification(logModule3);
        
        if(cp == true) {
            WhShipping whShipping = new WhShipping();
            whShipping.setRequestId(refId);
            whShipping.setMaterialPassNo(materialPassNo);
            whShipping.setStatus("Ready for Shipment");
            whShipping.setFlag("0");
            whShipping.setShippingBy(userSession.getFullname());
            WhShippingDAO whShippingDao = new WhShippingDAO();
            QueryResult queryResult2 = whShippingDao.insertWhShipping(whShipping);
            
            WhShippingDAO whShippingDAO3 = new WhShippingDAO();
            WhShipping query2 = whShippingDAO3.getWhShipping(refId);
            LogModule logModule2 = new LogModule();
            LogModuleDAO logModuleDAO2 = new LogModuleDAO();
            logModule2.setModuleId(query2.getId());
            logModule2.setReferenceId(refId);
            logModule2.setModuleName("hms_wh_shipping_list");
            logModule2.setStatus(query2.getStatus());
            QueryResult queryResult1 = logModuleDAO2.insertLog(logModule2);
        }
        
        String check = "";
        args = new String[1];
        args[0] = materialPassNo;
        if (queryResult.getResult() == 0 || cp == false) {
            //redirectAttrs.addFlashAttribute("error", messageSource.getMessage("general.label.update.error", args, locale));
            check = "redirect:/wh/whRequest/verify/" + refId;
        } else {
            redirectAttrs.addFlashAttribute("success", messageSource.getMessage("general.label.update.success4", args, locale));
            check = "redirect:/wh/whShipping/";
        }
        return check;
    }
    
    @RequestMapping(value = "/history/{whRequestId}", method = RequestMethod.GET)
    public String history(
            Model model,
            HttpServletRequest request,
            @PathVariable("whRequestId") String whRequestId
    ) throws UnsupportedEncodingException {
        LOGGER.info("Masuk view 1........");        
        String pdfUrl = URLEncoder.encode(request.getContextPath() + "/wh/whRequest/viewWhRequestLogPdf/" + whRequestId, "UTF-8");
        String backUrl = servletContext.getContextPath() + "/wh/whRequest";
        model.addAttribute("pdfUrl", pdfUrl);
        model.addAttribute("backUrl", backUrl);
        model.addAttribute("pageTitle", "Warehouse Management - Hardware Request History");
        LOGGER.info("Masuk view 2........");
        return "pdf/viewer";
    }
    
    @RequestMapping(value = "/viewWhRequestLogPdf/{whRequestId}", method = RequestMethod.GET)
    public ModelAndView viewWhRequestHistPdf(
            Model model,
            @PathVariable("whRequestId") String whRequestId
    ) {
        WhRequestDAO whRequestDAO = new WhRequestDAO();        
        LOGGER.info("Masuk 1........");
        List<WhRequestLog> whHistoryList = whRequestDAO.getWhReqLog(whRequestId);
//        WhRequestLog whHistoryList = whRequestDAO.getWhRetLog(whRequestId);
        LOGGER.info("Masuk 2........");
        return new ModelAndView("whRequestLogPdf", "whRequestLog", whHistoryList);
    }
    
    @RequestMapping(value = "/error/{whRequestId}", method = {RequestMethod.GET, RequestMethod.POST})
    public String error(
            Model model,
            Locale locale,
            HttpServletRequest request,
            RedirectAttributes redirectAttrs,
            @ModelAttribute UserSession userSession,
            @PathVariable("whRequestId") String whRequestId
    ) throws UnsupportedEncodingException {
        //send email
        LOGGER.info("send email to warehouse");
        WhRequestDAO whRequestDAO = new WhRequestDAO();
        int count = whRequestDAO.getCountExistingData(whRequestId);
        if (count != 0) {
            whRequestDAO = new WhRequestDAO();
            WhRequest query = whRequestDAO.getWhRequest(whRequestId);

            EmailSender emailSender = new EmailSender();
            emailSender.htmlEmail2(
                    servletContext,
                    query.getRequestedBy(), //user name
                    query.getRequestedEmail(), //to
                    "Error in Hardware Retrieval Verification in HMS", //subject
                    "Barcode Verification for hardware " + query.getEquipmentId() + " (" + query.getEquipmentType() + "), with material pass number: " + query.getMaterialPassNo() + " is INVALID." //msg
            );
            args = new String[1];
            args[0] = query.getMaterialPassNo();
            redirectAttrs.addFlashAttribute("success", messageSource.getMessage("general.label.update.success6", args, locale));
            System.out.println("Email has been sent.");
        }
        return "redirect:/wh/whRequest/verify/" + whRequestId;
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
            @RequestParam(required = false) String receivedDate1,
            @RequestParam(required = false) String receivedDate2,
            @RequestParam(required = false) String status
    ) {
        System.out.println("masukkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk!!!!!!!!!!!!!!!");
        
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
        if(receivedDate1!=null &&  receivedDate2!=null) {
            if(!receivedDate1.equals("") && !receivedDate2.equals("")) {
                count++;
                String receivedDate = " arrival_received_date BETWEEN CAST(\'" + receivedDate1 + "\' AS DATE) AND CAST(\'" + receivedDate2 +"\' AS DATE) ";
                if(count == 1)
                    query = receivedDate;
                else if(count>1)
                    query = query + " AND " + receivedDate;
            }
        }
        if(status!=null) {
            if(!("").equals(status)) {
                count++;
                if(count == 1)
                    query = " status = \'" + status + "\' ";
                else if(count>1)
                    query = query + " AND status = \'" + status + "\' ";
            }
        }
        
        System.out.println("Query: " + query);
        WhRequestDAO wh = new WhRequestDAO();
        List<WhRequest> requestQueryList = wh.getQuery(query);
        
        model.addAttribute("requestQueryList", requestQueryList);
        return "whRequest/query";
    }
}
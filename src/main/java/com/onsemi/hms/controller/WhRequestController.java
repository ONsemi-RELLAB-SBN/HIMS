package com.onsemi.hms.controller;

//import com.onsemi.hms.dao.WhShippingDAO;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import com.onsemi.hms.dao.WhRequestDAO;
import com.onsemi.hms.dao.WhShippingDAO;
import com.onsemi.hms.model.WhRequest;
import com.onsemi.hms.model.UserSession;
import com.onsemi.hms.model.WhShipping;
import com.onsemi.hms.tools.EmailSender;
import com.onsemi.hms.tools.QueryResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
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
    private static final String HEADER = "id,material_pass_no,equipment_type,equipment_id,quantity,requested_by,requested_date,remarks,date_verify,shipping_date,shipping_rack,shipping_slot,shipping_by,status";

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
        if (whRequest.getStatus().equals("New Request") || whRequest.getStatus().equals("Verification Fail")) {
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
        model.addAttribute("whRequest", whRequest);
        return "whRequest/verify";
    }
    
    @RequestMapping(value = "/setShipping", method = RequestMethod.POST)
    public String setShipping(
            Model model,
            Locale locale,
            HttpServletRequest request,
            RedirectAttributes redirectAttrs,
            @ModelAttribute UserSession userSession,
            @RequestParam(required = false) String refId,
            @RequestParam(required = false) String materialPassNo,
            @RequestParam(required = false) String equipmentType,
            @RequestParam(required = false) String equipmentId,
            @RequestParam(required = false) String quantity,
            @RequestParam(required = false) String requestedBy,
            @RequestParam(required = false) String requestedDate,
            @RequestParam(required = false) String materialPassExpiry,
            @RequestParam(required = false) String inventoryRack,
            @RequestParam(required = false) String inventorySlot,
            @RequestParam(required = false) String barcodeVerify,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String shippingDate
            
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
            whRequest.setStatus("Move to Shipping");
            whRequest.setFlag("1");
            cp = true;
            LOGGER.info("Move to Shipping");
            
            
        } else {
            whRequest.setStatus("Verification Fail");
            whRequest.setFlag("0");
            cp = false;
            LOGGER.info("Verification Fail");
        }
        
        WhRequestDAO whRequestDAO = new WhRequestDAO();
        QueryResult queryResult = whRequestDAO.updateWhRequestForShipping(whRequest);
        
        if(whRequest.getStatus().equals("Move to Shipping")) {
            //save id to table wh_shipping_list
            WhShipping whShipping = new WhShipping();
            whShipping.setRequestId(refId);
            whShipping.setMaterialPassNo(materialPassNo);
            whShipping.setStatus(whRequest.getStatus());
            whShipping.setShippingBy(userSession.getFullname());
            whShipping.setShippingDate(dateFormat.format(date));
            whShipping.setInventoryRack(inventoryRack);
            whShipping.setInventorySlot(inventorySlot);
            
            WhShippingDAO whShippingDAO = new WhShippingDAO();
            int count = whShippingDAO.getCountExistingData(whShipping.getRequestId());
            if (count == 0) {
                LOGGER.info("data xdeeeeee");
                whShippingDAO = new WhShippingDAO();
                QueryResult queryResult1 = whShippingDAO.insertWhShipping(whShipping);
                
                args = new String[1];
                args[0] = materialPassNo;
                if (queryResult.getResult() == 0 && cp == false) {
                    //redirectAttrs.addFlashAttribute("error", messageSource.getMessage("general.label.update.error", args, locale));
                } else {
                    String username = System.getProperty("user.name");
                    //SEND EMAIL
                    File file = new File("C:\\Users\\" + username + "\\Documents\\from HMS\\hms_shipping.csv");
                    if (file.exists()) {
                        LOGGER.info("dh ada header");
                        FileWriter fileWriter = null;
                        try {
                            fileWriter = new FileWriter("C:\\Users\\" + username + "\\Documents\\from HMS\\hms_shipping.csv", true);
                            //New Line after the header
                            fileWriter.append(LINE_SEPARATOR);
                            WhShippingDAO whdao = new WhShippingDAO();
                            WhShipping wh = whdao.getWhShippingMergeWithRequest(refId);

                            fileWriter.append(refId);
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(wh.getMaterialPassNo());
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(wh.getEquipmentType());
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(wh.getEquipmentId());
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(wh.getQuantity());
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(wh.getRequestedBy());
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(wh.getRequestedDate());
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(wh.getRemarks());
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(wh.getDateVerify());
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(wh.getShippingDate());
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(wh.getInventoryRack());
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(wh.getInventorySlot());
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(userSession.getFullname());
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(wh.getStatus());
//                            fileWriter.append(COMMA_DELIMITER);
                            System.out.println("append to CSV file Succeed!!!");
                        } catch (Exception ee) {
                            System.out.println("Error 1 occured while append the fileWriter");
                        } finally {
                            try {
                                fileWriter.close();
                            } catch (IOException ie) {
                                System.out.println("Error 2 occured while closing the fileWriter");
                            }
                        }
                    } else {
                        FileWriter fileWriter = null;
                        try {
                            fileWriter = new FileWriter("C:\\Users\\" + username + "\\Documents\\from HMS\\hms_shipping.csv");
                            LOGGER.info("no file yet");
                            //Adding the header
                            fileWriter.append(HEADER);

                            //New Line after the header
                            fileWriter.append(LINE_SEPARATOR);
                            WhShippingDAO whdao = new WhShippingDAO();
                            WhShipping wh = whdao.getWhShippingMergeWithRequest(refId);
                            fileWriter.append(refId);
                            fileWriter.append(COMMA_DELIMITER); 
                            fileWriter.append(wh.getMaterialPassNo());
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(wh.getEquipmentType());
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(wh.getEquipmentId());
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(wh.getQuantity());
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(wh.getRequestedBy());
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(wh.getRequestedDate());
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(wh.getRemarks());
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(wh.getDateVerify());
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(wh.getShippingDate());
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(wh.getInventoryRack());
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(wh.getInventorySlot());
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(userSession.getFullname());
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(wh.getStatus());
//                            fileWriter.append(COMMA_DELIMITER);              
                        } catch (Exception ee) {
                            System.out.println("Error 1 occured while append the fileWriter");
                        } finally {
                            try {
                                System.out.println("write new to CSV file Succeed!!!");
                                fileWriter.close();
                            } catch (IOException ie) {
                                System.out.println("Error 2 occured while closing the fileWriter");
                                ie.printStackTrace();
                            }
                        }
                    }

                    //send email
                    LOGGER.info("send email to warehouse");

                    //to get hostname
                    InetAddress ip;
                    String hostName ="";
                    try {
                        ip = InetAddress.getLocalHost();
                        hostName = ip.getHostName();
                    } catch (UnknownHostException ex) {
                        java.util.logging.Logger.getLogger(WhRequestController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    EmailSender emailSender = new EmailSender();
                    com.onsemi.hms.model.User user = new com.onsemi.hms.model.User();
                    user.setFullname(userSession.getFullname());
                    emailSender.htmlEmailWithAttachmentRequest(
                        servletContext,
                        user,                                                   //user name
                        "cdarsrel@gmail.com",                                   //to
                        "Verification Status for New Hardware Request from HMS",   //subject
                        "Verification for New Hardware Request has been made. Please go to this link " //msg
                        + "<a href=\"" + request.getScheme() + "://" + hostName + ":" + request.getServerPort() + request.getContextPath() + "/wh/whRequest/edit/" + refId + "\">HMS</a>"
                        + " for verification status checking."
                    );

                    redirectAttrs.addFlashAttribute("success", messageSource.getMessage("general.label.update.success3", args, locale));
                }
            } else {
                LOGGER.info("data adeeeeee");
            }
        }
        
        return "redirect:/wh/whShipping/";
    }
}
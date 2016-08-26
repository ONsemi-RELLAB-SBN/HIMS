package com.onsemi.hms.controller;

import com.onsemi.hms.dao.WhInventoryDAO;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import com.onsemi.hms.dao.WhRetrieveDAO;
import com.onsemi.hms.model.WhRetrieve;
import com.onsemi.hms.model.UserSession;
import com.onsemi.hms.model.WhInventory;
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
@RequestMapping(value = "/wh/whRetrieve")
@SessionAttributes({"userSession"})
public class WhRetrieveController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WhRetrieveController.class);
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
    public String whRetrieve(
            Model model, @ModelAttribute UserSession userSession
    ) {
        WhRetrieveDAO whRetrieveDAO = new WhRetrieveDAO();
        List<WhRetrieve> whRetrieveList = whRetrieveDAO.getWhRetrieveList();
        String groupId = userSession.getGroup();

        model.addAttribute("whRetrieveList", whRetrieveList);
        model.addAttribute("groupId", groupId);

        return "whRetrieve/whRetrieve";
    }

    @RequestMapping(value = "/view/{whRetrieveId}", method = RequestMethod.GET)
    public String view(
            Model model,
            HttpServletRequest request,
            @PathVariable("whRetrieveId") String whRetrieveId
    ) throws UnsupportedEncodingException {
        String pdfUrl = URLEncoder.encode(request.getContextPath() + "/wh/whRetrieve/viewWhRetrievePdf/" + whRetrieveId, "UTF-8");
        String backUrl = servletContext.getContextPath() + "/wh/whRetrieve";
        model.addAttribute("pdfUrl", pdfUrl);
        model.addAttribute("backUrl", backUrl);
        model.addAttribute("pageTitle", "Warehouse Management - Hardware Retrieve");
        return "pdf/viewer";
    }

    @RequestMapping(value = "/viewWhRetrievePdf/{whRetrieveId}", method = RequestMethod.GET)
    public ModelAndView viewWhRetrievePdf(
            Model model,
            @PathVariable("whRetrieveId") String whRetrieveId
    ) {
        WhRetrieveDAO whRetrieveDAO = new WhRetrieveDAO();
        WhRetrieve whRetrieve = whRetrieveDAO.getWhRetrieve(whRetrieveId);
        return new ModelAndView("whRetrievePdf", "whRetrieve", whRetrieve);
    }

    
    @RequestMapping(value = "/verify/{whRetrieveId}", method = RequestMethod.GET)
    public String verify(
            Model model,
            @PathVariable("whRetrieveId") String whRetrieveId
    ) {
        WhRetrieveDAO whRetrieveDAO = new WhRetrieveDAO();
        WhRetrieve whRetrieve = whRetrieveDAO.getWhRetrieve(whRetrieveId);
        
        String type = whRetrieve.getEquipmentType();
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
        if (whRetrieve.getStatus().equals("New Retrieval Request") || whRetrieve.getStatus().equals("Verification Fail")) {
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
        if (whRetrieve.getStatus().equals("Verification Pass")) {
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
        model.addAttribute("whRetrieve", whRetrieve);
        return "whRetrieve/verify";
    }
    
    @RequestMapping(value = "/verifyMp", method = RequestMethod.POST)
    public String verifyMp(
            Model model,
            Locale locale,
            RedirectAttributes redirectAttrs,
            @ModelAttribute UserSession userSession,
            @RequestParam(required = false) String refId,
            @RequestParam(required = false) String materialPassNo,
            @RequestParam(required = false) String materialPassExpiry,
            @RequestParam(required = false) String barcodeVerify,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String flag
    ) {
        WhRetrieve whRetrieve = new WhRetrieve();
        whRetrieve.setRefId(refId);
        whRetrieve.setBarcodeVerify(barcodeVerify);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        whRetrieve.setDateVerify(dateFormat.format(date));
        whRetrieve.setUserVerify(userSession.getFullname());
        String barcodeVerified = whRetrieve.getBarcodeVerify();
        whRetrieve.setMaterialPassNo(materialPassNo);
        LOGGER.info("barcodeVerified : " + barcodeVerified);
        boolean cp = false;
        if (materialPassNo.equals(barcodeVerified)) {
            whRetrieve.setStatus("Verification Pass");
            whRetrieve.setFlag("1");
            cp = true;
            LOGGER.info("Verification Pass");
        } else {
            whRetrieve.setStatus("Verification Fail");
            whRetrieve.setFlag("0");
            cp = false;
            LOGGER.info("Verification Fail");
        }
        WhRetrieveDAO whRetrieveDAO = new WhRetrieveDAO();
        QueryResult queryResult = whRetrieveDAO.updateWhRetrieveVerification(whRetrieve);
        args = new String[1];
        args[0] = barcodeVerify;
        if (queryResult.getResult() == 1 && cp == true) {
            redirectAttrs.addFlashAttribute("success", messageSource.getMessage("general.label.update.success2", args, locale));
        } else {
            //redirectAttrs.addFlashAttribute("error", messageSource.getMessage("general.label.update.error", args, locale));
        }
        return "redirect:/wh/whRetrieve/verify/" + refId;
    }
    
    @RequestMapping(value = "/setInventory", method = RequestMethod.POST)
    public String setInventory(
            Model model,
            Locale locale,
            HttpServletRequest request,
            RedirectAttributes redirectAttrs,
            @ModelAttribute UserSession userSession,
            @RequestParam(required = false) String refId,
            @RequestParam(required = false) String materialPassNo,
            @RequestParam(required = false) String materialPassExpiry,
            @RequestParam(required = false) String equipmentType,
            @RequestParam(required = false) String equipmentId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String quantity,
            @RequestParam(required = false) String barcodeVerify,
            @RequestParam(required = false) String dateVerify,
            @RequestParam(required = false) String inventoryDate,
            @RequestParam(required = false) String inventoryRack,
            @RequestParam(required = false) String inventorySlot,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String flag
    ) {
        WhRetrieve whRetrieve = new WhRetrieve();
        whRetrieve.setRefId(refId);      
        whRetrieve.setMaterialPassNo(materialPassNo);
        boolean cp = false;
        if(status.equals("Verification Pass")) {
            whRetrieve.setStatus("Move to Inventory");
            whRetrieve.setFlag("1");
            cp = true;
            LOGGER.info("Inventory Pass");
        } else {
            whRetrieve.setStatus(status);
            whRetrieve.setFlag(flag);
            cp = false;
            LOGGER.info("Inventory Fail");
        }
        WhRetrieveDAO whRetrieveDAO = new WhRetrieveDAO();
        QueryResult queryResult = whRetrieveDAO.updateWhRetrieveForInventory(whRetrieve);
        
        
        if(whRetrieve.getStatus().equals("Move to Inventory")) {
            //save id to table wh_inventory_list
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
        
            WhInventory whInventory = new WhInventory();
            whInventory.setRefId(refId);
            whInventory.setMaterialPassNo(materialPassNo);
            whInventory.setStatus(whRetrieve.getStatus());
            whInventory.setInventoryBy(userSession.getFullname());
            whInventory.setInventoryDate(dateFormat.format(date));
            whInventory.setInventoryRack(inventoryRack);
            whInventory.setInventorySlot(inventorySlot);
            
            WhInventoryDAO whInventoryDAO = new WhInventoryDAO();
            int count = whInventoryDAO.getCountExistingData(whInventory.getRefId());
            if (count == 0) {
                LOGGER.info("data xdeeeeee");
                whInventoryDAO = new WhInventoryDAO();
                QueryResult queryResult1 = whInventoryDAO.insertWhInventory(whInventory);
                
                args = new String[1];
                args[0] = materialPassNo;
                if (queryResult.getResult() == 0 && cp == false) {
                    //redirectAttrs.addFlashAttribute("error", messageSource.getMessage("general.label.update.error", args, locale));
                } else {
                    String username = System.getProperty("user.name");
                    //SEND EMAIL
                    File file = new File("C:\\Users\\" + username + "\\Documents\\from HMS\\hms_inventory.csv");
                    if (file.exists()) {
                        LOGGER.info("dh ada header");
                        FileWriter fileWriter = null;
                        try {
                            fileWriter = new FileWriter("C:\\Users\\" + username + "\\Documents\\from HMS\\hms_inventory.csv", true);
                            //New Line after the header
                            fileWriter.append(LINE_SEPARATOR);
                            WhInventoryDAO whdao = new WhInventoryDAO();
                            WhInventory wh = whdao.getWhInventoryMergeWithRetrievePdf(refId);

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
                            fileWriter.append(wh.getInventoryDate());
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
                            fileWriter = new FileWriter("C:\\Users\\" + username + "\\Documents\\from HMS\\hms_inventory.csv");
                            LOGGER.info("no file yet");
                            //Adding the header
                            fileWriter.append(HEADER);

                            //New Line after the header
                            fileWriter.append(LINE_SEPARATOR);
                            WhInventoryDAO whdao = new WhInventoryDAO();
                            WhInventory wh = whdao.getWhInventoryMergeWithRetrievePdf(refId);
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
                            fileWriter.append(wh.getInventoryDate());
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

                    /*to get hostname*/
                    InetAddress ip;
                    String hostName ="";
                    try {
                        ip = InetAddress.getLocalHost();
                        hostName = ip.getHostName();
                    } catch (UnknownHostException ex) {
                        java.util.logging.Logger.getLogger(WhRetrieveController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    EmailSender emailSender = new EmailSender();
                    com.onsemi.hms.model.User user = new com.onsemi.hms.model.User();
                    user.setFullname(userSession.getFullname());
                    emailSender.htmlEmailWithAttachmentRetrieve(
                        servletContext,
                        user,                                                   //user name
                        "cdarsrel@gmail.com",                                   //to
                        "Verification Status for New Hardware Request from HMS",   //subject
                        "Verification for New Hardware Request has been made. Please go to this link " //msg
                        + "<a href=\"" + request.getScheme() + "://" + hostName + ":" + request.getServerPort() + request.getContextPath() + "/wh/whRetrieve/edit/" + refId + "\">HMS</a>"
                        + " for verification status checking."
                    );

                    redirectAttrs.addFlashAttribute("success", messageSource.getMessage("general.label.update.success3", args, locale));
                }
            } else {
                LOGGER.info("data adeeeeee");
            }
        }
        
        return "redirect:/wh/whInventory/";
    }
}
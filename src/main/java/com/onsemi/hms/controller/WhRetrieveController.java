package com.onsemi.hms.controller;

import com.onsemi.hms.dao.LogModuleDAO;
import com.onsemi.hms.dao.WhInventoryDAO;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import com.onsemi.hms.dao.WhRetrieveDAO;
import com.onsemi.hms.model.IonicFtpRetrieve2;
import com.onsemi.hms.model.LogModule;
import com.onsemi.hms.model.WhRetrieve;
import com.onsemi.hms.model.UserSession;
import com.onsemi.hms.model.WhInventory;
import com.onsemi.hms.model.WhRetrieveLog;
import com.onsemi.hms.tools.EmailSender;
import com.onsemi.hms.tools.QueryResult;
import com.onsemi.hms.tools.SpmlUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
    private static final String HEADER = "retrieve_id,material_pass_no,material_pass_expiry,equipment_type,equipment_id,pcb_A,qty_qualA,pcb_B,qty_qualB,pcb_C,qty_qualC,pcb_control,qty_control,total_quantity,requested_by,requested_date,remarks,date_verify,inventory_date,inventory_rack,inventory_shelf,inventory_by,status";

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
        if (whRetrieve.getStatus().equals("New Inventory Request") || whRetrieve.getStatus().equals("Verification Fail")) {
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
            whRetrieve.setFlag("0");
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
        
        WhRetrieveDAO whRetrieveDAO2 = new WhRetrieveDAO();
        WhRetrieve query = whRetrieveDAO2.getWhRet(refId);
        LogModule logModule = new LogModule();
        LogModuleDAO logModuleDAO = new LogModuleDAO();
        logModule.setModuleId(query.getId());
        logModule.setReferenceId(refId);
        logModule.setModuleName("hms_wh_retrieval_list");
        logModule.setStatus(query.getStatus());
        logModule.setVerifiedBy(query.getUserVerify());
        logModule.setVerifiedDate(query.getDateVerify());
        QueryResult queryResult2 = logModuleDAO.insertLogForVerification(logModule);
        
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
            @RequestParam(required = false) String inventoryRack,
            @RequestParam(required = false) String inventoryShelf,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String flag
    ) {
        WhRetrieve whRetrieve = new WhRetrieve();
        whRetrieve.setRefId(refId);      
        whRetrieve.setMaterialPassNo(materialPassNo);
        
        boolean ck = false;
        LOGGER.info("************************************ " + inventoryRack + " vs " + inventoryShelf.substring(0,6) + " ************************************");
        if(inventoryShelf.substring(0,6).equals(inventoryRack)) {
            ck = true;
        }
        
        String url;
        if(ck == true) {
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

            WhRetrieveDAO whRetrieveDAO2 = new WhRetrieveDAO();
            WhRetrieve query2 = whRetrieveDAO2.getWhRetrieve(refId);
            LogModule logModule2 = new LogModule();
            LogModuleDAO logModuleDAO2 = new LogModuleDAO();
            logModule2.setModuleId(query2.getId());
            logModule2.setReferenceId(refId);
            logModule2.setModuleName("hms_wh_retrieval_list");
            logModule2.setStatus(query2.getStatus());
            QueryResult queryResult2 = logModuleDAO2.insertLog(logModule2);

            if(whRetrieve.getFlag().equals("1")) {
                //save id to table wh_inventory_list
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();

                WhInventory whInventory = new WhInventory();
                whInventory.setRefId(refId);
                whInventory.setMaterialPassNo(materialPassNo);
                whInventory.setStatus("Available");
                whInventory.setInventoryBy(userSession.getFullname());
                whInventory.setInventoryRack(inventoryRack);
                whInventory.setInventoryShelf(inventoryShelf);

                WhInventoryDAO whInventoryDAO = new WhInventoryDAO();
                int count = whInventoryDAO.getCountExistingData(whInventory.getRefId());
                if (count == 0) {
                    LOGGER.info("data xdeeeeee");
                    whInventoryDAO = new WhInventoryDAO();
                    QueryResult queryResult1 = whInventoryDAO.insertWhInventory(whInventory);

                    WhInventoryDAO whInventoryDAO3 = new WhInventoryDAO();
                    WhInventory query3 = whInventoryDAO3.getWhInventory(refId);
                    LogModule logModule3 = new LogModule();
                    LogModuleDAO logModuleDAO3 = new LogModuleDAO();
                    logModule3.setModuleId(query3.getId());
                    logModule3.setReferenceId(refId);
                    logModule3.setModuleName("hms_wh_inventory_list");
                    logModule3.setStatus(query3.getStatus());
                    logModule3.setVerifiedBy(query3.getInventoryBy());
                    logModule3.setVerifiedDate(query3.getInventoryDate());
                    QueryResult queryResult3 = logModuleDAO3.insertLogForVerification(logModule3);

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
                            FileReader fileReader = null;
                            try {
                                fileWriter = new FileWriter("C:\\Users\\" + username + "\\Documents\\from HMS\\hms_inventory.csv", true);
                                fileReader = new FileReader("C:\\Users\\" + username + "\\Documents\\from HMS\\hms_inventory.csv");
                                String targetLocation = "C:\\Users\\" + username + "\\Documents\\from HMS\\hms_inventory.csv";

                                BufferedReader bufferedReader = new BufferedReader(fileReader); 
                                String data = bufferedReader.readLine();
                                StringBuilder buff = new StringBuilder();

                                boolean check = false;
                                int row = 0;
                                while(data != null){
                                    LOGGER.info("start reading file..........");
                                    buff.append(data).append(System.getProperty("line.separator"));
                                    System.out.println("dataaaaaaaaa : \n" + data);

                                    String[] split = data.split(",");
                                    IonicFtpRetrieve2 retrieve = new IonicFtpRetrieve2(
                                        split[0], split[1], split[2],
                                        split[3], split[4], split[5], 
                                        split[6], split[7], split[8],
                                        split[9], split[10], split[11],
                                        split[12], split[13]
                                    );

                                    if(split[0].equals(refId)) {
                                        LOGGER.info(row + " : retrieve Id found...................." + data);
                                        check = true;
                                    } else {
                                        LOGGER.info("retrieve Id not found........" + data);
                                    }
                                    data = bufferedReader.readLine();
                                    row++;
                                }
                                bufferedReader.close();
                                fileReader.close();

                                if(check == false) {
                                    //New Line after the header
                                    fileWriter.append(LINE_SEPARATOR);
                                    WhInventoryDAO whdao = new WhInventoryDAO();
                                    WhInventory wh = whdao.getWhInventoryMergeWithRetrieve(refId);

                                    String pcbA = wh.getPcbA(), pcbB = wh.getPcbB(), pcbC = wh.getPcbC(), pcbControl = wh.getPcbControl();
                                    if(!wh.getEquipmentType().equals("PCB")) {
                                        if(wh.getPcbA() == null || wh.getPcbA().equals("null")) {
                                            pcbA = SpmlUtil.nullToEmptyString(wh.getPcbA());
                                        }
                                        if(wh.getPcbB() == null || wh.getPcbB().equals("null")) {
                                            pcbB = SpmlUtil.nullToEmptyString(wh.getPcbB());
                                        }
                                        if(wh.getPcbC() == null || wh.getPcbC().equals("null")) {
                                            pcbC = SpmlUtil.nullToEmptyString(wh.getPcbC());
                                        }
                                        if(wh.getPcbControl() == null || wh.getPcbControl().equals("null")) {
                                            pcbControl = SpmlUtil.nullToEmptyString(wh.getPcbControl());
                                        }
                                    }

                                    fileWriter.append(refId);
                                    fileWriter.append(COMMA_DELIMITER);
                                    fileWriter.append(wh.getMaterialPassNo());
                                    fileWriter.append(COMMA_DELIMITER);
                                    fileWriter.append(wh.getMaterialPassExpiry());
                                    fileWriter.append(COMMA_DELIMITER);
                                    fileWriter.append(wh.getEquipmentType());
                                    fileWriter.append(COMMA_DELIMITER);
                                    fileWriter.append(wh.getEquipmentId());
                                    fileWriter.append(COMMA_DELIMITER);
                                    fileWriter.append(pcbA);
                                    fileWriter.append(COMMA_DELIMITER);
                                    fileWriter.append(wh.getQtyQualA());
                                    fileWriter.append(COMMA_DELIMITER);
                                    fileWriter.append(pcbB);
                                    fileWriter.append(COMMA_DELIMITER);
                                    fileWriter.append(wh.getQtyQualB());
                                    fileWriter.append(COMMA_DELIMITER);
                                    fileWriter.append(pcbC);
                                    fileWriter.append(COMMA_DELIMITER);
                                    fileWriter.append(wh.getQtyQualC());
                                    fileWriter.append(COMMA_DELIMITER);
                                    fileWriter.append(pcbControl);
                                    fileWriter.append(COMMA_DELIMITER);
                                    fileWriter.append(wh.getQtyControl());
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
                                    fileWriter.append(wh.getInventoryShelf());
                                    fileWriter.append(COMMA_DELIMITER);
                                    fileWriter.append(userSession.getFullname());
                                    fileWriter.append(COMMA_DELIMITER);
                                    fileWriter.append(wh.getStatus());
        //                            fileWriter.append(COMMA_DELIMITER);
                                    System.out.println("append to CSV file Succeed!!!");
                                }
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
                                WhInventory wh = whdao.getWhInventoryMergeWithRetrieve(refId);

                                String pcbA = wh.getPcbA(), pcbB = wh.getPcbB(), pcbC = wh.getPcbC(), pcbControl = wh.getPcbControl();
                                if(!wh.getEquipmentType().equals("PCB")) {
                                    if(wh.getPcbA() == null || wh.getPcbA().equals("null")) {
                                        pcbA = SpmlUtil.nullToEmptyString(wh.getPcbA());
                                    }
                                    if(wh.getPcbB() == null || wh.getPcbB().equals("null")) {
                                        pcbB = SpmlUtil.nullToEmptyString(wh.getPcbB());
                                    }
                                    if(wh.getPcbC() == null || wh.getPcbC().equals("null")) {
                                        pcbC = SpmlUtil.nullToEmptyString(wh.getPcbC());
                                    }
                                    if(wh.getPcbControl() == null || wh.getPcbControl().equals("null")) {
                                        pcbControl = SpmlUtil.nullToEmptyString(wh.getPcbControl());
                                    }
                                }

                                fileWriter.append(refId);
                                fileWriter.append(COMMA_DELIMITER); 
                                fileWriter.append(wh.getMaterialPassNo());
                                fileWriter.append(COMMA_DELIMITER);
                                fileWriter.append(wh.getMaterialPassExpiry());
                                fileWriter.append(COMMA_DELIMITER);
                                fileWriter.append(wh.getEquipmentType());
                                fileWriter.append(COMMA_DELIMITER);
                                fileWriter.append(wh.getEquipmentId());
                                fileWriter.append(COMMA_DELIMITER);
                                fileWriter.append(pcbA);
                                fileWriter.append(COMMA_DELIMITER);
                                fileWriter.append(wh.getQtyQualA());
                                fileWriter.append(COMMA_DELIMITER);
                                fileWriter.append(pcbB);
                                fileWriter.append(COMMA_DELIMITER);
                                fileWriter.append(wh.getQtyQualB());
                                fileWriter.append(COMMA_DELIMITER);
                                fileWriter.append(pcbC);
                                fileWriter.append(COMMA_DELIMITER);
                                fileWriter.append(wh.getQtyQualC());
                                fileWriter.append(COMMA_DELIMITER);
                                fileWriter.append(pcbControl);
                                fileWriter.append(COMMA_DELIMITER);
                                fileWriter.append(wh.getQtyControl());
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
                                fileWriter.append(wh.getInventoryShelf());
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

                        System.out.println("******************* EMAIL CDARS ******************* cdarsrel@gmail.com");

                        EmailSender emailSender = new EmailSender();
                        emailSender.htmlEmailWithAttachmentRetrieve(
                            servletContext,
                            "CDARS",                                                   //user name
                            "cdarsrel@gmail.com",                                   //to
                            "Status for Hardware Inventory from HMS",  //subject
                            "Verification and inventory for Hardware has been made."    //msg
                        );
                        redirectAttrs.addFlashAttribute("success", messageSource.getMessage("general.label.update.success3", args, locale));
                    }
                } else {
                    LOGGER.info("data adeeeeee");
                }
            }
            url = "redirect:/wh/whInventory/";
        } else {
            url = "redirect:/wh/whRetrieve/verify/" + refId;
        }
        return url;
    }
    
    @RequestMapping(value = "/history/{whRetrieveId}", method = RequestMethod.GET)
    public String history(
            Model model,
            HttpServletRequest request,
            @PathVariable("whRetrieveId") String whRetrieveId
    ) throws UnsupportedEncodingException {
        LOGGER.info("Masuk view 1........");        
        String pdfUrl = URLEncoder.encode(request.getContextPath() + "/wh/whRetrieve/viewWhRetrieveLogPdf/" + whRetrieveId, "UTF-8");
        String backUrl = servletContext.getContextPath() + "/wh/whRetrieve";
        model.addAttribute("pdfUrl", pdfUrl);
        model.addAttribute("backUrl", backUrl);
        model.addAttribute("pageTitle", "Warehouse Management - Hardware Retrieve History");
        LOGGER.info("Masuk view 2........");
        return "pdf/viewer";
    }
    
    @RequestMapping(value = "/viewWhRetrieveLogPdf/{whRetrieveId}", method = RequestMethod.GET)
    public ModelAndView viewWhRetrieveHistPdf(
            Model model,
            @PathVariable("whInventoryId") String whRetrieveId
    ) {
        WhRetrieveDAO whRetrieveDAO = new WhRetrieveDAO();
        WhRetrieve whRetrieve = whRetrieveDAO.getWhRetrieve(whRetrieveId);
        String retrieveId = whRetrieve.getRefId();
        
        LOGGER.info("Masuk 1........");
        List<WhRetrieveLog> whHistoryList = whRetrieveDAO.getWhRetrieveLog(retrieveId);
        LOGGER.info("Masuk 2........");
        return new ModelAndView("whRetrieveLogPdf", "whRetrieve", whHistoryList);
    }
}
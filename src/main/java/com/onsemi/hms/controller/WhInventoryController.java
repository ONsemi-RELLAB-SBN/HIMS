package com.onsemi.hms.controller;

import com.onsemi.hms.dao.InventoryMgtDAO;
import com.onsemi.hms.tools.CSV;
import com.onsemi.hms.dao.LogModuleDAO;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import com.onsemi.hms.dao.WhInventoryDAO;
import com.onsemi.hms.model.IonicFtpInventory;
import com.onsemi.hms.model.LogModule;
import com.onsemi.hms.model.WhInventory;
import com.onsemi.hms.model.UserSession;
import com.onsemi.hms.model.WhInventoryLog;
import com.onsemi.hms.model.WhInventoryMgt;
import com.onsemi.hms.tools.EmailSender;
import com.onsemi.hms.tools.QueryResult;
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
@RequestMapping(value = "/wh/whInventory")
@SessionAttributes({"userSession"})
public class WhInventoryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WhInventoryController.class);
    String[] args = {};
    
    @Autowired
    private MessageSource messageSource;

    @Autowired
    ServletContext servletContext;

    String tempDUMMY;
    
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String whInventory(
            Model model,
            @ModelAttribute UserSession userSession
    ) {
        WhInventoryDAO whInventoryDAO = new WhInventoryDAO();
        List<WhInventory> whInventoryList = whInventoryDAO.getWhInventoryListMergeRetrieve();
        model.addAttribute("userSession", userSession);
        model.addAttribute("whInventoryList", whInventoryList);
        return "whInventory/whInventory";
    }

    @RequestMapping(value = "/edit/{whInventoryId}", method = RequestMethod.GET)
    public String edit(
            Model model,
            @PathVariable("whInventoryId") String whInventoryId
    ) {
        WhInventoryDAO whInventoryDAO = new WhInventoryDAO();
        WhInventory whInventory = whInventoryDAO.getWhInventoryMergeWithRetrievePdf(whInventoryId);
        LOGGER.info("whInventory.getEquipmentType() : " + whInventory.getEquipmentType());
        
        String type = whInventory.getEquipmentType();
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
        model.addAttribute("whInventory", whInventory);
        return "whInventory/edit";
    }
        
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(
            Model model,
            Locale locale,
            HttpServletRequest request,
            RedirectAttributes redirectAttrs,
            @ModelAttribute UserSession userSession,
            @RequestParam(required = false) String refId,
            @RequestParam(required = false) String materialPassNo,
            @RequestParam(required = false) String inventoryRack,
            @RequestParam(required = false) String inventoryShelf,
            @RequestParam(required = false) String equipmentType
    ) {
        WhInventory whInventory = new WhInventory();
        
        whInventory.setRefId(refId); //ref
        LOGGER.info(refId);
        whInventory.setMaterialPassNo(materialPassNo); //args
        LOGGER.info(materialPassNo);
        
        //start add
        boolean checkLength = false;
        if (inventoryRack.length() == 6 && inventoryShelf.length() == 10) {
            checkLength = true;
        }

        boolean checkRack = false;
        boolean ck = false;

        if (checkLength == true) {
            LOGGER.info("************************************ " + inventoryRack + " vs " + inventoryShelf.substring(0, 6) + " ************************************");
            if (equipmentType.equals("PCB")) {
                if (inventoryRack.substring(0, 4).equals("S-PC")) {
                    checkRack = true;
                }
            } else if (equipmentType.equals("Tray")) {
                if (inventoryRack.substring(0, 4).equals("S-TJ") || inventoryRack.substring(0, 4).equals("S-TR")) {
                    checkRack = true;
                }
            } else if (equipmentType.equals("Stencil")) {
                if (inventoryRack.substring(0, 4).equals("S-ST")) {
                    checkRack = true;
                }
            } else if (equipmentType.equals("Motherboard")) {
                if (inventoryRack.substring(0, 4).equals("S-SY") || inventoryRack.substring(0, 4).equals("S-AC") || inventoryRack.substring(0, 4).equals("S-WF") || inventoryRack.substring(0, 4).equals("S-IO")
                        || inventoryRack.substring(0, 4).equals("S-BB") || inventoryRack.substring(0, 4).equals("S-HA") || inventoryRack.substring(0, 4).equals("S-PT")) {
                    checkRack = true;
                }
            }

            if (checkRack == true && inventoryShelf.substring(0, 6).equals(inventoryRack)) {
                ck = true;
            }
        }

        WhInventoryDAO whInventoryDAO = new WhInventoryDAO();
        QueryResult queryResult = null;
        String url = "";
        if (ck == true) {
            whInventory.setInventoryRack(inventoryRack); //update
            whInventory.setInventoryShelf(inventoryShelf); //update
            whInventory.setInventoryBy(userSession.getFullname()); //update
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            whInventory.setInventoryDate(dateFormat.format(date)); //update
            queryResult = whInventoryDAO.updateWhInventory(whInventory);
            
            WhInventoryDAO whInventoryDAO3 = new WhInventoryDAO();
            WhInventory query3 = whInventoryDAO3.getWhInventory(refId);
            LogModule logModule3 = new LogModule();
            LogModuleDAO logModuleDAO3 = new LogModuleDAO();
            logModule3.setModuleId(query3.getId());
            logModule3.setReferenceId(refId);
            logModule3.setModuleName("hms_wh_inventory_list");
            logModule3.setStatus("Change of Inventory");
            logModule3.setVerifiedBy(query3.getInventoryBy());
            logModule3.setVerifiedDate(query3.getInventoryDate());
            QueryResult queryResult3 = logModuleDAO3.insertLogForVerification(logModule3);
            LOGGER.info("Inventory Pass");
        
            args = new String[1];
            args[0] = materialPassNo;
            if (queryResult.getResult() == 1) {
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

                        int row = 0;
                        while(data != null){
                            LOGGER.info("start reading file..........");
                            buff.append(data).append(System.getProperty("line.separator"));
                            System.out.println("dataaaaaaaaa : \n" + data);

                            String[] split = data.split(",");
                            IonicFtpInventory inventory = new IonicFtpInventory(
                                split[0], split[1], split[2],
                                split[3], split[4], split[5], 
                                split[6], split[7], split[8],
                                split[9], split[10], split[11],
                                split[12], split[13], split[14],
                                split[15], split[16], split[17],
                                split[18], split[19], split[20],
                                split[21], split[22], split[23] //date = [19], rack = [20], shelf = [21], by = [22]
                            );

                            if(split[0].equals(refId)) {
                                LOGGER.info(row + " : refId found...................." + data);
                                CSV csv = new CSV();
                                csv.open(new File(targetLocation));
                                csv.put(19, row, "" + whInventory.getInventoryDate());
                                csv.put(20, row, "" + whInventory.getInventoryRack());
                                csv.put(21, row, "" + whInventory.getInventoryShelf());
                                csv.put(22, row, "" + whInventory.getInventoryBy());
                                csv.save(new File(targetLocation)); 
                            } else {
                                LOGGER.info("refId not found........" + data);
                            }
                            data = bufferedReader.readLine();
                            row++;
                        }
                        bufferedReader.close();
                        fileReader.close();
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
                    LOGGER.info("File not exists.................");
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
                
                String[] to = {"cdarsreltest@gmail.com"};
//                String[] to = {"cdarsrel@gmail.com","cdarsreltest@gmail.com"};
                EmailSender emailSender = new EmailSender();
                emailSender.htmlEmailWithAttachmentTest2(
                    servletContext,
                    "CDARS", //user name
                    to, //to
                    "Status for Hardware Inventory from HIMS SF",  //subject
                    "Verification and inventory for Hardware has been made."    //msg
                );

                redirectAttrs.addFlashAttribute("success", messageSource.getMessage("general.label.update.success", args, locale));
                url = "redirect:/wh/whInventory/";
            } else {
                LOGGER.info("----------------------" + queryResult.getResult());
                redirectAttrs.addFlashAttribute("error", messageSource.getMessage("general.label.update.error", args, locale));
                url = "redirect:/wh/whInventory/edit/" + refId;
            }
        } else {
            LOGGER.info("Inventory Invalid");
            url = "redirect:/wh/whInventory/edit/" + refId;
        }
        return url;
    }
    
    @RequestMapping(value = "/view/{whInventoryId}", method = RequestMethod.GET)
    public String view(
            Model model,
            HttpServletRequest request,
            @PathVariable("whInventoryId") String whInventoryId
    ) throws UnsupportedEncodingException {
        LOGGER.info("Masuk view 1........");
        String pdfUrl = URLEncoder.encode(request.getContextPath() + "/wh/whInventory/viewWhInventoryPdf/" + whInventoryId, "UTF-8");
        String backUrl = servletContext.getContextPath() + "/wh/whInventory";
        model.addAttribute("pdfUrl", pdfUrl);
        model.addAttribute("backUrl", backUrl);
        model.addAttribute("pageTitle", "Hardware in SBN Factory");
        LOGGER.info("Masuk view 2........");
        return "pdf/viewer";
    }
    
    @RequestMapping(value = "/viewWhInventoryPdf/{whInventoryId}", method = RequestMethod.GET)
    public ModelAndView viewWhInventoryPdf(
            Model model,
            @PathVariable("whInventoryId") String whInventoryId
    ) {        
        LOGGER.info("Masuk 1........");
        WhInventoryDAO whInventoryDAO = new WhInventoryDAO();
        WhInventory whInventory = whInventoryDAO.getWhInventoryMergeWithRetrievePdf(whInventoryId);
        
        return new ModelAndView("whInventoryPdf", "whInventory", whInventory);
    }
    
    @RequestMapping(value = "/history/{whInventoryId}", method = RequestMethod.GET)
    public String history(
            Model model,
            HttpServletRequest request,
            @PathVariable("whInventoryId") String whInventoryId
    ) throws UnsupportedEncodingException {
        LOGGER.info("Masuk view 1........");        
        String pdfUrl = URLEncoder.encode(request.getContextPath() + "/wh/whInventory/viewWhInventoryLogPdf/" + whInventoryId, "UTF-8");
        String backUrl = servletContext.getContextPath() + "/wh/whInventory";
        model.addAttribute("pdfUrl", pdfUrl);
        model.addAttribute("backUrl", backUrl);
        model.addAttribute("pageTitle", "Hardware in SBN Factory History");
        LOGGER.info("Masuk view 2........");
        return "pdf/viewer";
    }
    
    @RequestMapping(value = "/viewWhInventoryLogPdf/{whInventoryId}", method = RequestMethod.GET)
    public ModelAndView viewWhInventoryHistPdf(
            Model model,
            @PathVariable("whInventoryId") String whInventoryId
    ) {
        WhInventoryDAO whInventoryDAO = new WhInventoryDAO();        
        LOGGER.info("Masuk 1........");
        List<WhInventoryLog> whHistoryList = whInventoryDAO.getWhInventoryRetLog(whInventoryId);
        LOGGER.info("Masuk 2........ tempDUMMY ___________ " + tempDUMMY);
        return new ModelAndView("whInventoryLogPdf", "whInventoryLog", whHistoryList);
    }
    
    /*
    *
    *   QUERY FOR EVERY SUBMODULE
    *
    */
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
        String query = "";
        int count = 0;
        
        if(materialPassNo!=null) {
            if(!materialPassNo.equals("")) {
                count++;
                if(count == 1)
                    query = " I.material_pass_no = \'" + materialPassNo + "\' ";
                else if(count>1)
                    query = query + " AND I.material_pass_no = \'" + materialPassNo + "\' ";
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
            if(!status.equals("")) {
                count++;
                if(count == 1)
                    query = " I.status = \'" + status + "\' ";
                else if(count>1)
                    query = query + " AND I.status = \'" + status + "\' ";
            }
        }
        
        System.out.println("Query: " + query);
        WhInventoryDAO wh = new WhInventoryDAO();
        List<WhInventory> inventoryQueryList = wh.getQuery(query);
        
        model.addAttribute("inventoryQueryList", inventoryQueryList);
        return "whInventory/query";
    }
    
    @RequestMapping(value = "/viewInventory", method = {RequestMethod.GET, RequestMethod.POST})
    public String viewInventory(
            Model model,
            Locale locale,
            RedirectAttributes redirectAttrs,
            @ModelAttribute UserSession userSession,
            @RequestParam(required = false) String rackId
    ) {
        String query = "";
        if(rackId == null) {
            LOGGER.debug("nullllllllllllllllllll~~~~");
        } else {
            if(!rackId.equals("All")) {
                query = "WHERE rack_id= '" + rackId + "' ";
            } else {
                query = "";
            }
        }
        InventoryMgtDAO wh = new InventoryMgtDAO();
        List<WhInventoryMgt> inventoryMgtList = wh.getInventoryDetailsList(query);
        InventoryMgtDAO wh2 = new InventoryMgtDAO();
        List<WhInventoryMgt> inventoryMgtList2 = wh2.getInventoryDetailsList2();
        model.addAttribute("inventoryMgtList", inventoryMgtList);
        model.addAttribute("inventoryMgtList2", inventoryMgtList2);
        return "whInventory/viewInventory";
    }
    
    //test utk fg79cj
    @RequestMapping(value = "/dummy/{dum}", method = {RequestMethod.GET, RequestMethod.POST})
    public String email(
            Model model,
            HttpServletRequest request,
            Locale locale,
            RedirectAttributes redirectAttrs,
            @ModelAttribute UserSession userSession,
            @PathVariable("dum") String dum,
            @RequestParam(required = false) String refId
    ) throws IOException {
        LOGGER.info("inputttt : ___________ " + dum);
        tempDUMMY = dum;
        LOGGER.info("refId : ___________ " + refId);
        return "redirect:/wh/whInventory/";
    }
}
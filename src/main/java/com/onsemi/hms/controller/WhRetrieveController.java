package com.onsemi.hms.controller;

import com.onsemi.hms.dao.InventoryMgtDAO;
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
import com.onsemi.hms.model.WhInventoryMgt;
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
    private static final String HEADER = "retrieve_id,material_pass_no,material_pass_expiry,equipment_type,equipment_id,pcb_A,qty_qualA,pcb_B,qty_qualB,pcb_C,qty_qualC,pcb_control,qty_control,total_quantity,requested_by,requested_date,remarks,date_verify,receival_time,inventory_date,inventory_rack,inventory_shelf,inventory_by,status";

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
        model.addAttribute("pageTitle", "Hardware for Shipment from Rel Lab");
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
        if (whRetrieve.getStatus().equals("Verification Pass") || whRetrieve.getStatus().equals("Inventory Invalid")) {
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
            @RequestParam(required = false) String requestedEmail,
            @RequestParam(required = false) String requestedBy,
            @RequestParam(required = false) String equipmentId,
            @RequestParam(required = false) String equipmentType,
            @RequestParam(required = false) String materialPassExpiry,
            @RequestParam(required = false) String barcodeVerify,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String flag,
            @RequestParam(required = false) int tempCount
    ) {
        LOGGER.info("requestedEmail : " + requestedEmail);
        WhRetrieveDAO wh = new WhRetrieveDAO();
        WhRetrieve w = wh.getWhRet(refId);
        LOGGER.info("w.getBarcodeVerify() : " + w.getBarcodeVerify());
        
        if (w.getBarcodeVerify() != null) {
            LOGGER.info("data da ade");
        } else {
            LOGGER.info("data xde");
//            EmailSender emailSender = new EmailSender();
//            emailSender.htmlEmail2(
//                    servletContext,
//                    requestedBy, //user name
//                    requestedEmail, //to
//                    "Status for Hardware Shipment to HIMS SF", //subject
//                    "The hardware " + equipmentId + " (" + equipmentType + "), with material pass number: " + materialPassNo + " has been safely arrived in HIMS SF." //msg
//            );
        }

        WhRetrieve whRetrieve = new WhRetrieve();
        whRetrieve.setRefId(refId);
        whRetrieve.setBarcodeVerify(barcodeVerify);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        whRetrieve.setDateVerify(dateFormat.format(date));
        whRetrieve.setUserVerify(userSession.getFullname());
//        String barcodeVerified = whRetrieve.getBarcodeVerify();
        whRetrieve.setMaterialPassNo(materialPassNo);
        LOGGER.info("barcodeVerify : " + barcodeVerify);
        boolean cp = false;
        if (materialPassNo.equals(barcodeVerify)) {
            whRetrieve.setStatus("Verification Pass");
            whRetrieve.setFlag("0");
            cp = true;
            LOGGER.info("Verification Pass");
            LogModule logModule = new LogModule();
            LogModuleDAO logModuleDAO = new LogModuleDAO();
            logModule.setModuleId(w.getId());
            logModule.setReferenceId(refId);
            logModule.setModuleName("hms_wh_retrieval_list");
            logModule.setStatus("Hardware Arrival");
            QueryResult queryResult2 = logModuleDAO.insertLog(logModule);
        } else {
            whRetrieve.setStatus("Verification Fail");
            whRetrieve.setFlag("0");
            cp = false;
            LOGGER.info("Verification Fail");
        }
        LOGGER.info("tempCount before : " + tempCount);
        tempCount = tempCount + 1;
        whRetrieve.setTempCount(Integer.toString(tempCount));
        LOGGER.info("tempCount after : " + tempCount);
        WhRetrieveDAO whRetrieveDAO = new WhRetrieveDAO();
        QueryResult queryResult = whRetrieveDAO.updateWhRetrieveVerification(whRetrieve);

        if(cp == true) {
            WhRetrieveDAO whRetrieve1DAO = new WhRetrieveDAO();
            WhRetrieve wr = new WhRetrieve();
            wr.setArrivalReceivedDate(whRetrieve.getDateVerify());
            wr.setRefId(refId);
            LOGGER.info("whRetrieve.getArrivalReceivedDate() : " + whRetrieve.getDateVerify());
            QueryResult queryResultA = whRetrieve1DAO.updateWhRetrieveReceivalTime(wr);
        }
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
            @RequestParam(required = false) String tempRack,
            @RequestParam(required = false) String tempShelf,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String flag
    ) {
        tempRack = tempRack.toUpperCase();
        tempShelf = tempShelf.toUpperCase();
        
        String checkValidity = "false";
        
        WhRetrieve whRetrieve = new WhRetrieve();
        whRetrieve.setRefId(refId);
        whRetrieve.setMaterialPassNo(materialPassNo);
        whRetrieve.setTempRack(tempRack);
        whRetrieve.setTempShelf(tempShelf);

        boolean checkLength = false;
        if (tempRack.length() == 6 && tempShelf.length() == 10) {
            checkLength = true;
        }

        boolean checkRack = false;
        boolean ck = false;
        boolean cp = false;

        
        InventoryMgtDAO inventoryMgtDao = new InventoryMgtDAO();
        int countShelf = inventoryMgtDao.getCountShelf(tempShelf);
        
        InventoryMgtDAO inventoryMgtDao2 = new InventoryMgtDAO();
        int countRack = inventoryMgtDao2.getCountRack(tempRack);
        
        boolean checkShelf = false;
        if(countRack != 0 && countShelf != 0) {
            WhInventoryMgt imgt = new WhInventoryMgt();
            InventoryMgtDAO inventoryMgtDAO3 = new InventoryMgtDAO();
            WhInventoryMgt whInventoryMgt = inventoryMgtDAO3.getInventoryDetails(tempShelf);
            imgt.setRackId(whInventoryMgt.getRackId());
            imgt.setShelfId(whInventoryMgt.getShelfId());
            imgt.setHardwareId(equipmentId);
            imgt.setMaterialPassNo(materialPassNo);
            if(whInventoryMgt.getHardwareId().equals("Empty")) {
                checkShelf = true;
                LOGGER.info("Shelf empty. Enter.");
            }
            
            if(checkShelf == true) {
                if (checkLength == true) {
                    LOGGER.info("************************************ " + tempRack + " vs " + tempShelf.substring(0, 6) + " ************************************");
                    if (equipmentType.equals("PCB")) {
                        if (tempRack.substring(0, 4).equals("S-PC")) {
                            checkRack = true;
                        }
                    } else if (equipmentType.equals("Tray")) {
                        if (tempRack.substring(0, 4).equals("S-TJ") || tempRack.substring(0, 4).equals("S-TR")) {
                            checkRack = true;
                        }
                    } else if (equipmentType.equals("Stencil")) {
                        if (tempRack.substring(0, 4).equals("S-ST")) {
                            checkRack = true;
                        }
                    } else if (equipmentType.equals("Motherboard")) {
                        if (tempRack.substring(0, 4).equals("S-SY") || tempRack.substring(0, 4).equals("S-AC") || tempRack.substring(0, 4).equals("S-WF") || tempRack.substring(0, 4).equals("S-IO")
                                || tempRack.substring(0, 4).equals("S-BB") || tempRack.substring(0, 4).equals("S-HA") || tempRack.substring(0, 4).equals("S-PT")) {
                            checkRack = true;
                        }
                    }

                    if (checkRack == true && tempShelf.substring(0, 6).equals(tempRack)) {
                        ck = true;
                    }
                }

                if (status.equals("Verification Pass") || status.equals("Inventory Invalid")) {
                    if (ck == true) {
                        whRetrieve.setStatus("Move to Inventory");
                        whRetrieve.setFlag("1");
                        cp = true;
                        LOGGER.info("Inventory Pass");
                        InventoryMgtDAO imdao = new  InventoryMgtDAO();
                        QueryResult queryMgt = imdao.updateInventoryDetails(imgt);
                    } else {
                        whRetrieve.setStatus("Inventory Invalid");
                        whRetrieve.setFlag(flag);
                        cp = false;
                        LOGGER.info("Inventory Invalid");
                    }
                } else {
                    whRetrieve.setStatus(status);
                    whRetrieve.setFlag(flag);
                    cp = false;
                    LOGGER.info("Inventory Not Stated");
                }
                WhRetrieveDAO whRetrieveDAO = new WhRetrieveDAO();
                QueryResult queryResult = whRetrieveDAO.updateWhRetrieveForInventory(whRetrieve);
            }
        } else {
            LOGGER.info("Rack or shelf invalid. Exit.");
            whRetrieve.setStatus("Inventory Invalid");
            whRetrieve.setFlag(flag);
            WhRetrieveDAO whRetrieveDAO = new WhRetrieveDAO();
            QueryResult queryResult = whRetrieveDAO.updateWhRetrieveForInventory(whRetrieve);
        }
        
        
        String url;
        if (ck == true && cp == true) {
            WhRetrieveDAO whRetrieveDAO2 = new WhRetrieveDAO();
            WhRetrieve query2 = whRetrieveDAO2.getWhRetrieve(refId);
            if (query2.getFlag().equals("1")) {
                LogModule logModule2 = new LogModule();
                LogModuleDAO logModuleDAO2 = new LogModuleDAO();
                logModule2.setModuleId(query2.getId());
                logModule2.setReferenceId(refId);
                logModule2.setModuleName("hms_wh_retrieval_list");
                logModule2.setStatus(query2.getStatus());
                logModule2.setVerifiedBy(userSession.getFullname());
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                logModule2.setVerifiedDate(dateFormat.format(date));
                QueryResult queryResult2 = logModuleDAO2.insertLogForVerification(logModule2);
            }
            
            if (whRetrieve.getFlag().equals("1")) {
                //save id to table wh_inventory_list
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();

                WhInventory whInventory = new WhInventory();
                whInventory.setRefId(refId);
                whInventory.setMaterialPassNo(materialPassNo);
                whInventory.setStatus("Available in Inventory");
                whInventory.setInventoryBy(userSession.getFullname());
                whInventory.setInventoryRack(tempRack);
                whInventory.setInventoryShelf(tempShelf);
                whInventory.setFlag("0");
                WhInventoryDAO whInventoryDAO = new WhInventoryDAO();
                int count = whInventoryDAO.getCountExistingData(whInventory.getRefId());

                if (count == 0) {
                    checkValidity = "true";
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
                    if (queryResult1.getResult() != 0) {
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
                                while (data != null) {
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

                                    if (split[0].equals(refId)) {
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

                                if (check == false) {
                                    //New Line after the header
                                    fileWriter.append(LINE_SEPARATOR);
                                    WhInventoryDAO whdao = new WhInventoryDAO();
                                    WhInventory wh = whdao.getWhInventoryMergeWithRetrieve(refId);

                                    String pcbA = wh.getPcbA(), pcbB = wh.getPcbB(), pcbC = wh.getPcbC(), pcbControl = wh.getPcbControl();
                                    if (!wh.getEquipmentType().equals("PCB")) {
                                        if (wh.getPcbA() == null || wh.getPcbA().equals("null")) {
                                            pcbA = SpmlUtil.nullToEmptyString(wh.getPcbA());
                                        }
                                        if (wh.getPcbB() == null || wh.getPcbB().equals("null")) {
                                            pcbB = SpmlUtil.nullToEmptyString(wh.getPcbB());
                                        }
                                        if (wh.getPcbC() == null || wh.getPcbC().equals("null")) {
                                            pcbC = SpmlUtil.nullToEmptyString(wh.getPcbC());
                                        }
                                        if (wh.getPcbControl() == null || wh.getPcbControl().equals("null")) {
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
                                    fileWriter.append(wh.getArrivalReceivedDate());
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
                                if (!wh.getEquipmentType().equals("PCB")) {
                                    if (wh.getPcbA() == null || wh.getPcbA().equals("null")) {
                                        pcbA = SpmlUtil.nullToEmptyString(wh.getPcbA());
                                    }
                                    if (wh.getPcbB() == null || wh.getPcbB().equals("null")) {
                                        pcbB = SpmlUtil.nullToEmptyString(wh.getPcbB());
                                    }
                                    if (wh.getPcbC() == null || wh.getPcbC().equals("null")) {
                                        pcbC = SpmlUtil.nullToEmptyString(wh.getPcbC());
                                    }
                                    if (wh.getPcbControl() == null || wh.getPcbControl().equals("null")) {
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
                                fileWriter.append(wh.getArrivalReceivedDate());
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
                        LOGGER.info("******************* EMAIL CDARS *******************");

                        WhInventoryDAO whidao = new WhInventoryDAO();
                        WhInventory whi = whidao.getWhInventoryMergeWithRetrieve(refId);
                        
                        String[] to = {"cdarsrel@gmail.com"};
//                        String[] to = {"cdarsreltest@gmail.com"};
                        EmailSender emailSender = new EmailSender();
                        emailSender.htmlEmailWithAttachmentTest2(
                            servletContext,
                            "CDARS", //user name
                            to, //to
                            "Status for Hardware Inventory from HIMS SF", //subject
                            "Verification and inventory for Hardware has been made." //msg
                        );
                        
                        LOGGER.info("******************* EMAIL REQUESTOR *******************");

                        WhInventoryDAO whidao2 = new WhInventoryDAO();
                        WhInventory whi2 = whidao2.getWhInventoryMergeWithRetrieve(refId);
                        
                        EmailSender emailSender2 = new EmailSender();
                        emailSender2.htmlEmail2(
                                servletContext,
                                whi2.getRequestedBy(), //user name
                                whi2.getRequestedEmail(), //to
//                                "muhdfaizal@onsemi.com", //to
                                "Status for Hardware Inventory from HIMS SF", //subject
                                "Verification and inventory has been made. The new inventory for Hardware ID: " + whi2.getEquipmentId() + " with material pass no. : " + whi.getMaterialPassNo() 
                                + " are at rack: " + whi2.getInventoryRack() + ", shelf: " + whi2.getInventoryShelf() + "." //msg
                        );
                        System.out.println("######################### END EMAIL PROCESS ########################### ");
                        
                        redirectAttrs.addFlashAttribute("success", messageSource.getMessage("general.label.update.success3", args, locale));
                    }
                } else {
                    LOGGER.info("data adeeeeee");
                }
            }
            
            url = "redirect:/wh/whInventory/";
        } else {
            WhRetrieveDAO whRetrieveDAO3 = new WhRetrieveDAO();
            WhRetrieve query3 = whRetrieveDAO3.getWhRetrieve(refId);
            if (query3.getFlag().equals("0")) {
                LogModule logModule3 = new LogModule();
                LogModuleDAO logModuleDAO3 = new LogModuleDAO();
                logModule3.setModuleId(query3.getId());
                logModule3.setReferenceId(refId);
                logModule3.setModuleName("hms_wh_retrieval_list");
                logModule3.setStatus(query3.getStatus());
                logModule3.setVerifiedBy(userSession.getFullname());
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                logModule3.setVerifiedDate(dateFormat.format(date));
                QueryResult queryResult3 = logModuleDAO3.insertLogForVerification(logModule3);
            }
            url = "redirect:/wh/whRetrieve/verify/" + refId;
        }
        LOGGER.info("checkValidity: " + checkValidity);
        model.addAttribute("checkValidity", checkValidity);
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
        model.addAttribute("pageTitle", "Hardware for Shipment from Rel Lab History");
        LOGGER.info("Masuk view 2........");
        return "pdf/viewer";
    }

    @RequestMapping(value = "/viewWhRetrieveLogPdf/{whRetrieveId}", method = RequestMethod.GET)
    public ModelAndView viewWhRetrieveHistPdf(
            Model model,
            @PathVariable("whRetrieveId") String whRetrieveId
    ) {
        WhRetrieveDAO whRetrieveDAO = new WhRetrieveDAO();
        List<WhRetrieveLog> whHistoryList = whRetrieveDAO.getWhRetLog(whRetrieveId);
        return new ModelAndView("whRetrieveLogPdf", "whRetrieveLog", whHistoryList);
    }

    @RequestMapping(value = "/error/{whRetrieveId}", method = {RequestMethod.GET, RequestMethod.POST})
    public String error(
            Model model,
            Locale locale,
            HttpServletRequest request,
            RedirectAttributes redirectAttrs,
            @ModelAttribute UserSession userSession,
            @PathVariable("whRetrieveId") String whRetrieveId
    ) throws UnsupportedEncodingException {
        //send email
        LOGGER.info("send email to warehouse");

//        String url = "";
        WhRetrieveDAO whRetrieveDAO = new WhRetrieveDAO();
        int count = whRetrieveDAO.getCountExistingData(whRetrieveId);
        if (count != 0) {
            whRetrieveDAO = new WhRetrieveDAO();
            WhRetrieve query = whRetrieveDAO.getWhRetrieve(whRetrieveId);

            EmailSender emailSender = new EmailSender();
            emailSender.htmlEmail2(
                    servletContext,
                    query.getRequestedBy(), //user name
                    query.getRequestedEmail(), //to
//                    "muhdfaizal@onsemi.com",
                    "Error in Hardware Retrieval Verification in HIMS SF", //subject
                    "Barcode Verification for hardware " + query.getEquipmentId() + " (" + query.getEquipmentType() + "), with material pass number: " + query.getMaterialPassNo() + " is INVALID. " +
                    "\nPlease identify the problem that occur." //msg
            );
//            url = "redirect:/wh/whRetrieve/";
            args = new String[1];
            args[0] = query.getMaterialPassNo();
            redirectAttrs.addFlashAttribute("success", messageSource.getMessage("general.label.update.success6", args, locale));
            
            LogModule logModule3 = new LogModule();
            LogModuleDAO logModuleDAO3 = new LogModuleDAO();
            logModule3.setModuleId(query.getId());
            logModule3.setReferenceId(query.getRefId());
            logModule3.setModuleName("hms_wh_retrieval_list");
            logModule3.setStatus("Sent Email to Requestor");
            QueryResult queryResult3 = logModuleDAO3.insertLog(logModule3);
            System.out.println("Email has been sent.");
        }
//        else {
//            url = "redirect:/wh/whRetrieve/verify/" + whRetrieveId;
//        }
        
        return "redirect:/wh/whRetrieve/verify/" + whRetrieveId;
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
        WhRetrieveDAO wh = new WhRetrieveDAO();
        List<WhRetrieve> retrieveQueryList = wh.getQuery(query);
        model.addAttribute("retrieveQueryList", retrieveQueryList);
        WhRetrieveDAO wr = new WhRetrieveDAO();
        List<WhRetrieve> hardwareIdList = wr.getHardwareId();
        model.addAttribute("hardwareIdList", hardwareIdList);
        WhRetrieveDAO wr2 = new WhRetrieveDAO();
        List<WhRetrieve> requestedByList = wr2.getRequestedBy();
        model.addAttribute("requestedByList", requestedByList);
        WhRetrieveDAO wr3 = new WhRetrieveDAO();
        List<WhRetrieve> statusList = wr3.getStatus();
        model.addAttribute("statusList", statusList);
        WhRetrieveDAO wr4 = new WhRetrieveDAO();
        List<WhRetrieve> hardwareTypeList = wr4.getHardwareType();
        model.addAttribute("hardwareTypeList", hardwareTypeList);
        
        return "whRetrieve/query";
    }
}
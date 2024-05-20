package com.onsemi.hms.controller;

import com.onsemi.hms.dao.InventoryMgtDAO;
import com.onsemi.hms.tools.CSV;
import com.onsemi.hms.dao.LogModuleDAO;
import com.onsemi.hms.dao.ParameterDetailsDAO;
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
@RequestMapping(value = "/wh/whInventory")
@SessionAttributes({"userSession"})
public class WhInventoryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WhInventoryController.class);
    String[] args = {};
    private static final String COMMA_DELIMITER = ",";
    private static final String LINE_SEPARATOR = "\n";
    //File header
//    private static final String HEADER = "retrieve_id,material_pass_no,material_pass_expiry,equipment_type,equipment_id,pcb_A,qty_qualA,pcb_B,qty_qualB,pcb_C,qty_qualC,pcb_control,qty_control,total_quantity,requested_by,requested_date,remarks,date_verify,receival_time,inventory_date,inventory_rack,inventory_shelf,inventory_by,status";
    private static final String HEADER = "retrieve_id,box_no,gts_no,equipment_type,equipment_id,pcb_A,qty_qualA,pcb_B,qty_qualB,pcb_C,qty_qualC,pcb_control,qty_control,total_quantity,requested_by,requested_date,remarks,date_verify,receival_time,inventory_date,inventory_rack,inventory_shelf,inventory_by,status";

    @Autowired
    private MessageSource messageSource;

    @Autowired
    ServletContext servletContext;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String whInventory(
            Model model,
            @ModelAttribute UserSession userSession
    ) {
        WhInventoryDAO whInventoryDAO = new WhInventoryDAO();
        List<WhInventory> whInventoryList = whInventoryDAO.getWhInventoryListMergeRetrieve();
        String groupId = userSession.getGroup();
//        LOGGER.info("groupId" + groupId);
        model.addAttribute("userSession", userSession);
        model.addAttribute("whInventoryList", whInventoryList);
        model.addAttribute("groupId", groupId);
        return "whInventory/whInventory";
    }

//    @RequestMapping(value = "/change", method = RequestMethod.GET)
//    public String change(
//            Model model,
//            @ModelAttribute UserSession userSession
//    ) {
//        WhInventoryDAO whInventoryDAO = new WhInventoryDAO();
//        List<WhInventory> whInventoryList = whInventoryDAO.getWhInventoryListMergeRetrieveForMpToBoxNo();
//        String groupId = userSession.getGroup();
////        LOGGER.info("groupId" + groupId);
//        model.addAttribute("userSession", userSession);
//        model.addAttribute("whInventoryList", whInventoryList);
//        model.addAttribute("groupId", groupId);
//        return "whInventory/change";
//    }
//
//    @RequestMapping(value = "/editChange/{whInventoryId}", method = RequestMethod.GET)
//    public String editChange(
//            Model model,
//            @PathVariable("whInventoryId") String whInventoryId
//    ) {
//        WhInventoryDAO whInventoryDAO = new WhInventoryDAO();
//        WhInventory whInventory = whInventoryDAO.getWhInventoryMergeWithRetrievePdf(whInventoryId);
//        String type = whInventory.getEquipmentType();
//        if ("Motherboard".equals(type)) {
//            String IdLabel = "Motherboard ID";
//            model.addAttribute("IdLabel", IdLabel);
//        } else if ("Stencil".equals(type)) {
//            String IdLabel = "Stencil ID";
//            model.addAttribute("IdLabel", IdLabel);
//        } else if ("Tray".equals(type)) {
//            String IdLabel = "Tray Type";
//            model.addAttribute("IdLabel", IdLabel);
//        } else if ("PCB".equals(type)) {
//            String IdLabel = "PCB Name";
//            model.addAttribute("IdLabel", IdLabel);
//        } else {
//            String IdLabel = "Hardware ID";
//            model.addAttribute("IdLabel", IdLabel);
//        }
//        model.addAttribute("whInventory", whInventory);
//        return "whInventory/editChange";
//    }
//
//    @RequestMapping(value = "/updateChange", method = RequestMethod.POST)
//    public String updateChange(
//            Model model,
//            Locale locale,
//            HttpServletRequest request,
//            RedirectAttributes redirectAttrs,
//            @ModelAttribute UserSession userSession,
//            @RequestParam(required = false) String refId
//    ) {
//
//        //change mpToBox from 0 to 1
//        WhInventory in = new WhInventory();
//        in.setRefId(refId);
//        in.setMpToBox("1");
//
//        WhInventoryDAO inv = new WhInventoryDAO();
//        QueryResult q = inv.updateWhInventoryMpToBox(in);
//
//        return "redirect:/wh/whInventory/editChange/" + refId;
//    }
//
//    @RequestMapping(value = "/viewWhBarcodeStickerPdf/{refId}", method = RequestMethod.GET)
//    public ModelAndView viewWhBarcodeStickerPdf(
//            Model model,
//            @PathVariable("refId") String refId
//    ) {
//        WhInventoryDAO whInventoryDAO = new WhInventoryDAO();
//        WhInventory whInventory = whInventoryDAO.getWhInventoryMergeWithRetrievePdf(refId);
//        return new ModelAndView("whBarcodeStickerPdf", "whInventory", whInventory);
//    }

    @RequestMapping(value = "/edit/{whInventoryId}", method = RequestMethod.GET)
    public String edit(
            Model model,
            @PathVariable("whInventoryId") String whInventoryId
    ) {
        WhInventoryDAO whInventoryDAO = new WhInventoryDAO();
        WhInventory whInventory = whInventoryDAO.getWhInventoryMergeWithRetrievePdf(whInventoryId);
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
            @RequestParam(required = false) String boxNo,
            @RequestParam(required = false) String materialPassNo,
            @RequestParam(required = false) String inventoryRack,
            @RequestParam(required = false) String inventoryShelf,
            @RequestParam(required = false) String equipmentType,
            @RequestParam(required = false) String equipmentId
    ) {
        
        LOGGER.info("LOGGER for SINI MASUK DEKAT UPDATE FUNCTION : ");
        inventoryRack = inventoryRack.toUpperCase();
        inventoryShelf = inventoryShelf.toUpperCase();

        WhInventory whInventory = new WhInventory();

        whInventory.setRefId(refId); //ref
        LOGGER.info(refId);
        whInventory.setMaterialPassNo(materialPassNo); //args
        whInventory.setBoxNo(boxNo); //args
//        LOGGER.info(materialPassNo);

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
            } else if (equipmentType.contains("Load Card") || equipmentType.contains("Program Card")) {
                if (inventoryRack.substring(0, 4).equals("S-LP")) {
                    checkRack = true;
                }
            } else if (equipmentType.contains("BIB Parts")) {
                if (inventoryRack.substring(0, 4).equals("S-BP")) {
                    checkRack = true;
                }
            } else if (equipmentType.substring(0, 13).contains("EQP_SPAREPART") || equipmentType.substring(0, 13).contains("ATE_SPAREPART")) {
                if (inventoryRack.substring(0, 4).equals("S-SP")) {
                    checkRack = true;
                } else if (inventoryRack.substring(0, 4).equals("S-FL") || inventoryRack.substring(0, 4).equals("S-HP") || inventoryRack.substring(0, 4).equals("S-HH")
                        || inventoryRack.substring(0, 4).equals("S-GH") || inventoryRack.substring(0, 4).equals("S-GR") || inventoryRack.substring(0, 4).equals("S-TI")) {
                    checkRack = true;
                } else if (equipmentId.contains("ACS BOX") || equipmentId.contains("IOL BOX") || equipmentId.contains("BLUE M BOX") || equipmentId.contains("Stencil Box")
                        || equipmentId.contains("WAKEFIELD BOX") || equipmentId.contains("Silica Gel") || equipmentId.contains("Plastic Trip Ticket") || equipmentId.contains("Loadcard Box")) {
                    checkRack = true;
                }
            }
            if (checkRack == true && inventoryShelf.substring(0, 6).equals(inventoryRack)) {
                ck = true;
            }
        }
        
        InventoryMgtDAO inventoryMgtDao = new InventoryMgtDAO();
        int countShelf = inventoryMgtDao.getCountShelf(inventoryShelf);

        InventoryMgtDAO inventoryMgtDao2 = new InventoryMgtDAO();
        int countRack = inventoryMgtDao2.getCountRack(inventoryRack);

        boolean checkRackShelf = false;
        if (ck == true) {
            if (countShelf != 0 && countRack != 0) {
                checkRackShelf = true;
            }
        }
        boolean checkShelf = false;

        WhInventoryDAO whInventoryDAO = new WhInventoryDAO();
        QueryResult queryResult = null;
        String url = "";
        if (checkRackShelf == true) {
            LOGGER.info("SINI CHECK RACK LEPAS");
            InventoryMgtDAO inventoryMgtDAO3 = new InventoryMgtDAO();
            WhInventoryMgt whInventoryMgt = inventoryMgtDAO3.getInventoryDetails(inventoryShelf);
            WhInventoryMgt imgt = new WhInventoryMgt();
            imgt.setRackId(whInventoryMgt.getRackId());
            imgt.setShelfId(whInventoryMgt.getShelfId());
            imgt.setHardwareId(equipmentId);
            imgt.setMaterialPassNo(materialPassNo);
            imgt.setBoxNo(boxNo);

            WhInventoryDAO invdao = new WhInventoryDAO();
            WhInventory inv = invdao.getWhInventory(refId);
            WhInventoryMgt imgt2 = new WhInventoryMgt();
            imgt2.setRackId(inv.getInventoryRack());
            imgt2.setShelfId(inv.getInventoryShelf());
            imgt2.setHardwareId("Empty");
            imgt2.setMaterialPassNo("Empty");
            imgt2.setBoxNo("Empty");

            if (whInventoryMgt.getHardwareId().equals("Empty")) {
                checkShelf = true;
                LOGGER.info("Shelf empty. Enter.");
            }

            if (checkShelf == true) {
                whInventory.setInventoryRack(inventoryRack); //update
                whInventory.setInventoryShelf(inventoryShelf); //update
                whInventory.setInventoryBy(userSession.getFullname()); //update
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                whInventory.setInventoryDate(dateFormat.format(date)); //update
                queryResult = whInventoryDAO.updateWhInventory(whInventory);

                InventoryMgtDAO imdao = new InventoryMgtDAO();
                QueryResult queryMgt = imdao.updateInventoryDetails(imgt);

                imdao = new InventoryMgtDAO();
                QueryResult queryMgt2 = imdao.updateInventoryRevert(imgt2);

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
                
                ParameterDetailsDAO pmdao001 = new ParameterDetailsDAO();
                String location = pmdao001.getURLPath("sf_path");
                ParameterDetailsDAO pmdao002 = new ParameterDetailsDAO();
                String file_inventory = pmdao002.getURLPath("sf_inventory");

                args = new String[1];
                args[0] = materialPassNo;
                if (queryResult.getResult() == 1) {
                    String username = System.getProperty("user.name");
                    String targetLocation = location + file_inventory;
                    //SEND EMAIL
//                    File file = new File("D:\\HIMS_CSV\\SF\\hms_inventory.csv");
                    File file = new File(targetLocation);
                    if (file.exists()) {
                        LOGGER.info("dh ada header");
                        FileWriter fileWriter = null;
                        FileReader fileReader = null;

                        boolean check = false;
                        try {
//                            fileWriter = new FileWriter("D:\\HIMS_CSV\\SF\\hms_inventory.csv", true);
//                            fileReader = new FileReader("D:\\HIMS_CSV\\SF\\hms_inventory.csv");
//                            String targetLocation = "D:\\HIMS_CSV\\SF\\hms_inventory.csv";
                            fileWriter = new FileWriter(targetLocation, true);
                            fileReader = new FileReader(targetLocation);
                            LOGGER.info("DEKAT SINI KITA WRITE EXISTING DATA AND READ THE DATA BACK FOR COMPARISON");

                            BufferedReader bufferedReader = new BufferedReader(fileReader);
                            String data = bufferedReader.readLine();
                            StringBuilder buff = new StringBuilder();

                            int row = 0;

                            while (data != null) {
                                LOGGER.info("start reading file..........");
                                buff.append(data).append(System.getProperty("line.separator"));
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

                                if (split[0].equals(refId)) {
                                    CSV csv = new CSV();
                                    csv.open(new File(targetLocation));
                                    csv.put(19, row, whInventory.getInventoryDate());
                                    csv.put(20, row, whInventory.getInventoryRack());
                                    csv.put(21, row, whInventory.getInventoryShelf());
                                    csv.put(22, row, whInventory.getInventoryBy());
                                    csv.save(new File(targetLocation));

                                    check = true;
                                } else {
                                    LOGGER.info("refId not found........" + data);
                                }
                                data = bufferedReader.readLine();
                                row++;
                            }
                            bufferedReader.close();
                            fileReader.close();
                        } catch (Exception ee) {
                            LOGGER.info("Error 1 occured while append the fileWriter");
                        } finally {
                            try {
                                fileWriter.close();
                            } catch (IOException ie) {
                                LOGGER.info("Error 2 occured while closing the fileWriter");
                            }
                        }

                        if (check == false) {
                            try {
                                LOGGER.info("DEKAT SINI KITA NK WRITE NEW DATA FOR INVENTORY");
//                                fileWriter = new FileWriter("D:\\HIMS_CSV\\SF\\hms_inventory.csv", true);
                                fileWriter = new FileWriter(targetLocation, true);
                                //New Line after the header
                                fileWriter.append(LINE_SEPARATOR);
                                WhInventoryDAO whdao = new WhInventoryDAO();
                                WhInventory wh = whdao.getWhInventoryMergeWithRetrieve(refId);

                                String pcbA = wh.getPcbA(), pcbB = wh.getPcbB(), pcbC = wh.getPcbC(), pcbControl = wh.getPcbControl();
                                String qtyPcbA = wh.getQtyQualA(), qtyPcbB = wh.getQtyQualB(), qtyPcbC = wh.getQtyQualC(), qtyPcbControl = wh.getQtyControl();

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

                                if (wh.getPairingType() != null) {
                                    if (wh.getPairingType().equals("PAIR")) {
                                        pcbA = wh.getLoadCardId();
                                        qtyPcbA = wh.getLoadCardQty();
                                        pcbB = wh.getProgCardId();
                                        qtyPcbB = wh.getProgCardQty();
                                    } else if (wh.getPairingType().equals("SINGLE")) {
                                        if (wh.getEquipmentType().equals("Load Card")) {
                                            pcbA = wh.getLoadCardId();
                                            qtyPcbA = wh.getLoadCardQty();
                                            pcbB = SpmlUtil.nullToEmptyString(wh.getPcbB());
                                            qtyPcbB = "0";
                                        } else if (wh.getEquipmentType().equals("Program Card")) {
                                            pcbA = SpmlUtil.nullToEmptyString(wh.getPcbA());
                                            qtyPcbA = wh.getLoadCardQty();
                                            pcbB = wh.getProgCardId();
                                            qtyPcbB = "0";
                                        }
                                    }
                                }
                                fileWriter.append(refId);
                                fileWriter.append(COMMA_DELIMITER);
//                                fileWriter.append(wh.getMaterialPassNo());
//                                fileWriter.append(COMMA_DELIMITER);
//                                fileWriter.append(wh.getMaterialPassExpiry());
                                fileWriter.append(wh.getBoxNo());
                                fileWriter.append(COMMA_DELIMITER);
                                fileWriter.append(wh.getGtsNo());
                                fileWriter.append(COMMA_DELIMITER);
                                fileWriter.append(wh.getEquipmentType());
                                fileWriter.append(COMMA_DELIMITER);
                                fileWriter.append(wh.getEquipmentId());
                                fileWriter.append(COMMA_DELIMITER);
                                fileWriter.append(pcbA);
                                fileWriter.append(COMMA_DELIMITER);
                                fileWriter.append(qtyPcbA);
                                fileWriter.append(COMMA_DELIMITER);
                                fileWriter.append(pcbB);
                                fileWriter.append(COMMA_DELIMITER);
                                fileWriter.append(qtyPcbB);
                                fileWriter.append(COMMA_DELIMITER);
                                fileWriter.append(pcbC);
                                fileWriter.append(COMMA_DELIMITER);
                                fileWriter.append(qtyPcbC);
                                fileWriter.append(COMMA_DELIMITER);
                                fileWriter.append(pcbControl);
                                fileWriter.append(COMMA_DELIMITER);
                                fileWriter.append(qtyPcbControl);
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
                    } else {
                        FileWriter fileWriter = null;
                        try {
                            LOGGER.info("KAT SINI KITA WRITE NEW INVENTORY DATA CHANGES");
//                            fileWriter = new FileWriter("D:\\HIMS_CSV\\SF\\hms_inventory.csv");
                            fileWriter = new FileWriter(targetLocation);
                            LOGGER.info("no file yet");
                            //Adding the header
                            fileWriter.append(HEADER);

                            //New Line after the header
                            fileWriter.append(LINE_SEPARATOR);
                            WhInventoryDAO whdao = new WhInventoryDAO();
                            WhInventory wh = whdao.getWhInventoryMergeWithRetrieve(refId);

                            String pcbA = wh.getPcbA(), pcbB = wh.getPcbB(), pcbC = wh.getPcbC(), pcbControl = wh.getPcbControl();
                            String qtyPcbA = wh.getQtyQualA(), qtyPcbB = wh.getQtyQualB(), qtyPcbC = wh.getQtyQualC(), qtyPcbControl = wh.getQtyControl();

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

                            if (wh.getPairingType() != null) {
                                if (wh.getPairingType().equals("PAIR")) {
                                    pcbA = wh.getLoadCardId();
                                    qtyPcbA = wh.getLoadCardQty();
                                    pcbB = wh.getProgCardId();
                                    qtyPcbB = wh.getProgCardQty();
                                } else if (wh.getPairingType().equals("SINGLE")) {
                                    if (wh.getEquipmentType().equals("Load Card")) {
                                        pcbA = wh.getLoadCardId();
                                        qtyPcbA = wh.getLoadCardQty();
                                        pcbB = SpmlUtil.nullToEmptyString(wh.getPcbB());
                                        qtyPcbB = "0";
                                    } else if (wh.getEquipmentType().equals("Program Card")) {
                                        pcbA = SpmlUtil.nullToEmptyString(wh.getPcbA());
                                        qtyPcbA = wh.getLoadCardQty();
                                        pcbB = wh.getProgCardId();
                                        qtyPcbB = "0";
                                    }
                                }
                            }
                            fileWriter.append(refId);
                            fileWriter.append(COMMA_DELIMITER);
//                            fileWriter.append(wh.getMaterialPassNo());
//                            fileWriter.append(COMMA_DELIMITER);
//                            fileWriter.append(wh.getMaterialPassExpiry());
                            fileWriter.append(wh.getBoxNo());
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(wh.getGtsNo());
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(wh.getEquipmentType());
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(wh.getEquipmentId());
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(pcbA);
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(qtyPcbA);
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(pcbB);
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(qtyPcbB);
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(pcbC);
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(qtyPcbC);
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(pcbControl);
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(qtyPcbControl);
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

                    /*to get hostname*/
                    InetAddress ip;
                    String hostName = "";
                    try {
                        ip = InetAddress.getLocalHost();
                        hostName = ip.getHostName();
                    } catch (UnknownHostException ex) {
                        java.util.logging.Logger.getLogger(WhRetrieveController.class.getName()).log(Level.SEVERE, null, ex);
                    }

//                    String[] to = {"cdarsreltest@gmail.com"};
//                    String[] to = {"cdarsrel@gmail.com"};
                    String[] to = {"hims@onsemi.com"};
                    EmailSender emailSender = new EmailSender();
                    emailSender.htmlEmailWithAttachmentTest2(
                            servletContext,
                            "CDARS", //user name
                            to, //to
                            "Status for Hardware Inventory from HIMS SF", //subject
                            "Verification and inventory for Hardware has been made." //msg
                    );

                    redirectAttrs.addFlashAttribute("success", messageSource.getMessage("general.label.update.success", args, locale));
                    url = "redirect:/wh/whInventory/";
                } else {
                    LOGGER.info("----------------------" + queryResult.getResult());
                    redirectAttrs.addFlashAttribute("error", messageSource.getMessage("general.label.update.error", args, locale));
                    url = "redirect:/wh/whInventory/edit/" + refId;
                }
            }
        } else {
            LOGGER.info("Inventory Invalid");
            redirectAttrs.addFlashAttribute("error", messageSource.getMessage("general.label.update.error1", args, locale));
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
//        LOGGER.info("Masuk view 1........");
        String pdfUrl = URLEncoder.encode(request.getContextPath() + "/wh/whInventory/viewWhInventoryPdf/" + whInventoryId, "UTF-8");
        String backUrl = servletContext.getContextPath() + "/wh/whInventory";
        model.addAttribute("pdfUrl", pdfUrl);
        model.addAttribute("backUrl", backUrl);
        model.addAttribute("pageTitle", "Hardware in SBN Factory");
//        LOGGER.info("Masuk view 2........");
        return "pdf/viewer";
    }

    @RequestMapping(value = "/viewWhInventoryPdf/{whInventoryId}", method = RequestMethod.GET)
    public ModelAndView viewWhInventoryPdf(
            Model model,
            @PathVariable("whInventoryId") String whInventoryId
    ) {
//        LOGGER.info("Masuk 1........");
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
//        LOGGER.info("Masuk view 1........");
        String pdfUrl = URLEncoder.encode(request.getContextPath() + "/wh/whInventory/viewWhInventoryLogPdf/" + whInventoryId, "UTF-8");
        String backUrl = servletContext.getContextPath() + "/wh/whInventory";
        model.addAttribute("pdfUrl", pdfUrl);
        model.addAttribute("backUrl", backUrl);
        model.addAttribute("pageTitle", "Hardware in SBN Factory History");
//        LOGGER.info("Masuk view 2........");
        return "pdf/viewer";
    }

    @RequestMapping(value = "/viewWhInventoryLogPdf/{whInventoryId}", method = RequestMethod.GET)
    public ModelAndView viewWhInventoryHistPdf(
            Model model,
            @PathVariable("whInventoryId") String whInventoryId
    ) {
        WhInventoryDAO whInventoryDAO = new WhInventoryDAO();
//        LOGGER.info("Masuk 1........");
        List<WhInventoryLog> whHistoryList = whInventoryDAO.getWhInventoryRetLog(whInventoryId);
//        LOGGER.info("Masuk 2........ ");
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
            @RequestParam(required = false) String boxNo,
            @RequestParam(required = false) String gtsNo,
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

        if (materialPassNo != null) {
            if (!materialPassNo.equals("")) {
                count++;
                if (count == 1) {
                    query = " I.material_pass_no = \'" + materialPassNo + "\' ";
                } else if (count > 1) {
                    query = query + " AND I.material_pass_no = \'" + materialPassNo + "\' ";
                }
            }
        }
        if (boxNo != null) {
            if (!boxNo.equals("")) {
                count++;
                if (count == 1) {
                    query = " I.box_no = \'" + boxNo + "\' ";
                } else if (count > 1) {
                    query = query + " AND I.box_no = \'" + boxNo + "\' ";
                }
            }
        }
        if (gtsNo != null) {
            if (!gtsNo.equals("")) {
                count++;
                if (count == 1) {
                    query = " I.gts_no = \'" + gtsNo + "\' ";
                } else if (count > 1) {
                    query = query + " AND I.gts_no = \'" + gtsNo + "\' ";
                }
            }
        }
        if (equipmentId != null) {
            if (!equipmentId.equals("")) {
                count++;
                if (count == 1) {
                    query = " equipment_id = \'" + equipmentId + "\' ";
                } else if (count > 1) {
                    query = query + " AND equipment_id = \'" + equipmentId + "\' ";
                }
            }
        }
        if (materialPassExpiry1 != null && materialPassExpiry2 != null) {
            if (!materialPassExpiry1.equals("") && !materialPassExpiry2.equals("")) {
                count++;
                String materialPassExpiry = " material_pass_expiry BETWEEN CAST(\'" + materialPassExpiry1 + "\' AS DATE) AND CAST(\'" + materialPassExpiry2 + "\' AS DATE) ";
                if (count == 1) {
                    query = materialPassExpiry;
                } else if (count > 1) {
                    query = query + " AND " + materialPassExpiry;
                }
            }
        }
        if (equipmentType != null) {
//            if(!equipmentType.equals("") !("").equals(equipmentType)) {
            if (!("").equals(equipmentType)) {
                count++;
                if (count == 1) {
                    query = " equipment_type = \'" + equipmentType + "\' ";
                } else if (count > 1) {
                    query = query + " AND equipment_type = \'" + equipmentType + "\' ";
                }
            }
        }
        if (requestedDate1 != null && requestedDate2 != null) {
            if (!requestedDate1.equals("") && !requestedDate2.equals("")) {
                count++;
                String requestedDate = " requested_date BETWEEN CAST(\'" + requestedDate1 + "\' AS DATE) AND CAST(\'" + requestedDate2 + "\' AS DATE) ";
                if (count == 1) {
                    query = requestedDate;
                } else if (count > 1) {
                    query = query + " AND " + requestedDate;
                }
            }
        }
        if (requestedBy != null) {
            if (!requestedBy.equals("")) {
                count++;
                if (count == 1) {
                    query = " requested_by = \'" + requestedBy + "\' ";
                } else if (count > 1) {
                    query = query + " AND requested_by = \'" + requestedBy + "\' ";
                }
            }
        }
        if (receivedDate1 != null && receivedDate2 != null) {
            if (!receivedDate1.equals("") && !receivedDate2.equals("")) {
                count++;
                String receivedDate = " arrival_received_date BETWEEN CAST(\'" + receivedDate1 + "\' AS DATE) AND CAST(\'" + receivedDate2 + "\' AS DATE) ";
                if (count == 1) {
                    query = receivedDate;
                } else if (count > 1) {
                    query = query + " AND " + receivedDate;
                }
            }
        }
        if (status != null) {
            if (!status.equals("")) {
                count++;
                if (count == 1) {
                    query = " I.status = \'" + status + "\' ";
                } else if (count > 1) {
                    query = query + " AND I.status = \'" + status + "\' ";
                }
            }
        }

        System.out.println("Query: " + query);
        WhInventoryDAO wh = new WhInventoryDAO();
        List<WhInventory> inventoryQueryList = wh.getQuery(query);
        model.addAttribute("inventoryQueryList", inventoryQueryList);
        WhInventoryDAO wi = new WhInventoryDAO();
        List<WhInventory> hardwareIdList = wi.getHardwareId();
        model.addAttribute("hardwareIdList", hardwareIdList);
        WhInventoryDAO wi2 = new WhInventoryDAO();
        List<WhInventory> requestedByList = wi2.getRequestedBy();
        model.addAttribute("requestedByList", requestedByList);
        WhInventoryDAO wiR = new WhInventoryDAO();
        List<WhInventory> statusRList = wiR.getStatusR();
        model.addAttribute("statusRList", statusRList);
        WhInventoryDAO wiI = new WhInventoryDAO();
        List<WhInventory> statusIList = wiI.getStatusI();
        model.addAttribute("statusIList", statusIList);
        WhInventoryDAO wi4 = new WhInventoryDAO();
        List<WhInventory> hardwareTypeList = wi4.getHardwareType();
        model.addAttribute("hardwareTypeList", hardwareTypeList);
        WhInventoryDAO wi5 = new WhInventoryDAO();
        List<WhInventory> rackList = wi5.getRack();
        model.addAttribute("rackList", rackList);
        WhInventoryDAO wi6 = new WhInventoryDAO();
        List<WhInventory> shelfList = wi6.getShelf();
        model.addAttribute("shelfList", shelfList);
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
        String query = "WHERE rack_id = '' ";

        if (rackId == null) {
            LOGGER.debug("RACK ID null~~~~");
        } else if (rackId.equals("")) {
            query = "WHERE rack_id = '' ";
            LOGGER.debug("RACK ID EMPTY");
        } else if (rackId.equals("All")) {
            query = "";
        } else {
            query = "WHERE rack_id = '" + rackId + "' ";
        }
        InventoryMgtDAO wh = new InventoryMgtDAO();
        List<WhInventoryMgt> inventoryMgtList = wh.getInventoryDetailsList(query);
        InventoryMgtDAO wh2 = new InventoryMgtDAO();
        List<WhInventoryMgt> inventoryMgtList2 = wh2.getInventoryDetailsList2();
        model.addAttribute("inventoryMgtList", inventoryMgtList);
        model.addAttribute("inventoryMgtList2", inventoryMgtList2);
        return "whInventory/viewInventory";
    }

}
package com.onsemi.hms.controller;

import com.onsemi.hms.dao.InventoryMgtDAO;
import com.onsemi.hms.tools.CSV;
import com.onsemi.hms.dao.LogModuleDAO;
import com.onsemi.hms.dao.WhInventoryDAO;
import com.onsemi.hms.dao.WhMpListDAO;
import com.onsemi.hms.dao.WhRequestDAO;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import com.onsemi.hms.dao.WhShippingDAO;
import com.onsemi.hms.model.IonicFtpShipping;
import com.onsemi.hms.model.LogModule;
import com.onsemi.hms.model.UserSession;
import com.onsemi.hms.model.WhInventory;
import com.onsemi.hms.model.WhInventoryMgt;
import com.onsemi.hms.model.WhMpList;
import com.onsemi.hms.model.WhRequest;
import com.onsemi.hms.model.WhShipping;
import com.onsemi.hms.model.WhShippingLog;
import com.onsemi.hms.tools.EmailSender;
import com.onsemi.hms.tools.QueryResult;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Locale;
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
@RequestMapping(value = "/wh/whShipping")
@SessionAttributes({"userSession"})
public class WhShippingController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WhShippingController.class);
    String[] args = {};

    @Autowired
    private MessageSource messageSource;

    @Autowired
    ServletContext servletContext;

    //Delimiters which has to be in the CSV file
    private static final String COMMA_DELIMITER = ",";
    private static final String LINE_SEPARATOR = "\n";
//    private static final String HEADER = "request id,material pass no,date verified,verified by,shipping date,shipping by,status";
    private static final String HEADER = "request id,box no,date verified,verified by,shipping date,shipping by,status";
    private static final String PATHSHIP = "D:\\Source Code\\archive\\CSV Import\\hms_shipping.csv";

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
//        LOGGER.info("Masuk view 1........");
        String pdfUrl = URLEncoder.encode(request.getContextPath() + "/wh/whShipping/viewWhShippingPdf/" + whShippingId, "UTF-8");
        String backUrl = servletContext.getContextPath() + "/wh/whShipping";
        model.addAttribute("pdfUrl", pdfUrl);
        model.addAttribute("backUrl", backUrl);
        model.addAttribute("pageTitle", "Hardware Shipping");
//        LOGGER.info("Masuk view 2........");
        return "pdf/viewer";
    }

    @RequestMapping(value = "/viewWhShippingPdf/{whShippingId}", method = RequestMethod.GET)
    public ModelAndView viewWhShippingPdf(
            Model model,
            @PathVariable("whShippingId") String whShippingId
    ) {
        WhShippingDAO whShippingDAO = new WhShippingDAO();
//        LOGGER.info("Masuk 1........");
        WhShipping whShipping = whShippingDAO.getWhShippingMergeWithRequest(whShippingId);
//        LOGGER.info("Masuk 2........");
        return new ModelAndView("whShippingPdf", "whShipping", whShipping);
    }

    @RequestMapping(value = "/history/{whShippingId}", method = RequestMethod.GET)
    public String history(
            Model model,
            HttpServletRequest request,
            @PathVariable("whShippingId") String whShippingId
    ) throws UnsupportedEncodingException {
//        LOGGER.info("Masuk view 1........");        
        String pdfUrl = URLEncoder.encode(request.getContextPath() + "/wh/whShipping/viewWhShippingLogPdf/" + whShippingId, "UTF-8");
        String backUrl = servletContext.getContextPath() + "/wh/whShipping";
        model.addAttribute("pdfUrl", pdfUrl);
        model.addAttribute("backUrl", backUrl);
        model.addAttribute("pageTitle", "Hardware Shipping History");
//        LOGGER.info("Masuk view 2........");
        return "pdf/viewer";
    }

    @RequestMapping(value = "/viewWhShippingLogPdf/{whShippingId}", method = RequestMethod.GET)
    public ModelAndView viewWhShippingHistPdf(
            Model model,
            @PathVariable("whShippingId") String whShippingId
    ) {
        WhShippingDAO whShippingDAO = new WhShippingDAO();
//        LOGGER.info("Masuk 1........");
        List<WhShippingLog> whHistoryList = whShippingDAO.getWhShippingReqLog(whShippingId);
//        LOGGER.info("Masuk 2........");
        return new ModelAndView("whShippingLogPdf", "whShippingLog", whHistoryList);
    }

    @RequestMapping(value = "/query", method = {RequestMethod.GET, RequestMethod.POST})
    public String query(
            Model model,
            Locale locale,
            RedirectAttributes redirectAttrs,
            @ModelAttribute UserSession userSession,
            @RequestParam(required = false) String materialPassNo,
            @RequestParam(required = false) String boxNo,
            @RequestParam(required = false) String equipmentId,
            @RequestParam(required = false) String materialPassExpiry1,
            @RequestParam(required = false) String materialPassExpiry2,
            @RequestParam(required = false) String equipmentType,
            @RequestParam(required = false) String requestedDate1,
            @RequestParam(required = false) String requestedDate2,
            @RequestParam(required = false) String shippingDate1,
            @RequestParam(required = false) String shippingDate2,
            @RequestParam(required = false) String shippingBy,
            @RequestParam(required = false) String requestedBy,
            @RequestParam(required = false) String inventoryRack,
            @RequestParam(required = false) String inventoryShelf,
            @RequestParam(required = false) String status
    ) {
        String query = "";
        int count = 0;

        if (materialPassNo != null) {
            if (!materialPassNo.equals("")) {
                count++;
                if (count == 1) {
                    query = " material_pass_no = \'" + materialPassNo + "\' ";
                } else if (count > 1) {
                    query = query + " AND material_pass_no = \'" + materialPassNo + "\' ";
                }
            }
        }

        if (boxNo != null) {
            if (!boxNo.equals("")) {
                count++;
                if (count == 1) {
                    query = " box_no = \'" + boxNo + "\' ";
                } else if (count > 1) {
                    query = query + " AND box_no = \'" + boxNo + "\' ";
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
        if (shippingDate1 != null && shippingDate2 != null) {
            if (!shippingDate1.equals("") && !shippingDate2.equals("")) {
                count++;
                String shippingDate = " shipping_date BETWEEN CAST(\'" + shippingDate1 + "\' AS DATE) AND CAST(\'" + shippingDate2 + "\' AS DATE) ";
                if (count == 1) {
                    query = shippingDate;
                } else if (count > 1) {
                    query = query + " AND " + shippingDate;
                }
            }
        }
        if (shippingBy != null) {
            if (!shippingBy.equals("")) {
                count++;
                if (count == 1) {
                    query = " shipping_by = \'" + shippingBy + "\' ";
                } else if (count > 1) {
                    query = query + " AND shipping_by = \'" + shippingBy + "\' ";
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
        if (inventoryRack != null) {
            if (!inventoryRack.equals("")) {
                count++;
                if (count == 1) {
                    query = " inventory_rack = \'" + inventoryRack + "\' ";
                } else if (count > 1) {
                    query = query + " AND inventory_rack = \'" + inventoryRack + "\' ";
                }
            }
        }
        if (inventoryShelf != null) {
            if (!inventoryShelf.equals("")) {
                count++;
                if (count == 1) {
                    query = " inventory_shelf = \'" + inventoryShelf + "\' ";
                } else if (count > 1) {
                    query = query + " AND inventory_shelf = \'" + inventoryShelf + "\' ";
                }
            }
        }
        if (status != null) {
            if (!("").equals(status)) {
                count++;
                if (count == 1) {
                    if (status.equals("Closed")) {
                        query = " (S.status = \'" + status + "\' OR S.status = \'Closed. Verified By Supervisor\') ";
                    } else {
                        query = " S.status = \'" + status + "\' ";
                    }
                } else if (count > 1) {
                    if (status.equals("Closed")) {
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
        WhShippingDAO wr = new WhShippingDAO();
        List<WhShipping> hardwareIdList = wr.getHardwareId();
        model.addAttribute("hardwareIdList", hardwareIdList);
        WhShippingDAO wi2 = new WhShippingDAO();
        List<WhShipping> requestedByList = wi2.getRequestedBy();
        model.addAttribute("requestedByList", requestedByList);
        WhShippingDAO wiR = new WhShippingDAO();
        List<WhShipping> statusRList = wiR.getStatusR();
        model.addAttribute("statusRList", statusRList);
        WhShippingDAO wiS = new WhShippingDAO();
        List<WhShipping> statusSList = wiS.getStatusS();
        model.addAttribute("statusSList", statusSList);
        WhShippingDAO wi4 = new WhShippingDAO();
        List<WhShipping> hardwareTypeList = wi4.getHardwareType();
        model.addAttribute("hardwareTypeList", hardwareTypeList);
        WhShippingDAO wi5 = new WhShippingDAO();
        List<WhShipping> rackList = wi5.getRack();
        model.addAttribute("rackList", rackList);
        WhShippingDAO wi6 = new WhShippingDAO();
        List<WhShipping> shelfList = wi6.getShelf();
        model.addAttribute("shelfList", shelfList);
        WhShippingDAO wi7 = new WhShippingDAO();
        List<WhShipping> shippingByList = wi7.getShippingBy();
        model.addAttribute("shippingByList", shippingByList);

        return "whShipping/query";
    }

    @RequestMapping(value = "/packingList", method = RequestMethod.GET)
    public String packingList(
            Model model
    ) {
        WhMpListDAO whMpListDAO = new WhMpListDAO();
        List<WhMpList> packingList = whMpListDAO.getWhMpListMergeWithShippingAndRequestList();
        whMpListDAO = new WhMpListDAO();
        int countAll = whMpListDAO.getCount();
        model.addAttribute("countAll", countAll);
        model.addAttribute("packingList", packingList);
        return "whShipping/packingList";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(
            Model model,
            HttpServletRequest request,
            Locale locale,
            RedirectAttributes redirectAttrs,
            @ModelAttribute UserSession userSession,
            //            @RequestParam(required = false) String materialPassNo
            @RequestParam(required = false) String boxNo
    ) throws IOException {
        WhShippingDAO whShipD = new WhShippingDAO();
        int count = whShipD.getCountBoxNo(boxNo); //mpno in shipping
        if (count != 0) {
            WhMpListDAO whMpListDAO = new WhMpListDAO();
            int countMpNo = whMpListDAO.getCountBoxNo(boxNo); //mpno in mplist
            model.addAttribute("count", countMpNo);
            if (countMpNo == 0) {
                WhShippingDAO whshipD = new WhShippingDAO();
                WhShipping whship = whshipD.getWhShippingMergeWithRequestByBoxNo(boxNo);
                WhMpList whMpList = new WhMpList();
                whMpList.setShippingId(whship.getId());
                String refId = whship.getRequestId();
                whMpList.setRequestId(refId);
//                whMpList.setMaterialPassNo(materialPassNo);
                whMpList.setBoxNo(boxNo);
                whMpList.setCreatedBy(userSession.getFullname());
                whMpList.setStatus("Ship");
                whMpListDAO = new WhMpListDAO();
                QueryResult queryResult = whMpListDAO.insertWhMpList(whMpList);

                WhShippingDAO whShippingDAO1 = new WhShippingDAO();
                WhShipping query1 = whShippingDAO1.getWhShipping(refId);
                WhMpListDAO whMpListDAO3 = new WhMpListDAO();
                WhMpList query2 = whMpListDAO3.getWhMpListMergeWithShippingAndRequest(refId); //double check
                LogModule logModule2 = new LogModule();
                LogModuleDAO logModuleDAO2 = new LogModuleDAO();
                logModule2.setModuleId(query1.getId());
                logModule2.setReferenceId(refId);
                logModule2.setModuleName("hms_wh_shipping_list");
                logModule2.setStatus("Ship");
                logModule2.setVerifiedBy(query2.getCreatedBy());
                logModule2.setVerifiedDate(query2.getCreatedDate());
                QueryResult queryResult1 = logModuleDAO2.insertLogForVerification(logModule2);

                args = new String[1];
                args[0] = boxNo;
                if (queryResult.getGeneratedKey().equals("0")) {
                    model.addAttribute("error", messageSource.getMessage("general.label.save.error", args, locale));
                    model.addAttribute("packingList", whMpList);
//                    return "redirect:wh/whShipping/packingList";
                } else {
                    /*create csv & email*/
                    String username = System.getProperty("user.name");
//                    File file = new File("D:\\HIMS_CSV\\SF\\hms_shipping.csv");
                    File file = new File(PATHSHIP);

                    WhMpListDAO whListDao = new WhMpListDAO();
                    WhMpList mplist = whListDao.getWhMpListMergeWithShippingAndRequest(whship.getRequestId());
                    if (file.exists()) { //create csv file                        
                        LOGGER.info("tiada header");
                        FileWriter fileWriter = null;
                        FileReader fileReader = null;
                        try {
                            String targetLocation = PATHSHIP;
                            fileWriter = new FileWriter(targetLocation, true);
                            fileReader = new FileReader(targetLocation);

                            BufferedReader bufferedReader = new BufferedReader(fileReader);
                            String data = bufferedReader.readLine();
                            StringBuilder buff = new StringBuilder();

                            boolean check = false;
                            int row = 0;
                            while (data != null) {
                                LOGGER.info("start reading file..........");
                                buff.append(data).append(System.getProperty("line.separator"));
//                                System.out.println("dataaaaaaaaa : \n" + data);

                                String[] split = data.split(",");
                                IonicFtpShipping shipping = new IonicFtpShipping(
                                        split[0], split[1], split[2],
                                        split[3], split[4], split[5],
                                        split[6] //date = [4], by = [5], status = [6]
                                );

                                if (split[0].equals(mplist.getRequestId())) {
                                    LOGGER.info(row + " : request Id found...................." + data);
                                    CSV csv = new CSV();
                                    csv.open(new File(targetLocation));
                                    csv.put(4, row, mplist.getCreatedDate());
                                    csv.put(5, row, mplist.getCreatedBy());
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

                            if (check == false) {
                                fileWriter = new FileWriter(targetLocation, true);
                                //New Line after the header
                                fileWriter.append(LINE_SEPARATOR);
                                fileWriter.append(mplist.getRequestId());
                                fileWriter.append(COMMA_DELIMITER);
//                                fileWriter.append(mplist.getMaterialPassNo());
                                fileWriter.append(mplist.getBoxNo());
                                fileWriter.append(COMMA_DELIMITER);
                                fileWriter.append(mplist.getDateVerify());
                                fileWriter.append(COMMA_DELIMITER);
                                fileWriter.append(mplist.getUserVerify());
                                fileWriter.append(COMMA_DELIMITER);
                                fileWriter.append(mplist.getCreatedDate());
                                fileWriter.append(COMMA_DELIMITER);
                                fileWriter.append(mplist.getCreatedBy());
                                fileWriter.append(COMMA_DELIMITER);
                                fileWriter.append(whMpList.getStatus());
//                                fileWriter.append(COMMA_DELIMITER);
                                System.out.println("append to CSV file Succeed!!!");
                            }
                        } catch (Exception ee) {
                            ee.printStackTrace();
                        } finally {
                            try {
                                fileWriter.close();
                            } catch (IOException ie) {
                                System.out.println("Error occured while closing the fileWriter");
                                ie.printStackTrace();
                            }
                        }
                    } else {
                        FileWriter fileWriter = null;
                        try {
                            fileWriter = new FileWriter(PATHSHIP);
                            LOGGER.info("no file yet");
                            //Adding the header
                            fileWriter.append(HEADER);

                            //New Line after the header
                            fileWriter.append(LINE_SEPARATOR);
                            fileWriter.append(mplist.getRequestId());
                            fileWriter.append(COMMA_DELIMITER);
//                            fileWriter.append(mplist.getMaterialPassNo());
                            fileWriter.append(mplist.getBoxNo());
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(mplist.getDateVerify());
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(mplist.getUserVerify());
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(mplist.getCreatedDate());
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(mplist.getCreatedBy());
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(whMpList.getStatus());
//                            fileWriter.append(COMMA_DELIMITER);
                            System.out.println("Write new to CSV file Succeed!!!");
                        } catch (Exception ee) {
                            ee.printStackTrace();
                        } finally {
                            try {
                                fileWriter.close();
                            } catch (IOException ie) {
                                System.out.println("Error occured while closing the fileWriter");
                                ie.printStackTrace();
                            }
                        }
                    }

                    //send email
//                    LOGGER.info("send email to warehouse");
//                    System.out.println("######################### START EMAIL PROCESS ########################### ");
//                    System.out.println("\n******************* EMAIL CDARS *******************");
//                    //sent to cdars
////                    String[] to = {"cdarsreltest@gmail.com"};
////                    String[] to = {"cdarsrel@gmail.com"};
//                    String[] to = {"hims@onsemi.com"};
//                    EmailSender emailSender = new EmailSender();
//                    emailSender.htmlEmailWithAttachmentTest(
//                            servletContext,
//                            "CDARS", //user name
//                            to, //to
//                            "Status for Hardware Shipping from HIMS SF", //subject
//                            "Verification and status for Hardware Shipping has been made." //msg
//                    );
//
//                    System.out.println("######################### END EMAIL PROCESS ########################### ");
                    WhShipping whShipping = new WhShipping();
                    whShipping.setRequestId(whship.getRequestId());
//                    whShipping.setMaterialPassNo(materialPassNo);
                    whShipping.setBoxNo(boxNo);
                    whShipping.setStatus("Ship");
                    whShipping.setFlag("1");
                    whShipping.setShippingBy(mplist.getCreatedBy());
                    whShipping.setShippingDate(mplist.getCreatedDate());
                    WhShippingDAO whShippingDao = new WhShippingDAO();
                    QueryResult queryResult2 = whShippingDao.updateWhShipping(whShipping);

                    WhRequest whReq = new WhRequest();
                    whReq.setRefId(whship.getRequestId());
                    whReq.setStatus("Ship");
                    whReq.setFlag("1");
                    WhRequestDAO whRequestDao = new WhRequestDAO();
                    QueryResult queryResult3 = whRequestDao.updateWhRequestStatus(whReq);

                    whRequestDao = new WhRequestDAO();
                    WhRequest queryShelf = whRequestDao.getWhRequest(refId);
                    InventoryMgtDAO inventoryMgtDAO3 = new InventoryMgtDAO();
                    WhInventoryMgt whInventoryMgt = inventoryMgtDAO3.getInventoryDetails(queryShelf.getInventoryShelf());

                    //add mpNo to shipping table - to display MP no in packing list (security need to close)
                    WhShipping whShipping2 = new WhShipping();
                    whShipping2.setRequestId(whship.getRequestId());
                    if (whInventoryMgt.getMaterialPassNo() == "Empty") {
                        whShipping2.setMaterialPassNo("");
                    } else {
                        whShipping2.setMaterialPassNo(whInventoryMgt.getMaterialPassNo());
                    }
                    WhShippingDAO whSD = new WhShippingDAO();
                    QueryResult qRe = whSD.updateWhShippingMaterialPass(whShipping2);

                    WhInventoryMgt imgt = new WhInventoryMgt();
                    imgt.setRackId(whInventoryMgt.getRackId());
                    imgt.setShelfId(whInventoryMgt.getShelfId());
                    imgt.setHardwareId("Empty");
                    imgt.setMaterialPassNo("Empty");
                    imgt.setBoxNo("Empty");
                    InventoryMgtDAO imdao = new InventoryMgtDAO();
                    QueryResult queryMgt = imdao.updateInventoryDetails(imgt);

                    WhInventory wi = new WhInventory();
                    wi.setFlag("1");
                    wi.setStatus("Unavailable in Inventory");
//                    wi.setMaterialPassNo(materialPassNo);
                    wi.setBoxNo(boxNo);
                    WhInventoryDAO widao = new WhInventoryDAO();
                    QueryResult querywi = widao.updateWhInventoryStatus(wi);

                    if (queryResult3.getResult() == 1) {
                        redirectAttrs.addFlashAttribute("success", messageSource.getMessage("general.label.update.success5", args, locale));
//                        return "redirect:/wh/whShipping/packingList";
                    }
                }
            } else {
                redirectAttrs.addFlashAttribute("error", messageSource.getMessage("general.label.save.error1", args, locale));
            }
        } else {
            redirectAttrs.addFlashAttribute("error", messageSource.getMessage("general.label.save.error2", args, locale));
        }
        return "redirect:/wh/whShipping/packingList";
    }

    @RequestMapping(value = "/print", method = RequestMethod.GET)
    public String print(
            Model model,
            HttpServletRequest request
    ) throws UnsupportedEncodingException {
//        LOGGER.info("Masuk view 1........");        
        String pdfUrl = URLEncoder.encode(request.getContextPath() + "/wh/whShipping/viewPackingListPdf", "UTF-8");
        String backUrl = servletContext.getContextPath() + "/wh/whShipping/packingList";
        model.addAttribute("pdfUrl", pdfUrl);
        model.addAttribute("backUrl", backUrl);
        model.addAttribute("pageTitle", "Hardware Packing List From SBN Factory to Rel Lab ON Semiconductor");
//        LOGGER.info("Masuk view 2........");
        return "pdf/viewer";
    }

    @RequestMapping(value = "/viewPackingListPdf", method = RequestMethod.GET)
    public ModelAndView viewPackingListPdf(
            Model model
    ) {
        WhMpListDAO whMpListDAO = new WhMpListDAO();
        LOGGER.info("Masuk 1........");
        List<WhMpList> packingList = whMpListDAO.getWhMpListMergeWithShippingAndRequestList();
        LOGGER.info("Masuk 2........");
        return new ModelAndView("packingListPdf", "packingList", packingList);
    }

    @RequestMapping(value = "/deleteAll", method = RequestMethod.GET)
    public String deleteAll(
            Model model,
            Locale locale,
            RedirectAttributes redirectAttrs
    ) {
        WhMpListDAO whMpListDAO = new WhMpListDAO();
        QueryResult queryResult = whMpListDAO.deleteAllWhMpList();
        args = new String[1];
        args[0] = "All data has been ";
        String error = "Unable to delete, please try again!";
        if (queryResult.getResult() > 0) {
            redirectAttrs.addFlashAttribute("success", messageSource.getMessage("general.label.delete.success", args, locale));
        } else {
            redirectAttrs.addFlashAttribute("error", error);
        }
        return "redirect:/wh/whShipping/packingList";
    }

//    @RequestMapping(value = "/delete/{whShippingId}", method = RequestMethod.GET)
//    public String delete(
//            Model model,
//            Locale locale,
//            RedirectAttributes redirectAttrs,
//            @ModelAttribute UserSession userSession,
//            @PathVariable("whShippingId") String whShippingId
//    ) throws IOException {
//        WhMpListDAO whMpListDAO = new WhMpListDAO();
//        WhMpList whMpList = whMpListDAO.getWhMpListMergeWithShippingAndRequest(whShippingId);
//        whMpListDAO = new WhMpListDAO();
//        QueryResult queryResult = whMpListDAO.deleteWhMpList(whShippingId);
//        args = new String[1];
//        args[0] = whMpList.getMaterialPassNo();
//        if (queryResult.getResult() == 1) {
//            redirectAttrs.addFlashAttribute("success", messageSource.getMessage("general.label.delete.success", args, locale));
//        }
//        return "";
//    }
    //TEMPORARYYYYY
//    @RequestMapping(value = "/email", method = {RequestMethod.GET, RequestMethod.POST})
//    public String email(
//            Model model,
//            HttpServletRequest request,
//            Locale locale,
//            RedirectAttributes redirectAttrs,
//            @ModelAttribute UserSession userSession
//    ) throws IOException {
//        WhMpListDAO whMpListDAO = new WhMpListDAO();
//        int countMpList = whMpListDAO.getCount();
//        if(countMpList != 0) {
//            whMpListDAO = new WhMpListDAO();
//            List<WhMpList> mpList = whMpListDAO.getWhMpListMergeWithShippingAndRequestList();
//            
//            String shippingId="", materialPassNo="", dateVerify="", userVerify="", createdDate="", createdBy="", status="";
//            for(int i=0; i<mpList.size(); i++) {
//                shippingId = mpList.get(i).getShippingId();
//                materialPassNo = mpList.get(i).getMaterialPassNo();
//                dateVerify = mpList.get(i).getDateVerify();
//                userVerify = mpList.get(i).getUserVerify();
//                createdDate = mpList.get(i).getCreatedDate();
//                createdBy = mpList.get(i).getCreatedBy();
//                status = mpList.get(i).getStatus();
//            }
//        }
//        return "redirect:/wh/whShipping/print";
//    }
    @RequestMapping(value = "/email", method = {RequestMethod.GET, RequestMethod.POST})
    public String email(
            Model model,
            HttpServletRequest request,
            Locale locale,
            RedirectAttributes redirectAttrs,
            @ModelAttribute UserSession userSession
    ) throws IOException {

        //send email
        LOGGER.info("send email to warehouse");
        System.out.println("######################### START EMAIL PROCESS ########################### ");
        System.out.println("\n******************* EMAIL CDARS *******************");
        //sent to cdars
//                    String[] to = {"cdarsrel@gmail.com"};
        String[] receiver = {"hims@onsemi.com"};
        EmailSender emailSenderCsv = new EmailSender();
        emailSenderCsv.htmlEmailWithAttachmentTest(
                servletContext,
                "CDARS", //user name
                receiver, //to
                "Status for Hardware Shipping from HIMS SF", //subject
                "Verification and status for Hardware Shipping has been made." //msg
        );

        WhMpListDAO whMpListDAO = new WhMpListDAO();
        int countMpList = whMpListDAO.getCount();

        if (countMpList != 0) {
            WhMpListDAO mpDao = new WhMpListDAO();
            List<WhMpList> mpList = mpDao.getWhMpListEmail();
            List<String> to = new ArrayList<String>();
            for (int i = 0; i < mpList.size(); i++) {
                to.add(mpList.get(i).getRequestedEmail());
            }

            String[] listEmail = new String[to.size()];
            to.toArray(listEmail);
            LOGGER.info("" + listEmail.length);
            LOGGER.info("" + to);

            LOGGER.info("send email to person in charge");
            EmailSender emailSender = new EmailSender();
            emailSender.htmlEmailTable(
                    servletContext,
                    "", //user name requestor
                    listEmail, //to
                    "List of Hardware(s) Ready for Shipment", //subject
                    "The list of hardware(s) that have been ready for shipment has been made.<br />"
                    + "This table will shows the details of each hardware in a material pass shipping list from Seremban Factory. <br /><br />"
                    + "<br /><br /> "
                    + "<style>table, th, td {border: 1px solid black;} </style>"
                    + "<table style=\"width:100%\">" //tbl
                    + "<tr>"
                    + "<th>BOX NO</th> "
                    //                    + "<th>MATERIAL PASS NO</th> "
                    //                    + "<th>MATERIAL PASS EXPIRY DATE</th> "
                    + "<th>HARDWARE TYPE</th>"
                    + "<th>HARDWARE ID</th>"
                    + "<th>QUANTITY</th>"
                    + "</tr>"
                    + table()
                    + "</table>"
                    + "<br />Thank you." //msg
            );
        }

        System.out.println("######################### END EMAIL PROCESS ########################### ");

        return "redirect:/wh/whShipping/print";
    }

    private String table() {
        WhMpListDAO whMpListDAO = new WhMpListDAO();
        List<WhMpList> whMpListList = whMpListDAO.getWhMpListMergeWithShippingAndRequestList();
//        String materialPassNo = "";
        String boxNo = "";
        String hardwareType = "";
        String hardwareId = "";
        String quantity = "";
        String text = "";

        for (int i = 0; i < whMpListList.size(); i++) {
//            materialPassNo = whMpListList.get(i).getMaterialPassNo();
            boxNo = whMpListList.get(i).getBoxNo();
            hardwareId = whMpListList.get(i).getEquipmentId();
            hardwareType = whMpListList.get(i).getEquipmentType();
            quantity = whMpListList.get(i).getQuantity();
            text = text + "<tr align = \"center\">";
            text = text + "<td>" + boxNo + "</td>";
//            text = text + "<td>" + materialPassExp + "</td>";
            text = text + "<td>" + hardwareType + "</td>";
            text = text + "<td>" + hardwareId + "</td>";
            text = text + "<td>" + quantity + "</td>";
            text = text + "</tr>";
        }
        return text;
    }
}

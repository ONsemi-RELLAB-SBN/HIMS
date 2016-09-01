package com.onsemi.hms.controller;

import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import com.onsemi.hms.dao.WhMpListDAO;
import com.onsemi.hms.dao.WhShippingDAO;
import com.onsemi.hms.model.WhMpList;
import com.onsemi.hms.model.UserSession;
import com.onsemi.hms.model.WhShipping;
import com.onsemi.hms.tools.EmailSender;
import com.onsemi.hms.tools.QueryResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import javax.mail.Address;
import javax.mail.internet.InternetAddress;
import javax.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/wh/whShipping/whMpList")
@SessionAttributes({"userSession"})
public class WhMpListController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WhMpListController.class);
    String[] args = {};

    @Autowired
    private MessageSource messageSource;

    @Autowired
    ServletContext servletContext;

    //Delimiters which has to be in the CSV file
    private static final String COMMA_DELIMITER = ",";
    private static final String LINE_SEPARATOR = "\n";
    private static final String HEADER = "id,hardware_type,hardware_id,quantity,material pass number,material pass expiry date,requested_by,"
            + "requested_date,remarks";

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String whMpList(
            Model model
    ) {
        WhMpListDAO whMpListDAO = new WhMpListDAO();
        List<WhMpList> whMpListList = whMpListDAO.getWhMpListList();
        model.addAttribute("whMpListList", whMpListList);
        return "whMpList/whMpList";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(Model model) {
        return "whMpList/add";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(
            Model model,
            HttpServletRequest request,
            Locale locale,
            RedirectAttributes redirectAttrs,
            @ModelAttribute UserSession userSession,
            @RequestParam(required = false) String mpNo
    ) {
        //check ad material pass ke tidak dkt shipping.
        WhShippingDAO whShipD = new WhShippingDAO();
        int count = whShipD.getCountMpNo(mpNo);
        if (count != 0) {
            //check da ade ke mp_no dkt whmplist
            WhMpListDAO whMpListDAO = new WhMpListDAO();
            int countMpNo = whMpListDAO.getCountMpNo(mpNo);
            if (countMpNo == 0) {
                WhShippingDAO whshipD = new WhShippingDAO();

                WhShipping whship = whshipD.getWhShippingMergeWithRequestByMpNo(mpNo);

                WhMpList whMpList = new WhMpList();
                whMpList.setWhShipId(whship.getId());
                whMpList.setMaterialPassNo(mpNo);
                whMpList.setMaterialPassExpiry(whship.getMaterialPassExpiry());
                whMpList.setEquipmentId(whship.getEquipmentId());
                whMpList.setEquipmentType(whship.getEquipmentType());
                whMpList.setQuantity(whship.getQuantity());
                whMpList.setRequestedBy(whship.getRequestedBy());
                whMpList.setRequestedDate(whship.getRequestedDate());
                whMpList.setCreatedBy(userSession.getFullname());
                whMpListDAO = new WhMpListDAO();
                QueryResult queryResult = whMpListDAO.insertWhMpList(whMpList);
                args = new String[1];
                args[0] = mpNo;
                if (queryResult.getGeneratedKey().equals("0")) {
                    model.addAttribute("error", messageSource.getMessage("general.label.save.error", args, locale));
                    model.addAttribute("whMpList", whMpList);
                    return "whMpList/add";
                } else {
                    File file = new File("C:\\hms_shipping.csv");

                    if (file.exists()) {
                        //create csv file
                        LOGGER.info("tiada header");
                        FileWriter fileWriter = null;
                        try {
                            fileWriter = new FileWriter("C:\\hms_ship_process.csv", true);
                            //New Line after the header
                            fileWriter.append(LINE_SEPARATOR);

                            fileWriter.append(queryResult.getGeneratedKey());
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(whship.getEquipmentType());
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(whship.getEquipmentId());
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(whship.getQuantity());
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(mpNo);
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(whship.getMaterialPassExpiry());
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(whship.getRequestedBy());
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(whship.getRequestedDate());
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(whship.getRemarks());
                            fileWriter.append(COMMA_DELIMITER);
                            System.out.println("append to CSV file Succeed!!!");
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
                            fileWriter = new FileWriter("C:\\hms_ship_process.csv");
                            LOGGER.info("no file yet");
                            //Adding the header
                            fileWriter.append(HEADER);

                            //New Line after the header
                            fileWriter.append(LINE_SEPARATOR);

                            fileWriter.append(queryResult.getGeneratedKey());
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(whship.getEquipmentType());
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(whship.getEquipmentId());
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(whship.getQuantity());
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(mpNo);
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(whship.getMaterialPassExpiry());
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(whship.getRequestedBy());
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(whship.getRequestedDate());
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(whship.getRemarks());
                            fileWriter.append(COMMA_DELIMITER);
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
                    //String[] to = {"hmsrelon@gmail.com", "cdarsrel@gmail.com"};
                    emailSender.htmlEmailWithAttachmentShipping(
                            servletContext,
                            user,                               //user name
                            "cdarsrel@gmail.com",               //to
                            "New Hardware Shipping from HMS",   //subject
                            "New hardware will be ship to storage factory. Please go to this link "     //msg
                            + "<a href=\"" + request.getScheme() + "://" + hostName + ":" + request.getServerPort() + request.getContextPath() + "/wh/whRequest/approval/" + queryResult.getGeneratedKey() + "\">HMS</a>"
                            + " to check the shipping list."
                    );
                    redirectAttrs.addFlashAttribute("success", messageSource.getMessage("general.label.save.success", args, locale));
                    return "whMpList/add";
                }
            } else {
                String messageError = "Material Pass Number " + mpNo + " already added to the list!";
                model.addAttribute("error", messageError);
                return "whMpList/add";
            }
        } else {
            String messageError = "Material Pass Number " + mpNo + " Not Exist!";
            model.addAttribute("error", messageError);
            return "whMpList/add";
        }

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
        return "redirect:/wh/whShipping/whMpList";
    }
}
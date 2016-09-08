package com.onsemi.hms.controller;

import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import com.onsemi.hms.dao.WhMpListDAO;
import com.onsemi.hms.dao.WhRequestDAO;
import com.onsemi.hms.dao.WhShippingDAO;
import com.onsemi.hms.model.IonicFtpShipping;
import com.onsemi.hms.model.WhMpList;
import com.onsemi.hms.model.UserSession;
import com.onsemi.hms.model.WhRequest;
import com.onsemi.hms.model.WhShipping;
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
    private static final String HEADER = "request id,material pass no,date verified,verified by,shipping date,shipping by,status";

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String whMpList(
            Model model
    ) {
        WhMpListDAO whMpListDAO = new WhMpListDAO();
        List<WhMpList> whMpListList = whMpListDAO.getWhMpListMergeWithShippingAndRequestList();
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
            @RequestParam(required = false) String materialPassNo
    ) throws IOException {
        WhShippingDAO whShipD = new WhShippingDAO();
        int count = whShipD.getCountMpNo(materialPassNo); //mpno in shipping
        if (count != 0) {
            WhMpListDAO whMpListDAO = new WhMpListDAO();
            int countMpNo = whMpListDAO.getCountMpNo(materialPassNo); //mpno in mplist
            if (countMpNo == 0) {
                WhShippingDAO whshipD = new WhShippingDAO();
                WhShipping whship = whshipD.getWhShippingMergeWithRequestByMpNo(materialPassNo);
                WhMpList whMpList = new WhMpList();
                whMpList.setShippingId(whship.getRequestId());
                whMpList.setMaterialPassNo(materialPassNo);
                whMpList.setCreatedBy(userSession.getFullname());
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                whMpList.setCreatedDate(dateFormat.format(date));
                whMpList.setStatus("Ship");
                whMpListDAO = new WhMpListDAO();
                QueryResult queryResult = whMpListDAO.insertWhMpList(whMpList);
                args = new String[1];
                args[0] = materialPassNo;
                if (queryResult.getGeneratedKey().equals("0")) {
                    model.addAttribute("error", messageSource.getMessage("general.label.save.error", args, locale));
                    model.addAttribute("whMpList", whMpList);
                    return "whMpList/add";
                } else {
                    /*create csv & email*/
                    String username = System.getProperty("user.name");
                    File file = new File("C:\\Users\\" + username + "\\Documents\\from HMS\\hms_shipping.csv");

                    WhMpListDAO whListDao = new WhMpListDAO();
                    WhMpList mplist = whListDao.getWhMpListMergeWithShippingAndRequest(whship.getRequestId());
                    if (file.exists()) { //create csv file                        
                        LOGGER.info("tiada header");
                        FileWriter fileWriter = null;
                        FileReader fileReader = null;
                        try {
                            fileWriter = new FileWriter("C:\\Users\\" + username + "\\Documents\\from HMS\\hms_shipping.csv", true);
                            fileReader = new FileReader("C:\\Users\\" + username + "\\Documents\\from HMS\\hms_shipping.csv");
                            String targetLocation = "C:\\Users\\" + username + "\\Documents\\from HMS\\hms_shipping.csv";

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
                                IonicFtpShipping shipping = new IonicFtpShipping(
                                    split[0], split[1], split[2],
                                    split[3], split[4], split[5], 
                                    split[6] //date = [4], by = [5], status = [6]
                                );

                                if(split[0].equals(mplist.getShippingId())) {
                                    LOGGER.info(row + " : request Id found...................." + data);
                                    CSV csv = new CSV();
                                    csv.open(new File(targetLocation));
                                    csv.put(4, row, "" + mplist.getCreatedDate());
                                    csv.put(5, row, "" + mplist.getCreatedBy());
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
                            
                            if(check == false) {
                                fileWriter = new FileWriter("C:\\Users\\" + username + "\\Documents\\from HMS\\hms_shipping.csv", true);
                                //New Line after the header
                                fileWriter.append(LINE_SEPARATOR);
                                fileWriter.append(mplist.getShippingId());
                                fileWriter.append(COMMA_DELIMITER);
                                fileWriter.append(mplist.getMaterialPassNo());
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
                            fileWriter = new FileWriter("C:\\Users\\" + username + "\\Documents\\from HMS\\hms_shipping.csv");
                            LOGGER.info("no file yet");
                            //Adding the header
                            fileWriter.append(HEADER);

                            //New Line after the header
                            fileWriter.append(LINE_SEPARATOR);
                            fileWriter.append(mplist.getShippingId());
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(mplist.getMaterialPassNo());
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
                    LOGGER.info("send email to warehouse");

                    //to get hostname
                    InetAddress ip;
                    String hostName ="";
                    try {
                        ip = InetAddress.getLocalHost();
                        hostName = ip.getHostName();
                    } catch (UnknownHostException ex) {
                        java.util.logging.Logger.getLogger(WhMpListController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    System.out.println("######################### START EMAIL PROCESS ########################### ");
                    System.out.println("\n******************* EMAIL CDARS ******************* cdarsrel@gmail.com");
                    //sent to cdars
                    EmailSender emailSender = new EmailSender();
                    emailSender.htmlEmailWithAttachmentShipping(
                        servletContext,
                        "CDARS",                                                   //user name
                        "cdarsrel@gmail.com",                                   //to
                        "Status for Hardware Shipping from HMS",  //subject
                        "Verification and status for Hardware Shipping has been made."    //msg
                    );
                    
                    System.out.println("******************* EMAIL REQUESTOR ******************* " + whship.getRequestedEmail());
                    //sent to requestor
                    EmailSender emailSender2 = new EmailSender();
                    String emailTitle = "Status for Hardware Shipping from HMS";
                    emailSender2.htmlEmail2(
                        servletContext,
                        whship.getRequestedBy(),                       //from
                        whship.getRequestedEmail(), //to
                        emailTitle,         
                        "Hardware has been ready to shipping. Please go to this link " //msg
                        + "<a href=\"" + request.getScheme() + "://fg79cj-l1:" + request.getServerPort() + "/CDARS/wh/whRetrieval/" + "\">CDARS</a>"
                        + " for status checking."
                    );
                    
                    System.out.println("######################### END EMAIL PROCESS ########################### ");
                    
                    WhShipping whShipping = new WhShipping();
                    whShipping.setRequestId(whship.getRequestId());
                    whShipping.setStatus("Ship");
                    WhShippingDAO whShippingDao = new WhShippingDAO();
                    QueryResult queryResult2 = whShippingDao.updateWhShippingStatus(whShipping);
                    
                    WhRequest whReq = new WhRequest();
                    whReq.setRefId(whship.getRequestId());
                    whReq.setStatus("Ship");
                    WhRequestDAO whRequestDao = new WhRequestDAO();
                    QueryResult queryResult3 = whRequestDao.updateWhRequestStatus(whReq);
                    
                    if(queryResult3.getResult() == 1){
                        redirectAttrs.addFlashAttribute("success", messageSource.getMessage("general.label.update.success5", args, locale));
                    return "redirect:/wh/whShipping/whMpList";  
                    }else{
                        LOGGER.info("Data failed to update");
                        return "whMpList/add";  
                    }
                    
                }
            } else {
                String messageError = "Material Pass Number " + materialPassNo + " already added to the list!";
                model.addAttribute("error", messageError);
                return "whMpList/add";
            }
        } else {
            String messageError = "Material Pass Number " + materialPassNo + " Not Exist!";
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
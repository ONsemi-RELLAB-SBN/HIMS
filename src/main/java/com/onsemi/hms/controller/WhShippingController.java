//package com.onsemi.hms.controller;
//
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//import java.util.List;
//import java.util.Locale;
//import javax.servlet.http.HttpServletRequest;
//import com.onsemi.hms.dao.WhShippingDAO;
//import com.onsemi.hms.model.IonicFtpShipping;
//import com.onsemi.hms.model.WhShipping;
//import com.onsemi.hms.model.UserSession;
//import com.onsemi.hms.tools.EmailSender;
//import com.onsemi.hms.tools.QueryResult;
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.net.InetAddress;
//import java.net.UnknownHostException;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.logging.Level;
//import javax.servlet.ServletContext;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.MessageSource;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.SessionAttributes;
//import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//@Controller
//@RequestMapping(value = "/wh/whShipping")
//@SessionAttributes({"userSession"})
//public class WhShippingController {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(WhShippingController.class);
//    String[] args = {};
//    
//    @Autowired
//    private MessageSource messageSource;
//
//    @Autowired
//    ServletContext servletContext;
//
//    @RequestMapping(value = "", method = RequestMethod.GET)
//    public String whShipping(
//            Model model
//    ) {
//        WhShippingDAO whShippingDAO = new WhShippingDAO();
//        List<WhShipping> whShippingList = whShippingDAO.getWhShippingListMergeRequest();
//        model.addAttribute("whShippingList", whShippingList);
//        return "whShipping/whShipping";
//    }
//
//    @RequestMapping(value = "/edit/{whShippingId}", method = RequestMethod.GET)
//    public String edit(
//            Model model,
//            @PathVariable("whShippingId") String whShippingId
//    ) {
//        WhShippingDAO whShippingDAO = new WhShippingDAO();
//        WhShipping whShipping = whShippingDAO.getWhShippingMergeWithRequest(whShippingId);
//        LOGGER.info("whShipping.getEquipmentType() : " + whShipping.getEquipmentType());
//        
//        String type = whShipping.getEquipmentType();
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
//        model.addAttribute("whShipping", whShipping);
//        return "whShipping/edit";
//    }
//        
//    @RequestMapping(value = "/update", method = RequestMethod.POST)
//    public String update(
//            Model model,
//            Locale locale,
//            HttpServletRequest request,
//            RedirectAttributes redirectAttrs,
//            @ModelAttribute UserSession userSession,
//            @RequestParam(required = false) String refId,
//            @RequestParam(required = false) String materialPassNo,
//            @RequestParam(required = false) String inventoryRack,
//            @RequestParam(required = false) String inventorySlot
//    ) {
//        WhShipping whShipping = new WhShipping();
//        
//        whShipping.setRefId(refId); //ref
//        LOGGER.info(refId);
//        whShipping.setMaterialPassNo(materialPassNo); //args
//        LOGGER.info(materialPassNo);
//        whShipping.setShippingRack(inventoryRack); //update
//        LOGGER.info(inventoryRack);
//        whShipping.setShippingSlot(inventorySlot); //update
//        LOGGER.info(inventorySlot);
//        LOGGER.info(materialPassNo);
//        whShipping.setShippingBy(userSession.getFullname()); //update
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date = new Date();
//        whShipping.setShippingDate(dateFormat.format(date));
//        WhShippingDAO whShippingDAO = new WhShippingDAO();
//        QueryResult queryResult = whShippingDAO.updateWhShipping(whShipping);
//        args = new String[1];
//        args[0] = materialPassNo;
//        if (queryResult.getResult() == 1) {
//            String username = System.getProperty("user.name");
//            //SEND EMAIL
//            File file = new File("C:\\Users\\" + username + "\\Documents\\from HMS\\hms_shipping.csv");
//            if (file.exists()) {
//                LOGGER.info("dh ada header");
//                FileWriter fileWriter = null;
//                FileReader fileReader = null;
//                
//                try {
//                    fileWriter = new FileWriter("C:\\Users\\" + username + "\\Documents\\from HMS\\hms_shipping.csv", true);
//                    fileReader = new FileReader("C:\\Users\\" + username + "\\Documents\\from HMS\\hms_shipping.csv");
//                    String targetLocation = "C:\\Users\\" + username + "\\Documents\\from HMS\\hms_shipping.csv";
//                    
//                    BufferedReader bufferedReader = new BufferedReader(fileReader); 
//                    String data = bufferedReader.readLine();
//                    StringBuilder buff = new StringBuilder();
//
//                    int row = 0;
//                    while(data != null){
//                        LOGGER.info("start reading file..........");
//                        buff.append(data).append(System.getProperty("line.separator"));
//                        System.out.println("dataaaaaaaaa : \n" + data);
//                        
//                        String[] split = data.split(",");
//                        IonicFtpShipping shipping = new IonicFtpShipping(
//                            split[0], split[1], split[2],
//                            split[3], split[4], split[5], 
//                            split[6], split[7], split[8],
//                            split[9], split[10], split[11],
//                            split[12], split[13] //date = [9], rack = [10], slot = [11], by = [12]
//                        );
//                            
//                        if(split[0].equals(refId)) {
//                            LOGGER.info(row + " : refId found...................." + data);
//                            CSV csv = new CSV();
//                            csv.open(new File(targetLocation));
//                            csv.put(9, row, "" + whShipping.getShippingDate());
//                            csv.put(10, row, "" + whShipping.getShippingRack());
//                            csv.put(11, row, "" + whShipping.getShippingSlot());
//                            csv.put(12, row, "" + whShipping.getShippingBy());
//                            csv.save(new File(targetLocation));  
//                        } else {
//                            LOGGER.info("refId not found........" + data);
//                        }
//                        data = bufferedReader.readLine();
//                        row++;
//                    }
//                    bufferedReader.close();
//                    fileReader.close();
//                } catch (Exception ee) {
//                    System.out.println("Error 1 occured while append the fileWriter");
//                } finally {
//                    try {
//                        fileWriter.close();
//                    } catch (IOException ie) {
//                        System.out.println("Error 2 occured while closing the fileWriter");
//                    }
//                }
//            } else {
//                LOGGER.info("File not exists.................");
//            }
//            
//            //send email
//            LOGGER.info("send email to warehouse");
//            
//            /*to get hostname*/
//            InetAddress ip;
//            String hostName ="";
//            try {
//                ip = InetAddress.getLocalHost();
//                hostName = ip.getHostName();
//            } catch (UnknownHostException ex) {
//                java.util.logging.Logger.getLogger(WhRequestController.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//            EmailSender emailSender = new EmailSender();
//            com.onsemi.hms.model.User user = new com.onsemi.hms.model.User();
//            user.setFullname(userSession.getFullname());
//            emailSender.htmlEmailWithAttachmentRequest(
//                servletContext,
//                user,                                                   //user name
//                "cdarsrel@gmail.com",                                   //to
//                "Status for Hardware Shipping from HMS",   //subject
//                "New shipping for the existing hardware has been made. Please go to this link " //msg
//                + "<a href=\"" + request.getScheme() + "://" + hostName + ":" + request.getServerPort() + request.getContextPath() + "/wh/whRequest/edit/" + refId + "\">HMS</a>"
//                + " for review."
//            );
//            
//            redirectAttrs.addFlashAttribute("success", messageSource.getMessage("general.label.update.success", args, locale));
//        } else {
//            LOGGER.info("----------------------" + queryResult.getResult());
//            redirectAttrs.addFlashAttribute("error", messageSource.getMessage("general.label.update.error", args, locale));
//        }
//        return "redirect:/wh/whShipping/";
//    }
//    
//    @RequestMapping(value = "/view/{whShippingId}", method = RequestMethod.GET)
//    public String view(
//            Model model,
//            HttpServletRequest request,
//            @PathVariable("whShippingId") String whShippingId
//    ) throws UnsupportedEncodingException {
//        LOGGER.info("Masuk view 1........");
//        String pdfUrl = URLEncoder.encode(request.getContextPath() + "/wh/whShipping/viewWhShippingPdf/" + whShippingId, "UTF-8");
//        String backUrl = servletContext.getContextPath() + "/wh/whShipping";
//        model.addAttribute("pdfUrl", pdfUrl);
//        model.addAttribute("backUrl", backUrl);
//        model.addAttribute("pageTitle", "Warehouse Management - Hardware Shipping");
//        LOGGER.info("Masuk view 2........");
//        return "pdf/viewer";
//    }
//
//    @RequestMapping(value = "/viewWhShippingPdf/{whShippingId}", method = RequestMethod.GET)
//    public ModelAndView viewWhShippingPdf(
//            Model model,
//            @PathVariable("whShippingId") String whShippingId
//    ) {
//        WhShippingDAO whShippingDAO = new WhShippingDAO();
//        LOGGER.info("Masuk 1........");
//        WhShipping whShipping = whShippingDAO.getWhShippingMergeWithRequest(whShippingId);
//        LOGGER.info("Masuk 2........");
//        return new ModelAndView("whShippingPdf", "whShipping", whShipping);
//    }
//}
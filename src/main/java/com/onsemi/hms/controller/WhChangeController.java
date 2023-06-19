package com.onsemi.hms.controller;

import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import com.onsemi.hms.dao.WhInventoryDAO;
import com.onsemi.hms.model.WhInventory;
import com.onsemi.hms.model.UserSession;
import com.onsemi.hms.tools.QueryResult;
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
@RequestMapping(value = "/whChange")
@SessionAttributes({"userSession"})
public class WhChangeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WhChangeController.class);
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

    @RequestMapping(value = "/{eqptType}", method = RequestMethod.GET)
    public String home(
            Model model,
            @ModelAttribute UserSession userSession,
            @PathVariable("eqptType") String eqptType
    ) {

        String eqptNew = "";
        if (eqptType.contains("Card")) {
            eqptNew = "Card";
        } else if (eqptType.contains("EQP_")) {
            eqptNew = "EQP";
        } else if (eqptType.contains("ATE_")) {
            eqptNew = "ATE";
        }else{
            eqptNew = eqptType;
        }

        WhInventoryDAO whInventoryDAO = new WhInventoryDAO();
        List<WhInventory> whInventoryList = whInventoryDAO.getWhInventoryListMergeRetrieveForMpToBoxNo(eqptNew);
        String groupId = userSession.getGroup();
//        LOGGER.info("groupId" + groupId);
        model.addAttribute("userSession", userSession);
        model.addAttribute("whInventoryList", whInventoryList);
        model.addAttribute("groupId", groupId);
        return "whInventory/change";
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
    @RequestMapping(value = "/editChange/{whInventoryId}", method = RequestMethod.GET)
    public String editChange(
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
        return "whInventory/editChange";
    }

    @RequestMapping(value = "/updateChange", method = RequestMethod.POST)
    public String updateChange(
            Model model,
            Locale locale,
            HttpServletRequest request,
            RedirectAttributes redirectAttrs,
            @ModelAttribute UserSession userSession,
            @RequestParam(required = false) String refId
    ) {

        //change mpToBox from 0 to 1
        WhInventory in = new WhInventory();
        in.setRefId(refId);
        in.setMpToBox("1");

        WhInventoryDAO inv = new WhInventoryDAO();
        QueryResult q = inv.updateWhInventoryMpToBox(in);

        return "redirect:/whChange/editChange/" + refId;
    }

    @RequestMapping(value = "/viewWhBarcodeStickerPdf/{refId}", method = RequestMethod.GET)
    public ModelAndView viewWhBarcodeStickerPdf(
            Model model,
            @PathVariable("refId") String refId
    ) {
        WhInventoryDAO whInventoryDAO = new WhInventoryDAO();
        WhInventory whInventory = whInventoryDAO.getWhInventoryMergeWithRetrievePdf(refId);
        return new ModelAndView("whBarcodeStickerPdf", "whInventory", whInventory);
    }

}

package com.onsemi.hms.controller;

import com.onsemi.hms.dao.InventoryMgtDAO;
import com.onsemi.hms.model.InventoryDetails;
import com.onsemi.hms.model.UserSession;
import java.util.List;
import javax.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping(value = "//inventory/inventoryMgt")
@SessionAttributes({"userSession"})
public class InventoryMgtController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WhRequestController.class);
    String[] args = {};
    
    @Autowired
    ServletContext servletContext;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String whRequest(
            Model model, @ModelAttribute UserSession userSession
    ) {
        InventoryMgtDAO inventoryMgtDAO = new InventoryMgtDAO();
        List<InventoryDetails> inventoryDetailsList = inventoryMgtDAO.getInventoryDetailsList();
        String groupId = userSession.getGroup();
        model.addAttribute("inventoryDetailsList", inventoryDetailsList);
        model.addAttribute("groupId", groupId);

        return "inventoryMgt/inventoryMgt";
    }
}

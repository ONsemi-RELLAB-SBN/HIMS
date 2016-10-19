package com.onsemi.hms.controller;

import com.onsemi.hms.dao.WhQueryDAO;
import com.onsemi.hms.model.WhQuery;
import java.util.List;
import javax.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping(value = "/wh/whQuery")
@SessionAttributes({"userSession"})
public class WhQueryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WhMpListController.class);
    String[] args = {};

    @Autowired
    private MessageSource messageSource;

    @Autowired
    ServletContext servletContext;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String whMpList(
            Model model
    ) {
        WhQueryDAO whQueryDAO = new WhQueryDAO();
//        List<WhQuery> whQueryList = whQueryDAO.getWhMpListMergeWithShippingAndRequestList();
//        model.addAttribute("whQueryList", whQueryList);
        return "whQuery/whQuery";
    }
}

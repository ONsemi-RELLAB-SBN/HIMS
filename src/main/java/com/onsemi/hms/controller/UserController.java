package com.onsemi.hms.controller;

import com.onsemi.hms.dao.UserDAO;
import com.onsemi.hms.model.User;
import com.onsemi.hms.model.UserAccess;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import com.onsemi.hms.model.UserSession;
import com.onsemi.hms.tools.EmailSender;
import com.onsemi.hms.tools.QueryResult;
import com.onsemi.hms.tools.SpmlUtil;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/user")
@SessionAttributes({"userSession"})
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    String[] args = {};

    @Autowired
    ServletContext servletContext;

    @Autowired
    private MessageSource messageSource;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String user(Model model, @ModelAttribute UserSession userSession) {
        return "user/user";
    }

    @RequestMapping(value = "/profile/{loginId}", method = RequestMethod.GET)
    public String profile(
            Model model,
            @PathVariable("loginId") String loginId
    ) {
        UserDAO userDAO = new UserDAO();
        UserAccess user = userDAO.getUserDetails(loginId);
        LOGGER.info(loginId + " ************************** ");
        LOGGER.info("MASUKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK 11111111111111111111111");
        model.addAttribute("user", user);
        return "user/profile";
    }
    
    @RequestMapping(value = "/password", method = {RequestMethod.GET, RequestMethod.POST})
    public String userPassword(
            Model model,
            Locale locale,
            RedirectAttributes redirectAttrs,
            @ModelAttribute UserSession userSession,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String currentPassword,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) String confirmPassword
    ) {
        UserDAO userDAO = new UserDAO();
        User currentUser = userDAO.getUser(userId);
        String link;
        if (BCrypt.checkpw(currentPassword, currentUser.getPassword())) {
            User user = new User();
            user.setId(userId);
            user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
            user.setModifiedBy(userSession.getId());
            userDAO = new UserDAO();
            QueryResult queryResult = userDAO.updatePassword(user);
            
            if (queryResult.getResult() == 1) {
                redirectAttrs.addFlashAttribute("success", messageSource.getMessage("admin.label.user.password.success", args, locale));
                link = "redirect:/";
            } else {
                redirectAttrs.addFlashAttribute("error", messageSource.getMessage("admin.label.user.password.error", args, locale));
                link = "redirect:/user/profile/" + userId;
            }
        } else {
            redirectAttrs.addFlashAttribute("error", messageSource.getMessage("admin.label.user.current_password.error", args, locale));
            link = "redirect:/user/profile/" + userId;
        }
        return link;
    }
    
    
//    @RequestMapping(value = "/userProfile", method = RequestMethod.POST)
//    public String userProfile(
//            Model model,
//            Locale locale,
//            HttpServletRequest request,
//            RedirectAttributes redirectAttrs,
//            @ModelAttribute UserSession userSession,
//            @RequestParam(required = false) String loginId,
//            @RequestParam(required = false) String fullname,
//            @RequestParam(required = false) String email,
//            @RequestParam(required = false) String code,
//            @RequestParam(required = false) String name,
//            @RequestParam(required = false) String groupCode,
//            @RequestParam(required = false) String groupId,
//            @RequestParam(required = false) String groupName,
//            @RequestParam(required = false) String isActive
//    ) {
//        LOGGER.info(userSession.getLoginId() + " ************************** " + loginId);
//        LOGGER.info("MASUKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK 2222222222222222222222");
//        User user = new User();
//        user.setLoginId(loginId);
//        user.setFullname(fullname);
//        user.setEmail(email);
//        user.setGroupCode(groupCode);
//        user.setGroupId(groupId);
//        user.setGroupName(groupName);
//        user.setIsActive(isActive);
//        return "redirect:/user/profile/userProfile/";
//    }

    @RequestMapping(value = "/email/text", method = RequestMethod.GET)
    public String textEmail(Model model, HttpServletRequest request) {
        EmailSender emailSender = new EmailSender();
        emailSender.textEmail(
                "sarmifairuz@gmail.com",
                "Hello From TEXTSPML",
                "Text Email Test"
        );
        return "redirect:/";
    }

    @RequestMapping(value = "/email/html", method = RequestMethod.GET)
    public String htmlEmail(Model model, @ModelAttribute UserSession userSession) {
        EmailSender emailSender = new EmailSender();
        com.onsemi.hms.model.User user = new com.onsemi.hms.model.User();
        user.setFullname(userSession.getFullname());
        emailSender.htmlEmail(
                servletContext,
                user,
                "sarmifairuz@gmail.com",
                "Hello From HTML SPML",
                "Text Email Test"
        );
        return "redirect:/";
    }

    @RequestMapping(value = "/email/mime", method = RequestMethod.GET)
    public String mimeEmail(Model model, @ModelAttribute UserSession userSession) {
        EmailSender emailSender = new EmailSender();
        com.onsemi.hms.model.User user = new com.onsemi.hms.model.User();
        user.setFullname(userSession.getFullname());
        emailSender.mimeEmail(
                servletContext,
                user,
                "sarmifairuz@gmail.com",
                "Hello From MIME SPML",
                "Text Email Test"
        );
        return "redirect:/";
    }

    @RequestMapping(value = "/email/login", method = RequestMethod.GET)
    public String emailLogin(
            Model model, 
            HttpServletRequest request, 
            @ModelAttribute UserSession userSession) {
        EmailSender emailSender = new EmailSender();
        com.onsemi.hms.model.User user = new com.onsemi.hms.model.User();
        user.setFullname(userSession.getFullname());
        String emailTitle = "HIMS-SF Login Information";
        String emailContent = "Login ID: " + userSession.getLoginId() + "<br/>"
                + "Password: ********<br/><br/>"
                + "Please login and complete your Profile Information.<br/><br/>"
                + "<a href='" + SpmlUtil.serverUrl(request) + "' target='_blank'>LOGIN HERE</a>";
        emailSender.htmlEmail(
                servletContext,
                user,
                "sarmifairuz@gmail.com",
                emailTitle,
                emailContent
        );
        return "redirect:/";
    }

}

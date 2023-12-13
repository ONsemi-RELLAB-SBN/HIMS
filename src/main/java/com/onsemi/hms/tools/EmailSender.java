package com.onsemi.hms.tools;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import com.onsemi.hms.dao.EmailDAO;
import com.onsemi.hms.model.Email;
import com.onsemi.hms.model.User;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

public class EmailSender extends SpringBeanAutowiringSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailSender.class);
    private final String emailTemplate = "resources/email";
//    private final String logoPath = "/resources/public/img/spml_all.png";
//    private final String logoPath = "/resources/img/cdars_logo.png";
    private final String logoPath = "/resources/img/warehouse.png";
    
//    private final String FILEPATHSF = "D:\\HIMS_CSV\\SF\\";
//    private final String FILEPATHRL = "D:\\HIMS_CSV\\RL\\";
    private final String FILEPATHSF = "D:\\Source Code\\archive\\CSV Import\\";
    private final String FILEPATHRL = "D:\\Source Code\\archive\\CSV Import\\";
    
    private final String FILEINV        = FILEPATHSF + "hms_inventory.csv";
    private final String STRING2        = FILEPATHSF + "hms_shipping.csv";
    private final String FILESHIPTO     = FILEPATHSF + "hms_wip_shipping.csv";
    private final String FILEVERIFIED   = FILEPATHSF + "hms_wip_verified.csv";
    private final String FILELOAD       = FILEPATHSF + "hms_wip_load.csv";
    private final String FILEUNLOAD     = FILEPATHSF + "hms_wip_unload.csv";
    private final String FILEINVENTORY  = FILEPATHSF + "hms_zero_inventory.csv";
    private final String FILESHIPBACK   = FILEPATHSF + "hms_zero_shipping.csv";
    
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment env;

    public EmailSender() {
    }

    public void textEmail(final String to, final String subject, final String msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom(env.getProperty("mail.sender"));
                message.setTo(to);
                message.setSubject(subject);
                message.setText(msg);
                mailSender.send(message);
            }
        }).start();
    }

    public void mimeEmail(final ServletContext servletContext, final User user, final String to, final String subject, final String msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MimeMessagePreparator preparator = new MimeMessagePreparator() {
                    @Override
                    public void prepare(MimeMessage mimeMessage) throws Exception {
                        MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                        message.setTo(to);
                        message.setFrom(env.getProperty("mail.sender"));
                        message.setSubject(subject);
                        Map model = new HashMap();
                        model.put("user", user.getFullname());
                        model.put("subject", subject);
                        model.put("message", msg);
                        Configuration freemarkerConfiguration = new Configuration(Configuration.VERSION_2_3_22);
                        freemarkerConfiguration.setServletContextForTemplateLoading(servletContext, emailTemplate);
                        String text = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate("template.html"), model);
                        message.setText(text, true);
                    }
                };
                mailSender.send(preparator);
            }
        }).start();
    }

    public void htmlEmail(final ServletContext servletContext, final User user, final String to, final String subject, final String msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HtmlEmail htmlEmail = new HtmlEmail();
                    EmailDAO emailDAO = new EmailDAO();
                    Email email = emailDAO.getEmail();

                    htmlEmail.setHostName(email.getHost());
                    htmlEmail.setSmtpPort(email.getPort());
                    htmlEmail.setAuthenticator(new DefaultAuthenticator(email.getUsername(), email.getPassword()));
//                    htmlEmail.setSSLOnConnect(true);
                    htmlEmail.setSSLOnConnect(false);
                    htmlEmail.setStartTLSEnabled(true); //16032022
                    htmlEmail.setDebug(true);

                    htmlEmail.setFrom(email.getSender());
                    htmlEmail.addTo(to);
                    htmlEmail.setSubject(subject);

                    String logo = servletContext.getRealPath(logoPath);
                    File logoFile = new File(logo);
                    String logoCid = htmlEmail.embed(logoFile);

                    Map model = new HashMap();
                    model.put("user", user.getFullname());
                    model.put("subject", subject);
                    model.put("message", msg);
                    model.put("logoCid", logoCid);
                    Configuration freemarkerConfiguration = new Configuration(Configuration.VERSION_2_3_22);
                    freemarkerConfiguration.setServletContextForTemplateLoading(servletContext, emailTemplate);
                    String msgContent = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate("template.html"), model);
                    htmlEmail.setHtmlMsg(msgContent);
                    String send = htmlEmail.send();
                    LOGGER.info("EMAIL SENDER: " + send);
                } catch (EmailException e) {
                    LOGGER.error(e.getMessage());
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                } catch (TemplateException e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }).start();
    }

    public void htmlEmail2(final ServletContext servletContext, final String user, final String to, final String subject, final String msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HtmlEmail htmlEmail = new HtmlEmail();
                    EmailDAO emailDAO = new EmailDAO();
                    Email email = emailDAO.getEmail();

                    htmlEmail.setHostName(email.getHost());
                    htmlEmail.setSmtpPort(email.getPort());
                    htmlEmail.setAuthenticator(new DefaultAuthenticator(email.getUsername(), email.getPassword()));
//                    htmlEmail.setSSLOnConnect(true);
                    htmlEmail.setSSLOnConnect(false);
                    htmlEmail.setStartTLSEnabled(true); //16032022
                    htmlEmail.setDebug(true);

                    htmlEmail.setFrom(email.getSender());
                    htmlEmail.addTo(to);
                    htmlEmail.setSubject(subject);

                    String logo = servletContext.getRealPath(logoPath);
                    File logoFile = new File(logo);
                    String logoCid = htmlEmail.embed(logoFile);

                    Map model = new HashMap();
                    model.put("user", user);
                    model.put("subject", subject);
                    model.put("message", msg);
                    model.put("logoCid", logoCid);
                    Configuration freemarkerConfiguration = new Configuration(Configuration.VERSION_2_3_22);
                    freemarkerConfiguration.setServletContextForTemplateLoading(servletContext, emailTemplate);
                    String msgContent = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate("template.html"), model);
                    htmlEmail.setHtmlMsg(msgContent);
                    String send = htmlEmail.send();
                    LOGGER.info("EMAIL SENDER: " + send);
                } catch (EmailException e) {
                    LOGGER.error(e.getMessage());
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                } catch (TemplateException e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }).start();
    }

    public void htmlEmailWithAttachmentRetrieve(final ServletContext servletContext, final String user, final String to, final String subject, final String msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HtmlEmail htmlEmail = new HtmlEmail();
                    EmailDAO emailDAO = new EmailDAO();
                    Email email = emailDAO.getEmail();

                    htmlEmail.setHostName(email.getHost());
                    htmlEmail.setSmtpPort(email.getPort());
                    htmlEmail.setAuthenticator(new DefaultAuthenticator(email.getUsername(), email.getPassword()));
//                    htmlEmail.setSSLOnConnect(true);
                    htmlEmail.setSSLOnConnect(false);
                    htmlEmail.setStartTLSEnabled(true); //16032022
                    htmlEmail.setDebug(true);

                    String username = System.getProperty("user.name");
                    File file = new File(FILEINV);

                    htmlEmail.setFrom(email.getSender());
                    htmlEmail.addTo(to);
                    htmlEmail.setSubject(subject);
                    htmlEmail.embed(file);

                    String logo = servletContext.getRealPath(logoPath);
                    File logoFile = new File(logo);
                    String logoCid = htmlEmail.embed(logoFile);

                    Map model = new HashMap();
                    model.put("user", user);
                    model.put("subject", subject);
                    model.put("message", msg);
                    model.put("logoCid", logoCid);
                    Configuration freemarkerConfiguration = new Configuration(Configuration.VERSION_2_3_22);
                    freemarkerConfiguration.setServletContextForTemplateLoading(servletContext, emailTemplate);
                    String msgContent = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate("template.html"), model);
                    htmlEmail.setHtmlMsg(msgContent);
                    String send = htmlEmail.send();
                    LOGGER.info("EMAIL SENDER: " + send);
                } catch (EmailException e) {
                    LOGGER.error(e.getMessage());
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                } catch (TemplateException e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }).start();
    }

    public void htmlEmailWithAttachmentRetrieve2(final ServletContext servletContext, final String user, final String to, final String subject, final String msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HtmlEmail htmlEmail = new HtmlEmail();
                    EmailDAO emailDAO = new EmailDAO();
                    Email email = emailDAO.getEmail();

                    htmlEmail.setHostName(email.getHost());
                    htmlEmail.setSmtpPort(email.getPort());
                    htmlEmail.setAuthenticator(new DefaultAuthenticator(email.getUsername(), email.getPassword()));
//                    htmlEmail.setSSLOnConnect(true);
                    htmlEmail.setSSLOnConnect(false);
                    htmlEmail.setStartTLSEnabled(true); //16032022
                    htmlEmail.setDebug(true);

                    String username = System.getProperty("user.name");
                    DateFormat dateFormat = new SimpleDateFormat("yyyyMMMdd");
                    Date date = new Date();
                    String todayDate = dateFormat.format(date);
                    File file = new File(FILEPATHSF + "Hardware Arrival Report (" + todayDate + ").xls");

                    htmlEmail.setFrom(email.getSender());
                    htmlEmail.addTo(to);
                    htmlEmail.setSubject(subject);
                    htmlEmail.embed(file);

                    String logo = servletContext.getRealPath(logoPath);
                    File logoFile = new File(logo);
                    String logoCid = htmlEmail.embed(logoFile);

                    Map model = new HashMap();
                    model.put("user", user);
                    model.put("subject", subject);
                    model.put("message", msg);
                    model.put("logoCid", logoCid);
                    Configuration freemarkerConfiguration = new Configuration(Configuration.VERSION_2_3_22);
                    freemarkerConfiguration.setServletContextForTemplateLoading(servletContext, emailTemplate);
                    String msgContent = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate("template.html"), model);
                    htmlEmail.setHtmlMsg(msgContent);
                    String send = htmlEmail.send();
                    LOGGER.info("EMAIL SENDER: " + send);
                } catch (EmailException e) {
                    LOGGER.error(e.getMessage());
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                } catch (TemplateException e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }).start();
    }

    public void htmlEmailWithAttachmentRequest2(final ServletContext servletContext, final String user, final String to, final String subject, final String msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HtmlEmail htmlEmail = new HtmlEmail();
                    EmailDAO emailDAO = new EmailDAO();
                    Email email = emailDAO.getEmail();

                    htmlEmail.setHostName(email.getHost());
                    htmlEmail.setSmtpPort(email.getPort());
                    htmlEmail.setAuthenticator(new DefaultAuthenticator(email.getUsername(), email.getPassword()));
//                    htmlEmail.setSSLOnConnect(true);
                    htmlEmail.setSSLOnConnect(false);
                    htmlEmail.setStartTLSEnabled(true); //16032022
                    htmlEmail.setDebug(true);

                    String username = System.getProperty("user.name");
                    DateFormat dateFormat = new SimpleDateFormat("yyyyMMMdd");
                    Date date = new Date();
                    String todayDate = dateFormat.format(date);
                    File file = new File(FILEPATHSF + "Hardware Shipping Report (" + todayDate + ").xls");

                    htmlEmail.setFrom(email.getSender());
                    htmlEmail.addTo(to);
                    htmlEmail.setSubject(subject);
                    htmlEmail.embed(file);

                    String logo = servletContext.getRealPath(logoPath);
                    File logoFile = new File(logo);
                    String logoCid = htmlEmail.embed(logoFile);

                    Map model = new HashMap();
                    model.put("user", user);
                    model.put("subject", subject);
                    model.put("message", msg);
                    model.put("logoCid", logoCid);
                    Configuration freemarkerConfiguration = new Configuration(Configuration.VERSION_2_3_22);
                    freemarkerConfiguration.setServletContextForTemplateLoading(servletContext, emailTemplate);
                    String msgContent = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate("template.html"), model);
                    htmlEmail.setHtmlMsg(msgContent);
                    String send = htmlEmail.send();
                    LOGGER.info("EMAIL SENDER: " + send);
                } catch (EmailException e) {
                    LOGGER.error(e.getMessage());
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                } catch (TemplateException e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }).start();
    }

    public void htmlEmailWithAttachmentShipping(final ServletContext servletContext, final String user, final String to, final String subject, final String msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HtmlEmail htmlEmail = new HtmlEmail();
                    EmailDAO emailDAO = new EmailDAO();
                    Email email = emailDAO.getEmail();

                    htmlEmail.setHostName(email.getHost());
                    htmlEmail.setSmtpPort(email.getPort());
                    htmlEmail.setAuthenticator(new DefaultAuthenticator(email.getUsername(), email.getPassword()));
//                    htmlEmail.setSSLOnConnect(true);
                    htmlEmail.setSSLOnConnect(false);
                    htmlEmail.setStartTLSEnabled(true); //16032022
                    htmlEmail.setDebug(true);

                    String username = System.getProperty("user.name");
                    File file = new File(STRING2);
//                    File file = new File("D:\\HIMS_CSV\\SF\\hms_shipping.csv");

                    htmlEmail.setFrom(email.getSender());
                    htmlEmail.addTo(to);
                    htmlEmail.setSubject(subject);
                    htmlEmail.embed(file);

                    String logo = servletContext.getRealPath(logoPath);
                    File logoFile = new File(logo);
                    String logoCid = htmlEmail.embed(logoFile);

                    Map model = new HashMap();
                    model.put("user", user);
                    model.put("subject", subject);
                    model.put("message", msg);
                    model.put("logoCid", logoCid);
                    Configuration freemarkerConfiguration = new Configuration(Configuration.VERSION_2_3_22);
                    freemarkerConfiguration.setServletContextForTemplateLoading(servletContext, emailTemplate);
                    String msgContent = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate("template.html"), model);
                    htmlEmail.setHtmlMsg(msgContent);
                    String send = htmlEmail.send();
                    LOGGER.info("EMAIL SENDER: " + send);
                } catch (EmailException e) {
                    LOGGER.error(e.getMessage());
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                } catch (TemplateException e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }).start();
    }

    public void htmlEmailWithAttachmentMpExpiry(final ServletContext servletContext, final String user, final String to, final String subject, final String msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HtmlEmail htmlEmail = new HtmlEmail();
                    EmailDAO emailDAO = new EmailDAO();
                    Email email = emailDAO.getEmail();

                    htmlEmail.setHostName(email.getHost());
                    htmlEmail.setSmtpPort(email.getPort());
                    htmlEmail.setAuthenticator(new DefaultAuthenticator(email.getUsername(), email.getPassword()));
//                    htmlEmail.setSSLOnConnect(true);
                    htmlEmail.setSSLOnConnect(false);
                    htmlEmail.setStartTLSEnabled(true); //16032022
                    htmlEmail.setDebug(true);

                    String username = System.getProperty("user.name");
                    DateFormat dateFormat = new SimpleDateFormat("yyyyMMMdd");
                    Date date = new Date();
                    String todayDate = dateFormat.format(date);
                    File file = new File(FILEPATHSF + "Material Pass Expiry Date Report Within 1 Month (" + todayDate + ").xls");

                    htmlEmail.setFrom(email.getSender());
                    htmlEmail.addTo(to);
                    htmlEmail.setSubject(subject);
                    htmlEmail.embed(file);

                    String logo = servletContext.getRealPath(logoPath);
                    File logoFile = new File(logo);
                    String logoCid = htmlEmail.embed(logoFile);

                    Map model = new HashMap();
                    model.put("user", user);
                    model.put("subject", subject);
                    model.put("message", msg);
                    model.put("logoCid", logoCid);
                    Configuration freemarkerConfiguration = new Configuration(Configuration.VERSION_2_3_22);
                    freemarkerConfiguration.setServletContextForTemplateLoading(servletContext, emailTemplate);
                    String msgContent = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate("template.html"), model);
                    htmlEmail.setHtmlMsg(msgContent);
                    String send = htmlEmail.send();
                    LOGGER.info("EMAIL SENDER: " + send);
                } catch (EmailException e) {
                    LOGGER.error(e.getMessage());
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                } catch (TemplateException e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }).start();
    }

    public void htmlEmailTableWithAttachment(final ServletContext servletContext, final String user, final String to, final String subject, final String msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HtmlEmail htmlEmail = new HtmlEmail();
                    EmailDAO emailDAO = new EmailDAO();
                    Email email = emailDAO.getEmail();

                    htmlEmail.setHostName(email.getHost());
                    htmlEmail.setSmtpPort(email.getPort());
                    htmlEmail.setAuthenticator(new DefaultAuthenticator(email.getUsername(), email.getPassword()));
//                    htmlEmail.setSSLOnConnect(true);
                    htmlEmail.setSSLOnConnect(false);
                    htmlEmail.setStartTLSEnabled(true); //16032022
                    htmlEmail.setDebug(true);

                    String username = System.getProperty("user.name");
                    DateFormat dateFormat = new SimpleDateFormat("yyyyMMMdd");
                    Date date = new Date();
                    String todayDate = dateFormat.format(date);
                    File file = new File(FILEPATHSF + "Material Pass Expiry Date Within 3 Days Report (" + todayDate + ").xls");

                    htmlEmail.setFrom(email.getSender());
                    htmlEmail.addTo(to);
                    htmlEmail.setSubject(subject);
                    htmlEmail.embed(file);

                    String logo = servletContext.getRealPath(logoPath);
                    File logoFile = new File(logo);
                    String logoCid = htmlEmail.embed(logoFile);

                    Map model = new HashMap();
                    model.put("user", user);
                    model.put("subject", subject);
                    model.put("message", msg);
                    model.put("logoCid", logoCid);
                    Configuration freemarkerConfiguration = new Configuration(Configuration.VERSION_2_3_22);
                    freemarkerConfiguration.setServletContextForTemplateLoading(servletContext, emailTemplate);
                    String msgContent = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate("template.html"), model);
                    htmlEmail.setHtmlMsg(msgContent);
                    String send = htmlEmail.send();
                    LOGGER.info("EMAIL SENDER: " + send);
                } catch (EmailException e) {
                    LOGGER.error(e.getMessage());
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                } catch (TemplateException e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }).start();
    }

    public void htmlEmailTableWithAttachmentMonth(final ServletContext servletContext, final String user, final String to, final String subject, final String msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HtmlEmail htmlEmail = new HtmlEmail();
                    EmailDAO emailDAO = new EmailDAO();
                    Email email = emailDAO.getEmail();

                    htmlEmail.setHostName(email.getHost());
                    htmlEmail.setSmtpPort(email.getPort());
                    htmlEmail.setAuthenticator(new DefaultAuthenticator(email.getUsername(), email.getPassword()));
//                    htmlEmail.setSSLOnConnect(true);
                    htmlEmail.setSSLOnConnect(false);
                    htmlEmail.setStartTLSEnabled(true); //16032022
                    htmlEmail.setDebug(true);

                    String username = System.getProperty("user.name");
                    DateFormat dateFormat = new SimpleDateFormat("yyyyMMMdd");
                    Date date = new Date();
                    String todayDate = dateFormat.format(date);
                    File file = new File(FILEPATHSF + "Material Pass Expiry Date Report Within 1 Month (" + todayDate + ").xls");

                    htmlEmail.setFrom(email.getSender());
                    htmlEmail.addTo(to);
                    htmlEmail.setSubject(subject);
                    htmlEmail.embed(file);

                    String logo = servletContext.getRealPath(logoPath);
                    File logoFile = new File(logo);
                    String logoCid = htmlEmail.embed(logoFile);

                    Map model = new HashMap();
                    model.put("user", user);
                    model.put("subject", subject);
                    model.put("message", msg);
                    model.put("logoCid", logoCid);
                    Configuration freemarkerConfiguration = new Configuration(Configuration.VERSION_2_3_22);
                    freemarkerConfiguration.setServletContextForTemplateLoading(servletContext, emailTemplate);
                    String msgContent = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate("template.html"), model);
                    htmlEmail.setHtmlMsg(msgContent);
                    String send = htmlEmail.send();
                    LOGGER.info("EMAIL SENDER: " + send);
                } catch (EmailException e) {
                    LOGGER.error(e.getMessage());
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                } catch (TemplateException e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }).start();
    }

    public void htmlEmailTableExceedUSL(final ServletContext servletContext, final String user, final String to, final String subject, final String msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HtmlEmail htmlEmail = new HtmlEmail();
                    EmailDAO emailDAO = new EmailDAO();
                    Email email = emailDAO.getEmail();

                    htmlEmail.setHostName(email.getHost());
                    htmlEmail.setSmtpPort(email.getPort());
                    htmlEmail.setAuthenticator(new DefaultAuthenticator(email.getUsername(), email.getPassword()));
//                    htmlEmail.setSSLOnConnect(true);
                    htmlEmail.setSSLOnConnect(false);
                    htmlEmail.setStartTLSEnabled(true); //16032022
                    htmlEmail.setDebug(true);

                    String username = System.getProperty("user.name");
                    DateFormat dateFormat = new SimpleDateFormat("ddMMMyyyy");
                    Date date = new Date();
                    String todayDate = dateFormat.format(date);
                    File file = new File(FILEPATHRL + "HIMS Upper Specs Limit Report (" + todayDate + ").xls");

                    htmlEmail.setFrom(email.getSender());
                    htmlEmail.addTo(to);
                    htmlEmail.setSubject(subject);
                    htmlEmail.embed(file);

                    String logo = servletContext.getRealPath(logoPath);
                    File logoFile = new File(logo);
                    String logoCid = htmlEmail.embed(logoFile);

                    Map model = new HashMap();
                    model.put("user", user);
                    model.put("subject", subject);
                    model.put("message", msg);
                    model.put("logoCid", logoCid);
                    Configuration freemarkerConfiguration = new Configuration(Configuration.VERSION_2_3_22);
                    freemarkerConfiguration.setServletContextForTemplateLoading(servletContext, emailTemplate);
                    String msgContent = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate("template.html"), model);
                    htmlEmail.setHtmlMsg(msgContent);
                    String send = htmlEmail.send();
                    LOGGER.info("EMAIL SENDER: " + send);
                } catch (EmailException e) {
                    LOGGER.error(e.getMessage());
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                } catch (TemplateException e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }).start();
    }

    public void htmlEmailTable(final ServletContext servletContext, final String user, final String[] to, final String subject, final String msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HtmlEmail htmlEmail = new HtmlEmail();
                    EmailDAO emailDAO = new EmailDAO();
                    Email email = emailDAO.getEmail();

                    htmlEmail.setHostName(email.getHost());
                    htmlEmail.setSmtpPort(email.getPort());
                    htmlEmail.setAuthenticator(new DefaultAuthenticator(email.getUsername(), email.getPassword()));
//                    htmlEmail.setSSLOnConnect(true);
                    htmlEmail.setSSLOnConnect(false);
                    htmlEmail.setStartTLSEnabled(true); //16032022
                    htmlEmail.setDebug(true);

                    htmlEmail.setFrom(email.getSender());
                    htmlEmail.addTo(to);
                    htmlEmail.setSubject(subject);

                    String logo = servletContext.getRealPath(logoPath);
                    File logoFile = new File(logo);
                    String logoCid = htmlEmail.embed(logoFile);

                    Map model = new HashMap();
                    model.put("user", user);
                    model.put("subject", subject);
                    model.put("message", msg);
                    model.put("logoCid", logoCid);
                    Configuration freemarkerConfiguration = new Configuration(Configuration.VERSION_2_3_22);
                    freemarkerConfiguration.setServletContextForTemplateLoading(servletContext, emailTemplate);
                    String msgContent = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate("template.html"), model);
                    htmlEmail.setHtmlMsg(msgContent);
                    String send = htmlEmail.send();
                    LOGGER.info("EMAIL SENDER: " + send);
                } catch (EmailException e) {
                    LOGGER.error(e.getMessage());
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                } catch (TemplateException e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }).start();
    }

    public void htmlEmailWithAttachmentTest(final ServletContext servletContext, final String user, final String[] to, final String subject, final String msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HtmlEmail htmlEmail = new HtmlEmail();
                    EmailDAO emailDAO = new EmailDAO();
                    Email email = emailDAO.getEmail();

                    htmlEmail.setHostName(email.getHost());
                    htmlEmail.setSmtpPort(email.getPort());
                    htmlEmail.setAuthenticator(new DefaultAuthenticator(email.getUsername(), email.getPassword()));
//                    htmlEmail.setSSLOnConnect(true);
                    htmlEmail.setSSLOnConnect(false);
                    htmlEmail.setStartTLSEnabled(true); //16032022
                    htmlEmail.setDebug(true);

                    String username = System.getProperty("user.name");
//                    File file = new File("D:\\HIMS_CSV\\SF\\hms_shipping.csv");
//                    File file = new File("C:\\HIMS_CSV\\SF\\hms_shipping.csv");
                    File file = new File(STRING2);

                    htmlEmail.setFrom(email.getSender());
                    htmlEmail.addTo(to);
                    htmlEmail.setSubject(subject);
                    htmlEmail.embed(file);

                    String logo = servletContext.getRealPath(logoPath);
                    File logoFile = new File(logo);
                    String logoCid = htmlEmail.embed(logoFile);

                    Map model = new HashMap();
                    model.put("user", user);
                    model.put("subject", subject);
                    model.put("message", msg);
                    model.put("logoCid", logoCid);
                    Configuration freemarkerConfiguration = new Configuration(Configuration.VERSION_2_3_22);
                    freemarkerConfiguration.setServletContextForTemplateLoading(servletContext, emailTemplate);
                    String msgContent = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate("template.html"), model);
                    htmlEmail.setHtmlMsg(msgContent);
                    String send = htmlEmail.send();
                    LOGGER.info("EMAIL SENDER: " + send);
                } catch (EmailException e) {
                    LOGGER.error(e.getMessage());
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                } catch (TemplateException e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }).start();
    }

    public void htmlEmailWithAttachmentTest2(final ServletContext servletContext, final String user, final String[] to, final String subject, final String msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HtmlEmail htmlEmail = new HtmlEmail();
                    EmailDAO emailDAO = new EmailDAO();
                    Email email = emailDAO.getEmail();

                    htmlEmail.setHostName(email.getHost());
                    htmlEmail.setSmtpPort(email.getPort());
                    htmlEmail.setAuthenticator(new DefaultAuthenticator(email.getUsername(), email.getPassword()));
//                    htmlEmail.setSSLOnConnect(true);
                    htmlEmail.setSSLOnConnect(false);
                    htmlEmail.setStartTLSEnabled(true); //16032022
                    htmlEmail.setDebug(true);

                    String username = System.getProperty("user.name");
//                    File file = new File("D:\\HIMS_CSV\\SF\\hms_inventory.csv");
                    File file = new File(FILEINV);

                    htmlEmail.setFrom(email.getSender());
                    htmlEmail.addTo(to);
                    htmlEmail.setSubject(subject);
                    htmlEmail.embed(file);

                    String logo = servletContext.getRealPath(logoPath);
                    File logoFile = new File(logo);
                    String logoCid = htmlEmail.embed(logoFile);

                    Map model = new HashMap();
                    model.put("user", user);
                    model.put("subject", subject);
                    model.put("message", msg);
                    model.put("logoCid", logoCid);
                    Configuration freemarkerConfiguration = new Configuration(Configuration.VERSION_2_3_22);
                    freemarkerConfiguration.setServletContextForTemplateLoading(servletContext, emailTemplate);
                    String msgContent = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate("template.html"), model);
                    htmlEmail.setHtmlMsg(msgContent);
                    String send = htmlEmail.send();
                    LOGGER.info("EMAIL SENDER: " + send);
                } catch (EmailException e) {
                    LOGGER.error(e.getMessage());
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                } catch (TemplateException e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }).start();
    }
    
    public void wipEmailVerify(final ServletContext servletContext, final String user, final String[] to, final String subject, final String msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HtmlEmail htmlEmail = new HtmlEmail();
                    EmailDAO emailDAO = new EmailDAO();
                    Email email = emailDAO.getEmail();

                    htmlEmail.setHostName(email.getHost());
                    htmlEmail.setSmtpPort(email.getPort());
                    htmlEmail.setAuthenticator(new DefaultAuthenticator(email.getUsername(), email.getPassword()));
//                    htmlEmail.setSSLOnConnect(true);
                    htmlEmail.setSSLOnConnect(false);
                    htmlEmail.setStartTLSEnabled(true);
                    htmlEmail.setDebug(true);

                    String username = System.getProperty("user.name");
                    File file = new File(FILEVERIFIED);

                    htmlEmail.setFrom(email.getSender());
                    htmlEmail.addTo(to);
                    htmlEmail.setSubject(subject);
                    htmlEmail.embed(file);

                    String logo = servletContext.getRealPath(logoPath);
                    File logoFile = new File(logo);
                    String logoCid = htmlEmail.embed(logoFile);

                    Map model = new HashMap();
                    model.put("user", user);
                    model.put("subject", subject);
                    model.put("message", msg);
                    model.put("logoCid", logoCid);
                    Configuration freemarkerConfiguration = new Configuration(Configuration.VERSION_2_3_22);
                    freemarkerConfiguration.setServletContextForTemplateLoading(servletContext, emailTemplate);
                    String msgContent = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate("templatewip.html"), model);
                    htmlEmail.setHtmlMsg(msgContent);
                    String send = htmlEmail.send();
                    LOGGER.info("EMAIL SENDER: " + send);
                } catch (EmailException e) {
                    LOGGER.error(e.getMessage());
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                } catch (TemplateException e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }).start();
    }
    
    public void wipEmailShip(final ServletContext servletContext, final String user, final String[] to, final String subject, final String msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HtmlEmail htmlEmail = new HtmlEmail();
                    EmailDAO emailDAO = new EmailDAO();
                    Email email = emailDAO.getEmail();

                    htmlEmail.setHostName(email.getHost());
                    htmlEmail.setSmtpPort(email.getPort());
                    htmlEmail.setAuthenticator(new DefaultAuthenticator(email.getUsername(), email.getPassword()));
//                    htmlEmail.setSSLOnConnect(true);
                    htmlEmail.setSSLOnConnect(false);
                    htmlEmail.setStartTLSEnabled(true);
                    htmlEmail.setDebug(true);

                    String username = System.getProperty("user.name");
                    File file = new File(FILESHIPTO);

                    htmlEmail.setFrom(email.getSender());
                    htmlEmail.addTo(to);
                    htmlEmail.setSubject(subject);
                    htmlEmail.embed(file);

                    String logo = servletContext.getRealPath(logoPath);
                    File logoFile = new File(logo);
                    String logoCid = htmlEmail.embed(logoFile);

                    Map model = new HashMap();
                    model.put("user", user);
                    model.put("subject", subject);
                    model.put("message", msg);
                    model.put("logoCid", logoCid);
                    Configuration freemarkerConfiguration = new Configuration(Configuration.VERSION_2_3_22);
                    freemarkerConfiguration.setServletContextForTemplateLoading(servletContext, emailTemplate);
                    String msgContent = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate("templatewip.html"), model);
                    htmlEmail.setHtmlMsg(msgContent);
                    String send = htmlEmail.send();
                    LOGGER.info("EMAIL SENDER: " + send);
                } catch (EmailException e) {
                    LOGGER.error(e.getMessage());
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                } catch (TemplateException e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }).start();
    }
    
    public void wipEmail(final ServletContext servletContext, final String user, final String[] to, final String subject, final String msg, final String mode) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HtmlEmail htmlEmail = new HtmlEmail();
                    EmailDAO emailDAO = new EmailDAO();
                    Email email = emailDAO.getEmail();

                    htmlEmail.setHostName(email.getHost());
                    htmlEmail.setSmtpPort(email.getPort());
                    htmlEmail.setAuthenticator(new DefaultAuthenticator(email.getUsername(), email.getPassword()));
//                    htmlEmail.setSSLOnConnect(true);
                    htmlEmail.setSSLOnConnect(false);
                    htmlEmail.setStartTLSEnabled(true);
                    htmlEmail.setDebug(false);

                    String username = System.getProperty("user.name");

                    htmlEmail.setFrom(email.getSender());
                    htmlEmail.addTo(to);
                    htmlEmail.setSubject(subject);
//                    htmlEmail.embed(file);

                    String logo = servletContext.getRealPath(logoPath);
                    File logoFile = new File(logo);
                    String logoCid = htmlEmail.embed(logoFile);

                    Map model = new HashMap();
                    model.put("user", user);
                    model.put("subject", subject);
                    model.put("message", msg);
                    model.put("logoCid", logoCid);
                    Configuration freemarkerConfiguration = new Configuration(Configuration.VERSION_2_3_22);
                    freemarkerConfiguration.setServletContextForTemplateLoading(servletContext, emailTemplate);
                    String msgContent = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate("templatewip.html"), model);
                    htmlEmail.setHtmlMsg(msgContent);
                    String send = htmlEmail.send();
                    for (String penerima : to) {
                        LOGGER.info("EMAIL >> " + penerima);
                    }
                    LOGGER.info("EMAIL SUBJECT : " + subject);
                    LOGGER.info("EMAIL CONTENT : " + msg);
                } catch (EmailException e) {
                    LOGGER.error(e.getMessage());
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                } catch (TemplateException e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }).start();
    }
    
    public void wipEmailWithAttach(final ServletContext servletContext, final String user, final String[] to, final String subject, final String msg, final String mode) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HtmlEmail htmlEmail = new HtmlEmail();
                    EmailDAO emailDAO = new EmailDAO();
                    Email email = emailDAO.getEmail();

                    htmlEmail.setHostName(email.getHost());
                    htmlEmail.setSmtpPort(email.getPort());
                    htmlEmail.setAuthenticator(new DefaultAuthenticator(email.getUsername(), email.getPassword()));
//                    htmlEmail.setSSLOnConnect(true);
                    htmlEmail.setSSLOnConnect(false);
                    htmlEmail.setStartTLSEnabled(true);
                    htmlEmail.setDebug(false);

                    String username = System.getProperty("user.name");
                    String filePath = "";
                    
                    if (mode.equalsIgnoreCase("VERIFY")) {
                        filePath = FILEVERIFIED;
                    } else if (mode.equalsIgnoreCase("SHIP")) {
                        filePath = FILESHIPTO;
                    } else if (mode.equalsIgnoreCase("LOAD")) {
                        filePath = FILELOAD;
                    } else if (mode.equalsIgnoreCase("UNLOAD")) {
                        filePath = FILEUNLOAD;
                    } else if (mode.equalsIgnoreCase("INVENTORY")) {
                        filePath = FILEINVENTORY;
                    } else if (mode.equalsIgnoreCase("SHIPBACK")) {
                        filePath = FILESHIPBACK;
                    } else if (mode.equalsIgnoreCase("")) {
                        
                    }
                    
//                    switch (mode) {
//                        case "VERIFY":
//                            filePath = FILEPATHVERIFY;
//                            break;
//                        case "SHIP":
//                            filePath = FILEPATHSHIP;
//                            break;
//                        case "LOAD":
//                            filePath = FILEPATHLOAD;
//                            break;
//                        case "UNLOAD":
//                            filePath = FILEPATHUNLOAD;
//                            break;
//                        default:
//                            break;
//                    }
                    
                    File file = new File(filePath);

                    htmlEmail.setFrom(email.getSender());
                    htmlEmail.addTo(to);
                    htmlEmail.setSubject(subject);
                    htmlEmail.embed(file);

                    String logo = servletContext.getRealPath(logoPath);
                    File logoFile = new File(logo);
                    String logoCid = htmlEmail.embed(logoFile);

                    Map model = new HashMap();
                    model.put("user", user);
                    model.put("subject", subject);
                    model.put("message", msg);
                    model.put("logoCid", logoCid);
                    Configuration freemarkerConfiguration = new Configuration(Configuration.VERSION_2_3_22);
                    freemarkerConfiguration.setServletContextForTemplateLoading(servletContext, emailTemplate);
                    String msgContent = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate("templatewip.html"), model);
                    htmlEmail.setHtmlMsg(msgContent);
                    String send = htmlEmail.send();
                    for (String penerima : to) {
                        LOGGER.info("EMAIL >> " + penerima);
                    }
                    LOGGER.info("EMAIL SUBJECT : " + subject);
                    LOGGER.info("EMAIL CONTENT : " + msg);
                } catch (EmailException e) {
                    LOGGER.error(e.getMessage());
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                } catch (TemplateException e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }).start();
    }

}
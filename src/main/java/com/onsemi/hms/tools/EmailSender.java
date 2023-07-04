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
    //private final String logoPath = "/resources/public/img/spml_all.png";
    private final String logoPath = "/resources/img/cdars_logo.png";
    
    private static final String emailTest = "zbqb9x@onsemi.com";
    private static final String FILEPATH = "D:\\Source Code\\archive\\CSV Import\\cdars_wip_shipping.csv";
    private static final String FILEPATH2 = "D:\\Source Code\\archive\\CSV Import\\hms_wip_shipping.csv";

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
                    File file = new File("D:\\HIMS_CSV\\SF\\hms_inventory.csv");

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
                    File file = new File("D:\\HIMS_CSV\\SF\\Hardware Arrival Report (" + todayDate + ").xls");

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
                    File file = new File("D:\\HIMS_CSV\\SF\\Hardware Shipping Report (" + todayDate + ").xls");

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
                    File file = new File("C:\\HIMS_CSV\\SF\\hms_shipping.csv");
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
                    File file = new File("D:\\HIMS_CSV\\SF\\Material Pass Expiry Date Report Within 1 Month (" + todayDate + ").xls");

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
                    File file = new File("D:\\HIMS_CSV\\SF\\Material Pass Expiry Date Within 3 Days Report (" + todayDate + ").xls");

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
                    File file = new File("D:\\HIMS_CSV\\SF\\Material Pass Expiry Date Report Within 1 Month (" + todayDate + ").xls");

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
                    File file = new File("D:\\HIMS_CSV\\RL\\HIMS Upper Specs Limit Report (" + todayDate + ").xls");

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
                    File file = new File(FILEPATH);

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
                    File file = new File("D:\\HIMS_CSV\\SF\\hms_inventory.csv");

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
}

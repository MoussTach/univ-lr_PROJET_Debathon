package fr.univlr.debathon.server.mailmanager;

import fr.univlr.debathon.log.generate.CustomLogger;
import fr.univlr.debathon.taskmanager.Task_Custom;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MailData {

    private static final CustomLogger LOGGER = CustomLogger.create(MailData.class.getName());

    private final MailInfos infos;

    private String username;
    private String password;

    private String host;
    private String port;

    private final List<BodyPart> attachedFiles = new ArrayList<>();

    public MailData() {
        this.infos = new MailInfos(this);

        this.username = MailManager.MAILDATA.get("From_User");
        this.password = MailManager.MAILDATA.get("From_Password");

        this.host = MailManager.MAILDATA.get("AInfos_Host");
        this.port = MailManager.MAILDATA.get("AInfos_Port");
    }

    /**
     * Method to send a mail with all gathered information.
     *
     * @return - {@link Task} - return the task that represent the mail to send it
     *
     * @author Gaetan Brenckle
     */
    public Task<?> buildMail() {
        return new Task_Custom<Void>("send a mail") {
            @Override
            protected Void call_Task() throws MessagingException {

                try {

                    if (LOGGER.isInfoEnabled()) {
                        LOGGER.info("Setup mail");
                    }

                    final Properties props = new Properties();
                    props.put("mail.smtp.host", host);
                    props.put("mail.smtp.port", port);
                    props.put("mail.smtp.auth", "true");
                    props.put("mail.smtp.starttls.enable", "true");


                    Session session = Session.getInstance(props);

                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(infos.getFrom()));

                    try {
                        for (String to : infos.toProperty()) {
                            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
                        }

                        for (String bcc : infos.bccProperty()) {
                            message.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc));
                        }
                    } catch (MessagingException e) {
                        if (LOGGER.isErrorEnabled()) {
                            LOGGER.error("Fail to add recipient", e);
                        }
                    }

                    message.setSubject(infos.getSubject());

                    Multipart mainPart = new MimeMultipart();

                    // Create the message
                    MimeBodyPart body = new MimeBodyPart();
                    body.setContent(infos.getBody(), "text/html; charset=utf-8");
                    mainPart.addBodyPart(body);

                    //additional files
                    for (BodyPart file : attachedFiles) {
                        mainPart.addBodyPart(file);
                    }

                    message.setContent(mainPart);

                    Transport.send(message, username, password);
                    if (LOGGER.isInfoEnabled()) {
                        LOGGER.info("Sent message successfully....");
                    }
                    infos.isSendProperty().set(true);

                } catch (Exception e) {
                    if (LOGGER.isErrorEnabled()) {
                        LOGGER.error("Error when building mail", e);
                    }
                }
                return null;
            }
        };
    }

    /**
     * Method to send a mail with all gathered information.
     *
     * @return - {@link Task} - return the task that represent the mail to send it
     *
     * @author Gaetan Brenckle
     */
    public Task<?> build_SMTPMail() {
        return new Task_Custom<Void>("send a mail") {
            @Override
            protected Void call_Task() throws MessagingException {

                final Properties props = new Properties();
                props.put("mail.smtp.host", host);
                props.put("mail.smtp.port", port);
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");


                Session session = Session.getInstance(props,
                        new jakarta.mail.Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(username, password);
                            }
                        });

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(infos.getFrom()));

                try {
                    for (String to : infos.toProperty()) {
                        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
                    }

                    for (String bcc : infos.bccProperty()) {
                        message.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc));
                    }
                } catch (MessagingException e) {
                    if (LOGGER.isErrorEnabled()) {
                        LOGGER.error("Fail to add recipient", e);
                    }
                }

                message.setSubject(infos.getSubject());

                Multipart mainPart = new MimeMultipart();

                // Create the message
                BodyPart body = new MimeBodyPart();
                body.setContent(infos.getBody(), "text/html; charset=UTF-8");
                mainPart.addBodyPart(body);

                //additional files
                for (BodyPart file : attachedFiles) {
                    mainPart.addBodyPart(file);
                }

                // Send the complete message parts
                message.setContent(mainPart);
                Transport.send(message);

                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("Sent message successfully....");
                }
                infos.isSendProperty().set(true);
                return null;
            }
        };
    }

    /**
     * getter of the variable infos
     *
     * @author Gaetan Brenckle
     *
     * @return {@link MailInfos} - return the variable infos
     */
    public MailInfos getInfos() {
        return infos;
    }


    /**
     * Setter for the variable username.
     *
     * @param username - {@link String} - username of the job class.
     * @return {@link MailData} - builder pattern
     */
    public MailData setUsername(String username) {
        this.username = username;
        return this;
    }

    /**
     * Setter for the variable password.
     *
     * @param password - {@link String} - password of the job class.
     * @return {@link MailData} - builder pattern
     */
    public MailData setPassword(String password) {
        this.password = password;
        return this;
    }

    /**
     * Setter for the variable host.
     *
     * @param host - {@link String} - host of the job class.
     * @return {@link MailData} - builder pattern
     */
    public MailData setHost(String host) {
        this.host = host;
        return this;
    }

    /**
     * Setter for the variable port.
     *
     * @param port - {@link String} - port of the job class.
     * @return {@link MailData} - builder pattern
     */
    public MailData setPort(String port) {
        this.port = port;
        return this;
    }

    /**
     * Setter for the variable attachedFiles.
     *
     * @param attachedFiles - {@link List} - attachedFiles of the job class.
     * @return {@link MailData} - builder pattern
     */
    public MailData setAttachedFiles(List<BodyPart> attachedFiles) {
        this.attachedFiles.addAll(attachedFiles);
        return this;
    }


    @Override
    public String toString() {
        return String.format("subject: %s" +
                        "\nTo: %s" +
                        "\nFrom : %s",
                this.infos.getSubject(),
                this.infos.toProperty().toString(),
                this.infos.getFrom());
    }
}

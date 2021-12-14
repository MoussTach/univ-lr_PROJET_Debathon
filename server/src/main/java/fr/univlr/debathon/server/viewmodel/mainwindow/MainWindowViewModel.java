package fr.univlr.debathon.server.viewmodel.mainwindow;

import fr.univlr.debathon.log.generate.CustomLogger;
import fr.univlr.debathon.server.Main;
import fr.univlr.debathon.server.communication.Server;
import fr.univlr.debathon.server.mailmanager.MailData;
import fr.univlr.debathon.server.mailmanager.MailManager;
import fr.univlr.debathon.server.viewmodel.ViewModel_SceneCycle;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.BodyPart;
import jakarta.mail.internet.MimeBodyPart;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

/**
 * ViewModel of the view {@link fr.univlr.debathon.server.view.mainwindow.MainWindowView}.
 * This ViewModel process some function for the whole application.
 *
 * @author Gaetan Brenckle
 */
public class MainWindowViewModel extends ViewModel_SceneCycle {

    private static final CustomLogger LOGGER = CustomLogger.create(MainWindowViewModel.class.getName());

    private final StringProperty tfKey_value = new SimpleStringProperty();

    /**
     * Default constructor
     *
     * @author Gaetan Brenckle
     */
    public MainWindowViewModel() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][constructor] Creation of the MainWindowViewModel() object.");
        }

        tfKey_value.set(Server.CREATERIGHTS_KEY);
        this.sendMail();
    }

    private void sendMail() {

        MailData mailStatistics = new MailData();
        mailStatistics.getInfos().setSubject("Statistics");
        mailStatistics.getInfos().setBody(
                new StringBuilder()
                        .append("<html><body>")
                        .append("Bonjour,<br>")
                        .append("Veuillez trouver ci-joint les statistiques %s.<br>")
                        .append("En vous souhaitant bonne réception.<br><br>")
                        .append("Pour visualiser le document joint, vous pouvez télécharger gratuitement l'application \"Adobe Reader\" à <a href=\"https://get.adobe.com/fr/reader/\">partir du site Web Adobe</a><br><br>")
                        .append("Cordialement<br><br>")
                        .append("L'application Debathon<br>")
                        .append("</body></html>")
                        .toString()
        );

        //Use properties that can connect to send the mail
        mailStatistics.getInfos().setFrom(MailManager.MAILDATA.get("From_User"));

        mailStatistics.setUsername(MailManager.MAILDATA.get("From_User"));
        mailStatistics.setPassword(MailManager.MAILDATA.get("From_Password"));

        mailStatistics.setHost(MailManager.MAILDATA.get("AInfos_Host"));
        mailStatistics.setPort(MailManager.MAILDATA.get("AInfos_Port"));

        mailStatistics.getInfos().bccProperty().addAll(Arrays.asList("gaetan.brenckle@etudiant.univ-lr.fr", "gaetan.brenckle@outlook.fr"));

        //Get the PDF locally
        final Properties properties = new Properties();
        try {
            properties.load(getClass().getResourceAsStream("/properties/default.properties"));
            final String name_destination_folder = properties.getProperty("name_dataCreated_folder");
            final String pdfDest = String.format(
                    "%s/.%s/%s",
                    System.getProperty("user.home"),
                    properties.getProperty("name"),
                    name_destination_folder
            );

            //Check if the folder exists and create one if not
            File destination_directory = new File(pdfDest);
            boolean destination_directory_exist = destination_directory.exists();
            if (!destination_directory_exist) {
                destination_directory.mkdir();
            }

            final String namePdfFile = "testPDF.pdf";
            final String pdfDest_file = String.format(
                    "%s/%s",
                    pdfDest,
                    namePdfFile
            );

            BodyPart messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(pdfDest_file);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(namePdfFile);

            mailStatistics.setAttachedFiles(Collections.singletonList(messageBodyPart));

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Finished to build mail");
            }

            Main.MAILMANAGER.sendMails(Collections.singletonList(mailStatistics));

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Mail send");
            }

        } catch (Exception e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Fail to create PDF", e);
            }
        }
    }


    /**
     * Property of the variable tfKey_value.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable tfKey_value.
     */
    public StringProperty tfKey_valueProperty() {
        return tfKey_value;
    }


    @Override
    public void onViewAdded_Cycle() {
        // default implementation
    }

    @Override
    public void onViewRemoved_Cycle() {
        // default implementation
    }
}
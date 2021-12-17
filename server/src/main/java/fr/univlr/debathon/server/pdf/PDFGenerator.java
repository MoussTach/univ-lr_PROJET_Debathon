
package fr.univlr.debathon.server.pdf;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import fr.univlr.debathon.log.generate.CustomLogger;
import fr.univlr.debathon.server.Main;
import fr.univlr.debathon.server.mailmanager.MailData;
import fr.univlr.debathon.server.mailmanager.MailManager;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.BodyPart;
import jakarta.mail.internet.MimeBodyPart;
import javafx.application.Platform;
import org.jfree.chart.JFreeChart;

import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

public class PDFGenerator {
    private static final CustomLogger LOGGER = CustomLogger.create(PDFGenerator.class.getName());

    public static PDFGenerator instance;

    public PDFGenerator(){}

    public static PDFGenerator getInstance(){
        if (instance == null) {
            instance = new PDFGenerator();
        }
        return instance;
    }

    public void requestPDF(int id_debat){
        List<PDFquestion> questions = PDFdata.getRequest1(id_debat);
        List<String> mails = PDFdata.getEmail(id_debat);
        String path = getPDF(questions, id_debat,mails);
        Platform.runLater(() -> {
            sendMail(path,mails);
        });

    }

    //Methode pour creer un PDF pour un debat avec la liste des questions
    public String getPDF(List<PDFquestion> questions,int id_debat,List<String> mails){
        try {
            final Properties properties = new Properties();

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

            final String namePdfFile = String.format("%s_%d.pdf", java.time.LocalDate.now().toString(), id_debat);
            final String pdfDest_file = String.format(
                    "%s/%s",
                    pdfDest,
                    namePdfFile
            );
            
            try (FileOutputStream fos = new FileOutputStream(pdfDest_file)) {

                //Creation document pdf
                Document pdf = new Document(PageSize.A4);

                //Creation list de chart avec les questions
                List<JFreeChart> charts = ChartGenerator.getInstance().genAllPieCharts(questions);

                //Writer pour le pdf
                PdfWriter writer = PdfWriter.getInstance(pdf, fos);

                //Ouverture du pdf
                pdf.open();

                //Creation pdf event pour border
                ParagraphBorder border = new ParagraphBorder();

                //Ajout de l'envent au writer
                writer.setPageEvent(border);
                pdf.addTitle("Récapitulatif");

                //Creation paragraphe titre
                Paragraph p = new Paragraph(String.format("%s -- %d",LocalDateTime.now().toString() ,id_debat));
                p.setAlignment(Element.ALIGN_CENTER);
                pdf.add(p);

                //SECTION POUR LES CHARTS
                for (JFreeChart chart:charts) {
                    //Creation buffer pour ajout image sur pdf
                    BufferedImage buffer = chart.createBufferedImage((int) (pdf.getPageSize().getWidth()/2),(int) (pdf.getPageSize().getHeight()/5));
                    Image image = Image.getInstance(writer, buffer,1.0f);
                    //redimensionnemnt image et changement position
                    image.scaleToFit(PageSize.A4.getWidth()/1.5f, image.getScaledWidth()/1.5f);
                    float x = (PageSize.A4.getWidth() - image.getScaledWidth()) / 2;
                    image.setAbsolutePosition(x, image.getAbsoluteY());
                    //Ajout map
                    pdf.add(image);
                    //Creation espace apres chart
                    Paragraph emptySpace = new Paragraph("");
                    emptySpace.setSpacingBefore(50);
                    emptySpace.setSpacingAfter(50);
                    pdf.add(emptySpace);
                }

                if (!questions.isEmpty()) {

                    //Page suivante
                    pdf.newPage();

                    //SECTION POUR LES COMMENTAIRES LIKES / DISLIKES
                    for (PDFquestion q : questions) {
                        //Creation chunk pour souligner text
                        Chunk chunk_titre = new Chunk("Question "+q.getIdQuestion()+" - "+q.getLabelQuestion());
                        chunk_titre.setUnderline(0.2f,-2f);
                        //Creation paragraphe - Ajout - Centrage
                        Paragraph titre = new Paragraph();
                        titre.add(chunk_titre);
                        titre.setAlignment(Element.ALIGN_CENTER);

                        titre.setSpacingBefore(20);
                        titre.setSpacingAfter(20);

                        //Ajout du titre
                        pdf.add(titre);
                        //Verification si au moins un comm est like ou dislike
                        System.out.println(questions.size());
                        System.out.println(q.getMost_nb_likes());
                        System.out.println(q.getMost_nb_dislikes());
                        if(q.getMost_nb_likes()>0 || q.getMost_nb_dislikes()>0){
                            //Verification si un commentaire est like
                            if(q.getMost_nb_likes()>0){
                                //CREATION PARAGRAPHE POUR COMMENTAIRE LE PLUS LIKE
                                //Creation paragraphe - Centrage - Ajout spacing
                                Paragraph p_like = new Paragraph(String.format("Commentaire ayant reçu le plus de Likes (%d)",q.getMost_nb_likes()));
                                p_like.setAlignment(Element.ALIGN_CENTER);
                                p_like.setSpacingAfter(8);
                                //Recup commentaire le plus like et remplacement " par '
                                String like_txt = q.getMost_like_comment();
                                if(like_txt != null){
                                    like_txt = q.getMost_like_comment().replace('\"','\'');
                                }
                                Paragraph p_like_txt = new Paragraph(like_txt);
                                p_like_txt.setAlignment(Element.ALIGN_CENTER);
                                pdf.add(p_like);
                                //Depart bordure
                                border.setActive(true);
                                //Ajout commentaire
                                pdf.add(p_like_txt);
                                //Fin bordure
                                border.setActive(false);
                            }
                            else{
                                Paragraph no_like = new Paragraph("Aucun commentaire liké",new
                                        Font(Font.FontFamily.HELVETICA, 12, Font.ITALIC));
                                pdf.add(no_like);
                            }

                            //Creation espace vide
                            Paragraph emptySpace = new Paragraph("");
                            emptySpace.setSpacingBefore(5);
                            emptySpace.setSpacingAfter(5);
                            pdf.add(emptySpace);

                            if(q.getMost_nb_dislikes()>0){
                                //CREATION PARAGRAPHE POUR COMMENTAIRE LE PLUS DISLIKE
                                //Creation paragraphe - Centrage - Ajout spacing
                                Paragraph p_dislike = new Paragraph("Commentaire ayant reçu le plus de Dislikes ( "+q.getMost_nb_dislikes()+" )" );
                                p_dislike.setAlignment(Element.ALIGN_CENTER);
                                p_dislike.setSpacingAfter(8);
                                //Recup commentaire le plus dislike et remplacement " par '
                                String dislike_txt = q.getMost_dislike_comment();
                                if(dislike_txt != null){
                                    dislike_txt = q.getMost_dislike_comment().replace('\"','\'');
                                }
                                Paragraph p_dislike_txt = new Paragraph(dislike_txt);
                                p_dislike_txt.setAlignment(Element.ALIGN_CENTER);
                                pdf.add(p_dislike);
                                //Depart bordure
                                border.setActive(true);
                                //Ajout commentaire
                                pdf.add(p_dislike_txt);
                                //Fin bordure
                                border.setActive(false);
                            }
                            else{
                                Paragraph no_dislike = new Paragraph("Aucun commentaire disliké",new
                                        Font(Font.FontFamily.HELVETICA, 12, Font.ITALIC));
                                pdf.add(no_dislike);
                            }

                            //Creation espace vide
                            emptySpace = new Paragraph("");
                            emptySpace.setSpacingBefore(5);
                            emptySpace.setSpacingAfter(5);
                            pdf.add(emptySpace);
                        }
                        else{
                            Paragraph no_dislike = new Paragraph("Aucun commentaire liké et disliké",new
                                    Font(Font.FontFamily.HELVETICA, 12, Font.ITALIC));
                            pdf.add(no_dislike);
                        }
                    }
                }
                pdf.close();
            }
            return pdfDest_file;

        } catch (DocumentException | IOException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(String.format("Error : %s", e.getMessage()), e);
            }
        }
        return "";
    }
    private void sendMail(String path,List<String> user_mails) {
        for(String user : user_mails){
            System.out.println(user);
        }
        MailData mailStatistics = new MailData();
        mailStatistics.getInfos().setSubject("Statistics");
        mailStatistics.getInfos().setBody(
                new StringBuilder()
                        .append("<html><body>")
                        .append("Bonjour,<br>")
                        .append("Veuillez trouver ci-joint les statistiques.<br>")
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

        mailStatistics.getInfos().bccProperty().addAll(user_mails);
        try {
            BodyPart messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(path);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(path.replace("/", "\\").substring(path.lastIndexOf("/")));
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

}

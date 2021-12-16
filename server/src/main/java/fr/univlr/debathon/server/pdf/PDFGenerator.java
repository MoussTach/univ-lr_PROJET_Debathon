
package fr.univlr.debathon.server.pdf;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.jfree.chart.JFreeChart;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;

public class PDFGenerator {
    public static Connection c;
    public static PDFGenerator instance;
    public PDFGenerator(){}

    public static PDFGenerator getInstance(){
        if (instance == null) {
            instance = new PDFGenerator();
        }
        return instance;
    }

    public Document requestPDF(int id_debat){
        List<PDFquestion> questions = PDFdata.getRequest1(id_debat);
        return getPDF(questions,id_debat);
    }

    //Methode pour creer un PDF pour un debat avec la liste des questions
    public Document getPDF(List<PDFquestion> questions,int id_debat){
        //Creation document pdf
        Document pdf = new Document(PageSize.A4);
        //Creation list de chart avec les questions
        List<JFreeChart> charts = ChartGenerator.getInstance().genAllPieCharts(questions);

        try {
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
                System.out.println(pdfDest);
                System.out.println(java.time.LocalDate.now());
                //Check if the folder exists and create one if not
                File destination_directory = new File(pdfDest);
                boolean destination_directory_exist = destination_directory.exists();
                if (!destination_directory_exist) {
                    destination_directory.mkdir();
                }

                final String namePdfFile = String.format("%s -- %d.pdf", java.time.LocalDate.now().toString(), id_debat);
                final String pdfDest_file = String.format(
                        "%s/%s",
                        pdfDest,
                        namePdfFile
                );
                FileOutputStream fos = new FileOutputStream(pdfDest_file);
                //Writer pour le pdf
                PdfWriter writer = PdfWriter.getInstance(pdf,fos);
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
                    Image image = Image.getInstance(writer,buffer,1.0f);
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
            } catch (IOException e) {
                e.printStackTrace();
            }




        } catch (DocumentException e) {
            e.printStackTrace();
        }

        pdf.close();
        return pdf;
    }


}
